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
				board[x][y].put("surrounding_mines?", 0);
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
		
		for(int x = 0; x < width; x++)
			for(int y = 0; y < height; y++)
				if((boolean) board[x][y].get("mine?"))
					System.out.println("("+x+","+y+")");
			
		
	}
	
	
	public static void main(String[] args){
		Board test = new Board();
	}
	
}
