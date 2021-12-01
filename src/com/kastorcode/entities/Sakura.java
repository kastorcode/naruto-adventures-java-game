package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.Sound;
import com.kastorcode.world.Camera;


public class Sakura extends Entity {
	private boolean
		showMessage = false, giveBonus = true;

	private int
		currentIndex = 0, phraseIndex = 0, frames = 0, maxFrames = 2,
		framesSpeed = maxFrames * 16, framesPause = 0 - framesSpeed * 2,
		frameIndex = 0, maxFrameIndex = maxFrames - 1;

	private static final Sound bonusSound = new Sound("/effects/jutsu.wav");

	public String[] phrases = new String[14];

	private static BufferedImage[] sprites;


	public Sakura (double x, double y) {
		super(x, y, 16, 16, Spritesheet.getSprite(0, 8 * 16, 16, 16));

		phrases[0] = "I am a medical kunoichi.";
		phrases[1] = "I can restore some of your life!";
		phrases[2] = "My name is Haruno Sakura.";
		phrases[3] = "Did not you know? Woman has to be strong to survive!";
		phrases[4] = "I've just been doing it all wrong... just to screw it up...";
		phrases[5] = "I don't want to screw up anymore... I don't want to screw it up anymore!";
		phrases[6] = "It's a difficult thing to get your thoughts to reach someone.";
		phrases[7] = "A smile is the easiest way out of a difficult situation.";
		phrases[8] = "⁠Each of us must do what we can! If we're going to die anyway, then it's better to die fighting than do nothing!";
		phrases[9] = "A love is like a mirror that can be easily broken.";
		phrases[10] = "The most important things are not written in a book, you have to learn them by experiencing them alone.";
		phrases[11] = "When a girl truly falls in love, her feelings don't change easily.";
		phrases[12] = "But no matter how much we think about someone, there are some who won't return.";
		phrases[13] = "I'll never depend on others again, I won't run away or cry...";

		sprites = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			sprites[i] = Spritesheet.getSprite(0 + (i * 16), 8 * 16, 16, 16);
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

			if (giveBonus || Game.player.life <= Enemy.LIFE) {
				giveBonus = false;
				bonusSound.play();
				Game.player.life += new Random().nextInt(
					Enemy.LIFE * 2 - Enemy.LIFE) + Enemy.LIFE;
			}
		}
		else {
			showMessage = false;
			currentIndex = 0;
		}

		frames++;

		if (frames > -1) {
			if (frames == framesSpeed) {
				frames = 0;
				frameIndex++;

				if (frameIndex > maxFrameIndex) {
					frames = framesPause;
					frameIndex = 0;
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