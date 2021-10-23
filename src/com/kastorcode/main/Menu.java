package com.kastorcode.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Menu {
	public String[] options = {
		"novo jogo", "carregar jogo", "sair"
	};
	
	public int
		currentOption = 0,
		maxOption = options.length - 1;
	
	public boolean
		up = false,
		down = false,
		enter = false,
		pause = false;


	public void tick () {
		if (up) {
			up = false;
			currentOption--;

			if (currentOption < 0) {
				currentOption = maxOption;
			}
		}
		else if (down) {
			down = false;
			currentOption++;

			if (currentOption > maxOption) {
				currentOption = 0;
			}
		}
		else if (enter) {
			enter = false;

			switch (options[currentOption]) {
				case "continuar":
				case "novo jogo": {
					pause = false;
					Game.state = "NORMAL";
					break;
				}
				
				case "sair": {
					System.exit(1);
					break;
				}
			}
		}
	}


	private Color getColor (int index) {
		if (index == currentOption) {
			return Color.BLUE;
		}
		return Color.WHITE;
	}


	public void render (Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color(0, 0, 0, 178));
		g2.fillRect(0, 0, Window.WIDTH * Window.SCALE, Window.HEIGHT * Window.SCALE);
		
		g.setColor(Color.ORANGE);
		g.setFont(new Font("arial", Font.BOLD, 32));
		g.drawString("NARUTO  ADVENTURES", (Window.WIDTH * Window.SCALE) / 5, (Window.HEIGHT * Window.SCALE) / 5);

		g.setFont(new Font("arial", Font.BOLD, 24));
		g.setColor(getColor(0));
		if (pause) {
			g.drawString("Continuar", (Window.WIDTH * Window.SCALE) / 5, (Window.HEIGHT * Window.SCALE) / 3);
		}
		else {
			g.drawString("Novo jogo", (Window.WIDTH * Window.SCALE) / 5, (Window.HEIGHT * Window.SCALE) / 3);
		}
		g.setColor(getColor(1));
		g.drawString("Carregar jogo", (Window.WIDTH * Window.SCALE) / 5, (int)((Window.HEIGHT * Window.SCALE) / 2.4));
		g.setColor(getColor(2));
		g.drawString("Sair", (Window.WIDTH * Window.SCALE) / 5, (Window.HEIGHT * Window.SCALE) / 2);
	}
}