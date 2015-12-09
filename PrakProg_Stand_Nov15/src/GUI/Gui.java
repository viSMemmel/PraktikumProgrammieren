package GUI;
// Done: Exportieren der erzeugten Liste in eine .txt --> Menüreiter Exportieren in Datei

import java.awt.event.ActionListener;

// Statistik erstellen

// Unter Hilfe Kurzinfo zum Projekt - 10 Seiten Dokumentation als modales Fenster
// Bei Datei wählen prüfen, ob die Dateiendung eine TXT-Datei ist, ansonsten Fehlermeldung -->Fertig
// DESIGN ÄNDERN

// Christoph: Pfad, Balloontipps, Zweiter Button - Zurücksetzen -- ALLES FERTIG!
// zusätzlich: nur XML Files auswählbar + zweites Textfeld + Button
// Fehlend: Actionhandler fehlt noch bei "Text auf Fremdwörter prüfen"!!

//TODO: "Wählen" Button -- Menüpunkt  "Wählen" --> selber Actionhandler
//TODO: "Rotes X" Button -- Menüpunkt "Beenden" --> selber Actionhandler
//TODO: Menüpunkt Crawler evtl. Unterpunkte: - Start -Stop -Optionen: Modales Fenster welche news genau
//TODO: Deutsche Entsprechung in Kalmmern hinter dem Fremdwort ( evtl. auch  highlighten oder andere Farbe)
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import parser.Parser;
import DokumenteSucheforGUI.Search;

//Für Auflösungszwecke
import javafx.stage.Screen;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
//import DokumenteSucheforGUI.SearchAlt;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Gui extends Application {

	private String filePath;

	
	private Screen screen = Screen.getPrimary();
	private Rectangle2D bounds = screen.getVisualBounds();
	private double xWert = 0.0;
	private double yWert = 0.0;

	private TextField textField0 = new TextField(); // Wird in der Subklasse des
													// FileChoosers benötigt an
													// dieser Stelle
	private Parser parser;
	private TextArea textArea0 = new TextArea();
	private ComboBox kontaktMenue0 = new ComboBox();
	private Button button0 = new Button("Wählen");

	private Button button1 = new Button("Zurücksetzen");

	private Button button2 = new Button("Text auf Fremdwörter prüfen");
	
	private Button button3 = new Button ("XML-Liste auswählen");

	private Pane pane0 = new Pane(); // Ordnungspanele auf dem Objekte gelegt
										// werden können(auf dem der Inhalt
										// liegt). Man kann so viele Panes
										// zuordnen wie man lustig ist

	private TextField textField1 = new TextField();

	public void buttonWaehlen() {
		System.out.println("Wählen-Button ausgelöst - Return-Code (0)");

		DirectoryChooser dChooser = new DirectoryChooser();

		File dir = dChooser.showDialog(pane0.getScene().getWindow());

		filePath = dir.getAbsolutePath();
		if (dir != null) {

			textField0.setText(filePath);
			textField0.setAlignment(Pos.BASELINE_LEFT);
		}
		System.out.println(filePath);
	}

	// Fenster heißt in JavaFX "stage" (Fenster)
	public void start(final Stage stage) throws Exception {

		stage.setTitle("Die Heuristiker - Anglizismenfinder");
		stage.centerOnScreen(); // Fenster wird mittig auf dem Bildschirm
								// platziert
		stage.setHeight(500);
		stage.setWidth(600);
		stage.setResizable(false); // Nicht veränderbar
		stage.getIcons().add(new Image("file:Unbenannt.png"));

		// Standardwerte speichern
		xWert = stage.getWidth();
		yWert = stage.getHeight();

		System.out.println("x: " +xWert+ "y: " +yWert );
		
		MenuBar menueLeiste = new MenuBar();
		menueLeiste.prefWidthProperty().bind(stage.widthProperty());

		Menu datei = new Menu("Datei");
		Menu suchen = new Menu("Suchen");
		Menu ansicht = new Menu("Ansicht");
		Menu hilfe = new Menu("Hilfe");

		// Menu-Iems für DATEI
		MenuItem schliessen = new MenuItem("Beenden");
		MenuItem waehlen = new MenuItem("Wählen");
		MenuItem exportieren = new MenuItem("Exportieren");
		MenuItem statistik = new MenuItem("Statistik anzeigen");

		// MenuItem für Suchen
		MenuItem xmlDurchsuchen = new MenuItem("XML");
		MenuItem crawlerDurchsuchen = new MenuItem("Crawler");
		MenuItem urlDurchsuchen = new MenuItem("URL");

		// Menu-Items für Hilfe
		MenuItem fragezeichen = new MenuItem("Über den Anglizismenfinder");

		// MenuItem für Ansicht
		MenuItem normalbild = new MenuItem("Normalbild anzeigen");
		MenuItem vollbild = new MenuItem("Vollbild anzeigen");
		MenuItem halbbild = new MenuItem("Halbbild anzeigen");

		// Alex
		MenuItem designAendern = new MenuItem("Design ändern");

		datei.getItems().add(waehlen);
		datei.getItems().add(exportieren);
		datei.getItems().add(statistik);
		datei.getItems().add(schliessen);

		suchen.getItems().add(xmlDurchsuchen);
		suchen.getItems().add(crawlerDurchsuchen);
		suchen.getItems().add(urlDurchsuchen);

		ansicht.getItems().add(normalbild);
		ansicht.getItems().add(vollbild);
		ansicht.getItems().add(halbbild);
		ansicht.getItems().add(designAendern);

		hilfe.getItems().add(fragezeichen);

		menueLeiste.getMenus().addAll(datei, suchen, ansicht, hilfe);

		textArea0.setScrollTop(Double.MAX_VALUE);
		textArea0.setLayoutX(10);
		textArea0.setLayoutY(200);

		textArea0.setEditable(false);
		textArea0.setWrapText(true); // Automatischer Zeilenumbruch
		textArea0.setPrefSize(570, 270);
		textArea0.setText(
				"Zeile: 15 --> (...) innerhalb eines Meetings werden neue Ziele vereinb (...) -->  Sitzung, Besprechung         \n");

		// TextFeld in dem der Dateipfad der XML-Source-Datei eingetragen wird
		textField0.setLayoutX(10);
		textField0.setLayoutY(50);
		textField0.setEditable(false);
		textField0.setPrefWidth(480); // Breite des Textfeldes

		textField0.setText("Bitte XML-Datei auswählen!");
		textField0.setMaxWidth(350);

		// zentriert Text im Textfeld
		textField0.setAlignment(Pos.CENTER);

		// Textfeld zur manuellen Eingabe eines zu prüfenden Textes auf
		// Fremdwörter
		textField1.setLayoutX(10);
		textField1.setLayoutY(150);
		textField1.setEditable(true);
		textField1.setPrefWidth(480); // Breite des Textfeldes
		textField1.setText("Hier zu prüfenden Text eingeben!");
		textField1.setMaxWidth(350);
		textField1.setAlignment(Pos.CENTER);

		button0.setLayoutX(380);
		button0.setLayoutY(45);
		button0.setPrefSize(80, 30);

		button1.setLayoutX(480);
		button1.setLayoutY(45);
		button1.setPrefSize(100, 30);

		button2.setLayoutX(380);
		button2.setLayoutY(145);
		button2.setPrefSize(200, 30);
		
		button3.setLayoutX(380);
		button3.setLayoutY(95);
		button3.setPrefSize(200, 30);

		// Combobox

		ObservableList<String> auswahl0 = FXCollections.observableArrayList("Noch kein XML hinterlegt");
		kontaktMenue0.setEditable(false);
	//	kontaktMenue0.getSelectionModel().select("Stefan");
		kontaktMenue0.setLayoutX(10);
		kontaktMenue0.setLayoutY(100);
		kontaktMenue0.setEditable(false);
		kontaktMenue0.setPrefWidth(350);

		String tooltiptext = "Bitte wählen Sie die XML Datei aus!";
		Tooltip tooltip = new Tooltip();
		tooltip.setText(tooltiptext);
		button0.setTooltip(tooltip);

		fragezeichen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				// Klasse Modaler Dialog
				ModalerDialog mDialog0 = new ModalerDialog();

				mDialog0.showAndWait(); // BLOCKIERT

				System.out.println("Button schließen im modalen Dialog gedrückt.");
			}
		});
		
		schliessen.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// System.exit(0);

				Alert alarm = new Alert(AlertType.CONFIRMATION);
				alarm.setTitle("Bestätigung");
				alarm.setHeaderText("Wollen Sie das Programm wirklich beenden?");
				alarm.setContentText("");

				Optional<ButtonType> ergebnis = alarm.showAndWait();
				if (ergebnis.get() == ButtonType.OK) {
					System.exit(0);

				} else {
					// ... user chose CANCEL or closed the dialog
				}
				alarm.close();

			}
		});

		waehlen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				buttonWaehlen();

			}
		});

		// Actionhändler zum Wählen von XML Source-Datei
		button0.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				buttonWaehlen(); // Zur Vermeidung von redundantem Code!!
			}
		});

		// Actionhandler für Button "Zurücksetzen"
		button1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				setBack();

			}

			private void setBack() {
				textArea0.setText("");
				textField0.setText("Bitte XML-Datei auswählen!");
				textField0.setAlignment(Pos.CENTER);
				textField1.setText("Hier zu prüfenden Text eingeben!");
				textField1.setMaxWidth(350);
				textField1.setAlignment(Pos.CENTER);
				String test;
				test=kontaktMenue0.getSelectionModel().getSelectedItem().toString();
				System.out.println(test);
			}
		});
		// actionhandler suche starten
		button2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				System.out.println("Button 2 gedrueckt");
				String Suchwort = null;
				String Output = null;
				kontaktMenue0.setEditable(true);
				Suchwort = textField1.getText();
				
				kontaktMenue0.getItems();
				
				String[] SuchAr = Suchwort.split(" ");
				try {
					System.out.println("KM"+ kontaktMenue0);
					
					Search suche = new Search(SuchAr, filePath);
					Output = suche.find();

				} catch (Exception e) {
					Output = e.getMessage();
				} finally {
					textArea0.setText(Output);
				}

			}
		});
		
		//Actionhändler zum auswählen von einer einzelnen XML Liste button3
		button3.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				System.out.println("Wählen-Button ausgelöst - Return-Code (0)");

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Resource File (only XML)");
				
				//nur XML Dateien erlaubt bei der Eingabe
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showOpenDialog(pane0.getScene().getWindow());
				 
				//kompletten Pfadnamen der ausgewählten Datei in Textfeld anzeigen
				String filePath = file.getAbsolutePath();
				parser = new Parser(filePath);

				kontaktMenue0.setEditable(true);
				try{
				List<String> Kinder = parser.getKids();
				ObservableList <String> oList =FXCollections.observableArrayList(Kinder);
				kontaktMenue0.setItems(oList);}
				catch(Exception e){
					System.out.println("Gehtnicht");
				}
				if (file != null) {

						System.out.println(filePath);
//						textField0.setText(filePath);
//						textField0.setAlignment(Pos.BASELINE_LEFT);
						}
			}
			
			
		});

		// Actionhandler zum exportieren der Liste als txt Datei!
		exportieren.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				// Öffnet den SaveDialog um die erzielte Suche zu speichern
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save File as .txt");
				fileChooser.setInitialFileName("Fremdwortsuche.txt");

				// Datei kann nur als txt gespeichert werden
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.xml)", "*.xml");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showSaveDialog(stage);

				// schreibt den String aus der TextArea in den zu speichernden
				// File
				if (file != null) {
					try {

						String txt = textArea0.getText();
						file.createNewFile();
						FileWriter writer = new FileWriter(file);
						writer.write(txt);
						writer.close();

					} catch (IOException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		});

		// Halbbild Anzeige
		halbbild.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				stage.setHeight(bounds.getHeight()-10);
				stage.setWidth(bounds.getWidth()/2);
				stage.centerOnScreen();
				xWert = stage.getWidth();
				yWert = stage.getHeight();
				
				textArea0.setLayoutX((xWert/2)-290);
				textField0.setLayoutX((xWert/2)-290);
				textField1.setLayoutX((xWert/2)-290);
				
				button0.setLayoutX((xWert/2)+80);
				button1.setLayoutX((xWert/2)+180);
				button2.setLayoutX((xWert/2)+80);
				button3.setLayoutX((xWert/2)+80);
				
				kontaktMenue0.setLayoutX((xWert/2)-290);
				
				textArea0.setPrefSize(570, 490);
				System.out.println("Halbbild.");
			}
		});

		// Vollbild Anzeige
		vollbild.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				stage.setX(bounds.getMinX()+5);
				stage.setY(bounds.getMinY()+5);
				stage.setWidth(bounds.getWidth()-5);
				stage.setHeight(bounds.getHeight()-5);

				System.out.println("Vollbild.");
				xWert = stage.getWidth();
				yWert = stage.getHeight();

				textField0.setLayoutX((xWert/2)-290);
				textField1.setLayoutX((xWert/2)-290);
				
				button0.setLayoutX((xWert/2)+80);
				button1.setLayoutX((xWert/2)+180);
				button2.setLayoutX((xWert/2)+80);
				button3.setLayoutX((xWert/2)+80);
				
				kontaktMenue0.setLayoutX((xWert/2)-290);
				
				textArea0.setLayoutX(20);
				textArea0.setPrefSize((xWert-50), 470);
			
				System.out.println("x: " +xWert+ "y: " +yWert );
			}
		});

		// Actionhändler zum Wählen von XML Source-Datei
		normalbild.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				stage.setHeight(500);
				stage.setWidth(600);
				stage.centerOnScreen();
				
				textArea0.setLayoutX(10);
				textArea0.setLayoutY(200);
				textArea0.setPrefSize(570, 270);
				
				textField0.setLayoutX(10);
				textField0.setLayoutY(50);
				textField0.setMaxWidth(350);

				// Textfeld zur manuellen Eingabe eines zu prüfenden Textes auf
				// Fremdwörter
				textField1.setLayoutX(10);
				textField1.setLayoutY(150);
				textField1.setAlignment(Pos.CENTER);

				button0.setLayoutX(380);
				button0.setLayoutY(45);
				
				button1.setLayoutX(480);
				button1.setLayoutY(45);
		
				button2.setLayoutX(380);
				button2.setLayoutY(145);
			
				kontaktMenue0.setLayoutX(10);
				kontaktMenue0.setLayoutY(100);
				
			}
		});

		// Actionhandler Crawleroptionspunkt crawlerDurchsuchen
		crawlerDurchsuchen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				ModalerDialogCrawler mDialog1 = new ModalerDialogCrawler();

				mDialog1.showAndWait();

			}
		});

		pane0.getChildren().addAll(textArea0, button0, button1, button3,  textField1, button2, menueLeiste, textField0, kontaktMenue0);

		Scene scene = new Scene(pane0); // Fensterinhalt in dem ein Panel gelegt
										// wird
		stage.setScene(scene); // Fensterinhalt aufs Fenster legen
		stage.show(); // Fenster sichtbar machen
		
		// CSS von Alex (Standardinitialisierung und Knopf zum wechseln der Designs
		
		scene.getStylesheets().clear();
		scene.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());
		
		designAendern.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if(scene.getStylesheets().get(0).contains("caspian")){
					scene.getStylesheets().clear();
					scene.getStylesheets().add(Gui.class.getResource("notransparency.css").toExternalForm());
				}
				else if(scene.getStylesheets().get(0).contains("notransparency")) {
					scene.getStylesheets().clear();
					scene.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());
				}
			}
		});
		
	}

	public static void main(String[] args) {

		launch(args); // Anwendung wird gestartet und Startmethode wird
						// aufgerufen

	}
}

// GEHÖRT IN EINE EXTRIGE KLASSE
class ModalerDialog extends Stage {

	public ModalerDialog() {

		super();
		setTitle("Über den Anglizismenfinder");
		initModality(Modality.APPLICATION_MODAL);

		TextArea textArea1 = new TextArea();
		textArea1.setEditable(false);
		textArea1.setWrapText(true); // Automatischer Zeilenumbruch
		textArea1.setPrefSize(574, 450);

		textArea1.setText(
				"LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER LEER \n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nZEILENUMBRUCH-TEST");

		Button schliessen1 = new Button("Schließen"); // Schließt modales
														// Femster
		schliessen1.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				close();
			}
		});

		BorderPane pane1 = new BorderPane();

		pane1.setBottom(schliessen1);
		pane1.setTop(textArea1);

		Scene scene1 = new Scene(pane1, 600, 500);
		setScene(scene1);

		scene1.getStylesheets().clear();
		scene1.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());

	}
}

class ModalerDialogCrawler extends Stage {

	public ModalerDialogCrawler() {
		super();
		setTitle("Crawler Optionen");
		initModality(Modality.APPLICATION_MODAL);

		Pane pane2 = new Pane();
		Scene scene2 = new Scene(pane2, 350, 250);
		setScene(scene2);

		Button starten = new Button("Crawler starten");
		starten.setLayoutX(20);
		starten.setLayoutY(80);
		starten.setPrefSize(120, 30);

		Button stoppen = new Button("Crawler stoppen");
		stoppen.setLayoutX(190);
		stoppen.setLayoutY(80);
		stoppen.setPrefSize(120, 30);

		Text text = new Text(10, 50, "Hier können Sie den Crawler starten und stoppen");

		text.setFont(Font.font("Verdana", FontWeight.BOLD, 12));

		TextArea crawler = new TextArea("Crawler läuft nicht");
		crawler.setLayoutX(20);
		crawler.setLayoutY(140);
		crawler.setPrefSize(290, 100);
		crawler.setEditable(false);
		crawler.setScrollTop(Double.MAX_VALUE);

		// starten Actionahdler
		starten.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				crawler.setText(crawler.getText() + "\nCrawler gestartet");

			}
		});

		// stoppen Actionhandler
		stoppen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				crawler.setText(crawler.getText() + "\nCrawler wieder gestoppt");

			}
		});

		pane2.getChildren().addAll(text, crawler, starten, stoppen);

		scene2.getStylesheets().clear();
		scene2.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());

	}

}
