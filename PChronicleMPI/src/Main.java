
import java.io.*;
import java.util.*;

import com.higherfrequencytrading.chronicle.Chronicle;
import com.higherfrequencytrading.chronicle.Excerpt;
import com.higherfrequencytrading.chronicle.impl.IndexedChronicle;

import mpi.MPI;

public class Main 
{	
	static Integer M_P = 2;
	static Integer P = 2;
	static Integer numR = 1;
	/*static Integer N = 120;
	static Integer M = 120;
	static Integer K = 120;
	
	static Double a = 1.0, X = 2.0, Y = 2.0, Z = 2.0;
	static Double hX = X / (N - 1), hY = Y / (M - 1), hZ = Z / (K - 1),
			owx = hX * hX, owy = hY * hY, owz = hZ * hZ;
	static Double c = (2.0 / owx) + (2.0 / owy) + (2.0 / owz) + a;

	static Double Ro(Double x, Double y, Double z) {
		Double d = -a * (x + y + z);
		return d;
	}*/
	static float pi = (float) Math.acos(-1); 
	static double a=pi;
	static double b=3*pi*pi;
	
	//kubting olshemderi
	static float X=1;
	static float Y=1;
	static float Z=1;
	//uakhit
	static float T=1;
	
	//tor olshemderi
	static int nX=120;
	static int nY=120;
	static int nZ=120;
	//static int xLeft;
	
	//khadam
	static float hX=X/(nX-2);
	static float hY=Y/(nY-2);
	static float hZ=Z/(nZ-2);
	//uakhit khadami
	static float dt=(float)0.15*hX*hX;
	static float v= (float)(dt/(hX*hX)); 
	
	//uakhitkha saykes iteratsia sani
	static int it=(int) (T/dt);
	
	//static int old=0,cur=1;
	
	//static double e=0001;
	
	//jilu otkizgishtik koeficenti
	public static double fi(float x,float y, float z){
		return (0.5*Math.cos(Math.pow(x, 2)*Math.pow(y, 2)*Math.pow(z, 2)));
	}
	//bastapkhi shart
	public static double Fi(float x,float y,float z){
		return Math.sin(a*x)*Math.sin(a*y)*Math.sin(a*z);
		
	}
	
	
	public static double F(float x,float y,float z,float t){
		//return Math.exp(-b*t)*Math.PI*(3*Math.PI*Math.sin(a*x)*Math.sin(a*y)*Math.sin(a*z)*(fi(x,y,z)-1)-2*(x*Math.cos(a*x)*Math.sin(a*y)*Math.sin(a*z)-y*Math.sin(a*x)*Math.cos(a*y)*Math.sin(a*z)-z*Math.sin(a*x)*Math.sin(a*y)*Math.cos(a*z)));
		return Math.exp(-b*t)*Math.sin(a*x)*Math.sin(a*y)*Math.sin(a*z)*fi(x,y,z);
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
		Chronicle chr1 = new IndexedChronicle("/tmp/time");
		final Excerpt excerpt0 = chr1.createExcerpt();
		excerpt0.index(0);
		 int t = excerpt0.readInt();
		 excerpt0.finish();
		 chr1.close();
		/*Scanner sc=new Scanner(new File("/home/hduser/time.txt"));
		double t=sc.nextDouble();
		sc.close();*/
		//FileWriter fw = new FileWriter(new File("/home/hduser/args.txt"));
		//fw.write(args[2]);
		//fw.close();
		MPI.Init(args);
		//Double data[][][] = new Double[(N / P)/M_P + 5][M + 5][K + 5];
		Double data[][][] = new Double[(nX / P)/M_P + 5][nY + 5][nZ + 5];
		Double newData[][][] = new Double[((nX / P)/M_P)+5][nY + 5][nZ + 5];
		Double data_r[][][] = new Double[(nX / P)+2][nY + 5][nZ + 5];
		//Double Fi, Fj, Fk;
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		Integer mp_s = (nX/P+2)/M_P;
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
		 int sz = (nX / P)/M_P;
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
			 for (int j = 0;j<nY;j++)
				 for(int k = 0;k<nZ;k++)
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
			dtx = numR * (nX / P);
			Integer nI;
			for (Integer i = 1; i <= sz; ++i)
				for (Integer j = 0; j < nY; ++j)
				{
					for (Integer k = 0; k < nZ; ++k) {
						/*
									if (j == 0)
									{
										Fj = data[i][j+1][k]/owy;
									}
									else if (j == nY - 1)
									{
										Fj = data[i][j-1][k]/owy;
									}
									else Fj = (data[i][j + 1][k] + data[i][j - 1][k]) / owy;
									if (k == 0)
									{
										Fk = (data[i][j][k + 1]) / owz;
									}
									else if (k==nZ-1)
									{
										Fk = (data[i][j][k - 1]) / owz;
									}
									else Fk = (data[i][j][k + 1] + data[i][j][k - 1]) / owz;
									if (data[i+1][j][k]!=null && data[i - 1][j][k]!=null)
									Fi = (data[i + 1][j][k] + data[i - 1][j][k]) / owx;
									else Fi = 0.0;
									//System.out.println("Fi"+" "+i + " "+j + " "+k +" "+ Fi);
									newData[i][j][k] = (Fi + Fj + Fk - Ro((i*rank + dtx)
											* hX, j * hY, k * hZ))
											/ c;*/
						if(j!=0&&j!=nY-1&&k!=0&&k!=nZ-1){
							newData[i][j][k]=(((fi((float) (Math.round((i+dtx)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i+1][j][k]-data[i][j][k])-(fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i-2+dtx)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j][k]-data[i-1][j][k]))/(hX*hX)+
							         ((fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round((j+1)*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j+1][k]-data[i][j][k])-(fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round((j-1)*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j][k]-data[i][j-1][k]))/(hY*hY)+
							         ((fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round((k+1)*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j][k+1]-data[i][j][k])-(fi((float) (Math.round((i-1+dtx)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round((k-1)*hZ*1000)/1000.0)))*(data[i][j][k]-data[i][j][k-1]))/(hZ*hZ))*dt*0.5+
							         data[i][j][k]+F((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0),(t+1)*dt);
						
						}else{
							newData[i][j][k]=data[i][j][k];
						}
						newData[i][j][k]=data[i][j][k];
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
				for (Integer j = 0; j < nY; ++j)
					for (Integer k = 0; k < nZ; ++k){
						/*String newVal = "0 " + j.toString()
						+ " " + k.toString() + " "
						+ data[0][j][k].toString();
						pw.print(newVal+"\n");*/
						data_r[0][j][k] = data[0][j][k];
					}
			if(rank==size-1){
				nI = (sz*rank)+(sz+1);
				for (Integer j = 0; j < nY; ++j)
					for (Integer k1 = 0; k1 < nZ; ++k1){
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
			chr.close();
	}

}
