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


	// Normal constructor
	public World (String name) {
		try {
			BufferedImage map = ImageIO.read(getClass().getResource("/images/" + name));
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			int[] pixels = new int[WIDTH * HEIGHT];
			tiles = new Tile[WIDTH * HEIGHT];
			BufferedImage lastTile = Tile.TILE_GRASS;
			map.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);

			for (int xx = 0; xx < WIDTH; xx++) {
				for (int yy = 0; yy < HEIGHT; yy++) {
					int pixel = pixels[xx + (yy * WIDTH)];
					//tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, Tile.TILE_GRASS);

					switch (pixel) {
						// Grass Floor
						case 0xff00ff00: {
							lastTile = Tile.TILE_GRASS;
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							break;
						}

						// Flower Floor
						case 0xffffc0cb: {
							lastTile = Tile.TILE_FLOWER;
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							break;
						}

						// Dark Grass Floor
						case 0xff013220: {
							lastTile = Tile.TILE_DARK_GRASS;
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							break;
						}

						// Ground Floor
						case 0xff993300: {
							lastTile = Tile.TILE_GROUND;
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							break;
						}

						// Sand Floor
						case 0xffffff00: {
							lastTile = Tile.TILE_SAND;
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							break;
						}

						// Trunk Wall
						case 0xffcc9966: {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_TRUNK);
							break;
						}

						// Rock Wall
						case 0xfffb8136: {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_ROCK);
							break;
						}

						// Cement Wall
						case 0xffd74894: {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_CEMENT);
							break;
						}

						// Water Wall
						case 0xff0000ff: {
							tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WATER);
							break;
						}

						// Life pack
						case 0xffff0000: {
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							Game.entities.add(new LifePack(xx * 16, yy * 16, 16, 16, Entity.LIFEPACK));
							break;
						}

						// Weapon
						case 0xffa9a9a9: {
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							Game.entities.add(new Weapon(xx * 16, yy * 16, 16, 16, Entity.WEAPON));
							break;
						}

						// Bullet
						case 0xff465945: {
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							Game.entities.add(new Bullet(xx * 16, yy * 16, 16, 16, Entity.BULLET));
							break;
						}

						// Npc
						case 0xff6a0dad: {
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);

							switch (Game.rand.nextInt(5)) {
								case 0: {
									Sasuke npc = new Sasuke(xx * 16, yy * 16);
									Game.entities.add(npc);
									break;
								}

								case 1: {
									Sakura npc = new Sakura(xx * 16, yy * 16);
									Game.entities.add(npc);
									break;
								}

								case 2: {
									Kakashi npc = new Kakashi(xx * 16, yy * 16);
									Game.entities.add(npc);
									break;
								}

								case 3: {
									Konohamaru npc = new Konohamaru(xx * 16, yy * 16);
									Game.entities.add(npc);
									break;
								}

								case 4: {
									Iruka npc = new Iruka(xx * 16, yy * 16);
									Game.entities.add(npc);
									break;
								}
							}
							break;
						}

						// Enemy
						case 0xff000000: {
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							Enemy enemy = new Enemy(xx * 16, yy * 16, 16, 16, Entity.ENEMY);
							Game.entities.add(enemy);
							Game.enemies.add(enemy);
							break;
						}

						// Player
						case 0xffffffff: {
							tiles[xx + (yy * WIDTH)] = new Tile(xx * 16, yy * 16, lastTile);
							Game.player.setX(16);
							Game.player.setY(16);
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


	// Random constructor
	/* public World (String name) {
		Game.player.setX(0);
		Game.player.setY(0);
		WIDTH = 100;
		HEIGHT = 100;
		tiles = new Tile[WIDTH * HEIGHT];

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				tiles[x + y * WIDTH] = new WallTile(x * 16, y * 16, Tile.TILE_WALL);
			}
		}

		int direction = 0, x = 0, y = 0;

		for (int i = 0; i < 200; i++) {
			tiles[x + y * WIDTH] = new FloorTile(x * 16, y * 16, Tile.TILE_FLOOR);

			switch (direction) {
				case 0: {
					// Right
					if (x < WIDTH) {
						x++;
					}
					break;
				}
				case 1: {
					// Left
					if (x > 0) {
						x--;
					}
					break;
				}
				case 2: {
					// Down
					if (y < HEIGHT) {
						y++;
					}
					break;
				}
				case 3: {
					// Up
					if (y > 0) {
						y--;
					}
					break;
				}
			}

			if (Game.rand.nextInt(100) < 30) {
				direction = Game.rand.nextInt(4);
			}
		}
	} */


	public static boolean isFree (int nextX, int nextY, int zPlayer) {
		int x1 = nextX / Tile.TILE_SIZE;
		int y1 = nextY / Tile.TILE_SIZE;
		
		int x2 = (nextX + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		int y2 = nextY / Tile.TILE_SIZE;
		
		int x3 = nextX / Tile.TILE_SIZE;
		int y3 = (nextY + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		
		int x4 = (nextX + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		int y4 = (nextY + Tile.TILE_SIZE - 1) / Tile.TILE_SIZE;
		
		if (!(
			tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
			tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
			tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
			tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile
		)) {
			return true;
		}
		
		if (zPlayer > 0) {
			return true;
		}
		
		return false;
	}


	public static boolean isFreeDynamic (int nextX, int nextY, int width, int height) {
		int x1 = nextX / Tile.TILE_SIZE;
		int y1 = nextY / Tile.TILE_SIZE;

		int x2 = (nextX + width - 1) / Tile.TILE_SIZE;
		int y2 = nextY / Tile.TILE_SIZE;

		int x3 = nextX / Tile.TILE_SIZE;
		int y3 = (nextY + height - 1) / Tile.TILE_SIZE;

		int x4 = (nextX + width - 1) / Tile.TILE_SIZE;
		int y4 = (nextY + height - 1) / Tile.TILE_SIZE;

		return !(
			tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile ||
			tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile ||
			tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile ||
			tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile
		);
	}


	public static void generateParticles (int x, int y, int amount) {
		for (int i = 0; i < amount; i++) {
			Game.entities.add(new Particle(x, y, 1, 1, null));
		}
	}


	public static void renderMinimap () {
		for (int i = 0; i < Game.minimapPixels.length; i++) {
			Game.minimapPixels[i] = 0;
		}

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HEIGHT; y++) {
				if (tiles[x + (y * WIDTH)] instanceof WallTile) {
					Game.minimapPixels[x + (y * WIDTH)] = 0xffffffff;
				}
			}
		}

		int playerX = Game.player.getX() / 16;
		int playerY = Game.player.getY() / 16;
		Game.minimapPixels[playerX + (playerY * WIDTH)] = 0xffffA500;
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