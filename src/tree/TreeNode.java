package tree;

import shape.Circle;

/**
 * A binary tree using circle objects.
 * @author Eric Canull
 * @version 1.0
 */
public class TreeNode {
	
	public Circle root;
	public TreeNode leftChild;
	public TreeNode rightChild;
	public TreeNode midChild; //my version
	public TreeNode neighbour; //my version
	public boolean highlightFlag;
	
	/**
	 * A binary tree using circle objects.
	 * @param root a root tree circle
	 * @param leftCircle a left tree circle
	 * @param rightChild a right tree circle
	 */
	public TreeNode(Circle root, TreeNode leftCircle, TreeNode rightChild, TreeNode midChild, TreeNode neighbour) {
		this.root = root;
		this.leftChild = leftCircle;
		this.rightChild = rightChild;
		this.midChild = midChild; //my version
		this.neighbour = neighbour; //my version
	}

	private static int max(int a, int b, int c) {
		return (a >= b) ? ((a >=c)? a : c) : ((b>=c)? b:c);
	}

	public int getHeight(TreeNode root) {
		if (root == null)
			return 0;
		return max(getHeight(root.leftChild), getHeight(root.rightChild), getHeight(root.midChild)) + 1;
	}
}
