package data;

import har.Labels;

import java.io.File;
import java.io.IOException;

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

}
