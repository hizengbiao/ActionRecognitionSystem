package utils;

import har.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import Jama.Matrix;

public class KMeans implements Serializable {
    static String LOGGER_ADDRESS = "logs/kmeans/";
    static int MIN_VALUE = -1000;
    static int MAX_VALUE = 1000;
    static double THRESHOLD = 100;
    private int n;
    private int m;
    private int k;
    private Matrix centroids;
    private int[] groups;
    private transient PrintWriter logger;

    public KMeans(Matrix data, int k) {
        File loggers = new File(LOGGER_ADDRESS);
        int fileNumber = loggers.list().length;
        String loggerAddress = LOGGER_ADDRESS + fileNumber
                + Constants.LOGGER_FILES_POSTFIX;
        try {
            logger = new PrintWriter(new FileOutputStream(new File(
                    loggerAddress)), true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        this.n = data.getRowDimension();
        this.m = data.getColumnDimension();
        this.k = k;
        this.centroids = initializeCentroids(data);
        this.groups = new int[n];
        int counter = 0;
        while (counter < 1000) {
        	logger.println(Constants.LOGGER_SEPERATOR);
            // Store previous centroids to calculate the change
            Matrix previousCentroids = new Matrix(centroids.getArrayCopy());

            // The main loop of k-means
            calculateDistances(data);
            calculateCentroids(data);

            // Calculate the change of centroids in comparison with previous
            // centroids
            // System.out.println(centroids);
            Matrix difference = previousCentroids.minus(centroids);
            double delta = difference.normF();//计算所有值的平方各的2次方根
            logger.println("Iteration number: " + (counter + 1) + " delta: "
                    + delta);
            if (delta < THRESHOLD) {
                logger.println("K-means converged.");
                logger.println("Centroids: ");
                centroids.print(logger, 4, 4);
                logger.println(Constants.LOGGER_SEPERATOR);
                break;
            }
            counter++;
            
        }
      //  logger.println(Constants.LOGGER_SEPERATOR);
    }

    private Matrix initializeCentroids() {
        Matrix mat = new Matrix(k, m);
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < m; j++) {
                double randValue = MIN_VALUE + (MAX_VALUE - MIN_VALUE)
                        * rand.nextDouble();
                mat.set(i, j, randValue);
            }
        }
        return mat;
    }

    private Matrix initializeCentroids(Matrix data) {
        int[] randomRows = new int[k];
        // Select k random numbers from 1 to n
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = i;
        }
        ArraysFunctions.shuffle(numbers);
        for (int i = 0; i < k; i++) {
            randomRows[i] = numbers[i];
        }
        Matrix mat = new Matrix(data.getMatrix(randomRows, 0, m - 1).getArray());
        return mat;
    }

    private void calculateDistances(Matrix data) {
        for (int i = 0; i < n; i++) {
            Matrix row = new Matrix(data.getMatrix(i, i, 0, m - 1).getArray());
            groups[i] = findNearest(row);
        }
    }

    public int findNearest(Matrix row) {
        double minValue = Double.POSITIVE_INFINITY;
        int minIndex = -1;
        for (int i = 0; i < k; i++) {
            double dist = calDistance(row, i);
            if (dist < minValue) {
                minValue = dist;
                minIndex = i;
            }
        }
        return minIndex;
    }

    private double calDistance(Matrix row, int centroidNumber) {
        double dist = 0;
        for (int i = 0; i < m; i++) {
            double d = row.get(0, i) - centroids.get(centroidNumber, i);
            dist += d * d;
        }
        return dist;
    }

    private void calculateCentroids(Matrix data) {
        int[] groupCounter = new int[k];
        for (int i = 0; i < n; i++) {
            if (groupCounter[groups[i]] == 0) {
                for (int j = 0; j < m; j++) {
                    centroids.set(groups[i], j, data.get(i, j));
                }
            } else {
                for (int j = 0; j < m; j++) {
                    centroids.set(groups[i], j, centroids.get(groups[i], j)
                            + data.get(i, j));
                }
            }
            groupCounter[groups[i]] = groupCounter[groups[i]] + 1;
        }
        for (int i = 0; i < k; i++) {
            if (groupCounter[i] != 0) {
                logger.println("Group " + i + " contains " + groupCounter[i]
                        + " elements.");
                for (int j = 0; j < m; j++) {
                    centroids.set(i, j, centroids.get(i, j) / groupCounter[i]);
                }
            } else {
                logger.println("Warning: centroid is empty");
            }
        }
        
    }

    public void closeLogger() {
        logger.println(Constants.LOGGER_SEPERATOR);
        logger.println("Closing K-means Logger");
        logger.println(Constants.LOGGER_SEPERATOR);
        logger.close();
    }

    public static void main(String[] args) {
        System.out.println("DATA: ");
        int N = 10;
        Matrix data = new Matrix(N, 2);
        for (int i = 0; i < N; i++) {
            if (i < (N / 2)) {
                data.set(i, 0, -100);
            } else {
                data.set(i, 0, 100);
            }
            data.set(i, 1, i);
        }
        /*data.set(N-1,0,-100);
        data.set(N-1,1,3);*/
        
        data.print(8, 2);
        KMeans kMeans = new KMeans(data, 2);
        System.out.println("Centroids: ");
        kMeans.centroids.print(8, 2);
        for (int i = 0; i < N; i++) {
            Matrix point = data.getMatrix(i, i, 0, 1);
            System.out.println("The point ");
            point.print(4, 2);
            int nearest = kMeans.findNearest(point);
            System.out.println("Cluster: " + nearest);
        }
    }
}
