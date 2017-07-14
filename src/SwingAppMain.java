import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class SwingAppMain {


	/**
	 * アプリケーションの起動
	 */
	public static void main(String[] args) {
		System.out.println("main :" + SwingUtilities.isEventDispatchThread());

		//文字にアンチエイリアスをかける。
//		System.setProperty("awt.useSystemAAFontSettings","on");
//		System.setProperty("swing.aatext", "true");

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowCardList();
			}
		});
	}

	/**
	 * マネージャーの生成と表示を行う。
	 */
	private static void createAndShowCardList() {
		System.out.println("createAndShowTodoList : " + SwingUtilities.isEventDispatchThread());
		JFrame mainFrame = new JFrame("単語カード");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(400, 240);
		Container contentPane = mainFrame.getContentPane();
		//パネルを生成
		JComponent newContentPane = new ManagerPane(mainFrame);
		contentPane.add(newContentPane, BorderLayout.CENTER);
		//Windowサイズを調整
		//mainFrame.pack();
		//表示
		mainFrame.setVisible(true);

	}

}
