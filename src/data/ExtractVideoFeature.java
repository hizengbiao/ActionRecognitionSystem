package data;

import java.awt.Dimension;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import Jama.Matrix;

public class ExtractVideoFeature {
	VideoCapture capture;
	int frame_width ;
	int frame_height;
	double frameCount ;
	
	public ExtractVideoFeature() {
		capture = new VideoCapture();		
	}
	
	public void exe(String videoFile){
		
//		capture.open(videoFile);//读取本地文件
		capture.open(0);//调取电脑的摄像头

		if (!capture.isOpened()) {
			System.out.println("could not load video data...");
			return;
		}
		frame_width = (int) capture.get(3);
		frame_height = (int) capture.get(4);
		frameCount = capture.get(7);
		Mat prev = new Mat();
		Mat next = new Mat();
		Mat frame = new Mat();
		ImageGUI gui = new ImageGUI();
		gui.createWin("OpenCV + Java视频读与播放演示", new Dimension(frame_width,
				frame_height));
		
		while(true){
			
			boolean have = capture.read(frame);
			Imgproc.cvtColor(frame, next, Imgproc.COLOR_RGB2GRAY);
			if (!have)
				break;
			if (!prev.empty()){

				System.out.println("test");
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
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	
	
	
	


}
