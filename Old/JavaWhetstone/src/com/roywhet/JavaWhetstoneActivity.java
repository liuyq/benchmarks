package com.roywhet;

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
import android.graphics.Color;
import android.os.Build;

public class JavaWhetstoneActivity  extends Activity implements View.OnClickListener
{
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
    private String date$;

    private static String[] titles = new String[9];
    private static float[] results = new float[9];
    private static double[] loop_time = new double[9];
    private static double[] loop_mops = new double[9];
    private static double[] loop_mflops = new double[9];
    private static String[] mops = new String[9];
    private static String[] mflops = new String[9];
    private static float[] e1 = new float[4];
    private static double TimeUsed;
    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static float x, y, z;
    private static float mwips;
    private static int section;  
    private static int pixWide;  
    private static int pixHigh;  
    private static int ismflops;   
    private static int testDone = 0; 
    private String[] args = new String[2];
    private int part;

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
        sout[3] = "";        sout[4]   = " Android Build Version      "
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
            xout[0]   = " Android Java Whetstone Benchmark 1.2 " + date$ + "\n";
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
            _myTask = new myWhetTask();  
            _myHandler = new Handler();  
            xout[15] = " Running - Time 10 to 20 seconds\n";
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
                            " The Whetstone Benchmark, written in Fortran, was the \n" + 
                            " first general purpose benchmark that set industry   \n" + 
                            " standards of computer performance. It was released  \n" + 
                            " in 1972, based on research by Brian Wichmann, and   \n" + 
                            " produced by Harold Curnow. Later updates became my  \n" + 
                            " responsibility. The three of us were UK Government  \n" + 
                            " employees. Speed was measured in terms of Million   \n" +                     
                            " Whetstone Instructions Per Second (MWIPS). Later, in\n" +
                            " order to identify compiler over-optimisation, speeds\n" +
                            " of individual tests were shown as MOPS or MFLOPS -  \n" +
                            " Millions of Operations or Floating Point Operations \n" +
                            " Per Second. In this version, test functions are run \n" +
                            " for a minimum of one second, milliseconds for the   \n" +
                            " originally defined pass count being used for MWIPS  \n" +
                            " calculations, as 10,000 / Total milliseconds\n" +
                            " \n" +
                            " Roy Longbottom 2013 - 41 years later\n");
                    
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
            builder.setNegativeButton("History, Results from 1960s", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                   Uri uri = Uri.parse("http://www.roylongbottom.org.uk/whetstone.htm"); 
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
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Java Whetstone Benchmark Results");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                                        xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                                xout[4]  + xout[5]  + xout[6]  + xout[7]  + 
                                xout[8]  + xout[9]  + xout[10] + xout[11] + 
                                xout[12] + xout[13] + xout[14] + xout[15] + 
                                xout[16] +
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
        mDisplayDetails.setText(xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                                        xout[4]  + xout[5]  + xout[6]  + xout[7]  + 
                                        xout[8]  + xout[9]  + xout[10] + xout[11] + 
                                        xout[12] + xout[13] + xout[14] + xout[15] + 
                                        xout[16]);
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
   
    private class myWhetTask implements Runnable  
    {  
        
        public void run() 
        {               
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = " Android Java Whetstone Benchmark 1.2 " + date$ + "\n";

            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            for (section=1; section<9; section++)
            {
                whetstones();
                displayAll();
            }
            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
               xout[13] = " MWIPS    "
                    + String.format("%9.2f", mwips)
                    + "        "
                    + String.format("%10.3f\n", TimeUsed * 1000.0);
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
        xout[0]  = " Android Java Whetstone Benchmark 1.2 " + date$ + "\n";
        xout[1]  = "\n";
        xout[2]  = " Test        MFLOPS    MOPS   millisecs    Results \n";
        xout[3]  = "\n";
        xout[4]  = " N1 float \n";
        xout[5]  = " N2 float \n";
        xout[6]  = " N3 if    \n";
        xout[7]  = " N4 fixpt \n";
        xout[8]  = " N5 cos   \n";
        xout[9]  = " N6 float \n";
        xout[10] = " N7 equal \n";
        xout[11] = " N8 exp   \n";
        xout[12] = "\n";
        xout[13] = " MWIPS \n";
        xout[14] = "\n";
        xout[15] = " millisecs is time for defined pass count\n";
        xout[16] = " MWIPS = 10000 / Total millisecs\n";
        
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
    
    
    
    public void whetstones() 
    {
        long startTime;
        long endTime;
        double runTime;
        int i, j, k, l, ix, xtra, n1mult;

        float t  =  0.49999975f;
        float t0 = t;  
        float t1 = 0.50000025f;
        float t2 = 2.0f;
        float Check = 0.0f;

        int x100 = 100;
        int[] num = new int[4];

        int n1 = 12*x100;
        int n2 = 14*x100;
        int n3 = 345*x100;
        int n4 = 210*x100;
        int n5 = 32*x100;
        int n6 = 899*x100;
        int n7 = 616*x100;
        int n8 = 93*x100;
               
        num[0] = 0;
        num[1] = 1;
        num[2] = 2;
        num[3] = 3;
        
        switch (section)
        {
        case 1:

        // Section 1, Array elements
                
            n1mult = 10;
            
            e1[0] =  1.0f;
            e1[1] = -1.0f;
            e1[2] = -1.0f;
            e1[3] = -1.0f;                   

            runTime = 0.0;
            xtra = 1;
            do
            {
                if (runTime > 0.5)
                {
                    xtra = xtra * 2;
                }
                else if (runTime > 0.2)
                {
                    xtra = xtra * 5;
                }
                else
                {
                    xtra = xtra * 10;
                }
                startTime = System.currentTimeMillis();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=0; i<n1*n1mult; i++)
                    {
                        e1[0] = (e1[0] + e1[1] + e1[2] - e1[3]) * t;
                        e1[1] = (e1[0] + e1[1] - e1[2] + e1[3]) * t;
                        e1[2] = (e1[0] - e1[1] + e1[2] + e1[3]) * t;
                        e1[3] = (-e1[0] + e1[1] + e1[2] + e1[3]) * t;
                    }
                    t = 1.0f - t;
                    if (ix == 0) results[section] = e1[3];
                }
                t =  t0;                    
                endTime = System.currentTimeMillis();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            
            Check = Check + e1[3];              
            loop_time[section] = runTime / (double)n1mult / (double)xtra;
            loop_mflops[section] = (double)(n1*16) / 1000000.0 / loop_time[section];
            loop_mops[section] = 0.0;
            titles[section] = " N1 float";
            ismflops = 1;
            TimeUsed = TimeUsed + loop_time[section];
            break;
        
        
        case 2:
                 
        // Section 2, Array as parameter

                runTime = 0.0;
            xtra = 1;
            do
            {
                if (runTime > 0.5)
                {
                    xtra = xtra * 2;
                }
                else if (runTime > 0.2)
                {
                    xtra = xtra * 5;
                }
                else
                {
                    xtra = xtra * 10;
                }
                startTime = System.currentTimeMillis();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=0; i<n2; i++)
                    {   
                          pa(e1,t,t2);
                    }
                    t = 1.0f - t;
                    if (ix == 0) results[section] = e1[3];
                }
                t =  t0;                    
                endTime = System.currentTimeMillis();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + e1[3];
            loop_time[section] = runTime / (double)xtra;
            loop_mflops[section] = (double)(n2*96) / 1000000.0 / loop_time[section];
            loop_mops[section] = 0.0;
            titles[section] = " N2 float";
            ismflops = 1;
            TimeUsed = TimeUsed + loop_time[section];
            break;

        case 3:

        // Section  3, Conditional jumps

        // num[0] to num[3] instead of 1, 2, 3 and 4
        
                runTime = 0.0;
                xtra = 1;
                j = 1;
                do
                {
                    if (runTime > 0.5)
                    {
                        xtra = xtra * 2;
                    }
                    else if (runTime > 0.2)
                    {
                        xtra = xtra * 5;
                    }
                    else
                    {
                        xtra = xtra * 10;
                    }
                    startTime = System.currentTimeMillis();
                    for (ix=0; ix<xtra; ix++)
                    {
                        for(i=0; i<n3; i++)
                        {
                            if (j == 1)
                            {       
                               j = num[2];
                            }
                            else
                            {
                               j = num[3];
                            }
                            if (j > 2)
                            {
                               j = num[0];
                            }
                            else
                            {
                               j = num[1];
                            }
                            if (j < 1)
                            {
                               j = num[1];
                            }
                            else
                            {
                               j = num[0];
                            }
                        }
                        if (ix == 0) results[section] = (float)j;
                    }                    
                    endTime = System.currentTimeMillis();
                    runTime = (endTime - startTime) / 1000.0;
                }
                while (runTime < 1.0);
                Check = Check + (float)j;
                loop_time[section] = runTime / (double)xtra;
                loop_mflops[section] = 0.0;
                loop_mops[section] = (double)(n3*3) / 1000000.0 / loop_time[section];
                titles[section] = " N3 if   ";
                ismflops = 0;
                TimeUsed = TimeUsed + loop_time[section];
                break;

        case 4:
            
        // Section 4, Integer arithmetic
                
        // num[1] to num[3] instead of j, k, l for 1, 2, 3

                j = 1;
                k = 2;
                l = 3;
                runTime = 0.0;
                xtra = 1;
                do
                {
                    if (runTime > 0.5)
                    {
                        xtra = xtra * 2;
                    }
                    else if (runTime > 0.2)
                    {
                        xtra = xtra * 5;
                    }
                    else
                    {
                        xtra = xtra * 10;
                    }
                    startTime = System.currentTimeMillis();
                    for (ix=0; ix<xtra; ix++)
                    {
                        for(i=0; i<n4; i++)
                        {
                             j = num[1] *(k-j)*(l-k);
                             k = num[3] * k - (l-j) * k;
                             l = (l-k) * (num[2]+j);
                             e1[l-2] = j + k + l;
                             e1[k-2] = j * k * l;
                        }
                        if (ix == 0) results[section] = e1[0]+e1[1];
                    }                    
                    endTime = System.currentTimeMillis();
                    runTime = (endTime - startTime) / 1000.0;
                }
                while (runTime < 1.0);
                Check = Check + e1[0]+e1[1];
                loop_time[section] = runTime / (double)xtra;    
                loop_mflops[section] = 0.0;
                loop_mops[section] = (double)(n4*15) / 1000000.0 / loop_time[section];
                titles[section] = " N4 fixpt";
                ismflops = 0;
                TimeUsed = TimeUsed + loop_time[section];
                 break;

        case 5:
            
        // Section 5, Trig function        
                 
            x = 0.5f;
            y = 0.5f;
            runTime = 0.0;
            xtra = 1;
            do
            {
                if (runTime > 0.5)
                {
                    xtra = xtra * 2;
                }
                else if (runTime > 0.2)
                {
                    xtra = xtra * 5;
                }
                else
                {
                    xtra = xtra * 10;
                }
                startTime = System.currentTimeMillis();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=1; i<n5; i++)
                    {
                        x = (float)(t*Math.atan(t2*Math.sin(x)*Math.cos(x)/
                                (Math.cos(x+y)+Math.cos(x-y)-1.0)));
                        y = (float)(t*Math.atan(t2*Math.sin(y)*Math.cos(y)/
                                 (Math.cos(x+y)+Math.cos(x-y)-1.0)));
                    }
                    t = 1.0f - t;
                    if (ix == 0) results[section] = y;
                }
                t =  t0;                    
                endTime = System.currentTimeMillis();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + y;
            loop_time[section] = runTime / (double)xtra;    
            loop_mflops[section] = 0.0;
            loop_mops[section] = (double)(n5*26) / 1000000.0 / loop_time[section];
            titles[section] = " N5 cos  ";
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time[section];
            break;

        case 6:
            x = 1.0f;
            y = 1.0f;
            z = 1.0f;
            runTime = 0.0;
            xtra = 1;
            do
            {
                if (runTime > 0.5)
                {
                    xtra = xtra * 2;
                }
                else if (runTime > 0.2)
                {
                    xtra = xtra * 5;
                }
                else
                {
                    xtra = xtra * 10;
                }
                startTime = System.currentTimeMillis();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=0; i<n6; i++)
                    {
                         p3(t,t1,t2);
                    }
                    if (ix == 0) results[section] = z;
                }
                t =  t0;                    
                endTime = System.currentTimeMillis();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + z;
            loop_time[section] = runTime / (double)xtra;
            loop_mflops[section] = (double)(n6*6) / 1000000.0 / loop_time[section];
            loop_mops[section] = 0.0;
            titles[section] = " N6 float";
            ismflops = 1;
            TimeUsed = TimeUsed + loop_time[section];        
            break;

        case 7:
                
        // Section 7, Array refrences 
            
            j = 0;
            k = 1;
            l = 2;
            e1[0] = 1.0f;
            e1[1] = 2.0f;
            e1[2] = 3.0f;
            runTime = 0.0;
            xtra = 1;
            do
            {
                if (runTime > 0.5)
                {
                    xtra = xtra * 2;
                }
                else if (runTime > 0.2)
                {
                    xtra = xtra * 5;
                }
                else
                {
                    xtra = xtra * 10;
                }
                startTime = System.currentTimeMillis();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=0;i<n7;i++)
                    {
                        po(e1,j,k,l);
                    }
                    if (ix == 0) results[section] = e1[2];
                }
                t =  t0;                    
                endTime = System.currentTimeMillis();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + e1[2];;
            loop_time[section] = runTime / (double)xtra;    
            loop_mflops[section] = 0.0;
            loop_mops[section] = (double)(n7*3) / 1000000.0 / loop_time[section];
            titles[section] = " N7 equal";
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time[section];
            break;

        case 8:
                
            // Section 8, Standard functions      

            x = 0.75f;
            runTime = 0.0;
            xtra = 1;
            do
            {
                if (runTime > 0.5)
                {
                    xtra = xtra * 2;
                }
                else if (runTime > 0.2)
                {
                    xtra = xtra * 5;
                }
                else
                {
                    xtra = xtra * 10;
                }
                startTime = System.currentTimeMillis();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=0; i<n8; i++)
                    {
                        x = (float)(Math.sqrt(Math.exp(Math.log(x)/t1)));
                    }
                    if (ix == 0) results[section] = x;
                }
                t =  t0;                    
                endTime = System.currentTimeMillis();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + x;
            loop_time[section] = runTime / (double)xtra;    
            loop_mflops[section] = 0.0;
            loop_mops[section] = (double)(n8*4) / 1000000.0 / loop_time[section];
            titles[section] = " N8 exp  ";
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time[section];
            mwips = (float)x100 / (float)(10.0 * TimeUsed);
            break;
       
        }
            if (ismflops == 1)
            {
                mflops[section] = String.format("%9.2f",  loop_mflops[section]);
                mops[section] = "        ";
            }
        else
        {
            mops[section] = String.format("%9.2f",  loop_mops[section]);
            mflops[section] = "        ";                   
        }
        xout[section+3] = titles[section]
                        + " "
                        + mflops[section]
                        + mops[section]
                        + String.format("%10.3f",  loop_time[section] * 1000.0)
                    + String.format(" %13.9f\n",  results[section]);
              resetTest();
               _myHandler.postDelayed(_myTask, 10);
    }
    
    static void po(float e1[], int j, int k, int l)
    {
        e1[j] = e1[k];
        e1[k] = e1[l];
        e1[l] = e1[j];
        return;
    }

    static void p3(float t, float t1, float t2)
    {
        x = y;
        y = z;
        x = t * (x + y);
        y = t1 * (x + y);
        z = (x + y)/t2;
        return;
    }



    static void pa(float e[], float t, float t2)
    { 
        int j;
        for(j=0;j<6;j++)
        {
            e[0] = (e[0]+e[1]+e[2]-e[3])*t;
            e[1] = (e[0]+e[1]-e[2]+e[3])*t;
            e[2] = (e[0]-e[1]+e[2]+e[3])*t;
            e[3] = (-e[0]+e[1]+e[2]+e[3])/t2;
        }
        return;
    }
    
} 
 
    
    
 
