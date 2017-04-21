package data;

import fileFilter.AviFileFilter;
import fileFilter.Mp4FileFilter;
import har.Constants;

import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class ThreadCtrl  implements Runnable {
	String cmd=null;
	
	
	ImageGUI videoGUI;
	JButton buttonRecover;
//	ImageGUI predictVideo;

	public ThreadCtrl(String command) {
		// TODO Auto-generated constructor stub
		cmd=command;
	}
	
	public void setGUI(ImageGUI v,JButton b){
		videoGUI=v;
		buttonRecover=b;
		/*if(no==1){
			trainVideo=v;
		}
		else if(no==2){
			predictVideo=v;
		}*/
	}
	
	public void setGUI(JButton b){
		buttonRecover=b;
	}
		

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if(cmd.equals("Extract")){
			
			//提取所有视频特征：
			ExtractAllVideos extA=new ExtractAllVideos();
			try {
				extA.exe(videoGUI);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			buttonRecover.setText(MyConstants.S_Extract);
			MainWindow.isRunning=false;
			MainWindow.ExtractButtonState=false;
		}
		else if(cmd.equals("Train")){
			
//			训练：
			try {
				int si=MySVM.loadTrainData();
				if(si==1)
//				MySVM.saveTrainDataTest();
//				System.out.println(MySVM.loadTrainData());
				MySVM.train();
				else if(si==0){
					System.out.println("训练数据加载失败！");
					MyTools.showTips("训练数据加载失败！\n    提取的特征是残缺的，请重新提取，提取的过程中不要点击终止按钮！",1);
				}
				else{
					MyTools.showTips("训练数据加载失败！",1);
				}
					
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			MainWindow.isRunning=false;
			MainWindow.TrainButtonState=false;
			buttonRecover.setText(MyConstants.S_Train);
		}
		else if(cmd.equals("Predict")){
			
			AviFileFilter aviFile=new AviFileFilter();
			Mp4FileFilter mp4File= new Mp4FileFilter();
			JFileChooser jfc=new JFileChooser(MyConstants.dataOfVideosAddress);
			jfc.setFileFilter(mp4File);
			jfc.setFileFilter(aviFile);
	        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
	        jfc.showDialog(new JLabel(), "选择");
	        File file=jfc.getSelectedFile();
	        
	        MainWindow.videoPath.setText(MyConstants.S_videoPath+file.getParent());
       	 	MainWindow.videoName.setText(MyConstants.S_videoName+file.getName());
	        
	        MySVM.predict(file.toString(),videoGUI);
	        MainWindow.isRunning=false;
	        MainWindow.PredictButtonState=false;
	        buttonRecover.setText(MyConstants.S_Predict);
		}
	}

}
