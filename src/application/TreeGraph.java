package application;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class TreeGraph {
	
	// State of the application
	AppData state;
	// the main tree of the application
	Tree mainTree;
	// the root node of main tree
	TreeNode root;
	// Selected Node will be updated here
	TreeNode selectedNode;
	
	// Canvas treeCanvas;
	Canvas treeCanvas;
	// canvas GraphicsContext
	GraphicsContext gc;
	// max x and y coordinates used to calculate width and height
	private int maxX=0, maxY=0;

	// graph Settings 
	// can be moved to state and initiated at @setState object to add an interface
	
	// graph node dimensions
	int nodeWidth = 40,
			nodeHeight = 40,
			hGap = 5,
			vGap = 5;
	// Graph Colors
	Color backgroundColor = Color.BEIGE,
		  nodeBackgroundColor = Color.web("#b0bec5"),
		  selectedNodeBackgroundColor = Color.web("#ffe082"),
	      nodeStrokeColor = Color.web("#808e95"),
	      selectedNodeStrokeColor = Color.web("#caae53"),
	      connectionLineColor = Color.DARKBLUE,
		  threadLineColor = Color.PURPLE,
	      nodeTextColor = Color.BLACK;
	
	
	/** Constructor
	 * @param mainTree
	 */
	public TreeGraph(AppData newState, Canvas treeCanvas) {
		setTreeCanvas(treeCanvas);
		setState(newState);
	}
	
	/*--- Getters and Setters ---*/
	
	/**
	 * @return the state
	 */
	public AppData getState() {
		return state;
	}
	/**
	 * @param state the state to set
	 */
	public void setState(AppData newState) {
		this.state = newState;
		setMainTree(state.getMainTree());
		this.selectedNode = state.getSelectedNode();
	}
	/**
	 * @return the mainTree
	 */
	public Tree getMainTree() {
		return mainTree;
	}
	/**
	 * @param mainTree the mainTree to set
	 */
	public void setMainTree(Tree mainTree) {
		this.mainTree = mainTree;
		// update tree root
		root = mainTree.getRoot();
	}
	/**
	 * @return the treeCanvas
	 */
	public Canvas getTreeCanvas() {
		return treeCanvas;
	}
	/**
	 * @param treeCanvas the treeCanvas to set
	 */
	public void setTreeCanvas(Canvas treeCanvas) {
		this.treeCanvas = treeCanvas;
		this.gc = this.treeCanvas.getGraphicsContext2D();
		// set paint text in center
		gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        // set open paths
        
	}
	
	
	/*--- paint logic ---*/
	
	public void paintTree(AppData newState) {
		// update application state first
		setState(newState);
		// calculate size and depth
		calculateXY();
		// paint the background 
		paintBackgroud();
		// paint the tree recursively
		paintTreeNode(root);
	}

	/**
	 * update canvas width and height and paint the background color 
	 */
	private void paintBackgroud() {
		// get canvas width and height and set it
		int canvasWidth = getWidth();
		int canvasHieght = getHeight();
		//update canvas width and height
		treeCanvas.setWidth(canvasWidth);
		treeCanvas.setHeight(canvasHieght);
		// fill background color
		gc.setFill(backgroundColor);
		gc.fillRect(0, 0, canvasWidth, canvasHieght);
	}
	
	public void paintTreeNode(TreeNode t) {
		// paint only if node is not nil 
		if(t==null || t.isNIL())
			return;
		// handle left
		if(t.isLeftThreaded()) {
			paintThreadLine(t, t.getLeft());
		} else if(t.hasLeftChild()) {
			paintConnectionLine(t, t.getLeft());
			paintTreeNode(t.getLeft());
		} 
		// handle right
		if(t.isRightThreaded()) {
			paintThreadLine(t, t.getRight());
		} else if(t.hasRightChild()) {
			paintConnectionLine(t, t.getRight());
			paintTreeNode(t.getRight());
		}
		// paint the body after (above) painting the connection lines 
		paintNodeBody(t);	
	}
	/** paints a connection line between parent and child nodes
	 * @param parent the parent node
	 * @param child the child node
	 */
	private void paintConnectionLine(TreeNode parent, TreeNode child) {
		// set stroke color
		gc.setStroke(connectionLineColor);
		// print the line using, middle coordinates
		gc.strokeLine(getXMidPos(parent), getYMidPos(parent), getXMidPos(child), getYMidPos(child));
	}
	/** paints a connection line between parent and child nodes
	 * @param threadFrom the parent node
	 * @param threadTo the child node
	 */
	private void paintThreadLine(TreeNode threadFrom, TreeNode threadTo) {
		// set thread line stroke color
		gc.setStroke(threadLineColor);
		// get x and y middle position coordinates of both nodes
		int parentX = getXMidPos(threadFrom),
			parentY = getYMidPos(threadFrom),
			childX = getXMidPos(threadTo),
			childY = getYMidPos(threadTo);
		
		int heightDiff = (parentY-childY) + nodeHeight;
		// Start path 
		gc.beginPath();
		// set starting point
	    gc.moveTo(getXMidPos(threadFrom), getYMidPos(threadFrom));
	    // Control point 1 (first two numbers), Control point 2 (second two numbers) and 
	    // end point (last two numbers)
	    gc.bezierCurveTo(parentX, parentY, parentX, parentY+heightDiff, childX, childY);
	    gc.stroke();
		gc.closePath();
		// print the line using, middle coordinates
	}
	
	/**
	 * @param t the node to paint
	 */
	private void paintNodeBody(TreeNode t) {
		//get coordinates in canvas
		int x = getXPos(t);
		int y = getYPos(t);
				
		// update stroke color if node is selected
		updateNodeColors(t);
		// fill the circle
		gc.fillOval(x, y, nodeWidth, nodeHeight);
		gc.strokeOval(x, y, nodeWidth, nodeHeight);
		
		// paint node key
		gc.setStroke(nodeTextColor);
		gc.strokeText(""+t.key(), getXMidPos(t), getYMidPos(t));
	}

	/** sets stroke color and background color before painting a given node
	 * @param t given node to choose color for
	 */
	private void updateNodeColors(TreeNode t) {
		// check if node is selected and change stroke color
		if(selectedNode!=null && selectedNode.isNotNIL() && t.isNotNIL() && t.key() == selectedNode.key()) {
			gc.setStroke(selectedNodeStrokeColor);
			gc.setFill(selectedNodeBackgroundColor);
		} else {
			gc.setStroke(nodeStrokeColor);
			gc.setFill(nodeBackgroundColor);
		}
	}
	
	public int getWidth(){ 
		return (nodeWidth + 2*hGap)*(maxX+1);
	}
	
	public int getHeight(){ 
		return (nodeHeight + 2*vGap)*(maxY+2); // leave room for threads
	}

	public int getXPos(TreeNode t){
		if(t.isNIL())
			return hGap;
		return (nodeWidth + 2*hGap)*t.getX() + hGap;
	}
	
	public int getYPos(TreeNode t){
		if(t.isNIL())
			return vGap;
		return (nodeHeight + 2*vGap)*t.getY() + vGap;
	}

	public int getXMidPos(TreeNode t){
		return getXPos(t) + nodeWidth/2;
	}
	
	public int getYMidPos(TreeNode t){
		return getYPos(t) + nodeHeight/2;
	}
	
	/*--- size methods ---*/
	
	/** calculate the y coordinate (depth) of the this tree node and all of it's subtrees 
	 * @param t the TreeNode to calculate y
	 * @param y the y coordinate of given tree node
	 */                                                                 
	void calculateY(TreeNode t, int y) {
		// if this node is NIL then size=0
		if(t == null || t.isNIL())
			return ;
		// otherwise set y to given y
		t.setY(y);
		// update max Y if necessary
		if (maxY<y)
			maxY = y;
		// otherwise if t has son's calculate their depths
		if(t.hasLeftChild()) 
			calculateY(t.getLeft(), y + 1);
		if(t.hasRightChild())
			calculateY(t.getRight(), y + 1);
	}
	/** calculate the X position of the tree node 
	 * finally returning the max depth  
	 */                                                                 
	void calculateX(TreeNode t, int x) {
		// if this node is NIL then size=0
		if(t==null||t.isNIL())
			return;
		// otherwise set x coordinate to given x
		t.setX(x);
		// update max X
		maxX = x;
		// set next node, i.e. the successor to x + 1
		calculateX(t.getSuccessor(), x + 1);
	}
	/** calculate the size of the this tree node and all of it's subtrees 
	 * finally returning the size 
	 */
	void calculateXY() {
		maxX = 0;
		maxY = 0;
		calculateY(root, 0);
		calculateX(root.getMinimum(), 0);
	}
}
