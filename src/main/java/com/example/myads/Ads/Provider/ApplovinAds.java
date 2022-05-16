package com.example.myads.Ads.Provider;

import android.app.Activity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.example.myads.Ads.AdsConstant;
import com.example.myads.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplovinAds extends AdsFormat {

    //Defaults
    public static ApplovinAds instance;
    public static Activity activity;

    //Unity Ids
    private static String unityGameID = "4388627";
    private static String RewardAdId = "Rewarded_Android";
    private static String fullScreenAdId = "Interstitial_Android";
    private static String NativeId = "Interstitial_Android";
    private static String Banner_ID = "Banner_Android";//4590626(my)

    //Unity loaded tags


    //ApplovinAds ad variables
    MaxAdView adView;

    public ApplovinAds(Activity activity) {
        this.activity = activity;
    }

    public static ApplovinAds getInstance(Activity activity1) {
        activity = activity1;
        if (instance == null) {
            instance = new ApplovinAds(activity1);
        }
        return instance;
    }


    @Override
    public void preloadAds(JSONObject jsonObject) {
        setAdsId(jsonObject);
        AppLovinSdk.getInstance(activity).setMediationProvider("max");
        AppLovinSdk.initializeSdk(activity, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
            }
        });

        preloadBannerAds();
        preloadNativeAds();
        preloadInterstitialAd();
    }

    private void preloadBannerAds() {
        if (isBannerLoaded()){
            return;
        }
        adView = new MaxAdView(Banner_ID, activity);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {

            }

            @Override
            public void onAdCollapsed(MaxAd ad) {

            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                setBannerLoaded(true);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {

            }

            @Override
            public void onAdHidden(MaxAd ad) {

            }

            @Override
            public void onAdClicked(MaxAd ad) {

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {

                setBannerLoaded(false);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {

                setBannerLoaded(false);
            }
        });
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);
        adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
        adView.loadAd();
    }

    private void preloadInterstitialAd() {

    }

    private void preloadNativeAds() {

    }


    @Override
    public void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.ApplovinADS);
            Banner_ID = googleJson.getString(AdsConstant.BannerAD_ID);
            fullScreenAdId = googleJson.getString(AdsConstant.FullScreen_ID);
            NativeId = googleJson.getString(AdsConstant.Native_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showInterstitialAds() {

    }

    @Override
    public void showBannerAds(LinearLayout adLayout) {
        if (adLayout != null) {
            adLayout.removeAllViews();
            if (adView != null) {
                adLayout.setGravity(Gravity.CENTER);
                adLayout.addView(adView);
                setBannerLoaded(false);
                preloadBannerAds();
            }
        }
    }

    @Override
    public void showNative(LinearLayout layout, ImageView img) {

    }

    @Override
    public void showRewardAds() {

    }
}
