
import java.io.*;
import java.util.*;

import com.higherfrequencytrading.chronicle.Chronicle;
import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;

import mpi.MPI;

public class mpj_puasson 
{	
	static Integer M_P = 2;
	static Integer P = 6;
	static Integer N = 120;
	static Integer M = 120;
	static Integer K = 120;
	static Integer numR=1;
	static Double a = 1.0, X = 2.0, Y = 2.0, Z = 2.0;
	static Double hx = X / (N - 1), hy = Y / (M - 1), hz = Z / (K - 1),
			owx = hx * hx, owy = hy * hy, owz = hz * hz;
	static Double c = (2.0 / owx) + (2.0 / owy) + (2.0 / owz) + a;

	static Double Ro(Double x, Double y, Double z) {
		Double d = -a * (x + y + z);
		return d;
	}
	private static void removeFile(String path) {
        if (new File(path).delete()) {
            System.out.println("removed: " + path);
        } else {
            System.out.println("could not remove: " + path);
        }
    }
	public static void main(String[] args) throws IOException
	{
		//FileWriter fw = new FileWriter(new File("/home/hduser/args.txt"));
		//fw.write(args[2]);
		//fw.close();
		MPI.Init(args);
		//Double data[][][] = new Double[(N / P)/M_P + 5][M + 5][K + 5];
		Double data[][][] = new Double[(N / P)/M_P + 5][M + 5][K + 5];
		Double newData[][][] = new Double[((N / P)/M_P)+5][M + 5][K + 5];
		Double data_r[][][] = new Double[(N / P)+2][M + 5][K + 5];
		Double Fi, Fj, Fk;
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		Integer mp_s = (N/P+2)/M_P;
//		System.out.println(rank);
		PrintWriter pw = new PrintWriter (new File("/home/hduser/chr_"+rank));
		//pw.write("Hello");
		//pw.close();
		int dtx;
		int ii, jj, kk;
		//Scanner sc = new Scanner(new File("/home/hduser/out_"+rank));
		//pw.write("Hello2");
		 Chronicle chr = new IndexedChronicle("/tmp/chronicle");
		 //pw.write("Hello3");
		 // final Excerpt excerpt = chr.createExcerpt();
		 //pw.write("Hello4");
		 final Excerpt excerpt1 = chr.createExcerpt();
		 //pw.write("Hello5");
		 //pw.write("Hello6");
		 //pw.write("MPI" + " "+rank);
		 //pw.write("Hello7");
		 //pw.print(chr.size());
		 int sz = (N / P)/M_P;
		 /*for(int i=0;i<sz;++i)
			for(int j=0;j<M;++j)
				for(int k=0;k<K;++k){
					ii = sc.nextInt()-rank*sz;
					//ii = sc.nextInt(); //Из-за изменения в коде Iterations.java
					jj = sc.nextInt();
					kk = sc.nextInt();
					data[ii][jj][kk]=sc.nextDouble();
				}	
		sc.close();*/
		 pw.println("DATA");
		 for (int i = 0;i<mp_s;i++)
		 {
			 excerpt1.index(rank*mp_s + i);
			 data[i] = (Double[][])excerpt1.readObject();
			 excerpt1.finish();
			 for (int j = 0;j<M;j++)
				 for(int k = 0;k<K;k++)
					 pw.print(i + " "+j + " "+ k + " " + data[i][j][k] + "\n");
		 }
		 pw.println("END_DATA");
		 pw.close();
		/*for (Integer i = 1; i <= data.length; ++i)
			for (Integer j = 0; j < data[i].length; ++j)
			{
				for (Integer k = 0; k < data[i][j].length; ++k) {
					//if (data[i][j][k] != null)
					//pw.print(" " + i + " " +j + " "+k);
					//pw.println(Runtime.getRuntime().freeMemory());
					//else pw.print(i + " " +j + " "+k + "\n");
					//pw.close();
				}
			}
		 pw.write("data" + " "+rank);
		 pw.close();*/
		 System.out.println("MPI" + " "+rank + "1");
		 
		//PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/hduser/out_"+rank), "UTF-8"));
			dtx = numR * (N / P);
			Integer nI;
			for (Integer i = 1; i <= sz; ++i)
				for (Integer j = 0; j < M; ++j)
				{
					for (Integer k = 0; k < K; ++k) {
						
									if (j == 0)
									{
										Fj = data[i][j+1][k]/owy;
									}
									else if (j == M - 1)
									{
										Fj = data[i][j-1][k]/owy;
									}
									else Fj = (data[i][j + 1][k] + data[i][j - 1][k]) / owy;
									if (k == 0)
									{
										Fk = (data[i][j][k + 1]) / owz;
									}
									else if (k==K-1)
									{
										Fk = (data[i][j][k - 1]) / owz;
									}
									else Fk = (data[i][j][k + 1] + data[i][j][k - 1]) / owz;
									if (data[i+1][j][k]!=null && data[i - 1][j][k]!=null)
									Fi = (data[i + 1][j][k] + data[i - 1][j][k]) / owx;
									else Fi = 0.0;
									//System.out.println("Fi"+" "+i + " "+j + " "+k +" "+ Fi);
									newData[i][j][k] = (Fi + Fj + Fk - Ro((i*rank + dtx)
											* hx, j * hy, k * hz))
											/ c;
									//pw.write("NI == " + 123);
									nI = i + rank * sz;
									data_r[nI][j][k] = newData[i][j][k];
									/*String newVal = nI.toString() + " " + j.toString()
											+ " " + k.toString() + " "
											+ newData[i][j][k].toString();
									*/
									//pw.print(newVal+"\n");
									 //System.out.println("MPI" + " "+rank + "2");
									pw.println(i+ " "+j + " "+k + data_r[nI][j][k]);
									
									
									
					}
					
				}
			pw.write("finish2");

			if(rank==0) 
				for (Integer j = 0; j < M; ++j)
					for (Integer k = 0; k < K; ++k){
						/*String newVal = "0 " + j.toString()
						+ " " + k.toString() + " "
						+ data[0][j][k].toString();
						pw.print(newVal+"\n");*/
						data_r[0][j][k] = data[0][j][k];
					}
			if(rank==size-1){
				nI = (sz*rank)+(sz+1);
				for (Integer j = 0; j < M; ++j)
					for (Integer k1 = 0; k1 < K; ++k1){
						/*String newVal = nI.toString()+" "+j.toString()
						+ " " + k.toString() + " "
						+ data[sz+1][j][k].toString();
						pw.print(newVal+"\n");*/
						data_r[nI][j][k1] = data[sz+1][j][k1];
					}				 
			}
			pw.write("finish0");
			//removeFile("/tmp/chronicle"+ ".data");
		    //removeFile("/tmp/chronicle"+ ".index");
		    chr = new IndexedChronicle("/tmp/chronicle");
			final Excerpt excerpt2 = chr.createExcerpt();
			for(int i = 0;i<mp_s;i++)
			{
				excerpt2.index(rank*mp_s + i);
				excerpt2.writeObject(data_r[rank*mp_s + i]);
				excerpt2.finish();
			}
		
			/*for (int i1 = 0;i1<data_r.length;i1++)
        		for (int j1 = 0;j1<data_r[i1].length;j1++)
        			for (int k1 = 0;k1<data_r[i1][j1].length;k1++)
        				pw.print(i1+" "+j1+" "+k1+ " "+data_r[i1][j1][k1]);
						pw.close();*/
			//pw.close();
			pw.write("finish");
			pw.close();
	}

}
