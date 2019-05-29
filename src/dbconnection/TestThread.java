package dbconnection;

import java.util.concurrent.Semaphore;

public class TestThread extends Thread 
{
    private volatile boolean stop = false;
    private volatile long total = 0;
    private volatile long insideCircle = 0;
    private Semaphore semaphore = new Semaphore(1, true);

    @Override
    public void run() 
    {
        while (!stop) {

            for (long i = 0; i < 100000; i++) {
                double x = Math.random();
                double y = Math.random();

                if (((x * x) + (y * y)) <= 1)
                    insideCircle++;

                total++;
            }

            // using semaphores is slow
            try 
            {
                // not to garbage stdout
                Thread.sleep(100000);

                semaphore.acquire();
                semaphore.release();
            }
            catch (InterruptedException e) {
                // exit
                return;
            }

            //System.out.println("pi: " + getPiApproximation());
            //System.out.flush();
        }
    }

    public void pauseComputation() 
    {
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resumeComputation() 
    {
        semaphore.release();
    }

    public void stopComputation() 
    {
        stop = true;
    }

    public double getPiApproximation() 
    {
        return 4*(double)insideCircle / (double)total;
    }
}