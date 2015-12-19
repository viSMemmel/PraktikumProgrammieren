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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import parser.Parser;
import DokumenteSucheforGUI.Search;
import crawler.*;

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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
	
	private static String filePath;
	private String filePath2;

	private Screen screen = Screen.getPrimary();
	private Rectangle2D bounds = screen.getVisualBounds();
	private double xWert = 0.0;
	private double yWert = 0.0;

	private TextField textField0 = new TextField(); // Wird in der Subklasse des FileChoosers benötigt an dieser Stelle

	private static Parser parser;
	private TextArea textArea0 = new TextArea();
	private static ComboBox<String> kontaktMenue0 = new ComboBox();
	private Button button0 = new Button("Wählen");

	private Button button1 = new Button("Zurücksetzen");

	private Button button2 = new Button("Text auf Fremdwörter prüfen");

	private Button button3 = new Button("XML-Liste auswählen");

	private Button button4 = new Button("Datei-Suche");
	
	private Button button5 = new Button("Crawler-Suche");
	
	private Button button6 = new Button("Ordner-Suche");
	
	private Button button7 = new Button("Website-Suche");
	
	private Button starten = new Button("Crawler starten");
	
	private Button stoppen = new Button("Crawler stoppen");
	
	private RadioButton radio1 = new RadioButton();
	
	private RadioButton radio2 = new RadioButton();
		
	private Pane pane0 = new Pane(); 	// Grundpane

	private TextField textField1 = new TextField();
	
	private TextField textField2 = new TextField();

	public void buttonWaehlen() {
		System.out.println("Wählen-Button ausgelöst - Return-Code (0)");

		try{
			DirectoryChooser dChooser = new DirectoryChooser();

			File dir = dChooser.showDialog(pane0.getScene().getWindow());

			filePath = dir.getAbsolutePath();
			if (dir != null) {

				textField0.setText(filePath);
				textField0.setAlignment(Pos.BASELINE_LEFT);
			}
			System.out.println(filePath);
		} catch(NullPointerException npex){
			System.out.println("Fenster wurde über Microsofts Schließen-/Abbrechen-Button geschlossen");
		}
	}

	// Fenster heißt in JavaFX "stage" (Fenster)
	public void start(final Stage stage) throws Exception {

		stage.setTitle("Die Heuristiker - Anglizismenfinder");
		stage.centerOnScreen(); // Fenster wird mittig auf dem Bildschirm
								// platziert
		stage.setHeight(525);
		stage.setWidth(600);
		stage.setResizable(false); // Nicht veränderbar
		stage.getIcons().add(new Image("file:Unbenannt.png"));

		// Standardwerte speichern
		xWert = stage.getWidth();
		yWert = stage.getHeight();

		System.out.println("x: " + xWert + "y: " + yWert);

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
		textArea0.setLayoutY(220);

		textArea0.setEditable(false);
		textArea0.setWrapText(true); // Automatischer Zeilenumbruch
		textArea0.setPrefSize(570, 270);
		textArea0.setText(
				"Zeile: 15 --> (...) innerhalb eines Meetings werden neue Ziele vereinb (...) -->  Sitzung, Besprechung         \n");

		// TextFeld in dem der Dateipfad der XML-Source-Datei eingetragen wird
		textField0.setLayoutX(10);
		textField0.setLayoutY(85);
		textField0.setEditable(true);
		textField0.setPrefWidth(480); // Breite des Textfeldes

		textField0.setText("Bitte XML-Datei auswählen!");
		textField0.setMaxWidth(350);

		// zentriert Text im Textfeld
		textField0.setAlignment(Pos.CENTER);

		// Textfeld zur manuellen Eingabe eines zu prüfenden Textes auf
		// Fremdwörter
		textField1.setLayoutX(40);
		textField1.setLayoutY(175);
		textField1.setEditable(true);
		textField1.setPrefWidth(320); // Breite des Textfeldes
		textField1.setText("Hier zu prüfenden Text eingeben!");
		textField1.setMaxWidth(350);
		textField1.setAlignment(Pos.CENTER);
		
		textField2.setLayoutX(10);
		textField2.setLayoutY(85);
		textField2.setEditable(true);
		textField2.setPrefWidth(480); // Breite des Textfeldes
		textField2.setText("Hier zu prüfende URL eingeben!");
		textField2.setMaxWidth(350);
		textField2.setAlignment(Pos.CENTER);

		button0.setLayoutX(380);
		button0.setLayoutY(80);
		button0.setPrefSize(80, 30);

		button1.setLayoutX(480);
		button1.setLayoutY(80);
		button1.setPrefSize(100, 30);

		button2.setLayoutX(380);
		button2.setLayoutY(170);
		button2.setPrefSize(200, 30);

		button3.setLayoutX(380);
		button3.setLayoutY(125);
		button3.setPrefSize(200, 30);
		
		button4.setLayoutX(10);
		button4.setLayoutY(35);
		button4.setPrefSize(140, 30);
		
		button5.setLayoutX(153.3);
		button5.setLayoutY(35);
		button5.setPrefSize(140, 30);
		
		button6.setLayoutX(296.7);
		button6.setLayoutY(35);
		button6.setPrefSize(140, 30);
		
		button7.setLayoutX(440);
		button7.setLayoutY(35);
		button7.setPrefSize(140, 30);
		
		starten.setLayoutX(10);
		starten.setLayoutY(80);
		starten.setPrefSize(283.3, 30);
		
		stoppen.setLayoutX(296.7);
		stoppen.setLayoutY(80);
		stoppen.setPrefSize(283.3, 30);
		
		final ToggleGroup group = new ToggleGroup();
		
		radio1.setLayoutX(15);
		radio1.setLayoutY(132);
		radio1.setToggleGroup(group);
		radio1.setSelected(true);
		
		radio2.setLayoutX(15);
		radio2.setLayoutY(178);
		radio2.setToggleGroup(group);

		// Combobox

		ObservableList<String> auswahl0 = FXCollections.observableArrayList("Noch kein XML hinterlegt");
		kontaktMenue0.setEditable(false);
		// kontaktMenue0.getSelectionModel().select("Stefan");
		kontaktMenue0.setLayoutX(40);
		kontaktMenue0.setLayoutY(130);
		kontaktMenue0.setEditable(false);
		kontaktMenue0.setPrefWidth(320);

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
				String selectedItem;
				selectedItem = kontaktMenue0.getSelectionModel().getSelectedItem().toString();
				//System.out.println(selectedItem);

				List<String> test;
				List<String> test2;

				Parser p = new Parser(filePath2);
				System.out.println(filePath2);
				test = p.getKids();
				test2=p.getRetAr(test.indexOf(selectedItem));
				String suchwort = null;
				for (String temp : test2) {
					suchwort+= " "+temp;
				}
				String speicher=suchwort.substring(5);
				textField1.setText(speicher);
				textArea0.setText(textArea0.getText()+ "\n" + speicher);
				
			}
		});

		// actionhandler suche starten
		button2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				System.out.println("Button 2 gedrueckt");
				String Suchwort = "";
				String Output = "";
				kontaktMenue0.setEditable(true);
				Suchwort = textField1.getText();

				kontaktMenue0.getItems();

				String[] SuchAr = Suchwort.split(" ");
				try {
					//System.out.println("KM" + kontaktMenue0);

					Search suche = new Search(SuchAr, filePath);
					Output = suche.find();

				} catch (Exception e) {
					Output = e.getMessage();
				} finally {
					
					textArea0.setText(Output);
				}

			}
		});

		// Actionhändler zum auswählen von einer einzelnen XML Liste button3
		button3.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				try{
					System.out.println("Wählen-Button ausgelöst - Return-Code (0)");

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Open Resource File (only XML)");

					// nur XML Dateien erlaubt bei der Eingabe
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
					fileChooser.getExtensionFilters().add(extFilter);

					File file = fileChooser.showOpenDialog(pane0.getScene().getWindow());

					// kompletten Pfadnamen der ausgewählten Datei in Textfeld
					// anzeigen

					String filePath = file.getAbsolutePath();
					parser = new Parser(filePath);
					filePath2 = file.getAbsolutePath();
					kontaktMenue0.setEditable(true);
					try {
						List<String> Kinder = parser.getKids();
						ObservableList<String> oList = FXCollections.observableArrayList(Kinder);
						kontaktMenue0.setItems(oList);
					} catch (Exception e) {
						System.out.println("Gehtnicht");
					}
					if (file != null) {

						System.out.println(filePath);
						// textField0.setText(filePath);
						// textField0.setAlignment(Pos.BASELINE_LEFT);
					}

				} catch(NullPointerException npex){
					System.out.println("Fenster wurde über Microsofts Schließen-/Abbrechen-Button geschlossen");
				}
			}
		});

		//Button um auf Dateisuche umzuschalten
		button4.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				pane0.getChildren().addAll(textArea0, textField1, radio1, radio2, button0, button1, button3, button2, button4, button5, button6, button7, menueLeiste, textField0, kontaktMenue0);
			}
		});
		
		//Button um auf Crawlersuche umzuschalten
		button5.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				pane0.getChildren().addAll(textArea0, starten, stoppen, radio1, radio2, button3, button2, button4, button5, button6, button7, menueLeiste, textField1, kontaktMenue0);	
			}
		});
		
		//Button um auf Ordnersuche umzuschalten
		button6.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				pane0.getChildren().addAll(textArea0, textField1, radio1, radio2, button0, button1, button3, button2, button4, button5, button6, button7, menueLeiste, textField0, kontaktMenue0);	
			}
		});

		//Button um auf Websitesuche umzuschalten
		button7.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				pane0.getChildren().addAll(textArea0, textField1, textField2, radio1, radio2, button3, button2, button4, button5, button6, button7, menueLeiste, kontaktMenue0);
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

				stage.setHeight(bounds.getHeight() - 10);
				stage.setWidth(bounds.getWidth() / 2);
				stage.centerOnScreen();
				xWert = stage.getWidth();
				yWert = stage.getHeight();

				textArea0.setLayoutX((xWert / 2) - 290);
				textField0.setLayoutX((xWert / 2) - 290);
				textField1.setLayoutX((xWert / 2) - 260);
				textField2.setLayoutX((xWert / 2) - 290);

				button0.setLayoutX((xWert / 2) + 80);
				button1.setLayoutX((xWert / 2) + 180);
				button2.setLayoutX((xWert / 2) + 80);
				button3.setLayoutX((xWert / 2) + 80);
				button4.setLayoutX((xWert / 2) - 290);
				button5.setLayoutX((xWert / 2) - 147.5);
				button6.setLayoutX((xWert / 2) - 3);
				button7.setLayoutX((xWert / 2) + 139.5);
				starten.setLayoutX((xWert / 2) - 289.5);
				stoppen.setLayoutX((xWert / 2) -3.5 );
				radio1.setLayoutX((xWert / 2) - 285);
				radio2.setLayoutX((xWert / 2) - 285);
				
				kontaktMenue0.setLayoutX((xWert / 2) - 260);

				textArea0.setPrefSize(570, (yWert/1.55));
				System.out.println("Halbbild.");
			}
		});

		// Vollbild Anzeige
		vollbild.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				stage.setX(bounds.getMinX() + 5);
				stage.setY(bounds.getMinY() + 5);
				stage.setWidth(bounds.getWidth() - 5);
				stage.setHeight(bounds.getHeight() - 5);

				System.out.println("Vollbild.");
				xWert = stage.getWidth();
				yWert = stage.getHeight();

				textField0.setLayoutX((xWert / 2) - 290);
				textField1.setLayoutX((xWert / 2) - 260);
				textField2.setLayoutX((xWert / 2) - 290);

				button0.setLayoutX((xWert / 2) + 80);
				button1.setLayoutX((xWert / 2) + 180);
				button2.setLayoutX((xWert / 2) + 80);
				button3.setLayoutX((xWert / 2) + 80);
				button4.setLayoutX((xWert / 2) - 290);
				button5.setLayoutX((xWert / 2) - 147.5);
				button6.setLayoutX((xWert / 2) - 3);
				button7.setLayoutX((xWert / 2) + 139.5);
				starten.setLayoutX((xWert / 2) - 289.5);
				stoppen.setLayoutX((xWert / 2) -3.5 );
				radio1.setLayoutX((xWert / 2) - 285);
				radio2.setLayoutX((xWert / 2) - 285);

				kontaktMenue0.setLayoutX((xWert / 2) - 260);

				textArea0.setLayoutX(20);
				textArea0.setPrefSize((xWert - 50), 470);

				System.out.println("x: " + xWert + "y: " + yWert);
			}
		});

		// Actionhändler zum Wählen von XML Source-Datei
		normalbild.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				stage.setHeight(525);
				stage.setWidth(600);
				stage.centerOnScreen();

				textArea0.setLayoutX(10);
				textArea0.setLayoutY(220);
				textArea0.setPrefSize(570, 270);

				textField0.setLayoutX(10);
				textField0.setLayoutY(85);
				textField0.setMaxWidth(350);

				// Textfeld zur manuellen Eingabe eines zu prüfenden Textes auf
				// Fremdwörter
				textField1.setLayoutX(40);
				textField1.setLayoutY(175);
				textField1.setAlignment(Pos.CENTER);

				textField2.setLayoutX(10);
				textField2.setLayoutY(85);

				button0.setLayoutX(380);
				button0.setLayoutY(80);

				button1.setLayoutX(480);
				button1.setLayoutY(80);

				button2.setLayoutX(380);
				button2.setLayoutY(170);

				button3.setLayoutX(380);
				button3.setLayoutY(125);
				
				button4.setLayoutX(10);
				button4.setLayoutY(35);
				
				button5.setLayoutX(153.3);
				button5.setLayoutY(35);
				
				button6.setLayoutX(296.7);
				button6.setLayoutY(35);
				
				button7.setLayoutX(440);
				button7.setLayoutY(35);
				
				starten.setLayoutX(10);
				starten.setLayoutY(80);
				
				stoppen.setLayoutX(296.7);
				stoppen.setLayoutY(80);
				
				radio1.setLayoutX(15);
				radio1.setLayoutY(132);
				
				radio2.setLayoutX(15);
				radio2.setLayoutY(178);
				
				kontaktMenue0.setLayoutX(40);
				kontaktMenue0.setLayoutY(130);

			}
		});

		// Actionhandler Crawleroptionspunkt crawlerDurchsuchen
		crawlerDurchsuchen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				ModalerDialogCrawler mDialog1 = new ModalerDialogCrawler();

				mDialog1.showAndWait();

			}
		});
		
		// Actionhandler Crawleroptionspunkt crawlerDurchsuchen
		urlDurchsuchen.setOnAction(new EventHandler<ActionEvent>() {

					public void handle(ActionEvent ae) {

						try {
							int start = 0;
							int stop = 0;
							String link = "http://www.html-seminar.de/erste-html-seite.htm";
							String urlText = "";
							// String pText = "";
							URL url = new URL(link);
							Scanner scanner = new Scanner(new InputStreamReader(url.openStream()));
							while (scanner.hasNextLine()) {

								String speicher = urlText;
								urlText = speicher + scanner.nextLine() + "\n";

							}
							String pElement;

							String searchString = "<p ";
							String searchString3 = "<p>";
							String searchstring2 = "</p>";
							int occurencesStart = 0;
							int occurencesStop = 0;
							List<Integer> startStelle = new ArrayList<Integer>();
							List<Integer> stopStelle = new ArrayList<Integer>();

							if (0 != searchString.length()) {
								for (int index = urlText.indexOf(searchString, 0); index != -1; index = urlText
										.indexOf(searchString, index + 1)) {

									startStelle.add(index);
									occurencesStart++;
								}
							}
							if (0 != searchString3.length()) {
								for (int index = urlText.indexOf(searchString3, 0); index != -1; index = urlText
										.indexOf(searchString3, index + 1)) {

									startStelle.add(index);
									occurencesStart++;
								}
							}
							if (0 != searchstring2.length()) {
								for (int index = urlText.indexOf(searchstring2, 0); index != -1; index = urlText
										.indexOf(searchstring2, index + 1)) {

									stopStelle.add(index);
									occurencesStop++;
								}
							}

							System.out.println("Anzahl Starttags: " + occurencesStart);
							for (int i = 0; i < startStelle.size(); i++) {
								System.out.println("Stelle: " + startStelle.get(i));
							}

							System.out.println("Anzahl an Endtags: " + occurencesStop);
							for (int i = 0; i < stopStelle.size(); i++) {
								System.out.println("Stelle: " + stopStelle.get(i));
							}

							List<String> pTagText = new ArrayList<String>();

							for (int i = 0; i < startStelle.size() && i <  stopStelle.size() ; i++) {
								String save = "";
								String pText = "";
								while (startStelle.get(i) < stopStelle.get(i)) {

									pText += urlText.charAt(startStelle.get(i));
									save = pText;
									int speicher = startStelle.get(i);
									speicher++;
									startStelle.set(i, speicher);
								}
								pTagText.add(save);
								// System.out.println(pText);

							}

							for (int i = 0; i < pTagText.size(); i++) {
								System.out.println(pTagText.get(i));
							}
							
							DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
							Document doc = docBuilder.newDocument();
							Element rootElement = doc.createElement("Referenzdaten");
							doc.appendChild(rootElement);

							for (int i = 0; i < pTagText.size(); i++) {
								Element letter = doc.createElement("Datensatz");
								rootElement.appendChild(letter);
								Element name = doc.createElement("name");
								name.appendChild(doc.createTextNode((pTagText.get(i))));
								letter.appendChild(name);
							}
							// Element letter = doc.createElement("letter");
							// letter.appendChild(doc.createTextNode(pTagText.get(0)));
							// rootElement.appendChild(letter);

							TransformerFactory transformerFactory = TransformerFactory.newInstance();
							Transformer transformer = transformerFactory.newTransformer();
							DOMSource source = new DOMSource(doc);
							StreamResult result = new StreamResult(new File(
									"C:/Users/Christoph/Studium/Semester/WS2015/eclipse_Workspace/PrakProg31_16_12_2015/WebText.xml"));
							transformer.transform(source, result);

						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (TransformerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
				});

		pane0.getChildren().addAll(textArea0, textField1, radio1, radio2, button0, button1, button3, button2, button4, button5, button6, button7, menueLeiste, textField0,
				kontaktMenue0);
		pane0.setVisible(true);		
		
		Scene scene = new Scene(pane0); // Fensterinhalt in dem ein Panel gelegt
										// wird
		stage.setScene(scene); // Fensterinhalt aufs Fenster legen
		stage.show(); // Fenster sichtbar machen

		// CSS von Alex (Standardinitialisierung und Knopf zum wechseln der
		// Designs

		scene.getStylesheets().clear();
		scene.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());

		designAendern.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (scene.getStylesheets().get(0).contains("caspian")) {
					scene.getStylesheets().clear();
					scene.getStylesheets().add(Gui.class.getResource("notransparency.css").toExternalForm());
				} else if (scene.getStylesheets().get(0).contains("notransparency")) {
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
// Ab hier nur noch Modale Dialoge
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

		TextArea crawler = new TextArea("Crawler läuft nicht \n Nach stoppen des Crawlers wird das modale Fenster geschlossen");
		crawler.setLayoutX(20);
		crawler.setLayoutY(140);
		crawler.setPrefSize(290, 100);
		crawler.setEditable(false);
		crawler.setScrollTop(Double.MAX_VALUE);
		
		
		try {
			RSSThread.setCurrdir(new java.io.File(".").getCanonicalPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RSSThread.setMessageFile(new ServerLogfile(RSSThread.getCurrdir() + "/messages.log"));
		RSSThread.setErrorFile(new ServerLogfile(RSSThread.getCurrdir() + "/errors.log"));
		
		RSSThread.setTerminate(new File(RSSThread.getCurrdir() + "/stop"));
		String configFile = RSSThread.getCurrdir() + "/configuration.properties";
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(configFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RSSThread.archivedir = properties.getProperty("archivedir");
		RSSThread.sourcedir = properties.getProperty("sourcedir");
		RSSThread.subscriptiondir = properties.getProperty("subscriptiondir");
		RSSThread.schemadir = properties.getProperty("schemadir");
		
		RSSThread thread = new RSSThread(100);
		
		// starten Actionahdler
		starten.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				crawler.setText(crawler.getText() + "\nCrawler gestartet");
				thread.start();
			}
		});

		// stoppen Actionhandler
		stoppen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
			
				crawler.setText(crawler.getText() + "\nCrawler wieder gestoppt");
				thread.interrupt();
				thread.stop();
				close();
				
				
				
			}
			
		});

		pane2.getChildren().addAll(text, crawler, starten, stoppen);

		scene2.getStylesheets().clear();
		scene2.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());
	}
}
