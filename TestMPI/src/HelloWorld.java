import mpi.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
public class HelloWorld {

public static void main(String args[]) throws Exception 
{
	  Configuration conf = new Configuration();
	  conf.addResource(new Path("home/hduser/opt/hadoop1.0.3/conf/core-site.xml"));
	  conf.addResource(new Path("home/hduser/opt/hadoop1.0.3/conf/hdfs-site.xml"));
	  FileSystem fileSystem = FileSystem.get(conf);
	  Path path = new Path("hdfs://Math230-3:9000/user/hduser/input_puasson");
	  if (fileSystem.exists(path))
	  {
  	 //System.out.println("File " + dest + " already exists");
		 return;
	  }
	    // Create a new file and write data to it.
	    FSDataOutputStream out = fileSystem.create(path);
	    byte[] b = new byte[1024];
	    int numBytes = 0;
	    String s = "Hello world!!!";
	    out.writeUTF(s);
	    // Close all the file descripters
    	    out.close();
    	    fileSystem.close();
	MPI.Init(args);
	int me = MPI.COMM_WORLD.Rank();
	int size = MPI.COMM_WORLD.Size();

	System.out.println("Hi from <"+me+">");
	if (me == 0)
	{
		char [] message = "Hello there!".toCharArray();
		for (int i = 1;i < size;i++)
		MPI.COMM_WORLD.Send(message,0,12,MPI.CHAR,i,99);	
		
	}
	else
	{
		char [] message = new char [12];
		MPI.COMM_WORLD.Recv(message,0,12,MPI.CHAR,0,99);
		System.out.println("recieved:" + new String(message) + ":");
	}
	
	//MPI.finish();
	
	MPI.Finalize();
}

}
