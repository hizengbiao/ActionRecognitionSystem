package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.opencv.core.Core;

public class Start {
	public static void main(String args[]) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String videoFile = "data/2.avi";
		ExtractVideoFeature ext=new ExtractVideoFeature();
		ext.exe(videoFile);
		
		
		/*FileWriter fw=new FileWriter("add.txt",true);
		PrintWriter out=new PrintWriter(new BufferedWriter(fw));
		out.println();
		out.close();*/
		
		/*try {
			File f = new File("add.txt"); 
			if (!f.exists()) {  
				System.out.print("文件不存在");  
                f.createNewFile();// 不存在则创建
            }  
			BufferedWriter output = new BufferedWriter(new FileWriter(f));
//			FileOutputStream out = new FileOutputStream(f);
			
//			output.write("hahaha\nhahaha");
			for(int i=0;i<100;i++){
			output.append(i+"\n");}
			 output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		

	}

}
