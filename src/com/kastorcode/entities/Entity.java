package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.util.Comparator;
import java.util.List;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.world.Camera;
import com.kastorcode.world.Node;
import com.kastorcode.world.Tile;
import com.kastorcode.world.Vector2i;


public class Entity {
	public static BufferedImage
		LIFEPACK = Spritesheet.getSprite(32, 0, 16, 16),
		WEAPON = Spritesheet.getSprite(48, 0, 16, 16),
		BULLET = Spritesheet.getSprite(64, 0, 16, 16),
		ENEMY = Spritesheet.getSprite(2 * 16, 2 * 16, 16, 16),
		PLAYER_WEAPON = Spritesheet.getSprite(5 * 16, 0, 16, 16),
		DAMAGED_PLAYER_WEAPON = Spritesheet.getSprite(6 * 16, 0, 16, 16);

	public double x, y, z;

	protected int width, height;
	
	protected List<Node> path;
	
	private BufferedImage sprite;
	
	public int depth;
	

	public Entity (double x, double y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
	}
	
	
	public int getX () {
		return (int)x;
	}
	
	
	public void setX (double newX) {
		x = newX;
	}
	
	
	public int getY () {
		return (int)y;
	}
	
	
	public void setY (double newY) {
		y = newY;
	}
	
	
	public int getWidth () {
		return width;
	}
	
	
	public int getHeight () {
		return height;
	}
	
	
	public static boolean isColliding (Entity entity1, Entity entity2) {
		Rectangle entity1Mask = new Rectangle(entity1.getX(), entity1.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
		Rectangle entity2Mask = new Rectangle(entity2.getX(), entity2.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
		return entity1Mask.intersects(entity2Mask);
	}


	public void tick () {}
	
	
	public double calculateDistance (int x1, int y1, int x2, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}


	public boolean isColliding (int nextX, int nextY) {
		Rectangle currentEnemy = new Rectangle(nextX, nextY, Tile.TILE_SIZE, Tile.TILE_SIZE);
		int enemiesSize = Game.enemies.size();
		
		for (int i = 0; i < enemiesSize; i++) {
			Enemy enemy = Game.enemies.get(i);
			
			if (enemy == this) { continue; }
			
			Rectangle targetEnemy = new Rectangle(enemy.getX(), enemy.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
			
			if (currentEnemy.intersects(targetEnemy)) {
				return true;
			}
		}

		return false;
	}


	public boolean pixelPerfectIsColliding (int x1, int y1, int x2, int y2, int[] pixels1, int[] pixels2, BufferedImage sprite1, BufferedImage sprite2) {
		for (int xx1 = 0; xx1 < sprite1.getWidth(); xx1++) {
			for (int yy1 = 0; yy1 < sprite1.getHeight(); yy1++) {
				for (int xx2 = 0; xx2 < sprite2.getWidth(); xx2++) {
					for (int yy2 = 0; yy2 < sprite2.getHeight(); yy2++) {
						int currentPixel1 = pixels1[xx1 + yy1 * sprite1.getWidth()];
						int currentPixel2 = pixels2[xx2 + yy2 * sprite2.getWidth()];

						if (currentPixel1 == 0x00ffffff || currentPixel2 == 0x00ffffff) {
							continue;
						}

						if (xx1 + x1 == xx2 + x2 && yy1 + y1 == yy2 + y2) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}


	public void followPath (List<Node> path) {
		if (path != null) {
			if (path.size() > 0) {
				Vector2i target = path.get(path.size() - 1).tile;
				//xprev = x;
				//yprev = y;

				if (x < target.x * 16 && !isColliding(getX() + 1, getY())) {
					x++;
				}
				else if (x > target.x * 16 && !isColliding(getX() - 1, getY())) {
					x--;
				}

				if (y < target.y * 16 && !isColliding(getX(), getY() + 1)) {
					y++;
				}
				else if (y > target.y * 16 && !isColliding(getX(), getY() - 1)) {
					y--;
				}

				if (x == target.x * 16 && y == target.y * 16) {
					path.remove(path.size() - 1);
				}
			}
		}
	}


	public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
		@Override

		public int compare (Entity e0, Entity e1) {
			if (e1.depth < e0.depth) {
				return + 1;
			}

			if (e1.depth > e0.depth) {
				return - 1;
			}

			return 0;
		}
	};


	public void render (Graphics g) {
		g.drawImage(sprite, getX() - Camera.getX(), getY() - Camera.getY(), null);
	}
}