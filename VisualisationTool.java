
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class VT extends Application implements Runnable {	
	
	double appWidth = 600, appHeight = 600;
	boolean gameOver = false;
	
	String fileName = "bunny.obj";
	Random rnd = new Random();
	RightPanel rpanel;
	MyGame game;
	Thread thread;
	
	@Override
	public void run() {
		while(!gameOver){			
			try {
				Thread.sleep(25);
				//vykreslenie
				Platform.runLater(new Runnable() { //ked bude mat cas, vykresli
					
					public void run() {
						game.paint();
					}
				});
			} catch (InterruptedException e) { }
		}	
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		rpanel = new RightPanel();
		VBox right = new VBox(rpanel);
		right.setPrefWidth(250);

		game = new MyGame("bunny.obj");	

		BorderPane root = new BorderPane();
		root.setCenter(game);
		root.setRight(rpanel);
		
		Scene scene = new Scene(root);	// vytvor scenu
		primaryStage.setTitle("VisualisationTool"); 	// pomenuj okno aplikacie, javisko
		primaryStage.setScene(scene); 			// vloz scenu do hlavneho okna, na javisko
		primaryStage.show(); 					// zobraz javisko
		
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		gameOver = true;
		try {
			thread.join();
		} catch (InterruptedException e) { }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public class RightPanel extends Pane{

		public RightPanel() {
			setStyle("-fx-background-color: silver;");
			paint();
		}
		
		private void paint(){
			//TODO: vykreslit okienka a buttony na uzivatelsky vstup
			//po stlaceni buttonu nastavy premenne podla vstupu
			//zavola game.update();
			getChildren().clear();
			Rectangle r = new Rectangle();
			r.setWidth(250);
			r.setHeight(20);
			r.setFill(Color.RED);
			r.setX(0);
			r.setY(0);
			getChildren().add(r);
		}
	}
	
	public class MyGame extends Pane{
						
		String bg = "-fx-background-color: white;";
		ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
		ArrayList<Double[]> vertices = new ArrayList<>();
		
		public MyGame(String inputName){
			
			widthProperty().addListener(e -> {appWidth = getWidth();});
			heightProperty().addListener(e -> {appHeight = getHeight();});
			
			//nastavenie velkosti okna a farby pozadia
			setPrefSize(appWidth, appHeight);
			setStyle(bg);	
			
			//initState
			try {
				readFile(fileName);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		
		public void readFile(String inputName) throws FileNotFoundException{
			
			File f = new File(inputName);
			FileInputStream fi = new FileInputStream(f);
			Scanner sc = new Scanner(fi);
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				if(line.charAt(0) == 'v'){
					String[] pole = line.split("\\s+");
					double x = Double.parseDouble(pole[1].toString());
					double y = Double.parseDouble(pole[2].toString());
					vertices.add(new Double[]{x, y});
				}
				if('s' == line.charAt(0)){
					for(int i = 0; i < vertices.size(); i++){
						graph.add(new ArrayList<>());
					}
				}
				if('f' == line.charAt(0)){
					String[] pole = line.split("\\s+");
					int v1 = Integer.parseInt(pole[1].toString())-1;
					int v2 = Integer.parseInt(pole[2].toString())-1;
					int v3= Integer.parseInt(pole[3].toString())-1;
					graph.get(v1).add(v2);
					graph.get(v1).add(v3);
					graph.get(v2).add(v3);
				}
			}
			sc.close();		
		}
		
		public void paint(){
			
			getChildren().clear();
			for(int i = 0; i < vertices.size(); i++){
				for(int j = 0; j < graph.get(i).size(); j++){
					double x1 = vertices.get(i)[0];
					double y1 = vertices.get(i)[1];
					double x2 = vertices.get(graph.get(i).get(j))[0];
					double y2 = vertices.get(graph.get(i).get(j))[1];
					
					Line l = new Line((100*x1)+appWidth/2, (100*y1)+appHeight/2, (100*x2)+appWidth/2, (100*y2)+appHeight/2);
					l.setStrokeWidth(0.2);
					getChildren().add(l);
				}
			}
		}

		public void update(){
			//TODO
			//vezmi hodnoty zo vstupu -transformacia, rotacia, skalovanie
			//vytvor maticu
			//(treba kazdy bod vo vertices prenasobyt novou maticou)
			//zavolaj fciu multiplyMatrix(vertices, matrix) -musi vratit ich sucin v poli
		}
		
	}
	
	
}
