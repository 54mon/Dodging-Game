package demo;

public class Hero extends FlyingObject{
	private int life; // 命

	public Hero(){ 
	life = 20; // 初始3條命 
	image = Demo.player; // 初始為player圖片 
	width = image.getWidth(); 
	height = image.getHeight(); 
	x = 150; y = 400;
}

	@Override
	public boolean isAlive() {
		return false;
	}

	@Override
	public void step() {
		return;
	}
	
	/** 當前物體移動了一下，相對距離，x,y滑鼠位置 */ 
	public void moveTo(int x, int y) {
		this.x = x - width/2-6;
		this.y = y - height/2-20;
		
	}
	public void addLife(){  
		life++;
	}
	public void subtractLife(){   
		life--;
	}
	public int getLife() {
		return life;
	}
	
	/** 碰撞演算法 **/
	public boolean hit(FlyingObject other){
		
		int x1 = other.x - this.width/2;                 
		int x2 = other.x + this.width/2 + other.width;   
		int y1 = other.y - this.height/2;                
		int y2 = other.y + this.height/2 + other.height; 
		int herox = this.x + this.width/2;               
		int heroy = this.y + this.height/2;              
		
		return herox>x1 && herox<x2 && heroy>y1 && heroy<y2;   
	}
}
