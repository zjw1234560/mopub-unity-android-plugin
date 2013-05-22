# mopub-unity-android-plugin

## Build Instructions

In order to properly build the MoPub Unity Plugin you will need to use files from this repository (referred to as **UnityPlugin** in this document) and the MoPub Android SDK found under the `mopub-sdk` directory at <https://github.com/mopub/mopub-android-sdk> (referred to as **MoPubSdk** in this document).

1. Build MoPubPlugin.jar  
    a. Clone **MoPubSdk**  
    b. Copy **UnityPlugin**'s `MoPubUnityPlugin.java` to **MoPubSdk**'s `src/main/java/com/mopub/mobileads` directory  
    c. Add **UnityPlugin**'s `classes.jar` to **MoPubSdk**'s build path (in Eclipse)  
    d. Use Eclipse (or your favorite tool) to build the **MoPubSdk** library to `MoPubPlugin.jar`  
2. Import **UnityPlugin**'s `MoPubAndroid.unitypackage` into your Unity Android Project  
3. Copy `MoPubPlugin.jar` to the Unity Project's `Plugins/Android` directory  
4. Copy **UnityPlugin**'s `android-support-v4.jar` to the Unity Project's `Plugins/Android` directory   
5. (Optional) Add **UnityPlugin**'s `Plugins/MoPubAndroid/testSupport/MoPubTestScene` to the Unity build window (contains the `MoPubUIManager.cs` example)  
6. Build and run your Unity project  

## API Documentation

`Plugins/MoPubAndroid/MoPubAndroid.cs` exposes the following methods:

```
// Creates a banner with the given ad unit placed based on the position parameter
public static void createBanner( string adUnitId, MoPubAdPlacement position )

// Hides/shows the ad banner
public static void hideBanner( bool shouldHide )

// Sets the search keywords for the banner
public static void setBannerKeywords( string keywords )

// Destroys the banner and removes it from view
public static void destroyBanner()

// Starts loading an interstitial ad
public static void requestInterstitalAd( string adUnitId )

// If an interstitial ad is loaded this will take over the screen and show the ad
public static void showInterstitalAd()

// Reports an app download to MoPub
public static void reportApplicationOpen()
````

`Plugins/MoPubAndroid/MoPubAndroidManager.cs` exposes the following methods:

```
// Fired when a new ad is loaded
public void onAdLoaded( string empty )

// Fired when an ad fails to load
public void onAdFailed( string empty )

// Fired when an interstitial ad is loaded
public void onInterstitialLoaded( string empty )

// Fired when an interstitial ad fails to load
public void onInterstitialFailed( string e )
```

`Plugins/MoPubAndroid/testSupport/MoPubUIManager.cs` provides an example of how this API may be consumed.
