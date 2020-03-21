package AP_Project;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 * represents the snake in the game.
 *
 */
public class Snake implements java.io.Serializable {
	private ArrayList<Ball> Body;

	
	/**
	 *  default constructor that initialises the body og the snake which is made of
	 * tokens balls
	 */
	public Snake() {
		Body = new ArrayList<Ball>();
	}

	
	/**
	 * @return the arraylist that represents the body of the snake
	 */
	public ArrayList<Ball> getBody() {
		return Body;
	}

	
	/**
	 * @param body sets the arraylist that represents the body of the snake
	 */
	public void setBody(ArrayList<Ball> body) {
		Body = body;
	}

	
	public int getsnakesize() {
		return Body.size();
	}

	
	/**
	 * changes the colour of all the balls of the snake
	 * @param C colour that the snake needs
	 */
	public void changecolor(Color C) {
		for (int i = 0; i < Body.size(); i++) {
			Circle c = (Circle) Body.get(i).getNode();
			c.setFill(C);
		}
	}

	
	public String toString() {
		String res = "";
		for (Ball b : Body) {
			Circle R = (Circle) b.getNode();
			double x = R.getCenterX() + R.getTranslateX();
			double y = R.getCenterY() + R.getTranslateY();
			Color color = (Color) R.getFill();
			String col = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
					(int) (color.getBlue() * 255));
			String tres = x + " " + y + " " + col + "\n";
			res += tres;
		}

		return res;
	}
}
