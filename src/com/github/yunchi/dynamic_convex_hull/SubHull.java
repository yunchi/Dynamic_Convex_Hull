package com.github.yunchi.dynamic_convex_hull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubHull extends CQueue<Coordinate2D> {

	/**
	 * @author Yun Chi
	 * @since 2012-10-12 
	 */

	public SubHull () {
		super();
	}

	public SubHull (Coordinate2D c) {
		super(c);
	}
	
	public SubHull (CQueue<Coordinate2D> q) {
		super(q.root, q.height, q.minNode, q.maxNode);
	}
	
	public SubHull (List<Coordinate2D> pointList) {
		super();
		Collections.sort(pointList);
		ArrayList<Coordinate2D> hull = new ArrayList<Coordinate2D>();
		for (Coordinate2D c : pointList) {
			while (hull.size() > 1) {
				Coordinate2D b = hull.get(hull.size() - 1);
				Coordinate2D a = hull.get(hull.size() - 2);
				if (computeSlope(a,b) > computeSlope(b,c)) {
					break;
				}
				else {
					hull.remove(hull.size() - 1);
				}
			}
			hull.add(c);
		}
		
		for (Coordinate2D p : hull) {
			this.insert(p);
		}
	}
	
	public static SubHull bridge (SubHull lHull, SubHull rHull) {
		assert lHull != null && rHull != null : "lHull != null && rHull != null";
		assert lHull.root != null && rHull.root != null : "lHull.root != null && rHull.root != null";
	
		//locate the appropriate pointers on both hulls
		Node<Coordinate2D> lItr = lHull.root;
		Node<Coordinate2D> rItr = rHull.root;
		
		boolean done = false;
		double middleX = (lHull.maxNode.ex.x + rHull.minNode.ex.x)/2.0;
		
		while ( !done ) {
			double t = computeSlope(lItr.lMax, rItr.lMax);
			int iL = determineCase(lItr.lMax, t);
			int iR = determineCase(rItr.lMax, t);
		
			switch (iL) {
			case -1:
				switch (iR) {
				case -1:
					rItr = rItr.right;
					break;
				case 0:
					lItr = lItr.right;
					if (!rItr.isLeaf && rItr.right != null) {
						rItr = rItr.right;
					}
					break;
				case +1: //the most difficult one
					double lHeight = lItr.lMax.ex.y + 
						computeSlope(lItr.lMax, lItr.lMax.right)*(middleX - lItr.lMax.ex.x);
					double rHeight = rItr.lMax.ex.y + 
						computeSlope(rItr.lMax.left, rItr.lMax)*(middleX - rItr.lMax.ex.x);
					if ( lHeight <= rHeight) {
						rItr = rItr.left;
					}
					else {
						lItr = lItr.right;
					}
					break;
				}
				break;
			case 0:
				switch (iR) {
				case -1:
					if (!lItr.isLeaf && lItr.left != null) {
						lItr = lItr.left;
					}
					rItr = rItr.right;
					break;
				case 0://done!
					lItr = lItr.lMax;
					rItr = rItr.lMax;
					done = true;
					break;
				case +1:
					if (!lItr.isLeaf && lItr.left != null) {
						lItr = lItr.left;
					}
					rItr = rItr.left;
					break;
				}
				break;
			case +1:
				switch (iR) {
				case -1:
					lItr = lItr.left;
					rItr = rItr.right;
					break;
				case 0:
					lItr = lItr.left;
					if (!rItr.isLeaf && rItr.right != null) {
						rItr = rItr.right;
					}
					break;
				case +1:
					lItr = lItr.left;
					break;
				}
				break;
			}
		}
		
		assert sanityCheck(lHull, rHull, lItr, rItr) : "wrong cut!";
		
		assert lItr != null && rItr != null : "otherwise, something is wrong";
		
		return new SubHull(concatenate(lHull.split(lItr.ex, LEFT, true), rHull.split(rItr.ex, RIGHT, false)));
	}
	
	public Coordinate2D locateAtSlope (double t) {
		assert this.root != null : "this.root != null";
		
		Node<Coordinate2D> itr = root;
		while (itr != null) {
			int i = determineCase(itr.lMax, t);
			if (i*t < 0) {//note: t positive angle vs. negative angle, the behaviors are different
				itr = itr.right;
			}
			else if (i*t > 0) {
				itr = itr.left;
			}
			else {
				itr = itr.lMax;
				break;
			}
		}
		
		assert itr != null : "something is wrong if itr == null";
		
		return itr.ex;
	}

	public boolean isValidHull ( ) {
		if (root == null) {
			return true;
		}
		Node<Coordinate2D> n = minNode;
		if (n.right == null) {
			return true; //a one-node hull is valid
		}
		
		n = n.right;
		while (n.right != null) {
			if (computeSlope(n.left, n) < computeSlope(n, n.right)) {
				return false;
			}
			n = n.right;			
		}
		
		return true;
	}
	
	public static boolean sameHull (SubHull h1, SubHull h2) {
		assert h1 != null && h2 != null : "h1 != null && h2 != null";

		Node<Coordinate2D> n1 = h1.minNode;
		Node<Coordinate2D> n2 = h2.minNode;
		
		boolean result = true;
		
		while (true) {
			if (n1.ex.compareTo(n2.ex) != 0) {
				result = false;
				break;
			}
			if (n1.right == null || n2.right == null) {
				if (n1.right != null || n2.right != null) {
					result = false;
				}
				break;
			}
			n1 = n1.right;
			n2 = n2.right;
		}
		
		return result;
	}
	
	public void printHull ( ) {
		if (root == null) {
			return;
		}
		
		Node<Coordinate2D> n = minNode;
		while (n != null) {
			System.out.println("(" + n.ex.x + "," + n.ex.y + ")");
			n = n.right;
		}
	}
	
	protected static double computeSlope (Node<Coordinate2D> leftN, Node<Coordinate2D> rightN) {
		assert leftN != null && rightN != null : "leftN != null && rightN != null";
		assert leftN.isLeaf && rightN.isLeaf : "leftN.isLeaf && rightN.isLeaf";
		assert (rightN.ex.x - leftN.ex.x)	 > 0 : "angle is defined form left to right";
		
		return (rightN.ex.y - leftN.ex.y)/(rightN.ex.x - leftN.ex.x);
	}
	
	protected static double computeSlope (Coordinate2D e1, Coordinate2D e2) {
		assert e1 != null && e2 != null : "e1 != null && e2 != null";
		assert (e2.x - e1.x)	 > 0 : "angle is defined form left to right";
		
		return (e2.y - e1.y)/(e2.x - e1.x);
	}
	
	protected static int determineCase (Node<Coordinate2D> n, double t) {
		assert n != null : "n != null";
		assert n.isLeaf : "n.isLeaf";

		boolean leftAbove = true;//fake left corner (at y=-infty)
		boolean rightAbove = false;//fake right corner (at y=-infty)
		
		if ( (n.left != null) && computeSlope(n.left, n) < t  ) {
			leftAbove = false;
		}
		
		if ( (n.right != null) && computeSlope(n, n.right) > t  ) {
			rightAbove = true;
		}
		
		if (leftAbove && rightAbove) {
			return -1;
		}
		else if (!leftAbove && !rightAbove) {
			return +1;
		}
		else {
			assert leftAbove && !rightAbove : "concave";
			
			//corner case for single-node-SubHull also comes here
			return 0;
		}
	}
	
	private static boolean sanityCheck (SubHull lHull, SubHull rHull, Node<Coordinate2D> lPtr, Node<Coordinate2D> rPtr) {
		
		double t = computeSlope(lPtr, rPtr);
		if (lPtr.left != null && computeSlope(lPtr.left, lPtr) < t) {
			return false;
		}
		if (lPtr.right != null && computeSlope(lPtr, lPtr.right) > t) {
			return false;
		}
		if (rPtr.left != null && computeSlope(rPtr.left, rPtr) < t) {
			return false;
		}
		if (rPtr.right != null && computeSlope(rPtr, rPtr.right) > t) {
			return false;
		}
		
		return true;
	}	

}
