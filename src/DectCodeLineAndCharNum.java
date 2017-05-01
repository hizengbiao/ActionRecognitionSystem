import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DectCodeLineAndCharNum {
//	author:曾彪
//	时间：_2017_5_1
//	注：1.一个英文字母、一个汉字、一个空格、一个制表符等，都只算一个字符，另外在emptyLineMode==false模式下回车也算一个字符
//	2.如果选择了emptyLineMode==false模式，空格、制表符、回车等的数量都计入总字符数，如果某一个代码行为空或者全为空格或制表符，也计入总行数
//	如果选择了emptyLineMode==false模式，则不计入。
	
	
	public static String excludeId="8zk3kidl3dld.x34klki.j";
	public static boolean excludeSign;//排除本文件的行数和字符数，该项不要更改
	public static boolean emptyLineMode = false;//true表示没有代码的行(回车、空格等)不算，false表示只有回车的一行也算
	public static String adds = "src/";//文件夹路径，程序将计算该路径下所有代码文件的行数和字符数
	public static int charNum;
	public static int lineNum;

	public DectCodeLineAndCharNum() {
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
		/*char ss[]=s.toCharArray();
		for (int i = 0; i < ss.length; i++) {
			if(!isBlank(ss[i]))
				return true;
		}

		return false;*/
		
//		方法二：
		if(s.length()==0)
			return false;
		String[] words = s.split("\t| ");
		if(words.length==0)
			return false;
		return true;

	}
	

	public static int dectFileLine(String ad) throws IOException {
		// 检测一个文件的代码行数
		File f = new File(ad);// 读取路径
		if (!f.exists()) {
			System.out.println(ad+"  文件不存在！");
			return 0;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(ad)));
		String line = null;
		int lineNum = 0;
		while ((line = in.readLine()) != null) {
			if(line.contains(excludeId))
				excludeSign=true;
			if (emptyLineMode == true) {
				if(hasCode(line))
					lineNum++;
			} else
				lineNum++;
		}
		in.close();
		return lineNum;
	}
	

	public static int dectPathLine(String ad) throws IOException{
//		检测一个文件夹内的代码行数
		int fileLineNum=0;
		File path = new File(ad);
		if (!path.exists()) {
			System.out.println(ad+"路径不存在！");
			return 0;
		}
		
		if(!path.isDirectory()){
			excludeSign=false;
			int num=dectFileLine(ad);
			if(excludeSign==false){
			System.out.println(ad+"  : "+num+"  行.");
			}
			return num;
		}
		
		String prenames[] = path.list();// 文件名
		int fileNum = path.list().length;
		ArrayList<String> filenames = new ArrayList<String>();// 路径+文件名
//		ArrayList<String> names = new ArrayList<String>();// 文件名
		ArrayList<Integer> nos = new ArrayList<Integer>();// 上面两个List中文件是非目录的序号
		for (int i = 0; i < fileNum; i++) {
			filenames.add(ad +'/'+ prenames[i]);
//			names.add(prenames[i]);
			File fi=new File(filenames.get(i));
			if(!fi.isDirectory()){
				nos.add(i);
//				System.out.println(filenames.get(i));
			}
//			System.out.println(i+"  : "+filenames.get(i));
		}
		
		
		/*for(int i=0;i<filenames.size();i++){
			System.out.println(i+"  : "+filenames.get(i));
		}*/
		
		
		for(int i=nos.size()-1;i>=0;i--){
//			System.out.println(filenames.get(i));
			excludeSign=false;
			int num=dectFileLine(filenames.get(nos.get(i)));
			if(excludeSign==false){
			fileLineNum+=num;
			System.out.println(filenames.get(nos.get(i))+"  : "+num+"  行.");
			}int pos=nos.get(i);
			filenames.remove(pos);
		}
//		System.out.println(filenames.size());
		for(int i=0;i<filenames.size();i++){
			fileLineNum+=dectPathLine(filenames.get(i));
		}
		return fileLineNum;
	}

	
	public static int charNum(String s){
//		检测一个字符串的字符数
		if(emptyLineMode==false){
			return s.length();
		}else{
			int n=0;
			char ss[]=s.toCharArray();
			for (int i = 0; i < ss.length; i++) {
				if(!isBlank(ss[i]))
					n++;
			}
			return n;
		}
	}
	public static int dectFileChar(String ad) throws IOException{
//		检测一个文件的字符数
		File f = new File(ad);// 读取路径
		if (!f.exists()) {
			System.out.println(ad+"  文件不存在！");
			return 0;
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(ad)));
		String line = null;
		int charNum1 = 0;
		while ((line = in.readLine()) != null) {
			if(line.contains(excludeId))
				excludeSign=true;
			charNum1+=charNum(line);
			if(emptyLineMode==false){
				charNum1+=1;
			}
		}
		in.close();
		return charNum1;
	}
	
	public static int dectPathChar(String ad) throws IOException{
//		检测一个文件夹内的字符数
		int fileCharNum=0;
		File path = new File(ad);
		if (!path.exists()) {
			System.out.println(ad+"路径不存在！");
			return 0;
		}
		
		if(!path.isDirectory()){
			excludeSign=false;
			int num=dectFileChar(ad);
			if(excludeSign==false){
//			fileCharNum+=num;
			System.out.println(ad+"  : "+num+"  个字符.");
			}
			return num;
		}
		
		String prenames[] = path.list();// 文件名
		int fileNum = path.list().length;
		ArrayList<String> filenames = new ArrayList<String>();// 路径+文件名
//		ArrayList<String> names = new ArrayList<String>();// 文件名
		ArrayList<Integer> nos = new ArrayList<Integer>();// 上面两个List中文件是非目录的序号
		for (int i = 0; i < fileNum; i++) {
			filenames.add(ad +'/'+ prenames[i]);
//			names.add(prenames[i]);
			File fi=new File(filenames.get(i));
			if(!fi.isDirectory()){
				nos.add(i);
//				System.out.println(filenames.get(i));
			}
//			System.out.println(i+"  : "+filenames.get(i));
		}
		
		
		/*for(int i=0;i<filenames.size();i++){
			System.out.println(i+"  : "+filenames.get(i));
		}*/
		
		
		for(int i=nos.size()-1;i>=0;i--){
//			System.out.println(filenames.get(i));
			excludeSign=false;
			int num=dectFileChar(filenames.get(nos.get(i)));
			if(excludeSign==false){
			fileCharNum+=num;
			System.out.println(filenames.get(nos.get(i))+"  : "+num+"  个字符.");
			}int pos=nos.get(i);
			filenames.remove(pos);
		}
//		System.out.println(filenames.size());
		for(int i=0;i<filenames.size();i++){
			fileCharNum+=dectPathChar(filenames.get(i));
		}
		return fileCharNum;
	}
	
	public static void dectLineAndChar(String ad) throws IOException{
//		检测一个文件夹内的代码行数和字符数
		File path = new File(ad);
		if (!path.exists()) {
			System.out.println(ad+"路径不存在！");
			return ;
		}
		if(!path.isDirectory()){
			excludeSign=false;
			int num=dectFileChar(ad);			
			int num2=dectFileLine(ad);
			
			if(excludeSign==false){
				charNum+=num;
				lineNum+=num2;
				System.out.println(ad+"  : "+num2+" 行 ，   "+num+"  个字符.");				
			}
			return;
		}
		String prenames[] = path.list();// 文件名
		int fileNum = path.list().length;
		ArrayList<String> filenames = new ArrayList<String>();// 路径+文件名
//		ArrayList<String> names = new ArrayList<String>();// 文件名
		ArrayList<Integer> nos = new ArrayList<Integer>();// 上面两个List中文件是非目录的序号
		for (int i = 0; i < fileNum; i++) {
			filenames.add(ad +'/'+ prenames[i]);
//			names.add(prenames[i]);
			File fi=new File(filenames.get(i));
			if(!fi.isDirectory()){
				nos.add(i);
//				System.out.println(filenames.get(i));
			}
//			System.out.println(i+"  : "+filenames.get(i));
		}
		
		
		/*for(int i=0;i<filenames.size();i++){
			System.out.println(i+"  : "+filenames.get(i));
		}*/
		
		
		for(int i=nos.size()-1;i>=0;i--){
//			System.out.println(filenames.get(i));
			excludeSign=false;
			int num=dectFileChar(filenames.get(nos.get(i)));			
			int num2=dectFileLine(filenames.get(nos.get(i)));
			
			if(excludeSign==false){
				charNum+=num;
				lineNum+=num2;
				System.out.println(filenames.get(nos.get(i))+"  : "+num2+" 行 ，   "+num+"  个字符.");				
			}
			int pos=nos.get(i);
			filenames.remove(pos);
		}
//		System.out.println(filenames.size());
		for(int i=0;i<filenames.size();i++){
			dectLineAndChar(filenames.get(i));
		}
//		return fileCharNum;
	}


	public static void main(String[] args) throws IOException {
		File path = new File(adds);
		if (!path.exists()) {
			System.out.println(adds+"路径不存在！");
			return ;
		}
		
		dectLineAndChar(adds);
		System.out.println("\n代码总行数： "+lineNum+"  ,字符总数： "+charNum);
		
		
		/*int lineNum=dectPathLine(adds);
		int charNum=dectPathChar(adds);
		System.out.println("\n代码总行数： "+lineNum+"  ,字符总数： "+charNum);
*/
	}

}
