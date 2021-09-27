package com.kastorcode.entities;

import java.awt.image.BufferedImage;
import java.awt.Rectangle;

import com.kastorcode.main.Game;
import com.kastorcode.world.Tile;
import com.kastorcode.world.World;


public class Enemy extends Entity {
	private double speed = 0.6;


	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
	}
	
	
	public void tick () {
		if (Game.rand.nextInt(100) < 75) {
			if (x < Game.player.getX() &&
				World.isFree((int)(x + speed), getY()) &&
				!isColliding((int)(x + speed), getY())
			) {
				x += speed;
			}
			else if (x > Game.player.getX() &&
				World.isFree((int)(x - speed), getY()) &&
				!isColliding((int)(x - speed), getY())
			) {
				x -= speed;
			}
	
			if (y < Game.player.getY() &&
				World.isFree(getX(), (int)(y + speed)) &&
				!isColliding(getX(), (int)(y + speed))
			) {
				y += speed;
			}
			else if (y > Game.player.getY() &&
				World.isFree(getX(), (int)(y - speed)) &&
				!isColliding(getX(), (int)(y - speed))
			) {
				y -= speed;
			}
		}
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
}