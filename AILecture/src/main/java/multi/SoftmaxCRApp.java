package multi;

import ai.SoftmaxNeuralNetwork;

/**
 * 文字認識 (Character Recognition)
 * @author tkato
 *
 */
public class SoftmaxCRApp {
	static final int MASS_X = 6; // マス目の数（縦）
	static final int MASS_Y = 6; // マス目の数（横）

	public static void main(String[] args) {

		SoftmaxNeuralNetwork nn = new SoftmaxNeuralNetwork(36, 36, 10);

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
				{1, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // この組み合わせを0とする
				{0, 1, 0, 0, 0, 0, 0, 0, 0, 0}, // この組み合わせを1とする
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

		double expectNumber[] = {
				0,
				1
		};

		for ( int i=0; i<unknownInputs.length; i++ ) {
			double[] output = nn.compute(unknownInputs[i]);
			System.out.println("実体値：");
			System.out.println(expectNumber[i]);
			System.out.println("予測値：");
		}
	}

}
