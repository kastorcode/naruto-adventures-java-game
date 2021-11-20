package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.main.Game;
import com.kastorcode.world.Camera;
import com.kastorcode.world.World;


public class BulletShoot extends Entity {
	private int life = 35, currentLife = 0;
	
	private double dx, dy, speed = 4;


	public BulletShoot (
		double x, double y, int width, int height, BufferedImage sprite,
		double dx, double dy
	) {
		super(x, y, width, height, sprite);

		this.dx = dx;
		this.dy = dy;
	}
	
	
	public void tick () {
		if (World.isFreeDynamic((int)(x + (dx * speed)), (int)(y + (dy * speed)), 16, 16)) {
			x += dx * speed;
			y += dy * speed;
		}
		else {
			Game.bullets.remove(this);
			return;
		}

		currentLife++;
		
		if (currentLife == life) {
			Game.bullets.remove(this);
			return;
		}
	}
	
	
	public void render (Graphics g) {
		g.drawImage(Entity.DAMAGED_PLAYER_WEAPON, getX() - Camera.getX(), getY() - Camera.getY(), null);
	}
}