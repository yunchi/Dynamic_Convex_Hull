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

	//@Test
	public final void testBridge() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testLocateAtSlope() {
		fail("Not yet implemented");
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
