package application;
import java.util.Arrays;
import java.util.Random;

/**
 * Diese Klasse repraesentiert einen ungerichteten gewichteten Graphen mittels einer Adjazenzmatrix. Die Klasse stellt Methoden bereit, um Knoten und Kanten zu loeschen und hinzuzufuegen.
 * 
 * @author Matze
 *
 */

public class Graph {
	
	private int[][] adjList;
	private int anzahlKanten;
	/**
	 * Erstellt ein neues Graph-Objekt
	 * @param dimension Die groesse des Graphen
	 * @param anzahlKanten Die Anzahl der Verbindungen unter den Knoten
	 */
	public Graph(int dimension, int anzahlKanten){
		adjList = new int[dimension][dimension]; // Felder die Verlinkung haben K÷NNTEN = -1
		for(int i = 0; i<adjList.length; i++){ // Felder die keine haben D‹RFEN = 0
			for (int j = 0; j<adjList[i].length; j++){
				if(i != j){
					adjList[i][j] = -1;
				}
			}
		}
		this.anzahlKanten = anzahlKanten;
		createGraph();
	}
	/**
	 * Erstellt ein neues Graph-Objekt, wird hauptsaechlich 
	 * verwendet, um das Laden eines Graphen zu realisieren
	 * @param adjList Eine bereits fertige Adjazenzmatrix
	 */
	public Graph(int[][] adjList) {
		this.adjList = adjList;
	}
	/**
	 * Gibt die Adjazenzmatrix zurueck
	 * @return Die Adjazenzmatrix
	 */
	public int[][] getAdjList(){
		return this.adjList;
	}
	/**
	 * Fuegt eine neue Kante an zwei uebergebene Knoten mit angegebenen Gewicht
	 * Eine Kante zwischen Knoten, die bereits eine Kante haben, ist nicht erlaubt.
	 * @param knotenAnfang Der Knoten, an dem die Kante beginnen soll
	 * @param knotenEnde Der Knoten, an dem die Kante enden soll
	 * @param weight Die Gewichtung dieser Verbindung
	 */
	public void addEdge(int knotenAnfang, int knotenEnde, int weight){
		if(knotenAnfang != knotenEnde && adjList[knotenAnfang][knotenEnde] < 0 && knotenAnfang < adjList.length && knotenEnde < adjList.length
				&& knotenAnfang >= 0 && knotenAnfang >= 0) {
			adjList[knotenAnfang][knotenEnde] = weight;
			adjList[knotenEnde][knotenAnfang] = weight;
		}
			
		
	}
	
	/**
	 * Fuegt einen Knoten an den Graphen an, die Adjazenzmatrix wird hierbei vergroeﬂert
	 */
	public void addKnoten() {
		int[][] tmpArray = new int[this.adjList.length+1][this.adjList.length+1];
		for(int i = 0; i<adjList.length; i++) {
			tmpArray[i] = Arrays.copyOf(this.adjList[i], this.adjList.length+1);
		}
		this.adjList = tmpArray;
		
		for(int i = 0; i<adjList.length; i++){ 
			for (int j = 0; j<adjList[i].length; j++){
				if(i != j && adjList[i][j] == 0){
					adjList[i][j] = -1;
				}
			}
		}
		//System.out.println();
		//printAsMatrix();
	}
	/**
	 * Loescht einen angegeben Knoten aus der Adjazenzmatrix, die Adjazenzmatrix wird hierbei verkleinert
	 * @param index Der Knoten, der geloescht werden soll
	 */
	public void loescheKnoten(int index) {
		System.out.println(index);
		System.out.println();
		int[][] tmpArray = new int[this.adjList.length-1][this.adjList.length-1];
		int k = 0;
		
		for(int i = 0; i<this.adjList.length; i++) {
			int l = 0;
			if(i != index) {
				for(int j = 0; j<this.adjList.length; j++) {
					if(j != index) {
						tmpArray[k][l] = this.adjList[i][j];
					}else l--;
					l++;
				}
			}else k--;
			k++;
		}
		this.adjList = tmpArray;
		
	}
	/**
	 * Loescht eine Kante aus dem Graphen, hierbei muessen die Knoten angegeben werden, zwischen 
	 * denen die Verbindung geloescht werden soll
	 * @param startKnoten Einer der Knoten, dessen Verbindung mit dem endKnoten geloescht werden soll
	 * @param endKnoten Einer der Knoten, dessen Verbindungen mit dem startKnoten geloescht werden soll
	 */
	public void loescheKante(int startKnoten, int endKnoten) {
		this.adjList[startKnoten][endKnoten] = -1;
		this.adjList[endKnoten][startKnoten] = -1;
	}
	/**
	 * Gibt den Graph als textuelle Ausgabe aus
	 */
	public void printGraph(){
		for(int i = 0; i<adjList.length; i++){
			System.out.println("Knoten " + i + " ist mit folgenden Knoten verbunden:");
			for (int j = 0; j<adjList[i].length; j++){
				if(adjList[i][j] > 0){
					System.out.print("Knoten " + j + " mit der Gewichtung " + adjList[i][j]);
					System.out.println();
				}
			}
		}
	}
	/**
	 * Gibt den Graphen in Matrix-Form aus
	 */
	public void printAsMatrix(){
		for(int i = 0; i<adjList.length; i++){
			for (int j = 0; j<adjList[i].length; j++){
				System.out.print(adjList[i][j]);
				System.out.print("\t");
			}
			System.out.println();
		}
	}
	
	/**
	 * Gibt ein Array mit allen Nachbarn des aktuellen Knoten zurueck 
	 * @param node
	 * @return Ein Array mit allen Nachbarn
	 */
	public int[] getNeighbours(int node)
	{
		int[] neighbours = null;
		int count = 0;
		
		// Zaehle zuerst die vorhandenen Nachbarn
		for(int i = 0; i < adjList[node].length; ++i)
			if(adjList[node][i] > 0)
				++count;
		
		neighbours = new int[count];
		count = 0;
		
		// Jetzt schreib die Verbindungen in eine Liste
		for(int i = 0; i < adjList[node].length; ++i)
			if(adjList[node][i] > 0)
				neighbours[count++] = i;
			
		return neighbours;
	}
	/**
	 * Erstellt den Graphen und verknuepft zufaellig Knoten mit Kanten
	 */
	private void createGraph(){
		Random rnd = new Random();
		for(int i = 0; i < anzahlKanten; i++){
			int knoten1 = rnd.nextInt(adjList.length);
			int knoten2 = rnd.nextInt(adjList.length);
			int gewicht = rnd.nextInt(20)+1;
			while(adjList[knoten1][knoten2] >= 0){
				knoten2 = rnd.nextInt(adjList.length);
				knoten1 = rnd.nextInt(adjList.length);
			}
			addEdge(knoten1, knoten2, gewicht);
		}
	}
	
	
	
	
	
}
