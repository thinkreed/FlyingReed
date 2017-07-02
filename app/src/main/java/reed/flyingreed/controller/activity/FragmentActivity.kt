package reed.flyingreed.controller.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import reed.flyingreed.R
import reed.flyingreed.controller.fragment.ListFragment

/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class FragmentActivity : AppCompatActivity() {

    abstract fun getFragment(): Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_fragment)

        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment == null) {
            fragment = getFragment()
            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
        }
    }
}