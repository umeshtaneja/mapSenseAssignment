package com.example.mapsense

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.MenuItemCompat
import com.example.mapsense.common.NetworkCheck
import com.example.mapsense.common.dialogs.BottomSheetDialog
import com.example.mapsense.data.model.WeatherResponse
import com.example.mapsense.uiLayer.MainViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

const val REQUEST_CODE_LOCATION = 123

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var myMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double? = null
    private var longitude: Double? = null

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment =
            this.supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

        setObservers()

    }

    private fun setObservers() {
        viewModel.weatherLiveData.observe(this) {
            it?.let {
                if (it.data != null) {
                    showAlertDialog(it.data)
                } else {
                    Toast.makeText(
                        applicationContext,
                        it.error,
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
        }
    }

    private fun showAlertDialog(data: WeatherResponse?) {
        val bottomSheet = BottomSheetDialog(data)
        bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
    }


    override fun onMapReady(map: GoogleMap) {
        myMap = map
        enableMyLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(REQUEST_CODE_LOCATION)
    private fun enableMyLocation() {
        if (hasLocationPermission()) {
            myMap.isMyLocationEnabled = true
            getLastLocation()
        } else {
            EasyPermissions.requestPermissions(
                this, getString(R.string.location),
                REQUEST_CODE_LOCATION, ACCESS_FINE_LOCATION
            )
        }
    }

    private fun updateCameraPosition(latitude: Double, longitude: Double) {
        val myLocation = LatLng(latitude, longitude)
        myMap.addMarker(
            MarkerOptions()
                .position(myLocation)
        )
        myMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(
                applicationContext,
                "Location permission required to show weather updates",
                Toast.LENGTH_LONG
            ).show()
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    latitude = it.latitude
                    longitude = it.longitude
                    if (latitude != null && longitude != null) {
                        updateCameraPosition(latitude!!, longitude!!)
                        getWeatherDataFromAPI(latitude!!, longitude!!)
                    }
                } ?: run {
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    applicationContext,
                    "Failed to get location: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun getWeatherDataFromAPI(latitude: Double, longitude: Double) {
       if(NetworkCheck.checkConnectivity(this)) {
           viewModel.getWeatherData(latitude, longitude)
        }else{
            Toast.makeText(this,"Please connect to a network",Toast.LENGTH_LONG).show()
        }
    }


    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchViewItem = menu.findItem(R.id.app_bar_search)
        val searchView: SearchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchView.clearFocus()
                if(NetworkCheck.checkConnectivity(applicationContext)) {
                    viewModel.getWeatherDataForCityName(query.toString())
                }else{
                    Toast.makeText(applicationContext,"Please connect to a network",Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

}