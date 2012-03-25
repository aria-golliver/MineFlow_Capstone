import processing.core.*;

public class MineFlow_Capstone extends PApplet{
	final static int WID = 30;
	final static int HEI = 16;
	final static int S_WID = 1920;
	final static int S_HEI = 1080;
	
	private Pixel_testing pt;
	boolean first = true;
	
	int[] pixel_array;
	
	public void setup() {
		size(S_WID,S_HEI,P2D);
		background(0);
		pixel_array = new int[S_WID*S_HEI];
		for(int i = 0; i<pixel_array.length; i++){
			pixel_array[i] = 0;
		}
		//MinesweeperThread threads = new MinesweeperThread(pixels);
		//threads.start();
		System.out.println(2);
	}
	public void draw(){
		if(first){
			pt = new Pixel_testing(pixel_array);
		}
		pt.start();
		int current_pixel;
		int seperated_colors[] = new int[3];
		loadPixels();
		for(int i = 0; i<pixel_array.length; i++){
			current_pixel = pixel_array[i];
			seperated_colors[0] = (current_pixel & 0x00ff0000) >> 16;  //red
			seperated_colors[1] = (current_pixel & 0x0000ff00) >> 8;   //green
			seperated_colors[2] = (current_pixel & 0x000000ff) >> 0;   //blue
			
			for(int color = 0; color<seperated_colors.length; color++){
		        if(seperated_colors[color] - 0x03 > 0){
		          seperated_colors[color] -= 0x03;
		        } else {
		          seperated_colors[color] = 0;
		        }
			}
			pixels[i] = color(seperated_colors[0],seperated_colors[1],seperated_colors[2]);
			//pixels[i] = 0xFFFFFFFF;
		}
		updatePixels();
	}
}
