package application;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Diese Klasse ist fuer die Erstellung und Bereitstellen von Dateien zur Speicherung von Graphen verantwortlich.
 * Sie stellt Methoden bereit, um Graphen in das Dateisystem abzulegen und vorhandene Graphen zu laden.
 * @author Matze
 *
 */

public class FileHandler {
	
	String path;
	
	public FileHandler() {
		path = this.getClass().getResource("/").getFile()+"/graph.txt";
	}
	/**
	 * Schreibt einen uebergebenen Graphen in das Dateisystem. Da nicht davon auszugehen ist, dass
	 * jeder Nutzer ueber das gleiche Dateisystem verfuegt, wird die Datei in den bin-Ordner dieses Projekts
	 * gesichert
	 * @param graph Der Graph, der gesichert werden soll
	 */
	public void writeGraphToFile(Graph graph) {
		
		
		File f = new File(path);
	    if(f.exists()) f.delete();
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(path.toString()));
			for(int i = 0; i < graph.getAdjList().length; i++) {
				for(int j = 0; j < graph.getAdjList()[i].length; j++) {
					bw.write(Integer.toString(graph.getAdjList()[i][j])+" ");
				}
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 

	}
	/**
	 * Liest die Datei, die den Graphen enthaelt, aus und gibt den erstellten Graphen zurueck
	 * @return Der Graph, der aus der Datei rekonstruiert wurde
	 * @throws FileNotFoundException Wenn keine Datei gefunden wurde
	 */
	public Graph readGraphFromFile() throws FileNotFoundException {
		ArrayList<ArrayList<Integer>> tmpList = new ArrayList<ArrayList<Integer>>();
		
			File f = new File(path);
			if(f.exists()) {
				Scanner s = new Scanner(new File(this.path));
				while(s.hasNextLine()) {
					Scanner srow = new Scanner(s.nextLine());
					ArrayList<Integer> rowList = new  ArrayList<Integer>();
					while(srow.hasNextInt()) {
						rowList.add(srow.nextInt());
					}
					tmpList.add(rowList);
				}

				int[][] tmpAdjArr = new int[tmpList.size()][tmpList.size()];
				for(int i = 0; i<tmpAdjArr.length; i++) {
					tmpAdjArr[i] = tmpList.get(i).stream().mapToInt(e -> e).toArray(); // supergeile Magie, versteht keiner, aber funktioniert
				}
				return new Graph(tmpAdjArr);
				
			}
			else throw new FileNotFoundException("Datei nicht vorhanden");

		
		
		
		
	}
		
	

}
