import java.util.concurrent.atomic.AtomicInteger;
import processing.core.*;

public class MineFlow_Capstone extends PApplet{
	final static int MULTIPLIER = 24;
	final static int WID = 30 * MULTIPLIER;
	final static int HEI = 16 * MULTIPLIER;
	final static int S_WID = WID * 2;
	final static int S_HEI = HEI * 2;
	final static int THREADS = 3;
	final static int MINES = (int) (99 * MULTIPLIER * MULTIPLIER * .99);
	
	MinesweeperThread[] threads;
	boolean first = true;
	
	AtomicInteger[] pixel_array;
	
	public static void main(String args[]) {
	    PApplet.main(new String[] { "--present", "--bgcolor=#000000", "--hide-stop", "MineFlow_Capstone"});
	    //PApplet.main(new String[] { "MineFlow_Capstone"});
	}
	
	public void setup() {
		size(S_WID,S_HEI,P2D);
		frameRate(24);
		noCursor();
		pixel_array = new AtomicInteger[S_WID*S_HEI];
		for(int i = 0; i<pixel_array.length; i++){
			pixel_array[i] = new AtomicInteger(0x0);
		}
		
		threads = new MinesweeperThread[THREADS];
		for(int i = 0; i<threads.length; i++){
			threads[i] = new MinesweeperThread(WID,HEI,S_WID,S_HEI,MINES,pixel_array);
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
			        if(seperated_colors[color] - 0x5 > 0){
			        	seperated_colors[color] -= 0x5;
			        } else {
			        	seperated_colors[color] = 0;
			        }
				}
				current_pixel = color(seperated_colors[0],seperated_colors[1],seperated_colors[2]);
				pixels[i] = current_pixel;
			} while(!(pixel_array[i].compareAndSet(expected_CAS, current_pixel)));
		}
		updatePixels();
		
		// compute frame rate
		if(frameCount % 24 == 0) System.out.println(frameRate);
		//saveFrame("img-########.jpg");
	}
	
	public void keyPressed() {
		//exit();
	}
}
