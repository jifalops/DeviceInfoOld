package com.jphilli85.deviceinfo.element.view;

import java.util.List;

import android.content.Context;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Cellular;
import com.jphilli85.deviceinfo.element.Element;


public class CellularView extends ListeningElementView implements Cellular.Callback {
	private Cellular mCellular;
	private Section mNeighboringCellSection;
	
	private final TextView 
			// Unknown
			mCallForwarding, 
			mMessageWaiting,
			
			// CellLocation
			mBaseStation, mBaseLat, mBaseLon,
			mNetwork, mSystem,
			mCell, mLac, mPsc,
			
			// Telephony
			mCallState, 
			mDataActivity,
			mDataConnectionState, 			
			mServiceState, 
			mNetworkType,
			
			// SignalStrength
			mCdmaDbm, mCdmaEcio, mEvdoDbm, mEvdoEcio,
			mEvdoSn, mGsmBitError, mGsmSignal, mIsGsm;
	
	public CellularView() {
		this(DeviceInfo.getContext());
	}
	
	protected CellularView(Context context) {
		super(context);
		mCellular = new Cellular(context);
		mCellular.setCallback(this);
				
		Section section;
//		Subsection subsection;
		TableSection table = new TableSection();
		
		mCallForwarding = table.getValueTextView();
		mMessageWaiting = table.getValueTextView();
		
		mCallState = table.getValueTextView();
		mDataActivity = table.getValueTextView();
		mDataConnectionState = table.getValueTextView();		
		mServiceState = table.getValueTextView();
		mNetworkType = table.getValueTextView();
		
		mBaseStation = table.getValueTextView();
		mBaseLat = table.getValueTextView();
		mBaseLon = table.getValueTextView();
		mNetwork = table.getValueTextView();
		mSystem = table.getValueTextView();
		mCell = table.getValueTextView();
		mLac = table.getValueTextView();
		mPsc = table.getValueTextView();
		
		mCdmaDbm = table.getValueTextView(); 
		mCdmaEcio = table.getValueTextView(); 
		mEvdoDbm = table.getValueTextView(); 
		mEvdoEcio = table.getValueTextView();
		mEvdoSn = table.getValueTextView(); 
		mGsmBitError = table.getValueTextView();
		mGsmSignal = table.getValueTextView(); 
		mIsGsm = table.getValueTextView();

		section = new Section("Cell Location");
		// CellLocation info
//		if (mCellular.getCellLocation() != null) {
			if (mCellular.getTelephonyManager().getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
//				CdmaCellLocation loc = (CdmaCellLocation) mCellular.getCellLocation();				
				table.add("Base Station ID", mBaseStation);
				table.add("Base Station Latitude", mBaseLat);
				table.add("Base Station Longitude", mBaseLon);
				table.add("Network ID", mNetwork);
				table.add("System ID", mSystem);
			}
			else if (mCellular.getTelephonyManager().getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
//				GsmCellLocation loc = (GsmCellLocation) mCellular.getTelephonyManager().getCellLocation();
				table.add("Cell ID", mCell);
				table.add("Location Area Code", mLac);
				table.add("Primary Scrambling Code", mPsc);
			}
//		}
//		else table.add("", "Cell location unknown");
		section.add(table);
		add(section);
		
		section = new Section("Signal Strength");
		table = new TableSection();
		// SignalStrength info
//		if (mCellular.getSignalStrength() != null) {
			table.add("CDMA dBm", mCdmaDbm);
			table.add("CDMA Ec/Io", mCdmaEcio);
			table.add("EVDO dBm", mEvdoDbm);
			table.add("EVDO Ec/Io", mEvdoEcio);
			table.add("EVDO Signal to Noise Ratio", mEvdoSn);
			table.add("GSM Bit Error Rate", mGsmBitError);
			table.add("GSM Signal Strength", mGsmSignal);
			table.add("Is GSM", mIsGsm);			
//		}
//		else table.add("", "Signal strength unknown");
		section.add(table);
		add(section);
		
		
		
		mNeighboringCellSection = new Section("Neighboring Cells");		
//		table = new TableSection();
//		// NeighboringCellInfo info
//		List<NeighboringCellInfo> cells = mCellular.getTelephonyManager().getNeighboringCellInfo();
//		if (cells != null && cells.size() > 0) {
//			int i = 0;
//			for (NeighboringCellInfo info : cells) {
//				subsection = new Subsection("Neighboring Cell " + (i + 1));
//				table = new TableSection();
//				table.add("Cell ID", String.valueOf(info.getCid()));
//				table.add("Location Area Code", String.valueOf(info.getLac()));
//				table.add("Network Type", String.valueOf(info.getNetworkType()));
//				table.add("Primary Scrambling Code", String.valueOf(info.getPsc()));
//				table.add("Received Signal Strength Indication", String.valueOf(info.getRssi()));
//				subsection.add(table);
//				section.add(subsection);
//				++i;
//			}
//		}
//		else {
//			table.add("", "No known neighboring cells");
//			section.add(table);
//		}
		add(mNeighboringCellSection);
		
		
		section = new Section("Telephony");
		table = new TableSection();
		// Cellular info
		table.add("MCC", String.valueOf(mCellular.getMcc()));
		table.add("MNC", String.valueOf(mCellular.getMnc()));							
		table.add("Radio Version", Cellular.getRadioVersion());
		table.add("Baseband", Cellular.getBaseband());
		table.add("RIL Version", Cellular.getRilVersion());
			
		// TelephonyManager info
		table.add("Call State", mCallState);
		table.add("Data Activity", mDataActivity);
		table.add("Data State", mDataConnectionState);
		table.add("Device ID", mCellular.getTelephonyManager().getDeviceId());
		table.add("Device Software Version", mCellular.getTelephonyManager().getDeviceSoftwareVersion());
		table.add("Line 1 Number", mCellular.getTelephonyManager().getLine1Number());
		table.add("Network Country ISO", mCellular.getTelephonyManager().getNetworkCountryIso());
		table.add("Network Operator", mCellular.getTelephonyManager().getNetworkOperator());
		table.add("Network Operator Name", mCellular.getTelephonyManager().getNetworkOperatorName());
		table.add("Network Type", mNetworkType);
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
		table.add("Service State", mServiceState);
		
		//Unknown
		table.add("Call Forwarding", mCallForwarding);
		table.add("Message Waiting", mMessageWaiting);

		section.add(table);
		add(section);
		
		// Fetch current values
		onCallStateChanged(null);
		onCellLocationChanged();
		onDataActivity();
		onDataConnectionStateChanged();
		onServiceStateChanged();
		onSignalStrengthsChanged();
		
		mHeader.play();
	}

	@Override
	public Element getElement() {
		return mCellular;
	}

	@Override
	public void onCallForwardingIndicatorChanged(boolean cfi) {
		mCallForwarding.setText(String.valueOf(cfi));
	}

	@Override
	public void onCallStateChanged(String incomingNumber) {
		mCallState.setText(mCellular.getCallState());
		if (incomingNumber != null) mCallState.append(" (" + incomingNumber + ")");
	}

	@Override
	public void onCellLocationChanged() {
		Subsection subsection;
		TableSection table = new TableSection();
		
		if (mCellular.getTelephonyManager().getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
			CdmaCellLocation loc = (CdmaCellLocation) mCellular.getCellLocation();				
			mBaseStation.setText(String.valueOf(loc.getBaseStationId()));
			mBaseLat.setText(String.valueOf(loc.getBaseStationLatitude()));
			mBaseLon.setText(String.valueOf(loc.getBaseStationLongitude()));
			mNetwork.setText(String.valueOf(loc.getNetworkId()));
			mSystem.setText(String.valueOf(loc.getSystemId()));
		}
		else if (mCellular.getTelephonyManager().getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
			GsmCellLocation loc = (GsmCellLocation) mCellular.getTelephonyManager().getCellLocation();
			mCell.setText(String.valueOf(loc.getCid()));
			mLac.setText(String.valueOf(loc.getLac()));
			mPsc.setText(String.valueOf(loc.getPsc()));
		}
		
		// NeighboringCellInfo info
		mNeighboringCellSection.getContent().removeAllViews();
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
				mNeighboringCellSection.add(subsection);
				++i;
			}
			
		}
		else {
			table.add("", "No known neighboring cells");
			mNeighboringCellSection.add(table);
		}
		
	}

	@Override
	public void onDataActivity() {
		mDataActivity.setText(mCellular.getDataActivity());
	}

	@Override
	public void onDataConnectionStateChanged() {
		mDataConnectionState.setText(mCellular.getDataState());
		mNetworkType.setText(mCellular.getNetworkType());
	}

	@Override
	public void onMessageWaitingIndicatorChanged(boolean mwi) {
		mMessageWaiting.setText(String.valueOf(mwi));
	}

	@Override
	public void onServiceStateChanged() {
		 mServiceState.setText(mCellular.getServiceStateString());
	}

	@Override
	public void onSignalStrengthsChanged() {
		if (mCellular.getSignalStrength() != null) {
			mCdmaDbm.setText(String.valueOf(mCellular.getSignalStrength().getCdmaDbm()));
			mCdmaEcio.setText(String.valueOf(mCellular.getSignalStrength().getCdmaEcio()));
			mEvdoDbm.setText(String.valueOf(mCellular.getSignalStrength().getEvdoDbm()));
			mEvdoEcio.setText(String.valueOf(mCellular.getSignalStrength().getEvdoEcio()));
			mEvdoSn.setText(String.valueOf(mCellular.getSignalStrength().getEvdoSnr()));
			mGsmBitError.setText(String.valueOf(mCellular.getSignalStrength().getGsmBitErrorRate()));
			mGsmSignal.setText(String.valueOf(mCellular.getSignalStrength().getGsmSignalStrength()));
			mIsGsm.setText(String.valueOf(mCellular.getSignalStrength().isGsm()));			
		}
	}
	
}
