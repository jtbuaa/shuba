package com.dev1024.utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;

public class CommonUtil {

	public static void Vibrate(final Activity activity, long milliseconds) {
		Vibrator vib = null;
		vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(milliseconds);
		if (vib != null) {
			vib.cancel();
			vib = null;
		}
	}

	public static void Vibrate(Context context, long[] pattern, int repeat) {
		Vibrator vib = (Vibrator) context
				.getSystemService(Service.VIBRATOR_SERVICE);
		vib.vibrate(pattern, repeat);
		if (vib != null) {
			vib.cancel();
			vib = null;
		}
	}

	public static void playNotificationVoice(Context context, int resid) {
		MediaPlayer mediaPlayer = null;
		try {
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION) != 0) {
				mediaPlayer = MediaPlayer.create(context, resid);
				if (mediaPlayer != null) {
					mediaPlayer.stop();
				}
				mediaPlayer
						.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
				mediaPlayer.setLooping(false);
				mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
					@Override
					public void onCompletion(MediaPlayer mp) {
						if (mp != null && mp.isPlaying()) {
							mp.stop();
						}
						mp.release();
						mp = null;
					}
				});
				mediaPlayer.prepare();
				mediaPlayer.start();
			}
		} catch (Exception e) {
		} finally {
			context = null;
			mediaPlayer = null;
		}
	}

}
