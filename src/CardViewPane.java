import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CardViewPane extends JPanel implements KeyListener, MouseListener {

	/**
	 * カード閲覧パネル。
	 */
	private JLabel cardLabel;
	private JLabel infoLabel;
	private JPanel textPane;
	private JButton nextButton;
	private JButton backButton;
	private JButton turnButton;
	private JButton shuffleButton;
	private JButton setDefaultFaceButton;
	private ActionListener nextAction;
	private ActionListener backAction;
	private ActionListener turnAction;
	private CardFolder folder;
	private Card card;

	/**
	 * コンストラクタ
	 * @param folder
	 */
	public CardViewPane(CardFolder folder) {
		super(new BorderLayout());
		this.folder = folder;
		card = folder.getCard(0);

		//ラベルの設定
		cardLabel = new JLabel();
		cardLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
		cardLabel.setHorizontalAlignment(JLabel.CENTER);

		//infoラベル
		infoLabel = new JLabel();
		infoLabel.setHorizontalAlignment(JLabel.RIGHT);

		draw();

		//ラベルをまとめる
		textPane = new JPanel(new BorderLayout());
		textPane.add(cardLabel, BorderLayout.CENTER);
		textPane.add(infoLabel, BorderLayout.SOUTH);

		//ボタンの生成
		nextButton = new JButton("次へ");
		backButton = new JButton("戻る");
		turnButton = new JButton("答え");
		shuffleButton = new JButton("シャッフル");
		setDefaultFaceButton = new JButton("カードの向きを変える");

		//ボタンにリスナを設定
		nextButton.addActionListener(nextAction = new NextActionHandler());
		backButton.addActionListener(backAction = new BackActionHandler());
		turnButton.addActionListener(turnAction = new TurnActionHandler());
		shuffleButton.addActionListener(new ShuffleActionHandler());
		setDefaultFaceButton.addActionListener(new SetDefaultFaceActionHandler());

		//ボタンをまとめる
		JPanel buttonPane1 = new JPanel();
		buttonPane1.setLayout(new GridLayout());
		buttonPane1.add(backButton);
		buttonPane1.add(turnButton);
		buttonPane1.add(nextButton);

		JPanel buttonPane2 = new JPanel(new GridLayout());
		buttonPane2.add(shuffleButton);
		buttonPane2.add(setDefaultFaceButton);

		JPanel buttonPane = new JPanel(new GridLayout(2, 1));
		buttonPane.add(buttonPane1);
		buttonPane.add(buttonPane2);

		//パネルを追加
		add(textPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.SOUTH);
		cardLabel.requestFocus();

		//パネルにリスナを設定
		addKeyListener(this);
		addMouseListener(this);
		setFocusable(true);
	}

	/**
	 * 更新処理
	 */
	public void draw() {

		card = folder.getCurrentCard();
		if (card != null) {
			cardLabel.setText("<html>" + card.getCurrentFace() + "</html>");
			String infoString = folder.accesser.getAccessFile().getName() + ":" + (folder.getCurrentCardNumber() + 1)
					+ "/" + folder.getSize();
			infoLabel.setText(infoString);
		}

	}

	/**
	 * 次アクションのハンドラ。内部クラス
	 *
	 */
	private class NextActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			Card tmp;
			if ((tmp = folder.getNextCard()) != null) {
				card.setFace(folder.getDefaultFace());
				card = tmp;
			}
			draw();
		}

	}

	/**
	 * 戻るアクションのハンドラ。内部クラス
	 *
	 */
	private class BackActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Card tmp;
			if ((tmp = folder.getBeforeCard()) != null) {
				card.setFace(folder.getDefaultFace());
				card = tmp;
			}
			draw();
		}
	}

	/**
	 * 切り替えアクションのハンドラ。内部クラス
	 *
	 */
	private class TurnActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			card = folder.getCurrentCard();
			card.changeFace();
			draw();
		}
	}

	/**
	 * シャッフルアクションのハンドラ。
	 *
	 */
	private class ShuffleActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ

			card.setFace(folder.getDefaultFace());

			Random rnd = new Random();
			int index = rnd.nextInt(folder.getSize());

			card = folder.getCard(index);
			if (card != null) {
				cardLabel.setText("<html>" + card.getCurrentFace() + "</html>");
				String infoString = folder.accesser.getAccessFile().getName() + ":ランダムアクセス中";
				infoLabel.setText(infoString);

			}
		}
	}

	/**
	 * setDefaltFaceアクションのハンドラ。内部クラス。
	 */
	private class SetDefaultFaceActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int face = folder.getDefaultFace();
			if (face == Card.FRONT) {
				folder.setDefaultFace(Card.BACK);
			} else {
				folder.setDefaultFace(Card.FRONT);
			}

			card = folder.getCard(0);
			draw();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			backAction.actionPerformed(null);
		}

		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			nextAction.actionPerformed(null);
		}

		if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN || key == KeyEvent.VK_W || key == KeyEvent.VK_S) {
			turnAction.actionPerformed(null);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ
		requestFocus();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO 自動生成されたメソッド・スタブ

	}
}
