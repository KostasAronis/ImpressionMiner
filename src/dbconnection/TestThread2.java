package dbconnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class TestThread2 extends Thread {
    @Override
    public void run() {
        System.out.println("Key listener started");
        boolean running = true;
        Scanner inputReader = new Scanner(System.in);
        while (running) 
        {
            if (inputReader.hasNext()) 
            {
                String userInput = inputReader.next();
                if (userInput.equals("p")) 
                {
                    System.out.println("Key pressed -> " + userInput);
                } 
                else 
                {
                    System.out.println("listener stopped");
                    break;
                }
            } 
            // BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            // try 
            // {
            //     String userinput = input.readLine();
            //     System.out.println("Key pressed -> " + userinput);
            // } 
            // catch (IOException e) 
            // {
            //     e.printStackTrace();
            // }
        }
    } 
    
}