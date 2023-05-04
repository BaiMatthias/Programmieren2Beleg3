package application;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * Diese Klasse repraesentiert eine Linie, die in der graphischen Oberflaeche zwei Knoten miteinander verbindet. 
 * Dies war noetig, um die Linien, die in der GUI gezeichnet werden, besser verwalten zu koennen.
 * Die Linie steht hierbei reprasentativ fuer eine Kante im Graphen
 * @author MatzesPC
 *
 */


public class LineObject {
	
	private Line line;
	private int startNode;
	private int endNode;
	private int gewicht;
	private int id;
	
	/**
	 * Erstellt ein neues LineObject, zusammen mit einer neuen Linie, die die Knoten miteinander verbindet
	 * @param startNode Der Startknoten, der verbunden wird
	 * @param endNode Der Endknoten, der mit dem Startknoten verbunden wird
	 * @param gewicht Die Gewichtung der Kante
	 * @param id Eine eindeutige ID zur besseren Identifizierung eines Objekts
	 */
	public LineObject(int startNode, int endNode, int gewicht, int id) {
		line = new Line();
		this.startNode = startNode;
		this.endNode = endNode;
		this.gewicht = gewicht;
		this.line.setId(id+"");
	}
	/**
	 * Gibt das GUI - Element Line zurueck
	 * @return Die Line als GUI-Element
	 */
	public Line getLine() {
		return this.line;
	}
	/**
	 * Gibt die Gewichtung der Kante zurueck
	 * @return die Gewichtung der Kante
	 */
	public int getGewicht() {
		return this.gewicht;
	}
	/** 
	 * gibt den Startknoten der Kante zurueck
	 * @return Der Startknoten der Kante
	 */
	public int getStartNode() {
		return this.startNode;
	}
	/**
	 * Gibt den Endknoten der Kante zurueck
	 * @return Der Endknoten der Kante
	 */
	public int getEndNode() {
		return this.endNode;
	}
	/**
	 * Verbindet zwei Knoten miteinander
	 * @param c1 Der Startknoten
	 * @param c2 Der Endknoten
	 */
	public void linieVerbinden(Circle c1, Circle c2) {
		this.line.startXProperty().bind(c1.centerXProperty());
        this.line.startYProperty().bind(c1.centerYProperty());

        this.line.endXProperty().bind(c2.centerXProperty());
        this.line.endYProperty().bind(c2.centerYProperty());

        this.line.setStrokeWidth(2);
        this.line.setStrokeLineCap(StrokeLineCap.BUTT);
      
	}

}
