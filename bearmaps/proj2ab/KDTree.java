package bearmaps.proj2ab;

import java.util.List;

/**
 * This class represents a KD-Tree. Although regular KD-Tree can represent multi dimensions,
 * this implementation can only work for the 2-dimensional case.
 */
public class KDTree implements PointSet {

  private Node root;

  // We assume that points has at least size 1.
  public KDTree(List<Point> points) {
    root = null;
    for (Point p : points) {
        this.root = add(root, p, Dimension.VERTICAL);
    }
  }


  @Override
  public Point nearest(double x, double y) {
    Point target = new Point(x, y);
    Node result = nearest(root, target, root);
    return result.getPoint();
  }

  // A helper method to traverse the tree and find the best point.
  private Node nearest(Node n, Point goal, Node best) {
    if (n == null)
      return best;
    if (n.distance(goal) < best.distance(goal))
      best = n;

    Node goodSide, badSide;
    if (compare(goal, n.getPoint(), n.dimension) < 0) {
      goodSide = n.leftChild;
      badSide = n.rightChild;
    } else {
      goodSide = n.rightChild;
      badSide = n.leftChild;
    }

    best = nearest(goodSide, goal, best);

    // This is the pruning rule here to boost up the speed.

    if (prune(n, goal, best)) {
      best = nearest(badSide, goal, best);
    }


    //best = nearest(badSide, goal, best);
    return best;
  }

  private boolean prune(Node node, Point goal, Node best) {
    if (node == null)
      return false;
    Point p;
    if (node.dimension.equals(Dimension.HORIZONTAL)) {
      p = new Point(goal.getX(), node.getPoint().getY());
    } else {
      p = new Point(node.getPoint().getX(), goal.getY());
    }
    return Point.distance(p, goal) < Point.distance(best.getPoint(), goal);
  }

  // Add a Node based on the rule of KDTree.
  private Node add(Node n, Point p, Dimension d) {
    // base case.
    if (n == null) {
      return new Node(p, d);
    }
    if (n.getPoint().equals(p))
      return n;
    // Change the dimension to the contrast one.
    Dimension newDimension = changeDimension(d);

    if (compare(p, n.getPoint(), d) >= 0)
      n.rightChild = add(n.rightChild, p, newDimension);
    else
      n.leftChild = add(n.leftChild, p, newDimension);
    return n;
  }

  // Return a dimension in contrast.
  private Dimension changeDimension(Dimension d) {
    if (d.equals(Dimension.VERTICAL))
      return Dimension.HORIZONTAL;
    else
      return Dimension.VERTICAL;
  }

  // Compare the two points based on vertical and horizontal.
  private int compare(Point a, Point b, Dimension d) {
    if (d.equals(Dimension.VERTICAL))
      return Double.compare(a.getX(), b.getX());
    else
      return Double.compare(a.getY(), b.getY());
  }

  /**
   * A private class that represents a node of a KDTree.
   */
  private class Node {
    private Dimension dimension;
    private Point point;
    private Node leftChild;
    private Node rightChild;

    // The constructor of Node. It contains a enum Dimension and a point.
    private Node(Point p, Dimension d) {
      this.dimension = d;
      this.point = p;
    }

    private Point getPoint() {
      return this.point;
    }

    private double distance(Point target) {
      return point.distance(point, target);
    }
  }
}
