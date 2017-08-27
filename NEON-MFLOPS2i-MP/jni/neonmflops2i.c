/*
  ~/workspace/MemSpeed/jni$ ~/Eclipse/android-ndk-r7/ndk-build
*/



 #include <stdio.h>
 #include <stdlib.h>
 #include <string.h>
 #include <math.h>
 #include <time.h> 
 #include "jni.h"
 #include <pthread.h> 
 #include <cpu-features.h>
 #include <arm_neon.h>

 float  x_cpu[4000000];
 double runSecs = 0.1;
 double startSecs;
 double  secs;
 char resultchars[1000];

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
 int     part;
 int     opwd;

 float32_t  xv[4] = {0.999950f, 0.999950f, 0.999950f, 0.999950f};
 float32_t  av[4] = {0.000020f, 0.000020f, 0.000020f, 0.000020f};
 float32_t  bv[4] = {0.999980f, 0.999980f, 0.999980f, 0.999980f};
 float32_t  cv[4] = {0.000011f, 0.000011f, 0.000011f, 0.000011f};
 float32_t  dv[4] = {1.000011f, 1.000011f, 1.000011f, 1.000011f};
 float32_t  ev[4] = {0.000012f, 0.000012f, 0.000012f, 0.000012f};
 float32_t  fv[4] = {0.999992f, 0.999992f, 0.999992f, 0.999992f};
 float32_t  gv[4] = {0.000013f, 0.000013f, 0.000013f, 0.000013f};
 float32_t  hv[4] = {1.000013f, 1.000013f, 1.000013f, 1.000013f};
 float32_t  jv[4] = {0.000014f, 0.000014f, 0.000014f, 0.000014f};
 float32_t  kv[4] = {0.999994f, 0.999994f, 0.999994f, 0.999994f};
 float32_t  lv[4] = {0.000015f, 0.000015f, 0.000015f, 0.000015f};
 float32_t  mv[4] = {1.000015f, 1.000015f, 1.000015f, 1.000015f};
 float32_t  ov[4] = {0.000016f, 0.000016f, 0.000016f, 0.000016f};
 float32_t  pv[4] = {0.999996f, 0.999996f, 0.999996f, 0.999996f};
 float32_t  qv[4] = {0.000017f, 0.000017f, 0.000017f, 0.000017f};
 float32_t  rv[4] = {1.000017f, 1.000017f, 1.000017f, 1.000017f};
 float32_t  sv[4] = {0.000018f, 0.000018f, 0.000018f, 0.000018f};
 float32_t  tv[4] = {1.000018f, 1.000018f, 1.000018f, 1.000018f};
 float32_t  uv[4] = {0.000019f, 0.000019f, 0.000019f, 0.000019f};
 float32_t  vv[4] = {1.000019f, 1.000019f, 1.000019f, 1.000019f};
 float32_t  wv[4] = {0.000021f, 0.000021f, 0.000021f, 0.000021f};
 float32_t  yv[4] = {1.000021f, 1.000021f, 1.000021f, 1.000021f};


 void triad(int n, float32_t *a, float32_t *b, float32_t *x)
 {
     int i;
     float32x4_t a41, b41, x41, z41;

     float32_t   *ptra1 = a;
     float32_t   *ptrb1 = b;
     float32_t   *ptrx1 = x;

     a41 = vld1q_f32(ptra1);
     b41 = vld1q_f32(ptrb1);
     
     for(i=0; i<n; i=i+4)
     {
         x41 = vld1q_f32(ptrx1);

         z41 = vaddq_f32(x41, a41);
         z41 = vmulq_f32(z41, b41);

         vst1q_f32(ptrx1, z41);

         ptrx1 = ptrx1 + 4;
     }
 }
 
 void triadplus2(int n, float32_t *a, float32_t *b, float32_t *c, float32_t *d,
                         float32_t *e, float32_t *f, float32_t *g, float32_t *h,
                         float32_t *j, float32_t *k, float32_t *l, float32_t *m,
                         float32_t *o, float32_t *p, float32_t *q, float32_t *r,
                         float32_t *s, float32_t *t, float32_t *u, float32_t *v,
                         float32_t *w, float32_t *y, float32_t *x)
 {
     int i;
     float32x4_t a41, b41, c41, d41, e41, f41, g41, h41,
                 j41, k41, l41, m41, o41, p41, q41, r41,
                 s41, t41, u41, v41, w41, y41;
     float32x4_t x41;
     float32x4_t z41, z42;

     float32_t   *ptra1 = a;
     float32_t   *ptrb1 = b;
     float32_t   *ptrc1 = c;
     float32_t   *ptrd1 = d;
     float32_t   *ptre1 = e;
     float32_t   *ptrf1 = f;
     float32_t   *ptrg1 = g;
     float32_t   *ptrh1 = h;
     float32_t   *ptrj1 = j;
     float32_t   *ptrk1 = k;

     float32_t   *ptrl1 = l;
     float32_t   *ptrm1 = m;
     float32_t   *ptro1 = o;
     float32_t   *ptrp1 = p;
     float32_t   *ptrq1 = q;
     float32_t   *ptrr1 = r;
     float32_t   *ptrs1 = s;
     float32_t   *ptrt1 = t;
     float32_t   *ptru1 = u;
     float32_t   *ptrv1 = v;
     float32_t   *ptrw1 = w;
     float32_t   *ptry1 = y;

     float32_t   *ptrx1 = x;

     a41 = vld1q_f32(ptra1);
     b41 = vld1q_f32(ptrb1);
     c41 = vld1q_f32(ptrc1);
     d41 = vld1q_f32(ptrd1);
     e41 = vld1q_f32(ptre1);
     f41 = vld1q_f32(ptrf1);
     g41 = vld1q_f32(ptrg1);
     h41 = vld1q_f32(ptrh1);
     j41 = vld1q_f32(ptrj1);
     k41 = vld1q_f32(ptrk1);
     l41 = vld1q_f32(ptrl1);
     m41 = vld1q_f32(ptrm1);
     o41 = vld1q_f32(ptro1);
     p41 = vld1q_f32(ptrp1);
     q41 = vld1q_f32(ptrq1);
     r41 = vld1q_f32(ptrr1);
     s41 = vld1q_f32(ptrs1);
     t41 = vld1q_f32(ptrt1);
     u41 = vld1q_f32(ptru1);
     v41 = vld1q_f32(ptrv1);
     w41 = vld1q_f32(ptrw1);
     y41 = vld1q_f32(ptry1);
     
     for(i=0; i<n; i=i+4)
     {
         x41 = vld1q_f32(ptrx1);

         z41 = vaddq_f32(x41, a41);
         z41 = vmulq_f32(z41, b41);

         z42 = vaddq_f32(x41, c41);
         z42 = vmulq_f32(z42, d41);
         z41 = vsubq_f32(z41, z42);

         z42 = vaddq_f32(x41, e41);
         z42 = vmulq_f32(z42, f41);
         z41 = vaddq_f32(z41, z42);

         z42 = vaddq_f32(x41, g41);
         z42 = vmulq_f32(z42, h41);
         z41 = vsubq_f32(z41, z42);

         z42 = vaddq_f32(x41, j41);
         z42 = vmulq_f32(z42, k41);
         z41 = vaddq_f32(z41, z42);

         z42 = vaddq_f32(x41, l41);
         z42 = vmulq_f32(z42, m41);
         z41 = vsubq_f32(z41, z42);

         z42 = vaddq_f32(x41, o41);
         z42 = vmulq_f32(z42, p41);
         z41 = vaddq_f32(z41, z42);

         z42 = vaddq_f32(x41, q41);
         z42 = vmulq_f32(z42, r41);
         z41 = vsubq_f32(z41, z42);
         
         z42 = vaddq_f32(x41, s41);
         z42 = vmulq_f32(z42, t41);
         z41 = vaddq_f32(z41, z42);

         z42 = vaddq_f32(x41, u41);
         z42 = vmulq_f32(z42, v41);
         z41 = vsubq_f32(z41, z42);

         z42 = vaddq_f32(x41, w41);
         z42 = vmulq_f32(z42, y41);
         z41 = vaddq_f32(z41, z42);

         vst1q_f32(ptrx1, z41);

         ptrx1 = ptrx1 + 4;
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
    
    float32_t *xsv = (float32_t *) xcp;

    for (i=0; i<runrepeats; i++)
    {
       // calculations in CPU
       if (part == 0)
       {
          triad(wds, av, xv, xsv);
          opwd = 2;
       }
       if (part == 1)
       {
          triadplus2(wds, av, bv, cv, dv, ev, fv, gv, hv, jv, kv, lv, mv, ov, pv, qv, rv, sv, tv, uv, vv, wv, yv, xsv);
          opwd = 32;
       }   
 
    }
 }

jstring
Java_com_neonmflops2i_NeonMFLOPS2i_stringFromJNI( JNIEnv* env, jobject thiz, jint size)
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
    int     threads;

    words[0]   = 3200;           // 3200
    words[1]   = words[0] * 10;  // 32000
    words[2]   = words[1] * 100; // 3200000
    repeats[0] = 50000;         // 5000  160000
    repeats[1] = repeats[0] / 10;
    repeats[2] = repeats[1] / 100;   

    uint64_t features;

    features = android_getCpuFeatures();
    if ((features & ANDROID_CPU_ARM_FEATURE_ARMv7) == 0)
    {
       sprintf(resultchars, " Can't run - Not an ARMv7 CPU \n");      
       return (*env)->NewStringUTF(env, resultchars);
    }

    if ((features & ANDROID_CPU_ARM_FEATURE_NEON) == 0)
    {
       sprintf(resultchars, " Can't run - CPU doesn't support NEON \n");
       return (*env)->NewStringUTF(env, resultchars);
    }
     
    for (threads=1; threads<9; threads=threads*2)
    {    
        for (part=pStart; part<2; part++)
        {
            for (p=0; p<pEnd; p++)
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
                     results[op] = 12345; // 0
                     i = words[p];
                  }
               }               
               op = op + 1; 
            }
        }
    }
    sprintf(resultchars, " 1T %8d%8d%8d%8d%8d%8d\n"
                         " 2T %8d%8d%8d%8d%8d%8d\n"
                         " 4T %8d%8d%8d%8d%8d%8d\n"
                         " 8T %8d%8d%8d%8d%8d%8d\n"
                         " Results x 100000, 12345 indicates ERRORS\n"
                         " 1T %8d%8d%8d%8d%8d%8d\n"
                         " 2T %8d%8d%8d%8d%8d%8d\n"
                         " 4T %8d%8d%8d%8d%8d%8d\n"
                         " 8T %8d%8d%8d%8d%8d%8d\n",
                          (int)mflops[0], (int)mflops[1], (int)mflops[2],
                          (int)mflops[3], (int)mflops[4], (int)mflops[5],
                          (int)mflops[6], (int)mflops[7], (int)mflops[8],
                          (int)mflops[9], (int)mflops[10], (int)mflops[11],
                          (int)mflops[12], (int)mflops[13], (int)mflops[14],
                          (int)mflops[15], (int)mflops[16], (int)mflops[17],
                          (int)mflops[18], (int)mflops[19], (int)mflops[20],                         
                          (int)mflops[21], (int)mflops[22], (int)mflops[23],
                          results[0], results[1], results[2],
                          results[3], results[4], results[5],
                          results[6], results[7], results[8],
                          results[9], results[10], results[11],
                          results[12], results[13], results[14],
                          results[15], results[16], results[17],
                          results[18], results[19], results[20],
                          results[21], results[22], results[23]);

     return (*env)->NewStringUTF(env, resultchars);
}






 

