package com.kastorcode.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Window;
import com.kastorcode.world.Camera;
import com.kastorcode.world.World;


public class Player extends Entity {
	public boolean right, left, up, down;
	
	public int rightDirection = 0, leftDirection = 1,
		direction = rightDirection;

	public double speed = 1.4;

	private int frames = 0, maxFrames = 5,
		frameIndex = 0, maxFrameIndex = maxFrames - 1;

	private boolean moved = false;

	private BufferedImage[] rightPlayer, leftPlayer;


	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[maxFrames];
		leftPlayer = new BufferedImage[maxFrames];
		
		for (int i = 0; i < maxFrames; i++) {
			rightPlayer[i] = Spritesheet.getSprite(32 + (i * 16), 0, 16, 16);
		}

		for (int i = 0; i < maxFrames; i++) {
			leftPlayer[i] = Spritesheet.getSprite(32 + (i * 16), 16, 16, 16);
		}
	}
	
	
	public void tick () {
		moved = false;

		if (right) {
			moved = true;
			direction = rightDirection;
			setX(x += speed);
		}
		else if (left) {
			moved = true;
			direction = leftDirection;
			setX(x -= speed);
		}
		
		if (up) {
			moved = true;
			setY(y -= speed);
		}
		else if (down) {
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
		if (direction == rightDirection) {
			g.drawImage(rightPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
		}
		else if (direction == leftDirection) {
			g.drawImage(leftPlayer[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);
		}
	}
}