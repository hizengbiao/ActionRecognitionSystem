package utils;

import har.Labels;
import har.Constants;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import Jama.Matrix;

public class SVM implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1377140616756384189L;
    public static String LOGGER_ADDRESS = "logs/svm/";
    private svm_model svmModel;
    private transient PrintWriter logger;

    public SVM(Matrix bagOfWordsData) {
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
        logger.println("==SVM==");

        int data_features_count = bagOfWordsData.getColumnDimension() - 1;
        svm_problem svmProblem = new svm_problem();
        int dataCount = bagOfWordsData.getRowDimension();
        svmProblem.l = dataCount;
        svmProblem.y = new double[dataCount];
        svmProblem.x = new svm_node[dataCount][data_features_count];
        logger.println("Number of Rows: " + dataCount);
        for (int i = 0; i < dataCount; i++) {
            for (int j = 0; j < data_features_count; j++) {
                svm_node node = new svm_node();
                node.index = j + 1;
                node.value = bagOfWordsData.get(i, j);
                svmProblem.x[i][j] = node;
            }
            svmProblem.y[i] = bagOfWordsData.get(i, data_features_count);
        }
        svm_parameter svmParameter = new svm_parameter();
        svmParameter.probability = 1;
        svmParameter.gamma = 0.01; // (1 / number_of_features)
        svmParameter.nu = 0.5;
        svmParameter.C = 1;
        svmParameter.svm_type = svm_parameter.C_SVC;
        svmParameter.kernel_type = svm_parameter.LINEAR;
        svmParameter.cache_size = 20000;
        svmParameter.eps = 0.001;
        logger.println("probability: " + svmParameter.probability);
        logger.println("gamma: " + svmParameter.gamma);
        logger.println("nu: " + svmParameter.nu);
        logger.println("C: " + svmParameter.C);
        logger.println("svm_type: " + svmParameter.svm_type);
        logger.println("kernel_type: " + svmParameter.kernel_type);
        logger.println("cache_size: " + svmParameter.cache_size);
        logger.println("eps: " + svmParameter.eps);
        logger.println(Constants.LOGGER_SEPERATOR);
        this.svmModel = svm.svm_train(svmProblem, svmParameter);
    }

    public Labels evaluate(Matrix m) {
        svm_node[] nodes = new svm_node[m.getColumnDimension()];
        for (int j = 0; j < m.getColumnDimension(); j++) {
            svm_node node = new svm_node();
            node.index = j + 1;
            node.value = m.get(0, j);
            nodes[j] = node;
        }
        int label = (int) svm.svm_predict(svmModel, nodes);
        logger.println(label);
        return Labels.values()[label];
    }

    public void closeLogger() {
        logger.println(Constants.LOGGER_SEPERATOR);
        logger.println("Closing SVM Logger");
        logger.println(Constants.LOGGER_SEPERATOR);
        logger.close();
    }
}
