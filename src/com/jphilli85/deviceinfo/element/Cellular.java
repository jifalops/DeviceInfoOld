package com.jphilli85.deviceinfo.element;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.jphilli85.deviceinfo.R;
import com.jphilli85.deviceinfo.ShellHelper;

public class Cellular implements ContentsMapper {
	private static final int API = Build.VERSION.SDK_INT;
	
	private static final int FREQUENCY_HIGH = 1000;
	private static final int FREQUENCY_MEDIUM = 2000;
	private static final int FREQUENCY_LOW = 5000;
	
	/** Methods correspond to PhoneStateListener methods */
	public interface Callback {
		void onCallForwardingIndicatorChanged(boolean cfi);
		void onCallStateChanged(int state, String incomingNumber);
		void onCellLocationChanged(CellLocation location);
		void onDataActivity(int direction);
		void onDataConnectionStateChanged(int state);
		void onDataConnectionStateChanged(int state, int networkType);
		void onMessageWaitingIndicatorChanged(boolean mwi);
		void onServiceStateChanged(ServiceState serviceState);
		void onSignalStrengthsChanged(SignalStrength signalStrength);
	}
	
	private final TelephonyManager mTelephonyManager;    
    private final PhoneStateListener mListener;
    
    // TelephonyManager Strings
    public final String CALL_STATE_IDLE;
    public final String CALL_STATE_OFFHOOK;
    public final String CALL_STATE_RINGING;
    public final String DATA_ACTIVITY_DORMANT;
    public final String DATA_ACTIVITY_IN;
    public final String DATA_ACTIVITY_INOUT;
    public final String DATA_ACTIVITY_NONE;
    public final String DATA_ACTIVITY_OUT;
    public final String DATA_CONNECTED;
    public final String DATA_CONNECTING;
    public final String DATA_DISCONNECTED;
    public final String DATA_SUSPENDED;
    public final String NETWORK_TYPE_1xRTT;
    public final String NETWORK_TYPE_CDMA;
    public final String NETWORK_TYPE_EDGE;
    public final String NETWORK_TYPE_EHRPD;
    public final String NETWORK_TYPE_EVDO_0;
    public final String NETWORK_TYPE_EVDO_A;
    public final String NETWORK_TYPE_EVDO_B;
    public final String NETWORK_TYPE_GPRS;
    public final String NETWORK_TYPE_HSDPA;
    public final String NETWORK_TYPE_HSPA;
    public final String NETWORK_TYPE_HSPAP;
    public final String NETWORK_TYPE_HSUPA;
    public final String NETWORK_TYPE_IDEN;
    public final String NETWORK_TYPE_LTE;
    public final String NETWORK_TYPE_UMTS;
    public final String NETWORK_TYPE_UNKNOWN;
    public final String PHONE_TYPE_CDMA;
    public final String PHONE_TYPE_GSM;
    public final String PHONE_TYPE_NONE;
    public final String PHONE_TYPE_SIP;
    public final String SIM_STATE_ABSENT;
    public final String SIM_STATE_NETWORK_LOCKED;
    public final String SIM_STATE_PIN_REQUIRED;
    public final String SIM_STATE_PUK_REQUIRED;
    public final String SIM_STATE_READY;
    public final String SIM_STATE_UNKNOWN;    
    // NeighboringCellInfo Strings
    public final String UNKNOWN_CID;
    public final String UNKNOWN_RSSI;    
    // ServiceState Strings
    public final String STATE_EMERGENCY_ONLY;
    public final String STATE_IN_SERVICE;
    public final String STATE_OUT_OF_SERVICE;
    public final String STATE_POWER_OFF;
    
    private final int mMcc;
    private final int mMnc;
    
    private boolean mIsListening;
    private Callback mCallback;
    private int mUpdateFrequency;
    private long mLastUpdateTimestamp;
    
    private ServiceState mServiceState;
    private CellLocation mCellLocation;
    private SignalStrength mSignalStrength;
    
	public Cellular(Context context) {
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
		mListener = new MyPhoneStateListener();
		
		Configuration config = context.getResources().getConfiguration();
		mMcc = config.mcc;
		mMnc = config.mnc;
		
		mServiceState = new ServiceState();
		mCellLocation = mTelephonyManager.getCellLocation();
		
		mUpdateFrequency = FREQUENCY_MEDIUM;
		
		CALL_STATE_IDLE = context.getString(R.string.call_state_idle);
		CALL_STATE_OFFHOOK = context.getString(R.string.call_state_offhook);
		CALL_STATE_RINGING = context.getString(R.string.call_state_ringing);
		DATA_ACTIVITY_DORMANT = context.getString(R.string.data_activity_dormant);
		DATA_ACTIVITY_IN = context.getString(R.string.data_activity_in);
	    DATA_ACTIVITY_INOUT = context.getString(R.string.data_activity_inout);
	    DATA_ACTIVITY_NONE = context.getString(R.string.data_activity_none);
	    DATA_ACTIVITY_OUT = context.getString(R.string.data_activity_out);
	    DATA_CONNECTED = context.getString(R.string.data_connected);
	    DATA_CONNECTING = context.getString(R.string.data_connecting);
	    DATA_DISCONNECTED = context.getString(R.string.data_disconnected);
	    DATA_SUSPENDED = context.getString(R.string.data_suspended);
	    NETWORK_TYPE_1xRTT = context.getString(R.string.network_type_1xrtt);
	    NETWORK_TYPE_CDMA = context.getString(R.string.network_type_cmda);
	    NETWORK_TYPE_EDGE = context.getString(R.string.network_type_edge);
	    NETWORK_TYPE_EHRPD = context.getString(R.string.network_type_ehrpd);
	    NETWORK_TYPE_EVDO_0 = context.getString(R.string.network_type_evdo_0);
	    NETWORK_TYPE_EVDO_A = context.getString(R.string.network_type_evdo_a);
	    NETWORK_TYPE_EVDO_B = context.getString(R.string.network_type_evdo_b);
	    NETWORK_TYPE_GPRS = context.getString(R.string.network_type_gprs);
	    NETWORK_TYPE_HSDPA = context.getString(R.string.network_type_hsdpa);
	    NETWORK_TYPE_HSPA = context.getString(R.string.network_type_hspa);
	    NETWORK_TYPE_HSPAP = context.getString(R.string.network_type_hspap);
	    NETWORK_TYPE_HSUPA = context.getString(R.string.network_type_hsupa);
	    NETWORK_TYPE_IDEN = context.getString(R.string.network_type_iden);
	    NETWORK_TYPE_LTE = context.getString(R.string.network_type_lte);
	    NETWORK_TYPE_UMTS = context.getString(R.string.network_type_umts);
	    NETWORK_TYPE_UNKNOWN = context.getString(R.string.network_type_unknown);
	    PHONE_TYPE_CDMA = context.getString(R.string.phone_type_cdma);
	    PHONE_TYPE_GSM = context.getString(R.string.phone_type_gsm);
	    PHONE_TYPE_NONE = context.getString(R.string.phone_type_none);
	    PHONE_TYPE_SIP = context.getString(R.string.phone_type_sip);
	    SIM_STATE_ABSENT = context.getString(R.string.sim_state_absent);
	    SIM_STATE_NETWORK_LOCKED = context.getString(R.string.sim_state_network_locked);
	    SIM_STATE_PIN_REQUIRED = context.getString(R.string.sim_state_pin_required);
	    SIM_STATE_PUK_REQUIRED = context.getString(R.string.sim_state_puk_required);
	    SIM_STATE_READY = context.getString(R.string.sim_state_ready);
	    SIM_STATE_UNKNOWN = context.getString(R.string.sim_state_unknown);    
	    // NeighboringCellInfo Strings
	    UNKNOWN_CID = context.getString(R.string.cell_info_unknown_cid);
	    UNKNOWN_RSSI = context.getString(R.string.cell_info_unknown_rssi);    
	    // ServiceState Strings
	    STATE_EMERGENCY_ONLY = context.getString(R.string.service_state_emergency_only);
	    STATE_IN_SERVICE = context.getString(R.string.service_state_in_service);
	    STATE_OUT_OF_SERVICE = context.getString(R.string.service_state_out_of_service);
	    STATE_POWER_OFF = context.getString(R.string.service_state_power_off);
	}
	
	public TelephonyManager getTelephonyManager() {
		return mTelephonyManager;
	}
	
	public PhoneStateListener getPhoneStateListener() {
		return mListener;
	}
	
	public ServiceState getServiceState() {
		return mServiceState;
	}
	
	public CellLocation getCellLocation() {
		return mCellLocation;
	}
	
	public SignalStrength getSignalStrength() {
		return mSignalStrength;
	}
	
	public int getMcc() {
		return mMcc;
	}
	
	public int getMnc() {
		return mMnc;
	}
	
	public String getCallStateString(int state) { 
		switch (state) {
		case TelephonyManager.CALL_STATE_IDLE: return CALL_STATE_IDLE;
		case TelephonyManager.CALL_STATE_OFFHOOK: return CALL_STATE_OFFHOOK;
		case TelephonyManager.CALL_STATE_RINGING: return CALL_STATE_RINGING;
		}
		return null;
	}

	public String getDataActivityString(int activity) {
		switch (activity) {
		case TelephonyManager.DATA_ACTIVITY_DORMANT: return DATA_ACTIVITY_DORMANT;
		case TelephonyManager.DATA_ACTIVITY_IN: return DATA_ACTIVITY_IN;
		case TelephonyManager.DATA_ACTIVITY_INOUT: return DATA_ACTIVITY_INOUT;
		case TelephonyManager.DATA_ACTIVITY_NONE: return DATA_ACTIVITY_NONE;
		case TelephonyManager.DATA_ACTIVITY_OUT: return DATA_ACTIVITY_OUT;
		}
		return null;
	}

	public String getDataStateString(int state) {
		switch (state) {
		case TelephonyManager.DATA_CONNECTED: return DATA_CONNECTED;
		case TelephonyManager.DATA_CONNECTING: return DATA_CONNECTING;
		case TelephonyManager.DATA_DISCONNECTED: return DATA_DISCONNECTED;
		case TelephonyManager.DATA_SUSPENDED: return DATA_SUSPENDED;
		}
		return null;
	}
	
	public String getNetworkTypeString(int type) {
		switch (type) { 
		case TelephonyManager.NETWORK_TYPE_1xRTT: return NETWORK_TYPE_1xRTT;
		case TelephonyManager.NETWORK_TYPE_CDMA: return NETWORK_TYPE_CDMA;
		case TelephonyManager.NETWORK_TYPE_EDGE: return NETWORK_TYPE_EDGE;
		case TelephonyManager.NETWORK_TYPE_EVDO_0: return NETWORK_TYPE_EVDO_0;
		case TelephonyManager.NETWORK_TYPE_EVDO_A: return NETWORK_TYPE_EVDO_A;
		case TelephonyManager.NETWORK_TYPE_GPRS: return NETWORK_TYPE_GPRS;
		case TelephonyManager.NETWORK_TYPE_HSDPA: return NETWORK_TYPE_HSDPA;
		case TelephonyManager.NETWORK_TYPE_HSPA: return NETWORK_TYPE_HSPA;
		case TelephonyManager.NETWORK_TYPE_HSUPA: return NETWORK_TYPE_HSUPA;
		case TelephonyManager.NETWORK_TYPE_UMTS: return NETWORK_TYPE_UMTS;
		case TelephonyManager.NETWORK_TYPE_UNKNOWN: return NETWORK_TYPE_UNKNOWN;
		}
		if (API >= 11 && type == TelephonyManager.NETWORK_TYPE_EHRPD) return NETWORK_TYPE_EHRPD;
		if (API >= 9 && type == TelephonyManager.NETWORK_TYPE_EVDO_B) return NETWORK_TYPE_EVDO_B;
		if (API >= 13 && type == TelephonyManager.NETWORK_TYPE_HSPAP) return NETWORK_TYPE_HSPAP;
		if (API >= 8 && type == TelephonyManager.NETWORK_TYPE_IDEN) return NETWORK_TYPE_IDEN;
		if (API >= 11 && type == TelephonyManager.NETWORK_TYPE_LTE) return NETWORK_TYPE_LTE;
		return null;
	}
	
	public String getPhoneTypeString(int type) {
		switch (type) { 
		case TelephonyManager.PHONE_TYPE_CDMA: return PHONE_TYPE_CDMA;
		case TelephonyManager.PHONE_TYPE_GSM: return PHONE_TYPE_GSM;
		case TelephonyManager.PHONE_TYPE_NONE: return PHONE_TYPE_NONE;
		}
		if (API >= 11 && type == TelephonyManager.PHONE_TYPE_SIP) return PHONE_TYPE_SIP;
		return null;
	}
	
	public String getSimStateString(int state) {
		switch (state) { 
		case TelephonyManager.SIM_STATE_ABSENT: return SIM_STATE_ABSENT;
		case TelephonyManager.SIM_STATE_NETWORK_LOCKED: return SIM_STATE_NETWORK_LOCKED;
		case TelephonyManager.SIM_STATE_PIN_REQUIRED: return SIM_STATE_PIN_REQUIRED;
		case TelephonyManager.SIM_STATE_PUK_REQUIRED: return SIM_STATE_PUK_REQUIRED;
		case TelephonyManager.SIM_STATE_READY: return SIM_STATE_READY;
		case TelephonyManager.SIM_STATE_UNKNOWN: return SIM_STATE_UNKNOWN;
		}
		return null;
	}
	
	public String getServiceStateString(int state) {
		switch (state) { 
		case ServiceState.STATE_EMERGENCY_ONLY: return STATE_EMERGENCY_ONLY;
		case ServiceState.STATE_IN_SERVICE: return STATE_IN_SERVICE;
		case ServiceState.STATE_OUT_OF_SERVICE: return STATE_OUT_OF_SERVICE;
		case ServiceState.STATE_POWER_OFF: return STATE_POWER_OFF;
		}
		return null;
	}
	
	public String getCallStateString() { 
		return getCallStateString(mTelephonyManager.getCallState());
	}
	
	public String getDataActivityString() {
		return getDataActivityString(mTelephonyManager.getDataActivity());
	}
	
	public String getDataStateString() {
		return getDataStateString(mTelephonyManager.getDataState());
	}
	
	public String getNetworkTypeString() {
		return getNetworkTypeString(mTelephonyManager.getNetworkType());
	}
	
	public String getPhoneTypeString() {
		return getPhoneTypeString(mTelephonyManager.getPhoneType());
	}
	
	public String getSimStateString() {
		return getSimStateString(mTelephonyManager.getSimState());
	}
	
	public String getServiceStateString() {
		return getServiceStateString(mServiceState.getState());
	}
	
	public void startListening() {
		startListening(true);
	}
	
	public void startListening(boolean onlyIfCallbackSet) {
		if (mIsListening || (onlyIfCallbackSet && mCallback == null)) return;
		mTelephonyManager.listen(mListener,
			PhoneStateListener.LISTEN_CALL_FORWARDING_INDICATOR
			| PhoneStateListener.LISTEN_CALL_STATE
			| PhoneStateListener.LISTEN_CELL_LOCATION
			| PhoneStateListener.LISTEN_DATA_ACTIVITY
			| PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
			| PhoneStateListener.LISTEN_MESSAGE_WAITING_INDICATOR
			| PhoneStateListener.LISTEN_SERVICE_STATE
			| PhoneStateListener.LISTEN_SIGNAL_STRENGTHS
		);
		mIsListening = true;
	}
	
	public void stopListening() {
		if (!mIsListening) return;
		mTelephonyManager.listen(mListener, PhoneStateListener.LISTEN_NONE);
		mIsListening = false;
	}
	
	public boolean isListening() {
		return mIsListening;
	}
	
	public Callback getCallback() {
		return mCallback;
	}
	
	public void setCallback(Callback callback) {
		mCallback = callback;
	}
	
	/** Minimum time between cell location updates in milliseconds */
	public int getUpdateFrequency() {
		return mUpdateFrequency;
	}
	
	/** Minimum time between cell location updates in milliseconds */
	public void setUpdateFrequency(int frequency) {
		mUpdateFrequency = frequency;
	}
	
	/** Last cell location update timestamp in milliseconds */
	public long getLastUpdateTimestamp() {
		return mLastUpdateTimestamp;
	}
	
	public String getRadioVersion() {
		if (API >= 14) return Build.getRadioVersion();
		else if (API >= 8) return Build.RADIO;
		return null;
	}
	
	public String getBaseband() {
		String bb = ShellHelper.getProp("gsm.version.baseband");
		if (bb == null || bb.equalsIgnoreCase("unknown")) {
			bb = ShellHelper.getProp("ro.baseband");
		}
		return bb;
	}
	
	public String getRilVersion() {
		return ShellHelper.getProp("gsm.version.ril-impl");
	}
	
	public String getRilBarcode() {
		return ShellHelper.getProp("ro.ril.barcode");
	}
	
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		// Cellular info
		contents.put("MCC", String.valueOf(getMcc()));
		contents.put("MNC", String.valueOf(getMnc()));
		contents.put("Is Listening", String.valueOf(isListening()));
		contents.put("Update Frequency", String.valueOf(getUpdateFrequency()));
		contents.put("Last Update Timestamp", String.valueOf(getLastUpdateTimestamp()));
		contents.put("Radio Version", getRadioVersion());
		contents.put("Baseband", getBaseband());
		contents.put("RIL Version", getRilVersion());
		contents.put("RIL Barcode", getRilBarcode());
		
		// TelephonyManager info
		contents.put("Call State", getCallStateString());
		contents.put("Data Activity", getDataActivityString());
		contents.put("Data State", getDataStateString());
		contents.put("Device ID", mTelephonyManager.getDeviceId());
		contents.put("Device Software Version", mTelephonyManager.getDeviceSoftwareVersion());
		contents.put("Line 1 Number", mTelephonyManager.getLine1Number());
		contents.put("Network Country ISO", mTelephonyManager.getNetworkCountryIso());
		contents.put("Network Operator", mTelephonyManager.getNetworkOperator());
		contents.put("Network Operator Name", mTelephonyManager.getNetworkOperatorName());
		contents.put("Network Type", getNetworkTypeString());
		contents.put("Phone Type", getPhoneTypeString());
		contents.put("SIM Country ISO", mTelephonyManager.getSimCountryIso());
		contents.put("SIM Operator", mTelephonyManager.getSimOperator());
		contents.put("SIM Operator Name", mTelephonyManager.getSimOperatorName());
		contents.put("SIM Serial Number", mTelephonyManager.getSimSerialNumber());
		contents.put("SIM State", getSimStateString());
		contents.put("Subscriber ID", mTelephonyManager.getSubscriberId());
		contents.put("Voice Mail Alpha Tag", mTelephonyManager.getVoiceMailAlphaTag());
		contents.put("Voice Mail Number", mTelephonyManager.getVoiceMailNumber());
		contents.put("Has ICC Card", String.valueOf(mTelephonyManager.hasIccCard()));
		contents.put("Is Network Roaming", String.valueOf(mTelephonyManager.isNetworkRoaming()));
		
		// CellLocation info
		if (mCellLocation != null) {
			if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
				CdmaCellLocation loc = (CdmaCellLocation) mCellLocation;				
				contents.put("Base Station ID", String.valueOf(loc.getBaseStationId()));
				contents.put("Base Station Latitude", String.valueOf(loc.getBaseStationLatitude()));
				contents.put("Base Station Longitude", String.valueOf(loc.getBaseStationLongitude()));
				contents.put("Network ID", String.valueOf(loc.getNetworkId()));
				contents.put("System ID", String.valueOf(loc.getSystemId()));
			}
			else if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
				GsmCellLocation loc = (GsmCellLocation) mTelephonyManager.getCellLocation();
				contents.put("Cell ID", String.valueOf(loc.getCid()));
				contents.put("Location Area Code", String.valueOf(loc.getLac()));
				contents.put("Primary Scrambling Code", String.valueOf(loc.getPsc()));
			}
		}
		else contents.put("CellLocation", null);
		
		// NeighboringCellInfo info
		List<NeighboringCellInfo> cells = mTelephonyManager.getNeighboringCellInfo();
		if (cells != null) {
			int i = 0;
			for (NeighboringCellInfo info : cells) {
				contents.put("NeighboringCellInfo " + i + " Cell ID", String.valueOf(info.getCid()));
				contents.put("NeighboringCellInfo " + i + " Location Area Code", String.valueOf(info.getLac()));
				contents.put("NeighboringCellInfo " + i + " Network Type", String.valueOf(info.getNetworkType()));
				contents.put("NeighboringCellInfo " + i + " Primary Scrambling Code", String.valueOf(info.getPsc()));
				contents.put("NeighboringCellInfo " + i + " Received Signal Strength Indication", String.valueOf(info.getRssi()));
				++i;
			}
		}
		else contents.put("NeighboringCellInfo", null);
		
		// ServiceState info		
		contents.put("Is Manual Selection", String.valueOf(mServiceState.getIsManualSelection()));
		contents.put("Operator Alpha Long", mServiceState.getOperatorAlphaLong());
		contents.put("Operator Alpha Short", mServiceState.getOperatorAlphaShort());
		contents.put("Operator Numeric", mServiceState.getOperatorNumeric());
		contents.put("Is Roaming", String.valueOf(mServiceState.getRoaming()));
		contents.put("Service State", getServiceStateString());
		
		// SignalStrength info
		if (mSignalStrength != null) {
			contents.put("CDMA dBm", String.valueOf(mSignalStrength.getCdmaDbm()));
			contents.put("CDMA Ec/Io", String.valueOf(mSignalStrength.getCdmaEcio()));
			contents.put("EVDO dBm", String.valueOf(mSignalStrength.getEvdoDbm()));
			contents.put("EVDO Ec/Io", String.valueOf(mSignalStrength.getEvdoEcio()));
			contents.put("EVDO Signal to Noise Ratio", String.valueOf(mSignalStrength.getEvdoSnr()));
			contents.put("GSM Bit Error Rate", String.valueOf(mSignalStrength.getGsmBitErrorRate()));
			contents.put("GSM Signal Strength", String.valueOf(mSignalStrength.getGsmSignalStrength()));
			contents.put("Is GSM", String.valueOf(mSignalStrength.isGsm()));			
		}
		else contents.put("SignalStrength", null);
		
		
		//TODO belongs elsewhere
		List<NetworkInterface> networks = new ArrayList<NetworkInterface>();
		try { networks = Collections.list(NetworkInterface.getNetworkInterfaces());	} 
		catch (SocketException ignored) {}
		int i = 0;		
		byte[] addr = null;
		StringBuilder address;
		int j;
		InetAddress inetAddress = null;
		for (NetworkInterface ni : networks) { 
			contents.put("NetworkInterface " + i + " Display Name", ni.getDisplayName());
			try { addr = ni.getHardwareAddress(); } 
			catch (SocketException ignored) {}
			if (addr != null) {
				address = new StringBuilder();
				for (byte b : addr) {
					address.append(b & 0xFF).append(':');
				}
				contents.put("NetworkInterface " + i + " Hardware Address", address.toString());
			}
			else contents.put("NetworkInterface " + i + " Hardware Address", null);
			try { contents.put("NetworkInterface " + i + " MTU", String.valueOf(ni.getMTU())); } 
			catch (SocketException ignored) {}
			contents.put("NetworkInterface " + i + " Name", String.valueOf(ni.getName()));
			contents.put("NetworkInterface " + i + " Parent index", String.valueOf(networks.indexOf(ni.getParent())));
			try { contents.put("NetworkInterface " + i + " Is Loopback", String.valueOf(ni.isLoopback())); } 
			catch (SocketException ignored) {}
			try { contents.put("NetworkInterface " + i + " Is PointToPoint", String.valueOf(ni.isPointToPoint())); } 
			catch (SocketException ignored) {}
			try { contents.put("NetworkInterface " + i + " Is Up", String.valueOf(ni.isUp())); } 
			catch (SocketException ignored) {}
			contents.put("NetworkInterface " + i + " Is Virtual", String.valueOf(ni.isVirtual()));
			try { contents.put("NetworkInterface " + i + " Supports Multicast", String.valueOf(ni.supportsMulticast())); } 
			catch (SocketException ignored) {}
			
			List<InterfaceAddress> interfaces = ni.getInterfaceAddresses();
			if (interfaces != null) {
				j = 0;			
				for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Network Prefix bits", 
							String.valueOf(ia.getNetworkPrefixLength()));
					inetAddress = ia.getAddress();
					addr = inetAddress.getAddress();
					if (addr != null) {
						address = new StringBuilder();
						for (byte b : addr) {
							address.append(b & 0xFF).append('.');
						}
						contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address Address", 
								address.toString());
					}
					else contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address Address", null);
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address CanonicalHostName", 
							inetAddress.getCanonicalHostName());
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address HostName", 
							inetAddress.getHostName());
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsAnyLocalAddress", 
							String.valueOf(inetAddress.isAnyLocalAddress()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsLinkLocalAddress", 
							String.valueOf(inetAddress.isLinkLocalAddress()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsLoopbackAddress", 
							String.valueOf(inetAddress.isLoopbackAddress()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsMCGlobal", 
							String.valueOf(inetAddress.isMCGlobal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsMCLinkLocal", 
							String.valueOf(inetAddress.isMCLinkLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsMCNodeLocal", 
							String.valueOf(inetAddress.isMCNodeLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsMCOrgLocal", 
							String.valueOf(inetAddress.isMCOrgLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsMCSiteLocal", 
							String.valueOf(inetAddress.isMCSiteLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsMulticast", 
							String.valueOf(inetAddress.isMulticastAddress()));
					try {
						contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsReachable", 
								String.valueOf(inetAddress.isReachable(5000)));
					} catch (IOException ignored) {}
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Address IsSiteLocal", 
							String.valueOf(inetAddress.isSiteLocalAddress()));
					
					inetAddress = ia.getBroadcast();
					addr = inetAddress.getAddress();
					if (addr != null) {
						address = new StringBuilder();
						for (byte b : addr) {
							address.append(b & 0xFF).append('.');
						}
						contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast Address", 
								address.toString());
					}
					else contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast Address", null);
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast CanonicalHostName", 
							inetAddress.getCanonicalHostName());
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast HostName", 
							inetAddress.getHostName());
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsAnyLocalAddress", 
							String.valueOf(inetAddress.isAnyLocalAddress()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsLinkLocalAddress", 
							String.valueOf(inetAddress.isLinkLocalAddress()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsLoopbackAddress", 
							String.valueOf(inetAddress.isLoopbackAddress()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsMCGlobal", 
							String.valueOf(inetAddress.isMCGlobal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsMCLinkLocal", 
							String.valueOf(inetAddress.isMCLinkLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsMCNodeLocal", 
							String.valueOf(inetAddress.isMCNodeLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsMCOrgLocal", 
							String.valueOf(inetAddress.isMCOrgLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsMCSiteLocal", 
							String.valueOf(inetAddress.isMCSiteLocal()));
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsMulticast", 
							String.valueOf(inetAddress.isMulticastAddress()));
					try {
						contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsReachable", 
								String.valueOf(inetAddress.isReachable(5000)));
					} catch (IOException ignored) {}
					contents.put("NetworkInterface " + i + " InterfaceAddress " + j + " Broadcast IsSiteLocal", 
							String.valueOf(inetAddress.isSiteLocalAddress()));
					
					++j;
				}
			}
			else contents.put("NetworkInterface " + i + " InterfaceAddress List", null);
			++i;
		}
		
		return contents;
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		public void onCallForwardingIndicatorChanged(boolean cfi) {
			
			if (mCallback != null) mCallback.onCallForwardingIndicatorChanged(cfi);
		}
		
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			
			if (mCallback != null) mCallback.onCallStateChanged(state, incomingNumber);
		}
		
		@Override
		public void onCellLocationChanged(CellLocation location) {
			long time = System.currentTimeMillis();
			if (time - mLastUpdateTimestamp < mUpdateFrequency) return;
			mLastUpdateTimestamp = time;
			mCellLocation = location;
			if (mCallback != null) mCallback.onCellLocationChanged(location);
		}
		
		@Override
		public void onDataActivity(int direction) {
			
			if (mCallback != null) mCallback.onDataActivity(direction);
		}
		
		@Override
		public void onDataConnectionStateChanged(int state) {
			
			if (mCallback != null) mCallback.onDataConnectionStateChanged(state);
		}
		
		@Override
		public void onDataConnectionStateChanged(int state, int networkType) {
			
			if (mCallback != null) mCallback.onDataConnectionStateChanged(state, networkType);
		}
		
		@Override
		public void onMessageWaitingIndicatorChanged(boolean mwi) {
			
			if (mCallback != null) mCallback.onMessageWaitingIndicatorChanged(mwi);
		}
		
		@Override
		public void onServiceStateChanged(ServiceState serviceState) {
			mServiceState = serviceState;
			if (mCallback != null) mCallback.onServiceStateChanged(serviceState);
		}
		
		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			mSignalStrength = signalStrength;
			if (mCallback != null) mCallback.onSignalStrengthsChanged(signalStrength);
		}
	}
}
