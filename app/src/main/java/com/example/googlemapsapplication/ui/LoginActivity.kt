package com.example.googlemapsapplication.ui

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.googlemapsapplication.R
import com.example.googlemapsapplication.data.ApiClient
import com.example.googlemapsapplication.data.requests.LoginRequest
import com.example.googlemapsapplication.data.responses.LoginResponse
import com.example.googlemapsapplication.databinding.ActivityLoginBinding
import com.example.googlemapsapplication.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    private lateinit var actionBar: ActionBar

    private lateinit var progressDialog: ProgressDialog

    private var email = ""
    private var password = ""

    private lateinit var sessionManager: SessionManager
    private lateinit var  apiClient: ApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Login"

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        apiClient = ApiClient()
        sessionManager = SessionManager(this)


        binding.loginBtn.setOnClickListener{
            validateData()
        }
    }

    private fun validateData() {
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailEt.setError("Invalid email format")
        }
        else if(TextUtils.isEmpty(password)){
            binding.passwordEt.setError("Please enter password")
        }
        else{
            sendLoginRequest()
        }
    }

    private fun sendLoginRequest() {
        progressDialog.show()
        apiClient.getApiService(this).login(LoginRequest(email = email, password = password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    failureNotification()
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    val loginResponse = response.body()
                    if (response.code() == 200) {
                        sessionManager.saveAuthToken(loginResponse?.authToken.toString().orEmpty())
                        successNotification()
                    } else {
                        failureNotification()
                    }
                }
            })
    }

    private fun failureNotification(){
        progressDialog.dismiss()
        Toast.makeText(this, "Failed to login", Toast.LENGTH_SHORT).show()
    }

    private fun successNotification(){
        progressDialog.dismiss()
        Toast.makeText(this, "LoggedIn as $email", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MapsActivity::class.java))
        finish()
    }
}