package data;

import har.Labels;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Mat;

public class MyTools {
	/*static int pauseTime=600000;
	static int preSpeed;*/
	public static boolean pause=false;

	public MyTools() {
		// TODO Auto-generated constructor stub
	}
	
	public static File mkdir(String dir,String fileName){
		File f1 = new File(dir);
		File f = new File(dir+fileName);//保存路径
		if (!f1.exists()) {
			f1.mkdirs();
        }  
		if (!f.exists()) {  
//			System.out.print("文件不存在");
            try {
				f.createNewFile();// 不存在则创建
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return f;
	}
	
	public static void speedUp(){
		if(ExtractVideoFeature.speed<=100)
			ExtractVideoFeature.speed-=30;
		else if(ExtractVideoFeature.speed>=500)
			ExtractVideoFeature.speed-=200;
		else{
			ExtractVideoFeature.speed-=100;
			if(ExtractVideoFeature.speed<100)
				ExtractVideoFeature.speed=100;
		}
			
		if(ExtractVideoFeature.speed<0)
			ExtractVideoFeature.speed=0;
		MyTools.showTips("当前播放间隔："+ExtractVideoFeature.speed+"us/帧",1);
	}
	public static void videoPlay(){
		pause=false;
	}
	public static void videoPause(){		
			pause=true;
	}
	
	public static boolean isPause(){
		return pause;
	}
	
	public static void speedDown(){
		if(ExtractVideoFeature.speed<=100)
			ExtractVideoFeature.speed+=30;
		else if(ExtractVideoFeature.speed>=500)
			ExtractVideoFeature.speed+=200;
		else
			ExtractVideoFeature.speed+=100;
		
//		ExtractVideoFeature.speed+=100;
		if(ExtractVideoFeature.speed>1000)
			ExtractVideoFeature.speed=1000;
		MyTools.showTips("当前播放间隔："+ExtractVideoFeature.speed+"us/帧",1);
	}
	
	public static void clearTips(){
		MainWindow.tips.setText("\n");
	}
	
	public static void showTips(String s,int sign){
		if(sign==1){
			MainWindow.tips.append("\n "+s+"\n");
		}		
	}
	
	public static void showTips(String s){
		MainWindow.tips.append("      "+s+"\n");
	}

	public static void saveFeaturesToText(Mat features,Labels c,int num,PrintWriter outAll,PrintWriter outAll_label) throws IOException {
		String hogDirAddress=MyConstants.VideoHogAddress+c.getName()+"/";
		String hogFileAddress=c.getName()+"_"+num+"hog.txt";
		
		File f=MyTools.mkdir(hogDirAddress,hogFileAddress);
		FileWriter fw=new FileWriter(f);
		PrintWriter out=new PrintWriter(new BufferedWriter(fw));
		
		for(int i=0;i<features.rows();i++){
			for(int k=0;k<features.cols();k++){
				double[] va=features.get(i, k);
				float value=(float)va[0];
				out.print(value + "\t");
				outAll.print(value + "\t");
			}
			out.println();
			outAll.println();
			outAll_label.println(c.ordinal());
		}
		out.close();		
	}

}
