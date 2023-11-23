package ma.ensa.authenti_maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ma.ensa.authenti_maps.databinding.ActivityMapsEnsaBinding

class MapsEnsa : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsEnsaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsEnsaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment

        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        addENSAUniversityMarker("ENSA Berrechid", 33.26553, -7.58754)
        //kenetra
        addENSAUniversityMarker("ENSA Knetra",34.2459295, -6.5908128 )
        //Rabat
        addENSAUniversityMarker("ENSA Rabat", 34.0102537, -6.8477147)
        //fes
        addENSAUniversityMarker("ENSA Fes", 33.99648931618568, -4.991668001133071)
        //
        addENSAUniversityMarker("ENSA Berrechid", 33.26553, -7.58754)
        //tetouan
        addENSAUniversityMarker("ENSA Tetouan", 35.57845, -5.36837)
        //Beni Mellal
        addENSAUniversityMarker("ENSA Beni Mellal", 32.334193, -6.353335)







        val fesLatLng = LatLng(34.0430, -5.0033) // Replace with the coordinates of Fes
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fesLatLng, 10f))
    }
    private fun addENSAUniversityMarker(name: String, latitude: Double, longitude: Double) {
        val ensaLocation = LatLng(latitude, longitude)
        mMap.addMarker(MarkerOptions().position(ensaLocation).title(name))
    }
}