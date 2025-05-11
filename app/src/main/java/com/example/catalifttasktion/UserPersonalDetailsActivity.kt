package com.example.catalifttasktion

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.catalifttasktion.databinding.ActivityUserPersonalDetailsBinding
import java.util.Calendar

class UserPersonalDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserPersonalDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserPersonalDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Day Spinner
        val days = listOf("DD") +(1..31).map { it.toString() }
        val dayAdapter = ArrayAdapter(this, R.layout.custome_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDay.adapter = dayAdapter

        // Month Spinner
        val months = listOf("MM","Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val monthAdapter = ArrayAdapter(this, R.layout.custome_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerMonth.adapter = monthAdapter

        // Year Spinner (e.g., from 1970 to current year)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = listOf("YYYY") +(1950..currentYear).map { it.toString() }.reversed()
        val yearAdapter = ArrayAdapter(this, R.layout.custome_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerYear.adapter = yearAdapter



        // Mother Tongue Spinner
        val languages = listOf("English","Hindi","Bengali","Russian","Japanese","Punjabi","Marathi","Telugu","Korean","Urdu","Tamil","Italian")
        val languagesAdapter = ArrayAdapter(this, R.layout.custome_spinner_item, languages)
        languagesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.motherTongueSpinnerInput.adapter = languagesAdapter


        // Gender Spinner
        val genders = listOf("Male","Female","Others")
        val gendersAdapter = ArrayAdapter(this, R.layout.custome_spinner_item, genders)
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinnerInput.adapter = gendersAdapter

        // Set up the submit button click listener
        binding.personalDetailsSubmitBtn.setOnClickListener {

            // Get the user inputs
            val userFullName = binding.fullNameInput.text.toString().trim()
            val userStudentId = binding.studentIdInput.text.toString().trim()
            val day = binding.spinnerDay.selectedItem.toString().trim()
            val month = binding.spinnerMonth.selectedItem.toString().trim()
            val year = binding.spinnerYear.selectedItem.toString().trim()
            val userDOB = "$day/$month/$year"
            val userMotherTongue = binding.motherTongueSpinnerInput.selectedItem.toString().trim()
            val userGender = binding.genderSpinnerInput.selectedItem.toString().trim()
            val userPhoneNumber = binding.phoneNumberInput.text.toString().trim()
            val userEmail = binding.emailInput.text.toString().trim()
            val userLinkedIn = binding.linkedInInput.text.toString().trim()

            // Validate the inputs
            userInformationCheck(userFullName, userStudentId, userDOB, userPhoneNumber, userEmail, userLinkedIn)
        }
    }

    // Function to check user information
    private fun userInformationCheck(userFullName: String, userStudentId: String, userDOB: String, userPhoneNumber: String, userEmail: String, userLinkedIn: String) {
        if (userFullName.isEmpty()) {
            binding.fullNameInput.error = "Please enter your full name"
            return
        }
        else if (userStudentId.isEmpty()) {
            binding.studentIdInput.error = "Please enter your student ID"
            return
        }
        else if (userDOB == "DD/MM/YYYY") {
            Toast.makeText(this, "Please select your date of birth", Toast.LENGTH_SHORT).show()
            return
        }
        else if (userPhoneNumber.isEmpty()) {
            binding.phoneNumberInput.error = "Please select your mother tongue"
            return
        }
        else if (userEmail.isEmpty()) {
            binding.emailInput.error = "Please enter your email"
            return
        }
        else if (userLinkedIn.isEmpty()) {
            binding.linkedInInput.error = "Please enter your LinkedIn URL or username"
            return
        }
        else{
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}