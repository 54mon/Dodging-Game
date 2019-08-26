package demo;

import java.util.Random;

public class Blood extends FlyingObject{
	private int xSpeed = 2;   
	private int ySpeed = 4;   	
	
	public Blood(){
		this.image = Demo.pill;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		y = -height;
		x = rand.nextInt(Demo.WIDTH - width);
	}
	
	public boolean isAlive() {
		return y>Demo.HEIGHT;
	}

	public void step() {      
		x += xSpeed;
		y += ySpeed;
		if(x > Demo.WIDTH-width){  
			xSpeed = -1;
		}
		if(x < 0){
			xSpeed = 1;
		}
	}

}
