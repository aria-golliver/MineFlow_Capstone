import java.util.concurrent.atomic.AtomicInteger;
import processing.core.*;

public class MineFlow_Capstone extends PApplet{
	final static int MULTIPLIER = 24;
	final static int WID = 30 * MULTIPLIER;
	final static int HEI = 16 * MULTIPLIER;
	final static int S_WID = 1920;//WID * 2;
	final static int S_HEI = 1080;///HEI * 2;
	final static int THREADS = 3;
	final static int MINES = (int) (99 * MULTIPLIER * MULTIPLIER * .99);
	
	
	final static int[] frame_rates = {1,3,5,7,9,12,15,18,21,24,27,30,33,36,39,42,45,48,51,54,57,60};
	int current_frame_rate = frame_rates.length - 1;
	
	MinesweeperThread[] threads;
	boolean first = true;
	
	AtomicInteger[] cell_color_array;
	float pixel_cell_ratio_width;
	float pixel_cell_ratio_height;
	
	public static void main(String args[]) {
	    //PApplet.main(new String[] { "--present", "--bgcolor=#000000", "--hide-stop", "MineFlow_Capstone"});
	    PApplet.main(new String[] { "MineFlow_Capstone"});
	}
	
	public void setup() {
		size(S_WID,S_HEI,P2D);
		frameRate(frame_rates[current_frame_rate]);
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
			
				seperated_colors[0] = (current_pixel & 0x00ff0000) >> 16;  //red
				seperated_colors[1] = (current_pixel & 0x0000ff00) >> 8;   //green
				seperated_colors[2] = (current_pixel & 0x000000ff) >> 0;   //blue
				
				for(int color = 0; color<seperated_colors.length; color++){
			        if(seperated_colors[color] - 0x5 > 0){
			        	seperated_colors[color] -= 0x5;
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
		
		// display frame rate aprox. every 1 second
		if(frameCount % frame_rates[current_frame_rate] == 0) System.out.println(frameRate);
		
		//saveFrame("img-########.jpg");
	}
	
	public void keyPressed() {
		current_frame_rate = (current_frame_rate + 1) % frame_rates.length;
		frameRate(frame_rates[current_frame_rate]);
		System.out.println(frame_rates[current_frame_rate]);
	}
}
