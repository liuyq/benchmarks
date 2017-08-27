 #include <stdio.h>       
 #include <stdlib.h>
 #include <string.h>
 #include <math.h>
 #include <time.h>
 #include <jni.h>
 #include <pthread.h> 


 unsigned int  xx[4200000];
 double runSecs = 0.1;
 double  startSecs;
 double  secs;
 char    resultchars[1200];
 char    printMessage[100];

 typedef struct 
 { 
    int      *x; 
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
 int     threads;
 int     size;

 unsigned int pattern1[16];
 unsigned int pattern2[16];
 unsigned int usePattern1;
 unsigned int usePattern2;
 unsigned int tempPattern;
 unsigned int printPattern;
 unsigned int calcResult[32][3];

 

void readData(int n, unsigned int *xr, int thread)
{
    int i, j;
    int eax;
    unsigned int ecx = xr[0];
    unsigned int edx = xr[0];
    unsigned int edi = xr[0];
    unsigned int esi = xr[0];

    for (j=0; j<runrepeats; j++)
    {
        for (eax=0; eax<n; eax=eax+32)
        {
            ecx = ecx - xr[eax];
            edx = edx - xr[eax+1];       
            edi = edi - xr[eax+2];       
            esi = esi - xr[eax+3];
            ecx = ecx - xr[eax+4];
            edx = edx - xr[eax+5];       
            edi = edi - xr[eax+6];       
            esi = esi - xr[eax+7];
            ecx = ecx + xr[eax+8];
            edx = edx + xr[eax+9];       
            edi = edi + xr[eax+10];       
            esi = esi + xr[eax+11];
            ecx = ecx + xr[eax+12];
            edx = edx + xr[eax+13];       
            edi = edi + xr[eax+14];       
            esi = esi + xr[eax+15];                   
            ecx = ecx - xr[eax+16];
            edx = edx - xr[eax+17];       
            edi = edi - xr[eax+18];       
            esi = esi - xr[eax+19];
            ecx = ecx - xr[eax+20];
            edx = edx - xr[eax+21];       
            edi = edi - xr[eax+22];       
            esi = esi - xr[eax+23];
            ecx = ecx + xr[eax+24];
            edx = edx + xr[eax+25];       
            edi = edi + xr[eax+26];       
            esi = esi + xr[eax+27];
            ecx = ecx + xr[eax+28];
            edx = edx + xr[eax+29];       
            edi = edi + xr[eax+30];       
            esi = esi + xr[eax+31];                  
        }        
    }
//    printf("%8.8X %8.8X %8.8X %8.8X SB %8.8X eax %d\n", ecx, edx, esi, edi, xx[0], eax); 

    ecx = ecx - edx;
    ecx = ecx + edi;
    ecx = ecx - esi;
    ecx = ecx + edi;
    calcResult[thread][size] = ecx;
}

 void writeData(int words)
 {
     int i, j;
     unsigned int ecx = xx[0];
     unsigned int ecy = xx[0];
     unsigned int edx = xx[4];
     unsigned int edy = xx[4];
     unsigned int eax;
     
     for (eax=0; eax<words; eax=eax+32)
     {
         xx[eax]    = ecx;
         xx[eax+1]  = ecx;
         xx[eax+2]  = ecx;
         xx[eax+3]  = ecx;
         xx[eax+4]  = edx;
         xx[eax+5]  = edx;
         xx[eax+6]  = edx;
         xx[eax+7]  = edx;
         xx[eax+8]  = ecx;
         xx[eax+9]  = ecx;
         xx[eax+10] = ecx;
         xx[eax+11] = ecx;
         xx[eax+12] = edx;
         xx[eax+13] = edx;
         xx[eax+14] = edx;
         xx[eax+15] = edx;
         xx[eax+16] = ecx;
         xx[eax+17] = ecx;
         xx[eax+18] = ecx;
         xx[eax+19] = ecx;
         xx[eax+20] = edx;
         xx[eax+21] = edx;
         xx[eax+22] = edx;
         xx[eax+23] = edx;
         xx[eax+24] = ecx;
         xx[eax+25] = ecx;
         xx[eax+26] = ecx;
         xx[eax+27] = ecx;
         xx[eax+28] = edx;
         xx[eax+29] = edx;
         xx[eax+30] = edx;
         xx[eax+31] = edx;
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
    long thread;
    int *xcp;
    
    thread = (long)arg;

    wds = xcalcs.xlen;
    xcp = xcalcs.x + thread * wds;
    readData(wds, xcp, thread);

        
 }


jstring
Java_com_mpintstress_MPIntStressActivity_stringFromJNI( JNIEnv* env, jobject thiz, jint jthreads, jint stressIt, jint jKBcode, jint rmult, jint pat)
{
    int  i, j, p;
    int  pStart = 0;
    int  pEnd = 3;
    long ii;
    int   newdata = 0;
    int   mbps[36];
    int errors = 0;

    size = 0;

    pattern1[0]  = 0x00000000;
    pattern1[1]  = 0xFFFFFFFF;
    pattern1[2]  = 0xA5A5A5A5;
    pattern1[3]  = 0x55555555;
    pattern1[4]  = 0x33333333;
    pattern1[5]  = 0xF0F0F0F0;
    pattern1[6]  = 0x00000000;
    pattern1[7]  = 0xFFFFFFFF;
    pattern1[8]  = 0xA5A5A5A5;
    pattern1[9]  = 0x55555555;
    pattern1[10] = 0x33333333;
    pattern1[11] = 0xF0F0F0F0;

    pattern2[0]  = 0x00000000;
    pattern2[1]  = 0xFFFFFFFF;
    pattern2[2]  = 0x5A5A5A5A;
    pattern2[3]  = 0xAAAAAAAA;
    pattern2[4]  = 0xCCCCCCCC;
    pattern2[5]  = 0x0F0F0F0F;
    pattern2[6]  = 0xFFFFFFFF;
    pattern2[7]  = 0x00000000;
    pattern2[8]  = 0x5A5A5A5A;
    pattern2[9]  = 0xAAAAAAAA;
    pattern2[10] = 0xCCCCCCCC;
    pattern2[11] = 0x0F0F0F0F;

    words[0]   = 4096;           // 3200    to 4096
    words[1]   = words[0] * 10;  // 32000   to 40960
    words[2]   = words[1] * 100; // 3200000 to 4096000
    repeats[0] = 500000 * rmult;         // 100000;
    repeats[1] = repeats[0] / 10;
    repeats[2] = repeats[1] / 100;   
    
    if (stressIt == 1)
    {
        pEnd = jKBcode; // 1, 2, 3
        pStart = pEnd - 1;
    }

    threads = jthreads;
    {    
            usePattern1 = pattern2[pat];
            usePattern2 = pattern1[pat];
            xx[0]   = usePattern1;
            xx[1]   = usePattern1;
            xx[2]   = usePattern1;
            xx[3]   = usePattern1;
            xx[4]   = usePattern2;
            xx[5]   = usePattern2;
            xx[6]   = usePattern2;
            xx[7]   = usePattern2;

            for (p=pStart; p<pEnd; p++)
            {
               xcalcs.x = xx;
               xcalcs.xlen = words[p] / threads;
               runrepeats = repeats[p];
  
               // Data for array
               writeData(words[p]);

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
               mbps[size] = (int)((double)repeats[p] * (double)words[p] * 4.0 / 1000000.0f / secs);
               size = size + 1;
            }
    }

    if (stressIt == 0)
    {
       sprintf(printMessage, "%8.8X    Yes", usePattern1);
       for (i=0; i<threads; i++)
       {
             for (j=0; j<3; j++)
             {
                 if (calcResult[i][j] != usePattern1)
                 {
                     errors = errors + 1;
                     sprintf(printMessage, "%8.8X    No %d", usePattern1, errors);
                 }
             }
       }
       sprintf(resultchars, " %2d %6d%6d%6d  %s", 
                           threads,
                           mbps[0], mbps[1], mbps[2], printMessage);
    }
    else
    {
      sprintf(printMessage, "%8.8X  Yes", usePattern1);

       printPattern = usePattern2;
       for (i=0; i<threads; i++)
       {
           if (calcResult[i][0] != usePattern1)
           {
               errors = errors + 1;
               sprintf(printMessage, "%8.8X  No %d", usePattern1, errors);
           }
       }   

       sprintf(resultchars, "%6d  %8d %s",  threads,  mbps[0], printMessage);
    }

    return (*env)->NewStringUTF(env, resultchars);
}


