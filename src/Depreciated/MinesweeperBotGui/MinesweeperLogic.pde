
public class MinesweeperLogic extends Thread {
  int wid;
  int hei;
  int mines;
  int losses = 0;
  int wins = 0;
  int id;
  Board board;
  public MinesweeperLogic(int wid, int hei, int mines, int id) {
    this.id = id;
    this.wid = wid;
    this.hei = hei;
    this.mines = mines;
    board = new Board(wid, hei, mines);
    board.new_game();

    int startx = (int) (Math.random() * wid);
    int starty = (int) (Math.random() * hei);

    //board.left_click(startx, starty);
  }
  
  public void run(){
    while(true){ step(); }
  }

  public void step() {
    if (!board.won) {
      boolean made_a_move = false;

      // get board state
      int[][] state = new int[wid][hei];
      for (int y = 0; y<hei; y++) {
        for (int x = 0; x<wid; x++) {
          state[x][y] = board.check_cell(x, y);
        }
      }

      // click all current known spaces

      for (int y = 0; y<hei; y++) {
        for (int x = 0; x<wid; x++) {
          if (state[x][y] >= 1) {
            int surrounding_empty_spaces = 0;

            if (board.check_cell(x-1, y-1) == 0  ||   board.check_cell(x-1, y-1) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x-1, y) == 0    ||   board.check_cell(x-1, y) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x-1, y+1) == 0  ||   board.check_cell(x-1, y+1) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x, y+1) == 0    ||   board.check_cell(x, y+1) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x+1, y+1) == 0  ||   board.check_cell(x+1, y+1) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x+1, y) == 0    ||   board.check_cell(x+1, y) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x+1, y-1) == 0  ||   board.check_cell(x+1, y-1) == -3) surrounding_empty_spaces++;
            if (board.check_cell(x, y-1) == 0    ||   board.check_cell(x, y-1) == -3) surrounding_empty_spaces++;

            if (surrounding_empty_spaces == board.check_cell(x, y)) {
              //board.left_click(x, y);
              made_a_move = board.right_click(x-1, y-1)  || made_a_move;
              made_a_move = board.right_click(x-1, y)    || made_a_move;
              made_a_move = board.right_click(x-1, y+1)  || made_a_move;
              made_a_move = board.right_click(x, y+1)    || made_a_move;
              made_a_move = board.right_click(x+1, y+1)  || made_a_move;
              made_a_move = board.right_click(x+1, y)    || made_a_move;
              made_a_move = board.right_click(x+1, y-1)  || made_a_move;
              made_a_move = board.right_click(x, y-1)    || made_a_move;
              //made_a_move = true;
            }
          }
        }
      }
      if (!made_a_move) {
        for (int y = 0; y<hei; y++) {
          for (int x = 0; x<wid; x++) {
            if (state[x][y] > 0) {
              int surrounding_empty_spaces = 0;
              int surrounding_flags = 0;
              if (board.check_cell(x-1, y-1) == -3) surrounding_flags++;
              if (board.check_cell(x-1, y)   == -3) surrounding_flags++;
              if (board.check_cell(x-1, y+1) == -3) surrounding_flags++;
              if (board.check_cell(x  , y+1) == -3) surrounding_flags++;
              if (board.check_cell(x+1, y+1) == -3) surrounding_flags++;
              if (board.check_cell(x+1, y)   == -3) surrounding_flags++;
              if (board.check_cell(x+1, y-1) == -3) surrounding_flags++;
              if (board.check_cell(x  , y-1) == -3) surrounding_flags++;

              if (board.check_cell(x-1, y-1) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x-1, y) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x-1, y+1) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x, y+1) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x+1, y+1) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x+1, y) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x+1, y-1) == 0) surrounding_empty_spaces++;
              if (board.check_cell(x, y-1) == 0) surrounding_empty_spaces++;

              if (surrounding_flags == state[x][y] && surrounding_empty_spaces != 0) {
                //System.out.println(x+" "+y+" | sur_flags: "+surrounding_flags+" | state: "+state[x][y]);
                made_a_move = board.middle_click(x, y)|| made_a_move;
              }
            }
          }
        }
      }
      if (!made_a_move) {
        while (true) {
          int x = (int) (Math.random()*wid);
          int y = (int) (Math.random()*hei);
          if (state[x][y] == 0) {
            //System.out.println("Picking random " + x + " " + y);
            board.left_click(x, y);
            break;
          }
        }
      }
      //board.printVisibleState();
      /*background(0);
      for (int iy = 0; iy<hei; iy++) {
       for (int ix = 0; ix<wid; ix++) {
       float cell_x = (ix * (((float)width)/wid));
       float cell_y = (iy * (((float)height)/hei));
       drawCell(cell_x, cell_y, (((float)width)/wid), (((float)height)/hei), state[ix][iy]);
       }
       }*/

      board.check_win();
      if (board.lost) { 
        //background(0);
        board.new_game();
        losses++;
        //System.out.println(losses + " losses and "+wins+" wins. " + wins/((float)(losses)+wins)*100.0 +" percent.");
      } 
      else if (board.won) {
        board.new_game();
        wins++;
        //System.out.println(losses + " losses and "+wins+" wins. " + wins/((float)(losses)+wins)*100.0 +" percent.");
        while(true){}
      }
    }
  }
}

