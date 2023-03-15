package com.ds.part2;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {

    public Server (){

    }
    public static void main(String[] args) throws RemoteException {
    	LocateRegistry.createRegistry(2020);
    	FolderSyncOperation obj = new FolderSyncOperation();
        try {
			Naming.rebind("rmi://localhost:2020/FolderSyncOperation",obj);
		} catch (Exception e) {
			System.err.println("Server error: " + e.toString());
			e.printStackTrace();
		}
        System.err.println("Server ready");
    }
}

