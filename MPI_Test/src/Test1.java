
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.util.*;


import mpi.MPI;

public class Test1 {

	public static void main(String[] args) throws IOException, ClassNotFoundException
	{
		
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		
		 Socket ss = new Socket("127.0.0.1",8383);
         ObjectInputStream is = (ObjectInputStream) ss.getInputStream();
         URI uri = (URI) is.readObject();
         System.out.println(uri);
         is.close();

		
		
	}

}
