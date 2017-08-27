package com.LinpackJava;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;  
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;  
import android.widget.Toast;
import android.widget.TextView;  
import android.widget.EditText;  
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.graphics.Typeface;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.Context;
import android.text.Editable;
import android.content.Intent;
import android.content.DialogInterface;      
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import static java.lang.Math.abs;
import android.graphics.Color;
import android.os.Build;

public class LinpackJavaActivity extends Activity implements View.OnClickListener
{
    private String version = " Android Java Linpack Benchmark 1.2 ";
    private Button mStartButton;  
    private Button mInfoButton;  
    private Button mMailButton;  
    private Handler _myHandler;  
    private Handler _myHandler2;  
    private TextView mDisplayDetails; 
    private Runnable _myTask;  
    private Runnable _myTask2;  
    private String[] xout = new String[20];
    private String[] sout = new String[20];
    private String[] iout = new String[20];
    private String x0;
    private String date$;

    private static double TimeUsed;
    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static int section;  
    private static int pixWide;  
    private static int pixHigh;    
    private static int testDone = 0; 
    private String[] args = new String[2];
    private int part;

    private static double ONE  = 1.0e0;
    private static double ZERO = 0.0;
    private static int NTIMES = 10;
    private static double startSecs;
    private static double  secs;

    private Typeface tf = Typeface.create("monospace", 0);
        private Context show;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main); 
        mStartButton = (Button)findViewById(R.id.startButton);  
        mInfoButton = (Button)findViewById(R.id.infoButton);  
        mMailButton = (Button)findViewById(R.id.mailButton);  

       
        mStartButton.setOnClickListener(this);  
        mInfoButton.setOnClickListener(this);
        mMailButton.setOnClickListener(this);

        clear();
        x0 = " Not run yet\n";
        testDone = 0;

        mDisplayDetails = (TextView)findViewById(R.id.displayDetails);  
        DisplayMetrics metrics = new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(metrics); 
        mDisplayDetails.setTypeface(tf);
        
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);

        part = 0;
        args[0] = "/system/bin/cat";
                args[1] = "/proc/cpuinfo";
                ReadCPUinfo();
                part = 1;
                args[1] = "/proc/version";
                ReadCPUinfo();
                        
        pixHigh = metrics.heightPixels;
        pixWide = metrics.widthPixels;
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        sout[0]   = "\n System Information\n";
        sout[1]   = " Device " + getDeviceName() + "\n";
        sout[2] = " Screen pixels w x h "
                + String.format("%d", pixWide)
                + " x "
                + String.format("%d", pixHigh)
                + " \n";
        sout[3] = "";
        sout[4]   = " Android Build Version      "
                        + AndroidVersion
                        + "\n";
        sout[5]  = "\n";
        sout[6]  = iout[0].trim() + "\n";
        sout[7]  = "\n";
        sout[8]  = iout[1].trim() + "\n";;
        sout[9]  = "\n";
        sout[10]  = "\n";
        sout[11]  = "\n";
       
        View title = getWindow().findViewById(android.R.id.title);
        View titleBar = (View) title.getParent();
        titleBar.setBackgroundColor(Color.rgb(0,0,255));
        getWindow().getDecorView().setBackgroundResource(R.drawable.bground);
        mDisplayDetails.setTextColor(Color.rgb(0,0,255));

        int pixels = 11;
        int pixMin = pixWide;
        if (pixHigh < pixMin) pixMin = pixHigh;
        if (pixHigh < 320)
        {
            xout[0]   = version + date$ + "\n";
            xout[1]   = "\n";
            xout[2]   = sout[2];
            xout[3]   = " At least 320 pixels high needed\n";
            xout[4]   = "\n";
            xout[5]   = "\n";
            xout[6]   = "\n";
            xout[7]   = "\n";
            xout[8]   = "\n";
            xout[9]   = "\n";
            xout[10]  = "\n";
            xout[11]  = "\n";
            xout[12]  = "\n";
            xout[13]  = "\n";
            xout[14]  = "\n";
            xout[15]  = "\n";
            xout[16]  = "\n";
        }
        else
        {
             if (pixMin < 401)
             {
                pixels = 12;
             }
             else if (pixMin < 451)
             {
                 pixels = 14;
             }
             else if (pixMin < 501)
             {
                 pixels = 15;
             }
             else if (pixMin < 551)
             {
                 pixels = 16;
             }
             else if (pixMin < 601)
             {
                 pixels = 18;                    
             }
             else if (pixMin < 651)
             {
                 pixels = 20;
             }
             else if (pixMin < 721)
             {
                 pixels = 22;                    
             }
             else if (pixMin < 751)
             {
                 pixels = 23;
             }
             else if (pixMin < 801)
             {
                 pixels = 24;                    
             }
             else if (pixMin < 851)
             {
                 pixels = 26;
             }
             else if (pixMin < 901)
             {
                 pixels = 28;                    
             }
             else if (pixMin < 951)
             {
                 pixels = 30;
             }
             else
             {
                 pixels = 32;                    
             }
             clear();                
        }

        mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels); 
        displayAll();

    }      

    public String getDeviceName() 
    {
       String manufacturer = Build.MANUFACTURER;
       String model = Build.MODEL;
       if (model.startsWith(manufacturer)) {
          return capitalize(model);
       } else {
          return capitalize(manufacturer) + " " + model;
       }
    }
    
    
    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private void ReadCPUinfo() 
    {
            ProcessBuilder cmd;  
            try
            {
                    cmd = new ProcessBuilder(args);
                    Process process = cmd.start();
                    InputStream in = process.getInputStream();
                    byte[] re = new byte[1024];
                    while(in.read(re) != -1)
                    {
                            System.out.println(new String(re));
                            iout[part] = new String(re);
                    }
                    in.close();
            } 
            catch(IOException ex)
            {
            Toast.makeText(getApplicationContext(),"Cannot Read System Information",Toast.LENGTH_LONG).show();
            }
    }

    public void onClick(View v) 
    {  
        if(v.getId() == R.id.startButton)
        {
             clear();
            _myTask = new myLinpTask();  
            _myHandler = new Handler();  
            xout[15] = " Running - Time 5 to 10 seconds\n";
            xout[16] = "\n"; 
            displayAll();  
            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            _myHandler.postDelayed(_myTask, 100);
 
        }  
        else if(v.getId() == R.id.infoButton)
        {
                showDialog(8);          
        }
        else if(v.getId() == R.id.mailButton)
        {
                if (testDone == 1)
                {
                        showDialog(9);
                }
                else
                {
                        Toast.makeText(getApplicationContext(),"No Results To Send",Toast.LENGTH_LONG).show();
                }
        }
   }

        @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
          case 8:
            Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Details and Results Web Pages");
            builder.setCancelable(true);
            builder.setPositiveButton("About Summary", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    mDisplayDetails.setText(
                           " The Linpack Benchmark was produced by Jack Dongarra  \n" + 
                          " from a package of linear algebra routines. It became\n" + 
                          " the primary benchmark for scientific computers from\n" + 
                          " the mid 1980's with a slant towards supercomputer\n" + 
                          " performance. The original double precision C version, \n" + 
                          " used here, operates on 100x100 matrices. Performance\n" + 
                          " is governed by an inner loop in function daxpy with\n" + 
                          " a linked triad dy[i] = dy[i] + da * dx[i], and is\n" +
                          " measured in Millions of Floating Point Operations\n" +
                          " Per Second (MFLOPS). Two versions use a Java front\n" +
                          " end with the main code compiled by Android Native\n" +
                          " Development Kit. A third comprises all Java code.\n" +
                          " Linpackv5.apk, using  old, slow instructions,\n" +
                          " Linpackv7.apk to use use faster vfpv3 hardware and\n" +
                          " LinpackJava.apk depending on a Runtime Environment.\n" +
                          " \n" +
                          " Roy Longbottom 2013\n");                  
                          resetTest();
                          testDone = 0;
                }
            });
 
            builder.setNeutralButton("My Android Benchmarks", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                   Uri uri = Uri.parse("http://www.roylongbottom.org.uk/android benchmarks.htm"); 
                   startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    Toast.makeText(getApplicationContext(),"Loading HTML",Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Linpack Results on PCs", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                   Uri uri = Uri.parse("http://www.roylongbottom.org.uk/linpack%20results.htm"); 
                   startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    Toast.makeText(getApplicationContext(),"Loading HTML",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            break;
 
          case 9:
              Builder builder2 = new AlertDialog.Builder(this);
              builder2.setMessage("Add note to results?");
                  final EditText input = new EditText(this);
                  builder2.setView(input);
                          
              builder2.setCancelable(true);
              builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int which) 
                  {
                    Editable value = input.getText();
                        sout[10] = " Note " + value.toString();
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        String[] recipients = new String[]{"results@roylongbottom.org.uk", "",};
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Java Linpack Benchmark Results");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                                        xout[0]  + xout[1]  + x0 +
                                        sout[0]  + sout[1]  + sout[2]  + sout[3]  +
                            sout[4]  + sout[5]  + sout[6]  + sout[7]  + 
                            sout[8]  + sout[9]  + sout[10] + sout[11]);
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                  }
              });
              builder2.show();
                  break;
            
        }
        return super.onCreateDialog(id);
    } 
        
    private void displayAll()
    {
        if (testDone == 0)
        {
            mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                                        xout[4]  + xout[5]  + xout[6]  + xout[7]  + 
                                        xout[8]  + xout[9]  + xout[10] + xout[11] + 
                                        xout[12] + xout[13] + xout[14] + xout[15] + 
                                        xout[16]);
         }
         else
         {
             mDisplayDetails.setText(xout[0]  + xout[1]  + x0 + xout[10] + xout[11] + 
                                    xout[12] + xout[13] + xout[14] + xout[15] + xout[16]);
         }
      }
      
    private void resetTest()  
    {  
        if(_myHandler != null)
        {  
            _myHandler.removeCallbacks(_myTask);  
        }  
        if(_myHandler2 != null)
        {  
                _myHandler2.removeCallbacks(_myTask2);  
        }  
    }  

    private class myLinpTask implements Runnable  
    {  
       
        public void run() 
        { 
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = version + date$ + "\n";

            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;

            linpack();

            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            xout[15] = " Total Elapsed Time  " + String.format("%5.1f", testTime)
                    + " seconds\n";
            xout[14] = "\n";
            xout[16] = "\n";
            testDone = 1;
            displayAll();
            resetTest();          
        }  
    }

    public void clear()
    {
        xout[0]  = version + date$ + "\n";
        xout[1]  = "\n";
        xout[2]  = " Measures speed in MFLOPS and shows results:\n";
        xout[3]  = "\n";
        xout[4]  = " norm. resid\n";
        xout[5]  = " resid\n";
        xout[6]  = " machep\n";
        xout[7]  = " x[0]-1\n";
        xout[8]  = " x[n-1]-1\n";
        xout[9]  = "\n";
        xout[10] = "\n";
        xout[11] = "\n";
        xout[12] = "\n";
        xout[13] = "\n";
        xout[14] = "\n";
        xout[15] = "\n";
        xout[16] = "\n";
        testDone = 0;       
    }
    @Override
    protected void onSaveInstanceState(Bundle outState)  
    {
      super.onSaveInstanceState(outState);
      outState.putString("displayData", (String)mDisplayDetails.getText());
    }

    @Override 
    public void onRestoreInstanceState(Bundle savedInstanceState) 
    {
        super.onRestoreInstanceState(savedInstanceState); 
        mDisplayDetails.setText(savedInstanceState.getString("displayData"));
    }  

    public void linpack()
    {
        double runSecs = 0.5;
        double[][] atime = new double[9][15];
        double [][] a = new double[500][501];             
        double[] b = new double[500];             
        double[] x = new double[500];             
        double cray,ops,total,norma,normx;
        double resid,residn,eps,tm2,epsn,x1,x2;
        double mflops;
        int[] ipvt = new int[500];
        int n,i,j,ntimes,info,lda,ldaa;
        int endit, pass, loop;
        double overhead1, overhead2, time2;
        double max1, max2;
        char[] resultchars = new char[1000];

        lda = 201;
        ldaa = 200;
        cray = .056; 
        n = 100;

        ops = (2.0e0*(n*n*n))/3.0 + 2.0*(n*n);
 
        norma = matgen(a,lda,n,b);
        start_time();
        info = dgefa(a,lda,n,ipvt);
        end_time();
        atime[0][0] = secs;
        start_time();
         dgesl(a,lda,n,ipvt,b,0);
        end_time();
        atime[1][0] = secs;
        total = atime[0][0] + atime[1][0];

//      compute a residual to verify results. 

        for (i = 0; i < n; i++) 
        {
                x[i] = b[i];
        }

        norma = matgen(a,lda,n,b);
        for (i = 0; i < n; i++) {
                b[i] = -b[i];
        }
        dmxpy(n,b,n,lda,x,a);
        resid = 0.0;
        normx = 0.0;
        for (i = 0; i < n; i++) 
        {
            resid = (resid > abs(b[i])) ? resid : abs(b[i]);
            normx = (normx > abs(x[i])) ? normx : abs(x[i]);
         }
        
        eps = epslon(ONE);
        residn = resid/( n*norma*normx*eps );
        epsn = eps;
        x1 = x[0] - 1;
        x2 = x[n-1] - 1;
        atime[2][0] = total;
        if (total > 0.0)
        {
            atime[3][0] = ops/(1.0e6*total);
            atime[4][0] = 2.0/atime[3][0];
        }
        else
        {
            atime[3][0] = 0.0;
            atime[4][0] = 0.0;
        }
        atime[5][0] = total/cray;

     // ************************************************************************
     // *       Calculate overhead of executing matgen procedure              *
     // ************************************************************************
            
             pass = -20;
             loop = NTIMES;
             do
             {
                 start_time();
                 pass = pass + 1;        
                 for ( i = 0 ; i < loop ; i++)
                 {
                     norma = matgen(a,lda,n,b);
                 }
                 end_time();
                 overhead1 = secs;
                 if (overhead1 > runSecs)
                 {
                     pass = 0;
                 }
                 if (pass < 0)
                 {
                     if (overhead1 < 0.1)
                     {
                         loop = loop * 10;
                     }
                     else
                     {
                         loop = loop * 2;
                     }
                 }
             }
             while (pass < 0);
             
             overhead1 = overhead1 / (double)loop;

          // ************************************************************************
          // *           Calculate matgen/dgefa passes for runSecs seconds                *
          // ************************************************************************
                 
                  pass = -20;
                  ntimes = NTIMES;
                  do
                  {
                      start_time();
                      pass = pass + 1;        
                      for ( i = 0 ; i < ntimes ; i++)
                      {
                          norma = matgen(a,lda,n,b);
                          info = dgefa(a,lda,n,ipvt);
                      }
                      end_time();
                      time2 = secs;
                      if (time2 > runSecs)
                      {
                          pass = 0;
                      }
                      if (pass < 0)
                      {
                          if (time2 < 0.1)
                          {
                              ntimes = ntimes * 10;
                          }
                          else
                          {
                              ntimes = ntimes * 2;
                          }
                      }
                  }
                  while (pass < 0);
                  
                  ntimes =  (int)(runSecs * (double)ntimes / time2);
                  if (ntimes == 0) ntimes = 1;

               // ************************************************************************
               // *                              Execute 5 passes                        *
               // ************************************************************************
                     
                       tm2 = ntimes * overhead1;
                       atime[3][6] = 0;

                       for (j=1 ; j<6 ; j++)
                       {
                           start_time();
                           for (i = 0; i < ntimes; i++)
                           {
                               norma = matgen(a,lda,n,b);
                               info = dgefa(a,lda,n,ipvt);
                           }
                           end_time();
                           atime[0][j] = (secs - tm2)/ntimes;

                           start_time();              
                           for (i = 0; i < ntimes; i++)
                           {
                               dgesl(a,lda,n,ipvt,b,0);
                           }
                           end_time();

                           atime[1][j] = secs/ntimes;
                           total       = atime[0][j] + atime[1][j];
                           atime[2][j] = total;
                           atime[3][j] = ops/(1.0e6*total);
                           atime[4][j] = 2.0/atime[3][j];
                           atime[5][j] = total/cray;
                           atime[3][6] = atime[3][6] + atime[3][j];
                           
                       }
                       atime[3][6] = atime[3][6] / 5.0;

                       
                    // ************************************************************************
                    // *             Calculate overhead of executing matgen procedure         *
                    // ************************************************************************

                            start_time();        
                            for ( i = 0 ; i < loop ; i++)
                            {
                                norma = matgen(a,ldaa,n,b);    
                            }
                            end_time();
                            overhead2 = secs;
                            overhead2 = overhead2 / (double)loop;
                            
                         // ************************************************************************
                         // *                              Execute 5 passes                        *
                         // ************************************************************************
                                       
                                 tm2 = ntimes * overhead2;
                                 atime[3][12] = 0;

                                 for (j=7 ; j<12 ; j++)
                                 {
                                     start_time();
                                     for (i = 0; i < ntimes; i++)
                                     {
                                         norma = matgen(a,ldaa,n,b);
                                         info = dgefa(a,ldaa,n,ipvt);
                                     }
                                     end_time();
                                     atime[0][j] = (secs - tm2)/ntimes;
                                     
                                     start_time();      
                                     for (i = 0; i < ntimes; i++)
                                     {
                                         dgesl(a,ldaa,n,ipvt,b,0);
                                     }
                                     end_time();
                                     atime[1][j] = secs/ntimes;
                                     total       = atime[0][j] + atime[1][j];
                                     atime[2][j] = total;
                                     atime[3][j] = ops/(1.0e6*total);
                                     atime[4][j] = 2.0/atime[3][j];
                                     atime[5][j] = total/cray;
                                     atime[3][12] = atime[3][12] + atime[3][j];

                                 }
                                 atime[3][12] = atime[3][12] / 5.0; 

                              // ************************************************************************
                              // *           Use minimum average as overall Mflops rating               *
                              // ************************************************************************
                                    
                                      mflops = atime[3][6];
                                      if (atime[3][12] < mflops) mflops = atime[3][12];


                              // ************************************************************************
                              // *              Add results to output file Linpack.txt                  *
                              // ************************************************************************

                                  max1 = 0;
                                  for (i=1 ; i<6 ; i++)
                                  {
                                      if (atime[3][i] > max1) max1 = atime[3][i];                 
                                  }

                                  max2 = 0;
                                  for (i=7 ; i<12 ; i++)
                                  {                 
                                      if (atime[3][i] > max2) max2 = atime[3][i];                 
                                  }
                                  if (max1 < max2) max2 = max1;

      
        x0 = " Speed          " + String.format("%10.2f", max2) + " MFLOPS\n\n" +
             " norm. resid    " + String.format("%16.2f\n", (double)residn) +
             " resid          " + String.format("%16.8e\n", (double)resid) +
             " machep         " + String.format("%16.8e\n", (double)epsn) +
             " x[0]-1         " + String.format("%16.8e\n", (double)x1) +
             " x[n-1]-1       " + String.format("%16.8e\n\n", (double)x2);
    }

    public void start_time()
    {
      startSecs = (double)System.currentTimeMillis();
      return;
    }
    
    public void end_time()
    {
      secs = ((double)System.currentTimeMillis() - startSecs) / 1000;
      return;
    }

  final double matgen (double a[][], int lda, int n, double b[])
  {
    double norma;
    int init, i, j;
    
    init = 1325;
    norma = 0.0;
    for (j = 0; j < n; j++) {
      for (i = 0; i < n; i++) {
        init = 3125*init % 65536;
        a[j][i] = (init - 32768.0)/16384.0;
        norma = (a[j][i] > norma) ? a[j][i] : norma;
      }
    }
    for (i = 0; i < n; i++) {
      b[i] = 0.0;
    }
    for (j = 0; j < n; j++) {
      for (i = 0; i < n; i++) {
        b[i] += a[j][i];
      }
    }
    
    return norma;
  }
  

  
  /*
    dgefa factors a double precision matrix by gaussian elimination.
    
    dgefa is usually called by dgeco, but it can be called
    directly with a saving in time if  rcond  is not needed.
    (time for dgeco) = (1 + 9/n)*(time for dgefa) .
    
    on entry
    
    a       double precision[n][lda]
    the matrix to be factored.
    
    lda     integer
    the leading dimension of the array  a .
    
    n       integer
    the order of the matrix  a .
    
    on return
    
    a       an upper triangular matrix and the multipliers
    which were used to obtain it.
    the factorization can be written  a = l*u  where
    l  is a product of permutation and unit lower
    triangular matrices and  u  is upper triangular.
    
    ipvt    integer[n]
    an integer vector of pivot indices.
    
    info    integer
    = 0  normal value.
    = k  if  u[k][k] .eq. 0.0 .  this is not an error
    condition for this subroutine, but it does
    indicate that dgesl or dgedi will divide by zero
    if called.  use  rcond  in dgeco for a reliable
    indication of singularity.
    
    linpack. this version dated 08/14/78.
    cleve moler, university of new mexico, argonne national lab.
    
    functions
    
    blas daxpy,dscal,idamax
  */
  final int dgefa( double a[][], int lda, int n, int ipvt[])
  {
    double[] col_k, col_j;
    double t;
    int j,k,kp1,l,nm1;
    int info;
    
    // gaussian elimination with partial pivoting
    
    info = 0;
    nm1 = n - 1;
    if (nm1 >=  0) {
      for (k = 0; k < nm1; k++) {
        col_k = a[k];
        kp1 = k + 1;
        
        // find l = pivot index
        
        l = idamax(n-k,col_k,k,1) + k;
        ipvt[k] = l;
        
        // zero pivot implies this column already triangularized
        
        if (col_k[l] != 0) {
          
          // interchange if necessary
          
          if (l != k) {
            t = col_k[l];
            col_k[l] = col_k[k];
            col_k[k] = t;
          }
          
          // compute multipliers
          
          t = -1.0/col_k[k];
          dscal(n-(kp1),t,col_k,kp1,1);
          
          // row elimination with column indexing
          
          for (j = kp1; j < n; j++) {
            col_j = a[j];
            t = col_j[l];
            if (l != k) {
              col_j[l] = col_j[k];
              col_j[k] = t;
            }
            daxpy(n-(kp1),t,col_k,kp1,1,
                  col_j,kp1,1);
          }
        }
        else {
          info = k;
        }
      }
    }
    ipvt[n-1] = n-1;
    if (a[(n-1)][(n-1)] == 0) info = n-1;
    
    return info;
  }

  
  
  /*
    dgesl solves the double precision system
    a * x = b  or  trans(a) * x = b
    using the factors computed by dgeco or dgefa.
  
    on entry
  
    a       double precision[n][lda]
    the output from dgeco or dgefa.
  
    lda     integer
    the leading dimension of the array  a .
    
    n       integer
    the order of the matrix  a .
  
    ipvt    integer[n]
    the pivot vector from dgeco or dgefa.

    b       double precision[n]
    the right hand side vector.
    
    job     integer
    = 0         to solve  a*x = b ,
    = nonzero   to solve  trans(a)*x = b  where
    trans(a)  is the transpose.
    
    on return
    
    b       the solution vector  x .
    
    error condition
    
    a division by zero will occur if the input factor contains a
    zero on the diagonal.  technically this indicates singularity
    but it is often caused by improper arguments or improper
    setting of lda .  it will not occur if the subroutines are
    called correctly and if dgeco has set rcond .gt. 0.0
    or dgefa has set info .eq. 0 .
    
    to compute  inverse(a) * c  where  c  is a matrix
    with  p  columns
    dgeco(a,lda,n,ipvt,rcond,z)
    if (!rcond is too small){
    for (j=0,j<p,j++)
    dgesl(a,lda,n,ipvt,c[j][0],0);
    }
    
    linpack. this version dated 08/14/78 .
    cleve moler, university of new mexico, argonne national lab.
    
    functions
    
    blas daxpy,ddot
  */
  final void dgesl( double a[][], int lda, int n, int ipvt[], double b[], int job)
  {
    double t;
    int k,kb,l,nm1,kp1;

    nm1 = n - 1;
    if (job == 0) {

      // job = 0 , solve  a * x = b.  first solve  l*y = b

      if (nm1 >= 1) {
        for (k = 0; k < nm1; k++) {
          l = ipvt[k];
          t = b[l];
          if (l != k){
            b[l] = b[k];
            b[k] = t;
          }
          kp1 = k + 1;
          daxpy(n-(kp1),t,a[k],kp1,1,b,kp1,1);
        }
      }

      // now solve  u*x = y

      for (kb = 0; kb < n; kb++) {
        k = n - (kb + 1);
        b[k] /= a[k][k];
        t = -b[k];
        daxpy(k,t,a[k],0,1,b,0,1);
      }
    }
    else {

      // job = nonzero, solve  trans(a) * x = b.  first solve  trans(u)*y = b

      for (k = 0; k < n; k++) {
        t = ddot(k,a[k],0,1,b,0,1);
        b[k] = (b[k] - t)/a[k][k];
      }

      // now solve trans(l)*x = y 

      if (nm1 >= 1) {
        for (kb = 1; kb < nm1; kb++) {
          k = n - (kb+1);
          kp1 = k + 1;
          b[k] += ddot(n-(kp1),a[k],kp1,1,b,kp1,1);
          l = ipvt[k];
          if (l != k) {
            t = b[l];
            b[l] = b[k];
            b[k] = t;
          }
        }
      }
    }
  }



  /*
    constant times a vector plus a vector.
    jack dongarra, linpack, 3/11/78.
  */
  final void daxpy( int n, double da, double dx[], int dx_off, int incx,
              double dy[], int dy_off, int incy)
  {
    int i,ix,iy;

    if ((n > 0) && (da != 0)) {
      if (incx != 1 || incy != 1) {

        // code for unequal increments or equal increments not equal to 1

        ix = 0;
        iy = 0;
        if (incx < 0) ix = (-n+1)*incx;
        if (incy < 0) iy = (-n+1)*incy;
        for (i = 0;i < n; i++) {
          dy[iy +dy_off] += da*dx[ix +dx_off];
          ix += incx;
          iy += incy;
        }
        return;
      } else {

        // code for both increments equal to 1

        for (i=0; i < n; i++)
          dy[i +dy_off] += da*dx[i +dx_off];
      }
    }
  }



  /*
    forms the dot product of two vectors.
    jack dongarra, linpack, 3/11/78.
  */
  final double ddot( int n, double dx[], int dx_off, int incx, double dy[],
               int dy_off, int incy)
  {
    double dtemp;
    int i,ix,iy;

    dtemp = 0;

    if (n > 0) {
      
      if (incx != 1 || incy != 1) {

        // code for unequal increments or equal increments not equal to 1

        ix = 0;
        iy = 0;
        if (incx < 0) ix = (-n+1)*incx;
        if (incy < 0) iy = (-n+1)*incy;
        for (i = 0;i < n; i++) {
          dtemp += dx[ix +dx_off]*dy[iy +dy_off];
          ix += incx;
          iy += incy;
        }
      } else {

        // code for both increments equal to 1
        
        for (i=0;i < n; i++)
          dtemp += dx[i +dx_off]*dy[i +dy_off];
      }
    }
    return(dtemp);
  }

  
  
  /*
    scales a vector by a constant.
    jack dongarra, linpack, 3/11/78.
  */
  final void dscal( int n, double da, double dx[], int dx_off, int incx)
  {
    int i,nincx;

    if (n > 0) {
      if (incx != 1) {

        // code for increment not equal to 1

        nincx = n*incx;
        for (i = 0; i < nincx; i += incx)
          dx[i +dx_off] *= da;
      } else {

        // code for increment equal to 1

        for (i = 0; i < n; i++)
          dx[i +dx_off] *= da;
      }
    }
  }

  
  
  /*
    finds the index of element having max. absolute value.
    jack dongarra, linpack, 3/11/78.
  */
  final int idamax( int n, double dx[], int dx_off, int incx)
  {
    double dmax, dtemp;
    int i, ix, itemp=0;

    if (n < 1) {
      itemp = -1;
    } else if (n ==1) {
      itemp = 0;
    } else if (incx != 1) {

      // code for increment not equal to 1

      dmax = abs(dx[0 +dx_off]);
      ix = 1 + incx;
      for (i = 1; i < n; i++) {
        dtemp = abs(dx[ix + dx_off]);
        if (dtemp > dmax)  {
          itemp = i;
          dmax = dtemp;
        }
        ix += incx;
      }
    } else {

      // code for increment equal to 1

      itemp = 0;
      dmax = abs(dx[0 +dx_off]);
      for (i = 1; i < n; i++) {
        dtemp = abs(dx[i + dx_off]);
        if (dtemp > dmax) {
          itemp = i;
          dmax = dtemp;
        }
      }
    }
    return (itemp);
  }


  
  /*
    estimate unit roundoff in quantities of size x.
    
    this program should function properly on all systems
    satisfying the following two assumptions,
    1.  the base used in representing dfloating point
    numbers is not a power of three.
    2.  the quantity  a  in statement 10 is represented to
    the accuracy used in dfloating point variables
    that are stored in memory.
    the statement number 10 and the go to 10 are intended to
    force optimizing compilers to generate code satisfying
    assumption 2.
    under these assumptions, it should be true that,
    a  is not exactly equal to four-thirds,
    b  has a zero for its last bit or digit,
    c  is not exactly equal to one,
    eps  measures the separation of 1.0 from
    the next larger dfloating point number.
    the developers of eispack would appreciate being informed
    about any systems where these assumptions do not hold.
    
    *****************************************************************
    this routine is one of the auxiliary routines used by eispack iii
    to avoid machine dependencies.
    *****************************************************************
  
    this version dated 4/6/83.
  */
  final double epslon (double x)
  {
    double a,b,c,eps;

    a = 4.0e0/3.0e0;
    eps = 0;
    while (eps == 0) {
      b = a - 1.0;
      c = b + b + b;
      eps = abs(c-1.0);
    }
    return(eps*abs(x));
  }

  

  /*
    purpose:
    multiply matrix m times vector x and add the result to vector y.
    
    parameters:
    
    n1 integer, number of elements in vector y, and number of rows in
    matrix m
    
    y double [n1], vector of length n1 to which is added
    the product m*x
    
    n2 integer, number of elements in vector x, and number of columns
    in matrix m
    
    ldm integer, leading dimension of array m
    
    x double [n2], vector of length n2
    
    m double [ldm][n2], matrix of n1 rows and n2 columns
  */

  final void dmxpy ( int n1, double y[], int n2, int ldm, double x[], double m[][])
  {
    int j,i;

    // cleanup odd vector
    for (j = 0; j < n2; j++) {
      for (i = 0; i < n1; i++) {
        y[i] += x[j]*m[j][i];
      }
    }
  }

  
 
}









