package com.exercise.CP_MHz2;

import android.app.Activity;
import android.os.Bundle;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.graphics.Color;
import android.os.Message;


public class CP_MHz2Activity extends Activity implements View.OnClickListener
{
    private Button mStartButton;  
    private Button mMailButton;  
    private Handler _myHandler;  
    private Handler _myHandler2;  
    private TextView mDisplayDetails; 
    private Runnable _myTask;  
    private Runnable _myTask2;  
    private String[] xout = new String[20];
    private String[] iout = new String[20];
    private String date$;

    private static long startTest;
    private static long endTest;
    private static double testTime;
    private static double mhz;
    private static int pixWide;  
    private static int pixHigh;  


    private static int testDone = 0; 
    private String[] args = new String[2];
    private int part;

    private Typeface tf = Typeface.create("monospace", 0);

        
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        
        
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main); 
        mStartButton = (Button)findViewById(R.id.startButton);    
        mMailButton = (Button)findViewById(R.id.mailButton);  

        mStartButton.setOnClickListener(this);  
        mMailButton.setOnClickListener(this);

        mDisplayDetails = (TextView)findViewById(R.id.displayDetails);  
        DisplayMetrics metrics = new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(metrics); 
        mDisplayDetails.setTypeface(tf);
        
        whatdate();
        
        part = 0;
        args[0] = "/system/bin/cat";
                args[1] = "/proc/cpuinfo";
                ReadCPUinfo();
                part = 1;
                args[1] = "/proc/version";
                ReadCPUinfo();
                        
        pixHigh = metrics.heightPixels;
        pixWide = metrics.widthPixels;
       
        View title = getWindow().findViewById(android.R.id.title);
        View titleBar = (View) title.getParent();
        titleBar.setBackgroundColor(Color.rgb(0,0,255));
        mDisplayDetails.setTextColor(Color.rgb(0,0,255));

        int pixels = 11;
        int pixMin = pixWide;
        if (pixHigh < pixMin) pixMin = pixHigh;
        if (pixHigh < 320)
        {
            xout[0]   = " Android CPU MHz 100 ms Sampling " + date$ + "\n";
            xout[1]   = "\n";
            xout[2]   = "\n";
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
            xout[17]  = "\n";
            xout[18]  = "\n";
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
            xout[17] = " Running Time 30+ Seconds\n";
            xout[15] = "\n"; 
            xout[16] = "\n"; 
            displayAll();    
            startTest = System.currentTimeMillis();
            _myHandler.postDelayed(_myTask, 100);
 
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
          case 9:
              Builder builder2 = new AlertDialog.Builder(this);
              builder2.setMessage("Device Model and Notes");
                  final EditText input = new EditText(this);
                  builder2.setView(input);
                          
              builder2.setCancelable(true);
              builder2.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int which) 
                  {
                        Editable value = input.getText();
                        xout[18] = " Device/Notes " + value.toString();
                        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                        String[] recipients = new String[]{"results@roylongbottom.org.uk", "",};
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, recipients);
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android CPU MHz2");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                                xout[0]  + xout[1]  + xout[2]  + xout[3]  +
                                xout[4]  + xout[5]  + xout[6]  + xout[7]  + 
                                xout[8]  + xout[9]  + xout[10] + xout[11] + 
                                xout[12] + xout[13] + xout[14] + xout[15] + 
                                xout[16] + xout[17] + xout[18]
                                );
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
                                        xout[16] + xout[17]);
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
                testDone = 0;
                whatdate();

            startTest = System.currentTimeMillis();
            runIt();
            displayAll();
            endTest = System.currentTimeMillis();
            testTime = (double)(endTest - startTest) / 1000.0;
            testDone = 1;
            displayAll();
            resetTest();          
        }  
    }
    
    public void whatdate()
    {
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        date$ = formatter.format(today);
        if (testDone == 0)
        {
            xout[0]  = " Android CPU MHz 100 ms Sampling " + date$ + "\n";
        }
    }

    public void clear()
    {
        xout[0]  = " Android CPU MHz 100 ms Sampling " + date$ + "\n";
        xout[1]  = "\n";
        xout[2]  = "\n";
        xout[3]  = "\n";
        xout[4]  = "\n";
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
        xout[15] = "\n";
        xout[16] = "\n";
        xout[17] = "\n";
        xout[18] = "\n";
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
    
    private String ReadCPUMhz()
    {
         ProcessBuilder cmd;
         String result="";
         try
         {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[1024];
            while(in.read(re) != -1)
            {
                result = result + new String(re);
            }
            in.close();
         } 
         catch(IOException ex)
         {
             ex.printStackTrace();
         }
         result = result.replace("\n", "");
         result = result.replace(" ", "");
         mhz = 0.0;
         if (result != "")
         {
             mhz = Double.parseDouble(result) / 1000;
         }
         return result;
    }    
    
    public void runIt() 
    {
        int i;
        int j;
        
        whatdate();
        for(i=1; i<16; i++)
        {
                for (j=0; j<20; j++)
                {
                endTest = System.currentTimeMillis();
                testTime = (double)(endTest - startTest) / 1000.0;

                if (j == 0)
                {
                         ReadCPUMhz();
                         xout[i] = String.format("%6.2f %6.0f",  testTime, mhz);
                }
                else
                {
                        ReadCPUMhz();
                        xout[i] = xout[i] + String.format(" %6.2f %6.0f",  testTime, mhz);
                        if (j == 19)
                        {
                        	 xout[i] = xout[i] + String.format(" X\n");
                        }
                }
                try
                {
                     Thread.sleep(100);
                }
                catch(InterruptedException e)
                { 
                        
                }
            }
        }
        testDone = 1;
        whatdate();
        xout[17] = " Running Finished     " + date$ + "\n";
        resetTest();
        _myHandler.postDelayed(_myTask, 10);
    }
 
   

}





