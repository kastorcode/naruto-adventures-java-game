package com.kastorcode.main;

import java.io.*;
import javax.sound.sampled.*;


public class NewerSound {
	public static class Clips {
		public Clip[] clips;
		private int p, count;


		public Clips (byte[] buffer, int count) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
			if (buffer == null) {
				return;
			}

			clips = new Clip[count];
			this.count = count;
			
			for (int i = 0; i < count; i++) {
				clips[i] = AudioSystem.getClip();
				clips[i].open(AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer)));
			}
		}


		public void play () {
			if (clips == null) { return; }

			clips[p].stop();
			clips[p].setFramePosition(0);
			clips[p].start();
			p++;
			
			if (p >= count) { p = 0; }
		}


		public void loop () {
			if (clips == null) { return; }

			clips[p].loop(300);
		}
	}


	public static Clips BG_MUSIC = load("bgmusic.wav", 1);


	private static Clips load (String name, int count) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Sound.class.getResourceAsStream("/sounds/" + name));
			byte[] buffer = new byte[1024];
			int read = 0;

			while ((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}

			dis.close();
			byte[] data = baos.toByteArray();

			return new Clips(data, count);
		}
		catch (Exception error) {
			try {
				return new Clips(null, 0);
			}
			catch (Exception error2) {
				return null;
			}
		}
	}
}