package com.jphilli85.deviceinfo.element;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Set;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothHealth;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.app.DeviceInfo;

public class Bluetooth extends ListeningElement {
	private static final int API = Build.VERSION.SDK_INT;
	
	public interface Callback extends ListeningElement.Callback {
		void onServiceConnected(int profile, BluetoothProfile proxy);
		void onServiceDisconnected(int profile);
	}
	
	// BluetoothProfile constants
	public final String PROFILE_A2DP;
	public final String PROFILE_HEADSET;
	public final String PROFILE_HEALTH;
	public final String STATE_CONNECTED;
	public final String STATE_CONNECTING;
	public final String STATE_DISCONNECTED;
	public final String STATE_DISCONNECTING;
	// BluetoothA2dp constants
	public final String STATE_PLAYING;
	public final String STATE_NOT_PLAYING;
	// BluetoothAdapter constants
	public final String ERROR;
	public final String MODE_CONNECTABLE;
	public final String MODE_CONNECTABLE_DISCOVERABLE;
	public final String MODE_NONE;
	public final String STATE_OFF;
	public final String STATE_ON;
	public final String STATE_TURNING_OFF;
	public final String STATE_TURNING_ON;
	// BluetoothDevice constants
	public final String BOND_BONDED;
	public final String BOND_BONDING;
	public final String BOND_NONE;
	// BluetoothHeadset constants
	public final String AT_CMD_TYPE_ACTION;
	public final String AT_CMD_TYPE_BASIC;
	public final String AT_CMD_TYPE_READ;
	public final String AT_CMD_TYPE_SET;
	public final String AT_CMD_TYPE_TEST;
	// BluetoothHealth constants
	public final String SUCCESS;
	public final String FAILURE;
	public final String CHANNEL_RELIABLE;
	public final String CHANNEL_STREAMING;
	public final String ROLE_SINK;
	public final String ROLE_SOURCE;
	// BluetoothClass.Device constants
	public final String DEVICE_CAMCORDER;
	public final String DEVICE_CAR_AUDIO;
	public final String DEVICE_HANDSFREE;
	public final String DEVICE_HEADPHONES;
	public final String DEVICE_HIFI_AUDIO;
	public final String DEVICE_LOUDSPEAKER;
	public final String DEVICE_MICROPHONE;
	public final String DEVICE_PORTABLE_AUDIO;
	public final String DEVICE_SET_TOP_BOX;
	public final String UNCATEGORIZED;
	public final String DEVICE_VCR;
	public final String DEVICE_VIDEO_CAMERA;
	public final String DEVICE_VIDEO_CONFERENCING;
	public final String DEVICE_VIDEO_DISPLAY_AND_LOUDSPEAKER;
	public final String DEVICE_VIDEO_GAMING_TOY;
	public final String DEVICE_VIDEO_MONITOR;
	public final String DEVICE_WEARABLE_HEADSET;
	public final String DEVICE_DESKTOP;
	public final String DEVICE_HANDHELD_PC_PDA;
	public final String DEVICE_LAPTOP;
	public final String DEVICE_PALM_SIZE_PC_PDA;
	public final String DEVICE_SERVER;
	public final String DEVICE_WEARABLE;
	public final String DEVICE_BLOOD_PRESSURE;
	public final String DEVICE_DATA_DISPLAY;
	public final String DEVICE_GLUCOSE;
	public final String DEVICE_PULSE_OXIMETER;
	public final String DEVICE_PULSE_RATE;
	public final String DEVICE_THERMOMETER;
	public final String DEVICE_WEIGHING;
	public final String DEVICE_CELLULAR_PHONE;
	public final String DEVICE_CORDLESS_PHONE;
	public final String DEVICE_ISDN;
	public final String DEVICE_MODEM_OR_GATEWAY;
	public final String DEVICE_SMART_PHONE;
	public final String DEVICE_CONTROLLER;
	public final String DEVICE_ACTION_FIGURE;
	public final String DEVICE_GAME;
	public final String DEVICE_ROBOT;
	public final String DEVICE_VEHICLE;
	public final String DEVICE_GLASSES;
	public final String DEVICE_HELMET;
	public final String DEVICE_JACKET;
	public final String DEVICE_PAGER;
	public final String DEVICE_WATCH;
	// BluetoothClass.Device.Major constants
	public final String MAJOR_DEVICE_AUDIO_VIDEO;
	public final String MAJOR_DEVICE_COMPUTER;
	public final String MAJOR_DEVICE_HEALTH;
	public final String MAJOR_DEVICE_IMAGING;
	public final String MAJOR_DEVICE_MISC;
	public final String MAJOR_DEVICE_NETWORKING;
	public final String MAJOR_DEVICE_PERIPHERAL;
	public final String MAJOR_DEVICE_PHONE;
	public final String MAJOR_DEVICE_TOY;
	public final String MAJOR_DEVICE_WEARABLE;
	// BluetoothClass.Service constants
	public final String SERVICE_AUDIO;
	public final String SERVICE_CAPTURE;
	public final String SERVICE_INFORMATION;
	public final String SERVICE_LIMITED_DISCOVERABILITY;
	public final String SERVICE_NETWORKING;
	public final String SERVICE_OBJECT_TRANSFER;
	public final String SERVICE_POSITIONING;
	public final String SERVICE_RENDER;
	public final String SERVICE_TELEPHONY;
	
	
	private final BluetoothAdapter mBluetoothAdapter;
	private BluetoothA2dp mA2dpProfile;
	private BluetoothHeadset mHeadsetProfile;
	private BluetoothHealth mHealthProfile;
	
	public Bluetooth(Context context) throws UnavailableFeatureException {
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) throw new UnavailableFeatureException();			
		
		// BluetoothProfile constants
		PROFILE_A2DP = context.getString(R.string.bluetooth_profile_a2dp);
		PROFILE_HEADSET = context.getString(R.string.bluetooth_profile_headset);
		PROFILE_HEALTH = context.getString(R.string.bluetooth_profile_health);
		STATE_CONNECTED = context.getString(R.string.bluetooth_state_connected);
		STATE_CONNECTING = context.getString(R.string.bluetooth_state_connecting);
		STATE_DISCONNECTED = context.getString(R.string.bluetooth_state_disconnected);
		STATE_DISCONNECTING = context.getString(R.string.bluetooth_state_disconnecting);
		// BluetoothA2dp constants
		STATE_PLAYING = context.getString(R.string.bluetooth_state_playing);
		STATE_NOT_PLAYING = context.getString(R.string.bluetooth_state_not_playing);
		// BluetoothAdapter constants
		ERROR = context.getString(R.string.bluetooth_error);
		MODE_CONNECTABLE = context.getString(R.string.bluetooth_mode_connectable);
		MODE_CONNECTABLE_DISCOVERABLE = context.getString(R.string.bluetooth_mode_connectable_discoverable);
		MODE_NONE = context.getString(R.string.bluetooth_mode_none);
		STATE_OFF = context.getString(R.string.bluetooth_state_off);
		STATE_ON = context.getString(R.string.bluetooth_state_on);
		STATE_TURNING_OFF = context.getString(R.string.bluetooth_state_turning_off);
		STATE_TURNING_ON = context.getString(R.string.bluetooth_state_turning_on);
		// BluetoothDevice constants
		BOND_BONDED = context.getString(R.string.bluetooth_bond_bonded);
		BOND_BONDING = context.getString(R.string.bluetooth_bond_bonding);
		BOND_NONE = context.getString(R.string.bluetooth_bond_none);
		// BluetoothHeadset constants
		AT_CMD_TYPE_ACTION = context.getString(R.string.bluetooth_at_cmd_type_action);
		AT_CMD_TYPE_BASIC = context.getString(R.string.bluetooth_at_cmd_type_basic);
		AT_CMD_TYPE_READ = context.getString(R.string.bluetooth_at_cmd_type_read);
		AT_CMD_TYPE_SET = context.getString(R.string.bluetooth_at_cmd_type_set);
		AT_CMD_TYPE_TEST = context.getString(R.string.bluetooth_at_cmd_type_test);
		// BluetoothHealth constants
		SUCCESS = context.getString(R.string.bluetooth_success);
		FAILURE = context.getString(R.string.bluetooth_failure);
		CHANNEL_RELIABLE = context.getString(R.string.bluetooth_channel_reliable);
		CHANNEL_STREAMING = context.getString(R.string.bluetooth_channel_streaming);
		ROLE_SINK = context.getString(R.string.bluetooth_role_sink);
		ROLE_SOURCE = context.getString(R.string.bluetooth_role_source);
		// BluetoothClass.Device constants
		DEVICE_CAMCORDER = context.getString(R.string.bluetooth_device_camcorder);
		DEVICE_CAR_AUDIO = context.getString(R.string.bluetooth_device_car_audio);
		DEVICE_HANDSFREE = context.getString(R.string.bluetooth_device_hands_free);
		DEVICE_HEADPHONES = context.getString(R.string.bluetooth_device_headphones);
		DEVICE_HIFI_AUDIO = context.getString(R.string.bluetooth_device_hifi_audio);
		DEVICE_LOUDSPEAKER = context.getString(R.string.bluetooth_device_loudspeaker);
		DEVICE_MICROPHONE = context.getString(R.string.bluetooth_device_microphone);
		DEVICE_PORTABLE_AUDIO = context.getString(R.string.bluetooth_device_portable_audio);
		DEVICE_SET_TOP_BOX = context.getString(R.string.bluetooth_device_set_top_box);
		UNCATEGORIZED = context.getString(R.string.bluetooth_device_uncategorized);
		DEVICE_VCR = context.getString(R.string.bluetooth_device_vcr);
		DEVICE_VIDEO_CAMERA = context.getString(R.string.bluetooth_device_video_camera);
		DEVICE_VIDEO_CONFERENCING = context.getString(R.string.bluetooth_device_video_conferencing);
		DEVICE_VIDEO_DISPLAY_AND_LOUDSPEAKER = context.getString(R.string.bluetooth_device_video_display_and_loudspeaker);
		DEVICE_VIDEO_GAMING_TOY = context.getString(R.string.bluetooth_device_video_gaming_toy);
		DEVICE_VIDEO_MONITOR = context.getString(R.string.bluetooth_device_video_monitor);
		DEVICE_WEARABLE_HEADSET = context.getString(R.string.bluetooth_device_wearable_headset);
		DEVICE_DESKTOP = context.getString(R.string.bluetooth_device_desktop);
		DEVICE_HANDHELD_PC_PDA = context.getString(R.string.bluetooth_device_handheld_pc_pda);
		DEVICE_LAPTOP = context.getString(R.string.bluetooth_device_laptop);
		DEVICE_PALM_SIZE_PC_PDA = context.getString(R.string.bluetooth_device_palm_size_pc_pda);
		DEVICE_SERVER = context.getString(R.string.bluetooth_device_server);
		DEVICE_WEARABLE = context.getString(R.string.bluetooth_device_wearable);
		DEVICE_BLOOD_PRESSURE = context.getString(R.string.bluetooth_device_blood_pressure);
		DEVICE_DATA_DISPLAY = context.getString(R.string.bluetooth_device_data_display);
		DEVICE_GLUCOSE = context.getString(R.string.bluetooth_device_glucose);
		DEVICE_PULSE_OXIMETER = context.getString(R.string.bluetooth_device_pulse_oximeter);
		DEVICE_PULSE_RATE = context.getString(R.string.bluetooth_device_pulse_rate);
		DEVICE_THERMOMETER = context.getString(R.string.bluetooth_device_thermometer);
		DEVICE_WEIGHING = context.getString(R.string.bluetooth_device_weighing);
		DEVICE_CELLULAR_PHONE = context.getString(R.string.bluetooth_device_cellular);
		DEVICE_CORDLESS_PHONE = context.getString(R.string.bluetooth_device_cordless);
		DEVICE_ISDN = context.getString(R.string.bluetooth_device_isdn);
		DEVICE_MODEM_OR_GATEWAY = context.getString(R.string.bluetooth_device_modem_or_gateway);
		DEVICE_SMART_PHONE = context.getString(R.string.bluetooth_device_smart);
		DEVICE_CONTROLLER = context.getString(R.string.bluetooth_device_controller);
		DEVICE_ACTION_FIGURE = context.getString(R.string.bluetooth_device_action_figure);
		DEVICE_GAME = context.getString(R.string.bluetooth_device_game);
		DEVICE_ROBOT = context.getString(R.string.bluetooth_device_robot);
		DEVICE_VEHICLE = context.getString(R.string.bluetooth_device_vehicle);
		DEVICE_GLASSES = context.getString(R.string.bluetooth_device_glasses);
		DEVICE_HELMET = context.getString(R.string.bluetooth_device_helmet);
		DEVICE_JACKET = context.getString(R.string.bluetooth_device_jacket);
		DEVICE_PAGER = context.getString(R.string.bluetooth_device_pager);
		DEVICE_WATCH = context.getString(R.string.bluetooth_device_watch);
		// BluetoothClass.Device.Major constants
		MAJOR_DEVICE_AUDIO_VIDEO = context.getString(R.string.bluetooth_major_device_audio_video);
		MAJOR_DEVICE_COMPUTER = context.getString(R.string.bluetooth_major_device_computer);
		MAJOR_DEVICE_HEALTH = context.getString(R.string.bluetooth_major_device_health);
		MAJOR_DEVICE_IMAGING = context.getString(R.string.bluetooth_major_device_imaging);
		MAJOR_DEVICE_MISC = context.getString(R.string.bluetooth_major_device_misc);
		MAJOR_DEVICE_NETWORKING = context.getString(R.string.bluetooth_major_device_networking);
		MAJOR_DEVICE_PERIPHERAL = context.getString(R.string.bluetooth_major_device_peripheral);
		MAJOR_DEVICE_PHONE = context.getString(R.string.bluetooth_major_device_phone);
		MAJOR_DEVICE_TOY = context.getString(R.string.bluetooth_major_device_toy);
		MAJOR_DEVICE_WEARABLE = context.getString(R.string.bluetooth_major_device_wearable);
		// BluetoothClass.Service constants
		SERVICE_AUDIO = context.getString(R.string.bluetooth_service_audio);
		SERVICE_CAPTURE = context.getString(R.string.bluetooth_service_capture);
		SERVICE_INFORMATION = context.getString(R.string.bluetooth_service_information);
		SERVICE_LIMITED_DISCOVERABILITY = context.getString(R.string.bluetooth_service_limited_discoverability);
		SERVICE_NETWORKING = context.getString(R.string.bluetooth_service_networking);
		SERVICE_OBJECT_TRANSFER = context.getString(R.string.bluetooth_service_object_transfer);
		SERVICE_POSITIONING = context.getString(R.string.bluetooth_service_positioning);
		SERVICE_RENDER = context.getString(R.string.bluetooth_service_render);
		SERVICE_TELEPHONY = context.getString(R.string.bluetooth_service_telephony);
				
	}
		
	public String getProfileType(int type) {
		if (API < 11) return null;
		switch (type) {
		case BluetoothProfile.A2DP: return PROFILE_A2DP;
		case BluetoothProfile.HEADSET: return PROFILE_HEADSET;		
		}
		if (API >= 14 && type == BluetoothProfile.HEALTH) return PROFILE_HEALTH;
		return null;
	}
	
	public String getProfileState(int state) {
		if (API < 11) return null;
		switch (state) {
		case BluetoothProfile.STATE_CONNECTED: return STATE_CONNECTED;
		case BluetoothProfile.STATE_CONNECTING: return STATE_CONNECTING;	
		case BluetoothProfile.STATE_DISCONNECTED: return STATE_DISCONNECTED;
		case BluetoothProfile.STATE_DISCONNECTING: return STATE_DISCONNECTING;
		default: return null;
		}
	}
	
	public String getA2dpState(int state) {
		if (API < 11) return null;
		switch (state) {
		case BluetoothA2dp.STATE_PLAYING: return STATE_PLAYING;
		case BluetoothA2dp.STATE_NOT_PLAYING: return STATE_NOT_PLAYING;
		default: return null;
		}
	}
	
	public String getScanMode(int mode) {
		switch (mode) {
		case BluetoothAdapter.SCAN_MODE_CONNECTABLE: return MODE_CONNECTABLE;
		case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE: return MODE_CONNECTABLE_DISCOVERABLE;
		case BluetoothAdapter.SCAN_MODE_NONE: return MODE_NONE;
		case BluetoothAdapter.ERROR: return ERROR;
		default: return null;
		}
	}
	
	public String getAdapterState(int state) {
		switch (state) {
		case BluetoothAdapter.STATE_ON: return STATE_ON;
		case BluetoothAdapter.STATE_OFF: return STATE_OFF;
		case BluetoothAdapter.STATE_TURNING_ON: return STATE_TURNING_ON;
		case BluetoothAdapter.STATE_TURNING_OFF: return STATE_TURNING_OFF;
		case BluetoothAdapter.ERROR: return ERROR;
		}
		if (API < 11) return null;
		switch (state) {
		case BluetoothAdapter.STATE_CONNECTED: return STATE_CONNECTED;
		case BluetoothAdapter.STATE_CONNECTING: return STATE_CONNECTING;
		case BluetoothAdapter.STATE_DISCONNECTED: return STATE_DISCONNECTED;
		case BluetoothAdapter.STATE_DISCONNECTING: return STATE_DISCONNECTING;
		default: return null;
		}
	}
	
	/** Gets the bond state (paired/pairing...) */
	public String getBondState(int state) {
		switch (state) {
		case BluetoothDevice.BOND_BONDED: return BOND_BONDED;
		case BluetoothDevice.BOND_BONDING: return BOND_BONDING;
		case BluetoothDevice.BOND_NONE: return BOND_NONE;
		case BluetoothDevice.ERROR: return ERROR;
		default: return null;
		}
	}
	
	public String getAtCommandType(int type) {
		if (API < 11) return null;
		switch (type) {
		case BluetoothHeadset.AT_CMD_TYPE_ACTION: return AT_CMD_TYPE_ACTION;
		case BluetoothHeadset.AT_CMD_TYPE_BASIC: return AT_CMD_TYPE_BASIC;
		case BluetoothHeadset.AT_CMD_TYPE_READ: return AT_CMD_TYPE_READ;
		case BluetoothHeadset.AT_CMD_TYPE_SET: return AT_CMD_TYPE_SET;
		case BluetoothHeadset.AT_CMD_TYPE_TEST: return AT_CMD_TYPE_TEST;
		default: return null;
		}
	}
	
	public String getScoState(int state) {
		if (API < 11) return null;
		switch (state) {
		case BluetoothHeadset.STATE_AUDIO_CONNECTED: return STATE_CONNECTED;
		case BluetoothHeadset.STATE_AUDIO_CONNECTING: return STATE_CONNECTING;
		case BluetoothHeadset.STATE_AUDIO_DISCONNECTED: return STATE_DISCONNECTED;
		default: return null;
		}
	}
	
	public String getHealthConfigState(int state) {
		if (API < 14) return null;
		switch (state) {
		case BluetoothHealth.APP_CONFIG_REGISTRATION_FAILURE: return FAILURE;
		case BluetoothHealth.APP_CONFIG_REGISTRATION_SUCCESS: return SUCCESS;
		case BluetoothHealth.APP_CONFIG_UNREGISTRATION_FAILURE: return FAILURE;
		case BluetoothHealth.APP_CONFIG_UNREGISTRATION_SUCCESS: return SUCCESS;
		default: return null;
		}
	}
	
	public String getHealthChannelType(int type) {
		if (API < 14) return null;
		switch (type) {
		case BluetoothHealth.CHANNEL_TYPE_RELIABLE: return CHANNEL_RELIABLE;
		case BluetoothHealth.CHANNEL_TYPE_STREAMING: return CHANNEL_STREAMING;
		default: return null;
		}
	}
	
	public String getHealthChannelState(int state) {
		if (API < 14) return null;
		switch (state) {
		case BluetoothHealth.STATE_CHANNEL_CONNECTED: return STATE_CONNECTED;
		case BluetoothHealth.STATE_CHANNEL_CONNECTING: return STATE_CONNECTING;
		case BluetoothHealth.STATE_CHANNEL_DISCONNECTED: return STATE_DISCONNECTED;
		case BluetoothHealth.STATE_CHANNEL_DISCONNECTING: return STATE_DISCONNECTING;
		default: return null;
		}
	}
	
	public String getHealthRole(int role) {
		if (API < 14) return null;
		switch (role) {
		case BluetoothHealth.SINK_ROLE: return ROLE_SINK;
		case BluetoothHealth.SOURCE_ROLE: return ROLE_SOURCE;
		default: return null;
		}
	}
	
	public String getDeviceType(int type) {		
		switch (type) {
		case BluetoothClass.Device.AUDIO_VIDEO_CAMCORDER: return DEVICE_CAMCORDER;
		case BluetoothClass.Device.AUDIO_VIDEO_CAR_AUDIO : return DEVICE_CAR_AUDIO;
		case BluetoothClass.Device.AUDIO_VIDEO_HANDSFREE : return DEVICE_HANDSFREE;
		case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES : return DEVICE_HEADPHONES;
		case BluetoothClass.Device.AUDIO_VIDEO_HIFI_AUDIO : return DEVICE_HIFI_AUDIO;
		case BluetoothClass.Device.AUDIO_VIDEO_LOUDSPEAKER : return DEVICE_LOUDSPEAKER;
		case BluetoothClass.Device.AUDIO_VIDEO_MICROPHONE : return DEVICE_MICROPHONE;
		case BluetoothClass.Device.AUDIO_VIDEO_PORTABLE_AUDIO : return DEVICE_PORTABLE_AUDIO;
		case BluetoothClass.Device.AUDIO_VIDEO_SET_TOP_BOX : return DEVICE_SET_TOP_BOX;		
		case BluetoothClass.Device.AUDIO_VIDEO_VCR : return DEVICE_VCR;
		case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CAMERA : return DEVICE_VIDEO_CAMERA;
		case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_CONFERENCING : return DEVICE_VIDEO_CONFERENCING;
		case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_DISPLAY_AND_LOUDSPEAKER : return DEVICE_VIDEO_DISPLAY_AND_LOUDSPEAKER;
		case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_GAMING_TOY : return DEVICE_VIDEO_GAMING_TOY;
		case BluetoothClass.Device.AUDIO_VIDEO_VIDEO_MONITOR : return DEVICE_VIDEO_MONITOR;
		case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET : return DEVICE_WEARABLE_HEADSET;
		case BluetoothClass.Device.COMPUTER_DESKTOP : return DEVICE_DESKTOP;
		case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA : return DEVICE_HANDHELD_PC_PDA;
		case BluetoothClass.Device.COMPUTER_LAPTOP : return DEVICE_LAPTOP;
		case BluetoothClass.Device.COMPUTER_PALM_SIZE_PC_PDA : return DEVICE_PALM_SIZE_PC_PDA;
		case BluetoothClass.Device.COMPUTER_SERVER : return DEVICE_SERVER;
		case BluetoothClass.Device.COMPUTER_WEARABLE : return DEVICE_WEARABLE;
		case BluetoothClass.Device.HEALTH_BLOOD_PRESSURE : return DEVICE_BLOOD_PRESSURE;
		case BluetoothClass.Device.HEALTH_DATA_DISPLAY : return DEVICE_DATA_DISPLAY;
		case BluetoothClass.Device.HEALTH_GLUCOSE : return DEVICE_GLUCOSE;
		case BluetoothClass.Device.HEALTH_PULSE_OXIMETER : return DEVICE_PULSE_OXIMETER;
		case BluetoothClass.Device.HEALTH_PULSE_RATE : return DEVICE_PULSE_RATE;
		case BluetoothClass.Device.HEALTH_THERMOMETER : return DEVICE_THERMOMETER;
		case BluetoothClass.Device.HEALTH_WEIGHING : return DEVICE_WEIGHING;
		case BluetoothClass.Device.PHONE_CELLULAR : return DEVICE_CELLULAR_PHONE;
		case BluetoothClass.Device.PHONE_CORDLESS : return DEVICE_CORDLESS_PHONE;
		case BluetoothClass.Device.PHONE_ISDN : return DEVICE_ISDN;
		case BluetoothClass.Device.PHONE_MODEM_OR_GATEWAY : return DEVICE_MODEM_OR_GATEWAY;
		case BluetoothClass.Device.PHONE_SMART : return DEVICE_SMART_PHONE;
		case BluetoothClass.Device.TOY_CONTROLLER : return DEVICE_CONTROLLER;
		case BluetoothClass.Device.TOY_DOLL_ACTION_FIGURE : return DEVICE_ACTION_FIGURE;
		case BluetoothClass.Device.TOY_GAME : return DEVICE_GAME;
		case BluetoothClass.Device.TOY_ROBOT : return DEVICE_ROBOT;
		case BluetoothClass.Device.TOY_VEHICLE : return DEVICE_VEHICLE;
		case BluetoothClass.Device.WEARABLE_GLASSES : return DEVICE_GLASSES;
		case BluetoothClass.Device.WEARABLE_HELMET : return DEVICE_HELMET;
		case BluetoothClass.Device.WEARABLE_JACKET : return DEVICE_JACKET;
		case BluetoothClass.Device.WEARABLE_PAGER : return DEVICE_PAGER;
		case BluetoothClass.Device.WEARABLE_WRIST_WATCH : return DEVICE_WATCH;
		
		case BluetoothClass.Device.AUDIO_VIDEO_UNCATEGORIZED : return UNCATEGORIZED;
		case BluetoothClass.Device.COMPUTER_UNCATEGORIZED : return UNCATEGORIZED;
		case BluetoothClass.Device.HEALTH_UNCATEGORIZED : return UNCATEGORIZED;
		case BluetoothClass.Device.PHONE_UNCATEGORIZED : return UNCATEGORIZED;
		case BluetoothClass.Device.TOY_UNCATEGORIZED : return UNCATEGORIZED;
		case BluetoothClass.Device.WEARABLE_UNCATEGORIZED : return UNCATEGORIZED;
		default: return null;
		}
	}
	
	public String getDeviceMajorType(int type) {
		switch (type) {
		case BluetoothClass.Device.Major.AUDIO_VIDEO: return MAJOR_DEVICE_AUDIO_VIDEO;
		case BluetoothClass.Device.Major.COMPUTER: return MAJOR_DEVICE_COMPUTER;
		case BluetoothClass.Device.Major.HEALTH: return MAJOR_DEVICE_HEALTH;
		case BluetoothClass.Device.Major.IMAGING: return MAJOR_DEVICE_IMAGING;
		case BluetoothClass.Device.Major.MISC: return MAJOR_DEVICE_MISC;
		case BluetoothClass.Device.Major.NETWORKING: return MAJOR_DEVICE_NETWORKING;
		case BluetoothClass.Device.Major.PERIPHERAL: return MAJOR_DEVICE_PERIPHERAL;
		case BluetoothClass.Device.Major.PHONE: return MAJOR_DEVICE_PHONE;
		case BluetoothClass.Device.Major.TOY: return MAJOR_DEVICE_TOY;
		case BluetoothClass.Device.Major.WEARABLE: return MAJOR_DEVICE_WEARABLE;
		case BluetoothClass.Device.Major.UNCATEGORIZED: return UNCATEGORIZED;
		default: return null;
		}
	}
	
	public String getServiceType(int type) {
		switch (type) {
		case BluetoothClass.Service.AUDIO: return SERVICE_AUDIO;
		case BluetoothClass.Service.CAPTURE: return SERVICE_CAPTURE;
		case BluetoothClass.Service.INFORMATION: return SERVICE_INFORMATION;
		case BluetoothClass.Service.LIMITED_DISCOVERABILITY: return SERVICE_LIMITED_DISCOVERABILITY;
		case BluetoothClass.Service.NETWORKING: return SERVICE_NETWORKING;
		case BluetoothClass.Service.OBJECT_TRANSFER: return SERVICE_OBJECT_TRANSFER;
		case BluetoothClass.Service.POSITIONING: return SERVICE_POSITIONING;
		case BluetoothClass.Service.RENDER: return SERVICE_RENDER;
		case BluetoothClass.Service.TELEPHONY: return SERVICE_TELEPHONY;
		default: return null;
		}
	}
	
	public BluetoothAdapter getBluetoothAdapter() {
		return mBluetoothAdapter;
	}
	
	public BluetoothA2dp getA2dpProfile() {
		if (API < 11) return null;
		return mA2dpProfile;
	}
	
	public BluetoothHeadset getHeadsetProfile() {
		if (API < 11) return null;
		return mHeadsetProfile;
	}
	
	public BluetoothHealth getHealthProfile() {
		if (API < 14) return null;
		return mHealthProfile;
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();

		// BluetoothAdapter
		String address = mBluetoothAdapter.getAddress();
		boolean isValid = BluetoothAdapter.checkBluetoothAddress(address);
		contents.put("MAC Address", address);
		contents.put("MAC Is Valid", String.valueOf(isValid));
		contents.put("Name", mBluetoothAdapter.getName());
		contents.put("Scan Mode", getScanMode(mBluetoothAdapter.getScanMode()));
		contents.put("Adapter State", getAdapterState(mBluetoothAdapter.getState()));
		contents.put("Is Discovering", String.valueOf(mBluetoothAdapter.isDiscovering()));
		contents.put("Is Enabled", String.valueOf(mBluetoothAdapter.isEnabled()));
		
		if (API >= 14) {
			contents.put("A2DP Profile Connection State", getProfileState(
					mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP)));
			contents.put("Headset Profile Connection State", getProfileState(
					mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)));
			contents.put("Health Profile Connection State", getProfileState(
					mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH)));
		}
		if (API >= 11 && mA2dpProfile == null) contents.put("Local A2DP Profile", null);
		if (API >= 11 && mHeadsetProfile == null) contents.put("Local Headset Profile", null);
		if (API >= 14 && mHealthProfile == null) contents.put("Local Health Profile", null);
		
		// BluetoothDevice
		if (isValid) {
			BluetoothDevice dev = mBluetoothAdapter.getRemoteDevice(address);
			if (dev == null) contents.put("Adapter Device", null);
			else {
				LinkedHashMap<String, String> subcontents = getBluetoothDeviceContents(dev);
				for (Entry<String, String> e : subcontents.entrySet()) {
					contents.put("Adapter Device " + e.getKey(), e.getValue());
				}
			}
		}
		
		Set<BluetoothDevice> devs = mBluetoothAdapter.getBondedDevices();
		if (devs == null || devs.isEmpty()) contents.put("Bonded Devices", null);
		else {		
			LinkedHashMap<String, String> subcontents;
			int i = 0;
			for (BluetoothDevice d : devs) {
				subcontents = getBluetoothDeviceContents(d);
				for (Entry<String, String> e : subcontents.entrySet()) {
					contents.put("Device " + i + " " + e.getKey(), e.getValue());
				}
				++i;
			}
		}
		
		return contents;
	}
	
	private LinkedHashMap<String, String> getBluetoothDeviceContents(BluetoothDevice device) {
		if (device == null) return null;
		LinkedHashMap<String, String> contents = super.getContents();
		
		String address = device.getAddress();
		contents.put("Address", address);
		contents.put("Address Valid", String.valueOf(BluetoothAdapter.checkBluetoothAddress(address)));
		contents.put("Bond State", getBondState(device.getBondState()));
		contents.put("Name", device.getName());							
		// BluetoothClass
		BluetoothClass btclass = device.getBluetoothClass();
		if (btclass == null) contents.put("BluetoothClass", null);
		else {
			contents.put("Major Class", getDeviceMajorType(btclass.getMajorDeviceClass()));
			contents.put("Minor Class", getDeviceType(btclass.getDeviceClass()));
			contents.put("Has Service Audio", String.valueOf(
					btclass.hasService(BluetoothClass.Service.AUDIO)));
			contents.put("Has Service Capture", String.valueOf(
					btclass.hasService(BluetoothClass.Service.CAPTURE)));
			contents.put("Has Service Information", String.valueOf(
					btclass.hasService(BluetoothClass.Service.INFORMATION)));
			contents.put("Has Service Limited Discoverability", String.valueOf(
					btclass.hasService(BluetoothClass.Service.LIMITED_DISCOVERABILITY)));
			contents.put("Has Service Networking", String.valueOf(
					btclass.hasService(BluetoothClass.Service.NETWORKING)));
			contents.put("Has Service Object Transfer", String.valueOf(
					btclass.hasService(BluetoothClass.Service.OBJECT_TRANSFER)));
			contents.put("Has Service Positioning", String.valueOf(
					btclass.hasService(BluetoothClass.Service.POSITIONING)));
			contents.put("Has Service Render", String.valueOf(
					btclass.hasService(BluetoothClass.Service.RENDER)));
			contents.put("Has Service Telephony", String.valueOf(
					btclass.hasService(BluetoothClass.Service.TELEPHONY)));
		}
		if (API >= 11 && mA2dpProfile != null) {
			contents.put("A2DP Profile Connection State", getProfileState(mA2dpProfile.getConnectionState(device)));
			// I reported bug, issue 29394
//			contents.put("A2DP Profile Is Playing", String.valueOf(mA2dpProfile.isA2dpPlaying(device)));
		}
		if (API >= 11 && mHeadsetProfile != null) {
			contents.put("Headset Profile Connection State", getProfileState(mHeadsetProfile.getConnectionState(device)));
			contents.put("Headset Profile Is Audio Connected", String.valueOf(mHeadsetProfile.isAudioConnected(device)));
		}
		if (API >= 14 && mHealthProfile != null) {
			contents.put("Health Profile Connection State", getProfileState(mHealthProfile.getConnectionState(device)));
		}
		
		return contents;
	}

	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (API < 11 || !super.startListening(onlyIfCallbackSet)) return false;
		BluetoothProfile.ServiceListener listener = new BluetoothProfile.ServiceListener() {			
			@Override
			public void onServiceDisconnected(int profile) {
				if (getCallback() != null) ((Callback) getCallback()).onServiceDisconnected(profile);			
			}
			@Override
			public void onServiceConnected(int profile, BluetoothProfile proxy) {
				// Must be at least API 11 if this is called
				if (profile == BluetoothProfile.A2DP) mA2dpProfile = (BluetoothA2dp) proxy;
				else if (profile == BluetoothProfile.HEADSET) mHeadsetProfile = (BluetoothHeadset) proxy;
				else if (API >= 14 && profile == BluetoothProfile.HEALTH) mHealthProfile = (BluetoothHealth) proxy;
				
				if (getCallback() != null) ((Callback) getCallback()).onServiceConnected(profile, proxy);
			}
		};
		
		boolean a,b,c = false;
		a = mBluetoothAdapter.getProfileProxy(DeviceInfo.getContext(), listener, BluetoothProfile.A2DP);
		b = mBluetoothAdapter.getProfileProxy(DeviceInfo.getContext(), listener, BluetoothProfile.HEADSET);
		if (API >= 14) c = mBluetoothAdapter.getProfileProxy(DeviceInfo.getContext(), listener, BluetoothProfile.HEALTH);
		return setListening(a || b || c);		
	}

	@Override
	public boolean stopListening() {
		if (API < 11 || !super.stopListening()) return false;
		mBluetoothAdapter.closeProfileProxy(BluetoothProfile.A2DP, mA2dpProfile);
		mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEADSET, mHeadsetProfile);
		if (API >= 14) mBluetoothAdapter.closeProfileProxy(BluetoothProfile.HEALTH, mHealthProfile);
		return !setListening(false);
	}
}
