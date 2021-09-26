package com.kastorcode.world;


public class Camera {
	public static int x, y;
	
	
	public static int getX () {
		return x;
	}
	
	
	public static void setX (int newX) {
		x = newX;
	}
	
	
	public static int getY () {
		return y;
	}
	
	
	public static void setY (int newY) {
		y = newY;
	}
	
	
	public static int clamp (int current, int min, int max) {
		if (current < min) {
			current = min;
		}

		if (current > max) {
			current = max;
		}

		return current;
	}
}