package com.jphilli85.deviceinfo.element.view;

import android.content.Context;
import android.location.Address;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.widget.TextView;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Location;
import com.jphilli85.deviceinfo.element.Location.ProviderWrapper;

public class LocationView extends ListeningElementView implements Location.GpsCallback, Location.ProviderCallback {
	private final Location mLocation;
	
	private final TextView
		mGpsStatusEvent, mGpsStatusMaxSatellites,
		mGpsStatusFirstFix, mNmea, 
		mBestProvider;
	
	private final TextView[] 
		mAccuracy, mPower, 
		mAddress, mStatus,
		mAltitude, mBearing,
		mLatitude, mLongitude,
		mSpeed, mEnabled;
		
	private final int mNumProviders;
	private final Section mSatellitesSection;
	
	public LocationView() {
		this(DeviceInfo.getContext());
	}
	
	protected LocationView(Context context) {
		super(context);
		mLocation = new Location(context);		
		mNumProviders = mLocation.getProviders().size();
		mLocation.setCallback(this);
		for (ProviderWrapper pw : mLocation.getProviders()) {
			pw.setCallback(this);
		}
		
		mSatellitesSection = new Section("Satellites");
		
		TableSection table = new TableSection();
		
		mGpsStatusEvent = table.getValueTextView();
		mGpsStatusMaxSatellites = table.getValueTextView();
		mGpsStatusFirstFix = table.getValueTextView();
		mNmea = table.getValueTextView();
		mBestProvider = table.getValueTextView();
		
		mAccuracy = new TextView[mNumProviders];
		mPower = new TextView[mNumProviders];
		mAddress = new TextView[mNumProviders];
		mStatus = new TextView[mNumProviders];
		mAltitude = new TextView[mNumProviders];
		mBearing = new TextView[mNumProviders];
		mLatitude = new TextView[mNumProviders];
		mLongitude = new TextView[mNumProviders];
		mSpeed = new TextView[mNumProviders];
		mEnabled = new TextView[mNumProviders];
		
		for (int i = 0; i < mNumProviders; ++i) {
			mAccuracy[i] = table.getValueTextView();
			mPower[i] = table.getValueTextView();
			mAddress[i] = table.getValueTextView();
			mStatus[i] = table.getValueTextView();
			mAltitude[i] = table.getValueTextView();
			mBearing[i] = table.getValueTextView();
			mLatitude[i] = table.getValueTextView();
			mLongitude[i] = table.getValueTextView();
			mSpeed[i] = table.getValueTextView();
			mEnabled[i] = table.getValueTextView();
		}
		
		table.add("Number of Providers", String.valueOf(mNumProviders));
		table.add("Best Provider", mBestProvider);
		add(table);
		
		ProviderWrapper pw;
		Section section = new Section("Providers");
		Subsection subsection;		
		for (int i = 0; i < mNumProviders; ++i) {
			pw = mLocation.getProviders().get(i);
			subsection = new Subsection("Provider " + (i + 1) 
					+ " (" + pw.getProviderString() + ")");
			table = new TableSection();			
			table.add("Enabled", mEnabled[i]);
			table.add("Accuracy", pw.getAccuracyString());
			table.add("Status", mStatus[i]);			
			table.add("Power", mPower[i]);
			table.add("Latitude", mLatitude[i]);
			table.add("Longitude", mLongitude[i]);
			table.add("Altitude (m)", mAltitude[i]);
			table.add("Speed (m/s)", mSpeed[i]);
			table.add("Bearing (°)", mBearing[i]);
			table.add("Accuracy (m)", mAccuracy[i]);
			table.add("Address", mAddress[i]);

			table.add("HasMonetaryCost", String.valueOf(pw.getProvider().hasMonetaryCost()));
			table.add("Requires Cellular", String.valueOf(pw.getProvider().requiresCell()));
			table.add("Requires Network", String.valueOf(pw.getProvider().requiresNetwork()));
			table.add("Requires Satellite", String.valueOf(pw.getProvider().requiresSatellite()));
			table.add("Supports Altitude", String.valueOf(pw.getProvider().supportsAltitude()));
			table.add("Supports Bearing", String.valueOf(pw.getProvider().supportsBearing()));
			table.add("Supports Speed", String.valueOf(pw.getProvider().supportsSpeed()));
			
			subsection.add(table);
			section.add(subsection);
		}
		
		add(section);
		add(mSatellitesSection);
		
		mHeader.play();
	}

	private int getProviderIndex(ProviderWrapper providerWrapper) {
		return mLocation.getProviders().indexOf(providerWrapper);
	}
	
	private void updateBestProvider() {
		ProviderWrapper best = mLocation.getBestProvider();
		mBestProvider.setText(best.getProviderString() 
				+ " (provider " + (getProviderIndex(best) + 1) + ")");
	}
	
	@Override
	public Element getElement() {
		return mLocation;
	}

	@Override
	public void onLocationChanged(ProviderWrapper providerWrapper) {
		int index = getProviderIndex(providerWrapper);		
		android.location.Location loc = providerWrapper.getLastLocation();
		if (loc != null) {
			mAccuracy[index].setText(String.valueOf(loc.getAccuracy()));
			mLatitude[index].setText(String.valueOf(loc.getLatitude()));
			mLongitude[index].setText(String.valueOf(loc.getLongitude()));		
			mAltitude[index].setText(String.valueOf(loc.getAltitude()));
			mBearing[index].setText(String.valueOf(loc.getBearing()));
			mSpeed[index].setText(String.valueOf(loc.getSpeed()));						
		}
		else {
			mAccuracy[index].setText(null);
			mLatitude[index].setText(null);
			mLongitude[index].setText(null);		
			mAltitude[index].setText(null);
			mBearing[index].setText(null);
			mSpeed[index].setText(null);
		}
		mPower[index].setText(providerWrapper.getPowerRequirementString());	
		updateBestProvider();
	}

	@Override
	public void onProviderDisabled(ProviderWrapper providerWrapper) {
		mEnabled[getProviderIndex(providerWrapper)].setText("false");
		updateBestProvider();
	}

	@Override
	public void onProviderEnabled(ProviderWrapper providerWrapper) {
		mEnabled[getProviderIndex(providerWrapper)].setText("true");
		updateBestProvider();
	}

	@Override
	public void onStatusChanged(ProviderWrapper providerWrapper) {
		mStatus[getProviderIndex(providerWrapper)].setText(providerWrapper.getLastStatusString());
	}

	@Override
	public void onAddressChanged(ProviderWrapper providerWrapper) {
		int index = getProviderIndex(providerWrapper);
		mAddress[index].setText("");
		Address address = providerWrapper.getLastAddress();
		if (address == null) return;		
		for (int i = 0; i < address.getMaxAddressLineIndex(); ++i) {
			mAddress[index].append(address.getAddressLine(i) + "\n");	
		}		
	}

	@Override
	public void onGpsStatusChanged(Location location) {
		mGpsStatusEvent.setText(mLocation.getLastGpsStatusEventString());
		GpsStatus status = mLocation.getGpsStatus();
		
		mSatellitesSection.getContent().removeAllViews();
		ListSection list = new ListSection();
		list.add("No Satellites Visible");
		
		if (status != null) {
			mGpsStatusFirstFix.setText(String.valueOf(status.getTimeToFirstFix()));
			mGpsStatusMaxSatellites.setText(String.valueOf(status.getMaxSatellites()));
			
			TableSection table = new TableSection();
			Iterable<GpsSatellite> sats = status.getSatellites();			
			if (sats != null) {		
				Subsection subsection;
				int count = 0; 
				int total = 0;
				for (GpsSatellite s : sats) ++total;
				table.add("Satellites Visible", String.valueOf(total));
				mSatellitesSection.add(table);
				for (GpsSatellite s : sats) {	
					++count;
					subsection = new Subsection("Satellite " + count);
					table = new TableSection();
					table.add("Azimuth (°)", String.valueOf(s.getAzimuth()));
					table.add("Elevation (°)", String.valueOf(s.getElevation()));
					table.add("PRN", String.valueOf(s.getPrn()));
					table.add("SNR", String.valueOf(s.getSnr()));
					table.add("hasAlmanac", String.valueOf(s.hasAlmanac()));
					table.add("hasEphemeris", String.valueOf(s.hasEphemeris()));
					table.add("usedInFix", String.valueOf(s.usedInFix()));
					subsection.add(table);
					mSatellitesSection.add(subsection);
				}				
			}
			else mSatellitesSection.add(list);
		}
		else {
			mGpsStatusFirstFix.setText(null);
			mGpsStatusMaxSatellites.setText(null);
			mSatellitesSection.add(list);
		}
	}

	@Override
	public void onNmeaReceived(Location location) {
		mNmea.setText(mLocation.getLastNmea());
	}

}
