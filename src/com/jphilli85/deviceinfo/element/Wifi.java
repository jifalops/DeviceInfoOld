package com.jphilli85.deviceinfo.element;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.GroupCipher;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiConfiguration.PairwiseCipher;
import android.net.wifi.WifiConfiguration.Protocol;
import android.net.wifi.WifiConfiguration.Status;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.text.TextUtils;

import com.jphilli85.deviceinfo.Convert;
import com.jphilli85.deviceinfo.R;

public class Wifi extends ListeningElement {
	
	public interface Callback extends ListeningElement.Callback {
		void onScanCompleted(List<ScanResult> results);
		void onNetworkIdsChanged(List<WifiConfiguration> configurations);
		void onNetworkStateChanged(NetworkInfo networkInfo, String bssid, WifiInfo wifiInfo);
		void onRssiChanged(int rssi);
		void onSupplicantConnectionChanged(boolean connected);
		void onSupplicantStateChanged(SupplicantState state, int error);
	}
	
	private interface StringsCallback {
		String getString(int value);
	}
	
	// WifiManager contstants
	public final String ERROR_AUTHENTICATING;
	public final String MODE_FULL;
	public final String MODE_FULL_HIGH_PERF;
	public final String MODE_SCAN_ONLY;
	public final String STATE_DISABLED;
	public final String STATE_DISABLING;
	public final String STATE_ENABLED;
	public final String STATE_ENABLING;
	public final String STATE_UNKNOWN;
	// SupplicantState contstants
	public final String SUPPLICANT_ASSOCIATED;
	public final String SUPPLICANT_ASSOCIATING;
	public final String SUPPLICANT_AUTHENTICATING;
	public final String SUPPLICANT_COMPLETED;
	public final String SUPPLICANT_DISCONNECTED;
	public final String SUPPLICANT_DORMANT;
	public final String SUPPLICANT_FOUR_WAY_HANDSHAKE;
	public final String SUPPLICANT_GROUP_HANDSHAKE;
	public final String SUPPLICANT_INACTIVE;
	public final String SUPPLICANT_INTERFACE_DISABLED;
	public final String SUPPLICANT_INVALID;
	public final String SUPPLICANT_SCANNING;
	public final String SUPPLICANT_UNINITIALIZED;
	// WifiConfiguration.AuthAlgorithm constants
	public final String CONFIG_AUTHALGORITHM_LEAP;
	public final String CONFIG_AUTHALGORITHM_OPEN;
	public final String CONFIG_AUTHALGORITHM_SHARED;
	// WifiConfiguration.GroupCipher constants
	public final String CONFIG_GROUPCIPHER_CCMP;
	public final String CONFIG_GROUPCIPHER_TKIP;
	public final String CONFIG_GROUPCIPHER_WEP104;
	public final String CONFIG_GROUPCIPHER_WEP40;
	// WifiConfiguration.KeyMgmt constants
	public final String CONFIG_KEYMGMT_8021X;
	public final String CONFIG_KEYMGMT_NONE;
	public final String CONFIG_KEYMGMT_WPA_EAP;
	public final String CONFIG_KEYMGMT_WPA_PSK;
	// WifiConfiguration.PairwiseCipher constants
	public final String CONFIG_PAIRWISECIPHER_CCMP;
	public final String CONFIG_PAIRWISECIPHER_TKIP;
	public final String CONFIG_PAIRWISECIPHER_NONE;
	// WifiConfiguration.Protocol constants
	public final String CONFIG_PROTOCOL_RSN;
	public final String CONFIG_PROTOCOL_WPA;
	// WifiConfiguration.Status constants
	public final String CONFIG_STAUS_CURRENT;
	public final String CONFIG_STAUS_DISABLED;
	public final String CONFIG_STAUS_ENABLED;
	
	
	private final WifiManager mWifiManager;
	private final WifiReceiver mReceiver;
	private final Network mNetwork;
	private final Context mContext;
	
	public Wifi(Context context) {	
		super(context);
		ERROR_AUTHENTICATING = context.getString(R.string.wifi_error_authenticating);
		MODE_FULL = context.getString(R.string.wifi_mode_full);
		MODE_FULL_HIGH_PERF = context.getString(R.string.wifi_mode_full_high_perf);
		MODE_SCAN_ONLY = context.getString(R.string.wifi_mode_scan_only);
		STATE_DISABLED = context.getString(R.string.wifi_state_disabled);
		STATE_DISABLING = context.getString(R.string.wifi_state_disabling);
		STATE_ENABLED = context.getString(R.string.wifi_state_enabled);
		STATE_ENABLING = context.getString(R.string.wifi_state_enabling);
		STATE_UNKNOWN = context.getString(R.string.wifi_state_unknown);
		SUPPLICANT_ASSOCIATED = context.getString(R.string.supplicant_state_associated);
		SUPPLICANT_ASSOCIATING = context.getString(R.string.supplicant_state_associating);
		SUPPLICANT_AUTHENTICATING = context.getString(R.string.supplicant_state_authenticating);
		SUPPLICANT_COMPLETED = context.getString(R.string.supplicant_state_completed);
		SUPPLICANT_DISCONNECTED = context.getString(R.string.supplicant_state_disconnected);
		SUPPLICANT_DORMANT = context.getString(R.string.supplicant_state_dormant);
		SUPPLICANT_FOUR_WAY_HANDSHAKE = context.getString(R.string.supplicant_state_four_way_handshake);
		SUPPLICANT_GROUP_HANDSHAKE = context.getString(R.string.supplicant_state_group_handshake);
		SUPPLICANT_INACTIVE = context.getString(R.string.supplicant_state_inactive);
		SUPPLICANT_INTERFACE_DISABLED = context.getString(R.string.supplicant_state_interface_disabled);
		SUPPLICANT_INVALID = context.getString(R.string.supplicant_state_invalid);
		SUPPLICANT_SCANNING = context.getString(R.string.supplicant_state_scanning);
		SUPPLICANT_UNINITIALIZED = context.getString(R.string.supplicant_state_uninitialized);
		CONFIG_AUTHALGORITHM_LEAP = context.getString(R.string.wifi_auth_algorithm_leap);
		CONFIG_AUTHALGORITHM_OPEN = context.getString(R.string.wifi_auth_algorithm_open);
		CONFIG_AUTHALGORITHM_SHARED = context.getString(R.string.wifi_auth_algorithm_shared);
		CONFIG_GROUPCIPHER_CCMP = context.getString(R.string.wifi_group_cipher_ccmp);
		CONFIG_GROUPCIPHER_TKIP = context.getString(R.string.wifi_group_cipher_tkip);
		CONFIG_GROUPCIPHER_WEP104 = context.getString(R.string.wifi_group_cipher_wep104);
		CONFIG_GROUPCIPHER_WEP40 = context.getString(R.string.wifi_group_cipher_wep40);
		CONFIG_KEYMGMT_8021X = context.getString(R.string.wifi_key_management_8021x);
		CONFIG_KEYMGMT_NONE = context.getString(R.string.wifi_key_management_none);
		CONFIG_KEYMGMT_WPA_EAP = context.getString(R.string.wifi_key_management_wpa_eap);
		CONFIG_KEYMGMT_WPA_PSK = context.getString(R.string.wifi_key_management_wpa_psk);
		CONFIG_PAIRWISECIPHER_CCMP = context.getString(R.string.wifi_pairwise_cipher_ccmp);
		CONFIG_PAIRWISECIPHER_TKIP = context.getString(R.string.wifi_pairwise_cipher_tkip);
		CONFIG_PAIRWISECIPHER_NONE = context.getString(R.string.wifi_pairwise_cipher_none);
		CONFIG_PROTOCOL_RSN = context.getString(R.string.wifi_protocol_rsn);
		CONFIG_PROTOCOL_WPA = context.getString(R.string.wifi_protocol_wpa);
		CONFIG_STAUS_CURRENT = context.getString(R.string.wifi_status_current);
		CONFIG_STAUS_DISABLED = context.getString(R.string.wifi_status_disabled);
		CONFIG_STAUS_ENABLED = context.getString(R.string.wifi_status_enabled);
		
		mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		mReceiver = new WifiReceiver(this);		
		mNetwork = new Network(context);
		mContext = context;
	}
	
	// TODO where can this be used?
	public String getWifiModeString(int mode) {
		switch (mode) {
		case WifiManager.WIFI_MODE_FULL: return MODE_FULL;
		case WifiManager.WIFI_MODE_FULL_HIGH_PERF: return MODE_FULL_HIGH_PERF;
		case WifiManager.WIFI_MODE_SCAN_ONLY: return MODE_SCAN_ONLY;
		default: return null;
		}		
	}
	
	public String getWifiStateString(int state) {
		switch (state) {
		case WifiManager.WIFI_STATE_DISABLED: return STATE_DISABLED;
		case WifiManager.WIFI_STATE_DISABLING: return STATE_DISABLING;
		case WifiManager.WIFI_STATE_ENABLED: return STATE_ENABLED;
		case WifiManager.WIFI_STATE_ENABLING: return STATE_ENABLING;
		case WifiManager.WIFI_STATE_UNKNOWN: return STATE_UNKNOWN;
		default: return null;
		}		
	}
	
	public String getSupplicantStateString(SupplicantState state) {
		switch (state) {
		case ASSOCIATED: return SUPPLICANT_ASSOCIATED;		
		case ASSOCIATING: return SUPPLICANT_ASSOCIATING;
		case AUTHENTICATING: return SUPPLICANT_AUTHENTICATING;
		case COMPLETED: return SUPPLICANT_COMPLETED;
		case DISCONNECTED: return SUPPLICANT_DISCONNECTED;
		case DORMANT: return SUPPLICANT_DORMANT;
		case FOUR_WAY_HANDSHAKE: return SUPPLICANT_FOUR_WAY_HANDSHAKE;
		case GROUP_HANDSHAKE: return SUPPLICANT_GROUP_HANDSHAKE;
		case INACTIVE: return SUPPLICANT_INACTIVE;
		case INTERFACE_DISABLED: return SUPPLICANT_INTERFACE_DISABLED;
		case INVALID: return SUPPLICANT_INVALID;
		case SCANNING: return SUPPLICANT_SCANNING;
		case UNINITIALIZED: return SUPPLICANT_UNINITIALIZED;
		default: return null;
		}		
	}
	
	public String getAuthAlgorithmString(int value) {
		switch (value) {
		case AuthAlgorithm.LEAP: return CONFIG_AUTHALGORITHM_LEAP;
		case AuthAlgorithm.OPEN: return CONFIG_AUTHALGORITHM_OPEN;
		case AuthAlgorithm.SHARED: return CONFIG_AUTHALGORITHM_SHARED;
		default: return null;
		}
	}
	
	public String getGroupCipherString(int value) {
		switch (value) {
		case GroupCipher.CCMP: return CONFIG_GROUPCIPHER_CCMP;
		case GroupCipher.TKIP: return CONFIG_GROUPCIPHER_TKIP;
		case GroupCipher.WEP104: return CONFIG_GROUPCIPHER_WEP104;
		case GroupCipher.WEP40: return CONFIG_GROUPCIPHER_WEP40;
		default: return null;
		}
	}
	
	public String getKeyManagementString(int value) {
		switch (value) {
		case KeyMgmt.IEEE8021X: return CONFIG_KEYMGMT_8021X;
		case KeyMgmt.NONE: return CONFIG_KEYMGMT_NONE;
		case KeyMgmt.WPA_EAP: return CONFIG_KEYMGMT_WPA_EAP;
		case KeyMgmt.WPA_PSK: return CONFIG_KEYMGMT_WPA_PSK;
		default: return null;
		}
	}
	
	public String getPairwiseCipherString(int value) {
		switch (value) {
		case PairwiseCipher.CCMP: return CONFIG_PAIRWISECIPHER_CCMP;
		case PairwiseCipher.NONE: return CONFIG_PAIRWISECIPHER_NONE;
		case PairwiseCipher.TKIP: return CONFIG_PAIRWISECIPHER_TKIP;
		default: return null;
		}
	}
	
	public String getProtocolString(int value) {
		switch (value) {
		case Protocol.RSN: return CONFIG_PROTOCOL_RSN;
		case Protocol.WPA: return CONFIG_PROTOCOL_WPA;
		default: return null;
		}
	}
	
	public String getStatusString(int value) {
		switch (value) {
		case Status.CURRENT: return CONFIG_STAUS_CURRENT;
		case Status.DISABLED: return CONFIG_STAUS_DISABLED;
		case Status.ENABLED: return CONFIG_STAUS_ENABLED;
		default: return null;
		}
	}
	
	public String[] getAuthAlgorithmStrings(BitSet bits) {
		return getStrings(bits, new StringsCallback() {			
			@Override
			public String getString(int value) {
				return getAuthAlgorithmString(value);
			}
		});
	}
	
	public String[] getGroupCipherStrings(BitSet bits) {
		return getStrings(bits, new StringsCallback() {			
			@Override
			public String getString(int value) {
				return getGroupCipherString(value);
			}
		});
	}
	
	public String[] getKeyManagementStrings(BitSet bits) {
		return getStrings(bits, new StringsCallback() {			
			@Override
			public String getString(int value) {
				return getKeyManagementString(value);
			}
		});
	}
	
	public String[] getPairwiseCipherStrings(BitSet bits) {
		return getStrings(bits, new StringsCallback() {			
			@Override
			public String getString(int value) {
				return getPairwiseCipherString(value);
			}
		});
	}
	
	public String[] getProtocolStrings(BitSet bits) {
		return getStrings(bits, new StringsCallback() {			
			@Override
			public String getString(int value) {
				return getProtocolString(value);
			}
		});
	}
	
	private String[] getStrings(BitSet bits, StringsCallback callback) {
		if (bits == null || bits.isEmpty() || callback == null) return null;
		if (bits.length() > 31) return null;
		String[] strings = new String[bits.cardinality()];
		int i = 0;
		int bitIndex = bits.nextSetBit(0);
		while (bitIndex > -1 && i < strings.length) {			
			strings[i] = callback.getString(bitIndex);
			++i;
			bitIndex = bits.nextSetBit(bitIndex + 1);
		}
		return strings;
	}
	
	public int getNumWepKeys(String[] keys) {
		if (keys == null || keys.length == 0) return 0;
		int count = 0;
		for (String s : keys) {
			if (s != null && s.length() > 0 && !s.equalsIgnoreCase("null")) ++count;
		}
		return count;
	}
	
	public boolean hasPreSharedKey(String key) {
		return key != null && key.length() > 0 && !key.equalsIgnoreCase("null");
	}
	
	public WifiManager getWifiManager() {
		return mWifiManager;
	}
	
	public String getDetailedState() {
		WifiInfo winfo = mWifiManager.getConnectionInfo();
		if (winfo == null) return null;
		return mNetwork.getStateString(WifiInfo.getDetailedStateOf(winfo.getSupplicantState()));	
	}
	
	public String getDetailedState(SupplicantState state) {		
		return mNetwork.getStateString(WifiInfo.getDetailedStateOf(state));	
	}
	
	
	// TODO use system properties for ip, dns, etc as a backup
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = super.getContents();
		
		// Wifi
		contents.put("State", getWifiStateString(mWifiManager.getWifiState()));
		
		// WifiManager
		contents.put("IsEnabled", String.valueOf(mWifiManager.isWifiEnabled()));
		
//		Requires CHANGE_WIFI_STATE permission
//		contents.put("Can Ping Supplicant", String.valueOf(mWifiManager.pingSupplicant()));
		
		// WifiInfo
		WifiInfo winfo = mWifiManager.getConnectionInfo();
		if (winfo != null) {
			contents.put("BSSID", winfo.getBSSID());
			contents.put("SSID", winfo.getSSID());
			contents.put("Is SSID Hidden", String.valueOf(winfo.getHiddenSSID()));
			contents.put("IP", Convert.Ip4.fromInt(winfo.getIpAddress()));
			contents.put("Link Speed (Mbps)", String.valueOf(winfo.getLinkSpeed()));
			contents.put("MAC Address", winfo.getMacAddress());
			contents.put("Network ID", String.valueOf(winfo.getNetworkId()));
			contents.put("RSSI (asu)", String.valueOf(winfo.getRssi()));
			contents.put("Supplicant State", getSupplicantStateString(winfo.getSupplicantState()));
			contents.put("Detailed State", mNetwork.getStateString(
					WifiInfo.getDetailedStateOf(winfo.getSupplicantState())));			
		}
		else contents.put("WifiInfo", null);
		
		// DhcpInfo
		DhcpInfo dinfo = mWifiManager.getDhcpInfo();
		if (dinfo != null) {
			contents.put("DHCP DNS1", Convert.Ip4.fromInt(dinfo.dns1));
			contents.put("DHCP DNS2", Convert.Ip4.fromInt(dinfo.dns2));
			contents.put("DHCP Gateway", Convert.Ip4.fromInt(dinfo.gateway));
			contents.put("DHCP IP", Convert.Ip4.fromInt(dinfo.ipAddress));
			contents.put("DHCP Lease Duration (s?)", String.valueOf(dinfo.leaseDuration));
			contents.put("DHCP Netmask", Convert.Ip4.fromInt(dinfo.netmask));
			contents.put("DHCP Server", Convert.Ip4.fromInt(dinfo.serverAddress));
		}
		else contents.put("DhcpInfo", null);
		
		// ScanResults
		List<ScanResult> scans = mWifiManager.getScanResults();
		if (scans != null) {
			for (int i = 0; i < scans.size(); ++i) {
				contents.put("ScanResult " + i + " BSSID", scans.get(i).BSSID);
				contents.put("ScanResult " + i + " SSID", scans.get(i).SSID);
				contents.put("ScanResult " + i + " capabilities", scans.get(i).capabilities);
				contents.put("ScanResult " + i + " frequency (MHz)", String.valueOf(scans.get(i).frequency));
				contents.put("ScanResult " + i + " level (dBm)", String.valueOf(scans.get(i).level));
			}
		}
		else contents.put("ScanResults", null);
		
		// WifiConfigurations
		List<WifiConfiguration> wifis = mWifiManager.getConfiguredNetworks();
		String[] strings;
		if (wifis != null) {
			for (int i = 0; i < wifis.size(); ++i) {
				contents.put("WifiConfiguration " + i + " BSSID", wifis.get(i).BSSID);
				contents.put("WifiConfiguration " + i + " SSID", wifis.get(i).SSID);
				contents.put("WifiConfiguration " + i + " Is Hidden SSID", String.valueOf(wifis.get(i).hiddenSSID));
				contents.put("WifiConfiguration " + i + " Network ID", String.valueOf(wifis.get(i).networkId));
				contents.put("WifiConfiguration " + i + " Has PreSharedKey", 
						String.valueOf(hasPreSharedKey(wifis.get(i).preSharedKey)));
				contents.put("WifiConfiguration " + i + " Priority", String.valueOf(wifis.get(i).priority));
				contents.put("WifiConfiguration " + i + " Status", getStatusString(wifis.get(i).status));
				contents.put("WifiConfiguration " + i + " Num WEP Keys", String.valueOf(getNumWepKeys(wifis.get(i).wepKeys)));
				contents.put("WifiConfiguration " + i + " Default WEP Key index", String.valueOf(wifis.get(i).wepTxKeyIndex));
				
				strings = getAuthAlgorithmStrings(wifis.get(i).allowedAuthAlgorithms);
				if (strings != null) contents.put("WifiConfiguration " + i + " Allowed AuthAlgorithms",	
						TextUtils.join(", ", strings));
				else contents.put("WifiConfiguration " + i + " Allowed AuthAlgorithms", null);
				
				strings = getGroupCipherStrings(wifis.get(i).allowedGroupCiphers);
				if (strings != null) contents.put("WifiConfiguration " + i + " Allowed GroupCiphers",	
						TextUtils.join(", ", strings));
				else contents.put("WifiConfiguration " + i + " Allowed GroupCiphers", null);

				strings = getKeyManagementStrings(wifis.get(i).allowedKeyManagement);
				if (strings != null) contents.put("WifiConfiguration " + i + " Allowed KeyManagement",	
						TextUtils.join(", ", strings));
				else contents.put("WifiConfiguration " + i + " Allowed KeyManagement", null);
				
				strings = getPairwiseCipherStrings(wifis.get(i).allowedPairwiseCiphers);
				if (strings != null) contents.put("WifiConfiguration " + i + " Allowed PairwiseCiphers",	
						TextUtils.join(", ", strings));
				else contents.put("WifiConfiguration " + i + " Allowed PairwiseCiphers", null);
				
				strings = getProtocolStrings(wifis.get(i).allowedProtocols);
				if (strings != null) contents.put("WifiConfiguration " + i + " Allowed Protocols",	
						TextUtils.join(", ", strings));
				else contents.put("WifiConfiguration " + i + " Allowed Protocols", null);
			}
		}
		else contents.put("WifiConfigurations", null);
		
		return contents;
	}

	public static class WifiReceiver extends BroadcastReceiver {
		private final Wifi mWifi;
		
		public WifiReceiver(Wifi wifi) {
			mWifi = wifi;
		}
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if (mWifi == null || mWifi.getCallback() == null || intent == null) return;
			
			// TODO use the intent extras to return values to the callback
			if (intent.getAction().equals(WifiManager.NETWORK_IDS_CHANGED_ACTION)) {
				((Callback) mWifi.getCallback()).onNetworkIdsChanged(mWifi.getWifiManager().getConfiguredNetworks());
			}
			else if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {		
				NetworkInfo ni = null;
				String bssid = null;
				WifiInfo wi = null;
				Parcelable p = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);			
				if (p != null) {
					ni = (NetworkInfo) p;
					if (ni.getState() == NetworkInfo.State.CONNECTED) {						
						bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
						p = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
						if (p != null) wi = (WifiInfo) p;
					}
				}
				
				((Callback) mWifi.getCallback()).onNetworkStateChanged(ni, bssid, wi);
			}
			else if (intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)) {				
				((Callback) mWifi.getCallback()).onRssiChanged(intent.getIntExtra(WifiManager.EXTRA_NEW_RSSI, 0));				
			}
			else if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				((Callback) mWifi.getCallback()).onScanCompleted(mWifi.getWifiManager().getScanResults());								
			}
			else if (intent.getAction().equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
				((Callback) mWifi.getCallback()).onSupplicantConnectionChanged(
						intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false));								
			}
			else if (intent.getAction().equals(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
				SupplicantState ss = null;
				Parcelable p = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
				if (p != null) ss = (SupplicantState) p; 
				
				((Callback) mWifi.getCallback()).onSupplicantStateChanged(
						ss, intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0));								
			}
		}
	}

	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet) || mContext == null) return false;
		mContext.registerReceiver(mReceiver, 
				new IntentFilter(WifiManager.NETWORK_IDS_CHANGED_ACTION));
		mContext.registerReceiver(mReceiver, 
				new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION));
		mContext.registerReceiver(mReceiver, 
				new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
		mContext.registerReceiver(mReceiver, 
				new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		mContext.registerReceiver(mReceiver, 
				new IntentFilter(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
		mContext.registerReceiver(mReceiver, 
				new IntentFilter(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION));
		return setListening(true);
	}

	@Override
	public boolean stopListening() {
		if (!super.stopListening() || mContext == null) return false;
		mContext.unregisterReceiver(mReceiver);
		return !setListening(false);
	}
}
