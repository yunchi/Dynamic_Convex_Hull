package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import org.junit.Test;

public class NodeTest {

	@Test
	public void testLeafConstructor() {
		Integer val = 5;
		Node<Integer> node = new Node<Integer>(val);
		
		assertTrue(node.isLeaf);
		assertEquals(Node.BLACK, node.color);
		assertEquals(val, node.ex);
		assertEquals(node, node.lMax);
		assertNull(node.left);
		assertNull(node.right);
	}
	
	@Test
	public void testInternalNodeConstructor() {
		Node<Integer> left = new Node<Integer>(3);
		Node<Integer> right = new Node<Integer>(7);
		Node<Integer> lMax = left;
		
		Node<Integer> node = new Node<Integer>(lMax, left, right);
		
		assertFalse(node.isLeaf);
		assertEquals(Node.RED, node.color);
		assertEquals(lMax, node.lMax);
		assertEquals(left, node.left);
		assertEquals(right, node.right);
	}
	
	@Test
	public void testChainedLeafConstructor() {
		Node<Integer> leftLeaf = new Node<Integer>(3);
		Node<Integer> rightLeaf = new Node<Integer>(7);
		Integer val = 5;
		
		Node<Integer> node = new Node<Integer>(val, leftLeaf, rightLeaf);
		
		assertTrue(node.isLeaf);
		assertEquals(Node.BLACK, node.color);
		assertEquals(val, node.ex);
		assertEquals(node, node.lMax);
		assertEquals(leftLeaf, node.left);
		assertEquals(rightLeaf, node.right);
	}
	
	@Test(expected = AssertionError.class)
	public void testChainedLeafConstructorAssertion() {
		// This should fail assertion because leftLeaf must be a leaf
		Node<Integer> left = new Node<Integer>(new Node<Integer>(1), new Node<Integer>(1), new Node<Integer>(2)); // Internal node
		new Node<Integer>(5, left, null);
	}

}
