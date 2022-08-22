package src.component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.*;

public class ImagePanel extends JComponent implements MouseListener{
    protected float scale = 1;          // ���ű���
    protected Image image = null;       // ��Ҫ��ʾ��ͼ��
    protected int x0,y0,x1,y1,cline;
    
    public ImagePanel(Image image) {
        this.image=image;
    }
    
    public void setImage(Image image) {
        this.image = image;
        setSize(getPreferredSize());
    }
    
    
    public void setScale(float scale) {
        this.scale = scale;
        setSize(getPreferredSize());
    }
    
    protected Dimension getImageSize() {
        if(image != null) {
            return new Dimension(Math.round(image.getWidth(null)*scale), Math.round(image.getHeight(null)*scale));
        }
        else return new Dimension(0, 0);
    }
    
   
    public Dimension getPreferredSize() {
        return getImageSize();    
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if(image == null) return ;
        Dimension destDim = getImageSize();
        g.drawImage(image, (getWidth()-destDim.width)/2, (getHeight()-destDim.height)/2, 
        		(getWidth()-destDim.width)/2+destDim.width,  (getHeight()-destDim.height)/2+destDim.height,
                    0, 0, image.getWidth(null), image.getHeight(null), null);
       
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("1234567890");
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
