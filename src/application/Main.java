package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Main extends Application {
	private static final String APPNAME = "MyERIMALISkript IDE";
	private MaliSkript mSkript;
	//
	private TextArea textArea; // Per tu aksesuar dhe ne metodat e tjera
	private double fontSizeTA;
    private double defaultFontSizeTA;
    private static final int changeFontSizeTA = 2;
    //
	private static TableView<String[]> tableView;
	private boolean errorOrderLF = false; // Last to first - initialized as false
	//
	private MenuItem changeErrorOrder;
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);

			textArea = new TextArea();
			defaultFontSizeTA = textArea.getFont().getSize();
			fontSizeTA = defaultFontSizeTA;
			root.setCenter(textArea);
			Button code = new Button("Run New");
            Button codeNotNew = new Button("Run OnTop");
            Button totalCode = new Button("All the code");

            VBox buttonBox = new VBox(code, codeNotNew, totalCode);
            buttonBox.setSpacing(10);
            buttonBox.setPadding(new Insets(10));

            root.setRight(buttonBox);
            
            //MAYBE CHANGE
			//Label label = new Label("Rezultati");
			//root.setBottom(label);
			TextArea label = new TextArea("Rezultati");
			label.setEditable(false);
			label.setWrapText(true);
			root.setBottom(label);
			/////////////////////////////////////
			mSkript = new MaliSkript("");
			code.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					mSkript.boshatis(textArea.getText()); // MyERI not emptied
					mSkript.ekzekuto(mSkript.getKodi());
					label.setText("Rezultati\n" + mSkript.shfaqRezultati());
					if(Kompilatori.komp.kaErrore()) {
						shfaqErroret();
					}
				}
			});
			codeNotNew.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					String kodiRi = textArea.getText();
					mSkript.appendKodi(kodiRi);
					mSkript.ekzekuto(kodiRi);
					label.setText("Rezultati\n" + mSkript.shfaqRezultati());
					if(Kompilatori.komp.kaErrore()) {
						shfaqErroret();
					}
				}
			});
			totalCode.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					popUpDialog(mSkript.getKodi(), "Kodi total i ekzekutuar");
				}
			});
			//MENU-JA
			MenuBar menuBar = new MenuBar();
			//FILE
			Menu fileMenu = new Menu("File");
			MenuItem openItem = new MenuItem("Open");
			MenuItem saveItem = new MenuItem("Save");
			//DATABASE
			Menu dbMenu = new Menu("DB File");
			MenuItem openItemMyERI = new MenuItem("Open MyERI");
			MenuItem saveItemMyERI = new MenuItem("Save MyERI");
			MenuItem openItemDB = new MenuItem("Import CSV");
			MenuItem saveItemDB = new MenuItem("Export CSV");
			//VIEW
			Menu viewMenu = new Menu("View");
			MenuItem increaseFSTA = new MenuItem("Zmadho font-size te textArea");
			MenuItem decreaseFSTA = new MenuItem("Zvogelo font-size te textArea");
			MenuItem defaultFSTA = new MenuItem("Default font-size te textArea");
			changeErrorOrder = new MenuItem("Rendi i erroreve: I pari ne fillim");

			
			//HELP
			Menu helpMenu = new Menu("Help");
			MenuItem commands = new MenuItem("Komandat MaliSkript");
			MenuItem commandsERI = new MenuItem("Komandat MyERI");

			MenuItem rregulloHapesirat = new MenuItem("Rregullo Hapesirat"); //Identimi
			
			fileMenu.getItems().addAll(openItem, saveItem);
			dbMenu.getItems().addAll(openItemMyERI, saveItemMyERI, openItemDB, saveItemDB);
			viewMenu.getItems().addAll(increaseFSTA, decreaseFSTA, defaultFSTA, changeErrorOrder);
			helpMenu.getItems().addAll(rregulloHapesirat,commands, commandsERI);

			menuBar.getMenus().addAll(fileMenu, dbMenu, viewMenu, helpMenu);

			// Eventet
			openItem.setOnAction(e -> loadFile());
			saveItem.setOnAction(e -> saveFile());
			
			openItemMyERI.setOnAction(e -> loadFileMyERI());
			saveItemMyERI.setOnAction(e -> saveFileMyERI());
			openItemDB.setOnAction(e -> loadFileDB());
			saveItemDB.setOnAction(e -> saveFileDB());
			
			increaseFSTA.setOnAction(e -> increaseFontSizeTA());
			decreaseFSTA.setOnAction(e -> decreaseFontSizeTA());
			defaultFSTA.setOnAction(e -> defaultFontSizeTA());
			changeErrorOrder.setOnAction(e -> changeErrorOrder());
			commands.setOnAction(e -> showCommands());
			commandsERI.setOnAction(e -> showCommandsERI());
			rregulloHapesirat.setOnAction(e -> rregulloHapesirat());

			// Menu-ja vendoset siper
			root.setTop(menuBar);

			primaryStage.setTitle(APPNAME);
			primaryStage.show();

			//////////////////////////////////
			/*
			String[][] tabele = { { "Column 1", "Column 2", "Column 3" },
					{ "Row 1, Col 1", "Row 1, Col 2", "Row 1, Col 3" },
					{ "Row 2, Col 1", "Row 2, Col 2", "Row 2, Col 3" },
					{ "Row 3, Col 1", "Row 3, Col 2", "Row 3, Col 3" } };
			tableBuilder("TEST", tabele, tableView); // you can reuse the same table
			*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadFileMyERI() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Hap File MyERI");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"MyERI files (*ERI)", "*.eri");
		fileChooser.getExtensionFilters().add(extFilter);

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				String content = new String(Files.readAllBytes(selectedFile.toPath()));
				// I behet load ne textArea
				textArea.setText(genMyERI(content));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String genMyERI(String c) {
		StringBuilder sb = new StringBuilder();
		sb.append("db krijo databaza\ndb ekz\n").append(c).append("\nstop");
		return sb.toString();
	}
	public void saveFileMyERI() {
		//in MyERI
		//String codeThatHasBeenRuned;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ruaj File MaliSkript");
		// MaliSkript --> .mskript
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"MyERI files (*ERI)", "*.eri");
		fileChooser.getExtensionFilters().add(extFilter);

		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {
			try {
				FileWriter fileWriter = new FileWriter(selectedFile);
				fileWriter.write(deMyERI());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public String deMyERI() {
		StringBuilder sb = new StringBuilder();
		String[] rows;
		if(mSkript != null)
			rows = mSkript.getKodi().split("\n");
		else
			rows = textArea.getText().split("\n");
		boolean scanning = false;
		for(String row : rows) {
			row = row.trim();
			if(row.equalsIgnoreCase(Sintaksa.STOP.get())) {
				scanning = false;
				continue;
			} else if(row.matches("(?i)db\\s+ekz")) {
				scanning = true;
				continue;
			}
			if(scanning) {
				sb.append(row).append("\n");
			}
		}
		return sb.toString();
	}
	public static void main(String[] args) {
		launch(args);
	}

	// Funksione shtese
	public void loadFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Hap File MaliSkript");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"MaliSkript files (*" + MSFile.FILE_EXTENSION + ")", "*" + MSFile.FILE_EXTENSION);
		fileChooser.getExtensionFilters().add(extFilter);

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				String content = new String(Files.readAllBytes(selectedFile.toPath()));
				// I behet load ne textArea
				textArea.setText(content);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void saveFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ruaj File MaliSkript");
		// MaliSkript --> .mskript
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"MaliSkript files (*" + MSFile.FILE_EXTENSION + ")", "*" + MSFile.FILE_EXTENSION);
		fileChooser.getExtensionFilters().add(extFilter);

		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {
			try {
				FileWriter fileWriter = new FileWriter(selectedFile);
				fileWriter.write(textArea.getText());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void popUpDialog(String message, String messageTitle) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Dialog");
		alert.setHeaderText(messageTitle);
		alert.setResizable(true);

		TextArea textArea = new TextArea(message);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setPrefSize(600, 400);

		alert.getDialogPane().setContent(textArea);

		ButtonType copyButton = new ButtonType("Kopjo ne Clipboard");
		ButtonType saveButton = new ButtonType("Ruaj ne File");
		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(copyButton, saveButton, okButton);

		Button copyToClipboardButton = (Button) alert.getDialogPane().lookupButton(copyButton);
		Button saveToFileButton = (Button) alert.getDialogPane().lookupButton(saveButton);

		copyToClipboardButton.setOnAction(event -> {
			Clipboard clipboard = Clipboard.getSystemClipboard();
			ClipboardContent content = new ClipboardContent();
			content.putString(message);
			clipboard.setContent(content);
		});

		saveToFileButton.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Ruaj ne File");
			File selectedFile = fileChooser.showSaveDialog(null);
			if (selectedFile != null) {
				try {
					FileWriter fileWriter = new FileWriter(selectedFile);
					fileWriter.write(message);
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		alert.showAndWait();
	}

	public void rregulloHapesirat() {
		StringBuilder sb = new StringBuilder();
		String[] fix = textArea.getText().split("\n");
		int indent = 0;
		boolean afterIndent = false;
		for (String hap : fix) {
			hap = hap.stripLeading();//IMPORTANT
			if (hap.startsWith(Sintaksa.MBYLL.get())) {
				indent--;
			} else if(hap.startsWith(Sintaksa.PERNDRYSHE.get())) {
				indent--;
				afterIndent = true;
			}
			for (int i = 0; i < indent; i++) {
				sb.append("\t");
			}
			sb.append(hap).append("\n");
			if (hap.startsWith(Sintaksa.CIKLI_FOR.get()) || hap.startsWith(Sintaksa.NESE.get())
					|| hap.startsWith(Sintaksa.FUNKSION.get())) {
				indent++;

			}
			if(afterIndent) {
				afterIndent = false;
				indent++;
			}
			////////else if not so well
			
		}
		textArea.setText(sb.toString());

	}

	public void showCommands() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Help - MaliSkript");
		alert.setHeaderText("Ndihme per komandat");
		alert.setResizable(true);

		TextArea textArea = new TextArea(Sintaksa.menuNdihmese());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setPrefSize(600, 400);

		alert.getDialogPane().setContent(textArea);

		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(okButton);

		alert.showAndWait();
	}
	private void showCommandsERI() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Help - MyERI");
		alert.setHeaderText("Ndihme per komandat");
		alert.setResizable(true);

		TextArea textArea = new TextArea(MySint.menuNdihmese());
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setPrefSize(600, 400);

		alert.getDialogPane().setContent(textArea);

		ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(okButton);

		alert.showAndWait();
	}
	public static void tableBuilder(String emri, String[][] tabele, TableView<String[]> tableView) {
	    if (tableView.getColumns().isEmpty()) {
	        // Create columns
	        for (int i = 0; i < tabele[0].length; i++) {
	            TableColumn<String[], String> column = new TableColumn<>(tabele[0][i]);
	            final int colIndex = i;
	            column.setCellValueFactory(cellData -> {
	                if (cellData.getValue().length > colIndex) {
	                    return new SimpleStringProperty(cellData.getValue()[colIndex]);
	                } else {
	                    return new SimpleStringProperty("");
	                }
	            });
	            tableView.getColumns().add(column);
	        }
	    }

	    // Add rows
	    for (int i = 1; i < tabele.length; i++) {
	        tableView.getItems().add(tabele[i]);
	    }

	    // Show table
	    Scene scene = new Scene(tableView, 600, 400);
	    Stage stage = new Stage();
	    stage.setTitle("MyERI Tabela " + emri);
	    stage.setScene(scene);
	    stage.show();
	}
	//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	public void loadFileDB() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Hap File MaliSkript");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"CSV files (*CSV)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);

		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			try {
				String content = new String(Files.readAllBytes(selectedFile.toPath()));
				System.out.println(content);
				mSkript.inicioDatabaze(selectedFile.getName());
				mSkript.getDB().fromCSV(content);
				
				/*
				Object[][] o = MSFile.readCSVTable(content);
				for(int i = 0; i < o.length; i++) {
					for(int j = 0; j < o[i].length; j++) {
						System.out.print(o[i][j].toString()+ " ");
					}
					System.out.println();
				}
				*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void saveFileDB() {
		if(mSkript == null) {
			return;
		} else if(mSkript.getDB() == null) {
			return;
		}
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Ruaj File MyERI ne CSV");
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"CSV files (*CSV)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		
		fileChooser.setInitialFileName(mSkript.getDB().getEmriDatabazes());
		
		File selectedFile = fileChooser.showSaveDialog(null);
		if (selectedFile != null) {
			try {
				FileWriter fileWriter = new FileWriter(selectedFile);
				fileWriter.write(mSkript.ktheCSVMyERI());
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static TableView<String[]> getTableView() {
		return tableView;
	}

	public static void setTableView(TableView<String[]> tableView) {
		Main.tableView = tableView;
	}
	
	public void changeFontSize(int ch) {
		fontSizeTA+=ch;
		if(fontSizeTA > defaultFontSizeTA*10 || fontSizeTA < defaultFontSizeTA/3) {
			defaultFontSizeTA();
			return;
		}
        textArea.setStyle("-fx-font-size:" + fontSizeTA + "px;");
	}
	public void defaultFontSizeTA() {
		fontSizeTA = defaultFontSizeTA; //
        textArea.setStyle("-fx-font-size:" + defaultFontSizeTA + "px;");
	}
	public void increaseFontSizeTA() {
		changeFontSize(changeFontSizeTA);
	}
	public void decreaseFontSizeTA() {
		changeFontSize(-changeFontSizeTA);
	}
	
	public void shfaqErroret() {
		String err, errTitle;
		if(!errorOrderLF) {
			err = Kompilatori.komp.zbrazErroret();
			errTitle = "Erroret nga i pari qe ndodhi";
		}
		else {
			err = Kompilatori.komp.zbrazErroretMbrapsht();
			errTitle = "Erroret nga i fundit qe ndodhi";
		}
		popUpDialog(err, errTitle);
		
	}
	public void changeErrorOrder() {
		errorOrderLF = !errorOrderLF;
		if(errorOrderLF)
			changeErrorOrder.setText("Rendi i erroreve: I fundit ne fillim");
		else
			changeErrorOrder.setText("Rendi i erroreve: I pari ne fillim");
	}
}
