package com.batterytest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Intent;
import android.content.Context;

import android.view.View;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import android.graphics.Paint;
import android.graphics.Color;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.Matrix;
import android.graphics.LinearGradient;
import android.graphics.SweepGradient;
import android.graphics.Shader;
import android.widget.Toast;


import android.util.Log;
import android.util.DisplayMetrics;

import java.util.Random;
import java.io.IOException;
import java.io.InputStream;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RunActivity extends Activity
{
    private String results;
    Paint circlePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);  
    private MyView mMyView;
    private static double mhz;
    private static double mhzSum = 0;
    private String filePath1;
    private String fileName = "BatteryTest.txt";
    private File file;
    private String thisMessage;
    private String headerMessage;
    private String resultMessage;
    private String runDetails;
    private String logFile;
    private String date$;
    private FileOutputStream fos;
    private Intent in1 = new Intent();
    private int runSecs = 1;
    private int sixty = 60;
    private int wpass = 0;
    
    @Override
    protected void onPause() 
    {
        super.onPause();
    }

    @Override
    protected void onResume() 
    {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        mMyView = new MyView(this);
        setContentView(mMyView);

        Bundle b = getIntent().getExtras();
        runSecs = b.getInt("doSecs");


        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH.mm");
        date$ = formatter.format(today);

        filePath1 = Environment.getExternalStorageDirectory().getPath();
        file = new File(filePath1, fileName);
       
        runDetails = String.format(" Up to 60 %d second runs, MHz 1 sample/frame\n", runSecs);
        logFile = String.format(" Log File %s/%s\n\n", filePath1, fileName);
        resultMessage = String.format( " Run    FPS   MHz   Run    FPS   MHz\n");
        headerMessage = " Last Results " + date$ + "\n" + runDetails + logFile + resultMessage;
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

    class MyView extends SurfaceView implements SurfaceHolder.Callback 
    {
        private Bitmap mbitmap;
        Context context;
        MyThread  mythread;
        private int frames = 0;
        private String msg;
        private double startTime;
        private double startFirstTime;
        private double runTime = 0;
        private double runTimeAll = 0;
        private double fps;
        private int test = 4;
        private int count = 0;
        private int flip = 0;
        private int saves = 1;
        private double[] frames_ps = new double[100];
        private int[] mhza = new int[100];
     
        private int updown = 1;   
        private int pos  = 20;
        private int pixWide;  
        private int pixHigh;
        private int pixMin; 
        private int ud2  = 3;
        private int circ = 120;
        private int grn = 0;
        private int gch = 3;
        Paint apaint = new Paint();
       
        DisplayMetrics metrics;
    
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);    
        Paint circlePaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        Paint circlePaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        Paint circlePaint3 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        Paint circlePaint4 = new Paint(Paint.ANTI_ALIAS_FLAG);  
        SweepGradient sweepGrad1;
        SweepGradient sweepGrad2;
        Typeface mType;


        boolean mRun;

        Random randNum = new Random();
        SurfaceHolder holder = getHolder();
        
        public MyView(Context ctx) 
        {
            super(ctx);
            context = ctx;
            metrics = context.getResources().getDisplayMetrics();
            pixHigh = metrics.heightPixels;
            pixWide = metrics.widthPixels;
            pixMin  = pixWide;
            if (pixHigh < pixWide) pixMin  = pixHigh;
            mType = Typeface.create(Typeface.MONOSPACE,  Typeface.NORMAL);     
            mPaint.setTextSize(24);
            mPaint.setColor(Color.WHITE); 
            mPaint.setTypeface(mType);  
            randNum.setSeed(999);   
            startTime = (double)System.currentTimeMillis();
            startFirstTime = startTime;
            holder.addCallback(this);             
        }
        public void surfaceCreated(SurfaceHolder holder) 
        {
            mythread = new MyThread(holder, context, this);
            mythread.setRunning(true);
            mythread.start();
        }
        
        public void surfaceDestroyed(SurfaceHolder holder) 
        {
            mythread.setRunning(false);
            boolean retry = true;
            while(retry)
            {
                try
                {
                    mythread.join();
                    retry = false;
                }
                catch(Exception e)
                {     
                   Log.v("Exception Occured", e.getMessage());
                }
            }   
        }
        
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) 
        {
        
        }
        public class MyThread extends Thread
        {
            Canvas mcanvas;
            SurfaceHolder surfaceHolder;
            Context context;
            MyView mView;

            public MyThread(SurfaceHolder sholder, Context ctx, MyView spanel)    
            {
                surfaceHolder = sholder;        
                context = ctx;
                mRun = false;
                mView = spanel;
            }
            
            void setRunning(boolean bRun)
            {   
                mRun = bRun;
            }
            
            @Override
            public void run()
            {
                super.run();
                while(mRun)
                {
                     mcanvas = surfaceHolder.lockCanvas();
                     if(mcanvas != null)
                     {
                         mView.doDraw(mcanvas);
                         surfaceHolder.unlockCanvasAndPost(mcanvas); 
                     }
               }
    
            }
        }

        void doDraw(Canvas canvas)
        {
            int i;
            int d;
            canvas.drawColor(Color.rgb(0, grn, 255));
    
            frames = frames + 1;
        
            if (test > 2)
            {
                LinearGradient linGrad = new LinearGradient(0, 0, 5, 5,  Color.WHITE, Color.BLACK,  Shader.TileMode.MIRROR);  
                int c2 = 5; 
                circlePaint3.setShader(linGrad);
                for (i=0; i<50; i++)  
                {
                    int r1 = (int)(randNum.nextFloat() * (float)pixMin / 4);
                    int r2 = (int)(randNum.nextFloat() * (float)pixMin / 4);
                    canvas.drawCircle(canvas.getWidth() / 2 + r1,  canvas.getHeight() / 2 + r2, c2, circlePaint3);
                    canvas.drawCircle(canvas.getWidth() / 2 - r1,  canvas.getHeight() / 2 - r2, c2, circlePaint3);
                    canvas.drawCircle(canvas.getWidth() / 2 + r1,  canvas.getHeight() / 2 - r2, c2, circlePaint3);
                    canvas.drawCircle(canvas.getWidth() / 2 - r1,  canvas.getHeight() / 2 + r2, c2, circlePaint3);
                }
            }

            if (test > 4)
            {
                LinearGradient linGrad2 = new LinearGradient(0, 0, 5, 5,  Color.WHITE, Color.BLACK,  Shader.TileMode.MIRROR);  
                int c22 = 5;
                circlePaint4.setShader(linGrad2);
                for (i=0; i<1000; i++)  
                {
                    int r12 = (int)(randNum.nextFloat() * (float)canvas.getWidth() / 2);
                    int r22 = (int)(randNum.nextFloat() * (float)canvas.getHeight() / 2);
                    canvas.drawCircle(canvas.getWidth() / 2 + r12,  canvas.getHeight() / 2 + r22, c22, circlePaint4);
                    canvas.drawCircle(canvas.getWidth() / 2 - r12,  canvas.getHeight() / 2 - r22, c22, circlePaint4);
                    canvas.drawCircle(canvas.getWidth() / 2 + r12,  canvas.getHeight() / 2 - r22, c22, circlePaint4);
                    canvas.drawCircle(canvas.getWidth() / 2 - r12,  canvas.getHeight() / 2 + r22, c22, circlePaint4);
                }
            }
            float ih;
            float iw;
            if (test > 3)
            {
                for (i=0; i<80; i++)
                {
                    ih = (Float)(canvas.getHeight() / 80 * (float)i);
                    iw = (Float)(canvas.getWidth() / 80 * (float)i);
                    apaint.setColor(Color.rgb(grn, 0, 0)); 
                    canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), (int)ih, apaint);
                    canvas.drawLine(canvas.getWidth() / 2, 0, (int)iw, canvas.getHeight(), apaint);
                    apaint.setColor(Color.rgb(255, grn, 0)); 
                    canvas.drawLine(canvas.getWidth(), canvas.getHeight() / 2, 0, (int)ih, apaint);
                    canvas.drawLine(canvas.getWidth() / 2, canvas.getHeight(), (int)iw, 0, apaint);
                }
            }

            if (test > 0)
            {
                mbitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bground);
                canvas.drawBitmap(mbitmap, pos/2, 50, null); // pos/4
                Matrix mirrorAndTilt30 = new Matrix(); 
                mirrorAndTilt30.preRotate(pos);          // pos/2
                mirrorAndTilt30.preScale(1, 1);
                Bitmap mirrorPic = Bitmap.createBitmap(mbitmap, 0, 0,  mbitmap.getWidth(),
                mbitmap.getHeight(), mirrorAndTilt30, false);
                canvas.drawBitmap(mirrorPic, pixWide - mbitmap.getWidth() * 5 / 4 - pos/5, 50, null);
                mbitmap.recycle();
            }
            ReadCPUMhz();
            mhzSum = mhzSum + mhz;
            if (test > 1)
            {
                int c1 = pixMin / 6;
                sweepGrad1 = new  SweepGradient (canvas.getWidth()-circ,  canvas.getHeight()-circ,  new int[] 
                { 
                     Color.RED, Color.YELLOW, Color.GREEN,  Color.BLUE, Color.MAGENTA 
                }, null);
                circlePaint1.setShader(sweepGrad1);  
                canvas.drawCircle(canvas.getWidth()-circ,  canvas.getHeight()-circ, c1,  circlePaint1);
        
        
                sweepGrad2 = new  SweepGradient (circ,  canvas.getHeight()-circ,  new int[] 
                { 
                     Color.GREEN, Color.RED, Color.BLUE,  Color.YELLOW, Color.MAGENTA 
                }, null);
                circlePaint2.setShader(sweepGrad2);  
                canvas.drawCircle(circ, canvas.getHeight()-circ, c1,  circlePaint2);
        
                circ = circ + ud2;
                if (circ > c1 * 3) ud2 = -3;
                if (circ < c1) ud2 = 3;
            }
            
            runTime = ((double)System.currentTimeMillis() - startTime) / 1000;
            runTimeAll = ((double)System.currentTimeMillis() - startFirstTime) / 60000;
            fps = (double)frames / runTime;
            msg = String.format(" %6.1f FPS, MHz %d, %6.1f minutes", fps, (int)mhz, runTimeAll);
            canvas.drawText(msg, 10, 30, mPaint);

            pos = pos + 1 * updown;
            if (pos > pixMin) updown = -1;
            if (pos < 20 ) updown = 1;

            if (runTime > runSecs)
            {
                frames_ps[count] = fps;
                mhzSum = mhzSum/(double)frames;
                mhza[count] = (int)mhzSum;
                flip = flip + 1;
                if (flip == 2)
                {
                    thisMessage =  String.format("%4d%7.1f%6d %5d%7.1f%6d\n", 
                    saves, frames_ps[count-1], mhza[count-1], saves+1, frames_ps[count], mhza[count]);
                    if (wpass == 0)
                    {
                        thisMessage = headerMessage + thisMessage;
                    }
                    flip = 0;
                    saves = saves + 2;
                    try
                    {
                       if (wpass == 0)
                       {
                           fos = new FileOutputStream(file);
                           wpass = 1;
                       }
                       else
                       { 
                           fos = new FileOutputStream(file, true);
                       }
                    }
                    catch(Exception e)
                    {
                        resultMessage = " Write Failed to open " + filePath1 + " " + fileName + "\n";
                        results = resultMessage;
                        in1.putExtra("ComingFrom", results);
                        setResult(1,in1);
                        finish();
                    }
                    writeFile();
                }

                mhzSum = 0;
                frames = 0;
                startTime = (double)System.currentTimeMillis();
                count = count + 1;
            }
            if (count == sixty)
            {
                  resultMessage = String.format( 
                        "Run    FPS   MHz   Run    FPS   MHz\n");
                 for (d=0; d<60; d=d+2)
                 {           
                      resultMessage = String.format("%s%3d%7.1f%6d %5d%7.1f%6d\n",
                                    resultMessage, d+1, frames_ps[d], mhza[d], d+2, frames_ps[d+1], mhza[d+1]);       
                 }
                 results = runDetails + logFile + resultMessage;
                 Intent in = new Intent();
                 in.putExtra("ComingFrom", results);
                 setResult(1,in);
                 finish();
            }

        }
    }

    public int writeFile()
    {
        try
        {
            fos.write(thisMessage.getBytes());
        }
        catch (FileNotFoundException e) 
        { 
            thisMessage = " FILE NOT FOUND " + filePath1 + " " + fileName + "\n";
            in1.putExtra("ComingFrom", thisMessage);
            setResult(1,in1);
            finish();
        } 
        catch (IOException ioe) 
        { 
            thisMessage = " Writing Failure " + filePath1 + " " + fileName + "\n";
            in1.putExtra("ComingFrom", thisMessage);
            setResult(1,in1);
            finish();
        } 
        finally 
        { 
            try 
            { 
                if (fos != null) 
                { 
                     fos.close(); 
                } 
            } 
            catch (IOException ioe) 
            { 
            thisMessage = " Failure CLOSING FILE " + filePath1 + " " + fileName + "\n";
            in1.putExtra("ComingFrom", thisMessage);
            setResult(1,in1);
            finish();
            } 
        } 
        return 1;
    }
}


