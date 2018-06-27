package myai;

import java.util.Random;

/**
 * 多値分類（出力層が多ニューロンのソフトマックス関数）を行うNN.
 * @author tkato
 *
 */
public class MultiNeuralNetwork implements INeuralNetwork {

	private double[] input;
	private double[] hidden; //入力層からの出力
	private double[] output; //隠れ層からの出力

	private double[][] w;
	private double[] b;
	private double[][] v;
	private double[] c;

	private double eta;

	private Random rnd = new Random();

	private double emin = 0.0000001;

	public static void main(String[] args) {

		MultiNeuralNetwork nn = new MultiNeuralNetwork(25, 100, 10, 0.1);

		double[][] ins = {
				{
					0, 1, 1, 1, 0,
					0, 1, 0, 1, 0,
					0, 1, 0, 1, 0,
					0, 1, 0, 1, 0,
					0, 1, 1, 1, 0,
				},
				{
					0, 0, 1, 1, 0,
					0, 0, 0, 1, 0,
					0, 0, 0, 1, 0,
					0, 0, 0, 1, 0,
					0, 0, 0, 1, 0,
				},
		};
		double[][] ts = {
				{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
				{ 0, 1, 0, 0, 0, 0, 0, 0, 0, 0 },
		};

		nn.learn(ins, ts);

		double[] demo = {
				0, 1, 1, 1, 0,
				0, 1, 0, 1, 0,
				0, 1, 0, 1, 0,
				0, 1, 0, 1, 0,
				0, 1, 1, 1, 0,
		};

		nn.compute(demo);
		nn.printResult();

	}

	public void printResult() {
		for ( int i=0; i<output.length; i++ ) {
			System.out.printf( "%d: %.4f%%\n", i, output[i]*100 );
		}
	}

	public MultiNeuralNetwork(int iSize, int hSize, int oSize, double eta) {

		input = new double[iSize];
		hidden = new double[hSize];
		output = new double[oSize];

		w = new double[iSize][hSize];
		b = new double[hSize];
		v = new double[hSize][oSize];
		c = new double[oSize];

		this.eta = eta;

		initRandomWeight();

	}

	/**
	 *
	 * @see https://qiita.com/m-hayashi/items/02065a2e2ec3e2269e0b
	 * @return
	 */
	protected double createRandom( double n ) {
		return rnd.nextGaussian() * Math.sqrt(2/n);
	}

	private void initRandomWeight() {

		for (int j = 0; j < hidden.length; j++) {
			for (int i = 0; i < input.length; i++) {
				w[i][j] = createRandom(input.length);
			}
			b[j] = createRandom(1);
		}

		for (int j = 0; j < output.length; j++) {
			for (int i = 0; i < hidden.length; i++) {
				v[i][j] = createRandom(hidden.length);
			}
			c[j] = createRandom(1);
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
				wxb += w[i][j] * input[i];
			}
			wxb += b[j];
			hidden[j] = sigmoid(wxb);
		}

		double sum = 0; // softmax関数の分母となる
		for ( int j=0; j<output.length; j++ ) {
			double vhc = 0;
			for ( int i=0; i<hidden.length; i++ ) {
				vhc += v[i][j] * hidden[i];
			}
			vhc += c[j];
			sum += output[j] = Math.exp(vhc);
		}

		for ( int i=0; i<output.length; i++ ) {
			output[i] = output[i] / sum;
		}

		return output;
	}

	@Override
	public void learn( double[][] ins, double[][] ts ) {
		for ( int j=0; j<100000; j++ ) {
			double e = 0;
			for ( int i=0; i<ins.length; i++ ) {
				compute(ins[i]);
				backPropagation(ins[i], ts[i]);
				for ( int k=0; k<output.length; k++ ) {
					e += Math.pow(output[k] - ts[i][k], 2);
				}
			}

			e *= 0.5;

//			if ( j % 100 == 0 ) {
//				System.out.println("error: " + e);
//			}

			if ( e < emin ) {
				System.out.println("e<" + emin);
				break;
			}
		}
//		printWeight();
	}

	private void backPropagation( double in[], double t[] ) {

		double[] dk = new double[output.length];

		for ( int i=0; i<output.length; i++ ) {
			dk[i] = t[i] - output[i];
			for ( int j=0; j<hidden.length; j++ ) {
				v[j][i] += eta * dk[i] * hidden[j];
			}
			c[i] += eta * dk[i];
		}

		for ( int j=0; j<hidden.length; j++ ) {
			double dj = 0;
			for ( int k=0; k<output.length; k++ ) {
				dj += v[j][k] * dk[k];
			}
			dj *= hidden[j] * ( 1 - hidden[j] );
			for ( int i=0; i<input.length; i++ ) {
				w[i][j] += eta * dj * input[i];
			}
			b[j] += eta * dj;
		}
	}

	public void printWeight() {

		for ( int i=0; i<w.length; i++ ) {
			System.out.print("w:(");
			for ( int j=0; j<w[i].length; j++ ) {
				System.out.print(w[i][j] + ",");
			}
			System.out.println(")");
		}

		System.out.print("b:(");
		for ( int i=0; i<b.length; i++ ) {
			System.out.print(b[i] + ",");
		}
		System.out.println(")");

		for ( int i=0; i<v.length; i++ ) {
			System.out.print("v:(");
			for ( int j=0; j<v[i].length; j++ ) {
				System.out.print(v[i][j] + ",");
			}
			System.out.println(")");
		}

		System.out.print("c:(");
		for ( int i=0; i<c.length; i++ ) {
			System.out.print(c[i] + ",");
		}
		System.out.println(")");

	}

}









