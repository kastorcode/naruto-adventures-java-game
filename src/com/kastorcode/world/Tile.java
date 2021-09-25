package com.kastorcode.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;


public class Tile {
	public static BufferedImage TILE_FLOOR = Spritesheet.getSprite(0, 0, 16, 16),
		TILE_WALL = Spritesheet.getSprite(16, 0, 16, 16);

	private BufferedImage sprite;

	private int x, y;


	public Tile (int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}


	public void render (Graphics g) {
		g.drawImage(sprite, x, y, null);
	}
}