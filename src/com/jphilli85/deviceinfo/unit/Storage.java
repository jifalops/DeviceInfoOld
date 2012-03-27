package com.jphilli85.deviceinfo.unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.StatFs;

import com.jphilli85.deviceinfo.ShellHelper;

public class Storage extends Unit {
	private static final String LOG_TAG = Storage.class.getSimpleName();
	
	private List<Mount> mMounts;
	
	// TODO use singleton
	public Storage() {
		mMounts = new ArrayList<Mount>();
		if (!updateMounts()) 
			throw new RuntimeException("Error updating mounts.");
	}
	
	
	public class Mount {
		private String mDevice;
		private String mMountPoint;
		private String mFileSystem;
		private String mOptionsString;
		private String[] mOptions;
		
		private StatFs mStatFs;
		
		public Mount(String desc) {
			if (desc == null || desc.isEmpty()) {
				throw new RuntimeException("Error creating Mount instance.");
			}
			String[] parts = desc.split(" ");
			if (parts.length != 6) {
				throw new RuntimeException("Error creating Mount instance. Unrecognized format.");
			}
			mDevice = parts[0];
			mMountPoint = parts[1];
			mFileSystem = parts[2];
			mOptionsString = parts[3];
			mOptions = parts[3].split(",");
		}
		
		public boolean hasOption(String option) {
			for (String s : mOptions) {
				if (option.equals(s)) return true;
			}
			return false;
		}
		
		public StatFs getStatFs() {
			if (mStatFs == null) {
				mStatFs = new StatFs(mMountPoint); 
			}
			return mStatFs; 
		}
		
	    public long getTotalSize() { 
	    	return ((long) getStatFs().getBlockSize()) * ((long) getStatFs().getBlockCount());
	    } 
	    
	    public long getFreeSpace() {
	    	return ((long) getStatFs().getBlockSize()) * ((long) getStatFs().getFreeBlocks());
	    }
	    
	    public long getAvailableSpace() {
	    	return ((long) getStatFs().getBlockSize()) * ((long) getStatFs().getAvailableBlocks());
	    }

		public boolean isReadOnly() {
			return hasOption("ro");
		}
		
		public String getDevice() {
			return mDevice;
		}
		
		public String getMountPoint() {
			return mMountPoint;
		}
		
		public String getFileSystem() {
			return mFileSystem;
		}
		
		public String[] getOptions() {
			return mOptions;
		}
		
		public String getOptionsString() {
			return mOptionsString;
		}
	}
	
	
	/** Get the current mounts from /proc */
	public boolean updateMounts() {
        List<String> mounts = ShellHelper.getProc("mounts");
        if (mounts == null || mounts.isEmpty()) return false;
        mMounts.clear();
        for (String s : mounts) {
        	if (s == null || s.isEmpty()) continue;
        	mMounts.add(new Mount(s));
        }
        return !mMounts.isEmpty();
    }
	
	public List<Mount> getMounts() {
		return mMounts;
	}
	
	public Mount getMountByPath(String mountPoint) {
        if (mountPoint == null || mountPoint.isEmpty()) return null;
		for (Mount m : mMounts) {
        	if (mountPoint.equals(m.getMountPoint())) {
        		return m;
        	}
        }
        return null;
    }
	
	public List<Mount> findMountsByPath(String regex) {
		if (regex == null || regex.isEmpty()) return null;
		List<Mount> matches = new ArrayList<Mount>();
		for (Mount m : mMounts) {
			if (m.getMountPoint().matches(regex)) {
				matches.add(m);
			}
		}
		return matches;
	}
	
	public List<Mount> getSdcardMounts() {
		return findMountsByPath(".*sd[^/]*");
	}
	
	public Mount getSystemMount() {
		return getMountByPath("/system");
	}
	
	public Mount getDataMount() {
		return getMountByPath("/data");
	}
	
	public Mount getCacheMount() {
		return getMountByPath("/cache");
	}
	
	public Mount getRootMount() {
		return getMountByPath("/");
	}
	
	// TODO ui facing strings
	@Override
	public Map<String, String> getContents() {
		Map<String, String> contents = new HashMap<String, String>();
		
		// All mount data
		Mount m = null;
		for (int i = 0, len = mMounts.size(); i < len; ++i) {
			m = mMounts.get(i);
			contents.put("Mount " + i + " Device", m.getDevice());
			contents.put("Mount " + i + " MountPoint", m.getMountPoint());
			contents.put("Mount " + i + " FileSystem", m.getFileSystem());
			contents.put("Mount " + i + " Options", m.getOptionsString());
			contents.put("Mount " + i + " TotalSize", String.valueOf(m.getTotalSize()));
			contents.put("Mount " + i + " FreeSpace", String.valueOf(m.getFreeSpace()));
			contents.put("Mount " + i + " AvailableSpace", String.valueOf(m.getAvailableSpace()));
		}
		
		// Interesting mounts
		contents.put("System Mount index", String.valueOf(mMounts.indexOf(getSystemMount())));
		contents.put("Data Mount index", String.valueOf(mMounts.indexOf(getDataMount())));
		contents.put("Cache Mount index", String.valueOf(mMounts.indexOf(getCacheMount())));
		contents.put("Root Mount index", String.valueOf(mMounts.indexOf(getRootMount())));		
		List<Mount> sdMounts = getSdcardMounts();
		for (int i = 0; i < sdMounts.size(); ++i) {				
			contents.put("SD card Mount " + i + " index", 
					String.valueOf(mMounts.indexOf(sdMounts.get(i))));			
		}
		
		return contents;
	}
}
