package reed.flyingreed.controller.activity

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import reed.flyingreed.IPlayerService
import reed.flyingreed.R
import reed.flyingreed.algo.zigZag
import reed.flyingreed.controller.fragment.ListFragment
import reed.flyingreed.controller.fragment.MusicFragment
import reed.flyingreed.controller.services.PlayerService

class MainActivity : AppCompatActivity() {



    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_discover -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_user_center -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
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
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_NETWORK_STATE), 0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
