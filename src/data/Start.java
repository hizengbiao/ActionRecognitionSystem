package data;

import org.opencv.core.Core;

public class Start {
	public static void main(String args[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String videoFile = "data/2.avi";
		ExtractVideoFeature ext=new ExtractVideoFeature();
		ext.exe(videoFile);

	}

}
