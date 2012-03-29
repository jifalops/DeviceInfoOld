package com.jphilli85.deviceinfo.unit;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;

import com.jphilli85.deviceinfo.DeviceInfo;
import com.jphilli85.deviceinfo.ShellHelper;

//TODO exact current frequency???
public class Cpu extends Unit {
	private List<String> mCpuinfo;
	private List<LogicalCpu> mLogicalCpus;
	private CpuStat mCpuStat;
	
	public Cpu() {		
		mCpuinfo = ShellHelper.getProc("cpuinfo");
		mLogicalCpus = new ArrayList<LogicalCpu>();
		mCpuStat = new CpuStat();
		
		File f = null;
		int i = 0;
		while (true) {
			f = new File("/sys/devices/system/cpu/cpu" + i);
			if (f.exists()) mLogicalCpus.add(new LogicalCpu(f, i));
			else break;
			++i;
		}
		updateCpuStats();
	}	
	
	public List<String> getCpuinfo() {
		return mCpuinfo;
	}
	
	public CpuStat getCpuStat() {
		return mCpuStat;
	}
	
	public List<LogicalCpu> getLogicalCpus() {
		return mLogicalCpus;
	}
	
	/** Updates the CpuStat for this and all logical CPUs. */
	public int updateCpuStats() {
		List<String> stats = ShellHelper.getProc("stat");
		if (stats == null || stats.isEmpty()) {
			return 0;
		}
		long timestamp = DeviceInfo.getTimestamp();
		String[] parts = null;
		String line = null;
		int updated = 0;
		for (int i = 0; i < stats.size(); ++i) {
			line = stats.get(i);
			if (line.startsWith("cpu")) {
				parts = line.split("\\s+");				
				if (parts[0].endsWith(String.valueOf(i - 1))) {
					if (mLogicalCpus.size() >= i &&
							mLogicalCpus.get(i - 1).getCpuStat().update(parts, timestamp)) {					
						++updated;
					}
				}
				else if (mCpuStat.update(parts, timestamp)) {
					++updated;
				}
			}
		}
		return updated;
	}
	
	
	
	private class LogicalCpu {
		private final String LOG_TAG = LogicalCpu.class.getSimpleName();
		
//		public final float bogoMips;
		private int mId;
		private File mRoot;
		private CpuStat mCpuStat;
		
		/** A file pointing to a logical cpu structure
		 * in the file system, such as /sys/devices/system/cpu/cpu0. */
		public LogicalCpu(File file, int id) {	
			if (mLogicalCpus.size() > id) {
				throw new AssertionError("Logical CPU with id " + id + " already exists!");
			}
			
			mRoot = file;
			mId = id;
			mCpuStat = new CpuStat(id);
		}
		
		public CpuStat getCpuStat() {
			return mCpuStat;
		}
		
		/** Get the id of this logical cpu. */
		public int getId() {
			return mId;
		}
		
		public File getRoot() {
			return mRoot;
		}
		
		/** Get the maximum frequency in MHz */
		public int getMaxFrequency() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/cpuinfo_max_freq");
			if (list == null || list.isEmpty()) return 0;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			return  value / 1000;
		}
		
		/** Get the minimum frequency in MHz */
		public int getMinFrequency() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/cpuinfo_min_freq");
			if (list == null || list.isEmpty()) return 0;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			return  value / 1000;
		}
		
		/** Get the current frequency in MHz */
		public int getFrequency() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/scaling_cur_freq");
			if (list == null || list.isEmpty()) return 0;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			return  value / 1000;
		}
		
		/** Get the available frequencies in MHz */
		public int[] getAvailableFrequencies() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/scaling_available_frequencies");
			if (list == null || list.isEmpty()) return null;
			String[] results = list.get(0).split("\\s");
			if (results == null || results.length == 0) {
				return null;
			}
			int len = results.length;
			int[] freqs = new int[len];
			for (int i = 0; i < len; ++i) {
				int value = 0;
				try { value = Integer.valueOf(results[i]); }
				catch (NumberFormatException ignored) {}
				freqs[i] = value / 1000;
			}
			return freqs;
		}
		
		/** Get the available governors */
		public String[] getAvailableGovernors() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/scaling_available_governors");
			if (list == null || list.isEmpty()) return null;
			return list.get(0).split("\\s");
		}
		
		/** Get the current governor */
		public String getGovernor() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/scaling_governor");
			if (list == null || list.isEmpty()) return null;
			return list.get(0);
		}
		
		/** Get the current driver */
		public String getDriver() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/scaling_driver");
			if (list == null || list.isEmpty()) return null;
			return list.get(0);
		}
		
		/** Get the frequency transition latency in nano-seconds */
		public int getTransitionLatency() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/cpuinfo_transition_latency");
			if (list == null || list.isEmpty()) return 0;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			return  value;			
		}
		
		/** Get the total number of frequency transitions */
		public int getTotalTransitions() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/stats/total_trans");
			if (list == null || list.isEmpty()) return 0;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			return  value;			
		}
		
		/** Get the total amount of time spent in frequency transitions in seconds */
		public float getTimeInTransitions() {
			return (float) ((long) getTotalTransitions() * (long) getTransitionLatency() / 1E9);
		}

		/** Get a list of the total time (in Jiffies) spent at each frequency (in MHz) */
		public int[][] getTimeInFrequency() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/stats/time_in_state");
			if (list == null || list.isEmpty()) return null;
			int len = list.size();
			int[][] times = new int[len][2];
			String[] parts = null;
			for (int i = 0; i < len; ++i) {
				parts = list.get(i).split("\\s");
				if (parts.length != 2) {
					Log.d(LOG_TAG, "time in state did not have exactly 2 parts.");
					continue;
				}
				int freq = 0, time = 0;
				try { 
					freq = Integer.valueOf(parts[0]) / 1000;
					time = Integer.valueOf(parts[1]);
				}
				catch (NumberFormatException ignored) {}
				times[i][0] = freq;
				times[i][1] = time;
			}
			return times;
		}
		
		/** Get the total time (in Jiffies) spent a frequency given in MHz. */
		public int getTimeInFrequency(int frequency) {
			int[][] times = getTimeInFrequency();
			if (times == null || times.length == 0) return 0;
			for (int[] f : times) {
				if (f[0] == frequency) return f[1];
			}
			return 0;
		}
		
		/** Get a list of the percentage of time spent at each frequency (in MHz) */
		public Map<Integer, Float> getPercentInFrequency() {
			Map<Integer, Float> percents = new LinkedHashMap<Integer, Float>();
			
			int[][] times = getTimeInFrequency();
			if (times == null || times.length == 0) return null;
			
			long total = 0;
			
			for (int i = 0; i < times.length; ++i) {
				total += times[i][1];
			}
			
			if (total == 0) return null;
			
			for (int i = 0; i < times.length; ++i) {
				percents.put(times[i][0], (float) times[i][1] / total * 100);				
			}
			
			return percents;
		}
		
		/** Get the percentage of time spent a frequency given in MHz. */
		public float getPercentInFrequency(int frequency) {
			Map<Integer, Float> percents = getPercentInFrequency();
			if (percents == null || percents.size() == 0) return 0;

			for (int f : percents.keySet()) {
				if (f == frequency) return percents.get(f);
			}
			return 0;
		}
	}
	
	private class CpuStat {
		private final String LOG_TAG = CpuStat.class.getSimpleName();
		
		public final int OVERALL_ID = -1;
		
		private int mId;
		
		private long mTimestamp = 0;
		private long mUser = 0;
		private long mNice = 0;
		private long mSystem = 0;
		private long mIdle = 0;
		private long mIoWait = 0;
		private long mIntr = 0;
		private long mSoftIrq = 0;		
		// The other two fields, Steal and Guest seem to always be zero.
		
		private long mTimestampPrevious = 0;
		private long mUserPrevious = 0;
		private long mNicePrevious = 0;
		private long mSystemPrevious = 0;
		private long mIdlePrevious = 0;
		private long mIoWaitPrevious = 0;
		private long mIntrPrevious = 0;
		private long mSoftIrqPrevious = 0;
		
		public CpuStat() {
			// The overall cpu stat.
			mId = OVERALL_ID;
		}
		
		public CpuStat(int id) {
			// Cpu stat for a particular cpu.
			mId = id;
		}
		
		/** 
		 * Update the cpu stats for this cpu.
		 * @param parts
		 * 		An array with at least 8 elements:<br>
		 * 	cpu[id] user nice system idle io_wait intr soft_irq<br>
		 * The id for cpu is optional.
		 * @return whether the update completed successfully.
		 */
		private boolean update(String[] parts, long timestamp) {		
			if (parts == null || parts.length < 8) {
				Log.d(LOG_TAG, "invalid array length to perform update.");
				return false;
			}
			
			String value = "cpu";			
			if (mId != OVERALL_ID) value += mId;
			
			if (!parts[0].equals(value)) {
				Log.d(LOG_TAG, "Tried to perform update on wrong CpuStat. Got '" 
						+ parts[0] + "', expected '" + value + "'.");
				return false;
			}
			
			long[] values = new long[7];
			
			try {				
				for (int i = 0; i < 7; ++i) {
					values[i] = Long.parseLong(parts[i + 1]);
				}				
			} 
			catch (NumberFormatException ignored) {
				return false;
			}
			
			mTimestampPrevious = mTimestamp;
			mUserPrevious = mUser;
			mNicePrevious = mNice;
			mSystemPrevious = mSystem;
			mIdlePrevious = mIdle;
			mIoWaitPrevious = mIoWait;
			mIntrPrevious = mIntr;
			mSoftIrqPrevious = mSoftIrq;
			
			mTimestamp = timestamp;
			mUser = values[0];
			mNice = values[1];
			mSystem = values[2];
			mIdle = values[3];
			mIoWait = values[4];
			mIntr = values[5];
			mSoftIrq = values[6];
			
			return true;
		}
		
		public int getId() {
			return mId;
		}
		
		public float getUserPercent() {
			float divisor = mUserPrevious;
			if (divisor == 0) return 0;
			return (mUser - mUserPrevious) / divisor  * 100;
		}
		
		public float getNicePercent() {
			float divisor = mNicePrevious;
			if (divisor == 0) return 0;
			return (mNice - mNicePrevious) / divisor * 100;
		}
		
		public float getSystemPercent() {
			float divisor = mSystemPrevious;
			if (divisor == 0) return 0;
			return (mSystem - mSystemPrevious) / divisor * 100;
		}
		
		public float getIdlePercent() {
			float divisor = mIdlePrevious;
			if (divisor == 0) return 0;
			return (mIdle - mIdlePrevious) / divisor * 100;
		}
		
		public float getIoWaitPercent() {
			float divisor = mIoWaitPrevious;
			if (divisor == 0) return 0;
			return (mIoWait - mIoWaitPrevious) / divisor * 100;
		}
		
		public float getIntrPercent() {
			float divisor = mIntrPrevious;
			if (divisor == 0) return 0;
			return (mIntr - mIntrPrevious) / divisor * 100;
		}
		
		public float getSoftIrqPercent() {
			float divisor = mSoftIrqPrevious;
			if (divisor == 0) return 0;
			return (mSoftIrq - mSoftIrqPrevious) / divisor * 100;
		}
		
		/** User + Nice */
		public float getUserTotalPercent() {
			float divisor = mUserPrevious + mNicePrevious;
			if (divisor == 0) return 0;
			return ((mUser - mUserPrevious) + (mNice - mNicePrevious))
				/ divisor * 100;
		}
		
		/** System + Intr + SoftIrq */
		public float getSystemTotalPercent() {
			float divisor = mSystemPrevious + mIntrPrevious + mSoftIrqPrevious;
			if (divisor == 0) return 0;
			return ((mSystem - mSystemPrevious) 
				+ (mIntr - mIntrPrevious)
				+ (mSoftIrq - mSoftIrqPrevious))
				/ divisor * 100;
		}
		
		/** Idle + IoWait */
		public float getIdleTotalPercent() {
			float divisor = mIdlePrevious + mIoWaitPrevious;
			if (divisor == 0) return 0;
			return ((mIdle - mIdlePrevious) + (mIoWait - mIoWaitPrevious))
					/ divisor * 100;
		}
		
		public float getTotalPercent() {
			float divisor = mUserPrevious
					+ mNicePrevious
					+ mSystemPrevious
					+ mIdlePrevious
					+ mIoWaitPrevious
					+ mIntrPrevious 
					+ mSoftIrqPrevious;
			if (divisor == 0) return 0;
			return ((mUser - mUserPrevious)
				+ (mNice - mNicePrevious)
				+ (mSystem - mSystemPrevious) 
				+ (mIdle - mIdlePrevious)
				+ (mIoWait - mIoWaitPrevious)
				+ (mIntr - mIntrPrevious)				
				+ (mSoftIrq - mSoftIrqPrevious))
				/ divisor * 100;
		}
		
		
		
		/** Timestamp in milliseconds */
		public long getTimestamp() {
			return mTimestamp;
		}
		
		public long getUser() {
			return mUser;
		}
		
		public long getNice() {
			return mNice;
		}
		
		public long getSystem() {
			return mSystem;
		}
		
		public long getIdle() {
			return mIdle;
		}
		
		public long getIoWait() {
			return mIoWait;
		}
		
		public long getIntr() {
			return mIntr;
		}
		
		public long getSoftIrq() {
			return mSoftIrq;
		}
		
		/** User + Nice */
		public long getUserTotal() {
			return mUser + mNice;
		}
		
		/** System + Intr + SoftIrq */
		public long getSystemTotal() {
			return mSystem + mIntr + mSoftIrq;
		}
		
		/** Idle + IoWait */
		public long getIdleTotal() {
			return mIdle + mIoWait;
		}
		
		public long getTotal() {
			return mUser + mNice + mSystem
				+ mIdle + mIoWait + mIntr + mSoftIrq;
		}
		
		
		
		
		public long getTimestampPrevious() {
			return mTimestampPrevious;
		}
		
		public long getUserPrevious() {
			return mUserPrevious;
		}
		
		public long getNicePrevious() {
			return mNicePrevious;
		}
		
		public long getSystemPrevious() {
			return mSystemPrevious;
		}
		
		public long getIdlePrevious() {
			return mIdlePrevious;
		}
		
		public long getIoWaitPrevious() {
			return mIoWaitPrevious;
		}
		
		public long getIntrPrevious() {
			return mIntrPrevious;
		}
		
		public long getSoftIrqPrevious() {
			return mSoftIrqPrevious;
		}
		
		/** User + Nice */
		public long getUserTotalPrevious() {
			return mUserPrevious + mNicePrevious;
		}
		
		/** System + Intr + SoftIrq */
		public long getSystemTotalPrevious() {
			return mSystemPrevious + mIntrPrevious + mSoftIrqPrevious;
		}
		
		/** Idle + IoWait */
		public long getIdleTotalPrevious() {
			return mIdlePrevious + mIoWaitPrevious;
		}
		
		public long getTotalPrevious() {
			return mUserPrevious + mNicePrevious + mSystemPrevious
				+ mIdlePrevious + mIoWaitPrevious + mIntrPrevious + mSoftIrqPrevious;
		}
		
		
	}


	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		for (int i = 0; i < mCpuinfo.size(); ++i) {
			contents.put("CPU Info " + i, mCpuinfo.get(i));
		}
		
		contents.putAll(getCpuStatContents(mCpuStat, "Overall"));
		
		for (LogicalCpu logicalCpu : mLogicalCpus) {
			contents.putAll(getLogicalCpuContents(logicalCpu));
		}
		
		return contents; 
	}
	
	private LinkedHashMap<String, String> getLogicalCpuContents(LogicalCpu cpu) {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		String key = "LogicalCpu " + cpu.getId();
		
		contents.put(key + " ID", String.valueOf(cpu.getId()));
		contents.put(key + " Root", cpu.getRoot().getAbsolutePath());
		contents.put(key + " Frequency (MHz)", String.valueOf(cpu.getFrequency()));
		contents.put(key + " MinFrequency (MHz)", String.valueOf(cpu.getMinFrequency()));
		contents.put(key + " MaxFrequency (MHz)", String.valueOf(cpu.getMaxFrequency()));
		
		int[] freqs = cpu.getAvailableFrequencies();
		if (freqs != null) {
			for (int i = 0; i < freqs.length; ++i) {
				contents.put(key + " AvailableFrequency " + i + " (MHz)", 
					String.valueOf(freqs[i]));
			}
		}
		
		String[] govs = cpu.getAvailableGovernors();
		if (govs != null) {
			for (int i = 0; i < govs.length; ++i) {
				contents.put(key + " AvailableGovernor " + i, govs[i]);
			}
		}
		
		contents.put(key + " Governor", cpu.getGovernor());
		contents.put(key + " Driver", cpu.getDriver());
		contents.put(key + " TransitionLatency (ns)", String.valueOf(cpu.getTransitionLatency()));
		contents.put(key + " TotalTransitions", String.valueOf(cpu.getTotalTransitions()));
		contents.put(key + " TimeInTransitions (s)", String.valueOf(cpu.getTimeInTransitions()));
		contents.put(key + " TimeInTransitions (s)", String.valueOf(cpu.getTimeInTransitions()));
		
		int[][] times = cpu.getTimeInFrequency();
		if (times != null) {
			for (int i = 0; i < times.length; ++i) {
				contents.put(key + " TimeInFrequency " + times[i][0] + "MHz (Jiffies (10ms))", 
						String.valueOf(times[i][1]));					
			}
		}
		
		Map<Integer, Float> percents = cpu.getPercentInFrequency();
		if (percents != null && percents.size() > 0) {
			for (int freq : percents.keySet()) {
				contents.put(key + " PercentInFrequency " + freq + "MHz", 
						String.valueOf(percents.get(freq)));
			}
		}
		
		contents.putAll(getCpuStatContents(cpu.getCpuStat(), key));
		
		return contents;
	}
	
	private LinkedHashMap<String, String> getCpuStatContents(CpuStat cpuStat, String key) {
		LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
		
		contents.put(key + " CpuStat ID", String.valueOf(cpuStat.getId()));
		contents.put(key + " CpuStat Timestamp", String.valueOf(cpuStat.getTimestamp()));
		contents.put(key + " CpuStat TimestampPrevious", String.valueOf(cpuStat.getTimestampPrevious()));
		contents.put(key + " CpuStat User", String.valueOf(cpuStat.getUser()));
		contents.put(key + " CpuStat UserPrevious", String.valueOf(cpuStat.getUserPrevious()));
		contents.put(key + " CpuStat UserPercent", String.valueOf(cpuStat.getUserPercent()));
		contents.put(key + " CpuStat Nice", String.valueOf(cpuStat.getNice()));
		contents.put(key + " CpuStat NicePrevious", String.valueOf(cpuStat.getNicePrevious()));
		contents.put(key + " CpuStat NicePercent", String.valueOf(cpuStat.getNicePercent()));
		contents.put(key + " CpuStat System", String.valueOf(cpuStat.getSystem()));
		contents.put(key + " CpuStat SystemPrevious", String.valueOf(cpuStat.getSystemPrevious()));
		contents.put(key + " CpuStat SystemPercent", String.valueOf(cpuStat.getSystemPercent()));
		contents.put(key + " CpuStat Idle", String.valueOf(cpuStat.getIdle()));
		contents.put(key + " CpuStat IdlePrevious", String.valueOf(cpuStat.getIdlePrevious()));
		contents.put(key + " CpuStat IdlePercent", String.valueOf(cpuStat.getIdlePercent()));
		contents.put(key + " CpuStat IoWait", String.valueOf(cpuStat.getIoWait()));
		contents.put(key + " CpuStat IoWaitPrevious", String.valueOf(cpuStat.getIoWaitPrevious()));
		contents.put(key + " CpuStat IoWaitPercent", String.valueOf(cpuStat.getIoWaitPercent()));
		contents.put(key + " CpuStat Intr", String.valueOf(cpuStat.getIntr()));
		contents.put(key + " CpuStat IntrPrevious", String.valueOf(cpuStat.getIntrPrevious()));
		contents.put(key + " CpuStat IntrPercent", String.valueOf(cpuStat.getIntrPercent()));
		contents.put(key + " CpuStat SoftIrq", String.valueOf(cpuStat.getSoftIrq()));
		contents.put(key + " CpuStat SoftIrqPrevious", String.valueOf(cpuStat.getSoftIrqPrevious()));
		contents.put(key + " CpuStat SoftIrqPercent", String.valueOf(cpuStat.getSoftIrqPercent()));
		contents.put(key + " CpuStat UserTotal", String.valueOf(cpuStat.getUserTotal()));
		contents.put(key + " CpuStat UserTotalPrevious", String.valueOf(cpuStat.getUserTotalPrevious()));
		contents.put(key + " CpuStat UserTotalPercent", String.valueOf(cpuStat.getUserTotalPercent()));
		contents.put(key + " CpuStat SystemTotal", String.valueOf(cpuStat.getSystemTotal()));
		contents.put(key + " CpuStat SystemTotalPrevious", String.valueOf(cpuStat.getSystemTotalPrevious()));
		contents.put(key + " CpuStat SystemTotalPercent", String.valueOf(cpuStat.getSystemTotalPercent()));
		contents.put(key + " CpuStat IdleTotal", String.valueOf(cpuStat.getIdleTotal()));
		contents.put(key + " CpuStat IdleTotalPrevious", String.valueOf(cpuStat.getIdleTotalPrevious()));
		contents.put(key + " CpuStat IdleTotalPercent", String.valueOf(cpuStat.getIdleTotalPercent()));
		contents.put(key + " CpuStat Total", String.valueOf(cpuStat.getTotal()));
		contents.put(key + " CpuStat TotalPrevious", String.valueOf(cpuStat.getTotalPrevious()));
		contents.put(key + " CpuStat TotalPercent", String.valueOf(cpuStat.getTotalPercent()));
		
		return contents; 
	}
}
