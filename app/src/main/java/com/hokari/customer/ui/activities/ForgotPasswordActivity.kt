package com.hokari.customer.ui.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hokari.customer.R
import com.hokari.customer.databinding.ActivityForgotPasswordBinding
import com.hokari.customer.databinding.ProgressBarBinding


class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var myProgressDialog: Dialog
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarForgotPasswordActivity)
        val actionBar = supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_new_24)
        }

        binding.toolbarForgotPasswordActivity.setNavigationOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSendResetEmail.setOnClickListener {
            sendResetMail()
        }

    }

    fun sendResetMail(){
        val email = binding.etEmailForgotPass.text.toString()
        showProgressBar()
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                hideProgressBar()
                if (task.isSuccessful) {
                    Toast.makeText(this,"An Email Has Been Sent to you, please check your mailbox.", Toast.LENGTH_LONG).show()
                    Toast.makeText(this,"Check Your Spam Folder If you can't see the mail.", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this,"An error occurred! Please enter your email correctly.",Toast.LENGTH_LONG).show()
                }
            }

    }

    private fun showProgressBar() {
        myProgressDialog = Dialog(this)
        val binding: ProgressBarBinding = ProgressBarBinding.inflate(layoutInflater)
        myProgressDialog.setContentView(binding.root)
        binding.tvProgressText.setText(R.string.please_wait)
        myProgressDialog.setCancelable(false)
        myProgressDialog.setCanceledOnTouchOutside(false)
        myProgressDialog.show()
    }

    private fun hideProgressBar() {
        myProgressDialog.dismiss()
    }




}