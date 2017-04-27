package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Mat;

public class CoTraining {

	public CoTraining() {
		// TODO Auto-generated constructor stub
	}

	public static void a(File f, File f2, File f_, File f2_, ImageGUI videoShow) throws IOException {
		MyTools.showTips("\nsvm特征提取中...", 1);
		
		FileWriter fw = new FileWriter(f);
		PrintWriter outAll = new PrintWriter(new BufferedWriter(fw));

		FileWriter fw2 = new FileWriter(f2);
		PrintWriter outAll_label = new PrintWriter(new BufferedWriter(fw2));


		for (Labels c : Labels.values()) {
			/*
			 * for(int y=0;y<1;y++){ Labels c=Labels.BOXING;
			 */
			for (int i = 1; i <= MyConstants.TrainVideoCount; i++) {
				// for(int i=1;i<=c.getNumberOfVideos();i++){
				String videoAddress = MyConstants.dataOfVideosAddress
						+ c.getName() + "/" + c.getName() + "_" + i + ".avi";

				MyTools.clearTips();
				MyTools.showTips("特征提取中...", 1);
				MyTools.showTips("进度："
						+ (c.ordinal() * MyConstants.TrainVideoCount + i)
						+ " / " + Labels.getLabelsCount()
						* MyConstants.TrainVideoCount);
				MainWindow.videoPath.setText(MyConstants.S_videoPath
						+ MyConstants.dataOfVideosAddress + c.getName() + "/");
				MainWindow.videoName.setText(MyConstants.S_videoName
						+ c.getName() + "_" + i + ".avi");

				// ExtractVideoFeature ext=new ExtractVideoFeature();
				Mat features = ExtractVideoFeature.extract(videoAddress,
						videoShow);

				MyTools.saveFeaturesToText(features, c.ordinal(), outAll,
						outAll_label);
				// ext.exe(videoAddress,c,i,outAll,outAll_label,VideoShow);
			}

		}
		MyTools.showTips("\nsvm特征提取完毕", 1);

		outAll.close();
		outAll_label.close();

		

		FileCopy.Copy(f, f_);
		MyTools.showTips("\n特征数据拷贝完成", 1);
		FileCopy.Copy(f2, f2_);
		MyTools.showTips("\n特征标签拷贝完成", 1);

		MyTools.loadFeature();
	}

	public static void start(ImageGUI VideoShow) throws IOException,
			InterruptedException {

		// svm特征提取：
		File f = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_data_tem);// 保存路径
		File f2 = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_label_tem);// 保存路径
		
		File f_ = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_data);// 保存路径
		File f2_ = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_label);// 保存路径

	
		a(f,f2,f_,f2_,VideoShow);

		/*while (MyTools.loadingFeature == true) {
			Thread.sleep(50);
		}
		if (MyTools.loadingFeatureResult == 1) {
			Classifiers.SVMtrain();
			// 每次训练完后启动一个线程重新加载一下训练模型：
			MyTools.loadSVMModel();
		} else if (MyTools.loadingFeatureResult == 0) {
			MyTools.showTips("训练数据加载失败！\n    提取的特征是残缺的，请重新提取，提取的过程中不要点击终止按钮！",
					1);
			return;
		} else {
			MyTools.showTips("训练数据加载失败！", 1);
			return;
		}*/
		// 到此svm.;训练完成第一阶段

		/*
		 * outAll.close(); outAll_label.close();
		 * 
		 * File
		 * f_=MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.data_hog_name
		 * );//保存路径 File
		 * f2_=MyTools.mkdir(Classifiers.data_hog_Address,Classifiers
		 * .data_hog_label);//保存路径
		 * 
		 * FileCopy.Copy(f, f_); MyTools.showTips("\n特征数据拷贝完成",1);
		 * FileCopy.Copy(f2, f2_); MyTools.showTips("\n特征标签拷贝完成",1);
		 */
	}

	

	

}
