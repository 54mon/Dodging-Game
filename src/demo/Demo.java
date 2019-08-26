package demo;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics; // draw image 
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent; // 偵測user 的滑鼠移動 
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.image.BufferedImage;
import java.time.Duration;

import javax.imageio.ImageIO; // 讀取所需圖片
import javax.swing.ImageIcon;
import javax.swing.JFrame; // 視窗
import javax.swing.JPanel; // Panel

public class Demo extends JPanel {
	public static final int WIDTH = 400;
	public static final int HEIGHT = 654;
	/* State of Game: START RUNNING PAUSE GAME_OVER */
	private int state = 0;
	private static final int START = 0;
	private static final int RUNNING = 1;
	private static final int PAUSE = 2;
	private static final int GAME_OVER = 3;

	private Timer timer; // 定時器
	private int interval = 1000 / 100;
	Duration duration = Duration.ofMillis(30);

	public static BufferedImage background;
	public static BufferedImage player;
	public static BufferedImage pause;
	public static BufferedImage start;
	public static BufferedImage gameover;
	public static BufferedImage bullet;
	public static BufferedImage bee;
	public static BufferedImage pill;
	public static BufferedImage spaceman;

	private Hero hero = new Hero(); // 英雄機
	private static FlyingObject[] Fly = new FlyingObject[200];
	Random rand = new Random();

	static { // 程式靜態區塊:讀取圖片（讀一次之後要用時，可以直接用）
		try {
			background = ImageIO.read(Demo.class.getResource("Background.jpg"));
			player = ImageIO.read(Demo.class.getResource("airplane.png"));
			pause = ImageIO.read(Demo.class.getResource("pause3.png"));
			gameover = ImageIO.read(Demo.class.getResource("gameover.png"));
			start = ImageIO.read(Demo.class.getResource("start.png"));
			bee = ImageIO.read(Demo.class.getResource("airlinerIcon64px_ccw.png"));
			pill = ImageIO.read(Demo.class.getResource("cross36.png"));
			spaceman = ImageIO.read(Demo.class.getResource("spaceman.png"));
			for (int i = 0; i < Fly.length; i++) {
				if (i % 10 == 0 || i%11==0) {
					Fly[i] = new Blood();
				} else if(i%13 ==0||i%15==0){
					Fly[i] = new Spaceman();
				}else {
					Fly[i] = new Bee();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Override
	public void paint(Graphics g) { // paint() 來自JComponent
		g.drawImage(background, 0, 0, null); // 放背景
		paintHero(g);
		paintTime(g);
		paintState(g);
		paintFlyingObjects(g);

	}

	public void paintHero(Graphics g) { // 把玩家所操控的畫出來
		g.drawImage(hero.getImage(), hero.getX(), hero.getY(), null);
	}

	public void paintFlyingObjects(Graphics g) { // 畫飛行物：蜜蜂、補血包
		for (int i = 0; i < Fly.length; i++) {
			FlyingObject tmp = Fly[i];
			if (hero.hit(tmp)) {
				tmp.setX(rand.nextInt(Demo.WIDTH));
				tmp.setY(-HEIGHT);
				g.drawImage(tmp.getImage(), tmp.getX(), tmp.getY(), null);
			} else {
				g.drawImage(tmp.getImage(), tmp.getX(), tmp.getY(), null);
			}
		}
	}

	int counttime = 0;

	public void paintTime(Graphics g) {
		int x = 8; // x座標
		int y = 25; // y座標
		Font font = new Font(Font.MONOSPACED, Font.BOLD, 25); // 字型
		g.setColor(new Color(0xFFFFFF)); // 顏色
		g.setFont(font); // 設定字型
		g.drawString("Time: " + counttime / 3000 + "m " + counttime / 100 % 60 + "s", x, y);
		g.drawString("Mario's Life: " + hero.getLife(), x, y + 25);
	}

	// 依照遊戲狀態去變換介面
	public void paintState(Graphics g) {
		switch (state) {
		case START:
			g.drawImage(start, 0, 0, null);
			break;
		case PAUSE:
			g.drawImage(pause, 0, 0, null);
			break;
		case GAME_OVER:
			g.drawImage(gameover, 0, 0, null);
			break;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("--->> Dodging Game <<---");
		Demo game = new Demo();
		frame.add(game); // 要extends JPanel game 才會被視為component
		frame.setSize(WIDTH, HEIGHT); // 設定視窗大小
		frame.setAlwaysOnTop(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 視窗關閉後即退出程式
		frame.setIconImage(new ImageIcon("airlinerIcon16px.png").getImage()); //
		frame.setLocationRelativeTo(null); // null - 會依照視窗的 size 與螢幕的大小來計算出視窗的位置，使得他出現在螢幕中央。
		frame.setVisible(true);
		frame.setResizable(false); // 禁止調整視窗
		game.MouseAction(); // 偵測玩家的滑鼠
	}

	public void MouseAction() {
		MouseAdapter l = new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				if (state == RUNNING) {
					int x = e.getX();
					int y = e.getY();
					hero.moveTo(x, y); // x 在 fly object 宣告
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) { // 滑鼠進到遊戲視窗內
				if (state == PAUSE) { // 更新遊戲狀態
					state = RUNNING;
				}
			}

			@Override
			public void mouseExited(MouseEvent e) { // 滑鼠離開遊戲視窗內
				if (state == RUNNING) { // 更新遊戲狀態
					state = PAUSE;
				}
			}

			public void mouseClicked(MouseEvent e) {
				switch (state) {
				case START:
					state = RUNNING;
					break;
				case GAME_OVER:
					// Fly = new FlyingObject[0];
					hero = new Hero(); // 重新再來過
					counttime = 0;
					state = START;
					break;
				}
			}
		};
		this.addMouseListener(l); // 處理滑鼠點選操作
		this.addMouseMotionListener(l); // 處理滑鼠滑動操作

		/* 控制程式執行流程 */
		timer = new Timer(); // 主流程控制
		// timer.schedule(task, firstTime, period)
		timer.schedule(new TimerTask() {
			@Override
			// 使用time task 的 run 類別
			public void run() {
				// 每次需要執行的代碼放到這裡面
				if (state == RUNNING) { // 執行狀態
					stepAction(); // 走一步
					outOfBoundsAction(); // 刪除出界的飛行物
					checkGameOver(); // 檢查遊戲結束
					counttime++;
				}
				repaint(); // 重繪圖，呼叫paint()方法
			}
		}, interval, interval); // (定時所需執行的code TimeTask(){}, 第一次執行的時間, 每隔period毫秒執行一次)
	}

	public void stepAction() {
		for (int i = 0; i < Fly.length; i++) {
			FlyingObject h = Fly[i];
			h.step();
		}
	}

	public void checkGameOver() {
		if (isGameover() == true) {
			for (int i = 0; i < Fly.length; i++) {
				FlyingObject obj = Fly[i];
				obj.setX(rand.nextInt(Demo.WIDTH - obj.width));
				obj.setY(-HEIGHT);
			}
			state = GAME_OVER;
		}
	}

	public void outOfBoundsAction() {
		for (int i = 0; i < Fly.length; i++) {
			FlyingObject obj = Fly[i];
			if (obj.isAlive()) {
				obj.setX(rand.nextInt(Demo.WIDTH - obj.width));
				obj.setY(-HEIGHT);
			}
		}
	}

	public boolean isGameover() {

		for (int i = 0; i < Fly.length; i++) {
			FlyingObject obj = Fly[i];
			if ((obj instanceof Blood) && hero.hit(obj)) {
				hero.addLife();
				obj.setX(rand.nextInt(Demo.WIDTH - obj.width));
				obj.setY(-HEIGHT);
			} else if ((obj instanceof Bee) && hero.hit(obj)) {
					hero.subtractLife();
					obj.setX(rand.nextInt(Demo.WIDTH - obj.width));
					obj.setY(-HEIGHT);
				}
			else if((obj instanceof Spaceman) && hero.hit(obj)){
				obj.setX(rand.nextInt(Demo.WIDTH - obj.width));
				obj.setY(-HEIGHT);
			}
			}
		return hero.getLife() <= 0;
	}
}
