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
	
	public void exe(ImageGUI VideoShow) throws IOException{
		File f = MyTools.mkdir(MySVM.data_hog_Address,MySVM.data_hog_name);//保存路径
		FileWriter fw=new FileWriter(f);
		PrintWriter outAll=new PrintWriter(new BufferedWriter(fw));
		
		
		
		File f2 = MyTools.mkdir(MySVM.data_hog_Address,MySVM.data_hog_label);//保存路径
		FileWriter fw2=new FileWriter(f2);
		PrintWriter outAll_label=new PrintWriter(new BufferedWriter(fw2));
		
		for (Labels c : Labels.values()) {
		/*for(int y=0;y<1;y++){
			Labels c=Labels.BOXING;*/
			for(int i=1;i<=40;i++){
//            for(int i=1;i<=c.getNumberOfVideos();i++){
            	 String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
            	 ExtractVideoFeature ext=new ExtractVideoFeature();
         		ext.exe(videoAddress,c,i,outAll,outAll_label,VideoShow);
            }
           
        }
		
		outAll.close();
		outAll_label.close();
	}

}
