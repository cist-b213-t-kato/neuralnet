package artificialintelligence;
import java.io.IOException;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NeuralNet {

	static final int N_INPUT = 6;
	static final int N_HIDDEN = 6;
	static final int N_OUTPUT = 2;

	double w1[][];	//入力層>隠れ層の重み
	double w2[][];	//隠れ層>出力層の重み

	double input[];
	double hidden[];
	double output[];

	double alpha = 0.1;	//学習率

	public NeuralNet(){

		input  = new double[N_INPUT];
		hidden = new double[N_HIDDEN];
		output = new double[N_OUTPUT];

		//重みを[-0.1, 0.1]で初期化
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

	@FXML
	private Button g;

	@FXML
	private Button c;

	@FXML
	private Button p;

	@FXML
	private Label comMessage;

	@FXML
	private Label message;

	private double d1 = 0, d2 = 0, d3 = 0, d4 = 0, d5 = 0, d6 = 0;
	private final double e2 = 0.0001;
	private int kachi, make, aiko;

	@FXML
	public void anybuttonClicked(ActionEvent event) throws IOException, Exception{


		//訓練データ（入力）
		double inputs[][] = {
//				{1, 1},
//				{1, 0},
//					{0, 1},
				{d1, d2, d3, d4, d5, d6}
		};

		d5 = d3;
		d6 = d4;
		d3 = d1;
		d4 = d2;

		Button button = (Button)event.getSource();

		String inputStr = button.getId();
        double[] a = Janken.trans(inputStr);
        d1 = a[0];
        d2 = a[1];
        int youHand = Janken.hand(d1, d2);

		compute(inputs[0]);
		int comHand = (Janken.hand(output[0], output[1])+2)%3;
		String str = String.format("%s", Janken.strHand(comHand));
		comMessage.setText("COM: "+str);

		int judge = Janken.judge(youHand, comHand);
		if(judge==1){
			kachi += 1;
		}else if(judge==0){
			aiko += 1;
		}else{
			make += 1;
		}
		message.setText("勝ち"+kachi+"  負け"+make+"  あいこ"+aiko);

		//訓練データ（出力）
		double ress[][] = {
//				{0, 1},
//				{0, 1},
//					{0, 0},
				{d1, d2}
		};
		while(true){

			//二乗誤差の総和
			double e = 0.0;

			//すべての訓練データについて、BP
			for(int i=0; i<inputs.length; i++){
				compute(inputs[0]);
				backPropagation(ress[i]);
				e += calcError(ress[i]);
			}

			//二乗誤差が十分小さくなったら、終了
			if(e < e2){
				break;
			}
		}

	}

//	//メイン関数
//	public static void main(String[] args) throws IOException, Exception {
//
//		NeuralNet nn = new NeuralNet();
//
//	}

	//NNに入力し、出力（＝Q値）を計算する
	public void compute(double in[]){

		//入力層
		for(int i=0; i<N_INPUT; i++){
			input[i] = in[i];
		}

		//隠れ層の計算
		for(int i=0; i<N_HIDDEN; i++){
			hidden[i] = 0.0;
			for(int j=0; j<N_INPUT; j++){
				hidden[i] += w1[j][i] * input[j];
			}
			hidden[i] = sigmoid(hidden[i]);
		}

		//出力層（Q値）の計算
		for(int i=0; i<N_OUTPUT; i++){
			output[i] = 0.0;
			for(int j=0; j<N_HIDDEN; j++){
				output[i] += w2[j][i] * (hidden[j]);
			}
			output[i] = sigmoid(output[i]);
		}
	}

	//シグモイド関数
	public double sigmoid(double i){
		double a = 1.0 / (1.0 + Math.exp(-i));
		return a;
	}

	//誤差逆伝播法による重みの更新
	public void backPropagation(double teach[]){

		//隠れ>出力の重みを更新
		for(int i=0; i<teach.length; i++){
			for(int j=0; j<N_HIDDEN; j++){
				double delta = -alpha*( -(teach[i]-output[i])*output[i]*(1.0-output[i])*hidden[j] );
				w2[j][i] += delta;
			}
		}

		//入力>隠れの重みを更新
		for(int i=0; i<N_HIDDEN; i++){

			double sum = 0.0;
			for(int k=0; k<teach.length; k++){
				sum += w2[i][k]*(teach[k]-output[k])*output[k]*(1.0-output[k]);
			}

			for(int j=0; j<N_INPUT; j++){
				double delta = alpha*hidden[i]*(1.0-hidden[i])*input[j]*sum;
				w1[j][i] += delta;
			}
		}
	}

	//二乗誤差
	public double calcError(double teach[]){
		double e = 0.0;
		for(int i=0; i<teach.length; i++){
			e += Math.pow(teach[i]-output[i], 2.0);
		}
		e *= 0.5;
		return e;
	}

}