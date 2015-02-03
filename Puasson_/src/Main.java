import org.apache.hadoop.conf.Configured;
import java.io.IOException;
import java.util.*;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
//import org.apache.hadoop.mapred.jobcontrol.Job;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
//import org.apache.hadoop.mapred.FileInputFormat;
//import org.apache.hadoop.mapred.FileOutputFormat;
//import org.apache.hadoop.mapred.JobClient;
//import org.apache.hadoop.mapred.JobConf;
//import org.apache.hadoop.mapred.MapReduceBase;
//import org.apache.hadoop.mapred.TextOutputFormat;
//import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapreduce.Mapper.*;
import org.apache.hadoop.mapreduce.Mapper.Context;
//import org.apache.hadoop.mapreduce.Reducer;
//import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.Reducer.*;
//import org.apache.hadoop.mapred.OutputCollector;
//import org.apache.hadoop.mapred.Reducer;
//import org.apache.hadoop.mapred.Reporter;
//import org.apache.hadoop.mapred.SequenceFileInputFormat;
//import org.apache.hadoop.mapred.jobcontrol.JobControl.*;
import org.apache.hadoop.tools.rumen.Pre21JobHistoryConstants.Values;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import java.io.IOException;
import java.util.StringTokenizer;
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

public abstract class Main extends Configured implements Tool {
	public static Integer i;

	public static void main(String[] args) throws Exception {

		int first, second = 0;
//		Iterations.writeDFS("hdfs://localhost:9000/user/hduser/Puasson/hi", "Hello");

		first = ToolRunner.run(new Configuration(), new Initial(), args);
		for (i = 0; i < 2; ++i) {
				
			Job job  = new Job();
			Configuration conf = job.getConfiguration();
			conf.addResource(new Path("/home/hduser/opt/hadoop1.0.3/conf1/core-site.xml"));
			conf.addResource(new Path("/home/hduser/opt/hadoop1.0.3/conf/hdfs-site.xml"));	
			//conf.addResource(new Path("/usr/local/hadoop/hadoop/conf/core-site.xml"));
			//conf.addResource(new Path("/usr/loca/hadoop/hadoop/conf/hdfs-site.xml"));
			FileSystem fs = FileSystem.get(conf);
			Path p = new Path("hdfs://Math230-3:9000/user/Puasson/out_" + i.toString()+ "/_logs");
			if (fs.exists(p))
			fs.delete(p , true);
			second = ToolRunner
					.run(new Configuration(), new Iterations(), args);
			
		}
		// int res3 = ToolRunner.run(new Configuration(), new Print(), args);
		System.exit(second);

	}

}