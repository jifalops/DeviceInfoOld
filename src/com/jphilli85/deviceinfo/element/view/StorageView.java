package com.jphilli85.deviceinfo.element.view;

import java.util.List;

import android.content.Context;

import com.jphilli85.deviceinfo.app.DeviceInfo;
import com.jphilli85.deviceinfo.element.Element;
import com.jphilli85.deviceinfo.element.Storage;
import com.jphilli85.deviceinfo.element.Storage.Mount;
import com.jphilli85.deviceinfo.element.Storage.Partition;


public class StorageView extends ElementView {
	private Storage mStorage;

	public StorageView(Context context) {
		super(context);

	}
	
	private TableSection getMountTable(Mount m) {		
		TableSection table = new TableSection(getContext()) ;
		if (m == null) return table;
		table.add("Device", m.getDevice());
		table.add("Mount Point", m.getMountPoint());
		table.add("File System", m.getFileSystem());
		table.add("Attributes", m.getAttributesString());
		table.add("Block Size", String.valueOf(m.getBlockSize()));
		table.add("Block Count", String.valueOf(m.getBlockCount()));
		table.add("Total Size", String.valueOf(m.getTotalSize()));
		table.add("FreeS pace", String.valueOf(m.getFreeSpace()));
		table.add("Available Space", String.valueOf(m.getAvailableSpace()));
		return table;
	}
	
	private TableSection getPartitionTable(Partition p) {		
		TableSection table = new TableSection(getContext()) ;
		if (p == null) return table;
		table.add("Alias", p.getAlias());
		table.add("Name", p.getName());
		table.add("Num Blocks", String.valueOf(p.getNumBlocks()));
		table.add("Block Size", String.valueOf(p.getBlockSize()));
		table.add("Total Size", String.valueOf(p.getTotalSize()));
		table.add("Device", p.getDevice());
		table.add("Device Major", String.valueOf(p.getDeviceMajor()));
		table.add("Device Minor", String.valueOf(p.getDeviceMinor()));
		return table;
	}

	@Override
	public Element getElement() {		 
		return mStorage;
	}

	@Override
	protected void initialize(Context context) {
		mStorage = new Storage(context);
		
		Subsection subsection2;
		
		Section section = new Section(getContext(), "Mounts");		
		Subsection subsection = new Subsection(getContext(), "Data");
		TableSection table = getMountTable(mStorage.getDataMount());
		
		subsection.add(table);
		section.add(subsection);		
		
		subsection = new Subsection(getContext(), "System");
		table = getMountTable(mStorage.getSystemMount());
		
		subsection.add(table);
		section.add(subsection);
		
		subsection = new Subsection(getContext(), "Cache");
		table = getMountTable(mStorage.getCacheMount());
		
		subsection.add(table);
		section.add(subsection);
		
		subsection = new Subsection(getContext(), "SD Card(s)");
		List<Mount> mounts = mStorage.getSdcardMounts();
		if (mounts == null || mounts.isEmpty()) {
			table = new TableSection(getContext()) ;
			table.add(null, "No SD card mounts found");			
			subsection.add(table);			
		}
		else {		
			int i = 0;
			for (Mount m : mounts) {
				subsection2 = new Subsection(getContext(), "SD card mount " + (i + 1));
				table = getMountTable(m);
				
				subsection2.add(table);
				subsection.add(subsection2);
				
				++i;
			}			
		}
		section.add(subsection);			
		
		subsection = new Subsection(getContext(), "All");
		int i = 0;
		for (Mount m : mStorage.getMounts()) {
			subsection2 = new Subsection(getContext(), "Mount " + (i + 1));
			table = getMountTable(m);
			subsection2.add(table);
			subsection.add(subsection2);
			++i;
		}
		
		section.add(subsection);
		add(section);
		
		
		section = new Section(getContext(), "Partitions");
		subsection = new Subsection(getContext(), "Named");
		List<Partition> partitions = mStorage.getAliasedPartitions(); 
		if (partitions == null || partitions.isEmpty()) {
			table = new TableSection(getContext()) ;
			table.add(null, "No named partitions found");			
			subsection.add(table);
		}
		else {
			i = 0;
			for (Partition p : partitions) {
				subsection2 = new Subsection(getContext(), "Named Partition " + (i + 1));
				table = getPartitionTable(p);				
				subsection2.add(table);
				subsection.add(subsection2);
				++i;
			}
		}
		section.add(subsection);
		
		subsection = new Subsection(getContext(), "All");
		i = 0;
		for (Partition p : mStorage.getPartitions()) {
			subsection2 = new Subsection(getContext(), "Partition " + (i + 1));
			table = getPartitionTable(p);
			subsection2.add(table);
			subsection.add(subsection2);
			++i;
		}
		section.add(subsection);
		
		add(section);
	}
}
