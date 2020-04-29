package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.Matrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.SparseMatrix
import kotlin.random.Random


class StudentDotsBoxGame(columns: Int = 8, rows: Int = 8, players: List<Player> =
    listOf(HumanPlayer(), HumanPlayer())) : AbstractDotsAndBoxesGame() {
    override val players: List<Player> = players
    val mColumns: Int = columns
    val mRows:Int = rows
    var turn:Int = 0

    override var currentPlayer: Player = players[0]
        get()= players[0]

    // NOTE: you may want to me more specific in the box type if you use that type in your class
    override val boxes: Matrix<DotsAndBoxesGame.Box> = MutableMatrix(columns, rows, ::StudentBox)//TODO("Create a matrix initialized with your own box type")

    override val lines: SparseMatrix<DotsAndBoxesGame.Line> = MutableSparseMatrix(rows, columns, ::StudentLine)//TODO("Create a matrix initialized with your own line type")

    override val isFinished: Boolean
        get() = isFinished//TODO("Provide this getter. Note you can make it a var to do so")

    override fun playComputerTurns() {
        var current = currentPlayer
        while (current is ComputerPlayer && ! isFinished) {
            current.makeMove(this)
            current = currentPlayer
        }
    }

    /**
     * This is an inner class as it needs to refer to the game to be able to look up the correct
     * lines and boxes. Alternatively you can have a game property that does the same thing without
     * it being an inner class.
     */
    inner class StudentLine(lineX: Int, lineY: Int) : AbstractLine(lineX, lineY) {
        override var isDrawn: Boolean = false
            get() = this.isDrawn//TODO("Provide this getter. Note you can make it a var to do so")


        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get() {
                //TODO("You need to look up the correct boxes for this to work")
                var boxAboveOrLeft: StudentBox?
                var boxBelowOrRight: StudentBox?
                if (this.lineY == 0) {
                    boxAboveOrLeft = null
                }
                if (this.lineY == mRows*2){
                    boxBelowOrRight = null
                }
            }

        override fun drawLine() {
            //TODO("Implement the logic for a player drawing a line. Don't forget to inform the listeners (fireGameChange, fireGameOver)")
            // NOTE read the documentation in the interface, you must also update the current player.
            this.isDrawn = true
            fireGameChange()
            if (turn%2 == 0){
                currentPlayer = players[0]
            } else {
                currentPlayer = players[1]
            }
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override val owningPlayer: Player?
            get() = TODO("Provide this getter. Note you can make it a var to do so")

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() = TODO("Look up the correct lines from the game outer class")

    }
}