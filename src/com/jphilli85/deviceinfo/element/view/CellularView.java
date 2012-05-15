package com.jphilli85.deviceinfo.element.view;

import java.util.List;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Cellular;
import com.jphilli85.deviceinfo.element.Element;


public class CellularView extends ListeningElementView implements Cellular.Callback {
	private Cellular mCellular;
	
	public CellularView() {
		this(DeviceInfo.getContext());
	}
	
	protected CellularView(Context context) {
		super(context);
		mCellular = new Cellular(context);
		
		Section section;
		Subsection subsection;
		TableSection table;
		
		section = new Section("Cell Location");
		table = new TableSection();	
		// CellLocation info
		if (mCellular.getCellLocation() != null) {
			if (mCellular.getTelephonyManager().getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
				CdmaCellLocation loc = (CdmaCellLocation) mCellular.getCellLocation();				
				table.add("Base Station ID", String.valueOf(loc.getBaseStationId()));
				table.add("Base Station Latitude", String.valueOf(loc.getBaseStationLatitude()));
				table.add("Base Station Longitude", String.valueOf(loc.getBaseStationLongitude()));
				table.add("Network ID", String.valueOf(loc.getNetworkId()));
				table.add("System ID", String.valueOf(loc.getSystemId()));
			}
			else if (mCellular.getTelephonyManager().getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
				GsmCellLocation loc = (GsmCellLocation) mCellular.getTelephonyManager().getCellLocation();
				table.add("Cell ID", String.valueOf(loc.getCid()));
				table.add("Location Area Code", String.valueOf(loc.getLac()));
				table.add("Primary Scrambling Code", String.valueOf(loc.getPsc()));
			}
		}
		else table.add("", "Cell location unknown");
		section.add(table);
		add(section);
		
		section = new Section("Signal Strength");
		table = new TableSection();
		// SignalStrength info
		if (mCellular.getSignalStrength() != null) {
			table.add("CDMA dBm", String.valueOf(mCellular.getSignalStrength().getCdmaDbm()));
			table.add("CDMA Ec/Io", String.valueOf(mCellular.getSignalStrength().getCdmaEcio()));
			table.add("EVDO dBm", String.valueOf(mCellular.getSignalStrength().getEvdoDbm()));
			table.add("EVDO Ec/Io", String.valueOf(mCellular.getSignalStrength().getEvdoEcio()));
			table.add("EVDO Signal to Noise Ratio", String.valueOf(mCellular.getSignalStrength().getEvdoSnr()));
			table.add("GSM Bit Error Rate", String.valueOf(mCellular.getSignalStrength().getGsmBitErrorRate()));
			table.add("GSM Signal Strength", String.valueOf(mCellular.getSignalStrength().getGsmSignalStrength()));
			table.add("Is GSM", String.valueOf(mCellular.getSignalStrength().isGsm()));			
		}
		else table.add("", "Signal strength unknown");
		section.add(table);
		add(section);
		
		section = new Section("Neighboring Cells");		
		table = new TableSection();
		// NeighboringCellInfo info
		List<NeighboringCellInfo> cells = mCellular.getTelephonyManager().getNeighboringCellInfo();
		if (cells != null && cells.size() > 0) {
			int i = 0;
			for (NeighboringCellInfo info : cells) {
				subsection = new Subsection("Neighboring Cell " + (i + 1));
				table = new TableSection();
				table.add("Cell ID", String.valueOf(info.getCid()));
				table.add("Location Area Code", String.valueOf(info.getLac()));
				table.add("Network Type", String.valueOf(info.getNetworkType()));
				table.add("Primary Scrambling Code", String.valueOf(info.getPsc()));
				table.add("Received Signal Strength Indication", String.valueOf(info.getRssi()));
				subsection.add(table);
				section.add(subsection);
				++i;
			}
		}
		else {
			table.add("", "No known neighboring cells");
			section.add(table);
		}
		add(section);
		
		
		section = new Section("Telephony");
		table = new TableSection();
		// Cellular info
		table.add("MCC", String.valueOf(mCellular.getMcc()));
		table.add("MNC", String.valueOf(mCellular.getMnc()));							
		table.add("Radio Version", Cellular.getRadioVersion());
		table.add("Baseband", Cellular.getBaseband());
		table.add("RIL Version", Cellular.getRilVersion());
			
		// TelephonyManager info
		table.add("Call State", mCellular.getCallState());
		table.add("Data Activity", mCellular.getDataActivity());
		table.add("Data State", mCellular.getDataState());
		table.add("Device ID", mCellular.getTelephonyManager().getDeviceId());
		table.add("Device Software Version", mCellular.getTelephonyManager().getDeviceSoftwareVersion());
		table.add("Line 1 Number", mCellular.getTelephonyManager().getLine1Number());
		table.add("Network Country ISO", mCellular.getTelephonyManager().getNetworkCountryIso());
		table.add("Network Operator", mCellular.getTelephonyManager().getNetworkOperator());
		table.add("Network Operator Name", mCellular.getTelephonyManager().getNetworkOperatorName());
		table.add("Network Type", mCellular.getNetworkType());
		table.add("Phone Type", mCellular.getPhoneType());
		table.add("SIM Country ISO", mCellular.getTelephonyManager().getSimCountryIso());
		table.add("SIM Operator", mCellular.getTelephonyManager().getSimOperator());
		table.add("SIM Operator Name", mCellular.getTelephonyManager().getSimOperatorName());
		table.add("SIM Serial Number", mCellular.getTelephonyManager().getSimSerialNumber());
		table.add("SIM State", mCellular.getSimState());
		table.add("Subscriber ID", mCellular.getTelephonyManager().getSubscriberId());
		table.add("Voice Mail Alpha Tag", mCellular.getTelephonyManager().getVoiceMailAlphaTag());
		table.add("Voice Mail Number", mCellular.getTelephonyManager().getVoiceMailNumber());
		table.add("Has ICC Card", String.valueOf(mCellular.getTelephonyManager().hasIccCard()));
		table.add("Is Network Roaming", String.valueOf(mCellular.getTelephonyManager().isNetworkRoaming()));
	
		// ServiceState info		
		table.add("Is Manual Selection", String.valueOf(mCellular.getServiceState().getIsManualSelection()));
		table.add("Operator Alpha Long", mCellular.getServiceState().getOperatorAlphaLong());
		table.add("Operator Alpha Short", mCellular.getServiceState().getOperatorAlphaShort());
		table.add("Operator Numeric", mCellular.getServiceState().getOperatorNumeric());
		table.add("Is Roaming", String.valueOf(mCellular.getServiceState().getRoaming()));
		table.add("Service State", mCellular.getServiceStateString());

		section.add(table);
		add(section);
		
		mHeader.play();
	}

	@Override
	public Element getElement() {
		return mCellular;
	}

	@Override
	public void onPlay(PlayableSection section) {
		mCellular.startListening();
	}

	@Override
	public void onPause(PlayableSection section) {
		mCellular.stopListening();
	}

	@Override
	public void onCallForwardingIndicatorChanged(boolean cfi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCellLocationChanged(CellLocation location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataActivity(int direction) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataConnectionStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataConnectionStateChanged(int state, int networkType) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessageWaitingIndicatorChanged(boolean mwi) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onServiceStateChanged(ServiceState serviceState) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignalStrengthsChanged(SignalStrength signalStrength) {
		// TODO Auto-generated method stub
		
	}
	
}
