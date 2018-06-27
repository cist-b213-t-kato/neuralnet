package myai;

public interface INeuralNetwork {
	public double[] compute( double[] in );
	public void learn( double[][] ins, double[][] ts );
}
