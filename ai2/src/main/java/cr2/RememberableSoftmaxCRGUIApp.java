package cr2;

import ai.INeuralNetwork;
import ai.NeuralNetService;
import ai.SoftmaxNeuralNetwork;
import javafx.scene.text.Font;

/**
 * DB(SQLite)にデータを記憶する文字認識アプリ
 * @author tkato
 *
 */
public class RememberableSoftmaxCRGUIApp extends AbstractCRGUIApp {

	private NeuralNetService service;

	@Override
	protected void initConst() {
		service = new NeuralNetService();
		inputs = service.getImageList();
		teaches = service.getTeachList();
	}

	public RememberableSoftmaxCRGUIApp() {
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
		answerLabel.setText(String.format("%.1f%%の確率で%d", output[maxIndex]*100, maxIndex));
		for ( int i=0; i<output.length; i++ ) {
			System.out.printf( "%d: %.4f%%\n", i, output[i]*100 );
		}
	}

	@Override
	protected void insertRecord(double[] inputArray, double[] teachArray) {
		service.register(inputArray, teachArray);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
