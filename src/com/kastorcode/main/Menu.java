package com.kastorcode.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


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
		enter = false;

	public static boolean
		pause = false,
		saveExists = false,
		saveGame = false;


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
					if (!pause) {
						File file = new File("save.txt");
						
						if (file.exists()) {
							file.delete();
						}
					}

					pause = false;
					Game.state = "NORMAL";
					break;
				}
				
				case "carregar jogo": {
					File file = new File("save.txt");
					
					if (file.exists()) {
						String saver = loadGame(10);
						applySave(saver);
					}

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
	
	
	public static void applySave (String save) {
		String[] keys = save.split("/");
		
		for (int i = 0; i < keys.length; i++) {
			String[] key_value = keys[i].split(":");
			
			switch (key_value[0]) {
				case "level": {
					Game.over("level" + key_value[1] + ".png");
					Game.state = "NORMAL";
					pause = false;
					break;
				}
			}
		}
	}


	public static String loadGame (int encode) {
		String line = "";
		File file = new File("save.txt");
		
		if (file.exists()) {
			try {
				String singleLine = null;
				
				BufferedReader reader = new BufferedReader(
					new FileReader("save.txt")
				);
				
				try {
					while ((singleLine = reader.readLine()) != null) {
						String[] key = singleLine.split(":");
						char[] value = key[1].toCharArray();
						key[1] = "";
						
						for (int i = 0; i < value.length; i++) {
							value[i] -= encode;
							key[1] += value[i];
						}
						
						line += key[0];
						line += ":";
						line += key[1];
						line += "/";
					}
				}
				catch (IOException error) {}
			}
			catch (FileNotFoundException error) {}
		}
		
		return line;
	}


	public static void saveGame (String[] keys, int[] values, int encode) {
		BufferedWriter write = null;
		
		try {
			write = new BufferedWriter(
				new FileWriter("save.txt")
			);
		}
		catch (IOException error) {}
		
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			key += ":";
			char[] value = Integer.toString(values[i]).toCharArray();
			
			for (int j = 0; j < value.length; j++) {
				value[j] += encode;
				key += value[j];
			}
			
			try {
				write.write(key);
				
				if (i < keys.length - 1) {
					write.newLine();
				}
			}
			catch (IOException error) {}
		}
		
		try {
			write.flush();
			write.close();
		}
		catch (IOException error) {}
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