import java.util.concurrent.atomic.AtomicInteger;

import processing.core.*;

public class MineFlow_Capstone extends PApplet{
	final static int WID = 30 * 24;
	final static int HEI = 16 * 24;
	final static int S_WID = 1920;
	final static int S_HEI = 1080;
	final static int THREADS = 3;
	
	MinesweeperThread[] threads;
	boolean first = true;
	
	AtomicInteger[] pixel_array;
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "MineFlow_Capstone"});
	}
	
	public void setup() {
		size(S_WID,S_HEI,P2D);
		background(0);
		frameRate(60);
		pixel_array = new AtomicInteger[S_WID*S_HEI];
		for(int i = 0; i<pixel_array.length; i++){
			pixel_array[i] = new AtomicInteger(0);
		}
		
		threads = new MinesweeperThread[THREADS];
		for(int i = 0; i<threads.length; i++){
			threads[i] = new MinesweeperThread(pixel_array);
		}
		for(int i = 0; i<threads.length; i++){
			threads[i].start();
		}
	}
	
	public void draw(){
		
		int current_pixel = 0;
		int seperated_colors[] = new int[3];
		if(frameCount == 1) loadPixels();
		for(int i = 0; i<pixel_array.length; i++){
			int expected_CAS;
			do{
				expected_CAS = pixel_array[i].get();
				current_pixel = expected_CAS;
			
			seperated_colors[0] = (current_pixel & 0x00ff0000) >> 16;  //red
			seperated_colors[1] = (current_pixel & 0x0000ff00) >> 8;   //green
			seperated_colors[2] = (current_pixel & 0x000000ff) >> 0;   //blue
			
			for(int color = 0; color<seperated_colors.length; color++){
		        if(seperated_colors[color] - 0x3 > 0){
		          seperated_colors[color] -= 0x3;
		        } else {
		          seperated_colors[color] = 0;
		        }
			}
			current_pixel = color(seperated_colors[0],seperated_colors[1],seperated_colors[2]);
			pixels[i] = current_pixel;
			} while(!(pixel_array[i].compareAndSet(expected_CAS, current_pixel)));
			//pixels[i] = 0xFFFFFFFF;
		}
		updatePixels();
		//saveFrame("img-########.jpg");
	}
}
