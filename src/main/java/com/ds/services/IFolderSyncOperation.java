package com.ds.services;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFolderSyncOperation extends Remote {

	 void upload(int bytesRead,String filename,byte[] bfr)  throws RemoteException,IOException;
	 
	 String delete(String filename) throws RemoteException;
	 
	 String[] listallfiles() throws RemoteException;
	 
}
