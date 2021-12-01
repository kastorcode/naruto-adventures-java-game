package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.Sound;
import com.kastorcode.world.Camera;


public class Konohamaru extends Entity {
	private boolean
		showMessage = false, framesUp = true,
		giveBonus = true;

	private int
		currentIndex = 0, phraseIndex = 0, frames = 0,
		maxFrames = 4, framesSpeed = 8, framesPause = -12,
		frameIndex = 0, maxFrameIndex = maxFrames - 1;

	private static final Sound bonusSound = new Sound("/effects/jutsu.wav");

	public String[] phrases = new String[11];

	private static BufferedImage[] sprites;


	public Konohamaru (double x, double y) {
		super(x, y, 16, 16, Spritesheet.getSprite(0, 6 * 16, 16, 16));

		phrases[0] = "Someday, you and I will fight for the Hokage title!";
		phrases[1] = "The others only see me as the grandson of the Hokage.";
		phrases[2] = "My name is Sarutobi Konohamaru.";
		phrases[3] = "Nobody recognizes me as I am, I'm sick of it.";
		phrases[4] = "You told me there are no shortcuts on the ninja path.";
		phrases[5] = "I am of the Sarutobi clan, endowed with the village name.";
		phrases[6] = "I am the grandson of the Third Hokage who protected Konohagakure.";
		phrases[7] = "I will train harder until I can pulverize Naruto!";
		phrases[8] = "Becoming Hokage is my dream.";
		phrases[9] = "Please teach me the sexy jutsu!";
		phrases[10] = "Go Naruto, defeat the enemies!";

		sprites = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			sprites[i] = Spritesheet.getSprite(0 + (i * 16), 6 * 16, 16, 16);
		}
	}


	public void tick () {
		int playerX = Game.player.getX();
		int playerY = Game.player.getY();
		int npcX = (int)x;
		int npcY = (int)y;

		if (Math.abs(playerX - npcX) < 16 && Math.abs(playerY - npcY) < 16) {
			if (!showMessage) {
				phraseIndex = Game.rand.nextInt(phrases.length);
			}

			showMessage = true;

			if (currentIndex < phrases[phraseIndex].length()) {
				currentIndex++;
			}

			if (giveBonus) {
				giveBonus = false;
				bonusSound.play();

				for (int i = 0; i < 4; i++) {
					Enemy enemy = Game.enemies.get(Game.enemies.size() - 1);
					Enemy enemy2 = new Enemy(enemy.x, enemy.y + 16, 16, 16, Entity.ENEMY);
					Game.entities.add(enemy2);
					Game.enemies.add(enemy2);
				}
			}
		}
		else {
			showMessage = false;
			currentIndex = 0;
		}

		frames++;

		if (frames > -1) {
			if (framesUp) {
				if (frames == framesSpeed) {
					frames = 0;
					frameIndex++;
	
					if (frameIndex == maxFrameIndex) {
						frames = framesPause;
						framesUp = false;
					}
				}
			}
			else {
				if (frames == framesSpeed) {
					frames = 0;
					frameIndex--;
	
					if (frameIndex == 0) {
						frames = framesPause;
						framesUp = true;
					}
				}
			}
		}
	}


	public void render (Graphics g) {
		g.drawImage(sprites[frameIndex], getX() - Camera.getX(), getY() - Camera.getY(), null);

		if (showMessage) {
			g.setFont(new Font("Arial", Font.PLAIN, 9));
			g.setColor(Color.WHITE);

			if (currentIndex < 34) {
				g.drawString(phrases[phraseIndex].substring(0, currentIndex), 4, 12);
			}
			else if (currentIndex < 68) {
				g.drawString(phrases[phraseIndex].substring(0, 34), 4, 12);
				g.drawString(phrases[phraseIndex].substring(34, currentIndex), 4, 22);
			}
			else {
				g.drawString(phrases[phraseIndex].substring(0, 34), 4, 12);
				g.drawString(phrases[phraseIndex].substring(34, 68), 4, 22);
				g.drawString(phrases[phraseIndex].substring(68, currentIndex), 4, 32);
			}
		}
	}
}