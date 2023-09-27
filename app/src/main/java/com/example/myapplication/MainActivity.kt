package com.example.myapplication

import ai.cardscan.insurance.CardError
import ai.cardscan.insurance.CardScanActivity
import ai.cardscan.insurance.CardScanActivityResult
import ai.cardscan.insurance.CardScanActivityResultHandler
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import cards.pay.paycardsrecognizer.sdk.Card
import cards.pay.paycardsrecognizer.sdk.ScanCardIntent
import com.example.myapplication.ui.theme.LuminosityAnalyzer
import com.example.myapplication.ui.theme.MyTextRecongnizerActivity
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import leadtools.Platform.*
import leadtools.ocr.OcrEngine
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit


class MainActivity : ComponentActivity(), CardScanActivityResultHandler {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var cameraExecutor: ExecutorService

    private var imageCapture: ImageCapture? = null

    private var ocrEngine: OcrEngine? = null
    private var sb: StringBuilder? = null

    lateinit var cardScanResultLauncher: ActivityResultLauncher<Intent>


    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        )
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            Log.i(TAG, "permissions : $permissions: ")
//            permissions..forEach {
//                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
//                    permissionGranted = false
//            }
            if (!permissionGranted) {
                Toast.makeText(
                    baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
//                startCamera()
            }
        }


    val content = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        Log.i(TAG, "result : ")
        var resultDisplayStr = ""
        if (it.data != null && it.data!!.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            val scanResult =
                it.data!!.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            resultDisplayStr =
                "result : Card Number: " + scanResult!!.getRedactedCardNumber() + "\n";

            // Do something with the raw number, e.g.:
            // myService.setCardNumber( scanResult.cardNumber );

            if (scanResult.isExpiryValid()) {
                resultDisplayStr += "result : Expiration Date: " + scanResult.expiryMonth + "/" + scanResult.expiryYear + "\n";
            }

            if (scanResult.cvv != null) {
                // Never log or display a CVV
                resultDisplayStr += "result : CVV has " + scanResult.cvv.length + " digits.\n";
            }

            if (scanResult.postalCode != null) {
                resultDisplayStr += "result : Postal Code: " + scanResult.postalCode + "\n";
            }
        } else {
            resultDisplayStr = "result : Scan was canceled.";
        }
        // do something with resultDisplayStr, maybe display it in a textView
        // resultTextView.setText(resultDisplayStr);

        // else handle other activity results
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        cardScanResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //use CardScanActivity for processing results and call the success/error/canceled callbacks.
                Log.i(TAG, "onCreate: result -> $result")
                CardScanActivity.processResult(this@MainActivity, result)


            }

//
        val button = findViewById<Button>(R.id.image_capture_button)
        button.setOnClickListener {
            Log.i(TAG, "onCreate: on click ")
            scanCard()
//            startCamera()

        }

        val secondButton = findViewById<Button>(R.id.new_scanner)
        secondButton.setOnClickListener {
            Log.i(TAG, "onCreate: on click : secondButton")

            startActivity(Intent(this, MyTextRecongnizerActivity::class.java))


        }

        cameraExecutor = Executors.newSingleThreadExecutor()


        val textView = findViewById<TextView>(R.id.text)
        val spannable =
            SpannableStringBuilder("You can start learning Android from Mindorks")

        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                Log.i(TAG, "onClick: ")
                Toast.makeText(view.context, "MindOrks Clicked!", Toast.LENGTH_SHORT).show()
            }
        }

        spannable.setSpan(clickable, 36, 44, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)


        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.purple_700)),
            36,
            44,
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannable.setSpan(UnderlineSpan(), 36, 44, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)


        textView.setText(spannable, TextView.BufferType.SPANNABLE)
        textView.setMovementMethod(LinkMovementMethod.getInstance());

//        if (allPermissionsGranted()) {
////            startCamera()
//        } else {
//            Log.i(TAG, "onCreate: request permission ")
//            requestPermissions()
//        }


    }

    fun onScanPress(v: View?) {
        val scanIntent = Intent(this, CardIOActivity::class.java)

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false) // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false) // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.

//        startActivityForResult(intent , 1001)

        content.launch(scanIntent)

    }

    private fun requestPermissions() {
        Log.i(TAG, "requestPermissions: ")
//        activityResultLauncher.launch(REQUIRED_PERMISSIONS[0])

    }

    override fun scanSuccess(card: CardScanActivityResult) {
        Log.i(TAG, " new scanner : scanSuccess: card object : $card")
    }

    override fun scanCanceled() {
        Log.i(TAG, "new scanner : scanCanceled: ")
        super.scanCanceled()
    }

    override fun scanError(error: CardError) {
        Log.i(TAG, "new scanner : scanError: $error ")
        super.scanError(error)
    }

//    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
//        ContextCompat.checkSelfPermission(
//            baseContext, it
//        ) == PackageManager.PERMISSION_GRANTED
//    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        val viewSurface = findViewById<PreviewView>(R.id.viewFinder)

        imageCapture = ImageCapture.Builder()
            .build()

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewSurface.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(
                        cameraExecutor, LuminosityAnalyzer(
                            { luma ->
                                Log.d(TAG, "Average luminosity: $luma")
                            },
                            context = this
                        )
                    )
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }


    @Throws(IOException::class)
    fun createImageFile(): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("MMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        val image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        return image
    }

    private fun scanCard() {
        Log.i(TAG, "scanCard: ")
        val intent = ScanCardIntent.Builder(this).build()
        startActivityForResult(intent, 1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult: ")
        Log.i(TAG, "onActivityResult: data : $data")
        Log.i(TAG, "onActivityResult: data : extra : ${data!!.extras.toString()}")
        val card = data!!.getParcelableExtra<Card>(ScanCardIntent.RESULT_PAYCARDS_CARD)

        Log.i(TAG, "onActivityResult: card :$card")

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.i(TAG, "onActivityResult: result  ok")
                val cardData = """
                Card number: ${card!!.cardNumberRedacted}
                Card holder: ${card.cardHolderName}
                Card expiration date: ${card.expirationDate}
                """.trimIndent()
                Log.i(TAG, "Card info: $cardData")
            } else if (resultCode == RESULT_CANCELED) {
                Log.i(TAG, "Scan canceled")
            } else {
                Log.i(TAG, "Scan failed")
            }
        }
    }


}








