package com.ds.part1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

import com.ds.services.DownloadDataType;
import com.ds.services.IFileHandling;

public class ClientFileOperation {
	Scanner in;
	String path;
	byte[] bfr;
	
	public ClientFileOperation()
	{
		
		Properties prop = new Properties();
		InputStream confStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("client1config.properties");
        try {
			prop.load(confStream);
			String dir = System.getProperty("user.dir");
			path= dir + prop.getProperty("path");
			bfr = new byte[Integer.parseInt(prop.getProperty("bfrbytesize"))]; 
        } catch (IOException e) {
        	System.err.println("Client exception: " + e.toString());
		}
        File dirpath = new File(path);
		if(!dirpath.exists())
			dirpath.mkdir();
	}
	
	public void callforupload(IFileHandling stub) throws IOException
	{
		in=new Scanner(System.in);
		String[] filesClient;
		File directoryPath = new File(path);
		filesClient = directoryPath.list();
		if(filesClient.length > 0)
		{
			System.out.println("Select the file number to upload");
			for(int i = 0; i < filesClient.length; i++)
			{
				System.out.println(i + ". " + filesClient[i]);
			}
			
			int fileno = in.nextInt();
			if(filesClient.length == 0)
			{
				System.out.println( "There is no file");
			}
			else if(fileno >= filesClient.length || fileno < 0)
				System.out.println("File number entered is not valid.");
			else {
				InputStream inpstr = null;
				String filename=filesClient[fileno];
				try {
					inpstr = new FileInputStream(path+filename);
					int bytesRead; 
					while ((bytesRead = inpstr.read(bfr)) > 0) 
					{
						stub.upload(bytesRead, filename, bfr);
						System.out.println("File Uploaded");
					}	
				}catch(Exception e)
				{
					System.err.println("client error is: "+ e.toString());
				}
				finally
				{
					inpstr.close();
				}	
			}
		}
		else
			System.out.println( "There is no file");
		
	}
	
	public void callfordownload(IFileHandling stub) throws IOException {

		in=new Scanner(System.in);
		String[] files = stub.listallfiles();
		if(files.length > 0)
		{
			System.out.println("Select the file number to download");
			for(int i = 0; i < files.length; i++)
			{
				System.out.println(i+". "+ files[i]);
			}
			int fileno = in.nextInt();
			if(files.length == 0)
			{
				System.out.println( "There is no file");
			}
			else if(fileno >= files.length || fileno < 0)
				System.out.println("File number entered is not valid.");
			else {
				OutputStream outstr = null;
	    		try {
	    					
	    			String file = files[fileno];	
	    			outstr = new FileOutputStream(path+file);
	    			DownloadDataType ds = stub.download(file);
	    			int bytesRead = ds.getbytesRead();
	    			outstr.write(ds.getbfr(), 0, bytesRead);
	    			System.out.println("File Downloaded");
	    			
	    		}catch(Exception e)
	    		{
	    			System.err.println("client error is: "+ e.toString());
	    		}
	    		finally
	    		{
	    			outstr.close();
	    		}	
			}
		}
		else
			System.out.println( "There is no file");
	}


	public void callforrename(IFileHandling stub) {
		String[] files;
		try {
			in=new Scanner(System.in);
			files = stub.listallfiles();
			if(files.length > 0)
			{
				System.out.println("Select the file to rename");
				for(int i=0; i<files.length; i++)
				{
					System.out.println(i + ". " + files[i]);
				}
				int fileno = in.nextInt();
				if(files.length == 0)
				{
					System.out.println( "There is no file");
				}
				else if(fileno >= files.length || fileno < 0)
					System.out.println("File number entered is not valid.");
				else {
					System.out.println("enter the name of new file without extension");
					String newname = in.next();
					if(newname.matches("^[A-za-z0-9_]{1,255}$")){
						String oldname = files[fileno];
						newname=newname + oldname.substring(oldname.lastIndexOf("."));
						System.out.println( ("response: " +stub.rename(oldname, newname)));
						System.out.println("File has been renamed");
					}
					else
						System.out.println("Entered file name is not valid");
				}
			}
			else
				System.out.println( "There is no file");
		
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
		}
	}

	public void callfordelete(IFileHandling stub){
		String[] files;
		try {
			in=new Scanner(System.in);
			files = stub.listallfiles();
			if(files.length > 0)
			{
				System.out.println("Select the file to delete");
				for(int i=0; i < files.length; i++)
				{
					System.out.println(i + ". " + files[i]);
				}
				int fileno = in.nextInt();
				if(fileno >= files.length || fileno < 0)
					System.out.println("File number entered is not valid.");
				else {
					System.out.println( ("response: " + stub.delete(files[fileno])));
					System.out.println("File has been deleted");
				}
			}
			else
				System.out.println( "There is no file");	
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
		}
	}
	
}
