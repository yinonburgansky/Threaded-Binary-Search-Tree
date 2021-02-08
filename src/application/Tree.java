package application;

public class Tree {
	// the tree root node
	TreeNode root;
	// median is saved here
	TreeNode median;
	// the size of the tree
	int totalCount;
	
	/**
	 * the constructor of the tree
	 */
	Tree(){
		// initiate all fields
		root = TreeNode.NIL();
		median = TreeNode.NIL();
		totalCount = 0;
	}
	
	/*--- Getters ---*/
	/**
	 * getter for Tree root
	 * @return the root of the tree
	 */
	public TreeNode getRoot() {
		return root;
	}
	
	/**
	 * returns true if the tree is empty otherwise false
	 * @return true if the tree is empty otherwise false
	 */
	public boolean isEmpty() {
		return root.isNIL();
	}	
	
	/*--- Search, Insert and Delete ---*/
	
	/** search a key in the subtree node - O(h) 
	 * @param node to search the key in
	 * @param key to search
	 * @return a node with the given key or NIL node
	 */
	TreeNode nodeSearch(TreeNode node, int key) {
		/**
		 * same as the book algorithm with 
		 * added checks that node has left child and it is not a thread
		 * this logical checks are done in O(1) 
		 * there by the total time complexity of algorithm remain O(h)   
		 */
		TreeNode current = node;
		while(current.isNotNIL() && current.key() != key) {
			if(key < current.key() && current.hasLeftChild()) 
				current = current.getLeft();
			else if (key > current.key() && current.hasRightChild())
				current = current.getRight();
			else 
				break ;
		}
		// if the final node has the same key return the node 
		if(current.key() == key) 
			return current;
		// otherwise return the nil node			
		return TreeNode.NIL();
	}
	/** returns a node search in root node - O(h)
	 * @param key the key search for
	 * @return the found node otherwise return NIL
	 */
	TreeNode treeSearch(int key) {
		return nodeSearch(root, key);
	}

	/** search by a (disconnected) node - O(h)
	 * overloaded version of search
	 * @param node to get key from and search
	 * @return the found node otherwise return NIL
	 */
	TreeNode treeSearch(TreeNode node) {
		return treeSearch(node.key());
	}
	/** inserts a node into the tree if not already exists - O(h)
	 * returns the node inserted or NIL node if already exists
	 * @param node to insert to the tree
	 * @return the node inserted or NIL node if already exists
	 */
	TreeNode treeInsert(TreeNode node) {
		/* same as the book insertion algorithm - page 220
		 * y = preSearchNode
		 * x = searchNode
		 * time complexity of the original algorithm O(h) where h is the tree height
		 * the time complexity of the added logic to the algorithm is O(h): 
		 * inside the while loop - O(1), thus the entire loop in O(h) as in the book 
		 * outside the while loop - threading in O(h)
		 * afterTreeInsert - O(h)
		 * there by the total time complexity is O(h)  
		 */
		TreeNode preSearchNode = TreeNode.NIL();
		TreeNode searchNode = root;
		// if key already exists return NIL
		if(node.key() == searchNode.key())
			return TreeNode.NIL();
		
		while(searchNode.isNotNIL()) {
			// if key already exists return NIL
			if(node.key() == searchNode.key())
				return TreeNode.NIL();
			
			// save search Node in  preSearch node before changing it 
			preSearchNode = searchNode;
			// check if node is in left or right
			if(node.key() < searchNode.key()) {
				// if the node is left Threaded we end the search otherwise continue as usual - O(1)
				if(searchNode.isLeftThreaded())
					searchNode = TreeNode.NIL();
				else
					searchNode = searchNode.getLeft();
			} else {
				// if the node is right Threaded we end the search otherwise continue as usual - O(1)
				if(searchNode.isRightThreaded())
					searchNode = TreeNode.NIL();
				else
					searchNode = searchNode.getRight();
			}
		}
		// the node will be inserted as a child of preSearch node
		// set node parent
		node.setParent(preSearchNode);
		if(preSearchNode.isNIL()) { // the tree was empty
			root = node;
		} else {
			if(node.key() < preSearchNode.key()) {
				preSearchNode.unthreadLeft();// added logic for threading - O(1)
				preSearchNode.setLeft(node);
			} else {
				preSearchNode.unthreadRight();// added logic for threading - O(1)
				preSearchNode.setRight(node);
			}
			node.threadBoth();// added logic for threading - O(h)
		}
		
		// call after insert event after actual insertion - O(h)
		afterTreeInsert(node);
		return node;
	}
	/** removes the given node from the tree and returns it or NIL node - O(h)
	 * same as the book deletion algorithm - page 221
	 * @return removes the given node from the tree and returns it or NIL node
	 */
	TreeNode treeDelete(TreeNode z) {
		// fire before delete event - O(h) thus doesn't change time complexity
		beforeTreeDelete(z);
		// same as the book deletion algorithm - page 221
		TreeNode x, y, yParent, prev, next;
		
		// 1-3 decide to remove the node or its successor
		if(z.hasLeftChild() && z.hasRightChild()) {
			y = z.getSuccessor();
		} else
			y = z;
		
		// 4-6 set x to the real son of y (i.e. not nil or thread node)
		if(y.hasLeftChild())
			x = y.getLeft();
		else if(y.hasRightChild())
			x = y.getRight();
		else // y is a leaf
			x = TreeNode.NIL();
		
		// added logic in O(h)
		// after establishing x and y 
		// before any changes being made
		// save y's following related nodes: parent, predecessor and successor, in order to handle threading logic 
		prev = y.getPredecessor(); // O(h)
		next = y.getSuccessor(); // O(h)
		yParent = y.getParent(); // O(1)
		// then remove all the threads in O(1).
		x.unthreadBoth(); 
		y.unthreadBoth();
		yParent.unthreadBoth();
		prev.unthreadBoth();
		next.unthreadBoth();
		// end added logic
		
		// 7-8 set x parent to y parent (i.e. remove y)
		if(x.isNotNIL())
			x.setParent(yParent);
		// 9-13 check for edge cases
		if(yParent.isNIL()) // y is root
			root = x;
		else if(y.isLeftChild()) // otherwise set x to be a child of y parent (i.e. remove y)
			yParent.setLeft(x);
		else if(y.isRightChild())
			yParent.setRight(x);
		
		// added logic - O(1)
		// remove node connections from the tree in O(1)
		y.setParent(TreeNode.NIL());
		y.setLeft(TreeNode.NIL());
		y.setRight(TreeNode.NIL());
		
		// 14-16 if node successor was removed, 
		// then Swap data between successor and the original node in O(1)
		if(z.key()!=y.key())
			z.swapStudentData(y); 
		
		// after all the changes being made rethread nodes in O(h) 
		// rethread: x, y's parent, prev and next.
		x.threadBoth();
		yParent.threadBoth();
		prev.threadBoth();
		next.threadBoth();
		
		// 17 return the removed node to free resources - java handles it already  
		return y;
	}
	/** removes the given node from the tree by a key and returns it or NIL node - O(h)
	 * @return removes the given node from the tree and returns it or NIL node
	 */
	TreeNode treeDelete(int key) {
		/**
		 * search the node by key in O(h) if not found return nil
		 * if found then delete the node in O(h) and return it 
		 */
		TreeNode temp = treeSearch(key);
		// if a node with corresponding key was not found return nil
		if(temp.isNIL())
			return TreeNode.NIL();
		// otherwise return the deletion method on this node
		return treeDelete(temp);
	}
	
	/*--- Minimum and Maximum ---*/
	
	/** returns the node the the minimum key or NIL node - O(h)
	 * @return the node with the minimum key or NIL node
	 */
	TreeNode treeMinimum() {
		// logic is in TreeNode class in O(h)
		return root.getMinimum();
	}
	/** returns the node the the maximum key or NIL node - O(h)
	 * @return the node with the maximum key or NIL node
	 */
	TreeNode treeMaximum() {
		// logic is in TreeNode class in O(h)
		return root.getMaximum();
	}
	
	/*--- Successor and Predecessor ---*/
	
	/** returns the successor of a given node or NIL node - O(h)
	 * @return the successor of a given node or NIL node
	 */
	TreeNode treeSuccessor(TreeNode node) {
		// logic is in TreeNode class in O(h)
		return node.getSuccessor();
	}
	/** returns the predecessor of a given node or NIL node - O(h)
	 * @return the predecessor of a given node or NIL node
	 */
	TreeNode treePredecessor(TreeNode node) {
		// logic is in TreeNode class in O(h)
		return node.getPredecessor();
	}
	
	/*--- Median ---*/
	/** getter for the median - O(1)
	 * @return the median of the tree in O(1)
	 */
	TreeNode treeMedian() {
		// simple getter thus O(1)
		return median;
	}
	
	/*--- Events to hook to in order to maintain tree qualities ---*/
	
	/** runs after insertion of each node
	 * update median and totalCount in O(h)
	 * @param t the node that was inserted
	 */
	void afterTreeInsert(TreeNode t) {
		// median logic first: update median
		// if the tree was empty then set newly added node to median and return - O(1)
		if(median.isNIL()) {
			median = t;
			totalCount++;
			return;
		}
		// Otherwise check if the added node is less than median or not - total O(h)
		if(t.key() < median.key()) {
			// only if the total count is odd before addition
			// than update median to its predecessor in O(h)
			if(totalCount % 2 == 1)
				median = median.getPredecessor();
		} else {
			// only if the total count is even before addition
			// than update median to its successor in O(h)
			if(totalCount % 2 == 0)
				median = median.getSuccessor(); 
		}	
		// update total count only after median logic - O(1)
		totalCount++;
	}
	/** runs before deletion of each node
	 * update median and totalCount in O(h)
	 * @param t the node that was inserted
	 */
	void beforeTreeDelete(TreeNode t) {
		// median logic first: update median
		// set median to its successor or predecessor in O(h) in the following way:
		// first check if the median was removed
		if(median.key() == t.key()) { 
			// if the count was even the new median will be the successor otherwise the predecessor
			if(totalCount % 2 == 0)
				median = median.getSuccessor(); // O(h)
			else
				median =  median.getPredecessor(); // O(h)
		} else if(t.key()<median.key()) {
			// if the key of the removed node is less than median
			// and the total count is even before deletion
			// than update median to its successor
			if(totalCount % 2 == 0)
				median = median.getSuccessor(); // O(h)
		} else {
			// else the key of the removed node is grater than median
			// only if the total count is odd before deletion
			// than update median to its predecessor
			if(totalCount % 2 == 1)
				median = median.getPredecessor(); // O(h)
		}	
		// update total count only after median logic in O(1)
		totalCount--;
	}
	
	
	/*--- Scans ---*/
	
	/** returns preOrder scan string representation - O(n) 
	 * @return preOrder scan string representation 
	 */
	String preOrderScan(TreeNode x) {
		// simple scan using recursion same as the book algorithm in O(n)
		// if x is nil return 
		if(x.isNIL())
			return "";
		// else if is not threaded scan children as well
		String res = "";
		res+=x.toString() + ", ";
		if(x.hasLeftChild())
			res += preOrderScan(x.getLeft());
		if(x.hasRightChild())
			res += preOrderScan(x.getRight());
		// return final result
		return res;
	}
	/** returns postOrder scan string representation - O(n) 
	 * @return postOrder scan string representation 
	 */
	String postOrderScan(TreeNode x) {
		// simple scan using recursion same as the book algorithm in O(n)
		// is x is nil return 
		if(x.isNIL())
			return "";
		// else if is not threaded scan children as well
		String res = "";
		if(x.hasLeftChild())
			res += postOrderScan(x.getLeft());
		if(x.hasRightChild())
			res += postOrderScan(x.getRight());
		res+=x.toString() + ", ";
		// return final result
		return res;
	}
	/** returns in order scan string representation - O(n)
	 * @return in order scan string representation 
	 */
	String inOrderScan() {
		// according to the book calling successor n times is done in O(n)
		// we added threads which only improves the search time if a node is threaded 
		// thus the total time complexity remains O(n) as the book
		String res = "";
		if(root.isNIL())
			return res;
		TreeNode x = treeMinimum();
		while(x.isNotNIL()) {
			res += x.toString() + ", ";
			x = x.getSuccessor();
		}
		// return final result
		return res;
	}
	/* helper method for recursion initiation of preOrder scan*/
	/** returns preOrder scan string representation - O(n)
	 * @return preOrder scan string representation 
	 */
	String preOrderScan() {
		return preOrderScan(root);
	}
	/* helper method for recursion initiation of postOrder scan*/
	/** returns postOrder scan string representation - O(n)
	 * @return postOrder scan string representation 
	 */
	String postOrderScan() {
		return postOrderScan(root);
	}
}
