package tw.edu.ntut.csie.game;

import tw.edu.ntut.csie.game.core.MovingBitmap;

public class MoveCloud implements GameObject {
	private MovingBitmap MoveCloud;
	private double newsize;
	
	public MoveCloud(){
		MoveCloud = new MovingBitmap(R.drawable.cloud);
	}
	
	public int getX() {
		return MoveCloud.getX();
	}
	
	public int getY() {
		return MoveCloud.getY();
	}
	
	public int getWidth() {
		return MoveCloud.getWidth();
	}
	
	public int getHeight() {
		return MoveCloud.getHeight();
	}
	
	public void release() {
		MoveCloud.release();
		MoveCloud = null;
	}
	
	public void move() {
		MoveCloud.setLocation(MoveCloud.getX()+1, MoveCloud.getY());
		
	}
	public void show() {
		
		MoveCloud.show();
	}
	public void setLocation(int x, int y){
		MoveCloud.setLocation(x, y);
	}
	
	public void resize(int x, int y){
		MoveCloud.resize(x, y);
	}
	
	public double sizechange(double change1, double change2){
		newsize = change1/change2;
		return newsize;
	}
	public void Newpic(){
		MoveCloud.loadBitmap(R.drawable.cloud);
	}
}
