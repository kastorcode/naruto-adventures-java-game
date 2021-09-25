package com.kastorcode.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

import com.kastorcode.entities.Entity;
import com.kastorcode.entities.Player;
import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.world.World;


public class Game extends Window implements Runnable, KeyListener, MouseListener {
	private static final long serialVersionUID = 1L;

	private Thread thread;

	private boolean isRunning;

	private BufferedImage image;
	
	public List<Entity> entities;
	
	public Spritesheet spritesheet;
	
	public World world;
	
	private Player player;


	public Game () {
		super();

		addKeyListener(this);
		addMouseListener(this);

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		spritesheet = new Spritesheet("spritesheet.png");
		world = new World("map.png");
		player = new Player(0, 0, 16, 16, Spritesheet.getSprite(32, 0, 16, 16));

		entities.add(player);
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
		int entitiesSize = entities.size();
		
		for (int i = 0; i < entitiesSize; i++) {
			Entity entity = entities.get(i);
			entity.tick();
		}
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

		int entitiesSize = entities.size();
		for (int i = 0; i < entitiesSize; i++) {
			Entity entity = entities.get(i);
			entity.render(g);
		}
		
		g.dispose();

		g = bs.getDrawGraphics();

		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
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
	public void mousePressed (MouseEvent e) {}


	@Override
	public void mouseReleased (MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
}