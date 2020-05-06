package org.example.student.dotsboxgame

import uk.ac.bournemouth.ap.dotsandboxeslib.ComputerPlayer
import uk.ac.bournemouth.ap.dotsandboxeslib.DotsAndBoxesGame
import kotlin.random.Random

/*This class implements a basic computer player which overrides the computer player abstract class in
* Player.kt from the lib module*/
class StudentComputerPlayer : ComputerPlayer() {
    /*The computer decides on the best move to make by filtering all the undrawn boxes into a list,
    * counting the surrounding lines and prioritising boxes with one line left till completion as
    * high priority, boxes with 2 lines remaining as lowest priority(this would give the user the
    * box) and giving boxes with no drawn lines or 1 drawn line medium priority. After the box is
    * chosen the computer then randomly picks an undrawn line to play*/
    override fun makeMove(game: DotsAndBoxesGame) {
        val undrawnBoxes = game.boxes.filter { it.owningPlayer == null }
        var chosenLine: DotsAndBoxesGame.Line
        var chosenBox: DotsAndBoxesGame.Box

        var lowestPriorityBoxes: List<DotsAndBoxesGame.Box> = listOf()
        var lowPriorityBoxes: List<DotsAndBoxesGame.Box> = listOf()
        var medPriorityBoxes: List<DotsAndBoxesGame.Box> = listOf()
        var highPriorityBoxes: List<DotsAndBoxesGame.Box> = listOf()

        for (boxes in undrawnBoxes) {
            when (boxes.boundingLines.filter { it.isDrawn }.count()) {
                3 -> highPriorityBoxes = highPriorityBoxes.plus(boxes)
                2 -> lowestPriorityBoxes = lowestPriorityBoxes.plus(boxes)
                1,0 -> medPriorityBoxes = medPriorityBoxes.plus(boxes)
            }
        }

        if (!highPriorityBoxes.isNullOrEmpty()) {
            chosenBox = highPriorityBoxes[Random.nextInt(0, highPriorityBoxes.size)]
        } else if (!medPriorityBoxes.isNullOrEmpty()) {
            chosenBox = medPriorityBoxes[Random.nextInt(0, medPriorityBoxes.size)]
        } else if (!lowPriorityBoxes.isNullOrEmpty()) {
            chosenBox = lowPriorityBoxes[Random.nextInt(0, lowPriorityBoxes.size)]
        } else {
            chosenBox = lowestPriorityBoxes[Random.nextInt(0, lowestPriorityBoxes.size)]
        }

        val chosenBoxesUndrawnLines = chosenBox.boundingLines.filter { !it.isDrawn }
        chosenLine = chosenBoxesUndrawnLines[Random.nextInt(0, chosenBoxesUndrawnLines.size)]
        chosenLine.drawLine()
    }
}