

import java.awt.Color;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 *
 * @author hadoop
 */
public class Filein  {
   int pixels[];
   int iw, ih;
    Image img;
   Filein(File a) throws InterruptedException, IOException 
   { img = (Image) ImageIO.read(a);
       
   iw= img.getWidth(null);
   ih = img.getHeight(null);
   pixels = new int [iw * ih];
   PixelGrabber pg = new PixelGrabber(img, 0 ,0 ,iw, ih, pixels, 0 ,iw);
   pg.grabPixels();
   int h =1;
   int w =0;
   File file = new File("res.txt");
   file.createNewFile();
   FileWriter write = new FileWriter(file);
   String we="";
   for(int i=0; i<iw*ih; i++)
   {
   int p = pixels[i];
   int r = 0xff & (p>>16);
   int g = 0xff & (p>>8);
   int b = 0xff & (p);
   
   we=h+"  "+(w+1)+" "+r+"  "+g+"  "+ b;
   System.out.println(we);
   write.write("\n"+we);
   w++;
   if(w==iw)
   {w=0;
   h++;}
   }
   write.flush();
   write.close();
   File ins = new File("new");
   file.renameTo(ins);
   
	 
   }
   
   }
    
    
