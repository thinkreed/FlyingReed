package reed.flyingreed;

import android.app.Application

/**
 * Created by thinkreed on 2017/6/18.
 */

class KotlinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: KotlinApplication
            private set
    }
}