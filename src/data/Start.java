package data;

import har.Constants;
import har.Labels;

import java.io.IOException;

import org.opencv.core.Core;

public class Start {
	public static void main(String args[]) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		/*ExtractAllVideos extA=new ExtractAllVideos();
		extA.exe();//提取所有视频特征
*/		
		/*MySVM.loadTrainData();
//		MySVM.saveTrainDataTest();
		MySVM.train();*/
		
		Labels c=Labels.BOXING;
		int i=1;
		String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
		MySVM.predict(videoAddress);
		
		System.exit(0);
	}

}
