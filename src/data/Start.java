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
		String videoFile = "data/2.avi";

		VideoCapture capture = new VideoCapture();
		
//				 capture.open(0);//调取电脑的摄像头
		capture.open(videoFile);//读取本地文件

		if (!capture.isOpened()) {
			System.out.println("could not load video data...");
			return;
		}

		Mat prev = new Mat();
		Mat next = new Mat();
		Mat frame = new Mat();
		// boolean have = capture.read(next);

		int frame_width = (int) capture.get(3);
		int frame_height = (int) capture.get(4);
		
		ImageGUI gui = new ImageGUI();
		gui.createWin("OpenCV + Java视频读与播放演示", new Dimension(frame_width,
				frame_height));
		
		
		double frameCount = capture.get(7);
//		System.out.println(frameCount);
		int i = 0;
		while (true) {

			// System.out.println(i++);
			/*if (++i == 3)
				break;*/
			// prev=next;
			/*capture.read(frame);
			capture.read(frame);
			capture.read(frame);
			capture.read(frame);
			capture.read(frame);
			capture.read(frame);
			capture.read(frame);
			capture.read(frame);
			capture.read(frame);*/
			boolean have = capture.read(frame);

			// cvtColor(frame, next,CV_BGR2GRAY);
			Imgproc.cvtColor(frame, next, Imgproc.COLOR_RGB2GRAY);
			// Highgui.imwrite("data/delete/"+i+".jpg", next);
			// Core.flip(frame, frame, 1);// 翻转图像
			if (!have)
				break;
			if (!prev.empty()) {

				// Mat prev = Mat.ones(3, 3, CvType.CV_8UC1);
				// Mat next = Mat.ones(3, 3, CvType.CV_8UC1);
				Mat flow = new Mat();
				// Mat flow=new Mat(frame_width,frame_height,CvType.CV_8UC1);
				Video.calcOpticalFlowFarneback(prev, next, flow, 0.5, 1, 1, 1,
						7, 1.5, 1);
				// prevImg(y,x)=nextImg(y+flow(y,x)[1]，x+fow(y,x)[0]);
				Matrix result = new Matrix(frame_width, frame_height);
				double max = 50, min = -50;
				for (int ii = 0; ii < frame_height; ii++) {
					for (int jj = 0; jj < frame_width; jj++) {
						double[] data;
						data = flow.get(ii, jj);
						double xuandu = data[0] - data[1];
						result.set(jj, ii, xuandu);
						if (xuandu > max)
							max = xuandu;
						if (xuandu < min)
							min = xuandu;

						/*
						 * if (data == null) { System.out.print(ii + " " + jj);
						 * return; } else
						 *//*
							 * if (data[0] > 0.01|| data[1] > 0.01) {
							 * System.out.print(data[0] + "  " + data[1]);
							 * System.out.println(); }
							 */
					}

				}
//				System.out.println(max + "  " + min);
				double max_border = max * 0.5;
				double min_border = min * 0.5;
				
				
				
				System.out.println("max_border:"+max_border+"  min_border:"+min_border);

				
				Vector<Point>  v1= new Vector<Point> ();
				
				

				for (int ii = 0; ii < frame_height; ii++) {
					for (int jj = 0; jj < frame_width; jj++) {
						if((result.get(jj, ii)>max_border)||(result.get(jj, ii)<min_border))
							
						v1.addElement(new Point(jj,ii)); 
//						System.out.println(ii+"  "+jj);
					}
				}
				double meanX=0;
				double meanY=0;
				for(int ii=0;ii<v1.size();ii++){
					meanX+=v1.get(ii).x;
					meanY+=v1.get(ii).y;
				}
				meanX/=v1.size();
				meanY/=v1.size();
				
				
				
				Mat paintPoint=frame.clone();
				int scale=56;
				Rect fanwei=new Rect((int)(meanX-scale/2),(int)(meanY-scale/2),scale,scale);
				if(v1.size()>10){
				Core.rectangle(paintPoint, fanwei.tl(), fanwei.br(),  new Scalar(255, 0, 0),2);
				Core.circle(paintPoint, new Point(meanX,meanY), (int)1, new Scalar(0, 255, 0),2);
				}
//				System.out.println(v1.size());
				for(int m=0;m<v1.size();m++){
					 Core.circle(paintPoint,v1.get(m),(int) 1,new Scalar(0, 0, 255),2);
				}
				gui.imshow(MyVideo.conver2Image(paintPoint));
				gui.repaint();

				// System.out.println(flow.width());
				// System.out.println(flow.height());
				// Mat motion2color = new Mat();
				// motionToColor(flow, motion2color);
				// Highgui.imwrite("data/delete/"+i+".jpg", flow);

			}
			prev = next.clone();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	/*
	 * private static void motionToColor(Mat flow, Mat color) { // TODO
	 * Auto-generated method stub if (color.empty()) color.create(flow.rows(),
	 * flow.cols(), CvType.CV_8UC3);
	 * 
	 * Vector<Scalar> colorwheel; //Scalar r,g,b if (colorwheel.isEmpty())
	 * makecolorwheel(colorwheel);
	 * 
	 * // determine motion range: float maxrad = -1;
	 * 
	 * // Find max flow to normalize fx and fy for (int i= 0; i < flow.rows();
	 * ++i) { for (int j = 0; j < flow.cols(); ++j) { cv.Vec2f flow_at_point =
	 * flow.at(i, j); float fx = flow_at_point[0]; float fy = flow_at_point[1];
	 * if ((fabs(fx) > UNKNOWN_FLOW_THRESH) || (fabs(fy) > UNKNOWN_FLOW_THRESH))
	 * continue; float rad = sqrt(fx * fx + fy * fy); maxrad = maxrad > rad ?
	 * maxrad : rad; } }
	 * 
	 * for (int i= 0; i < flow.rows; ++i) { for (int j = 0; j < flow.cols; ++j)
	 * { uchar *data = color.data + color.step[0] * i + color.step[1] * j; Vec2f
	 * flow_at_point = flow.at(i, j);
	 * 
	 * float fx = flow_at_point[0] / maxrad; float fy = flow_at_point[1] /
	 * maxrad; if ((fabs(fx) > UNKNOWN_FLOW_THRESH) || (fabs(fy) >
	 * UNKNOWN_FLOW_THRESH)) { data[0] = data[1] = data[2] = 0; continue; }
	 * float rad = sqrt(fx * fx + fy * fy);
	 * 
	 * float angle = atan2(-fy, -fx) / CV_PI; float fk = (angle + 1.0) / 2.0 *
	 * (colorwheel.size()-1); int k0 = (int)fk; int k1 = (k0 + 1) %
	 * colorwheel.size(); float f = fk - k0; //f = 0; // uncomment to see
	 * original color wheel
	 * 
	 * for (int b = 0; b < 3; b++) { float col0 = colorwheel[k0][b] / 255.0;
	 * float col1 = colorwheel[k1][b] / 255.0; float col = (1 - f) * col0 + f *
	 * col1; if (rad <= 1) col = 1 - rad * (1 - col); // increase saturation
	 * with radius else col *= .75; // out of range data[2 - b] = (int)(255.0 *
	 * col); } } } }
	 * 
	 * private static void makecolorwheel(Vector<Scalar> colorwheel) { // TODO
	 * Auto-generated method stub int RY = 15; int YG = 6; int GC = 4; int CB =
	 * 11; int BM = 13; int MR = 6;
	 * 
	 * int i;
	 * 
	 * for (i = 0; i < RY; i++) colorwheel.push_back(new Scalar(255, 255*i/RY,
	 * 0)); for (i = 0; i < YG; i++) colorwheel.push_back(Scalar(255-255*i/YG,
	 * 255, 0)); for (i = 0; i < GC; i++) colorwheel.push_back(Scalar(0, 255,
	 * 255*i/GC)); for (i = 0; i < CB; i++) colorwheel.push_back(Scalar(0,
	 * 255-255*i/CB, 255)); for (i = 0; i < BM; i++)
	 * colorwheel.push_back(Scalar(255*i/BM, 0, 255)); for (i = 0; i < MR; i++)
	 * colorwheel.push_back(Scalar(255, 0, 255-255*i/MR)); }
	 */

}
