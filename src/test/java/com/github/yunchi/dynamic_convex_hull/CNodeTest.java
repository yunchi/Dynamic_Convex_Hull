package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import org.junit.Test;

public class CNodeTest {

	@Test
	public void testLeafConstructor() {
		Coordinate2D c = new Coordinate2D(1.0, 2.0);
		CNode node = new CNode(c);
		
		assertTrue(node.isLeaf);
		assertEquals(Node.BLACK, node.color);
		assertEquals(c, node.ex);
		assertEquals(node, node.lMax);
		assertNotNull(node.hull);
		// Verify hull contains just the point
		assertEquals(1, node.hull.getSize());
	}
	
	@Test
	public void testInternalNodeConstructor() {
		CNode left = new CNode(new Coordinate2D(0.0, 0.0));
		CNode right = new CNode(new Coordinate2D(2.0, 2.0));
		CNode lMax = left;
		
		CNode node = new CNode(lMax, left, right);
		
		assertFalse(node.isLeaf);
		assertEquals(Node.RED, node.color);
		assertEquals(lMax, node.lMax);
		assertEquals(left, node.left);
		assertEquals(right, node.right);
		assertNotNull(node.hull);
		// Hull should be bridged
		assertTrue(node.hull.getSize() > 0);
	}
	
	@Test
	public void testChainedLeafConstructor() {
		CNode leftLeaf = new CNode(new Coordinate2D(0.0, 0.0));
		CNode rightLeaf = new CNode(new Coordinate2D(2.0, 2.0));
		Coordinate2D c = new Coordinate2D(1.0, 1.0);
		
		CNode node = new CNode(c, leftLeaf, rightLeaf);
		
		assertTrue(node.isLeaf);
		assertEquals(Node.BLACK, node.color);
		assertEquals(c, node.ex);
		assertEquals(node, node.lMax);
		assertEquals(leftLeaf, node.left);
		assertEquals(rightLeaf, node.right);
		assertNotNull(node.hull);
		assertEquals(1, node.hull.getSize());
	}

}
