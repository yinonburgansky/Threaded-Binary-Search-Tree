package application;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.Scanner;


public class MainLayoutController {
	
	// Layout and controls
	// text fields
	@FXML
	public TextField keyText, dataText;
	// text areas
	@FXML
	TextArea multiInputText, outputText;
	// Buttons
	@FXML
	Button  addButton, 
			searchButton, 
			deleteButton, 
			prevButton, 
			nextButton, 
			minButton, 
			maxButton,
			medianButton,
			preOrderButton,
			inOrderButton,
			postOrderButton,
			loadFromFileButton,
			runInputButton,
			clearInputButton,
			clearOutputButton,
			clearSelectedButton;
	// canvas
	@FXML
	Canvas treeCanvas;
	@FXML
	StackPane canvasContainer;
	// labels
	@FXML
	Label selectedText; 
	
	// the main tree of the application
	MainApplication app;
	// AppData with current State
	AppData state;
	private Stage primaryStage;
	// the file chooser
	private FileChooser fileChooser;
	
	@FXML
    public void initialize() {
		// initiate application object
		app = new MainApplication(treeCanvas);
		// initiate state from application
		state = app.getState();
		// initiate file chooser
		fileChooser = new FileChooser();
		fileChooser.setTitle("Load batch file actions");
		fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
		handleAppData(state);
	}
	
	// set primary stage
	public void setStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	
	/** handles an AppData object
	 */
	public void handleAppData(AppData newState) {
		this.state = newState;
		// clear text fields and append to scroll down
		multiInputText.setText("");
		outputText.setText("");
		multiInputText.appendText(state.getInput());
		outputText.appendText(state.getOutput());
		// fix the size 
		outputText.autosize();
		multiInputText.autosize();
		// outputText.setScrollTop(Double.MAX_VALUE);
		// multiInputText.setScrollTop(Double.MAX_VALUE);
		
		// set selected text field
		selectedText.setText(state.getSelectedText());
	}
	
	/** returns AppData object current state
	 */
	public AppData getState() {
		return state.setKeyString(keyText.getText())
				    .setData(dataText.getText())
				    .setInput(multiInputText.getText());
	}
	
	public void addButtonClick() {
		handleAppData(app.insert(getState()));
	}
	
	public void searchButtonClick() {
		handleAppData(app.search(getState()));
	}
	
	public void deleteButtonClick() {
		handleAppData(app.delete(getState()));
	}
	
	public void preOrderScanClick() {
		handleAppData(app.preOrderScan(getState()));
	}
	
	public void inOrderScanClick() {
		handleAppData(app.inOrderScan(getState()));
	}
	
	public void postOrderScanClick() {
		handleAppData(app.postOrderScan(getState()));
	}
	
	public void minButtonClick() {
		handleAppData(app.min(getState()));
	}
	
	public void maxButtonClick() {
		handleAppData(app.max(getState()));
	}
	
	public void medianButtonClick() {
		handleAppData(app.median(getState()));
	}
	
	public void prevButtonClick() {
		handleAppData(app.prev(getState()));
	}
	
	public void nextButtonClick() {
		handleAppData(app.next(getState()));
	}
	
	public void clearSelectedButtonClick() {
		handleAppData(app.clearSelected(getState()));
	}
	
	public void clearInputButtonClick() {
		handleAppData(app.clearInput(getState()));
	}
	
	public void clearOutputButtonClick() {
		handleAppData(app.clearOutput(getState()));
	}
	
	public void runInputButtonClick() {
		handleAppData(app.runInput(getState()));
	}
	
	public void loadFromFileButtonClick() {
		state = getState();
		File chosenFile = fileChooser.showOpenDialog(primaryStage);
		if(chosenFile == null)
		 {
			 handleAppData(app.setState(state.appendToOutput("File was not chosen")));
			 return;
		}
		if(!chosenFile.canRead()) {
			handleAppData(app.setState(state.appendToOutput("Cannot open the chosen file")));
			return ;
		}
		// otherwise the user chose a file 
		// scan it and add to state
		try (Scanner sc = new Scanner(chosenFile, "UTF-8")) {
			sc.useDelimiter("$^"); // regex matching nothing - read all file
			this.state.setInput(sc.next())
				 .clearOutput()
				 .appendToOutput("The file was loaded successfuly");
			handleAppData(state);
			runInputButtonClick();
			sc.close();
		} catch (Exception e) {
			handleAppData(app.setState(state.appendToOutput("An error occourd ")));
		}
	}	
}
