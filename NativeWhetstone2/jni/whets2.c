/*
*/

#include <jni.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h> 


float x, y, z;
double TimeUsed = 0.0;

double getTime()
{
  struct timespec tp1;
  clock_gettime(CLOCK_REALTIME, &tp1);
  return (double)tp1.tv_sec * 1000.0 + (double)tp1.tv_nsec / 1e6;
}

    void pa(float e[4], float t, float t2)
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

jstring
Java_com_natwhet2_NativeWhetstone2Activity_stringFromJNI( JNIEnv* env, jobject thiz, jint section )
{
    char resultchars[1000];

    double startTime;
    double endTime;
    double runTime;
    double loop_time;
    double loop_mflops;
    double loop_mops;

    float e1[4];  
    float t  =  0.49999975f;
    float t1 =  0.50000025f;
    float Check = 0.0f;
    float t0 = t;  
    float t2 = 2.0f;
    float results;
    float mwips; 

    int x100 = 100;
    int n1 = 12*x100;
    int n2 = 14*x100;
    int n3 = 345*x100;
    int n4 = 210*x100;
    int n5 = 32*x100;
    int n6 = 899*x100;
    int n7 = 616*x100;
    int n8 = 93*x100;
    int i, j, k, l, ix, xtra, n1mult;
    int ismflops;
    int num[4];

    char titles[25];
    char mops[20];
    char mflops[20];

    num[0] = 0;
    num[1] = 1;
    num[2] = 2;
    num[3] = 3;


    if (section == 1)
    { 
       // Section 1, Array elements

            TimeUsed = 0.0;
                
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
                startTime = getTime();
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
                    if (ix == 0) results = e1[3];
                }
                t =  t0;                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + e1[3];              
            loop_time = runTime / (double)n1mult / (double)xtra;
            loop_mflops = (double)(n1*16) / 1000000.0 / loop_time;
            loop_mops = 0.0;
            sprintf (titles, " N1 float");
            ismflops = 1;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 2)
    {
        // Section 2, Array as parameter
 
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
                startTime = getTime();
                for (ix=0; ix<xtra; ix++)
                {
                    for(i=0; i<n2; i++)
                    {   
                          pa(e1,t,t2);
                    }
                    t = 1.0f - t;
                    if (ix == 0) results = e1[3];
                }
                t =  t0;                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + e1[3];              
            loop_time = runTime / (double)xtra;
            loop_mflops = (double)(n2*96) / 1000000.0 / loop_time;
            loop_mops = 0.0;
            sprintf (titles, " N2 float");
            ismflops = 1;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 3)
    {
         // Section  3, Conditional jumps

            runTime = 0.0;
            j = 1;
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
                startTime = getTime();
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
                        if (ix == 0) results = (float)j;
                }                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + (float)j;              
            loop_time = runTime / (double)xtra;
            loop_mops = (double)(n3*3) / 1000000.0 / loop_time;
            loop_mflops = 0.0;
            sprintf (titles, " N3 if   ");
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 4)
    {
        // Section 4, Integer arithmetic
 
           runTime = 0.0;
           j = 1;
           k = 2;
           l = 3;
           xtra = 1;
            do
            {
                e1[0] = 0;
                e1[1] = 0;
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
                startTime = getTime();
                for (ix=0; ix<xtra; ix++)
                {
                        for(i=0; i<n4; i++)
                        {
                             j = num[1] *(k-j)*(l-k);
                             k = num[3] * k - (l-j) * k;
                             l = (l-k) * (num[2]+j);
                             e1[l-2] = e1[l-2] + j + k + l;
                             e1[k-2] = e1[k-2] + j * k * l;
                        }
                        if (ix == 0) results = (e1[0]+e1[1])/(float)n4;
                }                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + e1[0]+e1[1];              
            loop_time = runTime / (double)xtra;
            loop_mops = (double)(n4*15) / 1000000.0 / loop_time;
            loop_mflops = 0.0;
            sprintf (titles, " N4 fixpt");
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 5)
    {
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
                startTime = getTime();
                for (ix=0; ix<xtra; ix++)
                {
                        for(i=0; i<n5; i++)
                        {
                              x = t*atan(t2*sin(x)*cos(x)/(cos(x+y)+cos(x-y)-1.0));
                              y = t*atan(t2*sin(y)*cos(y)/(cos(x+y)+cos(x-y)-1.0));
                        }
                        t = 1.0 - t;
                        if (ix == 0) results = y;
                }
                t = t0;                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + y;              
            loop_time = runTime / (double)xtra;
            loop_mops = (double)(n5*26) / 1000000.0 / loop_time;
            loop_mflops = 0.0;
            sprintf (titles, " N5 cos  ");
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 6)
    {
        // Section 6 Floating Point      

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
                startTime = getTime();
                for (ix=0; ix<xtra; ix++)
                {
                        for(i=0; i<n6; i++)
                        {
                          p3(t,t1,t2);
                        }                      
                        if (ix == 0) results = z;
                }                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + z;              
            loop_time = runTime / (double)xtra;
            loop_mflops = (double)(n6*6) / 1000000.0 / loop_time;
            loop_mops = 0.0;
            sprintf (titles, " N6 float");
            ismflops = 1;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 7)
    {
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
                startTime = getTime();
                for (ix=0; ix<xtra; ix++)
                {
                        for(i=0; i<n7; i++)
                        {
                            po(e1,j,k,l);
                        }                       
                        if (ix == 0) results = e1[2];
                }                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + e1[2];              
            loop_time = runTime / (double)xtra;
            loop_mops = (double)(n7*3) / 1000000.0 / loop_time;
            loop_mflops = 0.0;
            sprintf (titles, " N7 equal");
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 8)
    {                
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
                startTime = getTime();
                for (ix=0; ix<xtra; ix++)
                {
                        for(i=0; i<n8; i++)
                        {
                           x = sqrt(exp(log(x)/t1));
                        }                       
                        if (ix == 0) results = x;
                }                    
                endTime = getTime();
                runTime = (endTime - startTime) / 1000.0;
            }
            while (runTime < 1.0);
            Check = Check + x;              
            loop_time = runTime / (double)xtra;
            loop_mops = (double)(n8*4) / 1000000.0 / loop_time;
            loop_mflops = 0.0;
            sprintf (titles, " N8 exp  ");
            ismflops = 0;
            TimeUsed = TimeUsed + loop_time;
    }
    else if (section == 9)
    {                
          mwips = (float)x100 / (float)(10.0 * TimeUsed);
          sprintf(resultchars, " MWIPS    %9.2f         %10.3f\n", mwips,  TimeUsed*1000.0);
    }
    if (section < 9)
    {
            if (ismflops == 1)
            {
                sprintf (mflops, "%9.2f",  loop_mflops);
                sprintf (mops, "        ");
            }
            else
            {
                sprintf (mops, "%9.2f",  loop_mops);
                sprintf (mflops, "        ");
            }

            sprintf(resultchars, "%9s %9s%9s%10.3f  %13.9f\n", titles, mflops, mops, loop_time*1000.0, results );
    }
    return (*env)->NewStringUTF(env, resultchars);
}
