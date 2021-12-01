package com.kastorcode.main;

import java.applet.Applet;
import java.applet.AudioClip;


@SuppressWarnings("deprecation")
public class Sound {
	private AudioClip clip;


	public Sound (String name) {
		try {
			clip = Applet.newAudioClip(
				Sound.class.getResource("/sounds" + name)
			);

			clip.play();
			clip.stop();
		}
		catch (Throwable error) {}
	}


	public void play () {
		try {
			clip.play();
		}
		catch (Throwable error) {}
	}


	public void stop () {
		try {
			clip.stop();
		}
		catch (Throwable error) {}
	}


	public void loop () {
		try {
			clip.loop();
		}
		catch (Throwable error) {}
	}
}