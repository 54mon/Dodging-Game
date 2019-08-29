package flyingObject;

import java.util.Random;

import demo.Demo;
import demo.GameState;

public class Spaceman extends FlyingObject{
	private int xSpeed = 2;   
	private int ySpeed = 4;   	
	
	public Spaceman(){
		this.image = Demo.spaceman;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		y = -height;
		x = rand.nextInt(GameState.WIDTH - width);
	}
	
	public boolean isAlive() {
		return y>GameState.HEIGHT;
	}

	public void step() {      
		x += xSpeed;
		y += ySpeed;
		if(x > GameState.WIDTH-width){  
			xSpeed = -1;
		}
		if(x < 0){
			xSpeed = 1;
		}
	}
}
