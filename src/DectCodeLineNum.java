import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DectCodeLineNum {
	public static boolean emptyLineMode = true;//true表示没有代码的行(回车、空格等)不算，false表示只有回车的一行也算
	public static int lineNum=0;
	public static String adds = "src/data/";

	public DectCodeLineNum() {
		// TODO Auto-generated constructor stub
	}
	
	public static boolean isBlank(char c){
		if(c=='\t'||c==' ')
			return true;
		return false;
	}

	public static boolean hasCode(String s) {
		// 检测该行是否有代码，空格、回车、Tab等不算代码
//		方法一：
		char ss[]=s.toCharArray();
		for (int i = 0; i < ss.length; i++) {
			if(!isBlank(ss[i]))
				return true;
		}

		return false;
		
//		方法二：
	/*	if(s.length()==0)
			return false;
		String[] words = s.split("\t| ");
		if(words.length==0)
			return false;
		return true;*/

	}

	public static int dectLine(String adds) throws IOException {
		// 检测一个文件的代码行数
		File f = new File(adds);// 读取路径
		if (!f.exists()) {
			System.out.println(adds+"  文件不存在！");
			return 0;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(adds)));
		String line = null;
		int lineNum = 0;
		while ((line = in.readLine()) != null) {
			if (emptyLineMode == true) {
				if(hasCode(line))
					lineNum++;
			} else
				lineNum++;
		}
		in.close();
		return lineNum;
	}

	public static void main(String[] args) throws IOException {
		lineNum=0;
		File path = new File(adds);
		if (!path.exists()) {
			System.out.println(adds+"路径不存在！");
			return ;
		}
		String names[] = path.list();// 文件名
		int fileNum = path.list().length;
		ArrayList<String> filenames = new ArrayList<String>();// 路径+文件名
		for (int i = 0; i < fileNum; i++) {
			filenames.add(adds + names[i]);
//			System.out.println(filenames.get(i));
			int num=dectLine(filenames.get(i));			
			lineNum+=num;
			System.out.println(filenames.get(i)+"  : "+num+"  行.");
		}
		System.out.println("\n代码总行数： "+lineNum);

	}

}
