package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.NewerSound;
import com.kastorcode.main.Window;
import com.kastorcode.world.Camera;
import com.kastorcode.world.World;


public class Player extends Entity {
	public final double
		SPEED = 1.0, MAX_SPEED = 2.0;

	public final int MAX_LIFE = 100;

	public boolean
		right, left, up, down, isDamaged = false,
		hasWeapon = false, shoot = false, mouseShoot = false,
		jump = false, isJumping = false, jumpUp = false,
		jumpDown = false;

	public int life = MAX_LIFE, rightDirection = 0,
		leftDirection = 1, direction = rightDirection,
		munition = 0, points = 0, mx, my, z = 0,
		jumpSpeed = 2, jumpFrames = 32, jumpCurrentFrame = 0;

	public double speed;

	private int frames = 0, maxFrames = 5,
		frameIndex = 0, maxFrameIndex = maxFrames - 1,
		damageFrames = 0;

	private boolean moved = false;

	public BufferedImage[] damageRightPlayer, damageLeftPlayer;

	private BufferedImage[] rightPlayer, leftPlayer;

	private static final NewerSound runningSound = new NewerSound("/effects/running.wav");
	private static final NewerSound jumpingSound = new NewerSound("/effects/jumping.wav");


	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		if (Game.currentLevel > 1 && Game.player != null) {
			life = Game.player.life;
			munition = Game.player.munition;
			points = Game.player.points;
		}

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
					Game.player.munition++;
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
					Game.player.munition += new Random().nextInt(
						Enemy.LIFE * 4 - Enemy.LIFE * 3) +
						Enemy.LIFE * 3;
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
					Game.entities.remove(i);
					life += new Random().nextInt(
						Enemy.LIFE * 3 - Enemy.LIFE * 2) +
						Enemy.LIFE * 2;
					return;
				}
			}
		}
	}
	
	
	public void tick () {
		depth = 1;

		if (jump && isJumping == false) {
			if (life > 1 || Game.rand.nextInt(100) < 33) {
				runningSound.stop();
				jumpingSound.stop();
				jumpingSound.play();
				jump = false;
				isJumping = true;
				jumpUp = true;

				if (life > 1) {
					life -= Game.rand.nextInt(2);
				}
			}
			else {
				jump = false;
			}
		}

		if (isJumping) {
			if (jumpUp) {
				jumpCurrentFrame += jumpSpeed;
			}
			else if (jumpDown) {
				jumpCurrentFrame -= jumpSpeed;
				
				if (jumpCurrentFrame < 1) {
					isJumping = false;
					jumpUp = false;
					jumpDown = false;
				}
			}
			z = jumpCurrentFrame;
			
			if (jumpCurrentFrame >= jumpFrames) {
				jumpUp = false;
				jumpDown = true;
			}
		}

		moved = false;

		if (right && World.isFree((int)(x + speed), getY(), z)) {
			moved = true;
			direction = rightDirection;
			setX(x += speed);
		}
		else if (left && World.isFree((int)(x - speed), getY(), z)) {
			moved = true;
			direction = leftDirection;
			setX(x -= speed);
		}
		
		if (up && World.isFree(getX(), (int)(y - speed), z)) {
			moved = true;
			setY(y -= speed);
		}
		else if (down && World.isFree(getX(), (int)(y + speed), z)) {
			moved = true;
			setY(y += speed);
		}
		
		if (moved) {
			if (z == 0 && frames == 4) {
				runningSound.loop();
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
			runningSound.stop();
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
				g.drawImage(damageRightPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY() - z, null);

				if (hasWeapon) {
					g.drawImage(Entity.DAMAGED_PLAYER_WEAPON, getX() - Camera.getX(), getY() - Camera.getY() - z, null);
				}
			}
			else if (direction == leftDirection) {
				g.drawImage(damageLeftPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY() - z, null);

				if (hasWeapon) {
					g.drawImage(Entity.DAMAGED_PLAYER_WEAPON, getX() - 8 - Camera.getX(), getY() - Camera.getY() - z, null);
				}
			}
		}
		else {
			if (direction == rightDirection) {
				g.drawImage(rightPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY() - z, null);
				
				if (hasWeapon) {
					g.drawImage(Entity.PLAYER_WEAPON, getX() - Camera.getX(), getY() - Camera.getY() - z, null);
				}
			}
			else if (direction == leftDirection) {
				g.drawImage(leftPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY() - z, null);
				
				if (hasWeapon) {
					g.drawImage(Entity.PLAYER_WEAPON, getX() - 8 - Camera.getX(), getY() - Camera.getY() - z, null);
				}
			}
		}
		
		if (isJumping) {
			g.setColor(Color.BLACK);
			g.fillOval(getX() - Camera.getX() + 4, getY() - Camera.getY() + 16, 8, 8);
		}
	}
}