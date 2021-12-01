package com.kastorcode.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Menu {
	public String[] options = {
		"new game", "load game", "exit"
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

	public static final NewerSound menuSound = new NewerSound("/bg/main_theme.wav");


	public Menu () {
		menuSound.loop();
	}


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
				case "continue":
				case "new game": {
					if (!pause) {
						File file = new File("save.txt");
						
						if (file.exists()) {
							file.delete();
						}
					}

					menuSound.stop();

					if (Game.bgSound != null) {
						Game.bgSound.loop();
					}

					pause = false;
					Game.state = "NORMAL";
					break;
				}
				
				case "load game": {
					File file = new File("save.txt");
					
					if (file.exists()) {
						menuSound.stop();
						String saver = loadGame(10);
						applySave(saver);
					}

					break;
				}
				
				case "exit": {
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
			String[] key_value = keys[i].split(" ");

			switch (key_value[0]) {
				case "level": {
					Game.currentLevel = Integer.parseInt(key_value[1]);
					Game.over("level" + key_value[1] + ".png");
					break;
				}

				case "life": {
					Game.player.life = Integer.parseInt(key_value[1]);
					break;
				}

				case "munition": {
					Game.player.munition = Integer.parseInt(key_value[1]);
					break;
				}

				case "points": {
					Game.player.points = Integer.parseInt(key_value[1]);
					break;
				}
			}
		}

		Game.state = "NORMAL";
		pause = false;
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
						String[] key = singleLine.split(" ");
						char[] value = key[1].toCharArray();
						key[1] = "";

						for (int i = 0; i < value.length; i++) {
							value[i] -= encode;
							key[1] += value[i];
						}
						
						line += key[0];
						line += " ";
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
			key += " ";
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
		g.setColor(new Color(0, 0, 0, 204));
		g.fillRect(0, 0, Window.WIDTH, Window.HEIGHT);

		g.setColor(Color.ORANGE);
		g.setFont(new Font("arial", Font.BOLD, 12));
		g.drawString("NARUTO  ADVENTURES", Window.WIDTH / 5, Window.HEIGHT / 5);

		g.setFont(new Font("arial", Font.BOLD, 10));
		g.setColor(getColor(0));

		if (pause) {
			g.drawString("Continue", Window.WIDTH / 5, Window.HEIGHT / 3);
		}
		else {
			g.drawString("New game", Window.WIDTH / 5, Window.HEIGHT / 3);
		}

		g.setColor(getColor(1));
		g.drawString("Load game", Window.WIDTH / 5, (int)(Window.HEIGHT / 2.3));

		g.setColor(getColor(2));
		g.drawString("Exit", Window.WIDTH / 5, (int)(Window.HEIGHT / 1.85));
	}
}