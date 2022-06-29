package com.example.corana

import android.content.Context
import android.graphics.Camera
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.Result
import kotlinx.android.synthetic.main.activity_bottom_navigator.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class ScannerFragment : Fragment(), ZXingScannerView.ResultHandler {

    private val key = "x-api-key"
    private val value = "8Wu&3Q2P>G68#3qXK8rk"
    private var endpoint = "https://auusparkapi.azurewebsites.net"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        scannerView = ZXingScannerView(activity)
        startHandler()
        return scannerView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun handleResult(result: Result?) {
        if (result != null) {
//            getQRCodeInfo(result.toString())
            updateStatus(status, result.toString())
            scannerView!!.stopCamera()
        } else {
            startHandler()
        }
    }

    private fun startHandler() {
        scannerView!!.setResultHandler(this)
        scannerView!!.startCamera()
    }

    private fun getQRCodeInfo(qrCode: String) {

        val qrCode = qrCode
//        https://auusparkapi-stg.azurewebsites.net/api/v1/qrcode/info
        Fuel.get("$endpoint/api/v1/qrcode/user-profile", listOf("content" to qrCode))
            .header(
                mapOf(
                    key to value,
                    "Content-Type" to "application/json"
                )
            )
            .responseString { _, _, result ->
                result.fold(success =
                { json ->
                    val listType = object : TypeToken<Info>() {}.type
                    val data = Gson().fromJson<Info>(json, listType)
//                    openDialog(data)
                    Toast.makeText(activity, json, Toast.LENGTH_LONG).show()
                },
                    failure = { error ->
                        Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
                        startHandler()
                    })
            }
    }

    private fun updateStatus(status: String, qrCode: String) {
        val qrCode = QrCode(qrCode, status)
        Fuel.post("https://auusparkapi.azurewebsites.net/api/v1/healthlog/submit")
            .header(
                mapOf(
                    key to value,
                    "Content-Type" to "application/json"
                )
            )
            .jsonBody(Gson().toJson(qrCode))
            .response { _, _, result ->
                result.fold(success = { data ->
                    Toast.makeText(activity, "Scanned Successfully", Toast.LENGTH_LONG).show()
                    startHandler()
                }, failure = { error ->
                    Toast.makeText(activity, error.localizedMessage, Toast.LENGTH_LONG).show()
                    startHandler()
                })
            }
    }
}
