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


public class Enemy extends Entity {
	public static final int LIFE = 8;

	private final double
		SPEED = 0.6, MAX_SPEED = 1.6;

	public int rightDirection = 0, leftDirection = 1,
			direction = rightDirection;

	private double speed;

	private int
		life = LIFE, frames = 0, maxFrames = 5,
		frameIndex = 0, maxFrameIndex = maxFrames - 1,
		damageFrames = 0;

	private boolean moved = false;

	public boolean isDamaged = false, pursue = false;
	
	private static BufferedImage[] rightEnemy, leftEnemy;

	public static BufferedImage[] damageRightEnemy, damageLeftEnemy;

	private static final Sound hurt = new Sound("/effects/hurt.wav");


	public Enemy(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		switch (Game.currentLevel) {
			case 1: {
				speed = SPEED;
				break;
			}
			case 2: {
				speed = SPEED + 0.10;
				break;
			}
			case 3: {
				speed = SPEED + 0.15;
				break;
			}
			case 4: {
				speed = SPEED + 0.20;
				break;
			}
			case 5: {
				speed = SPEED + 0.25;
				break;
			}
			case 6: {
				speed = SPEED + 0.30;
				break;
			}
			case 7: {
				speed = SPEED + 0.35;
				break;
			}
			case 8: {
				speed = SPEED + 0.40;
				break;
			}
			case 9: {
				speed = SPEED + 0.45;
				break;
			}
			case 10: {
				speed = SPEED + 0.50;
				break;
			}
			case 11: {
				speed = SPEED + 0.55;
				break;
			}
			case 12: {
				speed = SPEED + 0.60;
				break;
			}
			case 13: {
				speed = SPEED + 0.65;
				break;
			}
			case 14: {
				speed = SPEED + 0.70;
				break;
			}
			case 15: {
				speed = SPEED + 0.75;
				break;
			}
			case 16: {
				speed = SPEED + 0.80;
				break;
			}
			case 17: {
				speed = SPEED + 0.85;
				break;
			}
			case 18: {
				speed = SPEED + 0.90;
				break;
			}
			case 19: {
				speed = SPEED + 0.95;
				break;
			}
			case 20: {
				speed = MAX_SPEED;
				break;
			}
		}
		
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


	// Logic without the AStar algorithm
	/* public void tick () {
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
	} */


	// Logic with the AStar algorithm
	public void tick () {
		depth = 0;
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
					if (Game.rand.nextInt(100) < 50) {
						Game.player.isDamaged = true;
						Game.player.life -= Game.rand.nextInt(3);
						hurt.play();
					}
				}

				followPath(path, speed);

				if (Game.rand.nextInt(100) < 20) {
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
				Game.player.points += Game.player.life;
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
					life -= Game.rand.nextInt(3);
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