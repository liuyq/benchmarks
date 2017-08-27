package com.mpintstress;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;
import android.text.method.ScrollingMovementMethod;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.EditText;
import android.content.DialogInterface;
import android.text.Editable;
import android.content.Intent;


import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.widget.Toast;

import android.os.AsyncTask;
import android.os.Build;

public class MPIntStressActivity extends Activity 
{
    private String version = "  ARM/Intel MP-Int Stress Test V1.0 ";
    TextView outputText = null;
    ScrollView scroller = null;
    private Typeface tf = Typeface.create("monospace", 0);
    private String results;
    private static long startTime;
    private static long endTime;
    private static double testTime;

    private String[] xout = new String[20];
    private String[] sout = new String[20];
    private String[] iout = new String[20];
    private String[] mout = new String[1]; 
    private String params; 
    private int part;
    private int stressit = 0;
    private int testNumber;
    private int testMinutes;
    private int minNumber = 15;
    private int threads;
    private int thrdNumber = 8;
    private int kbCode;
    private int kbNumber = 2;
    private String size$ = "  160 KB";
    private int mult;
    private int pattern;

    private int firstTest;
    private int resCount;
    private int patCount;
    private static int testDone = 0; 

    private String[] args = new String[2];
    private String date$;
    private String x0;

    private static int pixWide;  
    private static int pixHigh;  
    public native String  stringFromJNI2();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        getDate();
        part = 0;
        args[0] = "/system/bin/cat";
                args[1] = "/proc/cpuinfo";
                ReadCPUinfo();
                part = 1;
                args[1] = "/proc/version";
                ReadCPUinfo();
                        
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

        outputText = (TextView)findViewById(R.id.OutputText);
        DisplayMetrics metrics = new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(metrics); 

        pixHigh = metrics.heightPixels;
        pixWide = metrics.widthPixels;

        outputText.setTypeface(tf);
        View title = getWindow().findViewById(android.R.id.title);
        View titleBar = (View) title.getParent();
        titleBar.setBackgroundColor(Color.rgb(0,0,255));
        getWindow().getDecorView().setBackgroundResource(R.drawable.bground);

        outputText.setTextColor(Color.rgb(0,0,255));
        int pixels = 11;
        int pixMin = pixWide;
        if (pixHigh < pixMin) pixMin = pixHigh;

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
        outputText.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels); 
        params = String.format(" params %d thrds, " + size$ + ", %d mins\n", thrdNumber, minNumber);
        outputText.setText(" " + stringFromJNI2() + " RunB - run Benchmark\n RunS - run Stress test\n SetS - change stress test parameters\n Now  -" + params);
        outputText.setMovementMethod(new ScrollingMovementMethod());
        scroller = (ScrollView)findViewById(R.id.Scroller);
    }


    public void getDate()
    {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);
    }

    public String getDeviceName() 
    {
       String manufacturer = Build.MANUFACTURER;
       String model = Build.MODEL;
       if (model.startsWith(manufacturer)) 
       {
          return capitalize(model);
       }
       else 
       {
          return capitalize(manufacturer) + " " + model;
       }
    }
    
    
    private String capitalize(String s) 
    {
        if (s == null || s.length() == 0) 
        {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) 
        {
            return s;
        }
        else 
        {
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
    static 
    {
        System.loadLibrary("mpintstresslib");
    }

    public void onRunButtonClick(View view)
    {
      stressit = 0;
      kbCode = 0;
      mult = 1;
      firstTest = 1;
      new RunTests().execute();
    }

    public void onSaveButtonClick(View view)
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

    public void onInfoButtonClick(View view)
    {
        outputText.setText("This test reads data comprising two data patterns out of 24 variations via alternate additions and subtractions " +
        "that leave the original data unchanged. This is checked for correctness and any errors reported. The tests use " +
         "1, 2, 4, 8, 16 and 32 threads. Data sizes are limited to three to use L1 and L2 caches and RAM at 16, 160 and " + 
         "16000 KB.\n\n" +
         "The benchmark runs all possible tests, up to 32 threads, to help in deciding which to use for stress testing.\n\n" + 
         " RunB - run Benchmark\n RunS - run Stress test\n SetS - change stress test parameters\n Now  -" + params + 
         "\n Save - Emails results to program author or/and whoever.\n\n" + 
         "WARNING - USE AT YOUR OWN RISK.\n" +
         "The tests can increase CPU temperatures and lead to slower speed and eventual power down. Test sessions of no more than 15 minutes are recommended.\n\n");
   }

    public void onStressButtonClick(View view)
    {
      threads = thrdNumber;
      testMinutes = minNumber;
      stressit = 1;
      kbCode = kbNumber;
      mult = 1;
      firstTest = 1;
      resCount = 0;
      patCount = 0;
       new RunStress().execute();
    }

    public void onSetupButtonClick(View view)
    {
        params = String.format(" params %d thrds, " + size$ + ", %d mins\n", thrdNumber, minNumber);
        outputText.setText(params + " Edit params \n");
        showDialog(4);
        showDialog(3);
        showDialog(2);
    }

    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {
          case 2:
              Builder builder2 = new AlertDialog.Builder(this);
              builder2.setMessage("Enter number of threads 1, 2, 4. 8, 16, or 32");
              final EditText input2 = new EditText(this);
              builder2.setView(input2);
              builder2.setCancelable(true);
              builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int whichButton) 
                  {
                  try 
                  {
                      testNumber = Integer.parseInt(input2.getText().toString());
                  } 
                      catch(NumberFormatException e) 
                  {
                      Toast.makeText(getApplicationContext(),"Invalid Number",Toast.LENGTH_LONG).show();
                  }
                  if (testNumber == 1 || testNumber == 2 || testNumber == 4 || testNumber == 8 || testNumber == 16 || testNumber == 32)
                  {
                       thrdNumber = testNumber;
                       params = String.format(" params %d thrds, " + size$ + ", %d mins\n", thrdNumber, minNumber);
                       String amessage = String.format(params);
                       Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                       outputText.setText(amessage);
                  }
                  else
                  {
                     String amessage = String.format(" Invalid number of threads, still %d\n", thrdNumber) + params;
                     Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                     outputText.setText(amessage);
                  }
              } 
              });
              builder2.show();
                  break;                  
 
          case 3:
              Builder builder3 = new AlertDialog.Builder(this);
              builder3.setMessage("Enter data size 1 for 16 KB, 2 for 160 KB, 3 for 16 MB ");
              final EditText input3 = new EditText(this);
              builder3.setView(input3);
              builder3.setCancelable(true);
              builder3.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int whichButton) 
                  {
                  try 
                  {
                      testNumber = Integer.parseInt(input3.getText().toString());
                  } 
                      catch(NumberFormatException e) 
                  {
                      Toast.makeText(getApplicationContext(),"Invalid Number",Toast.LENGTH_LONG).show();
                  }
                  if (testNumber == 1 || testNumber == 2 || testNumber == 3)
                  {
                       kbNumber = testNumber;
                       if (kbNumber ==1) size$ = "   16 KB"; 
                       if (kbNumber ==2) size$ = "  160 KB"; 
                       if (kbNumber ==3) size$ = "   16 MB"; 
                       params = String.format(" params %d thrds, " + size$ + ", %d mins\n", thrdNumber, minNumber);
                       String amessage = String.format(params);
                       Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                       outputText.setText(amessage);
                  }
                  else
                  {
                     String amessage = String.format(" Invalid data size, still " + size$ + "\n") + params;
                     Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                     outputText.setText(amessage);
                  }
              } 
              });
              builder3.show();
                  break;
                      
          case 4:
              Builder builder4 = new AlertDialog.Builder(this);
              builder4.setMessage("Enter minutes test duration ");
              final EditText input4 = new EditText(this);
              builder4.setView(input4);
              builder4.setCancelable(true);
              builder4.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int whichButton) 
                  {
                  try 
                  {
                      testNumber = Integer.parseInt(input4.getText().toString());
                  } 
                      catch(NumberFormatException e) 
                  {
                      testNumber = 0;                       
                      Toast.makeText(getApplicationContext(),"Invalid Number",Toast.LENGTH_LONG).show();
                  }
                  if (testNumber > 0)
                  {
                       minNumber = testNumber;
                       params = String.format(" params %d thrds, " + size$ + ", %d mins\n", thrdNumber, minNumber);
                       String amessage = String.format(params);
                       Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                       outputText.setText(amessage);
                  }
                  else
                  {
                     String amessage = String.format(" Invalid number of minutes, still %d\n", minNumber) + params;
                     Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                     outputText.setText(amessage);
                  }
              } 
              });
              builder4.show();
                  break;                  
 
         case 9:
              Builder builder9 = new AlertDialog.Builder(this);
              builder9.setMessage("Add note to results?");
                  final EditText input = new EditText(this);
                  builder9.setView(input);
                          
              builder9.setCancelable(true);
              builder9.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int which) 
                  {
                    Editable value = input.getText();
                        sout[10] = " Device " + value.toString();
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        String[] recipients = new String[]{"results@roylongbottom.org.uk", "",};
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android MP-Int-Stress Results");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                            mout[0]  +
                            sout[0]  + sout[1]  + sout[2]  + sout[3]  +
                            sout[4]  + sout[5]  + sout[6]  + sout[7]  + 
                            sout[8]  + sout[9]  + sout[10] + sout[11]);
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                  }
              });
              builder9.show();
                  break;


        }
        return super.onCreateDialog(id);
    } 


    public native String  stringFromJNI(int threads, int stressit, int kbCode, int mult, int pattern);

    public void testCPU()
    {
        startTime = System.currentTimeMillis();

        x0 = stringFromJNI(threads, stressit, kbCode, mult, pattern);
   
        endTime = System.currentTimeMillis();
        testTime = (double)(endTime - startTime) / 1000.0;
        results = String.format("%6.1f %s", testTime, x0); 
        testDone = 1;
    }

    private class RunTests extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() 
        {
            outputText.setText(version + date$ + "\n" + 
            " " + stringFromJNI2() + "\n" +
            "                 MB/second\n" +
            "               KB    KB    MB            Same All\n" +  
            "  Secs Thrds   16   160    16  Sumcheck   Tests\n\n");
            mout[0] = version + date$ + "\n" + 
            " " + stringFromJNI2() + "\n" +
            "                 MB/second \n" +
            "               KB    KB    MB            Same All\n" +  
            "  Secs Thrds   16   160    16  Sumcheck   Tests\n\n"; 
       }

        @Override
        protected Void doInBackground(Void... params) 
        {
             pattern = 0;
             for(threads=1; threads<33; threads=threads*2)
             {
                  testCPU();
                  publishProgress(threads); 
                  pattern = pattern + 1;
             }             
             return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) 
        {
              outputText.append(results + "\n");
              mout[0] = mout[0] + results + "\n";
              scroller.post(new Runnable() 
              {
                public void run() 
                {
                  scroller.fullScroll(ScrollView.FOCUS_DOWN);
                }
              });
        }
        
        @Override
        protected void onPostExecute(Void result) 
        {
            getDate();
            outputText.append("\n            End Time " + date$ + "\n" );
            mout[0] = mout[0] + "\n            End Time " + date$ + "\n\n";             
        }               
    }
        
    public void stressCPU()
    {
        x0 = stringFromJNI(threads, stressit, kbCode, mult, pattern); 
        endTime = System.currentTimeMillis();
        testTime = (double)(endTime - startTime) / 1000.0;
        results = String.format("%7.1f %s%s", testTime, size$, x0);
        if (resCount<200 && firstTest == 0)
        {
           mout[0] = mout[0] + results + "\n";
           resCount = resCount + 1;
        }
    }

    private class RunStress extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute() 
        {
            outputText.setText(version + date$ + "\n" + 
            " " + stringFromJNI2() + "\n" +
            "            Data                         Same All\n" +  
            " Seconds    Size Threads  MB/sec Sumcheck Threads\n\n");
            mout[0] = version + date$ + "\n" + 
            " " + stringFromJNI2() + "\n" +
            "            Data                         Same All\n" +  
            " Seconds    Size Threads  MB/sec Sumcheck Threads\n\n";
        }


        @Override
        protected Void doInBackground(Void... params) 
        {
           pattern = 0;   
           startTime = System.currentTimeMillis();
            do
            {
               stressCPU();
               if (firstTest == 0)
               {
                   publishProgress(threads);
                   patCount = patCount + 1;
                   if (patCount > 5)
                   {
                       patCount = 0;
                       pattern = pattern + 1;
                       if (pattern > 11) pattern = 0;
                   }
               }
               else if (testTime < 10.0)
               {
                   mult = (int)(10.0/testTime + 1.0);
               }
               firstTest = 0; 
            }
            while(testTime < 60*testMinutes); 
            testDone = 1;
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) 
        {
              outputText.append(results + "\n");
              scroller.post(new Runnable() 
              {
                public void run() 
                {
                  scroller.fullScroll(ScrollView.FOCUS_DOWN);
                }
              });
        }
        
        @Override
        protected void onPostExecute(Void result) 
        {
            getDate();
            outputText.append("\n            End Time " + date$ + "\n" );
            mout[0] = mout[0] + "\n            End Time " + date$ + "\n\n";                    
        }               
    }
}
