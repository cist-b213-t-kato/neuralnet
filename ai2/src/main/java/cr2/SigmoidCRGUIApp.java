package cr2;

import ai.INeuralNetwork;
import ai.SoftmaxNeuralNetwork;
import javafx.scene.text.Font;

/**
 * シグモイド関数を使った手書き文字認識アプリ
 * @author tkato
 *
 */
public class SigmoidCRGUIApp extends AbstractCRGUIApp {

	public SigmoidCRGUIApp() {
		System.out.println("データベースの件数: " + inputs.size());
		for ( int i=0; i<inputs.size(); i++ ) {
			drawInput(inputs.get(i));
			System.out.println("教師信号: " + calcMaxIndex(teaches.get(i)));
			System.out.println();
		}
	}

	@Override
	protected INeuralNetwork createNeuralNetwork() {
		return new SoftmaxNeuralNetwork(100, 100, 10, 0.1);
	}

	@Override
	protected void writingAreaOnMouseReleased() {
		double[] doubleArray = new double[yMax*xMax];
		for ( int i=0; i<yMax; i++ ) {
			for ( int j=0; j<xMax; j++ ) {
				doubleArray[xMax*i+j] = displayArray[xMax*i+j];
			}
		}
		double[] output = nn.compute(doubleArray);
		int maxIndex = calcMaxIndex(output);
		answerLabel.setFont(new Font("Arial", 20));
		answerLabel.setText(String.format("%d", maxIndex));
		for ( int i=0; i<output.length; i++ ) {
			System.out.printf( "%d: %.4f\n", i, output[i] );
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
