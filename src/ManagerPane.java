import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * パネルの管理をするJPanel
 *
 */
public class ManagerPane extends JPanel {

	/**
	 *
	 */
	public static final int VIEW = 0;
	public static final int RANDOM = 1;
	public static final int EDIT = 2;


	private int mode;
	private CardFolder folder;
	private JFrame mainFrame;
	private JPanel mainPane;
	private JButton changeButton;
	private JButton openButton;

	private CardLayout layout;
	private CardViewPane viewPane;

	private EditCsvTablePane editPane;

	public ManagerPane(JFrame frame) {
		super(new BorderLayout());

		mainFrame = frame;
		mode = VIEW;
		folder = new CardFolder();

		layout = new CardLayout();
		mainPane = new JPanel(layout);

		editPane = new EditCsvTablePane(folder);
		viewPane = new CardViewPane(folder);

		mainPane.add(viewPane, "viewPane");
		mainPane.add(editPane, "editPane");

		openButton = new JButton("ファイルを開く");
		changeButton = new JButton("編集モードへ");

		openButton.addActionListener(new openActionHandler());
		changeButton.addActionListener(new changeActionHandler());

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));

		buttonPane.add(openButton);
		buttonPane.add(changeButton);

		add(buttonPane, BorderLayout.NORTH);
		add(mainPane, BorderLayout.CENTER);

	}

	/**
	 * 開くボタンアクションのハンドラ。内部クラス
	 */

	private class openActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser filechooser = new JFileChooser("media/");

			int selected = filechooser.showOpenDialog(ManagerPane.this);
			if (selected == JFileChooser.APPROVE_OPTION) {
				File file = filechooser.getSelectedFile();
				folder.accesser.setCsvFile(file);
				folder.accesser.reloadCardFromCSV();
				editPane.resetFolderCard();
				folder.getCard(0);
				viewPane.draw();
				editPane.draw();

				System.out.println("読み込んだファイル" + file.getName());
			}
		}
	}

	/**
	 * 切り替えボタンアクションのハンドラ。内部クラス
	 *
	 */
	private class changeActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (mode == VIEW) {
				mode = EDIT;
				editPane.draw();
				mainFrame.setSize(400, 480);
				layout.show(mainPane, "editPane");

				changeButton.setText("閲覧モードへ");
			} else {
				mode = VIEW;
				folder.getCard(0);
				viewPane.draw();
				mainFrame.setSize(400,240);
				layout.show(mainPane, "viewPane");
				changeButton.setText("編集モードへ");
			}
		}
	}

}
