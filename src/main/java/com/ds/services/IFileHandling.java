package com.ds.services;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IFileHandling  extends Remote {

    void upload(int bytesRead,String filename,byte[] bfr)  throws RemoteException,IOException;

    DownloadDataType download(String filename) throws RemoteException, IOException;

    String delete(String filename) throws RemoteException;
    
    String rename(String oldname, String newname) throws RemoteException;
    
    String[] listallfiles() throws RemoteException;
}
