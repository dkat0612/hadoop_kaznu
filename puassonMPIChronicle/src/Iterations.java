import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

//import mpi.MPI;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.*;
import org.omg.CORBA.portable.InputStream;

import com.higherfrequencytrading.chronicle.Chronicle;
import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;



public class Iterations extends Configured implements Tool {

	static Integer P = 7;
	static Integer N =140;
	static Integer M =140;
	static Integer K = 140;
	static Integer M_P = 2;
	
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
			
			Integer k = (N/P+2)/M_P, numK = Integer.valueOf(key.toString()), ii;
			int rank0=-1, rank1=0;
			int num = 0,i,t0, t1;
			String val, nVal;
			FileWriter pw0 = null, pw1 = null, pw2 = null;
			Double vals[][][][] = new Double [M_P][(N/P)/M_P + 5][M+5][K+5];
			Double data_r[][][] = new Double[(N / P)+2][M + 5][K + 5];
			while (values.hasNext()) 
			{
				num++;
				val = values.next().toString();
				StringTokenizer itr = new StringTokenizer(val);
				int i1,j1,k1;
				i1 = Integer.valueOf(itr.nextToken());
				j1 = Integer.valueOf(itr.nextToken());
				k1 = Integer.valueOf(itr.nextToken());
				Double pv = Double.valueOf(itr.nextToken());
				vals[i1 / k][i1 % k][j1][k1] = pv;	
			}
			/*while (values.hasNext()) {
				num++;	
				val = values.next().toString();
				StringTokenizer itr = new StringTokenizer(val);
				i = Integer.valueOf(itr.nextToken());
				System.out.println (num);
				rank1 = i/k;
				if(rank1 != rank0) {
					if(pw0!=null) pw0.close();
					if(pw1!=null) pw1.close();
					if(pw2!=null) pw2.close();
					
					pw1 = new FileWriter (new File("/home/hduser/out_"+rank1),true);
					rank0 = rank1;
					t0 = rank1 - 1;
					t1 = rank1 + 1;
					if(rank1>0) pw0 = new FileWriter (new File("/home/hduser/out_"+t0),true);
					if(rank1<M_P-1) pw2 = new FileWriter (new File("/home/hduser/out_"+t1),true);
				}
				System.out.println (num + "Hello");
				if(rank1<M_P-1 && (i+1)%k==0) pw2.write(val+"\n");
				if(rank1>0 && i%k==0) pw0.write(val+"\n");
				pw1.write(val+"\n");
				System.out.println (num + "Hello1");
			}
			if (pw0!=null)
			pw0.close();
			
			if (pw1!=null)
			pw1.close();
			
			if (pw2!=null)
			pw2.close();
			*/
			System.out.println (numK + "Process1------------------------------------------");	
			Job job  = new Job();
			Configuration conf = job.getConfiguration();
			conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
			conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
			FileSystem hdfs = FileSystem.get(conf);
			Path file = new Path("hdfs://Math230-3:9000/user/Puasson/OUT_"
					+ key.toString());
			pw0=null; pw1=null; pw2=null;
			rank0=-1; rank1=0;
			System.out.print(num +"==" + (((N/P) + 2) * M * K) + "?");
			
			if(num != (((N/P) + 2) * M * K)){
				System.out.println (num + "Hello123");
			try
			{
                BufferedReader br=new BufferedReader(new InputStreamReader(hdfs.open(file)));
                val = br.readLine();
                while (val != null)
                {
                	StringTokenizer itr = new StringTokenizer(val);
                	int i1,j1,k1;
    				i1 = Integer.valueOf(itr.nextToken());
    				j1 = Integer.valueOf(itr.nextToken());
    				k1 = Integer.valueOf(itr.nextToken());
    				Double pv = Double.valueOf(itr.nextToken());
    				vals[i1 / k][i1 % k][j1][k1] = pv;
    				/*if(rank1!=rank0) {
    					if(pw0!=null) pw0.close();
    					if(pw1!=null) pw1.close();
    					if(pw2!=null) pw2.close();
    					pw1 = new FileWriter(new File("/home/hduser/out_"+rank1),true);
    					rank0 = rank1;
    					t0 = rank1-1;
    					t1 = rank1 + 1;
    					if(rank1>0) pw0 = new FileWriter(new File("/home/hduser/out_"+t0),true);
    					if(rank1<M_P-1) pw2 = new FileWriter(new File("/home/hduser/out_"+t1),true);
    				}
    				if(rank1<M_P-1 && (i+1)%k==0) pw2.write(val+"\n");
    				if(rank1>0 && i%k==0) pw0.write(val+"\n");
    				pw1.write(val+"\n");
    				System.out.println  (val + "val");*/
    				val = br.readLine();
                }  }catch(Exception e){
        }
		}
		Chronicle chr = new IndexedChronicle("/tmp/chronicle");
		final Excerpt excerpt1 = chr.createExcerpt();	
		for (i = 0;i<M_P;i++)
		{
			excerpt1.startExcerpt((k + 5)*(M+5)*(K+5));
			excerpt1.writeObject(vals[i]);
			excerpt1.finish();
		}	
		System.out.println (numK + "Process123------------------------------------------");	
		//exec here
			 Process pro1 = Runtime.getRuntime().exec("/home/hduser/run.sh");
			 try {
				pro1.waitFor();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(pro1.exitValue());
			 FileWriter fw = new FileWriter(new File("/home/hduser/run2.sh"),true);
			 fw.write(hdfs.toString());
			 fw.close();
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
        System.out.println (numK + "Process------------------------------------------");
        
        for (i = 0;i<M_P;i++)
		{
        	Double data_r1[][][] = new Double[(N / P)+2][M + 5][K + 5];
        	excerpt1.index(i);
        	data_r1 = (Double [][][])excerpt1.readObject();
        	
        	for (int j = i*k;j<(i+1)*k;j++)
        	{
        		data_r[j] = data_r1[j];
        	}
        	excerpt1.finish();
		}
        for (i = 0;i<(N/P+2);i++)
        {
        	for (int j = 0;j<M;j++)
        	{
        		for (int k1 = 0;k<K;k++)
        		{
        			if(i==1 && numK > 0){
        				Integer newK = numK-1; 
        				ii = N/P+1;
        				String newVal = ii + " "+j+" "+k+" "+data_r[i][j][k];
        				output.collect(new Text(newK.toString()), new Text(newVal));
        			} else if(i==N/P && numK < P-1){
        				Integer newK = numK+1;
        				ii = 0;
        				String newVal = ii + " "+j+" "+k+" "+data_r[i][j][k];
        				output.collect(new Text(newK.toString()), new Text(newVal));
        			}
        			else 
        			{
        				bw.write(i+" "+j+" "+k + " "+data_r[i][j][k]);
        			}
        		}
        	}
        }
        /*while(cnt<M_P){	
        sc = new Scanner(new File("/home/hduser/out_"+cnt));
        while(sc.hasNext())
        {
        	line=sc.nextLine();	
        	StringTokenizer itr = new StringTokenizer(line);
			i = Integer.valueOf(itr.nextToken());
//			System.out.println(i);
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
			else 
			{
				bw.write(line+"\n");
			}
        }
        
        
        sc.close();
        File f = new File("/home/hduser/out_"+cnt);
        boolean res = f.delete();
        if(res) System.out.println("deleted" + cnt); 
        else	System.out.println("not deleted" + cnt);
        ++cnt;
        }*/
        bw.close();
        
        System.out.println (numK + "Process1234567------------------------------------------");
        
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

		job.setNumMapTasks(7);
		job.setNumReduceTasks(7);

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

