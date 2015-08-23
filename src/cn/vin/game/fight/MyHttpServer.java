package cn.vin.game.fight;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	
	
	
	

	private static final ExecutorService executor = Executors.newCachedThreadPool();
	private static LoginServer loginServer = null;
	private static GameServer gameServer = null;
	
	
	
	public MyHttpServer() {
		loginServer = new LoginServer();
		gameServer = new GameServer();
	}


	@Override
	public void run() {
		try {
			ServerSocket server = new ServerSocket( Config.SERVER_PORT );
			try {
				Tools.debug( "Wait For Connect: " + server.toString() + "\n" );
				while( true ) {
					Socket client = server.accept();
					executor.execute( new ServerThread( client ) );
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				executor.shutdown();
				server.close();
			}
		} catch (IOException e) {
			Tools.error( "startServer err" );
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
			
			curDate = Tools.getCurDate();
			Tools.debugf( "%s connect in %s", remoteSocketAddress, curDate );
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
			
			Tools.debugf( "%s closed.", remoteSocketAddress );
		}
		
		
		/**
		 * head只有3类请求，请求文件，请求pid，请求游戏(pid标志)
		 * @param output
		 * @param head
		 * @throws IOException
		 */
		public void response( BufferedOutputStream output, HttpHead head ) throws IOException{
			byte[] data = null;
			if( head.isRequestFile() ){
				data = Tools.getFile( head.getCmd() );
			} else if ( head.isRequestPid() ) {
				data = loginServer.login( head );
			} else {
				String pid = head.getCmd();
				Player player = loginServer.getPlayer( pid );
				if( player != null ){
					data = gameServer.response( player, head );
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
					int result;// = input.read( request );
					
					StringBuffer rqStr = new StringBuffer();
					while( true ){
						result = input.read();
						// 只读协议部分，可以不读完是因为本链接只生效一次
						if( result == -1 || result == '\n' || result == '\r' ){
							break;
						}
						rqStr.append( ( char )result );
					}
					
					Tools.debugf( "request:%d\n%s", result, rqStr.toString() );
					
					if( result == -1 ){
						close();
						break;
					}
					
					HttpHead head = new HttpHead( remoteSocketAddress, rqStr.toString() );
					
					response( output, head );
					
					close();
					break;
				} catch ( IOException e ) {
//					e.printStackTrace();
					Tools.errorf( "%s IOException by %s" , remoteSocketAddress, e.getMessage() );
					close();
					break;
				}
			}
		}
	}
}
