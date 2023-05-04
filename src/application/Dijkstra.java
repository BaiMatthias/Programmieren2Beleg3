package application;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Diese Klasse repraesentiert den Dijkstra-Algorithmus. Sie stellt eine Methode zur Verfuegung, um den günstigsten Pfad zwischen zwei Knoten in einem
 * gegebenen Graphen zu ermitteln
 * @author Matze
 *
 */

public class Dijkstra {
	
	
	  private Graph graph;
	  private int[] distanz;
	  private boolean[] besucht;
	  private int[] nachbarn;
	  private int startNode;
	  private int endNode;
	  /**
	   * Initialisiert die noetigen Parameter fuer die Suche
	   * @param graph Der Graph, in dem gesucht werden soll
	   * @param startNode Der Knoten, von dem aus die Suche begonnen werden soll
	   * @param endNode Der Zielknoten
	   */
	  public Dijkstra(Graph graph, int startNode, int endNode) {
		    this.graph = graph;
		    distanz = new int[graph.getAdjList().length];
		    besucht = new boolean[graph.getAdjList().length];
		    nachbarn = new int[graph.getAdjList().length];
		    this.startNode = startNode;
		    this.endNode = endNode;
		  }
	  	  /**
	  	   * Der Dijkstra-Algorithmus, der unter Zuhilfename einer Priority-Queue
	  	   * und Nutzung eines greedy-Algorithmus den guenstigsten Pfad berechnet. 
	  	   * Hierbei werden die Distanzen zwischen den einzelnen Pfaden verglichen und 
	  	   * bereits besuchte Pfade markiert. Ueber eine Methode der Klasse Graph werden
	  	   * auch die Nachbarn des gerade besuchten Knotens ermittelt, um weiter nach
	  	   * dem guenstigsten Pfad suchen zu können.
	  	   * @return
	  	   */
		  public ArrayList<Integer> findPath() {
		    if (endNode == startNode) {
		        // Endknoten = Startknoten nicht erlaubt
		        return null;
		    }

		    int[][] graphForPathFinding = graph.getAdjList();

		    // Arrays befuellen
		    for (int i = 0; i < nachbarn.length; i++) {
		        nachbarn[i] = Integer.MAX_VALUE;
		        besucht[i] = false;
		        distanz[i] = Integer.MAX_VALUE;
		    }

		    PriorityQueue<Integer> queuePfad = new PriorityQueue<Integer>(1, new DistanceComparator(distanz)); // Der Comparator sorgt dafuer, dass
		    																								// geringe Distanzen die hoechste Prioritaet haben
		    distanz[startNode] = 0;
		    queuePfad.add(startNode); // Die Queue beginnt beim Startknoten

		    while (queuePfad.isEmpty() == false) {
		        int nextNode = queuePfad.poll(); // Der Head aus der Queue wird entfernt
		        besucht[nextNode] = true;

		        if (nextNode == endNode) {// Ziel erreicht
		            testPrint();
		            ArrayList<Integer> tmpList = new ArrayList<Integer>();
		            for (int node = endNode; node != startNode;) {
				        tmpList.add(node);
				        node = nachbarn[node];
				    }
		            return tmpList; // Pfad zurueckgeben
		        }

		        int[] nachbarnArr = graph.getNeighbours(nextNode); // Ermittle alle Nachbarn des aktiven Knotens
		        for (int nachbar : nachbarnArr) {
		            if (besucht[nachbar] == false) {
		                // Finde den guenstigsten Nachbar
		                int d = distanz[nextNode] + graphForPathFinding[nextNode][nachbar];
		                if (d < distanz[nachbar]) {
		                    distanz[nachbar] = d;
		                    nachbarn[nachbar] = nextNode;

		                    // Queue aktualisieren, falls der Nachbar schon drin ist
		                    if (queuePfad.contains(nachbar)) {
		                    	queuePfad.remove(nachbar);
		                    }
		                    queuePfad.add(nachbar); // guenstigsten Nachbar hinzufuegen
		                }
		            }
		        }
		    }
		    // Kein Pfad gefunden
			return null;
		  }

		  

		   private void testPrint() {
		    int weight = 0;
		    int steps = 0;
		    System.out.println("Pfad: ");
		    for (int node = endNode; node != startNode; steps++) {
		        System.out.print(node + "    ");
		        weight += graph.getAdjList()[nachbarn[node]][node];
		        node = nachbarn[node];
		    }
		    System.out.println(startNode);
		    System.out.println("Number of nodes: " + steps);
		    System.out.println("Weight:  " + weight);
		  }
}
