package com.example.myapplication.ui.theme

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.myapplication.LumaListener
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer


class LuminosityAnalyzer(private val listener: LumaListener, private val context: Context) :
    ImageAnalysis.Analyzer {

    private val TAG = LuminosityAnalyzer::class.java.simpleName

//     val options = BarcodeScannerOptions.Builder()
//         .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
//         .build()

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {

        Log.i(TAG, "analyze: image : $image")

        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average()

        listener(luma)


//        val result = extractText(image.image!!.toBitmap())


        image.close()
    }

    fun Image.toBitmap(): Bitmap {
        val yBuffer = planes[0].buffer // Y
        val vuBuffer = planes[2].buffer // VU

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, this.width, this.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, yuvImage.width, yuvImage.height), 50, out)
        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    @SuppressLint("SdCardPath")
    @Throws(Exception::class)
    private fun extractText(bitmap: Bitmap): String? {
        Log.i(TAG, "extractText: ")

//        return TesseractOCR(context).getOCRResult(bitmap)
        return ""
    }

}