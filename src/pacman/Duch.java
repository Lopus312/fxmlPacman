/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class Duch {
	private ImageView duch;
	private Timer timer = new Timer();
	private TimerTask waitforTimer;
	private Circle[][] hraciPole;
	private Timeline duchTl = new Timeline();
	private Random random = new Random();
	private SpawnTimer spawnTmr;
	private Pacman pacman;
	private Circle spawnCircle;
	private ObservableList<Node> list;
	private int respawnTime = 2;

//smer kam jde duch
	private int smer2 = 0;
	private int startX,startY;
	private int duchX = 1;
	private int duchY = 1;
	private double spawnTime;
	private boolean pause = false;

	/**
	 *
	 * @param hraciPole
	 */
	public Duch(Circle[][] hraciPole, int startX, int startY, ObservableList<Node> list, String file) {
		this.hraciPole = hraciPole;
		this.startX = startX;
		this.startY = startY;
		duchX = startX;
		duchY = startY;
		this.spawnTime = 20;
		this.list = list;

		duch = new ImageView();
		duch.setStyle("-fx-image: url('file:" + file + "')");
		duch.setFitHeight(20);
		duch.setFitWidth(20);
		duch.setLayoutX(10);
		duch.setLayoutY(10);
		if (startX != 1 || startY != 1) {
			duch.setLayoutX(duch.getLayoutX() + 20 * (duchX - 1));
			duch.setLayoutY(duch.getLayoutY() + 20 * (duchY - 1));
		}

	}

	/**
	 * po okraji, v rohu mění náhodně směr
	 */
	public void pohyb() {
	

		duchTl.setCycleCount(Timeline.INDEFINITE);
		duchTl.getKeyFrames().addAll(new KeyFrame(Duration.millis(100), (ActionEvent event) -> {
			if (!pause) {

				int[] volneCesty = kamJit(duchX, duchY);
				
				//pokud je pacman mrtev a duch je k nemu blizko, zmen smer
				if (pacman.getNazev().isDisabled() && Math.abs(duchX - pacman.x) < 3)
					zmenSmer();

				// pocetCest = po kolika cestach muze pacman jet
				int pocetCest = 0;
				for (int x : volneCesty) {
					if (x != 0)
						pocetCest++;
				}

				// pokud ma na vyber z v�ce nez dvou cest, nahodne vybere jednu
				if (!pacman.getNazev().isDisabled() && pocetCest > 2)
					zmenSmer();
				else if (volneCesty[smer2] != 1)// pokud nema na vyber a ani nemuze pokracovat, tak zmen smer
					zmenSmer();

				if (smer2 == 0) {
					duch.setLayoutX(duch.getLayoutX() + 20);
					duchX++;
				} else if (smer2 == 1) {
					duch.setLayoutY(duch.getLayoutY() + 20);
					duchY++;
				} else if (smer2 == 2) {
					duch.setLayoutX(duch.getLayoutX() - 20);
					duchX--;
				} else if (smer2 == 3) {
					duch.setLayoutY(duch.getLayoutY() - 20);
					duchY--;
				}
			}

		}));
		duchTl.play();
	}

	public int[] kamJit(int x, int y) {
		int[] smer = { 0, 0, 0, 0 };

		if (hraciPole[x + 1][y] != null) {
			smer[0] = 1;
		}
		if (hraciPole[x][y + 1] != null) {
			smer[1] = 1;
		}
		if (hraciPole[x - 1][y] != null) {
			smer[2] = 1;
		}
		if (hraciPole[x][y - 1] != null) {
			smer[3] = 1;
		}

		return smer;
	}

	public void zmenSmer() {
		int[] volneCesty = kamJit(duchX, duchY);

		// pokud je duch blizko pacmanovi
		if (pacman.getNazev().isDisabled() && Math.abs(duchX - pacman.x) < 7 && Math.abs(duchY - pacman.y) < 7) {
			int positiveX = volneCesty[0] == 1 ? Math.abs(duchX + 20 - pacman.x) : 0;
			int negativeX = volneCesty[2] == 1 ? Math.abs(duchX - 20 - pacman.x) : 0;
			int positiveY = volneCesty[1] == 1 ? Math.abs(duchY + 20 - pacman.y) : 0;
			int negativeY = volneCesty[3] == 1 ? Math.abs(duchY - 20 - pacman.y) : 0;
			if (positiveX > negativeX && positiveX > positiveY && positiveX > negativeY) {
				smer2 = 0;
				return;
			}
			if (negativeX > positiveX && negativeX > positiveY && negativeX > negativeY) {
				smer2 = 2;
				return;
			}
			if (positiveY > positiveX && positiveY > negativeX && positiveY > negativeY) {
				smer2 = 1;
				return;
			}
			if (negativeY > positiveX && negativeY > negativeX && negativeY > positiveX) {
				smer2 = 3;
				return;
			}

		}

		while (true) {
			int i = random.nextInt(4);
			if (volneCesty[i] != 0) {
				smer2 = i;
				return;
			}
		}
	}

	/**
	 * @return the nazev
	 */
	public ImageView getNazev() {
		return duch;
	}

	public void restart() {
		duchX = startX;
		duchY = startY;
		
		duch.setLayoutX(10);
		duch.setLayoutY(10);
		if (startX != 1 || startY != 1) {
			duch.setLayoutX(duch.getLayoutX() + 20 * (duchX - 1));
			duch.setLayoutY(duch.getLayoutY() + 20 * (duchY - 1));
		}
		
		pause();
	}
	/**
	 * @return the timer
	 */
	public Timeline getTimerDuch() {
		return duchTl;
	}

	public void pause() {
		pause = true;
	}

	public void unpause() {
		pause = false;
	}

	public void setPacman(Pacman pacman) {
		this.pacman = pacman;
	}

	public int x() {
		return duchX;
	}

	public int y() {
		return duchY;
	}

	public double width() {
		return duch.getFitWidth();
	}

	public double height() {
		return duch.getFitHeight();
	}
}
