package tw.edu.ntut.csie.game;

import tw.edu.ntut.csie.game.core.MovingBitmap;

import tw.edu.ntut.csie.game.GameObject;
import tw.edu.ntut.csie.game.R;

public class BestInteger implements GameObject {

	/** �w�]�������ܦ�ơC */
	public static final int DEFAULT_DIGITS = 4;

	/** ��ܾ�ƪ����W���y�СC */
	private int _x;
	private int _y;

	/** ��ܪ��ȩM��ơC */
	private int _value;
	private int _digits;

	/** �Ψ����0~9���έt�����Ϥ��C */
	private MovingBitmap[] _digitImages;

	/** �ϥιw�]�ȫإߤ@��<code>Integer</code>����C */
	public BestInteger() {
		this(DEFAULT_DIGITS);
	}

	/** �ϥΫ��w����ܦ�ƫإߤ@��<code>Integer</code>����C
	 * 
	 * @param digits ����ܪ����
	 */
	public BestInteger(int digits) {
		this(digits, 0, 0, 0);
	}

	/** �ϥΫ��w����ܦ�ơB��l�ȩM��l��m�A�إߤ@��<code>Integer</code>����C
	 * 
	 * @param digits ����ܪ����
	 * @param initValue ��l��
	 * @param x ��l��m��X�y��
	 * @param y ��l��m��X�y��
	 */
	public BestInteger(int digits, int initValue, int x, int y) {
		initialize();
		setDigits(digits);
		setLocation(x, y);
		_value = initValue;
	}

	/** ��ثe��ܪ��ƭȥ[�W���w���ȡC
	 * 
	 * @param addend �[��(�ثe���Ȭ��Q�[��)
	 */
	public void add(int addend) {
		_value += addend;
	}

	@Override
	public void move() {}

	@Override
	public void release() {
		for (int i = 0; i < 11; i++) {
			_digitImages[i].release();
		}
	}

	@Override
	public void show() {
		int nx;
		int MSB;
		if (_value >= 0) {
			MSB = _value;
			nx = _x + _digitImages[0].getHeight() * (_digits - 1);
		} else {
			MSB = -_value;
			nx = _x + _digitImages[0].getWidth() * _digits;
		}
		for (int i = 0; i < _digits; i++) {
			int d = MSB % 10;
			MSB /= 10;
			_digitImages[d].setLocation(nx, _y);
			_digitImages[d].show();
			nx -= _digitImages[d].getWidth();
		}
		if (_value < 0) {
			_digitImages[10].setLocation(nx, _y);
			_digitImages[10].show();
		}
	}

	/** ��ثe��ܪ��ƭȴ�h���w���ȡC
	 * 
	 * @param addend ���(�ثe���Ȭ��Q���)
	 */	
	public void subtract(int subtrahend) {
		_value -= subtrahend;
	}

	/** �ܧ���ܪ���ơC
	 * 
	 * @param digits �s����ܦ��
	 */
	public void setDigits(int digits) {
		_digits = digits;
	}

	/** �]�w�̰���Ʀr����ܦ�m�A��L��Ʒ|�̾ڨC�ӼƦr���Ϥ��j�p�̧ǱƦC��ܡC
	 * 
	 * @param x ��ܦ�m��x�y��
	 * @param y ��ܦ�m��x�y��
	 */
	public void setLocation(int x, int y) {
		_x = x;
		_y = y;
	}

	/** �]�w����ܪ���ƼƭȡC
	 * 
	 * @param value �s����ƭ�
	 */
	public void setValue(int value) {
		_value = value;
	}

	/** ���o�ثe��ܪ���ƼƭȡC
	 * 
	 * @return ��ƭ�
	 */
	public int getValue() {
		return _value;
	}

	/** �i���l�ơC */
	private void initialize() {
		_digitImages = new MovingBitmap[11];
		_digitImages[0] = new MovingBitmap(R.drawable.digit_0);
		_digitImages[1] = new MovingBitmap(R.drawable.digit_1);
		_digitImages[2] = new MovingBitmap(R.drawable.digit_2);
		_digitImages[3] = new MovingBitmap(R.drawable.digit_3);
		_digitImages[4] = new MovingBitmap(R.drawable.digit_4);
		_digitImages[5] = new MovingBitmap(R.drawable.digit_5);
		_digitImages[6] = new MovingBitmap(R.drawable.digit_6);
		_digitImages[7] = new MovingBitmap(R.drawable.digit_7);
		_digitImages[8] = new MovingBitmap(R.drawable.digit_8);
		_digitImages[9] = new MovingBitmap(R.drawable.digit_9);
		_digitImages[10] = new MovingBitmap(R.drawable.digit_10);
		for(int i=0; i<11; i++){
			_digitImages[i].resize((int)(_digitImages[i].getWidth()*0.5), (int)(_digitImages[i].getHeight()*0.5));
		}
	}
}