package data;

import har.Constants;
import har.Labels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainWindow  extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JButton Extract = new JButton("提取特征"); // 提取特征按钮
	private JButton Train = new JButton("训练"); // 按钮
	private JButton Predict = new JButton("预测"); // 按钮
	
	ImageGUI trainVideo=new ImageGUI();
	ImageGUI predictVideo=new ImageGUI();

	public MainWindow() {
		// TODO Auto-generated constructor stub
	}
	
	public void lanuchWindow(){
		this.setTitle("视频动作识别系统");
		setBounds(300, 150, 650, 590);
		
		contentPane = new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon(
						"images\\bg.jpg").getImage(), 0,
						0, getWidth(), getHeight(), null);
			}
		};
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		this.setLayout(null);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				System.exit(0);
			}
		});
		
		
		Extract.setBounds(50, 50, 120, 40);
		this.add(Extract);
		Extract.addActionListener(this);
		Train.setBounds(190, 50, 80, 40);
		this.add(Train);
		Train.addActionListener(this);
		Predict.setBounds(300, 50, 80, 40);
		this.add(Predict);
		Predict.addActionListener(this);
		
		trainVideo.setBounds(50, 150, 330, 240);
		/*trainVideo.createWin("OpenCV + Java视频读与播放演示", new Dimension(300,
				220));*/
		trainVideo.setMainWin(this);
		this.add(trainVideo);
		
		predictVideo.setBounds(300, 250, 330, 240);
		predictVideo.setMainWin(this);
		this.add(predictVideo);
		
		
		
		setVisible(true);
		this.setResizable(false);//窗口大小不可调整
		
		
		
		
		
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == Extract) {
			//提取所有视频特征：
			ExtractAllVideos extA=new ExtractAllVideos();
			try {
				extA.exe(trainVideo);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} else if (e.getSource() == Train) {
//			训练：
			try {
				MySVM.loadTrainData();
//				MySVM.saveTrainDataTest();
				MySVM.train();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		else if(e.getSource() == Predict){
//			预测：
			Labels c=Labels.BOXING;
			int i=72;
			String videoAddress=Constants.dataOfVideosAddress+c.getName()+"/"+c.getName()+"_"+i+".avi";
			MySVM.predict(videoAddress,predictVideo);
		}
	}

}
