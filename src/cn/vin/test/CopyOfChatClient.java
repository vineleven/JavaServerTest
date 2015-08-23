package cn.vin.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * 聊天客户端
 * 1、创建Socket对象
 * 2、写入数据
 * 3、读取从服务器端发来的数据
 * 4、关闭
 * @author L
 *
 */
public class CopyOfChatClient {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            //1、创建Socket对象，("192.168.1.107", 8857)分别为主机IP和端口号，两个类端口号要一致
            Socket client = new Socket("192.168.1.45", 8857);

            //2.1、创建输入流和输出流对象
            BufferedReader in = new BufferedReader( new InputStreamReader( client.getInputStream() ) );

            PrintWriter out = new PrintWriter(client.getOutputStream(), true);

            BufferedReader inByClient = new BufferedReader( new InputStreamReader(System.in) );

            //服务器端发送的数据
            String data = null;
            //客户端向服务器端写入的数据
            String answer = null;

//            do
//            {
                //2.2、客户端的读写操作
                System.out.print("你说：");
                //获取要发送给服务器端的数据
                answer = inByClient.readLine();
                //向服务器端写入数据
                out.println(answer);
                out.flush();

                //获取服务器端发送的数据
                char[] cbuf = new char[1024];
                in.read( cbuf );
                
                //输出从服务器端获取的数据
                System.out.println("服务器端返回信息是：" + new String( cbuf ) );
                
//            }while(!"bye".equals(data));

            //3、关闭
            in.close();
            out.close();
            inByClient.close();
            client.close();

            System.out.println("客户端关闭……");
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}