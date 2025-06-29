package com.github.yunchi.dynamic_convex_hull;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

public class CQueueTest {

	CQueue<Integer> tt;
	
	@Before
	public void setUp() throws Exception {
		tt = new CQueue<Integer>();
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

	@Test
	public final void testConcatenate() {
		List<Integer> list = new ArrayList<Integer>(100);
		Set<Integer> sc = new TreeSet<Integer>();
		for (int i = 0; i < 100; i+=2) {
			list.add(i+1);
			sc.add(i+1);
		}
		
		java.util.Collections.shuffle(list);
		for (Integer i : list) {
			tt.insert(i);
		}

		tt.insert(6);
		sc.add(6);
		tt.insert(30);
		sc.add(30);
		tt.delete(69);
		sc.remove(69);
		
		CQueue<Integer> Q20 = new CQueue<Integer>();
		CQueue<Integer> Q40 = new CQueue<Integer>();
		CQueue<Integer> Q46 = new CQueue<Integer>();
		CQueue<Integer> Q90 = new CQueue<Integer>();
		CQueue<Integer> Q100 = new CQueue<Integer>();

		
		List<Integer> ttList;
		
		List<Integer> orderedList = new ArrayList<Integer>(100);

		Q20 = tt.split(20, CQueue.LEFT, true);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 0 && i <= 20) {
				orderedList.add(i);
			}
		}
		ttList = Q20.inorderElements();
		assertEquals(orderedList, ttList);

		Q40 = tt.split(40, CQueue.LEFT, true);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 20 && i <= 40) {
				orderedList.add(i);
			}
		}
		ttList = Q40.inorderElements();
		assertEquals(orderedList, ttList);
		
		Q46 = tt.split(47, CQueue.LEFT, false);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 40 && i < 47) {
				orderedList.add(i);
			}
		}
		ttList = Q46.inorderElements();
		assertEquals(orderedList, ttList);

		Q100 = tt.split(90, CQueue.RIGHT, false);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 90 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q100.inorderElements();
		assertEquals(orderedList, ttList);

		Q90 = tt.split(90, CQueue.LEFT, true);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 46 && i <= 90) {
				orderedList.add(i);
			}
		}
		ttList = Q90.inorderElements();
		assertEquals(orderedList, ttList);
		
		Q20.insert(18);
		sc.add(18);
		Q20.delete(13);
		sc.remove(13);
		Q20.insert(2);
		sc.add(2);
		Q40.insert(26);
		sc.add(26);
		Q40.delete(25);
		Q40.delete(27);
		sc.remove(25);
		sc.remove(27);
		Q100.insert(100);
		sc.add(100);
		Q100.delete(93);
		sc.remove(93);
		Q100.insert(96);
		Q100.insert(92);
		sc.add(96);
		sc.add(92);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 0 && i <= 20) {
				orderedList.add(i);
			}
		}
		ttList = Q20.inorderElements();
		assertEquals(orderedList, ttList);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 20 && i <= 40) {
				orderedList.add(i);
			}
		}
		ttList = Q40.inorderElements();
		assertEquals(orderedList, ttList);
		
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 40 && i <= 46) {
				orderedList.add(i);
			}
		}
		ttList = Q46.inorderElements();
		assertEquals(orderedList, ttList);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 46 && i <= 90) {
				orderedList.add(i);
			}
		}
		ttList = Q90.inorderElements();
		assertEquals(orderedList, ttList);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 90 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q100.inorderElements();
		assertEquals(orderedList, ttList);

		CQueue<Integer> Q20_40 = CQueue.concatenate(Q20, Q40);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 0 && i <= 40) {
				orderedList.add(i);
			}
		}
		ttList = Q20_40.inorderElements();
		assertEquals(orderedList, ttList);
		
		CQueue<Integer> Q90_100 = CQueue.concatenate(Q90, Q100);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 46 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q90_100.inorderElements();
		assertEquals(orderedList, ttList);
		
		CQueue<Integer> Q46_100 = CQueue.concatenate(Q46, Q90_100);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 40 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q46_100.inorderElements();
		assertEquals(orderedList, ttList);
		
		CQueue<Integer> Q0_100 = CQueue.concatenate(Q20_40, Q46_100);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 0 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q0_100.inorderElements();
		assertEquals(orderedList, ttList);
	}

	@Test
	public final void testSplit() {
		List<Integer> list = new ArrayList<Integer>(100);
		Set<Integer> sc = new TreeSet<Integer>();
		for (int i = 0; i < 100; i+=2) {
			list.add(i+1);
			sc.add(i+1);
		}
		
		java.util.Collections.shuffle(list);
		for (Integer i : list) {
			tt.insert(i);
		}

		tt.insert(6);
		sc.add(6);
		tt.insert(30);
		sc.add(30);
		tt.delete(69);
		sc.remove(69);
		
		CQueue<Integer> Q20 = new CQueue<Integer>();
		CQueue<Integer> Q40 = new CQueue<Integer>();
		CQueue<Integer> Q46 = new CQueue<Integer>();
		CQueue<Integer> Q90 = new CQueue<Integer>();
		CQueue<Integer> Q100 = new CQueue<Integer>();

		
		List<Integer> ttList;
		
		List<Integer> orderedList = new ArrayList<Integer>(100);

		Q20 = tt.split(20, CQueue.LEFT, true);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 0 && i <= 20) {
				orderedList.add(i);
			}
		}
		ttList = Q20.inorderElements();
		assertEquals(orderedList, ttList);

		Q40 = tt.split(40, CQueue.LEFT, true);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 20 && i <= 40) {
				orderedList.add(i);
			}
		}
		ttList = Q40.inorderElements();
		assertEquals(orderedList, ttList);
		
		Q46 = tt.split(47, CQueue.LEFT, false);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 40 && i < 47) {
				orderedList.add(i);
			}
		}
		ttList = Q46.inorderElements();
		assertEquals(orderedList, ttList);

		Q100 = tt.split(90, CQueue.RIGHT, false);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 90 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q100.inorderElements();
		assertEquals(orderedList, ttList);

		Q90 = tt.split(90, CQueue.LEFT, true);
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 46 && i <= 90) {
				orderedList.add(i);
			}
		}
		ttList = Q90.inorderElements();
		assertEquals(orderedList, ttList);
		
		Q20.insert(18);
		sc.add(18);
		Q20.delete(13);
		sc.remove(13);
		Q20.insert(2);
		sc.add(2);
		Q40.insert(26);
		sc.add(26);
		Q40.delete(25);
		Q40.delete(27);
		sc.remove(25);
		sc.remove(27);
		Q100.insert(100);
		sc.add(100);
		Q100.delete(93);
		sc.remove(93);
		Q100.insert(96);
		Q100.insert(92);
		sc.add(96);
		sc.add(92);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 0 && i <= 20) {
				orderedList.add(i);
			}
		}
		ttList = Q20.inorderElements();
		assertEquals(orderedList, ttList);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 20 && i <= 40) {
				orderedList.add(i);
			}
		}
		ttList = Q40.inorderElements();
		assertEquals(orderedList, ttList);
		
		orderedList.clear();
		for (Integer i : sc) {
			if (i > 40 && i <= 46) {
				orderedList.add(i);
			}
		}
		ttList = Q46.inorderElements();
		assertEquals(orderedList, ttList);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 46 && i <= 90) {
				orderedList.add(i);
			}
		}
		ttList = Q90.inorderElements();
		assertEquals(orderedList, ttList);

		orderedList.clear();
		for (Integer i : sc) {
			if (i > 90 && i <= 100) {
				orderedList.add(i);
			}
		}
		ttList = Q100.inorderElements();
		assertEquals(orderedList, ttList);
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

}
