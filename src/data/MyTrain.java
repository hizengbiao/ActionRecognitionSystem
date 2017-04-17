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

import Jama.Matrix;

public class MyTrain {
	
//	public static Mat data_mat;
	public static Mat data_mat;
	public static Mat res_mat;
	public static String data_hog_Address=Constants.VideoHogAddress;
	public static String data_hog_name="data_mat_hog.txt";

	public MyTrain() {
		// TODO Auto-generated constructor stub
	}
	public static void loadTrainData() throws NumberFormatException, IOException{
		
		File f = new File(data_hog_Address+data_hog_name);//读取路径
		if (!f.exists()) {
			System.out.println("数据不存在！");
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
                double value = (float) Double.parseDouble(word);
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
	}
	
	public static void saveTrainDataTest() throws IOException{
		//没用，只是用来测试Mat的读取是否正常
		if(data_mat==null){
			System.out.println("Mat is uninitialize!");
			return;
		}
		File f11 = MyTools.mkdir(MyTrain.data_hog_Address,"data_mat_hog_duplicate.txt");//保存路径
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
		
	}
	public void exe(){
//		data_mat.
	}

}
