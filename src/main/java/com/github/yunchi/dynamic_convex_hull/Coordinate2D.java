package com.github.yunchi.dynamic_convex_hull;

public class Coordinate2D implements Comparable<Coordinate2D> {

	/**
	 * @author Yun Chi
	 * @since 2012-10-12
	 */

	protected final double x;
	protected final double y;

	public Coordinate2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Coordinate2D(Coordinate2D other) {
		this.x = other.x;
		this.y = other.y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int compareTo(Coordinate2D other) {// shallow comparison
		if (this.x < other.x) {
			return -1;
		} else if (this.x > other.x) {
			return +1;
		} else {
			if (this.y < other.y) {
				return -1;
			} else if (this.y > other.y) {
				return +1;
			} else {
				return 0;
			}
		}
	}

}
