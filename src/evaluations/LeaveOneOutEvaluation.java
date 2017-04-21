package evaluations;

import java.util.TreeMap;

import har.Labels;
import io.Database;
import models.Model;

public class LeaveOneOutEvaluation extends SimpleEvaluation {
    private double[] accuracy;

    public LeaveOneOutEvaluation(Model model) {
        super(model);
        accuracy = new double[25];
        for (int personNumber = 1; personNumber <= 25; personNumber++) {
            TreeMap<Integer, Labels>[] datasets = Database
                    .leaveOneOutSelect(personNumber);
            TreeMap<Integer, Labels> trainSet = datasets[0];
            TreeMap<Integer, Labels> testSet = datasets[1];
            model.train(trainSet);
            model.save("logs/models/" + (personNumber - 1) + ".model");
            accuracy[personNumber - 1] = this.evaluate(testSet);
        }
    }

    public double meanAccuracy() {
        double meanAccuracy = 0;
        for (double d : accuracy) {
            meanAccuracy += d;
        }
        meanAccuracy = meanAccuracy / accuracy.length;
        return meanAccuracy;
    }

    public double[] getAccuracy() {
        return this.accuracy;
    }
}
