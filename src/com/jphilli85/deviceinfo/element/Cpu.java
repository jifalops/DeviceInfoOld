package com.jphilli85.deviceinfo.element;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;

import com.jphilli85.deviceinfo.ShellHelper;

//TODO exact current frequency???
public class Cpu extends ListeningElement {
	
	public interface Callback extends ListeningElement.Callback {
		void onUpdated();
	}
	
	public static final int FREQUENCY_HIGH = 1000;
	public static final int FREQUENCY_MEDIUM = 2000;
	public static final int FREQUENCY_LOW = 5000;
	
	private final List<String> mCpuinfo;
	private final List<LogicalCpu> mLogicalCpus;
	private final CpuStat mCpuStat;
	
	private int mNumStatsUpdated;
	
	private long mTimestamp;
	private long mTimestampPrevious;

	private final RepeatingTask mUpdateTask;
	
	public Cpu() {	
		mCpuinfo = ShellHelper.getProc("cpuinfo");
		mLogicalCpus = new ArrayList<LogicalCpu>();
		mCpuStat = new CpuStat();
		mUpdateTask = new BackgroundRepeatingTask(new Runnable() {			
			@Override
			public void run() {
				setTimestamp();
				updateCpuStats();
				for (LogicalCpu c : mLogicalCpus) {
					c.updateFrequency();
					c.updateGovernor();
					c.updateTimeInFrequency();
					c.updateTotalTransitions();
				}
			}
		});
		mUpdateTask.setInterval(FREQUENCY_MEDIUM);
		mUpdateTask.setCallback(new Runnable() {			
			@Override
			public void run() {
				if (getCallback() != null) ((Callback) getCallback()).onUpdated(); 
			}
		});
		
		File f = null;
		int i = 0;
		while (true) {
			f = new File("/sys/devices/system/cpu/cpu" + i);
			if (f.exists()) mLogicalCpus.add(new LogicalCpu(f, i));
			else break;
			++i;
		}
	}	
	
	public void setUpdateInterval(int milliseconds) {
		mUpdateTask.setInterval(milliseconds);
	}
	
	public int getUpdateInterval() {
		return mUpdateTask.getInterval();
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
	
	public int getNumStatsUpdated() {
		return mNumStatsUpdated;
	}
	
	/** Updates the CpuStat for this and all logical CPUs. */
	private void updateCpuStats() {
		mNumStatsUpdated = 0;
		List<String> stats = ShellHelper.getProc("stat");
		if (stats == null || stats.isEmpty()) return;
		
		String[] parts = null;
		String line = null;
		for (int i = 0; i < stats.size(); ++i) {
			line = stats.get(i);
			if (line.startsWith("cpu")) {
				parts = line.split("\\s+");				
				if (parts[0].endsWith(String.valueOf(i - 1))) {
					if (mLogicalCpus.size() >= i &&
							mLogicalCpus.get(i - 1).getCpuStat().update(parts)) {					
						++mNumStatsUpdated;
					}
				}
				else if (mCpuStat.update(parts)) {
					++mNumStatsUpdated;
				}
			}
		}
	}
	
	/** Timestamp in milliseconds */
	public synchronized long getTimestamp() {
		return mTimestamp;
	}
	
	public synchronized long getTimestampPrevious() {
		return mTimestampPrevious;
	}
	
	public synchronized long getTimestampDifference() {
		return (mTimestamp - mTimestampPrevious);
	}
	
	private synchronized void setTimestamp() {
		mTimestampPrevious = mTimestamp;
		mTimestamp = System.currentTimeMillis();
	}
	
	
	public class LogicalCpu implements ContentsMapper {
		private final String LOG_TAG = LogicalCpu.class.getSimpleName();
		
//		public final float bogoMips;
		private final int mId;
		private final File mRoot;
		private final CpuStat mCpuStat;
		
		private int mMaxFrequency;
		private int mMinFrequency;
		private int mCurFrequency;
		private int[] mAvailableFrequencies;
		private String[] mAvailableGovernors;
		private String mCurGoverner;
		private String mDriver;
		private int mTransitionLatency;
		private int mTotalTransitions;
		private int[][] mTimeInFrequencies;
		
		
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
			if (mMaxFrequency == 0) {
				List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/cpuinfo_max_freq");
				if (list == null || list.isEmpty()) return 0;
				int value = 0;
				try { value = Integer.valueOf(list.get(0)); }
				catch (NumberFormatException ignored) {}
				mMaxFrequency = value / 1000;
			}
			return mMaxFrequency;
		}
		
		/** Get the minimum frequency in MHz */
		public int getMinFrequency() {
			if (mMinFrequency == 0) {
				List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/cpuinfo_min_freq");
				if (list == null || list.isEmpty()) return 0;
				int value = 0;
				try { value = Integer.valueOf(list.get(0)); }
				catch (NumberFormatException ignored) {}
				mMinFrequency = value / 1000;
			}
			return mMinFrequency;
		}
		
		/** Get the current frequency in MHz */
		public int getFrequency() {
			return mCurFrequency;
		}	
		
		public void updateFrequency() {
			List<String> list = ShellHelper.cat(
				mRoot.getAbsolutePath() + "/cpufreq/scaling_cur_freq");
			if (list == null || list.isEmpty()) return;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			mCurFrequency =  value / 1000;
		}
		
		/** Get the available frequencies in MHz */
		public int[] getAvailableFrequencies() {
			if (mAvailableFrequencies == null) {
				List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/scaling_available_frequencies");
				if (list == null || list.isEmpty()) return null;
				String[] results = list.get(0).split("\\s+");
				if (results == null || results.length == 0) {
					return null;
				}
				int len = results.length;
				mAvailableFrequencies = new int[len];
				for (int i = 0; i < len; ++i) {
					int value = 0;
					try { value = Integer.valueOf(results[i]); }
					catch (NumberFormatException ignored) {}
					mAvailableFrequencies[i] = value / 1000;
				}
			}
			return mAvailableFrequencies;
		}
		
		/** Get the available governors */
		public String[] getAvailableGovernors() {
			if (mAvailableGovernors == null) {
				List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/scaling_available_governors");
				if (list == null || list.isEmpty()) return null;
				mAvailableGovernors = list.get(0).split("\\s+");
			}
			return mAvailableGovernors;
		}
		
		/** Get the current governor */
		public String getGovernor() {
			return mCurGoverner;
		}
		
		public void updateGovernor() {
			List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/scaling_governor");
			if (list == null || list.isEmpty()) return;
			mCurGoverner = list.get(0);
		}
		
		/** Get the current driver */
		public String getDriver() {
			if (mDriver == null) {
				List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/scaling_driver");
				if (list == null || list.isEmpty()) return null;
				mDriver = list.get(0);
			}
			return mDriver;
		}
		
		/** Get the frequency transition latency in nano-seconds */
		public int getTransitionLatency() {
			if (mTransitionLatency == 0) {
				List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/cpuinfo_transition_latency");
				if (list == null || list.isEmpty()) return 0;
				int value = 0;
				try { value = Integer.valueOf(list.get(0)); }
				catch (NumberFormatException ignored) {}
				mTransitionLatency = value;			
			}
			return mTransitionLatency;
		}
		
		/** Get the total number of frequency transitions */
		public int getTotalTransitions() {
			return mTotalTransitions;
		}
		
		public void updateTotalTransitions() {
			List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/stats/total_trans");
			if (list == null || list.isEmpty()) return;
			int value = 0;
			try { value = Integer.valueOf(list.get(0)); }
			catch (NumberFormatException ignored) {}
			mTotalTransitions = value;
		}
		
		/** Get the total amount of time spent in frequency transitions in seconds */
		public float getTimeInTransitions() {
			return (float) ((long) getTotalTransitions() * (long) getTransitionLatency() / 1E9);
		}

		/** Get a list of the total time (in Jiffies) spent at each frequency (in MHz) */
		public int[][] getTimeInFrequency() {
			return mTimeInFrequencies;
		}
		
		public void updateTimeInFrequency() {
			List<String> list = ShellHelper.cat(
					mRoot.getAbsolutePath() + "/cpufreq/stats/time_in_state");
			if (list == null || list.isEmpty()) return;
			int len = list.size();
			int[][] times = new int[len][2];
			String[] parts = null;
			for (int i = 0; i < len; ++i) {
				parts = list.get(i).split("\\s+");
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
			mTimeInFrequencies = times;
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

		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> subcontents;
			
			contents.put("ID", String.valueOf(getId()));
			contents.put("Root", getRoot().getAbsolutePath());
			contents.put("Frequency (MHz)", String.valueOf(getFrequency()));
			contents.put("MinFrequency (MHz)", String.valueOf(getMinFrequency()));
			contents.put("MaxFrequency (MHz)", String.valueOf(getMaxFrequency()));
			
			int[] freqs = getAvailableFrequencies();
			if (freqs != null) {
				for (int i = 0; i < freqs.length; ++i) {
					contents.put("AvailableFrequency " + i + " (MHz)", 
						String.valueOf(freqs[i]));
				}
			}
			
			String[] govs = getAvailableGovernors();
			if (govs != null) {
				for (int i = 0; i < govs.length; ++i) {
					contents.put("AvailableGovernor " + i, govs[i]);
				}
			}
			
			contents.put("Governor", getGovernor());
			contents.put("Driver", getDriver());
			contents.put("TransitionLatency (ns)", String.valueOf(getTransitionLatency()));
			contents.put("TotalTransitions", String.valueOf(getTotalTransitions()));
			contents.put("TimeInTransitions (s)", String.valueOf(getTimeInTransitions()));
			contents.put("TimeInTransitions (s)", String.valueOf(getTimeInTransitions()));
			
			int[][] times = getTimeInFrequency();
			if (times != null) {
				for (int i = 0; i < times.length; ++i) {
					contents.put("TimeInFrequency " + times[i][0] + "MHz (Jiffies (10ms))", 
							String.valueOf(times[i][1]));					
				}
			}
			
			Map<Integer, Float> percents = getPercentInFrequency();
			if (percents != null && percents.size() > 0) {
				for (int freq : percents.keySet()) {
					contents.put("PercentInFrequency " + freq + "MHz", 
							String.valueOf(percents.get(freq)));
				}
			}
			
			subcontents = getCpuStat().getContents();
			for (Entry<String, String> e : subcontents.entrySet()) {
				contents.put("CpuStat " + e.getKey(), e.getValue());
			}
			
			return contents;
		}
	}
	
	public class CpuStat implements ContentsMapper {
		private final String LOG_TAG = CpuStat.class.getSimpleName();
		
		public final int OVERALL_ID = -1;
		
		private int mId;
		
		
		private long mUser = 0;
		private long mNice = 0;
		private long mSystem = 0;
		private long mIdle = 0;
		private long mIoWait = 0;
		private long mIntr = 0;
		private long mSoftIrq = 0;		
		// The other two fields, Steal and Guest, seem to always be zero.
		
		
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
		private boolean update(String[] parts) {		
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
			
			mUserPrevious = mUser;
			mNicePrevious = mNice;
			mSystemPrevious = mSystem;
			mIdlePrevious = mIdle;
			mIoWaitPrevious = mIoWait;
			mIntrPrevious = mIntr;
			mSoftIrqPrevious = mSoftIrq;
			
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
		
		
		
		public long getUserDifference() {
			return (mUser - mUserPrevious);
		}
		
		public long getNiceDifference() {
			return (mNice - mNicePrevious);
		}
		
		public long getSystemDifference() {
			return (mSystem - mSystemPrevious);
		}
		
		public long getIdleDifference() {
			return (mIdle - mIdlePrevious);
		}
		
		public long getIoWaitDifference() {
			return (mIoWait - mIoWaitPrevious);
		}
		
		public long getIntrDifference() {
			return (mIntr - mIntrPrevious);
		}
		
		public long getSoftIrqDifference() {
			return (mSoftIrq - mSoftIrqPrevious);
		}
		
		/** User + Nice */
		public long getUserTotalDifference() {
			return (mUser - mUserPrevious) + (mNice - mNicePrevious);
		}
		
		/** System + Intr + SoftIrq */
		public long getSystemTotalDifference() {
			return (mSystem - mSystemPrevious) + (mIntr - mIntrPrevious) + (mSoftIrq - mSoftIrqPrevious);
		}
		
		/** Idle + IoWait */
		public long getIdleTotalDifference() {
			return (mIdle - mIdlePrevious) + (mIoWait - mIoWaitPrevious);
		}
		
		public long getTotalDifference() {
			return (mUser - mUserPrevious) 
				+ (mNice - mNicePrevious) + (mSystem - mSystemPrevious)
				+ (mIdle - mIdlePrevious) + (mIoWait - mIoWaitPrevious)
				+ (mIntr - mIntrPrevious) + (mSoftIrq - mSoftIrqPrevious);
		}

		@Override
		public LinkedHashMap<String, String> getContents() {
			LinkedHashMap<String, String> contents = new LinkedHashMap<String, String>();
			
			contents.put("ID", String.valueOf(getId()));
			contents.put("Timestamp", String.valueOf(getTimestamp()));
			contents.put("TimestampPrevious", String.valueOf(getTimestampPrevious()));
			contents.put("TimestampDifference", String.valueOf(getTimestampDifference()));
			contents.put("User", String.valueOf(getUser()));
			contents.put("UserPrevious", String.valueOf(getUserPrevious()));
			contents.put("UserDifference", String.valueOf(getUserDifference()));
			contents.put("UserPercent", String.valueOf(getUserPercent()));
			contents.put("Nice", String.valueOf(getNice()));
			contents.put("NicePrevious", String.valueOf(getNicePrevious()));
			contents.put("NiceDifference", String.valueOf(getNiceDifference()));
			contents.put("NicePercent", String.valueOf(getNicePercent()));
			contents.put("System", String.valueOf(getSystem()));
			contents.put("SystemPrevious", String.valueOf(getSystemPrevious()));
			contents.put("SystemDifference", String.valueOf(getSystemDifference()));
			contents.put("SystemPercent", String.valueOf(getSystemPercent()));
			contents.put("Idle", String.valueOf(getIdle()));
			contents.put("IdlePrevious", String.valueOf(getIdlePrevious()));
			contents.put("IdleDifference", String.valueOf(getIdleDifference()));
			contents.put("IdlePercent", String.valueOf(getIdlePercent()));
			contents.put("IoWait", String.valueOf(getIoWait()));
			contents.put("IoWaitPrevious", String.valueOf(getIoWaitPrevious()));
			contents.put("IoWaitDifference", String.valueOf(getIoWaitDifference()));
			contents.put("IoWaitPercent", String.valueOf(getIoWaitPercent()));
			contents.put("Intr", String.valueOf(getIntr()));
			contents.put("IntrPrevious", String.valueOf(getIntrPrevious()));
			contents.put("IntrDifference", String.valueOf(getIntrDifference()));
			contents.put("IntrPercent", String.valueOf(getIntrPercent()));
			contents.put("SoftIrq", String.valueOf(getSoftIrq()));
			contents.put("SoftIrqPrevious", String.valueOf(getSoftIrqPrevious()));
			contents.put("SoftIrqDifference", String.valueOf(getSoftIrqDifference()));
			contents.put("SoftIrqPercent", String.valueOf(getSoftIrqPercent()));
			contents.put("UserTotal", String.valueOf(getUserTotal()));
			contents.put("UserTotalPrevious", String.valueOf(getUserTotalPrevious()));
			contents.put("UserTotalDifference", String.valueOf(getUserTotalDifference()));
			contents.put("UserTotalPercent", String.valueOf(getUserTotalPercent()));
			contents.put("SystemTotal", String.valueOf(getSystemTotal()));
			contents.put("SystemTotalPrevious", String.valueOf(getSystemTotalPrevious()));
			contents.put("SystemTotalDifference", String.valueOf(getSystemTotalDifference()));
			contents.put("SystemTotalPercent", String.valueOf(getSystemTotalPercent()));
			contents.put("IdleTotal", String.valueOf(getIdleTotal()));
			contents.put("IdleTotalPrevious", String.valueOf(getIdleTotalPrevious()));
			contents.put("IdleTotalDifference", String.valueOf(getIdleTotalDifference()));
			contents.put("IdleTotalPercent", String.valueOf(getIdleTotalPercent()));
			contents.put("Total", String.valueOf(getTotal()));
			contents.put("TotalPrevious", String.valueOf(getTotalPrevious()));
			contents.put("TotalDifference", String.valueOf(getTotalDifference()));
			contents.put("TotalPercent", String.valueOf(getTotalPercent()));
			
			return contents; 
		}
	}


	@Override
	public LinkedHashMap<String, String> getContents() {
		LinkedHashMap<String, String> contents = super.getContents();
		LinkedHashMap<String, String> subcontents;
		
		for (int i = 0; i < mCpuinfo.size(); ++i) {
			contents.put("CPU Info " + i, mCpuinfo.get(i));
		}
		
		subcontents = mCpuStat.getContents();
		for (String s : subcontents.keySet()) {
			contents.put("Overall CpuStat " + s, subcontents.get(s));
		}
		
		int i = 0;
		for (LogicalCpu logicalCpu : mLogicalCpus) {
			subcontents = logicalCpu.getContents();
			for (String s : subcontents.keySet()) {				
				contents.put("Logical CPU " + i + " " + s, subcontents.get(s));				
			}
			++i;
		}
		
		return contents; 
	}
	
	@Override
	public boolean startListening(boolean onlyIfCallbackSet) {
		if (!super.startListening(onlyIfCallbackSet)) return false;
		mUpdateTask.start();
		return setListening(true);
	}
	
	@Override
	public boolean stopListening() {
		if (!super.stopListening()) return false;
		mUpdateTask.stop();
		return !setListening(false);
	}
	
	
}
