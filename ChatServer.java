import java.net.*;
import java.io.*;

public class ChatServer{
    private ServerSocket server;
    private String name2;  //古い名前を格納
　　private List<String> namae = new ArrayList<String>();//クライアントの名前を格納

    public void listen(){
        try{
            server = new ServerSocket(18080);
            System.out.println("Chatサーバをポート18080で起動しました．");
            //Socket socket = server.accept();
            System.out.println("クライアントが接続してきました．");

            while(true){
                Socket socket = server.accept();
                ChatClientHandler handler = new ChatClientHandler(socket);
                handler.start();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
    
       
    //名前を変更する場合にその名前に変更可能かどうかを判定するメソッド
    public int changeName(String name) {
        for (int i=0; i<clients.size(); i++) {
            if(namae.get(i).equals(name)){
                //ここで名前が既に存在する場合の処理
                return 0;
            }
        }
        //ここで名前がそんざいしない場合の処理
        //名前が存在しなければ、namaeのリストの古い名前を削除、新しい名前を追加
        namae.remove(name2);
        namae.add(name);
        for (int i=0; i<namae.size(); i++) {
	    System.out.println(namae.get(i));
        }
        //クライアントハンドラの方の名前も新しいものにする
        return 1;
    }

    public static void main(String[] args){
        ChatServer echo = new ChatServer();
        echo.listen();
    }
}
