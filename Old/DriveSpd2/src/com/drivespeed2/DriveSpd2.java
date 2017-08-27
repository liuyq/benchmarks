package com.drivespeed2;

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
import android.os.Environment;
import android.os.StatFs;
import android.graphics.Color;

public class DriveSpd2 extends Activity  implements View.OnClickListener
{
    private String version = " Android DriveSpeed2 Benchmark 1.0 ";
    private String title2;
    private String driveUsed;
    private String driveMsg;
    private Button mStartButton;  
    private Button mMailButton;  
    private Button mRestartButton;  
    private Handler _myHandler;  
    private Handler _myHandler2;  
    private TextView mDisplayDetails; 
    private Runnable _myTask;  
    private String[] xout = new String[20];
    private String[] sout = new String[20];
    private String[] iout = new String[20];
    private String capacity;
    private String x0;
    private String date$;
    private String path;

    private static double TimeUsed;
    private static long startTest;
    private static long endTest;
    private static double testTime;  
    private static int pixWide;  
    private static int pixHigh;  
    private static int testDone = 0;  
    private static int size1; 
    private static int sizea = 0; 
    private String[] args = new String[2];
    private int part;
    private int runs;  
    private int driveToUse;
    private int delete = 1;    

    private Typeface tf = Typeface.create("monospace", 0);
    private Context show;
    private boolean noDetails;
    private boolean hasDirectIO;
         
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main); 
        mStartButton = (Button)findViewById(R.id.startButton);  
        mMailButton = (Button)findViewById(R.id.mailButton);  
        mRestartButton = (Button)findViewById(R.id.restartButton);  
 
        mStartButton.setOnClickListener(this);  
        mMailButton.setOnClickListener(this);
        mRestartButton.setOnClickListener(this);  

        x0 = " Not run yet\n\n\n\n\n\n\n\n\n\n\n\n";

        mDisplayDetails = (TextView)findViewById(R.id.displayDetails);  
        DisplayMetrics metrics = new DisplayMetrics(); 
        getWindowManager().getDefaultDisplay().getMetrics(metrics); 
        mDisplayDetails.setTypeface(tf);
        
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);
        title2 = version + date$ + "\n";
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
            xout[0]   = title2;
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
            xout[13]  = "\n\n";
            xout[14]  = "\n";
            xout[15]  = "\n";
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
             if (pixHigh < 401)
             {
                pixels = 11;
             }
             clear();                
        }
        mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels); 
        showDialog(8);
        displayAll();
    }
          

    static 
    {
        System.loadLibrary("drivespeed2lib");
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
// char filePath[1000] = "/data/data/com.drivespeed2/";
// char filePath[1000] = "/data/data/com/drivespeed2/files/";
// char filePath[1000] = "/sdcard/";
// char filePath[1000] = "/LocalDisk/";

    public void onClick(View v) 
    {  
        if(v.getId() == R.id.startButton)
        {
             driveToUse = 0;
             clear();
             noDetails = false;
             
            try
            {
                StatFs stat_fs2 = new StatFs(path);
        
                double total_int_space = (double)stat_fs2.getBlockCount() *(double)stat_fs2.getBlockSize();
                double free_int_space = (double)stat_fs2.getFreeBlocks() *(double)stat_fs2.getBlockSize();
        
                int MB_Tt2 = (int)(total_int_space / 1048576);
                int MB_Fr2 = (int)(free_int_space / 1048576);
                capacity = String.format("%7d", MB_Tt2) +
                  " Free " +  
                  String.format("%7d", MB_Fr2) +
                  "\n";
            } 
            catch(Exception e)
            {
               capacity = " Not Found\n";
               noDetails = true;
               Toast.makeText(getApplicationContext(),"Cannot Find Drive Details",Toast.LENGTH_LONG).show();
            }

            _myTask = new myWhetTask();  
            _myHandler = new Handler();
            xout[14] = " ****** Test Running - might take > 1 minute\n";
            xout[15] = " ****** File Path " + path + "\n";
            xout[16] = " ****** Drive MB " + capacity;
            displayAll(); 
            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            if (noDetails)
            {
                Intent intent = getIntent();
                finish();
                startActivity(intent);              
            }
            else
            {
                _myHandler.postDelayed(_myTask, 100);
            }
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
        else if(v.getId() == R.id.restartButton)
        {
            Intent intent = getIntent();
            finish();
            startActivity(intent);                         
        }
   }

    @Override
    protected Dialog onCreateDialog(int id) 
    {
        switch (id) 
        {


          case 8:
              Builder builder1 = new AlertDialog.Builder(this);
              builder1.setMessage("Enter File Path");
              final EditText input1 = new EditText(this);
              builder1.setView(input1);                          
              builder1.setCancelable(true);
              builder1.setNegativeButton("OK", new DialogInterface.OnClickListener() 
              {
                  public void onClick(DialogInterface dialog, int which) 
                  {
                        Editable value = input1.getText();
                        path = value.toString();
                        xout[15] = " ****** File Path " + path + "\n";
                        displayAll();
                  }
              });

              builder1.setNeutralButton("Help", new DialogInterface.OnClickListener() 
              {
                    public void onClick(DialogInterface dialog, int which) 
                    {
                        mDisplayDetails.setText(
                      " Original DriveSpeed identified external SD card using\n" + 
                      " standard functions. These could fail to connect the  \n" + 
                      " external SD card on later systems, the internal drive\n" + 
                      " being selected with part of it as Internal Drive. The\n" + 
                      " external SD card can be removed to identify a wrong  \n" + 
                      " selection. DriveSpeed now displays the path used. The\n" + 
                      " path has to be typed in for DriveSpd2 and this can be\n" +                     
                      " used to select USB devices. Often the FileBroxwser app \n" +
                      " identifies the drive directories. Example paths are: \n" +
                      " Internal Drive -  /data/data/com.drivespeed/files/ or\n" +
                      " /data/data/com.drivespeed/                           \n" +
                      " Internal SD Drive - as above, /sdcard/, /mnt/sdcard/,\n" +
                      " /mnt/flash/, /storage/emulated/                      \n" +
                      " External SD Card - /sdcard/, /mnt/sdcard/,           \n" +
                      " /mnt/extsd/, /mnt/external_sdcard/                   \n" +
                      " USB Device - /mnt/sda1/, /mnt/sda/sda1/, /udisk/     \n" +
                      " /mnt/udisk/, Not possible in some cases              \n");                        
                    }
              });
              AlertDialog dialog1 = builder1.create();
              dialog1.show();
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
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android DriveSpeed2 Benchmark Results");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                                           title2 + driveUsed + "\n" +
                                           xout[0]  + xout[1] + xout[2]  + xout[3]  +
                                           xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                                           xout[8]  + xout[9]  + xout[10] + xout[11] +
                                           xout[12] + xout[13] + "\n" + xout[14] +
                                           xout[15] + xout[16] + xout[17] +"\n" +                                     
                                       sout[0]  + sout[1]  + sout[2]  + sout[3]  +
                            sout[4]  + sout[5]  + sout[6]  + sout[7]  + 
                            sout[8]  + sout[9]  + sout[10] + sout[11]);
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                  }
              } );
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
                                        xout[12] + xout[13] + xout[14] +
                                                   xout[15] + xout[16]);
    }
     
    private void resetTest()  
    {  
        if(_myHandler != null)
        {  
            _myHandler.removeCallbacks(_myTask);  
        }  
    }  
    public native String doIt(int size1, String path, int cacheIt, int driveToUse);   

    private class myWhetTask implements Runnable  
    {  
       
        public void run() 
        {
            int cacheIt = 0;
            hasDirectIO = true;
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
            date$ = formatter.format(today);
            xout[0]  = title2;
 
            startTest = System.currentTimeMillis();
            runs =15;

            x0 = doIt(-1, path, cacheIt, driveToUse);
            if (x0.compareTo(" OK") != 0)
            {
                cacheIt = 1;
                hasDirectIO = false;
                driveMsg = " Data Cached";
                driveUsed = "           " + driveMsg + "\n";
            }
            else
            {
                driveMsg = " Data Not Cached";
                driveUsed = "          " + driveMsg + "\n";            
            }
            if (sizea == 88) driveUsed = "      " + " Read Only" + "\n";

            TimeUsed = 0.0;
            xout[2] = "\n";  
            xout[3] = "\n";  
            xout[5] = "\n";  
            xout[9] = "\n";  
            xout[13] = "\n\n";  
            if (sizea == 88)
            {                        
                x0 = doIt(sizea, path, cacheIt, driveToUse);
                xout[2] = x0;
                endTest = System.currentTimeMillis();
                testTime = (double)(endTest - startTest) / 1000.0;
            }
            else
            {
                for (size1=0; size1<runs; size1++)
                {
                   int use;
                   if (size1 == 2 || size1 == 3 || size1 == 5 || size1 == 9 || size1 == 13)
                   {
                       use = size1;
                       if (size1 == 2)
                       {
                           use = 5;
                       }
                       else if (size1 == 5)
                       {
                           use = 2;
                       }
                       x0 = doIt(size1, path, cacheIt, driveToUse);
                       xout[use] = x0;
                       endTest = System.currentTimeMillis();
                       testTime = (double)(endTest - startTest) / 1000.0;
                       if (testTime > 120) size1 = runs;
                   }
                }
            }   
            xout[14] = " No delete\n";
            if (delete == 1)
            {
                size1 = 99;
                x0 = doIt(size1, path, cacheIt, driveToUse);
                xout[14] = "\n"; 
            } 
            xout[0]  = "                     MBytes/Second\n";
            xout[1]  = "  MB    Write1 Write2 Write3  Read1  Read2  Read3\n";

 
            xout[4]  = " Cached\n";
 
            xout[6] = "\n";
            xout[7]  = " Random      Write                Read\n";
            xout[8]  = " From MB     4      8     16      4      8     16\n";

            xout[10] = "\n";
            xout[11] = " 200 Files   Write                Read            Delete \n";
            xout[12] = " File KB     4      8     16      4      8     16   secs \n";
 
    
            xout[14] = "       Total Elapsed Time  " + String.format("%5.1f", testTime)
                    + " seconds\n";
            xout[15] = "       File Path Used - " + path + "\n";
            xout[16] = "  ";
            xout[17] = "       Drive MB " + capacity;
            testDone = 1;
            displayAll();
            resetTest();          
        } 
    }

    public void clear()
    {
        xout[0]  = title2;
        xout[1]  = "\n";
        xout[2]  = " Test 1 - Write and read three 8 and 16 MB files\n";
        xout[3]  = " Test 2 - Write 8 MB, read can be cached in RAM\n";
        xout[4]  = " Test 3 - Random write and read 1 KB from 4 to 16 MB\n";
        xout[5]  = " Test 4 - Write and read 200 files 4 KB to 16 KB\n";
        xout[6]  = "\n";;
        xout[7]  = " Enter path, press Run, Save to Email results\n";
        xout[8]  = " Restart to select new path\n";
        xout[9]  = "\n";
        xout[10] = "\n";
        xout[11] = "\n";
        xout[12] = "\n";   
        xout[13] = "\n";
        xout[14] = "\n"; 
        xout[15] = "\n"; 
        xout[16] = "\n";
        xout[17] = "\n";
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

