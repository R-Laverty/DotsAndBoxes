package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.example.student.dotsboxgame.StudentDotsBoxGame

class GameView:View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private var mStudentDotsBoxGame: StudentDotsBoxGame = StudentDotsBoxGame(8,8)
    private val colCount = mStudentDotsBoxGame.mColumns
    private val rowCount = mStudentDotsBoxGame.mRows

    private var mBackgroundPaint: Paint
    private var mLineNotDrawnPaint: Paint
    private var mDrawnLinePaint: Paint
    private var mPlayer1BoxPaint: Paint
    private var mPlayer2BoxPaint: Paint

    private val myGestureDetector = GestureDetector (context, myGestureListener())

    init {
        mBackgroundPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
        }
        mLineNotDrawnPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.GRAY
        }
        mDrawnLinePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        mPlayer1BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }
        mPlayer2BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val chosenLineWidth: Float
        var paint: Paint

        val viewWidth: Float = width.toFloat()
        chosenLineWidth = ((viewWidth - (1+colCount)*10))/colCount
        val viewHeight: Float = height.toFloat()

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mBackgroundPaint)

        for (row in 0 until (rowCount*2) + 1){
            if ((row+2) % 2 == 0) {
                //Draw horizontal lines
                for (col in 0 until colCount) {
                    paint = mLineNotDrawnPaint
                    var horizontalLineTop = chosenLineWidth * (row/2) + ((row/2)*10)
                    var horizontalLineBottom = horizontalLineTop + 10
                    var horizontalLineLeft = 10 + (chosenLineWidth + 10) * col
                    var horizontalLineRight = chosenLineWidth + horizontalLineLeft
                    canvas.drawRect(horizontalLineLeft, horizontalLineTop, horizontalLineRight,
                                    horizontalLineBottom, paint)
                }
            } else if ((row+2) % 2 !=0) {
                //Draw vertical lines
                for (col in 0 until colCount+1) {
                    paint = mLineNotDrawnPaint
                    var verticalLineTop = 10+((chosenLineWidth+10)*(row/2))
                    var verticalLineBottom = verticalLineTop + chosenLineWidth
                    var verticalLineLeft = ((chosenLineWidth+10)*col)
                    var verticalLineRight = verticalLineLeft + 10
                    canvas.drawRect(verticalLineLeft, verticalLineTop, verticalLineRight,
                                    verticalLineBottom, paint)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return myGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }
    inner class myGestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val horizontalsColWidth = (width-70) / colCount
            var horizontalColTouch = (e.x.toInt()-35)/horizontalsColWidth

            val verticalsColWidth = width / (colCount+1)
            var verticalColTouch = e.x.toInt()/verticalsColWidth

            var horizontalsRowTouch = e.y.toInt() / verticalsColWidth
            var verticalsRowTouch = (e.y.toInt()-35) / horizontalsColWidth

            println("$horizontalColTouch,$horizontalsRowTouch / $verticalColTouch,$verticalsRowTouch")
            return true
        }
    }
}