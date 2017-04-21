package fileFilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class AviFileFilter extends FileFilter  {

	public String getDescription() {  
        return "*.avi";  
    }  
  
    public boolean accept(File file) {
    	if (file.isDirectory())
			return true;
        String name = file.getName();  
        return name.toLowerCase().endsWith(".avi");  
    }  

}
