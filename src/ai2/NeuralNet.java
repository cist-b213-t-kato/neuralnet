package ai2;

import java.util.Random;

public class NeuralNet {

	final int MASS_X = 6; // マス目の数（縦）
	final int MASS_Y = 6; // マス目の数（横）
	final int N_INPUT;  // 入力層の数、mass = 6 * 6 = 36
	final int N_HIDDEN; // 中間層の数、N_INPUTと同じ 36
	final int N_OUTPUT;  // 出力層の数、0~9にしたいので

	double w1[][];	//入力層>隠れ層の重み
	double w2[][];	//隠れ層>出力層の重み

	double input[];  // 入力層
	double hidden[]; // 中間層
	double output[]; // 出力層

	double alpha = 0.1;	//学習率

	public NeuralNet( int nInput, int nHidden, int nOutput ) {
		N_INPUT = nInput;
		N_HIDDEN = nHidden;
		N_OUTPUT = nOutput;

		input  = new double[N_INPUT];  // 入力層
		hidden = new double[N_HIDDEN]; // 中間層
		output = new double[N_OUTPUT]; // 出力層
	}

	// プログラムを起動する
	public static void main(String[] args) {
//		NeuralNet neuralNet = new NeuralNet(36, 36, 4);
		NeuralNet neuralNet = new NeuralNet(2, 2, 1);
		neuralNet.run();
	}

	// ニューラルネットワークの処理を行う
	public void run() {

		// 初期化
		initialize();

		double x[][] = {
				{ 0, 0 },
				{ 0, 1 },
				{ 1, 0 },
				{ 1, 1 },
		};

		double t[][] = {
				{ 0 },
				{ 1 },
				{ 1 },
				{ 0 }
		};

		/*
		// 訓練データ（入力）
		double x[][] = {
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
		*/

		int step = 0;

		// 学習
		while(true){

			double e = 0.0; // 二乗誤差の総和(初期値は0.0)
			double threshold = 0.01; // 二乗誤差の閾値

			// すべての訓練データをニューラルネットワークに入力・計算・誤差伝搬
			for(int i=0; i<x.length; i++){
				compute(x[i]);
				backPropagation(t[i]);
				e += calcError(t[i]);
			}

			System.out.println(step + " error:" + e);

			step++;

			// 二乗誤差が十分小さくなったら、終了
			if(e < threshold){
				break;
			}
		}

		// ---------------------
		// 推定はここから
		// ---------------------
		/*
		double[][] inputs = {
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
		*/

		double[][] inputs = {
				{ 0, 0 },
				{ 0, 1 },
				{ 1, 0 },
				{ 1, 1 },
		};

		for ( int i=0; i<inputs.length; i++ ) {
			compute(inputs[i]);
			System.out.println(output[0]);
		}

//		print(inputs[0], output, 0);
//		print(inputs[1], output, 1);

	}

	// 初期化処理を行う
	public void initialize(){
		// 重みを[-0.1, 0.1]で初期化
		Random rnd = new Random();

		w1 = new double[N_INPUT][N_HIDDEN];
		for(int i=0; i<N_INPUT; i++){
			for(int j=0; j<N_HIDDEN; j++){
				w1[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}

		w2 = new double[N_HIDDEN][N_OUTPUT];
		for(int i=0; i<N_HIDDEN; i++){
			for(int j=0; j<N_OUTPUT; j++){
				w2[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}
	}

	// NNに入力し、出力を計算する
	public void compute(double x[]){

		// 入力層の入力
		for(int i=0; i<N_INPUT; i++){
			input[i] = x[i];
		}

		// 中間層の計算
		for(int i=0; i<N_HIDDEN; i++){
			hidden[i] = 0.0;
			for(int j=0; j<N_INPUT; j++){
				hidden[i] += w1[j][i] * input[j];
			}
			hidden[i] = sigmoid(hidden[i]);
		}

		// 出力層の計算
		for(int i=0; i<N_OUTPUT; i++){
			output[i] = 0.0;
			for(int j=0; j<N_HIDDEN; j++){
				output[i] += w2[j][i] * (hidden[j]);
			}
			output[i] = sigmoid(output[i]);
		}
	}

	// シグモイド関数
	public double sigmoid(double i){
		double a = 1.0 / (1.0 + Math.exp(-i));
		return a;
	}

	// 誤差逆伝播法による重みの更新
	public void backPropagation(double teach[]){

		// 中間層>出力層の重みを更新
		for(int i=0; i<teach.length; i++){
			for(int j=0; j<N_HIDDEN; j++){
				double delta = alpha * output[i] * (1.0-output[i]) * (output[i]-teach[i]) * hidden[j] ;
				w2[j][i] -= delta;
			}
		}

		// 入力層>中間層の重みを更新
		for(int i=0; i<N_HIDDEN; i++){

			double sum = 0.0;
			for(int k=0; k<teach.length; k++){
				sum += w2[i][k] * output[k] * (1.0-output[k]) * (output[k]-teach[k]);
			}

			for(int j=0; j<N_INPUT; j++){
				double delta = alpha * hidden[i] * (1.0-hidden[i]) * sum * input[j];
				w1[j][i] -= delta;
			}
		}
	}

	// 二乗誤差
	public double calcError(double teach[]){
		double e = 0.0;
		for(int i=0; i<teach.length; i++){
			e += Math.pow(teach[i]-output[i], 2.0);
		}
		e *= 0.5;
		return e;
	}

	// 画面に入力データと実体値、予測値を表示する
	public void print(double[] inputs, double[] output, int expect) {
		System.out.println("入力データ");
		for (int i = 0; i < N_INPUT; ) {
			for (int j = 0; j < MASS_X; j++) {
				System.out.print((int)inputs[i]);
				System.out.print(" ");
				i++;
				if(i==N_INPUT) break;
			}
			System.out.println("");
		}
		System.out.println("実体値：" + expect + "　予測値：" + value(output[0], output[1], output[2], output[3]));
		System.out.println("\n");
	}

	// 出力データと数字のマッピングを行う
	public int value(double a1, double a2, double a3, double a4){
        // ステップ関数（0.5を閾値として0,1に変換）
        //  if     0 <= an < 0.5   then  0
        //  if  0.5 <   an <    1  then  1
		int v1 = (int)(a1+0.5);
		int v2 = (int)(a2+0.5);
		int v3 = (int)(a3+0.5);
		int v4 = (int)(a4+0.5);

		// 出力層の値
		int[][][][] outputValue = new int[2][2][2][2];
		outputValue[0][0][0][0] = 0;
		outputValue[0][0][0][1] = 1;
		outputValue[0][0][1][0] = 2;
		outputValue[0][0][1][1] = 3;
		outputValue[0][1][0][0] = 4;
		outputValue[0][1][0][1] = 5;
		outputValue[0][1][1][0] = 6;
		outputValue[0][1][1][1] = 7;
		outputValue[1][0][0][0] = 8;
		outputValue[1][0][0][1] = 9;

		return outputValue[v1][v2][v3][v4];
	}

}