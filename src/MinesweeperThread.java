import java.util.concurrent.atomic.AtomicInteger;

public class MinesweeperThread extends Thread {
	final Board board;
	final AtomicInteger[] cell_color_array;
	final int screen_width,
	screen_height,
	board_width,
	board_height,
	mines;

	public MinesweeperThread(int board_width, int board_height, int screen_width, int screen_height, int mines, AtomicInteger[] cell_color_array){
		this.board_width = board_width;
		this.board_height = board_height;
		this.screen_width = screen_width;
		this.screen_height = screen_height;
		this.mines = mines;

		board = new Board(board_width, board_height, screen_width, screen_height, mines, cell_color_array);
		this.cell_color_array = cell_color_array;
	}

	// call start to start continually solving minesweeper boards
	public void run(){
		while(true){
			// start a new game
			board.new_game();

			// while you have not won or lost
			while(!board.lost && !board.won){
				boolean made_a_move = false;

				/*
				 *  search for every cell looking for places that must be safe to middle_click
				 *  	- you know it's safe when the number of set flags exactly equals
				 *  	  the number of surrounding mines (indicated by board.view_cell(x, y)
				 */
				for(int x = 0; x<board.board_width; x++){
					for(int y = 0; y<board.board_height; y++){
						int surrounding_flags = board.total_arround_cell(x, y, "flagged?", true);

						if(surrounding_flags == board.view_cell(x, y) && 
							   (boolean) board.board[x][y].uncovered){
							made_a_move = board.middle_click(x, y) || made_a_move;
						}
					}
				}

				/*
				 * search for every cell that proves every open cell around it must be flagged
				 * 		- you know they must be flagged if the # of empty spaces around the cell exactly
				 * 		  equals the numbers of surrounding mines (indicated by board.view_cell(x, y)
				 */
				for(int x = 0; x<board.board_width; x++){
					for(int y = 0; y<board.board_height; y++){
						if(board.view_cell(x, y) > 0){
							int surrounding_empty_spaces = board.total_arround_cell(x, y, "uncovered?", false);

							if (surrounding_empty_spaces == board.view_cell(x,y)){
								made_a_move = board.right_click(x-1, y-1) || made_a_move;
								made_a_move = board.right_click(x-1, y  ) || made_a_move;
								made_a_move = board.right_click(x-1, y+1) || made_a_move;
								made_a_move = board.right_click(x  , y+1) || made_a_move;
								made_a_move = board.right_click(x+1, y+1) || made_a_move;
								made_a_move = board.right_click(x+1, y  ) || made_a_move;
								made_a_move = board.right_click(x+1, y-1) || made_a_move;
								made_a_move = board.right_click(x  , y-1) || made_a_move;
							}
						}
					}
				}

				/*
				 * if the solver wasn't able to find any new flags / clicks, it guesses randomly
				 */
				while(!made_a_move){
					int x = (int) (Math.random() * board.board_width);
					int y = (int) (Math.random() * board.board_height);
					made_a_move = board.left_click(x, y);
				}
				board.won = board.check_win();
			}
		}
	}
}
