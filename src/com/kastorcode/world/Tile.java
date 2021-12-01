package com.kastorcode.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;


public class Tile {
	public static final int TILE_SIZE = 16;

	public static BufferedImage
		TILE_GRASS = Spritesheet.getSprite(0, 0, 16, 16),
		TILE_FLOWER = Spritesheet.getSprite(112, 0, 16, 16),
		TILE_DARK_GRASS = Spritesheet.getSprite(9 * 16, 7 * 16, 16, 16),
		TILE_GROUND = Spritesheet.getSprite(128, 0, 16, 16),
		TILE_SAND = Spritesheet.getSprite(9 * 16, 5 * 16, 16, 16),
		TILE_TRUNK = Spritesheet.getSprite(16, 0, 16, 16),
		TILE_ROCK = Spritesheet.getSprite(9 * 16, 8 * 16, 16, 16),
		TILE_CEMENT = Spritesheet.getSprite(9 * 16, 9 * 16, 16, 16),
		TILE_WATER = Spritesheet.getSprite(9 * 16, 6 * 16, 16, 16);

	private BufferedImage sprite;

	private int x, y;


	public Tile (int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}


	public void render (Graphics g) {
		g.drawImage(sprite, x - Camera.getX(), y - Camera.getY(), null);
	}
}