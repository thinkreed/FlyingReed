package reed.flyingreed.common.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import reed.flyingreed.R
import reed.flyingreed.R.id

/**
 * Created by thinkreed on 2017/6/17.
 */

abstract class FragmentActivity : AppCompatActivity() {

  abstract fun getFragment(): Fragment

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_single_fragment)

    loadFragment()
  }

  private fun loadFragment() {

    if (supportFragmentManager.findFragmentById(id.container) == null) {
      supportFragmentManager.beginTransaction().add(id.container, getFragment()).commit()
    }
  }
}