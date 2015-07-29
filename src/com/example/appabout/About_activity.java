package com.example.appabout;



import jp.wasabeef.blurry.Blurry;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class About_activity extends Activity 
{
	private Button weiBoBtn, weChatBtn, qqBtn, updateBtn, recommandBtn, policyBtn;
	private ImageView mask, blurImageView;
	private RelativeLayout buttonLayout,aboutLayout;
	private Animation animationTopToMid, animationMidToBottom;
	private boolean isRecommandClicked = false;
	private final int blurlayout = 0;
	private final String TAG_ = "About_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tp_about_activity);
        setupDialogActionBar();
        initview();
    }
    
    private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.tp_about_actionbar);
		
		LinearLayout back = (LinearLayout)findViewById(R.id.tp_app_about_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
    private void initview()
    {
    	buttonLayout = (RelativeLayout) findViewById(R.id.tp_about_buttonlayout);
    	mask = (ImageView) findViewById(R.id.tp_about_activity_mask);
    	blurImageView = (ImageView) findViewById(R.id.tp_about_activity_blur);
    	buttonLayout.bringToFront();
    	aboutLayout = (RelativeLayout) findViewById(R.id.tp_about_layout);
    	recommandBtn = (Button) findViewById(R.id.tp_about_recommand_btn);
    	updateBtn = (Button) findViewById(R.id.tp_about_update_btn);
    	policyBtn = (Button) findViewById(R.id.tp_about_policy_btn);
    	weiBoBtn = (Button) findViewById(R.id.tp_about_weibo_btn);
    	weChatBtn = (Button) findViewById(R.id.tp_about_wechat_btn);
    	qqBtn = (Button) findViewById(R.id.tp_about_qq_btn);
    	
    	animationTopToMid = AnimationUtils.loadAnimation(About_activity.this, R.anim.tp_top_to_mid);
    	animationMidToBottom = AnimationUtils.loadAnimation(About_activity.this, R.anim.tp_mid_to_bottom);
    	animationMidToBottom.setAnimationListener(new onAnimationListener());
    	animationTopToMid.setAnimationListener(new onAnimationListener());
    	
    	policyBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent(About_activity.this, TermOfService_activity.class);
				startActivity(intent);
			}
		});
    	updateBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				
			}
		});
    	weiBoBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				Log.e(TAG_, "weiBoBtn click");
			}
		});
    	aboutLayout.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				if (arg0 != buttonLayout)
				{
					if (isRecommandClicked == true)
					{
						Log.e(TAG_, "aboutLayout click");
						isRecommandClicked = false;
						buttonLayout.startAnimation(animationMidToBottom);
						blurImageView.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
    	recommandBtn.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View arg0) 
			{
				isRecommandClicked = true;
				Message message = Message.obtain();
				message.what = blurlayout;
				handler.sendMessage(message);
				buttonLayout.startAnimation(animationTopToMid);
				/*if (blurred)
					Blurry.delete((ViewGroup) findViewById(R.id.tp_about_layout));
				else 
				{
					Blurry.with(About_activity.this)
							.radius(25)
							.sampling(2)
							.async()
							.animate(500)
							.onto((ViewGroup) findViewById(R.id.tp_about_layout));
				}
				blurred = !blurred;*/
			}
		});
    }
    public class onAnimationListener implements AnimationListener
    {
		@Override
		public void onAnimationEnd(Animation arg0) 
		{
			if (arg0 == animationTopToMid)
			{
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = buttonLayout.getWidth();
				int height = buttonLayout.getHeight();
				int left = (int) (dm.widthPixels - 100 - width);
				int top = (int) ((dm.heightPixels - 50) * 0.3);
				buttonLayout.layout(left, top, left + width, top + height);
				Log.e(TAG_, "layout animationTopToMid:" + left +" " + top + " " + width + " " + height);
				buttonLayout.clearAnimation();
				recommandBtn.setEnabled(true);
			}
			else if (arg0 == animationMidToBottom)
			{
				DisplayMetrics dm = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(dm);
				int width = buttonLayout.getWidth();
				int height = buttonLayout.getHeight();
				int left = (int) (dm.widthPixels - 100 - width);
				int top = 0;
				buttonLayout.layout(left, top, left + width, top + height);
				Log.e(TAG_, "layout animationMidToBottom:" + left + " " + top + " " + width + " " + height);
				buttonLayout.clearAnimation();
				buttonLayout.setVisibility(View.INVISIBLE);
				recommandBtn.setEnabled(true);
			}
		}

		@Override
		public void onAnimationRepeat(Animation arg0) 
		{
		}

		@Override
		public void onAnimationStart(Animation arg0) 
		{
			if (arg0 == animationTopToMid)
			{
				buttonLayout.setVisibility(View.VISIBLE);
				mask.setVisibility(View.VISIBLE);
				recommandBtn.setEnabled(false);
			}
			else if (arg0 == animationMidToBottom)
			{
				mask.setVisibility(View.INVISIBLE);
				recommandBtn.setEnabled(false);
			}
		}
    }
    private boolean blurred = false;
    private Handler handler = new Handler()
    {
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case blurlayout:
				RelativeLayout view = (RelativeLayout)findViewById(R.id.tp_about_layout);
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Bitmap bm = view.getDrawingCache();
				bm = Blur.fastblur(bm, 25);
				blurImageView.setImageBitmap(bm);
				//blurImageView.setBackgroundColor(Color.BLACK);
				blurImageView.setVisibility(view.VISIBLE);
				blurImageView.bringToFront();
				buttonLayout.bringToFront();
				break;
			}
		}
	};
}
