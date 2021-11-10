package com.kastorcode.world;


public class Vector2i {
	public int x, y;


	public Vector2i (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public boolean equals (Object object) {
		Vector2i vector = (Vector2i)object;
		
		if (vector.x == x && vector.y == y) {
			return true;
		}
		
		return false;
	}
}