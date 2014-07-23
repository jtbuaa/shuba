package com.dev1024.manager;

import java.util.Stack;

import android.app.Activity;

public class ScreenManager {

	private static Stack<Activity> activityStack;

	private static ScreenManager instance;

	private ScreenManager() {

	}

	/**
	 * 
	 * @return
	 */
	public static ScreenManager getScreenManager() {
		if (instance == null) {
			instance = new ScreenManager();
		}
		return instance;
	}

	/**
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	/**
	 * 
	 */
	private Activity currentActivity() {
		Activity activity = null;
		try {
			if (activityStack != null && !activityStack.isEmpty()) {
				activity = activityStack.pop();
			}
			return activity;
		} catch (Exception ex) {
			System.out.println("ScreenManager:currentActivity---->"
					+ ex.getMessage());
			return activity;
		} finally {
			activity = null;
		}
	}

	/**
	 * 
	 * @param activity
	 */
	public void pushActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.push(activity);
	}

	/**
	 */
	public void popAllActivity() {
		Activity activity = null;
		try {
			while (activityStack != null && !activityStack.isEmpty()) {
				activity = currentActivity();
				if (activity != null) {
					popActivity(activity);
				}
			}
		} catch (Exception ex) {
			System.out.println("ScreenManager:popAllActivity---->"
					+ ex.getMessage());
		} finally {
			activity = null;
		}
	}

}
