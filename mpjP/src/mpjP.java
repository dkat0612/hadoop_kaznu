
import java.io.*;
import java.util.*;

import mpi.MPI;

public class mpjP {
	
	static Integer M_P = 2;
	static Integer P = 2;
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
	static Integer numR=1;
	/*static Integer N = 720;
	static Integer M = 720;
	static Integer K = 720;
	
	static Double a = 1.0, X = 2.0, Y = 2.0, Z = 2.0;
	static Double hx = X / (N - 1), hy = Y / (M - 1), hz = Z / (K - 1),
			owx = hx * hx, owy = hy * hy, owz = hz * hz;
	static Double c = (2.0 / owx) + (2.0 / owy) + (2.0 / owz) + a;

	static Double Ro(Double x, Double y, Double z) {
		Double d = -a * (x + y + z);
		return d;
	}
	*/
	public static void main(String[] args) throws IOException
	{
		MPI.Init(args);
		int rank = MPI.COMM_WORLD.Rank();
		int size = MPI.COMM_WORLD.Size();
		//FileWriter fw = new FileWriter(new File("/home/hduser/args.txt"));
		Scanner iread = new Scanner (new File("/home/hduser/pressure_configs_input" + Integer.valueOf(rank).toString()));
		String params = iread.nextLine();
		iread.close();
		StringTokenizer token = new StringTokenizer(params);
		int []par = new int [10];
		int count = 0;
		while (token.hasMoreTokens())
		{
			par[count++] = Integer.valueOf(token.nextToken());
		}
		nX = par[0];nY = par[1];nZ = par[2];P = par[3];M_P = par[4];
		Double data[][][] = new Double[((nX / P)/M_P) + 5][nY + 5][nZ + 5];
		Double newData[][][] = new Double[((nX / P)/M_P)+5][nY + 5][nZ + 5];
		Double Fi, Fj, Fk;
//		System.out.println(rank);
		long time = System.currentTimeMillis()/1000;
		double sec = time/1000;
		double min = sec/60;
		double hrs = min/60;
		//fw.write("HEllo I am "+ rank + sec+" "+min+" "+hrs);
		//fw.close();
		int dtx;
		int ii, jj, kk;
		Scanner sc = new Scanner(new File("/home/hduser/out_"+rank));
		int sz = ((nX)/ P)/M_P;
		//OLD METHOD
		
		/*for(int i=0;i<sz;++i)
			for(int j=0;j<nY;++j)
				for(int k=0;k<nZ;++k){
					ii = sc.nextInt();
					ii-=(rank-1)*sz;
					//ii = sc.nextInt(); //Из-за изменения в коде Iterations.java
					jj = sc.nextInt();
					kk = sc.nextInt();
					
					data[ii][jj][kk]=sc.nextDouble();
					
				}
				*/
		//NEW METHOD
		//Double dr[][][] = new Double[(nX / P)/M_P + 5][nY + 5][nZ + 5];
		int nk = 0;
		int ni  = 0;
		int nj = 0;
		PrintWriter pwerr = new PrintWriter(new File("error_with_data"+rank));
		//pwerr.write("gfgfgf");
		//pwerr.write(sc.nextLine());
		//pwerr.close();
		StringTokenizer tk = new StringTokenizer(sc.nextLine());
		while (tk.hasMoreTokens())
		{
			data[ni][nj][nk] = Double.valueOf(tk.nextToken());
			pwerr.write(ni+" "+nj+" "+nk+" "+data[ni][nj][nk]+"\n");
			nk++;
			if (nk % nZ == 0 && nk!=0) {nj++;nk=0;}
			if (nj%nY==0 && nj!=0) {ni++;nj=0;nk=0;}
		}
		pwerr.close();
		sc.close();
		Scanner scan=new Scanner(new File("/home/hduser/time.txt"));
		int t=scan.nextInt();
		scan.close();
		PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("/home/hduser/out_"+rank), "UTF-8"));
			dtx = numR * (nX / P);
			Integer nI;
			for (Integer i = 1; i <= sz; ++i)
				for (Integer j = 0; j < nY; ++j)
					for (Integer k = 0; k < nZ; ++k) {
									/*Fi = (data[i + 1][j][k] + data[i - 1][j][k]) / owx;
									Fj = (data[i][j + 1][k] + data[i][j - 1][k]) / owy;
									Fk = (data[i][j][k + 1] + data[i][j][k - 1]) / owz;
									newData[i][j][k] = (Fi + Fj + Fk - Ro((i*rank + dtx)
											* hx, j * hy, k * hz))
											/ c;
									*/
									//if(j!=nY-1&&j!=0&&k!=nZ-1&&k!=0){
									  if (j!=nY-1 && j!=0 && k!=nZ-1 && k!=0)
									  {
										newData[i][j][k]=(((fi((float) (Math.round((i+dtx)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i+1][j][k]-data[i][j][k])-(fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i-2+dtx)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j][k]-data[i-1][j][k]))/(hX*hX)+
										         ((fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round((j+1)*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j+1][k]-data[i][j][k])-(fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round((j-1)*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j][k]-data[i][j-1][k]))/(hY*hY)+
										         ((fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round((k+1)*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0)))*(data[i][j][k+1]-data[i][j][k])-(fi((float) (Math.round((i-1+dtx)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0))+fi((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round((k-1)*hZ*1000)/1000.0)))*(data[i][j][k]-data[i][j][k-1]))/(hZ*hZ))*dt*0.5+
										         data[i][j][k]+F((float) (Math.round((i+dtx-1)*hX*1000)/1000.0),(float) (Math.round(j*hY*1000)/1000.0),(float) (Math.round(k*hZ*1000)/1000.0),(t+1)*dt);
									
									}else{
										newData[i][j][k]=data[i][j][k];
									}
									nI = i+rank*sz;
									String newVal = nI.toString() + " " + j.toString()
											+ " " + k.toString() + " "
											+ newData[i][j][k].toString();
									
									pw.print(newVal+"\n");
					}
			if(rank==0) 
				for (Integer j = 0; j < nY; ++j)
					for (Integer k = 0; k < nZ; ++k){
						String newVal = "0 " + j.toString()
						+ " " + k.toString() + " "
						+ data[0][j][k].toString();
						pw.print(newVal+"\n");
					}
			if(rank==size-1){
				nI = (sz*rank)+(sz+1);
				for (Integer j = 0; j < nY; ++j)
					for (Integer k = 0; k <nZ; ++k){
						String newVal = nI.toString()+" "+j.toString()
						+ " " + k.toString() + " "
						+ data[sz+1][j][k].toString();
						pw.print(newVal+"\n");
					}				 
			}
			pw.close();
	}
	 

}
