package demo;

import java.util.Random;

public class Bee extends FlyingObject {
	private int beeSpeedx = 1;
	private int beeSpeedy = 3;

	// 建構子：蜜蜂
	public Bee() {
		this.image = Demo.bee;
		width = image.getWidth();
		height = image.getHeight();
		Random rand = new Random();
		y = -height; // 出現的位置
		x = rand.nextInt(Demo.WIDTH); // 視窗寬-圖片寬
	}
	
	// 移動方式
	@Override
	public void step() {
		x = x + beeSpeedx;
		y = y + beeSpeedy;
		if (x > Demo.WIDTH - width) {
			beeSpeedx = -1;
		}
		if (x < 0) {
			beeSpeedx = 1;
		}
	}

	// 超出視窗
	@Override
	public boolean isAlive() {
		return y > Demo.HEIGHT; // true 即超出視窗
	}
}