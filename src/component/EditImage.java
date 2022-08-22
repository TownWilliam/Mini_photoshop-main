package src.component;

import src.component.util.Complex;
import src.component.util.ImageUtil;

import java.awt.image.BufferedImage;
class RGBvalue
{
	int rs[][];
	int gs[][];
	int bs[][];
}
public class EditImage {
	
	public static RGBvalue getRBG(BufferedImage srcImage)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int srcRGBs[] = srcImage.getRGB(0, 0, width, height, null, 0, width);
		
		int rgb[]=new int[3];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue.rs=new int[width][ height]; //
		rgbvalue.gs=new int[width][ height];
		rgbvalue.bs=new int[width][ height];
		
		for(int j=0; j<height; j++) {
		    for (int i = 0; i < width; i++) {
		    	ImageUtil.decodeColor(srcRGBs[j*width+i],rgb); //rgb[0]=R,rgb[1]=G,rgb[2]=B
		    	rgbvalue.rs[i][j]=rgb[0];   //R
		    	rgbvalue.gs[i][j]=rgb[1];   //G
		    	rgbvalue.bs[i][j]=rgb[2];   //b
		    }	
		}
		return rgbvalue; 
	}
	
	public static float fliterSmooth(int x,int y,int patternSize,int ImageWidth,int ImageHeight,int rgb[][])
	{
		float result=0f;
		int pattern1[][]= {{1,2,1},{2,4,2},{1,2,1}};
		for(int i=x-1;i<=x+1;i++)
			for(int j=y-1;j<=y+1;j++)
			{
				int tempx=i>0?i:-i;int tempy=j>0?j:-j;
		    	tempx=tempx>(ImageWidth-1)?ImageWidth-1:tempx;
		    	tempy=tempy>(ImageHeight-1)?ImageHeight-1:tempy;
				result+=(rgb[tempx][tempy]*pattern1[i-x+1][j-y+1]);
			}
				
		result/=16;
		return result;
	}
	public static float fliterArithmeticMean(int x,int y,int patternSize,int ImageWidth,int ImageHeight,int rgb[][])
	{
		float result=0f;
		for(int i=x-1;i<=x+1;i++)
			for(int j=y-1;j<=y+1;j++)
			{
				int tempx=i>0?i:-i;int tempy=j>0?j:-j;
		    	tempx=tempx>(ImageWidth-1)?ImageWidth-1:tempx;
		    	tempy=tempy>(ImageHeight-1)?ImageHeight-1:tempy;
				result+=rgb[tempx][tempy];
			}
				
		result/=9;
		return result;
	}
	public static float fliterMedian(int x,int y,int patternSize,int ImageWidth,int ImageHeight,int rgb[][])
	{
		int sort1[]=new int[9];
		int index=0;
		for(int i=x-1;i<=x+1;i++)
			for(int j=y-1;j<=y+1;j++)
			{
				int tempx=i>0?i:-i;int tempy=j>0?j:-j;
		    	tempx=tempx>(ImageWidth-1)?ImageWidth-1:tempx;
		    	tempy=tempy>(ImageHeight-1)?ImageHeight-1:tempy;
				sort1[index++]=rgb[tempx][tempy];
			}//将图像点周围像素组成新的9×9数组元素
		sort(sort1);	
	 
		return sort1[4];
	}
	public static int[] fliter_vectorMedian
	(int x,int y,int ImageWidth,int ImageHeight,int r1[][],int r2[][],int r3[][])
	{
		int sort1[]=new int[9];
		int sort2[]=new int[9];
		int sort3[]=new int[9];
		int index=0;
		for(int i=x-1;i<=x+1;i++)
			for(int j=y-1;j<=y+1;j++)
			{
				int tempx=i>0?i:-i;int tempy=j>0?j:-j;
		    	tempx=tempx>(ImageWidth-1)?ImageWidth-1:tempx;
		    	tempy=tempy>(ImageHeight-1)?ImageHeight-1:tempy;
				sort1[index]=r1[tempx][tempy];
				sort2[index]=r2[tempx][tempy];
				sort3[index]=r3[tempx][tempy];index++;
			}//将图像点周围像素组成新的9×9数组元素
		double v[]= {0,0,0};
		for(int k=0;k<9;k++)
		{
			v[0]+=sort1[k];
			v[1]+=sort2[k];
			v[2]+=sort3[k];
		}
		v[0]/=9;v[1]/=9;v[2]/=9;
		
		double min=999999;int medianIndexI=0;
			for(int k=0;k<9;k++)
			{
				double temp=(sort1[k]-v[0])*(sort1[k]-v[0])
				+(sort2[k]-v[1])*(sort2[k]-v[1])
				+(sort3[k]-v[2])*(sort3[k]-v[2]);
				
				if(temp<min)
				{
					min=temp;medianIndexI=k;
				}
			}	
	 
		int result[]={sort1[medianIndexI],sort2[medianIndexI],sort3[medianIndexI]};
		return result;
	}
	public static void sort(int sort[])
	{
		quick_sort(sort,0,sort.length-1);
	}
	public static void quick_sort(int a[],int begin,int end)
	{
		int i=begin;int j=end;
		int pivot=i;
		int temp=a[pivot];
		
		while(i<j) {
			
			while(i<j&&temp<a[j])
				j--;
			if(i<j)a[i++]=a[j];
			while(i<j&&a[i]<temp)
				i++;
			if(i<j)a[j--]=a[i];			
		}
		pivot=i;a[i]=temp;
		if(pivot-1>=begin)quick_sort(a, begin, pivot-1);
		if(pivot+1<=end)  quick_sort(a, pivot+1, end);
		return;
	}

	public static int logarithTransform(int rgb, double C,double b)
	{
		double s=C*Math.log((double)(1+rgb))/Math.log(b);
		int r=(int)(s+0.5f);
		return (r>255?255:r);
	}
	public static int powerTransform(int rgb,double gama)
	{
		double c=255/Math.pow(255, gama);
		double s=c*Math.pow(rgb, gama);
		return (int)(s+0.5f);
	}

	public static int[][] changeImage(int rgb[][])
	{
		int result[][]=new int [rgb.length][rgb[0].length];
		double k=(double)255/(double)(MAX(rgb)-MIN(rgb));
		double b=-(double)MIN(rgb)*k;
		for(int i=0;i<rgb.length;i++)
			for(int j=0;j<rgb[0].length;j++)
			result[i][j]=(int)(k*rgb[i][j]+b);
		return result;
	}

	public static BufferedImage image_sharp(BufferedImage srcImage)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
	
		rs=rgbvalue.rs;   //R
		gs=rgbvalue.gs;   //G
		bs=rgbvalue.bs;   //b
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			for (int j = 0; j < height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=rs[i][j]+laplace_sobel(i, j, width, height, rs)/2;
			    	rgb[1]=gs[i][j]+laplace_sobel(i, j, width, height, gs)/2;
			    	rgb[2]=bs[i][j]+laplace_sobel(i, j, width, height, bs)/2;
			    	if(rgb[0]>255)rgb[0]=255;
			    	if(rgb[1]>255)rgb[1]=255;
			    	if(rgb[2]>255)rgb[2]=255;
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}
		 
		
		return destImage;
	}

	public static BufferedImage image_not(BufferedImage srcImage) {
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
	
		rs=rgbvalue.rs;   //R
		gs=rgbvalue.gs;   //G
		bs=rgbvalue.bs;   //b
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
	    for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) {
				float ftemp;
		    	ftemp=255-rs[i][j];   //Rֵ
		    	rgb[0]=(int)ftemp;
		    	ftemp=255-gs[i][j];   //Gֵ
		    	rgb[1]=(int)ftemp;
		    	ftemp=255-bs[i][j];   //Bֵ
		    	rgb[2]=(int)ftemp;
		    	
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			}	
			return destImage;
	}

	public static BufferedImage image_vague(BufferedImage srcImage) {
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);

    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //b
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
	    for (int j = 0; j <height; j++) 
			for(int i=0; i<width; i++) {
				
				double X0=(i/10)*10;
				double Y0=(j/10)*10;
		    	rgb=interpol_nearest(X0, Y0, rs, gs, bs, srcImage.getWidth(), srcImage.getHeight());
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}	
		
     
		return destImage;
	}
	
	public static BufferedImage image_scale(BufferedImage srcImage,float scale) {
		int width = (int)(srcImage.getWidth()*scale+0.5f);
		int height = (int)(srcImage.getHeight()*scale+0.5f);
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);

    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //b
    	
    	
	    BufferedImage destImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		
        for (int j = 0; j <height; j++) 
			for(int i=0; i<width; i++) {
				
				double X0=i/scale;
				double Y0=j/scale;
		    	rgb=interpol_nearest(X0, Y0, rs, gs, bs, srcImage.getWidth(), srcImage.getHeight());
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}	
		
     
		return destImage;
	}
	
	public static BufferedImage image_mirror(BufferedImage srcImage) {
		int width = srcImage.getWidth();
		int height =srcImage.getHeight();
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);

    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //b
    	
    	
	    BufferedImage destImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		
        for (int j = 0; j <height; j++) 
			for(int i=0; i<width; i++) {
				
				int X0=width-1-i;
				int Y0=j;
				rgb[0]=rs[X0][Y0];
				rgb[1]=gs[X0][Y0];
				rgb[2]=bs[X0][Y0];
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}	
		
     
		return destImage;
	}
	
	public static BufferedImage image_add(BufferedImage srcImage,BufferedImage image1) {
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int width1 = image1.getWidth();
		int height1 = image1.getHeight();//Size of two pictures.
		
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		
		int rs1[][]=new int[width][ height]; //
		int gs1[][]=new int[width][ height];
		int bs1[][]=new int[width][ height];
		RGBvalue rgbvalue1=new RGBvalue();
		rgbvalue1=getRBG(image1);

    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //B    Of srcImage
    	rs1=rgbvalue1.rs;   //R
    	gs1=rgbvalue1.gs;   //G
    	bs1=rgbvalue1.bs;   //B	  Of image1
	   	
	    float a=0.5f;
	    int w=width;
	    int h=height;	  
		if (width > width1) w=width1;
		if (height > height1) h=height1;
		 BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
        for (int j = 0; j < h; j++) {
			for(int i=0; i<w; i++) {
				float ftemp;
		    	ftemp=rs[i][j]*a+ rs1[i][j]*(1-a);   //Rֵ
		    	rgb[0]=(int)(ftemp+0.5f);
		    	ftemp=gs[i][j]*a+ gs1[i][j]*(1-a);   //Gֵ
		    	rgb[1]=(int)(ftemp+0.5f);
		    	ftemp=bs[i][j]*a+ bs1[i][j]*(1-a);   //Bֵ
		    	rgb[2]=(int)(ftemp+0.5f);
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			}	
		}
     
		return destImage;
	}

	public static BufferedImage image_rotate(BufferedImage srcImage,double thea) {
		thea=Math.PI*thea/180;
		int width  =Math.abs( (int)(srcImage.getHeight()*Math.sin(thea)+srcImage.getWidth()*Math.cos(thea)+0.5) );
		int height =Math.abs( (int)(srcImage.getWidth()*Math.sin(thea)+srcImage.getHeight()*Math.cos(thea)+0.5) );
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		System.out.println("cos(PI)="+Math.cos(3.14));
    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //b
    	//xcos thea - y sin thea
    	//xsin thea + y cos thea
    	
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int W=srcImage.getWidth();int H=srcImage.getHeight();
        for (int j =(H-height)/2; j <height+(H-height)/2; j++) 
			for(int i=(W-width)/2; i<width+(W-width)/2; i++) {
				double X0,Y0;
				X0=(i-W/2)*Math.cos(thea)+(H/2-j)*Math.sin(thea)+W/2;
		    	Y0=H/2-(-(i-W/2)*Math.sin(thea)+(H/2-j)*Math.cos(thea));
		    	//Rotation
		    	rgb=interpol_doublinear(X0, Y0, rs, gs, bs,srcImage.getWidth(),srcImage.getHeight());	
		    	//destImage.setRGB(i+width-W,j+height-H, ImageUtil.encodeColor(rgb));	
		    	destImage.setRGB(i+(width-W)/2,j+(height-H)/2, ImageUtil.encodeColor(rgb));	
			}	
			return destImage;
	}
	public static BufferedImage image_mergeInBlue(BufferedImage srcImage,BufferedImage image1)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int width1 = image1.getWidth();
		int height1 = image1.getHeight();//Size of two pictures.
		
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		
		int rs1[][]=new int[width][ height]; //
		int gs1[][]=new int[width][ height];
		int bs1[][]=new int[width][ height];
		RGBvalue rgbvalue1=new RGBvalue();
		rgbvalue1=getRBG(image1);

    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //B    Of srcImage
    	rs1=rgbvalue1.rs;   //R
    	gs1=rgbvalue1.gs;   //G
    	bs1=rgbvalue1.bs;   //B	  Of image1
	   	
	    int w=width;int W=width1;
	    int h=height;int H=height1;	  //W H is the maximum,by contrast,w h is the minimum.
		if (width > width1) {
			w=width1;
			W=width;
		} 
		if (height > height1) {
			h=height1;
			H=height;
		}
		 BufferedImage destImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		
        for (int j = 0; j < H; j++) 
			for(int i=0; i<W; i++) 
				if(j<h&&i<w) {
					if(bs1[i][j]>=200) {
						rgb[0]=rs[i][j];
						rgb[1]=gs[i][j];
						rgb[2]=bs[i][j];
						destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));	
					}
					else
					{
						rgb[0]=rs1[i][j];
						rgb[1]=gs1[i][j];
						rgb[2]=bs1[i][j];
						destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
					}
				}else {
					rgb[0]=rs[i][j];
					rgb[1]=gs[i][j];
					rgb[2]=bs[i][j];
					destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
				}						
		return destImage;
	}
	
	public static BufferedImage image_mergeInGreen(BufferedImage srcImage,BufferedImage image1,int dx,int dy,int 
			Rmax,int Gmin,int Bmax)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int width1 = image1.getWidth();
		int height1 = image1.getHeight();//Size of two pictures.
		
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		
		int rs1[][]=new int[width][ height]; //
		int gs1[][]=new int[width][ height];
		int bs1[][]=new int[width][ height];
		RGBvalue rgbvalue1=new RGBvalue();
		rgbvalue1=getRBG(image1);
	
		rs=rgbvalue.rs;   //R
		gs=rgbvalue.gs;   //G
		bs=rgbvalue.bs;   //B    Of srcImage
		rs1=rgbvalue1.rs;   //R
		gs1=rgbvalue1.gs;   //G
		bs1=rgbvalue1.bs;   //B	  Of image1
	   	
	    int w=width;int W=width1;
	    int h=height;int H=height1;	  //W H is the maximum,by contrast,w h is the minimum.
		if (width > width1) {
			w=width1;
			W=width;
		} 
		if (height > height1) {
			h=height1;
			H=height;
		}
		 BufferedImage destImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
		
	    for (int j = 0; j < H; j++) 
			for(int i=0; i<W; i++) 
				if(j<h+dy&&j>=dy&&i<w+dx&&i>=dx) {
					if(gs1[i-dx][j-dy]>=Gmin&&rs1[i-dx][j-dy]<=Rmax&&bs1[i-dx][j-dy]<=Bmax) {
						rgb[0]=rs[i][j];
						rgb[1]=gs[i][j];
						rgb[2]=bs[i][j];
						destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));	
					}
					else
					{
						rgb[0]=rs1[i-dx][j-dy];
						rgb[1]=gs1[i-dx][j-dy];
						rgb[2]=bs1[i-dx][j-dy];
						destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
					}
				}else {
					rgb[0]=rs[i][j];
					rgb[1]=gs[i][j];
					rgb[2]=bs[i][j];
					destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
				}						
		return destImage;
	}

	public static BufferedImage image_fourier(BufferedImage srcImage) {
		int width =srcImage.getWidth();
		int height=srcImage.getHeight();
		
		int rgb[]=new int[3];
		int rs[][]; //
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		rs=rgbvalue.rs;   //R
		bs=rgbvalue.bs;   //b
		
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
	    Complex result[][]=new Complex[width][height];
	    result=Fourier(rs);
	    double Model[][]=new double[width][height];
	    double max=0;
	    for (int j = 0; j < height; j++) 
				for(int i=0; i<width; i++) {
					Model[i][j]=result[i][j].mod();
					if (max<Model[i][j])
						max=Model[i][j];
				}
	    double C=255/Math.log10(1+max);
	    for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) {
				
				int temp=(int)(C*Math.log10(1+Model[i][j])+0.5f);
		    	if(temp>255)
		    		temp=255;
				rgb[0]=temp;
		    	rgb[1]=temp;
		    	rgb[2]=temp;
		    	
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}
		    	
			return destImage;
	}

	public static BufferedImage image_stretch(BufferedImage srcImage)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		//Size of  picture.
		
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		
    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //B    Of srcImage
    	BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		rs=changeImage(rs);
		gs=changeImage(gs);
		bs=changeImage(bs);
		 
        for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) 
			{
				if(rs[i][j]>255)
					rs[i][j]=255;
				rgb[0]=rs[i][j];
				rgb[1]=rs[i][j];
				rgb[2]=rs[i][j];
				destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			}
				
										
		return destImage;
	}
	public static BufferedImage image_logarithTransform(BufferedImage srcImage,double b)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		//Size of  picture.
		
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		
		rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //B    Of srcImage
    	BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
    	double C=255/(Math.log(1+255)/Math.log(b)); 
        for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) 
			{	rgb[0]=logarithTransform(rs[i][j], C,b);
				rgb[1]=logarithTransform(gs[i][j], C,b);
				rgb[2]=logarithTransform(bs[i][j], C,b);
				destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));	
			}	
		return destImage;
	}
	public static BufferedImage image_powerTransform(BufferedImage srcImage,double gama)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		//Size of  picture.s
		
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		
		rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //B    Of srcImage
    	BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	 
        for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) 
			{	rgb[0]=powerTransform(rs[i][j], gama);
				rgb[1]=powerTransform(gs[i][j], gama);
				rgb[2]=powerTransform(bs[i][j], gama);
				destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));	
			}	
		return destImage;
	}
	public static BufferedImage image_transform(BufferedImage srcImage,double dx,double dy) {
		int width =(int)(srcImage.getWidth()+dx+0.5);
		int height = (int)(srcImage.getHeight()+dy+0.5);
		
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
    	rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //b
    	
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
        for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) {
				double ftemp;
				ftemp=i-dx;
		    	int I=(int)(ftemp+0.5);   // X before Rotation
		    	ftemp=j-dy;
		    	int J=(int)(ftemp+0.5);	  // Y before Rotation
		 
		    	if(I>=0&&J>=0&&I<srcImage.getWidth()&&J<srcImage.getHeight()) {
		    	rgb[0]=rs[I][J];
		    	rgb[1]=gs[I][J];
		    	rgb[2]=bs[I][J];
		    	
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}
		    	else
		    	{	int a[]= {255,255,255};
		    		destImage.setRGB(i,j, ImageUtil.encodeColor(a));
			    		
		    	}
			}	
			return destImage;
	}
	public static BufferedImage image_histEqlRGB(BufferedImage srcImage)
	{
		int width =srcImage.getWidth();
		int height=srcImage.getHeight();
		
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		rs=rgbvalue.rs;   //R
    	gs=rgbvalue.gs;   //G
    	bs=rgbvalue.bs;   //b
    	
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int function1[]=new int [256];
		int function2[]=new int [256];
		int function3[]=new int [256];
		function1=histEql_RGB(rs);
		function2=histEql_RGB(gs);
		function3=histEql_RGB(bs);
	    for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) {
		    	rgb[0]=function1[rs[i][j]];
		    	rgb[1]=function2[gs[i][j]];
		    	rgb[2]=function3[bs[i][j]];
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}
		    	
			return destImage;
	}
	public static BufferedImage image_histEqlHSI(BufferedImage srcImage)
	{
		int width =srcImage.getWidth();
		int height=srcImage.getHeight();
		double H[][]=new double [width][height];
		double S[][]=new double [width][height];
		double I[][]=new double [width][height];
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		rs=rgbvalue.rs;   //R
		gs=rgbvalue.gs;   //G
		bs=rgbvalue.bs;   //b
		
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int new_I[]=new int [256];
		
		
	    for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) {
		       	double  tempHSI[]=rgb2hsi(rs[i][j], gs[i][j], bs[i][j]);
		   		H[i][j]=tempHSI[0];
		   		S[i][j]=tempHSI[1];
		   		I[i][j]=(int)(tempHSI[2]+0.5);
		    	}
	   new_I=histEql_RGB(I);
	    
	    for (int j = 0; j < height; j++) 
			for(int i=0; i<width; i++) {
		    	int tempI=new_I[(int)I[i][j]];
				double tempRGB[]=hsi2rgb(H[i][j], S[i][j],tempI);//hfksadhfkjdh
				rgb[0]=(int)(tempRGB[0]+0.5);
				if(rgb[0]>255)	rgb[0]=255;
				if(rgb[0]<0)	rgb[0]=0;
				
				rgb[1]=(int)(tempRGB[1]+0.5);
				if(rgb[1]>255)	rgb[1]=255;
				if(rgb[1]<0)	rgb[1]=0;
				
				rgb[2]=(int)(tempRGB[2]+0.5);
				if(rgb[2]>255)	rgb[2]=255;	
				if(rgb[2]<0)	rgb[2]=0;
				
		    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
		    	}
	
			return destImage;
	}

	public static BufferedImage image_smthFliter(BufferedImage srcImage,String opt)
	{
		int width = srcImage.getWidth();
		int height = srcImage.getHeight();
		int rgb[]=new int[3];
		int rs[][]=new int[width][ height]; //
		int gs[][]=new int[width][ height];
		int bs[][]=new int[width][ height];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
	
		rs=rgbvalue.rs;   //R
		gs=rgbvalue.gs;   //G
		bs=rgbvalue.bs;   //b
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
	    if(opt.equals("1"))
	    {
	    	for (int j = 0; j<height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=(int)(fliterSmooth(i, j, 3, width, height, rs)+0.5f);
			    	rgb[1]=(int)(fliterSmooth(i, j, 3, width, height, gs)+0.5f);
			    	rgb[2]=(int)(fliterSmooth(i, j, 3, width, height, bs)+0.5f);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}	
	    }
	    if(opt.equals("2"))
	    {
	    	for (int j = 0; j<height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=(int)(fliterMedian(i, j, 3, width, height, rs)+0.5f);
			    	rgb[1]=(int)(fliterMedian(i, j, 3, width, height, gs)+0.5f);
			    	rgb[2]=(int)(fliterMedian(i, j, 3, width, height, bs)+0.5f);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}	
	    }
	    if(opt.equals("3"))
	    {
	    	for (int j = 0; j<height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=(int)(fliterArithmeticMean(i, j, 3, width, height, rs)+0.5f);
			    	rgb[1]=(int)(fliterArithmeticMean(i, j, 3, width, height, gs)+0.5f);
			    	rgb[2]=(int)(fliterArithmeticMean(i, j, 3, width, height, bs)+0.5f);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}	
	    }
	    if(opt.equals("4"))
		{
			for (int j =0 ; j < height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb=fliter_vectorMedian(i, j, width, height, rs, gs, bs);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}
		}
		return destImage;
	}

	public static BufferedImage image_laplace(BufferedImage srcImage,String option)
	{
		int width =srcImage.getWidth();
		int height=srcImage.getHeight();
		
		int rgb[]=new int[3];
		int rs[][]; //
		int gs[][];
		int bs[][];
		RGBvalue rgbvalue=new RGBvalue();
		rgbvalue=getRBG(srcImage);
		rs=rgbvalue.rs;   //R
		gs=rgbvalue.gs;   //G
		bs=rgbvalue.bs;   //b
		
	    BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		if(option.equals("1"))
		{
			for (int j = 0; j < height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=laplace_sobel(i, j, width, height, rs);
			    	rgb[1]=laplace_sobel(i, j, width, height, gs);
			    	rgb[2]=laplace_sobel(i, j, width, height, bs);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}
	
		}
	
		if(option.equals("2"))
		{
			for (int j = 0; j < height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=laplace_prewitt(i, j, width, height, rs);
			    	rgb[1]=laplace_prewitt(i, j, width, height, gs);
			    	rgb[2]=laplace_prewitt(i, j, width, height, bs);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}
		}
		if(option.equals("3"))
		{
			for (int j = 0; j < height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=(int)(grad_roberts(i, j, width, height, rs)+0.5f);
			    	rgb[1]=(int)(grad_roberts(i, j, width, height, gs)+0.5f);
			    	rgb[2]=(int)(grad_roberts(i, j, width, height, bs)+0.5f);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}
		}
		if(option.equals("4"))
		{
			for (int j = 0; j < height; j++) 
				for(int i=0; i<width; i++) {
			    	rgb[0]=laplace_1(i, j, width, height, rs);
			    	rgb[1]=laplace_1(i, j, width, height, gs);
			    	rgb[2]=laplace_1(i, j, width, height, bs);
			    	destImage.setRGB(i,j, ImageUtil.encodeColor(rgb));
			    	}
		}	 
		
			return destImage;
	}


	public static float grad_roberts(int x,int y,int ImageWidth,int ImageHeight,int rgb[][])
	{
				int tempx=x;int tempy=y;
		    	tempx=tempx>(ImageWidth-1)?ImageWidth-1:tempx;
		    	tempy=tempy>(ImageHeight-1)?ImageHeight-1:tempy;
				return ((int)Math.max(Math.abs(rgb[tempx+1][tempy+1]-rgb[tempx][tempy] ),
						Math.abs( rgb[tempx+1][tempy]-rgb[tempx][tempy+1])));
	
	}

	public static int laplace_1(int x,int y,int ImageWidth,int ImageHeight,int rgb[][])
	{
				int t[][]= {{-1,-1,-1},
						   {-1,8,-1},
						   {-1,-1,-1} };
			int cx=x;int cy=y;
			int x1=Math.abs(cx-1);int x2=(cx+1)>ImageWidth-1?ImageWidth-1:cx+1;
			int y1=Math.abs(cy-1);int y2=(cy+1)>ImageHeight-1?ImageHeight-1:cy+1;
		int result=t[0][0]*rgb[x1][y1] + t[0][1]*rgb[cx][y1]+ t[0][2]*rgb[x2][y1]+
				   t[1][0]*rgb[x1][y]  + t[1][1]*rgb[cx][y] + t[1][2]*rgb[x2][y] + 		
				   t[2][0]*rgb[x1][y2] + t[2][1]*rgb[cx][y2]+ t[2][2]*rgb[x2][y2];
			return (result=result>0?result:0)>255?255:result;
	}
	public static int laplace_prewitt(int x,int y,int ImageWidth,int ImageHeight,int rgb[][])
	{
				int tempx=x>0?x:1;int tempy=y>0?y:1;
		    	tempx=(tempx>=(ImageWidth-1))?ImageWidth-2:tempx;
		    	tempy=(tempy>=(ImageHeight-1))?ImageHeight-2:tempy;
				int gx=rgb[tempx-1][tempy+1]+rgb[tempx-1][tempy]+rgb[tempx-1][tempy-1]
					   -(rgb[tempx+1][tempy+1]+rgb[tempx+1][tempy]+rgb[tempx+1][tempy-1]);
				int gy=rgb[tempx-1][tempy+1]+rgb[tempx][tempy+1]+rgb[tempx+1][tempy+1]
					  -( rgb[tempx-1][tempy-1]+rgb[tempx][tempy-1]+rgb[tempx+1][tempy-1]);
			return(int)(Math.sqrt(gx*gx+gy*gy)+0.5f);
	}//Prewitt
	public static int laplace_sobel(int x,int y,int ImageWidth,int ImageHeight,int rgb[][])
	{
				int tempx=x>0?x:1;int tempy=y>0?y:1;
		    	tempx=(tempx>=(ImageWidth-1))?ImageWidth-2:tempx;
		    	tempy=(tempy>=(ImageHeight-1))?ImageHeight-2:tempy;
				int gx=rgb[tempx-1][tempy+1]+2*rgb[tempx-1][tempy]+rgb[tempx-1][tempy-1]
					   -(rgb[tempx+1][tempy+1]+2*rgb[tempx+1][tempy]+rgb[tempx+1][tempy-1]);
				int gy=rgb[tempx-1][tempy+1]+2*rgb[tempx][tempy+1]+rgb[tempx+1][tempy+1]
					  -( rgb[tempx-1][tempy-1]+2*rgb[tempx][tempy-1]+rgb[tempx+1][tempy-1]);
			return(int)(Math.sqrt(gx*gx+gy*gy)+0.5f);
	}
	public static int laplace_3(int x,int y,int ImageWidth,int ImageHeight,int rgb[][])
	{
			int t[][]= {	{0,-1,0},
							{-1,4,-1},
							{0,-1, 0} };
				int cx=x;int cy=y;
				int x1=Math.abs(cx-1);int x2=(cx+1)>ImageWidth-1?ImageWidth-1:cx+1;
				int y1=Math.abs(cy-1);int y2=(cy+1)>ImageHeight-1?ImageHeight-1:cy+1;
			int result=t[0][0]*rgb[x1][y1] + t[0][1]*rgb[cx][y1]+ t[0][2]*rgb[x2][y1]+
					   t[1][0]*rgb[x1][y]  + t[1][1]*rgb[cx][y] + t[1][2]*rgb[x2][y] + 		
					   t[2][0]*rgb[x1][y2] + t[2][1]*rgb[cx][y2]+ t[2][2]*rgb[x2][y2];
					
			return (result=result>0?result:0)>255?255:result;
		
	}
	public static int[] histEql_RGB(int rgb[][])
	{
		int function[]=new int [256];
		double frequency[]=new double[256];
		for(int i=0;i<rgb.length;i++)
			for(int j=0;j<rgb[0].length;j++)
				frequency[rgb[i][j]]++;
		for(int i=0;i<frequency.length;i++)
				frequency[i]/=(rgb.length*rgb[0].length);
		for(int i=0;i<frequency.length;i++)
		{
				frequency[i]+=(i==0?0:frequency[i-1]);
				function[i]=(int)(255*frequency[i]+0.5f);
		}
		
	return function;
	}
	public static int[] histEql_RGB(double rgb[][])
	{
		int function[]=new int [256];
		double frequency[]=new double[256];
		for(int i=0;i<rgb.length;i++)
			for(int j=0;j<rgb[0].length;j++)
				frequency[ (int)rgb[i][j] ]++;
		for(int i=0;i<frequency.length;i++)
				frequency[i]/=(rgb.length*rgb[0].length);
		for(int i=0;i<frequency.length;i++)
		{
				frequency[i]+=(i==0?0:frequency[i-1]);
				function[i]=(int)(255*frequency[i]+0.5f);
		}		
		
	return function;
	}
	public static int[] histEql_HSI(int HSI[][])
	{
		int function[]=new int [361];
		double frequency[]=new double[361];
		for(int i=0;i<HSI.length;i++)
			for(int j=0;j<HSI[0].length;j++)
				frequency[HSI[i][j]]++;
		for(int i=0;i<frequency.length;i++)
				frequency[i]/=(HSI.length*HSI[0].length);
		for(int i=0;i<frequency.length;i++)
		{
				frequency[i]+=(i==0?0:frequency[i-1]);
				function[i]=(int)(360*frequency[i]+0.5f);
		}		
		
	return function;
	}
	public static double[] rgb2hsi(int r,int g,int b)
	{
		double HSI[]=new double[3];
		double R=r; double G=g;double B=b;
		double t;
		if(R==G&&R==B&&G==B)
		t=0;
		else
		t=Math.acos(  (2*R-G-B) /  (2*Math.sqrt(  (R-G)*(R-G)+(R-B)*(G-B) ))   );
		HSI[0]=(B<=G ? t : 2*Math.PI-t);
		
		double minRGB=Math.min(Math.min(R, G), B);
		if(R<1&&G<1&&B<1)
			HSI[1]=0;
		else
		HSI[1]=1-3*minRGB/(R+G+B);
		
		HSI[2]=(R+G+B)/3;
		return HSI;
	}

	public static double[] hsi2rgb(double H,double S,double I)
	{
		double RGB[]=new double [3];
		double thea=0;
		if(H<=Math.PI*2/3)
		thea=0;	
		else if
		(H<=Math.PI*4/3)
		thea=Math.PI*2/3;
		else
		thea=Math.PI*4/3;		
		
		double t1=I*(1-S);
		double t2=I*(1+S*Math.cos(H-thea)/Math.cos(thea+Math.PI/3-H));
		double t3=3*I-(t1+t2);
		
		if(H<=Math.PI*2/3)
		{
			RGB[2]=t1;RGB[0]=t2;RGB[1]=t3;
		}
		else if(H<=Math.PI*4/3)
		{
			RGB[0]=t1;RGB[1]=t2;RGB[2]=t3;
		}
		else {
			RGB[1]=t1;RGB[2]=t2;RGB[0]=t3;
		}
		return RGB;
	}
	public static int[]interpol_nearest(double X0,double Y0,int rs[][],int gs[][],int bs[][],int width,int height)
	{
		int result[]=new int[3];
		int I=(int)(X0+0.5);
		int J=(int)(Y0+0.5);	  // Y before Rotation
		if(I>=0&&J>=0&&I<width&&J<height) {
	    	
			result[0]=rs[I][J];
			result[1]=gs[I][J];
			result[2]=bs[I][J];
		}else
	    	{	result[0]=255;
				result[1]=255;
				result[2]=255;			    		
	    	}
		return result;
	}
	
	public static int[] interpol_doublinear(double x0,double y0,int rs[][],int gs[][],int bs[][],int width,int height)
	{	int p00,p01,p10,p11;
		int result[]=new int[3];
		if(x0>=0 && y0>=0&& x0+1<width && y0+1<height ) {
		p00=rs[(int)x0][(int)y0];
		p01=rs[(int)x0][(int)y0+1];
		p10=rs[(int)x0+1][(int)y0];
		p11=rs[(int)x0+1][(int)y0+1];
		result[0]=interpol_doublinaer(x0, y0, p00, p01, p10, p11);
		p00=gs[(int)x0][(int)y0];
		p01=gs[(int)x0][(int)y0+1];
		p10=gs[(int)x0+1][(int)y0];
		p11=gs[(int)x0+1][(int)y0+1];
		result[1]=interpol_doublinaer(x0, y0, p00, p01, p10, p11);
		p00=bs[(int)x0][(int)y0];
		p01=bs[(int)x0][(int)y0+1];
		p10=bs[(int)x0+1][(int)y0];
		p11=bs[(int)x0+1][(int)y0+1];
		result[2]=interpol_doublinaer(x0, y0, p00, p01, p10, p11);
		}
		else
		{	result[0]=255;
			result[1]=255;
			result[2]=255;			    		
		}
		return result;	
	}//Find the target point. 
	
	public static int interpol_doublinaer(double x0,double y0,int c00,int c01,int c10,int c11)
	{
		int color=0;
		double x=x0-(int)(x0);
		double y=y0-(int)(y0);
		color=(int)( c00*(1-x)*(1-y)+c10*x*(1-y)+c01*(1-x)*y+c11*x*y);
		return color;
	} //Calculate the color at one point by double linear .
	public static int MAX(int array[][])
	{
		int temp=0;
		for(int i=0;i<array.length;i++)
			for(int j=0;j<array[0].length;j++)
			temp=(array[i][j]>temp?array[i][j]:temp);
		return temp;
	}
	public static int MIN(int array[][])
	{
		int temp=255;
		for(int i=0;i<array.length;i++)
			for(int j=0;j<array[0].length;j++)
			temp=(array[i][j]<temp?array[i][j]:temp);
		return temp;
	}
	public static Complex[][] Fourier(int rgb[][])
	{
		Complex G[][]=new Complex[rgb.length][rgb[0].length];
		Complex F[][]=new Complex[rgb.length][rgb[0].length];
		int Nx=G.length;int Ny=G[0].length;
		int x,y,u,v=0;
		for( x=0;x<Nx;x++)
			for( v=0;v<Ny;v++)
			{	G[x][v]=new Complex(0, 0);
				for( y=0;y<Ny;y++)
				G[x][v]=G[x][v].add( new Complex(0, -2*Math.PI*v*y/Ny).exp().
							multiple((double)(rgb[x][y]*Math.pow(-1, x+y))/Math.sqrt(Nx*Ny)) );
			    
			}
		for( u=0;u<Nx;u++ )
			for(v=0;v<Ny;v++)
			{
				F[u][v]=new Complex(0,0);
				for(x=0;x<Nx;x++)
					F[u][v]=F[u][v].add( new Complex(0, -2*Math.PI*u*x/Nx).exp().
							multiple(G[x][v]).multiple(1/Math.sqrt(Nx*Ny) ));
				
			}
		return F;
	}

}
