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
import org.junit.runner.Computer
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player

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
        var paint: Paint
        val viewWidth: Float = width.toFloat()
        val viewHeight: Float = height.toFloat()
        val columns = mGame.boxes.maxWidth
        val spaceBetweenBoxes = 10*columns-1

        chosenLineWidth = (viewWidth - 20*(columns+1))/columns

        canvas.drawRect(0.toFloat(), 0.toFloat(), viewWidth, viewHeight, mBackgroundPaint)

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
}