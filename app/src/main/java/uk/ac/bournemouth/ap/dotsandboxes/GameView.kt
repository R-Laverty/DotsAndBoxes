package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import org.example.student.dotsboxgame.StudentDotsBoxGame
import org.junit.runner.Computer
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import kotlin.math.floor

class GameView:View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr)

    private var mBackgroundPaint: Paint
    private var mLineNotDrawnPaint: Paint
    private var mDrawnLinePaint: Paint
    private var mPlayer1BoxPaint: Paint
    private var mPlayer2BoxPaint: Paint
    private var mUnownedBoxPaint: Paint
    private var mGame = StudentDotsBoxGame(5,5)
    private val myGestureDetector = GestureDetector (context, myGestureListener())
    private val columns = mGame.boxes.maxWidth
    private val rows = mGame.boxes.maxHeight

    init {
        mBackgroundPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
        }
        mLineNotDrawnPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.WHITE
        }
        mDrawnLinePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.GRAY
        }
        mPlayer1BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLUE
        }
        mPlayer2BoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.RED
        }
        mUnownedBoxPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.CYAN
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val chosenLineWidth: Float
        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()


        chosenLineWidth = (viewWidth - 20*(columns+1))/columns

        //draw background
        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mBackgroundPaint)

        //draw boxes
        for(box in mGame.boxes){
            var boxLeft = 20*(box.boxX+1) + chosenLineWidth*box.boxX
            var boxRight = boxLeft + chosenLineWidth
            var boxTop = 20*(box.boxY+1) + chosenLineWidth*box.boxY
            var boxBottom = boxTop+chosenLineWidth
            var boxPaint: Paint

            if (box.owningPlayer == mGame.players[0]){
                boxPaint = mPlayer1BoxPaint
            } else if(box.owningPlayer == mGame.players[1]){
                boxPaint = mPlayer2BoxPaint
            }else{
                boxPaint = mUnownedBoxPaint
            }

            canvas.drawRect(boxLeft, boxTop, boxRight, boxBottom, boxPaint)
        }
        //draw lines
        for(line in mGame.lines){
            var lineLeft: Float
            var lineRight: Float
            var lineTop:Float
            var lineBottom:Float
            var linePaint: Paint = mLineNotDrawnPaint

            if(line.isDrawn){
                linePaint = mDrawnLinePaint
            }

            if (line.lineY%2==0){
                lineTop = line.lineY/2 * chosenLineWidth + 20*line.lineY/2
                lineBottom = lineTop+20
                lineLeft = 20*(line.lineX+1) + chosenLineWidth*(line.lineX)
                lineRight = lineLeft + chosenLineWidth
            }else{
                lineTop = 20*((line.lineY+1)/2) + chosenLineWidth*(((line.lineY+1)/2)-1)
                lineBottom = lineTop + chosenLineWidth
                lineLeft = 20*line.lineX + chosenLineWidth*line.lineX
                lineRight = lineLeft+20
            }

            canvas.drawRect(lineLeft, lineTop, lineRight, lineBottom, linePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return myGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    inner class myGestureListener: GestureDetector.SimpleOnGestureListener(){
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            var x = e.x
            var y = e.y

            var detectionZone = width/columns
            var boxPressedX = floor(x/detectionZone)
            var boxPressedY = floor(y/detectionZone)

            x = x - detectionZone*boxPressedX
            y = y - detectionZone*boxPressedY

            var yx = y+x

            if (y>x){
                if (yx < detectionZone){
                    //lne left of box
                    x = boxPressedX
                    y = boxPressedY*2+1
                }else if (yx > detectionZone){
                    //line below box
                    x = boxPressedX
                    y = (boxPressedY+1)*2
                }
            }else if (y<x){
                if (yx < detectionZone){
                    //line above box
                    x = boxPressedX
                    y = boxPressedY*2
                }else if (yx > detectionZone){
                    //line right of box
                    x = boxPressedX+1
                    y = boxPressedY*2+1
                }
            }

            try {
                mGame.lines[x.toInt(),y.toInt()].drawLine()
                invalidate()
            }catch (e: java.lang.Exception){}
            return true
        }
    }
}