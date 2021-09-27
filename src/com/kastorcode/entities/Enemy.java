package com.kastorcode.entities;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.world.Camera;
import com.kastorcode.world.Tile;
import com.kastorcode.world.World;


public class Enemy extends Entity {
	public int rightDirection = 0, leftDirection = 1,
			direction = rightDirection;

	private double speed = 0.6;
	
	private int frames = 0, maxFrames = 5,
			frameIndex = 0, maxFrameIndex = maxFrames - 1;
	
	private boolean moved = false;
	
	private BufferedImage[] rightEnemy, leftEnemy;


	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightEnemy = new BufferedImage[maxFrames];
		leftEnemy = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			rightEnemy[i] = Spritesheet.getSprite(32 + (i * 16), 32, 16, 16);
		}

		for (int i = 0; i < maxFrames; i++) {
			leftEnemy[i] = Spritesheet.getSprite(32 + (i * 16), 48, 16, 16);
		}
	}
	
	
	public void tick () {
		moved = false;
		
		if (isCollidingWithPlayer()) {
			if (Game.rand.nextInt(100) < 10) {
				Game.player.life -= Game.rand.nextInt(3);
				
				if (Game.player.life < 1) {
					System.out.println("Game Over!");
				}
			}
		}
		else if (Game.rand.nextInt(100) < 75) {
			if (x < Game.player.getX() &&
				World.isFree((int)(x + speed), getY()) &&
				!isColliding((int)(x + speed), getY())
			) {
				moved = true;
				direction = rightDirection;
				x += speed;
			}
			else if (x > Game.player.getX() &&
				World.isFree((int)(x - speed), getY()) &&
				!isColliding((int)(x - speed), getY())
			) {
				moved = true;
				direction = leftDirection;
				x -= speed;
			}
	
			if (y < Game.player.getY() &&
				World.isFree(getX(), (int)(y + speed)) &&
				!isColliding(getX(), (int)(y + speed))
			) {
				moved = true;
				y += speed;
			}
			else if (y > Game.player.getY() &&
				World.isFree(getX(), (int)(y - speed)) &&
				!isColliding(getX(), (int)(y - speed))
			) {
				moved = true;
				y -= speed;
			}

			if (moved) {
				frames++;
				
				if (frames == maxFrames) {
					frames = 0;
					frameIndex++;
					
					if (frameIndex > maxFrameIndex) {
						frameIndex = 0;
					}
				}
			}
			else {
				frameIndex = 0;
				frames = 0;
			}
		}
	}
	
	
	public boolean isCollidingWithPlayer () {
		Rectangle currentEnemy = new Rectangle(getX(), getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);

		return currentEnemy.intersects(player);
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
	
	
	public void render (Graphics g) {
		if (direction == rightDirection) {
			g.drawImage(rightEnemy[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
		}
		else if (direction == leftDirection) {
			g.drawImage(leftEnemy[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
		}
	}
}