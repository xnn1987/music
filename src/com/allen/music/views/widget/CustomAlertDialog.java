package com.allen.music.views.widget;

import java.lang.ref.WeakReference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.allen.music.R;

@SuppressWarnings("all")
public class CustomAlertDialog extends Dialog implements DialogInterface {
	private DialogCache mDialogCache;
	private DialogInterface mDialogInterface;
	private Handler mHandler;
	private Button mLeftButton;
	private Button mRightButton;
	private View mButtonDivider;
	private TextView mTitle;
	private TextView mMessage;
	private ImageView mDivider;
	private FrameLayout mContentView;
	private View mButtonGroup;
	private ListView mListView;
	private ListAdapter mListAdapter;
	private DialogInterface.OnClickListener mOnClickListener;
	private int mCheckedItem = -1;

	private boolean mBeyondHoneycomb = Build.VERSION.SDK_INT >= 11;

	private View.OnClickListener mButtonHandler = new View.OnClickListener() {
		public void onClick(View v) {
			Message buttonMessage = (Message) v.getTag();
			if (buttonMessage == null) {
				CustomAlertDialog.this.dismiss();
				return;
			}

			Message m = Message.obtain(buttonMessage);

			if (m != null) {
				m.sendToTarget();
			}

			CustomAlertDialog.this.mHandler.obtainMessage(1, CustomAlertDialog.this.mDialogInterface).sendToTarget();
		}
	};

	public CustomAlertDialog(DialogCache dialogCache) {
		super(dialogCache.mContext, R.style.AlertDialog);
		this.mDialogCache = dialogCache;
		this.mDialogInterface = this;
		this.mHandler = new ButtonHandler(this.mDialogInterface);
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.public_show_dialog_alert);

		this.mLeftButton = ((Button) findViewById(R.id.left_button));
		this.mRightButton = ((Button) findViewById(R.id.right_button));
		this.mButtonDivider = findViewById(R.id.dialog_split_v);
		this.mTitle = ((TextView) findViewById(R.id.dialog_title));
		this.mMessage = ((TextView) findViewById(R.id.dialog_message));
		this.mDivider = ((ImageView) findViewById(R.id.dialog_divider));
		this.mContentView = ((FrameLayout) findViewById(R.id.dialog_content_view));
		this.mButtonGroup = findViewById(R.id.dialog_button_group);

		setupTitle();
		setupMessage();
		setupView();
		setupButtons();
	}

	private void setupTitle() {
		if (TextUtils.isEmpty(this.mDialogCache.mTitle)) {
			this.mTitle.setVisibility(8);
			this.mDivider.setVisibility(8);
		} else {
			this.mTitle.setVisibility(0);
			this.mDivider.setVisibility(0);

			if (this.mDialogCache.mIcon != null) {
				this.mTitle.setCompoundDrawablesWithIntrinsicBounds(this.mDialogCache.mIcon, null, null, null);
			}

			this.mTitle.setText(this.mDialogCache.mTitle);
		}
	}

	private void setupMessage() {
		boolean showMessage = (!TextUtils.isEmpty(this.mDialogCache.mMessage)) && (this.mDialogCache.mView == null);

		if (showMessage) {
			this.mMessage.setVisibility(0);

			this.mMessage.setText(this.mDialogCache.mMessage);
		} else {
			this.mMessage.setVisibility(8);
		}
	}

	private void setupView() {
		if (this.mDialogCache.mView == null) {
			return;
		}

		this.mContentView.removeAllViews();
		this.mContentView.addView(this.mDialogCache.mView);
	}

	private boolean setupButtons() {
		int buttonCount = 0;

		Button positiveButton = this.mBeyondHoneycomb ? this.mRightButton : this.mLeftButton;
		Button negativeButton = this.mBeyondHoneycomb ? this.mLeftButton : this.mRightButton;

		if (TextUtils.isEmpty(this.mDialogCache.mPositiveButton)) {
			positiveButton.setVisibility(8);
		} else {
			positiveButton.setVisibility(0);
			positiveButton.setText(this.mDialogCache.mPositiveButton);
			positiveButton.setOnClickListener(this.mButtonHandler);
			positiveButton.setTag(this.mDialogCache.mButtonPositiveMessage);
			buttonCount++;
		}

		if (TextUtils.isEmpty(this.mDialogCache.mNegativeButton)) {
			negativeButton.setVisibility(8);
		} else {
			negativeButton.setVisibility(0);
			negativeButton.setText(this.mDialogCache.mNegativeButton);
			negativeButton.setOnClickListener(this.mButtonHandler);
			negativeButton.setTag(this.mDialogCache.mButtonNegativeMessage);
			buttonCount++;
		}

		this.mButtonDivider.setVisibility(buttonCount > 1 ? 0 : 8);
		this.mButtonGroup.setVisibility(buttonCount == 0 ? 8 : 0);

		return buttonCount != 0;
	}

	public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener, Message msg) {
		if ((msg == null) && (listener != null)) {
			msg = this.mHandler.obtainMessage(whichButton, listener);
		}

		switch (whichButton) {
		case -1:
			this.mDialogCache.mPositiveButton = text;
			this.mDialogCache.mButtonPositiveMessage = msg;
			break;
		case -2:
			this.mDialogCache.mNegativeButton = text;
			this.mDialogCache.mButtonNegativeMessage = msg;
			break;
		default:
			throw new IllegalArgumentException("Button does not exist");
		}
	}

	public static class Builder {
		private final CustomAlertDialog.DialogCache mDialogCache;

		public Builder(Context context) {
			this.mDialogCache = new CustomAlertDialog.DialogCache();
			this.mDialogCache.mContext = context;
		}

		public Builder setMessage(int resId) {
			this.mDialogCache.mMessage = this.mDialogCache.mContext.getText(resId);
			return this;
		}

		public Builder setMessage(CharSequence message) {
			this.mDialogCache.mMessage = message;
			return this;
		}

		public Builder setTitle(int resId) {
			return setTitle(this.mDialogCache.mContext.getText(resId));
		}

		public Builder setTitle(CharSequence title) {
			this.mDialogCache.mTitle = title;
			return this;
		}

		public Builder setIcon(int resId) {
			return setIcon(this.mDialogCache.mContext.getResources().getDrawable(resId));
		}

		public Builder setIcon(Drawable drawable) {
			this.mDialogCache.mIcon = drawable;
			return this;
		}

		public Builder setView(View view) {
			this.mDialogCache.mView = view;
			return this;
		}

		public Builder setPositiveButton(int resId, DialogInterface.OnClickListener listener) {
			return setPositiveButton(this.mDialogCache.mContext.getString(resId), listener);
		}

		public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
			this.mDialogCache.mPositiveButton = text;
			this.mDialogCache.mPositiveButtonListener = listener;
			return this;
		}

		public Builder setNegativeButton(int resId, DialogInterface.OnClickListener listener) {
			return setNegativeButton(this.mDialogCache.mContext.getText(resId), listener);
		}

		public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
			this.mDialogCache.mNegativeButton = text;
			this.mDialogCache.mNegativeButtonListener = listener;
			return this;
		}

		public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem, DialogInterface.OnClickListener listener) {
			this.mDialogCache.mIsListMode = true;
			this.mDialogCache.mAdapter = adapter;
			this.mDialogCache.mOnClickListener = listener;
			this.mDialogCache.mCheckedItem = checkedItem;
			return this;
		}

		public void apply(CustomAlertDialog dialog) {
			if (this.mDialogCache.mPositiveButton != null) {
				dialog.setButton(-1, this.mDialogCache.mPositiveButton, this.mDialogCache.mPositiveButtonListener, null);
			}

			if (this.mDialogCache.mNegativeButton != null)
				dialog.setButton(-2, this.mDialogCache.mNegativeButton, this.mDialogCache.mNegativeButtonListener, null);
		}

		public void setCancelable(boolean flag) {
			this.mDialogCache.mCancelable = flag;
		}

		public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
			this.mDialogCache.mOnKeyListener = onKeyListener;
		}

		public CustomAlertDialog create() {
			CustomAlertDialog dialog = new CustomAlertDialog(this.mDialogCache);
			apply(dialog);

			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(this.mDialogCache.mCancelable);
			dialog.setOnCancelListener(this.mDialogCache.mOnCancelListener);
			if (this.mDialogCache.mOnKeyListener != null) {
				dialog.setOnKeyListener(this.mDialogCache.mOnKeyListener);
			}

			return dialog;
		}

		public CustomAlertDialog show() {
			CustomAlertDialog dialog = create();
			dialog.show();

			return dialog;
		}
	}

	private static final class ButtonHandler extends Handler {
		private static final int MSG_DISMISS_DIALOG = 1;
		private WeakReference<DialogInterface> mDialog;

		public ButtonHandler(DialogInterface dialog) {
			super();

			this.mDialog = new WeakReference(dialog);
		}

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -3:
			case -2:
			case -1:
				((DialogInterface.OnClickListener) msg.obj).onClick((DialogInterface) this.mDialog.get(), msg.what);
				break;
			case 1:
				((DialogInterface) msg.obj).dismiss();
			case 0:
			}
		}
	}

	private static class DialogCache {
		CharSequence mTitle;
		CharSequence mMessage;
		Drawable mIcon;
		Context mContext;
		View mView;
		DialogInterface.OnCancelListener mOnCancelListener;
		DialogInterface.OnKeyListener mOnKeyListener;
		CharSequence mPositiveButton;
		CharSequence mNegativeButton;
		DialogInterface.OnClickListener mPositiveButtonListener;
		DialogInterface.OnClickListener mNegativeButtonListener;
		Message mButtonPositiveMessage;
		Message mButtonNegativeMessage;
		boolean mCancelable = false;
		ListAdapter mAdapter;
		public int mCheckedItem = -1;
		DialogInterface.OnClickListener mOnClickListener;
		boolean mIsListMode = false;
	}
}