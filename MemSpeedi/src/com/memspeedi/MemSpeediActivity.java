package com.memspeedi;

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

public class MemSpeediActivity extends Activity implements View.OnClickListener
{
    private String version = " ARM/Intel MemSpeed Benchmark 1.2 ";
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
    private static int pixWide;  
    private static int pixHigh;  
    private static int testDone = 0; 
    private static int size1; 
    private String[] args = new String[2];
    private int part;
    private int runs = 10; // 2 * 4 DP MW arrays = 2 * 32 MB

    private Typeface tf = Typeface.create("monospace", 0);
    private Context show;
    public native String  stringFromJNI2();

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
        x0 = " Not run yet\n\n\n\n\n\n\n\n\n\n\n\n";
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
            xout[0]   = version + date$ + "\n" + stringFromJNI2();
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
    static 
    {
        System.loadLibrary("memspeedilib");
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

    public void onClick(View v) 
    {  
        if(v.getId() == R.id.startButton)
        {
             clear();
            _myTask = new myWhetTask();  
            _myHandler = new Handler();  
            xout[15] = " **************** Running ****************\n";
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
                        " This benchmark measures data reading speeds in Mega  \n" + 
                        " Bytes per second carrying out calculations on arrays \n" + 
                        " of cache and RAM data, sized 2 x 8 KB to 2 x 32 MB.  \n" + 
                        " Calculations are x[m]=x[m]+s*y[m] and x[m]=x[m]+y[m] \n" + 
                        " using double and single precision floating point and \n" + 
                        " x[m]=x[m]+s+y[m] and x[m]=x[m]+y[m] with integers.   \n" + 
                        " Million Floating Point Operations Per Second (MFLOPS) \n" +                     
                        " speed can be calculated by dividing double precision  \n" +
                        " MB/second by 8 and 16, for the two tests, and single \n" +
                        " precision speeds by 4 and 8. Assembly listings for   \n" +
                        " integer tests show that Millions of Instructions Per \n" +
                        " Second can be found by multiplying MB/second by 0.78 \n" +
                        " with 2 adds and 0.66 for the other test. Cache sizes \n" +
                        " are indicated by varying performance as memory usage \n" +
                        " changes.\n" +
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
 /*
            builder.setNegativeButton("MemSpeed Results on PCs", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which)
               
                {
                   Uri uri = Uri.parse("http://www.roylongbottom.org.uk/memspd2k results.htm"); 
                   startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    Toast.makeText(getApplicationContext(),"Loading HTML",Toast.LENGTH_LONG).show();
                }
            });
 */
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
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ARM/Intel MemSpeed Benchmark Results");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                                           xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                                           xout[4]  + xout[5]  + xout[6]  + xout[7]  + 
                                           xout[8]  + xout[9]  + xout[10] + xout[11] +
                                           xout[12] + xout[13] + xout[14] +
                                           xout[15] + xout[16] +                                      
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
                                        xout[12]  + xout[13] + xout[14] +
                                                    xout[15] + xout[16]);
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
    public native String  stringFromJNI(int size1);

    private class myWhetTask implements Runnable  
    {  
       
        public void run() 
        {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = version + date$ + "\n" + stringFromJNI2();

            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            for (size1=0; size1<runs; size1++)
            {
               x0 = stringFromJNI(size1);
               xout[size1+5] = x0;
               endTest = System.currentTimeMillis();
               testTime = (double)(endTest - startTest) / 1000.0;
               if (testTime > 120) size1 = runs;
            }
            xout[15] = "\n";
            xout[16] = "          Total Elapsed Time  " + String.format("%5.1f", testTime)
                    + " seconds\n";
            testDone = 1;
            displayAll();
            resetTest();          
        }  
    }

    public void clear()
    {
        xout[0]  = version + date$ + "\n" + stringFromJNI2();
        xout[1]  = "\n";
        xout[2]  = "              Reading Speed in MBytes/Second\n";
        xout[3]  = "  Memory  x[m]=x[m]+s*y[m] Int+   x[m]=x[m]+y[m]\n";
        xout[4]  = "  KBytes   Dble   Sngl    Int   Dble   Sngl    Int\n";
        xout[5]  = "\n";
        xout[6]  = "\n";
        xout[7]  = "\n";
        xout[8]  = "\n";
        xout[9]  = "\n";
        xout[10] = "\n";
        xout[11] = "\n";
        xout[12] = "\n";
        xout[13] = "\n";
        xout[14] = "\n";
        xout[15] = " Running time 15 to 25 seconds ARMv7 fast FPU\n"; 
        xout[16] = " Slower CPUs stop testing after 120+ seconds\n"; 
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
}
