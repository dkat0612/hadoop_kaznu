/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

/**
 *
 * @author hadoop
 */
public class izmenit  {
    Image img1;
    int hasd;
    izmenit(File s,int wimg,int himg) throws InterruptedException, IOException 
    {	
    	
    Color a[] = {new Color(255,0,0, 125),new Color(0,255,0, 125),new Color(0,0,255, 125),new Color(0,255,255, 125),new Color(100,255,100, 125),new Color(0,255,55, 125),new Color(255,255,55, 125)};
    img1 = (Image) ImageIO.read(s);
    BufferedImage img = (BufferedImage) img1;
    int h = img.getHeight();
    int w = img.getWidth();
    // создаем новое изображение
    BufferedImage newImage = new BufferedImage(w, h,
            BufferedImage.TYPE_INT_ARGB);
          // получаем канву, на которой можно рисовать
    Graphics g = newImage.getGraphics();
   
    
    // помещаем в центр загруженное изображение
    g.drawImage(img, 0, 0, null);
    int k = 50, i = 0;
    int x=0,y=0;    
    String q;
        Scanner in = new Scanner(new File("12"));
        String hs = "";
        while(in.hasNext())
        {
            q = in.nextLine() + "\r\n";
        	/*StringTokenizer t = new StringTokenizer(q,":");
        	String key = t.nextToken();
			String val = t.nextToken();
			StringTokenizer t_key = new StringTokenizer(val);
			//String[] strs=val.split(" ");
            int n=0;
             //x=Integer.valueOf(strs[1]);
             
              g.setColor(a[i]);
            while(t_key.hasMoreTokens())
            {  //  System.out.println(x); 
            	
            	int id = Integer.parseInt(t_key.nextToken());
            	int L = Integer.parseInt(t_key.nextToken());
				 int A = Integer.parseInt(t_key.nextToken());
				 int B = Integer.parseInt(t_key.nextToken());
            	//System.out.println(id);  
             g.drawLine(id/w, id%w, id/w, id%w);
             }
         	*/
            StringTokenizer t = new StringTokenizer(q);
            while (t.hasMoreTokens())
            {
            	int x1 = Integer.valueOf(t.nextToken());
            	int y1 = Integer.valueOf(t.nextToken());
            	int r1 = Integer.valueOf(t.nextToken());
            	int g1 = Integer.valueOf(t.nextToken());
            	int b1 = Integer.valueOf(t.nextToken());
            	int x2 = Integer.valueOf(t.nextToken());
            	int y2 = Integer.valueOf(t.nextToken());
            	int r2 = Integer.valueOf(t.nextToken());
            	int g2 = Integer.valueOf(t.nextToken());
            	int b2 = Integer.valueOf(t.nextToken());
            	g.setColor(new Color(r1,g1,b1));
            	g.drawLine(x2, y2, x2, y2);
            }
            
            //	n+=4;
            
      
      
            i++;
        }
        in.close(); 
       // System.out.println(q[0]);
   
          
   { 
   }
    
    g.dispose();
    ImageIO.write(newImage, "PNG", new File("newImage.png")); 
    File ja = new File("newImage.png")  ;
    }
}