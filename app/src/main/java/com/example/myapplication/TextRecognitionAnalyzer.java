package com.example.myapplication;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptionsInterface;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class TextRecognitionAnalyzer extends CommonAnalyzer<Text> {

    private TextRecognizer mDetector;

    public TextRecognitionAnalyzer() {
        this(null);
    }

    public TextRecognitionAnalyzer(TextRecognizerOptionsInterface options) {
        if (options != null) {
            mDetector = TextRecognition.getClient(options);
        } else {
            mDetector = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        }
    }

    @NonNull
    @Override
    protected Task<Text> detectInImage(InputImage inputImage) {
        return mDetector.process(inputImage);
    }
}
