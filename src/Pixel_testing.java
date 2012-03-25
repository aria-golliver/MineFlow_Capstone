


public class Pixel_testing extends Thread{
	int[] pixels;
	public Pixel_testing(int[] pixels){
		this.pixels = pixels;
	}
	
	public void run(){
		while(true){
			for(int i = 0; i < pixels.length; i++){
				pixels[i] = (int)(Math.random()*0xFFFFFF);
			}
		}
	}
	
}
