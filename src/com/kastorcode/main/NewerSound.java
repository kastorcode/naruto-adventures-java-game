package com.kastorcode.main;

import java.io.*;
import javax.sound.sampled.*;


public class NewerSound {
	private Clip clip = null;


	public NewerSound (String name) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream("/sounds" + name));
			byte[] buffer = new byte[1024];
			int read = 0;

			while ((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}

			dis.close();
			byte[] data = baos.toByteArray();

			if (data == null) { return; }

			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(data)));
		}
		catch (Exception error) {}
	}


	public void play () {
		if (clip == null) { return; }

		clip.start();
	}


	public void pause () {
		if (clip == null) { return; }

		clip.stop();
	}


	public void stop () {
		if (clip == null) { return; }

		clip.stop();
		clip.setFramePosition(0);
	}


	public void loop () {
		if (clip == null) { return; }

		clip.loop(500);
	}
}