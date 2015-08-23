package cn.vin.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * 服务器端代码
 * 获取客户端发送的信息，显示并且返回对应的回复
 * 1、创建ServerSocket对象
 * 2、调用accept方法获取客户端连接
 * 3、使用输入流读取客户端发送的数据
 * 4、使用输出流向客户端写入数据
 * 5、关闭对应的对象
 * @author L
 *
 */

public class ChatServer {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            //1、创建ServerSocket对象,8875为自定义端口号
            ServerSocket server = new ServerSocket(51234);

            //简单提示
            System.out.println("等待客户端连接……");

            //2、获取客户端连接
            Socket client = server.accept();

            //获取客户端的相关信息
            System.out.println(client.getInetAddress().getHostAddress() + "连接上来了……");

            //3.1、定义输入流和输出流对象
            BufferedReader in = new BufferedReader( new InputStreamReader( client.getInputStream() ) );

            //用来获取从控制台输入的数据，将该数据发送给客户端
            BufferedReader inByServer = new BufferedReader( new InputStreamReader(System.in) );

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            //读取到的数据
            String data = null;
            String answer = null;

            //循环和客户端进行通信
            do
            {
                //3.2、读取客户端发送的数据
                data = in.readLine();

                //在服务器端显示读取到的数据
                System.out.println("客户端发送信息：" + data);

                //获取服务器端要发送给客户端的信息
                System.out.print("服务器端回复客户端：");
                answer = inByServer.readLine();

                //3.3、将数据写入到客户端
                out.println(answer);
                out.flush();
            }while(!"bye".equals(data));

            //4、关闭相关资源
            out.flush();
            in.close();
            inByServer.close();
            out.close();

            //关闭Socket对象
            client.close();
            server.close();

            System.out.println("服务器端关闭……");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
		}
	}
}