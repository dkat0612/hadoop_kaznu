
import java.io.*;
import java.util.*;
import mpi.MPI;

public class mpj_puasson {
	
	static Integer M_P = 2;
	static Integer P = 2;
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
	
	public static void main(String[] args) throws IOException
	{
		FileWriter fw = new FileWriter(new File("/home/hduser/args.txt"));
		
		fw.write(args[2]);
		fw.close();
		MPI.Init(args);
		Double data[][][] = new Double[(N / P)/M_P + 5][M + 5][K + 5];
		Double newData[][][] = new Double[((N / P)/M_P)+5][M + 5][K + 5];
		
		Double Fi, Fj, Fk;
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
//		System.out.println(rank);
		
		int dtx;
		int ii, jj, kk;
		Scanner sc = new Scanner(new File("/home/hduser/out_"+rank));
		int sz = (N / P)/M_P;
		
		for(int i=0;i<sz;++i)
			for(int j=0;j<M;++j)
				for(int k=0;k<K;++k){
					ii = sc.nextInt()-rank*sz;
					//ii = sc.nextInt(); //Из-за изменения в коде Iterations.java
					jj = sc.nextInt();
					kk = sc.nextInt();
					data[ii][jj][kk]=sc.nextDouble();
				}	
		sc.close();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/hduser/out_"+rank), "UTF-8"));
			dtx = numR * (N / P);
			Integer nI;
			for (Integer i = 1; i <= sz; ++i)
				for (Integer j = 0; j < M; ++j)
					for (Integer k = 0; k < K; ++k) {
									Fi = (data[i + 1][j][k] + data[i - 1][j][k]) / owx;
									Fj = (data[i][j + 1][k] + data[i][j - 1][k]) / owy;
									Fk = (data[i][j][k + 1] + data[i][j][k - 1]) / owz;
									newData[i][j][k] = (Fi + Fj + Fk - Ro((i*rank + dtx)
											* hx, j * hy, k * hz))
											/ c;
									nI = i+rank*sz;
									String newVal = nI.toString() + " " + j.toString()
											+ " " + k.toString() + " "
											+ newData[i][j][k].toString();
									
									pw.print(newVal+"\n");
					}
			if(rank==0) 
				for (Integer j = 0; j < M; ++j)
					for (Integer k = 0; k < K; ++k){
						String newVal = "0 " + j.toString()
						+ " " + k.toString() + " "
						+ data[0][j][k].toString();
						pw.print(newVal+"\n");
					}
			if(rank==size-1){
				nI = (sz*rank)+(sz+1);
				for (Integer j = 0; j < M; ++j)
					for (Integer k = 0; k < K; ++k){
						String newVal = nI.toString()+" "+j.toString()
						+ " " + k.toString() + " "
						+ data[sz+1][j][k].toString();
						pw.print(newVal+"\n");
					}				 
			}
			pw.close();
	}
	 

}
