package com.mopub.mobileads;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.mopub.mobileads.MoPubConversionTracker;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.MoPubInterstitialListener;
import com.mopub.mobileads.MoPubView;
import com.mopub.mobileads.MoPubView.OnAdFailedListener;
import com.mopub.mobileads.MoPubView.OnAdLoadedListener;
import com.unity3d.player.UnityPlayer;



public class MoPubUnityPlugin implements OnAdLoadedListener, OnAdFailedListener, MoPubInterstitialListener
{
	private static MoPubUnityPlugin _instance;

	// used for testing directly in Eclipse
	public Activity _activity;
	
	private static String TAG = "MoPub";
	private MoPubInterstitial _interstitial;
	private MoPubView _adView;
	private RelativeLayout _layout;


	
	public static MoPubUnityPlugin instance()
	{
		if( _instance == null )
			_instance = new MoPubUnityPlugin();
		return _instance;
	}

	
	private Activity getActivity()
	{
		if( _activity != null )
			return _activity;
		
		return UnityPlayer.currentActivity;
	}
	
	
	private void UnitySendMessage( String go, String m, String p )
	{
		if( _activity != null )
		{
			Log.i( TAG, "UnitySendMessage: " + go + ", " + m + ", " + p );
		}
		else
		{
			UnityPlayer.UnitySendMessage( go, m, p );
		}
	}
	
	
	private float getScreenDensity()
	{
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics( metrics );

		return metrics.density;
	}
	
	
	private void prepLayout( int alignment )
	{
		// create a RelativeLayout and add the ad view to it
		if( _layout == null )
		{
			_layout = new RelativeLayout( getActivity() );
		}
		else
		{
    		// remove the layout if it has a parent
    		FrameLayout parentView = (FrameLayout)_layout.getParent();
    		if( parentView != null )
    			parentView.removeView( _layout );
		}

		int gravity = 0;
		
		switch( alignment )
		{
			case 0:
				gravity = Gravity.TOP | Gravity.LEFT;
				break;
			case 1:
				gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
				break;
			case 2:
				gravity = Gravity.TOP | Gravity.RIGHT;
				break;
			case 3:
				gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
				break;
			case 4:
				gravity = Gravity.BOTTOM | Gravity.LEFT;
				break;
			case 5:
				gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
				break;
			case 6:
				gravity = Gravity.BOTTOM | Gravity.RIGHT;
				break;
		}
		
		_layout.setGravity( gravity );
	}
	
	
	// Banners
	public void showBanner( final String adUnitId, final int alignment )
	{
		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
        		if( _adView != null )
        			return;

        		_adView = new MoPubView( getActivity() );
        		_adView.setAdUnitId( adUnitId );
        		_adView.setOnAdFailedListener( MoPubUnityPlugin.this );
        		_adView.setOnAdLoadedListener( MoPubUnityPlugin.this );
        		_adView.loadAd();
        		
        		prepLayout( alignment );
        		
        		_layout.addView( _adView );
        		getActivity().addContentView( _layout, new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT ) );
        		
        		_layout.setVisibility( RelativeLayout.VISIBLE );
            }
        });
	}


	public void hideBanner( final boolean shouldHide )
	{
		if( _adView == null )
			return;

		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
            	if( shouldHide )
            	{
            		_adView.setVisibility( View.GONE );
            	}
            	else
            	{
            		_adView.setVisibility( View.VISIBLE );
            	}
            }
        });
	}

	
	public void setBannerKeywords( final String keywords )
	{
		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
        		if( _adView == null )
        			return;

        		_adView.setKeywords( keywords );
        		_adView.loadAd();
            }
        });
	}
	
	
	public void destroyBanner()
	{
		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
        		if( _adView == null || _layout == null )
        			return;
        		
        		_layout.removeAllViews();
        		_layout.setVisibility( LinearLayout.GONE );
        		_adView.destroy();
        		_adView = null;
            }
        });
	}

	
	// Interstitials
	public void requestInterstitalAd( final String adUnitId )
	{
		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
            	_interstitial = new MoPubInterstitial( getActivity(), adUnitId );
            	_interstitial.setListener( MoPubUnityPlugin.this );
            	_interstitial.load();
            }
        });
	}
	
	
	public void showInterstitalAd()
	{
		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
            	_interstitial.show();
            }
        });
	}
	
	
	public void reportApplicationOpen()
	{
		getActivity().runOnUiThread( new Runnable()
        {
            public void run()
            {
            	new MoPubConversionTracker().reportAppOpen( getActivity() );
            }
        });
	}
	
	
	

	// OnAdLoadedListener
	@Override
	public void OnAdLoaded( MoPubView m )
	{
		UnitySendMessage( "MoPubAndroidManager", "onAdLoaded", "" );
		
		// re-center the ad
		int height = _adView.getAdHeight();
		int width = _adView.getAdWidth();
		float density = getScreenDensity();
		
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)_adView.getLayoutParams();
		params.width = (int)( width * density );
		params.height = (int)(height * density );

		_adView.setLayoutParams( params );
	}


	@Override
	public void OnAdFailed( MoPubView m )
	{
		UnitySendMessage( "MoPubAndroidManager", "onAdFailed", "" );
	}
	
	
	
	// Interstitial listener
	@Override
	public void OnInterstitialLoaded()
	{
		UnitySendMessage( "MoPubAndroidManager", "onInterstitialLoaded", "" );
	}


	@Override
	public void OnInterstitialFailed()
	{
		UnitySendMessage( "MoPubAndroidManager", "onInterstitialFailed", "" );
	}
	
}
