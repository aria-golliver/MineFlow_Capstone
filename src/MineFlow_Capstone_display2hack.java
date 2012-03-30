/* THIS FILE SHOULD BE A NEAR-EXACT COPY OF 
 		MineFlow_Capstone.java
 		
 * the only difference being:
 * set S_WID and S_HEI to your second monitor's screen width and height
 */

import java.util.concurrent.atomic.AtomicInteger;
import processing.core.*;

@SuppressWarnings("serial")
public class MineFlow_Capstone_display2hack extends PApplet{

	final int THREADS = 3;
	
	final int MULTIPLIER = 24;
	final int WID = 30 * MULTIPLIER;
	final int HEI = 16 * MULTIPLIER;
	final int MINES = (int) (99 * MULTIPLIER * MULTIPLIER * .99);
	
	MinesweeperThread[] threads;
	
	AtomicInteger[] cell_color_array;
	final int S_WID = screen.width;
	final int S_HEI = screen.height;
	float pixel_cell_ratio_width;
	float pixel_cell_ratio_height;
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "--bgcolor=#000000", "--hide-stop", "MineFlow_Capstone"});
	    
	    /*
	     * uncomment the following line if you want it to run on two displays
	     * make sure you go into MineFlow_Capstone_display2hack and switch the height/width
	     * to exactly that of your second monitor
	     * 
	     * I use an application called ShiftWindow to shunt the new screen over to the other monitor, because processing's
	     * --display=2 command was doing nothing
	     *
	     * ---------*
	    	PApplet.main(new String[] { "--present", "--bgcolor=#000000", "--hide-stop", "MineFlow_Capstone_display2hack"});
	     /* ---------
	     */
	}
	
	public void setup() {
		size(S_WID,S_HEI,P2D);
		
		frameRate(24);
		noCursor();
		
		cell_color_array = new AtomicInteger[WID*HEI];
		for(int i = 0; i<cell_color_array.length; i++){
			cell_color_array[i] = new AtomicInteger(0x0);
		}
		
		threads = new MinesweeperThread[THREADS];
		for(int i = 0; i<threads.length; i++){
			threads[i] = new MinesweeperThread(WID,HEI,S_WID,S_HEI,MINES,cell_color_array);
		}
		for(int i = 0; i<threads.length; i++){
			threads[i].start();
		}
		

		this.pixel_cell_ratio_width = ((float)S_WID)/WID;
		this.pixel_cell_ratio_height = ((float)S_HEI)/HEI;
	}
	
	public void draw(){
		int current_pixel = 0;
		int seperated_colors[] = new int[3];
		if(frameCount == 1) loadPixels();
		for(int i = 0; i<cell_color_array.length; i++){
			int expected_CAS;
			do{
				expected_CAS = cell_color_array[i].get();
				current_pixel = expected_CAS;
			
				seperated_colors[0] = (current_pixel & 0x00ff0000) >> 16;  // red
				seperated_colors[1] = (current_pixel & 0x0000ff00) >> 8;   // green
				seperated_colors[2] = (current_pixel & 0x000000ff) >> 0;   // blue
				
				for(int color = 0; color<seperated_colors.length; color++){
			        if(seperated_colors[color] - 0x2 > 0){
			        	seperated_colors[color] -= 0x2;
			        } else {
			        	seperated_colors[color] = 0;
			        }
				}
				current_pixel = color(seperated_colors[0],seperated_colors[1],seperated_colors[2]);
			} while(!(cell_color_array[i].compareAndSet(expected_CAS, current_pixel)));
			final int px = i % WID;
			final int py = i / WID;
			final int start_x = (int) (px * pixel_cell_ratio_width);
			final int start_y = (int) (py * pixel_cell_ratio_height);
			final int end_x =   (int) ((px+1) * pixel_cell_ratio_width);
			final int end_y =   (int) ((py+1) * pixel_cell_ratio_height);
			for(int iy = start_y; iy <end_y; iy++){
				for(int ix = start_x; ix <end_x; ix++){
					pixels[iy * S_WID + ix] = current_pixel;
				}
			}
		}
		updatePixels();
	}
}