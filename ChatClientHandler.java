import java.io.*;
import java.net.*;

public class ChatClientHandler extends Thread{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;

    public ChatClientHandler(Socket socket){
        this.socket = socket;
    }

    /**
     * 並列実行を行うときに実行されるメソッド．
     */
    public void run(){
        try{
            open();
            while(true){
                String message = receive();
                if(message.equals("")) break;
                send(message);
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            close();
        }
    }

    /**
     * クライアントとのデータのやり取りを行うストリームを開くメソッド．
     */
    public void open() throws IOException{
        in = new BufferedReader(
            new InputStreamReader(socket.getInputStream())
        );
        out = new BufferedWriter(
            new OutputStreamWriter(socket.getOutputStream())
        );
    }

    /**
     * クライアントからデータを受け取るメソッド．
     */
    public String receive() throws IOException{
        String line = in.readLine();
        System.out.println(line);
        return line;
    }

    /**
     * クライアントにデータを送信するメソッド．
     */
    public void send(String message) throws IOException{
        out.write(message);
        out.write("\r\n");
        out.flush();
    }

    /**
     * クライアントとの接続を閉じるメソッド．
     */
    public void close(){
        if(in != null){
            try{
                in.close();
            } catch(IOException e){ }
        }
        if(out != null){
            try{
                out.close();
            } catch(IOException e){ }
        }
        if(socket != null){
            try{
                socket.close();
            } catch(IOException e){ }
        }
    }
}
