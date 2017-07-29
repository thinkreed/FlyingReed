package reed.flyingreed.experimental.camera

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.PopupWindow
import kotlinx.android.synthetic.main.fragment_camera2.*
import kotlinx.coroutines.experimental.newCoroutineContext
import reed.flyingreed.R

/**
 * Created by thinkreed on 2017/7/27.
 */
class Camera2Fragment : Fragment() {

    private var mState = State.IDLE
    private var mWorkerThread: HandlerThread? = null
    private var mHandler: Handler? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_camera2, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        record.setOnClickListener {
            when (mState) {
                State.IDLE -> {
                    startRecordVideo()
                }
                State.RECODING -> {
                    stopRecordVideo()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        startWorker()
    }

    override fun onPause() {
        super.onPause()
        stopWorker()
    }

    private fun startRecordVideo() {

    }

    private fun stopRecordVideo() {

    }

    private fun startWorker() {
        val worker = HandlerThread("camera_worker")
        mWorkerThread = worker
        worker.start()
        mHandler = Handler(worker.looper)
    }

    private fun stopWorker() {
        val worker = mWorkerThread
        if (worker != null) {
            worker.quitSafely()
            worker.join()
            mWorkerThread = null
            mHandler = null
        }
    }

    companion object {
        fun getInstance(): Camera2Fragment {
            return Camera2Fragment()
        }
    }

    enum class State {
        RECODING, IDLE
    }
}