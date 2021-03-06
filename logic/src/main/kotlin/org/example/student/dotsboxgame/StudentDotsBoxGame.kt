package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.*
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.Matrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.MutableSparseMatrix
import uk.ac.bournemouth.ap.dotsandboxeslib.matrix.SparseMatrix
import java.lang.Exception
import kotlin.random.Random

class StudentDotsBoxGame(columns: Int = 8, rows: Int = 8, players: List<Player> =
    listOf(HumanPlayer(), HumanPlayer())) : AbstractDotsAndBoxesGame() {

    override var players: List<Player> = players.toList()

    override var currentPlayer: Player = players[0]

    // NOTE: you may want to me more specific in the box type if you use that type in your class
    override val boxes: Matrix<StudentBox> = MutableMatrix(columns, rows, ::StudentBox)

    override val lines: SparseMatrix<DotsAndBoxesGame.Line> =
        MutableSparseMatrix(columns+1, (rows*2)+1, ::StudentLine,
                            {x,y -> !(y%2==0 && x==columns)})

    override var isFinished: Boolean = false

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
            //get() = false//TODO("Provide this getter. Note you can make it a var to do so")


        override val adjacentBoxes: Pair<StudentBox?, StudentBox?>
            get()  {
                /*This getter works out the coordinates for a lines surrounding boxes by first
                working out if a line is horizontal or vertical after which it has to work out if a
                line has no box on one side then it will do some maths to calculate the coords of
                those boxes*/
                if (lineY%2==0){
                    if (lineY == 0){
                        return Pair(null,boxes[lineX, lineY/2])
                    }
                    if (lineY == lines.maxHeight-1) {
                        return Pair(boxes[lineX,(lineY-2)/2], null)
                    }
                    return Pair(boxes[lineX, (lineY-2)/2],boxes[lineX, lineY/2])
                }else{
                    if (lineX == 0){
                        return Pair(null, boxes[lineX,(lineY-1)/2])
                    }
                    if(lineX == lines.maxWidth-1){
                        return Pair(boxes[lineX-1,(lineY-1)/2], null)
                    }
                    return Pair(boxes[lineX-1, (lineY-1)/2],boxes[lineX, (lineY-1)/2])
                }
            }

        override fun drawLine() {
            if(this.isDrawn){
                throw Exception("Line already drawn")
            }
            this.isDrawn = true
            //if the player does not need to be changed ie: current player has completed a box then
            // change player will equal false
            var changePlayer = true
            //set boxes which do not have an owning player and have been drawn to belong to the
            // current player
            for (box in adjacentBoxes.toList()){
                if (box != null) {
                    if (box.boundingLines.filter { it.isDrawn }.count() == 4){
                        box.owningPlayer = currentPlayer
                        changePlayer = false
                    }
                }
            }
            if (boxes.filter { it.owningPlayer == null }.count() == 0){
                val scores = getScores().mapIndexed{index, score -> Pair(players[index], score)}
                fireGameOver(scores)
                isFinished = true
            }
            if (changePlayer) {
                if (currentPlayer == players[0]) {
                    currentPlayer = players[1]
                } else {
                    currentPlayer = players[0]
                }
            }
            if (currentPlayer is ComputerPlayer){
                playComputerTurns()
            }
            fireGameChange()
        }
    }

    inner class StudentBox(boxX: Int, boxY: Int) : AbstractBox(boxX, boxY) {

        override var owningPlayer: Player? = null

        /**
         * This must be lazy or a getter, otherwise there is a chicken/egg problem with the boxes
         */
        override val boundingLines: Iterable<DotsAndBoxesGame.Line>
            get() = lines.filter { it.adjacentBoxes.first == this || it.adjacentBoxes.second == this  }
    }
}