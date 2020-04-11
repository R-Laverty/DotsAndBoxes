package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GameView:View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private var mBackgroundPaint: Paint
    private var mNoPlayerPaint: Paint
    private var mPlayer1Paint: Paint
    private var mPlayer2Paint: Paint

    init {
        mBackgroundPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }
        mNoPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.GRAY
        }
        mPlayer1Paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }
        mPlayer2Paint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val chosenWidth: Float
        val chosenHeight: Float
        var paint: Paint

        val viewWidth: Float = width.toFloat()
        chosenWidth = ((viewWidth-35)/8) - 35 //TODO replace 8 with var
        val viewHeight: Float = height.toFloat()
        chosenHeight = viewHeight/8 //TODO replace 8 with var

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mBackgroundPaint)

        //draw horizontal lines
        for (col in 0 until 8) {  //TODO replace 8 with var
            for (row in 0 until 8) { //TODO replace 8 with var
                paint = mNoPlayerPaint
                var left = chosenWidth * row + (35*row) + 35
                var right = chosenWidth + left
                canvas.drawRect(left.toFloat(), 5.toFloat(), right.toFloat(), 10.toFloat(), paint)
                //canvas.drawRect(left.toFloat(), 5.toFloat(), 5.toFloat(), left.toFloat(), paint)
            }
        }
    }
}