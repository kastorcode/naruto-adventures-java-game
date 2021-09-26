package com.kastorcode.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import com.kastorcode.entities.*;
import com.kastorcode.main.Game;
import com.kastorcode.main.Window;


public class World {
	public static int WIDTH, HEIGHT;

	public static Tile[] tiles;


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
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

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
							Game.player.setX(xx * 16);
							Game.player.setY(yy * 16);
							break;
						}
						
						// Enemy
						case 0xFFFF0000: {
							Game.entities.add(new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY));
							break;
						}
						
						// Weapon
						case 0xFFFF6A00: {
							Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON));
							break;
						}
						
						// Life pack
						case 0xFFFF7F7F: {
							Game.entities.add(new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK));
							break;
						}
						
						// Bullet
						case 0xFFFFD800: {
							Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET));
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
	
	
	public static boolean isFree (int nextX, int nextY) {
		int x1 = nextX / Tile.TILE_SIZE;
		int y1 = nextY / Tile.TILE_SIZE;
		
		int x2 = (nextX + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		int y2 = nextY / Tile.TILE_SIZE;
		
		int x3 = nextX / Tile.TILE_SIZE;
		int y3 = (nextY + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		
		int x4 = (nextX + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		int y4 = (nextY + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		
		return !(
			tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
			tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
			tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
			tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile
		);
	}
	
	
	public void render (Graphics g) {
		int xStart = Camera.getX() >> 4;
		int yStart = Camera.getY() >> 4;
		int xFinal = xStart + (Window.WIDTH >> 4);
		int yFinal = yStart + (Window.HEIGHT >> 4);

		for (int xx = xStart; xx <= xFinal; xx++) {
			for (int yy = yStart; yy <= yFinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;

				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}
	}
}