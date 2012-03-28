import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Board {
	public boolean won, lost;
	//HashMap mine_position;
	public HashMap[][] board;

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
		//generate_board();
	}
	/*
	public Board(AtomicInteger[] pixels){
		board_width = 30*24;
		board_height = 16*24;
		mines = (int) (99*24*24*1.05);
		
		this.pixels_array = pixels;
		
		//generate_board();
	}*/
	
	public void new_game(){
		generate_board();
	}
	
	private void generate_board(){
		board = new HashMap[board_width][board_height];
		first_move = true;
		lost = false;
		won = false;
		
		// create the board
		// it is a 2 dimensional array of hash-maps
		for(int x = 0; x < board_width; x++){
			for(int y = 0; y < board_height; y++){
				board[x][y] = new HashMap();
				//board[x][y].put("x", x);
				//board[x][y].put("y", y);
				board[x][y].put("mine?", false);
				board[x][y].put("flagged?", false);
				board[x][y].put("uncovered?", false);
				board[x][y].put("surrounding_mines", 0);
			}
		}
		
		int mines_left = mines;
		
		while(mines_left > 0){
			int x = (int)(Math.random()*board_width);
			int y = (int)(Math.random()*board_height);
			
			if(!((boolean) board[x][y].get("mine?"))){
				board[x][y].put("mine?", true);
				mines_left--;
			}
		}
		

		generate_surrounding_mines_metadata();

		/*
		for(int y = 0; y < width; y++){
			for(int x = 0; x < height; x++){
				if((boolean) board[x][y].get("mine?")){
					System.out.print(" X");
				} else {
					System.out.print(" .");
				}
			}
			System.out.println();
		}
		
		for(int y = 0; y < width; y++){
			for(int x = 0; x < height; x++){
				System.out.print(" " + board[x][y].get("surrounding_mines"));
			}
			System.out.println();
		}*/
	}

	private void generate_surrounding_mines_metadata() {
		for(int x = 0; x < board_width; x++){
			for(int y = 0; y < board_height; y++){
				board[x][y].put("surrounding_mines", total_arround_cell(x,y, "mine?"));
			}
		}
	}
	
	public int total_arround_cell(int x, int y, String key){
		int surrounding_true_cells = 0;
		if(!out_of_bounds(x-1,y-1)) if((boolean) board[x-1][y-1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x  ,y-1)) if((boolean) board[x  ][y-1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x+1,y-1)) if((boolean) board[x+1][y-1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x+1,y  )) if((boolean) board[x+1][y  ].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x+1,y+1)) if((boolean) board[x+1][y+1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x  ,y+1)) if((boolean) board[x  ][y+1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x-1,y+1)) if((boolean) board[x-1][y+1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x-1,y  )) if((boolean) board[x-1][y  ].get(key)) surrounding_true_cells++;
		return surrounding_true_cells;
	}
	
	public int total_arround_cell(int x, int y, String key, boolean GET_TRUE_KEYS){
		int surrounding_true_cells = 0;
		if(!out_of_bounds(x-1,y-1)) if((boolean) board[x-1][y-1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x  ,y-1)) if((boolean) board[x  ][y-1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x+1,y-1)) if((boolean) board[x+1][y-1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x+1,y  )) if((boolean) board[x+1][y  ].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x+1,y+1)) if((boolean) board[x+1][y+1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x  ,y+1)) if((boolean) board[x  ][y+1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x-1,y+1)) if((boolean) board[x-1][y+1].get(key)) surrounding_true_cells++;
		if(!out_of_bounds(x-1,y  )) if((boolean) board[x-1][y  ].get(key)) surrounding_true_cells++;
		
		if(GET_TRUE_KEYS) return surrounding_true_cells;
		return 8 - surrounding_true_cells;
	}
	
	private boolean is_mine(int x, int y){
		if(out_of_bounds(x,y)) return false;
		return (boolean) board[x][y].get("mine?");
	}
	
	public boolean left_click(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		
		//System.out.println("CLICK (" +x+","+y+")");
		boolean made_a_move = false;
		if(!(boolean) board[x][y].get("flagged?")) made_a_move = reveal(x,y);
		//print_board();
		return made_a_move;
	}
	
	public boolean middle_click(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		//System.out.println("MIDDLE_CLICK (" +x+","+y+")");
		boolean made_a_move = false;
		made_a_move = left_click(x-1,y-1) || made_a_move;
		made_a_move = left_click(x  ,y-1) || made_a_move;
		made_a_move = left_click(x+1,y-1) || made_a_move;
		made_a_move = left_click(x+1,y  ) || made_a_move;
		made_a_move = left_click(x+1,y+1) || made_a_move;
		made_a_move = left_click(x  ,y+1) || made_a_move;
		made_a_move = left_click(x-1,y+1) || made_a_move;
		made_a_move = left_click(x-1,y  ) || made_a_move;
		//print_board();
		return made_a_move;
	}
	
	public boolean right_click(int x, int y){
		
		if(toggle_flag(x,y)) {
			//System.out.println("FLAG " + "("+x+","+y+")");
			//print_board();
			return true;
		} return false;
		
		
	}
	
	private boolean toggle_flag(int x, int y){
		if(out_of_bounds(x,y)) return false;
		if(!((boolean) board[x][y].get("uncovered?")) && !((boolean) board[x][y].get("flagged?"))){
			board[x][y].put("flagged?", true);
			set_screen_pixels(x,y);
			return true;
		}
		return false;
	}
	
	private boolean reveal(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		
		if((boolean) board[x][y].get("mine?") && !((boolean) board[x][y].get("flagged?"))){
			//if you hit a mine
			if(first_move){
				first_move = false;
				// if it was your first move, move the mine the first cell w/o a mine starting at the top left
				board[x][y].put("mine?", false);
				boolean moved = false;
				for(int iy = 0; iy < board_height; iy++){
					for(int ix = 0; ix < board_width && !moved; ix++){
						if(!(boolean)board[ix][iy].get("mine?")){
							board[ix][iy].put("mine?", true);
							moved = true;
						}
					}
				}
				generate_surrounding_mines_metadata();
				reveal(x,y); 
				return true;
			}
			lost = true;
			return true;
		}
		first_move = false;
		//print_board();
		// if you didn't hit a mine...
		if((boolean) board[x][y].get("uncovered?")){
			return false;
		} else {
			board[x][y].put("uncovered?", true);
			if((int) board[x][y].get("surrounding_mines") == 0){
				reveal(x-1,y-1);
				reveal(x  ,y-1);
				reveal(x+1,y-1);
				reveal(x+1,y  );
				reveal(x+1,y+1);
				reveal(x  ,y+1);
				reveal(x-1,y+1);
				reveal(x-1,y  );
			}
			//won = check_win();
			set_screen_pixels(x,y);
			return true;
		}
	}
	
	private void set_screen_pixels(int x, int y) {
		int color;
		
		int start_x = (int) Math.floor((x+0) * pixel_cell_ratio_width);
		int start_y = (int) Math.floor((y+0) * pixel_cell_ratio_height);
		int end_x =   (int) Math.floor((x+1) * pixel_cell_ratio_width);
		int end_y =   (int) Math.floor((y+1) * pixel_cell_ratio_height);
		if((boolean) board[x][y].get("flagged?")){
			color = 0xFF3232FF;
		} else if((boolean) board[x][y].get("uncovered?")){
			int surrounding_mines = (int) board[x][y].get("surrounding_mines");
			int alpha = 0xFF;
			int grey = 0xFF/8 * surrounding_mines;
			color = (alpha << 24) + (grey << 16) + (grey << 8) + (grey << 0);

		} else {
			color = 0xFF000000;
		}
		for(int iy = start_y; iy < end_y; iy++){
			for(int ix = start_x; ix < end_x; ix++){
				pixels_array[(iy * (screen_width)) + ix].set(color);
			}
		}
	}

	public boolean check_win() {
		for(int x = 0; x < board_width; x++){
			for(int y = 0; y < board_height; y++){
				if(!(boolean) board[x][y].get("uncovered?") && !(boolean) board[x][y].get("mine?")) return false;
			}
		}
		return true;
	}

	public void print_board(){
		System.out.println("    0 1 2 3 4 5 6 7 8 9");
		for(int y = 0; y < board_height; y++){
			System.out.print(y+"| ");
			for(int x = 0; x < board_width; x++){
				if((boolean) board[x][y].get("flagged?")){
					System.out.print(" F");
				} else if((boolean) board[x][y].get("mine?")){
					System.out.print(" #");
				} else if((boolean) board[x][y].get("uncovered?")){
					int surrounding_mines = (int) board[x][y].get("surrounding_mines");
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
	
	private boolean out_of_bounds(int x, int y){
		return (x >= board_width || x < 0 || y >= board_height || y < 0);
	}
	
	
	
	/*public static void main(String[] args){
		Board test = new Board(new AtomicInteger[1]);
		test.new_game();
		//test.print_board();
		test.right_click(0,0);
		System.out.println(test.board[0][0].get("flagged?"));
		//test.left_click(3, 3);
		System.out.println("------------");
		test.print_board();
		//test.left_click(4, 8);
		System.out.println("------------");
		test.print_board();
		//test.left_click(6, 2);
		System.out.println("------------");
		test.print_board();
	}*/

	public int view_cell(int x, int y) {
		if(out_of_bounds(x,y)) return -1;
		if((boolean) board[x][y].get("uncovered?")){
			return (int) board[x][y].get("surrounding_mines");
		}
		return -1;
	}
	
}
