import java.io.*;
import java.net.*;

class Cliente extends Thread {

	public static void main(String argv[]) throws Exception {
		String sentence = null;
		KeyboardListener keyboardListener;
		ServerListener serverListener;

		String server = Servidor.host;
		System.out.println("COMEÇO");

		Socket clientSocket = new Socket(server, 6789);

		keyboardListener = new KeyboardListener(sentence, clientSocket);
		serverListener = new ServerListener(clientSocket);

		keyboardListener.start();
		System.out.println("after thread");
		serverListener.start();
		
		
		keyboardListener.join();
		serverListener.join();
		clientSocket.close();
		System.out.println("Signout");
	}
}

class ServerListener extends Thread{
	Socket clientSocket;
	BufferedReader inFromServer; 
	String mensagem;
	
	public ServerListener(Socket c){
		clientSocket = c;
	}
	
	public void run(){
		System.out.println("serverListener is running");
		try{
			while(true){
				inFromServer = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				mensagem = inFromServer.readLine();
				System.out.println("FROM SERVER: " + mensagem);
				if(mensagem.equals("SAIR")){
					break;
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}

class KeyboardListener extends Thread {
	String sentence;
	Socket clientSocket;
	BufferedReader inFromUser;
	DataOutputStream outToServer;

	public KeyboardListener(String s, Socket c) throws IOException {
		sentence = s;
		clientSocket = c;
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
	}

	public void run() {
		System.out.println("keyboardListener is running");
		try {
			while (true) {
				sentence = inFromUser.readLine();
				outToServer.writeBytes(sentence + '\n');
				if (sentence.endsWith("sair")) {
					System.out.println("FIM!");
					break;
				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}