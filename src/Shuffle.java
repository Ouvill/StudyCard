import java.util.Random;

/*
 * ShuffleCardViewPaneに必要なメソッド
 * 何故か、別クラスを作った。今思えば疑問。
 * まあ、汎用性のあるクラスではある。
 */
public class Shuffle {

	public static int[] shuffleInt(int size) {

		int[] shuffle = new int[size];
		for (int i = 0; i < size; i++) {
			shuffle[i] = i;
		}

		Random rnd = new Random();

		for (int i = size; i > 0; i--) {
			swap(rnd.nextInt(i), i - 1, shuffle);
		}

		return shuffle;
	}

	public static void swap(int x, int y, int[] array) {
		int tmp = array[x];
		array[x] = array[y];
		array[y] = tmp;
	}

}
