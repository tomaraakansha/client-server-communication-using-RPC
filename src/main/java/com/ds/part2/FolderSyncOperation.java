package com.ds.part2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import com.ds.services.IFolderSyncOperation;

public class FolderSyncOperation extends UnicastRemoteObject implements IFolderSyncOperation {
	
	private static final long serialVersionUID = 1L;
	String path;
	byte[] bfr;

    public FolderSyncOperation() throws RemoteException {
    	
    	Properties prop = new Properties();
		InputStream confStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server2config.properties");
        try {
			prop.load(confStream);
			String dir = System.getProperty("user.dir");
			path= dir + prop.getProperty("path");
        } catch (IOException e) {
        	System.err.println("Server exception: " + e.toString());
        }
        File dirpath = new File(path);
		if(!dirpath.exists())
			dirpath.mkdir();
    }

    @Override
    public void upload(int bytesRead,String filename,byte[] bfr) throws RemoteException,IOException {
    		OutputStream outstr=null;
    		try {
    					
    			String file = filename;	
    			outstr = new FileOutputStream(path+file);
    			outstr.write(bfr,0,bytesRead);
    			System.out.println("File Uploaded");
    			
    		}catch(Exception e)
    		{
    			System.err.println("client error is: " + e.toString());
    		}
    		finally
    		{
    			outstr.close();
    		}
    }

   @Override
    public String delete(String filename) throws RemoteException {
    	File file=new File(path + filename);
    	try {
    	 if(file.exists())
    	 {
    		 file.delete();
    		 return "file has been deleted";
    	 }
    	 else
    		 return "file can not be deleted as it does not exist"; 
    	}catch(Exception e)
    	{
    		return e.toString();
    	}
    }
    
	@Override
	public String[] listallfiles() throws RemoteException {
		File directoryPath = new File(path);
	    return directoryPath.list();
	}
	
}
