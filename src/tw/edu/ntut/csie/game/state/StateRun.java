package tw.edu.ntut.csie.game.state;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.util.Log;


import tw.edu.ntut.csie.game.Coin;
import tw.edu.ntut.csie.game.Game;
import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.MoveCloud;
import tw.edu.ntut.csie.game.Pointer;
import tw.edu.ntut.csie.game.R;
import tw.edu.ntut.csie.game.Stair;
import tw.edu.ntut.csie.game.leadingRole;
import tw.edu.ntut.csie.game.core.Audio;
import tw.edu.ntut.csie.game.core.MovingBitmap;
import tw.edu.ntut.csie.game.extend.Animation;
import tw.edu.ntut.csie.game.extend.Integer;
import tw.edu.ntut.csie.game.engine.GameEngine;



public class StateRun extends GameState {
	public static final int DEFAULT_SCORE_DIGITS = 4;
	private MovingBitmap _background;
	private MovingBitmap _android;
	private MovingBitmap _door;
	
	//Big Stair(under)
	private MovingBitmap _under;
	private boolean _underIsNotused;
	
	//Hero
	private leadingRole role;
	private int _rx,_ry;
	private int originalUnderLocation;
	
	//Stair
	private Stair[] _stair;
	private int _lastStairIndex;
	private int _buttomStairY;
	private int _topStairIndex;
	private int _topStairY;
	private int stairSpace;
	
	//Cloud
	private MoveCloud[] _cloud;
	
	//Score
	private Integer _scores;
	private static int score;
	private int scoreFlag;
	private int _bestScore;
	
	//Coin
	private Coin _coin;
	private boolean _isHaveCoin;
	
	private boolean _grab;
	
	private Audio _music;
	private Audio _basicmatch;
	private Audio _eatCoin;

	public StateRun(GameEngine engine) {
		super(engine);
	}

	@Override
	public void initialize(Map<String, Object> data) {		
		
		int jellyLocation = (java.lang.Integer)data.get("jellyLocation");
		//Log.d("initialize", "jellyLocation "+jellyLocation);
		
		//Under
		_under = new MovingBitmap(R.drawable.under);
		_under.resize((int)(_under.getWidth()*0.64), (int)(_under.getHeight()*0.64));
		_under.setLocation((376/2)-(_under.getWidth()/2), 640-_under.getHeight()+27);
		_underIsNotused=false;
		originalUnderLocation=640-_under.getHeight()+27;
		
		//Role
		role = new leadingRole();
		role.initialize();
		role.SetX(jellyLocation);
		_rx=role.getX();
		_ry=role.getY();
		
		//Stair
		_stair = new Stair[15];
		for(int i=0;i<15;i++){
			_stair[i] = new Stair();
		}
		for(int i=0;i<15;i++){
			_stair[i].setLocation((int)(Math.random()*(376-_stair[i].getWidth()))
								  ,(640-_under.getHeight()-role.getHeight()-5)-(60*(i+1))
								  +(int)(Math.random()*(50-_stair[i].getHeight())));
		}
		_lastStairIndex = 0;
		_buttomStairY = _stair[_lastStairIndex].getY();
		_topStairIndex = 14;
		_topStairY = _stair[_topStairIndex].getY();
		stairSpace = _stair[0].getY() - _stair[14].getY();
		
		
		//Background
		_background = new MovingBitmap(R.drawable.background);
		_background.resize(410, 681);
		_background.setLocation(-4, -15);
		
		//Cloud
		_cloud = new MoveCloud[4];
		for(int i=0;i<4;i++){
			_cloud[i] = new MoveCloud();
		}
		for(int i=0;i<4;i++){
			double randomsize;
			randomsize = Math.random();
			if(randomsize>=0.5){
				randomsize -= 0.5;
			}
			int size = (int)(randomsize*150);
			_cloud[i].Newpic();
			_cloud[i].resize(_cloud[i].getWidth()-size,(int)(_cloud[i].getHeight()*_cloud[i].sizechange(_cloud[i].getWidth()-size,_cloud[i].getWidth())));				    
		    _cloud[i].setLocation((int)(Math.random()*(-300-_cloud[i].getWidth())), 
				                  (640-_under.getHeight()-30)-((640-_under.getHeight())/3
				            	  *(i+1))+(int)(Math.random()*((640-_under.getHeight()-10)/3
				            	  -_cloud[i].getHeight())));
		}
		
		//score
		score = 0;
		_scores = new Integer(DEFAULT_SCORE_DIGITS, 0, 50, 10);
	    scoreFlag = _stair[0].getY();
	    _bestScore = (java.lang.Integer)data.get("bestScore");
		
	    //Coin
	    _coin = new Coin();
	    _isHaveCoin = false;
	    
		//Music
		_music = new Audio(R.raw.sunahuru);
		_music.setRepeating(true);
		_music.play();
		_basicmatch = new Audio(R.raw.basicmatch);
		_basicmatch.setRepeating(false);
		_eatCoin = new Audio(R.raw.squeak);
		_eatCoin.setRepeating(false);
		
		//debug用(直接進入stateOver)
		_android = new MovingBitmap(R.drawable.android_green);
		_android.setLocation(100, 200);
		_door = new MovingBitmap(R.drawable.door);
		_door.setLocation(300, 200);
		
		
		_grab = false;
	}
	
	@Override
	public void move() {
		for(int i=0;i<4;i++){
		    _cloud[i].move();
			if (_cloud[i].getX()>_background.getWidth()+50){
				double randomsize;
				randomsize = Math.random();
				if(randomsize>=0.5){
					randomsize -= 0.5;
				}
				int size = (int)(randomsize*120)+200;
				_cloud[i].Newpic();
				_cloud[i].resize(_cloud[i].getWidth()-size,(int)(_cloud[i].getHeight()*_cloud[i].sizechange(_cloud[i].getWidth()-size,_cloud[i].getWidth())));				    
				_cloud[i].setLocation((int)(Math.random()*(-340-_cloud[i].getWidth())), 
					                  (640-_under.getHeight()-50)-((640-_under.getHeight())/3
					            	  *(i+1))+(int)(Math.random()*((640-_under.getHeight()-10)/3
					            	  -_cloud[i].getHeight())));
			}			
		}
		
		
		if (role.getRising()) {			// 上升狀態
			role.resizeNormal(role.getY());
			if (role.getVelocity() > 0) {
				if(role.getY()<=320){//超過銀幕中間 jelly不移動，其他物件下降
					for(int i=0;i<15;i++){//移動stair
						_stair[i].setLocation(_stair[i].getX(), _stair[i].getY()+role.getVelocity());
						if(_stair[i].getY()>=750){
							_stair[i].setLocation((int)(Math.random()*(376-_stair[i].getWidth())), _stair[i].getY()-stairSpace-50);
							if(_isHaveCoin && ( _stair[i].getY() < (_coin.getButtomCoin().getY()+_coin.getButtomCoin().getHeight())
										   &&	_stair[i].getY() > _coin.getTopCoin().getY() ) ){//如果有coin而且stair比coin高的情況下則將stair消失在銀幕中(x位置+1000)
								_stair[i].setLocation(_stair[i].getX()+1000, _stair[i].getY());
							}
						}
					}
					if(_isHaveCoin){
						for(int i=0; i<_coin.size(); i++){//如果有coin則移動coin
							_coin.setLocation(i, _coin.getX(i), _coin.getY(i) + role.getVelocity());
							if(_coin.getY(i) > 750){
								_coin.setLocation(i, 999, _coin.getY(i));
							}
						}
					}
					if(_isHaveCoin){
						if( _coin.getTopCoin().getY() > 750 ){
							_isHaveCoin = false;
						}
					}
					for(int i=0;i<15;i++){//尋找最底下以及最上面的stair位置
						if(_stair[i].getY() > _stair[_lastStairIndex].getY()){
							_lastStairIndex = i;
						}
						if(_stair[i].getY() < _stair[_topStairIndex].getY()){
							_topStairIndex = i;
						}
					}
					if(!_underIsNotused){ _under.setLocation(_under.getX(), _under.getY()+role.getVelocity()); }
					if((!_underIsNotused) && (_under.getY()>=800)){
						_underIsNotused=true;
						_under.setLocation(999, 0);
					}
					if(_stair[_lastStairIndex].getY() > scoreFlag){
						score+=role.getVelocity()*0.05;
						_scores.add(score);
					}
					role.subVelocity(2);
				}else if(role.getY()>320){//jelly還沒到銀幕中間，jelly正常上升
					_ry -= role.getVelocity();	// 當速度 > 0時，y軸上升(移動velocity個點，velocity的單位為 點/次)
					//velocity--;		
					role.subVelocity(2);// 受重力影響，下次的上升速度降低
				}
			} else {//上升到最高點
				role.setRising(false); // 當速度 <= 0，上升終止，下次改為下降
				role.setVelocity(1);	// 下降的初速(velocity)為1
				scoreFlag = _stair[_lastStairIndex].getY();
				score = 0;
				
				if(!_isHaveCoin){//隨機產生是否有coin的判斷
					int randCoin = (int)(Math.random()*2);
					if(randCoin == 0){
						_isHaveCoin = true;
						int coinType = (int)(Math.random()*2)+1;
						_coin.setCoinType(coinType, _stair[_topStairIndex].getY());
					}
				}
			}
		} else {				// 下降狀態
			for(int i=0;i<15;i++){//是否採到小踏板
				if(role.isStepStair(_stair[i])){
					_ry=_stair[i].getY();
					role.setLocation(role.getX(), _ry-role.getHeight());
					_basicmatch.play();
					role.setRising(true);
					role.setVelocity(role.getInitialVelocity());
					role.resizeFlat(role.getY());
					//break;
				}
			}
			if((!_underIsNotused) && (role.isStepUnder(_under))){//是否採到初始大踏板
				_ry=_under.getY();
				role.setLocation(role.getX(), _ry-role.getHeight());
				_basicmatch.play();
				role.setRising(true);
				role.setVelocity(role.getInitialVelocity());
				role.resizeFlat(role.getY());
			}
			if(!role.getRising()){//下降狀態
				//Log.d("test","originalYLocation"+ originalUnderLocation);
				role.resizeNormal(role.getY());
				if((role.getY()+role.getHeight())<( originalUnderLocation )){//jelly正常下降
					_ry += role.getVelocity();	// y軸下降(移動velocity個點，velocity的單位為 點/次)
					if(_ry>originalUnderLocation){ _ry=originalUnderLocation; }
					//velocity++;		// 受重力影響，下次的下降速度增加
					if(role.getVelocity()<15){
						role.addVelocity(2);
					}
				}else{ //jelly下降到原始高度，由其他物件上升至原始高度
					if(!_underIsNotused){ _under.setLocation(_under.getX(), _under.getY()-role.getVelocity()); }
					for(int i=0;i<15;i++){//stair上升
						_stair[i].setLocation(_stair[i].getX(), _stair[i].getY()-role.getVelocity());
					}
					if(_isHaveCoin){
						for(int i=0; i<_coin.size(); i++){
							_coin.setLocation(i, _coin.getX(i), _coin.getY(i) - role.getVelocity());
						}
					}
					_buttomStairY = _stair[_lastStairIndex].getY();//紀錄最底下的stair，當jelly超過這個stair就死掉
					if(role.getVelocity()<15){//正常加速
						role.addVelocity(2);
					}
					if(role.getY()>= _stair[_lastStairIndex].getY()){//死掉加速
						role.addVelocity(5);
					}
				}
			}
		}
		role.setLocation(_rx, _ry-role.getHeight());
		if(_isHaveCoin){
			for(int coinIndex=0;coinIndex<_coin.size();coinIndex++){//是否吃到金幣
				if(role.isEatCoin(_coin.getCoin(coinIndex))){
					_eatCoin.play();
					_coin.setLocation(coinIndex, 999, _coin.getY(coinIndex));
					role.setRising(true);
					role.setVelocity(role.getInitialVelocity());				
				}
			}
		}
		if(_scores.getValue() > 10000){
			_scores.setDigits(5);
		}
		else if(_scores.getValue() > 100000){
			_scores.setDigits(6);
		}
		
		if(_buttomStairY<=(-150)){
			Map<String, Object> paras = new HashMap<String, Object>();
			paras.put("score", _scores.getValue());
			paras.put("bestScore", _bestScore);
			//Log.d("score", "_scores.getValue "+_scores.getValue());
			changeState(Game.OVER_STATE, paras);
		}
	}

	@Override
	public void show() {
		// 呼叫順序為貼圖順序		
		_background.show();
		for(int i=0;i<4;i++){		
			_cloud[i].show();
		}
		//_door.show();
		//_android.show();
		_under.show();
		for(int i = 0;i<15;i++){
			int hide = (int)(Math.random()*10);
			if(hide==10){
				continue;
			}else{
			_stair[i].show();
			}
		}
		_coin.show();
		_scores.show();
		role.show();
	}

	@Override
	public void release() {
		_background.release();
		_scores.release();
		_android.release();
		for(int i=0;i<4;i++){
			_cloud[i].release();
		}
		_music.release();
		_door.release();
		_under.release();
		role.release();
		_basicmatch.release();
		for(int i=0;i<15;i++){
			_stair[i].release();
		}
		_coin.release();
		_eatCoin.release();
		
		_background = null;
		_scores = null;
		_android = null;
		for(int i=0;i<4;i++){
			_cloud[i] = null;
		}
		_music = null;
		_door = null;	
		_under = null;
		role = null;
		_basicmatch = null;
		for(int i=0;i<15;i++){
			_stair[i]=null;
		}
		_coin=null;
		_eatCoin=null;
	}

	@Override
	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(int keyCode) {
		// TODO Auto-generated method stub
	}


	public void orientationChanged(float pitch, float azimuth, float roll) {
		//腳色左右移動--------------------------------
		if(roll > 1 && roll < 90 ){
			if(_rx > 0-role.getWidth()){
				_rx -= (int)((Math.sqrt(roll))*2);
			}else{
				_rx = 376;
			}
		}
		if(roll < -1 && roll > -90 ){
			if(_rx < 376){
				_rx += (int)((Math.sqrt(-(roll)))*2);
			}else{
				_rx = 0-role.getWidth();
			}
		}
		role.setLocation(_rx, role.getY());
		//--------------------------------------
	}

	@Override
	public void accelerationChanged(float dX, float dY, float dZ) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean pointerPressed(List<Pointer> pointers) {
		if(pointers.size() == 1) {
			int touchX = pointers.get(0).getX();
			int touchY = pointers.get(0).getY();
			if(touchX > _android.getX() && touchX < _android.getX() + _android.getWidth() &&
					touchY > _android.getY() && touchY < _android.getY() + _android.getHeight()) {
				_grab = true;
			} else {
				_grab = false;
			}
		}
		return true;
	}

	@Override
	public boolean pointerMoved(List<Pointer> pointers) {
		/**if(_grab)
			_android.setLocation(pointers.get(0).getX() - _android.getWidth() / 2, pointers.get(0).getY() - _android.getHeight() / 2);
		int moveX = _android.getX();
		int moveY = _android.getY();
		if(moveX + _android.getWidth() / 2 > _door.getX() && moveX < _door.getX() + _door.getWidth() / 2 &&
				moveY + _android.getHeight() / 2 > _door.getY() && moveY < _door.getY() + _door.getHeight() / 2){
			
			Map<String, Object> paras = new HashMap<String, Object>();
			paras.put("score", _scores.getValue());
			paras.put("bestScore", _bestScore);
			//Log.d("score", "_scores.getValue "+_scores.getValue());
			changeState(Game.OVER_STATE, paras);
		}*/
		return false;
	}

	@Override
	public boolean pointerReleased(List<Pointer> pointers) {
		_grab = false;
		return false;
	}

	@Override
	public void pause() {
		_music.pause();
		_basicmatch.pause();
	}

	@Override
	public void resume() {
		_music.resume();
		_basicmatch.resume();
	}
	
	public static int getScore(){
		return score;
	}
}
