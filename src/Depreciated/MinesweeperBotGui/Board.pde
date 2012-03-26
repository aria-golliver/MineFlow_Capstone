import java.io.Console;

class Board {
  public boolean won;
  public boolean lost;
  public boolean first_pick;

  private int[][] hidden_board_state;
  private int[][] visible_board_state;
  private boolean[][] mine_position;
  private boolean[][] flagged;

  private int wid, hei, total_mines;

  public Board(int wid, int hei, int total_mines) {
    this.wid = wid;
    this.hei = hei;
    this.total_mines = total_mines;

    // generateBoard();
  }

  public int check_cell(int x, int y) {
    if (x >= wid || x < 0 || y >= hei || y < 0) {
      return -100;
    }
    return(visible_board_state[x][y]);
  }

  public void new_game() {
    won = false;
    lost = false;
    first_pick = true;
    generateBoard();
  }

  public boolean left_click(int x, int y) {
    boolean made_a_move = false;
    if (x >= wid || x < 0 || y >= hei || y < 0) return false;
    if (visible_board_state[x][y] != 0) return false;
    made_a_move = checkSquare(x, y) || made_a_move;
    //System.out.println("leftclick: "+x+" "+y);
    while (first_pick && lost) {
      lost = false;
      generateBoard();
      made_a_move = checkSquare(x, y) || made_a_move;
      //printMinePosition();
    }
    first_pick = false;

    float cell_x = (x * (((float)width)/wid));
    float cell_y = (y * (((float)height)/hei));
    drawCell(cell_x, cell_y, (((float)width)/wid), (((float)height)/hei), visible_board_state[x][y]);

    return made_a_move;
  }

  public boolean right_click(int x, int y) {
    if (x >= wid || x < 0 || y >= hei || y < 0) return false;

    if (visible_board_state[x][y] == -3) {
      return false;
    } 
    else if (visible_board_state[x][y] == 0) {
      visible_board_state[x][y] = -3;
      float cell_x = (x * (((float)width)/wid));
      float cell_y = (y * (((float)height)/hei));
      drawCell(cell_x, cell_y, (((float)width)/wid), (((float)height)/hei), visible_board_state[x][y]);
      return true;
    }
    return false;
  }

  public boolean middle_click(int x, int y) {
    boolean made_a_move = false;
    made_a_move = left_click(x-1, y-1) || made_a_move;
    made_a_move = left_click(x-1, y)   || made_a_move;
    made_a_move = left_click(x-1, y+1) || made_a_move;
    made_a_move = left_click(x, y+1) || made_a_move;
    made_a_move = left_click(x+1, y+1) || made_a_move;
    made_a_move = left_click(x+1, y)   || made_a_move;
    made_a_move = left_click(x+1, y-1) || made_a_move;
    made_a_move = left_click(x, y-1) || made_a_move;
    return made_a_move;
  }

  //private void flag(int x, int y){

  //}

  /*public static void main(String args[]){
   		Board board = new Board(9,9,10);
   		
   		board.new_game();
   		
   		board.printMinePosition();
   		System.out.println("-------------------------------");
   		board.printBoardState();
   		System.out.println("-------------------------------");
   		board.printVisibleState();
   		while(true){
   			board.getInput();
   			board.printVisibleState();
   		}
   	}*/

  private boolean checkSquare(int x, int y) {
    boolean made_a_move = false;
    if (x < 0 || x >= wid || y < 0 || y >= hei) return false;
    switch(hidden_board_state[x][y]) {
    case -1:
      made_a_move = true;
      //System.out.println("YOU LOSE!");
      lost = true;
      return made_a_move;
    case 0:
      if (visible_board_state[x][y] == 0) {
        made_a_move = true;
        visible_board_state[x][y] = -2;
        checkSquare(x-1, y-1);
        checkSquare(x-1, y);
        checkSquare(x-1, y+1);
        checkSquare(x, y+1);
        checkSquare(x+1, y+1);
        checkSquare(x+1, y);
        checkSquare(x+1, y-1);
        checkSquare(x, y-1);

        float cell_x = (x * (((float)width)/wid));
        float cell_y = (y * (((float)height)/hei));
        drawCell(cell_x, cell_y, (((float)width)/wid), (((float)height)/hei), visible_board_state[x][y]);
        return made_a_move;
      }
      return false;
    default:
      made_a_move = true;
      if (visible_board_state[x][y] !=-3) visible_board_state[x][y] = hidden_board_state[x][y];

      float cell_x = (x * (((float)width)/wid));
      float cell_y = (y * (((float)height)/hei));
      drawCell(cell_x, cell_y, (((float)width)/wid), (((float)height)/hei), visible_board_state[x][y]);
      return made_a_move;
    }
  }

  private void getInput() {
    Console console = System.console();
    String input_x = console.readLine("x: ");
    String input_y = console.readLine("y: ");
    System.out.println("("+input_x+","+input_y+")");
    try {
      int x = Integer.parseInt(input_x);
      int y = Integer.parseInt(input_y);
      if (x<wid && y<hei) {
        left_click(x, y);
      }
    }
    catch (NumberFormatException e) {
    }
  }

  private void generateBoard() {
    // clear board
    hidden_board_state = new int[wid][hei];
    mine_position = new boolean[wid][hei];
    visible_board_state = new int[wid][hei];
    flagged = new boolean[wid][hei];
    //System.out.print("(" + wid + "," + hei + ")\n\n");
    for (int iy = 0; iy < hei; iy++) {
      for (int ix = 0; ix < wid; ix++) {
        hidden_board_state[ix][iy] = 0;
        mine_position[ix][iy] = false;
        visible_board_state[ix][iy] = 0;
        flagged[ix][iy] = false;
      }
    }

    // place mines
    int placed_mines = 0;
    while (placed_mines < total_mines) {
      int px = (int)(Math.random()*wid);
      int py = (int)(Math.random()*hei);
      if (!mine_position[px][py]) {
        mine_position[px][py] = true;
        placed_mines++;
      }
    }

    //updateBoardState
    generateBoardState();
  }

  private void generateBoardState() {
    for (int iy = 0; iy<hei; iy++) {
      for (int ix=0; ix<wid; ix++) {
        if (mine_position[ix][iy]) {
          hidden_board_state[ix][iy] = -1;
        } 
        else {
          try {
            if (mine_position[ix-1][iy-1]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix][iy-1]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix+1][iy-1]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix+1][iy]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix+1][iy+1]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix][iy+1]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix-1][iy+1]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }

          try {
            if (mine_position[ix-1][iy]) {
              hidden_board_state[ix][iy]++;
            }
          } 
          catch(Exception e) {
          }
        }
      }
    }
  }

  private void printBoardState() {
    System.out.println("    0 1 2 3 4 5 6 7 8");
    System.out.println("    -----------------");
    for (int iy = 0; iy < hei; iy++) {
      System.out.print(iy + " |");
      for (int ix = 0; ix < wid; ix++) {
        if (hidden_board_state[ix][iy]>=0) {
          if (hidden_board_state[ix][iy] == 0) {
            System.out.print(" .");
          } 
          else {
            System.out.print(" " + hidden_board_state[ix][iy]);
          }
        } 
        else {
          System.out.print("  ");
        }
      }
      System.out.println("");
    }
  }

  private void printMinePosition() {
    for (int iy = 0; iy < hei; iy++) {
      for (int ix = 0; ix < wid; ix++) {
        if (mine_position[ix][iy]) {
          System.out.print(" X");
        } 
        else {
          System.out.print(" .");
        }
      }
      System.out.println(" ");
    }
  }

  public void printVisibleState() {
    //System.out.println("\t    0 1 2 3 4 5 6 7 8");
    //System.out.println("    -----------------");
    for (int iy = 0; iy < hei; iy++) {
      //System.out.print(iy + "\t|");
      for (int ix = 0; ix < wid; ix++) {
        switch(visible_board_state[ix][iy]) {
        case -3:
          System.out.print(" °");
          break;
        case -2:
          System.out.print("  ");
          break;
        case -1:
          System.out.print(" X");
          break;
        case 0:
          System.out.print(" ·");
          break;
        default:
          System.out.print(" " + visible_board_state[ix][iy]);
          break;
        }
      }
      System.out.println("");
    }
    System.out.println("--------------------------------------");
  }

  public void check_win() {
    int uncovered_spaces = 0;
    for (int y = 0; y<hei; y++) {
      for (int x = 0; x<wid; x++) {
        if (visible_board_state[x][y] == 0 ||visible_board_state[x][y] == -3) {
          uncovered_spaces++;
        }
      }
    }
    if (uncovered_spaces == total_mines) {
      won = true;
    }
  }
  void drawCell(float px, float py, float wid, float hei, int ID) {
    //PIXELS_LOCK.lock();
    try {
    color col = 0;
      switch(ID) {
      case -3:
        col = 0xFF3232FF;
        //fill(50, 50, 255);
        break;
      case -2:
        col = 0xFF000000;
        //fill(0, 0, 0);
        break;
      case -1:
        col = 0xFF0000FF;
        //fill(0, 0, 255);
        break;
      case 0:
        col = 0xFF000000;
       // fill(0, 0, 0);
        return;
      case 1:
        col = color(255/8);
       // fill(col, col, col);
        break;
      case 2:
        col =  color(255/8 * 2);
       // fill(col, col, col);
        break;
      case 3:
        col =  color(255/8 * 3);
        //fill(col, col, col);
        break;
      case 4:
        col =  color(255/8 * 4);
       //fill(col, col, col);
        break;
      case 5:
        col =  color(255/8 * 5);
       // fill(col, col, col);
        break;
      case 6:
        col =  color(255/8 * 6);
       // fill(col, col, col);
        break;
      case 7:
        col =  color(255/8 * 7);
       // fill(col, col, col);
        break;
      case 8:
        col = color(255);
       // fill(255, 0, 0);
        break;
      }
      noStroke();
      for(int ix = (int) px; ix<(int)(px+wid); ix++){
        for(int iy = (int) py; iy<(int)(py+hei); iy++){
          pixels[iy*width+ix] = col;
        }
      }
      //rect(px, py, wid, hei);
    } 
    finally {
      //PIXELS_LOCK.unlock();
    }
  }
}

