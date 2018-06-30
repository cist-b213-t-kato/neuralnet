package oldai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class NeuralNet {

	double w1[][]; // 入力層>隠れ層の重み
	double w2[][]; // 隠れ層>出力層の重み

	double input[];
	double hidden[];
	double output[];

	double[] b1;
	double[] b2;

	double alpha = 0.1; // 学習率
//	private final double e2 = 0.0001;
	private final double e2 = 0.001;

	public static void main(String[] args) {
		NeuralNet nn = new NeuralNet(2, 10, 1);

		double[][] inputs = {
				{0, 0},
				{0, 1},
				{1, 0},
				{1, 1},
		};

		double[][] teachs = {
				{0},
				{1},
				{1},
				{0},
		};

		nn.learn(inputs, teachs);

		double[] in = { 1, 1 };

		System.out.println(nn.compute(in)[0]);

	}

	public NeuralNet(int inputLayer, int hiddenLayer, int outputLayer) {

		input = new double[inputLayer];
		hidden = new double[hiddenLayer];
		output = new double[outputLayer];

		w1 = new double[input.length][hidden.length];
		w2 = new double[hidden.length][output.length];

		b1 = new double[hidden.length];
		b2 = new double[output.length];

//		read();
		initWeight();

	}

	/**
	 * 重みを乱数で初期化
	 */
	private void initWeight() {

		// 重みを[-0.1, 0.1]で初期化
		Random rnd = new Random();

		for (int i = 0; i < input.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
				w1[i][j] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
			}
		}
		for ( int i=0; i<hidden.length; i++ ) {
			b1[i] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
		}

		for (int i = 0; i < hidden.length; i++) {
			for (int j = 0; j < output.length; j++) {
				w2[i][j] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
			}
		}
		for ( int i=0; i<output.length; i++ ) {
			b2[i] = (rnd.nextDouble() * 2.0 - 1.0) * 0.1;
		}

	}

	private void read() {

		try (	FileReader fr1 = new FileReader("w1.csv");
				BufferedReader br1 = new BufferedReader(fr1);
				FileReader fr2 = new FileReader("w2.csv");
				BufferedReader br2 = new BufferedReader(fr2); ){

			String line;
			StringTokenizer token;
			int i = 0;
			while ((line = br1.readLine()) != null) {
				token = new StringTokenizer(line, ",");

				int j = 0;
				while (token.hasMoreTokens()) {
					double value = Double.parseDouble(token.nextToken());
					w1[i][j] = value;
					j++;
				}
				i++;
			}

			i = 0;
			while ((line = br2.readLine()) != null) {
				token = new StringTokenizer(line, ",");

				int j = 0;
				while (token.hasMoreTokens()) {
					double value = Double.parseDouble(token.nextToken());
					w2[i][j] = value;
					j++;
				}
				i++;
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// NNに入力し、出力（＝Q値）を計算する
	public double[] compute(double in[]) {
		// 入力層
		for (int i = 0; i < input.length; i++) {
			input[i] = in[i];
		}

		// 隠れ層の計算
		for (int i = 0; i < hidden.length; i++) {
			hidden[i] = 0.0;
			for (int j = 0; j < input.length; j++) {
				hidden[i] += w1[j][i] * input[j];
			}
			hidden[i] += b1[i]; // b1[i] * 1;
			hidden[i] = sigmoid(hidden[i]);
		}

		// 出力層（Q値）の計算
		for (int i = 0; i < output.length; i++) {
			output[i] = 0.0;
			for (int j = 0; j < hidden.length; j++) {
				output[i] += w2[j][i] * (hidden[j]);
			}
			output[i] += b2[i]; // b2[i] * 1;
			output[i] = sigmoid(output[i]);
		}
		return output;
	}

	// シグモイド関数
	public double sigmoid(double i) {
		double a = 1.0 / (1.0 + Math.exp(-i));
		return a;
	}

	// 誤差逆伝播法による重みの更新
	private void backPropagation(double teach[]) {

		// 隠れ>出力の重みを更新
		for (int i = 0; i < teach.length; i++) {
			for (int j = 0; j < hidden.length; j++) {
//				double delta = -alpha * (-(teach[i] - output[i]) * output[i] * (1.0 - output[i]) * hidden[j]);
				double delta = alpha * ((teach[i] - output[i]) * output[i] * (1.0 - output[i]) * hidden[j]);
				w2[j][i] += delta;
			}
			double delta = alpha * ((teach[i] - output[i]) * output[i] * (1.0 - output[i]) * 1);
			b2[i] += delta;
		}

		// 入力>隠れの重みを更新
		for (int i = 0; i < hidden.length; i++) {

			double sum = 0.0;
			for (int k = 0; k < teach.length; k++) {
				sum += w2[i][k] * (teach[k] - output[k]) * output[k] * (1.0 - output[k]);
						// + b2[k]  * (teach[k] - output[k]) * output[k] * (1.0 - output[k]); // ほんと？
			}

			for (int j = 0; j < input.length; j++) {
				double delta = alpha * hidden[i] * (1.0 - hidden[i]) * input[j] * sum;
				w1[j][i] += delta;
			}
			double delta = alpha * hidden[i] * (1.0 - hidden[i]) * 1 * sum;
			b1[i] += delta;
		}
	}

	// 二乗誤差
	private double calcError(double teach[]) {
		double e = 0.0;
		for (int i = 0; i < teach.length; i++) {
			double ee = Math.pow(teach[i] - output[i], 2.0);
			e += ee;
		}
		e *= 0.5;
		return e;
	}

	public void learn(double[][] inputs, double[][] teach) {

		int j = 0;
		while (true) {

			// 二乗誤差の総和
			double e = 0.0;

			// すべての訓練データについて、BP
			for (int i = 0; i < inputs.length; i++) {
				compute(inputs[i]);
				backPropagation(teach[i]);
				e += calcError(teach[i]);
//				if ( e >= 0.1 && j % 1000 == 0 ) {
//					System.out.println("e: "+ e);
//					System.out.println("i: "+ i);
//					System.out.println("teach: "+ teach[i]);
//					System.out.println("output: "+ output[i]);
//				}
			}

			// 二乗誤差が十分小さくなったら、終了
			if (e < e2) {
				break;
			} else if ( j % 10000 == 0 ) {
//				if ( errorDrawable != null ) {
//					errorDrawable.draw(e);
//				}
				System.out.println("e: " + e);
			}

			j++;
		}

		write();

	}

	/**
	 * 学習結果をファイルに書き込むためのメソッド
	 */
	public void write() {

		try (FileWriter w1f = new FileWriter("w1.csv", false);
				PrintWriter pw1 = new PrintWriter(new BufferedWriter(w1f));
				FileWriter w2f = new FileWriter("w2.csv", false);
				PrintWriter pw2 = new PrintWriter(new BufferedWriter(w2f));) {

			StringBuilder sb1 = new StringBuilder();
			for (int i = 0; i < input.length; i++) {
				for (int j = 0; j < hidden.length; j++) {
					sb1.append(w1[i][j]);
					if (j + 1 != hidden.length) {
						sb1.append(",");
					}
				}
				sb1.append("\n");
			}
			pw1.print(sb1.toString());

			StringBuilder sb2 = new StringBuilder();
			for (int i = 0; i < hidden.length; i++) {
				for (int j = 0; j < output.length; j++) {
					sb2.append(w2[i][j]);
					if (j + 1 != output.length) {
						sb2.append(",");
					}
				}
				sb2.append("\n");
			}
			pw2.print(sb2.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
