package com.example.myapplication.ui.theme;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

//public class TesseractOCR {
//
//    private static final String TAG = TesseractOCR.class.getName();
//    private final TessBaseAPI mTess;
//
//    public TesseractOCR(Context context) {
//        mTess = new TessBaseAPI();
//        boolean fileExistFlag = false;
//
//        AssetManager assetManager = context.getAssets();
//
//        String dstPathDir = "/tesseract/tessdata/";
//
//        String srcFile = "eng.traineddata";
//        InputStream inFile = null;
//
//        dstPathDir = context.getFilesDir() + dstPathDir;
//        String dstInitPathDir = context.getFilesDir() + "/tesseract";
//        String dstPathFile = dstPathDir + srcFile;
//        FileOutputStream outFile = null;
//
//        try {
//            inFile = assetManager.open(srcFile);
//
//            File f = new File(dstPathDir);
//
//            if (!f.exists()) {
//                if (!f.mkdirs()) {
//                    Toast.makeText(context, srcFile + " can't be created.", Toast.LENGTH_SHORT).show();
//                }
//                outFile = new FileOutputStream(new File(dstPathFile));
//            } else {
//                fileExistFlag = true;
//            }
//
//        } catch (Exception ex) {
//            Log.e(TAG, "TesseractOCR: ", ex);
//            Log.e(TAG, ex.getMessage());
//
//        } finally {
//
//            if (fileExistFlag) {
//                try {
//                    if (inFile != null) inFile.close();
//                    mTess.init(dstInitPathDir, "eng");
//                    return;
//
//                } catch (Exception ex) {
//                    Log.e(TAG, ex.getMessage());
//                }
//            }
//
//            if (inFile != null && outFile != null) {
//                try {
//                    //copy file
//                    byte[] buf = new byte[1024];
//                    int len;
//                    while ((len = inFile.read(buf)) != -1) {
//                        outFile.write(buf, 0, len);
//                    }
//                    inFile.close();
//                    outFile.close();
//                    mTess.init(dstInitPathDir, "eng");
//                } catch (Exception ex) {
//                    Log.e(TAG, ex.getMessage());
//                }
//            } else {
//                Log.e(TAG, "TesseractOCR: else error ");
//                Log.e(TAG, "TesseractOCR: in file " + inFile);
//                Log.e(TAG, "TesseractOCR: out file " + outFile);
////                Toast.makeText(context, srcFile + " can't be read.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public String getOCRResult(Bitmap bitmap) {
//        mTess.setImage(bitmap);
//        return mTess.getUTF8Text();
//    }
//
//    public void onDestroy() {
//        if (mTess != null) mTess.end();
//    }
//}
