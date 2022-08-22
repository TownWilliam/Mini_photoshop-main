package src.component;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BasicUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuBar mb;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem clearItem;
    JMenuItem undoItem;
    JMenuItem exitItem;
    
    JMenu editMenu;      
    JMenuItem addItem;
    JMenuItem mergeBule;
    JMenuItem mergeGreen;
    JMenuItem notItem;     //Menu1
    JMenuItem vagueItem;     //Menu2
    JMenuItem scaleItem;
    JMenuItem rotateItem;
    JMenuItem transformItem;        
    JMenuItem stretchItem;
    JMenuItem logarithItem;
    JMenuItem powertransfItem;
    JMenuItem evenItem;
    JMenuItem fourierItem;
    JMenuItem mirrorItem;
    JMenuItem sharpItem;
    JMenuItem laplaceItem;
    JMenuItem smoothfliterItem;
    //------------------Graph-------------
    ImagePanel imagePanel;
    ImagePanel imageLabel;
    JScrollPane scrollPane;
    ImageIcon imageIcon;
    BufferedImage image;
    BufferedImage image1=null;
    //-------------------File Chooser-----------
    JFileChooser chooser;
    ImagePreviewer imagePreviewer;
    ImageFileView fileView;
    
    ImageFileFilter bmpFilter;
    ImageFileFilter jpgFilter;
	ImageFileFilter gifFilter;
	ImageFileFilter bothFilter;
	//---------------------Component---
	int R=120;int G=150;int B=120;
 
    public BasicUI() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exit(e);
            }
        });
        
        initComponents();
    }//Exit when the window is closed
    
    private void initComponents() {
    	Container contentPane = getContentPane();
    	this.setSize(1376, 798);
    	JSplitPane splitPane=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    	
    	splitPane.setDividerLocation(this.getWidth()/2);
    	imagePanel = new ImagePanel(image);
        scrollPane = new JScrollPane(imagePanel);
       
        imageLabel=new ImagePanel(image);
        
        splitPane.setLeftComponent(new JScrollPane(imageLabel));
    	splitPane.setRightComponent(scrollPane);
        contentPane.add(splitPane);
        
        chooser = new JFileChooser();
        imagePreviewer = new ImagePreviewer(chooser);
        fileView = new ImageFileView();
	    bmpFilter = new ImageFileFilter("bmp", "BMP Image Files");
	    jpgFilter = new ImageFileFilter("jpg", "JPEG Compressed Image Files");
		gifFilter = new ImageFileFilter("gif", "GIF Image Files");
		bothFilter = new ImageFileFilter(new String[] {"bmp", "jpg", "gif"}, "BMP, JPEG and GIF Image Files");
	    
		chooser.addChoosableFileFilter(jpgFilter);
	    chooser.addChoosableFileFilter(gifFilter);
	    chooser.addChoosableFileFilter(bmpFilter);
        chooser.addChoosableFileFilter(bothFilter);
        chooser.setAccessory(imagePreviewer);
        chooser.setFileView(fileView);
        chooser.setAcceptAllFileFilterUsed(false);
         
              
		//----菜单条------------------------------------------------------------
		mb = new JMenuBar();
		setJMenuBar(mb);
		//----File菜单----------------------------------------------------------
		fileMenu = new JMenu("文件(F)");
		//fileMenu.setIcon(fileIcon);
		fileMenu.setMnemonic('F');
		mb.add(fileMenu);
		
		
		openItem = new JMenuItem("打开(O)");  
		openItem.setMnemonic('O');
		openItem.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
		openItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile(e);
			}
		});
		
		saveItem = new JMenuItem("保存(S)"); 
		saveItem.setMnemonic('S');
		saveItem.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
		
		clearItem = new JMenuItem("Clear Right(C)");
		clearItem.setMnemonic('C');
		clearItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear_rImage(e);
			}
		});
		exitItem = new JMenuItem("退出(X)");
		exitItem.setMnemonic('X');
		
		
		undoItem = new JMenuItem("撤销(Ctrl+Z)");
		undoItem.setAccelerator(KeyStroke.getKeyStroke('Z',Event.CTRL_MASK));
		undoItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		    	imagePanel.setImage(image1);
		    	imagePanel.repaint();
			}
		});
		
		//--------------File--
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(clearItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		fileMenu.add(undoItem);
		
		//----Edit--------------------------------------------
		editMenu = new JMenu("编辑(E)");
		editMenu.setMnemonic('E');
		mb.add(editMenu);
		
		addItem = new JMenuItem("图像加运算(A)");   
		addItem.setMnemonic('A');
		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				imageAdd(e);       //子菜单处理程序
			}
		});
		
		//-------------Merge Blue---------------
		mergeBule = new JMenuItem("背景抠蓝");
		mergeBule.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					image_mergeBlue(e);
				}
		});
		
		mergeGreen = new JMenuItem("背景抠绿");
		mergeGreen.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e)
				{
					image_mergeGreen(e);
				}
		});
		//---------------stretch-----------------------
		stretchItem = new JMenuItem("对比度增大");
		stretchItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
					image_stretch(e);			
			}
		});
		mirrorItem = new JMenuItem("镜像");
		mirrorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_mirror(e);
			}
		});
		//------------------not-------------------------------
		notItem = new JMenuItem("取反");   ;
		notItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_not(e);      //NOT command
			}
		});
		//-------------------Vague--------
		vagueItem = new JMenuItem("马赛克");  
		vagueItem.setMnemonic('R');
		vagueItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_vague(e);       //Vague Command
			}
		});
		
		scaleItem = new JMenuItem("放缩");
		scaleItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_scale(e);  //Scale command
				
			}
		});
		
		rotateItem = new JMenuItem("旋转");
		rotateItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_rotate(e);  //Scale command
				
			}
		});
		//-------------------Transform----------------
		transformItem =new JMenuItem("平移");
		transformItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//image_transform(e);
				image_transform(e);
				
			}
		});
		//-----------------Compress________
		logarithItem =new JMenuItem("灰度对数变换");
		logarithItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_logarithTransform(e);
			}
		});
		//-----------------PowerTransform------
		powertransfItem = new JMenuItem("灰度幂律变换");
		powertransfItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_powerTransform(e);
			}
		});
		//--------------EvenGray-------------
		evenItem = new JMenuItem("直方图均衡化");
		evenItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			Object[] possibleValues = { "直方图均衡化(RGB方式)", "直方图均衡化(HSI方式)"}; 
			Object selectedValue = JOptionPane.showInputDialog
					(null, "直方图均衡化", "选择方式", 
			JOptionPane.INFORMATION_MESSAGE, null, 
				possibleValues, possibleValues[0]);
			if(selectedValue.equals(possibleValues[0]))
				image_adjust_RGB(e);
			else
				image_adjust_HSI(e);
			}
		});	
		//----------------Sharp----------------
		
		sharpItem = new JMenuItem("锐化");
		sharpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_sharp(e);	
			}
		});	
		//----------------Laplace----------------
		
		laplaceItem = new JMenuItem("边缘提取算子");
		laplaceItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_Laplace(e);
					}
				});	
		
		//---------------FourierTransform-------
		fourierItem = new JMenuItem("普通傅里叶变换");
		fourierItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_fourier(e);
				
			}
		});
		//---------------------------
		smoothfliterItem = new JMenuItem("去噪滤波");
		smoothfliterItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				image_smooth(e);
				
			}
		});
		
		//---------------------------
		
		editMenu.add(addItem);
		editMenu.add(notItem);
		editMenu.add(vagueItem);
		editMenu.addSeparator();
		editMenu.add(scaleItem);
		editMenu.add(rotateItem);
		editMenu.add(mirrorItem);
		editMenu.add(transformItem);
		editMenu.addSeparator();
		editMenu.add(mergeBule);
		editMenu.add(mergeGreen);
		editMenu.addSeparator();
		editMenu.add(stretchItem);
		editMenu.add(logarithItem);
		editMenu.add(powertransfItem);
		editMenu.add(evenItem);
		editMenu.add(fourierItem);
		editMenu.add(sharpItem);
		editMenu.add(smoothfliterItem);
		editMenu.add(laplaceItem);
    }
    
    /** 退出程序事件 */
    private void exit(WindowEvent e) {
        System.exit(0);
    }
    
    void openFile(ActionEvent e) {
    	chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    	if(chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
    		try { 
    		image = ImageIO.read(chooser.getSelectedFile()); 
    		}
        	catch(Exception ex)
    		{ return ;
    		}
        	imageLabel.setImage(image); //左图像 
        	imagePanel.repaint();      	
    	}
    }
    
    void saveFile(ActionEvent e) {
        	
    }
    
    void clear_rImage(ActionEvent e) {
    	
     	imagePanel.setImage(null);
    	imagePanel.repaint();	
    }
      
    void imageAdd(ActionEvent e) {
    	BufferedImage image1=null;
    	
       	chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    	if(chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
    		try { image1 = ImageIO.read(chooser.getSelectedFile()); }
        	catch(Exception ex) { return ;}
    	}
           	
      	image1=EditImage.image_add(image,image1);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }    
    void image_not(ActionEvent e)
    {
    	BufferedImage image1=null;
      	image1=EditImage.image_not(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    void image_mirror(ActionEvent e)
    {
    	BufferedImage image1=null;
      	image1=EditImage.image_mirror(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    void image_vague(ActionEvent e)
    {
    	BufferedImage image1=null;
      	image1=EditImage.image_vague(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    
    void image_sharp(ActionEvent e)
    {
    	BufferedImage image1=null;
      	image1=EditImage.image_sharp(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    void image_scale(ActionEvent e)
    {
    	String s =JOptionPane.showInputDialog("Please Input the scale");
    	float f= Float.parseFloat(s);
    	BufferedImage image1=null;
    	image1=EditImage.image_scale(image,f);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void image_rotate(ActionEvent e)
    {
    	String s =JOptionPane.showInputDialog("Please Input the angle");
    	double f= Double.parseDouble(s);
    	BufferedImage image1=null;
    	image1=EditImage.image_rotate(image,f);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void image_transform(ActionEvent e)
    {
    	String s =JOptionPane.showInputDialog("Please Input the transform dx,dy");
    	int split=0;
    	String s1="";
    	String s2="";
    	for(int i=0;i<s.length();i++)
    	{	
    		char ch=s.charAt(i);
    		split++;
    		if(ch!=',')
    		{ s1+=ch;
    		  
    		}else {break;}
    	}
    	for(int i=split+1;i<s.length();i++)
    	{	
    		char ch=s.charAt(i);
    		s2+=ch;	
    	}
    	System.out.println(s1+" "+s2);
    	double dx= Double.parseDouble(s1);
    	double dy= Double.parseDouble(s2);
    	BufferedImage image1=null;
    	image1=EditImage.image_transform(image, dx, dy);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void image_mergeBlue(ActionEvent e)
    {
    	BufferedImage image1=null;
    	
       	chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    	if(chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
    		try { image1 = ImageIO.read(chooser.getSelectedFile()); }
        	catch(Exception ex) { return ;}
    	}     	
      	image1=EditImage.image_mergeInBlue(image,image1);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void image_Laplace(ActionEvent e)
    {
    	BufferedImage image1=null;
    	String s=JOptionPane.showInputDialog("Please input the kind of Laplace");
      	image1=EditImage.image_laplace(image,s);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void makeSlider()
    {
    	
    }
    void image_mergeGreen(ActionEvent e)
    {
    	
    	
    	JSlider slider1 = new JSlider(JSlider.HORIZONTAL,0,255,20);
    	slider1.setMajorTickSpacing(35);
    	slider1.setMinorTickSpacing(1);
    	slider1.setPaintTicks(true);
    	slider1.setPaintLabels(true);
    	slider1.setBounds(10,10,260,120);
    	slider1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 R=((JSlider)e.getSource()).getValue();
				 BufferedImage temp=null;
				 temp=EditImage.image_mergeInGreen(image, image1, 10, 10,R,G,B);
			    	imagePanel.setImage(temp);
			    	imagePanel.repaint();
			}
		});
    	
    	
    	JSlider slider2 = new JSlider(JSlider.HORIZONTAL,0,255,20);
    	slider2.setMajorTickSpacing(35);
    	slider2.setMinorTickSpacing(1);
    	slider2.setPaintTicks(true);
    	slider2.setPaintLabels(true);
    	slider2.setBounds(10,10,260,150);
    	slider2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 G=((JSlider)e.getSource()).getValue();
				 BufferedImage temp=null;
				 temp=EditImage.image_mergeInGreen(image, image1, 10, 10,R,G,B);
			    	imagePanel.setImage(temp);
			    	imagePanel.repaint();
			}
		});
    	
    	JSlider slider3= new JSlider(JSlider.HORIZONTAL,0,255,20);
    	slider3.setMajorTickSpacing(35);
    	slider3.setMinorTickSpacing(1);
    	slider3.setPaintTicks(true);
    	slider3.setPaintLabels(true);
    	slider3.setBounds(10,10,260,120);
    	slider3.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 B=((JSlider)e.getSource()).getValue();
				 BufferedImage temp=null;
				 temp=EditImage.image_mergeInGreen(image, image1, 10, 10,R,G,B);
			    	imagePanel.setImage(temp);
			    	imagePanel.repaint();
			}
		});
    	
    
    	JFrame a=new JFrame("Adjust");
		Container c=a.getContentPane();
		c.setLayout(new GridLayout(3, 1));
		c.add(slider1);
		c.add(slider2);
		c.add(slider3);
		a.setSize(310,200);
		a.setVisible(true);
	
    	
       	chooser.setDialogType(JFileChooser.OPEN_DIALOG);
    	if(chooser.showDialog(this, null) == JFileChooser.APPROVE_OPTION) {
    		try { image1 = ImageIO.read(chooser.getSelectedFile()); }
        	catch(Exception ex) { return ;}
    	}    
    	BufferedImage temp=null;
      	temp=EditImage.image_mergeInGreen(image, image1, 10, 10,120,150,120);
    	imagePanel.setImage(temp);
    	imagePanel.repaint();
    }
    
    void image_stretch(ActionEvent e)
    {
    	BufferedImage image1=null;
    	
    	image1=EditImage.image_stretch(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void image_fourier(ActionEvent e)
    {
    	BufferedImage image1=null;
    		image1=EditImage.image_fourier(image);
        	imagePanel.setImage(image1);
        	imagePanel.repaint();
     }
    void image_logarithTransform(ActionEvent e)
    {
    	String s=JOptionPane.showInputDialog("Please input the value of the base number of this transformation");
    	double b=Double.parseDouble(s);
    	if(b>1)
    	{
    		BufferedImage image1=null;
        	image1=EditImage.image_logarithTransform(image, b);
        	imagePanel.setImage(image1);
        	imagePanel.repaint();
    	}
    	    }
    void image_powerTransform(ActionEvent e)
    {
    	String s=JOptionPane.showInputDialog("Please input the value of the gama of this transformation");
    	double gama=Double.parseDouble(s);
    	BufferedImage image1=null;
    	
    	image1=EditImage.image_powerTransform(image, gama);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    }
    void image_adjust_RGB(ActionEvent e)
    {
    	BufferedImage image1=null;
      	image1=EditImage.image_histEqlRGB(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    void image_adjust_HSI(ActionEvent e)
    {
    	BufferedImage image1=null;
      	image1=EditImage.image_histEqlHSI(image);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    void image_smooth(ActionEvent e)
    {
    	BufferedImage image1=null;
    	String opt=JOptionPane.showInputDialog("请输入平滑滤波种类");
      	image1=EditImage.image_smthFliter(image,opt);
    	imagePanel.setImage(image1);
    	imagePanel.repaint();
    	
    }
    void imageSize(ActionEvent e) {
    	
		final JDialog findDialog = new JDialog(this, "放大/缩小", false);// false时允许其他窗口同时处于激活状态(即无模式)
		Container con = findDialog.getContentPane();// 返回此对话框的contentPane对象

		con.setLayout(new FlowLayout(FlowLayout.LEFT));
		final JTextField changeText = new JTextField(5);// 用指定的列数构造一个新的空文本字段
		JButton YesButton = new JButton("确定");
		JButton cancel = new JButton("取消");
		// 取消按钮事件处理
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findDialog.dispose();// 释放窗口
			}
		});
		YesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	BufferedImage image1=null;
				String str1 = changeText.getText();
				double multiple = Integer.parseInt(str1);
					//BufferedImage image2 = null;
					image1 = EditImage.image_scale(image1,(float)multiple);
					imagePanel.setImage(image1);
					imagePanel.repaint();
					findDialog.dispose();
				

			}
		});
		

    }   
}

