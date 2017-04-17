package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import har.Constants;
import har.Labels;

public class ExtractAllVideos {

	public ExtractAllVideos() {
		// TODO Auto-generated constructor stub
	}
	
	public void exe() throws IOException{
		File f = MyTools.mkdir(MyTrain.data_hog_Address,MyTrain.data_hog_name);//保存路径
		FileWriter fw=new FileWriter(f);
		PrintWriter outAll=new PrintWriter(new BufferedWriter(fw));
		
		for (Labels c : Labels.values()) {
            for(int i=1;i<=c.getNumberOfVideos();i++){
            	 String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
            	 ExtractVideoFeature ext=new ExtractVideoFeature();
         		ext.exe(videoAddress,c,i,outAll);
            }
           
        }
		
		outAll.close();
		
	}

}
