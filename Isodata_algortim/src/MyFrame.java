
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;


public class MyFrame extends javax.swing.JFrame{
	private Image image;
private JLabel drawLabel;
// коор. верхнего левого угла изображения
private int x, y;
// ширина увеличительного окна
private int widthBox;
// высота увеличительного окна
private int heightBox;
// коеф. увеличения изображения
private int zoom;
// скорость перемещения
private int speed;
// ширина изображения
private int width;
// высота изображения
private int height;
// ширина превъю изображения
private int widthPreview;
// высота превъю изображения
private int heightPreview;
// процент от размера главного окна
private double percentPreview;
// вспомогательные перем.
private int x1, y1;
private int x2 ,y2;
// коор. верхнего левого угла прямоугольника
private int xRect;
private int yRect;
// ширина и высота прямоугольника
private int widthRect;
private int heightRect;
// прозрачность прямоугольника
private float alpha;
 private Color BLUE;
    private Color CYAN;
    private Color GREEN;
    private Color MAGENTA;
    private Color ORANGE;
    private Color PINK;
    private Color RED;
    private Color YELLOW;
    private Color LIGHT_GRAY;
private Color colors[] = { BLUE, CYAN, GREEN,
MAGENTA, ORANGE, PINK,
RED, YELLOW, LIGHT_GRAY
};
private Image img; 
	File w;
	public int wimg;
	public int himg;
        int i = 0;
        JPanel a = new JPanel();
        public MyFrame (String str){
		super(str);
		                        
		final JFileChooser fc = new JFileChooser();
		   
		JMenu file = new JMenu ("File (F)");
		file.setMnemonic('F');
		final JButton open = new JButton ("Open");
		open.setMnemonic('O');
		open.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent ev){
	            	   if (ev.getSource() == open) {
	                       int returnVal = fc.showOpenDialog(MyFrame.this);

	                       if (returnVal == JFileChooser.APPROVE_OPTION) {
	                           File file = fc.getSelectedFile();
                                   w=file;
	                           		BufferedImage bi;
	                           		  if(i==1)
	                           		  {
	                           			//a.removeAll();
	                                   //removeAll();
                                                      remove(drawLabel);
	                                    repaint();
	                                    setVisible(true);
	                                    
	                           		  }
                                                try
	                           			{
	                           				
	                           				bi=ImageIO.read(file);
											//ImageImplement panel = new ImageImplement(new ImageIcon(bi).getImage());
											//wimg= new ImageIcon(bi).getImage().getWidth(null) ;
											//himg=new ImageIcon(bi).getImage().getHeight(null);
                                                                     
                                                                     // a.add(panel);
                                                                        image = new ImageIcon(bi).getImage();

 
width = new ImageIcon(bi).getImage().getWidth(null) ;
height = new ImageIcon(bi).getImage().getHeight(null);
 
setMaximizedBounds(new Rectangle(width, height));
 
zoom = 2;
speed = 5;
alpha = 0.3f;
percentPreview = 0.25;
 
x = width * zoom / 3;
y = height * zoom / 3;
 
drawLabel = new JLabel() {
public void paintComponent(Graphics g) {
drawLabel.setMaximumSize(new Dimension(500, 400));
 
widthBox = getWidth();
heightBox = getHeight();
 
// макс. ширина и высота левого верхнего угла
int maxX = zoom * width - widthBox;
int maxY = zoom * height - heightBox;
 
if (x > maxX || y > maxY) {
x = maxX;
y = maxY;
}
g.drawImage(image, -x, -y, width * zoom, height * zoom, null);
 
widthPreview = (int) (widthBox * percentPreview);
heightPreview = (int) (heightBox * percentPreview);
 
g.drawImage(image, 0, 0, widthPreview, heightPreview, null);
 
// коэффициент ширины и высоты
double widthFactor = (double) (zoom * width)
/ (zoom * width - x);
double heightFactor = (double) (zoom * height)
/ (zoom * height - y);
 
// коор. верхнего левого угла прямоугольника
int xRect = widthPreview - (int) (widthPreview / widthFactor);
int yRect = heightPreview - (int) (heightPreview / heightFactor);
 
// ширина и высота прямоугольника
int widthRect = (int) (widthPreview / (double) (zoom * width / (double) widthBox));
int heightRect = (int) (heightPreview / (double) (zoom * height / (double) heightBox));
 
Graphics2D g2 = (Graphics2D) g;
AlphaComposite ac = AlphaComposite.SrcOver.derive(alpha);
g2.setColor(colors[6]);
g2.setComposite(ac);
g2.fillRect(xRect, yRect, widthRect, heightRect);
g2.setColor(colors[0]);
g2.drawRect(xRect, yRect, widthRect, heightRect);
}
};
 
drawLabel.addMouseListener(new MouseAdapter() {
                                @Override
public void mousePressed(MouseEvent e) {
setCursor(Cursor.MOVE_CURSOR);
x1 = e.getX();
y1 = e.getY();
// при клике на миниатюре отображаем выбранную часть изображения
if(x1 <= widthPreview && y1 <= heightPreview) {
x = (int) (x1 * (double)(width * zoom - widthBox) / widthPreview);
y = (int) (y1 * (double)(height * zoom - heightBox) / heightPreview);
 
drawLabel.repaint();
}
}
                                @Override
public void mouseReleased(MouseEvent e) {
setCursor(Cursor.DEFAULT_CURSOR);
}
});
 
drawLabel.addMouseMotionListener(new MouseMotionAdapter() {
                                @Override
public void mouseDragged(MouseEvent e) {
//setCursor(Cursor.MOVE_CURSOR);
 
x1 = e.getX();
y1 = e.getY();
 
if (y2 > y1) {
y += speed;
} else if (y2 < y1) {
y -= speed;
}
 
if (x2 > x1) {
x += speed;
} else if (x2 < x1) {
x -= speed;
}
 
x2 = x1;
y2 = y1;
drawLabel.repaint();
 
if (x < 0) {
x = 0;
} else if (x > width * zoom - widthBox) {
x = width * zoom - widthBox;
}
 
if (y < 0) {
y = 0;
} else if (y > height * zoom - heightBox) {
y = height * zoom - heightBox;
}
}
});
 
addMouseWheelListener(new MouseWheelListener() {
public void mouseWheelMoved(MouseWheelEvent e) {
int point = e.getWheelRotation();
if (point < 0 && zoom > 1) {
zoom--;
drawLabel.repaint();
} else if (point > 0) {
zoom++;
drawLabel.repaint();
}
}
});
 
add(drawLabel);
setSize(800,600); 

setVisible(true);                      

                                                                       
                                                                       i=1;
                                                                       
						                      // add(panel);
                                                                       
						                       setVisible(true);
						                     
                                                                       //setSize(800,600);
	                           			}
	                           			catch(IOException e){}
	            		   }
	                   }
	               }
		});
		
		
		JMenuBar bar = new JMenuBar();
		bar.add(open);
		setJMenuBar(bar);
		//bar.add(file);
		JButton getstart = new JButton("GetSTART");
		getstart.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
                try {
                    Filein b= new Filein(w);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
               String [] www = null;
				int res;
				try {
					res = ToolRunner.run(new Configuration(), new isodata_algorithm(w), www);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
				try {
                   izmenit Kist = new izmenit(w,wimg,himg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
				BufferedImage bi;
				File ja = new File("newImage.png");  
				         			//a.removeAll();
                remove(drawLabel);
	         repaint();
	                                    setVisible(true);
                  
         		  
                          try
         			{
         				
         				bi=ImageIO.read(ja);
						/*ImageImplement panel = new ImageImplement(new ImageIcon(bi).getImage());
	                                                                  
                                                a.add(panel);
                                               i=1;
                                                 
	                       add(a);
                                                 
	                       setVisible(true);
	                     
                                                 //setSize(800,600);*/
                                       image = new ImageIcon(bi).getImage();
                                        width = new ImageIcon(bi).getImage().getWidth(null) ;
height = new ImageIcon(bi).getImage().getHeight(null);
 
setMaximizedBounds(new Rectangle(width, height));
 
zoom = 2;
speed = 5;
alpha = 0.3f;
percentPreview = 0.25;
 
x = width * zoom / 3;
y = height * zoom / 3;
 
drawLabel = new JLabel() {
public void paintComponent(Graphics g) {
drawLabel.setMaximumSize(new Dimension(500, 400));
 
widthBox = getWidth();
heightBox = getHeight();
 
// макс. ширина и высота левого верхнего угла
int maxX = zoom * width - widthBox;
int maxY = zoom * height - heightBox;
 
if (x > maxX || y > maxY) {
x = maxX;
y = maxY;
}
g.drawImage(image, -x, -y, width * zoom, height * zoom, null);
 
widthPreview = (int) (widthBox * percentPreview);
heightPreview = (int) (heightBox * percentPreview);
 
g.drawImage(image, 0, 0, widthPreview, heightPreview, null);
 
// коэффициент ширины и высоты
double widthFactor = (double) (zoom * width)
/ (zoom * width - x);
double heightFactor = (double) (zoom * height)
/ (zoom * height - y);
 
// коор. верхнего левого угла прямоугольника
int xRect = widthPreview - (int) (widthPreview / widthFactor);
int yRect = heightPreview - (int) (heightPreview / heightFactor);
 
// ширина и высота прямоугольника
int widthRect = (int) (widthPreview / (double) (zoom * width / (double) widthBox));
int heightRect = (int) (heightPreview / (double) (zoom * height / (double) heightBox));
 
Graphics2D g2 = (Graphics2D) g;
AlphaComposite ac = AlphaComposite.SrcOver.derive(alpha);
g2.setColor(colors[6]);
g2.setComposite(ac);
g2.fillRect(xRect, yRect, widthRect, heightRect);
g2.setColor(colors[0]);
g2.drawRect(xRect, yRect, widthRect, heightRect);
}
};
 
drawLabel.addMouseListener(new MouseAdapter() {
                                @Override
public void mousePressed(MouseEvent e) {
setCursor(Cursor.MOVE_CURSOR);
x1 = e.getX();
y1 = e.getY();
// при клике на миниатюре отображаем выбранную часть изображения
if(x1 <= widthPreview && y1 <= heightPreview) {
x = (int) (x1 * (double)(width * zoom - widthBox) / widthPreview);
y = (int) (y1 * (double)(height * zoom - heightBox) / heightPreview);
 
drawLabel.repaint();
}
}
                                @Override
public void mouseReleased(MouseEvent e) {
setCursor(Cursor.DEFAULT_CURSOR);
}
});
 
drawLabel.addMouseMotionListener(new MouseMotionAdapter() {
                                @Override
public void mouseDragged(MouseEvent e) {
//setCursor(Cursor.MOVE_CURSOR);
 
x1 = e.getX();
y1 = e.getY();
 
if (y2 > y1) {
y += speed;
} else if (y2 < y1) {
y -= speed;
}
 
if (x2 > x1) {
x += speed;
} else if (x2 < x1) {
x -= speed;
}
 
x2 = x1;
y2 = y1;
drawLabel.repaint();
 
if (x < 0) {
x = 0;
} else if (x > width * zoom - widthBox) {
x = width * zoom - widthBox;
}
 
if (y < 0) {
y = 0;
} else if (y > height * zoom - heightBox) {
y = height * zoom - heightBox;
}
}
});
 
addMouseWheelListener(new MouseWheelListener() {
public void mouseWheelMoved(MouseWheelEvent e) {
int point = e.getWheelRotation();
if (point < 0 && zoom > 1) {
zoom--;
drawLabel.repaint();
} else if (point > 0) {
zoom++;
drawLabel.repaint();
}
}
});
setSize(800,600); 
add(drawLabel);
 

setVisible(true);     
         			}
         			catch(IOException e1){}	
			}
		});
		bar.add(getstart);
		JMenuItem count = new JMenuItem ("Count Pixels (C)");
		count.setMnemonic('C');
		count.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
                try {
                    Filein b= new Filein(w);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
			}
		});
		file.add(count);
		
		JMenuItem move = new JMenuItem ("Show image");
		move.setMnemonic('M');
		move.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				BufferedImage bi = null;
				File ja = new File("newImage.png");  
				if(i==1)
         		  {
         			a.removeAll();
                  remove(a);
                  repaint();
                  setVisible(true);
                  
         		  }
                try {
                    bi=ImageIO.read(ja);
                } catch (IOException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
						ImageImplement panel = new ImageImplement(new ImageIcon(bi).getImage());
	                                                                  
                                                a.add(panel);
                                               i=1;
                                                 
	                       add(a);
                                                 
	                       setVisible(true);
	                     
                                                 //setSize(800,600);
         			
         			
	                
			}
		});
		
		
		JMenuItem hadoop = new JMenuItem ("Hadoop (H)");
		hadoop.setMnemonic('H');
		hadoop.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				String [] www = null;
				int res;
				try {
					res = ToolRunner.run(new Configuration(), new isodata_algorithm(w), www);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//System.exit(res);
				
			}
		});
		file.add(hadoop);
		
		JMenuItem show = new JMenuItem ("Pixel");
		show.setMnemonic('S');
		show.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
                try {
                    izmenit Kist = new izmenit(w,wimg,himg);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MyFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
			}
		});
		file.add(show);
		file.add(move);
		//JMenuItem exit = new JMenuItem ("Exit (E)");
		JButton exit = new JButton ("Exit");
		exit.setMnemonic('E');
		exit.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
			setVisible(false);
			dispose();
			}
		});
		file.add(exit);
		
		JMenu help = new JMenu ("Help (H)");
		help.setMnemonic('H');
		JMenuItem about = new JMenuItem ("About (A)");
		about.setMnemonic('A');
		about.addActionListener(new ActionListener (){
			public void actionPerformed (ActionEvent e){
				JOptionPane.showMessageDialog(null, "About");
			}
		});
		help.add(about);
		//bar.add(help);
		bar.add(exit);
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		setTitle("Window");
		setSize (800,600);
		setVisible(true);
		setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}

}