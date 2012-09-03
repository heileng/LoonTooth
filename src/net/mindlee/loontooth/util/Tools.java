package net.mindlee.loontooth.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Tools {
	public static long getThreadId() {
		Thread t = Thread.currentThread();
		return t.getId();
	}

	public static String getThreadSignature() {
		Thread t = Thread.currentThread();
		long l = t.getId();
		String name = t.getName();
		long p = t.getPriority();
		String gname = t.getThreadGroup().getName();
		return (name + ":(id)" + l + ":(priority)" + p + ":(group)" + gname);
	}

	public static void logThreadSignature(String tag) {
		Log.w(tag, getThreadSignature());
	}

	public static void sleepForInSecs(int secs) {
		try {
			Thread.sleep(secs * 1000);
		} catch (InterruptedException x) {
			throw new RuntimeException("interrupted", x);
		}
	}

	public static String sizeFormat(String size) {
		long num = Long.valueOf(size);
		String res = null;
		double sizeKbyte = num / 1024;
		double sizeMbyte = num / 1024 / 1024;// bytes -> M bytes
		double sizeGbyte = sizeMbyte / 1024;// bytes -> G bytes
		DecimalFormat df = new DecimalFormat();
		if (sizeMbyte < 1) {//kb
			String style = "#0.00KB";
			df.applyPattern(style);
			res = df.format(sizeKbyte);
		} else {
			if (sizeGbyte < 1) {//mb
				String style = "#0.00MB";
				df.applyPattern(style);
				res = df.format(sizeMbyte);
			} else {//gb
				String style = "#0.00GB";
				df.applyPattern(style);
				res = df.format(sizeGbyte);
			}
		}
		return res;
	}

	public static String durationFormat(String duration) {
		long num = Long.valueOf(duration);
		long hour = num / 1000 / 3600;
		long minutes = (num / 1000 - hour * 3600) / 60;
		long seconds = num / 1000 % 60;
		DecimalFormat df = new DecimalFormat();
		String style = "00";
		df.applyPattern(style);

		String res = df.format(minutes) + ":" + df.format(seconds);
		if (hour > 0) {
			res = hour + ":" + res;
		}
		return res;
	}

	public static String secondsToDate(String timeStr) {
		System.out.println("timeStr" + timeStr);
		long time = Long.valueOf(timeStr) * 1000;
		Date date = new Date(time);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");

		System.out.println(date.toString());
		return dateFormat.format(date).toString();
	}
}
