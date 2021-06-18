/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.Scene;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


public class FXMLDocumentController implements Initializable {
    
    @FXML
    private AnchorPane kontejner;
    @FXML
    private AnchorPane menu;
    @FXML
    private AnchorPane vt_pane;
    @FXML
    private Label Lscore;   
    @FXML
    private Slider sliderVolume;
    @FXML
    private CheckBox chkbxMute;
    @FXML
    private Label labelVolume;
    @FXML
    private ImageView imgLife1;
    @FXML
    private ImageView imgLife2;
    @FXML
    private ImageView imgLife3;
    @FXML
    private ImageView imgLife4;
    @FXML
    private ImageView imgLife5;
    @FXML
    private AnchorPane go_pane;
    @FXML
    private Label go_score;
    @FXML
    private Label vt_score;
    @FXML
    private Label vt_lifes;


    private Duch[] duchove = new Duch[4];
    private ImageView[] lifes = new ImageView[3];
    private AudioPlayer audioPlayer;
    private MediaPlayer deathEffect;
    private MediaPlayer gameOverMedia;
    private MediaPlayer victoryMedia;
    private final String victoryMediaPath = "soundEffects/VictoryFanfare.mp3";
    private final String deathEffectPath = "soundEffects/OOF.mp3";
    private final String gameOverMediaPath = "soundEffects/CurbYourEnthusiasm.mp3";
    private ObservableList<Node> list;
    private Circle[][] hraciPole;
    private Mapa m;
    private Pacman pacman;
    private Timeline update = new Timeline();
	private SpawnTimer myTimer;
	private Timer timer = new Timer();
	private TimerTask task;
	private Scene scene;
    
    private boolean gameOver = false;
    private String musicDirectory = "music";
    private boolean firstTime = true;
    private boolean pausable = true;

    //mapa
    public void play() {
    	if(firstTime) {
    		firstTime = false;
			m=new Mapa(list);
		    m.mapa1();
		    hraciPole=m.getHraciPole();  
		    
		    lifes[0] = imgLife1;
		    lifes[1] = imgLife2;
		    lifes[2] = imgLife3;

		    for(ImageView life : lifes) {
				life.setVisible(true);
			}
		    
		    pacman=new Pacman(hraciPole,1,1,list,"pacman.png");   
		    duchove[0]=new Duch(hraciPole,1,10,list, "Blinky.png");
		    duchove[1] = new Duch(hraciPole,hraciPole.length-2, hraciPole.length-2,list,"Pinky.png");
		    duchove[2] = new Duch(hraciPole,hraciPole.length-2,1,list,"Inky.png");
		    duchove[3] = new Duch(hraciPole,1,hraciPole.length-2,list,"Clyde.png");
		    
		    
			for(Duch duch : duchove) {
				list.add(duch.getNazev());
				duch.setPacman(pacman);
			}
			
			list.add(pacman.getNazev());
			
			pacman.setLifes(lifes);
			update.play();
			audioPlayer.play();
			menu.toFront();
			go_pane.toFront();
			vt_pane.toFront();

			kontejner.requestFocus();
			
			
			 for(Duch duch : duchove) 
				 duch.pohyb();	 	 
			
    	}
    	
    }
    
    //hra
    @FXML
    void onKeyPressed(KeyEvent event) throws IOException {
    	if(pausable && event.getCode() == KeyCode.ESCAPE)
    		toggleMenu();
    	
    	pacman.jed(event);
    	
    }
    
    //Options menu
    void toggleMenu () {
    	menu.setVisible(!menu.isVisible());
    	//pause game	
    	if(menu.isVisible()) 
    		pause();
    	else unpause();
    }
    
    public void pause() {
    	pacman.pause();
		for(Duch duch : duchove)
			duch.pause();
    }
    
    public void unpause() {
    	pacman.unpause();
		for(Duch duch : duchove)
			duch.unpause();
    }
    
    public void stop(){
    	
    	for(Duch duch : duchove)
    		duch.getTimerDuch().stop();
    }
    
    @FXML
    void restart() {
    	pausable = false;
    	vt_pane.setVisible(false);
    	go_pane.setVisible(false);
    	menu.setVisible(false);  	    	 
        
        for(ImageView life : lifes)
        	life.setVisible(true);
        
        pacman.restart();
        
        for(Duch duch : duchove) { 
        	duch.restart();
        }
        
        
        for (int i = 0; i < hraciPole[0].length; i++) {
			for (int j = 0; j < hraciPole.length; j++) {
				if(hraciPole[i][j] != null)
					hraciPole[i][j].setVisible(true);
			}		
		}
		
        myTimer = new SpawnTimer(list,kontejner.getWidth()/2,kontejner.getWidth()/2,200,5);
        myTimer.setFontSize(150);
        
        task = new TimerTask() {
        	@Override
        	public void run() {
        		if(myTimer.isDone()) {
        			
        			for(Duch duch : duchove) { 
        	        	duch.unpause();
        	        }
        			
        			gameOverMedia.stop();
        			victoryMedia.stop();
        			audioPlayer.play();
        			pacman.unpause();
        			gameOver = false;
        			myTimer = null;
        			pausable = true;
        			task.cancel();
        		}
        	}
        };
        
        timer.schedule(task, 0,10);


        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    		
          	list=kontejner.getChildren();
  		
          	//Update score
          	update.setCycleCount(Timeline.INDEFINITE);
          	update.getKeyFrames().addAll(        
          			new KeyFrame(Duration.millis(1), 
          					(ActionEvent event) -> {	            	 	            	 
          						//updating score
          						Lscore.setText(String.format("SCORE: %d", pacman.getScore()));
          						         						
          						
          						//checking for crash
          						for(Duch duch : duchove)
	          						if(!gameOver && !duch.getNazev().isDisabled() && !pacman.getNazev().isDisabled() && pacman.x == duch.x() && pacman.y == duch.y()) {
	          							
	          							if(pacman.getHealth()<=0) {
	          								gameOver = true;
	          								go_pane.setVisible(true);
	          								go_score.setText(pacman.getScore()+"");
	          								pacman.pause();
	          								for(Duch duch2 : duchove)
	          									duch2.pause();
	          								gameOverMedia = new MediaPlayer(new Media(new File(gameOverMediaPath).toURI().toString()));
	          								gameOverMedia.setVolume(audioPlayer.getVolume());
	          								gameOverMedia.play();
	          								audioPlayer.pause();
	          								return;
	          							}
	          							
	          							pacman.respawn();
	          							deathEffect = new MediaPlayer(new Media(new File(deathEffectPath).toURI().toString()));
	          							deathEffect.setVolume(audioPlayer.getVolume());
	          							deathEffect.play();
	          							
	          						}
          						if(!gameOver && pacman.getScore()==2770) {
          							gameOver = true;
          							pacman.pause();
          							for(Duch duch : duchove)
      									duch.pause();
          							vt_pane.setVisible(true);
          							vt_score.setText(pacman.getScore()+"");
          							vt_lifes.setText(pacman.getHealth()+"");
          							victoryMedia = new MediaPlayer(new Media(new File(victoryMediaPath).toURI().toString()));
          							victoryMedia.setVolume(audioPlayer.getVolume());
          							victoryMedia.play();
          							audioPlayer.pause();
          							
          						}
	       		            		
	          						
			        	 
          					}));
          	//setting up Audio players
			audioPlayer = new AudioPlayer(sliderVolume,labelVolume);
			audioPlayer.addSongDirectory(musicDirectory);
			gameOverMedia = new MediaPlayer(new Media(new File(gameOverMediaPath).toURI().toString()));
			victoryMedia = new MediaPlayer(new Media(new File(victoryMediaPath).toURI().toString()));
    }    
    
    @FXML
    public void mute() {   	
    	audioPlayer.mute();
    	  		
    	sliderVolume.setDisable(!sliderVolume.isDisabled());
    }
    
    
    
    public void changeScore() {
    	Lscore.setText("Hej");
    }

    public void setScene(Scene scene){
    	this.scene = scene;
	}
}
