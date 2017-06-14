package data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.opencv.core.Mat;

import utils.FileChooser;
import utils.FileCopy;
import utils.ImageGUI;
import utils.MyTools;

public class CoTraining {
//	static int basisVidN=2* Labels.getLabelsCount()* MyConstants.TrainVideoCount;
//	static int nowN=0;

	public CoTraining() {
		// TODO Auto-generated constructor stub
	}

	public static void start(ImageGUI VideoShow) throws IOException,
			InterruptedException {
//		nowN=0;

		// svm 特征及标签文件：
		File f_svm_data_tem = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_data_tem);// 保存路径
		File f_svm_label_tem = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_label_tem);// 保存路径
		
		File f_svm_data = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_data);// 保存路径
		File f_svm_label = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.svm_label);// 保存路径
		
		// knn 特征及标签文件：
		
		File f_knn_data_tem = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.knn_data_tem);// 保存路径
		File f_knn_label_tem = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.knn_label_tem);// 保存路径
		
		File f_knn_data = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.knn_data);// 保存路径
		File f_knn_label = MyTools.mkdir(Classifiers.data_hog_Address,
				Classifiers.knn_label);// 保存路径

		//svm特征合并：
		MyTools.mixFeatures(f_svm_data_tem,f_svm_label_tem,f_svm_data,f_svm_label,1);
		
		
		//knn特征合并：
		MyTools.mixFeatures(f_knn_data_tem,f_knn_label_tem,f_knn_data,f_knn_label,MyConstants.TrainVideoCount+1);

		MyTools.loadFeature();
		
		//svm训练：
		while (MyTools.loadingFeature == true) {
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
		}
		
		while(MyTools.loadingModel==true){
			Thread.sleep(50);
		}
		if(MyTools.loadingModelResult!=1){
			MyTools.showTips("分类器没有成功加载，无法预测！", 1);
			return;
		}
		// 到此训练完成第一阶段，训练出两个基本的分类器

		
		//保存所有未标记视频的路径：
		File path = new File(MyConstants.unLabeledVideosAddress);
        String names[]=path.list();
		int numOfUnlabeledVideos=path.list().length;
		ArrayList<String> svm_unlabeled_filename = new ArrayList<String>();
		ArrayList<String> knn_unlabeled_filename = new ArrayList<String>();
		for(int i=0;i<numOfUnlabeledVideos;i++){
			svm_unlabeled_filename.add(MyConstants.unLabeledVideosHogAddress+names[i]);
			knn_unlabeled_filename.add(MyConstants.unLabeledVideosHogAddress+names[i]);
			
			MainWindow.videoPath.setText(MyConstants.S_videoPath
					+MyConstants.unLabeledVideosHogAddress);
			MainWindow.videoName.setText(MyConstants.S_videoName+names[i]);
			
//			MyTools.showTips("未标识视频特征提取中...", 1);
//			MyTools.showTips("进度："	+ (i+1)+ " / " + numOfUnlabeledVideos);
//			
//			//提取这些视频的特征并保存：	
//			Mat features = ExtractVideoFeature.extract(MyConstants.unLabeledVideosAddress+names[i],
//					VideoShow);
//			MyTools.saveFeaturesToText(features,MyConstants.unLabeledVideosHogAddress,names[i]+".txt");
		}
/*		for(int i=0;i<svm_unlabeled_filename.size();i++){
			MyTools.showTips((String)knn_unlabeled_filename.get(i), 1);
		}*/
		
		//协同训练：
		int iteration=0;
		while(svm_unlabeled_filename.size()!=0||knn_unlabeled_filename.size()!=0){
			iteration++;
			MyTools.showTips("\n第"+iteration+"次迭代：", 1);
			VideoConfidence[] svm_believe=new VideoConfidence[svm_unlabeled_filename.size()];
			VideoConfidence[] knn_believe=new VideoConfidence[knn_unlabeled_filename.size()];
			for(int i=0;i<svm_unlabeled_filename.size();i++){
//				加载第i个数据
				Mat svm_fea=Classifiers.loadTrainData(svm_unlabeled_filename.get(i)+".txt");
//				Mat knn_fea=Classifiers.loadTrainData(knn_unlabeled_filename.get(i)+".txt");
//				进行预测并计算它的置信度
				svm_believe[i]=Classifiers.SVMpredict(svm_fea);
				svm_believe[i].setId(i);
//				knn_believe[i]=Classifiers.KNNpredict(knn_fea);
//				knn_believe[i].setId(i);
//				MyTools.showTips(knn_unlabeled_filename.get(i), 1);
//				MyTools.showTips("id:"+knn_believe[i].getId()+" 类别："+knn_believe[i].getVideoType()+" 置信度："+knn_believe[i].getConfidence(), 1);
			}
			
			for(int i=0;i<knn_unlabeled_filename.size();i++){
//				加载第i个数据
//				Mat svm_fea=Classifiers.loadTrainData(svm_unlabeled_filename.get(i)+".txt");
				Mat knn_fea=Classifiers.loadTrainData(knn_unlabeled_filename.get(i)+".txt");
//				进行预测并计算它的置信度
//				svm_believe[i]=Classifiers.SVMpredict(svm_fea);
//				svm_believe[i].setId(i);
				knn_believe[i]=Classifiers.KNNpredict(knn_fea);
				knn_believe[i].setId(i);
//				MyTools.showTips(knn_unlabeled_filename.get(i), 1);
//				MyTools.showTips("id:"+knn_believe[i].getId()+" 类别："+knn_believe[i].getVideoType()+" 置信度："+knn_believe[i].getConfidence(), 1);
			}
			
//			对置信度由小到大排序：
			MyTools.sortBeliefSmallToBig(svm_believe, svm_believe.length);
			MyTools.sortBeliefSmallToBig(knn_believe, knn_believe.length);
//			
//			根据置信度阀值筛选出置信度最小的前N个：
			float ConfiThreshold=MyConstants.ConfiThreshold;//置信度小于ConfiThreshold的视频不考虑
			int N1=0;
			for(N1=0;N1<svm_believe.length;N1++){
				if(svm_believe[N1].getConfidence()>ConfiThreshold)
					break;
			}
			int N2;
			for(N2=0;N2<knn_believe.length;N2++){
				if(knn_believe[N2].getConfidence()>ConfiThreshold)
					break;
			}
//			
			
//			调试用的：
//			for(int i=0;i<svm_believe.length;i++){
//				MyTools.showTips(i+"  "+svm_believe[i].getConfidence());
//			}
//			
//			for(int i=0;i<knn_believe.length;i++){
//				MyTools.showTips(i+"  "+knn_believe[i].getConfidence());
//			}
//			MyTools.showTips("N1:"+N1+"  svm_believe.length:"+svm_believe.length, 1);
//			MyTools.showTips("N2:"+N2+"  knn_believe.length:"+knn_believe.length, 1);
			
			
//			对前N个按序号由大到小排序：
			MyTools.sortId(svm_believe, N1);
			MyTools.sortId(knn_believe, N2);
			
//			删除这N个数据：
//			MyTools.showTips(svm_believe.length+"  svm_believe.length",1);
			for(int i=0;i<N1;i++){
//				MyTools.showTips(i+"  i",1);
				int tt=svm_believe[i].getId();
//				MyTools.showTips(tt+"  tt",1);
				svm_unlabeled_filename.remove(tt);
			}
			for(int i=0;i<N2;i++){
			knn_unlabeled_filename.remove(knn_believe[i].getId());
			}
			
//			如果N！=0，continue:
			if(N1!=0||N2!=0)
				continue;
			
//			对置信度进行由大到小排序：
			MyTools.sortBelief(svm_believe, svm_believe.length);
			MyTools.sortBelief(knn_believe, knn_believe.length);
			
//			MyTools.showTips("\n\n\n", 1);
//			for(int i=0;i<svm_unlabeled_filename.size();i++){
//				MyTools.showTips("id:"+knn_believe[i].getId()+" 类别："+knn_believe[i].getVideoType()+" 置信度："+knn_believe[i].getConfidence(), 1);
//			}
			
//			
//			再对前5个的序号进行由大到小的排列，调用ArryList的remove方法时先删除序号大的，否则每次remove一个元素后ArryList的序号都是动态变化的,会产生错误
			int svm_coTrainTop=MyConstants.coTrainNum;
			int knn_coTrainTop=MyConstants.coTrainNum;
			if(svm_believe.length<svm_coTrainTop)
				svm_coTrainTop=svm_believe.length;
			if(knn_believe.length<knn_coTrainTop)
				knn_coTrainTop=knn_believe.length;
			MyTools.sortId(svm_believe,svm_coTrainTop);
			MyTools.sortId(knn_believe,knn_coTrainTop);
			
			/*MyTools.showTips("\n\n\n", 1);
			for(int i=0;i<svm_unlabeled_filename.size();i++){
				MyTools.showTips("id:"+knn_believe[i].getId()+" 类别："+knn_believe[i].getVideoType()+" 置信度："+knn_believe[i].getConfidence(), 1);
			}*/
//
//
//			
//			选取置信度最高的5个加入到另一个分类器中进行训练,并把这5个数据从训练集中移除
//			MyTools.savingFeature=true;
			
			for(int i=0;i<svm_coTrainTop;i++){
				String svm_adds=svm_unlabeled_filename.get(svm_believe[i].getId())+".txt";
				Mat svm_fea=Classifiers.loadTrainData(svm_adds);
//				
//				//knn特征更新：
				MyTools.updateFeature(f_knn_data_tem,f_knn_label_tem,f_knn_data,f_knn_label,svm_fea,svm_believe[i].getVideoType());
			
//				移除这5个数据：
				svm_unlabeled_filename.remove(svm_believe[i].getId());
			}
			
			for(int i=0;i<knn_coTrainTop;i++){
				String knn_adds=knn_unlabeled_filename.get(knn_believe[i].getId())+".txt";
				Mat knn_fea=Classifiers.loadTrainData(knn_adds);
//				
//				//svm特征更新：
				MyTools.updateFeature(f_svm_data_tem,f_svm_label_tem,f_svm_data,f_svm_label,knn_fea,knn_believe[i].getVideoType());
//				
//				移除这5个数据：
				knn_unlabeled_filename.remove(knn_believe[i].getId());
			}
			
//			MyTools.savingFeature=false;
			
			while (MyTools.savingFeature == true) {
				Thread.sleep(50);
			}
			MyTools.loadFeature();
			//svm训练：
			while (MyTools.loadingFeature == true) {
				Thread.sleep(50);
			}
			if (MyTools.loadingFeatureResult == 1) {
//				File ff=FileChooser.saveFile();
//				Classifiers.svm_modelAddress=ff.getParent();
//				Classifiers.svm_modelName=ff.getName();
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
			}
			
//			
		}
		
		File f = new File(Classifiers.svm_modelAddress + Classifiers.svm_modelName);
		File ff=FileChooser.saveFile();//选择保存路径
		FileCopy.Copy(f, ff);
		MyTools.showTips("协同训练完成！", 1);
		
	}

	

	

}
