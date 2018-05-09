package reed.flyingreed.common.activity

import android.Manifest
import android.Manifest.permission
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import reed.flyingreed.R
import reed.flyingreed.R.id
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
        checkPermission()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        loadFragment()
    }

    private fun loadFragment() {

        if (supportFragmentManager.findFragmentById(id.container) == null) {
            supportFragmentManager.beginTransaction().add(id.container,
                    MusicFragment.getInstance()).commit()
        }
    }

    private fun checkPermission() {
        if (isNeedRequestPermission()) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.CAMERA), 0)
        }
    }

    private fun isNeedRequestPermission(): Boolean {
        return VERSION.SDK_INT >= VERSION_CODES.M && checkNeededPermissions()
    }

    private fun checkNeededPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(this,
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) or (ActivityCompat.checkSelfPermission(
                this,
                permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) or (ActivityCompat.checkSelfPermission(
                this,
                permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) or (ActivityCompat.checkSelfPermission(
                this, permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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
