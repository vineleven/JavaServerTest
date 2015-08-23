package cn.vin;

import java.io.*;
import java.net.*;

public class ServerTest1 {
	public static void main(String[] args) throws IOException {
		ServerSocket s = new ServerSocket( 51234 );
		System.out.println("开始：" + s);
		try {
			Socket socket = s.accept();
			try {
				System.out.println("连接接受" + socket);
				BufferedReader in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
				PrintWriter out = new PrintWriter(new BufferedWriter( new OutputStreamWriter(socket.getOutputStream())), true);
				while (true) {
					String str = in.readLine();
					if (str.equals("q"))
						break;
					System.out.println("对方说：" + str);
					BufferedReader is = new BufferedReader(
							new InputStreamReader(System.in));
					String input = new String();
					input = is.readLine().trim();
					out.println(input);
					System.out.print("我说：");
					out.flush();
				}
			} finally {
				System.out.println("关闭....");
				socket.close();
			}
		} finally {
			s.close();
		}
	}
}