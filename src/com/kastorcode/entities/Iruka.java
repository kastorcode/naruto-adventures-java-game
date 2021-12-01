package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.Sound;
import com.kastorcode.world.Camera;


public class Iruka extends Entity {
	private boolean
		showMessage = false, framesUp = true,
		giveBonus = true;

	private int
		currentIndex = 0, phraseIndex = 0, frames = 0,
		maxFrames = 3, framesSpeed = 7, framesPause = -192,
		frameIndex = 0, maxFrameIndex = maxFrames - 1;

	private static final Sound bonusSound = new Sound("/effects/jutsu.wav");

	public String[] phrases = new String[14];

	private static BufferedImage[] sprites;


	public Iruka (double x, double y) {
		super(x, y, 16, 16, Spritesheet.getSprite(0, 5 * 16, 16, 16));

		phrases[0] = "Powered by KastorCode.";
		phrases[1] = "kastorcode@gmail.com";
		phrases[2] = "My name is Umino Iruka.";
		phrases[3] = "https://github.com/kastorcode";
		phrases[4] = "Programmed by Matheus Ramalho de Oliveira.";
		phrases[5] = "He is a Brazilian full-stack developer.";
		phrases[6] = "Stop acting like a baby, you wanna know what I really think of you?!";
		phrases[7] = "You are one of my students, and, you are like a little brother to me.";
		phrases[8] = "I already expected you would try to run out onto the battlefield.";
		phrases[9] = "I know you are ready to fight.";
		phrases[10] = "There's something I need to tell you: come back alive, Naruto!";
		phrases[11] = "I know how you feel, Naruto.";
		phrases[12] = "You feel loneliness, and it hurts inside.";
		phrases[13] = "There's no need to be logical, people just sacrifice for something that's really important to them.";

		sprites = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			sprites[i] = Spritesheet.getSprite(0 + (i * 16), 5 * 16, 16, 16);
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
				Enemy enemy = Game.enemies.get(Game.enemies.size() - 1);
				Game.entities.add(new LifePack(enemy.x, enemy.y + 16, 16, 16, Entity.LIFEPACK));
				Game.entities.add(new Weapon(enemy.x, enemy.y + 32, 16, 16, Entity.WEAPON));
				Game.entities.add(new Bullet(enemy.x, enemy.y + 48, 16, 16, Entity.BULLET));
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