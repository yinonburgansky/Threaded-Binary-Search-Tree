/**
 * 
 */
package application;

/**
 * Contains data transfer between controller and main app
 *
 */
public class AppData {

	private int key;
	private String keyString,
				   data,
    			   input,
				   output,
				   selectedText;
	TreeNode selectedNode;
	Tree mainTree;

	/**
	 * 
	 */
	public AppData() {
		keyString = "";
		data = "";
		input = "";
		output = "";
		selectedText = "";
		selectedNode = TreeNode.NIL();
		mainTree = new Tree();
	}

	/**
	 * @return the key
	 */
	public int getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 * @return this object after Change
	 */
	public AppData setKey(int key) {
		this.key = key;
		return this;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
 	 * @return this object after Change
	 */
	public AppData setData(String data) {
		this.data = data;
		return this;
	}

	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}
	/**
	 * @param input the input to set
	 * @return this object after Change
	 */
	public AppData setInput(String input) {
		this.input = input;
		return this;
	}
	/** appends text to to be appended to input
	 * @param text the input to be appended
	 * @return this object after Change
	 */
	public AppData appendToInput(String text) {
		this.input += text + "\n";
		return this;
	}
	/** clears the input field
	 * @return this object after Change
	 */
	public AppData clearInput() {
		this.input = "";
		return this;
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}
	/**
	 * @param output the output to set
	 * @return this object after Change
	 */
	public AppData setOutput(String output) {
		this.output = output;
		return this;
	}
	/** appends text to to be appended to output
	 * @param text the text to to be appended to output
	 * @return this object after Change
	 */
	public AppData appendToOutput(String text) {
		this.output += text + "\n";
		return this;
	}
	/** clears the output field
	 * @return this object after Change
	 */
	public AppData clearOutput() {
		this.output = "";
		return this;
	}
	
	/**
	 * @return the selected text
	 */
	public String getSelectedText() {
		return selectedText;
	}
	/**
	 * @param selectedText the selected text to set
	 * @return this object after Change
	 */
	public AppData setSelectedText(String selectedText) {
		this.selectedText = selectedText;
		return this;
	}
	/** appends text to selected text field
	 * @param text the text to be appended
	 * @return this object after Change
	 */
	public AppData appendToSelectedText(String text) {
		this.selectedText += ", " + text;
		return this;
	}

	/**
	 * @return the keyString
	 */
	public String getKeyString() {
		return keyString;
	}
	/**
	 * @param keyString the keyString to set
	 * @return this object after Change
	 */
	public AppData setKeyString(String keyString) {
		this.keyString = keyString;
		return this;
	}

	/**
	 * @return the selectedNode
	 */
	public TreeNode getSelectedNode() {
		return selectedNode;
	}
	/**
	 * @param selectedNode the selectedNode to set
	 * @return this object after Change
	 */
	public AppData setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
		return this;
	}

	/**
	 * @return the mainTree
	 */
	public Tree getMainTree() {
		return mainTree;
	}
	/**
	 * @param mainTree the mainTree to set
	 * @return this object after Change
	 */
	public AppData setMainTree(Tree mainTree) {
		this.mainTree = mainTree;
		return this;
	}
	
	
}
