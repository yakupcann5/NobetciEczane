package com.yakupcan.nobetcieczane.util

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.yakupcan.nobetcieczane.R

object InterstitialAdManager {
    private var interstitialAd: InterstitialAd? = null
    private var isLoading = false
    private var interactionCount = 0
    private const val INTERACTIONS_BEFORE_AD = 3

    fun loadAd(context: Context) {
        if (isLoading || interstitialAd != null) return

        isLoading = true
        val adRequest = AdRequest.Builder().build()
        val adUnitId = context.getString(R.string.interstitial_ad_unit_id)

        InterstitialAd.load(
            context,
            adUnitId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoading = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoading = false
                }
            }
        )
    }

    fun showAd(activity: Activity, onAdDismissed: () -> Unit) {
        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadAd(activity)
                    onAdDismissed()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    interstitialAd = null
                    loadAd(activity)
                    onAdDismissed()
                }
            }
            ad.show(activity)
        } else {
            loadAd(activity)
            onAdDismissed()
        }
    }

    /**
     * Increment interaction counter and show ad if threshold reached
     * Returns true if ad was shown
     */
    fun incrementAndShowIfReady(activity: Activity, onAdDismissed: () -> Unit): Boolean {
        interactionCount++
        if (interactionCount >= INTERACTIONS_BEFORE_AD && interstitialAd != null) {
            interactionCount = 0
            showAd(activity, onAdDismissed)
            return true
        }
        onAdDismissed()
        return false
    }

    /**
     * Show ad on screen transition (Map <-> List)
     */
    fun showAdOnTransition(activity: Activity, onComplete: () -> Unit) {
        if (interstitialAd != null) {
            showAd(activity, onComplete)
        } else {
            loadAd(activity)
            onComplete()
        }
    }

    /**
     * Track interaction without showing ad
     */
    fun trackInteraction() {
        interactionCount++
    }

    fun resetInteractionCount() {
        interactionCount = 0
    }

    fun isAdReady(): Boolean = interstitialAd != null
}
