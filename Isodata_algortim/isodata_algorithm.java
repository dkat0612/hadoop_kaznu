import org.apache.hadoop.conf.Configured;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
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
	isodata_algorithm(File s) throws IOException
	{
		img1 = (Image) ImageIO.read(s);
		
	}
	public static Pixel threshold;
	public static int pixel_count = 0;
	public static int width = 1159;
	public static int height = 597;
	
	public static ArrayList <Pixel> c_centers = new ArrayList <Pixel>();
	
	public static long  average_pixel_count(double percent)
	{
		long points = Math.round( pixel_count / c_centers.size());
		return Math.round((double)points * percent);
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
		String mainPath = "hdfs://Math121-3:9000/user/hduser";
		Job job  = new Job();
		Configuration conf = job.getConfiguration();
		conf.addResource(new Path("/usr/local/hadoop/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/usr/loca/hadoop/hadoop/conf/hdfs-site.xml"));
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
			//out.close();
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
		conf.addResource(new Path("/usr/local/hadoop/hadoop/conf/core-site.xml"));
		conf.addResource(new Path("/usr/loca/hadoop/hadoop/conf/hdfs-site.xml"));
		FileSystem hdfs = FileSystem.get(conf);
		//Path inputDir = new Path(input);
		Path hdfsFile = new Path(input);
		String buff = "";
		
		try
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
		return buff;
	}
	public static class MapClass extends Mapper<LongWritable, Text, IntWritable, Text> {
		public void map(LongWritable key, Text value, 
                Context context) throws IOException, InterruptedException
        {
			
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
			   //pixel.Print();
			   // System.out.println();
			    Pixel tmp = new Pixel();
			    for (Pixel p:c_centers)
			    {
			    	if (pixel.find_distance(p) < mindis)
			    		{
			    			mindis = pixel.find_distance(p);
			    			tmp = p;
			    		}
			    	 
			    }
			    context.write(new IntWritable(tmp.pixel_id),new Text(pixel.toString()));
			   
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
						  pixels += pixel.pixel_id + " " +pixel.L + " " + pixel.A + " " + pixel.B + " ";  
						  average = average.find_average(average, pixel, count + 1);
						  count++;
						}
					  	//output.collect(key,new Text(res));
					  	//res += average.pixel_id + " " + average.L + " " + average.A + " "+ average.B;
					  	res += count + " " + average.pixel_id + " " + average.L + " " + average.A + " "+ average.B + ":";
					  //CHANGED // context.write(new IntWritable(count) , new Text(res));
					  	//TO // 
					  	context.write(new Text(res) , new Text(pixels));
		          }
 }
 public void init_cluster_centers (int w,int h,int centers_count)
 {
	 for (int i = 0;i < centers_count; i++)
	 {
		 Pixel p = new Pixel();
		 p.Random_pixel(w, h);
		 c_centers.add(p);
	 }
 }
	public int run(String[] args) throws Exception {
		   	
		   init_cluster_centers(width,height,7);
		   int iter = 0;
		   while(iter!=1)
		   {
			   Configuration conf = new Configuration(); //New API
			   conf.addResource(new Path("/usr/local/hadoop/hadoop/conf/core-site.xml"));
			   conf.addResource(new Path("/usr/loca/hadoop/hadoop/conf/hdfs-site.xml"));
			   Job job = new Job(conf);
			   FileSystem hdfs = FileSystem.get(conf);
			   FileSystem local = FileSystem.getLocal(conf);
			   
			   job.setJarByClass(isodata_algorithm.class);
			   job.setJobName("ISODATA algorithm"); 
			   job.setMapperClass(isodata_algorithm.MapClass.class);
			   job.setReducerClass(isodata_algorithm.Reduce.class);
			   if (hdfs.exists(new Path("hdfs://Math121-3:9000/user/hduser/isodata_out")))
			   {
				   hdfs.delete(new Path("hdfs://Math121-3:9000/user/hduser/isodata_out"),true);
			   }
			   FileInputFormat.setInputPaths(job , new Path("hdfs://Math121-3:9000/user/hduser/isodata_in"));
			   FileOutputFormat.setOutputPath(job , new Path("hdfs://Math121-3:9000/user/hduser/isodata_out"));
			   // CHANGED// job.setOutputKeyClass(IntWritable.class);
			   //TO//
			   job.setOutputKeyClass(Text.class);
			   job.setOutputValueClass(Text.class);
			   
			   job.setMapOutputKeyClass(IntWritable.class);
			   job.setMapOutputValueClass(Text.class);
			   job.waitForCompletion(true);
			   //String r = readFromHDFS("hdfs://Math121-3:9000/user/hduser/isodata_out");
			   
			   conf.addResource(new Path("/usr/local/hadoop/hadoop/conf/core-site.xml"));
				conf.addResource(new Path("/usr/loca/hadoop/hadoop/conf/hdfs-site.xml"));
				//FileSystem hdfs = FileSystem.get(conf);
				//Path inputDir = new Path(input);
				Path hdfsFile = new Path("hdfs://Math121-3:9000/user/hduser/isodata_out");
				String buff = "";
				
				try
				{
				  FileStatus[] inputFiles = hdfs.listStatus(hdfsFile);
				  int i = 0;
				  for (int k=0; k<inputFiles.length; k++) {
					  //System.out.println(inputFiles[i].getPath().getName());
					  FSDataInputStream in = hdfs.open(inputFiles[k].getPath());
					  byte buffer[] = new byte[256];
					  int bytesRead = 0;
					  String h = "";
					  
					  while((h = in.readLine())!=null)
					  {
						  //out.write(buffer, 0, bytesRead);
						  //buff += h + " ";
						  //System.out.println(h);
						  StringTokenizer t = new StringTokenizer(h,":");
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
							 
							 if (is_Valid(count))
							 {
								 c_centers.set(i, new_center);
								 i++;
							 }
						  	 //c_centers.get(i).Print();
							 //array[i]  = new_center;
						  	 //array[i].Print();
						  	 
						   }
					  }
					  in.close();
				  }
				  
				}
				catch (IOException e) {
					e.printStackTrace();
					}
						   boolean find_merge = false;
						   for (int it = 0; it< c_centers.size();it++)
						   {
							   for(int j = 0;j < c_centers.size();j++)
							   {
								   if (it!=j)
								   {
									   Pixel a = c_centers.get(it);
									   Pixel b = c_centers.get(j);
									   int ind = (a.pixel_id + b.pixel_id)/2;
									   int L = (a.L + b.L)/2;
									   int A = (a.A + b.A)/2;
									   int B = (a.B + b.B)/2;
									   if (!is_Valid_center(a,b,50))
									   {
										   Pixel np = new Pixel(ind,L,A,B);
										   c_centers.remove(it);
										   c_centers.remove(j);
										   c_centers.add(np);
										   find_merge = true;
										   break;
									   }
								   }
								   if (find_merge) break;
							   }
							   
						   }
							  
							  
							  
							
							
						   //System.out.println(r);
						   
						   iter++;
					  }
					  
				
		   
	
			  
		return 0;
		
		    }
		          
	

}
