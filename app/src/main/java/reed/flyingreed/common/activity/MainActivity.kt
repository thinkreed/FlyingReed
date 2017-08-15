package reed.flyingreed.common.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import reed.flyingreed.R
import reed.flyingreed.common.component.DataFetcher
import reed.flyingreed.common.fragment.ListFragment
import reed.flyingreed.live.LiveFragment
import reed.flyingreed.music.MusicFragment

class MainActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        changeFragment(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launch(CommonPool) {
            DataFetcher.getHttpData()
        }
        checkPermission()

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment == null) {
            fragment = MusicFragment.getInstance()
            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
        }
    }

    private fun checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.CAMERA), 0)
            }
        }
    }

    private fun changeFragment(itemId: Int) {

        val fragment = when (itemId) {
            R.id.navigation_home -> {
                MusicFragment.getInstance()
            }
            R.id.navigation_discover -> {
                ListFragment.instance
            }
            R.id.navigation_user_center -> {
                LiveFragment.getInstance()
            }
            else -> throw IllegalArgumentException("not support page")
        }
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
