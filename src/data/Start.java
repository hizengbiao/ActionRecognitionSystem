package data;

import har.Labels;

import java.io.IOException;

import org.opencv.core.Core;

public class Start {
	public static void main(String args[]) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		ExtractAllVideos extA=new ExtractAllVideos();
		extA.exe();//提取所有视频特征
		
		/*MyTrain.loadTrainData();
		MyTrain.saveTrainDataTest();*/
		
		System.exit(0);
	}

}
