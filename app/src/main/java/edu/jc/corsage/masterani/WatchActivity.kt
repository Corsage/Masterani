package edu.jc.corsage.masterani

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.ContentLoadingProgressBar
import android.util.Log
import android.view.View
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_watch.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.ui.SimpleExoPlayerView

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class WatchActivity : AppCompatActivity(), Player.EventListener {
    private var player: SimpleExoPlayer? = null
    private var simpleExoPlayerView: SimpleExoPlayerView? = null

    private var videoLoadingBar: ContentLoadingProgressBar? = null

    private var mURL: String? = null

    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                        View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
    private val mShowPart2Runnable = Runnable {
        // Delayed display of UI elements
        supportActionBar?.show()
    }
    private var mVisible: Boolean = false
    private val mHideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_watch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // TEST
        val intent: Intent? = intent

        if (intent != null) {
            mURL = intent.getStringExtra("URL")
            Log.d("WatchActivity", "Recieved link: " + mURL)
        }

        mVisible = true

        // TEST
        /*
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "ExoPlayer"))
        val uri: Uri = Uri.parse(mURL)

        val dashMediaSource: DashMediaSource = DashMediaSource(uri, dataSourceFactory,
                DefaultDashChunkSource.Factory(dataSourceFactory), null, null)

        val bandwidthMeter = DefaultBandwidthMeter()
        val trackSelector: TrackSelector = DefaultTrackSelector(AdaptiveTrackSelection.Factory(bandwidthMeter))

        val simpleExoPlayer: SimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector)

        fullscreen_content.player = simpleExoPlayer
        simpleExoPlayer.prepare(dashMediaSource)
        */

        // TEST
        // 1. Create a default TrackSelector.
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

        // 2. Create a default loadControl.
        val loadControl = DefaultLoadControl()

        // 3. Create the player.
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl)
        simpleExoPlayerView = SimpleExoPlayerView(this)
        simpleExoPlayerView = findViewById(R.id.fullscreen_content)

        // 4. Set-up media control.
        simpleExoPlayerView?.useController = true
        simpleExoPlayerView?.requestFocus()

        // 5. Bind the player to view.
        simpleExoPlayerView?.player = player

        // 6. Set-up content.
        val url = Uri.parse(mURL)

        // Measures bandwidth during playback.
        val bandwidthMeterA = DefaultBandwidthMeter()

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "Masterani"), bandwidthMeterA)

        // Produces Extractor instances for parsing the media data.
        val extractorsFactory = DefaultExtractorsFactory()

        // 7. We are always dealing with live-stream.
        //val videoSource = HlsMediaSource(url, dataSourceFactory, 1, null, null)
        val videoSource = ExtractorMediaSource(url, dataSourceFactory, extractorsFactory, null, null)
        val loopingMediaSource = LoopingMediaSource(videoSource)

        // Before 8. we need to initialize our loading bar.
        videoLoadingBar = findViewById(R.id.videoLoadingBar)

        // 8. Prepare the player with the source.
        player?.prepare(loopingMediaSource)

        // 9. Set the event listener.
        player?.addListener(this)

        // Set up the user interaction to manually show or hide the system UI.
        fullscreen_content.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    /* Player event listener */

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING) {
            videoLoadingBar?.visibility = View.VISIBLE
            videoLoadingBar?.show()
        } else {
            videoLoadingBar?.hide()
            videoLoadingBar?.visibility = View.INVISIBLE
        }
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {

    }

    override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {

    }

    override fun onLoadingChanged(isLoading: Boolean) {

    }

    override fun onPlayerError(error: ExoPlaybackException?) {

    }

    override fun onPositionDiscontinuity() {

    }

    override fun onRepeatModeChanged(repeatMode: Int) {

    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {

    }

    // We use onResume to ensure that everything is hidden again after coming back to foreground.
    override fun onResume() {
        super.onResume()

        mVisible = true
    }

    override fun onPostResume() {
        super.onPostResume()
        delayedHide(100)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    private fun toggle() {
        if (mVisible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        supportActionBar?.hide()
        mVisible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable)
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    private fun show() {
        // Show the system bar
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        mVisible = true

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable)
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        mHideHandler.removeCallbacks(mHideRunnable)
        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300
    }
}
