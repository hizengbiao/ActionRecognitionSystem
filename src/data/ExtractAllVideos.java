package data;

import java.io.File;
import java.io.IOException;
import org.opencv.core.Mat;

public class ExtractAllVideos {

	public ExtractAllVideos() {
		// TODO Auto-generated constructor stub
	}

	public void exe(ImageGUI VideoShow) throws IOException {
		// File f =
		// MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.svm_data_tem);//保存路径
		// File f2 =
		// MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.svm_label_tem);//保存路径
		//
		// FileWriter fw=new FileWriter(f);
		// PrintWriter outAll=new PrintWriter(new BufferedWriter(fw));
		//
		// FileWriter fw2=new FileWriter(f2);
		// PrintWriter outAll_label=new PrintWriter(new BufferedWriter(fw2));
		//
		MyTools.showTips("\n特征提取中...", 1);
/*
		for (Labels c : Labels.values()) {
			// for(int y=0;y<1;y++){
			// Labels c=Labels.BOXING;
			int all=c.getNumberOfVideos();
//			int all=MyConstants.ExtractVideoCount;
//			for (int i = 1; i <= MyConstants.ExtractVideoCount; i++) {
//				 for(int i=1;i<=c.getNumberOfVideos();i++){
			 for(int i=1;i<=all;i++){
				String videoAddress = MyConstants.dataOfVideosAddress
						+ c.getName() + "/" + c.getName() + "_" + i + ".avi";

				MyTools.clearTips();
				MyTools.showTips("特征提取中...", 1);
				MyTools.showTips("进度："
						+ (c.ordinal() * all + i)
						+ " / " + Labels.getLabelsCount()
						* all);
				MainWindow.videoPath.setText(MyConstants.S_videoPath
						+ MyConstants.dataOfVideosAddress + c.getName() + "/");
				MainWindow.videoName.setText(MyConstants.S_videoName
						+ c.getName() + "_" + i + ".avi");

				// ExtractVideoFeature ext=new ExtractVideoFeature();
				Mat features = ExtractVideoFeature.extract(videoAddress,
						VideoShow);
				MyTools.saveFeaturesToText(features,
						MyConstants.VideoHogAddress + c.getName() + "/",
						c.getName() + "_" + i + "hog_tem.txt");
				// ext.exe(videoAddress,c,i,outAll,outAll_label,VideoShow);

				File f = MyTools.mkdir(
						MyConstants.VideoHogAddress + c.getName() + "/",
						c.getName() + "_" + i + "hog_tem.txt");// 保存路径
				File f_ = MyTools.mkdir(
						MyConstants.VideoHogAddress + c.getName() + "/",
						c.getName() + "_" + i + "hog.txt");// 保存路径

				FileCopy.Copy(f, f_);
				// MyTools.showTips("\n特征数据拷贝完成",1);

			}

		}
*/
		// 保存所有未标记视频的特征：
		File path = new File(MyConstants.unLabeledVideosAddress);
		String names[] = path.list();
		int numOfUnlabeledVideos = path.list().length;
		// ArrayList<String> svm_unlabeled_filename = new ArrayList<String>();
		// ArrayList<String> knn_unlabeled_filename = new ArrayList<String>();
		for (int i = 0; i < numOfUnlabeledVideos; i++) {
			// svm_unlabeled_filename.add(MyConstants.unLabeledVideosHogAddress+names[i]);
			// knn_unlabeled_filename.add(MyConstants.unLabeledVideosHogAddress+names[i]);

			MainWindow.videoPath.setText(MyConstants.S_videoPath
					+ MyConstants.unLabeledVideosHogAddress);
			MainWindow.videoName.setText(MyConstants.S_videoName + names[i]);
			
			MyTools.clearTips();
			MyTools.showTips("未标识视频特征提取中...", 1);
			MyTools.showTips("进度：" + (i + 1) + " / " + numOfUnlabeledVideos);
			//
			// //提取这些视频的特征并保存：
			Mat features = ExtractVideoFeature.extract(
					MyConstants.unLabeledVideosAddress + names[i], VideoShow);
			MyTools.saveFeaturesToText(features,
					MyConstants.unLabeledVideosHogAddress, names[i] + ".txt");
		}

		MyTools.showTips("\n特征提取完毕", 1);
		//
		// outAll.close();
		// outAll_label.close();
		//
		// File
		// f_=MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.svm_data);//保存路径
		// File
		// f2_=MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.svm_label);//保存路径
		//
		// File
		// f_knn=MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.knn_data);//保存路径
		// File
		// f2_knn=MyTools.mkdir(Classifiers.data_hog_Address,Classifiers.knn_label);//保存路径
		//
		// FileCopy.Copy(f, f_);
		// MyTools.showTips("\nsvm特征数据拷贝完成",1);
		// FileCopy.Copy(f2, f2_);
		// MyTools.showTips("\nsvm特征标签拷贝完成",1);
		//
		// FileCopy.Copy(f_, f_knn);
		// MyTools.showTips("\nknn特征数据拷贝完成",1);
		// FileCopy.Copy(f2_, f2_knn);
		// MyTools.showTips("\nknn特征标签拷贝完成",1);

	}

}
