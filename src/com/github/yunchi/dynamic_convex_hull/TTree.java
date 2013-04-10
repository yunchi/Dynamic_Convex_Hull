package com.github.yunchi.dynamic_convex_hull;

import java.util.LinkedList;
import java.util.List;

public class TTree<E extends Comparable<E>> implements Tree<E> {

	/**
	 * @author Yun Chi
	 * @since 2012-10-10 
	 */

	final protected static boolean RED = false;
	final protected static boolean BLACK = true;
	final protected static boolean LEFT = false;
	final protected static boolean RIGHT = true;

	protected Node<E> root;
	protected int size;
	
	public TTree () {
		this.root = null;
		this.size = 0;
	}
	
	public int getSize() {
		return size;
	}
	
	protected static <E extends Comparable<E>>  Node<E> rotateLeft (Node<E> n) {
		assert n != null : "n != null";
		assert !n.isLeaf && !n.right.isLeaf && n.right.color == RED : 
		"rotateLeft a BLACK right node will break the tree balance";
		
		Node<E> tempNode = n.right;
		boolean tempColor = n.color;
		n.right = tempNode.left;
		n.color = tempNode.color;
		tempNode.left = n;
		tempNode.color = tempColor;
		return tempNode;
	}
	
	protected static <E extends Comparable<E>> Node<E> rotateRight (Node<E> n) {
		assert n != null : "n != null";
		assert !n.isLeaf && !n.left.isLeaf && n.left.color == RED :
		"rotateRight a BLACK left node will break the tree balance";
		
		Node<E> tempNode = n.left;
		boolean tempColor = n.color;
		n.left = tempNode.right;
		n.color = tempNode.color;
		tempNode.right = n;
		tempNode.color = tempColor;
		return tempNode;
	}

	protected static <E extends Comparable<E>> void flipTripleColor (Node<E> n) {
		assert n != null : "n != null";
		assert !n.isLeaf && !n.left.isLeaf && !n.right.isLeaf : "!n.isLeaf && !n.left.isLeaf && !n.right.isLeaf";
		
		n.color = !n.color;
		n.left.color = !n.left.color;
		n.right.color = !n.right.color;
	}
	
	protected static <E extends Comparable<E>> Node<E> fixUp (Node<E> n) {
		assert n != null : "n != null";
		
		if (n.isLeaf) {
			return n;
		}
		
		if (n.left.color == BLACK && n.right.color == RED) {
			n = rotateLeft(n);
		}
		else { 
			if (n.left.color == RED && n.left.left.color == RED) {
				assert n.right.color == BLACK : "n.right.color == BLACK";
				
				n = rotateRight(n);
			}
		
			if (n.left.color == RED && n.right.color == RED) {
				flipTripleColor(n);
			}
		}
		return n;
	}

	protected Node<E> addLeaf (E e, Node<E> leftLeaf, Node<E> rightLeaf) {
		assert e != null : "e != null";
		assert (leftLeaf == null || leftLeaf.isLeaf) 
		&& (rightLeaf == null || rightLeaf.isLeaf) :
			"leftLeaf and rightLeaf must be leafs";
		
		Node<E> n = new Node<E>(e);//for TTree, ignore left and right neighbors of this new leaf
		size++;
		return n;
	}
	
	protected void removeLeaf (Node<E> n) {
		assert n != null && n.isLeaf : "n != null && n.isLeaf";
		
		//for TTree, do nothing to left and right siblings	
		size--;
		
		assert size >= 0 : "size >= 0";
	}
	
	protected Node<E> insertAt (Node<E> n, E e) {
		assert n != null : "n != null";
		
		if (e.compareTo(n.lMax.ex) <= 0) {
			if (n.isLeaf) {
				if (e.compareTo(n.ex) == 0) {//corner case, replace old value by the new one
					n.ex = e;
				}
				else {
					Node<E> nNew = addLeaf(e, n.left, n);
					n = new Node<E>(nNew, nNew, n);
				}
			}
			else {
				n.left = insertAt(n.left, e);
			}
		}
		else {
			if (n.isLeaf) {
				Node<E> nNew = addLeaf(e, n, n.right);
				n = new Node<E>(n, n, nNew);
			}
			else {
				n.right = insertAt(n.right, e);
			}
		}
		
		n = fixUp(n);
		
		return n;
	}
	
	protected Node<E> deleteAt (Node<E> n, E e) {
		assert n != null : "n != null";
		assert !n.isLeaf : "!n.isLeaf";
		assert n.color == RED || n.left.color == RED : "n.color == RED || n.left.color == RED";
		
		if (e.compareTo(n.lMax.ex) <= 0) {//to the left subtree
			if (n.left.isLeaf) {
				if (e.compareTo(n.left.ex) != 0) {//didn'd find e
					return n;
				}
				else {//base case
					assert n.color == RED : "n.color == RED";
					assert n.right.isLeaf : "n.right.isLeaf";
				
					removeLeaf(n.left);
					return n.right;
				}
			}
			
			if (e.compareTo(n.lMax.ex) == 0) {
				//need to update this lMax
				//happens only once per delete
				assert !n.left.isLeaf : "!n.left.isLeaf";
				
				Node<E> tempNode = n.left;
				while (!tempNode.right.isLeaf) {
					tempNode = tempNode.right;
				}
				n.lMax = tempNode.lMax;
			}
			
			if (n.left.color == RED || n.left.left.color == RED) {//safe to go down
				n.left = deleteAt(n.left,e);
			}
			else {//need help
				assert !n.right.isLeaf : "!n.right.isLeaf";
				assert n.color == RED : "n.color == RED";
				
				flipTripleColor(n);
				n.left = deleteAt(n.left,e);
				if (n.left.color == RED) {//didn't use the help
					flipTripleColor(n);
				}
				else if (n.right.left.color == BLACK) {
					n = rotateLeft(n);
				}
				else {
					n.right = rotateRight(n.right);
					n = rotateLeft(n);
					flipTripleColor(n);
				}
			}
		}
		else {//to the right subtree
			if (n.right.isLeaf) {
				if (e.compareTo(n.right.ex) != 0) {//didn't find e
					return n;
				}
				else {//base case
					removeLeaf(n.right);
					n.left.color = BLACK;
					//note: n.left is either a RED internal node, in which case n is BLACK, or n.left is a BLACK leaf node
					return n.left;
				}
			}
			else if (n.right.left.color == RED) {//safe to go down
				n.right = deleteAt(n.right,e);
			}
			else if (n.color == RED) {//need help
				assert n.left.color == BLACK : "n.left.color == BLACK";
			
				flipTripleColor(n);
				n.right = deleteAt(n.right,e);
				if (n.right.color == RED) {//didn't use the help
					flipTripleColor(n);
				}
				else if (n.left.left.color == RED) {
					n = rotateRight(n);
					flipTripleColor(n);
				}
			}
			else {
				assert n.left.color == RED : "n.left.color == RED";
				
				n = rotateRight(n);
				n.right = deleteAt(n.right,e);
				if (n.right.color == RED) {//didn't use the help
					n = rotateLeft(n);
				}
			}
		}
		
		return n;
	}

	protected static <E extends Comparable<E>> int inorderPrintLeaf(Node<E> n, int level) {
		if (n == null) {
			return 0;
		}
		if (n.isLeaf) {
			System.out.println("level " + level + "\t" + n.ex.toString());
			return 1;
		}
		else {
			return inorderPrintLeaf(n.left, level+1) + inorderPrintLeaf(n.right, level+1);
		}
	}
	
	public void inorderPrintLeaf( ) {
		int checksum = inorderPrintLeaf(root, 0);
		System.out.println("total leafs:\t" + checksum);
	}
	
	public List<E> inorderElements( ) {
		List<E> result = new LinkedList<E>();
		if (root != null) {
			inorderEnumerateElements(root, result);
		}
		return result;
	}
	
	protected static <E extends Comparable<E>> void inorderEnumerateElements(Node<E> n, List<E> result) {
		if (n.isLeaf	) {
			result.add(n.ex);
		}
		else {
			inorderEnumerateElements(n.left, result);
			inorderEnumerateElements(n.right, result);
		}
	}
	
	public E search(E e) {
		if (root == null) {
			return null;
		}
		
		Node<E> n = root;
		while (!n.isLeaf) {
			if (e.compareTo(n.lMax.ex) == 0) {
				return n.lMax.ex;
			}
			else if (e.compareTo(n.lMax.ex) < 0	) {
				n = n.left;
			}
			else {
				n = n.right;
			}
		}
	
		if (e.compareTo(n.ex) != 0) {
			return null;
		}
		else {
			return n.ex;
		}
	}

	public void insert (E e) {
		if (root == null) {//empty tree
			root = new Node<E>(e);
			size = 1;
		}
		else {
			root = insertAt(root,e);
			if (root.color == RED) {
				root.color = BLACK;
			}
		}
	}
	
	public void delete (E e) {
	
		if (root == null) {
			return;
		}
		else if (root.isLeaf) {
			if (e.compareTo(root.ex) == 0) {
				root = null;
				size = 0;
			}
			return;
		}
		else {
			if (root.left.color == BLACK && root.right.color == BLACK) {//may need help
				root.color = RED;
			}
			root = deleteAt(root, e);
			if (root.color == RED) {//didn't use the help
				root.color = BLACK;
			}
			return;
		}
	}

}
