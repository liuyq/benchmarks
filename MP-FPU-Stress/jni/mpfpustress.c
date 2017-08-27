 #include <stdio.h>       
 #include <stdlib.h>
 #include <string.h>
 #include <math.h>
 #include <time.h>
 #include <jni.h>
 #include <pthread.h> 


 float  x_cpu[4000000];
 double runSecs = 0.1;
 double  startSecs;
 double  secs;
 char    resultchars[1200];

 typedef struct 
 { 
    float      *x; 
    int        xlen; 
 }
 MYCALCS;

 MYCALCS xcalcs;

 pthread_t tid[100]; 
 pthread_attr_t * attrt = NULL; 
 pthread_mutex_t mutext = PTHREAD_MUTEX_INITIALIZER;

 int     words[3]; 
 int     repeats[3]; 
 int     runrepeats;
 int     opwd;
 int     threads;
 float   xval = 0.999950f;
 float   aval = 0.000020f;
 float   bval = 0.999980f;
 float   cval = 0.000011f;
 float   dval = 1.000011f;
 float   eval = 0.000012f;
 float   fval = 0.999992f;
 float   gval = 0.000013f;
 float   hval = 1.000013f;
 float   jval = 0.000014f;
 float   kval = 0.999994f;
 float   lval = 0.000015f;
 float   mval = 1.000015f;
 float   oval = 0.000016f;
 float   pval = 0.999996f;
 float   qval = 0.000017f;
 float   rval = 1.000017f;
 float   sval = 0.000018f;
 float   tval = 1.000018f;
 float   uval = 0.000019f;
 float   vval = 1.000019f;
 float   wval = 0.000021f;
 float   yval = 1.000021f;

 void triad(int n, float a, float b, float *x)
 {
     int i, ii;

   for(ii=0; ii<runrepeats; ii++)
   {
     for(i=0; i<n; i++)
     x[i] = (x[i]+a)*b;
   }
 }


 void triadplus(int n, float a, float b, float c, float d, float e, float f, float *x)
 {
     int i, ii;

    for (ii=0; ii<runrepeats; ii++)
    {
       for(i=0; i<n; i++)
       x[i] = (x[i]+a)*b-(x[i]+c)*d+(x[i]+e)*f;
    }
 }


 void triadplus2(int n, float a, float b, float c, float d, float e, float f, float g, float h, float j, float k, float l, float m, float o, float p, float q, float r, float s, float t, float u, float v, float w, float y, float *x)
 {
    int i, ii;
    for (ii=0; ii<runrepeats; ii++)
    {
       for(i=0; i<n; i++)
       {
          x[i] = (x[i]+a)*b-(x[i]+c)*d+(x[i]+e)*f-(x[i]+g)*h+(x[i]+j)*k-(x[i]+l)*m+(x[i]+o)*p-(x[i]+q)*r+(x[i]+s)*t-(x[i]+u)*v+(x[i]+w)*y;
       }
    }
 } 




 double getTime()
 {
     struct timespec tp1;
     clock_gettime(CLOCK_REALTIME, &tp1);
     return (double)tp1.tv_sec + (double)tp1.tv_nsec / 1e9;
 }
 
 void start_time()
  {
      startSecs = getTime();
      return;
  }

  void end_time()
  {
      secs = getTime() - startSecs;
      return;
  }

 void *runTests(void *arg)
 {
    int  i;
    int  wds;
    long offset;
    float *xcp;
    
    offset = (long)arg;

    wds = xcalcs.xlen;
    xcp = xcalcs.x + offset * wds;
        
       // calculations in CPU
       if (opwd == 2)
       {
          triad(wds, aval, xval, xcp);
       }
       if (opwd == 8)
       {
          triadplus(wds, aval, bval, cval, dval, eval, fval, xcp);
       }
       if (opwd == 32)
       {
          triadplus2(wds, aval, bval, cval, dval, eval, fval, gval, hval, jval, kval, lval, mval, oval, pval, qval, rval, sval, tval, uval, vval, wval, yval,  xcp);
       }   
 }


jstring
Java_com_mpfpustress_MPFPUStressActivity_stringFromJNI( JNIEnv* env, jobject thiz, jint jthreads, jint jopspw, jint stressIt, jint jKBcode, jint rmult)
{
    int  i, p;
    int  pStart = 0;
    int  pEnd = 3;
    int  isok1 = 0;
    long ii;
    float   newdata = 0.999999f;
    float   fpmops[36];
    float   mflops[36];
    int     results[36];
    int     op = 0;

    words[0]   = 3200;           // 3200
    words[1]   = words[0] * 10;  // 32000
    words[2]   = words[1] * 100; // 3200000
    repeats[0] = 100000 * rmult;         // 100000;
    repeats[1] = repeats[0] / 10;
    repeats[2] = repeats[1] / 100;   
    
    if (stressIt == 1)
    {
        pEnd = jKBcode;
        pStart = pEnd - 1;
        op = pStart;
    }

    opwd = jopspw;
    threads = jthreads;
    {    

            for (p=pStart; p<pEnd; p++)
            {
               xcalcs.x = x_cpu;
               xcalcs.xlen = words[p] / threads;
               runrepeats = repeats[p];
  
               // Data for array
               for (i=0; i<words[p]; i++)
               {
                  x_cpu[i] = newdata;
               }

               start_time();
               for (ii=0; ii<threads; ii++)
               {
                   pthread_create(&tid[ii], attrt, runTests, (void *)ii);
               }
    
               for(ii=0;ii<threads;ii++)
               {
                  pthread_join(tid[ii], NULL);
               }
               end_time();
               fpmops[op] = (float)words[p] * (float)opwd;
               mflops[op] = (float)repeats[p] * fpmops[op] / 1000000.0f / (float)secs;
               results[op] = (int)(x_cpu[0] * 100000);
               isok1  = 0;
               float one = x_cpu[0];
               for (i=1; i<words[p]; i++)
               {
                  if (one != x_cpu[i])
                  {
                     isok1 = 1;
                     results[op] = 0;
                     i = words[p];
                  }
               }               
               op = op + 1; 
            }
    }

    if (stressIt == 0)
    {
       sprintf(resultchars, "  T%d  %2d%6d%6d%6d %7d%7d%7d", 
                           threads, opwd,
                           (int)mflops[0], (int)mflops[1], (int)mflops[2],
                           results[0], results[1], results[2]);
    }
    else
    {
       sprintf(resultchars, "%8d%8d%8d%8d", 
                           threads, opwd,
                           (int)mflops[pStart], results[pStart]);
    }

    return (*env)->NewStringUTF(env, resultchars);
}


