import java.io.IOException;
import java.util.*;

import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.util.*;

public class Initial extends Configured implements Tool {

	static Integer P = 5;
	static Integer N = 240;
	static Integer M = 240;
	static Integer K = 240;
	static Double a = 1.0, X = 2.0, Y = 2.0, Z = 2.0;
	static Double hx = X / (N - 1), hy = Y / (M - 1), hz = Z / (K - 1),
			owx = hx * hx, owy = hy * hy, owz = hz * hz;
	static Double c = (2.0 / owx) + (2.0 / owy) + (2.0 / owz) + a;

	static Double Fresh(Double x, Double y, Double z) {
		Double res = x + y + z;
		return res;
	}

	public static class MapClass extends MapReduceBase implements
			Mapper<Text, Text, Text, Text> {

		public void map(Text key, Text value,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			P = Integer.valueOf(key.toString());
			String val = "";
			for (Integer i = 0; i < P; ++i)
				output.collect(new Text(i.toString()), new Text(val));
		}
	}

	public static class Reduce extends MapReduceBase implements
			Reducer<Text, Text, Text, Text> {

		
		public void reduce(Text key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			String val="";
			Double V=0.0;
			Integer i, j, k, newK = Integer.valueOf(key.toString());
			for (i = 0; i <= N / P + 1; ++i)
				for (j = 0; j < M; ++j)
					for (k = 0; k < K; ++k) {
						if ((i>0&&i<N/P+1)&&((i == 1 && newK == 0)
								|| (i == N / P && newK == P - 1) || j == 0
								|| j == M - 1 || k == 0 || k == K - 1))
							V = Fresh((newK * (N / P) + i) * hx, j * hy, k * hz);
						else
							V = 0.0;
						val = i.toString() + " " + j.toString() + " "
								+ k.toString() + " " + V.toString();
						output.collect(key, new Text(val));
					}
		}
	}

	public int run(String[] args) throws Exception {

		Configuration conf = getConf();
		JobConf job = new JobConf(conf, Iterations.class);

		Path in = new Path("hdfs://Math230-3:9000/user/Puasson/in/input_puasson");
		Path out = new Path("hdfs://Math230-3:9000/user/Puasson/out_0");
//		Path in = new Path(args[0]);
//		Path out = new Path(args[1]);
		// N = Integer.valueOf(args[2]);
		// M = Integer.valueOf(args[3]);
		// K = Integer.valueOf(args[4]);

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);
		job.setNumMapTasks(5);
		job.setNumReduceTasks(5);

		// conf.addResource(new
		// Path("/usr/local/hadoop/hadoop/conf/core-site.xml"));
		// conf.addResource(new
		// Path("/usr/loca/hadoop/hadoop/conf/hdfs-site.xml"));
		// FileSystem hdfs = FileSystem.get(conf);
		// FileSystem local = FileSystem.getLocal(conf);
		// if (hdfs.exists(new
		// Path("hdfs://Math121-3:9000/user/matrix_in_sort"))) {
		// hdfs.delete(new Path("hdfs://Math121-3:9000/user/matrix_in_sort"),
		// true);
		// }
		// job.setJarByClass(Matrix_sort.class);
		job.setJobName("Initial");
		job.setMapperClass(MapClass.class);
		job.setReducerClass(Reduce.class);

		// FileInputFormat.setInputPaths(job, new Path(
		// "hdfs://Math121-3:9000/user/matrix_in/right"));

		// FileOutputFormat.setOutputPath(job, new Path(
		// "hdfs://Math121-3:9000/user/matrix_in_sort"));
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		job.setInputFormat(KeyValueTextInputFormat.class);
		job.setOutputFormat(TextOutputFormat.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		// job.set("key.value.separator.in.input.line"," ");

		JobClient.runJob(job);

		return 0;

	}

}