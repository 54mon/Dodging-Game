package demo;

public class GameState {
	/* State of Game: START RUNNING PAUSE GAME_OVER */
	public static final int WIDTH = 400;
	public static final int HEIGHT = 654;

	public static final int START = 0;
	public static final int RUNNING = 1;
	public static final int PAUSE = 2;
	public static final int GAME_OVER = 3;

	private int state = 0; 
	
	public GameState() {
		state = 0; // initialization
	}
	
	public void setState(int stateNum) {
		this.state = stateNum;
	}

	public int getState() {
		return this.state;
	}
}
