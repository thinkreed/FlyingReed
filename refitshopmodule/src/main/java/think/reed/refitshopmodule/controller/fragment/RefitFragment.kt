package think.reed.refitshopmodule.controller.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.launch
import think.reed.refitshopmodule.R
import think.reed.refitshopmodule.mediacodec.CodecRegistry
import think.reed.refitshopmodule.mediacodec.VideoCodec

/**
 * Created by thinkreed on 2017/7/17.
 */
class RefitFragment : Fragment() {

    private lateinit var mJob :Job

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val textureView = TextureView(activity)
        textureView.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?,
                                                     width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                mJob.cancel()
                return true
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?,
                                                   width: Int, height: Int) {
                CodecRegistry.registerComponent(CodecRegistry.KEY_SURFACE,
                        Surface(surface))
                mJob = launch(CommonPool) {
                    wrapperFun()
                }
            }
        }
        return textureView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    suspend private fun wrapperFun() {
        doMediaDecode()
    }

    private fun doMediaDecode() {
        val videoCodec = CodecRegistry.getComponent(CodecRegistry.KEY_VIDEO_CODEC) as VideoCodec
        videoCodec.doDecodeMP4("/sdcard/video/dcw.mp4",
                VideoCodec.ProcessMode.SYNC_WITH_SURFACE)
    }

    private fun doLocation() {
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