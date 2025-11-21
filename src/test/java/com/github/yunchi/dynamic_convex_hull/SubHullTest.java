package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class SubHullTest {

	Random rand;
	
	@Before
	public void setUp() throws Exception {
		rand = new Random(100);
	}

	@Test
	public final void testBridge() {
		// Create two separate hulls and bridge them
		List<Coordinate2D> l1 = new ArrayList<Coordinate2D>();
		l1.add(new Coordinate2D(0, 0));
		l1.add(new Coordinate2D(1, 1));
		l1.add(new Coordinate2D(2, 0));
		SubHull h1 = new SubHull(l1);
		
		List<Coordinate2D> l2 = new ArrayList<Coordinate2D>();
		l2.add(new Coordinate2D(3, 0));
		l2.add(new Coordinate2D(4, 1));
		l2.add(new Coordinate2D(5, 0));
		SubHull h2 = new SubHull(l2);
		
		SubHull bridged = SubHull.bridge(h1, h2);
		assertTrue(bridged.isValidHull());
		
		// Verify that the bridged hull contains points from both
		// The bridge should connect (1,1) and (4,1) roughly, or similar upper tangent
		// Actually, for these points: (0,0), (1,1), (2,0) and (3,0), (4,1), (5,0)
		// The upper hull should be (0,0)->(1,1)->(4,1)->(5,0)
		// (2,0) and (3,0) should be inside or below. 
		// Wait, SubHull seems to store the upper hull? 
		// Let's check SubHull constructor.
		// "if (computeSlope(a,b) > computeSlope(b,c)) { break; } else { hull.remove(hull.size() - 1); }"
		// This logic keeps points that make a "right turn" or "concave" shape?
		// If a->b slope > b->c slope.
		// Example: (0,0)->(1,1) slope 1. (1,1)->(2,0) slope -1. 1 > -1. Break.
		// So it keeps (1,1).
		// It seems to compute the UPPER hull.
		
		// Let's verify the bridged hull has the expected points.
		// The points should be sorted by x.
		// (0,0), (1,1), (4,1), (5,0) are expected on the upper hull.
		// (2,0) and (3,0) are below the segment (1,1)-(4,1).
		
		// Let's check if (2,0) is in the bridged hull.
		// search returns the exact object if found.
		// But SubHull extends CQueue, and CQueue.search searches by key (Coordinate2D).
		// Coordinate2D compares by x only?
		// "public int compareTo(Coordinate2D other) {//shallow comparison ... if (this.x < other.x) ..."
		// Yes, compares by x.
		// So search(new Coordinate2D(2,0)) will find the node with x=2 if it exists.
		
		// If (2,0) is removed from the hull, searching for x=2 might return null or the node if it was kept?
		// Wait, SubHull constructor filters points.
		// SubHull.bridge returns a NEW SubHull which is a concatenation of parts of h1 and h2.
		// It splits h1 and h2.
		// So the internal nodes for (2,0) might be gone from the new tree.
		
		assertNotNull(bridged.search(new Coordinate2D(0, 0)));
		assertNotNull(bridged.search(new Coordinate2D(1, 1)));
		assertNotNull(bridged.search(new Coordinate2D(4, 1)));
		assertNotNull(bridged.search(new Coordinate2D(5, 0)));
		
		// (2,0) should NOT be in the upper hull because it's below (1,1)-(4,1)
		// But wait, search(Coordinate2D) finds by X.
		// If the node with x=2 is removed, search should return null.
		assertNull(bridged.search(new Coordinate2D(2, 0)));
		assertNull(bridged.search(new Coordinate2D(3, 0)));
	}

	@Test
	public final void testLocateAtSlope() {
		List<Coordinate2D> l = new ArrayList<Coordinate2D>();
		l.add(new Coordinate2D(0, 0));
		l.add(new Coordinate2D(1, 1));
		l.add(new Coordinate2D(2, 0));
		SubHull h = new SubHull(l);
		
		// Slopes: (0,0)->(1,1) is 1. (1,1)->(2,0) is -1.
		// locateAtSlope(t) should find the point where the tangent has slope t.
		// If t > 1, it should be (0,0).
		// If -1 < t < 1, it should be (1,1).
		// If t < -1, it should be (2,0).
		
		Coordinate2D p;
		
		p = h.locateAtSlope(2.0);
		assertEquals(0.0, p.getX(), 0.000001);
		
		p = h.locateAtSlope(0.0);
		assertEquals(1.0, p.getX(), 0.000001);
		
		p = h.locateAtSlope(-2.0);
		assertEquals(2.0, p.getX(), 0.000001);
	}

	@Test
	public final void testIsValidHull() {
		List<Coordinate2D> l1 = new ArrayList<Coordinate2D>();
		List<Coordinate2D> l2 = new ArrayList<Coordinate2D>();
		List<Coordinate2D> l3 = new ArrayList<Coordinate2D>();
		List<Coordinate2D> l4 = new ArrayList<Coordinate2D>();
		List<Coordinate2D> l = new ArrayList<Coordinate2D>();
		Coordinate2D d;
		for (int i = 0; i < 1000; i++) {
			d = new Coordinate2D(0.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			l1.add(d);
			l.add(d);
			d = new Coordinate2D(1.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			l2.add(d);
			l.add(d);
			d = new Coordinate2D(2.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			l3.add(d);
			l.add(d);
			d = new Coordinate2D(3.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			l4.add(d);
			l.add(d);
		}
		SubHull h1 = new SubHull(l1);
		assertTrue(h1.isValidHull());
		SubHull h2 = new SubHull(l2);
		assertTrue(h2.isValidHull());
		SubHull h3 = new SubHull(l3);
		assertTrue(h3.isValidHull());
		SubHull h4 = new SubHull(l4);
		assertTrue(h4.isValidHull());
	
		
		/*
		SubHull h = new SubHull(l3_4);
		SubHull b = SubHull.bridge(h3, h4);
		assertTrue(SubHull.sameHull(h, b));
		b = SubHull.bridge(h2, b);
		b = SubHull.bridge(h1, b);
		h = new SubHull(l);
		assertTrue(SubHull.sameHull(h, b));
		*/
		
		SubHull h = new SubHull(l);
		SubHull b = h3;
		b = SubHull.bridge(h2, b);
		b = SubHull.bridge(b, h4);
		b = SubHull.bridge(h1, b);
		assertTrue(SubHull.sameHull(h, b));
		
	}

}
