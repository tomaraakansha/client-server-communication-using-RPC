package com.ds.part1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Properties;

import com.ds.services.DownloadDataType;
import com.ds.services.IFileHandling;

public class FileHandling extends UnicastRemoteObject implements IFileHandling {

	private static final long serialVersionUID = 1L;
	String path;
	byte[] bfr;

    public FileHandling() throws RemoteException {
    	
    	Properties prop = new Properties();
		InputStream confStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("server1config.properties");
        try {
			prop.load(confStream);
			String dir = System.getProperty("user.dir");
			path= dir + prop.getProperty("path");
			bfr = new byte[Integer.parseInt(prop.getProperty("bfrbytesize"))]; 
        } catch (IOException e) {
        	System.err.println("Server exception: " + e.toString());
		}
        File dirpath = new File(path);
		if(!dirpath.exists())
			dirpath.mkdir();
    }

    @Override
    public void upload(int bytesRead,String filename,byte[] bfr) throws IOException {
    		OutputStream outstr=null;
    		try {
    					
    			String file = filename;	
    			outstr = new FileOutputStream(path+file);
    			outstr.write(bfr,0,bytesRead);
    			System.out.println("File Uploaded");
    			
    		}catch(Exception e)
    		{
    			System.err.println("client error is: "+ e.toString());
    		}
    		finally
    		{
    			outstr.close();
    		}
    }

    @Override
    public DownloadDataType download(String filename) throws IOException
	{	
    	InputStream inpstr = null;
    	DownloadDataType ds = null;
		try {
			inpstr = new FileInputStream(path+filename);
			int bytesRead = inpstr.read(bfr);
			ds = new DownloadDataType(bytesRead , bfr);
		}
			catch(Exception e)
		{
			System.err.println("client error is: "+ e.toString());
		}
		finally
		{
			inpstr.close();
		}	
		return ds;
	}

    @Override
    public String delete(String filename) {
    	File file = new File(path+filename);
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
    public String rename(String oldname, String newname) throws RemoteException {
    	try{
    		File oldfile = new File(path+oldname);
	        File newfile = new File(path+newname);
	        
	        oldfile.renameTo(newfile);
	        String resp = "file has been renamed";
	        System.out.println(resp);
	    	return resp;
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
