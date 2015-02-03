import java.util.*;
import java.io.*;
public class create_isodata_input {

	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		PrintWriter out = new PrintWriter (new File ("/home/hduser/isodata_in/1"));
		int size = 1000;
		int w = 1000;
		int h = 1000;
		for (int i = 0;i <= w;i++)
		{
			for (int j = 0;j <= h;j++)
			{
				Pixel p = new Pixel();
				p.Random_pixel_color(i,j,w, h);
				String pixel_line = "";
				Point pp = p.decode_id(w, h);
				pixel_line += pp.x + " " + pp.y+ " " + p.L + " "+ p.A + " " + p.B;
				out.println(pixel_line);
			}
		}
		out.close();
	}

}
