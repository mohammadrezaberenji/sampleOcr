package com.example.myapplication.ui.theme

import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import com.example.myapplication.TextRecognitionAnalyzer
import com.example.myapplication.TextRecognizerActivity
import com.google.mlkit.vision.text.Text
import com.king.camera.scan.AnalyzeResult
import com.king.camera.scan.analyze.Analyzer


class MyTextRecongnizerActivity : TextRecognizerActivity() {

    private val TAG = MyTextRecongnizerActivity::class.java.simpleName

    override fun onScanResultCallback(result: AnalyzeResult<Text>) {
        Log.i(TAG, "onScanResultCallback: result : ${result.result.text} ")
        val textString = result.result.text

        val split = textString.split("\n")

        Log.i(TAG, "onScanResultCallback: split : $split")
        Log.i(TAG, "onScanResultCallback: split : size  : ${split.size}")

        if (split.size >= 3){
            Log.i(TAG, "onScanResultCallback: split : $split")
            cameraScan.setAnalyzeImage(false)
        }


        if (result.result.text.isEmpty()) {
            return
        }

//        cameraScan.setAnalyzeImage(false)


    }

    override fun createAnalyzer(): Analyzer<Text> {
        return TextRecognitionAnalyzer()
    }
}