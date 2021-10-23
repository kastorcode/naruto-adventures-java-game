package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.Window;
import com.kastorcode.world.Camera;
import com.kastorcode.world.World;


public class Player extends Entity {
	public boolean right, left, up, down, isDamaged = false,
		shoot = false, mouseShoot = false;
	
	public int rightDirection = 0, leftDirection = 1,
		direction = rightDirection, munition = 10000 /*0*/, mx, my;

	public double speed = 1.4, life = 10000 /*100*/, maxLife = 100;

	private int frames = 0, maxFrames = 5,
		frameIndex = 0, maxFrameIndex = maxFrames - 1, damageFrames = 0;

	private boolean moved = false, hasWeapon = false;

	public BufferedImage[] damageRightPlayer, damageLeftPlayer;

	private BufferedImage[] rightPlayer, leftPlayer;


	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[maxFrames];
		leftPlayer = new BufferedImage[maxFrames];
		damageRightPlayer = new BufferedImage[maxFrames];
		damageLeftPlayer = new BufferedImage[maxFrames];
		
		for (int i = 0; i < maxFrames; i++) {
			rightPlayer[i] = Spritesheet.getSprite(i * 16, 16, 16, 16);
		}

		for (int i = 0; i < maxFrames; i++) {
			leftPlayer[i] = Spritesheet.getSprite(i * 16, 32, 16, 16);
		}
		
		for (int i = 0; i < maxFrames; i++) {
			damageRightPlayer[i] = Spritesheet.getSprite(i * 16, 48, 16, 16);
		}

		for (int i = 0; i < maxFrames; i++) {
			damageLeftPlayer[i] = Spritesheet.getSprite(i * 16, 64, 16, 16);
		}
	}
	
	
	public void checkCollisionWeapon () {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity entity = Game.entities.get(i);
			
			if (entity instanceof Weapon) {
				if (Entity.isColliding(this, entity)) {
					hasWeapon = true;
					Game.entities.remove(i);

					return;
				}
			}
		}
	}
	
	
	public void checkCollisionMunition () {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity entity = Game.entities.get(i);
			
			if (entity instanceof Bullet) {
				if (Entity.isColliding(this, entity)) {
					munition += 10;
					Game.entities.remove(i);

					return;
				}
			}
		}
	}
	
	
	public void checkCollisionLifePack () {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity entity = Game.entities.get(i);
			
			if (entity instanceof LifePack) {
				if (Entity.isColliding(this, entity)) {
					life += 8;
					
					if (life > 100) { life = 100; }
					
					Game.entities.remove(i);

					return;
				}
			}
		}
	}
	
	
	public void tick () {
		moved = false;

		if (right && World.isFree((int)(x + speed), getY())) {
			moved = true;
			direction = rightDirection;
			setX(x += speed);
		}
		else if (left && World.isFree((int)(x - speed), getY())) {
			moved = true;
			direction = leftDirection;
			setX(x -= speed);
		}
		
		if (up && World.isFree(getX(), (int)(y - speed))) {
			moved = true;
			setY(y -= speed);
		}
		else if (down && World.isFree(getX(), (int)(y + speed))) {
			moved = true;
			setY(y += speed);
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

		checkCollisionLifePack();
		checkCollisionWeapon();
		checkCollisionMunition();
		
		if (isDamaged) {
			damageFrames++;
			
			if (damageFrames == 8) {
				damageFrames = 0;
				isDamaged = false;
			}
		}
		
		if (shoot) {
			shoot = false;
			
			if (hasWeapon && munition > 0) {
				munition--;
				int dx, px = 0, py = 0;
				
				if (direction == rightDirection) {
					dx = 1;
				}
				else {
					px = -4;
					dx = -1;
				}
				
				BulletShoot bullet = new BulletShoot(getX() + px, getY() + py, 3, 3, null, dx, 0);
				Game.bullets.add(bullet);
			}
		}
		
		if (mouseShoot) {
			mouseShoot = false;
			
			if (hasWeapon && munition > 0) {
				munition--;

				int px = 0, py = 0;

				if (direction == leftDirection) {
					px = -4;
				}
				
				double angle = Math.atan2(
					my - (getY() + py - Camera.y),
					mx - (getX() + px - Camera.x)
				);
				
				double dx = Math.cos(angle), dy = Math.sin(angle);
				
				BulletShoot bullet = new BulletShoot(getX() + px, getY() + py, 3, 3, null, dx, dy);
				Game.bullets.add(bullet);
			}
		}
		
		if (life < 1) {
			life = 0;
			Game.state = "GAME_OVER";
		}

		updateCamera();
	}


	public void updateCamera () {
		Camera.setX(Camera.clamp(
			getX() - (Window.WIDTH / 2),
			0,
			(World.WIDTH * 16) - Window.WIDTH
		));
		Camera.setY(Camera.clamp(
			getY() - (Window.HEIGHT / 2),
			0,
			(World.HEIGHT * 16) - Window.HEIGHT
		));
	}


	public void render (Graphics g) {
		if (isDamaged) {
			if (direction == rightDirection) {
				g.drawImage(damageRightPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);

				if (hasWeapon) {
					g.drawImage(Entity.DAMAGED_PLAYER_WEAPON, getX() - Camera.getX(), getY() - Camera.getY(), null);
				}
			}
			else if (direction == leftDirection) {
				g.drawImage(damageLeftPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);

				if (hasWeapon) {
					g.drawImage(Entity.DAMAGED_PLAYER_WEAPON, getX() - 8 - Camera.getX(), getY() - Camera.getY(), null);
				}
			}
		}
		else {
			if (direction == rightDirection) {
				g.drawImage(rightPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
				
				if (hasWeapon) {
					g.drawImage(Entity.PLAYER_WEAPON, getX() - Camera.getX(), getY() - Camera.getY(), null);
				}
			}
			else if (direction == leftDirection) {
				g.drawImage(leftPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
				
				if (hasWeapon) {
					g.drawImage(Entity.PLAYER_WEAPON, getX() - 8 - Camera.getX(), getY() - Camera.getY(), null);
				}
			}
		}
	}
}