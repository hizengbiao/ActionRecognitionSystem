package start;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.opencv.core.Core;

import utils.FileChooser;
import utils.FileCopy;
import utils.MyTools;

import data.Classifiers;
import data.MainWindow;

public class Start {
	public static void main(String args[]) throws IOException {
//		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//		System.load("E:\\workspace\\android\\ActionRecognitionSystem\\opencv_java249.dll");
//		System.load("E:\\workspace\\android\\ActionRecognitionSystem\\opencv_ffmpeg249_64.dll");
		
//		URL url = this.getClass().getResource("/"); 
//		String path = url.getFile();
//		
		File f = new File("opencv_java249.dll");
		File f1=MyTools.mkdir("C:\\ActionRecognitionSystemRely\\", "opencv_java249.dll");
		FileCopy.Copy(f, f1);
		
		f = new File("opencv_ffmpeg249_64.dll");
		f1=MyTools.mkdir("C:\\ActionRecognitionSystemRely\\", "opencv_ffmpeg249_64.dll");
		FileCopy.Copy(f, f1);
		
		f = new File("images\\bg.jpg");
		f1=MyTools.mkdir("C:\\ActionRecognitionSystemRely\\", "bg.jpg");
		FileCopy.Copy(f, f1);
		
		System.load("C:\\ActionRecognitionSystemRely\\opencv_java249.dll");
		System.load("C:\\ActionRecognitionSystemRely\\opencv_ffmpeg249_64.dll");
		
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
