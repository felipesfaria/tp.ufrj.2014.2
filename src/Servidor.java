import java.io.*;
import java.net.*;

class Servidor {

	public static String host;
	private static ServerSocket welcomeSocket;

	public static void main(String argv[]) throws Exception {
		Thread[] threads = new Thread[10];
		Integer contador = 0;

		host = InetAddress.getLocalHost().getHostName();

		welcomeSocket = new ServerSocket(6789);

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			threads[contador] = new Comunicacao(connectionSocket);
			threads[contador].start();
		}
	}
}

class Comunicacao extends Thread {
	Socket connectionSocket;
	
	public Comunicacao(Socket c){
		connectionSocket = c;
	}
	public void run() {

		String clientSentence;
		String capitalizedSentence;
		BufferedReader inFromClient;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());

			clientSentence = inFromClient.readLine();

			capitalizedSentence = clientSentence.toUpperCase() + '\n';

			outToClient.writeBytes(capitalizedSentence);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}