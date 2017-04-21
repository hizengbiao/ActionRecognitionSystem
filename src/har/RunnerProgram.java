package har;

import io.Database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import models.BagOfWords;
import models.Model;
import utils.ApprioriAll;
import utils.KMeans;
import utils.PCA;
import Jama.Matrix;
import evaluations.LeaveOneOutEvaluation;
import evaluations.SimpleEvaluation;

public class RunnerProgram {
	static boolean extract_all_videos_gradient_data = true;
	static String allGradeintDataFileAddress = Constants.allGradientDataOfVideosAddress;

	static boolean visualizeFeaturesAndGradient = false;
	static boolean visualizeAllWords = false;
	static Labels videoToVisualizeClass = Labels.BOXING;
	static int videoNumberToVisualizeInClass = 1;
	static int wordNumberInVideoToVisualize = 6;
	static int frameToShow = 1;

	static boolean trainTheModel = false;
	static boolean simpleEvaluation = false;
	static double trainPercent = 0.9;

	static boolean leaveOneOutEvaluation = false;

	static boolean checkPCAWords = false;

	static boolean checkKMeans = false;
	static int kmeansCenterWordNumber = 478;

	// I should check what is going on in my model.
	// I call all of my works as postProcess.
	static boolean postProcess = true;

	public static void main(String[] args) {
        if (extract_all_videos_gradient_data) {
            Database.saveAllVideosGradientData();
        }
        if (simpleEvaluation) {
            // TreeMap<Integer, Classes>[] datasets = ReadData
            // .randomSelect(trainPercent);
            TreeMap<Integer, Labels>[] datasets = Database
                    .randomSelect(trainPercent);
            TreeMap<Integer, Labels> trainSet = datasets[0];
            TreeMap<Integer, Labels> testSet = datasets[1];
            Model m = null;
            if (trainTheModel) {
                m = new BagOfWords();
                m.train(trainSet);
                m.save("bagOfWords.model");
            } else {
                m = (BagOfWords) (Model.load("bagOfWords.model"));
            }
            SimpleEvaluation evaluation = new SimpleEvaluation(m);
            double accuracy = evaluation.evaluate(testSet);
            ((BagOfWords) m).closeLogger();
            System.out.println(accuracy);
        }
        if (leaveOneOutEvaluation) {
            long curTime = System.currentTimeMillis();
            Model m = new BagOfWords();
            LeaveOneOutEvaluation evaluation = new LeaveOneOutEvaluation(m);
            for (double d : evaluation.getAccuracy()) {
                System.out.println(d);
            }
            System.out.println("The mean accuracy is: "
                    + evaluation.meanAccuracy());
            long finishTime = System.currentTimeMillis() - curTime;
            System.out.println("Time to finish: " + finishTime);
        }
        if (visualizeFeaturesAndGradient) {
            Matrix[] word;
            Matrix[] wordGradient;
            try {
                word = Database.readAWordOfVideo(videoToVisualizeClass,
                        videoNumberToVisualizeInClass,
                        wordNumberInVideoToVisualize);
                wordGradient = Database.readGradientDataOfVideoWord(
                        videoToVisualizeClass, videoNumberToVisualizeInClass,
                        wordNumberInVideoToVisualize);
                if (visualizeAllWords) {
                    DrawVideosDataPoint.drawAllWords(word);
                    DrawVideosDataPoint.drawAllWords(wordGradient);
                } else {
                    DrawVideosDataPoint.draw(word, frameToShow);
                    DrawVideosDataPoint.draw(wordGradient, frameToShow);
                }

                if (checkPCAWords) {
                	BagOfWords bagOfWords = (BagOfWords) (Model
                            .load("bagOfWords.model"));
                    PCA pca = bagOfWords.getPCA();
                    Matrix videoGradient;
                    try {
                        videoGradient = Database.readGradientDataOfAVideo(
                                videoToVisualizeClass,
                                videoNumberToVisualizeInClass);
                        Matrix videoGradientWord = videoGradient.getMatrix(
                                wordNumberInVideoToVisualize,
                                wordNumberInVideoToVisualize, 0, 1689);
                        Matrix transformedVideoGradientWord = pca
                                .transform(videoGradientWord);
                        Matrix[] videoGradientWordPicture = new Matrix[] { transformedVideoGradientWord };
                        DrawVideosDataPoint.draw(videoGradientWordPicture, 0);
                    } catch (NumberFormatException | IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (checkKMeans) {
            Matrix[] word;
            Matrix[] wordGradient;
            Matrix videoGradient;
            BagOfWords bagOfWords = (BagOfWords) (Model
                    .load("bagOfWords.model"));
            PCA pca = bagOfWords.getPCA();
            KMeans kMeans = bagOfWords.getKMeans();
            for (Labels c : Labels.values()) {
                for (int i = 1; i <= c.getNumberOfVideos(); i++) {
                    try {
                        videoGradient = Database.readGradientDataOfAVideo(c, i);
                        for (int j = 0; j < 200; j++) {
                            Matrix videoGradientWord = videoGradient.getMatrix(
                                    j, j, 0, 1689);
                            Matrix transformedVideoGradientWord = pca
                                    .transform(videoGradientWord);
                            int nearest = kMeans
                                    .findNearest(transformedVideoGradientWord);
                            System.out.println("Class: " + c.getName()
                                    + " Video: " + i + " nearest: " + nearest);
                            if (nearest == kmeansCenterWordNumber) {
                                word = Database.readAWordOfVideo(c, i, j + 1);
                                wordGradient = Database
                                        .readGradientDataOfVideoWord(c, i,
                                                j + 1);
                                JOptionPane.showMessageDialog(null, "class: "
                                        + c.getName() + " video: " + i
                                        + " word: " + (j + 1));
                                DrawVideosDataPoint.drawAllWords(word);
                                DrawVideosDataPoint.drawAllWords(wordGradient);
                            }
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        if (postProcess) {
            BagOfWords bagOfWords = (BagOfWords) BagOfWords
                    .load("logs/models/0.model");
            Matrix postProcessData = new Matrix(599, 200);
            int rowCounter = 0;
            for (Labels label : Labels.values()) {
                for (int videoNumber = 1; videoNumber <= label
                        .getNumberOfVideos(); videoNumber++) {
                    try {
                        Matrix gradientFeatures = Database
                                .readGradientDataOfAVideo(label, videoNumber);
                        Matrix pCATransformedData = bagOfWords.getPCA()
                                .transform(gradientFeatures);
                        for (int i = 0; i < pCATransformedData
                                .getRowDimension(); i++) {
                            Matrix row = pCATransformedData
                                    .getMatrix(i, i, 0, pCATransformedData
                                            .getColumnDimension() - 1);
                            int word = bagOfWords.getKMeans().findNearest(row);
                            postProcessData.set(rowCounter, i, word);
                            System.out.print(word);
                            System.out.print(", ");
                        }
                        rowCounter++;
                        System.out.println();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
//            ApprioriAll apprioirAll = new ApprioriAll(postProcessData);
        }
    }
}
