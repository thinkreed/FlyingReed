package reed.flyingreed;

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import reed.flyingreed.controller.services.PlayerService

/**
 * Created by thinkreed on 2017/6/18.
 */

class KotlinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        LeakCanary.install(this)
        CrashReport.initCrashReport(this, "1236da7bfe", false)
    }


    companion object {
        lateinit var instance: KotlinApplication
            private set
    }
}