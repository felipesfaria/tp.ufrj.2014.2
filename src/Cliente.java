import java.io.*;
import java.net.*;
import java.util.Scanner;

class Cliente extends Thread {

	public static void main(String argv[]) throws Exception {
		String sentence = null;
		KeyboardListener keyboardListener;
		ServerListener serverListener;

		Scanner entrada = new Scanner (System.in);
		System.out.println("Digite o ip do servidor ou 'Local' para conversar dentro da propria maquina");
		String server = entrada.nextLine();
		if(server.equals("Local"))server = "127.0.0.1";
		System.out.println("COMEÇO");
		System.out.println("Comandos:");
		System.out.println("ajuda - imprime essa lista");
		System.out.println("getUsers - recebe lista de usuarios");
		System.out.println("@numero - envia mensagem particular");
		System.out.println("nome: - troca o nome");
		System.out.println("sair - sai do chat");

		Socket clientSocket = new Socket(server, 6789);

		keyboardListener = new KeyboardListener(sentence, clientSocket);
		serverListener = new ServerListener(clientSocket);

		keyboardListener.start();
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
		try{
			while(true){
				inFromServer = new BufferedReader(
						new InputStreamReader(clientSocket.getInputStream()));
				mensagem = inFromServer.readLine();
				System.out.println(mensagem);
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
		try {
			while (true) {
				sentence = inFromUser.readLine();
				if(sentence.equals("ajuda")){
					System.out.println("Comandos:");
					System.out.println("ajuda - imprime essa lista");
					System.out.println("getUsers - recebe lista de usuarios");
					System.out.println("@numero - envia mensagem particular");
					System.out.println("nome: - troca o nome");
					System.out.println("sair - sai do chat");
				}else{
					outToServer.writeBytes(sentence + '\n');
					if (sentence.endsWith("sair")) {
						System.out.println("FIM!");
						break;
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}