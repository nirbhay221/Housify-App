package com.example.housify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.housify.fragments.allPropertiesPostedFragment
import com.example.housify.fragments.filteredPropertiesPostedFragment
import com.google.android.material.chip.Chip

class searchFilterActivity : AppCompatActivity() {
    private lateinit var showResultsButton: Button
    private lateinit var House:Chip
    private lateinit var Apartment: Chip
    private lateinit var Condo:Chip
    private lateinit var Cabin : Chip
    private lateinit var Other: Chip
    private lateinit var AnyPrice: Chip
    private lateinit var Monthly: Chip
    private lateinit var Anually: Chip
    private lateinit var AnyFacilities: Chip
    private lateinit var Kitchen: Chip
    private lateinit var SelfCheckIn: Chip
    private lateinit var freeParking:Chip
    private lateinit var airConditioner: Chip
    private lateinit var security : Chip
    private lateinit var furnished: Chip
    private lateinit var television:Chip

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_filter)

        showResultsButton= findViewById(R.id.showResultsButton)
        House = findViewById(R.id.chipHouse)
        Apartment = findViewById(R.id.chipApartment)
        Condo = findViewById(R.id.chipCondo)
        Cabin = findViewById(R.id.chipCabin)
        Other = findViewById(R.id.chipOther)
        AnyPrice = findViewById(R.id.chipAnyPrice)
        Monthly = findViewById(R.id.chipMonthly)
        Anually = findViewById(R.id.chipAnually)
        AnyFacilities = findViewById(R.id.chipAnyFacilities)
        Kitchen = findViewById(R.id.chipKitchen)
        SelfCheckIn = findViewById(R.id.chipSelfCheckIn)
        freeParking = findViewById(R.id.chipFreeParking)
        airConditioner = findViewById(R.id.chipAirConditioner)
        security = findViewById(R.id.chipSecurity)
        furnished = findViewById(R.id.chipFurnished)
        television = findViewById(R.id.chipTV)

        showResultsButton.setOnClickListener{
            var selectedPropertyTypes = mutableListOf<String>()

            var selectedPropertyMoneyTypes = mutableListOf<String>()

            var selectedPropertyFacilities = mutableListOf<String>()

            if(House.isChecked){
                selectedPropertyTypes.add("House")
            }
            if(Apartment.isChecked){
                selectedPropertyTypes.add("Apartment")
            }
            if(Condo.isChecked){
                selectedPropertyTypes.add("Condo")
            }
            if(Cabin.isChecked){
                selectedPropertyTypes.add("Cabin")
            }
            if(Other.isChecked){
            selectedPropertyTypes.add("Any")
        }
            if(AnyPrice.isChecked){
            selectedPropertyMoneyTypes.add("Any")
        }
            if(Monthly.isChecked){
            selectedPropertyMoneyTypes.add("Monthly")
        }
            if(Anually.isChecked){
            selectedPropertyMoneyTypes.add("Annually")
        }
            if(AnyFacilities.isChecked){
                selectedPropertyFacilities.add("Any")
            }
            if(Kitchen.isChecked){
            selectedPropertyFacilities.add("Kitchen")
        }
            if(SelfCheckIn.isChecked){
            selectedPropertyFacilities.add("Self-Check-In")
        }
            if(freeParking.isChecked){
                selectedPropertyFacilities.add("Parking")
            }
            if(airConditioner.isChecked){
                selectedPropertyFacilities.add("Air-Conditioner")
            }
            if(security.isChecked){
                selectedPropertyFacilities.add("Security")
            }
            if(furnished.isChecked){
                selectedPropertyFacilities.add("Furnished")
            }
            if(television.isChecked){
                selectedPropertyFacilities.add("TV")
            }
            val bundle = Bundle()
            bundle.putStringArrayList("selectedPropertyTypes", ArrayList(selectedPropertyTypes))
            bundle.putStringArrayList("selectedPropertyMoneyTypes", ArrayList(selectedPropertyMoneyTypes))
            bundle.putStringArrayList("selectedPropertyFacilities", ArrayList(selectedPropertyFacilities))

            val allPropertiesFragment = filteredPropertiesPostedFragment()
            allPropertiesFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, allPropertiesFragment)
                .addToBackStack(null)
                .commit()


        }
    }
}