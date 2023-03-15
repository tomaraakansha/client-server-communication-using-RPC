package com.ds.part2;

public class Client {
    public static void main(String[] args) {
    	
    	System.out.println("Run helper thread");
    	Thread thread = new ClientScheduledHelper();
        thread.start();
        
    }
}

