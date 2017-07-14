import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * ランダムアクセス用に作ったパネル
 *
 */
/*
 * CardViewPaneにランダムボタンを追加すればいいんじゃないか？
 * と気がついたのでお蔵入り。でも、このパネルを使えば重複なしでカードにランダムアクセスできる。
 * 何か使い道があるかもなので、置いておく。
 */
public class ShuffleCardViewPane extends JPanel {

	int index = 0;
	int[] shuffle;
	private JLabel cardLabel;
	private JLabel infoLabel;
	private JButton nextButton;
	private JButton backButton;
	private JButton turnButton;
	private CardFolder folder;
	private Card card;

	public ShuffleCardViewPane(CardFolder folder) {
		// TODO 自動生成されたコンストラクター・スタブ
		super(new BorderLayout());
		this.folder = folder;

		//ラベルの設定
		cardLabel = new JLabel();
		cardLabel.setFont(new Font("MS ゴシック", Font.BOLD, 16));
		cardLabel.setHorizontalAlignment(JLabel.CENTER);

		//infoラベル
		infoLabel = new JLabel();
		infoLabel.setHorizontalAlignment(JLabel.RIGHT);

		//ラベルをまとめる
		JPanel textPane = new JPanel(new BorderLayout());
		textPane.add(cardLabel, BorderLayout.CENTER);
		textPane.add(infoLabel, BorderLayout.SOUTH);

		//ボタンの生成
		nextButton = new JButton("次へ");
		backButton = new JButton("戻る");
		turnButton = new JButton("裏返す");


		//ボタンにリスナを設定
		nextButton.addActionListener(new NextActionHandler());
		backButton.addActionListener(new BackActionHandler());
		turnButton.addActionListener(new TurnActionHandler());

		//ボタンをまとめる
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new GridLayout());
		buttonPane.add(backButton);
		buttonPane.add(turnButton);
		buttonPane.add(nextButton);

		add(textPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.SOUTH);

		int size = folder.getSize();
		shuffle = Shuffle.shuffleInt(size);
		folder.getCard(shuffle[0]);
		draw();
	}

	/**
	 * 更新処理
	 */
	public void draw() {

		card = folder.getCard(shuffle[index]);
		if (card != null) {
			cardLabel.setText("<html>" + card.getCurrentFace() + "</html>");
			String infoString = (index + 1) + "/" + folder.getSize();
			infoLabel.setText(infoString);
		}

	}

	public void shuffle() {
		index = 0;
		shuffle = Shuffle.shuffleInt(folder.getSize());
	}

	/**
	 * 次ボタンアクションのハンドラ。内部クラス
	 *
	 */
	private class NextActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (index < folder.getSize() - 1) {
				card.setFace(Card.FRONT);
				card = folder.getCard(++index);
			}
			draw();
		}
	}

	/**
	 * 戻るボタンアクションのハンドラ。内部クラス
	 *
	 */
	private class BackActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (index > 0) {
				card.setFace(Card.FRONT);
				card = folder.getCard(--index);
			}
			draw();
		}
	}

	/**
	 * 切り替えボタンアクションのハンドラ。内部クラス
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

}
