package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.graphics.Spritesheet;
import com.kastorcode.main.Game;
import com.kastorcode.main.Sound;
import com.kastorcode.world.Camera;


public class Kakashi extends Entity {
	private boolean
		showMessage = false, framesUp = true,
		giveBonus = true;

	private int
		currentIndex = 0, phraseIndex = 0, frames = 0,
		maxFrames = 3, framesSpeed = 48, framesPause = -256,
		frameIndex = 0, maxFrameIndex = maxFrames - 1;

	private static final Sound bonusSound = new Sound("/effects/jutsu.wav");

	public String[] phrases = new String[23];

	private static BufferedImage[] sprites;


	public Kakashi (double x, double y) {
		super(x, y, 16, 16, Spritesheet.getSprite(0, 7 * 16, 16, 16));

		phrases[0] = "I'm known as the ninja who copy.";
		phrases[1] = "My Sharingan and my Genjutsu can paralyze opponents!";
		phrases[2] = "My name is Hatake Kakashi.";
		phrases[3] = "I will not allow my comrades to die. I will protect you with my life.";
		phrases[4] = "Knowing what is right and ignoring it is the act of a coward.";
		phrases[5] = "In the ninja world, whoever breaks the rules is rubbish, but whoever abandons his friends is worse than rubbish.";
		phrases[6] = "If your trusted companions gather around you, hope can take physical form and become visible.";
		phrases[7] = "The emptiness in your heart fills with whoever is on your side.";
		phrases[8] = "As long as you don't give up, there will always be salvation.";
		phrases[9] = "Forget revenge, the fate of those seeking revenge is bleak.";
		phrases[10] = "Even if you get your revenge, the only thing that will persist is emptiness.";
		phrases[11] = "There are times when compassionate words only make things worse.";
		phrases[12] = "You didn't get it, you think you got it, that means you didn't get it, got it?";
		phrases[13] = "A ninja must see through deception.";
		phrases[14] = "Being different doesn't always mean being better.";
		phrases[15] = "I'm sorry I'm late, I got lost in the paths of life.";
		phrases[16] = "The place where people think of you is the place you must always return to.";
		phrases[17] = "Well, even though you don't accept it, that's the way it is.";
		phrases[18] = "As for my life's goal, I haven't really thought about it.";
		phrases[19] = "You talk like you're the best, but you're still the worst.";
		phrases[20] = "Life is like a mirror that just reflects what you do in it.";
		phrases[21] = "When love is real, the mind can forget, but it will remain forever in the deepest place of the heart.";
		phrases[22] = "Remember: the one who knows how to wait, will be expected.";

		sprites = new BufferedImage[maxFrames];

		for (int i = 0; i < maxFrames; i++) {
			sprites[i] = Spritesheet.getSprite(0 + (i * 16), 7 * 16, 16, 16);
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

				for (int i = 0; i < Game.enemies.size(); i++) {
					Enemy enemy = Game.enemies.get(i);
					enemy.pursue = false;
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