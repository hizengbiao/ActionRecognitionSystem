package fileFilter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class Mp4FileFilter extends FileFilter {

	public String getDescription() {
		return "*.mp4";
	}

	public boolean accept(File file) {
		if (file.isDirectory())
			return true;
		String name = file.getName();
		return name.toLowerCase().endsWith(".mp4");
	}

}
