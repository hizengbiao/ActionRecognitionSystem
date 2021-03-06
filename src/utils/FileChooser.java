package utils;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import data.MyConstants;

public class FileChooser extends JFrame implements ActionListener{  
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton open=null;
    String absolutePath;
    public static void main(String[] args) {
//    	System.out.println(System.getProperty("user.dir")+"/data/kthdata/");
//        new FileChooser(System.getProperty("user.dir")+"/data/kthdata/");
        new FileChooser(MyConstants.dataOfVideosAddress);
    }  
    public FileChooser(String path){
    	absolutePath=path;
        open=new JButton("open");  
        this.add(open);  
        this.setBounds(400, 200, 100, 100);  
        this.setVisible(true);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        open.addActionListener(this);  
    }  
    @Override  
    public void actionPerformed(ActionEvent e) {  
        // TODO Auto-generated method stub
        JFileChooser jfc=new JFileChooser(absolutePath);  
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
        jfc.showDialog(new JLabel(), "选择");
        File file=jfc.getSelectedFile();  
        if(file.isDirectory()){  
            System.out.println("文件夹:"+file.getAbsolutePath());  
        }else if(file.isFile()){  
            System.out.println("文件:"+file.getAbsolutePath());  
        }  
        System.out.println(jfc.getSelectedFile().getName());  
          
    }  
  
}  