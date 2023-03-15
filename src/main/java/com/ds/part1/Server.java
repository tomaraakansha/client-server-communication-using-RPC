package com.ds.part1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.*;

public class Server {
	public static void main(String args[]) throws RemoteException, MalformedURLException {
		try {
	        LocateRegistry.createRegistry(2021);
	        FileHandling obj = new FileHandling();
	        Naming.rebind("rmi://localhost:2021/FileHandling",obj);
	        System.err.println("Server ready");
		}catch (Exception e) {
		        System.err.println("Server exception: " + e.toString());
		        e.printStackTrace();
		}
	}
}

