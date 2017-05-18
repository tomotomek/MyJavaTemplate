
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Arcanoid extends Application implements Runnable {	
	
	private ArrayList<Obdlznik> box = new ArrayList<>();
	private ArrayList<Rectangle> paintObjectIntBox = new ArrayList<>();
	private ArrayList<Obdlznik> forRemove = new ArrayList<>();
	private Set<KeyCode> pressed = new HashSet<>();
	
	boolean gameOver = false;
	boolean pause = false;
	int pocetCyklov = 0;
	double appWidth = 300, appHeight = 500;
	double mouseX = appWidth/2, mouseY = appHeight/2;
	
	double widthRectangle = appWidth/10;
	double heightRectangle = 20;
	double offset = 20;
	
	String finish = "Interupted";
	
	Random rnd = new Random();
	
	MyGame game;
	Thread thread;
	
	//fcie, ktore sa mozu hodit
	
	public double getVzdialenost(double x1, double y1, double x2, double y2){
		return Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y1-y2,2));
	}
	
	public Color generateColor(){
		Random rnd = new Random();
		return new Color(rnd.nextDouble(),rnd.nextDouble(), rnd.nextDouble(), 1);
	}
	
//	public void readFile(String inputName) throws FileNotFoundException{
//		
//		File f = new File(inputName);
//		FileInputStream fi = new FileInputStream(f);
//		Scanner sc = new Scanner(fi);
//		while(sc.hasNextLine()){
//			String line = sc.nextLine();
//		}
//		sc.close();		
//	}
//	
//	public void writeFile(String outputName) throws FileNotFoundException{
//		
//		PrintStream ps = new PrintStream(new File(outputName));
//		ps.print("");
//		ps.println("");
//	}

	@Override
	public void run() {
		while(!gameOver){			
			try {
				pocetCyklov++;
				
				while(pause){
					Thread.sleep(25);
				} 
				
				Thread.sleep(25);
				//vykreslenie
				Platform.runLater(new Runnable() { //ked bude mat cas, vykresli
					
					public void run() {
						game.paint();
						game.update();
					}
				});
				if(pocetCyklov==40) { //kazdy sekundu urob update
					pocetCyklov = 0;
					
				}
			} catch (InterruptedException e) { }

		}	
		System.out.println(finish);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		game = new MyGame();		
		Scene scene = new Scene(game);	// vytvor scenu
		primaryStage.setTitle("Oblidzniky"); 	// pomenuj okno aplikacie, javisko
		primaryStage.setScene(scene); 			// vloz scenu do hlavneho okna, na javisko
		primaryStage.show(); 					// zobraz javisko
		
		thread = new Thread(this);
		thread.start();
		
		//mouse eavents
//		setOnMouseMoved(e -> {
//			
//		});
		
		scene.setOnMouseClicked(e ->{
			e.consume();
			mouseX = e.getSceneX();
			mouseY = e.getSceneY();
		});
//		
//		scene.setOnMouseEntered(e ->{
//			pause = false;
//		});
//		
//		scene.setOnMouseExited(e->{
//			pause = true;
//		});
		
		scene.setOnKeyPressed(e -> {
			pressed.add(e.getCode());
//			if(e.getCode() == KeyCode.A);
		});
	}

	public void stop() {
		gameOver = true;
		pause = false;
		try {
			thread.join();
		} catch (InterruptedException e) { }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public class MyGame extends Pane{
						
		Gulicka g = new Gulicka(mouseX, mouseY, 10, Color.BLACK);
		String bg = "-fx-background-color: white;";
		
		public MyGame(){
			
			widthProperty().addListener(e -> {appWidth = getWidth();});
			heightProperty().addListener(e -> {appHeight = getHeight();});
			
			//nastavenie velkosti okna a farby pozadia
			setPrefSize(appWidth, appHeight);
			setStyle(bg);	
			
			//arcanoid init state
			for(int i = 0; i < 10; i++){
				for(int j = 0; j < 5; j++){
					Obdlznik ob = new Obdlznik((double) i*widthRectangle, (double) j*heightRectangle+offset, widthRectangle, heightRectangle, 0, generateColor(), i);
					box.add(ob);
				}
			}
		}
		
		public void paint(){
			
			//vycistenie sceny
			forRemove.clear();
			paintObjectIntBox.clear();
			getChildren().clear();
						
			//pridanie textu
			Text Score = new Text();
			Score.setText("Score: " );//+ score);
			Score.setFont(new Font(30));
			Score.setX(10);
			Score.setY(Score.getFont().getSize());
			getChildren().add(Score);
			
			//vykreslenie sceny
			for(Obdlznik o : box){
				if(zmaz(o)){
					forRemove.add(o);
				}
				else{
					Rectangle r = new Rectangle();
					o.x = o.stlpec*appWidth/10;
					o.width = appWidth/10;
					r.setX(o.x);
					r.setY(o.y);
					r.setWidth(o.width);
					r.setHeight(o.height);
					r.setStroke(Color.BLACK);
					r.setFill(o.farba);
					paintObjectIntBox.add(r);
				}
				o.update();
			}
			getChildren().addAll(paintObjectIntBox);
			
			//arcanoid
			Rectangle r = new Rectangle();
			r.setX(mouseX-50);
			r.setY(mouseY);
			r.setWidth(100);
			r.setHeight(10);
			r.setStroke(Color.BLACK);
			r.setFill(Color.YELLOW);
			getChildren().add(r);
			
			if(g.x >= mouseX-50 && g.x <= mouseX+50 && g.y+g.r >= mouseY && g.y+g.r <= mouseY+50){
				g.dy = -g.dy;
				g.y = mouseY-g.r;
			}
			
			Circle c = new Circle(g.x, g.y, g.r, g.color);
			getChildren().add(c);
			
			//zmaz objekty na zmazanie
			for(Obdlznik o : forRemove){
				box.remove(o);
			}
		}
		
		private boolean zmaz(Obdlznik o){
			//TODO:podmenky, kedy ma zmazat objekt. Ak true -> zmaz, inak false
			if(g.x >= o.x && g.x <= o.x+o.width && g.y-g.r <= o.y+o.height && g.y+g.r>o.y){
				forRemove.add(o);
				g.dy = -g.dy;
				g.y = o.y+heightRectangle+g.r;
			}
			return false;
		}
		
		public void update(){
			//TODO
			g.update();
		}
		
	}
	
	class Obdlznik{
		
		private double x;
		private double y;
		private double width;
		private double height;
		private Color farba;
		private double speed;
		int stlpec;
		
		public Obdlznik(double x, double y, double w, double h, double speed, Color f, int stlpec){
		
			this.x = x;
			this.y = y;
			this.width = w;
			this.height = h;
			this.farba = f;
			this.speed = speed;
			this.stlpec = stlpec;
		}
		
		public void update(){
			//TODO
		}

	}
	
	class Gulicka{
		double x, y;
		double dx, dy;
		double r;
		double onBall = 0;
		
		Color color;
		
		public Gulicka(double x, double y, double r, Color c) {
			this.r = r;
			this.x = x;
			this.y = y;
						
			this.dx = rnd.nextInt(4)+1;//Arcanoid.VECTORX;
			this.dy = (rnd.nextInt(4)+1)*(-1);//Arcanoid.VECTORY;
			this.color = c; //generateColor();
		}
		
		public void update() {
			x += dx;
			y += dy;
			if (x < r){
				dx = -dx;
				x = r;
			}
			if (y < -r){
				gameOver = true;
				finish = "YOU WIN";
			}
			if (x > appWidth-r){
				dx = -dx;
				x=appWidth-r;
			}	
			if (y > appHeight+r){
				gameOver = true;
				finish = "YOU LOSE";
			}
			
//			dx *= K;	//spomalovanie lopticky
//			dy = (dy+G)*K; //padanie lopticky podla gravitacie
		}
	}
	
	class Balloon{

    	double x;
    	double y;
    	double rx;
    	double ry;
    	double dx;
    	double dy;
    	
        int phase = 0;
        Color color;
        Text label;
        int initNumber;
        
        public Balloon(double x, double y, double rx, double ry, double dx, double dy, int number){
        	//nahodna pozicia, nahodna velkost, nahodny smer
        	this.x = x;
        	this.y = y;
        	this.rx = rx;
        	this.ry = ry;
        	this.dx = dx;
        	this.dy = dy;
        	this.label = new Text();
        	initNumber = number;
        	//color = colors[0];	//biela farba na zaciatku, menit sa bude po 500ms
        }

      public void update(){
        	x+=dx;
        	y+=dy;

			if (x < rx){
				dx = -dx;
				x = rx;
			}
			if (y < ry){
				dy = -dy;
				y = ry;
			}
			if (x > appWidth-rx){
				dx = -dx;
				x=appWidth-rx;
			}	
			if (y > appHeight-ry){
				dy = -dy;
				y = appHeight-ry;
			}
        	
			dy += 0.1;
        	label.setLayoutX(x-rx/2);
        	label.setLayoutY(y-ry/2);

      }
	}
	
}
