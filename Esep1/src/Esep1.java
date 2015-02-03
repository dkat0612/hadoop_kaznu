
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public class Esep1 {
/*****Block of Functions*****/
/*f1*/
	
double f1(double a)													
{
    return Math.pow(a,2.0);
}
/*f2*/
double f2(double a)
{
    return Math.pow(1-a,2.0);
}
/*Converter*/
String convertInt(int number)
{
if (number == 0) 
{
    return "0";
}
String temp="";
String returnvalue="";
int i=0;
int n=0;
while (number>0){
temp+=Integer.toString(number%10+48);
number/=10;
i++;

}

n=temp.length();
//System.out.print(temp);
char[] m =temp.toCharArray();
for (i=0;i<n;i++) 
{
      returnvalue+=m[n-i-1];
      //System.out.print("m="+m[i]);
}
return returnvalue;
}

public static void main(String args[]) throws IOException{
		String no, name;
        final int n =30;
        double[][][] P0 = new double[n][n][n];
        double[][][] P1 = new double[n][n][n];
        double[][][] S1 = new double[n][n][n];
        double[][][] S0 = new double[n][n][n];
        double S_nachw;
        double S_nacho;
        double[] x = new double[n];
        double[] y = new double[n];
        double[][][] lq  = new double[n][n][n];
	    double[][][] M   = new double[n][n][n];
        double[][][] Mx  = new double [n][n][n];
        double[][][] My  = new double[n][n][n];
        double[][][] Mz  = new double[n][n][n];
        double[][][] K1x = new double[n][n][n];
        double[][][] K2x = new double[n][n][n];
        double[][][] K1y = new double[n][n][n];
        double[][][] K2y = new double[n][n][n];
        double[][][] K1z = new double[n][n][n];
        double[][][] K2z = new double[n][n][n];
        double[][][] K1  = new double[n][n][n];
        double[][][] K2  = new double[n][n][n];
        double M1x,M2x,M3y,M4y,M5z,M6z,M71,M72;
	    double Pnag,Pdob;
	    double miu1,miu2,rc;
        double[][][] perm = new double[n][n][n];
        double[][][] permx = new double[n][n][n];
        double[][][] permy = new double[n][n][n];
        double[][][] permz = new double[n][n][n];
        double[][][] m = new double [n][n][n];
	    double shP,epsP,iterP;
	    double h,tau;
	    int    i,j,k;
	    double miuhar,mhar,rchar,Phar;
	    double nPnag,nPdob,nmiu1,nmiu2,nm,nrc;
	    double Kk1x,Kk2x,Kk3y,Kk4y,Kk5z,Kk6z;
        double[][][] q1_inj = new double[n][n][n];
        double [][][] q1_prod = new double[n][n][n];
        
        Esep1 e = new Esep1();
        try{
        FileWriter iterPout = new FileWriter("out_iterP.txt",true);
        BufferedWriter iterPoutfile = new BufferedWriter(iterPout);
        }catch (IOException ioex){
        ioex.printStackTrace();
        }
	
        /*Input Data*/
	    String fname1   = "out1"; 
        String pre = "000"; 
        String suf = ".dat"; 
        String outname;
	    String fname2   = "out2";

	epsP=0.0001;
	h=(1./n);
	tau=h*h/2;
	
	nm=0.2;		    // porosity
	nmiu1=0.15;     // viscosity of water
	nmiu2=0.09;     // viscosity of oil
	nrc=0.015;		// well radius		
	//nPpl=60;      // reservoir pressure	
	nPnag=50;
	nPdob=10;
	
	mhar=0.2; //0,65
	miuhar=0.5;
	rchar=100;
	Phar=100;
	
	/*Permeability tensor (Reading from file)*/
	for(i = 0; i < n; i++) {    
	for(j = 0; j < n; j++) {
	for(k = 0; k < n; k++) {
	permx[i][j][k]=0.1;
	permy[i][j][k]=0.1;
	permz[i][j][k]=0.1;
	perm[i][j][k]=0.1*Math.sqrt(3);}}}	
	
	for( i = 0; i < n; i++) {
	for( j = 0; j < n; j++) {
	x[i] = i*h;
	y[j] = j*h;}}
	/*END(Permeability tensor (Reading from file))*/
        
          /*Porosity*/
	for(i = 0; i < n; i++){    
	for(j = 0; j < n; j++){
	for(k = 0; k < n; k++){
	m[i][j][k]=0.2;
	}}}/*END(Porosity)*/

	/*Dimensionless form*/
	miu1=nmiu1/miuhar;
	miu2=nmiu2/miuhar;
	rc=nrc/rchar;	
	Pnag=nPnag/Phar;
	Pdob=nPdob/Phar;
	
	/************/
	

	/***************/	
	for(i=0;i<n;i++){
	for(j=0;j<n;j++){
	for(k=0;k<n;k++){
	lq[i][j][k]=0;		
	}}}
	//lq[15][15][15]=1; 
	for(k=11;k<20;k++)
	{
		lq[15][15][k]=1; 
	}

	/*Initial conditions*/
	/*Pressure*/
	for(i=0;i<n;i++){ 
	for(j=0;j<n;j++){
	for(k=0;k<n;k++){
	P0[i][j][k]=0.3;
	P1[i][j][k]=P0[i][j][k];
	}}}
	/*Saturation*/
	S_nachw=0.4;
	S_nacho=0.6;
	for(i=0;i<n;i++){
	for(j=0;j<n;j++){
	for(k=0;k<n;k++){	
	S0[i][j][k]=S_nachw;
	S1[i][j][k]=S_nachw;
	}}}

	/*END(Initial conditions)*/

        /*Computing block*/
	 int time,nT;
	    nT=2500;
        System.out.println(nT);
	
	
	/*Time layer*/
        time =0;
	//for(time=0;time<nT;time++)
	do {	
		
		if((time%1000)==0)
		{   System.out.println(time);
			
			    no = file_no(time);
				name = "P".concat(no);
				name = name.concat(".dat");
				FileOutputStream fout2 = null;
				try {
					fout2 = new FileOutputStream(name);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PrintStream out2 = new PrintStream(fout2);
				out2.println("TITLE = \"Heat equation result: 3D-Volume Data\"");
				out2.println("VARIABLES =\"X\", \"Y\", \"Z\", \"P(x,y,z)\"");
				out2.println("ZONE I=" +(n-2) + ",      J="		+ (n-2) + ", K="+ (n-2) + ",    F=Point");
				for (i = 0; i < (n-2); i++) {
					for (j = 0; j < (n-2); j++) {
						for (k = 0; k < (n-2); k++) {
							out2.println((float)(i + 1) + "      " + (float)(j + 1)
									+ "       " + (float)(k + 1) + "       "
									+ P1[i][j][k]+ "       "  + S1[i][j][k]);
						}
					}
				}

				fout2.close();
			
		}
		/*{
			Output of the Data
			if(time<10)
			{pre="000000";}
			if(time>=10 && time<100)
			{pre="00000";}
			if(time>=100 && time<1000)
			{pre="0000";}
			if(time>=1000 && time<10000)
			{pre="000";}
			if(time>=10000 && time<100000)
			{pre="00";}
			if(time>=100000)
			{pre="0";}
			äëÿ TECPLOT
			outname = fname1+pre+e.convertInt(time)+suf;
	try{
        FileWriter fstream = new FileWriter(outname,true);
        BufferedWriter outputfile = new BufferedWriter(fstream);
        	outputfile.write("TITLE=\"USERData\"\nVARIABLES=i,j,k,Pressure,Sat\n" );
                outputfile.write("ZONE T=\"ZONE1\", i="+(n-2)+" j="+(n-2)+" k="+(n-2)+" f=Point\n");
			for(i=1;i<n-1;i++){
			for(j=1;j<n-1;j++){
			for(k=1;k<n-1;k++){
			outputfile.write(i*h+"\t"+j*h+"\t"+k*h+"\t"+P1[i][j][k]+"\t"+S1[i][j][k]+"\n");
			
                        }}}
			outputfile.close();
        }
        catch(Exception ex){
            System.err.println("Error "+ex.getMessage());
        }	
		}*/
		
		/*FileOutputStream fout2 = new FileOutputStream(name);
		PrintStream out2 = new PrintStream(fout2);
		out2.println("TITLE = \"Heat equation result: 3D-Volume Data\"");
		out2.println("VARIABLES =\"X\", \"Y\",\"Z\", \"U(x,y,z)\"");
		
		out2.println("ZONE I="	+ (mathMesh.getNX())+  "   J=" + (mathMesh.getNY())  + 
				", K=" + (mathMesh.getNZ()) + ", F=POINT");
		
		for (i = 0; i <(mathMesh.getNX()); i++) {
			for (j = 0; j < (mathMesh.getNY()) ; j++) {
			for (k = 0; k < (mathMesh.getNZ()); k++) {
					out2.println((float) (i+1) + "        "+  (float)( j+1) + "        " +  (float) (k+1)+ "        " + comRes[i][j][k]);
				}}}*/

		Kk1x = 0;
                Kk2x = 0;
                Kk3y = 0;
                Kk4y = 0;
                Kk5z = 0; 
                Kk6z = 0;

		for(i=0;i<n;i++){
		for(j=0;j<n;j++){
		for(k=0;k<n;k++){
		K1x[i][j][k]=permx[i][j][k]*e.f1(S0[i][j][k])/miu1;
		K2x[i][j][k]=permx[i][j][k]*e.f2(S0[i][j][k])/miu2;
		K1y[i][j][k]=permy[i][j][k]*e.f1(S0[i][j][k])/miu1;
		K2y[i][j][k]=permy[i][j][k]*e.f2(S0[i][j][k])/miu2;
		K1z[i][j][k]=permz[i][j][k]*e.f1(S0[i][j][k])/miu1;
		K2z[i][j][k]=permz[i][j][k]*e.f2(S0[i][j][k])/miu2;
		K1[i][j][k]=perm[i][j][k]*e.f1(S0[i][j][k])/miu1;
		K2[i][j][k]=perm[i][j][k]*e.f2(S0[i][j][k])/miu2;
		Mx[i][j][k]=K1x[i][j][k]+K2x[i][j][k];				
		My[i][j][k]=K1y[i][j][k]+K2y[i][j][k];
		Mz[i][j][k]=K1z[i][j][k]+K2z[i][j][k];
		M[i][j][k]=K1[i][j][k]+K2[i][j][k];
		}}}
		
			/*Calculation of the Pressure*/
			
			iterP=0; 
			do
			{   iterP++; //cout<<iterP<<endl;
				M1x = 0; M2x = 0; M3y = 0; M4y = 0; 

				for(i=1;i<n-1;i++){
				for(j=1;j<n-1;j++){
				for(k=1;k<n-1;k++){
				M1x = 0.5 * (Mx[i + 1][j][k] + Mx[i][j][k]);
				M2x = 0.5 * (Mx[i - 1][j][k] + Mx[i][j][k]);
				M3y = 0.5 * (My[i][j + 1][k] + My[i][j][k]);
				M4y = 0.5 * (My[i][j - 1][k] + My[i][j][k]);
				M5z = 0.5 * (Mz[i][j][k + 1] + Mz[i][j][k]);
				M6z = 0.5 * (Mz[i][j][k - 1] + Mz[i][j][k]);
				M71 = h*h*2*3.14*K1[i][j][k]/Math.log(0.2*h/rc);
				M72 = h*h*2*3.14*M[i][j][k]/Math.log(0.2*h/rc);
				
				if(lq[i][j][k]==0)
				{P1[i][j][k]=(M1x*(P0[i+1][j][k])+M2x*(P0[i-1][j][k])+M3y*(P0[i][j+1][k])+M4y*(P0[i][j-1][k])+M5z*(P0[i][j][k+1])+M6z*(P0[i][j][k-1]))/(M1x+M2x+M3y+M4y+M5z+M6z);}
				else
				if(lq[i][j][k]==1)
				{P1[i][j][k]=(M1x*(P0[i+1][j][k])+M2x*(P0[i-1][j][k])+M3y*(P0[i][j+1][k])+M4y*(P0[i][j-1][k])+M5z*(P0[i][j][k+1])+M6z*(P0[i][j][k-1])+M71*Pnag)/(M1x+M2x+M3y+M4y+M5z+M6z+M71);}
				else
				if(lq[i][j][k]==2)
				{P1[i][j][k]=(M1x*(P0[i+1][j][k])+M2x*(P0[i-1][j][k])+M3y*(P0[i][j+1][k])+M4y*(P0[i][j-1][k])+M5z*(P0[i][j][k+1])+M6z*(P0[i][j][k-1])+M72*Pdob)/(M1x+M2x+M3y+M4y+M5z+M6z+M72);}
				}}}

				for(i=0;i<n;i++){
				for(j=0;j<n;j++){ 
				P1[i][j][0]=P1[i][j][1];
				P1[i][j][n-1]=P1[i][j][n-2];
				}}
				for(i=0;i<n;i++){
				for(k=0;k<n;k++){
				P1[i][0][k]=P1[i][1][k];
				P1[i][n-1][k]=P1[i][n-2][k];
				}}
				for(j=0;j<n;j++){
				for(k=0;k<n;k++){ 
				P1[0][j][k]=P1[1][j][k];
				P1[n-1][j][k]=P1[n-2][j][k];
				}}

				shP=0;
				for(i=1;i<n;i++){
				for(j=1;j<n;j++){
				for(k=1;k<n;k++){
				if(shP<Math.abs(P1[i][j][k]-P0[i][j][k]))
				shP=Math.abs(P1[i][j][k]-P0[i][j][k]);}}}
				
				for(i=0;i<n;i++){
				for(j=0;j<n;j++){
				for(k=0;k<n;k++){
				P0[i][j][k]=P1[i][j][k];}}}
			}   while(epsP<shP);
			
                      



			/*Calculation of the Saturation */
		for(i=1;i<n-1;i++){
		for(j=1;j<n-1;j++){
		for(k=1;k<n-1;k++){
		Kk1x = 0.5 * (K1x[i + 1][j][k] + K1x[i][j][k]);
		Kk2x = 0.5 * (K1x[i - 1][j][k] + K1x[i][j][k]);
		Kk3y = 0.5 * (K1y[i][j + 1][k] + K1y[i][j][k]);
		Kk4y = 0.5 * (K1y[i][j - 1][k] + K1y[i][j][k]);
		Kk5z = 0.5 * (K1z[i][j][k + 1] + K1z[i][j][k]);
		Kk6z = 0.5 * (K1z[i][j][k - 1] + K1z[i][j][k]);
		q1_inj[i][j][k] =-(P1[i][j][k]-Pnag)*2*3.14*K1[i][j][k]/Math.log(0.2*h/rc);
		q1_prod[i][j][k]=-(P1[i][j][k]-Pdob)*2*3.14*K1[i][j][k]/Math.log(0.2*h/rc);
				
		if (lq[i][j][k]==1)
		{S1[i][j][k]=S0[i][j][k]+(tau/(m[i][j][k]*h*h))*(h*h*q1_inj[i][j][k]+Kk1x*(P1[i+1][j][k]-P1[i][j][k])-Kk2x*(P1[i][j][k]-P1[i-1][j][k])+Kk3y*(P1[i][j+1][k]-P1[i][j][k])-Kk4y*(P1[i][j][k]-P1[i][j-1][k])+Kk5z*(P1[i][j][k+1]-P1[i][j][k])-Kk6z*(P1[i][j][k]-P1[i][j][k-1]));}
		else
		if (lq[i][j][k]==2)
		{S1[i][j][k]=S0[i][j][k]+(tau/(m[i][j][k]*h*h))*(h*h*q1_prod[i][j][k]+Kk1x*(P1[i+1][j][k]-P1[i][j][k])-Kk2x*(P1[i][j][k]-P1[i-1][j][k])+Kk3y*(P1[i][j+1][k]-P1[i][j][k])-Kk4y*(P1[i][j][k]-P1[i][j-1][k])+Kk5z*(P1[i][j][k+1]-P1[i][j][k])-Kk6z*(P1[i][j][k]-P1[i][j][k-1]));}
		else
		{S1[i][j][k]=S0[i][j][k]+(tau/(m[i][j][k]*h*h))*(Kk1x*(P1[i+1][j][k]-P1[i][j][k])-Kk2x*(P1[i][j][k]-P1[i-1][j][k])+Kk3y*(P1[i][j+1][k]-P1[i][j][k])-Kk4y*(P1[i][j][k]-P1[i][j-1][k])+Kk5z*(P1[i][j][k+1]-P1[i][j][k])-Kk6z*(P1[i][j][k]-P1[i][j][k-1]));}
		
		}}}

		for(i=0;i<n;i++){
		for(j=0;j<n;j++){
		S1[i][j][0]=S1[i][j][1];
		S1[i][j][n-1]=S1[i][j][n-2];
		}}
		for(i=0;i<n;i++){
		for(k=0;k<n;k++){
		S1[i][0][k]=S1[i][1][k];
		S1[i][n-1][k]=S1[i][n-2][k];
		}}
		for(j=0;j<n;j++){
		for(k=0;k<n;k++){
		S1[0][j][k]=S1[1][j][k];
		S1[n-1][j][k]=S1[n-2][j][k];
		}}

		for(i=0;i<n;i++){
		for(j=0;j<n;j++){ 	
		for(k=0;k<n;k++){ 	
		S0[i][j][k]=S1[i][j][k];}}}
		
		time ++;
		
	    }while(time<nT);/*END(Time layer)*/
 } 
public static String file_no(int ii) {
	char[] c = new char[10];
	String c1, no;
	c[0] = '0';
	c[1] = '1';
	c[2] = '2';
	c[3] = '3';
	c[4] = '4';
	c[5] = '5';
	c[6] = '6';
	c[7] = '7';
	c[8] = '8';
	c[9] = '9';
	if (ii < 10) {
		no = "000";
		c1 = Character.toString(c[ii]);
		no = no.concat(c1);
	} else if (ii < 100) {
		no = "00";
		c1 = Character.toString(c[ii / 10]);
		no = no.concat(c1);
		c1 = Character.toString(c[ii % 10]);
		no = no.concat(c1);
	} else if (ii < 1000) {
		no = "0";
		c1 = Character.toString(c[ii / 100]);
		no = no.concat(c1);
		c1 = Character.toString(c[(ii % 100) / 10]);
		no = no.concat(c1);
		c1 = Character.toString(c[ii % 10]);
		no = no.concat(c1);
	} else if(ii<10000){
		no = "00";
		no = Character.toString(c[ii / 1000]);
		c1 = Character.toString(c[(ii % 1000) / 100]);
		no = no.concat(c1);
		c1 = Character.toString(c[(ii % 100) / 10]);
		no = no.concat(c1);
		c1 = Character.toString(c[ii % 10]);
		no = no.concat(c1);
	}
	else if(ii<100000) {
		no = "0";
		no = Character.toString(c[ii / 10000]);
		c1 = Character.toString(c[(ii % 10000) / 1000]);
		no = no.concat(c1);
		c1 = Character.toString(c[(ii % 1000) / 100]);
		no = no.concat(c1);
		c1 = Character.toString(c[(ii % 100)/10]);
		no = no.concat(c1);
		c1 = Character.toString(c[ii % 10]);
		no = no.concat(c1);
	} else {
	no = Character.toString(c[ii / 100000]);
	c1 = Character.toString(c[(ii % 100000) / 10000]);
	no = no.concat(c1);
	c1 = Character.toString(c[(ii % 10000) / 1000]);
	no = no.concat(c1);
	c1 = Character.toString(c[(ii % 1000)/100]);
	no = no.concat(c1);
	c1 = Character.toString(c[(ii % 100)/10]);
	no = no.concat(c1);
	c1 = Character.toString(c[ii % 10]);
	no = no.concat(c1);
	}
	return no;
}

}
