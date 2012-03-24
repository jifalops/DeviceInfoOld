package com.jphilli85.deviceinfo.subgroup;

import java.util.ArrayList;
import java.util.List;

import android.os.StatFs;

public class Storage extends Subgroup {
	private static final String LOG_TAG = Storage.class.getSimpleName();
	
	private List<Mount> mMounts;
	
	// TODO use singleton
	public Storage() {
		mMounts = new ArrayList<Mount>();
		updateMounts();
	}
	
	
	public class Mount {
		private String mDevice;
		private String mMountPoint;
		private String mFileSystem;
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
	}
	
	
	/** Get the current mounts from /proc */
	public boolean updateMounts() {
        List<String> mounts = getProc("mounts");
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
	
	public Mount getRootFsMount() {
		return getMountByPath("/");
	}
}
