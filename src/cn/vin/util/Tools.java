package cn.vin.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cn.vin.Config;

public class Tools {
	private static DateFormat dateFormat = new SimpleDateFormat( Config.DATE_FORMAT_STRING );
	
	
	
	/**
	 * 基于位移的int转化成byte[]
	 * 
	 * @param int number
	 * @return byte[]
	 */
	public static byte[] intToByte( int number ) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ( number & 0xff );
		bytes[1] = (byte) ( ( number >> 8 ) & 0xff );
		bytes[2] = (byte) ( ( number >> 16 ) & 0xff );
		bytes[3] = (byte) ( ( number >> 24 ) & 0xff );
		
		return bytes;
	}


	/**
	 * 基于位移的 byte[]转化成int
	 * 
	 * @param byte[] bytes
	 * @return int number
	 */
	public static int bytesToInt( byte[] bytes ) {
		int number = bytes[0] & 0xFF;
		number |= ((bytes[1] << 8) & 0xFF00);
		number |= ((bytes[2] << 16) & 0xFF0000);
		number |= ((bytes[3] << 24) & 0xFF000000);
		return number;
	}
	
	
	public static String[] getUri( String head ){
		String[] arr = head.split( " " );
		if( arr != null  && arr.length == 3 ){
			String uri = arr[1];
			String[] data = uri.split( "?" );
			
//			if( data.length == 1 ){
//				
//			} else {
//				
//			}
			
			return data;
		} else {
			return null;
		}
	}
	
	
	public static byte[] getFile( String fileName ){
		if( fileName == null || "".equals( fileName ) )
			return null;
		
		if( "/".equals( fileName ) ){
			fileName = "index.html";
		}
		
		try {
			File file = new File( Config.SERVER_FILE_ROOT + fileName );
			if( !file.exists() ){
				return null;
			}
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = new FileInputStream( Config.SERVER_FILE_ROOT + fileName );
			int b;
			while ( ( b = in.read() ) != -1 ) {
				out.write( b );
			}
			byte[] data = out.toByteArray();
			
			in.close();
			out.close();
			
			return data;
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			
		}
		return null;
	}
	
	
	public static byte[] getHead( int len ){
		String headerStr = "HTTP/1.0 200 OK\r\n" +
				"Server: OneFile 1.0\r\n" +
				"Content-length: "+ len + "\r\n"+
				"Content-type: "+ Config.SERVER_CONTENT_TYPE + "\r\n\r\n";
//		try {
			byte[] header = headerStr.getBytes();
			return header;
//		} catch ( UnsupportedEncodingException e ) {
//			e.printStackTrace();
//		}
		
//		return null;
	}
	
	
	public static synchronized String getCurDate(){
		Date date = new Date( System.currentTimeMillis() );
		return dateFormat.format( date );
	}
	
	
	public static void debug( String msg ){
		System.out.println( msg );
	}
	
	
	public static void debugf( String formatStr, Object ... args ){
		System.out.printf( formatStr + "\n", args );
	}
	
	
	public static void error( String msg ){
		System.err.println( msg );
	}
	
	
	public static void errorf( String formatStr, Object ... args ){
		System.err.printf( formatStr + "\n", args );
	}
}
