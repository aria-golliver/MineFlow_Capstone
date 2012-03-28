public class Cell {
	public boolean mine,
				   flagged,
				   uncovered;
	
	public int surrounding_mines;
	
	public Cell(){
		mine = false;
		flagged = false;
		uncovered = false;
		surrounding_mines = 0;
	}
}
