import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.util.*;



import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.*;
import org.omg.CORBA.portable.InputStream;



public class Test extends Configured implements Tool {
	
	public static class MapClass extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {

		public void map(Text key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			output.collect(key, value);
			System.out.println("----");

		}
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			System.out.println("****");
			Job job  = new Job();
			Configuration conf = job.getConfiguration();
			conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
			conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
			FileSystem hdfs = FileSystem.get(conf);
			Path file = new Path("hdfs://Math230-3:9000/user/Test/OUT_"
					+ key.toString());
			/*if ( hdfs.exists( file )) { hdfs.delete( file, true ); }
			OutputStream os =  hdfs.create( file );
			BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );*/
			
			ServerSocket ss = new ServerSocket(8383);
			System.out.println("1111");
            Socket s=ss.accept();
            System.out.println("8785");
            
            ObjectOutputStream os = (ObjectOutputStream) s.getOutputStream();
            System.out.println("555");
            URI uri = hdfs.getUri();
            System.out.println("444");
            os.writeObject(uri);
            System.out.println("333");
            os.flush();
            os.close();
			
		//exec here
			 Process pro1 = Runtime.getRuntime().exec("/home/hduser/run00.sh");
			 try {
				pro1.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(pro1.exitValue());

			 Process pro2 = Runtime.getRuntime().exec("/home/hduser/run111.sh");
			 try {
				pro2.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(pro2.exitValue());	
			
		
		
        
         }
	}

	public int run(String[] args) throws Exception {

		//MPI.Init(args);
		
		Job job1 = new Job();
		Configuration conf1 = job1.getConfiguration();
		conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		conf1.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		conf1 = getConf();
		FileSystem hdfs = FileSystem.get(conf1);

		JobConf job = new JobConf(conf1, Test.class);
		Path in, out;
		
		job.setNumMapTasks(4);
		job.setNumReduceTasks(4);

		job.setJobName("Test");
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormat(KeyValueTextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
//		Integer OUT = Main.i + 1;
		System.out.println("999");
		in = new Path("hdfs://Math230-3:9000/user/Puasson/in/input_puasson");
		
		out = new Path("hdfs://Math230-3:9000/user/Test/out_");

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);

		JobClient.runJob(job);

		return 0;

	}

}

