package AP_Project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import org.omg.CORBA.portable.RemarshalException;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * This class carries out main functionality of the game and renders GUI according to user input
 */
public class Game extends Application implements java.io.Serializable {
	LeaderBoard L;

	Scene MainPage;

	Group V;
	
	int collect;

	Button StartGame;
	Button ExitGame;
	Button ShowLeaderBoard;

	Stage stage;

	int score;
	int m_score;

	Label showScore;
	Label _score;
	Label _length;
	Label showLength;
	Button ResumeGame;
	Label showLife;   
	Label _lifes;

	Media coinmedia;
	MediaPlayer coinmediaplayer;
	Media bombmedia;
	MediaPlayer bombmediaplayer;
	Media shieldmedia;
	MediaPlayer shieldmediaplayer;
	Media magnetmedia;
	MediaPlayer magnetmediaplayer;
	Media over;
	MediaPlayer overmediaplayer;

	Text score_p;
	Text score_l;
	
	int t_lives;

	int highscore;

	Snake snake;

	Group P;
	double t = 0;
	Random R = new Random();

	long lastcall = 0;

	AnimationTimer T;

	ArrayList<Entity> dying = new ArrayList<Entity>();
	ArrayList<Entity> dyingsnake = new ArrayList<Entity>();

	ArrayList<Wall> walls = new ArrayList<Wall>();
	ArrayList<Block> blocks = new ArrayList<Block>();
	ArrayList<Magnet> magnets = new ArrayList<Magnet>();
	ArrayList<Shield> shields = new ArrayList<Shield>();
	ArrayList<Ball> balls = new ArrayList<Ball>();
	ArrayList<ImageView> lifes = new ArrayList<ImageView>();

	ArrayList<DestroyBlock> dBlocks = new ArrayList<DestroyBlock>();

	javafx.scene.paint.Color block_colors[] = { Color.CHARTREUSE, Color.BLANCHEDALMOND, Color.DARKSALMON,
			Color.DEEPSKYBLUE, Color.GAINSBORO, Color.INDIANRED };

	Random random = new Random();

	public Game() throws FileNotFoundException, ClassNotFoundException, IOException {
		L = new LeaderBoard();

		ArrayList<Tuple> l = null;
		l = DeSerializeL();
		if (l != null) {
			L.setTop10(l);
			L.initialise();
		} else {
			L.initialise();
		}

		highscore = 0;

		for (int i = 0; i < L.getTop10().size(); i++) {
			if (L.getTop10().get(i).getScore() > highscore)
				highscore = L.getTop10().get(i).getScore();
		}

		V = new Group();

		m_score = DeserializeScore();

		initialize();

		T = new AnimationTimer() {
			public void handle(long now) {
				try {
					onTimer();
				} catch (GameOverException E) {
					try {
						SerializeScore();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// L.sort();
					L.update_score(score);
					// L.sort();
					if (score > highscore)
						highscore = score;
					score_l.setText(String.valueOf(highscore));

					// L.setLeaderBoard(stage, MainPage);

					// P.getChildren().remove(score_l);
					// P.getChildren().add(score_l);

					score_p.setText(Integer.toString(score));
					long t = System.currentTimeMillis();
					stage.setScene(MainPage);
					T.stop();
					emptyfiles();
					t = 0;
				} catch (Exception E) {
					;
				}

			}
		};
	}
	
	/**
	 * Method to initialize game variables and construct GUI components
	 */
	public void initialize() {
		ResumeGame = new Button();
		StartGame = new Button();
		ExitGame = new Button();
		ShowLeaderBoard = new Button();
		
		collect=0;
		t_lives=0;

		ResumeGame.setText("Resume Game");
		StartGame.setText("Start Game");
		ExitGame.setText(" Exit Game");
		ShowLeaderBoard.setText("Show LeaderBoard");
		ResumeGame.setTranslateX(85);
		ResumeGame.setTranslateY(370);
		StartGame.setTranslateX(100);
		StartGame.setTranslateY(410);
		ShowLeaderBoard.setTranslateX(70);
		ShowLeaderBoard.setTranslateY(450);
		ExitGame.setTranslateX(100);
		ExitGame.setTranslateY(490);

		StartGame.setStyle(
				"-fx-background-color: transparent; -fx-text-fill:#c70cff; -fx-font-size: 20px; -fx-font-family: \"centauri\";");
		ExitGame.setStyle(
				"-fx-background-color: transparent; -fx-text-fill:#c70cff; -fx-font-size: 20px; -fx-font-family: \"centauri\";");
		ShowLeaderBoard.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: #c70cff; -fx-font-size: 20px; -fx-font-family: \"centauri\";");
		ResumeGame.setStyle(
				"-fx-background-color: transparent; -fx-text-fill: #c70cff; -fx-font-size: 20px; -fx-font-family: \"centauri\";");

		Text Snake = new Text();
		Snake.setText("Snake");
		Snake.setFill(Color.WHITE);
		Snake.setFont(Font.font("centauri", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 50));
		Snake.setX(100);
		Snake.setY(100);

		Text vs = new Text();
		vs.setText("VS");
		vs.setFill(Color.WHITE);
		vs.setFont(Font.font("centauri", FontWeight.EXTRA_BOLD, FontPosture.REGULAR, 40));
		vs.setX(140);
		vs.setY(160);

		Text blocks = new Text();
		blocks.setText("Blocks");
		blocks.setFill(Color.WHITE);
		blocks.setFont(Font.font("centauri", FontWeight.EXTRA_LIGHT, FontPosture.REGULAR, 50));
		blocks.setX(100);
		blocks.setY(220);

		ImageView im1 = new ImageView(new Image("/AP_Project/white-crown.png"));
		im1.setTranslateX(100);
		im1.setTranslateY(300);
		im1.setFitWidth(70);
		im1.setFitHeight(70);

		score_l = new Text();
		score_l.setText(Integer.toString(highscore));
		score_l.setFill(Color.WHITE);
		score_l.setFont(Font.font("centauri", FontWeight.NORMAL, FontPosture.REGULAR, 40));
		score_l.setX(170);
		score_l.setTranslateY(350);

		Text Show_score = new Text();
		Show_score.setText("Score :");
		Show_score.setFill(Color.RED);
		Show_score.setFont(Font.font("centauri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
		Show_score.setX(100);
		Show_score.setTranslateY(300);
		

		score_p = new Text();
		score_p.setText(Integer.toString(m_score));
		score_p.setFill(Color.RED);
		score_p.setFont(Font.font("centauri", FontWeight.NORMAL, FontPosture.REGULAR, 30));
		score_p.setX(200);
		score_p.setTranslateY(300);

		V.getChildren().addAll(StartGame, ExitGame, ShowLeaderBoard, Snake, Show_score, vs, blocks, im1, score_p,
				score_l, ResumeGame);

		MainPage = new Scene(V, 325, 550, Color.BLACK);

		snake = new Snake();

		score = 0;
		showScore = new Label();
		showScore.setTranslateX(265);
		showScore.setTranslateY(10);
		showScore.setStyle("-fx-text-fill: white; -fx-font-family:\"Ïmpact\"; -fx-font-size: 30px;");

		_length = new Label();
		_score = new Label();
		_lifes = new Label();

		_score.setTranslateX(265);
		_score.setTranslateY(10);
		_score.setText("SCORE: ");
		_score.setStyle("-fx-text-fill: white; -fx-font-family:\"Ïmpact\"; -fx-font-size: 10px;");

		showLength = new Label();
		showLength.setTranslateX(265);
		showLength.setTranslateY(45);
		showLength.setStyle("-fx-text-fill: white; -fx-font-family:\"Ïmpact\"; -fx-font-size: 30px;");

		_length.setTranslateX(265);
		_length.setTranslateY(45);
		_length.setText("LENGTH: ");
		_length.setStyle("-fx-text-fill: white; -fx-font-family:\"Ïmpact\"; -fx-font-size: 10px;");
		
		showLife = new Label();
		showLife.setTranslateX(265);
		showLife.setTranslateY(90);
		showLife.setStyle("-fx-text-fill: white; -fx-font-family:\"Ïmpact\"; -fx-font-size: 30px;");
		
		_lifes.setTranslateX(265);
		_lifes.setTranslateY(90);
		_lifes.setText("LIFES: ");
		_lifes.setStyle("-fx-text-fill: white; -fx-font-family:\"Ïmpact\"; -fx-font-size: 10px;");
		
		

	}

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Start method is the entry point to any javafx application. Event handlers for various buttons are declared here.
	 */
	@Override
	public void start(Stage primaryStage) throws FileNotFoundException, ClassNotFoundException, IOException {
		primaryStage.setTitle("Snake VS Blocks");
		primaryStage.setScene(MainPage);
		primaryStage.show();
		primaryStage.setResizable(false);
		stage = primaryStage;

		stage.setOnCloseRequest(e -> {
			try {
				SerializeScore();
				SerializeG();
				SerializeL(L.getTop10());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});

		ShowLeaderBoard.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				L.setLeaderBoard(primaryStage, MainPage);
			}
		});

		ExitGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				try {
					SerializeL(L.getTop10());
					SerializeScore();
				} catch (Exception e1) {
					;
				}
				primaryStage.close();
			}
		});

		StartGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				P = new Group();
				startGame(false);
			}
		});

		ResumeGame.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				boolean how = true;
				try {
					File f = new File("snake.txt");
					if (f.length() == 0) {
						System.out.println("Can not resume game. Starting new game.");
						P = new Group();
						how = false;
					}

					deSerializeG();
				} catch (IOException e1) {
					;
				}
				startGame(how);
			}
		});
	}

	/**
	 * @param res
	 * res indicates if the we are resuming a game or starting a new one
	 * method initializes/sets lists of all tokens, score, javafx Pane and the snake
	 */
	public void startGame(boolean res) {

		if (!res) {
			P = new Group();
			snake.getBody().clear();
			walls.clear();
			balls.clear();
			blocks.clear();
			magnets.clear();
			shields.clear();
			dBlocks.clear();
			lifes.clear();
			score = 0;
			collect=0;
			t_lives=0;
			showScore.setText("0");
			showLife.setText("1");
			for (int i = 0; i < 4; i++) {
				snake.getBody().add(new Ball(new Circle(175, 400 + i * 20, 10, Color.GOLD)));

			}

			for (Ball B : snake.getBody())
				P.getChildren().add(B.getNode());
		}

		P.getChildren().addAll(showScore, _score, showLength, _length, _lifes, showLife);

		showScore.setTextFill(Color.WHITE);
		showLife.setTextFill(Color.WHITE);

		// String coinsound = "coincound.mp3";
		// media = new Media(new File(coinsound).toURI().toString());
		// mediaplayer = new MediaPlayer(media);

		// P.getChildren().add(mediaplayer);

		showLength.setTextFill(Color.WHITE);
		showLength.setText("4");

		stage.setTitle("Snake vs Blocks");
		ChoiceBox<String> dropmenu = new ChoiceBox<String>();
		dropmenu.getItems().addAll("Go to Main Menu", "Restart", "Exit", "MENU");
		dropmenu.setValue("MENU");
		dropmenu.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font: 16px \"Impact\";");

		dropmenu.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
			try {
				menuclick(newVal);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		dropmenu.setTranslateX(30);
		dropmenu.setTranslateY(10);

		P.getChildren().add(dropmenu);

		Scene S = new Scene(P, 325, 550, Color.BLACK);

		stage.setScene(S);

		T.start();

		S.setOnKeyPressed(e -> {
			switch (e.getCode()) {
			case LEFT:
				moveLeft();
				break;
			case RIGHT:
				moveRight();
				break;
			case P:
				T.stop();
				//System.out.println("here");
				break;
			case L:
				T.start();
				break;
			default:
				;
			}
		});

	}

	/**
	 * Method to spawn the block token. Probability for spawning five blocks in a row: 0.55
	 * Probability for spawning less than 5 blocks is 0.45, number of blocks are determined randomly at runtime
	 * Label between blocks is a random function depending on snake length
	 * In case of five blocks, it is ensured that at least one has points less than snake length
	 * More than one block may have score less than snake length(Determined randomly at run time)
	 * It is ensured that no collisions occur, we try to spawn a token thrice, if it collides with
	 * something else in all three tries, we abort spawning.
	 */
	public void spawn_block() {

		float t = random.nextFloat();

		if (t > 0.45) {
			int x = random.nextInt(5);

			for (int i = 0; i < x; i++) {
				int tries = 0;

				while (tries < 3) {
					int x1 = random.nextInt(270);

					Rectangle R = new Rectangle(x1, -100, 65, 60);

					int pts = 3, slen = snake.getBody().size();
					if (slen <= 10)
						pts = 3;
					else if (slen <= 20)
						pts = 5;
					else if (slen <= 50)
						pts = 10;
					else
						slen = 15;

					Block addR = new Block(R, snake.getsnakesize() + random.nextInt(pts));
					addR.num.setLayoutX(x1 + 30);
					addR.num.setLayoutY(-100 + 15);
					R.setFill(block_colors[random.nextInt(5)]);
					R.setArcHeight(10.0);
					R.setArcWidth(10.0);
					R.setStrokeWidth(1);
					R.setStroke(Color.BLACK);

					if (!canAdd(R)) {
						tries++;
					} else {
						P.getChildren().addAll(R, addR.num);
						blocks.add(addR);
						break;
					}
				}
			}
		} else {
			int pts[] = new int[5];
			int slen = snake.getsnakesize();

			if (slen == 1)
				pts[random.nextInt(5)] = 1;
			else
				pts[random.nextInt(5)] = random.nextInt(slen - 1) + 1;
			int smaller = random.nextInt(2) + 3;

			for (int i = 0; i < smaller; i++) {
				if (pts[i] == 0)
					pts[i] = random.nextInt(slen - 1) + 1;
			}

			for (int i = 0; i < 5; i++) {
				if (pts[i] == 0) {
					pts[i] = slen + random.nextInt(10);
				}
			}

			for (int i = 0; i < 5; i++) {

				Rectangle R1 = new Rectangle(i * 65, -100, 65, 60);
				Block addR1 = new Block(R1, pts[i]);
				addR1.num.setLayoutX(i * 65 + 30);
				addR1.num.setLayoutY(-100 + 15);
				R1.setFill(block_colors[i]);
				R1.setStrokeWidth(1);
				R1.setStroke(Color.BLACK);
				R1.setArcHeight(10.0);
				R1.setArcWidth(10.0);
				P.getChildren().addAll(R1, addR1.num);
				blocks.add(addR1);
			}
		}

	}

	/**
	 * Method to spawn ball token. Position is determined randomly.
	 * Score on point is a random function depending on snake length.
	 * No collisions are ensured, each token is given three chances. 
	 */
	public void spawn_ball() {
		int n = random.nextInt(3) + 1;

		for (int i = 0; i < n; i++) {

			int x = random.nextInt(300) + 10;
			int y = random.nextInt(70);

			int tries = 0;
			Circle C = new Circle(x, -100 + y, 15, Color.IVORY);
			int pts = 0;

			int slen = snake.getBody().size();
			if (slen <= 20)
				pts = random.nextInt(5) + 1;
			if (slen <= 50)
				pts = random.nextInt(10) + 1;
			else
				pts = random.nextInt(15) + 1;

			Ball addC = new Ball(C, pts);
			addC.getNum().setLayoutX(x - 3);
			addC.getNum().setLayoutY(-110 + y);
			C.setFill(block_colors[random.nextInt(5)]);

			while (tries < 3) {
				if (!canAdd(C)) {
					tries++;
				} else {
					P.getChildren().addAll(C, addC.getNum());
					balls.add(addC);
					break;
				}

			}
		}
	}

	/**
	 * Method to spawn magnet, shield, destroy block, life token
	 * Probability for shield: 1/3
	 * Probability for destroy blocks: 1/3
	 * Probability for magnet: 1/3
	 * Probability for life: 1/5
	 */
	public void spawn_token() {

		float f = random.nextFloat();
		if (f <= 0.33) {
			int tries = 0;
			ImageView im1 = new ImageView(new Image("/AP_Project/bomb.png"));
			im1.setTranslateX(random.nextInt(320));
			im1.setTranslateY(-50);
			im1.setFitWidth(40);
			im1.setFitHeight(40);

			DestroyBlock addIm1 = new DestroyBlock(im1);
			while (tries < 100) {
				if (!canAdd(im1)) {
					tries++;
				} else {
					P.getChildren().add(im1);
					dBlocks.add(addIm1);
					break;
				}

			}
		}

		else if (f <= 0.66) {
			int tries = 0;
			ImageView im1 = new ImageView(new Image("/AP_Project/magnet.png"));
			im1.setTranslateX(random.nextInt(320));
			im1.setTranslateY(-50);
			im1.setFitWidth(40);
			im1.setFitHeight(40);
			Magnet M = new Magnet(im1);
			while (tries < 100) {
				if (!canAdd(im1)) {
					tries++;
				} else {
					P.getChildren().add(im1);
					magnets.add(M);
					break;
				}

			}
		} else {
			int tries = 0;
			ImageView im1 = new ImageView(new Image("/AP_Project/shield.png"));
			im1.setX(random.nextInt(300));
			im1.setY(-50);
			im1.setFitWidth(40);
			im1.setFitHeight(40);

			Shield S = new Shield(im1);
			while (tries < 100) {
				if (!canAdd(im1)) {
					tries++;
				} else {
					P.getChildren().add(im1);
					shields.add(S);
					break;
				}

			}
		}
		if (f <= 0.2) {
			int tries = 0;
			ImageView im5 = new ImageView(new Image("/AP_Project/tenor.gif"));
			if (t_lives >= 2)
				return;
			im5.setTranslateX(random.nextInt(320));
			im5.setTranslateY(-50);
			im5.setFitWidth(40);
			im5.setFitHeight(40);
			// Magnet M = new Magnet(im5);
			while (tries < 100) {
				if (!canAdd(im5)) {
					tries++;
				} else  {
					P.getChildren().add(im5);
					lifes.add(im5);
					break;
				}

			}
		}

	}

	/**
	 * Method to spawn a wall.
	 * Since a wall occupies more space, it is given a 100 chances to spawn without collision
	 */
	public void spawn_wall() {
		int tries = 0;
		while (tries < 100) {
			Rectangle R = new Rectangle(random.nextInt(320), -100, 7, random.nextInt(30) + 180);
			R.setFill(Color.WHITE);
			Wall WR = new Wall(R);

			if (!canAdd(R)) {
				tries++;
			} else {
				P.getChildren().add(R);
				walls.add(WR);
				break;
			}
		}
	}

	/**
	 * @param N
	 * @return
	 * Method to check if the Node N can be added to Pane such that it does not collide with any existing tokens
	 */
	public boolean canAdd(Node N) {
		for (Ball B : balls)
			if (B.getNode().getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;
		for (Wall B : walls)
			if (B.getNode().getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;
		for (Block B : blocks)
			if (B.getNode().getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;
		for (DestroyBlock B : dBlocks)
			if (B.getNode().getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;
		for (Magnet B : magnets)
			if (B.getNode().getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;
		for (Shield B : shields)
			if (B.getNode().getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;
		for (ImageView B : lifes)
			if (B.getBoundsInParent().intersects(N.getBoundsInParent()))
				return false;

		return true;
	}

	/**
	 * @param A
	 * @param B
	 * @return
	 * Method to get distance between circle A and circle B
	 */
	public double getDist(Circle A, Circle B) {
		double ax = A.getCenterX() + A.getTranslateX();
		double ay = A.getCenterY() + A.getTranslateY();

		double bx = B.getCenterX() + B.getTranslateX();
		double by = B.getCenterY() + B.getTranslateY();

		return Math.sqrt(Math.abs((ax - bx) * (ax - bx) + (ay - by) * (ay - by)));

	}

	/**
	 * @throws GameOverException
	 * Method to update GUI components on each cycle.
	 * Internally calls methods to facilitate animations for block explosion and snake ball explosion
	 * Checks collision with tokens. Plays audios accordingly. Updates Score and snake length.
	 * Spawn methods for various tokens are called at different intervals, this is contolled by a global time 
	 * variable: t.
	 * Activation and duration of tokens is also monitored here.
	 * Method also removes unnecessary elements from Pane, and other lists.
	 */
	public void onTimer() throws GameOverException {

		updateDying(dying);
		updateDyingSnake(dyingsnake);

		for (Magnet N : magnets) {
			if (N.isActive()) {
				if (System.currentTimeMillis() - N.getTime() <= 5000) {
					for (Ball B : balls) {
						Circle a = (Circle) B.getNode();
						Circle b = (Circle) snake.getBody().get(0).getNode();
						if (getDist(a, b) < 150) {
							String coinsound = "coinsound.wav";
							coinmedia = new Media(new File(coinsound).toURI().toString());
							coinmediaplayer = new MediaPlayer(coinmedia);
							coinmediaplayer.play();

							B.hide();
							B.getNum().setVisible(false);
							Circle c = (Circle) snake.getBody().get(0).getNode();
							Color af = (Color) c.getFill();
							for (int i = 0; i < B.getPoints(); i++) {
								Circle Ctemp = (Circle) snake.getBody().get(snake.getBody().size() - 1).getNode();
								double x = Ctemp.getCenterX();
								double y = Ctemp.getCenterY();
								double addx = Ctemp.getTranslateX();
								double addy = Ctemp.getTranslateY() + 20;

								Circle toAddC = new Circle(x + addx, y + addy, 10, af);
								Ball C = new Ball(toAddC);
								P.getChildren().add(toAddC);
								snake.getBody().add(C);
							}
							showLength.setText(String.valueOf(snake.getBody().size()));

						}
					}
				} else {
					N.setActive(false);
					N.setTime(0);
					setSnakeColor();
				}
			}
		}

		for (Shield N : shields) {
			if (N.isActive()) {
				if (System.currentTimeMillis() - N.getTime() >= 5000) {
					N.setActive(false);
					N.setTime(0);
					setSnakeColor();
				}
			}
		}

		for (Block N : blocks) {

			if (N.getNode().getBoundsInParent().intersects(snake.getBody().get(0).getNode().getBoundsInParent())) {
				boolean activeS = haveShield();
				if (!activeS) {
					if (N.getSc() > 5) {

						while (N.getSc() > 5) {

							if (System.currentTimeMillis() - lastcall > 150) {
								snake.getBody().get(snake.getBody().size() - 1).hide();
								P.getChildren().remove(snake.getBody().get(snake.getBody().size() - 1).getNode());
								snake.getBody().remove(snake.getBody().size() - 1);
								if (snake.getsnakesize() == 0) {
									if (collect > 0) {
										collect--;
										showLife.setText(Integer.toString(collect+1));
										//System.out.println(collect);
										//lifes.remove(lifes.size() - 1);
										for (int i = 0; i < 4; i++) {
											snake.getBody()
													.add(new Ball(new Circle(175, 400 + i * 20, 10, Color.GOLD)));

										}

										for (Ball B : snake.getBody())
											P.getChildren().add(B.getNode());
										
										for (Block b : blocks) {
											Rectangle r = (Rectangle) b.getNode();
											b.hide();
											if (r.getY() + r.getTranslateY() > -50 && r.getY() + r.getTranslateY() < 550) {
												r.setVisible(false);
												//score += b.getSc();
												showScore.setText(String.valueOf(score));
												//System.out.println("xxx");
												playDeath(b);
											}
										}


									} else {
										String oversound = "over.wav";
										over = new Media(new File(oversound).toURI().toString());
										overmediaplayer = new MediaPlayer(over);
										overmediaplayer.play();
										throw new GameOverException();
									}
								}

								N.setSc(N.getSc() - 1);
								N.setNum(N.getSc());

								Circle C1 = (Circle) snake.getBody().get(0).getNode();

								showLength.setText(String.valueOf(snake.getBody().size()));
								score++;
								showScore.setText(String.valueOf(score));

								Snakedeath(C1);
								lastcall = System.currentTimeMillis();
							}

							return;
						}

					} else {
						for (int i = 0; i < N.getSc(); i++) {
							snake.getBody().get(snake.getBody().size() - 1).hide();
							P.getChildren().remove(snake.getBody().get(snake.getBody().size() - 1).getNode());
							snake.getBody().remove(snake.getBody().size() - 1);
							showScore.setText(String.valueOf(score));
							score++;
							if (snake.getsnakesize() == 0) {
								if (collect > 0) {
									collect--;
									showLife.setText(Integer.toString(collect+1));
									//System.out.println(collect);
									//lifes.remove(lifes.size() - 1);
									for (int i1 = 0; i1 < 4; i1++) {
										snake.getBody().add(new Ball(new Circle(175, 400 + i1 * 20, 10, Color.GOLD)));

									}

									for (Ball B : snake.getBody())
										P.getChildren().add(B.getNode());
									
									for (Block b : blocks) {
										Rectangle r = (Rectangle) b.getNode();
										b.hide();
										if (r.getY() + r.getTranslateY() > -50 && r.getY() + r.getTranslateY() < 550) {
											r.setVisible(false);
											//score += b.getSc();
											showScore.setText(String.valueOf(score));
											//System.out.println("xxx");
											playDeath(b);
										}
									}

									break;

								} else {
									String oversound = "over.wav";
									over = new Media(new File(oversound).toURI().toString());
									overmediaplayer = new MediaPlayer(over);
									overmediaplayer.play();
									throw new GameOverException();
								}
							}
						}
					}

				}

				else {
					score += N.getSc();
				}

				playDeath(N);
				showScore.setText(String.valueOf(score));
				N.hide();
				N.num.setVisible(false);

				showLength.setText(String.valueOf(snake.getBody().size()));
			}

		}

		for (Block N : blocks) {
			N.moveDown(snake.getBody().size());
		}

		for (ImageView N : lifes) {
			//collect++;
			lifesmoveDown(N, snake.getBody().size());
			if (N.getBoundsInParent().intersects(snake.getBody().get(0).getNode().getBoundsInParent())) {
				collect++;
				t_lives++;
				showLife.setText(Integer.toString(collect+1));
				//System.out.println(collect);
				N.setVisible(false);
			}
		}

		for (Ball N : balls) {
			if (N.isVisibile()) {
				N.moveDown(snake.getBody().size());
				if (N.getNode().getBoundsInParent().intersects(snake.getBody().get(0).getNode().getBoundsInParent())) {
					N.hide();
					N.getNum().setVisible(false);
					// mediaplayer.play();
					String coinsound = "coinsound.wav";
					coinmedia = new Media(new File(coinsound).toURI().toString());
					coinmediaplayer = new MediaPlayer(coinmedia);
					coinmediaplayer.play();

					Circle c = (Circle) snake.getBody().get(0).getNode();
					Color af = (Color) c.getFill();
					for (int i = 0; i < N.getPoints(); i++) {
						Circle Ctemp = (Circle) snake.getBody().get(snake.getBody().size() - 1).getNode();
						double x = Ctemp.getCenterX();
						double y = Ctemp.getCenterY();
						double addx = Ctemp.getTranslateX();
						double addy = Ctemp.getTranslateY() + 20;

						Circle toAddC = new Circle(x + addx, y + addy, 10, af);
						Ball C = new Ball(toAddC);
						P.getChildren().add(toAddC);
						snake.getBody().add(C);
					}
					showLength.setText(String.valueOf(snake.getBody().size()));

				}
			}
		}

		for (Magnet N : magnets) {
			N.moveDown(snake.getBody().size());
			if (N.getNode().getBoundsInParent().intersects(snake.getBody().get(0).getNode().getBoundsInParent())) {
				String magsound = "magnetsound.mp3";
				magnetmedia = new Media(new File(magsound).toURI().toString());
				magnetmediaplayer = new MediaPlayer(magnetmedia);
				magnetmediaplayer.play();

				N.hide();
				N.setActive(true);
				N.setTime(System.currentTimeMillis());
				setSnakeColor();
				showLength.setText(String.valueOf(snake.getBody().size()));
			}
		}

		for (Shield N : shields) {
			if (N.isVisibile()) {
				N.moveDown(snake.getBody().size());
				if (N.getNode().getBoundsInParent().intersects(snake.getBody().get(0).getNode().getBoundsInParent())) {
					String shldsound = "shieldsound.wav";
					shieldmedia = new Media(new File(shldsound).toURI().toString());
					shieldmediaplayer = new MediaPlayer(shieldmedia);
					shieldmediaplayer.play();

					N.hide();
					N.setActive(true);
					N.setTime(System.currentTimeMillis());

					setSnakeColor();

				}
			}
		}

		for (DestroyBlock N : dBlocks) {
			N.moveDown(snake.getBody().size());
			if (N.getNode().getBoundsInParent().intersects(snake.getBody().get(0).getNode().getBoundsInParent())) {
				String bmbsound = "bombsound.mp3";
				bombmedia = new Media(new File(bmbsound).toURI().toString());
				bombmediaplayer = new MediaPlayer(bombmedia);
				bombmediaplayer.play();
				N.hide();
				for (Block b : blocks) {
					Rectangle r = (Rectangle) b.getNode();
					if (r.getY() + r.getTranslateY() > -50 && r.getY() + r.getTranslateY() < 550) {
						r.setVisible(false);
						score += b.getSc();
						showScore.setText(String.valueOf(score));
						playDeath(b);
					}
				}

			}
		}

		for (Wall N : walls) {
			N.moveDown(snake.getBody().size());
		}

		showLength.setText(String.valueOf(snake.getBody().size()));

		ArrayList<Token> toRem = new ArrayList<Token>();
		ArrayList<Node> toRemlife = new ArrayList<Node>();
		for (Wall N : walls) {
			if (rem(N)) {
				toRem.add(N);
				P.getChildren().remove(N.getNode());
			}
		}
		walls.removeAll(toRem);

		toRem.clear();

		for (Ball N : balls) {
			if (rem(N)) {
				toRem.add(N);
				P.getChildren().remove(N.getNode());
				P.getChildren().remove(N.getNum());
			}
		}
		balls.removeAll(toRem);
		toRem.clear();

		for (ImageView N : lifes) {
			if (remlife(N)) {
				//toRemlife.add(N);
				P.getChildren().remove(N);

			}
		}
		//lifes.removeAll(toRemlife);
		//toRemlife.clear();

		for (Block N : blocks) {
			if (rem(N)) {
				toRem.add(N);
				P.getChildren().remove(N.getNode());
				P.getChildren().remove(N.getNum());
			}
		}
		blocks.removeAll(toRem);
		toRem.clear();
		for (DestroyBlock N : dBlocks) {
			if (rem(N)) {
				toRem.add(N);
				P.getChildren().remove(N.getNode());
			}
		}
		dBlocks.removeAll(toRem);
		toRem.clear();
		for (Magnet N : magnets) {

			if (rem(N) && !(N.isActive())) {
				toRem.add(N);
				P.getChildren().remove(N.getNode());
			}
		}
		magnets.removeAll(toRem);
		toRem.clear();
		for (Shield N : shields) {
			if (rem(N) && !(N.isActive())) {
				toRem.add(N);
				P.getChildren().remove(N.getNode());
			}
		}
		shields.removeAll(toRem);
		toRem.clear();

		lifes.removeIf(n -> remlife(n));
		walls.removeIf(n -> rem(n));
		balls.removeIf(n -> rem(n));
		blocks.removeIf(n -> rem(n));
		dBlocks.removeIf(n -> rem(n));
		magnets.removeIf(n -> (rem(n) && !(n.isActive())));
		shields.removeIf(n -> rem(n) && !(n.isActive()));

		t++;
		if (t % 100 == 0)
			spawn_block();
		if (t % 125 == 0)
			spawn_ball();
		if (t % 150 == 0)
			spawn_wall();
		if (t % 175 == 0)
			spawn_token();

	}

	public void lifesmoveDown(ImageView N, int length) {
		double speed = 0;
		if (length < 20)
			speed = 0;
		else if (length <= 40)
			speed = 0.5;
		else if (length <= 60)
			speed = 1;
		else
			speed = 1.5;
		N.setTranslateY(N.getTranslateY() + 4 + speed);
	}

	/**
	 * @param n
	 * This method adds n balls to snake.
	 */
	public void addToSnake(int n) {
		Circle c = (Circle) snake.getBody().get(0).getNode();
		Color a = (Color) c.getFill();
		for (int i = 0; i < n; i++) {
			Circle Ctemp = (Circle) snake.getBody().get(snake.getBody().size() - 1).getNode();
			double x = Ctemp.getCenterX();
			double y = Ctemp.getCenterY();
			double addx = Ctemp.getTranslateX();
			double addy = Ctemp.getTranslateY() + 20;

			Circle toAddC = new Circle(x + addx, y + addy, 10, a);
			Ball C = new Ball(toAddC);
			P.getChildren().add(toAddC);
			snake.getBody().add(C);
		}
		showLength.setText(String.valueOf(snake.getBody().size()));
	}

	/**
	 * @param x
	 * @return
	 * Method checks if TOoken x can be removed from it's corresponding list.
	 */
	public boolean rem(Token x) {
		return !x.isVisibile() || x.getNode().getLayoutY() + x.getNode().getTranslateY() > 600;
	}

	/**
	 * @param x
	 * @return
	 * Method to check if life node can be removed from it's list and pane.
	 */
	public boolean remlife(Node x) {
		boolean t=!x.isVisible() || x.getLayoutY() + x.getTranslateY() > 600;
		if(t) P.getChildren().remove(x);
		return t;
	}

	/**
	 * @param x
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Method to handle user input to in game drop down menu.
	 */
	public void menuclick(String x) throws FileNotFoundException, IOException {
		if (x.equals("Exit")) {
			SerializeG();
			SerializeL(L.getTop10());
			stage.close();

		} else if (x.equals("Restart")) {
			T.stop();
			t = 0;
			startGame(false);
		} else {
			T.stop();
			t = 0;
			SerializeG();
			stage.setScene(MainPage);
		}
	}

	/**
	 * Method to move snake to the left.
	 * Makes sure snake doesn't cross a wall or leaves the screen.
	 */
	public void moveLeft() {

		Circle H = (Circle) snake.getBody().get(0).getNode();

		double x = H.getCenterX() + H.getTranslateX();
		// System.out.println(x);
		if (x <= 10)
			return;

		for (Wall W : walls)
			if (snake.getBody().get(0).getNode().getBoundsInParent().intersects(W.getNode().getBoundsInParent()))
				return;

		for (Ball C : snake.getBody())
			C.getNode().setTranslateX(C.getNode().getTranslateX() - 10);
	}

	/**
	 * Method to move snake to the right.
	 * Makes sure snake doesn't cross a wall or leaves the screen.
	 */
	public void moveRight() {

		Circle H = (Circle) snake.getBody().get(0).getNode();
		double x = H.getCenterX() + H.getTranslateX();
		// System.out.println(x);

		if (x >= 315)
			return;

		for (Wall W : walls)
			if (snake.getBody().get(0).getNode().getBoundsInParent().intersects(W.getNode().getBoundsInParent()))
				return;

		for (Ball C : snake.getBody())
			C.getNode().setTranslateX(C.getNode().getTranslateX() + 10);
	}

	/**
	 * @param dying
	 * Method for block burst animation. Velocities are assigned randomly.
	 * Also removes unnecessary nodes from pane.
	 */
	public void updateDying(ArrayList<Entity> dying) {
		ArrayList<Entity> rem = new ArrayList<Entity>();
		for (Entity E : dying) {
			Rectangle R = (Rectangle) E.getR();
			R.setTranslateY(R.getTranslateY() + E.getDy());
			R.setTranslateX(R.getTranslateX() + E.getDx());
			if (R.getY() + R.getTranslateY() > 600 || R.getY() + R.getTranslateY() < 0) {
				rem.add(E);
				P.getChildren().remove(R);
			}
			if (R.getX() + R.getTranslateX() > 400 || R.getX() + R.getTranslateX() < 0) {
				rem.add(E);
				P.getChildren().remove(R);
			}
			E.setDy(E.getDy() + 0.05);

			if (System.currentTimeMillis() - E.getTime() > 500) {
				rem.add(E);
				P.getChildren().remove(R);
			}
		}

		dying.removeAll(rem);
	}

	/**
	 * @param dying
	 * Method for snake ball burst animation. Velocities are assigned randomly.
	 * Also removes unnecessary nodes from pane.
	 */

	public void updateDyingSnake(ArrayList<Entity> dying) {
		ArrayList<Entity> rem = new ArrayList<Entity>();
		for (Entity E : dying) {
			Circle R = (Circle) E.getR();
			R.setTranslateY(R.getTranslateY() + E.getDy());
			R.setTranslateX(R.getTranslateX() + E.getDx());
			if (R.getCenterY() + R.getTranslateY() > 600 || R.getCenterY() + R.getTranslateY() < 0) {
				rem.add(E);
				P.getChildren().remove(R);
			}
			if (R.getCenterX() + R.getTranslateX() > 400 || R.getCenterX() + R.getTranslateX() < 0) {
				rem.add(E);
				P.getChildren().remove(R);
			}

			if (System.currentTimeMillis() - E.getTime() > 175) {
				rem.add(E);
				P.getChildren().remove(R);
			}
		}

		dying.removeAll(rem);
	}

	/**
	 * @param B
	 * Method to initiate burst animation for a snake ball.
	 */
	public void Snakedeath(Node B) {
		Circle r = (Circle) B;
		Random A = new Random();
		double bx = r.getCenterX() + r.getTranslateX();
		double by = r.getCenterY() + r.getTranslateY();
		int radius = 6;
		Color[] arr = { Color.RED, Color.PALEVIOLETRED, Color.ORANGERED, Color.INDIANRED };
		Circle R = new Circle(bx, by, radius);
		R.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R, 2, 0));
		P.getChildren().add(R);

		Circle R1 = new Circle(bx, by, radius);
		R1.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R1, -2, 0));
		P.getChildren().add(R1);

		Circle R2 = new Circle(bx, by, radius);
		R2.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R2, 0, 2));
		P.getChildren().add(R2);

		Circle R3 = new Circle(bx, by, radius);
		R3.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R3, 0, -2));
		P.getChildren().add(R3);

		Circle R4 = new Circle(bx, by, radius);
		R4.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R4, 1.5, 1.5));
		P.getChildren().add(R4);

		Circle R5 = new Circle(bx, by, radius);
		R5.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R5, -1.5, -1.5));
		P.getChildren().add(R5);

		Circle R6 = new Circle(bx, by, radius);
		R6.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R6, -1.5, 1.5));
		P.getChildren().add(R6);

		Circle R7 = new Circle(bx, by, radius);
		R7.setFill(arr[A.nextInt(3)]);
		dyingsnake.add(new Entity(R7, 1.5, -1.5));
		P.getChildren().add(R7);

	}

	/**
	 * @param B
	 * Method to initiate block burst animation for block B.
	 */
	public void playDeath(Block B) {
		Rectangle r = (Rectangle) B.getNode();
		double bx = r.getX();
		double by = r.getY() + r.getTranslateY();

		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				double dy = 0, dx = 0;
				Rectangle R = new Rectangle(bx + i * 6, by + j * 6, 6, 6);
				R.setFill(r.getFill());
				dying.add(new Entity(R, dx, dy));
				P.getChildren().add(R);
			}
		}
		for (Entity E : dying) {
			Random b = new Random();
			int p = b.nextInt(2);
			float vel = b.nextFloat() + 1;
			if (p == 0) {
				E.setDx(vel);
			}
			if (p == 1) {
				E.setDx(-vel);

			}

		}

		for (Entity E : dying) {
			Random b = new Random();
			int p = b.nextInt(2);
			float vel = b.nextFloat() + 1;
			if (p == 0) {
				E.setDy(vel);
			}
			if (p == 1) {
				E.setDy(-vel);

			}

		}
	}

	/**
	 * @return
	 * Method to check if snake has an active shield.
	 */
	public boolean haveShield() {
		for (Shield S : shields)
			if (S.isActive())
				return true;
		return false;
	}

	/**
	 * @return
	 * Method to check if snake has an active magnet.
	 */
	public boolean haveMagnet() {
		for (Magnet M : magnets)
			if (M.isActive())
				return true;
		return false;
	}

	/**
	 * Method to set snake color according to powerups user has collected.
	 */
	public void setSnakeColor() {
		boolean S = haveShield(), M = haveMagnet();

		if (S && M)
			snake.changecolor(Color.CRIMSON);
		else if (S)
			snake.changecolor(Color.DODGERBLUE);
		else if (M)
			snake.changecolor(Color.DARKGREEN);
		else
			snake.changecolor(Color.GOLD);
	}

	/**
	 * @param t
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Method to serialize leaderboard.
	 */
	public void SerializeL(ArrayList<Tuple> t) throws FileNotFoundException, IOException {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("Leaderboard.txt"));
			out.writeObject(t);
		} finally {
			out.close();
		}

		// System.out.println("srlsd");

	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Method to serialize score.
	 */
	public void SerializeScore() throws FileNotFoundException, IOException {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream("Score.txt"));
			out.writeInt(score);
		
		} finally {
			out.close();
		}
	}

	/**
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 * Method to deserialize score.
	 */
	public int DeserializeScore() throws IOException, FileNotFoundException {
		int s = 0;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("Score.txt"));
			s = (int) in.readInt();
			in.close();
		} catch (FileNotFoundException E) {
			;
		}
		return s;
	}

	/**
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * Method to deserialize leaderboard.
	 */
	public ArrayList<Tuple> DeSerializeL() throws FileNotFoundException, IOException, ClassNotFoundException {
		ArrayList<Tuple> t = null;
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new FileInputStream("Leaderboard.txt"));
			t = (ArrayList<Tuple>) in.readObject();
			in.close();
		} catch (FileNotFoundException E) {
			;
		}
		return t;
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * Method to serialize the game state.
	 */
	public void SerializeG() throws FileNotFoundException, IOException {
		FileWriter outdblk = null;
		FileWriter outwll = null;
		FileWriter outblk = null;
		FileWriter outbll = null;
		FileWriter outsld = null;
		FileWriter outmgn = null;
		FileWriter outsnk = null;
		FileWriter outscore = null;

		try {
			outblk = new FileWriter("blocks.txt");
			outwll = new FileWriter("walls.txt");
			outdblk = new FileWriter("dblocks.txt");
			outbll = new FileWriter("balls.txt");
			outsld = new FileWriter("shields.txt");
			outmgn = new FileWriter("mangets.txt");
			outsnk = new FileWriter("snake.txt");
			outscore = new FileWriter("SScore.txt");

			for (Ball B : balls)
				outbll.write(B.toString());
			for (Wall B : walls)
				outwll.write(B.toString());
			for (DestroyBlock B : dBlocks)
				outdblk.write(B.toString());
			for (Block B : blocks)
				outblk.write(B.toString());
			for (Shield B : shields)
				outsld.write(B.toString());
			for (Magnet B : magnets)
				outmgn.write(B.toString());
			outsnk.write(snake.toString());

			outscore.write(String.valueOf(score));
			outscore.write(" ");
			outscore.write(String.valueOf(t_lives));
			outscore.write(" ");
			outscore.write(String.valueOf(collect));

		} finally {
			outsnk.close();
			outblk.close();
			outwll.close();
			outbll.close();
			outsld.close();
			outmgn.close();
			outdblk.close();
			outscore.close();

			// System.out.println("gsrld");
		}

	}
	
	/**
	 * Method to empty snake file on losing a game.
	 */
	public void emptyfiles() {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("snake.txt");
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			;
		}
	}

	/**
	 * @throws IOException
	 * Method to deserialize a saved game state.
	 */
	public void deSerializeG() throws IOException {
		snake.getBody().clear();
		BufferedReader rS = null;
		P = new Group();

		try {
			rS = new BufferedReader(new FileReader("snake.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);
				Color c = Color.web(line[2]);

				Circle C = new Circle(x, y, 10, c);
				Ball B = new Ball(C);

				P.getChildren().add(C);
				snake.getBody().add(B);

			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		balls.clear();
		rS = null;

		try {
			rS = new BufferedReader(new FileReader("balls.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);
				Color c = Color.web(line[2]);
				int p = Integer.parseInt(line[3]);

				Circle C = new Circle(x, y, 15, c);
				Ball B = new Ball(C, p);
				balls.add(B);

				B.getNum().setLayoutX(x - 3);
				B.getNum().setLayoutY(-10 + y);

				P.getChildren().add(C);
				P.getChildren().add(B.getNum());

			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		blocks.clear();
		rS = null;

		try {
			rS = new BufferedReader(new FileReader("blocks.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);
				Color c = Color.web(line[2]);
				int sc = Integer.parseInt(line[3]);

				Rectangle C = new Rectangle(x, y, 65, 60);
				C.setFill(c);
				Block B = new Block(C, sc);
				blocks.add(B);

				C.setArcHeight(10.0);
				C.setArcWidth(10.0);
				C.setStrokeWidth(1);
				C.setStroke(Color.BLACK);

				B.getNum().setLayoutX(x + 30);
				B.getNum().setLayoutY(15 + y);

				P.getChildren().add(C);
				P.getChildren().add(B.getNum());

			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		walls.clear();
		rS = null;

		try {
			rS = new BufferedReader(new FileReader("walls.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);
				double w = Double.parseDouble(line[2]);
				double h = Double.parseDouble(line[3]);

				Rectangle C = new Rectangle(x, y, w, h);
				C.setFill(Color.WHITE);
				Wall B = new Wall(C);
				walls.add(B);

				P.getChildren().add(C);
			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		magnets.clear();
		rS = null;

		try {
			rS = new BufferedReader(new FileReader("mangets.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);

				ImageView C = new ImageView(new Image("/AP_Project/magnet.png"));
				C.setTranslateX(x);
				C.setTranslateY(y);
				C.setFitWidth(40);
				C.setFitHeight(40);

				Magnet B = new Magnet(C);

				if (line[2].equals("true")) {
					B.setActive(true);
					B.setTime(System.currentTimeMillis());
				}

				else
					B.setActive(false);

				magnets.add(B);

				P.getChildren().add(C);
			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		shields.clear();
		rS = null;

		try {
			rS = new BufferedReader(new FileReader("shields.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);

				ImageView C = new ImageView(new Image("/AP_Project/shield.png"));
				C.setTranslateX(x);
				C.setTranslateY(y);
				C.setFitWidth(40);
				C.setFitHeight(40);

				Shield B = new Shield(C);

				if (line[2].equals("true")) {
					B.setActive(true);
					B.setTime(System.currentTimeMillis());
				}

				else
					B.setActive(false);

				shields.add(B);

				P.getChildren().add(C);
			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		dBlocks.clear();
		rS = null;

		try {
			rS = new BufferedReader(new FileReader("dblocks.txt"));
			String l;

			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				Double x = Double.parseDouble(line[0]);
				double y = Double.parseDouble(line[1]);

				ImageView C = new ImageView(new Image("/AP_Project/bomb.png"));
				C.setTranslateX(x);
				C.setTranslateY(y);
				C.setFitWidth(40);
				C.setFitHeight(40);

				DestroyBlock B = new DestroyBlock(C);
				dBlocks.add(B);

				P.getChildren().add(C);
			}
			rS.close();
		} catch (FileNotFoundException E) {
			;
		}

		try {
			rS = new BufferedReader(new FileReader("SScore.txt"));
			String l;
			while ((l = rS.readLine()) != null) {
				String line[] = l.split(" ");
				
				score = Integer.parseInt(line[0]);
				showScore.setText(String.valueOf(score));
				t_lives = Integer.parseInt(line[1]);
				collect = Integer.parseInt(line[2]);
				showLife.setText(Integer.toString(collect+1));
				rS.close();
			}
		} catch (FileNotFoundException E) {

		}

		setSnakeColor();

	}

}
