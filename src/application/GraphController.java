package application;
	

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

/**
 * Diese Klasse verwaltet die GUI - Elemente, zeichnet den Graphen und bewaeltigt die Interaktion mit dem Nutzer
 * @author Matze
 *
 */

public class GraphController extends Application {
	
	private Graph graph;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			fh = new FileHandler();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GraphGUI.fxml")); // Aufruf der FXML-Datei
			loader.setController(this);
			Parent lc = loader.load();
			Scene scene = new Scene(lc, 1200, 800);
			deaktiviereElemente();
			primaryStage.setTitle("Graphen");
			primaryStage.setScene(scene);
			primaryStage.show(); 
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	// ---------FXML--------------
	@FXML private GridPane gridBasis;
	@FXML private Slider slidKnoten;
	@FXML private Slider slidKanten;
	@FXML private Button btnloescheKnoten;
	@FXML private Button btnloescheKante;
	@FXML private Button btnKnotenHinzufuegen;
	@FXML private Button btnKanteHinzufuegen;
	@FXML private Button btnGraphSichern;
	@FXML private Button btnGraphLaden;
	@FXML private Button btnDijkstra;
	@FXML private Pane paneGraph;
	@FXML private TextField txtStart;
	@FXML private TextField txtEnd;
	@FXML private TextField txtGewicht;
	@FXML private Button btnErstellen;
	@FXML private TextField txtStartPfad;
	@FXML private TextField txtEndPfad;
	@FXML private Button btnPfad;
	
    //----------------------------
	FileHandler fh;
	ArrayList<LineObject> loList = new ArrayList<LineObject>();
	private Circle activeCircle;
	private Line activeLine;
	/**
	 * Erzeugt einen Graphen durch den Aufruf der Graph-Klasse
	 * aktiviert ebenfalls GUI-Elemente, um Operationen am Graphen durchfuehren zu koennen
	 * zeichnet anschließend den erstellten Graphen
	 */
	@FXML
	public void erzeugeGraph() {
		
		this.graph = new Graph((int)slidKnoten.getValue(), (int)slidKanten.getValue());
		aktiviereElemente();
		zeichneGraph();
	}
	/**
	 * Loescht einen Knoten aus dem Graphen und zeichnet den Graphen neu.
	 */
	@FXML
	public void loescheKnoten() {
		if(activeCircle != null) {
			this.graph.loescheKnoten(Integer.valueOf(activeCircle.getId()));
			activeCircle = null;
			zeichneGraph();
		}
	}
	/**
	 * Loescht eine Kante aus dem Graphen und zeichnet den Grapen neu
	 */
	@FXML 
	public void loescheKante() {
		
		if(activeLine != null) {
			
			for (LineObject lineObject : loList) {
				if(lineObject.getLine().getId().equals(activeLine.getId())) {
					graph.loescheKante(lineObject.getStartNode(), lineObject.getEndNode());
					activeLine = null;
				}
			}
			zeichneGraph();
			
		}
		
	}
	/**
	 * Fuegt einen Knoten an den Graphen an und zeichnet den Graphen neu
	 */
	@FXML
	public void hinzufuegenKnoten() {
		this.graph.addKnoten();
		zeichneGraph();
	}
	/**
	 * Erstellt den Nutzerdialog, um dem Nutzer die Moeglichkeit zu geben, 
	 * eine Kante nach seinen Parametern zu erstellen
	 */
	@FXML 
	public void showKanteDialog() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/KanteErstellenGUI.fxml"));
			loader.setController(this);
			Parent lc = loader.load();
			Scene scene = new Scene(lc, 400, 300);
			Stage kanteStage = new Stage();	
			kanteStage.setTitle("Neue Kante erstellen");
			kanteStage.setScene(scene);
			kanteStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Erstellt eine neue Kante und zeichnet den Graphen neu.
	 * Die noetigen Parameter kommen vom Nutzer
	 * 
	 */
	@FXML
	public void erstellenKante() {
		try {
			int anfang = Integer.valueOf(txtStart.getText());
			int ende = Integer.valueOf(txtEnd.getText());
			int gewicht = Integer.valueOf(txtGewicht.getText());
			this.graph.addEdge(anfang-1, ende-1, gewicht);
			Stage stage = (Stage) btnErstellen.getScene().getWindow();
		    stage.close();
		    zeichneGraph();
		}
		catch(NumberFormatException e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Keine gültige Eingabe.");
			a.show();
		}
		
	}
	/**
	 * Ruft den FileHandler auf, um den Graph zu speichern
	 */
	@FXML 
	public void sichereGraph() {
		fh.writeGraphToFile(graph);
	}
	/**
	 * Ruft den FileHandler auf, um den gesicherten Graphen zu laden
	 * und zeichnet den Graphen neu
	 */
	@FXML
	public void ladeGraph() {
		try {
			this.graph = fh.readGraphFromFile();
			zeichneGraph();
		} catch (FileNotFoundException e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Datei nicht gefunden.");
			a.show();
		}
	}
	/**
	 * Erstellt einen Nutzerdialog, um Parameter fuer
	 * den Dijkstra-Algorithmus zu erhalten
	 */
	@FXML
	public void starteDijkstra() {
	
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/DijkstraGUI.fxml"));
			loader.setController(this);
			Parent lc = loader.load();
			Scene scene = new Scene(lc, 400, 300);
			Stage pfadStage = new Stage();	
			pfadStage.setTitle("Pfad suchen");
			pfadStage.setScene(scene);
			pfadStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Ruft die Dijkstra-Klasse auf, um den
	 * guenstigsten Pfad zu ermitteln und markiert 
	 * den Pfad
	 */
	@FXML
	public void suchePfad() {
		try {
			zeichneGraph();
			int anfang = Integer.valueOf(txtStartPfad.getText());
			int ende = Integer.valueOf(txtEndPfad.getText());
			Dijkstra d = new Dijkstra(graph, anfang-1, ende-1);
			ArrayList<Integer> list = d.findPath();
			if(list == null) {
				Alert a = new Alert(AlertType.ERROR);
				a.setContentText("Pfaderstellung nicht möglich.");
				a.show();
			}
			else {
				list.add(anfang-1);
				System.out.println();
				for (Integer integer : list) {
					System.out.println(integer);
				}
				pfadMarkieren(list);
				Stage stage = (Stage) btnPfad.getScene().getWindow();
			    stage.close();
			}
			
			
			
		}
		catch(NumberFormatException e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setContentText("Keine gültige Eingabe.");
			a.show();
		}
		
	}
	
	@FXML
	private void reset() {
		zeichneGraph();
	}
	/**
	 * zeichnet den Graphen, indem Kreise als Knoten und Linien als Kanten
	 * gezeichnet werden. Auch werden Zahlen in die Kreise gezeichnet, um dem
	 * Nutzer eine bessere Identifikation zu bieten.
	 * Auch die Linien erhalten einen Text, der die Gewichtung der Kante widerspiegelt
	 */
	private void zeichneGraph() {
		paneGraph.getChildren().clear();
		ArrayList<Circle> circleList = new ArrayList<Circle>();
		loList.clear();
		try{
			
		
		//System.out.println("Zeichnen");
		double winkelErhoehung = 360/this.graph.getAdjList().length;
		double winkel = winkelErhoehung;
		double newRadius = paneGraph.getWidth()/2-50;
		//Circle cNew = new Circle(paneGraph.getWidth()/2, paneGraph.getHeight()/2, newRadius);
		for(int i = 0; i<graph.getAdjList().length; i++){
			double x = newRadius * Math.sin(winkel/360*2*Math.PI);
			double y = newRadius * Math.cos(winkel/360*2*Math.PI);
			Circle c = new Circle(x-100+paneGraph.getWidth()/2,y-50+paneGraph.getHeight()/2,20, Paint.valueOf("Blue"));
			c.setId(i+"");
			c.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
	            kreisMarkieren(e);
	        });
			c.setCursor(Cursor.CROSSHAIR);
			Text text = new Text(x-105+paneGraph.getWidth()/2,y-45+paneGraph.getHeight()/2, i+1+"");
			text.setCursor(Cursor.CROSSHAIR);
			text.setBoundsType(TextBoundsType.VISUAL);
			text.setFill(Paint.valueOf("White"));
			text.setStyle(
	                "-fx-font-family: \"Times New Roman\";" +
	                "-fx-font-size: 16px;"
	        );
			
			//System.out.println(x + "   "+ y + "   "+ winkel);
			winkel += winkelErhoehung;
			
			
			paneGraph.getChildren().addAll(c,text);
			circleList.add(c);
			
		
		}
		
		// Linien zeichnen
		int begrenzer = 0; // Geiler Name
		for(int i = 0; i<graph.getAdjList().length; i++) {
			for(int j = begrenzer; j<graph.getAdjList().length; j++) {
			if(graph.getAdjList()[i][j] > 0) {
					LineObject lo = new LineObject(i, j, graph.getAdjList()[i][j], i*graph.getAdjList().length+j);
					lo.getLine().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			            linieMarkieren(e);
			        });
					lo.getLine().setCursor(Cursor.CROSSHAIR);
					Label label = new Label(graph.getAdjList()[i][j]+"");
					label.setTextFill(Paint.valueOf("Black"));
					lo.linieVerbinden(circleList.get(i), circleList.get(j));
					loList.add(lo);
					paneGraph.getChildren().add(lo.getLine());
					lo.getLine().toBack();
					
					label.layoutXProperty().bind(lo.getLine().endXProperty().subtract(lo.getLine().endXProperty().subtract(lo.getLine().startXProperty()).divide(2)));
					label.layoutYProperty().bind(lo.getLine().endYProperty().subtract(lo.getLine().endYProperty().subtract(lo.getLine().startYProperty()).divide(2)));
					label.setFont(new Font(15));
					label.setStyle("-fx-font-weight: bold");
					label.setTextFill(Paint.valueOf(("Red")));

					paneGraph.getChildren().add(label);
				}
			}
			begrenzer++;
		}
		//graph.printAsMatrix();
		//System.out.println();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Markiert einen Kreis, nachdem er angeklickt wurde
	 * @param e Das Mouse-Event, in dem Fall das Klick-Event
	 */
	private void kreisMarkieren(MouseEvent e) {
		Circle c = (Circle) e.getSource();
		if(activeCircle != null) 
			activeCircle.setFill(Paint.valueOf("Blue"));
			
		c.setFill(Paint.valueOf("Red"));
		activeCircle = c;
	}
	/**
	 * Markiert eine Linie, nachdem sie angeklickt wurde
	 * @param e Das Mouse-Event, in dem Fall das Klick-Event
	 */
	private void linieMarkieren(MouseEvent e) {
		Line l = (Line) e.getSource();
		if(activeLine != null) 
			activeLine.setStroke(Paint.valueOf("Blue"));
			
		l.setStroke(Paint.valueOf("Red"));
		activeLine = l;
	}
	/**
	 * Markiert alle Knoten und Kanten, die durch den
	 * vorhergegangenen Dijkstra-Algorithmus als guenstigster
	 * Pfad ausgemacht wurde
	 * @param pfad Eine Liste, die die Knoten als Pfad enthaelt
	 */
	private void pfadMarkieren(ArrayList<Integer> pfad) {
		for (Node n : paneGraph.getChildren()) {
			if(n instanceof Circle) {
				Circle c = (Circle) n;
				for (Integer integer : pfad) {
					if(Integer.valueOf(c.getId()) == integer) {
						c.setFill(Paint.valueOf("GREEN"));
					}
				}
			}
		}
		for(int i = 0; i+1<pfad.size(); i++) {
			for (LineObject lo : loList) {
				if((lo.getStartNode() == pfad.get(i) && lo.getEndNode()==pfad.get(i+1)) // Abfrage noetig, da unbekannt, welcher Knoten nun Anfang oder Ende ist
						|| (lo.getStartNode() == pfad.get(i+1) && lo.getEndNode() == pfad.get(i))) {
					lo.getLine().setStroke(Paint.valueOf("GREEN"));
				}
			}
		}
		
	}
	/**
	 * Deaktiviert wichtige GUI-Elemente, fuer dessen
	 * Benutzung das Vorhandensein eines Graphens gewaehrleistet werden muss
	 * Wird verwendet, wenn kein Graph in der Oberflaeche vorhanden ist
	 */
	private void deaktiviereElemente() {
		btnloescheKnoten.setDisable(true);
		btnloescheKante.setDisable(true);
		btnKanteHinzufuegen.setDisable(true);
		btnKnotenHinzufuegen.setDisable(true);
		btnGraphLaden.setDisable(true);
		btnGraphSichern.setDisable(true);
		btnDijkstra.setDisable(true);
	}
	/**
	 * Aktiviert wichtige GUI-Elemente, mit denen mit dem Graphen
	 * interagiert werden kann.
	 */
	private void aktiviereElemente() {
		btnloescheKnoten.setDisable(false);
		btnloescheKante.setDisable(false);
		btnKanteHinzufuegen.setDisable(false);
		btnKnotenHinzufuegen.setDisable(false);
		btnGraphLaden.setDisable(false);
		btnGraphSichern.setDisable(false);
		btnDijkstra.setDisable(false);
	}
	
	
	
	
	
}