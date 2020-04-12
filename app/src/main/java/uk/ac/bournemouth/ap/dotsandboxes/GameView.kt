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
            color = Color.BLACK
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
        var paint: Paint
        val cols = 5//TODO change for actual assignment
        var rows = 8

        val viewWidth: Float = width.toFloat()
        chosenWidth = ((viewWidth - (1+cols)*10)-70)/cols
        val viewHeight: Float = height.toFloat()

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mBackgroundPaint)

        //draw horizontal lines
        for (row in 0 until rows+1) {
            var horizontalTop = chosenWidth * row + 35 + (row*10)
            var horizontalBottom = horizontalTop + 10
            for (col in 0 until cols) {
                paint = mNoPlayerPaint
                var horizontalLeft = (chosenWidth + 10) * col + 35
                var horizontalRight = chosenWidth + horizontalLeft
                canvas.drawRect(horizontalLeft, horizontalTop, horizontalRight, horizontalBottom, paint)
            }
        }

        //draw vertical lines
        for (row in 0 until rows){
            var verticalTop = 45+((chosenWidth+10)*row)
            var verticalBottom = verticalTop + chosenWidth
            for (col in 0 until cols+1) {
                paint = mNoPlayerPaint
                var verticalLeft = 25 + ((chosenWidth+10)*col)
                var verticalRight = verticalLeft + 10
                canvas.drawRect(verticalLeft,verticalTop,verticalRight,verticalBottom,paint)
            }
        }
    }
}