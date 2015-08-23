package cn.vin.util;

public class Response {
	public static final String RES_PID = "RES_PID";
	public static final String RES_JOIN_SUCCESS = "RES_JOIN_SUCCESS";
	public static final String RES_JOIN_FAIL = "RES_JOIN_FAIL";
	public static final String RES_SUSPEND = "RES_SUSPEND";
	
	
	private static byte[] pack( String cmd, String data ){
		return cmd.concat( "?" ).concat( data ).getBytes();
	}
	
	
	public static byte[] packPid( String pid ){
		return pack( RES_PID, pid );
	}
	
	
	public static byte[] packJoinSuccess( String roomId ){
		return pack( RES_JOIN_SUCCESS, roomId );
	}
	
	
	public static byte[] packJoinFail( String reason ){
		return pack( RES_JOIN_FAIL, reason );
	}
	
	
	public static byte[] packSuspend(){
		return pack( RES_SUSPEND, "wait" );
	}
	
}
