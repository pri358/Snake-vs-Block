package AP_Project;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

/**
 * This class is a subclass for Tokens and represents the blocks token in the
 * game.
 *
 */
public class Block extends Token implements java.io.Serializable {
	Label num;
	int sc;
	int tsc;

	/**
	 * This is a parametrised constructor that initialses the node, the value on the
	 * block and creates a label for the value.
	 * 
	 * @param n
	 *            this node represents a block and is passed to the constructor of
	 *            the superclass Token.
	 * @param s
	 *            this integer represents the value on a block.
	 */
	public Block(Node n, int s) {
		super(n);
		num = new Label();
		sc = s;
		tsc = sc;
		num.setText(Integer.toString(s));
		num.setTextFill(Color.BLACK);
		num.setFont(new Font("centauri", 20));

	}

	public int getTsc() {
		return tsc;
	}

	public void setTsc(int tsc) {
		this.tsc = tsc;
	}

	public void setNum(Label num) {
		this.num = num;
	}

	public Label getNum() {
		return num;
	}

	public void setNum(int n) {
		num.setText(Integer.toString(n));
	}

	public int getSc() {
		return sc;
	}

	public void setSc(int sc) {
		this.sc = sc;
	}

	@Override
	/**
	 * This function is used to move down the block and sets the speed according to
	 * the length of the snake
	 */
	public void moveDown(int length) {
		double speed = 0;
		if (length < 20)
			speed = 0;
		else if (length <= 40)
			speed = 0.5;
		else if (length <= 60)
			speed = 1;
		else
			speed = 1.5;
		node.setTranslateY(node.getTranslateY() + 4 + speed);
		num.setTranslateY(num.getTranslateY() + 4 + speed);
	}

	/**
	 * This function creates the String version of the object which is used in
	 * serialisation and deserialisation.
	 */
	public String toString() {
		Rectangle R = (Rectangle) node;
		double x = R.getX() + R.getTranslateX();
		double y = R.getY() + R.getTranslateY();
		Color color = (Color) R.getFill();
		String col = String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
				(int) (color.getBlue() * 255));

		return x + " " + y + " " + col + " " + tsc + "\n";

	}
}
