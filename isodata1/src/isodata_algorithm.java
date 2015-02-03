import org.apache.hadoop.conf.Configured;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Mapper.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer.*;
import org.apache.hadoop.tools.rumen.Pre21JobHistoryConstants.Values;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import com.sun.org.apache.xerces.internal.util.URI;

public class isodata_algorithm extends Configured implements Tool {
	
	/**
	 * @param args
	 */
	public int k=0;
	public int v=0;
	Image img1;
	/*isodata_algorithm(File s) throws IOException
	{
		img1 = (Image) ImageIO.read(s);
		
	}*/
	public static Pixel threshold;
	public static int pixel_count = 0;
	public static int width;
	public static int height;
	public static ArrayList <Pixel> c_centers = new ArrayList <Pixel>();
	public static ArrayList < ArrayList<Pixel> > Pixels = new ArrayList < ArrayList<Pixel> > ();  
	public static long  average_pixel_count(double percent)
	{
		long points = Math.round( pixel_count / c_centers.size());
		return Math.round((double)points * percent);
	}
	public static void create_isodata_input () throws IOException
	{
		//PrintWriter out = new PrintWriter (new File ("/home/hduser/isodata_in/1"));
		System.out.println ("Hello!!!");
		int size = 1000;
		int w = width;
		int h = height;
		String pixels = "";
		String res = "";
		String mainPath = "hdfs://Math230-3:9000/user/isodata_in/input";
		Job job  = new Job();
		Configuration conf = job.getConfiguration();
		conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		Path file = new Path(mainPath);
		FileSystem hdfs = FileSystem.get(conf);
		//Path inputDir = new Path(input);
		if ( hdfs.exists( file )) { hdfs.delete( file, true ); }
		OutputStream os =  hdfs.create( file );
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
		for (Pixel pi:c_centers)
		{
			pixels += pi.pixel_id + "-" + pi.L + "-" + pi.A + "-" + pi.B + ":";
		}
		Scanner in = new Scanner (new File ("/home/hduser/workspace1/isodata1/new"));
		String f = "";
		System.out.println ("1234");
		/*String lv = "";
		while (in.hasNext())
		{
			lv = in.nextLine();
			if (lv!=null) f += lv;
			//System.out.println (lv);
		}
		in.close();*/
		 	File file1 = new File("/home/hduser/workspace1/isodata1/new");
		    FileInputStream fis = new FileInputStream(file1);
		    byte[] data = new byte[(int)file1.length()];
		    fis.read(data);
		    fis.close();
		    //
		    f = new String(data, "UTF-8");
		    System.out.println("123456");
		StringTokenizer st = new StringTokenizer(f);
		while (st.hasMoreTokens())
		{
			String x = st.nextToken();
			String y = st.nextToken();
			String l = st.nextToken();
			String a = st.nextToken();
			String b = st.nextToken();
			Pixel p = new Pixel();
			String pixel_line = x+ " " + y + " " + l + " " + a + " " + b + " "+ pixels;
			//res += pixel_line + " ";
			bw.write(pixel_line + " ");
			System.out.println(pixel_line);
			//out.println(pixel_line);
		}
		/*for (int i = 0;i <= w;i++)
		{
			for (int j = 0;j <= h;j++)
			{
				Pixel p = new Pixel();
				p.Random_pixel_color(i,j,w, h);
				String pixel_line = "";
				Point pp = p.decode_id(w, h);
				pixel_line += pp.x + " " + pp.y+ " " + p.L + " "+ p.A + " " + p.B  + " "+ pixels;
				//res += pixel_line + " ";
				bw.write(pixel_line + " ");
				//out.println(pixel_line);
			}
		}*/
		bw.close();
		//writeToHDFS("input","isodata_in",res);
		//out.close();
	}
	public static boolean is_Valid(int points_count)
	{
		return  (points_count >= average_pixel_count(0.05));
	}
	
	public static boolean is_Valid_center(Pixel x , Pixel y, double average)
	{
		if (x.is_Less(y, average))
		{
			return false;
		}
		else return true;
	}
	
	public static int writeToHDFS(String filename , String directoryname, String data) throws IOException
	{
		String mainPath = "hdfs://Math230-3:9000/user";
		Job job  = new Job();
		Configuration conf = job.getConfiguration();
		conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		
		FileSystem hdfs = FileSystem.get(conf);
		FSDataOutputStream out;
		//Path inputDir = new Path(input);
		Path hdfsFile;
		if(hdfs.exists(new Path(mainPath + "/"+ directoryname)))
		{
			if (hdfs.exists(new Path(mainPath + "/"+ directoryname + "/" + filename)))
			{
				hdfsFile = new Path(mainPath + "/"+ directoryname + "/" + filename);
				String buff = "";
				out = hdfs.create(hdfsFile);
			}
			else
			{
				//hdfs.create(new Path(mainPath + "/"+ directoryname + "/" + filename));
				hdfsFile = new Path(mainPath + "/"+ directoryname + "/" + filename);
				out = hdfs.create(hdfsFile);
			}
		}
		else
		{
			hdfs.delete(new Path(mainPath + "/"+ directoryname));
			//hdfs.create(new Path(mainPath + "/" + directoryname));
			//hdfs.create(new Path(mainPath + "/"+ directoryname + "/" + filename));
			hdfsFile = new Path(mainPath + "/"+ directoryname + "/" + filename);
			out = hdfs.create(hdfsFile);
		}
		try
		{
			
			out.writeBytes(data);
			//out.writeUTF(data);
			out.close();
			return 0;  
		}
		
		catch (IOException e) {
			e.printStackTrace();
			return 1;	
			}
		
	}
	
	public static String readFromHDFS(String input) throws IOException
	{
		Job job  = new Job();
		Configuration conf = job.getConfiguration();
		conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		FileSystem hdfs = FileSystem.get(conf);
		//Path inputDir = new Path(input);
		Path hdfsFile = new Path(input);
		String buff = "";
		
		/*try
		{
		  	
		  FileStatus[] inputFiles = hdfs.listStatus(hdfsFile);
		  
		  for (int i=0; i<inputFiles.length; i++) {
			  //System.out.println(inputFiles[i].getPath().getName());
			  FSDataInputStream in = hdfs.open(inputFiles[i].getPath());
			  byte buffer[] = new byte[256];
			  int bytesRead = 0;
			  String h = "";
			  while((h = in.readLine())!=null)
			  {
				  //out.write(buffer, 0, bytesRead);
				  buff += h + " ";
				  //System.out.println(h);
			  }
			  
			  in.close();
			  
			  }
		}
		catch (IOException e) {
			e.printStackTrace();
			}
			*/
		BufferedReader br=new BufferedReader(new InputStreamReader(hdfs.open(new Path (input))));
        String line;
        line=br.readLine();
        buff += line;
        /*while (line != null)
        {
        	line = br.readLine();
        	if (line!=null)
        	buff += line;
        }*/
		return buff;
	}
	public static class MapClass extends Mapper<LongWritable, Text, IntWritable, Text> {
		public void map(LongWritable key, Text value, 
                Context context) throws IOException, InterruptedException
        {
				System.out.println ("enter map");
								/*
				Scanner in  = new Scanner(new File("/home/hduser/cluster_centers"));
				String data = "";
				while (in.hasNext())
				{
					data += in.nextLine();
				}*/
			   //writeToHDFS("map","map",value.toString());
			   //System.out.println(value);
			   //System.out.println(context.getInputSplit());
			   //System.out.println(key);	
			   //System.out.println ("mapper");
			   /*Configuration conf1 = new Configuration(); //New API
			   conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
			   conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
			   FileSystem hdfs1 = FileSystem.get(conf1);
			   Path hdfsFile1 = new Path("hdfs://Math230-3:9000/user/isodata_map_in");
			   BufferedReader br=new BufferedReader(new InputStreamReader(hdfs1.open(hdfsFile1)));
			   
		       String line1 = "";
		       String buff = "";
		       //char buff [] = new char [256];
		       int kk=0;
		       while (line1 != null && kk<10)
		       {
		    	   System.out.println(line1);
		    	   ++kk;
		    	   
		    	   line1 = br.readLine();
		    	   if (line1!=null)
		    	   buff += line1;
		    	   
		       }
		       br.close();
		       
		       System.out.println (buff);*/
		      /* StringTokenizer st = new StringTokenizer(data);
		       while (st.hasMoreTokens())
		       {
					Pixel tmp = new Pixel();
					tmp.pixel_id = Integer.valueOf(st.nextToken());
					tmp.L = Integer.valueOf(st.nextToken());
					tmp.A = Integer.valueOf(st.nextToken());
					tmp.B = Integer.valueOf(st.nextToken());
					c_centers.add(tmp);
		       }*/
			//System.out.println ("MAPPP"+ " "+c_centers.size());
			String line = value.toString();
		    StringTokenizer itr = new StringTokenizer(line);
		    while (itr.hasMoreTokens())
		    {
		    	//CHANGED
		    	/*
			    int p_id = Integer.parseInt(itr.nextToken());
			    */
		    	//TO
		    	int x = Integer.parseInt(itr.nextToken());
		    	int y = Integer.parseInt(itr.nextToken());
		    	int p_id = y * width + x;
			    double mindis = Double.MAX_VALUE; 
			    int l =  Integer.parseInt(itr.nextToken());
			    int a =  Integer.parseInt(itr.nextToken());
			    int b =  Integer.parseInt(itr.nextToken());
			    
			    Pixel pixel = new Pixel(p_id,l,a,b);
			    String cc = itr.nextToken();
			    StringTokenizer tt = new StringTokenizer(cc,":");
			    ArrayList <Pixel> c_centers1 = new ArrayList <Pixel>();
			    while (tt.hasMoreTokens())
			    {
			    	String token = tt.nextToken();
			    	StringTokenizer tt1 = new StringTokenizer(token,"-");
			    	while (tt1.hasMoreTokens())
			    	{
			    		Pixel pp = new Pixel();
			    		pp.pixel_id = Integer.valueOf(tt1.nextToken());
			    		pp.L = Integer.valueOf(tt1.nextToken());
			    		pp.A = Integer.valueOf(tt1.nextToken());
			    		pp.B = Integer.valueOf(tt1.nextToken());
			    		c_centers1.add(pp);
			    	}
			    }
			    
			    //pixel.Print();
			    //System.out.println();
			    Pixel tmp = new Pixel();
			    for (Pixel p:c_centers1)
			    {
			    	if (pixel.find_distance(p) < mindis)
			    		{
			    			mindis = pixel.find_distance(p);
			    			tmp = p;
			    		}
			    }
			    context.write(new IntWritable(tmp.pixel_id),new Text(pixel.toString()));
		    }
		    System.out.println("exit_map");
		}
	}
	public static class MapClass1 extends Mapper<LongWritable, Text, IntWritable, Text> {
		public void map(LongWritable key, Text value, 
                Context context) throws IOException, InterruptedException
        {
				System.out.println ("enter map");
								/*
				Scanner in  = new Scanner(new File("/home/hduser/cluster_centers"));
				String data = "";
				while (in.hasNext())
				{
					data += in.nextLine();
				}*/
			   //writeToHDFS("map","map",value.toString());
			   //System.out.println(value);
			   //System.out.println(context.getInputSplit());
			   //System.out.println(key);	
			   //System.out.println ("mapper");
			   /*Configuration conf1 = new Configuration(); //New API
			   conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
			   conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
			   FileSystem hdfs1 = FileSystem.get(conf1);
			   Path hdfsFile1 = new Path("hdfs://Math230-3:9000/user/isodata_map_in");
			   BufferedReader br=new BufferedReader(new InputStreamReader(hdfs1.open(hdfsFile1)));
			   
		       String line1 = "";
		       String buff = "";
		       //char buff [] = new char [256];
		       int kk=0;
		       while (line1 != null && kk<10)
		       {
		    	   System.out.println(line1);
		    	   ++kk;
		    	   
		    	   line1 = br.readLine();
		    	   if (line1!=null)
		    	   buff += line1;
		    	   
		       }
		       br.close();
		       
		       System.out.println (buff);*/
		      /* StringTokenizer st = new StringTokenizer(data);
		       while (st.hasMoreTokens())
		       {
					Pixel tmp = new Pixel();
					tmp.pixel_id = Integer.valueOf(st.nextToken());
					tmp.L = Integer.valueOf(st.nextToken());
					tmp.A = Integer.valueOf(st.nextToken());
					tmp.B = Integer.valueOf(st.nextToken());
					c_centers.add(tmp);
		       }*/
			//System.out.println ("MAPPP"+ " "+c_centers.size());
			String line = value.toString();
		    StringTokenizer itr = new StringTokenizer(line);
		    while (itr.hasMoreTokens())
		    {
		    	//CHANGED
		    	/*
			    int p_id = Integer.parseInt(itr.nextToken());
			    */
		    	//TO
		    	int x = Integer.parseInt(itr.nextToken());
		    	int y = Integer.parseInt(itr.nextToken());
		    	int p_id = y * width + x;
			    double mindis = Double.MAX_VALUE; 
			    int l =  Integer.parseInt(itr.nextToken());
			    int a =  Integer.parseInt(itr.nextToken());
			    int b =  Integer.parseInt(itr.nextToken());
			    Pixel pixel = new Pixel(p_id,l,a,b);
			    String cc = itr.nextToken();
			    StringTokenizer tt = new StringTokenizer(cc,":");
			    ArrayList <Pixel> c_centers1 = new ArrayList <Pixel>();
			    while (tt.hasMoreTokens())
			    {
			    	String token = tt.nextToken();
			    	StringTokenizer tt1 = new StringTokenizer(token,"-");
			    	while (tt1.hasMoreTokens())
			    	{
			    		Pixel pp = new Pixel();
			    		pp.pixel_id = Integer.valueOf(tt1.nextToken());
			    		pp.L = Integer.valueOf(tt1.nextToken());
			    		pp.A = Integer.valueOf(tt1.nextToken());
			    		pp.B = Integer.valueOf(tt1.nextToken());
			    		c_centers1.add(pp);
			    	}
			    }
			   
			    //pixel.Print();
			    //System.out.println();
			    Pixel tmp = new Pixel();
			    for (Pixel p:c_centers1)
			    {
			    	if (pixel.find_distance(p) < mindis)
			    		{
			    			mindis = pixel.find_distance(p);
			    			tmp = p;
			    		}
			    }
			    context.write(new IntWritable(tmp.pixel_id),new Text(pixel.toString() + " "+ cc + " "));
		    }
		    System.out.println("exit_map");
		}
	}
	public static class Combiner extends Reducer<IntWritable, Text, /*IntWritable*/ 
	IntWritable, Text> 
	 {
		public void reduce(IntWritable key, Iterable<Text> values,
		        Context context) throws IOException, InterruptedException
			          {
			    //System.out.println(key);
			 	String res = "";
			    Pixel average = new Pixel(-1,0,0,0);
			    int c_ind = 0;
			    /*for (int i = 0;i<c_centers.size();i++)
				  {
					  if (c_centers.get(i).pixel_id == Integer.valueOf(key.toString()))
					  {
						  System.out.println(c_centers.get(i).pixel_id);
						  c_ind = i;break;
					  }
				  }*/
			    int count = 0;
			  //(OLD API)while (values.hasNext())
			 //NEW 
			   String pixels = "";
			  for (Text val: values)
				{ 
				  Pixel pixel =  new Pixel();
				  String line = val.toString();
				  StringTokenizer t = new StringTokenizer(line);
				  pixel.pixel_id = Integer.parseInt(t.nextToken());
				  pixel.L = Integer.parseInt(t.nextToken());
				  pixel.A = Integer.parseInt(t.nextToken());
				  pixel.B = Integer.parseInt(t.nextToken());
				  //System.out.println(pixel.pixel_id + " " +pixel.L + " " + pixel.A + " " + pixel.B + " ");
				  //NEW
				  //pixels += pixel.pixel_id + " " +pixel.L + " " + pixel.A + " " + pixel.B + " ";
				  //Pixels.get(c_ind).add(pixel);
				  average = average.find_average(average, pixel, count + 1);
				  count++;
				}
			  	context.write(key , new Text(average.toString()));
			    //context.write(new IntWritable(average.pixel_id), new Text(average.toStr()));
			}
	 }
	public static class ReduceLast extends Reducer<IntWritable, Text, /*IntWritable*/ 
	Text, Text> 
	 {
		public void reduce(IntWritable key, Iterable<Text> values,
		        Context context) throws IOException, InterruptedException
			          {
			 			 //System.out.println ("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAb" + c_centers.size());
						 //System.out.println("Hello world!!!");
						  String pixels = "";	
						  for (Text val: values)
						  {
							  Pixel pixel =  new Pixel();
							  String line = val.toString();
							  StringTokenizer t = new StringTokenizer(line);
							  pixel.pixel_id = Integer.parseInt(t.nextToken());
							  pixel.L = Integer.parseInt(t.nextToken());
							  pixel.A = Integer.parseInt(t.nextToken());
							  pixel.B = Integer.parseInt(t.nextToken());
							  Point point = pixel.decode_id(width, height);
							  String cc = t.nextToken();
							    StringTokenizer tt = new StringTokenizer(cc,":");
							    ArrayList <Pixel> c_centers1 = new ArrayList <Pixel>();
							    while (tt.hasMoreTokens())
							    {
							    	String token = tt.nextToken();
							    	StringTokenizer tt1 = new StringTokenizer(token,"-");
							    	while (tt1.hasMoreTokens())
							    	{
							    		Pixel pp = new Pixel();
							    		pp.pixel_id = Integer.valueOf(tt1.nextToken());
							    		pp.L = Integer.valueOf(tt1.nextToken());
							    		pp.A = Integer.valueOf(tt1.nextToken());
							    		pp.B = Integer.valueOf(tt1.nextToken());
							    		c_centers1.add(pp);
							    	}
							    }
							  //System.out.println(point.x + " "+point.y);
							  Pixel center = null;
							  Point cen;
							  //System.out.println ("ERROR"+" "+c_centers.size());
							  for (int i = 0;i < c_centers1.size();i++)
							  {
								  //System.out.println ("ERROR:    " + Integer.valueOf(key.toString()) + " "+c_centers1.get(i).pixel_id);
								  if (Integer.valueOf(key.toString()) == c_centers1.get(i).pixel_id)
								  {
									  center =c_centers1.get(i); 
								  }
							  }
							  //System.err.println(center.toString());
							  cen = center.decode_id(width, height);
							  context.write(new Text(cen.x + " "+cen.y + " "+ center.toStr()), new Text(point.x + " "+point.y +  " " + pixel.toStr()));
						  }
			          }
	 }
	public static class Reduce extends Reducer<IntWritable, Text, /*IntWritable*/ 
Text, Text> 
 {
	public void reduce(IntWritable key, Iterable<Text> values,
	        Context context) throws IOException, InterruptedException
		          {	
					   String res = "";
					   ArrayList <Pixel> p = new ArrayList 	<Pixel> ();
					   Pixel average = new Pixel(-1,0,0,0);
					   int count = 0;
					  //(OLD API)while (values.hasNext())
					 //NEW 
					   String pixels = "";
					  for (Text val: values)
						{ 
						  Pixel pixel =  new Pixel();
						  String line = val.toString();
						  StringTokenizer t = new StringTokenizer(line);
						  pixel.pixel_id = Integer.parseInt(t.nextToken());
						  pixel.L = Integer.parseInt(t.nextToken());
						  pixel.A = Integer.parseInt(t.nextToken());
						  pixel.B = Integer.parseInt(t.nextToken());
						  
						  //System.out.println(pixel.pixel_id + " " +pixel.L + " " + pixel.A + " " + pixel.B + " ");
						  //NEW
						  //changed
						  	pixels += pixel.pixel_id + " " +pixel.L + " " + pixel.A + " " + pixel.B + " ";
						  //end changed
						  average = average.find_average(average, pixel, count + 1);
						  count++;
						}
					  	//changed
					  	/*int c_ind = 0;
					    for (int i = 0;i<c_centers.size();i++)
						  {
							  if (c_centers.get(i).pixel_id == Integer.valueOf(key.toString()))
							  {
								  c_ind = i;break;
							  }
						  }*/
					    //end changed
					    //changed
					  	/*
					  	for (Pixel pix : Pixels.get(c_ind))
					  	{
					  		pixels += pix.toString();
					  	}*/
					  	//end changed
					  	
					  	//output.collect(key,new Text(res));
					  	//res += average.pixel_id + " " + average.L + " " + average.A + " "+ average.B;
					  	res += count + " " + average.pixel_id + " " + average.L + " " + average.A + " "+ average.B + ":";
					  //CHANGED // context.write(new IntWritable(count) , new Text(res));
					  	//TO // 
					  	//System.out.println ("end of reduce"  + "   "+pixels);
					  	context.write(new Text(res) , new Text(pixels + ":"));
		          }
 }
 public void init_cluster_centers (int w,int h,int centers_count)
 {
	 for (int i = 0;i < centers_count; i++)
	 {
		 Pixel p = new Pixel();
		 p.Random_pixel(w, h);
		 c_centers.add(p);
		 Pixels.add(new ArrayList <Pixel>());
	 }
 }
	public int run(String[] args) throws Exception 
	{
			DistributedCache d;
			
		   init_cluster_centers(width,height,10);
		   create_isodata_input();
		   int iter = 0;
		   /*Configuration conf1 = new Configuration(); //New API
		   //JobConf job1 = new JobConf();
		   //job1.setNumMapTasks(20);
		   conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		   conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		   FileSystem hdfs1 = FileSystem.get(conf1);
		   
		   if (hdfs1.exists(new Path("hdfs://Math230-3:9000/user/isodata_map_in")))
		   {
			   hdfs1.delete(new Path("hdfs://Math230-3:9000/user/isodata_map_in"),true);
		   }
		   Path hdfsFile1 = new Path("hdfs://Math230-3:9000/user/isodata_map_in");
		   FSDataOutputStream out;
		   out = hdfs1.create(hdfsFile1);
		   String data  = "";
		   for (Pixel pi : c_centers)
		   {
			   data += pi.toString() + " ";
		   }
		   out.writeBytes(data);
		   out.close();*/
		   
		   
		   // begin Вариант с созданием локальных файлов
		   /*String data = "";
		   PrintWriter pw = new PrintWriter(new File("/home/hduser/cluster_centers"));
		   for (Pixel pi : c_centers)
		   {
			   data += pi.toString() + " ";
		   }
		   pw.write(data);
		   pw.close();
		   Process pro1 = Runtime.getRuntime().exec("/home/hduser/copytolocal.sh");
			 try 
			 {
				pro1.waitFor();
			 } 
			 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//end  Вариант с созданием локальных файлов
			 
		   while(iter != 5)
		   {
			   System.out.println("HELLO_RUN"+" "+c_centers.size());
			   for (Pixel pi:c_centers)
			   {
				   System.out.println(pi.toString());
			   }
			   Configuration conf = new Configuration(); //New API
			   conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
			   conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
			   //System.out.println (conf.getInt("mapred.map.tasks", 2));
			   Job job = new Job(conf);
			   //int v = conf.getInt("mapred.tasktracker.map.tasks.maximum",2);
			   //System.out.println("VVVVVVVVVVV"+v);
			   FileSystem hdfs = FileSystem.get(conf);
			   FileSystem local = FileSystem.getLocal(conf);
			   job.setJarByClass(isodata_algorithm.class);
			   job.setJobName("ISODATA algorithm"); 
			   job.setMapperClass(isodata_algorithm.MapClass.class);
			   job.setReducerClass(isodata_algorithm.Reduce.class);
			   //job.setCombinerClass(Combiner.class);
			   
			   if (hdfs.exists(new Path("hdfs://Math230-3:9000/user/isodata_out")))
			   {
				   hdfs.delete(new Path("hdfs://Math230-3:9000/user/isodata_out"),true);
			   }
			   FileInputFormat.setInputPaths(job , new Path("hdfs://Math230-3:9000/user/isodata_in"));
			   FileOutputFormat.setOutputPath(job , new Path("hdfs://Math230-3:9000/user/isodata_out"));
			   // CHANGED// job.setOutputKeyClass(IntWritable.class);
			   //TO//
			   job.setOutputKeyClass(Text.class);
			   job.setOutputValueClass(Text.class);
			     // job.setNumReduceTasks(28);			   
			   job.setMapOutputKeyClass(IntWritable.class);
			   job.setMapOutputValueClass(Text.class);
			   job.waitForCompletion(true);
			   
			  
			   //Pixels.clear();
			   
			   //String r = readFromHDFS("hdfs://Math121-3:9000/user/hduser/isodata_out");
			   
			    //FileSystem hdfs = FileSystem.get(conf);
				//Path inputDir = new Path(input);
			   
				Path hdfsFile = new Path("hdfs://Math230-3:9000/user/isodata_out");
				String buff = "";
				
				try
				{
				  FileStatus[] inputFiles = hdfs.listStatus(hdfsFile);
				  int i = 0;
				  String m = "";
				  for (int k=0; k<inputFiles.length; k++) {
					 // System.out.println(inputFiles[i].getPath().getName());
					  FSDataInputStream in = hdfs.open(inputFiles[k].getPath());
					  byte buffer[] = new byte[256];
					  int bytesRead = 0;
					  String h = "";
					  while((h = in.readLine()) != null)
					  {
						  m += h;
					  }
					  in.close();
				  }
						  //out.write(buffer, 0, bytesRead);
						  //buff += h + " ";
						  //System.out.println(h);
						  StringTokenizer t = new StringTokenizer(m,":");
						  while (t.hasMoreTokens())
						  {
							  String key = t.nextToken();
							  String val = t.nextToken();
							  StringTokenizer t_key = new StringTokenizer(key);
							   //Pixel []array = new Pixel[c_centers.size()];
							   //c_centers.toArray(array);
							   
							   while (t_key.hasMoreTokens())
							   {
								 int count = Integer.parseInt(t_key.nextToken());
								 int id = Integer.parseInt(t_key.nextToken());
								 int L = Integer.parseInt(t_key.nextToken());
								 int A = Integer.parseInt(t_key.nextToken());
								 int B = Integer.parseInt(t_key.nextToken());
								 Pixel new_center = new Pixel(id,L,A,B);
								 System.out.println("new pixel center" + " "+new_center.toString());
								 c_centers.set(i, new_center);
								 i++;
								 /*if (is_Valid(count))
								 {
									 c_centers.set(i, new_center);
									 //Pixels.add(new ArrayList <Pixel>());
									 //c_centers.get(i).Print();
									 i++;
								 }*/
								 //array[i]  = new_center;
							  	 //array[i].Print();
							  	 
							   }
						  }
					  
					  
				  
				  
				}
				catch (IOException e) {
					e.printStackTrace();
					}
				 	System.out.println("HELLO_RUN_RUN"+" "+c_centers.size());
				   for (Pixel pi:c_centers)
				   {
					   System.out.println(pi.toString());
				   }
						   boolean find_merge = false;
						   ArrayList < Pair<Integer,Integer> > pairs = new ArrayList < Pair<Integer,Integer> >();
						  // System.out.println(c_centers.size());
						   boolean removed [] = new boolean [c_centers.size()];
						   for (boolean a : removed)
						   {
							   a = false;
						   }
						   for (int it = 0; it< c_centers.size();it++)
						   {
							  // System.out.println(c_centers.size() + "it");
							   for(int j = it + 1;j < c_centers.size();j++)
							   {
									   Pixel a = c_centers.get(it);
									   Pixel b = c_centers.get(j);
									   int ind = (a.pixel_id + b.pixel_id)/2;
									   int L = (a.L + b.L)/2;
									   int A = (a.A + b.A)/2;
									   int B = (a.B + b.B)/2;
									   if (!is_Valid_center(a,b,50) && !removed[j] && !removed[it])
									   {
										   Pixel np = new Pixel(ind,L,A,B);
										   System.out.println(it);
										   removed[it] = true;
										   removed[j] = true;
										   pairs.add(new Pair (it,j));
										   //System.out.println (it + " "+j);
										   //c_centers.remove(it);
										   //c_centers.remove(j);
										   //c_centers.add(np);
										   //find_merge = true;
										   break;
									   }
								   
								   //if (find_merge) break;
							   }
						   }
						   		ArrayList <Pixel> c_tmp = new ArrayList<Pixel>();
							   for (Pair p : pairs)
							   {
								   //System.out.println(p.first + " " +p.second);
								   Pixel a = c_centers.get((int)p.first);
								   Pixel b = c_centers.get((int)p.second);
								   int ind = (a.pixel_id + b.pixel_id)/2;
								   int L = (a.L + b.L)/2;
								   int A = (a.A + b.A)/2;
								   int B = (a.B + b.B)/2;
								   Pixel np = new Pixel(ind,L,A,B);
								   //c_centers.remove(p.first);
								   //c_centers.remove(p.second);
								   c_tmp.add(np);
							   }
							   for (int i = 0;i<c_centers.size();i++)
							   {
								   if (!removed[i]) c_tmp.add(c_centers.get(i));
							   }
							   c_centers = c_tmp;
							   iter++;
							   String pLine = "";
							   for (Pixel pp:c_centers)
							   {
								   	pLine +=pp.pixel_id + "-" + pp.L + "-" + pp.A + "-" + pp.B + ":";
							   }
							   String inputFile = readFromHDFS("hdfs://Math230-3:9000/user/isodata_in/input");
							   
							   StringTokenizer tt = new StringTokenizer(inputFile);
							   String wLine = "";
							   String mainPath = "hdfs://Math230-3:9000/user/isodata_in/input";
							   Path file = new Path(mainPath);
							   if ( hdfs.exists( file )) { hdfs.delete( file, true ); }
								OutputStream os =  hdfs.create( file );
								BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
								//System.out.println (inputFile.substring(0,100));
							   while (tt.hasMoreTokens())
							   {
							    	int x = Integer.parseInt(tt.nextToken());
							    	int y = Integer.parseInt(tt.nextToken());
							    	int p_id = y * width + x;
								    double mindis = Double.MAX_VALUE; 
								    int l =  Integer.parseInt(tt.nextToken());
								    int a =  Integer.parseInt(tt.nextToken());
								    int b =  Integer.parseInt(tt.nextToken());
								    
								    Pixel pixel = new Pixel(p_id,l,a,b);
								    String cc = tt.nextToken();
								    Point pp = new Point();
								    pp = pixel.decode_id(width, height);
								    wLine = pp.x + " " +pp.y + " "+pixel.toStr() + " "+ pLine + " ";
								    bw.write(wLine);
								    /*StringTokenizer tt1 = new StringTokenizer(cc,":");
								    while (tt1.hasMoreTokens())
								    {
								    	String token = tt1.nextToken();
								    	StringTokenizer tt2 = new StringTokenizer(token);
								    	while (tt2.hasMoreTokens())
								    	{
								    		Pixel pp = new Pixel();
								    		pp.pixel_id = Integer.valueOf(tt1.nextToken());
								    		pp.L = Integer.valueOf(tt1.nextToken());
								    		pp.A = Integer.valueOf(tt1.nextToken());
								    		pp.B = Integer.valueOf(tt1.nextToken());
								    		
								    	}
								    }
							   /*String data1 = "";
							   PrintWriter pw1 = new PrintWriter(new File("/home/hduser/cluster_centers"));
							   for (Pixel pi : c_centers)
							   {
								   data += pi.toString() + " ";
							   }
							   pw.write(data);
							   pw.close();
							   Process pro2 = Runtime.getRuntime().exec("/home/hduser/copytolocal.sh");
								 try 
								 {
									pro2.waitFor();
								 } 
								 catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								*/
						   }
						bw.close();	    
		   }
		   
						   //System.out.println(r);  
		   Configuration conf = new Configuration(); //New API
		   conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		   conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		   Job job = new Job(conf);
		   FileSystem hdfs = FileSystem.get(conf);
		   FileSystem local = FileSystem.getLocal(conf);
		   job.setJarByClass(isodata_algorithm.class);
		   job.setJobName("ISODATA algorithm"); 
		   job.setMapperClass(isodata_algorithm.MapClass1.class);
		   job.setReducerClass(isodata_algorithm.ReduceLast.class);
		   if (hdfs.exists(new Path("hdfs://Math230-3:9000/user/isodata_out")))
		   {
			   hdfs.delete(new Path("hdfs://Math230-3:9000/user/isodata_out"),true);
		   }
		   FileInputFormat.setInputPaths(job , new Path("hdfs://Math230-3:9000/user/isodata_in"));
		   FileOutputFormat.setOutputPath(job , new Path("hdfs://Math230-3:9000/user/isodata_out"));
		   // CHANGED// job.setOutputKeyClass(IntWritable.class);
		   //TO//
		   job.setOutputKeyClass(Text.class);
		   job.setOutputValueClass(Text.class);
		   //job.setNumReduceTasks(28);
		   job.setMapOutputKeyClass(IntWritable.class);
		   job.setMapOutputValueClass(Text.class);
		   System.out.println ("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + c_centers.size());
		   job.waitForCompletion(true);   			
		   if (hdfs.exists(new Path("hdfs://Math230-3:9000/user/isodata_in/input")))
		   {
			   hdfs.delete(new Path("hdfs://Math230-3:9000/user/isodata_in/input"));
		   }
		   hdfs.copyToLocalFile(new Path("hdfs://Math230-3:9000/user/isodata_out/part-r-00000"), new Path("12"));
	
		return 0;
		
		    }

}
