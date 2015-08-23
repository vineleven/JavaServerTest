package cn.vin.game.fightold;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.vin.Config;
import cn.vin.util.HttpHead;
import cn.vin.util.Tools;

public class MyHttpServer extends Thread {
	
	/****************************** 启动代码 **********************************/
	public static void main(String[] args) {
		new MyHttpServer().start();
	}
	/************************************************************************/
	
	
	
	
	private static DateFormat dateFormat = new SimpleDateFormat( "yyyy/MM/dd_HH/mm/ss" );

//	private static LinkedList< Socket > clientList = new LinkedList<Socket>();
	private static Hashtable<String, Socket> clientList = new Hashtable<String, Socket>( 10 );
	
	
	private static ExecutorService executor = null;
	private static PlayerMgr clientMgr = null;
	
	
	
	public MyHttpServer() {
		executor = Executors.newCachedThreadPool();
		clientMgr = new PlayerMgr();
	}


	@Override
	public void run() {
		try {
			clientMgr.start();
			ServerSocket server = new ServerSocket( Config.SERVER_PORT );
			try {
				System.out.println( "Wait For Connect: " + server.toString() + "\n" );
				while( true ) {
					Socket client = server.accept();
					executor.execute( new ServerThread( client ) );
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				clientMgr.stopRun();
				clientList.clear();
				executor.shutdown();
				server.close();
			}
		} catch (IOException e) {
			System.err.println( "startServer err" );
		}
	}
	
	
	static class ServerThread implements Runnable {
		private Socket client;
		private BufferedInputStream input;
		private BufferedOutputStream output;
		private String remoteSocketAddress;
		private String curDate;
		
		public ServerThread( Socket client ) throws IOException {
			this.client = client;
			input = new BufferedInputStream( client.getInputStream() );
			output = new BufferedOutputStream( client.getOutputStream() );
			remoteSocketAddress = client.getRemoteSocketAddress().toString();
			
			long createTime = System.currentTimeMillis();
			Date date = new Date( createTime );
			curDate = dateFormat.format( date );
			System.out.printf( "%s connect in %s\n", remoteSocketAddress, curDate );
		}
		
		
		public void close(){
			try {
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			clientList.remove( PID );
			
			System.out.printf( "%s closed.\n", remoteSocketAddress );
		}
		
		
		public void response( BufferedOutputStream output, HttpHead head ) throws IOException{
			byte[] data = null;
			if( head.isRequestFile() ){
//				data = Tools.getFile( head.getCmd() );
			} else if ( head.isRequestPid() ) {
				String PID = "/" + curDate;
				data = PID.getBytes();
				
				// 这里是可能会失败的
				clientMgr.addPlayer( PID );
			} else {
				Player player = clientMgr.getPlayer( head.getCmd() );
				if( player != null ){
					synchronized( player ){
						String command = head.getValueByIndex( 0 );
						String param = head.getValueByIndex( 1 );
						if( command != null && param != null ){
							data = player.parseCommand( command, param );
						}
					}
				}
			}

			if( data != null ){
				output.write( data );
				output.flush();
			}
		}
		
		
		@Override
		public void run() {
			while ( true ) {
				try {
					byte[] request = new byte[ 1000 ];
					int result = input.read( request );
					
					StringBuffer rqStr = new StringBuffer();
					while( true ){
						result = input.read();
						if( result == -1 || result == '\n' || result == '\r' ){
							break;
						}
						rqStr.append( ( char )result );
					}
					
					HttpHead head = new HttpHead( remoteSocketAddress, rqStr.toString() );
					System.out.printf( "request:%d\n%s\n", result, rqStr );
					
					if( result == -1 ){
						close();
						break;
					}
					
					response( output, head );
					
					//
					
					close();
					break;
				} catch ( IOException e ) {
//					e.printStackTrace();
					System.err.printf( "%s IOException by %s\n" , remoteSocketAddress, e.getMessage() );
					close();
					break;
				}
			}
		}
	}
}
