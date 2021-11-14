package com.kastorcode.main;

import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class Window extends Canvas {
	private static final long serialVersionUID = 1L;

	public static final int WIDTH = 240, HEIGHT = 160, SCALE = 3;

	public static JFrame frame;

	
	public Window () {
		//setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));

		frame = new JFrame("Game #1");
		frame.add(this);
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.pack();
		
		Image icon = null;

		try {
			icon = ImageIO.read(getClass().getResource("/images/icon.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image cursorImage = toolkit.getImage(getClass().getResource("/images/cursor.png"));
		Cursor cursor = toolkit.createCustomCursor(cursorImage, new Point(0, 0), "img");

		frame.setCursor(cursor);
		frame.setIconImage(icon);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		requestFocus();
	}
}