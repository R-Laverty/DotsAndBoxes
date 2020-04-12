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
        val gridSize = 8//TODO change for actual assignment

        val viewWidth: Float = width.toFloat()
        chosenWidth = ((viewWidth - (1+gridSize)*10)-70)/gridSize
        val viewHeight: Float = height.toFloat()

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mBackgroundPaint)

        for (col in 0 until gridSize + 1) {
            for (row in 0 until gridSize) {
                paint = mNoPlayerPaint
                //draw vertical lines
                var verticalTop = 45 + ((chosenWidth+10) * row)
                var verticalBottom = chosenWidth + verticalTop
                var verticalLeft = (chosenWidth * col) + 35 + 10*col - 10
                var verticalRight = verticalLeft + 10
                canvas.drawRect(verticalLeft,verticalTop,verticalRight,verticalBottom, paint)
                //draw horizontal lines
                var horizontalLeft = (chosenWidth + 10) * row + 35
                var horizontalRight = chosenWidth + horizontalLeft
                var horizontalTop = chosenWidth * col + 35 + (col*10)
                var horizontalBottom = horizontalTop + 10
                canvas.drawRect(horizontalLeft, horizontalTop, horizontalRight, horizontalBottom, paint)

            }
        }
    }
}