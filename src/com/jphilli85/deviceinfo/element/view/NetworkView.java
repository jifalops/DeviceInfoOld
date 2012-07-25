package com.jphilli85.deviceinfo.element.view;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.jphilli85.deviceinfo.Convert;
import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Network;


public class NetworkView extends ElementView {
	private Network mNetwork;	
	private NetworkInterface mActiveInterface;
	private String mActiveInterfaceType;
	
	public NetworkView(Context context) {
		super(context);
	}

	@Override
	public Element getElement() {
		return mNetwork;
	}

	@Override
	protected void initialize(Context context) {
		mNetwork = new Network(context);
		
		TableSection table = new TableSection(getContext()) ;
		
		updateActiveInterface();
		table.add("Active Interface", mActiveInterfaceType);
		table.add("IP Address CIDR", getIpAddressCidr());
		table.add("Hardware Address", getHardwareAddress());
		
		table.add("Network Preference", mNetwork.getNetworkPreferenceString());
		table.add("Background Data Allowed", String.valueOf(mNetwork.getBackgroundDataSetting()));
		table.add("Valid Network Types", TextUtils.join(", ", mNetwork.getValidNetworkTypes()));
		add(table);
		
		Section section = new Section(getContext(), "Networks");
		
		
		NetworkInfo[] infos = mNetwork.getConnectivityManager().getAllNetworkInfo();	
		NetworkInfo active = mNetwork.getConnectivityManager().getActiveNetworkInfo();
		
		if (active != null) {
			table = new TableSection(getContext()) ;
			table.add("Active NetworkInfo Type", active.getTypeName());
			section.add(table);			
		}
		if (infos != null) {
			Subsection subsection;
			int i = 0;
			for (NetworkInfo ni : infos) {
				subsection = new Subsection(getContext(), "Network " + (i + 1));
				table = new TableSection(getContext()) ;
				table.add("Type", mNetwork.getNetworkTypeString(ni.getType()));
				table.add("Subtype", mNetwork.getNetworkTypeString(ni.getSubtype()));
				table.add("Type Name", ni.getTypeName());
				table.add("Subtype Name", ni.getSubtypeName());
				table.add("State", mNetwork.getStateString(ni.getState()));
				table.add("Detailed State", mNetwork.getStateString(ni.getDetailedState()));
				table.add("Extra Info", ni.getExtraInfo());
				table.add("Reason Failed", ni.getReason());
				table.add("Active", String.valueOf(ni == active));
				table.add("Available", String.valueOf(ni.isAvailable()));
				table.add("Connected", String.valueOf(ni.isConnected()));
				table.add("Connecting", String.valueOf(mNetwork.isConnecting(ni)));
				table.add("Failover", String.valueOf(ni.isFailover()));
				table.add("Roaming", String.valueOf(ni.isRoaming()));
				
				subsection.add(table);
				section.add(subsection);
				++i;
			}
		}		
		add(section);
	}
	
	// TODO this is not very solid
	private void updateActiveInterface() {
		List<NetworkInterface> networks = new ArrayList<NetworkInterface>();
		try { networks = Collections.list(NetworkInterface.getNetworkInterfaces());	} 
		catch (SocketException ignored) {}
		if (networks == null) return;
		for (NetworkInterface ni : networks) { 
			if (ni.getName().equalsIgnoreCase("ppp0")) {
				mActiveInterfaceType = mNetwork.TYPE_MOBILE;
				mActiveInterface = ni;
				return;
			}
			if (!ni.getName().equalsIgnoreCase("lo")) {
				mActiveInterfaceType = mNetwork.TYPE_WIFI;
				mActiveInterface = ni;
				return;
			}
		}
	}
	
	public String getHardwareAddress() {
		if (mActiveInterface == null) return null;
		byte[] addr = null;
		try { addr = mActiveInterface.getHardwareAddress(); } 
		catch (SocketException ignored) {}
		if (addr == null) return null;
		return Convert.ByteArray.toHexString(addr, ":");
//		StringBuilder address = new StringBuilder();
//		boolean first = true;
//		for (byte b : addr) {
//			if (first) {
//				address.append(b & 0xFF);
//				first = false;
//			}
//			else address.append(':').append(b & 0xFF);
//		}
//		return address.toString();		
	}
	
	public String getIpAddressCidr() {
		if (mActiveInterface == null) return null;
		List<InterfaceAddress> interfaces = mActiveInterface.getInterfaceAddresses();
		if (interfaces == null || interfaces.isEmpty()) return null;
		short networkBits =	interfaces.get(0).getNetworkPrefixLength();
		InetAddress ina = interfaces.get(0).getAddress();
		if (ina == null) return null;
		byte[] addr = ina.getAddress();
		if (addr == null) return null;
		StringBuilder address = new StringBuilder();
		boolean first = true;
		for (byte b : addr) {
			if (first) {
				address.append(b & 0xFF);
				first = false;
			}
			else address.append('.').append(b & 0xFF);
		}
		return address.toString() + "/" + networkBits;		
	}
}
