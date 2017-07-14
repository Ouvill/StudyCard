public class Card {

	/**
	 * カードクラス
	 */
	public static final int FRONT = 0;
	public static final int BACK = 1;

	private static int no = 0;

	private int face = 0;
	private int serialNumber;
	private String front;
	private String back;

	/**
	 * コンストラクタ
	 * @param front
	 * @param back
	 */
	public Card(String front, String back, int face) {
		this.serialNumber = no;
		Initialize(front, back, face);
		no++;
	}

	/**
	 * 初期化
	 * @param front
	 * @param back
	 */
	public void Initialize(String front, String back, int face) {
		this.front = front;
		this.back = back;
		this.face = face;
	}

	//getter
	public int getFace() {
		return face;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public String getFront() {
		return front;
	}

	public String getBack() {
		return back;
	}

	public String getCurrentFace() {
		if (face == FRONT) {
			return getFront();
		} else {
			return getBack();
		}
	}

	public void setFace(int face) {
		this.face = face;
	}

	public void changeFace() {
		if (face == FRONT) {
			setFace(BACK);
		} else {
			setFace(FRONT);
		}
	}

	@Override
	public String toString() {
		return front + "," + back;
	}
}
