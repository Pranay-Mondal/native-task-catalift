package com.example.catalifttasktion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.catalifttasktion.databinding.ActivityUserSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class UserSignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserSignInBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Google Sign-In options
        val googleSignInOptions = com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id ))
            .requestEmail()
            .build()

        // Initialize Google Sign-In client
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Set up the sign-in button click listener
        binding.signInWithGoogleBtn.setOnClickListener {
            signInGoogle()
        }

        // This is a mock database for demonstration purposes
        val testUsers = mapOf(
            "9876543211" to "pass111",
            "9876543212" to "pass222",
            "9876543213" to "pass333",
            "9876543214" to "pass444"
        )

        // Set up the sign-in button click listener
        binding.SignInBtn.setOnClickListener {
            val enteredPhoneNumber = binding.signInPhoneNumber.text.toString().trim()
            val enteredPassword = binding.signInPassword.text.toString().trim()
            userSingInInfoCheck(enteredPhoneNumber, enteredPassword, testUsers)
        }
    }

    // Function to sign in with Google
    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    private val launcher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
            if (result.resultCode == RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            } else {
                // Handle sign-in failure
                Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
            }
        }

    // Function to handle Google Sign-In results
    private fun handleResults(task: com.google.android.gms.tasks.Task<com.google.android.gms.auth.api.signin.GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val credential = com.google.firebase.auth.GoogleAuthProvider.getCredential(account.idToken, null)

            // Sign in with Firebase using the Google credential
            auth.signInWithCredential(credential).addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    Toast.makeText(this, "Google Sign-In successful!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, UserPersonalDetailsActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Firebase Auth failed: ${authResult.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
        else{
            // Handle sign-in failure
            Toast.makeText(this, "Google Sign-In failed.", Toast.LENGTH_SHORT).show()
        }
    }

    //    // Function to check user sign-in information
    private fun userSingInInfoCheck(enteredPhoneNumber: String, enteredPassword: String, testUsers: Map<String, String>) {
        if (enteredPhoneNumber.isEmpty()){
            binding.signInPhoneNumber.error="Please enter your phone number"
            return
        }
        if (enteredPassword.isEmpty()){
            binding.signInPassword.error="Please enter your password"
            return
        }
        val testUserPassword=testUsers[enteredPhoneNumber]
        if (testUserPassword==null){
            binding.signInPhoneNumber.error="User not found"
            return
        }
        if (enteredPassword==testUserPassword) {
            Toast.makeText(this,"Login successful!",Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,UserPersonalDetailsActivity::class.java))
            finish()
        }
        else {
            binding.signInPassword.error="Incorrect password"
        }
    }
}

