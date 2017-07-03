package reed.flyingreed;

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI

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
        Config.DEBUG = true
        PlatformConfig.setQQZone("1106185635", "5f8Hpw2HCTbupIT7")
        UMShareAPI.get(this)
    }


    companion object {
        lateinit var instance: KotlinApplication
            private set
    }
}