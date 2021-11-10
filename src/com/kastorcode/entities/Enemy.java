package com.kastorcode.entities;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Rectangle;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.Sound;
import com.kastorcode.world.AStar;
import com.kastorcode.world.Camera;
import com.kastorcode.world.Tile;
import com.kastorcode.world.Vector2i;
import com.kastorcode.world.World;


public class Enemy extends Entity {
	public int rightDirection = 0, leftDirection = 1,
			direction = rightDirection;

	private double speed = 0.6;
	
	private int frames = 0, maxFrames = 5,
		frameIndex = 0, maxFrameIndex = maxFrames - 1,
		damageFrames = 0, life = 10;
	
	private boolean moved = false, pursue = false;
	
	public boolean isDamaged = false;
	
	private BufferedImage[] rightEnemy, leftEnemy;
	
	public BufferedImage[] damageRightEnemy, damageLeftEnemy;


	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightEnemy = new BufferedImage[maxFrames];
		leftEnemy = new BufferedImage[maxFrames];
		damageRightEnemy = new BufferedImage[maxFrames];
		damageLeftEnemy = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			rightEnemy[i] = Spritesheet.getSprite(80 + (i * 16), 16, 16, 16);
		}

		for (int i = 0; i < maxFrames; i++) {
			leftEnemy[i] = Spritesheet.getSprite(80 + (i * 16), 32, 16, 16);
		}
		
		for (int i = 0; i < maxFrames; i++) {
			damageRightEnemy[i] = Spritesheet.getSprite(80 + (i * 16), 48, 16, 16);
		}
		
		for (int i = 0; i < maxFrames; i++) {
			damageLeftEnemy[i] = Spritesheet.getSprite(80 + (i * 16), 64, 16, 16);
		}
	}
	
	
	/*
	public void tick () {
		moved = false;

		if (pursue) {
			if (isCollidingWithPlayer()) {
				if (Game.rand.nextInt(100) < 10) {
					Sound.HURT_EFFECT.play();
					Game.player.isDamaged = true;
					Game.player.life -= Game.rand.nextInt(3);
				}
			}
			else if (Game.rand.nextInt(100) < 75) {
				if (x < Game.player.getX() &&
					World.isFree((int)(x + speed), getY(), 0) &&
					!isColliding((int)(x + speed), getY())
				) {
					moved = true;
					direction = rightDirection;
					x += speed;
				}
				else if (x > Game.player.getX() &&
					World.isFree((int)(x - speed), getY(), 0) &&
					!isColliding((int)(x - speed), getY())
				) {
					moved = true;
					direction = leftDirection;
					x -= speed;
				}
		
				if (y < Game.player.getY() &&
					World.isFree(getX(), (int)(y + speed), 0) &&
					!isColliding(getX(), (int)(y + speed))
				) {
					moved = true;
					y += speed;
				}
				else if (y > Game.player.getY() &&
					World.isFree(getX(), (int)(y - speed), 0) &&
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
	
				isCollidingWithBullet();
				
				if (life < 1) {
					destroySelf();
					return;
				}
				
				if (isDamaged) {
					damageFrames++;
					
					if (damageFrames == 8) {
						damageFrames = 0;
						isDamaged = false;
					}
				}
			}
		}
		else if (calculateDistance(getX(), getY(), Game.player.getX(), Game.player.getY()) < 64) {
			pursue = true;
		}
	}
	*/
	
	
	public void tick () {
		moved = false;

		if (pursue) {
			moved = true;

			if (moved) {
				if (!isCollidingWithPlayer()) {
					if (path == null || path.size() == 0) {
						Vector2i start = new Vector2i((int)(x / 16), (int)(y / 16));
						Vector2i end = new Vector2i((int)(Game.player.x / 16), (int)(Game.player.y / 16));
						path = AStar.findPath(Game.world, start, end);
					}
				}
				else {
					if (Game.rand.nextInt(100) < 10) {
						Sound.HURT_EFFECT.play();
						Game.player.isDamaged = true;
						Game.player.life -= Game.rand.nextInt(3);
					}
				}

				if (Game.rand.nextInt(100) < 75) {
					followPath(path);
				}
				
				if (Game.rand.nextInt(100) < 5) {
					Vector2i start = new Vector2i((int)(x / 16), (int)(y / 16));
					Vector2i end = new Vector2i((int)(Game.player.x / 16), (int)(Game.player.y / 16));
					path = AStar.findPath(Game.world, start, end);
				}

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
		
			isCollidingWithBullet();
					
			if (life < 1) {
				destroySelf();
				return;
			}
	
			if (isDamaged) {
				damageFrames++;
	
				if (damageFrames == 8) {
					damageFrames = 0;
					isDamaged = false;
				}
			}
		}
		else if (calculateDistance(getX(), getY(), Game.player.getX(), Game.player.getY()) < 64) {
			pursue = true;
		}
	}


	public void destroySelf () {
		Game.enemies.remove(this);
		Game.entities.remove(this);
		return;
	}
	
	
	public void isCollidingWithBullet () {
		for (int i = 0; i < Game.bullets.size(); i++) {
			Entity e = Game.bullets.get(i);
			
			if (e instanceof BulletShoot) {
				if (Entity.isColliding(this, e)) {
					isDamaged = true;
					life -= 5;
					Game.bullets.remove(i);
					return;
				}
			}
		}
	}
	
	
	public boolean isCollidingWithPlayer () {
		Rectangle currentEnemy = new Rectangle(getX(), getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(), Tile.TILE_SIZE, Tile.TILE_SIZE);

		if (currentEnemy.intersects(player) &&
			this.z == Game.player.z
		) {
			return true;
		}

		return false;
	}


	public void render (Graphics g) {
		if (isDamaged) {
			if (direction == rightDirection) {
				g.drawImage(damageRightEnemy[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
			}
			else if (direction == leftDirection) {
				g.drawImage(damageLeftEnemy[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
			}
		}
		else {
			if (direction == rightDirection) {
				g.drawImage(rightEnemy[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
			}
			else if (direction == leftDirection) {
				g.drawImage(leftEnemy[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
			}
		}
	}
}