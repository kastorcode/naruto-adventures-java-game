package com.kastorcode.main;

import java.applet.Applet;
import java.applet.AudioClip;


public class Sound {
	private AudioClip clip;
	
	public static final Sound BG_MUSIC = new Sound("bgmusic.wav");

	public static final Sound HURT_EFFECT = new Sound("hurt.wav");


	@SuppressWarnings("deprecation")
	private Sound (String name) {
		try {
			clip = Applet.newAudioClip(
				Sound.class.getResource("/sounds/" + name)
			);
		}
		catch (Throwable error) {}
	}
	
	
	public void play () {
		try {
			new Thread() {
				public void run () {
					clip.play();
				}
			}
			.start();
		}
		catch (Throwable error) {}
	}
	
	
	public void loop () {
		try {
			new Thread() {
				public void run () {
					clip.loop();
				}
			}
			.start();
		}
		catch (Throwable error) {}
	}
}