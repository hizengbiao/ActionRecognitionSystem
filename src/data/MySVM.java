package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import har.Constants;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

import Jama.Matrix;

public class MySVM {
	
//	public static Mat data_mat;
	private static Mat data_mat;
	private static Mat label_mat;
	private static CvSVM clasificador;
	public static String data_hog_Address=Constants.VideoHogAddress;
	public static String data_hog_name="data_mat_hog.txt";
	public static String data_hog_label="data_mat_hog_label.txt";
	public static String svm_modelAddress=Constants.dataAddress;
	public static String svm_modelName="svm_model.xml";
	
	public MySVM() {
		// TODO Auto-generated constructor stub
	}
	public static void loadTrainData() throws NumberFormatException, IOException{
		
		File f = new File(data_hog_Address+data_hog_name);//读取路径
		if (!f.exists()) {
			System.out.println("hog数据不存在！");
			return;
        }
		data_mat=new Mat();
		BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(data_hog_Address+data_hog_name)));
        String line = null;
        int cols=0;
        int k=0;
        while ((line = in.readLine()) != null) {        	
            String[] words = line.split("\t");
            if(k==0)
            	cols=words.length;
            if(cols!=words.length)
            	break;
            Mat aRow=new Mat(1,words.length,CvType.CV_32FC1);
//            float[]values=new float[words.length];
            for (k = 0; k < words.length; k++) {
                String word = words[k];
                double value = Float.parseFloat(word);
//                double value = (float) Double.parseDouble(word);
//                values[k]= (float) Double.parseDouble(word);
                aRow.put(0, k, value);
            }
//            Mat aRow=new Mat();
           /* MatOfFloat aRow=new MatOfFloat();
            aRow.fromArray(values);*/
            
//            System.out.println("k="+k+"   words.length="+words.length);
            data_mat.push_back(aRow);
        }
        in.close();  
        
        
//        读取label数据：
		File f2 = new File(data_hog_Address+data_hog_label);//读取label路径
		if (!f2.exists()) {
			System.out.println("label数据不存在！");
			return;
        }		
		label_mat=new Mat();		
		BufferedReader in2 = new BufferedReader(new InputStreamReader(
                new FileInputStream(data_hog_Address+data_hog_label)));
        String line2 = null;
        int k2=0;
        while ((line2 = in2.readLine()) != null) {        	
            String[] words = line2.split("\t");
            Mat aRow2=new Mat(1,words.length,CvType.CV_32FC1);
//            float[]values=new float[words.length];
            for (k2 = 0; k2 < words.length; k2++) {
                String word = words[k2];
                int value=Integer.parseInt(word);
                aRow2.put(0, k2, value);
            }
//            Mat aRow=new Mat();
           /* MatOfFloat aRow=new MatOfFloat();
            aRow.fromArray(values);*/
            
//            System.out.println("k="+k+"   words.length="+words.length);
            label_mat.push_back(aRow2);
        }
        in2.close();
        
	}
	
	public static void saveTrainDataTest() throws IOException{
		//没用，只是用来测试Mat的读取是否正常
		if(data_mat==null){
			System.out.println("Mat is uninitialize!");
			return;
		}
		File f11 = MyTools.mkdir(MySVM.data_hog_Address,"data_mat_hog_duplicate.txt");//保存路径
		FileWriter fw11=new FileWriter(f11);
		PrintWriter out3=new PrintWriter(new BufferedWriter(fw11));
//		System.out.println("data_mat.rows():"+data_mat.rows()+"  data_mat.cols():"+data_mat.cols());
		for(int j=0;j<data_mat.rows();j++){
			for(int k=0;k<data_mat.cols();k++){
				double[] va=data_mat.get(j, k);
				out3.print( (float)va[0]+ "\t");
			}
			
		/*float[] aLine = ((MatOfFloat)data_mat.row(j)).toArray();
//		System.out.println(hogOut1.length);//获取向量维数
		for (int i = 0; i < aLine.length; i++) {
			out3.print(aLine[i] + "\t");
		}*/
			
			
		out3.println();
		}
		out3.close();
		
		
		//存储label:
		if(label_mat==null){
			System.out.println("labelMat is uninitialize!");
			return;
		}
		File f22 = MyTools.mkdir(MySVM.data_hog_Address,"data_hog_label.txt");//保存路径
		FileWriter fw22=new FileWriter(f22);
		PrintWriter out22=new PrintWriter(new BufferedWriter(fw22));
//		System.out.println("data_mat.rows():"+data_mat.rows()+"  data_mat.cols():"+data_mat.cols());
		for(int j=0;j<label_mat.rows();j++){
			for(int k=0;k<label_mat.cols();k++){
				double[] va=label_mat.get(j, k);
				out22.print( (int)va[0]+ "\t");
			}
			
		/*float[] aLine = ((MatOfFloat)data_mat.row(j)).toArray();
//		System.out.println(hogOut1.length);//获取向量维数
		for (int i = 0; i < aLine.length; i++) {
			out3.print(aLine[i] + "\t");
		}*/
			
			
		out22.println();
		}
		out22.close();
		
	}
	
	
	public static void train(){
		
		CvSVMParams params = new CvSVMParams();
        params.set_kernel_type(CvSVM.LINEAR);
        clasificador = new CvSVM(data_mat, label_mat, new Mat(), new Mat(), params);
        MyTools.mkdir(svm_modelAddress, svm_modelName);
        clasificador.save(svm_modelAddress+svm_modelName);
		
		
		
//		CvSVM svm = new CvSVM();
//	    CvSVMParams param;    
//	    CvTermCriteria criteria;      
//	    criteria = new cvTermCriteria( CV_TERMCRIT_EPS, 1000, FLT_EPSILON );      
//	    param = new CvSVMParams( CvSVM.C_SVC, CvSVM.RBF, 10.0, 0.09, 1.0, 10.0, 0.5, 1.0, null, criteria );
//	    
////	    SVM种类：CvSVM::C_SVC     
////	    Kernel的种类：CvSVM::RBF     
////	    degree：10.0（此次不使用）     
////	    gamma：8.0     
////	    coef0：1.0（此次不使用）     
////	    C：10.0     
////	    nu：0.5（此次不使用）     
////	    p：0.1（此次不使用）     
////	    然后对训练数据正规化处理，并放在CvMat型的数组里。
//	    //SVM学习
//		svm.train(data_mat, label_mat, new Mat(), new Mat(), param);
//	    //利用训练数据和确定的学习参数,进行SVM学习
//	    svm.save( "E:/apple/SVM_DATA.xml" );
	    
	    
	}
	
	public static void predict(String viAdr){
		if(clasificador==null){
			clasificador=new CvSVM();
			System.out.println("load classificator...");
			clasificador.load(svm_modelAddress+svm_modelName);
		}			
		
		Mat features =ExtractVideoFeature.extract(viAdr);
		
		for(int i=0;i<features.rows();i++){
			float result=clasificador.predict(features.row(i));
			System.out.println(result);
		}
	}

}
