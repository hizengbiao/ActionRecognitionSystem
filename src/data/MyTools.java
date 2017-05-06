package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Mat;

public class MyTools {
	/*
	 * static int pauseTime=600000; static int preSpeed;
	 */
	public static boolean pause = false;
	public static boolean loadingFeature = false;
	public static int loadingFeatureResult;
	public static boolean savingFeature = false;
	public static int savingFeatureResult;
	public static boolean loadingModel = false;
	public static int loadingModelResult;

	public MyTools() {
		// TODO Auto-generated constructor stub
	}

	public static File mkdir(String dir, String fileName) {
		File f1 = new File(dir);
		File f = new File(dir + fileName);// 保存路径
		if (!f1.exists()) {
			f1.mkdirs();
		}
		if (!f.exists()) {
			// System.out.print("文件不存在");
			try {
				f.createNewFile();// 不存在则创建
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return f;
	}

	public static void speedUp() {
		if (ExtractVideoFeature.speed <= 100)
			ExtractVideoFeature.speed -= 30;
		else if (ExtractVideoFeature.speed >= 500)
			ExtractVideoFeature.speed -= 200;
		else {
			ExtractVideoFeature.speed -= 100;
			if (ExtractVideoFeature.speed < 100)
				ExtractVideoFeature.speed = 100;
		}

		if (ExtractVideoFeature.speed < 0)
			ExtractVideoFeature.speed = 0;
		MyTools.showTips("当前播放间隔：" + ExtractVideoFeature.speed + "us/帧", 1);
	}

	public static void videoPlay() {
		pause = false;
	}

	public static void videoPause() {
		pause = true;
	}

	public static boolean isPause() {
		return pause;
	}

	public static void speedDown() {
		if (ExtractVideoFeature.speed <= 100)
			ExtractVideoFeature.speed += 30;
		else if (ExtractVideoFeature.speed >= 500)
			ExtractVideoFeature.speed += 200;
		else
			ExtractVideoFeature.speed += 100;

		// ExtractVideoFeature.speed+=100;
		if (ExtractVideoFeature.speed > 1000)
			ExtractVideoFeature.speed = 1000;
		MyTools.showTips("当前播放间隔：" + ExtractVideoFeature.speed + "us/帧", 1);
	}

	public static void clearTips() {
		MainWindow.tips.setText("\n");
	}

	public static void showTips(String s, int sign) {
		if (sign == 1) {
			MainWindow.tips.append("\n " + s + "\n");
		}
	}

	public static void showTips(String s) {
		MainWindow.tips.append("      " + s + "\n");
	}

	public static void mixFeatures(File f, File f2, File f_, File f2_,
			int startVideoNum) throws IOException {
		// 合并特征：
		int nowN = 0;
		int basisVidN = Labels.getLabelsCount() * MyConstants.TrainVideoCount;
		// MyTools.showTips("\n特征合并中...", 1);
		MyTools.savingFeature = true;
		// FileWriter fw = new FileWriter(f,true);
		FileWriter fw = new FileWriter(f);
		PrintWriter outAll = new PrintWriter(new BufferedWriter(fw));

		FileWriter fw2 = new FileWriter(f2);
		PrintWriter outAll_label = new PrintWriter(new BufferedWriter(fw2));

		for (Labels c : Labels.values()) {

			// for(int y=0;y<1;y++){ Labels c=Labels.BOXING;

			for (int i = startVideoNum; i < startVideoNum
					+ MyConstants.TrainVideoCount; i++) {
				// for(int i=1;i<=c.getNumberOfVideos();i++){
				nowN++;
				// String videoAddress = MyConstants.dataOfVideosAddress
				// + c.getName() + "/" + c.getName() + "_" + i + ".avi";
				// MyTools.showTips("videoAddress "+videoAddress, 1);
				MyTools.clearTips();
				MyTools.showTips("特征合并中...", 1);
				MyTools.showTips("进度：" + nowN + " / " + basisVidN);
				MainWindow.videoPath.setText("特征路径：    "
						+ MyConstants.VideoHogAddress + c.getName() + "/");
				MainWindow.videoName.setText("特征名字：    " + c.getName() + "_"
						+ i + ".txt");
				MainWindow.videoFrame.setVisible(false);

				// ExtractVideoFeature ext=new ExtractVideoFeature();
				// Mat features = ExtractVideoFeature.extract(videoAddress,
				// videoShow);
				Mat features = Classifiers
						.loadTrainData(MyConstants.VideoHogAddress
								+ c.getName() + "/" + c.getName() + "_" + i
								+ "hog.txt");

				MyTools.saveFeaturesToText(features, c.ordinal(), outAll,
						outAll_label);
				// ext.exe(videoAddress,c,i,outAll,outAll_label,VideoShow);
			}

		}
		MyTools.showTips("\n特征合并完毕", 1);

		outAll.close();
		outAll_label.close();

		FileCopy.Copy(f, f_);
		MyTools.showTips("\n特征数据拷贝完成", 1);
		FileCopy.Copy(f2, f2_);
		MyTools.showTips("\n特征标签拷贝完成", 1);

		MyTools.savingFeature = false;

	}

	public static void updateFeature(File f, File f2, File f_, File f2_,
			Mat features, int c) throws IOException {
		// MyTools.showTips("\nsvm特征提取中...", 1);
		// MyTools.savingFeature=true;
		FileWriter fw = new FileWriter(f, true);
		// FileWriter fw = new FileWriter(f);
		PrintWriter outAll = new PrintWriter(new BufferedWriter(fw));

		FileWriter fw2 = new FileWriter(f2, true);
		PrintWriter outAll_label = new PrintWriter(new BufferedWriter(fw2));

		// for (Labels c : Labels.values()) {

		// for(int y=0;y<1;y++){ Labels c=Labels.BOXING;

		// for (int i = startVideoNum; i <=
		// startVideoNum+MyConstants.TrainVideoCount; i++) {
		// for(int i=1;i<=c.getNumberOfVideos();i++){
		// String videoAddress = MyConstants.dataOfVideosAddress
		// + c.getName() + "/" + c.getName() + "_" + i + ".avi";
		//
		// MyTools.clearTips();
		// MyTools.showTips("特征提取中...", 1);
		// MyTools.showTips("进度："
		// + (c.ordinal() * MyConstants.TrainVideoCount + i)
		// + " / " + Labels.getLabelsCount()
		// * MyConstants.TrainVideoCount);
		// MainWindow.videoPath.setText(MyConstants.S_videoPath
		// + MyConstants.dataOfVideosAddress + c.getName() + "/");
		// MainWindow.videoName.setText(MyConstants.S_videoName
		// + c.getName() + "_" + i + ".avi");
		//
		// // ExtractVideoFeature ext=new ExtractVideoFeature();
		// Mat features = ExtractVideoFeature.extract(videoAddress,
		// videoShow);

		MyTools.saveFeaturesToText(features, c, outAll, outAll_label);
		// ext.exe(videoAddress,c,i,outAll,outAll_label,VideoShow);
		// }

		// }
		// MyTools.showTips("\nsvm特征提取完毕", 1);

		outAll.close();
		outAll_label.close();

		// MyTools.savingFeature=false;

		FileCopy.Copy(f, f_);
		MyTools.showTips("\n特征数据拷贝完成", 1);
		FileCopy.Copy(f2, f2_);
		MyTools.showTips("\n特征标签拷贝完成", 1);

	}

	public static void saveFeaturesToText(Mat features, Labels c, int num,
			PrintWriter outAll, PrintWriter outAll_label) throws IOException {
		String hogDirAddress = MyConstants.VideoHogAddress + c.getName() + "/";
		String hogFileAddress = c.getName() + "_" + num + "hog.txt";
		savingFeature = true;
		File f = MyTools.mkdir(hogDirAddress, hogFileAddress);
		FileWriter fw = new FileWriter(f);
		PrintWriter out = new PrintWriter(new BufferedWriter(fw));

		for (int i = 0; i < features.rows(); i++) {
			for (int k = 0; k < features.cols(); k++) {
				double[] va = features.get(i, k);
				float value = (float) va[0];
				out.print(value + "\t");
				outAll.print(value + "\t");
			}
			out.println();
			outAll.println();
			outAll_label.println(c.ordinal());
		}
		out.close();
		savingFeature = false;
	}

	public static void saveFeaturesToText(Mat features, int c,
			PrintWriter outAll, PrintWriter outAll_label) throws IOException {
		// 已知一个视频的特征、类别，将这些特征写入到特征集中

		// savingFeature=true;
		// String hogDirAddress = MyConstants.VideoHogAddress + c.getName() +
		// "/";
		// String hogFileAddress = c.getName() + "_" + num + "hog.txt";
		//
		// File f = MyTools.mkdir(hogDirAddress, hogFileAddress);
		// FileWriter fw = new FileWriter(f);
		// PrintWriter out = new PrintWriter(new BufferedWriter(fw));

		for (int i = 0; i < features.rows(); i++) {
			for (int k = 0; k < features.cols(); k++) {
				double[] va = features.get(i, k);
				float value = (float) va[0];
				// out.print(value + "\t");
				outAll.print(value + "\t");
			}
			// out.println();
			outAll.println();
			outAll_label.println(c);
		}
		// out.close();
		// savingFeature=false;
	}

	public static void saveFeaturesToText(Mat features, String hogDirAddress,
			String hogFileAddress) throws IOException {
		// savingFeature=true;
		File f = MyTools.mkdir(hogDirAddress, hogFileAddress);
		FileWriter fw = new FileWriter(f);
		PrintWriter out = new PrintWriter(new BufferedWriter(fw));

		for (int i = 0; i < features.rows(); i++) {
			for (int k = 0; k < features.cols(); k++) {
				double[] va = features.get(i, k);
				float value = (float) va[0];
				out.print(value + "\t");
			}
			out.println();
		}
		out.close();
		// savingFeature=false;
	}

	public static void sortN(float[][] NoTimes, int n) {
		// TODO Auto-generated method stub
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				if (NoTimes[j][0] < NoTimes[j + 1][0]) {
					float tem[] = new float[2];
					tem[0] = NoTimes[j][0];
					tem[1] = NoTimes[j][1];
					NoTimes[j][0] = NoTimes[j + 1][0];
					NoTimes[j][1] = NoTimes[j + 1][1];
					NoTimes[j + 1][0] = tem[0];
					NoTimes[j + 1][1] = tem[1];
				}
			}
		}

	}

	public static void sortBelief(VideoConfidence[] NoTimes, int n) {
		// 排序
		// int n=Labels.getLabelsCount();//n=6
		// float[][] NoTimes = new float[n][2];
		// // 6是视频各类数量，第一个列向量是视频各类id，第二个是特征符合的数量
		// // 第7行是总数
		// for (int w = 0; w < n; w++) {
		// NoTimes[w][0] = w;
		// NoTimes[w][1] = r[w];
		// }
		// for (int u = 0; u < n; u++) {
		// NoTimes[r[u]][1]++;
		// NoTimes[n][1]++;
		// }

		// 排序：
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				if (NoTimes[j].getConfidence() < NoTimes[j + 1].getConfidence()) {
					VideoConfidence tem = NoTimes[j];
					NoTimes[j] = NoTimes[j + 1];
					NoTimes[j + 1] = tem;
				}
			}
		}
		//
		// return NoTimes;
	}

	public static void sortBeliefSmallToBig(VideoConfidence[] NoTimes, int n) {
		// 从小到大排序：
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				if (NoTimes[j].getConfidence() > NoTimes[j + 1].getConfidence()) {
					VideoConfidence tem = NoTimes[j];
					NoTimes[j] = NoTimes[j + 1];
					NoTimes[j + 1] = tem;
				}
			}
		}
		//
		// return NoTimes;
	}

	public static void sortId(VideoConfidence[] NoTimes, int n) {
		// 排序
		// int n=Labels.getLabelsCount();//n=6
		// float[][] NoTimes = new float[n][2];
		// // 6是视频各类数量，第一个列向量是视频各类id，第二个是特征符合的数量
		// // 第7行是总数
		// for (int w = 0; w < n; w++) {
		// NoTimes[w][0] = w;
		// NoTimes[w][1] = r[w];
		// }
		// for (int u = 0; u < n; u++) {
		// NoTimes[r[u]][1]++;
		// NoTimes[n][1]++;
		// }

		// 排序：
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				if (NoTimes[j].getId() < NoTimes[j + 1].getId()) {
					VideoConfidence tem = NoTimes[j];
					NoTimes[j] = NoTimes[j + 1];
					NoTimes[j + 1] = tem;
				}
			}
		}
		//
		// return NoTimes;
	}

	public static void playVideo(String vidAdd) {
		if (MainWindow.SVMPredictButtonState == false) {
			if (MainWindow.isRunning == true) {
				System.out.println(MyConstants.ThreadConflictMsg);
				// MainWindow.tips.append(MyConstants.ThreadConflictMsg);
				MyTools.showTips(MyConstants.ThreadConflictMsg);
				return;
			}
			MainWindow.isRunning = true;
			MainWindow.SVMPredictButtonState = true;

			MyTools.clearTips();
			MyTools.videoPlay();
			MainWindow.videoPause.setText("暂停");
			MainWindow.HiddenJLabels();
			MainWindow.HiddenJLabels(1);
			MainWindow.optionStatus.setText(MyConstants.S_optionStatus
					+ MyConstants.S_svm_Predict);

			MainWindow.SVMPredict.setText(MyConstants.S_Terminate
					+ MyConstants.S_svm_Predict);

			ThreadCtrl ctrl = new ThreadCtrl("SVMPredict");
			ctrl.setGUI(MainWindow.videoGUI, MainWindow.SVMPredict);
			File f = new File(vidAdd);
			ctrl.setFile(f);
			MainWindow.myThread = new Thread(ctrl);
			MainWindow.myThread.start();
		}
	}

	public static void ExtractAndTrain() throws IOException {
		/*
		 * ExtractAllVideos extA=new ExtractAllVideos();
		 * extA.exe(MainWindow.videoGUI);
		 * 
		 * // 训练： MySVM.loadTrainData(); // MySVM.saveTrainDataTest();
		 * MySVM.train();
		 */

		Thread deb = new Thread() {
			public void run() {
				ExtractVideoFeature.speed = 0;

				MainWindow.Extract.doClick();
				// while(MainWindow.isRunning==true) i++;

				while (true) {
					if (MainWindow.isRunning == false) {
						break;
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				MainWindow.Train.doClick();
			}
		};
		deb.start();

	}

	public static void loadFeature() {
		loadingFeature = true;
		Thread lod = new Thread() {
			public void run() {
				try {
					loadingFeatureResult = Classifiers.loadTrainData();
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				loadingFeature = false;
			}
		};
		lod.start();

	}

	public static void loadSVMModel() {
		// TODO Auto-generated method stub
		loadingModel = true;
		Thread lod = new Thread() {
			public void run() {
				loadingModelResult = Classifiers.loadSVMModel();
				loadingModel = false;
			}
		};
		lod.start();
	}

	public static void test() {
		// TODO Auto-generated method stub

		Thread lod = new Thread() {
			public void run() {
				try {
					CoTraining.start(MainWindow.videoGUI);
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		lod.start();
	}

	public static void uncoTrainMixFeature() throws IOException {
		// TODO Auto-generated method stub
		// svm 特征及标签文件：
		File f_svm_data_tem = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_data_tem);// 保存路径
		File f_svm_label_tem = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_label_tem);// 保存路径

		File f_svm_data = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_data);// 保存路径
		File f_svm_label = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_label);// 保存路径

		// svm特征合并：
		MyTools.mixFeatures(f_svm_data_tem, f_svm_label_tem, f_svm_data,
				f_svm_label, 1);

		File f_knn_data = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.knn_data);// 保存路径
		File f_knn_label = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.knn_label);// 保存路径

		FileCopy.Copy(f_svm_data, f_knn_data);
		MyTools.showTips("\nknn特征数据拷贝完成", 1);
		FileCopy.Copy(f_svm_label, f_knn_label);
		MyTools.showTips("\nknn特征标签拷贝完成", 1);
	}

}
