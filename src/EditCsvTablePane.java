import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class EditCsvTablePane extends JPanel {

	private CardFolder folder;
	private String[] columNames = { "表", "裏" };
	private JLabel infoLabel;
	private JButton upRowButton;
	private JButton downRowButton;
	private JButton addButton;
	private JButton resetButton;
	private JButton deleteButton;
	private JButton saveAsButton;
	private JButton applyButton;
	private DefaultTableModel tableModel;
	private JTable table;

	public EditCsvTablePane(CardFolder folder) {
		super(new BorderLayout());

		this.folder = folder;

		tableModel = new DefaultTableModel(columNames, 0);
		setFolderCard();

		table = new JTable(tableModel);
		JScrollPane sp = new JScrollPane(table);

		infoLabel = new JLabel();
		upRowButton = new JButton("上に移動");
		downRowButton = new JButton("下に移動");
		addButton = new JButton("行を追加");
		deleteButton = new JButton("行を削除");
		resetButton = new JButton("リセット");
		saveAsButton = new JButton("名前をつけて保存");
		applyButton = new JButton("保存");

		draw();

		upRowButton.addActionListener(new upRowActionHandler());
		downRowButton.addActionListener(new downRowActionHandler());
		addButton.addActionListener(new addActionHandler());
		deleteButton.addActionListener(new deleteActionHandler());
		resetButton.addActionListener(new resetActionHandler());
		saveAsButton.addActionListener(new saveAsActionHandler());
		applyButton.addActionListener(new applyActionHandler());

		JPanel moveRowPane = new JPanel(new GridLayout());
		moveRowPane.add(upRowButton);
		moveRowPane.add(downRowButton);

		JPanel buttonPane1 = new JPanel(new GridLayout());
		buttonPane1.add(addButton);
		buttonPane1.add(deleteButton);
		buttonPane1.add(resetButton);


		JPanel buttonPane2 = new JPanel(new GridLayout());
		buttonPane2.add(saveAsButton);
		buttonPane2.add(applyButton);

		JPanel buttonPane = new JPanel(new GridLayout(3, 1));
		buttonPane.add(moveRowPane);
		buttonPane.add(buttonPane1);
		buttonPane.add(buttonPane2);

		add(infoLabel, BorderLayout.NORTH);
		add(sp, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.SOUTH);

		int tmp = tableModel.getRowCount();
		String front, back;
		for (int i = 0; i < tmp; i++) {
			front = (String) tableModel.getValueAt(i, 0);
			back = (String) tableModel.getValueAt(i, 1);
			System.out.println(front + "," + back);
		}
	}

	public void draw() {
		String info = folder.accesser.getAccessFile().getName();
		infoLabel.setText(info);
	}

	/**
	 * カードフォルダーからtableModelにカードを配置
	 * @return
	 */
	public void setFolderCard() {
		int size = folder.getSize();

		Card card;
		String addCard[] = new String[2];
		for (int i = 0; i < size; i++) {
			card = folder.getCard(i);
			addCard[0] = card.getFront();
			addCard[1] = card.getBack();
			tableModel.addRow(addCard);
		}

	}

	/**
	 * カードフォルダーからtableModelにカードを再設置
	 */
	public void resetFolderCard() {
		//テーブルモデルの初期化
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; i++) {
			tableModel.removeRow(0);
		}

		//テーブルモデルにカードを設置
		setFolderCard();

	}

	/**
	 *upRowアクションのハンドラー
	 */
	private class upRowActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			int[] selectedRows = table.getSelectedRows();
			int size = selectedRows.length;
			if (selectedRows == null || size == 0) {
				return;
			}
			if (selectedRows[0] == 0) {
				return;
			}

			for (int i = 0; i < size; i++) {
				tableModel.moveRow(selectedRows[i], selectedRows[i], selectedRows[i] - 1);
			}

			table.clearSelection();

			for (int i = 0; i < size; i++) {
				table.addRowSelectionInterval(selectedRows[i] - 1, selectedRows[i] - 1);
			}

		}
	}

	private class downRowActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			int[] selectedRows = table.getSelectedRows();
			int size = selectedRows.length;
			if (selectedRows == null || size == 0) {
				return;
			}

			if (selectedRows[size - 1] == (tableModel.getRowCount() - 1)) {
				return;
			}

			for (int i = 0; i < size; i++) {
				tableModel.moveRow(selectedRows[i], selectedRows[i], selectedRows[i] + 1);
			}

			table.clearSelection();

			for (int i = 0; i< size; i++) {
				table.addRowSelectionInterval(selectedRows[i] + 1, selectedRows[i] + 1);
			}
		}
	}

	/**
	 * 追加ボタンアクションのハンドラー
	 *
	 */
	private class addActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String[] nullString = { "front", "back" };
			tableModel.addRow(nullString);

		}
	}

	/**
	 * 削除ボタンアクションのハンドラー
	 *
	 */
	private class deleteActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int[] selectedRows = table.getSelectedRows();
			int size = selectedRows.length;
			if (selectedRows == null || size <= 0) {
				return;
			}
			for (int i = 0; i < size; i++) {
				tableModel.removeRow(selectedRows[i] - i);
			}
		}
	}

	/**
	 * 名前をつけて保存アクションのハンドラー
	 */
	private class saveAsActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO 自動生成されたメソッド・スタブ
			JFileChooser filechooser = new JFileChooser("media");
			FileFilter csvFilter = new FileNameExtensionFilter("CSV(カンマ区切り)(*.csv)", "csv");
			filechooser.addChoosableFileFilter(csvFilter);
			filechooser.setAcceptAllFileFilterUsed(false);

			int selected = filechooser.showSaveDialog(EditCsvTablePane.this);

			if (selected == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();

				try {
					file.createNewFile();
				} catch (IOException e1) {
					// TODO 自動生成された catch ブロック
					e1.printStackTrace();
				}

				folder.accesser.setCsvFile(file);

				writeTableDataToCSV();
			}
		}
	}

	/**
	 * 適用ボタンアクションのハンドラー
	 *
	 */
	private class applyActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			writeTableDataToCSV();
		}
	}

	/**
	 *	リセットボタンアクションのハンドラー
	 *
	 */
	private class resetActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			resetFolderCard();

		}
	}

	//テーブルデータからファイルに書き込み
	public void writeTableDataToCSV() {
		//フォルダーの初期化
		folder.Initialize();

		//テーブルモデルからデータをフォルダーに格納
		String front, back;
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; i++) {
			front = tableModel.getValueAt(i, 0).toString();
			back = tableModel.getValueAt(i, 1).toString();

			String comma = "&#44;";
			front = front.replaceAll(",", comma);
			back = back.replace(",", comma);

			folder.addCard(new Card(front, back, folder.getDefaultFace()));
		}
		folder.accesser.writeAllCardToCSV();

		draw();
	}

}
