package ai;

/**
 * 文字認識 (Character Recognition)
 * @author tkato
 *
 */
public class SigmoidCRApp {
	static final int MASS_X = 6; // マス目の数（縦）
	static final int MASS_Y = 6; // マス目の数（横）

	public static void main(String[] args) {

		SigmoidNeuralNetwork nn = new SigmoidNeuralNetwork(36, 36, 4);

		// 訓練データ（入力）
		double knownInputs[][] = {
			{ // 0の訓練データ
			  0, 0, 1, 1, 0, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 0, 1, 1, 0 ,0
			},
			{ // 1の訓練データ
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0 ,0
			}
		};

		// 教師データ
		double t[][] = {
				{0, 0, 0, 0}, // この組み合わせを0とする
				{0, 0, 0, 1}  // この組み合わせを1とする
		};

		// 学習
		nn.learn(knownInputs, t);

		// ---------------------
		// 推定はここから
		// ---------------------
		double[][] unknownInputs = {
			{ // 0の入力データ
			  0, 1, 1, 1, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 1, 0, 0, 1, 0,
			  0, 0, 1, 1, 0 ,0
			},
			{ // 1の入力データ
			  0, 0, 0, 0, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0, 0,
			  0, 0, 0, 1, 0 ,0
			}
		};

		// 教師データ
		double expects[][] = {
				{0, 0, 0, 0}, // この組み合わせを0とする
				{0, 0, 0, 1}  // この組み合わせを1とする
		};

		for ( int i=0; i<unknownInputs.length; i++ ) {
			double[] output = nn.compute(unknownInputs[i]);
			print(unknownInputs[i], output, expects[i]);
		}
	}

	// 画面に入力データと実体値、予測値を表示する
	public static void print(double[] input, double[] output, double[] expect) {
		System.out.println("入力データ");
		for (int j = 0; j < MASS_Y; j++) {
			for (int k=0; k<MASS_X; k++) {
				System.out.print((int)input[j*MASS_X+k]);
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println("実体値：" + value(expect) + " 予測値：" + value(output));
		System.out.println();
	}

	// 出力データと数字のマッピングを行う
	public static int value(double[] a){
        // ステップ関数（0.5を閾値として0,1に変換）
        //  if     0 <= an < 0.5   then  0
        //  if  0.5 <   an <    1  then  1
		int v1 = (int)(a[0]+0.5);
		int v2 = (int)(a[1]+0.5);
		int v3 = (int)(a[2]+0.5);
		int v4 = (int)(a[3]+0.5);

		// 出力層の値
		return v1 * 8 + v2 * 4 + v3 * 2 + v4 * 1;
	}
}
