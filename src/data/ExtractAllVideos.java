package data;

import java.io.IOException;

import har.Constants;
import har.Labels;

public class ExtractAllVideos {

	public ExtractAllVideos() {
		// TODO Auto-generated constructor stub
	}
	
	public void exe() throws IOException{
		for (Labels c : Labels.values()) {
			c=Labels.HANDCLAPPING;
            for(int i=1;i<=c.getNumberOfVideos();i++){
            	 String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
            	 ExtractVideoFeature ext=new ExtractVideoFeature();
         		ext.exe(videoAddress,c,i);
            }
           
        }
		
	}

}
