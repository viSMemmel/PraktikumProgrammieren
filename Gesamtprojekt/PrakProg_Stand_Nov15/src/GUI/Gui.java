
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

import org.apache.lucene.queryparser.classic.ParseException;
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
import javafx.stage.WindowEvent;

public class Gui extends Application {

	private static String filePath;
	private String filePath2;

	private boolean started = false;
	private boolean caspian = true;
	private RSSThread thread1;
	private int counter = 0;

	private Screen screen = Screen.getPrimary();
	private Rectangle2D bounds = screen.getVisualBounds();
	private double xWert = 0.0;
	private double yWert = 0.0;

	private TextField textField0 = new TextField(); // Wird in der Subklasse des
													// FileChoosers benötigt an
													// dieser Stelle

	private static Parser parser;
	private TextArea textArea0 = new TextArea();
	private static ComboBox<String> kontaktMenue0 = new ComboBox();
	private Button waehlenButton = new Button("Wählen");

	private Button zuruecksetzenButton = new Button("Zurücksetzen");

	private Button fremdwortsucheStartenButton = new Button("Text auf Fremdwörter prüfen");

	private Button xmlListeWaehlenButton = new Button("XML-Liste auswählen");

	private Button dateiSucheButton = new Button("Datei-Suche");

	private Button crawlerSucheButton = new Button("Crawler-Suche");

	private Button ordnerSucheButton = new Button("Ordner-Suche");

	private Button webseiteSucheButton = new Button("Website-Suche");

	private Button crawlerStarten = new Button("Crawler starten");

	private Button crawlerStoppen = new Button("Crawler stoppen");

	private RadioButton radioListe = new RadioButton();

	private RadioButton radioWoerter = new RadioButton();

	private Pane pane0 = new Pane(); // Grundpane

	// private Scene scene = new Scene(null);
	private TextField textField1 = new TextField();

	private TextField textField2 = new TextField();

	public void buttonWaehlenAction() {
		System.out.println("Wählen-Button ausgelöst - Return-Code (0)");

		try {
			DirectoryChooser dChooser = new DirectoryChooser();

			File dir = dChooser.showDialog(pane0.getScene().getWindow());

			filePath = dir.getAbsolutePath();
			if (dir != null) {

				textField0.setText(filePath);
				textField0.setAlignment(Pos.BASELINE_LEFT);
			}
			System.out.println(filePath);
		} catch (NullPointerException npex) {
			System.out.println("Fenster wurde über Microsofts Schließen-/Abbrechen-Button geschlossen");

		}
	}

	public void crawlerStart() {
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RSSThread.archivedir = properties.getProperty("archivedir");
		RSSThread.sourcedir = properties.getProperty("sourcedir");
		RSSThread.subscriptiondir = properties.getProperty("subscriptiondir");
		RSSThread.schemadir = properties.getProperty("schemadir");
	
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
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		     public void handle(WindowEvent t) {
		        System.exit(0);
		    }
		});

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

		waehlenButton.setLayoutX(380);
		waehlenButton.setLayoutY(80);
		waehlenButton.setPrefSize(80, 30);

		zuruecksetzenButton.setLayoutX(480);
		zuruecksetzenButton.setLayoutY(80);
		zuruecksetzenButton.setPrefSize(100, 30);

		fremdwortsucheStartenButton.setLayoutX(380);
		fremdwortsucheStartenButton.setLayoutY(170);
		fremdwortsucheStartenButton.setPrefSize(200, 30);

		xmlListeWaehlenButton.setLayoutX(380);
		xmlListeWaehlenButton.setLayoutY(125);
		xmlListeWaehlenButton.setPrefSize(200, 30);

		dateiSucheButton.setLayoutX(10);
		dateiSucheButton.setLayoutY(35);
		dateiSucheButton.setPrefSize(140, 30);

		crawlerSucheButton.setLayoutX(153.3);
		crawlerSucheButton.setLayoutY(35);
		crawlerSucheButton.setPrefSize(140, 30);

		ordnerSucheButton.setLayoutX(296.7);
		ordnerSucheButton.setLayoutY(35);
		ordnerSucheButton.setPrefSize(140, 30);

		webseiteSucheButton.setLayoutX(440);
		webseiteSucheButton.setLayoutY(35);
		webseiteSucheButton.setPrefSize(140, 30);

		crawlerStarten.setLayoutX(10);
		crawlerStarten.setLayoutY(80);
		crawlerStarten.setPrefSize(283.3, 30);

		crawlerStoppen.setLayoutX(296.7);
		crawlerStoppen.setLayoutY(80);
		crawlerStoppen.setPrefSize(283.3, 30);

		final ToggleGroup group = new ToggleGroup();

		radioListe.setLayoutX(15);
		radioListe.setLayoutY(132);
		radioListe.setToggleGroup(group);
		radioListe.setSelected(true);

		radioWoerter.setLayoutX(15);
		radioWoerter.setLayoutY(178);
		radioWoerter.setToggleGroup(group);

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
		waehlenButton.setTooltip(tooltip);

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

				buttonWaehlenAction();

			}
		});

		// Actionhändler zum Wählen von XML Source-Datei
		waehlenButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				buttonWaehlenAction(); // Zur Vermeidung von redundantem Code!!
			}
		});

		// Actionhandler für Button "Zurücksetzen"
		zuruecksetzenButton.setOnAction(new EventHandler<ActionEvent>() {
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
				// System.out.println(selectedItem);

				List<String> test;
				List<String> test2;

				Parser p = new Parser(filePath2);
				System.out.println(filePath2);
				test = p.getKids();
				test2 = p.getRetAr(test.indexOf(selectedItem));
				String suchwort = null;
				for (String temp : test2) {
					suchwort += " " + temp;
				}
				String speicher = suchwort.substring(5);
				textField1.setText(speicher);
				textArea0.setText(textArea0.getText() + "\n" + speicher);

			}
		});

		// actionhandler suche starten
		fremdwortsucheStartenButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				System.out.println("Button 2 gedrueckt");
				String suchwort = null;
				String Output = "";
				// kontaktMenue0.setEditable(true);
				// kontaktMenue0.getItems();

				if (radioWoerter.isSelected()) {
					System.out.println("radioWoerter is selected");
					suchwort = textField1.getText();

				} else {

					System.out.println("radioListe is selected");
					String selectedItem;
					selectedItem = kontaktMenue0.getSelectionModel().getSelectedItem().toString();
					List<String> test;
					List<String> test2;

					Parser p = new Parser(filePath2);
					System.out.println(filePath2);
					test = p.getKids();
					test2 = p.getRetAr(test.indexOf(selectedItem));

					for (String temp : test2) {
						suchwort += " " + temp;
					}
					String speicher = suchwort.substring(5);
					suchwort = speicher;
					textField1.setText(speicher);
					textArea0.setText(textArea0.getText() + "\n" + speicher);

				}

				String[] SuchAr = suchwort.split(" ");
				Search suche = new Search(SuchAr, filePath);
				try {
					Output = suche.find();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textArea0.setText(Output);

			}
		});

		// Actionhändler zum auswählen von einer einzelnen XML Liste button3
		xmlListeWaehlenButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				try {
					System.out.println("Wählen-Button ausgelöst - Return-Code (0)");

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Open Resource File (only XML)");

					// nur XML Dateien erlaubt bei der Eingabe
					FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)",
							"*.xml");
					fileChooser.getExtensionFilters().add(extFilter);

					File file = fileChooser.showOpenDialog(pane0.getScene().getWindow());

					// kompletten Pfadnamen der ausgewählten Datei in Textfeld
					// anzeigen

					String filePath = file.getAbsolutePath();
					parser = new Parser(filePath);
					filePath2 = file.getAbsolutePath();
					System.out.println(filePath2);
					kontaktMenue0.setEditable(false);
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

				} catch (NullPointerException npex) {
					System.out.println("Fenster wurde über Microsofts Schließen-/Abbrechen-Button geschlossen");
				}
			}
		});

		crawlerStart();
		RSSThread thread1 = new RSSThread(100);
		counter = 0;
		// Actionhandler zum Starten des Crawlers direkt in der Gui
		crawlerStarten.setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent ae) {
				//textArea0.setText("Crawler bereit zum Starten");
				System.out.println(counter);
				if (counter == 0) {

					thread1.start();
				} else {
					thread1.resume();
				}
				textArea0.setText(textArea0.getText()+"\nCrawler wurde gestartet");
				counter++;
				System.out.println(counter);
				
				datei.setDisable(true);
				suchen.setDisable(true);
				ansicht.setDisable(true);
				hilfe.setDisable(true);
				
				xmlListeWaehlenButton.setDisable(true);
				dateiSucheButton.setDisable(true);
				ordnerSucheButton.setDisable(true);
				webseiteSucheButton.setDisable(true);
				fremdwortsucheStartenButton.setDisable(true);
				

			}
		});
		
		crawlerStoppen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				
				started = false;
				textArea0.setText(textArea0.getText() + "\nCrawler wurde gestoppt");

				thread1.suspend();
				xmlListeWaehlenButton.setDisable(false);
				dateiSucheButton.setDisable(false);
				ordnerSucheButton.setDisable(false);
				webseiteSucheButton.setDisable(false);
				fremdwortsucheStartenButton.setDisable(false);
				datei.setDisable(false);
				suchen.setDisable(false);
				ansicht.setDisable(false);
				hilfe.setDisable(false);
			}
			
			
		});

		// Button um auf Dateisuche umzuschalten
		dateiSucheButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				
				pane0.getChildren().addAll(textArea0, textField1, radioListe, radioWoerter, waehlenButton,
						zuruecksetzenButton, xmlListeWaehlenButton, fremdwortsucheStartenButton, dateiSucheButton,
						crawlerSucheButton, ordnerSucheButton, webseiteSucheButton, menueLeiste, textField0,
						kontaktMenue0);

			}
			
			
		});

		// Button um auf Crawlersuche umzuschalten
		crawlerSucheButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				
				pane0.getChildren().addAll(textArea0, crawlerStarten, crawlerStoppen, radioListe, radioWoerter,
						xmlListeWaehlenButton, fremdwortsucheStartenButton, dateiSucheButton, crawlerSucheButton,
						ordnerSucheButton, webseiteSucheButton, menueLeiste, textField1, kontaktMenue0);
				textArea0.setText("Wenn der Crawler läuft muss dieser beendet werden bevor was anderes gemacht werden kann!"
						+ "\nCrawler bereit zum starten");
				
			}
		});

		// Button um auf Ordnersuche umzuschalten
		ordnerSucheButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				pane0.getChildren().addAll(textArea0, textField1, radioListe, radioWoerter, waehlenButton,
						zuruecksetzenButton, xmlListeWaehlenButton, fremdwortsucheStartenButton, dateiSucheButton,
						crawlerSucheButton, ordnerSucheButton, webseiteSucheButton, menueLeiste, textField0,
						kontaktMenue0);
			}
		});

		// Button um auf Websitesuche umzuschalten
		webseiteSucheButton.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				pane0.getChildren().clear();
				pane0.getChildren().addAll(textArea0, textField1, textField2, radioListe, radioWoerter,
						xmlListeWaehlenButton, fremdwortsucheStartenButton, dateiSucheButton, crawlerSucheButton,
						ordnerSucheButton, webseiteSucheButton, menueLeiste, kontaktMenue0);
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

				waehlenButton.setLayoutX((xWert / 2) + 80);
				zuruecksetzenButton.setLayoutX((xWert / 2) + 180);
				fremdwortsucheStartenButton.setLayoutX((xWert / 2) + 80);
				xmlListeWaehlenButton.setLayoutX((xWert / 2) + 80);
				dateiSucheButton.setLayoutX((xWert / 2) - 290);
				crawlerSucheButton.setLayoutX((xWert / 2) - 147.5);
				ordnerSucheButton.setLayoutX((xWert / 2) - 3);
				webseiteSucheButton.setLayoutX((xWert / 2) + 139.5);
				crawlerStarten.setLayoutX((xWert / 2) - 289.5);
				crawlerStoppen.setLayoutX((xWert / 2) - 3.5);
				radioListe.setLayoutX((xWert / 2) - 285);
				radioWoerter.setLayoutX((xWert / 2) - 285);

				kontaktMenue0.setLayoutX((xWert / 2) - 260);

				textArea0.setPrefSize(570, (yWert / 1.55));
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

				waehlenButton.setLayoutX((xWert / 2) + 80);
				zuruecksetzenButton.setLayoutX((xWert / 2) + 180);
				fremdwortsucheStartenButton.setLayoutX((xWert / 2) + 80);
				xmlListeWaehlenButton.setLayoutX((xWert / 2) + 80);
				dateiSucheButton.setLayoutX((xWert / 2) - 290);
				crawlerSucheButton.setLayoutX((xWert / 2) - 147.5);
				ordnerSucheButton.setLayoutX((xWert / 2) - 3);
				webseiteSucheButton.setLayoutX((xWert / 2) + 139.5);
				crawlerStarten.setLayoutX((xWert / 2) - 289.5);
				crawlerStoppen.setLayoutX((xWert / 2) - 3.5);
				radioListe.setLayoutX((xWert / 2) - 285);
				radioWoerter.setLayoutX((xWert / 2) - 285);

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

				waehlenButton.setLayoutX(380);
				waehlenButton.setLayoutY(80);

				zuruecksetzenButton.setLayoutX(480);
				zuruecksetzenButton.setLayoutY(80);

				fremdwortsucheStartenButton.setLayoutX(380);
				fremdwortsucheStartenButton.setLayoutY(170);

				xmlListeWaehlenButton.setLayoutX(380);
				xmlListeWaehlenButton.setLayoutY(125);

				dateiSucheButton.setLayoutX(10);
				dateiSucheButton.setLayoutY(35);

				crawlerSucheButton.setLayoutX(153.3);
				crawlerSucheButton.setLayoutY(35);

				ordnerSucheButton.setLayoutX(296.7);
				ordnerSucheButton.setLayoutY(35);

				webseiteSucheButton.setLayoutX(440);
				webseiteSucheButton.setLayoutY(35);

				crawlerStarten.setLayoutX(10);
				crawlerStarten.setLayoutY(80);

				crawlerStoppen.setLayoutX(296.7);
				crawlerStoppen.setLayoutY(80);

				radioListe.setLayoutX(15);
				radioListe.setLayoutY(132);

				radioWoerter.setLayoutX(15);
				radioWoerter.setLayoutY(178);

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

					for (int i = 0; i < startStelle.size() && i < stopStelle.size(); i++) {
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

		pane0.getChildren().addAll(textArea0, textField1, radioListe, radioWoerter, waehlenButton, zuruecksetzenButton,
				xmlListeWaehlenButton, fremdwortsucheStartenButton, dateiSucheButton, crawlerSucheButton,
				ordnerSucheButton, webseiteSucheButton, menueLeiste, textField0, kontaktMenue0);
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
					caspian = true;

					scene.getStylesheets().clear();
					scene.getStylesheets().add(Gui.class.getResource("notransparency.css").toExternalForm());
				} else if (scene.getStylesheets().get(0).contains("notransparency")) {
					caspian = false;
					scene.getStylesheets().clear();
					scene.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());
				}
			}
		});
	}

	public Pane getPane0() {
		return pane0;
	}

	public void setPane0(Pane pane0) {
		this.pane0 = pane0;
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
				"Folgende Schritte müssen für eine erfolgreiche Suche beachtet werden:"
				+ "\n1. Als erstes sollte eine XML-Liste ausgewählt werden auf die später ausgewählte Texte durchsucht werden. "
				+ "Alternativ können Texte auch auf einzelne Wörter geprüft werden. Diese kann man in das untere Textfeld durch ein Leerzeichen getrennt eingeben."
				+ " Welche von beiden Alternativern ausgeführt wird, kann man über die Radiobuttons vor dem Textfeld und dem Dropdown-Menü  bestimmen."
				+ "\n2. Anschließend muss entweder noch die zu durchsuchende XML-Liste, ein ordner oder eine URL angegeben werden"
				+ "\n3. Die Suche kann durch klicken auf den Button \"Text auf Fremdwörter prüfen\"  gestartet werden");

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

	private RSSThread thread;
	private boolean started = false;
	private int counter = 0;
	/*
	 * public void crawlerStart(){ 
	 * try { RSSThread.setCurrdir(new
	 * java.io.File(".").getCanonicalPath()); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * RSSThread.setMessageFile(new ServerLogfile(RSSThread.getCurrdir() +
	 * "/messages.log")); RSSThread.setErrorFile(new
	 * ServerLogfile(RSSThread.getCurrdir() + "/errors.log"));
	 * RSSThread.setTerminate(new File(RSSThread.getCurrdir() + "/stop"));
	 * String configFile = RSSThread.getCurrdir() + "/configuration.properties";
	 * Properties properties = new Properties(); try { properties.load(new
	 * FileReader(configFile)); } catch (IOException e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); }
	 * 
	 * RSSThread.archivedir = properties.getProperty("archivedir");
	 * RSSThread.sourcedir = properties.getProperty("sourcedir");
	 * RSSThread.subscriptiondir = properties.getProperty("subscriptiondir");
	 * RSSThread.schemadir = properties.getProperty("schemadir"); }
	 */

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

		TextArea crawler = new TextArea("Crawler bereit zum starten");
		crawler.setLayoutX(20);
		crawler.setLayoutY(140);
		crawler.setPrefSize(290, 100);
		crawler.setEditable(false);
		crawler.setScrollTop(Double.MAX_VALUE);
		Gui gui = new Gui();
		gui.crawlerStart();
		// crawlerStart();
		RSSThread thread = new RSSThread(100);

		// starten Actionhandler
		starten.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				if (counter == 0) {

					thread.start();
				} else {
					thread.resume();
				}
				crawler.setText(crawler.getText() + "\nCrawler wurde gestartet");
				counter++;
				started = true;
				System.out.println(counter);
			}
		});

		// stoppen Actionhandler
		stoppen.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {
				started = false;
				crawler.setText(crawler.getText() + "\nCrawler wurde gestoppt");

				thread.suspend();
				// thread.stop();
				// close();
				// crawlerStart();
				// RSSThread thread = new RSSThread(100);

			}

		});

		pane2.getChildren().addAll(text, crawler, starten, stoppen);

		scene2.getStylesheets().clear();
		scene2.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());
	}
}
