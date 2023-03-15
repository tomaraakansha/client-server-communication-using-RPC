package com.ds.services;

import java.io.Serializable;

public class DownloadDataType implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int bytesRead;
	byte[] bfr;
	
	public DownloadDataType(int br, byte[] buf)
    {
		bytesRead=br;
		bfr=buf;
    }
	public int getbytesRead()
	{
		return bytesRead;
	}
	public byte[] getbfr()
	{
		return bfr;
	}
}
