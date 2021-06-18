package pacman;

import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


public class Pacman {
	
	private Circle[][] hraciPole;
	private ImageView pacman;
	private ImageView[] lifes;	
	private Timeline pacmanTl= new Timeline();
	private Timer timer = new Timer();
	private TimerTask waitforTimer;
	private SpawnTimer spawnTmr;
	private ObservableList<Node> list;

	
	private int lifeCount = 1;
	private int facing = 0; //0-prava, 1-dolu, 2-doleva, 3-nahoru
	//last facing je tu na odchyceni posledniho zadaneho smeru uzivatelem, pouzivam to na zataceni tak ze kdyz uzivatel da nejaky prikaz na pohyb tak se ulozi, a vykona az pri prvni prilezitosti co pujde udelat.
	private int lastFacing=0;
	private int score = 0;
	private PacManMain main = new PacManMain();
	private boolean pause = false;
	
	int x,y,startX,startY;

	public Pacman(Circle[][] hraciPole, int startX, int startY,ObservableList<Node> list, String file) {	
		this.list = list;
		this.startX= startX;
		this.startY = startY;
		x=startX;
		y=startY;
		this.hraciPole = hraciPole;
		
		pacman=new ImageView();
		pacman.setStyle("-fx-image: url('file:"+file+"')");
		pacman.setFitHeight(20);
		pacman.setFitWidth(20);
		pacman.setLayoutX(10);
		pacman.setLayoutY(10);		
		
		 pacmanTl.setCycleCount(Timeline.INDEFINITE);
		 pacmanTl.getKeyFrames().addAll(        
	         new KeyFrame(Duration.millis(150), 
	             (ActionEvent event) -> {
	            	 
	            	 if(!pause && !pacman.isDisabled()) {
		            	 int[] smer = kamJit(x,y);
		            	 
		            	
		            	 if(smer[facing]==1) {
		            		 lastFacing = facing;
		            	 } 
		            	 
		            	 //Pohyb pacmana
		            	 if(lastFacing==0 && smer[0]==1) {
		            		 pacman.setLayoutX(pacman.getLayoutX()+20);
		            		 pacman.setRotate(0);
		            		 x++;	            		 
		            	 }else if(lastFacing==1 && smer[1]==1) {
	            			 pacman.setLayoutY(pacman.getLayoutY()+20);  
	            			 pacman.setRotate(90);
	            			 y++;
		            	 }else if (lastFacing==2 && smer[2]==1) {
	            			 pacman.setLayoutX(pacman.getLayoutX()-20);
	            			 pacman.setRotate(180);
	            			 x--;
		            	 }else if (lastFacing==3 && smer[3]==1) {
	            			 pacman.setLayoutY(pacman.getLayoutY()-20);
	            			 pacman.setRotate(-90);
	            			 y--;
		            	 }
		            	 		            	 
		            	 if(hraciPole[x][y].isVisible()) {
		            		 hraciPole[x][y].setVisible(false);
		            		 score+=10;
		            	 }
	            	 }
	         })
	     );
	   
		 pacmanTl.play();
	}
	
	
	public void jed(KeyEvent event) {
		
		if(pause || pacman.isDisabled()) return;
		
		if(event.getCode()==KeyCode.D || event.getCode()==KeyCode.RIGHT) 
			facing=0;
		if(event.getCode()==KeyCode.S || event.getCode()==KeyCode.DOWN)
			facing=1;
		if(event.getCode()==KeyCode.A || event.getCode()==KeyCode.LEFT)
			facing=2;
		if(event.getCode()==KeyCode.W || event.getCode()==KeyCode.UP)
			facing=3;							
	}	
	
	public int[] kamJit(int x, int y){
	     int[] smer={0,0,0,0};
	     
	     
	     if(hraciPole[x+1][y]!=null){//pravo
	         smer[0]=1;   
	        } 
	        if(hraciPole[x][y+1]!=null){//dole
	         smer[1]=1;   
	        } 
	        if(hraciPole[x-1][y]!=null){//levo
	         smer[2]=1;   
	        } 
	        if(hraciPole[x][y-1]!=null){//nahore
	         smer[3]=1;   
	        } 
	     
	     return smer;
	 }
	
	public void respawn()	 {	
		pacman.setDisable(true);
		lifes[lifeCount-1].setVisible(false);
		lifeCount--;
		
		spawnTmr = new SpawnTimer(list, pacman.getLayoutX()+pacman.getFitWidth()/2, pacman.getLayoutY()+pacman.getFitHeight()/2,pacman.getFitHeight()/2 , 3);
		waitforTimer = new TimerTask() {
    		@Override
    		public void run() {
    			if(spawnTmr.isDone()) {
    				 pacman.setDisable(false);
    				 spawnTmr = null;
    				 waitforTimer.cancel();
    			}
    		}
    	};
    	
    	timer.schedule(waitforTimer, 0,50);
	}
	
	public ImageView getNazev() {
		return pacman;
	}
	
	public Integer getScore() {
		return score;
	}
	
	public void restart() {
		x=startX;
		y=startY;
		
		pacman.setLayoutX(10);
		pacman.setLayoutY(10);	
		
		score = 0;
		lastFacing = 0;
		facing = 0;
		lifeCount = lifes.length;
		
		pause();
	}
	
	public void pause() {
		pause = true;
	}
	
	public void unpause() {
		pause = false;
	}
	
	public int x() {
		return x;
	}
	
	public int y() {
		return y;
	}
	
	public void setLifes(ImageView[] lifes) {
		this.lifes = new ImageView[lifes.length];
		
		for (int i = 0; i < lifes.length; i++) {
			this.lifes[i] = lifes[i];
			
		}
		lifeCount = lifes.length;
	}
	
	public ImageView[] getLifes() {
		return lifes;
	}
	
	public int getHealth() {
		return lifeCount;
	}
}
