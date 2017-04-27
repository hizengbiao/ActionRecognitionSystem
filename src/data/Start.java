package data;


import java.io.IOException;

import org.opencv.core.Core;

public class Start {
	public static void main(String args[]) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		new MainWindow().lanuchWindow();
		
/*		//提取所有视频特征：
		ExtractAllVideos extA=new ExtractAllVideos();
		extA.exe();
		
//		训练：
		MySVM.loadTrainData();
//		MySVM.saveTrainDataTest();
		MySVM.train();*/
		
/*//		预测：
		Labels c=Labels.HANDWAVING;
		int i=12;
		String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
		MySVM.predict(videoAddress);*/
		
//		System.exit(0);
	}

}
