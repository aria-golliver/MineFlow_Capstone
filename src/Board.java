import java.util.HashMap;
import processing.core.*;

public class Board {
	public boolean won, lost;
	//HashMap mine_position;
	public HashMap[][] board;

	public final int width = 9;
	public final int height = 9;
	public final int total_mines = 9;
	private boolean first_move;
	
	public Board(){
		generate_board();
	}
	
	public void generate_board(){
		board = new HashMap[width][height];
		first_move = true;
		lost = false;
		won = false;
		
		// create the board
		// it is a 2 dimensional array of hash-maps
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				board[x][y] = new HashMap();
				board[x][y].put("x", x);
				board[x][y].put("y", y);
				board[x][y].put("mine?", false);
				board[x][y].put("flagged?", false);
				board[x][y].put("uncovered?", false);
				board[x][y].put("surrounding_mines", 0);
			}
		}
		int mines_left = total_mines;
		while(mines_left > 0){
			int x = (int)(Math.random()*width);
			int y = (int)(Math.random()*height);
			
			if(!((boolean) board[x][y].get("mine?"))){
				board[x][y].put("mine?", true);
				mines_left--;
			}
		}
		

		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				board[x][y].put("surrounding_mines", get_surrounding_mines(x,y));
			}
		}

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
		}
	}
	
	public int get_surrounding_mines(int x, int y){
		int surrounding_mines = 0;
		if(is_mine(x-1,y-1)) surrounding_mines++;
		if(is_mine(x  ,y-1)) surrounding_mines++;
		if(is_mine(x+1,y-1)) surrounding_mines++;
		if(is_mine(x+1,y  )) surrounding_mines++;
		if(is_mine(x+1,y+1)) surrounding_mines++;
		if(is_mine(x  ,y+1)) surrounding_mines++;
		if(is_mine(x-1,y+1)) surrounding_mines++;
		if(is_mine(x-1,y  )) surrounding_mines++;
		return surrounding_mines;
	}
	
	private boolean is_mine(int x, int y){
		if(out_of_bounds(x,y)) return false;
		return (boolean) board[x][y].get("mine?");
	}
	
	public boolean left_click(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		return reveal(x,y);
	}
	
	public boolean middle_click(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		boolean made_a_move = false;
		made_a_move = reveal(x-1,y-1) || made_a_move;
		made_a_move = reveal(x  ,y-1) || made_a_move;
		made_a_move = reveal(x+1,y-1) || made_a_move;
		made_a_move = reveal(x+1,y  ) || made_a_move;
		made_a_move = reveal(x+1,y+1) || made_a_move;
		made_a_move = reveal(x  ,y+1) || made_a_move;
		made_a_move = reveal(x-1,y+1) || made_a_move;
		made_a_move = reveal(x-1,y  ) || made_a_move;
		return made_a_move;
	}
	
	private boolean reveal(int x, int y){
		if(out_of_bounds(x,y) || won || lost) return false;
		
		if((boolean) board[x][y].get("mine?") && !((boolean) board[x][y].get("flag?"))){
			//if you hit a mine
			if(first_move){
				// if it was your first move, move the mine the first cell w/o a mine starting at the top left
				board[x][y].put("mine?", false);
				boolean moved = false;
				for(int iy = 0; iy < height; iy++){
					for(int ix = 0; ix < width && !moved; ix++){
						if(!(boolean)board[ix][iy].get("mine?")){
							board[ix][iy].put("mine?", true);
							moved = true;
						}
					}
				}
				reveal(x,y);
				return true;
			}
			lost = true;
			return true;
		}
		
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
			return true;
		}
	}
	
	private boolean out_of_bounds(int x, int y){
		return (x >= width || x < 0 || y >= height || y < 0);
	}
	
	public static void main(String[] args){
		Board test = new Board();
	}
	
}
