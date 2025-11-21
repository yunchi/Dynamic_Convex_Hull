# Dynamic Convex Hull

This project implements the dynamic convex hull algorithm by Overmars and van Leeuwen [1]. It maintains the convex hull of a set of points in the plane under insertions and deletions of points.

This implementation was used in the paper on distribution-based query scheduling [2].

## Algorithm

The implementation uses a **concatenable queue** [3] data structure.
-   The concatenable queue is implemented using a **2-3 tree** [3].
-   The 2-3 tree implementation extends the **Left-Leaning Red-Black (LLRB) tree** [4].
-   Extensions to LLRB include:
    1.  Storing data only at leaf nodes.
    2.  Adding pointers to each leaf node to point to its left and right neighbors.

## Prerequisites

-   Java Development Kit (JDK)
-   Eclipse IDE (optional, but the project is structured as an Eclipse project)

## Project Structure

The source code is located in `src/main/java`.
The main package is `com.github.yunchi.dynamic_convex_hull`.

-   `ConvexHull.java`: The main class representing the dynamic convex hull.
-   `Coordinate2D.java`: Represents a point in 2D space.
-   `TTree.java`: Implementation of the 2-3 tree (LLRB based).
-   `SubHull.java`: Represents a part of the convex hull.

## Usage

### Initialization

```java
import com.github.yunchi.dynamic_convex_hull.ConvexHull;
import com.github.yunchi.dynamic_convex_hull.Coordinate2D;

ConvexHull hull = new ConvexHull();
```

### Inserting Points

```java
Coordinate2D point = new Coordinate2D(1.5, 2.5);
hull.insert(point);
```

### Deleting Points

```java
hull.delete(point);
```

## References

[1] M. H. Overmars and J. van Leeuwen. Maintenance of configurations in the plane. J. Comput. Syst. Sci., 23(2):166–204, 1981.

[2] Y. Chi, H. Hacigumus, W.-P. Hsiung, J. F. Naughton. Distribution-based query scheduling. PVLDB 2013.

[3] A. V. Aho and J. E. Hopcroft. The Design and Analysis of Computer Algorithms. Addison-Wesley Longman Publishing Co., Inc., Boston, MA, USA, 1st edition, 1974.

[4] R. Sedgewick. Left-leaning red-black trees. In Dagstuhl Workshop on Data Structures, 2008.
