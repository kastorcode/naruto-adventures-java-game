package com.kastorcode.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.kastorcode.entities.BulletShoot;
import com.kastorcode.entities.Enemy;
import com.kastorcode.entities.Entity;
import com.kastorcode.entities.Player;
import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.graphics.UI;
import com.kastorcode.world.World;


public class Game extends Window implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private Thread thread;

	private boolean isRunning, restart = false, showGameOverMessage = true;

	private BufferedImage image;
	
	private int currentLevel = 1, maxLevel = 2, framesGameOverMessage = 0;
	
	public static List<Entity> entities;

	public static List<Enemy> enemies;
	
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public UI ui;
	
	public static String state = "GAME_OVER";//NORMAL


	public Game () {
		super();

		addKeyListener(this);
		addMouseListener(this);

		rand = new Random();
		ui = new UI();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("spritesheet.png");
		player = new Player(0, 0, 16, 16, Spritesheet.getSprite(32, 0, 16, 16));

		entities.add(player);

		world = new World("level1.png");
	}


	public synchronized void start () {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}


	public synchronized void stop () {
		isRunning = false;

		try {
			thread.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public static void main (String args[]) {
		Game game = new Game();
		game.start();
	}


	public void tick () {
		if (state == "NORMAL") {
			restart = false;

			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);
				entity.tick();
			}
			
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).tick();
			}
			
			if (enemies.size() == 0) {
				currentLevel++;
				
				if (currentLevel > maxLevel) {
					currentLevel = 1;
				}
				
				String newWorld = "level" + currentLevel + ".png";
				over(newWorld);
			}
		}
		else if (state == "GAME_OVER") {
			framesGameOverMessage++;
			
			if (framesGameOverMessage == 48) {
				framesGameOverMessage = 0;
				showGameOverMessage = !showGameOverMessage;
			}
			
			if (restart) {
				state = "NORMAL";
				currentLevel = 1;
				over("level" + currentLevel + ".png");
			}
		}
	}


	public static void over (String level) {
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		spritesheet = new Spritesheet("spritesheet.png");
		player = new Player(0, 0, 16, 16, Spritesheet.getSprite(32, 0, 16, 16));

		entities.add(player);

		world = new World("/" + level);
		return;
	}


	public void render () {
		BufferStrategy bs = this.getBufferStrategy();
		
		if (bs == null) {
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();

		g.setColor(new Color(0, 0, 0));
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		world.render(g);

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			entity.render(g);
		}
		
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
		}

		ui.render(g);

		g.dispose();

		g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		
		if (state == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D)g;

			g2.setColor(new Color(0, 0, 0, 100));
			g2.fillRect(0, 0, Window.WIDTH * Window.SCALE, Window.HEIGHT * Window.SCALE);

			g.setFont(new Font("arial", Font.BOLD, 32));
			g.setColor(Color.WHITE);
			g.drawString("Game Over", (Window.WIDTH * Window.SCALE) / 2 - 96, (Window.HEIGHT * Window.SCALE) / 2);

			if (showGameOverMessage) {
				g.setFont(new Font("arial", Font.PLAIN, 16));
				g.drawString("> Pressione Enter para reiniciar", (Window.WIDTH * Window.SCALE) / 2 - 96, (Window.HEIGHT * Window.SCALE) / 2 + 32);
			}
		}

		bs.show();
	}


	public void run () {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();

		requestFocus();

		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS: " + frames);
				frames = 0;
				timer += 1000;
			}
		}

		stop();
	}


	@Override
	public void keyPressed (KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D: {
				player.right = true;
				break;
			}
			
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A: {
				player.left = true;
				break;
			}
			
			case KeyEvent.VK_X:
			case KeyEvent.VK_SPACE: {
				player.shoot = true;
			}
			
			case KeyEvent.VK_ENTER:
			case KeyEvent.VK_BACK_SPACE: {
				restart = true;
				break;
			}
		}
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W: {
				player.up = true;
				break;
			}
			
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S: {
				player.down = true;
				break;
			}
		}
	}


	@Override
	public void keyReleased (KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_D: {
				player.right = false;
				break;
			}
			
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_A: {
				player.left = false;
				break;
			}
		}
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
			case KeyEvent.VK_W: {
				player.up = false;
				break;
			}
			
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_S: {
				player.down = false;
				break;
			}
		}
	}


	@Override
	public void keyTyped (KeyEvent e) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseClicked (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseEntered (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mouseExited (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}


	@Override
	public void mousePressed (MouseEvent e) {
		player.mouseShoot = true;
		player.mx = e.getX() / Window.SCALE;
		player.my = e.getY() / Window.SCALE;
	}


	@Override
	public void mouseReleased (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}