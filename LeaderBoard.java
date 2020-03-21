package AP_Project;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * this is the Leaderboard class which represents the top10 scores in the game
 *
 */
public class LeaderBoard implements Serializable {
	ArrayList<Tuple> top10;

	// int highscore;

	transient Button Back;

	transient Group P;

	transient Scene S;

	transient ArrayList<Text> index;
	transient ArrayList<Text> date;
	transient ArrayList<Text> lscore;

	
	/**
	 * Default Constructor to initialise the values of the variables
	 */
	public LeaderBoard() {

		top10 = new ArrayList<Tuple>();
		index = new ArrayList<Text>();
		date = new ArrayList<Text>();
		lscore = new ArrayList<Text>();

		// highscore=0;

		Text heading = new Text();
		heading.setText("\nLEADERBOARD");
		heading.setX(50);
		heading.setY(70);
		heading.setFont(Font.font("centauri", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 35));
		heading.setFill(Color.WHITE);

		Back = new Button();
		Back.setText("Go to Main menu");
		Back.setStyle(
				"-fx-background-color: transparent; -fx-text-fill:#fedcba; -fx-font-size: 15px; -fx-font-family: \"centauri\";");

		P = new Group();

		P.getChildren().addAll(Back, heading);

		S = new Scene(P, 325, 550);
		S.setFill(Color.BLACK);
	}

	
	/**
	 * This functions is used to initialise and set the labels on the screen.
	 */
	public void initialise() {
		// index.clear(); lscore.clear(); date.clear();

		for (int i = 0; i < 10; i++) {

			index.add(new Text(Integer.toString(i + 1)));
			lscore.add(new Text());
			date.add(new Text());
		}
		for (int i = 0; i < 10; i++) {
			index.get(i).setX(30);
			index.get(i).setY(140 + i * 30);
			index.get(i).setFill(Color.WHITE);
			index.get(i).setFont(Font.font("centauri", 20));
			lscore.get(i).setX(80);
			lscore.get(i).setY(140 + i * 30);
			lscore.get(i).setFill(Color.WHITE);
			lscore.get(i).setFont(Font.font("centauri", 20));
			date.get(i).setX(150);
			date.get(i).setY(140 + i * 30);
			date.get(i).setFill(Color.WHITE);
			date.get(i).setFont(Font.font("centauri", 20));
		}
		for (int i = 0; i < top10.size(); i++) {
			lscore.get(i).setText(Integer.toString(top10.get(i).getScore()));
			date.get(i).setText(top10.get(i).getShowdate());
			P.getChildren().add(lscore.get(i));
			P.getChildren().add(date.get(i));
			// P.getChildren().add(index.get(i));
		}
		P.getChildren().addAll(index);

	}

	/**
	 * 
	 * @return the list of top 10 scores.
	 */
	public ArrayList<Tuple> getTop10() {
		return top10;
	}

	/**
	 * 
	 * @param top10
	 *            the list of top10 scores with the dates.
	 */
	public void setTop10(ArrayList<Tuple> top10) {
		this.top10 = top10;
	}

	/**
	 * this function sets the leaderboard on the stage
	 * 
	 * @param pStage
	 *            stage of the game
	 * @param S1
	 *            scene on MainMenu which is set when back button is clicked.
	 */
	public void setLeaderBoard(Stage pStage, Scene S1) {
		sort();
		pStage.setScene(S);
		// System.out.println("enter2");
		Back.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				// System.out.println("ENTER");
				pStage.setScene(S1);
			}
		});
	}

	/**
	 * this function is used to update the score list when a new score is made by
	 * comparing with the existing list of scores.
	 * 
	 * @param score
	 *            most recent score
	 */
	public void update_score(int score) {
		Tuple t = new Tuple();
		t.setScore(score);
		if (top10.size() < 10) {
			// System.out.println(" new Score added");
			top10.add(t);
			date.get(top10.size() - 1).setText(t.getShowdate());
			lscore.get(top10.size() - 1).setText(Integer.toString(t.getScore()));
			P.getChildren().add(date.get(top10.size() - 1));
			P.getChildren().add(lscore.get(top10.size() - 1));
		} else {
			for (int i = 0; i < 10; i++) {
				if (top10.get(i).getScore() < score) {
					// System.out.println("Score updated ");
					top10.remove(top10.size() - 1);
					top10.add(t);
					date.get(top10.size() - 1).setText(t.getShowdate());
					lscore.get(top10.size() - 1).setText(Integer.toString(t.getScore()));
					date.add(new Text(t.getShowdate()));
					lscore.add(new Text(Integer.toString(t.getScore())));
					break;
				}
			}
		}
		sort();
	}

	/**
	 * used to sort the list of top 10 scores in descending order, uses collection
	 * to do it and sets the labels accordingly.
	 */
	public void sort() {

		Collections.sort(top10);

		for (int i = 0; i < top10.size(); i++) {
			date.get(i).setText(top10.get(i).getShowdate());
			lscore.get(i).setText(Integer.toString(top10.get(i).getScore()));
		}

	}

}
