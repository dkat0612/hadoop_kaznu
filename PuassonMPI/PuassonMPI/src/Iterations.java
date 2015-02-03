import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

import mpi.MPI;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.*;
import org.omg.CORBA.portable.InputStream;

import com.sun.org.apache.xerces.internal.util.URI;
import java.util.Collections;
import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;


public class Iterations extends Configured implements Tool {

	static Integer P = 5;
	static Integer N =120;
	static Integer M =120;
	static Integer K = 120;
	static Integer M_P = 4;
	
	public static class MapClass extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {

		public void map(Text key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			output.collect(key, value);

		}
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {

		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {

			Integer i = null,numK = Integer.valueOf(key.toString()), m_p0=0, m_p1=0,ii;

			PrintWriter pw0 = new PrintWriter(new File("/home/hduser/out_"+0)), pw1 = new PrintWriter(new File("/home/hduser/out_"+0));
			String val;
			int num = 0;
			while (values.hasNext()) {
				num++;	
				val = values.next().toString();
				StringTokenizer itr = new StringTokenizer(val);
				i = Integer.valueOf(itr.nextToken());
				System.out.println("i ="+i);
				/*if(i % ((N/P)/M_P) == 0 && i != 0 && (i != N/P))
				{
					m_p0 = i/((N/P)/M_P);
					pw0.close();
					pw0 = new PrintWriter(new File("/home/hduser/out_" + m_p0));
					pw0.println(val);
					pw1 = new PrintWriter(new File("/home/hduser/out_" + m_p0 + 1));
					pw1.println(val);
			    } 
				/*else if( i % ((N/P)/M_P) != 0 && (m_p1 + 1) == i / ((N/P)/M_P) )
			    {
			    	++m_p1;
			    	pw1.close();
			    	pw1 = new PrintWriter(new File("/home/hduser/out_"+m_p1));
			    }*/
				/*
				else
				{
					int ind = i/((N/P)/M_P);
					pw1 = new PrintWriter(new File("/home/hduser/out_" + ind));
					pw1.println(val);
					pw1.close();
				}
				//pw1.println (val);
				 * 
				 */
				if (i % ((N/P)/M_P) == 0 && i != 0 && i != N/P)
            	{
            		m_p0 = i/((N/P)/M_P);
            		Integer layer = (m_p0 - 1);
            		PrintWriter pw = new PrintWriter("/home/hduser/out_" + m_p0.toString());
            		pw.println("0" +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
            		PrintWriter pw_ = new PrintWriter("/home/hduser/out_"+layer.toString());
            		pw_.println(layer.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
            		pw.close();
            		pw_.close();
            		
            	}
            	else if (i % ((N/P)/M_P) == 1 && i != 1)
            	{
            		m_p0 = i/((N/P)/M_P);
            		Integer layer = (m_p0 - 1);
            		Integer shiftdown = ((N/P)/M_P) + 1; 
            		PrintWriter pw = new PrintWriter("/home/hduser/out_" + layer.toString());
            		pw.println(shiftdown.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
            		PrintWriter pw_ = new PrintWriter("/home/hduser/out_"+m_p0);
            		pw_.println(m_p0.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
            		pw.close();
            		pw_.close();
            	}
            	else 
            	{
            		m_p0 = i/((N/P)/M_P);
            		PrintWriter pw = new PrintWriter("/home/hduser/out_" + m_p0.toString());
            		String l = itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken();
            		pw.println(m_p0.toString() +" " + l);
            		System.out.println(m_p0.toString() +" " + l );
            		pw.close();
            	}
			}
			pw0.close();
            pw1.close();
			Job job  = new Job();
			Configuration conf = job.getConfiguration();
			conf.addResource(new Path("/home/hduser/opt/hadoop1.0.3/conf/core-site.xml"));
			conf.addResource(new Path("/home/hduser/opt/hadoop1.0.3/conf/hdfs-site.xml"));
			FileSystem hdfs = FileSystem.get(conf);
			Path file = new Path("hdfs://Math230-3:9000/user/Puasson/OUT_"
					+ key.toString());
			if(num != (N/P) * M * K){	
			try
			{
                BufferedReader br=new BufferedReader(new InputStreamReader(hdfs.open(file)));
                String line;
                line=br.readLine();
                m_p0=0;
                m_p1=0;
                pw1 = new PrintWriter(new File("/home/hduser/out_"+0));
                pw0 = new PrintWriter(new File("/home/hduser/out_"+0));
                
                while (line != null)
                {
                	StringTokenizer itr = new StringTokenizer(line);
                	i = Integer.valueOf(itr.nextToken());
    				/*if(i % ((N/P)/M_P)==0 && (m_p1 + 1) == i/((N/P)/M_P)){
    					m_p0 = i/((N/P)/M_P);
    					pw0.close();
    					pw0 = new PrintWriter(new File("/home/hduser/out_"+m_p0));
    					pw0.println(line);
    			    } else if(i%((N/P)/M_P)!=0 && m_p1+1==i/((N/P)/M_P)){
    			    	++m_p1;
    			    	pw1.close();
    			    	pw1 = new PrintWriter(new File("/home/hduser/out_"+m_p1));
    			     }
    			     */
                	if (i % ((N/P)/M_P) == 0 && i != 0 && i != N/P)
                	{
                		m_p0 = i/((N/P)/M_P);
                		Integer layer = (m_p0 + 1);
                		PrintWriter pw = new PrintWriter("/home/hduser/out_" + layer.toString());
                		pw.println("0" +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
                		PrintWriter pw_ = new PrintWriter("/home/hduser/out_"+m_p0);
                		pw_.println(m_p0.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
                		pw.close();
                		pw_.close();
                		
                	}
                	else if (i % ((N/P)/M_P) == 1 && i != 1)
                	{
                		m_p0 = i/((N/P)/M_P);
                		Integer layer = (m_p0 - 1);
                		Integer shiftdown = ((N/P)/M_P) + 1; 
                		PrintWriter pw = new PrintWriter("/home/hduser/out_" + layer.toString());
                		pw.println(shiftdown.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
                		PrintWriter pw_ = new PrintWriter("/home/hduser/out_"+m_p0);
                		pw_.println(m_p0.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
                		pw.close();
                		pw_.close();
                	}
                	else 
                	{
                		m_p0 = i/((N/P)/M_P);
                		PrintWriter pw = new PrintWriter("/home/hduser/out_" + m_p0.toString());
                		pw.println(m_p0.toString() +" " + itr.nextToken() + " "+itr.nextToken() + " "+ itr.nextToken());
                	}
    				//pw1.println(line);
                    line=br.readLine();
//                    System.out.println(line);
                }
                br.close();
                pw0.close();
                pw1.close();
        }catch(Exception e){
        }
		}
			
		//exec here
			 Process pro1 = Runtime.getRuntime().exec("/home/hduser/run.sh");
			 try {
				pro1.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(pro1.exitValue());
			 Process pro2 = Runtime.getRuntime().exec("/home/hduser/run2.sh");
			 try {
				pro2.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(pro2.exitValue());	
			
		if ( hdfs.exists( file )) { hdfs.delete( file, true ); }
		OutputStream os =  hdfs.create( file );
		BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
		Scanner sc;
        String line=null;
        
        int cnt=0;
        
        while(cnt<M_P){
        sc = new Scanner(new File("/home/hduser/out_"+cnt));
        ++cnt;
        while(sc.hasNext()){
        	line=sc.nextLine();	
        	StringTokenizer itr = new StringTokenizer(line);
			i = Integer.valueOf(itr.nextToken());
			System.out.println(i);
			if(i==1 && numK > 0){
				Integer newK = numK-1; 
				ii = N/P+1;
				String newVal = ii + " "+itr.nextToken()+" "+itr.nextToken()+" "+itr.nextToken();
				output.collect(new Text(newK.toString()), new Text(newVal));
			} else if(i==N/P && numK < P-1){
				Integer newK = numK+1;
				ii = 0;
				String newVal = ii + " "+itr.nextToken()+" "+itr.nextToken()+" "+itr.nextToken();
				output.collect(new Text(newK.toString()), new Text(newVal));
			}
			else {
				bw.write(line+"\n");
			}
        }
        sc.close();
        }
        bw.close();
        System.out.println("************");
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

		JobConf job = new JobConf(conf1, Iterations.class);
		Path in, out;

		job.setNumMapTasks(P);
		job.setNumReduceTasks(P);

		job.setJobName("Iterations");
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormat(KeyValueTextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		Integer IN = Main.i, OUT = Main.i + 1;

		in = new Path("hdfs://Math230-3:9000/user/Puasson/out_" + IN.toString());
		out = new Path("hdfs://Math230-3:9000/user/Puasson/out_"
				+ OUT.toString());

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);

		JobClient.runJob(job);

		return 0;

	}

}
