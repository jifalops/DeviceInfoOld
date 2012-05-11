package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.text.TextUtils;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.element.Audio;
import com.jphilli85.deviceinfo.element.Element;


public class AudioView extends ElementView {
	private final Audio mAudio;
	
	public AudioView() {
		this(DeviceInfo.getAppContext());
	}
	
	public AudioView(Context context) {
		super(context);
		mAudio = new Audio(context);
		
		TableSection table = new TableSection(context);
		
		table.add("Mode", mAudio.getMode());
		table.add("Ringer Mode", mAudio.getRingerMode());
		table.add("System Volume", mAudio.getSystemVolume() + "/" + mAudio.getSystemVolumeMax());
		table.add("Ring Volume", mAudio.getRingVolume() + "/" + mAudio.getRingVolumeMax());
		table.add("Call Volume", mAudio.getCallVolume() + "/" + mAudio.getCallVolumeMax());
		table.add("Music Volume", mAudio.getMusicVolume() + "/" + mAudio.getMusicVolumeMax());
		table.add("Alarm Volume", mAudio.getAlarmVolume() + "/" + mAudio.getAlarmVolumeMax());
		table.add("DTMF Volume", mAudio.getDtmfVolume() + "/" + mAudio.getDtmfVolumeMax());
		table.add("Notification Volume", mAudio.getNotificationVolume() + "/" + mAudio.getNotificationVolumeMax());
		table.add("Ringer Vibrate", mAudio.getRingerVibrateSetting());
		table.add("Notification Vibrate", mAudio.getNotificationVibrateSetting());
		table.add("Bluetooth A2DP On", String.valueOf(mAudio.isBluetoothA2dpOn()));
		table.add("Bluetooth SCO On", String.valueOf(mAudio.isBluetoothScoOn()));
		table.add("Bluetooth SCO Available Off Call", String.valueOf(mAudio.isBluetoothScoAvailableOffCall()));
		table.add("Microphone Muted", String.valueOf(mAudio.isMicrophoneMute()));
		table.add("Music Active", String.valueOf(mAudio.isMusicActive()));
		table.add("Speakerphone On", String.valueOf(mAudio.isSpeakerphoneOn()));
		table.add("Wired Headset Connected", String.valueOf(mAudio.isWiredHeadsetConnected()));
		table.add("Supported Formats", TextUtils.join(", ", mAudio.getSupportedFormats()));
		table.add("Supported Sources", TextUtils.join(", ", mAudio.getSupportedSources()));		
		add(table);
	}
	
	@Override
	public Element getElement() {
		return mAudio;
	}
}
