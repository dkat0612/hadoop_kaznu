import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.util.ToolRunner;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {	int res;		
	//res = ToolRunner.run(new Configuration(), new isodata_algorithm(w), www);
int res1 = ToolRunner.run(new Configuration(), new isodata_algorithm(), args);
System.exit(res1);
}
}
