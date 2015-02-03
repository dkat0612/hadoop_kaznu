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

import com.sun.org.apache.xerces.internal.util.URI;

public class Iterations extends Configured implements Tool {

	static Integer P = 8;
	static Integer N = 1024;
	static Integer M = 1024;
	static Integer K = 1024;
	static Double a = 1.0, X = 2.0, Y = 2.0, Z = 2.0;
	static Double hx = X / (N - 1), hy = Y / (M - 1), hz = Z / (K - 1),
			owx = hx * hx, owy = hy * hy, owz = hz * hz;
	static Double c = (2.0 / owx) + (2.0 / owy) + (2.0 / owz) + a;
	//static Configuration conf;
//	static FileSystem hdfs;

	static Double Ro(Double x, Double y, Double z) {
		Double d = -a * (x + y + z);
		return d;
	}

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
			// output.collect(key, new Text("5"));

			Double data[][][] = new Double[N / P + 5][M + 5][K + 5];
			Double newData[][][] = new Double[N / P + 5][M + 5][K + 5];
			Integer x, y, z, dtx, numK = Integer.valueOf(key.toString());
			String val;
			Double v, Fi, Fj, Fk;
			int num = 0;
			while (values.hasNext()) {
				num++;	
				val = values.next().toString();
				StringTokenizer itr = new StringTokenizer(val);
				x = Integer.valueOf(itr.nextToken());
				y = Integer.valueOf(itr.nextToken());
				z = Integer.valueOf(itr.nextToken());
				v = Double.valueOf(itr.nextToken());
				data[x][y][z] = v;
				// Integer kk = Integer.valueOf(key.toString());
				// if(	kk==3)
				// System.out.println(x+" "+y+" "+z+" "+data[x][y][z]);

				// System.out.println(key+"\t"+data[x][y][z]);
			} // System.out.println(key);
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
                while (line != null){
                	StringTokenizer itr = new StringTokenizer(line);
    				x = Integer.valueOf(itr.nextToken());
    				y = Integer.valueOf(itr.nextToken());
    				z = Integer.valueOf(itr.nextToken());
    				v = Double.valueOf(itr.nextToken());
    				data[x][y][z] = v;
                    line=br.readLine();
                }
                br.close();
        }catch(Exception e){
        }
		}
			if ( hdfs.exists( file )) { hdfs.delete( file, true ); }
			//Path file1 = new Path("hdfs://Math121-3:9000/user/Puasson/test_"
			//		+ key.toString());
			//OutputStream os1 =  hdfs.create( file1);
			//os1.close();
			OutputStream os =  hdfs.create( file );
			BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
			
			dtx = numK * (N / P);
			for (Integer i = 0; i <= N / P + 1; ++i)
				for (Integer j = 0; j < M; ++j)
					for (Integer k = 0; k < K; ++k) {
//						System.out.println(key+" "+i+" "+j+" "+k);
						if (i > 0 && i < N / P + 1 && j > 0 && j < M - 1
								&& k > 0 && k < K - 1
								&& (!(numK == 0 && i == 1))
								&& (!(numK == P - 1 && i == N / P))) {
							Fi = (data[i + 1][j][k] + data[i - 1][j][k]) / owx;
							Fj = (data[i][j + 1][k] + data[i][j - 1][k]) / owy;
							Fk = (data[i][j][k + 1] + data[i][j][k - 1]) / owz;
							newData[i][j][k] = (Fi + Fj + Fk - Ro((i + dtx)
									* hx, j * hy, k * hz))
									/ c;
							String newVal = i.toString() + " " + j.toString()
									+ " " + k.toString() + " "
									+ newData[i][j][k].toString();
							br.write(newVal+"\n");
//							output.collect(key, new Text(newVal));
						} else if ((i > 0 && i < N / P + 1)
								|| (i == 0 && numK == 0)
								|| (i == N / P + 1 && numK == P - 1)) {
							newData[i][j][k] = data[i][j][k];
							String newVal = i.toString() + " " + j.toString()
									+ " " + k.toString() + " "
									+ newData[i][j][k].toString();
							
							
							br.write(newVal+"\n");
							/*writeDFS(
									"hdfs://localhost:9000/user/hduser/Puasson/OUT_"
											+ key.toString(), key.toString()
											+ "\t" + newVal+"\n");*/

//							output.collect(key, new Text(newVal));
						}
						// if(numK==3 && i==2 &&j==10&&k==10)
						// System.out.println(data[i + 1][j][k]+" "+data[i -
						// 1][j][k]);
						Integer newK = Integer.valueOf(key.toString()), ii;
						if ((i == 1 && newK > 0)
								|| (i == N / P && newK < P - 1)) {

							if (i == 1) {
								newK--;
								ii = N / P + 1;
							} else {
								newK++;
								ii = 0;
							}
							String newVal = ii.toString() + " " + j.toString()
									+ " " + k.toString() + " "
									+ newData[i][j][k].toString();
							output.collect(new Text(newK.toString()), new Text(
									newVal));
						}
					}
			 br.close();
			 
		}
	}

	public int run(String[] args) throws Exception {

		Job job1  = new Job();
		Configuration conf1 = job1.getConfiguration();
		conf1.addResource(new Path("/home/hduser/opt/hadoop1.0.3/conf/core-site.xml"));
		conf1.addResource(new Path("/home/hduser/opt/hadoop1.0.3/conf/hdfs-site.xml"));	
		 conf1 = getConf();
		 //conf.addResource(new Path("/home/hduser/opt/hadoop/conf/core-site.xml"));
		 //conf.addResource(new Path("/home/hduser/opt/hadoop/conf/hdfs-site.xml"));
		 FileSystem hdfs = FileSystem.get(conf1 );
		 
		JobConf job = new JobConf(conf1, Iterations.class);
		Path in, out;
		
		job.setNumMapTasks(8);
		job.setNumReduceTasks(8);
		// t = (int) Math.log10((double) N) + 1;

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

		in = new Path("hdfs://Math230-3:9000/user/Puasson/out_"
				+ IN.toString());
		out = new Path("hdfs://Math230-3:9000/user/Puasson/out_"
				+ OUT.toString());

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		
		JobClient.runJob(job);

		return 0;

	}

/*	public static void writeDFS(String in, String data) throws IOException {    
		 Configuration config = new Configuration(); 
		 config.addResource(new Path("/usr/local/hadoop/conf/core-site.xml"));
		 config.addResource(new Path("/usr/local/hadoop/conf/hdfs-site.xml"));
		 FileSystem hdfs = FileSystem.get(config );
		 Path file = new Path(in);
//		 if ( hdfs.exists( file )) { hdfs.delete( file, true ); } 
		 OutputStream os =  hdfs.create( file);
		 BufferedWriter br = new BufferedWriter( new OutputStreamWriter( os, "UTF-8" ) );
		 br.write(data);
		 br.close();
		
		 
	}*/

}
