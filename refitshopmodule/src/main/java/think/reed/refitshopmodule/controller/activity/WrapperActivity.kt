package think.reed.refitshopmodule.controller.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import think.reed.refitshopmodule.R
import think.reed.refitshopmodule.controller.fragment.RefitFragment

/**
 * Created by thinkreed on 2017/7/17.
 */
class WrapperActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.refitshop_activity_single_fragment)
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
        }
        var fragment = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment == null) {
            fragment = RefitFragment.getInstance()
            supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
        }
    }
}