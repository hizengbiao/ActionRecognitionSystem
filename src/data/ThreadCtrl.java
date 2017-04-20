package data;

import har.Constants;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;

public class ThreadCtrl  implements Runnable {
	String cmd=null;
	public static boolean isRunning=false;//线程是否在启动中
	
	ImageGUI videoGUI;
//	ImageGUI predictVideo;

	public ThreadCtrl(String command) {
		// TODO Auto-generated constructor stub
		cmd=command;
	}
	
	public void setGUI(ImageGUI v){
		videoGUI=v;
		/*if(no==1){
			trainVideo=v;
		}
		else if(no==2){
			predictVideo=v;
		}*/
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		if(cmd.equals("Extract")){
			if(isRunning==true){
				System.out.println("已经有线程在启动！");
				return;
			}
				
			isRunning=true;
			//提取所有视频特征：
			ExtractAllVideos extA=new ExtractAllVideos();
			try {
				extA.exe(videoGUI);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			isRunning=false;
		}
		else if(cmd.equals("Train")){
			if(isRunning==true){
				System.out.println("已经有线程在启动！");
				return;
			}
			isRunning=true;

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
			}
			isRunning=false;
		}
		else if(cmd.equals("Predict")){
			if(isRunning==true){
				System.out.println("已经有线程在启动！");
				return;
			}
			isRunning=true;
			JFileChooser jfc=new JFileChooser(Constants.dataOfVideosAddress);  
	        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
	        jfc.showDialog(new JLabel(), "选择");
	        File file=jfc.getSelectedFile();
	        MySVM.predict(file.toString(),videoGUI);
	        isRunning=false;
		}
	}

}
