package cn.vin;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class SingleFileHTTPServer extends Thread {
	
	private String encoding = "ASCII";
	private String MIMEType = "";
	private int port = 51235;
	private static final String FILE_ROOT = "D:/mygame/";
	
	public SingleFileHTTPServer( String encoding, String MIMEType )throws UnsupportedEncodingException {
		this.encoding = encoding;
		this.MIMEType = MIMEType;
	}
	
	public void run() {
		try {
			ServerSocket server = new ServerSocket( this.port );
			System.out.println("Accepting connections on port " + server );
			
			while (true) {
				try {
					Socket connection = server.accept();
					System.out.println("connections on port " + server.getLocalPort());
//					connection = server.accept();
//					new Thread( new Runnable() {
//						@Override
//						public void run() {
							try {
								OutputStream out = new BufferedOutputStream( connection.getOutputStream() );
								InputStream in = new BufferedInputStream( connection.getInputStream() );
								
//								StringBuffer request = new StringBuffer();
//								while (true) {
//									int c = in.read();
//									if ( c == '\n' || c == '\r' || c == -1 ) {
//										break;
//									}
//									request.append( (char)c );
//								}
								
								String request;
									byte[] b = new byte[200];
									int c = in.read( b );
									if ( c == -1 ) {
										break;
									}
								request = new String( b );
								
								String from = connection.getRemoteSocketAddress().toString();//connection.getInetAddress().getHostAddress();
								String requestContent = request.toString();
								System.out.println( "\n来自" + from + "的请求:" + requestContent );
								String[] requestCmd = requestContent.split( " " );
//					System.out.println( "请求cmd:" + requestCmd[1] );
								
								String file = "";
								if( requestCmd != null && requestCmd.length > 1 ){
									if( "/".equals( requestCmd[1] ) ){
										file = "test.html";
//										file = "index.html";
									} else if( requestCmd[1].startsWith( "/sdfs" ) ){
										file = "test.txt";
									} else {
										file = requestCmd[1];
//										return;
									}
								}
								boolean break1 = false;
								while( "/sdfs".equals( file ) ){
									if( break1 )
										break;
								}
								byte[] responseContent = getFile( file );
								
								if( responseContent == null ) {
									out.write( getHead( 0 ) );
									out.flush();
									connection.close();
									continue;
								}
								
								//如果检测到是HTTP/1.0及以后的协议，按照规范，需要发送一个MIME首部
//					if (request.toString().indexOf("HTTP/")!=-1) {
//						out.write( getHead( responseContent.length ) );
//					}

								out.write( responseContent );
								out.flush();
								
								connection.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
//						}
//					} ).start();
					
				} catch (IOException e) {
					if( server != null ) {
						server.close();
					}
					break;
				}finally{
//					if (connection!=null) {
//						connection.close();
//					}
					
				}
			}
			
		} catch (IOException e) {
			System.err.println("Could not start server. Port Occupied");
		}
	}

	
	private byte[] getFile( String fileName ){
		if( fileName == null || "".equals( fileName ) )
			return null;
		try {
			File file = new File( FILE_ROOT + fileName );
			if( !file.exists() ){
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			InputStream in = new FileInputStream( FILE_ROOT + fileName );
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
	
	private byte[] getHead( int len ){
		String headerStr = "HTTP/1.0 200 OK\r\n" +
				"Server: OneFile 1.0\r\n" +
				"Content-length: "+ len + "\r\n"+
				"Content-type: "+ MIMEType + "\r\n\r\n";
		try {
			byte[] header = headerStr.getBytes( encoding );
			return header;
		} catch ( UnsupportedEncodingException e ) {
			e.printStackTrace();
		}
		
		return null;
	}


	public static void main( String[] args ) {
		try {
			String contentType="text/plain";
			contentType="text/html";
			
			//设置监听端口
			String encoding="ASCII";

			Thread t = new SingleFileHTTPServer( encoding, contentType );
			t.start();
			
		} catch (ArrayIndexOutOfBoundsException e) {
			 System.out.println("Usage:java SingleFileHTTPServer filename port encoding");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
