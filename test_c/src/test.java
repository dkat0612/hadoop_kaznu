
import java.io.*;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.lang.*;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.LogOutputStream;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ShutdownHookProcessDestroyer;

public class test {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException
	{
		// These are the files to include in the ZIP file
	   /* String[] source = new String[]{"/home/hduser/mpj-user/HelloWorld.java"};

	    // Create a buffer for reading the files
	    byte[] buf = new byte[1024];

	    try {
	        // Create the ZIP file
	        String target = "/home/hduser/mpj-user/target.jar";
	        //ZipOutputStream out = new ZipOutputStream(new FileOutputStream(target));
	        JarOutputStream out = new JarOutputStream(new FileOutputStream(target));
	        // Compress the files
	        for (int i=0; i<source.length; i++) {
	            FileInputStream in = new FileInputStream(source[i]);

	            // Add ZIP entry to output stream.
	            out.putNextEntry(new JarEntry(source[i]));

	            // Transfer bytes from the file to the ZIP file
	            int len;
	            while ((len = in.read(buf)) > 0) {
	                out.write(buf, 0, len);
	            }

	            // Complete the entry
	            out.closeEntry();
	            in.close();
	        }

	        // Complete the ZIP file
	        out.close();
	    } catch (IOException e) {
	    }
		ProcessBuilder pb = new ProcessBuilder("/usr/lib/jvm/java-7-openjdk-amd64", "-jar", "/home/hduser/mpj-user/target.jar");
		pb.directory(new File("/home/hduser/local_puasson"));
		Process p = pb.start();*/
		/* Process pro1 = Runtime.getRuntime().exec("javac -cp .:/home/hduser/mpj/lib/mpj.jar /home/hduser/mpj-user/HelloWorld.java");
		 pro1.waitFor();
		 Process pro2 = Runtime.getRuntime().exec("/home/hduser/mpj/bin/mpjrun.sh -np 2  /home/hduser/mpj-user/HelloWorld");
		 pro2.waitFor();
		 //Process pro0 = Runtime.getRuntime().exec("cp ");
		
		try
	      {
			Process processCompile = Runtime.getRuntime().exec(new String[]{"javac", "HelloWorld.java"}, null, new File("/home/hduser/mpj-user"));
			Process processCompile1 = Runtime.getRuntime().exec("javac HelloWorld.java", null, new File("/home/hduser/mpj-user"));
			processCompile.waitFor();
			processCompile1.waitFor();
			int c = processCompile.exitValue();
			System.out.println("c=="+c); 
			 try
		      {
				 BufferedReader inStream = new BufferedReader(
		                            new InputStreamReader( processCompile.getInputStream() ));  
		         System.out.println(inStream.readLine());
		      }
		      catch(IOException e)
		      {
		         System.err.println("Error on inStream.readLine()");
		         e.printStackTrace();  
		      }
	      }
	      catch(IOException e)
	      {
	         System.err.println("Error on exec() method");
	         e.printStackTrace();  
	      }
		
		String[] envv = new String[100000];
        Map <String,String> mp = new HashMap ();
        mp = System.getenv();
        int i = 0;
        for (Map.Entry<String, String> entry : mp.entrySet())
        {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            envv[i] = entry.getValue();
            i++;
        }*/
		
		//Process pro1 = Runtime.getRuntime().exec("javac HelloWorld.java",null,new File ("/home/hduser"));
	    //pro1.waitFor();
	    //System.out.println("Hello world");
	    //Process pro2 = Runtime.getRuntime().exec("java HelloWorld",null,new File ("/home/hduser"));
	    //pro2.waitFor();
	    
	    /*System.out.println("Hello world123");
		String[] args1 = new String[4];
        args1[0] = "/home/hduser/mpj/bin/mpjrun.sh";
        args1[1] = " -np";
        args1[2] = "4";
        args1[3] = "HelloWorld";
        
        //Process proc = Runtime.getRuntime().exec("javac HelloWorld.java",envv,new File ("//home//hduser//mpj-user//"));
        //int rc = proc.waitFor();
		 String []env = new String [3];
		 //env[0] = System.getenv("PATH");
		 //System.out.print(env[0]);
		 //env[1] = System.getenv("MPJ_HOME");
		 //System.out.print(env[1]);
		 Process pro1 = Runtime.getRuntime().exec("javac -cp .:/home/hduser/mpj/lib/mpj.jar /home/hduser/HelloWorld.java");
		 pro1.waitFor();
		 System.out.println(pro1.exitValue());
		 //Process pro2 = Runtime.getRuntime().exec("java -jar /home/hduser/mpj/lib/starter.jar -np 4 /home/hduser/HelloWorld");
		 //Process pro2 = Runtime.getRuntime().exec(args1,null,new File ("/home/hduser/"));
		 //pro2.waitFor();
		// System.out.println(pro2.exitValue());
		 //Process pro1 = Runtime.getRuntime().exec("javac /home/hduser/HelloWorld.java");
		// pro1.waitFor();
		//Process pro2 = Runtime.getRuntime().exec("java /home/hduser/HelloWorld");
		// pro2.waitFor();
		 //Command to be executed
		  CommandLine command = new CommandLine("java -jar /home/hduser/mpj/lib/starter.jar");
		 	 
		 	 //Adding its arguments
		 	 command.addArguments(new String[]{"-np 4","HelloWorld"});
		  
		 	 //Infinite timeout
		 	 ExecuteWatchdog watchDog = new ExecuteWatchdog(1000000);
		 	 
		 	 //Result Handler for executing the process in a Asynch way
		 	 DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
		 	 
		 	 //Using Std out for the output/error stream
		  PumpStreamHandler streamHandler = new PumpStreamHandler();
		 	 
		 	 //This is used to end the process when the JVM exits
		  ShutdownHookProcessDestroyer processDestroyer = new ShutdownHookProcessDestroyer();
		 	 
		 	 //Our main command executor
		 	 DefaultExecutor executor = new DefaultExecutor();
		 	 
		 	 //Setting the properties
		 	 executor.setStreamHandler(streamHandler);
		 	 executor.setWatchdog(watchDog);
		 	 //Setting the working directory
		 	 //Use of recursion along with the ls makes this a long running process
		 	 executor.setWorkingDirectory(new File("/home/hduser"));
		 	 executor.setProcessDestroyer(processDestroyer);
		 	 
		 	 //Executing the command
		 	 executor.execute(command,resultHandler);
		 	 
		 	 //The below section depends on your need
		 	 //Anything after this will be executed only when the command completes the execution
		 	 resultHandler.waitFor();
		 	 int exitValue = resultHandler.getExitValue();
		 	 System.out.println(exitValue);
		 	 if(executor.isFailure(exitValue)){
		 	    System.out.println("Execution failed");
		 	 }else{
		 	    System.out.println("Execution Successful");
		 	 }*/
		System.out.println(System.getenv("MPJ_HOME"));
		Process pro1 = Runtime.getRuntime().exec("/home/hduser/run3.sh");
		 pro1.waitFor();
		 System.out.println(pro1.exitValue());
		 //Process pro2 = Runtime.getRuntime().exec("java -jar /home/hduser/mpj/lib/starter.jar -np 4 /home/hduser/HelloWorld");
		 Process pro2 = Runtime.getRuntime().exec("/home/hduser/run4.sh");
		 pro2.waitFor();
	}

}
