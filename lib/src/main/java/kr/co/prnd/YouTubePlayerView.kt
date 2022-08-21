package kr.co.prnd

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.lib.R
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerAndroidxFragment

class YouTubePlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var fragmentName: String? = "fragmentByLongHRK"
    var onInitializedListener: OnInitializedListener? = null
    private var fragmentManager: FragmentManager
    private val youTubePlayerAndroidxFragment = YouTubePlayerAndroidxFragment()
    var isPlaying = false

    init {
        LayoutInflater.from(context).inflate(R.layout.view_prnd_you_tube_player, this, true)

        fragmentManager = getFragmentManager()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, youTubePlayerAndroidxFragment)
            .commitAllowingStateLoss()
    }

    private fun getFragmentManager(): FragmentManager {
        val activityFragmentManager = getFragmentActivity().supportFragmentManager
        if (fragmentName == null) {
            return activityFragmentManager
        } else {
            for (fragment in activityFragmentManager.fragments) {
                return fragment.childFragmentManager
            }
            throw IllegalArgumentException("[$fragmentName] can not found. Please check your fragment name")
        }
    }

    private fun getFragmentActivity(): FragmentActivity {
        var targetContext = context
        var targetActivity: Activity? = null
        while (targetContext is ContextWrapper) {
            if (targetContext is Activity) {
                targetActivity = targetContext
                break
            }
            targetContext = targetContext.baseContext
        }
        return (targetActivity as? FragmentActivity)
            ?: throw IllegalArgumentException("You have to extend FragmentActivity or AppCompatActivity")
    }

    fun play(videoId: String, listener: OnInitializedListener? = null) {
        listener?.let { this.onInitializedListener = it }
        isPlaying = true
        LayoutInflater.from(context).inflate(R.layout.view_prnd_you_tube_player, this, true)

        fragmentManager = getFragmentManager()
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, youTubePlayerAndroidxFragment!!)
            .commitAllowingStateLoss()

        youTubePlayerAndroidxFragment!!.initialize(
            javaClass.simpleName,
            object : YouTubePlayer.OnInitializedListener {
                override fun onInitializationSuccess(
                    provider: YouTubePlayer.Provider,
                    player: YouTubePlayer,
                    wasRestored: Boolean
                ) {
                    player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
                    player.loadVideo(videoId)
                    Log.d("TAG", "onInitializationSuccess: ")
                    onInitializedListener?.onInitializationSuccess(provider, player, wasRestored)
                }

                override fun onInitializationFailure(
                    provider: YouTubePlayer.Provider,
                    result: YouTubeInitializationResult
                ) {
                    Log.d("TAG", "onInitializationFailure: $result")
                    onInitializedListener?.onInitializationFailure(provider, result)
                }
            })
    }

    fun onDestroyView() {
        isPlaying = false
        youTubePlayerAndroidxFragment!!.onDestroy()
    }

    interface OnInitializedListener {
        fun onInitializationSuccess(
            provider: YouTubePlayer.Provider,
            player: YouTubePlayer,
            wasRestored: Boolean
        )

        fun onInitializationFailure(
            provider: YouTubePlayer.Provider,
            result: YouTubeInitializationResult
        )
    }
}