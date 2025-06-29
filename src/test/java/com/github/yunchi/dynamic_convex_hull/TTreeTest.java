package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class TTreeTest {

	TTree<Integer> tt;
	
	Random rand;
	
	@Before
	public void setUp() throws Exception {
		tt = new TTree<Integer>();
		rand = new Random(100);
	}

	@Test
	public final void testGetSize() {
		assertEquals(0, tt.getSize());
		tt.insert(5);
		tt.insert(7);
		tt.insert(3);
		assertEquals(3, tt.getSize());
		tt.delete(4);
		assertEquals(3, tt.getSize());
		tt.insert(9);
		tt.insert(8);
		tt.insert(4);
		assertEquals(6, tt.getSize());
		tt.delete(5);
		assertEquals(5, tt.getSize());
		tt.delete(4);
		assertEquals(4, tt.getSize());
		tt.delete(3);
		assertEquals(3, tt.getSize());
		tt.insert(3);
		assertEquals(4, tt.getSize());
	}

	@Test
	public final void testSearch() {
		List<Integer> list = new ArrayList<Integer>(100);
		List<Integer> orderedList = new ArrayList<Integer>(100);
		for (int i = 0; i < 100; i+=2) {
			list.add(i+1);
			orderedList.add(i+1);
		}
		java.util.Collections.shuffle(list);
		for (Integer i : list) {
			tt.insert(i);
		}
		List<Integer> ttList = tt.inorderElements();
		assertEquals(orderedList, ttList);
		Integer i1 = Integer.valueOf(5);
		assertEquals(i1, tt.search(5));
		i1 = Integer.valueOf(7);
		assertEquals(i1, tt.search(7));
		i1 = Integer.valueOf(99);
		assertEquals(i1, tt.search(99));
		assertEquals(null, tt.search(6));
		assertEquals(null, tt.search(200));
		assertEquals(null, tt.search(-1));
		assertEquals(null, tt.search(1000));
	}

	@Test
	public final void testInsert() {
		List<Integer> list = new ArrayList<Integer>(100);
		List<Integer> orderedList = new ArrayList<Integer>(100);
		for (int i = 0; i < 100; i+=2) {
			list.add(i+1);
			orderedList.add(i+1);
		}
		java.util.Collections.shuffle(list);
		for (Integer i : list) {
			tt.insert(i);
		}
		List<Integer> ttList = tt.inorderElements();
		assertEquals(orderedList, ttList);
		
		TTree<Coordinate2D> t2 = new TTree<Coordinate2D>();
		List<Coordinate2D> l = new ArrayList<Coordinate2D>();
		Coordinate2D d;
		for (int i = 0; i < 1000; i++) {
			System.out.println(i);
			d = new Coordinate2D(0.0 + rand.nextDouble(), 1.0*rand.nextDouble());
			l.add(d);
			t2.insert(d);
		}
		java.util.Collections.sort(l);
		List<Coordinate2D> t2List = t2.inorderElements();
		assertEquals(l, t2List);
	}

	@Test
	public final void testDelete() {
		List<Integer> list = new ArrayList<Integer>(100);
		List<Integer> orderedList = new ArrayList<Integer>(100);
		for (int i = 0; i < 100; i++) {
			list.add(i+1);
			if (i % 2 == 0) {
				orderedList.add(i+1);
			}
		}
		java.util.Collections.shuffle(list);
		for (Integer i : list) {
			tt.insert(i);
		}

		for (int i = 0; i <= 100; i+=2) {
			tt.delete(i);
		}
		
		List<Integer> ttList = tt.inorderElements();
		assertEquals(orderedList, ttList);
	}

}
