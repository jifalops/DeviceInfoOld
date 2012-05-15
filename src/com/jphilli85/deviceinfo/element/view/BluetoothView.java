package com.jphilli85.deviceinfo.element.view;

import java.util.Set;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Bluetooth;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.UnavailableFeatureException;


public class BluetoothView extends ListeningElementView implements Bluetooth.Callback {
	private Bluetooth mBluetooth;
	
	private TextView mA2dpProfile;
	private TextView mHeadsetProfile;
	private TextView mHeadsetProfileAudio;
	private TextView mHealthProfile;
	
	private BluetoothDevice mAdapterDevice;
	
	public BluetoothView() {
		this(DeviceInfo.getContext());
	}
	
	protected BluetoothView(Context context) {
		super(context);
		try { mBluetooth = new Bluetooth(context); } 
		catch (UnavailableFeatureException e) {
			ListSection list = new ListSection();
			list.add("Bluetooth not supported", null);
			add(list);
			return;
		}
		
		mBluetooth.setCallback(this);
		
		
		String address = mBluetooth.getBluetoothAdapter().getAddress();
						
		TableSection table = new TableSection();
		
		mA2dpProfile = table.getValueTextView();
		mHeadsetProfile = table.getValueTextView();
		mHeadsetProfileAudio = table.getValueTextView();
		mHealthProfile = table.getValueTextView();
		
		table.add("Enabled", String.valueOf(mBluetooth.getBluetoothAdapter().isEnabled()));
		table.add("Name", mBluetooth.getBluetoothAdapter().getName());
		table.add("MAC Address", address);
		table.add("Scan Mode", mBluetooth.getScanMode());
		table.add("Adapter State", mBluetooth.getAdapterState());
		table.add("Discovering", String.valueOf(mBluetooth.getBluetoothAdapter().isDiscovering()));
		
		mAdapterDevice = mBluetooth.getBluetoothAdapter().getRemoteDevice(address);
		if (mAdapterDevice == null) table.add("Adapter Device", "");
		else {
			table.add("Paired State", mBluetooth.getBondState(mAdapterDevice.getBondState()));
			BluetoothClass cls = mAdapterDevice.getBluetoothClass();
			if (cls == null) table.add("Bluetooth Class", "");
			else {
				table.add("Major Class", mBluetooth.getDeviceMajorType(cls.getMajorDeviceClass()));
				table.add("Minor Class", mBluetooth.getDeviceType(cls.getDeviceClass()));
				table.add("Services", TextUtils.join(", ", mBluetooth.getServices(cls)));
			}
		}
		
		table.add("A2DP Profile Connection State", mA2dpProfile);			
		table.add("Headset Profile Connection State", mHeadsetProfile);
		table.add("Headset Profile Audio Connected", mHeadsetProfileAudio);	
		table.add("Health Profile Connection State", mHealthProfile);		
	
		Section adapter = new Section("Adapter");
		adapter.add(table);
		Section devices = new Section("Devices");
		Set<BluetoothDevice> devs = mBluetooth.getBluetoothAdapter().getBondedDevices();
		if (devs == null) {
			ListSection list = new ListSection();
			list.add(null, "No known devices");
			devices.add(list);			
		}
		else {
			int i = 1;
			for (BluetoothDevice d : devs) {
				Subsection section = new Subsection("Device " + i);
				section.add(getDeviceTable(d));
				devices.add(section);
				++i;
			}
		}
		
		add(adapter);
		add(devices);
		
		update();
		mHeader.play();
	}
	
	private TableSection getDeviceTable(BluetoothDevice dev) {
		TableSection table = new TableSection();
		
		if (dev == null) {
			table.add("Device", "");
			return table;
		}
		
		table.add("Name", dev.getName());
		table.add("Paired State", mBluetooth.getBondState(dev.getBondState()));
		table.add("Address", dev.getAddress());
		BluetoothClass cls = dev.getBluetoothClass();
		if (cls == null) table.add("Bluetooth Class", "");
		else {
			table.add("Major Class", mBluetooth.getDeviceMajorType(cls.getMajorDeviceClass()));
			table.add("Minor Class", mBluetooth.getDeviceType(cls.getDeviceClass()));
			table.add("Services", TextUtils.join(", ", mBluetooth.getServices(cls)));
		}

		
//		if (API >= 11) {
//			if (mBluetooth.getA2dpProfile() == null) table.add("A2DP Profile", "");
//			else {
//				table.add("A2DP Profile Connection State", mBluetooth.getProfileState(
//						mBluetooth.getA2dpProfile().getConnectionState(dev)));
//			}
//			if (mBluetooth.getHeadsetProfile() == null) table.add("Headset Profile", "");
//			else {
//				table.add("Headset Profile Connection State", mBluetooth.getProfileState(
//						mBluetooth.getHeadsetProfile().getConnectionState(dev)));
//				table.add("Headset Profile Audio Connected", String.valueOf(
//						mBluetooth.getHeadsetProfile().isAudioConnected(dev)));
//			}
//		}
		
		return table;
	}

	@Override
	public Element getElement() {
		return mBluetooth;
	}

	@Override
	public void onPlay(PlayableSection section) {
		mBluetooth.startListening();
	}

	@Override
	public void onPause(PlayableSection section) {
		mBluetooth.stopListening();
	}

	@Override
	public void onServiceConnected(int profile, BluetoothProfile proxy) {
		update();			
	}

	@Override
	public void onServiceDisconnected(int profile) {
		update();
	}	
	
	private void update() {
		if (API >= 14) {
			mA2dpProfile.setText(mBluetooth.getProfileState(
					mBluetooth.getBluetoothAdapter().getProfileConnectionState(BluetoothProfile.A2DP)));
			mHeadsetProfile.setText(mBluetooth.getProfileState(
					mBluetooth.getBluetoothAdapter().getProfileConnectionState(BluetoothProfile.HEADSET)));
			if (mBluetooth.getHeadsetProfile() != null) {
				mHeadsetProfileAudio.setText(String.valueOf(
					mBluetooth.getHeadsetProfile().isAudioConnected(mAdapterDevice)));
			}
			mHealthProfile.setText(mBluetooth.getProfileState(
					mBluetooth.getBluetoothAdapter().getProfileConnectionState(BluetoothProfile.HEALTH)));	
		}
		else if (API >= 11) {
			if (mBluetooth.getA2dpProfile() != null) {
				mA2dpProfile.setText(mBluetooth.getProfileState(
						mBluetooth.getA2dpProfile().getConnectionState(mAdapterDevice)));
			}
			if (mBluetooth.getHeadsetProfile() != null) {
				mHeadsetProfile.setText(mBluetooth.getProfileState(
						mBluetooth.getHeadsetProfile().getConnectionState(mAdapterDevice)));
				mHeadsetProfileAudio.setText(String.valueOf(
						mBluetooth.getHeadsetProfile().isAudioConnected(mAdapterDevice)));				
			}
		}
	}
}
