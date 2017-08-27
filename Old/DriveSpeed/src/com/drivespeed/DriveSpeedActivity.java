package com.drivespeed;

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

public class DriveSpeedActivity extends Activity  implements View.OnClickListener
{
    private String version = " Android DriveSpeed Benchmark 1.1X ";
    private String title2;
    private String driveUsed;
    private String driveMsg;
    private String drive;
    private Button mStartButton;  
    private Button mStartButton2;  
    private Button mInfoButton;  
    private Button mMailButton;  
    private Handler _myHandler;  
    private Handler _myHandler2;  
    private TextView mDisplayDetails; 
    private Runnable _myTask;  
    private String[] xout = new String[20];
    private String[] sout = new String[20];
    private String[] iout = new String[20];
    private String x0;
    private String xd$;
    private String id$;
    private String date$;
    private String path;
    private String msg1;

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
    private boolean mSDcard;
    private boolean hasDirectIO;
         
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.main); 
        mStartButton = (Button)findViewById(R.id.startButton);  
        mStartButton2 = (Button)findViewById(R.id.startButton2);  
        mInfoButton = (Button)findViewById(R.id.infoButton);  
        mMailButton = (Button)findViewById(R.id.mailButton);  

       
        mStartButton.setOnClickListener(this);  
        mStartButton2.setOnClickListener(this);  
        mInfoButton.setOnClickListener(this);
        mMailButton.setOnClickListener(this);

        x0 = " Not run yet\n\n\n\n\n\n\n\n\n\n\n\n";
        msg1 = " ****** Normal Write, Read, Delete   ******\n";

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
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) 
        {
            mSDcard = true;
            StatFs stat_fs1 = new StatFs(Environment.getExternalStorageDirectory().getPath());
    
            double total_sdm_space = (double)stat_fs1.getBlockCount() *(double)stat_fs1.getBlockSize();
            double free_sdm_space = (double)stat_fs1.getFreeBlocks() *(double)stat_fs1.getBlockSize();
    
            int MB_Tt1 = (int)(total_sdm_space / 1048576);
            int MB_Fr1 = (int)(free_sdm_space / 1048576);
    
            xd$ = " SD Card        MB " +
                   String.format("%7d", MB_Tt1) +
                   " Free " +
                    String.format("%7d", MB_Fr1) +
                    "\n";
        }
        else
        {
           xd$ = " SD Card Not Available\n";
           mSDcard = false;
           Toast.makeText(getApplicationContext(),"SD Card not available to write and read",Toast.LENGTH_LONG).show();
        }
        StatFs stat_fs2 = new StatFs(getFilesDir().getAbsolutePath());
  
        double total_int_space = (double)stat_fs2.getBlockCount() *(double)stat_fs2.getBlockSize();
        double free_int_space = (double)stat_fs2.getFreeBlocks() *(double)stat_fs2.getBlockSize();

        int MB_Tt2 = (int)(total_int_space / 1048576);
        int MB_Fr2 = (int)(free_int_space / 1048576);

        id$ = " Internal Drive MB " +
              String.format("%7d", MB_Tt2) +
              " Free " +  
              String.format("%7d", MB_Fr2) +
              "\n";
        mDisplayDetails.setTextSize(TypedValue.COMPLEX_UNIT_PX, pixels); 
        displayAll();

    }      
    static 
    {
        System.loadLibrary("drivespeedlib");
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
             if (mSDcard)
             {  
                 driveToUse = 0;
                 path = Environment.getExternalStorageDirectory().getPath() + "/";
                 drive = "        SD Card";
                 clear();
                _myTask = new myWhetTask();  
                _myHandler = new Handler();
                xout[15] = " ****** Running Might Take 2 Minutes ******\n";
                xout[16] = msg1;
                displayAll(); 
                Toast.makeText(getApplicationContext(),xd$,Toast.LENGTH_LONG).show();
                startTest = System.currentTimeMillis();
                TimeUsed = 0.0;
                _myHandler.postDelayed(_myTask, 100);
             }
             else
             {
                Toast.makeText(getApplicationContext(),"SD Card not available to write and read",Toast.LENGTH_LONG).show();
             }
        }
        else if(v.getId() == R.id.startButton2)
        {
            driveToUse = 1;
            path = getFilesDir().getAbsolutePath() + "/";
            drive = "    Internal Drive";
            clear();
            _myTask = new myWhetTask();  
            _myHandler = new Handler();  
              
            xout[15] = " ****** Running Might Take 2 Minutes ******\n";
            xout[16] = msg1;
            displayAll();
            Toast.makeText(getApplicationContext(),id$,Toast.LENGTH_LONG).show();
            startTest = System.currentTimeMillis();
            TimeUsed = 0.0;
            _myHandler.postDelayed(_myTask, 100);              
        }  
        else if(v.getId() == R.id.infoButton)
        {
                showDialog(5);          
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
          case 5:
            Builder builder5 = new AlertDialog.Builder(this);
            builder5.setMessage("Delete 3 x 8 MB at end, Read only ");
            builder5.setCancelable(true);
            delete = 1;
            sizea  = 0;
            msg1 = " ****** Normal Write, Read, Delete   ******\n";
            builder5.setPositiveButton("Don't Delete", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    delete = 0;
                    msg1 = " *** Write, Read, Don't Delete 3 x 8 MB ***\n";
                }
            });

            builder5.setNeutralButton("Read Only", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    sizea = 88;
                    msg1 = " ****** Read Only, Delete All Files  ******\n";
                }
            });

            builder5.setNegativeButton("Both", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    sizea = 88;
                    delete = 0;
                    msg1 = " **** Read Only, Don't Delete 3 x 8 MB ****\n";
               }
            });
            AlertDialog dialog5 = builder5.create();
            dialog5.show();
            break;
          
          
          case 7:
            Builder builder3 = new AlertDialog.Builder(this);
            builder3.setMessage("Finished - delete three 8 MB files");
            builder3.setCancelable(true);
            builder3.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    delete = 1;
                }
            });

            builder3.setNeutralButton("No", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    delete = 0;
                }
            });

            AlertDialog dialog3 = builder3.create();
            dialog3.show();
            break;
            
           case 8:
            Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Details and Results Web Pages");
            builder.setCancelable(true);
            builder.setPositiveButton("About Summary", new DialogInterface.OnClickListener() 
            {
                public void onClick(DialogInterface dialog, int which) 
                {
                    mDisplayDetails.setText(
                  " The first version of DriveSpeed measures performance \n" + 
                  " of external SD cards. As most of my other Android    \n" + 
                  " benchmarks, the code has a Java front end with most  \n" + 
                  " work carried out in C, using the Native Development  \n" + 
                  " Kit. Compared with Linux and Windows, programming to \n" + 
                  " write to and read from drives is incomprehensible,   \n" + 
                  " using Android. For this benchmark, the files are     \n" +                     
                  " opened in C, using file path /sdcard/, and this      \n" +
                  " might not be appropriate for all devices.            \n" +
                  " Four types of test are run, the first writing and    \n" +
                  " reading large files, where data should not be cached \n" +
                  " in memory. Test 2 allows caching where reading speed \n" +
                  " should reflect RAM speeds. Test 3 uses random write  \n" +
                  " and read. The last test writes and reads 200 small   \n" +
                  " files. Writing tends to be slow using Flash Memory.  \n" +
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
            builder.setNegativeButton("DriveSpeed Results on PCs", new DialogInterface.OnClickListener() 
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
                        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android DriveSpeed Benchmark Results");
                        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, 
                                           title2 + driveUsed + "\n" +
                                           xout[0]  + xout[1] + xout[2]  + xout[3]  +
                                           xout[4]  + xout[5]  + xout[6]  + xout[7]  +
                                           xout[8]  + xout[9]  + xout[10] + xout[11] +
                                           xout[12] + xout[13] + xout[14] +
                                           xout[15] + xout[16] +                                    
                                       sout[0]  + sout[1]  + id$ + xd$ + sout[2]  + sout[3]  +
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
                driveUsed = "      " + drive + driveMsg + "\n";
            }
            else
            {
                driveMsg = " Data Not Cached";
                driveUsed = "    " + drive + driveMsg + "\n";            
            }
            if (sizea == 88) driveUsed = "      " + drive + " Read Only" + "\n";

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
                xout[14] = " Files Deleted\n"; 
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
 
    
            xout[15] = "          Total Elapsed Time  " + String.format("%5.1f", testTime)
                    + " seconds\n";
            xout[16] = "           Path Used " + path; // driveUsed;
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
        xout[7]  = " Use RunS to test SD card, RunI for internal drive,\n";
        xout[8]  = " Email to save results.\n";
        xout[9]  = "\n";
        xout[10] = " Android might force data to be cached, producing\n";
        xout[11] = " results that represent memory data transfer speed.\n";
        xout[12] = " In this case, Use More button to choose not to del-\n";   
        xout[13] = " ete files, switch off and reboot, use More again to\n select read only when RunS or RunI next used.\n";
        xout[14] = "\n"; 
        xout[15] = " Can take a while, time out after 120+ seconds\n"; 
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
}

