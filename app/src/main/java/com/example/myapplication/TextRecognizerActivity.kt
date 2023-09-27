package com.example.myapplication

import com.google.mlkit.vision.text.Text
import com.king.camera.scan.BaseCameraScanActivity
import com.king.camera.scan.analyze.Analyzer

public abstract class TextRecognizerActivity : BaseCameraScanActivity<Text>() {

    override fun createAnalyzer(): Analyzer<Text> {
        return TextRecognitionAnalyzer()
    }
}