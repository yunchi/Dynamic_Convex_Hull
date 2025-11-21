package com.github.yunchi.dynamic_convex_hull;

import java.util.LinkedList;
import java.util.List;

public class CQueue<E extends Comparable<E>> extends TTree<E> {

	/**
	 * @author Yun Chi
	 * @since 2012-10-10
	 */

	protected int height;
	// height=-1 for empty tree,
	// height = 0 for one-leaf-tree,
	// otherwise, height is the level of internal nodes, namely,
	// the length of the rightmost path

	// note: because of "split", field "size" becomes useless

	// note: for an empty CQueue, its minNode and maxNode are undetermined

	protected Node<E> minNode, maxNode;

	public CQueue() {
		this.root = null;
		this.height = -1;
		this.minNode = null;
		this.maxNode = null;
	}

	public CQueue(E e) {
		this.root = new Node<E>(e);
		this.height = 0;
		this.minNode = root;
		this.maxNode = root;
	}

	public CQueue(Node<E> root, int height, Node<E> minNode, Node<E> maxNode) {
		this.root = root;
		this.height = height;
		this.minNode = minNode;
		this.maxNode = maxNode;
	}

	public void shallowCopy(CQueue<E> other) {

		if (other == null) {
			return;
		}

		else {
			this.root = other.root;
			this.height = other.height;
			this.minNode = other.minNode;
			this.maxNode = other.maxNode;
		}
	}

	public int getSize() {
		// warning: this is very expensive

		if (minNode == null) {
			return 0;
		} else {
			Node<E> tempNode = minNode;
			int i = 1;
			while (tempNode.right != null) {
				i++;
				tempNode = tempNode.right;
			}
			return i;
		}
	}

	protected Node<E> addLeaf(E e, Node<E> leftLeaf, Node<E> rightLeaf) {
		assert (leftLeaf == null || leftLeaf.isLeaf)
				&& (rightLeaf == null || rightLeaf.isLeaf) : "leftLeaf and rightLeaf must be leafs";

		Node<E> n = new Node<E>(e, leftLeaf, rightLeaf);

		if (n.left != null) {
			n.left.right = n;
		} else {
			minNode = n;
		}

		if (n.right != null) {
			n.right.left = n;
		} else {
			maxNode = n;
		}

		return n;
	}

	protected void removeLeaf(Node<E> n) {
		assert n.isLeaf : "n.isLeaf";

		if (n.left != null) {
			n.left.right = n.right;
		} else {
			minNode = n.right;
		}

		if (n.right != null) {
			n.right.left = n.left;
		} else {
			maxNode = n.left;
		}
	}

	public void insert(E e) {
		if (root == null) {// empty tree
			root = minNode = maxNode = new Node<E>(e);
			height = 0;
		} else {
			root = insertAt(root, e);
			if (root.color == RED) {
				root.color = BLACK;
				height++;
			}
		}
	}

	public void delete(E e) {
		if (root == null) {
			return;
		} else if (root.isLeaf) {
			if (e.compareTo(root.ex) == 0) {
				root = minNode = maxNode = null;
				height = -1;
			}
		} else {
			if (root.left.color == BLACK && root.right.color == BLACK) {// may need help
				root.color = RED;
				height--;
			}
			root = deleteAt(root, e);
			if (root.color == RED) {// didn't use the help
				root.color = BLACK;
				height++;
			}
		}
	}

	protected static <E extends Comparable<E>> Node<E> glueTree(Node<E> lN, Node<E> rN, int lH, int rH, Node<E> lMax) {
		assert !(lN.color == RED && rN.color == RED) : "!(lN.color == RED && rN.color == RED)";

		if (lN == null) {
			return rN;
		} else if (rN == null) {
			return lN;
		} else if (lH == rH) {
			assert lN.color == BLACK && rN.color == BLACK : "only glue two equal-height BLACK nodes";

			return new Node<E>(lMax, lN, rN);
		} else if (lH > rH) {
			lN.right = glueTree(lN.right, rN, lH - 1, rH, lMax);
			lN = fixUp(lN);
			return lN;
		} else {
			if (rN.left.color == RED) {
				rN.left = glueTree(lN, rN.left, lH, rH, lMax);
				rN = fixUp(rN);
			} else {
				rN.left = glueTree(lN, rN.left, lH, rH - 1, lMax);
			}
			return rN;
		}
	}

	public static <E extends Comparable<E>> CQueue<E> concatenate(CQueue<E> qLeft, CQueue<E> qRight) {

		// step 0, handle corner cases
		// Here, the implication is that empty tree's minNode and maxNode will be
		// ignored
		if (qLeft == null || qLeft.height == -1) {
			return qRight;
		} else if (qRight == null || qRight.height == -1) {
			return qLeft;
		}

		// first, concatenate the leafs
		qLeft.maxNode.right = qRight.minNode;
		qRight.minNode.left = qLeft.maxNode;

		// second, concatenate the two trees
		int newHeight = Math.max(qLeft.height, qRight.height);
		Node<E> newRoot = glueTree(qLeft.root, qRight.root, qLeft.height, qRight.height, qLeft.maxNode);
		if (newRoot.color == RED) {
			newRoot.color = BLACK;
			newHeight++;
		}

		return new CQueue<E>(newRoot, newHeight, qLeft.minNode, qRight.maxNode);
	}

	protected static <E extends Comparable<E>> void cutAt(Node<E> n) {
		assert n == null || n.isLeaf : "n == null || n.isLeaf";

		if (n != null && n.right != null) {
			n.right.left = null;
			n.right = null;
		}
	}

	protected static <E extends Comparable<E>> void splitAt(Node<E> n, int h, E e, CQueue<E> qLeft, CQueue<E> qRight) {
		assert e != null && n != null : "e != null && n != null";
		assert qLeft != null && qRight != null : "qLeft != null && qRight != null";
		assert n.color == BLACK : "only split at a BLACK node";

		if (n.isLeaf) {// base case
			assert h == 0 : "h == 0";

			if (e.compareTo(n.ex) < 0) {
				qRight.root = n;
				qRight.minNode = n;
				qRight.height = 0;
				qLeft.maxNode = n.left;
				cutAt(n.left);
			} else {
				qLeft.root = n;
				qLeft.maxNode = n;
				qLeft.height = 0;
				qRight.minNode = n.right;
				cutAt(n);
			}
		} else {
			if (e.compareTo(n.lMax.ex) == 0) {// another base case
				qLeft.root = n.left;
				qLeft.height = h - 1;
				qLeft.maxNode = n.lMax;
				if (qLeft.root.color == RED) {
					qLeft.root.color = BLACK;
					qLeft.height++;
				}
				qRight.root = n.right;
				qRight.height = h - 1;
				qRight.minNode = n.lMax.right;
				cutAt(n.lMax);
			} else if (e.compareTo(n.lMax.ex) < 0) {// to the left subtree
				if (n.left.color == RED) {
					n.left.color = BLACK;
					splitAt(n.left, h, e, qLeft, qRight);
				} else {
					splitAt(n.left, h - 1, e, qLeft, qRight);
				}
				int tempHeight = qRight.height;
				qRight.root = glueTree(qRight.root, n.right, qRight.height, h - 1, n.lMax);
				qRight.height = Math.max(tempHeight, h - 1);
				if (qRight.root.color == RED) {
					qRight.root.color = BLACK;
					qRight.height++;
				}
			} else {// to the right subtree
				splitAt(n.right, h - 1, e, qLeft, qRight);
				if (n.left.color == RED) {
					n.left.color = BLACK;
					qLeft.root = glueTree(n.left, qLeft.root, h, qLeft.height, n.lMax);
					qLeft.height = h;
				} else {
					qLeft.root = glueTree(n.left, qLeft.root, h - 1, qLeft.height, n.lMax);
					qLeft.height = h - 1;
				}

				if (qLeft.root.color == RED) {
					qLeft.root.color = BLACK;
					qLeft.height++;
				}
			}
		}
	}

	public CQueue<E> split(E e, boolean returnLoR, boolean inclusive) {
		CQueue<E> qLeft = new CQueue<E>();
		CQueue<E> qRight = new CQueue<E>();

		// step 0, handle corner cases
		if (root == null) {
			return qLeft;
		} else if (e.compareTo(minNode.ex) < 0 || (e.compareTo(minNode.ex) == 0 && !inclusive)) {
			if (returnLoR == RIGHT) {
				qRight.shallowCopy(this);
				this.shallowCopy(qLeft);
				return qRight;
			} else {
				return qLeft;
			}
		} else {
			// first, locate the appropriate splitter
			Node<E> itr = root;
			while (!itr.isLeaf) {
				if (e.compareTo(itr.lMax.ex) <= 0) {
					itr = itr.left;
				} else {
					itr = itr.right;
				}
			}
			if (e.compareTo(itr.ex) == 0) {
				if (inclusive) {
					e = itr.ex;
				} else {
					e = itr.left.ex;
				}
			} else if (e.compareTo(itr.ex) < 0) {
				e = itr.left.ex;
			} else {
				e = itr.ex;
			}
		}

		// second, spit
		qLeft.minNode = this.minNode;
		qRight.maxNode = this.maxNode;
		splitAt(this.root, this.height, e, qLeft, qRight);

		// third, return the appropriate half
		if (returnLoR == RIGHT) {
			this.shallowCopy(qLeft);
			return qRight;
		} else {
			this.shallowCopy(qRight);
			return qLeft;
		}

	}

	public List<E> inorderElements() {
		List<E> result = new LinkedList<E>();
		if (root != null) {
			Node<E> n = minNode;
			while (n != null) {
				result.add(n.ex);
				n = n.right;
			}

		}
		return result;
	}

}
