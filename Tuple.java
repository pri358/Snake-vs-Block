package AP_Project;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * this class represents the scores along with date which are stored in the
 * leaderboard.
 *
 */

public class Tuple implements Serializable, Comparable<Tuple> {
	private int score;
	transient Date d;
	String showdate;

	/**
	 * initializes the score as 0 and sets the date as the current date.
	 */
	public Tuple() {
		// TODO Auto-generated constructor stub
		score = 0;
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy ");
		showdate = sdf.format(d);
	}

	/**
	 * 
	 * @return the score of the tuple
	 */
	public int getScore() {
		return score;
	}

	/**
	 * sets the score
	 * 
	 * @param score
	 */
	public void setScore(int score) {
		this.score = score;
	}

	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}

	public String getShowdate() {
		return showdate;
	}

	public void setShowdate(String showdate) {
		this.showdate = showdate;
	}

	/**
	 * function that helps in the sorting of the scores.
	 */
	public int compareTo(Tuple T1) {
		return -this.getScore() + T1.getScore();
	}

}
