package com.example.tictactoeusingminimaxalgorithm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //Creating a 2D Array of ImageViews
    private val boardCells = Array(3) { arrayOfNulls<ImageView>(3) }

    //Creating the board instance
    var board = Board()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //calling the function to load out tic tac toe board
        loadBoard()

        //Restarting the game
        button_restart.setOnClickListener {
            /**
             * Creating a new board instance
             * emptying every cell
             */
            board = Board()

            //Setting the results to empty
            text_view_result.text = ""

            mapBoardToUi()
        }
    }

    //Mapping the internal board to the ImageView array board
    private fun mapBoardToUi() {
        for (i in board.board.indices) {
            for (j in board.board.indices) {
                when (board.board[i][j]) {
                    Board.PLAYER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.circle)
                        boardCells[i][j]?.isEnabled = false
                    }
                    Board.COMPUTER -> {
                        boardCells[i][j]?.setImageResource(R.drawable.cross)
                        boardCells[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardCells[i][j]?.setImageResource(0)
                        boardCells[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    // This function is generating the tic tac toe board
    private fun loadBoard() {
        for (i in boardCells.indices) {
            for (j in boardCells.indices) {
                boardCells[i][j] = ImageView(this)
                boardCells[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 250
                    height = 250
                    bottomMargin = 5
                    topMargin = 5
                    leftMargin = 5
                    rightMargin = 5
                }
                boardCells[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))

                //attached a click listener to the board
                boardCells[i][j]?.setOnClickListener(cellClickListener(i, j))
                layout_board.addView(boardCells[i][j])
            }
        }
    }

    inner class cellClickListener(
        private val i: Int,
        private val j: Int
    ) : View.OnClickListener {

        override fun onClick(v: View?) {
            //Checking if the game is not over
            if(!board.isGameOver) {
                //creating a new cell with the clicked index
                val cell = Cell(i, j)
                //placing the move for player
                board.placeMove(cell, Board.PLAYER)
                //calling minimax to calculate the computer move
                board.minimax(0, Board.COMPUTER)
                //performing the move for computer
                board.computersMove?.let{
                    board.placeMove(it, Board.COMPUTER)
                }

                //mapping the internal board to visual board
                mapBoardToUi()
            }

            //Displaying the result
            when{
                board.hasComputerWon() -> text_view_result.text = "You lose😢"
                board.hasPlayerWon() -> text_view_result.text = "You won😁"
                board.isGameOver -> text_view_result.text = "Game Tied🤷‍♂️"
            }
        }
    }
}

