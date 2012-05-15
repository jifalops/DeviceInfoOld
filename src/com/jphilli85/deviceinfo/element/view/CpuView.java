package com.jphilli85.deviceinfo.element.view;

import java.util.LinkedHashMap;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Cpu;


public class CpuView extends ListeningElementView implements Cpu.Callback {
	private Cpu mCpu;
	
	public CpuView() {
		this(DeviceInfo.getContext());
	}
	protected CpuView(Context context) {
		super(context);
		mCpu = new Cpu();
		
		TableSection table = new TableSection();
		
		LinkedHashMap<String, String> map = getElement().getContents();
		for (String key : map.keySet()) {
			table.add(key,  map.get(key));
		}
		
		add(table);
		
		mHeader.play();
	}

	@Override
	public Element getElement() {
		return mCpu;
	}
	@Override
	public void onPlay(PlayableSection section) {
		mCpu.startListening();
	}
	@Override
	public void onPause(PlayableSection section) {
		mCpu.stopListening();
	}
	@Override
	public void onUpdated() {
		// TODO Auto-generated method stub
		
	}

}
