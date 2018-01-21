package myai;

import java.util.Random;

/**
 * 多値分類（出力層が多ニューロンのソフトマックス関数）を行うNN.
 * @author tkato
 *
 */
public class MultiNeuralNetwork {

	private double[] input;
	private double[] hidden; //入力層からの出力
	private double[] output; //隠れ層からの出力

	private double[][] w;
	private double[] b;
	private double[][] v;
	private double[] c;

	private double eta;

	public static void main(String[] args) {

		new MultiNeuralNetwork(2, 2, 1, 0.1).execute(1000);		//収束しない

		/*
		 * ( 結論？ )
		 * 排他的論理和ほどの規模の回路なら、学習率0.5くらいがちょうどいい？
		 * やはり隠れ層はニューロンが多いほど学びやすそう
		 * */

	}

	public void execute( int count ) {

		double[][] ins = {
				{ 0, 0 },
				{ 0, 1 },
				{ 1, 0 },
				{ 1, 1 },
		};
		double[][] ts = {
				{ 0 },
				{ 1 },
				{ 1 },
				{ 0 },
		};

		System.out.printf("input:%d hidden:%d output:%d eta:%f count:%d\n", input.length, hidden.length, output.length, eta, count);

		for ( int j=1; j<=count; j++ ) {
			for ( int i=0; i<4; i++ ) {
				learn(ins[i], ts[i]);
				if ( j % count == 0 ) {
					double[] y = compute(ins[i]);
					System.out.printf("in:(%f,%f)  y:(%f)\n", ins[i][0], ins[i][1], y[0]);
				}
			}
			if ( j % 100 == 0 ) {
//				nn.printWeight();
			}
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

	private void initRandomWeight() {

		Random rnd = new Random();

		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				w[i][j] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
			}
		}
		for ( int i=0; i<hidden.length; i++ ) {
			b[i] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
		}

		for (int i = 0; i < hidden.length; i++) {
			for (int j = 0; j < output.length; j++) {
				v[i][j] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
			}
		}
		for ( int i=0; i<output.length; i++ ) {
			c[i] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
		}

	}

	private double sigmoid( double x ) {
		return 1 / ( 1 + Math.exp(-x) );
	}

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

		for ( int j=0; j<output.length; j++ ) {
			double vhc = 0;
			for ( int i=0; i<hidden.length; i++ ) {
				vhc += v[i][j] * hidden[i];
			}
			vhc += c[j];
			output[j] = sigmoid(vhc);
		}

		return output;
	}

	public void learn( double in[], double t[] ) {
		compute(in);

		for ( int i=0; i<output.length; i++ ) {
			double dk = output[i] - t[i];
			for ( int j=0; j<hidden.length; j++ ) {
				v[j][i] += - eta * dk * hidden[j];
			}
			c[i] += - eta * dk;
		}

		for ( int j=0; j<hidden.length; j++ ) {
			double dj = 0;
			for ( int k=0; k<output.length; k++ ) {
				double dk = output[k] - t[k];
				dj += v[j][k] * dk;
			}
			dj *= hidden[j] * ( 1 - hidden[j] );
			for ( int i=0; i<input.length; i++ ) {
				w[i][j] += - eta * dj * input[i];
			}
			b[j] += - eta * dj;
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









