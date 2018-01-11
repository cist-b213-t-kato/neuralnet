package artificialintelligence;

import java.util.Random;

public class NeuralNet {

	double w1[][];	//入力層>隠れ層の重み
	double w2[][];	//隠れ層>出力層の重み

	double input[];
	double hidden[];
	double output[];

	double alpha = 0.1;	//学習率
	private final double e2 = 0.0001;

	public NeuralNet(int inputLayer, int hiddenLayer, int outputLayer) {

		input = new double[inputLayer];
		hidden = new double[hiddenLayer];
		output = new double[outputLayer];

		//重みを[-0.1, 0.1]で初期化
		Random rnd = new Random();

		w1 = new double[input.length][hidden.length];
		for(int i=0; i<input.length; i++){
			for(int j=0; j<hidden.length; j++){
				w1[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}

		w2 = new double[hidden.length][output.length];
		for(int i=0; i<hidden.length; i++){
			for(int j=0; j<output.length; j++){
				w2[i][j] = (rnd.nextDouble()*2.0 - 1.0) * 0.1;
			}
		}

	}



	//NNに入力し、出力（＝Q値）を計算する
	public double[] compute(double in[]){
		//入力層
		for(int i=0; i<input.length; i++){
			input[i] = in[i];
		}

		//隠れ層の計算
		for(int i=0; i<hidden.length; i++){
			hidden[i] = 0.0;
			for(int j=0; j<input.length; j++){
				hidden[i] += w1[j][i] * input[j];
			}
			hidden[i] = sigmoid(hidden[i]);
		}

		//出力層（Q値）の計算
		for(int i=0; i<output.length; i++){
			output[i] = 0.0;
			for(int j=0; j<hidden.length; j++){
				output[i] += w2[j][i] * (hidden[j]);
			}
			output[i] = sigmoid(output[i]);
		}
		return output;
	}

	//シグモイド関数
	public double sigmoid(double i){
		double a = 1.0 / (1.0 + Math.exp(-i));
		return a;
	}

	//誤差逆伝播法による重みの更新
	private void backPropagation(double teach[]){

		//隠れ>出力の重みを更新
		for(int i=0; i<teach.length; i++){
			for(int j=0; j<hidden.length; j++){
				double delta = -alpha*( -(teach[i]-output[i])*output[i]*(1.0-output[i])*hidden[j] );
				w2[j][i] += delta;
			}
		}

		//入力>隠れの重みを更新
		for(int i=0; i<hidden.length; i++){

			double sum = 0.0;
			for(int k=0; k<teach.length; k++){
				sum += w2[i][k]*(teach[k]-output[k])*output[k]*(1.0-output[k]);
			}

			for(int j=0; j<input.length; j++){
				double delta = alpha*hidden[i]*(1.0-hidden[i])*input[j]*sum;
				w1[j][i] += delta;
			}
		}
	}

	//二乗誤差
	private double calcError(double teach[]){
		double e = 0.0;
		for(int i=0; i<teach.length; i++){
			e += Math.pow(teach[i]-output[i], 2.0);
		}
		e *= 0.5;
		return e;
	}

	public void learn( double[][] inputs,  double[][] ress ) {

		while(true){

			//二乗誤差の総和
			double e = 0.0;

			//すべての訓練データについて、BP
			for(int i=0; i<inputs.length; i++){
				compute(inputs[i]);
				backPropagation(ress[i]);
				e += calcError(ress[i]);
			}

			//二乗誤差が十分小さくなったら、終了
			if(e < e2){
				break;
			}
		}
	}


}
