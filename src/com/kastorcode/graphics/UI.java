package com.kastorcode.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.kastorcode.main.Game;
import com.kastorcode.main.Window;


public class UI {
	public void render (Graphics g) {
		g.setColor(Color.RED);
		g.fillRect(Window.WIDTH - 54, 4, 50, 8);

		g.setColor(Color.GREEN);
		g.fillRect(Window.WIDTH - 54, 4, (int)((Game.player.life / Game.player.maxLife) * 50), 8);
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial", Font.BOLD, 8));
		g.drawString(Integer.toString((int)Game.player.life), Window.WIDTH - 35, 11);
		
		g.setFont(new Font("arial", Font.PLAIN, 7));
		g.drawString("Munição: " + Game.player.munition, Window.WIDTH - 45, 20);
	}
}