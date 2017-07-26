package think.reed.refitshopmodule.controller.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import think.reed.refitshopmodule.R

/**
 * Created by thinkreed on 2017/7/17.
 */
class RefitFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.refitshop_fragment_wrapper, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            val locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    Log.d("thinkreed", "latitude:" + location.latitude +
                            " longitude:" + location.longitude)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}

                override fun onProviderEnabled(provider: String) {}

                override fun onProviderDisabled(provider: String) {}
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    0.toFloat(), locationListener)

            val lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            Log.d("thinkreed", "last latitude:" + lastLocation?.latitude +
                    " last longitude:" + lastLocation?.longitude)


        }

    }

    companion object {
        fun getInstance(): RefitFragment {
            return RefitFragment()
        }
    }
}