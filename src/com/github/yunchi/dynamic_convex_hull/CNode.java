package com.github.yunchi.dynamic_convex_hull;

public class CNode extends Node<Coordinate2D> {

	/**
	 * @author Yun Chi
	 * @since 2012-10-13
	 */
	
	protected CNode left, right, lMax;
	protected SubHull hull;
	
	public CNode (Coordinate2D c) {
		//new leaf node
		this.isLeaf = true;
		this.color = BLACK;
		this.ex = c;
		this.lMax = this;

		hull = new SubHull(c);
	}

	public CNode (CNode lMax, CNode left, CNode right) {
		//new internal node
		this.isLeaf = false;//internal node
		this.color = RED;
		this.lMax = lMax;
		this.left = left;
		this.right = right;
		
		hull = SubHull.bridge(left.hull, right.hull);
	}

	public CNode(Coordinate2D c, CNode leftLeaf, CNode rightLeaf) {
		//new leaf node
		//only use this constructor is the leafs are to be chained
		assert (leftLeaf == null || leftLeaf.isLeaf) 
			&& (rightLeaf == null || rightLeaf.isLeaf) :
				"leftLeaf and rightLeaf must be leafs";
		
		this.isLeaf = true;
		this.color = BLACK;
		this.ex = c;
		this.lMax = this;
		this.left = leftLeaf;
		this.right = rightLeaf;

		hull = new SubHull(c);
	}

}
