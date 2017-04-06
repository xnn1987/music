package com.allen.music.tools;

import android.app.Activity;
import android.content.Context;

import com.allen.music.R;

public class AnimUtils {

	public static void overridePendingTransition_Default(Context cont, int enter_anim, int exit_anim) {
		((Activity) cont).overridePendingTransition(enter_anim, exit_anim);
	}

	public static void overridePendingTransition_Right(Context cont) {
		((Activity) cont).overridePendingTransition(R.anim.anka_push_right_in, R.anim.anka_push_right_out);
	}

	public static void overridePendingTransition_Left(Context cont) {
		((Activity) cont).overridePendingTransition(R.anim.anka_push_left_in, R.anim.anka_push_left_out);
	}
}
