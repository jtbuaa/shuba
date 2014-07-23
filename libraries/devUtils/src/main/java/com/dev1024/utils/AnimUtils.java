package com.dev1024.utils;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.dev1024.johnliu.R;
import com.dev1024.utils.listener.AnimListener;

/**
 * 
 * AnimUtils
 * 
 * Created by John on 2014-6-15
 */
public class AnimUtils {

	public static void showFromBottom(final View view, int duration,
			final AnimListener listener) {
		if (view == null)
			return;

		if (view.getVisibility() == View.GONE) {
			System.out.println("showFromBottom");
			view.setVisibility(View.VISIBLE);
			Animation anim = AnimationUtils.loadAnimation(view.getContext(),
					R.anim.show_from_bottom);
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					if (listener != null)
						listener.onStart();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (listener != null)
						listener.onEnd();
				}
			});
			anim.setDuration(duration);
			view.startAnimation(anim);
		}
	}

	public static void hideFromBottom(final View view, int duration,
			final AnimListener listener) {
		if (view == null)
			return;

		if (view.getVisibility() == View.VISIBLE) {
			System.out.println("hideFromBottom");
			Animation anim = AnimationUtils.loadAnimation(view.getContext(),
					R.anim.hide_from_bottom);
			anim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {
					if (listener != null)
						listener.onStart();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (listener != null)
						listener.onEnd();
					view.setVisibility(View.GONE);
				}
			});
			anim.setDuration(duration);
			view.startAnimation(anim);
		}
	}

}
