package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import data.MyConstants;
import fileFilter.AviFileFilter;
import fileFilter.Mp4FileFilter;

public class FileChooser extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton open = null;
	String absolutePath;

	public static void main(String[] args) {
		// System.out.println(System.getProperty("user.dir")+"/data/kthdata/");
		// new FileChooser(System.getProperty("user.dir")+"/data/kthdata/");
		// new FileChooser(MyConstants.dataOfVideosAddress);
		// File f=saveFile();
	}

	public FileChooser(String path) {
		absolutePath = path;
		open = new JButton("open");
		this.add(open);
		this.setBounds(400, 200, 100, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		open.addActionListener(this);
	}

	public static File openFile() {
		// 选择一个文件或路径
		AviFileFilter aviFile = new AviFileFilter();
		Mp4FileFilter mp4File = new Mp4FileFilter();
		JFileChooser jfc = new JFileChooser(MyConstants.dataOfVideosAddress);
		jfc.setFileFilter(mp4File);
		jfc.setFileFilter(aviFile);
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "确定");
		File file = jfc.getSelectedFile();
		return file;
	}

	public static File chooseTrainSet() {
		// 选择一个文件或路径
		JFileChooser jfc = new JFileChooser(MyConstants.dataAddress);
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "确定");
		File file = jfc.getSelectedFile();
		return file;
	}

	public static File saveFile() {
		// 选择一个文件或路径进行保存
		// AviFileFilter aviFile = new AviFileFilter();
		// Mp4FileFilter mp4File = new Mp4FileFilter();
		JFileChooser jfc = new JFileChooser(MyConstants.dataOfVideosAddress);
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "保存");
		File file = jfc.getSelectedFile(); // 获得你输入要保存的文件
		// String fileName=file.getName(); //获得文件名
		// String path=file.getAbsolutePath(); //得到要保存文件的路径(包括文件名）
		// String path1=file.getParent();//获取目录（不包括文件名）
		// System.out.println(fileName);
		// System.out.println(path);
		// System.out.println(path1);

		return file;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser jfc = new JFileChooser(absolutePath);
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		if (file.isDirectory()) {
			System.out.println("文件夹:" + file.getAbsolutePath());
		} else if (file.isFile()) {
			System.out.println("文件:" + file.getAbsolutePath());
		}
		System.out.println(jfc.getSelectedFile().getName());

	}

}