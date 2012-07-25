package com.jphilli85.deviceinfo.element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Context;
import android.os.StatFs;
import android.util.Log;

import com.jphilli85.deviceinfo.ShellHelper;

public class Storage extends Element {
	private static final String LOG_TAG = Storage.class.getSimpleName();
	
	private List<Mount> mMounts;
	private List<Partition> mPartitions;
	
	// TODO use singleton
	
	//TODO android.os.Environment
	public Storage(Context context) {
		super(context);
		mMounts = new ArrayList<Mount>();
		mPartitions = new ArrayList<Partition>();
		if (!updateMounts()) 
			Log.e(LOG_TAG, "Error updating mounts.");
		if (!updatePartitions()) 
			Log.e(LOG_TAG, "Error updating partitions.");		
	}
	
	public class Partition implements ContentsMapper {
		// TODO look up block size
		/** It *seems* that the partition block sizes are 
		 * (almost) always 1024. Frequently enough to count
		 * on it for now =[
		 */
		public static final int BLOCK_SIZE = 1024;
		private int mMajor;
		private int mMinor;
		private int mBlocks;
		private String mName;
		private String mAlias;
		
		private long mTotalSize;
		
		private String mDevice;
		
		public Partition(String desc) {
			if (desc == null || desc.length() == 0) {
				Log.e(LOG_TAG, "Error creating Partition instance.");
				return;
			}
			
			String[] parts = desc.split("\\s+");
			
			if (parts.length < 4) {
				Log.e(LOG_TAG, "Error creating Partition instance. Unrecognized format.");
			}
			
			try {
				mMajor = Integer.valueOf(parts[0]);
				mMinor = Integer.valueOf(parts[1]);
				mBlocks = Integer.valueOf(parts[2]);
				mName = parts[3];
				mAlias = parts[4]; // optional
			}
			catch (IndexOutOfBoundsException ignored) {}
			catch (NumberFormatException ignored) {}
			catch (NullPointerException ignored) {}
			
			mTotalSize = ((long) mBlocks) * ((long) BLOCK_SIZE);
			
			mDevice = getDevice(mMajor);
		}
		
		private String getDevice(int major) {
			List<String> devices = ShellHelper.getProc("devices");
	        if (devices == null || devices.isEmpty()) return null;
	        // return *last* match (to match in "Block devices:")
	        String device = null;	   
	        String[] parts;
	        for (String s : devices) {	        	
	        	if (s == null || s.length() == 0) continue;
	        	parts = s.split("\\s+");
	        	try {
	        		if (Integer.valueOf(parts[0]) == major) {
	        			device = parts[1];
	        		}
	        	} catch (NumberFormatException ignored) {}
	        }
	        return device;
		}
		
		public String getDevice() {
			return mDevice;
		}
		
		public int getDeviceMajor() {
			return mMajor;
		}
		
		public int getDeviceMinor() {
			return mMinor;
		}
		
		public int getNumBlocks() {
			return mBlocks;
		}
		
		public int getBlockSize() {
			return BLOCK_SIZE;
		}
		
		public long getTotalSize() {
			return mTotalSize;
		}
		
		public String getName() {
			return mName;
		}
		
		public String getAlias() {
			return mAlias;
		}
		
		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("Alias", getAlias());
			contents.put("Name", getName());
			contents.put("Num Blocks", String.valueOf(getNumBlocks()));
			contents.put("Block Size", String.valueOf(getBlockSize()));
			contents.put("Total Size", String.valueOf(getTotalSize()));
			contents.put("Device", getDevice());
			contents.put("Device Major", String.valueOf(getDeviceMajor()));
			contents.put("Device Minor", String.valueOf(getDeviceMinor()));
			
			return contents;
		}
		
	}
	
	public class Mount implements ContentsMapper {
		private static final int NO_STATFS = -1;
		
		private String mDevice;
		private String mMountPoint;
		private String mFileSystem;
		private String mAttributesString;
		private String[] mAttributes;
		
		private StatFs mStatFs;
		private int mBlockSize;
		private int mBlockCount;
		private long mTotalSize;
		
		public Mount(String desc) {
			if (desc == null || desc.length() == 0) {
				Log.e(LOG_TAG, "Error creating Mount instance.");
				return;
			}
			
			String[] parts = desc.split("\\s+");
			
			if (parts.length != 6) {
				Log.e(LOG_TAG, "Error creating Mount instance. Unrecognized format.");
			}
			
			try {
				mDevice = parts[0];
				mMountPoint = parts[1];
				mFileSystem = parts[2];
				mAttributesString = parts[3];
				mAttributes = parts[3].split(",");
			}
			catch (IndexOutOfBoundsException ignored) {}
			catch (NullPointerException ignored) {}
			
			// Initialize to -1 to represent not being able to
			// use a StatFs on this mount.
			mStatFs = null;
			mBlockSize = NO_STATFS; 
			mBlockCount = NO_STATFS;
			mTotalSize = NO_STATFS;
			try { 
				mStatFs = new StatFs(mMountPoint);
				mBlockSize = mStatFs.getBlockSize();
				mBlockCount = mStatFs.getBlockCount();
				mTotalSize = (long) mBlockSize * (long) mBlockCount;
			} catch (Exception ignored) {}
		}
		
		public boolean hasAttribute(String attr) {
			if (attr == null || attr.length() == 0)
			for (String s : mAttributes) {
				if (attr.equals(s)) return true;
			}
			return false;
		}
		
		public StatFs getStatFs() {
			return mStatFs; 
		}
		
		public int getBlockSize() {
			return mBlockSize;
		}
		
		public int getBlockCount() {
			return mBlockCount;
		}
		
	    public long getTotalSize() { 
	    	return mTotalSize;
	    } 
	    
	    public long getFreeSpace() {
	    	if (mStatFs == null) return NO_STATFS;
	    	return ((long) mBlockSize) * ((long) mStatFs.getFreeBlocks());
	    }
	    
	    public long getAvailableSpace() {
	    	if (mStatFs == null) return NO_STATFS;
	    	return ((long) mBlockSize) * ((long) mStatFs.getAvailableBlocks());
	    }

		public boolean isReadOnly() {
			return hasAttribute("ro");
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
		
		public String[] getAttributes() {
			return mAttributes;
		}
		
		public String getAttributesString() {
			return mAttributesString;
		}

		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("Device", getDevice());
			contents.put("MountPoint", getMountPoint());
			contents.put("FileSystem", getFileSystem());
			contents.put("Attributes", getAttributesString());
			contents.put("BlockSize", String.valueOf(getBlockSize()));
			contents.put("BlockCount", String.valueOf(getBlockCount()));
			contents.put("TotalSize", String.valueOf(getTotalSize()));
			contents.put("FreeSpace", String.valueOf(getFreeSpace()));
			contents.put("AvailableSpace", String.valueOf(getAvailableSpace()));
			
			return contents;
		}
	}
	
	
	/** Get the current mounts from /proc */
	public boolean updateMounts() {
        List<String> mounts = ShellHelper.getProc("mounts");
        if (mounts == null || mounts.isEmpty()) return false;
        mMounts.clear();
        for (String s : mounts) {
        	if (s == null || s.length() == 0) continue;
        	mMounts.add(new Mount(s));
        }
        return !mMounts.isEmpty();
    }
	
	private boolean updatePartitions() {
        List<String> partitions = ShellHelper.getProc("partitions");
        if (partitions == null || partitions.isEmpty()) return false;
        mPartitions.clear();
        boolean first = true;
        for (String s : partitions) {
        	// Skip the column headers
        	if (first) {
        		first = false;
        		continue;
        	}
        	if (s == null || s.length() == 0) continue;
        	mPartitions.add(new Partition(s));
        }
        return !mPartitions.isEmpty();
    }
	
	public List<Mount> getMounts() {
		return mMounts;
	}
	
	public Mount getMountByPath(String mountPoint) {
        if (mountPoint == null || mountPoint.length() == 0) return null;
		for (Mount m : mMounts) {
        	if (mountPoint.equals(m.getMountPoint())) {
        		return m;
        	}
        }
        return null;
    }
	
	public List<Mount> findMountsByPath(String regex) {
		if (regex == null || regex.length() == 0) return null;
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
	
	public List<Partition> getPartitions() {
		return mPartitions;
	}
	
	public List<Partition> getAliasedPartitions() {
		List<Partition> list = new ArrayList<Partition>();
		for (Partition p : mPartitions) {
        	if (p.getAlias() != null) {
        		list.add(p);
        	}
        }
        return list;
    }
	
	public Partition getPartitionByAlias(String alias) {
        if (alias == null || alias.length() == 0) return null;
		for (Partition p : mPartitions) {
        	if (alias.equals(p.getAlias())) {
        		return p;
        	}
        }
        return null;
    }
	
	public Partition getBootPartition() {
		return getPartitionByAlias("boot");
	}
	
	public Partition getRecoveryPartition() {
		return getPartitionByAlias("recovery");
	}
	
	// TODO ui facing strings
	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
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
		
		// All mounts
		LinkedHashMap<String, String> subcontents;
		for (int i = 0, len = mMounts.size(); i < len; ++i) {
			subcontents = mMounts.get(i).getContents();
			for (String s : subcontents.keySet()) {
				contents.put("Mount " + i + " " + s, subcontents.get(s));				
			}
		}
		
		// Interesting partitions
		contents.put("Boot Partition index", String.valueOf(mPartitions.indexOf(getBootPartition())));
		contents.put("Recovery Partition index", String.valueOf(mPartitions.indexOf(getRecoveryPartition())));
		
		// All partitions
		for (int i = 0, len = mPartitions.size(); i < len; ++i) {
			subcontents = mPartitions.get(i).getContents();
			for (String s : subcontents.keySet()) {
				contents.put("Partition " + i + " " + s, subcontents.get(s));				
			}
		}
		
		// Aliased partitions
		List<Partition> aliased = getAliasedPartitions();
		for (int i = 0, len = aliased.size(); i < len; ++i) {
			contents.put("Aliased Partition " + i + " index", 
					String.valueOf(mPartitions.indexOf(aliased.get(i)))
					+ " (" + aliased.get(i).getAlias() + ")");
		}
		
		return contents;
	}
}
