package tw.edu.ntut.csie.game;

import tw.edu.ntut.csie.game.core.MovingBitmap;
import android.R.bool;
import tw.edu.ntut.csie.game.SensorEventHandler;
import tw.edu.ntut.csie.game.engine.GameEngine;
import tw.edu.ntut.csie.game.extend.Animation;


public class leadingRole implements GameObject{
	private	int x, y;				// 圖形座標
	private int floor;				// 地板的Y座標
	private boolean rising;			// true表上升、false表下降
	private int initial_velocity;	// 初始速度
	private int velocity;			// 目前的速度(點/次)
	private MovingBitmap Role;
	
	public leadingRole(){
		int INITIAL_VELOCITY = 30;
		int FLOOR = (int)(640-196*0.64+27);
		Role = new MovingBitmap(R.drawable.jelly);
		Role.resize((int)(Role.getWidth()*0.6), (int)(Role.getHeight()*0.6));
		floor = FLOOR;
		x=188-(Role.getWidth()/2);
		y=floor;
		rising = true;
		initial_velocity = INITIAL_VELOCITY;
		velocity = initial_velocity;
	}
	
	public void initialize(){
		Role.setLocation(x, y-Role.getHeight()+6);
	}
	
	public	void SetFloor(int floor){
		this.floor = floor;
	}
	
	public int getX() {
		return Role.getX();
	}
	
	public int getY() {
		return Role.getY();
	}
	
	public int getWidth() {
		return Role.getWidth();
	}
	
	public int getHeight() {
		return Role.getHeight();
	}
	
	public void SetX(int x){
		Role.setLocation(x, Role.getY());
	}
	
	@Override
	public void release() {
		// TODO Auto-generated method stub
		Role.release();
		Role = null;
	}
	
	@Override
	public void move() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		Role.show();
	}
	
	public void reJump(){
		this.velocity = initial_velocity;
		rising = true;
	}
	
	public boolean getRising(){
		return rising;
	}
	
	public void setRising(boolean r){
		this.rising = r;
	}
	
	public int getVelocity(){
		return velocity;
	}
	
	public void setVelocity(int v){
		this.velocity = v;
	}
	
	public void subVelocity(int v){
		this.velocity -= v;
	}
	
	public void addVelocity(int v){
		this.velocity += v;
	}
	
	public int getFloor(){
		return floor;
	}
	
	public int getInitialVelocity(){
		return initial_velocity;
	}
	
	public void setLocation(int x, int y){
		Role.setLocation(x, y);
	}
	
	public boolean isStepStair(Stair stair){
		boolean xRange,yRange;
		xRange = (((Role.getX() + (Role.getWidth()*0.25)) < (stair.getX()+stair.getWidth())) && 
				 (Role.getX() + (Role.getWidth()*0.75) > stair.getX()));
		yRange = ( ((Role.getY() + Role.getHeight()) >= stair.getY())   && 
				   ((Role.getY() + Role.getHeight()) <= (stair.getY()+stair.getHeight())) );
		return ( xRange && yRange );
	}
	
	public boolean isStepUnder(MovingBitmap _under){
		boolean xRange,yRange;
		xRange = (((Role.getX() + (Role.getWidth()*0.25)) < (_under.getX()+_under.getWidth())) && 
				 (Role.getX() + (Role.getWidth()*0.75) > _under.getX()));
		yRange = ( ((Role.getY() + Role.getHeight()) >= _under.getY())   && 
				   ((Role.getY() + Role.getHeight()) <= (_under.getY()+_under.getHeight())) );
		return ( xRange && yRange );
	}
	
	public boolean isEatCoin(MovingBitmap coin){
		boolean xRange,yRange;
		xRange = (((Role.getX() + (Role.getWidth()*0.15)) < (coin.getX()+coin.getWidth())) && 
				 (Role.getX() + (Role.getWidth()*0.85) > (coin.getX()/*+coin.getWidth()*/)));
		yRange = ( ( Role.getY() <= (coin.getY()+coin.getHeight()) )   && 
				   ( (Role.getY() + Role.getHeight()) >= coin.getY() ) );
		return ( xRange && yRange );
	}
	
	public void resizeFlat(int jelly_y){
		int midLocation = x+(int)(Role.getWidth()*0.5);
		Role.loadBitmap(R.drawable.jelly);
		Role.resize((int)(Role.getWidth()*0.6), (int)(Role.getHeight()*0.4));
		Role.setLocation(midLocation - (int)(Role.getWidth()*0.5), jelly_y);
	}
	
	public void resizeNormal(int jelly_y){
		int midLocation = x+(int)(this.getWidth()*0.5);
		Role.loadBitmap(R.drawable.jelly);
		Role.resize((int)(Role.getWidth()*0.6), (int)(Role.getHeight()*0.6));
		Role.setLocation(midLocation - (int)(Role.getWidth()*0.5), jelly_y);
	}
};