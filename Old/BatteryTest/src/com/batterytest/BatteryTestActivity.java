package com.batterytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;  
import android.view.View;
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
import android.text.Editable;
import android.content.Intent;
import android.content.DialogInterface;      
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import android.graphics.Color;

public class BatteryTestActivity extends Activity implements View.OnClickListener
{
    private Button mStartButton;  
    private Button mTimeButton;  
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
    private String defaults;

    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static int pixWide;  
    private static int pixHigh;  
    private static int testDone = 0; 
    private String[] args = new String[2];
    private int part;
    private int testSecs = 1;

    private Typeface tf = Typeface.create("monospace", 0);
    
    private FileInputStream  fis;
    private String filePath1;
    private String fileName = "BatteryTest.txt";
    private File file;
    private String thisMessage;

    int requestCode;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main); 
        mStartButton = (Button)findViewById(R.id.startButton);  
        mTimeButton = (Button)findViewById(R.id.timeButton);  
        mInfoButton = (Button)findViewById(R.id.infoButton);  
        mMailButton = (Button)findViewById(R.id.mailButton);  


        mStartButton.setOnClickListener(this);  
        mTimeButton.setOnClickListener(this);  
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
        sout[1]   = "\n";
        sout[2] = " Screen pixels w x h "
                + String.format("%d", pixWide)
                + " x "
                + String.format("%d", pixHigh)
                + " \n";
        sout[3] = "\n";
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
            xout[0]   = " Android Battery Test " + date$ + "\n";
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
            testDone = 0;
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

        filePath1 = Environment.getExternalStorageDirectory().getPath();
        file = new File(filePath1, fileName);
        StringBuffer fileContent = new StringBuffer("");
        byte[] buffer = new byte[1024];
        try
        {
            fis = new FileInputStream(file);
        }
        catch(Exception e)
        {
            thisMessage = " Failed to open " + filePath1 + " " + fileName + "\n";
            Toast.makeText(getApplicationContext(),thisMessage,Toast.LENGTH_LONG).show();            
            xout[1]  = "\n WARNING, RUN AT YOUR OWN RISK. The effects of writing to  \n" +
                          " a drive when powering down can be unpredictable. However,\n" +
                          " no problems have been seen on testing.\n\n" +
                          " Default 60x1 second runs, click Time to change\n\n";
        }
        try
        {
            int n;
            while ((n = fis.read(buffer)) != -1) 
            { 
              fileContent.append(new String(buffer, 0, n)); 
            } 
        }
        catch(Exception e)
        {
            thisMessage = " Failed to read " + filePath1 + " " + fileName + "\n";
            try
            {
                fis.close();
            }
            catch(Exception f)
            {
            }
        }
        testDone = 1;             
        xout[2] = fileContent.toString();      
        
        displayAll();

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
            xout[15] = " Running\n";
            xout[16] = "\n"; 
            displayAll();  
            startTest = System.currentTimeMillis();
            _myHandler.postDelayed(_myTask, 100);
 

        }  
        else if(v.getId() == R.id.timeButton)
        {
           showDialog(10);
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
                        mDisplayDetails.setText
                        (
                          " BatteryTest.apk is a Java program using a test from my  \n" +
                          " JavaDraw Benchmark, drawing multiple objects. There are \n" +
                          " 60 test runs, with default running time 1 second each.  \n" +
                          " The latter can be increased via a Time button. As each  \n" +
                          " pair of runs is finished, speeds in FPS and measured    \n" +
                          " CPU MHz are saved to the default internal drive in file \n" +
                          " BatteryTest.txt. If all tests are completed, results are \n" +
                          " displayed. Otherwise, they are displayed the next time  \n" +
                          " program is loaded. In either case, they can be saved via \n" +
                          " the Email button. Before running, Display/Power Settings \n" +
                          " should be changed to never switch off and CPU to run at \n" +    
                          " maximum speed, if possible. \n" +
                          " In the case of the first devices used, the results file  \n" +
                          " was visible via Windows Explorer, using a USB connection.\n" +
                          " \n" +
                          " WARNING, RUN AT YOUR OWN RISK. The effects of writing to \n" +
                          " a drive when powering down can be unpredictable. However,\n" +
                          " no problems have been seen on testing.\n");
                          resetTest();
                 }
            });
 
            builder.setNegativeButton("My Android Benchmarks", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                   Uri uri = Uri.parse("http://www.roylongbottom.org.uk/android benchmarks.htm"); 
                   startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    Toast.makeText(getApplicationContext(),"Loading HTML",Toast.LENGTH_LONG).show();
                }
            });
            builder.setNeutralButton("Description and Results", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                   Uri uri = Uri.parse("http://www.roylongbottom.org.uk/android graphics benchmarks.htm"); 
                   startActivity(new Intent(Intent.ACTION_VIEW, uri));

                    Toast.makeText(getApplicationContext(),"Loading HTML",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            break;
 
          case 9:
              Builder builder2 = new AlertDialog.Builder(this);
              builder2.setMessage("Device Model?");
                  final EditText input = new EditText(this);
                  builder2.setView(input);
                          
              builder2.setCancelable(true);
              builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int which) 
                  {
                    Editable value = input.getText();
                        sout[10] = " Device " + value.toString();
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        String[] recipients = new String[]{"results@roylongbottom.org.uk", "",};
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Java Battery Test Results");
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
                  
          case 10:
              Builder builder3 = new AlertDialog.Builder(this);
              builder3.setMessage("Enter seconds for each of 60 runs ");
              final EditText input2 = new EditText(this);
              builder3.setView(input2);
              builder3.setCancelable(true);
              builder3.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int whichButton) 
                  {
                  try 
                  {
                      testSecs = Integer.parseInt(input2.getText().toString());
                  } 
                      catch(NumberFormatException e) 
                  {
                      Toast.makeText(getApplicationContext(),"Invalid Number, still 1 second, try again",Toast.LENGTH_LONG).show();
                  }
                  if (testSecs > 1)
                  {
                       String amessage = String.format(" Test running time %d seconds", testSecs);
                       Toast.makeText(getApplicationContext(), amessage, Toast.LENGTH_LONG).show();
                  }          
              } 
              });
              builder3.show();
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
            xout[0]  = " Android Battery Test " + date$ + "\n";

            startTest = System.currentTimeMillis();
            testTime = 0.0;
            testDone = 0;
            runit();        }  
    }
    
  
    public void runit() 
    {
 
        Intent intent = new Intent(this, RunActivity.class);
        Bundle b = new Bundle();
        b.putInt("doSecs", testSecs);
        intent.putExtras(b);        
        startActivityForResult(intent, requestCode);
    }  

@Override 
public void finish() 
{ 
      super.finish(); 
      overridePendingTransition(0, 0);
} 
    public void clear()
    {
        xout[0]  = " Android Java Battery Test " + date$ + "\n";
        xout[1]  = "\n Default 60x1 second runs, click Time to change\n\n";
        xout[2]  = "";
        xout[3]  = ""; 
        xout[4]  = "";
        xout[5]  = "";
        xout[6]  = "";
        xout[7]  = "";
        xout[8]  = "";
        xout[9]  = "";
        xout[10] = "";
        xout[11] = "";
        xout[12] = "";
        xout[13] = "";
        xout[14] = "";
        xout[15] = "";
        xout[16] = "";         
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
    

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        String extraData=data.getStringExtra("ComingFrom");
        if(resultCode == 1)
        {
            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            xout[2] = extraData;
            xout[3]  = "";
            xout[4]  = "";
            xout[5]  = "";
            xout[6]  = "";
            xout[7]  = "";
            xout[8]  = "";
            xout[9]  = "";
            xout[10] = "";
            xout[11] = "\n";
            xout[12] = " Total Elapsed Time  " + String.format("%5.1f", testTime) + " seconds\n";
            xout[13] = "";
            xout[14] = "";
            xout[15] = "";
            xout[16] = "";
            testDone = 1;
            displayAll();           
            Toast.makeText(this, "Pass", Toast.LENGTH_LONG).show();
        }
        else
        {
            xout[2]  = "\n";
            xout[3]  = "\n";
            xout[4]  = "\n";
            xout[5]  = "\n";
            xout[6]  = "\n";
            xout[7]  = "\n";
            xout[8]  = "\n";
            xout[9]  = "\n";
            xout[10] = "\n";
            xout[11] = "  Program failed\n";
            xout[12] = "\n";
            xout[13] = "\n";
            xout[14] = "\n";
            xout[15] = "\n";
            xout[16] = "\n";
            testDone = 1;
            displayAll();           
          Toast.makeText(this, "Fail", Toast.LENGTH_LONG).show();
        }
    }
}



