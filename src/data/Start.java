package data;

import java.io.IOException;

import org.opencv.core.Core;

public class Start {
	public static void main(String args[]) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String videoFile = "data/2.avi";
		ExtractVideoFeature ext=new ExtractVideoFeature();
		ext.exe(videoFile);

	}

}
