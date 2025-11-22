package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import org.junit.Test;

public class Coordinate2DTest {

	@Test
	public void testConstructorsAndGetters() {
		Coordinate2D c1 = new Coordinate2D(1.5, 2.5);
		assertEquals(1.5, c1.getX(), 0.000001);
		assertEquals(2.5, c1.getY(), 0.000001);

		Coordinate2D c2 = new Coordinate2D(c1);
		assertEquals(1.5, c2.getX(), 0.000001);
		assertEquals(2.5, c2.getY(), 0.000001);
	}

	@Test
	public void testCompareTo() {
		Coordinate2D c1 = new Coordinate2D(1.0, 2.0);
		Coordinate2D c2 = new Coordinate2D(2.0, 2.0);
		Coordinate2D c3 = new Coordinate2D(1.0, 3.0);
		Coordinate2D c4 = new Coordinate2D(0.0, 2.0);

		assertTrue(c1.compareTo(c2) < 0);
		assertTrue(c2.compareTo(c1) > 0);
		assertTrue(c1.compareTo(c3) < 0); // Now compares y as well
		assertTrue(c1.compareTo(c4) > 0);
		assertTrue(c1.compareTo(c1) == 0);
	}

}
