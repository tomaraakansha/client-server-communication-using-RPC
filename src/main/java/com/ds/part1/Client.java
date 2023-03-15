package com.ds.part1;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

import com.ds.services.IFileHandling;

public class Client {
	
	public static void main(String args[]) { 
		String host = (args.length < 1) ? null : args[0];
        try {
            LocateRegistry.getRegistry(host);
            IFileHandling stub = (IFileHandling) Naming.lookup("rmi://localhost:2021/FileHandling");
            try (Scanner in = new Scanner(System.in)) {
				while(true)
				{
					int i=0;
					System.out.println("press 1 for upload a file,\t press 2 to download a file,\t press 3 to rename a file,\t press 4 to delete a file");
					i=in.nextInt();
					ClientFileOperation cfo=new ClientFileOperation();
					switch(i)
					{
					   case 1 :
						   System.out.println("upload method call");
				           cfo.callforupload(stub);
					      break;
					   case 2 :
						   System.out.println("Download method call");
						   cfo.callfordownload(stub);
					      break;
					   case 3 :
						   System.out.println("Rename method call");
						   cfo.callforrename(stub);
				 	      break; 
				 	   case 4 :
				 		  System.out.println("Delete method call");
				 		  cfo.callfordelete(stub);
				 	      break;
					   default : 
						   System.out.println("Please select a valid option");
					}
				}
			}
                      
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
        }
	}
	
	
}
