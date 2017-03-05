package com.fyzs.tool;

import com.fyzs.Http.FeedbackHttp;
import com.fyzs.fragment.PerInfoFragment;
import com.czfy.zsfy.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FeedbackDialog extends Dialog {

	public FeedbackDialog(Context context) {
		super(context);
	}

	public FeedbackDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context
					.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			final CustomDialog dialog = new CustomDialog(context,
					R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_feedback, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.fb_title)).setText(title);
			final EditText ed_em = (EditText) layout
					.findViewById(R.id.et_feed_email);
			final EditText ed_con = (EditText) layout
					.findViewById(R.id.et_feed_content);
			// set the confirm button
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.fb_positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.fb_positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									new Thread() {

										@Override
										public void run() {
											// TODO Auto-generated method stub
											SharedPreferences sp1 = context.getSharedPreferences(
													"StuData", 0);
											
											FeedbackHttp.Back(ed_em.getText()
													.toString()+sp1.getString("banji", "")+sp1.getString("name", ""), ed_con
													.getText().toString(),
													context);
											super.run();
										}

									}.start();
									Toast.makeText(context, "·´À¡³É¹¦", 0).show();
									dialog.dismiss();
									System.out.println(ed_em.getText()
											.toString());
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.fb_positiveButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.fb_negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.fb_negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.fb_negativeButton).setVisibility(
						View.GONE);
			}
			// set the content message
			// if (message != null) {
			// ((TextView) layout.findViewById(R.id.message)).setText(message);
			// } else if (contentView != null) {
			// // if no message set
			// // add the contentView to the dialog body
			// ((LinearLayout) layout.findViewById(R.id.content))
			// .removeAllViews();
			// ((LinearLayout) layout.findViewById(R.id.content))
			// .addView(contentView, new
			// LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			// }
			dialog.setContentView(layout);
			return dialog;
		}
	}
}
