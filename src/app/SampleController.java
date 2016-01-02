package app;


import java.math.BigDecimal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SampleController {
	// 表示ディスプレイ
	@FXML
	private TextArea display;

	// 数字入れるList
	private BigDecimal[] left = new BigDecimal[100];

	// 演算子入れるList
	private String[] selectoperator = new String[100];

	// 入れた回数カウントするやつ
	private int sumcount;
	private int oprcount;

	//計算結果
	private BigDecimal sum;

	 //コンストラクタ
	public SampleController() {
		this.sumcount = 0;
		this.oprcount = 0;
	}

	// ボタン押された時のEvent

	@FXML
	public void anybuttonClicked(ActionEvent e) {

		String str = display.getText();

		Button button = (Button) e.getSource();

		if (button.getText().equals("C") || button.getText().equals("AC")) {

			if (button.getText().equals("AC")) {

				for (int i = 0; i < sumcount; i++) {
					left[i] = BigDecimal.ZERO;
				}
				for (int i = 0; i < oprcount; i++) {
					selectoperator[i] = "";
				}
				sumcount = 0;
				oprcount = 0;
			}

			if (button.getText().equals("C")) {
				left[sumcount] = BigDecimal.ZERO;
				sumcount--;
			}
			display.setText("");

			return;
		}
		if (button.getText().matches("[0-9\\.]")) {
			String btntext = button.getText();
			display.setText(str + btntext);
			return;
		}
		if (button.getText().matches("[+－×÷]")) {
			BigDecimal big = new BigDecimal(display.getText());
			selectoperator[oprcount] = button.getText();
			left[sumcount] = big;

			display.setText("");
			sumcount++;
			oprcount++;

			return;
		}
		if (button.getText().matches("=")) {
			BigDecimal big = new BigDecimal(display.getText());
			left[sumcount] = big;
			 display.setText(calculate());

			return;
		}

	}


	public String calculate() {
		for (int i = 0; i < oprcount; i++) {
			if (selectoperator[i].equals("÷")) {
				selectoperator[i]="";
				try {
					left[i] = left[i].divide(left[i + 1]);
				} catch (ArithmeticException e) {
					left[i] = left[i].divide(left[i + 1], 4,
							BigDecimal.ROUND_HALF_UP);
				}
				for (int j = i + 1; j < sumcount; j++) {
					left[j] = left[j + 1];
				}
			}
		}

		for (int i = 0; i < oprcount; i++) {
			if (selectoperator[i].equals("×")) {
				left[i] = left[i].multiply(left[i + 1]);

				for (int j = i + 1; j < sumcount; j++) {
					left[j] = left[j + 1];
				}
			}
		}

		for (int i = 0; i < oprcount; i++) {
			if (selectoperator[i].equals("+")) {
				left[i] = left[i].add(left[i + 1]);

				for (int j = i + 1; j < sumcount; j++) {
					left[j] = left[j + 1];
				}
			}

			if (selectoperator[i].equals("-")) {
				left[i] = left[i].subtract(left[i + 1]);

				for (int j = i + 1; j < sumcount; j++) {
					left[j] = left[j + 1];
				}
			}
		}

		for(int i=0;i<oprcount;i++){
			selectoperator[i]="";
		}

		sum = left[0];
		sumcount = 1;
		oprcount = 1;
		return sum.toString();

	}

}

