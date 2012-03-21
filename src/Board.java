import java.util.HashMap;
import processing.core.*;

public class Board {
	public boolean won, lost, first_pick;
	//HashMap mine_position;
	public HashMap[][] board;

	public final int width = 9;
	public final int height = 9;
	public final int total_mines = 9;
	
	
	public Board(){
		board = new HashMap[width][height];
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
		if(x >= width || x < 0 || y >= height || y < 0) return false;
		return (boolean) board[x][y].get("mine?");
	}
	
	public static void main(String[] args){
		Board test = new Board();
	}
	
}
