This is an implementation of the dynamic convex hull algorithm by Overmars and van Leeuwen as described in [1].

For the implementation, a special data structure named concatenable queue [2] is needed. We implemented the concatenable queue by using a 2-3 tree [2], and we implemented the 2-3 tree by extending the recently proposed leftleaning-red-black-tree (LLRB) [3]. Our extensions to LLRB include (1) storing data only at leaf nodes and (2) adding pointers to each leaf node, to point to the left and right neighbors of the node.

[1] M. H. Overmars and J. van Leeuwen. Maintenance of configurations in the plane. J. Comput. Syst. Sci., 23(2):166–204, 1981.

[2] A. V. Aho and J. E. Hopcroft. The Design and Analysis of Computer Algorithms. Addison-Wesley Longman Publishing Co., Inc., Boston, MA, USA, 1st edition, 1974.

[3] R. Sedgewick. Left-leaning red-black trees. In Dagstuhl Workshop on Data Structures, 2008.
