package data;

import java.awt.Dimension;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.video.Video;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Scalar;
//import org.opencv.core.Core;

import Jama.Matrix;

public class Start {
	public static void main(String args[]) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String videoFile = "data/2.mp4";
		ExtractVideoFeature ext=new ExtractVideoFeature();
		ext.exe(videoFile);

	}

}
