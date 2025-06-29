package com.github.yunchi.dynamic_convex_hull;

public class Node<E extends Comparable<E>> {

	/**
	 * @author Yun Chi
	 * @since 2012-10-10 
	 */

	final protected static boolean RED = false;
	final protected static boolean BLACK = true;	

	protected E ex;
	protected Node<E> left, right, lMax;
	protected boolean color;
	protected boolean isLeaf;

	public Node() {
		
	}
	
	public Node(E e) {
		//new leaf node
		this.isLeaf = true;
		this.color = BLACK;
		this.ex = e;
		this.lMax = this;
	}

	public Node(Node<E> lMax, Node<E> left, Node<E> right) {
		//new internal node
		this.isLeaf = false;//internal node
		this.color = RED;
		this.lMax = lMax;
		this.left = left;
		this.right = right;
	}

	public Node(E e, Node<E> leftLeaf, Node<E> rightLeaf) {
		//new leaf node
		//only use this constructor is the leafs are to be chained
		assert (leftLeaf == null || leftLeaf.isLeaf) 
			&& (rightLeaf == null || rightLeaf.isLeaf) :
				"leftLeaf and rightLeaf must be leafs";
		
		this.isLeaf = true;
		this.color = BLACK;
		this.ex = e;
		this.lMax = this;
		this.left = leftLeaf;
		this.right = rightLeaf;
	}

}
