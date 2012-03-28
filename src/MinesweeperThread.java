import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
public class MinesweeperThread extends Thread {
	final Board board;
	final AtomicInteger[] pixels;
	final int screen_width;
	final int screen_height;
	final int board_width;
	final int board_height;
	final int mines;
	
	public static void main(String[] args){
		//MinesweeperThread ms = new MinesweeperThread(new AtomicInteger[screen_width * screen_height]);
		//while(true) ms.run();
		
	}
	
	public MinesweeperThread(int board_width, int board_height, int screen_width, int screen_height, int mines, AtomicInteger[] pixel_array){
		this.board_width = board_width;
		this.board_height = board_height;
		this.screen_width = screen_width;
		this.screen_height = screen_height;
		this.mines = mines;
		
		board = new Board(board_width, board_height, screen_width, screen_height, mines, pixel_array);
		this.pixels = pixel_array;
	}

	// call start to start continually solving minesweeper boards
	public void run(){
		while(true){
			board.new_game();
			
			while(!board.lost && !board.won){
				boolean made_a_move = false;
				
				for(int x = 0; x<board.board_width; x++){
					//System.out.println("1");
					for(int y = 0; y<board.board_height; y++){
						if(board.view_cell(x, y) > 0){
							
							int surrounding_empty_spaces = board.total_arround_cell(x, y, "uncovered?", false);
							//int surrounding_flags = board.total_arround_cell(x, y, "flagged?");
							
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
				try {
					sleep(0);
				} catch (InterruptedException e) {}*/
				int total_clicks = 0;
				if(!made_a_move){
				for(int x = 0; x<board.board_width; x++){
					//System.out.println("2");
					for(int y = 0; y<board.board_height; y++){
						int surrounding_empty_spaces = board.total_arround_cell(x, y, "uncovered?", false);
						int surrounding_flags = board.total_arround_cell(x, y, "flagged?");
						
						if(surrounding_empty_spaces > 0 && 
						   surrounding_flags == board.view_cell(x, y) && 
						   (boolean) board.board[x][y].get("uncovered?")/* &&
						   total_clicks < 100*/){
							total_clicks++;
								made_a_move = board.middle_click(x, y) || made_a_move;
						}
					}
				}
				}
				while(!made_a_move){
					int x = (int) (Math.random() * board.board_width);
					int y = (int) (Math.random() * board.board_height);
					//System.out.println("guessing (" +x+","+y+")");
					made_a_move = board.left_click(x, y);
				}
				board.won = board.check_win();
				if(board.won){
					//System.out.println("WON!");
					//board.print_board();
					//while(true){}
				} else if(board.lost){
					//System.out.println("LOST!");
					//board.print_board();
					//while(true){}
				}
			}
		}
	}
}
