package data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import har.Constants;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.ml.CvKNearest;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

import Jama.Matrix;

public class Classifiers {

	// public static Mat data_mat;
	private static Mat data_mat;
	private static Mat label_mat;
	private static CvSVM svm_classifier;
	private static CvKNearest knn_classifier;
	public static String data_hog_Address = MyConstants.VideoHogAddress;
	public static String data_hog_name = "data_mat_hog.txt";
	public static String data_hog_label = "data_mat_hog_label.txt";
	public static String data_hog_name_tem = "data_mat_hog_tem.txt";
	public static String data_hog_label_tem = "data_mat_hog_label_tem.txt";
	public static String svm_modelAddress = MyConstants.dataAddress;
	public static String svm_modelName = "svm_model.xml";
	public static String knn_modelAddress = MyConstants.dataAddress;
	public static String knn_modelName = "knn_model.xml";

	public Classifiers() {
		// TODO Auto-generated constructor stub
	}

	public static int loadTrainData() throws NumberFormatException, IOException {

		int dataLines1 = 0;
		int dataLines2 = 0;
		File f = new File(data_hog_Address + data_hog_name);// 读取路径
		if (!f.exists()) {
			System.out.println("hog数据不存在！");
			// MainWindow.tips.append("hog数据不存在！\n");
			MyTools.showTips("hog数据不存在！", 1);
			return -1;
		}

		File f2 = new File(data_hog_Address + data_hog_label);// 读取label路径
		if (!f2.exists()) {
			System.out.println("label数据不存在！");
			// MainWindow.tips.append("label数据不存在！\n");
			MyTools.showTips("label数据不存在！", 1);
			return -2;
		}

		// 读取hog:
		MyTools.showTips("读取特征数据中。。。", 1);
		data_mat = new Mat();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream(data_hog_Address + data_hog_name)));
		String line = null;
		int cols = 0;
		int k = 0;

		while ((line = in.readLine()) != null) {
			dataLines1++;
			String[] words = line.split("\t");
			if (k == 0)
				cols = words.length;
			if (cols != words.length)
				break;
			Mat aRow = new Mat(1, words.length, CvType.CV_32FC1);
			// float[]values=new float[words.length];
			for (k = 0; k < words.length; k++) {
				String word = words[k];
				double value = Float.parseFloat(word);
				// double value = (float) Double.parseDouble(word);
				// values[k]= (float) Double.parseDouble(word);
				aRow.put(0, k, value);
			}
			// Mat aRow=new Mat();
			/*
			 * MatOfFloat aRow=new MatOfFloat(); aRow.fromArray(values);
			 */

			// System.out.println("k="+k+"   words.length="+words.length);
			data_mat.push_back(aRow);
		}
		in.close();

		// 读取label数据：

		label_mat = new Mat();
		BufferedReader in2 = new BufferedReader(new InputStreamReader(
				new FileInputStream(data_hog_Address + data_hog_label)));
		String line2 = null;
		int k2 = 0;
		while ((line2 = in2.readLine()) != null) {
			dataLines2++;
			String[] words = line2.split("\t");
			Mat aRow2 = new Mat(1, words.length, CvType.CV_32FC1);
			// float[]values=new float[words.length];
			for (k2 = 0; k2 < words.length; k2++) {
				String word = words[k2];
				int value = Integer.parseInt(word);
				aRow2.put(0, k2, value);
			}
			// Mat aRow=new Mat();
			/*
			 * MatOfFloat aRow=new MatOfFloat(); aRow.fromArray(values);
			 */

			// System.out.println("k="+k+"   words.length="+words.length);
			label_mat.push_back(aRow2);
		}
		in2.close();

		// System.out.println(dataLines1+"  "+dataLines2);
		if (dataLines1 == dataLines2 && dataLines1 != 0){			
			MyTools.showTips("特征数据加载完成！", 1);
			return 1;
		}
		data_mat=null;
		label_mat=null;
		MyTools.showTips("训练数据与标签不一致！",1);
		return 0;
	}

	public static void saveTrainDataTest() throws IOException {
		// 没用，只是用来测试Mat的读取是否正常
		if (data_mat == null) {
			System.out.println("Mat is uninitialize!");
			// MainWindow.tips.append("Mat is uninitialize!\n");
			MyTools.showTips("Mat is uninitialize!");
			return;
		}
		File f11 = MyTools.mkdir(Classifiers.data_hog_Address,
				"data_mat_hog_duplicate.txt");// 保存路径
		FileWriter fw11 = new FileWriter(f11);
		PrintWriter out3 = new PrintWriter(new BufferedWriter(fw11));
		// System.out.println("data_mat.rows():"+data_mat.rows()+"  data_mat.cols():"+data_mat.cols());
		for (int j = 0; j < data_mat.rows(); j++) {
			for (int k = 0; k < data_mat.cols(); k++) {
				double[] va = data_mat.get(j, k);
				out3.print((float) va[0] + "\t");
			}

			/*
			 * float[] aLine = ((MatOfFloat)data_mat.row(j)).toArray(); //
			 * System.out.println(hogOut1.length);//获取向量维数 for (int i = 0; i <
			 * aLine.length; i++) { out3.print(aLine[i] + "\t"); }
			 */

			out3.println();
		}
		out3.close();

		// 存储label:
		if (label_mat == null) {
			System.out.println("labelMat is uninitialize!");
			// MainWindow.tips.append("Mat is uninitialize!\n");
			MyTools.showTips("Mat is uninitialize!");
			return;
		}
		File f22 = MyTools.mkdir(Classifiers.data_hog_Address,
				"data_hog_label.txt");// 保存路径
		FileWriter fw22 = new FileWriter(f22);
		PrintWriter out22 = new PrintWriter(new BufferedWriter(fw22));
		// System.out.println("data_mat.rows():"+data_mat.rows()+"  data_mat.cols():"+data_mat.cols());
		for (int j = 0; j < label_mat.rows(); j++) {
			for (int k = 0; k < label_mat.cols(); k++) {
				double[] va = label_mat.get(j, k);
				out22.print((int) va[0] + "\t");
			}

			/*
			 * float[] aLine = ((MatOfFloat)data_mat.row(j)).toArray(); //
			 * System.out.println(hogOut1.length);//获取向量维数 for (int i = 0; i <
			 * aLine.length; i++) { out3.print(aLine[i] + "\t"); }
			 */

			out22.println();
		}
		out22.close();

	}

	public static void SVMtrain() {

		CvSVMParams params = new CvSVMParams();
		params.set_kernel_type(CvSVM.LINEAR);
		MyTools.showTips("分类器训练中。。。", 1);
		svm_classifier = new CvSVM(data_mat, label_mat, new Mat(), new Mat(),
				params);
		MyTools.mkdir(svm_modelAddress, svm_modelName);
		svm_classifier.train(data_mat, label_mat);
		svm_classifier.save(svm_modelAddress + svm_modelName);
		MyTools.showTips("分类器训练完毕。", 1);

		// CvSVM svm = new CvSVM();
		// CvSVMParams param;
		// CvTermCriteria criteria;
		// criteria = new cvTermCriteria( CV_TERMCRIT_EPS, 1000, FLT_EPSILON );
		// param = new CvSVMParams( CvSVM.C_SVC, CvSVM.RBF, 10.0, 0.09, 1.0,
		// 10.0, 0.5, 1.0, null, criteria );
		//
		// // SVM种类：CvSVM::C_SVC
		// // Kernel的种类：CvSVM::RBF
		// // degree：10.0（此次不使用）
		// // gamma：8.0
		// // coef0：1.0（此次不使用）
		// // C：10.0
		// // nu：0.5（此次不使用）
		// // p：0.1（此次不使用）
		// // 然后对训练数据正规化处理，并放在CvMat型的数组里。
		// //SVM学习
		// svm.train(data_mat, label_mat, new Mat(), new Mat(), param);
		// //利用训练数据和确定的学习参数,进行SVM学习
		// svm.save( "E:/apple/SVM_DATA.xml" );

	}
	
	public static void loadSVMModel(){
		MyTools.showTips("load svm_classifier...", 1);
		File f = new File(svm_modelAddress + svm_modelName);// svm路径
		if (!f.exists()) {
			System.out.println("svm_classifier doesn't exist!...");
			// MainWindow.tips.append("svm_classifier doesn't exist!...\n");
			MyTools.showTips("classificator doesn't exist!...");
			return;
		}
		if (svm_classifier == null)
			svm_classifier = new CvSVM();			
		svm_classifier.load(svm_modelAddress + svm_modelName);
		MyTools.showTips("svm_classifier加载完成！", 1);
	}

	public static void SVMpredict(String viAdr, ImageGUI predictVideo) {
		if (svm_classifier == null) {
			// System.out.println("haha");
			svm_classifier = new CvSVM();
			System.out.println("load svm_classifier...");
			// MainWindow.tips.append("load svm_classifier...\n");
			loadSVMModel();
		}
		MyTools.showTips("processing...", 1);
		Mat features = ExtractVideoFeature.extract(viAdr, predictVideo);

		int result[] = new int[features.rows()];
		for (int i = 0; i < features.rows(); i++) {
			result[i] = (int) svm_classifier.predict(features.row(i));
			// System.out.println(result[i]+"   "+Labels.getNameById(result[i]));
		}
		System.out.println("count:");
		// MainWindow.tips.append("count:\n");
		MyTools.showTips("predict result:", 1);
		int nt[][] = chooseOne(result,result.length);
		for (int q = 0; q < 6; q++) {
			System.out.println(nt[q][0] + "   " + Labels.getNameById(nt[q][0])
					+ "   次数：" + nt[q][1]);
			// MainWindow.tips.append(nt[q][0]+"   "+Labels.getNameById(nt[q][0])+"   次数："+nt[q][1]+"\n");
			MyTools.showTips(nt[q][0] + "(类别Id)   "
					+ Labels.getNameById(nt[q][0]) + "   次数：" + nt[q][1]);
		}
		for (int a = 0; a < 2; a++) {
			System.out.println("视频为" + Labels.getNameById(nt[a][0]) + "的概率为："
					+ (nt[a][1] / (float) nt[6][1] * 100) + "%");
			// MainWindow.tips.append("视频为"+Labels.getNameById(nt[a][0])+"的概率为："+(nt[a][1]/(float)nt[6][1]*100)+"%\n");
			MyTools.showTips("视频为" + Labels.getNameById(nt[a][0]) + "的概率为："
					+ (nt[a][1] / (float) nt[6][1] * 100) + "%");
		}

	}

	public static int[][] chooseOne(int[] r,int num) {
		int n=Labels.getLabelsCount();//n=6
		int[][] NoTimes = new int[n+1][2];
		// 6是视频各类数量，第一个列向量是视频各类id，第二个是特征符合的数量
		// 第7行是总数
		for (int w = 0; w < n+1; w++) {
			NoTimes[w][0] = w;
			NoTimes[w][1] = 0;
		}
		for (int u = 0; u < num; u++) {
			NoTimes[r[u]][1]++;
			NoTimes[n][1]++;
		}

		// 排序：
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < n - 1 - i; j++) {
				if (NoTimes[j][1] < NoTimes[j + 1][1]) {
					int tem[] = new int[2];
					tem[0] = NoTimes[j][0];
					tem[1] = NoTimes[j][1];
					NoTimes[j][0] = NoTimes[j + 1][0];
					NoTimes[j][1] = NoTimes[j + 1][1];
					NoTimes[j + 1][0] = tem[0];
					NoTimes[j + 1][1] = tem[1];
				}
			}
		}

		return NoTimes;
	}
	
	private static int[][] chooseOne(Mat kNNs) {
		int da[]=new int[kNNs.cols()];
		for(int u=0;u<kNNs.cols();u++){
			double sss[]=kNNs.get(0, u);
			da[u]=(int)sss[0];
		}
		return chooseOne(da,da.length);
	}

	public static void KNNtrain() {
		
		/*CvSVMParams params = new CvSVMParams();
		params.set_kernel_type(CvSVM.LINEAR);
		MyTools.showTips("分类器训练中。。。", 1);
		svm_classifier = new CvSVM();
		MyTools.mkdir(svm_modelAddress, svm_modelName);
		svm_classifier.train(data_mat, label_mat);
		svm_classifier.save(svm_modelAddress + svm_modelName);
		MyTools.showTips("分类器训练完毕。", 1);*/
		
		
//		CvSVMParams params = new CvSVMParams();
//		params.set_kernel_type(CvSVM.LINEAR);
//		MyTools.showTips("分类器训练中。。。", 1);
		if(data_mat==null){
			System.out.println("hehello");
		}
		knn_classifier = new CvKNearest(data_mat, label_mat);
//		MyTools.mkdir(km_modelAddress, km_modelName);
//		km_classifier.train(data_mat, label_mat);
		
//		km_classifier.save("kNearest_model.xml");
//		km_classifier.save(km_modelAddress + km_modelName);
		
		
//		MyTools.showTips("分类器训练完毕。", 1);
		
		/*
		Mat features = ExtractVideoFeature.extract("1.avi", MainWindow.videoGUI);

		for (int i = 0; i < features.rows(); i++) {
			int resu=(int)km_classifier.find_nearest(features.row(i), 5, new Mat(), new Mat(), new Mat());
			MyTools.showTips(Labels.getNameById(resu)+"", 1);
		}*/
		
		
//		CvKNearest::train(const Mat& trainData, const Mat& responses, const Mat& sampleIdx=Mat(), bool isRegression=false, int maxK=32, bool updateBase=false )
//		trainData：    即训练数据。
//		response：    对应每一个训练数据的类别或回归值；
//		sampleIdx：  暂时不清楚其意义；
//		isRegression：如果是true表示回归，false表示分类；
//		maxK：           最大的近邻个数
//		updateBase： 指定模型是否从头训练(update_base = false),或是更新使用新的训练数据(update_base = true)。在后一种情况下,参数maxK不得大于原始值。 
//
//		float CvKNearest::find_nearest(const Mat& samples, int k, Mat* results, const float** neighbor, Mat* neighborResponses, Mat* dists) const
//		float CvKNearest::find_nearest(const Mat& samples, int k, Mat& results, Mat& neighborResponses, Mat& dists) const
//		samples：   输入参数，大小为样本数(rows)×样本特征数(cols)；
//		k：               输入参数，即需要返回的k个邻近；
//		results：      输出参数，返回的结果（类别或者是回归值），大小为样本数(rows)；
//		neighbor：   输出参数，返回的是指向邻居向量本身的指针，即指向的是原来的训练数据。
//		neighborResponses： 输出参数，输出每个样本对应的k个最邻近的response，大小为样本数(rows)×k；
//		dists：          输出参数，输出每个样本对应的k个最邻近的距离，大小为样本数(rows)×k；
	}

	public static void KNNpredict(String viAdr, ImageGUI predictVideo) throws NumberFormatException, IOException {
		
		if(data_mat==null||label_mat==null){
			if(loadTrainData()!=1)
				return;
		}		
		if (knn_classifier == null) {
			MyTools.showTips("initializing knn_classifier...", 1);
			knn_classifier = new CvKNearest(data_mat, label_mat);
		}
		
		MyTools.showTips("processing...", 1);
		
		Mat features = ExtractVideoFeature.extract(viAdr, predictVideo);
/*
		for (int i = 0; i < features.rows(); i++) {
			int resu=(int)km_classifier.find_nearest(features.row(i), 5, new Mat(), new Mat(), new Mat());
			MyTools.showTips(Labels.getNameById(resu)+"", 1);
		}
		
		*/

		int result[] = new int[features.rows()];
		int valid=0;
		Mat KNNs = new Mat(1, MyConstants.K, CvType.CV_32FC1);
		for (int i = 0; i < features.rows(); i++) {
			knn_classifier.find_nearest(features.row(i), MyConstants.K, new Mat(), KNNs, new Mat());
			int qw[][]=chooseOne(KNNs);
			if(qw[0][0]>MyConstants.KNN_threshold)
			result[++valid] = qw[0][0];
			// System.out.println(result[i]+"   "+Labels.getNameById(result[i]));
		}
		System.out.println("count:");
		// MainWindow.tips.append("count:\n");
		MyTools.showTips("predict result:", 1);
		int nt[][] = chooseOne(result,valid);
		for (int q = 0; q < 6; q++) {
			System.out.println(nt[q][0] + "   " + Labels.getNameById(nt[q][0])
					+ "   次数：" + nt[q][1]);
			// MainWindow.tips.append(nt[q][0]+"   "+Labels.getNameById(nt[q][0])+"   次数："+nt[q][1]+"\n");
			MyTools.showTips(nt[q][0] + "(类别Id)   "
					+ Labels.getNameById(nt[q][0]) + "   次数：" + nt[q][1]);
		}
		for (int a = 0; a < 2; a++) {
			System.out.println("视频为" + Labels.getNameById(nt[a][0]) + "的概率为："
					+ (nt[a][1] / (float) nt[6][1] * 100) + "%");
			// MainWindow.tips.append("视频为"+Labels.getNameById(nt[a][0])+"的概率为："+(nt[a][1]/(float)nt[6][1]*100)+"%\n");
			MyTools.showTips("视频为" + Labels.getNameById(nt[a][0]) + "的概率为："
					+ (nt[a][1] / (float) nt[6][1] * 100) + "%");
		}

	}

	

}
