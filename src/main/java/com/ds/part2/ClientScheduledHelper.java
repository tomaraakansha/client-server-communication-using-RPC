package com.ds.part2;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.Naming;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.ds.services.IFolderSyncOperation;

public class ClientScheduledHelper extends Thread{
	Scanner in;
	String path;
	int scheduledMin;
	byte[] bfr;
	
	public ClientScheduledHelper ()
	{
		
		in = new Scanner(System.in);
		Properties prop = new Properties();
		InputStream confStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("client2config.properties");
        try {
			prop.load(confStream);
			String dir = System.getProperty("user.dir");
			path= dir + prop.getProperty("path");
			scheduledMin = Integer.parseInt(prop.getProperty("scheduledMin"));
			bfr = new byte[Integer.parseInt(prop.getProperty("bfrbytesize"))]; 
        } catch (IOException e) {
        	System.err.println("Client exception: " + e.toString());
		}
        File dirpath = new File(path);
		if(!dirpath.exists())
			dirpath.mkdir();
	}
	
	 @Override
    public void run()
    {
        while(true) {
           try {
        	   IFolderSyncOperation stub = (IFolderSyncOperation) Naming.lookup("rmi://localhost:2020/FolderSyncOperation");
        	   ClientScheduledHelper ch=new ClientScheduledHelper();
        	   ch.SyncFolders(stub);
        	   Date date = new Date();
        	   long timenextrun=(date.getTime()+TimeUnit.MINUTES.toMillis(scheduledMin));
        	   Date date1 = new Date(timenextrun);
        	   System.out.println("next run will be at: " + date1.toString() );
        	   Thread.sleep(scheduledMin*60000);
			} catch (Exception e) {
				System.out.println("Client side error: "+e.toString());
			}
        }
    }
	
	public void callforupload(String filename,IFolderSyncOperation stub) throws IOException
	{
		
		InputStream inpstr = null;
		try {
			inpstr = new FileInputStream(path+filename);
			int bytesRead; 
			while ((bytesRead = inpstr.read(bfr)) > 0) 
			{
				stub.upload(bytesRead,filename,bfr);
			}	
		}
			catch(Exception e)
		{
			System.err.println("client error is: " + e.toString());
		}
		finally
		{
			inpstr.close();
		}	
	}
	
	public void SyncFolders(IFolderSyncOperation stub) throws IOException {

		String[] filesServer = stub.listallfiles();
		String[] filesClient;
		File directoryPath = new File(path);
		filesClient = directoryPath.list();
		if(filesClient.length < 1 && filesServer.length < 1)
		{
			System.out.println("no files on client or server");
		}
		
		for(int i = 0; i < filesClient.length; i++)
		{
			long modifieddate = new File(path+filesClient[i]).lastModified();
			long currentdate = System.currentTimeMillis();
			if(currentdate-modifieddate < TimeUnit.MINUTES.toMillis(scheduledMin))
			{
				callforupload(filesClient[i],stub);
			}
			else {
				int j;
				for(j = 0; j < filesServer.length; j++)
				{
					if(filesServer[j].equals(filesClient[i]))
						break;
				}
				if(j == filesServer.length)
					callforupload(filesClient[i],stub);
			}
		}
		for(int i = 0; i < filesServer.length; i++)
		{
			int j;
			for(j = 0; j < filesClient.length; j++)
			{
				if(filesServer[i].equals(filesClient[j]))
					break;
			}
			if(j == filesClient.length)
				stub.delete(filesServer[i]);
		}
		System.out.println("Files has been synced");
				
	}
}
