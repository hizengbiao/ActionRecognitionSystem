package data;

public class MyConstants {
	
	 
    public final static String dataAddress = "data/";
    public final static String VideoHogAddress=dataAddress+"hog/";
    public final static String dataOfVideosAddress = dataAddress + "kthdata/";
    public final static String unLabeledVideosAddress = dataOfVideosAddress + "unlabeled/";
    public final static String unLabeledVideosHogAddress = VideoHogAddress + "unlabeled/";

    
    
    public static String allGradientDataOfVideosAddress = dataAddress
            + "kth_gradient_data/";
    public static String ThreadConflictMsg="已经有线程在启动，请终止后再尝试！";
    public static String S_Extract="提取特征";
    public static String S_Train  ="SVM训练";
    public static String S_co_Train  ="协同训练";
    public static String S_knn_Predict  ="KNN预测";
    public static String S_svm_Predict  ="SVM预测";
//    public static String S_Predict  ="预测";
    public static String S_Terminate="终止";
    public static String S_optionStatus="当前操作：   ";
    public static String S_videoPath="视频路径：    ";
    public static String S_videoName="视频文件名：  ";
    public static String S_frame="当前帧数：   ";
    
    public static String S_videoCapture="videoCapture";
    public static String S_Console="Console";
    
    public static int TrainVideoCount=40;
    public static int K=7;//KNN的K值
    public static int KNN_threshold=0;


}
