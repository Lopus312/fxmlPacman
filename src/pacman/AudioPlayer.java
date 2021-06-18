package pacman;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioPlayer {
	
	private Slider slider;
	private Label label;
	private MediaPlayer player;
	private final List<Media> list = new ArrayList<>();
	private final Random random = new Random();
	
	private double volume = 0.25;
	private boolean muted = false;
	

	AudioPlayer(Slider slider, Label label) {
            this.slider = slider;
            this.label = label;

            label.setText(volume+"");
            slider.setValue(volume*100);
			label.setText(Math.round(volume*100)+"%");

            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                volume = (double)newValue/100;
                player.setVolume(volume);
                label.setText(Math.round(volume*100)+"%");

        });
	}
	
	public void addSong(String path) {
		list.add(new Media(new File(path).toURI().toString()));
	}
	
	public void addSongDirectory(String path) {
		File dir = new File(path);
		for(File file : dir.listFiles()) {
			if(file.toString().endsWith(".mp3"))
				list.add(new Media(file.toURI().toString()));
		}
	}
	
	public boolean setVolume(double volume) {		
		if(muted || volume > 1.0 || volume < 0)
			return false;
		this.volume = volume;
		player.setVolume(volume);
		return true;
	}
	
	public void play() {
		player = new MediaPlayer(list.get(random.nextInt(list.size())));
		player.setVolume(volume);
		player.play();
		
		player.setOnEndOfMedia((() -> {
			player = new MediaPlayer(list.get(random.nextInt(list.size())));
			player.setVolume(muted ? 0 : volume);
			player.play();
		}));
		
	}
	
	public void mute() {	
		muted = !muted;
		
		if(!muted) 
			setVolume(volume);
		else player.setVolume(0);;
				
	}
	
	public double getVolume() {
		return volume;
	}
	
	public void pause() {
		player.pause();
	}
	public void resume() {
		player.play();
	}
}
