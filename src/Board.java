import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Board {
	public boolean won, lost;
	//HashMap mine_position;
	public Cell[][] board;

	public final int board_width;
	public final int board_height;
	public final int screen_width;
	public final int screen_height;
	public final int mines;
	private boolean first_move;
	private AtomicInteger[] pixels_array;
	private final float pixel_cell_ratio_width;
	private final float pixel_cell_ratio_height;
	
	public Board(int board_width, int board_height, int screen_width, int screen_height, int mines, AtomicInteger[] pixel_array){
		this.board_width = board_width;
		this.board_height = board_height;
		this.screen_width = screen_width;
		this.screen_height = screen_height;
		this.mines = mines;
		pixel_cell_ratio_width = ((float)screen_width)/board_width;
		pixel_cell_ratio_height = ((float)screen_height)/board_height;

		this.pixels_array = pixel_array;
	}
	
	public void new_game(){
		generate_board();
	}
	
	private void generate_board(){
		board = new Cell[board_width][board_height];
		first_move = true;
		lost = false;
		won = false;
		
		/* 
		 * create the board
		 * it is a 2 dimensional array of Cells
		 */
		
		for(int x = 0; x < board_width; x++){
			for(int y = 0; y < board_height; y++){
				board[x][y] = new Cell();
			}
		}
		
		int mines_left = mines;
		
		//places mines randomly
		while(mines_left > 0){
			int x = (int)(Math.random()*board_width);
			int y = (int)(Math.random()*board_height);
			
			if(!((boolean) board[x][y].mine)){
				board[x][y].mine = true;
				mines_left--;
			}
		}
		
		generate_surrounding_mines_metadata();
	}

	/*
	 * stores how many mines are adjacent to each cell
	 * this information is available to the player after they have revealed the cell
	 */
	private void generate_surrounding_mines_metadata() {
		for(int x = 0; x < board_width; x++){
			for(int y = 0; y < board_height; y++){
				board[x][y].surrounding_mines = total_arround_cell(x,y, "mine?", true);
			}
		}
	}
	
	public int total_arround_cell(int x, int y, String key, boolean GET_TRUE_KEYS){
		int surrounding_true_cells = 0;
		switch(key){
		// I wish to god I could just make this like I could with Clojure
		case "mine?":
			if(!out_of_bounds(x-1,y-1)) if((boolean) board[x-1][y-1].mine) surrounding_true_cells++;
			if(!out_of_bounds(x  ,y-1)) if((boolean) board[x  ][y-1].mine) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y-1)) if((boolean) board[x+1][y-1].mine) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y  )) if((boolean) board[x+1][y  ].mine) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y+1)) if((boolean) board[x+1][y+1].mine) surrounding_true_cells++;
			if(!out_of_bounds(x  ,y+1)) if((boolean) board[x  ][y+1].mine) surrounding_true_cells++;
			if(!out_of_bounds(x-1,y+1)) if((boolean) board[x-1][y+1].mine) surrounding_true_cells++;
			if(!out_of_bounds(x-1,y  )) if((boolean) board[x-1][y  ].mine) surrounding_true_cells++;
			break;
		case "uncovered?":
			if(!out_of_bounds(x-1,y-1)) if((boolean) board[x-1][y-1].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x  ,y-1)) if((boolean) board[x  ][y-1].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y-1)) if((boolean) board[x+1][y-1].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y  )) if((boolean) board[x+1][y  ].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y+1)) if((boolean) board[x+1][y+1].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x  ,y+1)) if((boolean) board[x  ][y+1].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x-1,y+1)) if((boolean) board[x-1][y+1].uncovered) surrounding_true_cells++;
			if(!out_of_bounds(x-1,y  )) if((boolean) board[x-1][y  ].uncovered) surrounding_true_cells++;
			break;
		case "flagged?":
			if(!out_of_bounds(x-1,y-1)) if((boolean) board[x-1][y-1].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x  ,y-1)) if((boolean) board[x  ][y-1].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y-1)) if((boolean) board[x+1][y-1].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y  )) if((boolean) board[x+1][y  ].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x+1,y+1)) if((boolean) board[x+1][y+1].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x  ,y+1)) if((boolean) board[x  ][y+1].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x-1,y+1)) if((boolean) board[x-1][y+1].flagged) surrounding_true_cells++;
			if(!out_of_bounds(x-1,y  )) if((boolean) board[x-1][y  ].flagged) surrounding_true_cells++;
			break;
		}
		
		if(GET_TRUE_KEYS) return surrounding_true_cells;
		return 8 - surrounding_true_cells;
	}
	
	private boolean is_mine(int x, int y){
		if(out_of_bounds(x,y)) return false;
		return (boolean) board[x][y].mine;
	}
	
	public boolean left_click(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		boolean made_a_move = false;
		if(!(boolean) board[x][y].flagged) made_a_move = reveal(x,y);
		return made_a_move;
	}
	
	/*
	 * public function that accepts which cell was middle_clicked on
	 * emulates the middle_click or double_click functionality of most minesweeper boards
	 * basically, it clicks all adjacent cells around a specified cell
	 * 
	 * this version does *not* check to make sure # of flags == # of surrounding mines
	 */
	public boolean middle_click(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		boolean made_a_move = false;
		made_a_move = left_click(x-1,y-1) || made_a_move;
		made_a_move = left_click(x  ,y-1) || made_a_move;
		made_a_move = left_click(x+1,y-1) || made_a_move;
		made_a_move = left_click(x+1,y  ) || made_a_move;
		made_a_move = left_click(x+1,y+1) || made_a_move;
		made_a_move = left_click(x  ,y+1) || made_a_move;
		made_a_move = left_click(x-1,y+1) || made_a_move;
		made_a_move = left_click(x-1,y  ) || made_a_move;
		return made_a_move;
	}
	
	/* 
	 * public way to set flags
	 */
	public boolean right_click(int x, int y){
		if(toggle_flag(x,y)) {
			return true;
		} return false;
		
		
	}
	
	/*
	 * does the actual setting of the flagged? flag
	 * if the cell is already flagged, it does nothing and returns false
	 * if the cell is not flagged, it flags it, then returns true
	 */
	private boolean toggle_flag(int x, int y){
		if(out_of_bounds(x,y)) return false;
		if(!((boolean) board[x][y].uncovered) && !((boolean) board[x][y].flagged)){
			board[x][y].flagged= true;
			set_screen_pixels(x,y);
			return true;
		}
		return false;
	}
	
	/*
	 * recursive function gets a cell location (from either a click or a prior reveal)
	 * and starts revealing from that point in all directions
	 */
	private boolean reveal(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		
		if((boolean) board[x][y].mine && !((boolean) board[x][y].flagged)){
			//if you hit a mine
			if(first_move){
				first_move = false;
				// if it was your first move, move the mine the first cell w/o a mine starting at the top left
				board[x][y].mine = false;
				boolean moved = false;
				for(int iy = 0; iy < board_height; iy++){
					for(int ix = 0; ix < board_width && !moved; ix++){
						if(!(boolean)board[ix][iy].mine){
							board[ix][iy].mine = true;
							moved = true;
						}
					}
				}
				generate_surrounding_mines_metadata();
				reveal(x,y); 
				return true;
			}
			// if it wasn't your first click, you lose
			lost = true;
			return true;
		}
		first_move = false;
		
		// if you didn't hit a mine...
		if((boolean) board[x][y].uncovered){
			// if it's already uncovered, return false
			return false;
		} else {
			// else reveal it
			board[x][y].uncovered = true;
			// and if the cell has no surrounding mines
			if((int) board[x][y].surrounding_mines == 0){
				// reveal all adjacent cells
				reveal(x-1,y-1);
				reveal(x  ,y-1);
				reveal(x+1,y-1);
				reveal(x+1,y  );
				reveal(x+1,y+1);
				reveal(x  ,y+1);
				reveal(x-1,y+1);
				reveal(x-1,y  );
			}
			// draw to pixels_array
			set_screen_pixels(x,y);
			return true;
		}
	}
	
	private void set_screen_pixels(int x, int y) {
		int color;
		/* 
		 * computers the rectangle to be colored
		 * start[x,y] = [x  ,y  ] * screen/board_ratio[width,height]
		 * end[x,y]   = [x+1,y+1] * screen/board_ratio[width,height]
		 */
		int start_x = (int) Math.floor((x  ) * pixel_cell_ratio_width);
		int start_y = (int) Math.floor((y  ) * pixel_cell_ratio_height);
		int end_x =   (int) Math.floor((x+1) * pixel_cell_ratio_width);
		int end_y =   (int) Math.floor((y+1) * pixel_cell_ratio_height);
		/*
		 * the color of the rectangle is based on the uncovered cell's state
		 * 		- flagged   = blue
		 * 		- uncovered	= brighter grey based on number of surrounding mines
		 * 		- else 		= black 
		 */
		if((boolean) board[x][y].flagged){
			color = 0xFF3232FF;
		} else if((boolean) board[x][y].uncovered){
			int surrounding_mines = (int) board[x][y].surrounding_mines;
			int alpha = 0xFF;
			int grey = 0xFF/8 * surrounding_mines;
			color = (alpha << 24) + (grey << 16) + (grey << 8) + (grey << 0);

		} else {
			color = 0xFF000000;
		}
		
		/*
		 * the pixel array is 1-dimensional, so the correct indice  is (y*width) + x instead of [x][y]
		 */
		for(int iy = start_y; iy < end_y; iy++){
			for(int ix = start_x; ix < end_x; ix++){
				pixels_array[(iy * (screen_width)) + ix].set(color);
			}
		}
	}
	
	/*
	 * returns whether or not the board is in a 'win' state, where all non-mine cells are uncovered
	 */
	public boolean check_win() {
		for(int x = 0; x < board_width; x++){
			for(int y = 0; y < board_height; y++){
				if(!(boolean) board[x][y].uncovered && !(boolean) board[x][y].mine) return false;
			}
		}
		return true;
	}

	public void print_board(){
		System.out.println("    0 1 2 3 4 5 6 7 8 9");
		for(int y = 0; y < board_height; y++){
			System.out.print(y+"| ");
			for(int x = 0; x < board_width; x++){
				if((boolean) board[x][y].flagged){
					System.out.print(" F");
				} else if((boolean) board[x][y].mine){
					System.out.print(" #");
				} else if((boolean) board[x][y].uncovered){
					int surrounding_mines = (int) board[x][y].surrounding_mines;
					if(surrounding_mines == 0){
						System.out.print(" .");
					} else {
						System.out.print(" " + surrounding_mines);
					}
				} else {
					System.out.print(" #");
				}
			}
			System.out.println();
		}
	}
	/*
	 * used as a range checker in most functions, to make sure you aren't checking cells off the board
	 */
	private boolean out_of_bounds(int x, int y){
		return (x >= board_width || x < 0 || y >= board_height || y < 0);
	}
	
	/* returns -1 if out of bounds, or is not uncovered
	 * else returns the number of surrounding mines
	 * 
	 */
	public int view_cell(int x, int y) {
		if(out_of_bounds(x,y)) return -1;
		if((boolean) board[x][y].uncovered){
			return (int) board[x][y].surrounding_mines;
		}
		return -1;
	}
	
}
