package ai;

import java.util.Random;

/**
 * 出力層が Softmax 関数の NN.
 * @author tkato
 *
 */
public class SoftmaxNeuralNetwork implements INeuralNetwork {

	private double[] input;
	private double[] hidden; //入力層からの出力
	private double[] output; //隠れ層からの出力

	private double[][] w1;
	private double[][] w2;
	private double[] b1;
	private double[] b2;

	private final double alpha;

	private Random rnd = new Random();

	private double emin = 0.00001;

	protected int printStep = 10;

	public SoftmaxNeuralNetwork(int iSize, int hSize, int oSize, double alpha) {
		input = new double[iSize];
		hidden = new double[hSize];
		output = new double[oSize];

		w1 = new double[iSize][hSize];
		b1 = new double[hSize];
		w2 = new double[hSize][oSize];
		b2 = new double[oSize];

		this.alpha = alpha;

		initWeight();
	}

	public SoftmaxNeuralNetwork(int iSize, int hSize, int oSize) {
		this(iSize, hSize, oSize, 0.1);
	}
	/**
	 *
	 * @see https://qiita.com/m-hayashi/items/02065a2e2ec3e2269e0b
	 * @return
	 */
	protected double createRandom( double n ) {
		return rnd.nextGaussian() * Math.sqrt(2/n);
	}

	/**
	 *  重みの初期化
	 */
	private void initWeight() {

		for (int j = 0; j < hidden.length; j++) {
			for (int i = 0; i < input.length; i++) {
				w1[i][j] = createRandom(input.length);
			}
			b1[j] = createRandom(1);
		}

		for (int j = 0; j < output.length; j++) {
			for (int i = 0; i < hidden.length; i++) {
				w2[i][j] = createRandom(hidden.length);
			}
			b2[j] = createRandom(1);
		}

	}

	private double sigmoid( double x ) {
		return 1 / ( 1 + Math.exp(-x) );
	}

	@Override
	public double[] compute( double[] in ) {

		input = in;

		for ( int j=0; j<hidden.length; j++ ) {
			double wxb = 0;
			for ( int i=0; i<input.length; i++ ) {
				wxb += w1[i][j] * input[i];
			}
			wxb += b1[j];
			hidden[j] = sigmoid(wxb);
		}

		double sum = 0; // softmax関数の分母となる
		for ( int j=0; j<output.length; j++ ) {
			double vhc = 0;
			for ( int i=0; i<hidden.length; i++ ) {
				vhc += w2[i][j] * hidden[i];
			}
			vhc += b2[j];
			output[j] = Math.exp(vhc);
			sum += output[j];
		}

		for ( int i=0; i<output.length; i++ ) {
			output[i] = output[i] / sum;
		}

		return output;
	}

	@Override
	public void learn( double[][] knownInputs, double[][] teach ) {
		for ( int j=0; j<100000; j++ ) {
			double e = 0;
			for ( int i=0; i<knownInputs.length; i++ ) {
				compute(knownInputs[i]);
				backPropagation(knownInputs[i], teach[i]);
				for ( int k=0; k<output.length; k++ ) {
					e += Math.pow(output[k] - teach[i][k], 2);
				}
			}

			e *= 0.5;

			if ( j % printStep == 0 ) {
				printError(e);
			}

			if ( e < emin ) {
				System.out.println("e<" + emin);
				break;
			}
		}
//		printWeight();
	}

	/**
	 * 誤差逆伝播を1ステップ行う
	 * @param in
	 * @param t
	 */
	private void backPropagation( double in[], double t[] ) {

		double[] dk = new double[output.length];

		for ( int i=0; i<output.length; i++ ) {
			dk[i] = t[i] - output[i];
			for ( int j=0; j<hidden.length; j++ ) {
				w2[j][i] += alpha * dk[i] * hidden[j];
			}
			b2[i] += alpha * dk[i];
		}

		for ( int j=0; j<hidden.length; j++ ) {
			double dj = 0;
			for ( int k=0; k<output.length; k++ ) {
				dj += w2[j][k] * dk[k];
			}
			dj *= hidden[j] * ( 1 - hidden[j] );
			for ( int i=0; i<input.length; i++ ) {
				w1[i][j] += alpha * dj * input[i];
			}
			b1[j] += alpha * dj;
		}
	}

	/**
	 * 全ての重みを表示するメソッド
	 */
	public void printWeight() {

		for ( int i=0; i<w1.length; i++ ) {
			System.out.print("w:(");
			for ( int j=0; j<w1[i].length; j++ ) {
				System.out.print(w1[i][j] + ",");
			}
			System.out.println(")");
		}

		System.out.print("b:(");
		for ( int i=0; i<b1.length; i++ ) {
			System.out.print(b1[i] + ",");
		}
		System.out.println(")");

		for ( int i=0; i<w2.length; i++ ) {
			System.out.print("v:(");
			for ( int j=0; j<w2[i].length; j++ ) {
				System.out.print(w2[i][j] + ",");
			}
			System.out.println(")");
		}

		System.out.print("c:(");
		for ( int i=0; i<b2.length; i++ ) {
			System.out.print(b2[i] + ",");
		}
		System.out.println(")");

	}

	protected void printError(double error) {
		System.out.println("error: " + error);
	}

}









