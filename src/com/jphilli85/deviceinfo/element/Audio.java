package com.jphilli85.deviceinfo.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder.AudioEncoder;
import android.media.MediaRecorder.AudioSource;
import android.media.MediaRecorder.OutputFormat;
import android.os.Build;

import com.jphilli85.deviceinfo.R;

public class Audio extends Element {	
	private static final int API = Build.VERSION.SDK_INT;
	//TODO live audio
	// AudioManager constants
	public final String MODE_CURRENT;
	public final String MODE_INVALID;
	public final String MODE_IN_CALL;
	public final String MODE_IN_COMMUNICATION;
	public final String MODE_NORMAL;
	public final String MODE_RINGTONE;
	public final String RINGER_MODE_NORMAL;
	public final String RINGER_MODE_SILENT;
	public final String RINGER_MODE_VIBRATE;
	public final String VIBRATE_SETTING_OFF;
	public final String VIBRATE_SETTING_ON;
	public final String VIBRATE_SETTING_ONLY_SILENT;
	// MediaRecorder.AudioEncoder constants
	public final String FORMAT_DEFAULT;
	public final String FORMAT_AAC;
	public final String FORMAT_AMR_NB;
	public final String FORMAT_AMR_WB;
	// MediaRecorder.OutputFormat constants
	public final String FORMAT_RAW_AMR;
	public final String FORMAT_THREE_GPP;
	public final String FORMAT_MPEG_4;
	// MediaRecorder.AudioSource constants
	public final String SOURCE_CAMCORDER;
	public final String SOURCE_DEFAULT;
	public final String SOURCE_MIC;
	public final String SOURCE_VOICE_CALL;
	public final String SOURCE_VOICE_COMMUNICATION;
	public final String SOURCE_VOICE_DOWNLINK;
	public final String SOURCE_VOICE_RECOGNITION;
	public final String SOURCE_VOICE_UPLINK;
	
	private AudioManager mAudioManager;
	
	public Audio(Context context) {
		super(context);
		MODE_CURRENT = context.getString(R.string.audio_mode_current);
		MODE_INVALID = context.getString(R.string.audio_mode_invalid);
		MODE_IN_CALL = context.getString(R.string.audio_mode_in_call);
		MODE_IN_COMMUNICATION = context.getString(R.string.audio_mode_in_communication);
		MODE_NORMAL = context.getString(R.string.audio_mode_normal);
		MODE_RINGTONE = context.getString(R.string.audio_mode_ringtone);
		RINGER_MODE_NORMAL = context.getString(R.string.ringer_mode_normal);
		RINGER_MODE_SILENT = context.getString(R.string.ringer_mode_silent);
		RINGER_MODE_VIBRATE = context.getString(R.string.ringer_mode_vibrate);
		VIBRATE_SETTING_OFF = context.getString(R.string.vibrate_setting_off);		
		VIBRATE_SETTING_ON = context.getString(R.string.vibrate_setting_on);
		VIBRATE_SETTING_ONLY_SILENT = context.getString(R.string.vibrate_setting_only_silent);
		FORMAT_DEFAULT = context.getString(R.string.audio_format_default);
		FORMAT_AAC = context.getString(R.string.audio_format_aac);
		FORMAT_AMR_NB = context.getString(R.string.audio_format_amr_nb);
		FORMAT_AMR_WB = context.getString(R.string.audio_format_amr_wb);
		FORMAT_RAW_AMR = context.getString(R.string.audio_format_raw_amr);
		FORMAT_THREE_GPP = context.getString(R.string.audio_format_three_gpp);
		FORMAT_MPEG_4 = context.getString(R.string.audio_format_mpeg_4);
		SOURCE_CAMCORDER = context.getString(R.string.audio_source_camcorder);
		SOURCE_DEFAULT = context.getString(R.string.audio_source_default);
		SOURCE_MIC = context.getString(R.string.audio_source_mic);
		SOURCE_VOICE_CALL = context.getString(R.string.audio_source_voice_call);
		SOURCE_VOICE_COMMUNICATION = context.getString(R.string.audio_source_voice_communication);
		SOURCE_VOICE_DOWNLINK = context.getString(R.string.audio_source_voice_downlink);
		SOURCE_VOICE_RECOGNITION = context.getString(R.string.audio_source_voice_recognition);
		SOURCE_VOICE_UPLINK = context.getString(R.string.audio_source_voice_uplink);
		
		mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public AudioManager getAudioManager() {
		return mAudioManager;
	}
	
	public String getMode(int mode) {
		switch (mode) {
		case AudioManager.MODE_CURRENT: return MODE_CURRENT;
		case AudioManager.MODE_IN_CALL: return MODE_IN_CALL;
		case AudioManager.MODE_INVALID: return MODE_INVALID;
		case AudioManager.MODE_NORMAL: return MODE_NORMAL;
		case AudioManager.MODE_RINGTONE: return MODE_RINGTONE;
		}
		if (API >= 11 && mode == AudioManager.MODE_IN_COMMUNICATION) return MODE_IN_COMMUNICATION;
		return null;
	}
	
	public String getRingerMode(int mode) {		
		switch (mode) {
		case AudioManager.RINGER_MODE_NORMAL: return RINGER_MODE_NORMAL;
		case AudioManager.RINGER_MODE_SILENT: return RINGER_MODE_SILENT;
		case AudioManager.RINGER_MODE_VIBRATE: return RINGER_MODE_VIBRATE;
		default: return null;
		}		
	}
	
	public String getVibrateSetting(int setting) {
		switch (setting) {
		case AudioManager.VIBRATE_SETTING_OFF: return VIBRATE_SETTING_OFF;
		case AudioManager.VIBRATE_SETTING_ON: return VIBRATE_SETTING_ON;
		case AudioManager.VIBRATE_SETTING_ONLY_SILENT: return VIBRATE_SETTING_ONLY_SILENT;
		default: return null;
		}
	}
	
	public String getAudioEncoder(int codec) {
		switch (codec) {
		case AudioEncoder.AMR_NB: return FORMAT_AMR_NB;
		case AudioEncoder.AMR_WB: return FORMAT_AMR_WB;
		case AudioEncoder.DEFAULT: return FORMAT_DEFAULT;
		}
		if (API >= 10 && codec == AudioEncoder.AAC) return FORMAT_AAC;
		return null;
	}
	
	public String getOutputFormat(int format) {
		switch (format) {
		case OutputFormat.RAW_AMR: return FORMAT_RAW_AMR;
		case OutputFormat.THREE_GPP: return FORMAT_THREE_GPP;
		case OutputFormat.MPEG_4: return FORMAT_MPEG_4;
		case OutputFormat.DEFAULT: return FORMAT_DEFAULT;
		}
		if (API > 10) {
			switch (format) {
			case OutputFormat.AMR_NB: return FORMAT_AMR_NB;
			case OutputFormat.AMR_WB: return FORMAT_AMR_WB;
			}
		}
		return null;
	}
	
	public String getAudioSource(int source) {
		switch (source) {
		case AudioSource.CAMCORDER: return SOURCE_CAMCORDER;
		case AudioSource.DEFAULT: return SOURCE_DEFAULT;
		case AudioSource.MIC: return SOURCE_MIC;
		case AudioSource.VOICE_CALL: return SOURCE_VOICE_CALL;
		case AudioSource.VOICE_DOWNLINK: return SOURCE_VOICE_DOWNLINK;
		case AudioSource.VOICE_RECOGNITION: return SOURCE_VOICE_RECOGNITION;
		case AudioSource.VOICE_UPLINK: return SOURCE_VOICE_UPLINK;
		}
		if (API >= 11 && source == AudioSource.VOICE_COMMUNICATION) return SOURCE_VOICE_COMMUNICATION;
		return null;
	}
	
	public String[] getSupportedFormats() {
		List<String> formats = new ArrayList<String>();
		formats.add(FORMAT_AMR_NB);
		formats.add(FORMAT_RAW_AMR);
		formats.add(FORMAT_DEFAULT);
		formats.add(FORMAT_THREE_GPP);
		if (API >= 10) {
			formats.add(FORMAT_AAC);
			formats.add(FORMAT_AMR_WB);
		}
		return formats.toArray(new String[formats.size()]);
	}
	
	public String[] getSupportedSources() {
		List<String> sources = new ArrayList<String>();
		sources.add(SOURCE_CAMCORDER);
		sources.add(SOURCE_DEFAULT);
		sources.add(SOURCE_MIC);
		sources.add(SOURCE_VOICE_CALL);		
		sources.add(SOURCE_VOICE_DOWNLINK);		
		sources.add(SOURCE_VOICE_UPLINK);
		sources.add(SOURCE_VOICE_RECOGNITION);
		if (API >= 11) {
			sources.add(SOURCE_VOICE_COMMUNICATION);
		}
		return sources.toArray(new String[sources.size()]);
	}
	
	public String getMode() {
		return getMode(mAudioManager.getMode());
	}
	
	public String getRingerMode() {
		return getRingerMode(mAudioManager.getRingerMode());
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
	
	
	
	public boolean isBluetoothA2dpOn() {
		return mAudioManager.isBluetoothA2dpOn();
	}
	
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
