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


public class Sasuke extends Entity {
	private boolean
		showMessage = false, framesUp = true,
		giveBonus = true;

	private int
		currentIndex = 0, phraseIndex = 0, frames = 0, maxFrames = 4,
		framesSpeed = maxFrames * 3, framesPause = 0 - framesSpeed * 2,
		frameIndex = 0, maxFrameIndex = maxFrames - 1;

	private static final Sound bonusSound = new Sound("/effects/jutsu.wav");

	public String[] phrases = new String[20];

	private static BufferedImage[] sprites;


	public Sasuke (double x, double y) {
		super(x, y, 16, 16, Spritesheet.getSprite(0, 9 * 16, 16, 16));

		phrases[0] = "There's training tomorrow, loser!";
		phrases[1] = "You're not scared, are you? Scared brat!";
		phrases[2] = "My name is Uchiha Sasuke.";
		phrases[3] = "What I have is not a dream, because I'm going to make it a reality.";
		phrases[4] = "I will restore my clan, and destroy a certain someone.";
		phrases[5] = "It's because we have ties that we suffer...";
		phrases[6] = "I must gain power... I am an avenger!";
		phrases[7] = "In the end, I decided to get revenge. That was always my purpose for living.";
		phrases[8] = "Tears and rain fall on my face, my body is unable to stay, but my heart is still not willing to leave.";
		phrases[9] = "I closed my eyes too long, my goals are in darkness.";
		phrases[10] = "Just shut up at once! What the hell do you know about this?!";
		phrases[11] = "You were alone from the start, what makes you think you know anything about this?!";
		phrases[12] = "Naruto... It's too late, nothing you can say will change me!";
		phrases[13] = "The shinobi's soul remains the same, even for his son.";
		phrases[14] = "I've told you this before, I'm an avenger!";
		phrases[15] = "I don't care for this test, Chunin or not whatever.";
		phrases[16] = "I'm strong? That's the answer I want.";
		phrases[17] = "I think anything I say now would be useless.";
		phrases[18] = "I will never stop, no matter what you say.";
		phrases[19] = "When a person knows love, also takes the risk of knowing hate.";

		sprites = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			sprites[i] = Spritesheet.getSprite(0 + (i * 16), 9* 16, 16, 16);
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

			if (giveBonus || Game.player.munition == 0) {
				giveBonus = false;
				bonusSound.play();
				Game.player.hasWeapon = true;
				Game.player.munition += new Random().nextInt(
					Enemy.LIFE * 3 - Enemy.LIFE * 2) +
					Enemy.LIFE * 2;
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

			if (currentIndex < 35) {
				g.drawString(phrases[phraseIndex].substring(0, currentIndex), 4, 12);
			}
			else if (currentIndex < 70) {
				g.drawString(phrases[phraseIndex].substring(0, 35), 4, 12);
				g.drawString(phrases[phraseIndex].substring(35, currentIndex), 4, 22);
			}
			else {
				g.drawString(phrases[phraseIndex].substring(0, 35), 4, 12);
				g.drawString(phrases[phraseIndex].substring(35, 70), 4, 22);
				g.drawString(phrases[phraseIndex].substring(70, currentIndex), 4, 32);
			}
		}
	}
}