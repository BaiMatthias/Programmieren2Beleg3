//package application;
//	
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.stage.Stage;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
//
//
//public class Main extends Application {
//	@Override
//	public void start(Stage primaryStage) {
//		try {
//			
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("/GraphGUI.fxml")); // Aufruf der FXML-Datei
//			loader.setController(this);
//			Parent lc = loader.load();
//			Scene scene = new Scene(lc, 800, 800);
//			primaryStage.setTitle("Graphen");
//			primaryStage.setScene(scene);
//			primaryStage.show(); 
//			
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public static void main(String[] args) {
//		launch(args);
//	}
//}
