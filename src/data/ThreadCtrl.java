package data;

import fileFilter.AviFileFilter;
import fileFilter.Mp4FileFilter;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import utils.ImageGUI;
import utils.MyTools;

public class ThreadCtrl implements Runnable {
	String cmd = null;

	ImageGUI videoGUI;
	JButton buttonRecover;
	File file = null;

	// ImageGUI predictVideo;

	public ThreadCtrl(String command) {
		// TODO Auto-generated constructor stub
		cmd = command;
	}

	public void setGUI(ImageGUI v, JButton b) {
		videoGUI = v;
		buttonRecover = b;
		/*
		 * if(no==1){ trainVideo=v; } else if(no==2){ predictVideo=v; }
		 */
	}

	public void setGUI(JButton b) {
		buttonRecover = b;
	}

	public void setFile(File f) {
		file = f;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if (cmd.equals("Extract")) {

			// 提取所有视频特征：
			ExtractAllVideos extA = new ExtractAllVideos();
			try {
				extA.exe(videoGUI);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// 每次提取完后启动一个线程重新加载一下特征数据：
			// MyTools.loadFeature();

			buttonRecover.setText(MyConstants.S_Extract);
			// System.out.println("hhhh");
			MainWindow.isRunning = false;
			MainWindow.ExtractButtonState = false;
		} else if (cmd.equals("Train")) {
			// 训练：

			// 合并特征：
			try {
				MyTools.uncoTrainMixFeature();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// 加载特征：
			MyTools.loadFeature();

			try {
				while (MyTools.loadingFeature == true) {
					Thread.sleep(50);
				}
				if (MyTools.loadingFeatureResult == 1) {
					// MySVM.saveTrainDataTest();
					// System.out.println(MySVM.loadTrainData());
					// Classifiers.SVMtrain();
					Classifiers.SVMtrain();
					// 每次训练完后启动一个线程重新加载一下训练模型：
					MyTools.loadSVMModel();
				} else if (MyTools.loadingFeatureResult == 0) {
					System.out.println("训练数据加载失败！");
					MyTools.showTips(
							"训练数据加载失败！\n    提取的特征是残缺的，请重新提取，提取的过程中不要点击终止按钮！", 1);
				} else {
					MyTools.showTips("训练数据加载失败！", 1);
				}

			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainWindow.isRunning = false;
			MainWindow.TrainButtonState = false;
			buttonRecover.setText(MyConstants.S_Train);
		} else if (cmd.equals("COTrain")) {

			// // 训练：
			// try {
			// while(MyTools.loadingFeature==true){
			// Thread.sleep(50);
			// }
			// if(MyTools.loadingFeatureResult==1){
			// // MySVM.saveTrainDataTest();
			// // System.out.println(MySVM.loadTrainData());
			// // Classifiers.SVMtrain();
			// Classifiers.SVMtrain();
			// //每次训练完后启动一个线程重新加载一下训练模型：
			// MyTools.loadSVMModel();
			// }
			// else if(MyTools.loadingFeatureResult==0){
			// System.out.println("训练数据加载失败！");
			// MyTools.showTips("训练数据加载失败！\n    提取的特征是残缺的，请重新提取，提取的过程中不要点击终止按钮！",1);
			// }
			// else{
			// MyTools.showTips("训练数据加载失败！",1);
			// }
			//
			// } catch (NumberFormatException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }catch (InterruptedException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			try {
				CoTraining.start(MainWindow.videoGUI);
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			MainWindow.isRunning = false;
			MainWindow.COTrainButtonState = false;
			buttonRecover.setText(MyConstants.S_co_Train);
		} else if (cmd.equals("SVMPredict")) {

			if (file == null) {
				AviFileFilter aviFile = new AviFileFilter();
				Mp4FileFilter mp4File = new Mp4FileFilter();
				JFileChooser jfc = new JFileChooser(
						MyConstants.dataOfVideosAddress);
				jfc.setFileFilter(mp4File);
				jfc.setFileFilter(aviFile);
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "选择");
				file = jfc.getSelectedFile();
			}

			MainWindow.videoPath.setText(MyConstants.S_videoPath
					+ file.getParent());
			MainWindow.videoName.setText(MyConstants.S_videoName
					+ file.getName());

			while (MyTools.loadingModel == true) {
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				Classifiers.SVMpredict(file.toString(), videoGUI);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// try {
			// Classifiers.KNNpredict(file.toString(),videoGUI);
			// } catch (NumberFormatException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			MainWindow.isRunning = false;
			MainWindow.SVMPredictButtonState = false;
			buttonRecover.setText(MyConstants.S_svm_Predict);
		} else if (cmd.equals("KNNPredict")) {

			if (file == null) {
				AviFileFilter aviFile = new AviFileFilter();
				Mp4FileFilter mp4File = new Mp4FileFilter();
				JFileChooser jfc = new JFileChooser(
						MyConstants.dataOfVideosAddress);
				jfc.setFileFilter(mp4File);
				jfc.setFileFilter(aviFile);
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jfc.showDialog(new JLabel(), "选择");
				file = jfc.getSelectedFile();
			}
			MainWindow.videoPath.setText(MyConstants.S_videoPath
					+ file.getParent());
			MainWindow.videoName.setText(MyConstants.S_videoName
					+ file.getName());
			
			while (MyTools.loadingFeature == true) {
				try {
					MyTools.clearTips();
					MyTools.showTips("KNN分类器加载中，请稍等。。。",1);
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			try {
				Classifiers.KNNpredict(file.toString(), videoGUI);
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// try {
			// Classifiers.KNNpredict(file.toString(),videoGUI);
			// } catch (NumberFormatException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			MainWindow.isRunning = false;
			MainWindow.KNNPredictButtonState = false;
			buttonRecover.setText(MyConstants.S_knn_Predict);
		}
	}

}
