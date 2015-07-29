import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClientHandler extends Thread{
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    String name;  //ユーザの名前
    ChatServer chat2;  //チャットサーバの参照型変数
    
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
		String[] commands = message.split(" ");
		//それぞれの入力コマンドによって分岐させる
		if(commands[0].equalsIgnoreCase("help")) {
		    help(); 
		}
                if(commands[0].equalsIgnoreCase("name")) {
                    name(commands[1]);
                }
		if(commands[0].equalsIgnoreCase("whoami")){
		    whoami();
		}
		 if(commands[0].equalsIgnoreCase("post")) {
                    post(commands[1]);
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        } finally{
            this.close();
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

    //helpコマンド(使えるコマンドの種類を表示させる)
    public void help() throws IOException {
        this.send("HELP,NAME,WHOAMI,BYE,POST");
    }
    
    //nameコマンド(自分の名前を設定する処理)
    public void name(String name) throws IOException {
        int a = chat2.changeName(name);
        if(a == 0) {
            this.send("this name is used");     
        } else {
            this.name = name;
            this.send(name);
        }
    }

    //whoamiコマンド(自分の名前を返す処理)
    public void whoami() throws IOException {
	this.send(name);
    }
    
    //postコマンド(接続しているクライアントにメッセージを送るメソッド)
    public void post(String message) throws IOException {
	//接続しているクライアント全員にmessageを送りたい
	//名前のリストを保存
	List names = new ArrayList();
	for(int i=0;i<clients.size();i++) {
	    ChatClientHandler handler = (ChatClientHandler)clients.get(i);
	    //自分自身で無ければメッセージを送る
	    if(handler!=this) {
		//誰に送ったのかの一覧表示
		names.add(handler.getClientName());
		//送るメッセージ
		handler.send("[" + this.getClientName() + "]" + message);	
	    }
	}
	//リストをソート済み
	Collections.sort(names);
	//このリストを転送
	//コンマ区切りにする
	String returnMessage = "";
	for(int i=0; i<names.size(); i++) {
	    returnMessage = returnMessage + names.get(i) + ",";
	}
	//本人にこのメッセージを送る
	this.send(returnMessage);
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
