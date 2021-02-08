package application;

import javafx.scene.canvas.Canvas;

/**
 *  The Main Application class containing main logic
 */
public class MainApplication {
	
	// helper variables storing current key and data
	int key;
	String data; 
	// the main tree of the application
	Tree mainTree;
	// the current selected node
	TreeNode selectedNode;
	// AppData object
	AppData state;
	// TreeGraph handles graph logic
	TreeGraph graph;
	
    /** the constructor of the main application 
     * @param treeCanvas
     */
    public MainApplication(Canvas treeCanvas) {
		// set up a new tree
		mainTree = new Tree();
		// setup new state
		state = new AppData();
		// set selected node to nil
		state.setSelectedNode(TreeNode.NIL())
			 .setMainTree(mainTree);
		// set up graph object
		graph = new TreeGraph(state, treeCanvas);
		update(state);
	}
    /** Sanitize user Input and return true if is valid otherwise false
     * @param keyString the key to be sanitized
	 * @param dataString the data to be sanitized
	 * @return true if is valid otherwise false
	 */
	public boolean sanatizeInput(String keyString, String dataString) {
		try {
			this.key = Integer.parseInt(keyString);
			this.data = dataString;
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	/** Sanitize given state and return true if is valid otherwise false
	 * @return true if is valid otherwise false
	 */
	public boolean sanatizeState() {
		if(!sanatizeInput(state.getKeyString() ,state.getData())) {
			state.appendToOutput("Illegal input");
			return false;
		}
		state.setKey(key);
		return true;
	}
	/** Check if user selected a node and return true otherwise false
	 * @return true user selected a node otherwise false
	 */
	public boolean hasSelected() {
		// update object selected
		selectedNode = state.getSelectedNode();
		// if no node has been selected
		if(state.getSelectedNode() == null || state.getSelectedNode().isNIL()) {
			state.appendToOutput("You have to search a node first");
			return false;
		}
		return true;
	}
	/** Check if tree is empty and update
	 * @return true user selected a node otherwise false
	 */
	public boolean isTreeEmpty() {
		// if tree is empty
		if(mainTree.isEmpty()) {
			state.appendToOutput("The tree is empty, you have to add nodes first");
			return true;
		}
		return false;
	}
	
	/**
	 * @return a TreeNode with user input, use only after data has been sanitized
	 */
	public TreeNode getTreeNode() {
		return new TreeNode(key, data);
	}
	
	/** getter for application state
	 * @return the state
	 */
	public AppData getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 * @return 
	 */
	public AppData setState(AppData state) {
		this.state = state;
		return update(state);
	}
	
	/** inserts a student into the tree using text fields 
	 * return an AppData object to be displayed
	 * @param newState AppData containing :
	 * studentId the student id to be inserted
	 * studentName the student name to be inserted
	 * @return AppData object to be displayed
	 */
	public AppData insert(AppData newState) {
		// set the new application state
		state = newState;
		// if not is not sanitized return modified state
		if(!sanatizeState()) 
			return state;
		// otherwise passed sanitation -> proceed
		// get tree node and insert it
		TreeNode t = getTreeNode();
		TreeNode temp = mainTree.treeInsert(t);
		// check if inserted successfully
		if(temp.isNIL())
			return state.appendToOutput(t+" already exists");
		// only if inserted successfully
		state.appendToOutput(temp+" added to the tree")
		     .setSelectedNode(temp);
		// update current state and return it
		return update(state);
	}
	
	
	public AppData search(AppData newState) {
		// first set the new application state
		state = newState;
		// if not is not sanitized return modified state
		if(!sanatizeState()) 
			return state;
		// otherwise passed sanitation -> proceed
		// get tree node and insert it
		TreeNode t = getTreeNode();
		TreeNode temp = mainTree.treeSearch(t);
		// check if key was not found
		if(temp.isNIL())
			return state.appendToOutput(t+" does not exist in the tree");
		// otherwise key was found
		state.appendToOutput(temp+" was found in the tree")
		     .setSelectedNode(temp);		
		// update current state
		return update(state);
	}	
		
	public AppData delete(AppData newState) {
		// first set the new application state
		state = newState;
		// if not is not sanitized return modified state
		if(!sanatizeState()) 
			return state;
		// otherwise passed sanitation -> proceed
		// get tree node and insert it
		TreeNode t = getTreeNode();
		TreeNode temp = mainTree.treeDelete(t.key());
		// check if key was found 
		if(temp.isNIL())
			return state.appendToOutput(t+" does not exist in the tree");
		// otherwise treeNode deleted successfully 
		state.appendToOutput(temp+" deleted from the tree");
		// clears selected node and update current state and return it
		return clearSelected(state);
	}

	public AppData preOrderScan(AppData newState) {
		// first set the new application state
		state = newState;
		// check if tree is empty return updated state
		if(isTreeEmpty())
			return state;
		// otherwise
		return state.appendToOutput("Preorder Traversal: " + mainTree.preOrderScan());
	}
	
	public AppData inOrderScan(AppData newState) {
		// first set the new application state
		state = newState;
		// check if tree is empty return updated state
		if(isTreeEmpty())
			return state;
		// otherwise
		return state.appendToOutput("Inorder Traversal: " + mainTree.inOrderScan());
	}
	
	public AppData postOrderScan(AppData newState) {
		// first set the new application state
		state = newState;
		// check if tree is empty return updated state
		if(isTreeEmpty())
			return state;
		// otherwise
		return state.appendToOutput("Postorder Traversal: " + mainTree.postOrderScan());
	}
	
	public AppData min(AppData newState) {
		// first set the new application state
		state = newState;
		// check if tree is empty return updated state
		if(isTreeEmpty())
			return state;
		// otherwise
		selectedNode = mainTree.treeMinimum();
		state.setSelectedNode(selectedNode)
			 .appendToOutput("The Minimum is: " + selectedNode);
		// update current state and return
		return update(state);
	}
	
	public AppData max(AppData newState) {
		// first set the new application state
		state = newState;
		// check if tree is empty return updated state
		if(isTreeEmpty())
			return state;
		// otherwise
		selectedNode = mainTree.treeMaximum();
		state.setSelectedNode(selectedNode)
			 .appendToOutput("The Maximum is: " + selectedNode);
		// update current state and return
		return update(state);
	}
	
	public AppData median(AppData newState) {
		// first set the new application state
		state = newState;
		// check if tree is empty return updated state
		if(isTreeEmpty())
			return state;
		// otherwise
		selectedNode =  mainTree.treeMedian();
		state.setSelectedNode(selectedNode)
		     .appendToOutput("The median is: " + selectedNode);
		// update current state and return
		return update(state);
	}
	// 
	public AppData prev(AppData newState) {
		// first set the new application state
		state = newState;
		// if no node has been selected
		if(!hasSelected())
			return state;
		// otherwise get predecessor
		TreeNode temp = selectedNode.getPredecessor();
		// if selected node doesn't have predecessor
		if(temp.isNIL()) 
			return state.appendToOutput("There is no predecessor for: " + selectedNode);
		// otherwise update selected print the predecessor
		state.setSelectedNode(temp)
			 .appendToOutput("The predecessor is: " + temp);
		// update current state and return
		return update(state);
	}
	
	public AppData next(AppData newState) {
		// first set the new application state
		state = newState;
		// if no node has been selected
		if(!hasSelected())
			return state;
		// otherwise get the successor
		TreeNode temp = selectedNode.getSuccessor();
		// if selected node doesn't have successor
		if(temp.isNIL()) 
			return state.appendToOutput("There is no successor for: " + selectedNode);
		// otherwise update selected print the successor
		state.setSelectedNode(temp)
		     .appendToOutput("The successor is: " + temp);
		// update current state and return
		return update(state);
	}
	
	public AppData clearSelected(AppData newState) {
		// first set the new application state
		state = newState;
		// if selected node is not NIL then clear it 
		if(state.getSelectedNode().isNIL())
			state.appendToOutput("Selection is already clean");
		else
			state.setSelectedNode(TreeNode.NIL())
		     	 .appendToOutput("Selection cleaned");
		return update(state);
	}
	
	/** update 
	 */
	public AppData update(AppData newState) {
		state = newState;
		// update selected node
		updateSelectedNode(state);
		// update main tree in state
		state.setMainTree(mainTree);
		// paint the tree
		graph.paintTree(state);
		return state;
	}
	
	public AppData updateSelectedNode(AppData newState) {
		// first set the new application state
		state = newState;
		selectedNode = state.getSelectedNode();
		if(selectedNode.isNIL()) 
			return state.setSelectedText("None");
		// add selected node string representation first
		state.setSelectedText("Node: " + selectedNode.toString());
		// add indication if selected node has left thread
		if(selectedNode.isLeftThreaded())
			state.appendToSelectedText("Left thread: " + selectedNode.left);
		// add indication if selected node has right thread
		if(selectedNode.isRightThreaded())
			state.appendToSelectedText("Right thread: " + selectedNode.right);
		// update the selected text field accordingly
		return state;
	}
	
	/** clears the input
	 * @param newState the application state
	 * @return the state after change
	 */
	public AppData clearInput(AppData newState) {
		return update(newState.clearInput());
	}
	/** clears the output
	 * @param newState the application state
	 * @return the state after change
	 */
	public AppData clearOutput(AppData newState) {
		return update(newState.clearOutput());
	}
	
	/** runs the multiLine input
	 * @param newState the application state
	 * @return the state after change
	 */
	public AppData runInput(AppData newState) {
		// first set the new application state
		state = newState;
		// command for each line
		String[] lines = state.getInput().split("\n");
	    for (String line : lines) {
			runLine(line);
		}
		return update(state);
	}
	
	/** run a command from a given line 
	 * @param state
	 * @param line
	 */
	public AppData runLine(String line) {
		String command = 		 	 getCommandFromLine(line, 0, false),
	     	   studentIdString = 	 getCommandFromLine(line, 1, false),
			   studentNameString =	 getCommandFromLine(line, 2, true);
		// set the state accordingly
		state.setKeyString(studentIdString)
			 .setData(studentNameString);
		
		// all commands
		switch(command){
			case "add": 
				return insert(state);
			case "search": 
				return search(state);
			case "delete": 
				return delete(state);
			case "predecessor": 
				return prev(state);
			case "successor": 
				return next(state);
			case "min": 
				return min(state);
			case "max": 
				return max(state);
			case "median": 
				return median(state);
			case "preorder": 
				return preOrderScan(state);
			case "inorder": 
				return inOrderScan(state);
			case "postorder": 
				return postOrderScan(state);
			default:
				return update(state.appendToOutput("There is no action named: "+command));
		}
	}
	
	/** returns a selected command parameter separated by a space from a given line
	 * @param line
	 * @param index
	 * @param isLastCommand
	 * @return string part containing the selected command
	 */
	public String getCommandFromLine(String line, int index, boolean isLastCommand) {
		if(index<0)
			return "";
		// set index of chosen command part start
		int start = ordinalIndexOf(line, " ", index);
		if(index == 0)
			start = 0;
		else if(start == -1)
			return "";
		else
			start++;
		// set index of chosen command part end
	    int end = ordinalIndexOf(line, " ", index + 1);
		if (isLastCommand || end == -1)
			end = line.length();
		//
		if(start>end)
			return "";
		// return the chosen command part from the string
		return line.substring(start, end);
	}

	/**
	 * String helper method from StringUtils.ordinalIndexOf
	 * @return the index of the n'th match 
	 */
	public static int ordinalIndexOf(String str, String substr, int n) {
		int counter = n;
	    int pos = str.indexOf(substr);
	    while (--counter > 0 && pos != -1)
	        pos = str.indexOf(substr, pos + 1);
	    return pos;
	}
	
}
