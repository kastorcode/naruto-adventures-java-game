package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.world.Camera;


public class Entity {
	public static BufferedImage
		LIFEPACK = Spritesheet.getSprite(7 * 16, 0, 16, 16),
		WEAPON = Spritesheet.getSprite(7 * 16, 16, 16, 16),
		BULLET = Spritesheet.getSprite(8 * 16, 0, 16, 16),
		ENEMY = Spritesheet.getSprite(2 * 16, 2 * 16, 16, 16);

	protected double x, y;

	protected int width, height;
	
	private BufferedImage sprite;
	

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
	
	
	public void tick () {}
	
	
	public void render (Graphics g) {
		g.drawImage(sprite, getX() - Camera.getX(), getY() - Camera.getY(), null);
	}
}