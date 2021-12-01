package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kastorcode.main.Game;
import com.kastorcode.world.Camera;


public class Particle extends Entity {
	public int
		lifeTime = 4, currentTime = 0, speed = 1;

	public double
		dx = 0, dy = 0;


	public Particle(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		dx = new Random().nextGaussian();
		dy = new Random().nextGaussian();
	}


	public void tick () {
		x += dx * speed;
		y += dy * speed;
		currentTime++;

		if (currentTime == lifeTime) {
			Game.entities.remove(this);
		}
	}


	public void render (Graphics g) {
		g.setColor(Color.ORANGE);
		g.fillRect(getX() - Camera.getX(), getY() - Camera.getY(), width, height);
	}
}