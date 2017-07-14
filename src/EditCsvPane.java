import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditCsvPane extends JPanel {

	/**
	 * カード編集用のパネル。
	 * @param args
	 */
	//最初に作った編集パネル。表を使ったパネルを作ったので現在未使用。
	JLabel frontLabel;
	JLabel backLabel;
	JTextField frontTextfield;
	JTextField backTextfield;
	JButton addButton;

	JButton modifyButton;
	CardFolder folder;
	Card card;

	public EditCsvPane(CardFolder folder) {
		super(new BorderLayout());
		this.folder = folder;

		//ラベル、テキストエリアの生成
		frontLabel = new JLabel("表");
		backLabel = new JLabel("裏");
		frontTextfield = new JTextField(10);
		backTextfield = new JTextField(10);

		draw();

		//入力パネルをまとめる
		JPanel frontPane = new JPanel(new BorderLayout());
		JPanel backPane = new JPanel(new BorderLayout());

		frontPane.add(frontLabel, BorderLayout.LINE_START);
		frontPane.add(frontTextfield, BorderLayout.LINE_END);
		backPane.add(backLabel, BorderLayout.LINE_START);
		backPane.add(backTextfield, BorderLayout.LINE_END);

		JPanel inputPane = new JPanel();
		inputPane.add(frontPane);
		inputPane.add(backPane);

		//ボタンを生成
		addButton = new JButton("新規追加");
		modifyButton = new JButton("カードを更新");
		//アクションハンドラを追加
		addButton.addActionListener(new addActionHandler());
		modifyButton.addActionListener(new modifyActionHandler());

		JPanel buttonPane = new JPanel(new GridLayout());
		buttonPane.add(addButton);
		buttonPane.add(modifyButton);

		add(inputPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.SOUTH);
	}

	public void draw() {
		card = folder.getCurrentCard();
		frontTextfield.setText(card.getFront());
		backTextfield.setText(card.getBack());
	}

	/**
	 * 追加ボタンアクションのハンドラ;
	 *
	 */
	private class addActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String front = frontTextfield.getText();
			String back = backTextfield.getText();
			frontTextfield.setText("");
			backTextfield.setText("");
			folder.addCard(new Card(front,back,folder.getDefaultFace()));
		}
	}

	/**
	 * 更新ボタンアクションのハンドラ
	 */
	private class modifyActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String front = frontTextfield.getText();
			String back = backTextfield.getText();
			card.Initialize(front, back, folder.getDefaultFace());
		}
	}
}
