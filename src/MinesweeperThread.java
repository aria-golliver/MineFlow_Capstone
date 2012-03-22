import java.util.HashMap;
public class MinesweeperThread extends Thread {
	Board board;
	
	public MinesweeperThread(){
		board = new Board();
	}

	
	public void run(){
		board.new_game();
		int startx = (int) (Math.random() * board.width);
		int starty = (int) (Math.random() * board.height);
		while(!board.lost && !board.won){
			boolean made_a_move = false;
			for(int x = 0; x<board.width; x++){
				for(int y = 0; y<board.width; y++){
					if(!(boolean) board.board[x][y].get("revealed?")){
						
						int surrounding_empty_spaces = board.total_arround_cell(x, y, "revealed?", false);
						int surrounding_flags = board.total_arround_cell(x, y, "revealed?");
						
						if (surrounding_empty_spaces == (int) board.board[x][y].get("surrounding_mines")){
							made_a_move = board.right_click(x-1, y-1)  || made_a_move;
							made_a_move = board.right_click(x-1, y)    || made_a_move;
							made_a_move = board.right_click(x-1, y+1)  || made_a_move;
							made_a_move = board.right_click(x, y+1)    || made_a_move;
							made_a_move = board.right_click(x+1, y+1)  || made_a_move;
							made_a_move = board.right_click(x+1, y)    || made_a_move;
							made_a_move = board.right_click(x+1, y-1)  || made_a_move;
							made_a_move = board.right_click(x, y-1)    || made_a_move;
						}
					}
				}
			}

			for(int x = 0; x<board.width; x++){
				for(int y = 0; y<board.width; y++){
					int surrounding_empty_spaces = board.total_arround_cell(x, y, "revealed?", false);
					int surrounding_flags = board.total_arround_cell(x, y, "revealed?");
					
					if(surrounding_empty_spaces > 0 && surrounding_flags == board.view_cell(x, y));
					made_a_move = board.middle_click(x, y) || made_a_move;
				}
			}
		}
	}
}
