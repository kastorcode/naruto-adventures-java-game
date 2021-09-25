package com.kastorcode.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;


public class World {
	public static int WIDTH, HEIGHT;

	private Tile[] tiles;


	public World (String name) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource("/images/" + name));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					int pixel = pixels[xx + (yy * WIDTH)];

					switch (pixel) {
						// Floor
						case 0xFF000000: {
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
							break;
						}

						// Wall
						case 0xFFFFFFFF: {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
							break;
						}

						// Player
						case 0xFF0026FF: {
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
							break;
						}

						// Floor
						default: {
							tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
							break;
						}
					}
				}
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void render (Graphics g) {
		for (int xx = 0; xx < WIDTH; xx++) {
			for (int yy = 0; yy < HEIGHT; yy++) {
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}