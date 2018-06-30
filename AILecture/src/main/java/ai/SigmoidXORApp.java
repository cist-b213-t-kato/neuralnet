package ai;

/**
 * 排他的論理和を計算する
 * @author tkato
 *
 */
public class SigmoidXORApp {
	public static void main(String[] args) {

		SigmoidNeuralNetwork nn = new SigmoidNeuralNetwork(2, 4, 1);

		double[][] knownInputs = {
				{ 0, 0 },
				{ 0, 1 },
				{ 1, 0 },
				{ 1, 1 },
		};

		double teach[][] = {
				{ 0 },
				{ 1 },
				{ 1 },
				{ 0 }
		};

		// 学習
		nn.learn(knownInputs, teach);

		double[][] inputs = {
				{ 0, 0 },
				{ 0, 1 },
				{ 1, 0 },
				{ 1, 1 },
		};

		for ( int i=0; i<inputs.length; i++ ) {
			double[] output = nn.compute(inputs[i]);
			System.out.printf("%.0f %.0f %.4f\n", inputs[i][0], inputs[i][1], output[0]);
		}




	}
}
