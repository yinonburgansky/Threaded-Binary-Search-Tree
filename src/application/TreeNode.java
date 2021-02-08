package application;

/**
 * tree node class representing a node in the tree
 *
 */
class TreeNode implements Comparable<TreeNode> {
	
	/*--- Fields ---*/
	// The NIL node
	private static TreeNode nil = new TreeNode(-1, "NIL");
	// node key 
	int key;
	// additional information
	String data;
	// the pointers to the left, right and parent (was p) same as the book in page 213. 
	TreeNode left=nil, right=nil, parent=nil;
	// indication if left and right nodes are threaded
	private boolean isLeftThreaded = false,
			isRightThreaded = false;
	// x and y fields used for graphing purposes only (not being used for tree logic)
	int graphX=0, graphY=0;
	
	
	/*--- Constructors and toString ---*/
	
	// the main constructor
	TreeNode(int key, String data)
	{
		this.key = key;
		this.data = data;
	}
	// another constructor without a student name required
	TreeNode(int key)
	{
		this.key = key;
		this.data = "";
	}
	/** returns a string representation of this node 
	 * (studentId) OR (studentId, studentName) 
	 * to string override to
	 * @return a string representation of this node 
	 */
	public String toString()
	{
		String representation = Integer.toString(this.key);
		if(data.length()>0)
			representation += ", "+this.data;
		return "("+representation+")";
	}
    
	
	/*--- Getters and Setters ---*/
	
	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}
	/**
	 * @return the key
	 */
	public int key() {
		return key;
	}
	/**
	 * @param key the key to set
	 */
	public void setKey(int key) {
		this.key = key;
	}
	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the left
	 */
	public TreeNode getLeft() {
		return left;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(TreeNode left) {
		this.left = left;
	}
	/**
	 * @return the right
	 */
	public TreeNode getRight() {
		return right;
	}
	/**
	 * @param right the right to set
	 */
	public void setRight(TreeNode right) {
		this.right = right;
	}
	/**
	 * @return the parent
	 */
	public TreeNode getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	/* sets key and data from another node 
	 */
	public void copyFromNode(TreeNode other) {
		this.key = other.key;
		this.data = other.data;
		/*
		 * Don't copy other data
		 */
	}
	/**
	 * Swap key and data between this node and a given node
	 * @param z a given node to swap student information with
	 * time complexity O(1) - simple setter
	 */
	public void swapStudentData(TreeNode z) {
		// Swap student id and name 
		int tempKey = key;
		String tempData = data;
		key = z.key;
		data = z.data;
		z.key = tempKey;
		z.data = tempData;
	}
	
	
	/*--- Basic Tree Nodes logic all in O(1) ---*/
	
	/* returns true if node is leaf otherwise false
	 * @return true if node is leaf otherwise false 
	 */
	public boolean isLeaf() {
		return isNotNIL() && ((left.isNIL() || isLeftThreaded()) && (right.isNIL() || isRightThreaded()));
	}
	/* returns false if node is leaf otherwise true
	 * @return false if node is leaf otherwise true 
	 */
	public boolean isNotLeaf() {
		return !isLeaf();
	}
	/* returns true if node is a left child otherwise false
	 * @return true if node is a left child otherwise false 
	 */
	public boolean isLeftChild() {
		return isNotNIL() && parent.isNotNIL() && parent.isNotLeftThreaded() && parent.left.key() == key();
	}
	/* returns true if this node is a right child otherwise false
	 * @return true if this node is a right child otherwise false 
	 */
	public boolean isRightChild() {
		return isNotNIL() && parent.isNotNIL() && parent.isNotRightThreaded() && parent.right.key() == key();
	}
	/* returns true if this node has a right child otherwise false
	 * @return true if this node has a right child otherwise false 
	 */
	public boolean hasRightChild() {
		return this.isNotNIL() && this.isNotRightThreaded() && this.right.isNotNIL();
	}
	/* returns true if this node has a left child otherwise false
	 * @return true if this node has a left child otherwise false 
	 */
	public boolean hasLeftChild() {
		return this.isNotNIL() && this.isNotLeftThreaded() && this.left.isNotNIL();
	}

	
	/*--- Compare To and NIL logic all in O(1) ---*/

	/** override compare to return compare to between the nodes keys
	 * return integer compare to between the nodes keys
	 */
	@Override
	public int compareTo(TreeNode other) {
	  return Integer.compare(key(), other.key());
	}
	
	public static TreeNode NIL() {
		return nil;
	}
	public boolean isNIL() {
		return this.key() == -1;
		//return this==nil;
	}
	public boolean isNotNIL() {
		return !this.isNIL();
	}

	
	/*--- Minimum, Maximum, Successor and Predecessor  ---*/
	// same as the book algorithms which runs in O(h)
	// added checks for threading in O(1)

	/** returns the left most node of this (i.e. min)   - O(h)
	 * @return the left most node of this (i.e. min)
	 */
	TreeNode getMinimum() {
		TreeNode leftmost = this;
		while(leftmost.hasLeftChild()) {
			leftmost = leftmost.getLeft();
		}
		return leftmost;
	}
	/** returns the right most node of this (i.e. max) - O(h)
	 * @return the right most node of this (i.e. max)
	 */
	TreeNode getMaximum() {
		TreeNode rightmost = this;
		while(rightmost.hasRightChild()) {
			rightmost = rightmost.getRight();
		}
		return rightmost;
	}
	/** returns the successor of this node or nil 
	 * same as the book with added check for threading - page 218
	 * time complexity O(h)
	 * @return the successor of this node or nil
	 */
	TreeNode getSuccessor() {
		// if is right threaded return right - O(1) 
		if(isRightThreaded())
			return right;
		// 1-2
		if(right.isNotNIL())
			return right.getMinimum(); // O(h)
		// 3
		TreeNode x = this;
		TreeNode successor = x.getParent();
		// 4-6
		while(successor.isNotNIL() && x.isRightChild()) { // O(h)
			x = successor;
			successor = successor.getParent();
		}
		// 7
		return successor;
	}
	/** returns the predecessor of this node or nil
	 * same as the book with added check for threading - page 218
	 * time complexity O(h)
	 * @return the predecessor of this node or nil
	 */
	TreeNode getPredecessor() {
		// if is left threaded return left - O(1)
		if(isLeftThreaded())
			return left;
		// 1-2
		if(left.isNotNIL())
			return left.getMaximum();
		// 3
		TreeNode x = this;
		TreeNode predecessor = x.getParent();
		// 4-6
		while(predecessor.isNotNIL() && x.isLeftChild()) {
			x = predecessor;
			predecessor = predecessor.getParent();
		}
		// 7
		return predecessor;
	}

	
	/*--- Threading logic ---*/
	/**
	 * @return true if this node  is Left Threaded - O(1)
	 */
	public boolean isLeftThreaded() {
		return isLeftThreaded;
	}
	/**
	 * @return true if this node is Right Threaded - O(1)
	 */
	public boolean isRightThreaded() {
		return isRightThreaded;
	}	
	
	/**
	 * @return true if this node  is Left Threaded - O(1)
	 */
	public boolean isNotLeftThreaded() {
		return !isLeftThreaded();
	}
	/**
	 * @return true if this node is Right Threaded - O(1)
	 */
	public boolean isNotRightThreaded() {
		return !isRightThreaded();
	}	
	
	/** 
	 * thread both the left child and right child - O(h)
	 */
	void threadBoth() {
		threadLeft(); // O(h)
		threadRight(); // O(h)
	}
	/** 
	 * unthread both the left child and right child - O(1)
	 */
	void unthreadBoth() {
		unthreadLeft();
		unthreadRight();
	}
	/** 
	 * thread the left child - O(h)
	 */
	void threadLeft() {
		// only if not NIL and is not already left threaded and left child is NIL
		if(isNotNIL() && isNotLeftThreaded() && left.isNIL()) // O(1)
		{
			left = getPredecessor(); // O(h)	
			isLeftThreaded = left.isNotNIL(); // set left threaded only if node has a predecessor
		}
	}
	/** 
	 * thread the right child - O(h)
	 */
	void threadRight() {
		// only if not NIL and is not already right threaded and right child is NIL
		if(isNotNIL() && isNotRightThreaded() && right.isNIL()) // O(1)
		{
			right = getSuccessor();	// O(h)
			isRightThreaded = right.isNotNIL(); // set right threaded only if node has a Successor
		}
	}
	/** 
	 * unthread the left child - O(1)
	 */
	void unthreadLeft() {
		// only if not NIL and is left threaded proceed
		if(isNotNIL() && isLeftThreaded())
		{
			left = nil;	// O(1)
			isLeftThreaded = false; // O(1)
		}
	}	
	/** 
	 * unthread the right child - O(1)
	 */
	void unthreadRight() {
		// only if not NIL and is right threaded proceed
		if(isNotNIL() && isRightThreaded()) // O(1)
		{
			right = nil;	// O(1)
			isRightThreaded = false;  // O(1)
		}
	}
	
	
	/*--- size, depth, max depth setters and getters used for graph only ---*/
	
	/** getter for x coordinate
	 * @return x coordinate
	 */  
	public int getX() {
		return graphX;
	}
	/** Setter for x coordinate
	 * @param x the x coordinate to set
	 */ 
	public void setX(int x) {
		this.graphX = x;
	}
	/** getter for y coordinate
	 * @return y coordinate
	 */ 
	public int getY() {
		return graphY;
	}
	/** Setter for y coordinate
	 * @param y the y coordinate to set
	 */ 
	public void setY(int y) {
		this.graphY = y;
	}
}


