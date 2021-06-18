package pacman;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class SpawnTimer {
	
	private Label countdownLabel;
	private Arc loading;
	private Circle bgCircle;
	private Circle outerCircle;
	
	private ObservableList<Node> list;
	private Timeline timeline = new Timeline();
	private double countdown;
	private double arcAdd;
	private boolean done = false;
	
	SpawnTimer(ObservableList<Node> list,double x, double y, double r,double time) {
		this.list = list;
		countdown = time;
		double d =r*2;
		bgCircle = new Circle(x,y,r);
		bgCircle.setFill(Color.web("000000", 0.6));
		this.list.add(bgCircle);
		
		outerCircle = new Circle(x,y,r);
		outerCircle.setFill(Color.TRANSPARENT);
		outerCircle.setStroke(Color.web("#353535"));
		outerCircle.setStrokeWidth(3);
	    this.list.add(outerCircle);
	    
	    countdownLabel = new Label(time+"");
	    countdownLabel.setLayoutX(x-d/2);
	    countdownLabel.setLayoutY(y-d/2);
	    countdownLabel.setTextFill(Color.WHITE);
	    countdownLabel.setText(countdown+"");
	    countdownLabel.setPrefWidth(d);
	    countdownLabel.setPrefHeight(d);
	    countdownLabel.setAlignment(Pos.CENTER);
	    this.list.add(countdownLabel);    
	    
	    loading = new Arc(x, y, r, r, 0, 0);
	    loading.setFill(Color.TRANSPARENT);
	    loading.setStroke(Color.web("ffffff"));
	    loading.setStrokeWidth(3);
	    this.list.add(loading);
	    
	    arcAdd = 360/countdown/10;
	    
	    timeline.setCycleCount(Timeline.INDEFINITE);
	    timeline.getKeyFrames().addAll(        
	         new KeyFrame(Duration.millis(100), 
	             (ActionEvent event) -> {	            	 	            	 
	            	 countdown-=0.1;
	            	 countdownLabel.setText(String.format("%.1f", countdown));
	            	 loading.setLength(loading.getLength()+arcAdd);
	            	 if(countdown<=0) {
	            		 delete();
	            	 }
	         })
	     );
	   
	    timeline.play();
		
	}
	
	public void delete() {
		timeline.stop();
		list.remove(countdownLabel);
		list.remove(loading);
		list.remove(outerCircle);
		list.remove(bgCircle);
		done = true;
	}
	
	public boolean isDone() {
		return done;
	}
	
	public void setFontSize(double size) {
		countdownLabel.setFont(new Font(size));
	}
}
