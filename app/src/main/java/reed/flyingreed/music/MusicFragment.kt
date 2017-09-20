package reed.flyingreed.music

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_music.friday
import kotlinx.android.synthetic.main.fragment_music.monday
import kotlinx.android.synthetic.main.fragment_music.thursday
import kotlinx.android.synthetic.main.fragment_music.tuesday
import kotlinx.android.synthetic.main.fragment_music.wednesday
import reed.flyingreed.IPlayerService
import reed.flyingreed.R
import reed.flyingreed.common.model.Const
import reed.flyingreed.common.model.Week
import reed.flyingreed.common.model.Week.FRIDAY
import reed.flyingreed.common.model.Week.MONDAY
import reed.flyingreed.common.model.Week.THURSDAY
import reed.flyingreed.common.model.Week.TUESDAY
import reed.flyingreed.common.model.Week.WEDNESDAY
import reed.flyingreed.common.services.PlayerService
import reed.flyingreed.music.activity.MusicPlayerActivity

/**
 * Created by thinkreed on 2017/6/30.
 */

class MusicFragment : Fragment(), View.OnClickListener {

  private lateinit var mPlayerService: IPlayerService
  private lateinit var mBlinkingView: View

  private val mHandler by lazy {
    Handler()
  }

  private val mServiceConnection by lazy {
    object : ServiceConnection {
      override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val musicService = IPlayerService.Stub.asInterface(service)
        if (musicService != null) {
          mPlayerService = musicService
        }
      }

      override fun onServiceDisconnected(name: ComponentName?) {
      }
    }
  }


  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
      savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.fragment_music, container,
      false)

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    view?.let {
      setOnClickListeners()
      bindToMusicService()
    }
  }

  private fun setOnClickListeners() {
    monday.setOnClickListener(this)
    tuesday.setOnClickListener(this)
    wednesday.setOnClickListener(this)
    thursday.setOnClickListener(this)
    friday.setOnClickListener(this)
  }

  private fun bindToMusicService(): Boolean {
    val intent = Intent(activity, PlayerService::class.java)
    return activity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE)
  }

  override fun onClick(v: View?) {
    v?.let {
      openPlayer(selectFavor(v))
    }
  }

  private fun selectFavor(v: View): Int {
    when (v.id) {
      R.id.monday -> {
        return MONDAY.ordinal
      }
      R.id.tuesday -> {
        return TUESDAY.ordinal

      }
      R.id.wednesday -> {
        return WEDNESDAY.ordinal
      }
      R.id.thursday -> {
        return THURSDAY.ordinal
      }
      R.id.friday -> {
        return FRIDAY.ordinal
      }
      else -> return MONDAY.ordinal
    }
  }

  private fun openPlayer(week: Int) {
    val intent = Intent(this.context, MusicPlayerActivity::class.java)
    val bundle = Bundle()
    bundle.putInt(Const.KEY_WEEK, week)
    intent.putExtras(bundle)
    startActivity(intent)
  }

  override fun onResume() {
    super.onResume()
    try {
      if (mPlayerService.isPlaying) {
        mBlinkingView = getBlinkingView()
        mHandler.post(ShiningTask())
      }
    } catch (e: UninitializedPropertyAccessException) {
      e.printStackTrace()
    }


  }

  private fun getBlinkingView(): View {
    when (Week.values()[mPlayerService.favor]) {
      MONDAY -> {
        return monday
      }
      TUESDAY -> {
        return tuesday
      }
      WEDNESDAY -> {
        return wednesday
      }
      THURSDAY -> {
        return thursday
      }
      FRIDAY -> {
        return friday
      }
    }
  }

  override fun onStop() {
    //reset the blinking view
    try {
      mBlinkingView.animate()?.alpha(1.0f)?.duration = 0
    } catch (e: UninitializedPropertyAccessException) {
      e.printStackTrace()
    }
    mHandler.removeCallbacksAndMessages(null)
    super.onStop()
  }

  override fun onDestroyView() {
    activity.unbindService(mServiceConnection)
    super.onDestroyView()
  }

  override fun onDestroy() {
    super.onDestroy()
  }

  inner class ShiningTask : Runnable {
    override fun run() {
      try {
        val alpha = if (mBlinkingView.alpha == 0f) 1f else 0f
        mBlinkingView.animate().alpha(alpha).duration = 1000
      } catch (e: UninitializedPropertyAccessException) {
        e.printStackTrace()
      }

      //set the delay time a little longer than duration to ensure animation ended
      mHandler.postDelayed(this, 1100)
    }
  }

  companion object {
    fun getInstance(): MusicFragment {
      return MusicFragment()
    }
  }
}