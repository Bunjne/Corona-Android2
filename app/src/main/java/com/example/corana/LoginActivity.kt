package com.example.corana

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.github.kittinunf.fuel.Fuel
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.log

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etPassword.doAfterTextChanged {
            var password = etPassword.text.toString()
            if (password.length == 6 && password != "272903") {
                etPassword.setText("")
                Toast.makeText(this, "Incorrect Password", Toast.LENGTH_LONG).show()
            } else
                if (etPassword.text.toString().equals("272903")) {
                var intent = Intent(this, ScanActivity::class.java)
                startActivity(intent)
            }
        }
    }

}

