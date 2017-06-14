package data;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import utils.ImageGUI;
import utils.MyTools;

public class MainWindow extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public static JButton Extract = new JButton(MyConstants.S_Extract); // 提取特征按钮
	public static JButton Train = new JButton(MyConstants.S_Train); // 按钮
	public static JButton COTrain = new JButton(MyConstants.S_co_Train); // 按钮
	public static JButton SVMPredict = new JButton(MyConstants.S_svm_Predict); // 按钮
	public static JButton KNNPredict = new JButton(MyConstants.S_knn_Predict); // 按钮
	private JButton Terminate = new JButton("退出系统");// 终止按钮
	private JButton RecogRate = new JButton("识别率");// 识别率按钮
	public static JButton speedUp = new JButton("加速");
	public static JButton speedDown = new JButton("减速");
	public static JButton videoPause = new JButton("暂停");
	
	public static JButton videoPlay = new JButton("播放视频");

	private JButton myDebug = new JButton("调试按钮");

	public static JLabel optionStatus = new JLabel(MyConstants.S_optionStatus);
	public static JLabel videoPath = new JLabel(MyConstants.S_videoPath);// 视频路径
	public static JLabel videoName = new JLabel(MyConstants.S_videoName);// 视频文件名
	// public static JLabel videoFrame = new JLabel(MyConstants.S_frame);
	public static JLabel videoFrame = new JLabel(MyConstants.S_frame);

	public static JLabel videoCapture = new JLabel(MyConstants.S_videoCapture);
	public static JLabel Console = new JLabel(MyConstants.S_Console);

	public static TextArea tips = new TextArea();

	public static boolean ExtractButtonState = false;
	public static boolean TrainButtonState = false;
	public static boolean COTrainButtonState = false;
	public static boolean SVMPredictButtonState = false;
	public static boolean KNNPredictButtonState = false;
	public static Thread myThread = null;
	public static boolean isRunning = false;// 线程是否在启动中

	/*
	 * ImageGUI trainVideo=new ImageGUI(); ImageGUI predictVideo=new ImageGUI();
	 */
	public static ImageGUI videoGUI = new ImageGUI();

	public MainWindow() {
		// TODO Auto-generated constructor stub
	}

	public static void HiddenJLabels() {
		optionStatus.setVisible(false);
		videoPath.setVisible(false);
		videoName.setVisible(false);
		videoFrame.setVisible(false);
	}

	public static void HiddenJLabels(int state) {
		if (state == 1) {
			// extract and predict condition
			optionStatus.setVisible(true);
			videoPath.setVisible(true);
			videoName.setVisible(true);
			videoFrame.setVisible(true);
		} else if (state == 2) {
			// train condition
			optionStatus.setVisible(true);
			videoPath.setVisible(false);
			videoName.setVisible(false);
			videoFrame.setVisible(false);
		}
	}

	public void lanuchWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("视频动作识别系统");
		this.setSize(690, 600);
		this.setLocation((screenSize.width - this.getWidth()) / 2,
				(screenSize.height - this.getHeight()) / 2);
		// setBounds(300, 150, 700, 550);

		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("images\\bg.jpg").getImage(), 0, 0,
						getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		this.setLayout(null);

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				System.exit(0);
			}
		});

		// Buttons:
		int sx = 60;
		int sy = 40;
		int wid = 120;
		int hei = 40;
		int di = 10;
		Extract.setBounds(sx,sy + 25 , wid, hei);
		this.add(Extract);
		Extract.addActionListener(this);
		Extract.setVisible(false);
		
		Train.setBounds(sx + wid * 1 + di * 1, sy, wid, hei);
		this.add(Train);
		
		Train.setVisible(false);
		
		COTrain.addActionListener(this);
//		COTrain.setBounds(sx + wid * 1 + di * 1, sy + hei + di, wid, hei);
		
		COTrain.setBounds(sx + wid * 1 + di * 1,sy + 25, wid, hei);
		
		this.add(COTrain);
		Train.addActionListener(this);
		SVMPredict.setBounds(sx + wid * 2 + di * 2, sy, wid, hei);
		this.add(SVMPredict);
		SVMPredict.addActionListener(this);
		
		SVMPredict.setVisible(false);
		
		KNNPredict.setBounds(sx + wid * 2 + di * 2+50, sy + 25, wid, hei);
		
//		KNNPredict.setBounds(sx + wid * 2 + di * 2, sy + hei + di, wid, hei);
		this.add(KNNPredict);
		KNNPredict.addActionListener(this);
		Terminate.setBounds(sx + wid * 3 + di * 3, sy + 25, wid, hei);
		this.add(Terminate);
		Terminate.addActionListener(this);
		Terminate.setVisible(false);
		
		
		videoPlay.setBounds(sx + wid * 3 + di * 3, sy + 25, wid, hei);
		this.add(videoPlay);
		videoPlay.addActionListener(this);
		
		
		RecogRate.setBounds(sx + wid * 3 + di * 3, sy + 25, wid, hei);
		this.add(RecogRate);
		RecogRate.addActionListener(this);
		RecogRate.setVisible(false);

		videoCapture.setBounds(136, sy + hei * 2 + di * 2, 100, 20);
		this.add(videoCapture);
		Console.setBounds(476, sy + hei * 2 + di * 2, 100, 20);
		this.add(Console);

		videoGUI.setBounds(27, sy + hei * 2 + di * 3 + 20, 300, 200);
		/*
		 * trainVideo.createWin("OpenCV + Java视频读与播放演示", new Dimension(300,
		 * 220));
		 */
		videoGUI.setMainWin(this);
		this.add(videoGUI);

		// textArea tips:
		tips.setBounds(357, sy + hei * 2 + di * 3 + 20, 300, 200);
		tips.setEditable(false);
		this.add(tips);

		speedUp.setBounds(230, sy + hei * 2 + di * 3 + 30+200, 60, 30);
		this.add(speedUp);
		speedUp.addActionListener(this);
		videoPause.setBounds(150, sy + hei * 2 + di * 3 + 30+200, 60, 30);
		this.add(videoPause);
		videoPause.addActionListener(this);
		speedDown.setBounds(70, sy + hei * 2 + di * 3 + 30+200, 60, 30);
		this.add(speedDown);
		speedDown.addActionListener(this);

		myDebug.setBounds(450, 400, 100, 30);
		this.add(myDebug);
		myDebug.addActionListener(this);
		
		myDebug.setVisible(false);

		// Jlabels:

		int firstLable_x = 20;
		int firstLable_y =  sy + hei * 2 + di * 3 + 90+200;
		int y_interval = 30;

		optionStatus.setBounds(firstLable_x, firstLable_y, 300, 20);
		this.add(optionStatus);
		videoPath.setBounds(firstLable_x, firstLable_y + y_interval * 1, 650,
				20);
		this.add(videoPath);
		videoName.setBounds(firstLable_x, firstLable_y + y_interval * 2, 300,
				20);
		this.add(videoName);
		videoFrame.setBounds(firstLable_x, firstLable_y + y_interval * 3, 300,
				20);
		this.add(videoFrame);

		HiddenJLabels();

		/*
		 * predictVideo.setBounds(330, 330, 300, 200);
		 * predictVideo.setMainWin(this); this.add(predictVideo);
		 */

		setVisible(true);
		this.setResizable(false);// 窗口大小不可调整

		// //加载数据：
		MyTools.loadFeature();
//		MyTools.loadSVMModel();
		//
		//

	}

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == videoPlay) {
			MyTools.clearTips();
			MyTools.videoPlay();
			videoPause.setText("暂停");
			HiddenJLabels();
			HiddenJLabels(1);
			optionStatus.setText(MyConstants.S_optionStatus
					+ "视频播放");

//			KNNPredict.setText(MyConstants.S_Terminate
//					+ MyConstants.S_knn_Predict);

			ThreadCtrl ctrl = new ThreadCtrl("vidPlay");
			ctrl.setGUI(videoGUI, videoPlay);
			myThread = new Thread(ctrl);
			myThread.start();
		}
		else if (e.getSource() == Extract) {
			if (ExtractButtonState == false) {
				if (isRunning == true) {
					System.out.println(MyConstants.ThreadConflictMsg);
					// MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning = true;
				ExtractButtonState = true;

				MyTools.clearTips();
				MyTools.videoPlay();
				videoPause.setText("暂停");
				HiddenJLabels();
				HiddenJLabels(1);
				optionStatus.setText(MyConstants.S_optionStatus
						+ MyConstants.S_Extract);

				Extract.setText(MyConstants.S_Terminate + MyConstants.S_Extract);
				ThreadCtrl ctrl = new ThreadCtrl("Extract");
				ctrl.setGUI(videoGUI, Extract);
				myThread = new Thread(ctrl);
				myThread.start();
			} else {
				// 关闭线程
				ExtractButtonState = false;
				Extract.setText(MyConstants.S_Extract);
				myThread.stop();
				myThread = null;
				isRunning = false;

			}
		} else if (e.getSource() == Train) {

			if (TrainButtonState == false) {
				if (isRunning == true) {
					System.out.println(MyConstants.ThreadConflictMsg);
					// MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning = true;
				TrainButtonState = true;

				MyTools.clearTips();
				HiddenJLabels();
				HiddenJLabels(2);
				optionStatus.setText(MyConstants.S_optionStatus
						+ MyConstants.S_Train);

				Train.setText(MyConstants.S_Terminate + MyConstants.S_Train);

				ThreadCtrl ctrl = new ThreadCtrl("Train");
				ctrl.setGUI(Train);
				myThread = new Thread(ctrl);
				myThread.start();
			} else {
				// 关闭线程
				TrainButtonState = false;
				Train.setText(MyConstants.S_Train);
				myThread.stop();
				myThread = null;
				isRunning = false;
			}

			/*
			 * // 训练： try { if(MySVM.loadTrainData()) //
			 * MySVM.saveTrainDataTest(); //
			 * System.out.println(MySVM.loadTrainData()); MySVM.train(); else
			 * System.out.println("训练数据加载失败！"); } catch (NumberFormatException
			 * e1) { // TODO Auto-generated catch block e1.printStackTrace(); }
			 * catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */

		} else if (e.getSource() == COTrain) {

			if (COTrainButtonState == false) {
				if (isRunning == true) {
					System.out.println(MyConstants.ThreadConflictMsg);
					// MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning = true;
				COTrainButtonState = true;

				MyTools.clearTips();
				HiddenJLabels();
				HiddenJLabels(1);
				optionStatus.setText(MyConstants.S_optionStatus
						+ MyConstants.S_co_Train);

				COTrain.setText(MyConstants.S_Terminate
						+ MyConstants.S_co_Train);

				ThreadCtrl ctrl = new ThreadCtrl("COTrain");
				ctrl.setGUI(COTrain);
				myThread = new Thread(ctrl);
				myThread.start();
			} else {
				// 关闭线程
				COTrainButtonState = false;
				COTrain.setText(MyConstants.S_co_Train);
				myThread.stop();
				myThread = null;
				isRunning = false;
			}

			/*
			 * // 训练： try { if(MySVM.loadTrainData()) //
			 * MySVM.saveTrainDataTest(); //
			 * System.out.println(MySVM.loadTrainData()); MySVM.train(); else
			 * System.out.println("训练数据加载失败！"); } catch (NumberFormatException
			 * e1) { // TODO Auto-generated catch block e1.printStackTrace(); }
			 * catch (IOException e1) { // TODO Auto-generated catch block
			 * e1.printStackTrace(); }
			 */

		} else if (e.getSource() == SVMPredict) {
			// 预测：
			/*
			 * Labels c=Labels.BOXING; // int i=69; // int i=72; int i=29;
			 * String
			 * videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+
			 * c.getName()+"_"+i+".avi";
			 * MySVM.predict(videoAddress,predictVideo);
			 */

			/*
			 * JFileChooser jfc=new JFileChooser(Constants.dataOfVideosAddress);
			 * jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
			 * jfc.showDialog(new JLabel(), "选择"); File
			 * file=jfc.getSelectedFile();
			 * MySVM.predict(file.toString(),predictVideo);
			 */

			if (SVMPredictButtonState == false) {
				if (isRunning == true) {
					System.out.println(MyConstants.ThreadConflictMsg);
					// MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning = true;
				SVMPredictButtonState = true;

				MyTools.clearTips();
				MyTools.videoPlay();
				videoPause.setText("暂停");
				HiddenJLabels();
				HiddenJLabels(1);
				optionStatus.setText(MyConstants.S_optionStatus
						+ MyConstants.S_svm_Predict);

				SVMPredict.setText(MyConstants.S_Terminate
						+ MyConstants.S_svm_Predict);

				ThreadCtrl ctrl = new ThreadCtrl("SVMPredict");
				ctrl.setGUI(videoGUI, SVMPredict);
				myThread = new Thread(ctrl);
				myThread.start();
			} else {
				// 关闭线程
				SVMPredictButtonState = false;
				SVMPredict.setText(MyConstants.S_svm_Predict);
				myThread.stop();
				myThread = null;
				isRunning = false;
			}

		} else if (e.getSource() == KNNPredict) {
			// 预测：
			/*
			 * Labels c=Labels.BOXING; // int i=69; // int i=72; int i=29;
			 * String
			 * videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+
			 * c.getName()+"_"+i+".avi";
			 * MySVM.predict(videoAddress,predictVideo);
			 */

			/*
			 * JFileChooser jfc=new JFileChooser(Constants.dataOfVideosAddress);
			 * jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
			 * jfc.showDialog(new JLabel(), "选择"); File
			 * file=jfc.getSelectedFile();
			 * MySVM.predict(file.toString(),predictVideo);
			 */

			if (KNNPredictButtonState == false) {
				if (isRunning == true) {
					System.out.println(MyConstants.ThreadConflictMsg);
					// MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning = true;
				KNNPredictButtonState = true;

				MyTools.clearTips();
				MyTools.videoPlay();
				videoPause.setText("暂停");
				HiddenJLabels();
				HiddenJLabels(1);
				optionStatus.setText(MyConstants.S_optionStatus
						+ MyConstants.S_knn_Predict);

				KNNPredict.setText(MyConstants.S_Terminate
						+ MyConstants.S_knn_Predict);

				ThreadCtrl ctrl = new ThreadCtrl("KNNPredict");
				ctrl.setGUI(videoGUI, KNNPredict);
				myThread = new Thread(ctrl);
				myThread.start();
			} else {
				// 关闭线程
				KNNPredictButtonState = false;
				KNNPredict.setText(MyConstants.S_knn_Predict);
				myThread.stop();
				myThread = null;
				isRunning = false;
			}

		} else if (e.getSource() == Terminate) {
			ExtractButtonState = false;
			Extract.setText(MyConstants.S_Extract);
			TrainButtonState = false;
			Train.setText(MyConstants.S_Train);
			COTrainButtonState = false;
			COTrain.setText(MyConstants.S_co_Train);
			KNNPredictButtonState = false;
			KNNPredict.setText(MyConstants.S_knn_Predict);
			SVMPredictButtonState = false;
			SVMPredict.setText(MyConstants.S_svm_Predict);
			if (myThread != null)
				myThread.stop();
			myThread = null;
			isRunning = false;
		}
		else if (e.getSource() == RecogRate) {
			MyTools.RecognitionRateCalcCtrl();
		}
		else if (e.getSource() == speedUp) {
			MyTools.speedUp();
		} else if (e.getSource() == speedDown) {
			MyTools.speedDown();
		} else if (e.getSource() == videoPause) {
			if (MyTools.isPause()) {
				videoPause.setText("暂停");
				MyTools.videoPlay();
			} else {
				videoPause.setText("播放");
				MyTools.videoPause();
			}
		} else if (e.getSource() == myDebug) {
			
			MyTools.testConsole();
//			 String
//			 add=MyConstants.dataOfVideosAddress+"jogging/jogging_10.avi";
//			 MyTools.playVideo(add);
			 

			/*
			 * try { MyTools.ExtractAndTrain(); } catch (IOException e1) { //
			 * TODO Auto-generated catch block e1.printStackTrace(); }
			 */

//			MyTools.test();
			
//			MyTools.RecognitionRateCalcCtrl();
			
			
			
//			// knn 特征及标签文件：
//			
//			File f_knn_data_tem = MyTools.mkdir(Classifiers.data_hog_Address,
//					Classifiers.knn_data_tem);// 保存路径
//			File f_knn_label_tem = MyTools.mkdir(Classifiers.data_hog_Address,
//					Classifiers.knn_label_tem);// 保存路径
//			
//			File f_knn_data = MyTools.mkdir(Classifiers.data_hog_Address,
//					Classifiers.knn_data);// 保存路径
//			File f_knn_label = MyTools.mkdir(Classifiers.data_hog_Address,
//					Classifiers.knn_label);// 保存路径
//			try {
//				MyTools.mixFeatures(f_knn_data_tem,f_knn_label_tem,f_knn_data,f_knn_label,MyConstants.TrainVideoCount+1);
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			MyTools.loadFeature();
		}
	}

}
