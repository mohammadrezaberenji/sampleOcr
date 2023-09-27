package com.example.myapplication.ui.theme

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class LinesView @JvmOverloads
constructor(
    private val ctx: Context,
    private val attributeSet: AttributeSet? = null,
    private val defStyleAttr: Int = 0
) : View(ctx, attributeSet, defStyleAttr) {

    private val TAG = LinesView::class.java.simpleName


    private var firstLineY = 2f
    private var firstLineX = 2f

    private var secondLineXStop = ctx.resources.displayMetrics.widthPixels
    private var secondLineYStop = 0f

    private var isFirstTime = true

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val numberPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val backgroundRect = RectF()
    private val cornerRadius = 10f


    init {
        Log.i(TAG, "init width is : ${ctx.resources.displayMetrics.widthPixels}: ")
        backgroundPaint.color = ContextCompat.getColor(context, R.color.red)
        linePaint.color = ContextCompat.getColor(context, R.color.red)
        linePaint.strokeWidth = 1f
        numberPaint.color = ContextCompat.getColor(context, R.color.white)
        numberPaint.textSize = 64f


    }

    private val firstLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 10f
    }

    private val secondLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        strokeWidth = 10f
    }

    var path = Path()
    var pathPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#a20a0a")
        setShadowLayer(100f, 100f, 100f, Color.RED)
    }

    private var firstLineFinish = false

    override fun onDraw(canvas: Canvas) {
        Log.i(TAG, "onDraw: ")
        super.onDraw(canvas);

        val displayWidth = width
        val displayHeight = height

        Log.i(TAG, "onDraw: with  : $displayWidth")
        Log.i(TAG, "onDraw: height  : $displayHeight")

        Log.i(TAG, "onDraw: weight / 3   : ${displayWidth * 0.3}")
        Log.i(TAG, "onDraw: wight * ( 2 / 3)  : ${displayWidth * 0.6}")




        backgroundRect.set(
            (displayWidth.toFloat() * 0.3).toFloat(),
            displayHeight.toFloat() / 4,
            (displayWidth.toFloat() * 0.7).toFloat(),
            1000f
        )
        canvas?.drawRoundRect(backgroundRect, cornerRadius, cornerRadius, backgroundPaint)
        canvas?.drawLine(0f, 100f, displayWidth!!.toFloat(), 500f, linePaint)

//        val textWidth = numberPaint.measureText("1")

        canvas?.drawText("1", backgroundRect.centerX(), backgroundRect.centerY(), numberPaint)


//        if (firstLineX <= width - 50f && firstLineY <= height / 2f) {
//            Log.i(TAG, "onDraw: if statement :  endX : $firstLineX ")
//            Log.i(TAG, "onDraw: if statement : endY : $firstLineY ")
//            firstLineX += 15
//            firstLineY += 15
////            postInvalidateDelayed(1)
//            firstLinePaint.color = Color.BLACK
//            postInvalidate()
//            isFirstTime = true
//        } else {
//            Log.i(TAG, "onDraw: else :  ")
//
//            if (isFirstTime){
//                isFirstTime = false
//                secondLineXStop = width
//                secondLineYStop = 0f
//            }
//
//            firstLinePaint.color = Color.TRANSPARENT
//            canvas?.drawLine(
//                width.toFloat(),
//                0f,
//                secondLineXStop.toFloat(),
//                secondLineYStop,
//                secondLine
//            )
//            Log.i(TAG, "onDraw: else : second line x stop : $secondLineXStop")
//            if (secondLineXStop >= 0 && secondLineYStop <= height / 2) {
//                Log.i(TAG, "onDraw: else :  second line if ")
//                Log.i(
//                    TAG,
//                    "onDraw: else :  second line if : second line x stop : $secondLineXStop "
//                )
//                Log.i(
//                    TAG,
//                    "onDraw: else :  second line if : second line y stop : $secondLineYStop "
//                )
//                secondLineXStop -= 15
//                secondLineYStop += 15
//
//                secondLine.color = Color.RED
//
//            } else {
//                Log.i(TAG, "onDraw: else : second line else ")
//                secondLine.color = Color.TRANSPARENT
//                firstLineX = 0f
//                firstLineY = 0f
//            }
//
//            Log.i(TAG, "onDraw: first line x : $firstLineX")
//
//            postInvalidate()
//        }


    }

    private fun drawLinedPath(canvas: Canvas?) {
        Log.i(TAG, "drawLinedPath: ")

        path.moveTo(0.1f * width, 0.1f * height)
        path.lineTo(0.1f * width, 0.5f * height)
        path.lineTo(0.9f * width, 0.1f * height)

        canvas?.drawPath(path, pathPaint)

    }


}