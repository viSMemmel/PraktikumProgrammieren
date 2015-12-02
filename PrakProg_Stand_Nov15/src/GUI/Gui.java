package GUI;
// Done: Exportieren der erzeugten Liste in eine .txt --> Menüreiter Exportieren in Datei

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
import java.util.Optional;

import DokumenteSucheforGUI.Search;
//import DokumenteSucheforGUI.SearchAlt;
import javafx.stage.Modality;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Gui extends Application {



	private String filePath;

	private TextField textField0 = new TextField(); // Wird in der Subklasse des
													// FileChoosers benötigt an
													// dieser Stelle

	private TextArea textArea0 = new TextArea();

	private Button button0 = new Button("Wählen");

	private Button button1 = new Button("Zurücksetzen");

	private Button button2 = new Button("Text auf Fremdwörter prüfen");

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

		MenuBar menueLeiste = new MenuBar();
		menueLeiste.prefWidthProperty().bind(stage.widthProperty()); // Passt
																		// die
																		// Menüleiste
																		// auf
																		// die
																		// Breite
																		// des
																		// Fensters
																		// an

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

		hilfe.getItems().add(fragezeichen);

		menueLeiste.getMenus().addAll(datei, suchen,  ansicht, hilfe);

		textArea0.setScrollTop(Double.MAX_VALUE);
		textArea0.setLayoutX(10);
		textArea0.setLayoutY(150);

		textArea0.setEditable(false);
		textArea0.setWrapText(true); // Automatischer Zeilenumbruch
		textArea0.setPrefSize(574, 350);
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
		textField1.setLayoutY(100);
		textField1.setEditable(true);
		textField1.setPrefWidth(480); // Breite des Textfeldes
		textField1.setText("Hier zu prüfenden Text eingeben!");
		textField1.setMaxWidth(350);
		textField1.setAlignment(Pos.CENTER);

		button0.setLayoutX(380);
		button0.setLayoutY(47);
		button0.setPrefSize(80, 30);

		button1.setLayoutX(480);
		button1.setLayoutY(47);
		button1.setPrefSize(100, 30);

		button2.setLayoutX(380);
		button2.setLayoutY(97);
		button2.setPrefSize(200, 30);

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

				textArea0.setText("");
				textField0.setText("Bitte XML-Datei auswählen!");
				textField0.setAlignment(Pos.CENTER);
				textField1.setText("Hier zu prüfenden Text eingeben!");
				textField1.setMaxWidth(350);
				textField1.setAlignment(Pos.CENTER);

			}
		});
		// actionhandler suche starten
		button2.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent ae) {
				System.out.println("Button 2 gedrueckt");
				String Suchwort = null;
				String Output = null;
				Suchwort = textField1.getText();
				String [] SuchAr =Suchwort.split(" ");
				try {
 					Search suche = new Search(SuchAr, filePath);
					Output = suche.find();

				} catch (Exception e) {
					Output = e.getMessage();
				} finally {
					textArea0.setText(Output);
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
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
				fileChooser.getExtensionFilters().add(extFilter);

				File file = fileChooser.showSaveDialog(stage);

				// schreibt den Stringh aus der TextArea in den zu speichernden
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

		// Actionhändler zum Wählen von XML Source-Datei
		normalbild.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent ae) {

				textArea0.setLayoutX(10);
				textArea0.setLayoutY(150);
			}
		});

		pane0.getChildren().addAll(button0, button1, textField1, button2, textArea0, menueLeiste, textField0);

		Scene scene = new Scene(pane0); // Fensterinhalt in dem ein Panel gelegt
										// wird
		stage.setScene(scene); // Fensterinhalt aufs Fenster legen
		stage.show(); // Fenster sichtbar machen
		// CSS von Alex

		scene.getStylesheets().clear();
		scene.getStylesheets().add(Gui.class.getResource("caspian.css").toExternalForm());
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
