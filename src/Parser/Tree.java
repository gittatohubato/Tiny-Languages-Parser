package Parser;

public class Tree {

    public Node root;

    public Tree(){
        root = null;
    }

    public static void printPreorder(Node node)
    {
        if (node == null)
            return;

        System.out.print(node.root.getSearchKey()+"  node shape = "+node.shape + "->");

        printPreorder(node.leftChild);
        printPreorder(node.midChild);

        printPreorder(node.rightChild);
        printPreorder(node.neighbour);
    }
}
