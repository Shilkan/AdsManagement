package com.example.myads.Ads.Provider;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myads.Ads.AdsConstant;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAdsShowOptions;
import com.unity3d.ads.metadata.MediationMetaData;
import com.unity3d.ads.metadata.PlayerMetaData;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBannerSize;
import com.unity3d.services.banners.UnityBanners;
import com.unity3d.services.banners.view.BannerPosition;

import org.json.JSONException;
import org.json.JSONObject;

public class UnityAds extends AdsFormat {

    //Defaults
    public static UnityAds instance;
    public static Activity activity;

    //Unity Ids
    private static String unityGameID = "14851";
    private static String RewardAdId = "Rewarded_Android";
    private static String fullScreenAdId = "Interstitial_Android";
    private static String Banner_ID = "Banner_Android";//4590626(my)

    //Unity loaded tags


    //Unity ad variables
//    private View bannerView;
    private BannerView bottomBanner;

    public static String TAG = "123UUU";//BaseClass.class.getSimpleName();

    public UnityAds(Activity activity) {
        this.activity = activity;
    }

    public static UnityAds getInstance(Activity activity) {
        if (instance == null) {
            instance = new UnityAds(activity);
        }
        return instance;
    }

    @Override
    public void preloadAds(JSONObject jsonObject) {
        setAdsId(jsonObject);
        com.unity3d.ads.UnityAds.initialize(activity, unityGameID, true, new IUnityAdsInitializationListener() {
            @Override
            public void onInitializationComplete() {
                preloadBannerAds();
                preloadInterstitialAd();
            }

            @Override
            public void onInitializationFailed(com.unity3d.ads.UnityAds.UnityAdsInitializationError error, String message) {
                Log.e(TAG, "Unity Ads initialization failed: [" + error + "] " + message);
            }
        });
    }

    private void preloadInterstitialAd() {
        if (isInterstitialLoaded()) {
            return;
        }
        com.unity3d.ads.UnityAds.load(fullScreenAdId, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.v(TAG, "Ad for " + placementId + " loaded");
                setInterstitialLoaded(true);
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, com.unity3d.ads.UnityAds.UnityAdsLoadError error, String message) {
                setInterstitialLoaded(false);
            }
        });

    }

    private void preloadNativeAds() {
        if (isNativeLoaded()) {
            return;
        }
    }

    private void preloadBannerAds() {
        if (isBannerLoaded()) {
            return;
        }
        bottomBanner = new BannerView(activity, Banner_ID, new UnityBannerSize(320, 50));
        bottomBanner.setListener(bannerListener);
        bottomBanner.setGravity(RelativeLayout.CENTER_IN_PARENT);
        bottomBanner.load();

    }
    private BannerView.IListener bannerListener = new BannerView.IListener() {
        @Override
        public void onBannerLoaded(BannerView bannerAdView) {
            Log.v(TAG, "onBannerLoaded: " + bannerAdView.getPlacementId());
            setBannerLoaded(true);
        }

        @Override
        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
            setBannerLoaded(false);
        }

        @Override
        public void onBannerClick(BannerView bannerAdView) {
            Log.v(TAG, "onBannerClick: " + bannerAdView.getPlacementId());
        }

        @Override
        public void onBannerLeftApplication(BannerView bannerAdView) {
            Log.v(TAG, "onBannerLeftApplication: " + bannerAdView.getPlacementId());
        }
    };


    @Override
    public void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.UnityADS);
            Banner_ID = googleJson.getString(AdsConstant.BannerAD_ID);
            fullScreenAdId = googleJson.getString(AdsConstant.FullScreen_ID);
//          NativeId =  googleJson.getString(AdsConstant.Native_ID);
            RewardAdId = googleJson.getString(AdsConstant.RewardAd_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void showInterstitialAds() {
        com.unity3d.ads.UnityAds.show(activity, fullScreenAdId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
            @Override
            public void onUnityAdsShowFailure(String placementId, com.unity3d.ads.UnityAds.UnityAdsShowError error, String message) {
                Log.e(TAG, "onUnityAdsShowFailure: " + error + " - " + message);
            }

            @Override
            public void onUnityAdsShowStart(String placementId) {
                Log.v(TAG, "onUnityAdsShowStart: " + placementId);
            }

            @Override
            public void onUnityAdsShowClick(String placementId) {
                Log.v(TAG,"onUnityAdsShowClick: " + placementId);
            }

            @Override
            public void onUnityAdsShowComplete(String placementId, com.unity3d.ads.UnityAds.UnityAdsShowCompletionState state) {
                Log.v(TAG,"onUnityAdsShowComplete: " + placementId);
            }
        });
//        if (com.unity3d.ads.UnityAds.isReady(fullScreenAdId)) {
//            com.unity3d.ads.UnityAds.show(activity, fullScreenAdId, new IUnityAdsShowListener() {
//                @Override
//                public void onUnityAdsShowFailure(String s, com.unity3d.ads.UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
//
//                }
//
//                @Override
//                public void onUnityAdsShowStart(String s) {
//
//                }
//
//                @Override
//                public void onUnityAdsShowClick(String s) {
//
//                }
//
//                @Override
//                public void onUnityAdsShowComplete(String s, com.unity3d.ads.UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
//                    AfterDismissInterstitial();
//                    preloadInterstitialAd();
//                }
//            });
//        }
    }

    @Override
    public void showBannerAds(LinearLayout adLayout) {
        if (adLayout != null) {
            adLayout.removeAllViews();
            if (bottomBanner != null) {
                adLayout.setGravity(Gravity.CENTER);
                adLayout.addView(bottomBanner);
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
