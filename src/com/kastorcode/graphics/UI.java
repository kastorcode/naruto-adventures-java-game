package com.kastorcode.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.kastorcode.main.Game;
import com.kastorcode.main.Window;


public class UI {
	public void render (Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(Window.WIDTH - 36, 4, 32, 5);

		g.setColor(Color.GREEN);
		g.fillRect(Window.WIDTH - 36, 4, Game.player.life * 32 / Game.player.MAX_LIFE, 5);

		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 7));
		g.drawString(Integer.toString(Game.player.life), Window.WIDTH - 28, 9);

		g.setFont(new Font("arial", Font.PLAIN, 7));
		g.drawString(Game.player.munition + " kunais", Window.WIDTH - 37, 17);

		g.drawString(Game.player.points + " points", 4, 9);
	}
}