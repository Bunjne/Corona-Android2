package com.example.corana


import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_scan.*
import kotlinx.android.synthetic.main.dialog_bottom_selection.*
import me.dm7.barcodescanner.zxing.ZXingScannerView
import java.util.jar.Manifest


var scannerView: ZXingScannerView? = null
var status = "N"

class ScanActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_scan)

        if (savedInstanceState == null) {
            checkPermission(android.Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE)
            toolbar_title.text = "NORMAL"
        }

        btSelect.setOnClickListener {
            showBottomSheetDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        scannerView?.stopCamera()
        openFragment(ScannerFragment())
        Log.e("Cycle", status)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this, "Camera Permission Granted", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this, "Camera Permission Denied", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission)
            == PackageManager.PERMISSION_DENIED
        ) { // Requesting the permission
            ActivityCompat.requestPermissions(
                this, arrayOf(permission),
                requestCode
            )
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
//        transaction.addToBackStack(null)  //because to avoid the empty activity when we use backToNull
        transaction.commit()
    }


    private fun showBottomSheetDialog() {

        if (scannerView!! == null) {
            scannerView = ZXingScannerView(this)
        }

        val dialog = BottomSheetDialog(this)
        val mDialogView = layoutInflater.inflate(R.layout.dialog_bottom_selection, null)
        dialog.setContentView(mDialogView)
        dialog.btNormal.setOnClickListener {
            dialog.dismiss()
            status = "N"
            toolbar_title.text = "NORMAL"
//            openFragment(ScannerFragment())
            my_toolbar.setBackgroundColor(resources.getColor(R.color.green))
        }
        dialog.btPrecuation.setOnClickListener {
            dialog.dismiss()
            status = "P"
            toolbar_title.text = "PRECAUTION"
            my_toolbar.setBackgroundColor(resources.getColor(R.color.red))
        }
        dialog.btCancle.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
