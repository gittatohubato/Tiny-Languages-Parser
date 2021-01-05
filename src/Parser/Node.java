package Parser;

import shape.Circle;

public class Node {

    public Node leftChild;
    public Node rightChild;
    public Node midChild;
    public Node neighbour;
    public String name;
    public Circle root;
    public boolean highlightFlag;

    public int shape;

    public Node(String name, int shape){

        leftChild = null;
        rightChild = null;
        midChild = null;
        neighbour = null;
        this.shape = shape;
        this.name = name;
        this.root = new Circle(name);
    }

    private static double max3(double a, double b, double c) {
        return (a >= b) ? ((a >=c)? a : c) : ((b>=c)? b:c);
    }
    private static double max(double a, double b, double c, double d) {
        return Math.max(max3(a,b,c), d);
    }


    public double getHeight(Node root) {
        if (root == null)
            return 0;
        return max(getHeight(root.leftChild), getHeight(root.rightChild), getHeight(root.midChild), getHeight(root.neighbour)) + 1;
    }

    public double getWidth(Node root) {
        if (root == null)
            return 0;
        return max(getWidth(root.leftChild), getWidth(root.rightChild), getWidth(root.midChild), getWidth(root.neighbour)) + 1;
    }


}
