package cn.vin.util;

/**
 * 通信协议
 * cmd = / /xxxx 资源或者uid
 * 
 * params = {
 * 	1 = data1;
 * 	2 = data2;
 * 	3 = data3;
 * }
 * @author vineleven
 *
 */


public class HttpHead {
	public static final String REQ_PID = "/REQ_PID";
	
	public static final String REQ_SUB_JOIN_ROOM = "REQ_JOIN_ROOM";
	public static final String REQ_SUB_SUSPEND = "REQ_SUB_SUSPEND";
	public static final String REQ_SUB_FRAME_UPDATE = "REQ_SUB_FRAME_UPDATE";
	
	
	
	private String cmd = "";
	private HttpParam[] params = null;
	
	private String ip = "";
	
	//	GET /sdfs?test=test&test2=1234 HTTP/1.1
	public HttpHead( String ip, String httpContent ) {
		this.ip = ip;
		String[] arr = httpContent.split( " " );
		if( arr != null  && arr.length == 3 ){
			String uri = arr[1];
			String[] data = uri.split( "\\?" );
			
			cmd = data[0];
			if( data.length == 2 ){				
				String paramStr = data[1];
				String[] paramArr = paramStr.split( "&" );
				params = new HttpParam[ paramArr.length ];
				
				for( int i = 0; i < paramArr.length; i++ ){
					params[ i ] = new HttpParam( paramArr[ i ] );
				}
			} else if ( data.length == 1 ){
			} else {
				System.err.println( "HttpHead params parse err!" );
			}
		} else {
			System.err.println( "HttpHead content parse err!" );
		}
	}
	
	
	public String getIp(){
		return ip;
	}
	
	
	public String getCmd(){
		return cmd;
	}
	
	
	public boolean isRequestFile(){
		return 	cmd.equals( "/" ) ||
				cmd.endsWith( ".js" ) ||
				cmd.endsWith( ".gif" ) ||
				cmd.endsWith( ".html" ) ||
				cmd.endsWith( ".mp3" ) ||
				cmd.endsWith( ".png" );
	}
	
	
	public boolean isRequestPid(){
		return cmd.equals( REQ_PID );
	}
	
	
	/*************************** 以下为具体用户请求 ***************************/
	
	public boolean isRequestJoinRoom(){
		return REQ_SUB_JOIN_ROOM.equals( getKeyByIndex( 0 ) );
	}
	
	
	public boolean isRequestSuspend(){
		return REQ_SUB_SUSPEND.equals( getKeyByIndex( 0 ) );
	}
	
	
	public boolean isRequestFrameUpdate(){
		return REQ_SUB_FRAME_UPDATE.equals( getKeyByIndex( 0 ) );
	}
	
	
	public int getParamsCount(){
		if( params != null ){
			return params.length;
		} else {
			return 0;
		}
	}
	
	
	public String getValueByKey( String key ){
		if( params == null ) return null;
		
		for (int i = 0; i < params.length; i++) {
			if( params[i].getKey().equals( key ) ){
				return params[i].getValue();
			}
		}
		
		return null;
	}
	
	
	public String getKeyByIndex( int index ){
		if( params != null && params.length > index ){
			return params[ index ].getKey();
		} else {
			return null;
		}
	}
	
	
	public String getValueByIndex( int index ){
		if( params != null && params.length > index ){
			return params[ index ].getValue();
		} else {
			return null;
		}
	}
	
	
	class HttpParam {
		private String[] data = null;
		
		HttpParam( String param ) {
			data = param.split( "=" );
			if( data == null || data.length != 2 ){
				System.err.println( "HttpParam parse err!" );
				data = new String[2];
			}
		}
		
		
		public String getKey(){
			return data[0];
		}
		
		
		public String getValue(){
			return data[1];
		}
	}
}
