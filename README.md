This is a simple implementation of a dots and boxes game based on the code and tests provided.
The only extension is that the program wil ask the user weather they would like to play against anoter Humanplayer or a Computer player.

Documentation
StudentDotsBoxGame - This class contains all the logic for the game including its inner classes Student box and student line, which are filled into matrixes which and are used to help draw the UI.
StudentComputerPlayer - This class is the implementation of a computer player which decides on the next best move by prioritising all possible moves and randomly picking one of the best
GameView - GameView is the custom view for the game which initialises the StudentDotsBoxGame class which by default will be a Human player vs a human player but may change if the user selects they want to play against a computer player. GameView only restarts when the app is closed and run again.
