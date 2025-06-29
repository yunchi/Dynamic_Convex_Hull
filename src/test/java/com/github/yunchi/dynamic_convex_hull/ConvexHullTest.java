package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;


public class ConvexHullTest {

	ConvexHull con;
	Random rand;
	
	@Before
	public void setUp() throws Exception {
		con = new ConvexHull();
		rand = new Random(100);
	}

	@Test
	public void testInsertAtCNodeCoordinate2D() {
		
		List<Coordinate2D> l = new ArrayList<Coordinate2D>();
		Coordinate2D d;
		for (int i = 0; i < 1000; i++) {
			d = new Coordinate2D(0.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			//System.out.println(i + ": " + d.x + "\t" + d.y);
			l.add(d);
			con.insert(d);
		}
		assertTrue(con.root.hull.isValidHull());
		SubHull baseline = new SubHull(l);
		assertTrue(SubHull.sameHull(baseline, con.root.hull));
		
		for (int i = 0; i < 1000; i++) {
			System.out.println(i);
			
			int j = rand.nextInt(l.size());
			con.delete(l.get(j));
			l.remove(j);
			baseline = new SubHull(l);
			assertTrue(SubHull.sameHull(baseline, con.root.hull));

			d = new Coordinate2D(0.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			con.insert(d);
			l.add(d);
			baseline = new SubHull(l);
			assertTrue(SubHull.sameHull(baseline, con.root.hull));
			
		}
	}

	//@Test
	public void testDeleteAtCNodeCoordinate2D() {
		fail("Not yet implemented");
	}

}
