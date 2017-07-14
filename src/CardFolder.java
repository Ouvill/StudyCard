import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * カードを管理するクラス。データの格納はこのクラスを通して行う。
 *
 */
public class CardFolder {

	public static final int FRONT = 0;
	public static final int BACK = 1;

	private int size;
	private int currentCardNumber = 0;
	private ArrayList<Card> cardList = new ArrayList<Card>();
	private int defaultFace = 0;
	private File setting = new File("setting");
	public AccessCSV accesser;

	/**
	 * コンストラクタ
	 */
	public CardFolder() {

		//以前アクセスしたファイルを調べる。
		String recentFileName = "default.csv";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(setting));
			String tmp;
			if ((tmp = br.readLine()) != null) {
				recentFileName = tmp;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			try {
				setting.createNewFile();
			} catch (IOException e1) {
				// TODO 自動生成された catch ブロック
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println(recentFileName);
		File csv = new File(recentFileName);

		accesser = new AccessCSV(csv);
		accesser.loadCardFromCSV();
	}

	/**
	 * 初期化
	 */
	public void Initialize() {
		size = 0;
		currentCardNumber = 0;
		cardList.clear();

	}

	/**
	 * 格納しているカードの数を得る
	 * @return カードの数
	 */
	public int getSize() {
		return size;
	}

	/**
	 * デフォルトで表示するカードの向きを得る。
	 * @return
	 */
	public int getDefaultFace() {
		return defaultFace;
	}

	/**
	 * デフォルトのカードの向きを変更
	 */
	public void setDefaultFace(int face) {
		defaultFace = face;

		for(int i = 0; i < size; i++) {
			Card card = getCard(i);
			card.setFace(face);
		}
	}

	/**
	 * 現在参照しているカードの番号を得る。
	 * @return カードの番号
	 */
	public int getCurrentCardNumber() {
		return currentCardNumber;
	}

	/**
	 * 指定したカードのインスタンスを得る
	 * @param index
	 * @return
	 */
	public Card getCard(int index) {
		if (index >= size) {
			return null;
		} else {
			currentCardNumber = index;
			return cardList.get(index);

		}
	}

	/**
	 * 現在参照しているカードのインスタンスを得る
	 * @return
	 */
	public Card getCurrentCard() {
		return getCard(currentCardNumber);
	}

	/**
	 * 現在参照しているカードの次のカードのインスタンスを得る。
	 * @return
	 */
	public Card getNextCard() {
		if (currentCardNumber >= size - 1) {
			return null;
		}
		return getCard(++currentCardNumber);
	}

	/**
	 * 現在参照しているカードの後ろのカードのインスタンスを得る。
	 * @return
	 */
	public Card getBeforeCard() {
		if (currentCardNumber == 0) {
			return null;
		}
		return getCard(--currentCardNumber);
	}

	/**
	 * ホルダーにカードを加える
	 * @param card
	 */
	public void addCard(Card card) {
		size++;
		cardList.add(card);
	}

	/**
	 * カードをホルダーに加える。更にcsvファイルにも書き込む
	 * @param card
	 * @param append
	 */
	public void addCard(Card card, boolean append) {

		if (append == true) {
			if (accesser.addCardToCSV(card)) {
				addCard(card);
			}
		}
	}

	/**
	 * カードをホルダーから削除する
	 * @param index
	 */
	public void remeveCard(int index) {
		cardList.remove(index);
	}

	/**
	 * 現在参照指定カードをホルダーから削除する。
	 */
	public void removeCurrentCard() {
		cardList.remove(currentCardNumber);
	}

	/**
	 * CSVファイルにアクセスする内部クラス
	 *
	 */
	public class AccessCSV {
		private File csv;

		public AccessCSV(File csv) {
			setCsvFile(csv);
		}

		/**
		 * アクセスするファイルを指定する。
		 */
		public void setCsvFile(File csv) {
			this.csv = csv;

			//アクセスしたファイル名をsettingに記入
			try {
				if (checkBeforeWritefile(setting)) {
					FileWriter filewriter = new FileWriter(setting);
					BufferedWriter bw = new BufferedWriter(filewriter);
					PrintWriter pw = new PrintWriter(bw);

					pw.println(csv);
					pw.close();
				}
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		public File getAccessFile() {
			return csv;
		}

		/**
		 * CSVファイルからカードの読み込みを行う。
		 */
		public void loadCardFromCSV() {
			BufferedReader br = null;

			try {
				br = new BufferedReader(new FileReader(csv));
				String line = "";
				while ((line = br.readLine()) != null) {
					String[] lineAry = line.split(",");
					int size = lineAry.length;
					if ( lineAry == null || size == 0){

					} else if(size == 1) {
						addCard(new Card(lineAry[FRONT], "" , defaultFace));

					} else {
							addCard(new Card(lineAry[FRONT], lineAry[BACK], defaultFace));
					}
				}
				br.close();
			} catch (FileNotFoundException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		/**
		 * CSVファイルを再読み込み
		 */
		public void reloadCardFromCSV() {
			Initialize();
			loadCardFromCSV();
		}

		/**
		 * CSVファイルにカードを追記する
		 * @param card
		 */
		public boolean addCardToCSV(Card card) {
			try {
				if (!(card.getFront().equals("") || card.getBack().equals(""))) {
					if (checkBeforeWritefile(csv)) {
						FileWriter filewriter = new FileWriter(csv, true);
						BufferedWriter bw = new BufferedWriter(filewriter);
						PrintWriter pw = new PrintWriter(bw);
						pw.println(card);
						pw.close();

						return true;
					} else {
						System.out.println("ファイルに書き込めません");
						return false;
					}
				}
				return false;
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return false;
			}

		}

		/**
		 * フォルダーの全てのカードをCSVに上書きする
		 */
		public boolean writeAllCardToCSV() {
			try {
				if (checkBeforeWritefile(csv)) {
					FileWriter filewriter = new FileWriter(csv);
					BufferedWriter bw = new BufferedWriter(filewriter);
					PrintWriter pw = new PrintWriter(bw);
					Card card = getCard(0);
					pw.println(card);
					while ((card = getNextCard()) != null) {
						pw.println(card);
					}
					pw.close();
					return true;
				} else {
					System.out.println("ファイルに書き込めません");
					return false;
				}

			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return false;
			}

		}

	}

	/**
	 * CSVファイルが書き込み可能であるか確認する。
	 * @param file
	 * @return
	 */
	private boolean checkBeforeWritefile(File file) {
		if (file.exists()) {
			if (file.isFile() && file.canWrite()) {
				return true;
			}
		}

		return false;
	}
}
