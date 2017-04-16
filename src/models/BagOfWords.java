package models;

import har.Labels;
import har.Constants;
import io.Database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.TreeMap;

import utils.KMeans;
import utils.PCA;
import utils.SVM;
import Jama.Matrix;

public class BagOfWords extends Model {
    /**
     * 
     */
    private static final long serialVersionUID = -2933777412600001083L;
    public static String LOGGER_ADDRESS = "logs/bag_of_words/";
    public static int DIMENSION = 100;
    public static int NUMBER_OF_DICTIONARY_WORDS = 1000;
    private PCA pca;
    private KMeans kmeans;
    private SVM svm;
    private transient PrintWriter logger;

    public BagOfWords() {
        File loggers = new File(LOGGER_ADDRESS);
        int counter = loggers.list().length;
        String loggerAddress = LOGGER_ADDRESS + counter
                + Constants.LOGGER_FILES_POSTFIX;
        try {
            logger = new PrintWriter(new FileOutputStream(new File(
                    loggerAddress)), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void train(TreeMap<Integer, Labels> trainSet) {
        logger.println("Videos for Training: ");
        for (int videoNumber : trainSet.keySet()) {
            logger.println(videoNumber);
        }
        logger.println(Constants.LOGGER_SEPERATOR);

        Matrix data = Database.readDataOfSomeVideos(trainSet.keySet());

        long now = System.currentTimeMillis();
        PCA pca = new PCA(data, DIMENSION);
        this.pca = pca;
        data = pca.transform(data);

        long timeForPCADataTransformation = System.currentTimeMillis() - now;
        logger.println("Time for Transform Train Data: "
                + timeForPCADataTransformation);
        logger.println("Transformed Train Data: ");
        // data.print(logger, 4, 4);
        logger.println(Constants.LOGGER_SEPERATOR);

        KMeans kmeans = new KMeans(data, NUMBER_OF_DICTIONARY_WORDS);
        this.kmeans = kmeans;
        Matrix bagOfWordsData = new Matrix(trainSet.size(),
                NUMBER_OF_DICTIONARY_WORDS + 1);//最后一列用来存Label的序号
        int counter = 0;
        for (int i : trainSet.keySet()) {
            Matrix videoBagOfWord = convertVideoToBagOfWords(i);
            for (int j = 0; j < NUMBER_OF_DICTIONARY_WORDS; j++) {
                bagOfWordsData.set(counter, j, videoBagOfWord.get(0, j));
            }
            int classNumber = trainSet.get(i).ordinal();
            bagOfWordsData
                    .set(counter, NUMBER_OF_DICTIONARY_WORDS, classNumber);
            counter++;
        }

        logger.println("Bag of Words for Train Data Set: ");
        bagOfWordsData.print(logger, 4, 1);
        logger.println(Constants.LOGGER_FILES_POSTFIX);

        this.svm = new SVM(bagOfWordsData);
    }

    private Matrix convertVideoToBagOfWords(int videoNumber) {
        Object[] o = Database.getClassAndVideoNumber(videoNumber);
        Labels c = (Labels) o[0];
        videoNumber = (int) o[1];
        try {
            Matrix videoGradientData = Database.readGradientDataOfAVideo(c,
                    videoNumber);
            return convertVideoToBagOfWords(videoGradientData);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    private Matrix convertVideoToBagOfWords(Matrix videoGradientData) {    	
        int videoWordsNumber = videoGradientData.getRowDimension();
        Matrix data = pca.transform(videoGradientData);
        Matrix bagOfWords = new Matrix(1, NUMBER_OF_DICTIONARY_WORDS);
        for (int i = 0; i < videoWordsNumber; i++) {
            int word = kmeans.findNearest(new Matrix(data.getMatrix(i, i, 0,
                    DIMENSION - 1).getArray()));
            bagOfWords.set(0, word, bagOfWords.get(0, word) + 1);
        }
        return bagOfWords;
    }

    @Override
    public Labels test(int videoNumber) {
        Object[] o = Database.getClassAndVideoNumber(videoNumber);
        Labels c = (Labels) o[0];
        videoNumber = (int) o[1];
        try {
            Matrix videoGradientData = Database.readGradientDataOfAVideo(c,
                    videoNumber);
            return test(videoGradientData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Labels test(Matrix videoGradientData) {
        Matrix m = convertVideoToBagOfWords(videoGradientData);
        return svm.evaluate(m);
    }

    public PCA getPCA() {
        return this.pca;
    }

    @Override
    public void save(String address) {
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(
                    new FileOutputStream(new File(address)));
            oos.writeObject(this);
            oos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public KMeans getKMeans() {
        return this.kmeans;
    }

    public void closeLogger() {
        this.pca.closeLogger();
        this.kmeans.closeLogger();
        this.svm.closeLogger();
        logger.println(Constants.LOGGER_SEPERATOR);
        logger.println("Closing Bag of Words Logger");
        logger.println(Constants.LOGGER_SEPERATOR);
        logger.close();
    }
}
