package data;

import har.Constants;
import har.Labels;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import org.opencv.objdetect.HOGDescriptor;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import Jama.Matrix;

public class ExtractVideoFeature {

	// int frame_width;
	// int frame_height;
	// double frameCount;
	static double xuandu_max = 40;// 旋度最大值，若为正，则最小为50
	static double xuandu_min = -40;// 旋度最小值
	static double xuandu_fazhi = 0.2;// 旋度阀值
	static int scale = 60;// 矩形框尺寸
	static int featurePointNumberBorder = 1;// 特征点数量的阀值
	public static int speed = 300;// 播放视频时各帧的间隔
	static int spaceTimeSize=5;

	public ExtractVideoFeature() {

	}

	//
	// public void exe(String videoFile,Labels c,int num,PrintWriter
	// outAll,PrintWriter outAll_label,ImageGUI gui) throws IOException {
	// VideoCapture capture= new VideoCapture();
	// capture.open(videoFile);// 读取本地文件
	// // capture.open(0);// 调取电脑的摄像头
	//
	// String hogDirAddress=MyConstants.VideoHogAddress+c.getName()+"/";
	// String hogFileAddress=c.getName()+"_"+num+"hog.txt";
	// /*File f1 = new File(hogDirAddress);
	// File f = new File(hogFileAddress);//保存路径
	// if (!f1.exists()) {
	// f1.mkdirs();
	// }
	// if (!f.exists()) {
	// // System.out.print("文件不存在");
	// f.createNewFile();// 不存在则创建
	// } */
	// File f=MyTools.mkdir(hogDirAddress,hogFileAddress);
	// FileWriter fw=new FileWriter(f);
	// PrintWriter out=new PrintWriter(new BufferedWriter(fw));
	//
	//
	// if (!capture.isOpened()) {
	// System.out.println("could not load video data...");
	// // MainWindow.tips.append("could not load video data...");
	// MyTools.showTips("could not load video data...");
	// out.close();
	// return;
	// }
	// int frame_width = (int) capture.get(3);
	// int frame_height = (int) capture.get(4);
	// int frameCount = (int)capture.get(7);
	// Mat prev = new Mat();
	// Mat next = new Mat();
	// Mat frame = new Mat();
	// MatOfFloat TenFramesHog= new MatOfFloat();//存储spaceTimeSize帧图像的hog
	//
	// int spaceSize = 0;// 时空立方体帧数
	// int start_extract = 0;// 开始提取时空体特征
	// // ImageGUI gui = new ImageGUI();
	// /*gui.createWin("OpenCV + Java视频读与播放演示", new Dimension(frame_width,
	// frame_height));*/
	//
	// int frameNo=1;
	//
	// while (true) {
	//
	// boolean have = capture.read(frame);
	// Imgproc.cvtColor(frame, next, Imgproc.COLOR_RGB2GRAY);
	// if (!have)
	// break;
	//
	// MainWindow.videoFrame.setText(MyConstants.S_frame+frameNo+" / "+frameCount);
	// frameNo++;
	// if (!prev.empty()) {
	// Mat flow = new Mat();
	// // Mat flow=new Mat(frame_width,frame_height,CvType.CV_8UC1);
	// Video.calcOpticalFlowFarneback(prev, next, flow, 0.5, 1, 1, 1,
	// 7, 1.5, 1);
	// // prevImg(y,x)=nextImg(y+flow(y,x)[1]，x+fow(y,x)[0]);
	// Matrix result = new Matrix(frame_width, frame_height);
	//
	// for (int ii = 0; ii < frame_height; ii++) {
	// for (int jj = 0; jj < frame_width; jj++) {
	// // 查找旋度最大值和最小值，保存在result里
	// double[] data;
	// data = flow.get(ii, jj);
	// double xuandu = data[0] - data[1];
	// result.set(jj, ii, xuandu);
	// if (xuandu > xuandu_max)
	// xuandu_max = xuandu;
	// if (xuandu < xuandu_min)
	// xuandu_min = xuandu;
	// }
	// }
	//
	// double max_border = xuandu_max * xuandu_fazhi;
	// double min_border = xuandu_min * xuandu_fazhi;
	//
	// // System.out.println("max_border:" + max_border +
	// // "  min_border:"
	// // + min_border);
	//
	// Vector<Point> v1 = new Vector<Point>();
	//
	// for (int ii = 0; ii < frame_height; ii++) {
	// for (int jj = 0; jj < frame_width; jj++) {
	// // 保存关键点
	// if ((result.get(jj, ii) > max_border)
	// || (result.get(jj, ii) < min_border))
	// v1.addElement(new Point(jj, ii));
	// // System.out.println(ii+"  "+jj);
	// }
	// }
	// double meanX = 0;// 所有关键点的平均x值
	// double meanY = 0;
	// for (int ii = 0; ii < v1.size(); ii++) {
	// meanX += v1.get(ii).x;
	// meanY += v1.get(ii).y;
	// }
	// meanX /= v1.size();
	// meanY /= v1.size();
	//
	// Mat paintPoint = frame.clone();
	//
	// int ltx = (int) (meanX - scale / 2);// leftTopX
	// int lty = (int) (meanY - scale / 2);// 左上角顶点的Y值
	// if (ltx < 0)
	// ltx = 0;
	// if (lty < 0)
	// lty = 0;
	// if ((ltx + scale) > frame_width)
	// ltx = frame_width - scale;
	// if ((lty + scale) > frame_height)
	// lty = frame_height - scale;
	// Rect fanwei = new Rect(ltx, lty, scale, scale);
	// if (v1.size() > featurePointNumberBorder) {
	// Core.rectangle(paintPoint, fanwei.tl(), fanwei.br(),
	// new Scalar(255, 0, 0), 2);
	// Core.circle(paintPoint, new Point(meanX, meanY), (int) 1,
	// new Scalar(0, 255, 0), 2);
	// }
	// // System.out.println(v1.size());
	// for (int m = 0; m < v1.size(); m++) {
	// Core.circle(paintPoint, v1.get(m), (int) 1, new Scalar(0,
	// 0, 255), 2);
	// }
	// gui.imshow(MyVideo.conver2Image(paintPoint));
	// gui.repaint();
	//
	//
	// if (v1.size() > featurePointNumberBorder && spaceSize == 0) {
	// start_extract = 1;
	// TenFramesHog = new MatOfFloat();
	// }
	// if (start_extract == 1) {
	// // 提取HOG特征：
	// HOGDescriptor desc = new HOGDescriptor(new Size(scale,
	// scale), new Size(20, 20), new Size(5, 5), new Size(
	// 10, 10), 5);
	// // System.out.println("维数"+desc.getDescriptorSize());//获取向量维数
	// MatOfFloat hogVector = new MatOfFloat();
	// Mat src=new Mat(next,new Range(lty,lty+scale),new Range(ltx,ltx+scale));
	// desc.compute(src, hogVector);
	// //
	// System.out.println("size："+hogVector.size()+"   rows:"+hogVector.rows()+"  cols:"+hogVector.cols()+"  demions:"+hogVector.dims());
	//
	// TenFramesHog.push_back(hogVector);
	// // float[] hogOut = hogVector.toArray();
	// // System.out.println(hogOut.length);//获取向量维数
	//
	// // BufferedWriter output = new BufferedWriter(new FileWriter(f,true));
	// // FileOutputStream out = new FileOutputStream(f);
	// /*for (int i = 0; i < hogOut.length; i++) {
	// out.print(hogOut[i] + "\t");
	// }
	// out.println();*/
	//
	// spaceSize++;
	// if (spaceSize == spaceTimeSize) {
	//
	// float[] hogOut1 = TenFramesHog.toArray();
	// // System.out.println(hogOut1.length);//获取向量维数
	// for (int i = 0; i < hogOut1.length; i++) {
	// out.print(hogOut1[i] + "\t");
	// outAll.print(hogOut1[i] + "\t");
	// // if(i==0)
	// // outAll_label.print(c.ordinal());
	// // outAll_label.print("k");
	// }
	// out.println();
	// outAll.println();
	// outAll_label.println(c.ordinal());
	//
	// start_extract = 0;
	// spaceSize = 0;
	// }
	//
	// }
	//
	// }
	// prev = next.clone();
	// try {
	// do{
	// Thread.sleep(speed);
	// }while(MyTools.pause);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	// out.close();
	// // gui.setVisible(false);
	// // gui.getUi().setVisible(false);
	// }

	public static Mat extract(String videoFile, ImageGUI gui) {
		// TODO Auto-generated method stub
		VideoCapture capture = new VideoCapture();
		capture.open(videoFile);// 读取本地文件
		Mat video_mat = new Mat();

		if (!capture.isOpened()) {
			System.out.println("could not load video data...");
			// MainWindow.tips.append("could not load video data...");
			MyTools.showTips("could not load video data...");
			// out.close();
			return null;
		}
		int frame_width = (int) capture.get(3);
		int frame_height = (int) capture.get(4);
		int frameCount = (int) capture.get(7);
		Mat prev = new Mat();
		Mat next = new Mat();
		Mat frame = new Mat();
		MatOfFloat TenFramesHog = new MatOfFloat();// 存储spaceTimeSize帧图像的hog
		MatOfFloat TenFramesDisMove = new MatOfFloat();// 存储spaceTimeSize帧图像的位移特征
		
		int spaceSize = 0;// 时空立方体帧数
		int start_extract = 0;// 开始提取时空体特征
		double preMeanX = 0;// 上一帧的特征点X均值
		double preMeanY = 0;// 上一帧的特征点Y均值

		// ImageGUI gui = new ImageGUI();
		// gui.createWin("OpenCV + Java视频读与播放演示", new Dimension(frame_width,
		// frame_height));

		int frameNo = 0;
		int jump=0;
		while (true) {

			boolean have = capture.read(frame);
			Imgproc.cvtColor(frame, next, Imgproc.COLOR_RGB2GRAY);
			if (!have)
				break;
			frameNo++;
			if(++jump!=2)
				continue;
			jump=0;
			MainWindow.videoFrame.setText(MyConstants.S_frame + frameNo + " / "
					+ frameCount);			
			if (!prev.empty()) {
				Mat flow = new Mat();
				// Mat flow=new Mat(frame_width,frame_height,CvType.CV_8UC1);
				Video.calcOpticalFlowFarneback(prev, next, flow, 0.5, 1, 1, 1,
						7, 1.5, 1);
				// prevImg(y,x)=nextImg(y+flow(y,x)[1]，x+fow(y,x)[0]);
				Matrix result = new Matrix(frame_width, frame_height);

				for (int ii = 0; ii < frame_height; ii++) {
					for (int jj = 0; jj < frame_width; jj++) {
						// 查找旋度最大值和最小值，保存在result里
						double[] data;
						data = flow.get(ii, jj);
						// double xuandu = data[0] - data[1];
						double xuandu = Math.sqrt(data[0] * data[0] + data[1]
								* data[1]);
						result.set(jj, ii, xuandu);
						if (xuandu > xuandu_max)
							xuandu_max = xuandu;
						if (xuandu < xuandu_min)
							xuandu_min = xuandu;
					}
				}

				double max_border = xuandu_max * xuandu_fazhi;
				double min_border = xuandu_min * xuandu_fazhi;

				// System.out.println("max_border:" + max_border +
				// "  min_border:"
				// + min_border);

				Vector<Point> v1 = new Vector<Point>();

				for (int ii = 0; ii < frame_height; ii++) {
					for (int jj = 0; jj < frame_width; jj++) {
						// 保存关键点
						if ((result.get(jj, ii) > max_border)
								|| (result.get(jj, ii) < min_border)) {
							v1.addElement(new Point(jj, ii));
//							 System.out.print((int)result.get(jj, ii)+"  ");
						}
						
					}
				}
//				System.out.println();
				
				/*if (v1.size() < featurePointNumberBorder)
					continue;*/
				
				double meanX = 0;// 所有关键点的平均x值
				double meanY = 0;
				for (int ii = 0; ii < v1.size(); ii++) {
					meanX += v1.get(ii).x;
					meanY += v1.get(ii).y;
				}
				meanX /= v1.size();
				meanY /= v1.size();

				Mat paintPoint = frame.clone();
				
				if (v1.size() > featurePointNumberBorder && spaceSize == 0) {
					start_extract = 1;
					TenFramesHog = new MatOfFloat();
					TenFramesDisMove = new MatOfFloat();
					preMeanX = meanX;
					preMeanY = meanY;
				}

//				if (v1.size() < featurePointNumberBorder) {
					meanX = preMeanX;
					meanY = preMeanY;
//				}
//				preMeanX = meanX;
//				preMeanY = meanY;

				int ltx = (int) (meanX - scale / 2);// leftTopX
				int lty = (int) (meanY - scale / 2);// 左上角顶点的Y值
				if (ltx < 0)
					ltx = 0;
				if (lty < 0)
					lty = 0;
				if ((ltx + scale) > frame_width)
					ltx = frame_width - scale;
				if ((lty + scale) > frame_height)
					lty = frame_height - scale;
				Rect fanwei = new Rect(ltx, lty, scale, scale);
				 if (v1.size() > featurePointNumberBorder) {
//				if (start_extract == 1) {
				Core.rectangle(paintPoint, fanwei.tl(), fanwei.br(),
						new Scalar(255, 0, 0), 2);
				Core.circle(paintPoint, new Point(meanX, meanY), (int) 1,
						new Scalar(0, 255, 0), 2);
				}
				// }
				// System.out.println(v1.size());
				for (int m = 0; m < v1.size(); m++) {
					Core.circle(paintPoint, v1.get(m), (int) 1, new Scalar(0,
							0, 255), 2);
				}
				gui.imshow(MyVideo.conver2Image(paintPoint));
				gui.repaint();

				
				if (start_extract == 1) {
					// 提取HOG特征：
					HOGDescriptor desc = new HOGDescriptor(new Size(scale,
							scale), new Size(20, 20), new Size(5, 5), new Size(
							10, 10), 5);
//					 System.out.println("维数"+desc.getDescriptorSize());//获取向量维数
					MatOfFloat hogVector = new MatOfFloat();
					Mat src = new Mat(next, new Range(lty, lty + scale),
							new Range(ltx, ltx + scale));
					desc.compute(src, hogVector);					
//					 System.out.println("size："+hogVector.size()+"   rows:"+hogVector.rows()+"  cols:"+hogVector.cols()+"  demions:"+hogVector.dims());
					TenFramesHog.push_back(hogVector);
					{
						float []f2=new float[scale*scale];
						int y=0;
						for(int m=lty;m<scale;m++){
							for(int n=ltx;n<scale;n++){
								f2[y++]=(float) result.get(ltx,lty);
							}
						}
					MatOfFloat feature2=new MatOfFloat(f2);
					TenFramesDisMove.push_back(feature2);
					}
					// float[] hogOut = hogVector.toArray();
					// System.out.println(hogOut.length);//获取向量维数

					// BufferedWriter output = new BufferedWriter(new
					// FileWriter(f,true));
					// FileOutputStream out = new FileOutputStream(f);
					/*
					 * for (int i = 0; i < hogOut.length; i++) {
					 * out.print(hogOut[i] + "\t"); } out.println();
					 */

					spaceSize++;
					if (spaceSize == spaceTimeSize) {
						TenFramesHog.push_back(TenFramesDisMove);

						float[] words = TenFramesHog.toArray();

						Mat aRow = new Mat(1, words.length, CvType.CV_32FC1);
						// float[]values=new float[words.length];
//						System.out.println( words.length);
						for (int k = 0; k < words.length; k++) {
							// String word = words[k];
							// double value = Float.parseFloat(word);
							// double value = (float) Double.parseDouble(word);
							// values[k]= (float) Double.parseDouble(word);
							aRow.put(0, k, words[k]);
						}
						// Mat aRow=new Mat();
						/*
						 * MatOfFloat aRow=new MatOfFloat();
						 * aRow.fromArray(values);
						 */

						// System.out.println("k="+k+"   words.length="+words.length);
						video_mat.push_back(aRow);

						// System.out.println(hogOut1.length);//获取向量维数
						// for (int i = 0; i < hogOut1.length; i++) {
						// out.print(hogOut1[i] + "\t");
						// outAll.print(hogOut1[i] + "\t");
						// if(i==0)
						// outAll_label.print(c.ordinal());
						// outAll_label.print("k");
						// }
						// out.println();
						// outAll.println();
						// outAll_label.println();

						start_extract = 0;
						spaceSize = 0;
					}

				}

			}
			prev = next.clone();
			try {
				do {
					Thread.sleep(speed);
				} while (MyTools.pause);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return video_mat;
	}
}
