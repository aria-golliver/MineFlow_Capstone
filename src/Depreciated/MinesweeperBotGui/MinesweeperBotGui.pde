

import java.util.concurrent.locks.*;
MinesweeperLogic[] logic;
final int TOTAL_THREADS = 3;
final ReentrantLock PIXELS_LOCK = new ReentrantLock();
//final ReentrantLock[] THREAD_LOCKS = ReentrantLock[TOTAL_THREADS];

void setup() {
  noCursor();
  size(1920, 1080, P2D);
  background(0);
  int multiplier = 24;
  //logic = new MinesweeperLogic(30 * multiplier, 16 * multiplier,(int)(99 * multiplier * multiplier));

  logic = new MinesweeperLogic[TOTAL_THREADS];
  for (int i=0;i<logic.length;i++) {
    logic[i] = new MinesweeperLogic(30 * multiplier, 16 * multiplier, (int)(99 * multiplier * multiplier * .95), i);
  }
  //logic = new MinesweeperLogic(9,9,10);
  //println("The board is " + 30 * multiplier + "x" + 16 * multiplier + " with + " + 99 * multiplier * multiplier + " mines.");
  //logic.start();

  frameRate(60);
  loadPixels();
  
  for (int i=0;i<logic.length;i++) {
    logic[i].start();
  }
}

void draw() {
  //PIXELS_LOCK.lock();
  try {
    //loadPixels();
    int[] c = new int[3];
    int tc;
    for (int i = 0; i<pixels.length; i++) {
      // saves a temporary copy of the pixel
      //  and seperates the red, green, and blue components
      tc = pixels[i];
      c[0] = (tc & 0x00ff0000) >> 16;  //red
      c[1] = (tc & 0x0000ff00) >> 8;   //green
      c[2] = (tc & 0x000000ff) >> 0;   //blue
      for(int ic = 0; ic<c.length; ic++){
        if(c[ic] - 0x03 > 0){
          c[ic] -= 0x03;
        //if(c[ic] * .95 > 0){
          //c[ic] *= .95; 
        } else {
          c[ic] = 0;
        }
      }
      pixels[i] = color(c[0],c[1],c[2]);
      
      /*if(pixels[i]-0x050505 > color(0,0,0)){
        pixels[i] -= 0x050505;
      } else {
        pixels[i] = color(0);
      }*/
    }
    updatePixels();
    //saveFrame("img-########.jpg");
  } 
  finally {
    //PIXELS_LOCK.unlock();
  }
  //background 
  //logic.step();

  //logic.draw();
}


