package com.example.googlemapsapplication.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.VectorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.googlemapsapplication.R
import com.example.googlemapsapplication.Repository.ObstacleLocation
import com.example.googlemapsapplication.databinding.ActivityMapsBinding
import com.example.googlemapsapplication.utils.InjectorUtils
import com.example.googlemapsapplication.utils.SessionManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException


class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivityMapsBinding
    private lateinit var searchView: SearchView

    private var locationPermissionGranted = false
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null
    private lateinit  var sessionManager : SessionManager
    private lateinit var obstacleLocation: ObstacleLocation

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
        private val TAG = MapsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 18
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        sessionManager = SessionManager(this)

        super.onCreate(savedInstanceState)

        val viewModel = MapsViewModel(InjectorUtils.mapRepository())

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        searchView = findViewById(R.id.sv_location)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val location = searchView.query.toString()
                var addressList: List<Address> = emptyList()

                if (location != "") {
                    mMap.clear()

                    val geocoder = Geocoder(applicationContext)
                    try {
                        addressList = geocoder.getFromLocationName(location, 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    val address = addressList[0]
                    val latLng = LatLng(address.latitude, address.longitude)
                    mMap.addMarker(MarkerOptions().position(latLng).title(location))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                }

                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }
        })
        if(sessionManager.fetchAuthToken().isNullOrEmpty()){
            showLogin()
        }else{
            showLogout()
        }

        // Retrieve the content view that renders the map.
        //setContentView(R.layout.activity_maps)
        binding.loginBtn.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            showLogout()
        }



        binding.logoutBtn.setOnClickListener{
           logout()
            showLogin()
        }
    }

    private fun showLogin(){
        binding.loginBtn.visibility = View.VISIBLE
        binding.logoutBtn.visibility = View.GONE
    }

    private fun showLogout(){
        binding.loginBtn.visibility = View.GONE
        binding.logoutBtn.visibility = View.VISIBLE
    }


    private fun logout(){
        sessionManager.clearAuthToken()
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            mMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        mMap.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        mMap.uiSettings.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
        updateLocationUI()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)





        mMap.setOnMapLongClickListener {location ->
            mMap.clear()
            mMap.addMarker(MarkerOptions().position(location))

            val button: Button = findViewById(R.id.btn_report)
            button.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    // Code here executes on main thread after user presses button
                    onHTTPRequest(location)
                    mMap.clear()
                }
            })

        }

        mMap.setOnMapClickListener {
            mMap.clear()
        }

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI()

        // Get the current location of the device and set the position of the map.
        getDeviceLocation()



        setUpMap()
    }

    private fun onHTTPRequest(location: LatLng){

        val roadLat = location.latitude.toString()
        val roadLong = location.longitude.toString()
        var full = roadLat.plus(",").plus(roadLong)
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://roads.googleapis.com/v1/nearestRoads?points=$full&key=AIzaSyD9y9En0zDS3fxSll0-CL8hFBzhH9lNLqg")
            .method("GET", null)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val strResponse = response.body?.string().toString()

                    var gson = Gson()
                    obstacleLocation = gson.fromJson(strResponse, ObstacleLocation::class.java)
                    val obstacleLatitude = obstacleLocation.snappedPoints[0].location.latitude
                    val obstacleLongitude = obstacleLocation.snappedPoints[0].location.longitude
                    val obstacleLatLng = LatLng(obstacleLatitude,obstacleLongitude)

                    Log.w("RESPONSE", obstacleLatLng.toString())

                    this@MapsActivity.runOnUiThread {
                        mMap.addMarker(MarkerOptions().position(obstacleLatLng)
                            .title("Obstacle")
                            .snippet("Hey, this is an obstacle!!!!")
                            .icon(getBitmapDescriptor(R.drawable.ic_baseline_wrong_location)))
                    }

                }
            }
        })
    }



    private fun getBitmapDescriptor(id: Int): BitmapDescriptor? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = getDrawable(id) as VectorDrawable?
            val h = vectorDrawable!!.intrinsicHeight
            val w = vectorDrawable.intrinsicWidth
            vectorDrawable.setBounds(0, 0, w, h)
            val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)
            vectorDrawable.draw(canvas)
            BitmapDescriptorFactory.fromBitmap(bm)
        } else {
            BitmapDescriptorFactory.fromResource(id)
        }
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
            return
        }

        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                val button: Button = findViewById(R.id.btn_report)
                button.setOnClickListener(object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        // Code here executes on main thread after user presses button
                        onHTTPRequest(currentLatLong)
                        mMap.clear()
                    }
                })
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 18f))
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)

    }

    override fun onMarkerClick(p0: Marker) = false
}

