package com.jphilli85.deviceinfo.unit;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

public class Audio extends Unit {
	
	private static final int API = Build.VERSION.SDK_INT;
	
	private AudioManager mAudioManager;
	
	public Audio(Context context) {
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public AudioManager getAudioManager() {
		return mAudioManager;
	}
	
	// TODO ui facing strings
	public String getMode() {
		int mode = mAudioManager.getMode();
		if (mode == AudioManager.MODE_CURRENT) return "MODE_CURRENT";
		else if (mode == AudioManager.MODE_IN_CALL) return "MODE_IN_CALL";
		else if (API >= 11 && mode == AudioManager.MODE_IN_COMMUNICATION) return "MODE_IN_COMMUNICATION";
		else if (mode == AudioManager.MODE_INVALID) return "MODE_INVALID";
		else if (mode == AudioManager.MODE_NORMAL) return "MODE_NORMAL";
		else if (mode == AudioManager.MODE_RINGTONE) return "MODE_RINGTONE";
		else return null;
	}
	
	// TODO ui facing strings
	public String getRingerMode() {
		int mode = mAudioManager.getRingerMode();
		if (mode == AudioManager.RINGER_MODE_NORMAL) return "RINGER_MODE_NORMAL";
		else if (mode == AudioManager.RINGER_MODE_SILENT) return "RINGER_MODE_SILENT";			
		else if (mode == AudioManager.RINGER_MODE_VIBRATE) return "RINGER_MODE_VIBRATE";
		else return null;
	}
	
	public int getAlarmVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
	}
	
	public int getAlarmVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
	}
	
	public int getDtmfVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_DTMF);
	}
	
	public int getDtmfVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_DTMF);
	}
	
	public int getMusicVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	}
	
	public int getMusicVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	}
	
	public int getNotificationVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
	}
	
	public int getNotificationVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
	}
	
	public int getRingVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
	}
	
	public int getRingVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
	}
	
	public int getSystemVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
	}
	
	public int getSystemVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
	}
	
	public int getCallVolume() {
		return mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
	}
	
	public int getCallVolumeMax() {
		return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
	}
	
	public String getRingerVibrateSetting() {
		return getVibrateSetting(mAudioManager.getVibrateSetting(
				AudioManager.VIBRATE_TYPE_RINGER));
	}
	
	public String getNotificationVibrateSetting() {
		return getVibrateSetting(mAudioManager.getVibrateSetting(
				AudioManager.VIBRATE_TYPE_NOTIFICATION));
	}
	
	private String getVibrateSetting(int type) {
		if (type == AudioManager.VIBRATE_SETTING_OFF) return "VIBRATE_SETTING_OFF";
		else if (type == AudioManager.VIBRATE_SETTING_ON) return "VIBRATE_SETTING_ON";
		else if (type == AudioManager.VIBRATE_SETTING_ONLY_SILENT) return "VIBRATE_SETTING_ONLY_SILENT";
		else return null;
	}
	
	public boolean isBluetoothA2dpOn() {
		return mAudioManager.isBluetoothA2dpOn();
	}
	
	@SuppressLint("NewApi")
	public boolean isBluetoothScoAvailableOffCall() {
		return API >= 8 && mAudioManager.isBluetoothScoAvailableOffCall();
	}
	
	public boolean isBluetoothScoOn() {
		return mAudioManager.isBluetoothScoOn();
	}
	
	public boolean isMicrophoneMute() {
		return mAudioManager.isMicrophoneMute();
	}
	
	public boolean isMusicActive() {
		return mAudioManager.isMusicActive();
	}
	
	public boolean isSpeakerphoneOn() {
		return mAudioManager.isSpeakerphoneOn();
	}
	
	public boolean isWiredHeadsetConnected() {
		return mAudioManager.isWiredHeadsetOn();
	}
	
	public String[] getSupportedFormats() {
		List<String> formats = new ArrayList<String>();
		formats.add("AMR_NB");
		formats.add("AMR_RAW");
		formats.add("DEFAULT");
		formats.add("THREE_GPP");
		if (API >= 10) {
			formats.add("AAC");
			formats.add("AMR_WB");
		}
		return formats.toArray(new String[formats.size()]);
	}
	
	public String[] getSupportedSources() {
		List<String> sources = new ArrayList<String>();
		sources.add("CAMCORDER");
		sources.add("DEFAULT");
		sources.add("MIC");
		sources.add("VOICE_CALL");		
		sources.add("VOICE_DOWNLINK");		
		sources.add("VOICE_UPLINK");
		sources.add("VOICE_RECOGNITION");
		if (API >= 11) {
			sources.add("VOICE_COMMUNICATION");
		}
		return sources.toArray(new String[sources.size()]);
	}
	
	//TODO Channel mono/stereo/quad/surround
	// encoding 8bit/16bit
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put("Mode", getMode());
		contents.put("Ringer Mode", getRingerMode());
		contents.put("System Volume", String.valueOf(getSystemVolume()));
		contents.put("System Volume Max", String.valueOf(getSystemVolumeMax()));
		contents.put("Ring Volume", String.valueOf(getRingVolume()));
		contents.put("Ring Volume Max", String.valueOf(getRingVolumeMax()));
		contents.put("Call Volume", String.valueOf(getCallVolume()));
		contents.put("Call Volume Max", String.valueOf(getCallVolumeMax()));
		contents.put("Music Volume", String.valueOf(getMusicVolume()));
		contents.put("Music Volume Max", String.valueOf(getMusicVolumeMax()));
		contents.put("Alarm Volume", String.valueOf(getAlarmVolume()));
		contents.put("Alarm Volume Max", String.valueOf(getAlarmVolumeMax()));
		contents.put("DTMF Volume", String.valueOf(getDtmfVolume()));
		contents.put("DTMF Volume Max", String.valueOf(getDtmfVolumeMax()));		
		contents.put("Notification Volume", String.valueOf(getNotificationVolume()));
		contents.put("Notification Volume Max", String.valueOf(getNotificationVolumeMax()));		
		contents.put("Ringer Vibrate Setting", getRingerVibrateSetting());
		contents.put("Notification Vibrate Setting", getNotificationVibrateSetting());
		contents.put("Bluetooth A2DP On", String.valueOf(isBluetoothA2dpOn()));
		contents.put("Bluetooth SCO On", String.valueOf(isBluetoothScoOn()));
		contents.put("Bluetooth SCO Available Off Call", String.valueOf(isBluetoothScoAvailableOffCall()));
		contents.put("Microphone Mute", String.valueOf(isMicrophoneMute()));
		contents.put("Music Active", String.valueOf(isMusicActive()));
		contents.put("Speakerphone On", String.valueOf(isSpeakerphoneOn()));
		contents.put("Wired Headset Connected", String.valueOf(isWiredHeadsetConnected()));
		String[] formats = getSupportedFormats();
		for (int i = 0; i < formats.length; ++i) {
			contents.put("Supported Format " + i, formats[i]);
		}
		String[] sources = getSupportedSources();
		for (int i = 0; i < sources.length; ++i) {
			contents.put("Supported Source " + i, sources[i]);
		}
		
		return contents;
	}

}
