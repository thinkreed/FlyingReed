package reed.flyingreed;

import android.content.Context
import android.support.multidex.MultiDex
import android.util.Log
import cn.tongdun.android.shell.FMAgent
import cn.tongdun.bugly.Bugly
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.qihoo360.replugin.RePlugin
import com.qihoo360.replugin.RePluginApplication
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.socialize.Config
import com.umeng.socialize.PlatformConfig
import com.umeng.socialize.UMShareAPI
import java.io.IOException

/**
 * Created by thinkreed on 2017/6/18.
 */

class KotlinApplication : RePluginApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return
        }
        FMAgent.init(applicationContext, FMAgent.ENV_PRODUCTION)
        LeakCanary.install(this)
        CrashReport.initCrashReport(this, "1236da7bfe", false)
        Config.DEBUG = true
        PlatformConfig.setQQZone("1106185635", "5f8Hpw2HCTbupIT7")
        UMShareAPI.get(this)
        Bugly.init(this, "reed.flyingreed", "1.5")
        RePlugin.install("/sdcard/video/app-debug.apk")

        val aDid = generateADid(this)
        Log.i("reed","AdvertisingID:"+aDid);
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }


    companion object {
        lateinit var instance: KotlinApplication
            private set
    }

    private fun generateADid(ctx: Context): String {
        var adInfo: AdvertisingIdClient.Info? = null
        try {
            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(ctx);

        } catch (e: IOException) {
        } catch (e: GooglePlayServicesNotAvailableException) {
        } catch (e: GooglePlayServicesRepairableException) {
        } catch (e: IllegalStateException) {
        }
        return if (adInfo != null) adInfo.id else ""
    }

}