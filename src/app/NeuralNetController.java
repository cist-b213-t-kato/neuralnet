package app;
import java.io.IOException;

import artificialintelligence.Janken;
import artificialintelligence.NeuralNet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NeuralNetController {

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

	@FXML
	private Label youMessage;

	//訓練データ（入力）
	double inputs[][] = {
			{ 0, 0, 0, 0, 0, 0 }
	};

	private int kachi, make, aiko;

	private NeuralNet neuralNet;

	public NeuralNetController(){
		neuralNet = new NeuralNet(6, 6, 2);
	}


	@FXML
	public void anybuttonClicked(ActionEvent event) throws IOException, Exception{

		// 推定
		double[] output = neuralNet.compute(inputs[0]);

		Button button = (Button)event.getSource();

		String inputStr = button.getId();
        double[] a = Janken.toBinary(inputStr);
        int youHand = Janken.hand(a[0], a[1]);

		int comHand = (Janken.hand(output[0], output[1])+2)%3;
		String comStr = String.format("%s", Janken.strHand(comHand));
		comMessage.setText("COM: "+comStr);
		String youStr = String.format("%s", Janken.strHand(youHand));
		youMessage.setText("あなた: "+youStr);

		int judge = Janken.judge(youHand, comHand);
		if(judge==1){
			kachi += 1;
		}else if(judge==0){
			aiko += 1;
		}else{
			make += 1;
		}
		message.setText("勝ち"+kachi+"  負け"+make+"  あいこ"+aiko);

		// 訓練データ
		double ress[][] = {
				{a[0], a[1]}
		};

		// 学習
		neuralNet.learn(inputs, ress);

		// ログをずらす
		inputs[0][0] = inputs[0][2];
		inputs[0][1] = inputs[0][3];
		inputs[0][2] = inputs[0][4];
		inputs[0][3] = inputs[0][5];
		inputs[0][4] = a[0];
		inputs[0][5] = a[1];

	}

}