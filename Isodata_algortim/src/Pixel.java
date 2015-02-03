import java.util.Random;


public class Pixel
{
	public int pixel_id;
	public int L;
	public int A;
	public int B;
	Pixel (int id,int l,int a,int b)
	{
		L = l;
		A = a;
		B = b;
		pixel_id = id;
	}
	public Pixel add(Pixel p1 , Pixel p2)
	{
		Pixel res = new Pixel();
		res.A = p1.A + p2.A;
		res.L = p1.L + p2.L;
		res.B = p1.B + p2.B;
		res.pixel_id = p1.pixel_id + p2.pixel_id;
		return res;
	}
	public Pixel div_and_sub (Pixel a, int value,Pixel cur)
	
	{
		Pixel res = new Pixel();
		res.A = (a.A - cur.A )/ value;
		res.L = (a.L - cur.L )/ value;
		res.B = (a.B - cur.B )/ value;
		res.pixel_id = (a.pixel_id - cur.pixel_id) / value;
		return res;
	}
	
	public Pixel find_average(Pixel cur_average,Pixel newpoint,int count)
	{
		Pixel res = new Pixel ();
		Pixel tmp = new Pixel ();
		tmp = (cur_average.add(cur_average,div_and_sub(newpoint,count,cur_average)));
		res.pixel_id = Math.abs(tmp.pixel_id);
		res.A = Math.abs(tmp.A);
		res.L = Math.abs(tmp.L);
		res.B = Math.abs(tmp.B);
		return res;
	}
	
	
	Pixel () {}
	
	public double find_distance (Pixel s)
	{
		double distance  = 0;
		distance = Math.sqrt(Math.pow(s.L - L, 2) + Math.pow(s.A - A, 2) + Math.pow(s.B - B, 2) );
		return distance;
	}
	
	void Random_pixel(int w,int h)
	{
		Random r = new Random();
		 int nw = Math.abs(r.nextInt() % (w + 1));
		 int nh = Math.abs(r.nextInt() % (h + 1));
		 int id = nh * w + nw;
		 int l = Math.abs(r.nextInt() % 256);
		 int a = Math.abs(r.nextInt() % 256);
		 int b = Math.abs(r.nextInt() % 256);
		 L = l;
		 A = a;
		 B = b;
		 pixel_id = id;
	}
	void Random_pixel_color(int x,int y,int w,int h)
	{
		 Random r = new Random();
		 int l = Math.abs(r.nextInt() % 256);
		 int a = Math.abs(r.nextInt() % 256);
		 int b = Math.abs(r.nextInt() % 256);
		 int id = x * w + y;
		 L = l;
		 A = a;
		 B = b;
		 pixel_id = id;
	}
	
	public Point decode_id (int w,int h)
	{
		Point p = new Point();
		p.x = pixel_id / w;
		p.y = pixel_id % w;
		return p;
	}
	
	public boolean is_Less(Pixel a , double t)
	{
		if (find_distance(a) <= t)
			
		{
			return true;
			
		}
		else return  false;
	
	}
	
	public String toString ()
	{
		return pixel_id + " " + L + " " + A + " " + B;
	}
	
	public void Print ()
	{
		System.out.println("pixel_id = "+ pixel_id + " " + "L = "+ L + " "+ "A = " + A + " " + "B = "+ B);
	}
};
