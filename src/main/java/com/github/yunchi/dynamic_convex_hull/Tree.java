package com.github.yunchi.dynamic_convex_hull;

public interface Tree<E> {
	
	/**
	 * @author Yun Chi
	 * @since 2012-10-10 
	 */
	
	public int getSize();
	public E search (E e);
	public void insert(E e);
	public void delete(E e);
}
