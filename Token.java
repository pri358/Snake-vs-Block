package AP_Project;

import javafx.scene.Node;

/**
 * abstract class to represent all the token in the game.
 *
 */
public abstract class Token implements java.io.Serializable {
	transient Node node;

	/**
	 * Default constructor to initialize the Token.
	 * 
	 * @param n
	 *            represents all the tokens.
	 */
	public Token(Node n) {
		node = n;
	}

	/**
	 * sets the visibility of the token true on the pane
	 */
	public void show() {
		node.setVisible(true);
	}

	/**
	 * hides the token from the pane.
	 */
	 public void hide() {
		node.setVisible(false);
	}

	/**
	 * abstract method that helps the tokens to move down and is overriden by all
	 * the subclasses.
	 * @param length length of the snake
	 */
	public abstract void moveDown(int length);

	/**
	 * @return the current node
	 */
	public Node getNode() {
		return node;
	}

	
	/**
	 * @return the visibility of the node.
	 */
	public boolean isVisibile() {
		return node.isVisible();
	}
}
