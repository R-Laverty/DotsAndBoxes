package uk.ac.bournemouth.ap.dotsandboxes

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import org.example.student.dotsboxgame.StudentComputerPlayer
import org.example.student.dotsboxgame.StudentDotsBoxGame
import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import uk.ac.bournemouth.ap.dotsandboxeslib.HumanPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.Player
import kotlin.math.floor

class GameView: View, DotsAndBoxesGame.GameChangeListener, DotsAndBoxesGame.GameOverListener {
    //both of the following functions and the constructor were generated automatically by android studio
    override fun onGameOver(game: DotsAndBoxesGame, scores: List<Pair<Player, Int>>) {
        invalidate()
    }

    override fun onGameChange(game: DotsAndBoxesGame) {
        invalidate()
    }

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
    private var player1TextPaint: Paint
    private var player2TextPaint: Paint
    private val mVSComputerPlayerPaint: Paint
    private val mVSComputerPlayerTextPaint: Paint
    private val mVSHumanPlayerPaint: Paint
    private val mVSHumanPlayerTextPaint: Paint

    //Code for the setter taken from lecture 10 part 7 "Using listeners in gameview"
    private var mGame: StudentDotsBoxGame = StudentDotsBoxGame(5,5,listOf(HumanPlayer(), StudentComputerPlayer()))
        set(value) {
            field.removeOnGameChangeListener(this)
            field = value
            value.addOnGameChangeListener(this)
            field.removeOnGameOverListener(this)
            field = value
            value.addOnGameOverListener(this)
        }

    private val myGestureDetector = GestureDetector(context, MyGestureListener())
    private val columns = mGame.boxes.maxWidth
    private var playerScores = mGame.getScores()
    private var playersChosen = false

    init {
        //code for iniitialising the paints came from Lab Task 03A
        mGame.addOnGameChangeListener(this)
        mGame.addOnGameOverListener(this)
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
        player1TextPaint = Paint().apply {
            color = Color.BLUE
            textSize = 75.toFloat()
        }
        player2TextPaint = Paint().apply {
            color = Color.RED
            textSize = 75.toFloat()
        }
        mVSComputerPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.BLACK
        }
        mVSComputerPlayerTextPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 25f
        }
        mVSHumanPlayerPaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
        mVSHumanPlayerTextPaint = Paint().apply {
            color = Color.BLACK
            textSize = 25f
        }
    }

    override fun onDraw(canvas: Canvas) {
        if(playersChosen) {
            super.onDraw(canvas)

            val chosenLineWidth: Float
            val viewWidth: Float = width.toFloat()
            val viewHeight: Float = height.toFloat()
            playerScores = mGame.getScores()
            val player1Score = playerScores[0]
            val player2Score = playerScores[1]
            val lineThickness = 10


            chosenLineWidth = (viewWidth - lineThickness * (columns + 1)) / columns

            //draw background
            canvas.drawRect(0f, 0f, viewWidth, viewHeight, mBackgroundPaint)

            if (mGame.isFinished) {
                if (player1Score > player2Score) {
                    canvas.drawText(
                        "Player 1 wins with a score of: $player1Score",
                        0f,
                        height - height / 6.toFloat(),
                        player1TextPaint
                                   )
                } else {
                    canvas.drawText(
                        "Player 2 wins with a score of: $player2Score",
                        0f,
                        (height + 70) - height / 6.toFloat(),
                        player2TextPaint
                                   )
                }
            } else {
                //Draw player scores
                canvas.drawText(
                    "Player 1 Score: $player1Score",
                    width / 4.toFloat(),
                    height - height / 6.toFloat(),
                    player1TextPaint
                               )
                canvas.drawText(
                    "Player 2 Score: $player2Score",
                    width / 4.toFloat(),
                    (height + 70) - height / 6.toFloat(),
                    player2TextPaint
                               )
            }

            //draw boxes
            for (box in mGame.boxes) {
                val boxLeft = lineThickness * (box.boxX + 1) + chosenLineWidth * box.boxX
                val boxRight = boxLeft + chosenLineWidth
                val boxTop = lineThickness * (box.boxY + 1) + chosenLineWidth * box.boxY
                val boxBottom = boxTop + chosenLineWidth
                var boxPaint: Paint = mUnownedBoxPaint


                when (box.owningPlayer) {
                    mGame.players[0] -> boxPaint = mPlayer1BoxPaint
                    mGame.players[1] -> boxPaint = mPlayer2BoxPaint
                }

                canvas.drawRect(boxLeft, boxTop, boxRight, boxBottom, boxPaint)
            }
            //draw lines
            for (line in mGame.lines) {
                var lineLeft: Float
                var lineRight: Float
                var lineTop: Float
                var lineBottom: Float
                var linePaint: Paint = mLineNotDrawnPaint

                if (line.isDrawn) {
                    linePaint = mDrawnLinePaint
                }

                if (line.lineY % 2 == 0) {
                    //draw line horizontally
                    lineTop = line.lineY / 2 * chosenLineWidth + lineThickness * line.lineY / 2
                    lineBottom = lineTop + lineThickness
                    lineLeft = lineThickness * (line.lineX + 1) + chosenLineWidth * (line.lineX)
                    lineRight = lineLeft + chosenLineWidth
                } else {
                    //draw line vertically
                    lineTop =
                        lineThickness * ((line.lineY + 1) / 2) + chosenLineWidth * (((line.lineY + 1) / 2) - 1)
                    lineBottom = lineTop + chosenLineWidth
                    lineLeft = lineThickness * line.lineX + chosenLineWidth * line.lineX
                    lineRight = lineLeft + lineThickness
                }

                canvas.drawRect(lineLeft, lineTop, lineRight, lineBottom, linePaint)
            }
        }else{
            //draw The settings View
            super.onDraw(canvas)
            val halfWidth: Float = width/2f
            canvas.drawRect(0f,0f,halfWidth,height.toFloat(),mVSComputerPlayerPaint)
            canvas.drawText("play VS Computer", halfWidth/3, height/2f,
                            mVSComputerPlayerTextPaint)
            canvas.drawRect(halfWidth,0f,width.toFloat(),height.toFloat(),mVSHumanPlayerPaint)
            canvas.drawText("Play VS Human", halfWidth/3+halfWidth, height/2f,
                            mVSHumanPlayerTextPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return myGestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    inner class MyGestureListener: GestureDetector.SimpleOnGestureListener(){
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (playersChosen) {
                var x = e.x
                var y = e.y

                val detectionZone = width / columns
                val boxPressedX = floor(x / detectionZone)
                val boxPressedY = floor(y / detectionZone)

                x -= detectionZone * boxPressedX
                y -= detectionZone * boxPressedY

                val yx = y + x

                if (y > x) {
                    if (yx < detectionZone) {
                        //line left of box
                        x = boxPressedX
                        y = boxPressedY * 2 + 1
                    } else if (yx > detectionZone) {
                        //line below box
                        x = boxPressedX
                        y = (boxPressedY + 1) * 2
                    }
                } else if (y < x) {
                    if (yx < detectionZone) {
                        //line above box
                        x = boxPressedX
                        y = boxPressedY * 2
                    } else if (yx > detectionZone) {
                        //line right of box
                        x = boxPressedX + 1
                        y = boxPressedY * 2 + 1
                    }
                }

                try {
                    mGame.lines[x.toInt(), y.toInt()].drawLine()
                } catch (e: java.lang.Exception) {
                }
            } else {
                if (e.x < width/2){
                    mGame.players = listOf(HumanPlayer(), StudentComputerPlayer())
                }else{
                    mGame.players = listOf(HumanPlayer(), HumanPlayer())
                }
                playersChosen = true
                mGame.currentPlayer = mGame.players[0]
                invalidate()
            }
            return true
        }
    }
}