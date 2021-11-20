package com.kastorcode.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.kastorcode.main.Game;
import com.kastorcode.main.Window;


public class Npc extends Entity {
	public boolean showMessage = false;

	public int currentIndex = 0;

	public String[] phrases = new String[5];


	public Npc(double x, double y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);

		phrases[0] = "Olá, seja muito bem vindo ao jogo!";
	}


	public void tick () {
		int playerX = Game.player.getX();
		int playerY = Game.player.getY();
		int npcX = (int)x;
		int npcY = (int)y;

		if (Math.abs(playerX - npcX) < 24 && Math.abs(playerY - npcY) < 24) {
			showMessage = true;

			if (currentIndex < phrases[0].length()) {
				currentIndex++;
			}
		}
		else {
			showMessage = false;
			currentIndex = 0;
		}
	}


	public void render (Graphics g) {
		super.render(g);
		
		if (showMessage) {
			g.setColor(Color.WHITE);
			g.fillRect(9, 9, Window.WIDTH - 18, Window.HEIGHT - 18);

			g.setColor(Color.BLUE);
			g.fillRect(10, 10, Window.WIDTH - 20, Window.HEIGHT - 20);

			g.setFont(new Font("Arial", Font.BOLD, 7));
			g.setColor(Color.WHITE);
			g.drawString(phrases[0].substring(0, currentIndex), (int)x, (int)y);
		}
	}
}