package catdany.bfdist.log;

import android.app.Activity;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import catdany.bfdist.BFHelper;
import distcomp.catdany.distcompapp.MainActivity;
import distcomp.catdany.distcompapp.ProgressActivity;

public class BFLog
{
	public static final String TAG = "DistComp-%s"; //%s=Thread

	/**
	 * Info logging
	 * @param format
	 * @param args
	 */
	public static void i(String format, Object... args)
	{
		Log.i(tag(), String.format(format, args));
	}
	
	/**
	 * Error logging
	 * @param format
	 * @param args
	 */
	public static void e(String format, Object... args)
	{
		Log.e(tag(), String.format(format, args));
	}
	
	/**
	 * Debug logging
	 * @param format
	 * @param args
	 */
	public static void d(String format, Object... args)
	{
		Log.v(tag(), String.format(format, args));
	}
	
	/**
	 * Warning logging
	 * @param format
	 * @param args
	 */
	public static void w(String format, Object... args)
	{
		Log.w(tag(), String.format(format, args));
	}
	
	/**
	 * Log stack trace
	 * @param t
	 */
	public static void t(Exception t)
	{
		Log.e(tag(), t.getClass().getCanonicalName() + ": " + t.getMessage());
		for (StackTraceElement i : t.getStackTrace())
		{
			Log.e(tag(), "  at " + i.toString());
		}
	}
	
	/**
	 * Make a {@link BFException} and {@link #t(Exception) print stacktrace}
	 * @param format
	 * @param args
	 */
	public static void t(String format, Object... args)
	{
		BFException t = new BFException(format, args);
		t(t);
	}
	
	/**
	 * {@link #t(Exception)} -> exit<br>
	 * Exit code is a hash code of the unformatted error message
	 * @param format
	 * @param args
	 */
	public static void exit(String format, Object... args)
	{
		t(format, args);
		ProgressActivity.activityInstance.finish();
	}

	public static String tag() {
		return TAG + "-" + Thread.currentThread().getName();
	}
}
