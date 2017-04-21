package data;

import har.Labels;

import java.io.File;
import java.io.IOException;

public class MyTools {

	public MyTools() {
		// TODO Auto-generated constructor stub
	}
	
	public static File mkdir(String dir,String fileName){
		File f1 = new File(dir);
		File f = new File(dir+fileName);//保存路径
		if (!f1.exists()) {
			f1.mkdirs();
        }  
		if (!f.exists()) {  
//			System.out.print("文件不存在");
            try {
				f.createNewFile();// 不存在则创建
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
		return f;
	}
	
	public static void clearTips(){
		MainWindow.tips.setText("\n");
	}
	public static void showTips(String s,int sign){
		if(sign==1){
			MainWindow.tips.append("\n "+s+"\n");
		}		
	}
	
	public static void showTips(String s){
		MainWindow.tips.append("      "+s+"\n");
	}

}
