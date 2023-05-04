package application;

import java.util.Comparator;

/**
 * Diese Klasse hat nur den Zweck, um den Dijkstra-Algorithmus bei dem Vergleich der Distanzen zu helfen.
 * @author Matze
 *
 */

public class DistanceComparator implements Comparator<Integer> {

	private final int[] distanz;

	public DistanceComparator(int[] distanz) {
        this.distanz = distanz;
    }
	
	 
	@Override
	public int compare(Integer arg0, Integer arg1) {
		return Integer.compare(distanz[arg0], distanz[arg1]);
		
	}

}
