package com.jphilli85.deviceinfo.element.view;

import java.util.List;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.TextView;

import com.jphilli85.deviceinfo.Convert;
import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Wifi;


public class WifiView extends ListeningElementView implements Wifi.Callback {
	private Wifi mWifi;
	
	private final Section 
		mScanResultsSection, mWifiConfigSection;
	
	private final TextView
		mBssid, mSsid,
		mHidden, mIp,
		mMac, mId,
		mState, mEnabled,
		mRssi, mSupplicantState, 
		mSpeed, mDetailedState,
		
		mDns1, mDns2,
		mGateway, mDhcpIp,
		mLease, mNetmask,
		mServer,
		
		mSupplicantConnected;
	
	public WifiView() {
		this(DeviceInfo.getContext());
	}
	
	protected WifiView(Context context) {
		super(context);
		mScanResultsSection = new Section("Last Scan Results");
		mWifiConfigSection = new Section("Wifi Configurations");
		
		TableSection table = new TableSection();
		mRssi = table.getValueTextView();
		mSpeed = table.getValueTextView();
		mSupplicantState = table.getValueTextView();
		mDetailedState = table.getValueTextView();
		
		mState = table.getValueTextView();
		mEnabled = table.getValueTextView();
		mBssid = table.getValueTextView();
		mSsid = table.getValueTextView();
		mHidden = table.getValueTextView();
		mIp = table.getValueTextView();
		mMac = table.getValueTextView();
		mId = table.getValueTextView();
		
		mDns1 = table.getValueTextView();
		mDns2 = table.getValueTextView();
		mGateway = table.getValueTextView();
		mDhcpIp = table.getValueTextView();
		mLease = table.getValueTextView();
		mNetmask = table.getValueTextView();
		mServer = table.getValueTextView();
		
		mSupplicantConnected = table.getValueTextView();
	}
	
	private void populateWifiInfo(WifiInfo info) {		
		WifiManager wm = mWifi.getWifiManager();		
		mEnabled.setText(String.valueOf(wm.isWifiEnabled()));		
		mState.setText(mWifi.getWifiStateString(wm.getWifiState()));
		if (info == null) {
//			mBssid.setText(null);
//			mSsid.setText(null);
//			mHidden.setText(null);
//			mIp.setText(null);
//			mMac.setText(null);
//			mId.setText(null);
			return;
		}
		mBssid.setText(info.getBSSID());
		mSsid.setText(info.getSSID());
		mHidden.setText(String.valueOf(info.getHiddenSSID()));
		mIp.setText(Convert.Ip4.fromInt(info.getIpAddress()));
		mMac.setText(info.getMacAddress());
		mId.setText(String.valueOf(info.getNetworkId()));
		
		mRssi.setText(String.valueOf(info.getRssi()));
		mSpeed.setText(String.valueOf(info.getLinkSpeed()));
		mSupplicantState.setText(mWifi.getSupplicantStateString(info.getSupplicantState()));
		mDetailedState.setText(mWifi.getDetailedState());
		
		populateDhcpInfo(wm.getDhcpInfo());
	}
	
	private void populateDhcpInfo(DhcpInfo info) {
		if (info == null) {
//			mDns1.setText(null);
//			mDns2.setText(null);
//			mGateway.setText(null);
//			mDhcpIp.setText(null);
//			mLease.setText(null);
//			mNetmask.setText(null);
//			mServer.setText(null);
			return;
		}
		mDns1.setText(Convert.Ip4.fromInt(info.dns1));
		mDns2.setText(Convert.Ip4.fromInt(info.dns2));
		mGateway.setText(Convert.Ip4.fromInt(info.gateway));
		mDhcpIp.setText(Convert.Ip4.fromInt(info.ipAddress));
		mLease.setText(String.valueOf(info.leaseDuration));
		mNetmask.setText(Convert.Ip4.fromInt(info.netmask));
		mServer.setText(Convert.Ip4.fromInt(info.serverAddress));
	}
	
	private void populateScanResults(List<ScanResult> results) {
		mScanResultsSection.getContent().removeAllViews();		
		TableSection table = new TableSection();
		if (results == null || results.isEmpty()) {
			table.add(null, "None");
			mScanResultsSection.add(table);
			return;
		}
		Subsection subsection;
		for (int i = 0; i < results.size(); ++i) {
			subsection = new Subsection("Scan Result " + (i + 1));
			table = new TableSection();
			table.add("BSSID", results.get(i).BSSID);
			table.add("SSID", results.get(i).SSID);
			table.add("capabilities", results.get(i).capabilities);
			table.add("frequency (MHz)", String.valueOf(results.get(i).frequency));
			table.add("level (dBm)", String.valueOf(results.get(i).level));
			subsection.add(table);
			mScanResultsSection.add(subsection);
			subsection.expand();
		}
	}
	
	private void populateWifiConfigs(List<WifiConfiguration> configs) {
		mWifiConfigSection.getContent().removeAllViews();		
		TableSection table = new TableSection();
		if (configs == null || configs.isEmpty()) {
			table.add(null, "None");
			mWifiConfigSection.add(table);
			return;
		}
		Subsection subsection;
		String[] strings;
		for (int i = 0; i < configs.size(); ++i) {
			subsection = new Subsection("Wifi Configuration " + (i + 1));
			table = new TableSection();
			table.add("BSSID", configs.get(i).BSSID);
			table.add("SSID", configs.get(i).SSID);
			table.add("Is Hidden SSID", String.valueOf(configs.get(i).hiddenSSID));
			table.add("Network ID", String.valueOf(configs.get(i).networkId));
			table.add("Has PreSharedKey", 
					String.valueOf(mWifi.hasPreSharedKey(configs.get(i).preSharedKey)));
			table.add("Priority", String.valueOf(configs.get(i).priority));
			table.add("Status", mWifi.getStatusString(configs.get(i).status));
			table.add("Num WEP Keys", String.valueOf(mWifi.getNumWepKeys(configs.get(i).wepKeys)));
			table.add("Default WEP Key index", String.valueOf(configs.get(i).wepTxKeyIndex));
			
			strings = mWifi.getAuthAlgorithmStrings(configs.get(i).allowedAuthAlgorithms);
			if (strings != null) table.add("Allowed AuthAlgorithms",	
					TextUtils.join(", ", strings));
			else table.add("Allowed AuthAlgorithms", "None");
			
			strings = mWifi.getGroupCipherStrings(configs.get(i).allowedGroupCiphers);
			if (strings != null) table.add("Allowed GroupCiphers",	
					TextUtils.join(", ", strings));
			else table.add("Allowed GroupCiphers", "None");

			strings = mWifi.getKeyManagementStrings(configs.get(i).allowedKeyManagement);
			if (strings != null) table.add("Allowed KeyManagement",	
					TextUtils.join(", ", strings));
			else table.add("Allowed KeyManagement", "None");
			
			strings = mWifi.getPairwiseCipherStrings(configs.get(i).allowedPairwiseCiphers);
			if (strings != null) table.add("Allowed PairwiseCiphers",	
					TextUtils.join(", ", strings));
			else table.add("Allowed PairwiseCiphers", "None");
			
			strings = mWifi.getProtocolStrings(configs.get(i).allowedProtocols);
			if (strings != null) table.add("Allowed Protocols",	
					TextUtils.join(", ", strings));
			else table.add("Allowed Protocols", "None");
			subsection.add(table);
			mWifiConfigSection.add(subsection);
			subsection.expand();
		}
	}
	
	private void updateLinkSpeed() {
		try { mSpeed.setText(String.valueOf(mWifi.getWifiManager().getConnectionInfo().getLinkSpeed())); }
		catch (NullPointerException e) {
			mSpeed.setText(null);
		}
	}

	@Override
	public Element getElement() {
		return mWifi;
	}

	@Override
	protected void initialize(Context context) {
		mWifi = new Wifi(context);
		
		Section section = new Section("Wifi Info");
		TableSection table = new TableSection();
		table.add("Enabled", mEnabled);
		table.add("State", mState);
		
		table.add("Connected", mSupplicantConnected);
		
		table.add("Supplicant State", mSupplicantState);
		table.add("Detailed State", mDetailedState);
		table.add("RSSI (asu)", mRssi);
		table.add("Link Speed (Mbps)", mSpeed);
		
		table.add("BSSID", mBssid);
		table.add("SSID", mSsid);
		table.add("SSID Hidden", mHidden);
		table.add("IP", mIp);
		table.add("MAC Address", mMac);
		table.add("Network ID", mId); //TODO prioritize this wificonfiguration
		
		section.add(table);
		add(section);
		
		section = new Section("DHCP info");
		table = new TableSection();
		table.add("DNS1", mDns1);
		table.add("DNS2", mDns2);
		table.add("Gateway", mGateway);
		table.add("IP", mDhcpIp);
		table.add("Netmask", mNetmask);
		table.add("Lease Duration (s?)", mLease);		
		table.add("Server", mServer);
		
		section.add(table);
		add(section);
		
		add(mScanResultsSection);
		add(mWifiConfigSection);
		
		WifiManager wm = mWifi.getWifiManager();
		populateWifiInfo(wm.getConnectionInfo());
		populateScanResults(wm.getScanResults());
		populateWifiConfigs(wm.getConfiguredNetworks());
	}
	
	@Override
	protected void onInitialized() {
		mWifi.setCallback(this);
		super.onInitialized();
	}

	@Override
	public void onScanCompleted(List<ScanResult> results) {
		populateScanResults(results);
	}

	@Override
	public void onNetworkIdsChanged(List<WifiConfiguration> configurations) {
		populateWifiConfigs(configurations);
	}

	@Override
	public void onNetworkStateChanged(NetworkInfo networkInfo, String bssid, WifiInfo wifiInfo) {
		populateWifiInfo(wifiInfo);
	}

	@Override
	public void onRssiChanged(int rssi) {
		mRssi.setText(String.valueOf(rssi));
		updateLinkSpeed();
	}

	@Override
	public void onSupplicantConnectionChanged(boolean connected) {
		mSupplicantConnected.setText(String.valueOf(connected));
		try { onSupplicantStateChanged(mWifi.getWifiManager().getConnectionInfo().getSupplicantState(), 0); }
		catch (NullPointerException ignored) {}
	}

	@Override
	public void onSupplicantStateChanged(SupplicantState state, int error) {
		mSupplicantState.setText(mWifi.getSupplicantStateString(state));
		if (error == WifiManager.ERROR_AUTHENTICATING) {
			mSupplicantState.append(" (" + mWifi.ERROR_AUTHENTICATING + ")");
		}
		mDetailedState.setText(mWifi.getDetailedState(state));		
		updateLinkSpeed();
	}
}
