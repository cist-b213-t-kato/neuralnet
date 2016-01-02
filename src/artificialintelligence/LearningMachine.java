package artificialintelligence;
//LearningMachine.java
public interface LearningMachine {
    //学習
    void learn(int cls, double[] data);
    //評価
    int trial(double[] data);
}