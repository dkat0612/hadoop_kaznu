import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
 
public class Print {
	public static void main(String[] args) {
		try {
 
			String content = "This is the content to write into file";
			String content2 = "Second time";
 
			File file = new File("/home/hduser/file");
 
			FileWriter sw = new FileWriter(file,true);

		    sw.write("Add this text to the end of datafile by FileWriter" + "\n");

		    sw.close();
			
		    
		    FileWriter sw2 = new FileWriter(file,true);
		    
		    sw2.write(content2);
		    sw2.close();
		    
			System.out.println("Done");
 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}