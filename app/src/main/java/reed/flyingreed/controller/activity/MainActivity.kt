package reed.flyingreed.controller.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import reed.flyingreed.R
import reed.flyingreed.algo.zigZag
import reed.flyingreed.controller.fragment.EmptyFragment
import reed.flyingreed.controller.fragment.ListFragment
import reed.flyingreed.controller.fragment.LiveFragment
import reed.flyingreed.controller.fragment.MusicFragment

class MainActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        changeFragment(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("thinkreed", zigZag("PAYPALISHIRING", 3))
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
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE), 0)
            }
        }
    }

    private fun changeFragment(itemId: Int) {

        val fragment = when (itemId) {
            R.id.navigation_home -> {
                MusicFragment.getInstance()
            }
            R.id.navigation_discover -> {
                EmptyFragment.getInstance()
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
