package com.example.housify

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.Manifest
import android.annotation.SuppressLint

import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.tomtom.sdk.common.SystemClock
import com.tomtom.sdk.location.GeoLocation
import com.tomtom.sdk.location.GeoPoint
import com.tomtom.sdk.map.display.MapOptions
import com.tomtom.sdk.map.display.TomTomMap
import com.tomtom.sdk.map.display.camera.CameraOptions
import com.tomtom.sdk.map.display.camera.CameraTrackingMode
import com.tomtom.sdk.map.display.gesture.GestureType
import com.tomtom.sdk.map.display.image.ImageFactory
import com.tomtom.sdk.map.display.location.LocationAccuracyPolicy
import com.tomtom.sdk.map.display.location.LocationMarkerOptions
import com.tomtom.sdk.map.display.marker.Marker
import com.tomtom.sdk.map.display.marker.MarkerOptions
import com.tomtom.sdk.map.display.style.StyleMode
import com.tomtom.sdk.map.display.ui.MapFragment
import com.tomtom.sdk.search.Search
import com.tomtom.sdk.search.SearchCallback
import com.tomtom.sdk.search.SearchOptions
import com.tomtom.sdk.search.SearchResponse
import com.tomtom.sdk.search.common.error.SearchFailure
import com.tomtom.sdk.search.online.OnlineSearch
import kotlin.properties.Delegates
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds

class propertyInfoActivity : AppCompatActivity() {

    private lateinit var propertyLocation: TextView
    private lateinit var propertyTitle: TextView
    private lateinit var propertyBedrooms: TextView
    private lateinit var propertyBaths: TextView
    private lateinit var propertyType: TextView
    private lateinit var propertyYear: TextView
    private lateinit var propertyRent: TextView
    private lateinit var propertyArea: TextView
    private lateinit var propertyImages: ImageView
    private lateinit var propertyUid: TextView
    private lateinit var addUserToCurrentUserCollection: ImageView
    private lateinit var propUid: String
    private lateinit var phoneNumber: String

    private lateinit var tomTomMap: TomTomMap
    private lateinit var userName : TextView
    private lateinit var callToUser : ImageView
    private lateinit var zoomIn : ImageView
    private var sentFirstName: String = ""
    private var sentLastName: String = ""
    private var sentNumber: String = ""
    private var sentUserImage: String = ""
    private var sentUserUid: String = ""

    private var propertyLongitude:Double = 0.0
    private var propertyLatitude:Double = 0.0
    private var propertyLocationAddressPinned: String=""

    private lateinit var zoomOut : ImageView


    private lateinit var phoneNumbeListedWithUser : TextView
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_property_info)
        propertyTitle = findViewById(R.id.propertyTitle)
        propertyLocation = findViewById(R.id.propertyLocation)
        propertyBedrooms = findViewById(R.id.propertyBedrooms)
        propertyBaths = findViewById(R.id.propertyBaths)
        propertyType = findViewById(R.id.propertyType)
        propertyYear = findViewById(R.id.propertyYear)
        propertyArea = findViewById(R.id.propertyArea)
        propertyRent = findViewById(R.id.propertyRent)
        propertyImages = findViewById<ImageView>(R.id.propertyImages)
        propertyUid = findViewById(R.id.propertyUidInfo)
        addUserToCurrentUserCollection = findViewById(R.id.chatUserNow)
        userName = findViewById(R.id.usernameInfoPage)
        callToUser = findViewById(R.id.phoneUser)
        phoneNumbeListedWithUser = findViewById(R.id.propertyUserListedPhoneNumber)
        val searchApi = OnlineSearch.create(this, "eXRlAZJos3TBi0kr7fSrXrp8Kl7Nt1e8")


        val extras = intent.extras
        if (extras != null) {
            val propName = extras.getString("propertyName")
            val propLocation = extras.getString("propertyLocation")

            propUid = extras.getString("propertyUid").toString()
            val propUserUid = extras.getString("userUid").toString()
            propertyTitle.text = propName
            propertyBedrooms.text = "3"
            propertyLocation.text = propLocation
            propertyUid.text = propUid
            Toast.makeText(this,"$propUserUid",Toast.LENGTH_LONG).show()
            phoneNumber= getUserNameFromUid(propUserUid)


            Toast.makeText(this,"$phoneNumber",Toast.LENGTH_LONG).show()

            callToUser.setOnClickListener{
                if (phoneNumbeListedWithUser.text.isNotEmpty()) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.CALL_PHONE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:$phoneNumber")
                        startActivity(intent)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CALL_PHONE),
                            100
                        )
                    }
                } else {
                    Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show()
                }
            }



            val propertyLocation = propLocation
            val options = propertyLocation?.let { SearchOptions(query = it, limit = 1) }

            if (options != null) {
                val search = searchApi.search(options, object : SearchCallback {

                    override fun onSuccess(result: SearchResponse) {
                        if (result.results.isNotEmpty()) {
                            val firstResult = result.results.first()
                            val place = firstResult.place

                            if (place != null && place.coordinate != null) {
                                val latitude = place.coordinate.latitude
                                val longitude = place.coordinate.longitude
                                propertyLatitude=latitude
                                propertyLongitude =longitude
                                propertyLocationAddressPinned = propLocation
                                setupMap()

                                Log.d("TomTom", "Latitude: $latitude, Longitude: $longitude")
                                }

//                            val location = firstResult.position
//                            val latitude = location.latitude
//                            val longitude = location.longitude

//                            Log.d("TomTom", "Latitude: $latitude, Longitude: $longitude")
                        } else {

                        }
                    }

                    override fun onFailure(failure: SearchFailure) {
                    }
                })
            }


            propUid?.let { propertyUid ->
                FirebaseFirestore.getInstance().collection("Properties").document(propUid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            var userImageBase64 = document.getString("userPropertyImages")

                            if (!userImageBase64.isNullOrBlank()) {
                                var decodedImage = decodeBase64ToBitmap(userImageBase64)
                                propertyImages.setImageBitmap(decodedImage)
                            }
                        } else {
                            Log.d("Property View Info", "Property details not found")
                        }

//                        Property Details
                        propertyBaths.text = document.getString("propertyBathrooms")
                        propertyBedrooms.text = document.getString("propertyBedrooms")
                        propertyArea.text = document.getString("propertyArea")
                        propertyRent.text = document.getString("propertyPrice")

                    }.addOnFailureListener { exception ->
                        Log.e("Property View Info", "Error getting property details", exception)
                    }



            }
            addUserToCurrentUserCollection.setOnClickListener {
                sentNumber = getUserNameFromUid(propUserUid)
                val intent = Intent(this, chatUserActivity::class.java)

                intent.putExtra("sentFirstName", sentFirstName)
                intent.putExtra("sentLastName", sentLastName)
                intent.putExtra("sentNumber", sentNumber)
                intent.putExtra("sentUserImage", sentUserImage)
                intent.putExtra("sentUserUid", sentUserUid)

                startActivity(intent)


            }
        }
    }

    private fun setupMap() {
        val mapOptions = MapOptions(mapKey = "eXRlAZJos3TBi0kr7fSrXrp8Kl7Nt1e8")
        val mapFragment = MapFragment.newInstance(mapOptions)

        supportFragmentManager.beginTransaction()
            .replace(R.id.map_fragment, mapFragment)
            .commit()
        val amsterdam = GeoPoint(propertyLatitude, propertyLongitude)
        val markerOptions = MarkerOptions(
            coordinate = amsterdam,
            pinImage = ImageFactory.fromResource(R.drawable.ic_location_marker)
        )
        mapFragment.getMapAsync{
                tomtomMap ->
            this@propertyInfoActivity.tomTomMap = tomtomMap
            tomTomMap.setFrameRate(24)
            tomTomMap.setStyleMode(StyleMode.DARK)
            val layers = tomTomMap.layers
            tomTomMap.showHillShading()
            val mapLocationProvider = tomTomMap.getLocationProvider()
            val isLocationInVisibleArea = tomTomMap.isCurrentLocationInMapBoundingBox
            val currentLocation: GeoLocation? = tomTomMap.currentLocation
            val locationMarkerOptions = LocationMarkerOptions(
                type = LocationMarkerOptions.Type.Chevron
            )
            tomTomMap.enableLocationMarker(locationMarkerOptions)
            tomTomMap.locationAccuracyPolicy = LocationAccuracyPolicy { location: GeoLocation ->
                val isAccurate = (location.accuracy?.inMeters() ?: 0.0) < 100.0
                val isFresh = location.elapsedRealtimeNanos.nanoseconds >
                        (SystemClock.elapsedRealtimeNanos().nanoseconds  - 60.seconds)

                isAccurate && isFresh
            }

            tomtomMap.addMapClickListener { coordinate: GeoPoint ->

                val newCameraOptions = CameraOptions(
                    position = amsterdam,
                    zoom = 15.0,
                )
                tomTomMap.moveCamera(newCameraOptions)
                return@addMapClickListener true
            }
            tomtomMap.addMapDoubleClickListener { coordinate: GeoPoint ->
                val newCameraOptions = CameraOptions(
                    position = amsterdam,
                    zoom = 20.0,
                )
                tomTomMap.moveCamera(newCameraOptions)
                return@addMapDoubleClickListener true
            }

            tomtomMap.addMapLongClickListener { coordinate: GeoPoint ->
                val newCameraOptions = CameraOptions(
                    position = amsterdam,
                    zoom = 10.0,
                )
                tomTomMap.moveCamera(newCameraOptions)
                return@addMapLongClickListener true
            }
            tomtomMap.changeGestureExclusions(GestureType.Move, setOf(GestureType.Scale))
            tomtomMap.changeGestureExclusions(GestureType.Move, setOf())
            val cameraPosition = tomTomMap.cameraPosition

            val markerOptions = MarkerOptions(
                coordinate = amsterdam,
                pinImage = ImageFactory.fromResource(R.drawable.ic_location_marker),
                balloonText = "$propertyLocationAddressPinned"
            )
            this.tomTomMap.addMarker(markerOptions)
            tomTomMap.addMarkerClickListener { marker: Marker ->
                if (!marker.isSelected()) {
                    marker.select()
                }}

            val amsterdam = GeoPoint(propertyLatitude, propertyLongitude)
            val newCameraOptions = CameraOptions(
                position = amsterdam,
                zoom = 5.0,
            )
            tomTomMap.moveCamera(newCameraOptions)

        }

    }

    private fun createChatDocument(currentUserUid: String, selectedUserUid: String) {
            var chatCollection = FirebaseFirestore.getInstance().collection("ChatsCollection")
            var chatData = hashMapOf(
                "UserParticipants" to arrayListOf(currentUserUid, selectedUserUid),
                "messageUserUid" to getUniqueMessageUserUid(currentUserUid, selectedUserUid),
                "timestamp" to FieldValue.serverTimestamp()
            )
//        chatCollection.add(chatData).addOnSuccessListener {
//            document-> Log.d("propertyInfoActivity","Chat Document created")
//            chatCollection.document(document.id).update("UserParticipants",FieldValue.arrayUnion(selectedUserUid))
//                .addOnSuccessListener { Log.d("propertyInfoActivity","User added to chat successfully")
//                chatCollection.document(document.id).update("UserParticipants",FieldValue.arrayUnion(currentUserUid)).
//                addOnSuccessListener { Log.d("propertInfoActivity","User added to chat") }
//                    .addOnFailureListener{
//                        exception ->
//                    Log.e("propertyInfoActivity","User add failure",exception)}}
//                .addOnFailureListener{
//                        exception ->
//                    Log.e("propertyInfoActivity","User add failure",exception)
//                }
//
//        }.addOnFailureListener{exception ->
//            Log.e("propertyInfoActivity","User add failure",exception)}

            chatCollection.whereArrayContains("UserParticipants", currentUserUid)
                .whereArrayContains("UserParticipants", selectedUserUid)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        chatCollection.add(chatData)
                            .addOnSuccessListener { document ->
                                Log.d("propertyInfoActivity", "Chat Document Created")
                                val messageUserUid = chatData["messageUserUid"].toString()
                                chatCollection.document(document.id)
                                    .update(
                                        "UserParticipants",
                                        FieldValue.arrayUnion(selectedUserUid, currentUserUid)
                                    )
                                    .addOnSuccessListener {
                                        Log.d(
                                            "propertyInfoActivity",
                                            "User added to chat successfully"
                                        )
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("propertyInfoActivity", "User add failure", exception)
                                    }
                            }
                    } else {
                        Log.d("propertyInfoActivity", "Chat Document Already Exists")

                    }
                }.addOnFailureListener { exception ->
                    Log.e("propertyInfoActivity", "Error checking chat document")
                }
        }

        private fun getUniqueMessageUserUid(
            currentUserUid: String,
            selectedUserUid: String
        ): String {
            return "${currentUserUid}_${selectedUserUid}_${System.currentTimeMillis()}"
        }

        private fun getUserNameFromUid(uid: String):String {
            val firestore = FirebaseFirestore.getInstance()
            val usersCollection = firestore.collection("User")
            var userPhoneNumber = ""
            if (uid != null) {
                usersCollection.document(uid).get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        Toast.makeText(this,"It Enters.",Toast.LENGTH_LONG).show()

                        val userFirstName = documentSnapshot.getString("firstName")
                        val userLastName = documentSnapshot.getString("lastName")
                        sentFirstName = userFirstName.toString()
                        sentLastName = userLastName.toString()
                        sentNumber = documentSnapshot.getString("number").toString()
                        sentUserImage = documentSnapshot.getString("userImage").toString()
                        sentUserUid  = documentSnapshot.getString("uid").toString()
                        userPhoneNumber= documentSnapshot.getString("number").toString()
                        phoneNumbeListedWithUser.text = userPhoneNumber
                        userName.text = userFirstName + " "+ userLastName
                        Toast.makeText(this,"User Name:  $userFirstName, userPhoneNumber: $userPhoneNumber ",Toast.LENGTH_LONG).show()
                        propertyUid.text = userFirstName
                    }
                }
                    return userPhoneNumber
            }
            return userPhoneNumber
        }

    private fun addToChatCluster(currentUserUid: String, selectedUserUid: String) {
        var chatUserClusterCollection =
            FirebaseFirestore.getInstance().collection("ChatUserCluster")
        chatUserClusterCollection.document(currentUserUid)
            .update("connectedUsers", FieldValue.arrayUnion(selectedUserUid))
            .addOnSuccessListener {
                Log.d(
                    "propertyInfoActivity",
                    "User $selectedUserUid added to the following $currentUserUid cluster successfully."
                )
                chatUserClusterCollection.document(selectedUserUid)
                    .update("connectedUsers", FieldValue.arrayUnion(currentUserUid))
                    .addOnSuccessListener {
                        Log.d(
                            "propertyInfoActivity",
                            "User $currentUserUid added to the following $selectedUserUid cluster successfully."
                        )
                    }.addOnFailureListener { exception ->
                        Log.e(
                            "propertyInfoActivity",
                            "Errorr in adding user to the chat cluster",
                            exception
                        )
                    }
            }
            .addOnFailureListener { exception ->
                Log.e("propertyInfoActivity", "Error in adding user to the chat cluster", exception)
            }
    }
    private fun decodeBase64ToBitmap(base64:String): Bitmap {
        var decodeByteArray = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodeByteArray,0,decodeByteArray.size)
    }

}





