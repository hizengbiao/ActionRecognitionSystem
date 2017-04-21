package data;

import har.Constants;
import har.Labels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainWindow  extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JButton Extract = new JButton(MyConstants.S_Extract); // 提取特征按钮
	private JButton Train = new JButton(MyConstants.S_Train); // 按钮
	private JButton Predict = new JButton(MyConstants.S_Predict); // 按钮
	private JButton Terminate=new JButton("终止所有");//终止按钮
	
	public static JLabel optionStatus = new JLabel(MyConstants.S_optionStatus);
	public static JLabel videoPath = new JLabel(MyConstants.S_videoPath);//视频路径
	public static JLabel videoName = new JLabel(MyConstants.S_videoName);//视频文件名
//	public static JLabel videoFrame = new JLabel(MyConstants.S_frame);
	public static JLabel videoFrame = new JLabel(MyConstants.S_frame);
	
	public static JLabel videoCapture = new JLabel(MyConstants.S_videoCapture);
	public static JLabel Console = new JLabel(MyConstants.S_Console);
			
	public static TextArea tips=new TextArea();
	
	public static boolean ExtractButtonState=false;
	public static boolean TrainButtonState=false;
	public static boolean PredictButtonState=false;
	Thread myThread=null;
	public static boolean isRunning=false;//线程是否在启动中
	
	/*ImageGUI trainVideo=new ImageGUI();
	ImageGUI predictVideo=new ImageGUI();*/
	ImageGUI videoGUI=new ImageGUI();

	public MainWindow() {
		// TODO Auto-generated constructor stub
	}
	
	public static void HiddenJLabels(){
		optionStatus.setVisible(false);
		videoPath.setVisible(false);
		videoName.setVisible(false);
		videoFrame.setVisible(false);
	}
	
	public static void HiddenJLabels(int state){
		if(state==1){
			//extract and predict condition
			optionStatus.setVisible(true);
			videoPath.setVisible(true);
			videoName.setVisible(true);
			videoFrame.setVisible(true);
		}
		else if(state==2){
			//train condition
			optionStatus.setVisible(true);
			videoPath.setVisible(false);
			videoName.setVisible(false);
			videoFrame.setVisible(false);
		}
	}
	
	public void lanuchWindow(){
		Dimension screenSize =Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("视频动作识别系统");
		this.setSize(690, 530);
		this.setLocation((screenSize.width-this.getWidth())/2, (screenSize.height-this.getHeight())/2);
//		setBounds(300, 150, 700, 550);
		
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(
						"images\\bg.jpg").getImage(), 0,
						0, getWidth(), getHeight(), null);
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
		
		//Buttons:
		
		Extract.setBounds(50, 50, 120, 40);
		this.add(Extract);
		Extract.addActionListener(this);
		Train.setBounds(190, 50, 100, 40);
		this.add(Train);
		Train.addActionListener(this);
		Predict.setBounds(300, 50, 100, 40);
		this.add(Predict);
		Predict.addActionListener(this);
		Terminate.setBounds(450, 50, 100, 40);
		this.add(Terminate);
		Terminate.addActionListener(this);
		
		videoGUI.setBounds(20, 150, 300, 200);
		/*trainVideo.createWin("OpenCV + Java视频读与播放演示", new Dimension(300,
				220));*/
		videoGUI.setMainWin(this);
		this.add(videoGUI);
		
		//Jlabels:
		
		int firstLable_x=20;
		int firstLable_y=350;
		int y_interval=30;

		optionStatus.setBounds(firstLable_x, firstLable_y, 300, 20);
		this.add(optionStatus);		
		videoPath.setBounds(firstLable_x, firstLable_y+y_interval*1, 650, 20);
		this.add(videoPath);		
		videoName.setBounds(firstLable_x, firstLable_y+y_interval*2, 300, 20);
		this.add(videoName);		
		videoFrame.setBounds(firstLable_x, firstLable_y+y_interval*3, 300, 20);
		this.add(videoFrame);
		
		HiddenJLabels();
		
		videoCapture.setBounds(130, 120, 100, 20);
		this.add(videoCapture);	
		Console.setBounds(470, 120, 100, 20);
		this.add(Console);
		
		//textArea tips:
		tips.setBounds(350, 150, 300, 200);
		tips.setEditable(false);
		this.add(tips);
	/*	
		predictVideo.setBounds(330, 330, 300, 200);
		predictVideo.setMainWin(this);
		this.add(predictVideo);*/
		
		
		
		setVisible(true);
		this.setResizable(false);//窗口大小不可调整
		
		
		
		
		
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == Extract) {
			if(ExtractButtonState==false){
				if(isRunning==true){
					System.out.println(MyConstants.ThreadConflictMsg);
//					MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning=true;
				ExtractButtonState=true;
				
				MyTools.clearTips();
				HiddenJLabels();
				HiddenJLabels(1);
				optionStatus.setText(MyConstants.S_optionStatus+MyConstants.S_Extract);
				
				Extract.setText(MyConstants.S_Terminate+MyConstants.S_Extract);
				ThreadCtrl ctrl = new ThreadCtrl("Extract");
				ctrl.setGUI(videoGUI,Extract);
				myThread=new Thread(ctrl);
				myThread.start();
			}
			else{
				//关闭线程
				ExtractButtonState=false;
				Extract.setText(MyConstants.S_Extract);
				myThread.stop();
				myThread=null;
				isRunning=false;
				
			}
		} else if (e.getSource() == Train) {
			
			if(TrainButtonState==false){
				if(isRunning==true){
					System.out.println(MyConstants.ThreadConflictMsg);
//					MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning=true;
				TrainButtonState=true;
				
				MyTools.clearTips();
				HiddenJLabels();
				HiddenJLabels(2);
				optionStatus.setText(MyConstants.S_optionStatus+MyConstants.S_Train);
				
				Train.setText(MyConstants.S_Terminate+MyConstants.S_Train);

				ThreadCtrl ctrl = new ThreadCtrl("Train");
				ctrl.setGUI(Train);
				myThread=new Thread(ctrl);
				myThread.start();
			}
			else{
				//关闭线程
				TrainButtonState=false;
				Train.setText(MyConstants.S_Train);
				myThread.stop();
				myThread=null;
				isRunning=false;
			}
			
			
			/*
//			训练：
			try {
				if(MySVM.loadTrainData())
//				MySVM.saveTrainDataTest();
//				System.out.println(MySVM.loadTrainData());
				MySVM.train();
				else
					System.out.println("训练数据加载失败！");
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			
		}
		else if(e.getSource() == Predict){
//			预测：
			/*Labels c=Labels.BOXING;
//			int i=69;
//			int i=72;
			int i=29;
			String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
			MySVM.predict(videoAddress,predictVideo);
			*/
			
		/*	JFileChooser jfc=new JFileChooser(Constants.dataOfVideosAddress);  
	        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
	        jfc.showDialog(new JLabel(), "选择");
	        File file=jfc.getSelectedFile();
	        MySVM.predict(file.toString(),predictVideo);*/
	        
			if(PredictButtonState==false){
				if(isRunning==true){
					System.out.println(MyConstants.ThreadConflictMsg);
//					MainWindow.tips.append(MyConstants.ThreadConflictMsg);
					MyTools.showTips(MyConstants.ThreadConflictMsg);
					return;
				}
				isRunning=true;
				PredictButtonState=true;
				
				MyTools.clearTips();
				HiddenJLabels();
				HiddenJLabels(1);
				optionStatus.setText(MyConstants.S_optionStatus+MyConstants.S_Predict);
				
				Predict.setText(MyConstants.S_Terminate+MyConstants.S_Predict);


		        ThreadCtrl ctrl = new ThreadCtrl("Predict");
		        ctrl.setGUI(videoGUI,Predict);
		        myThread=new Thread(ctrl);
		        myThread.start();
			}
			else{
				//关闭线程
				PredictButtonState=false;
				Predict.setText(MyConstants.S_Predict);
				myThread.stop();
				myThread=null;
				isRunning=false;
			}
	        
			
		}
		else if(e.getSource() == Terminate){
			ExtractButtonState=false;
			Extract.setText(MyConstants.S_Extract);
			TrainButtonState=false;
			Train.setText(MyConstants.S_Train);
			PredictButtonState=false;
			Predict.setText(MyConstants.S_Predict);
			myThread.stop();
			myThread=null;
			isRunning=false;
		}
	}

}
