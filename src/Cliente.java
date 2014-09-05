import java.io.*;
import java.net.*;

class Cliente {

	public static void main(String argv[]) throws Exception {
		String sentence = null;
		String modifiedSentence;
		Teclado teclado;

		String server = Servidor.host;
		System.out.println("COMEÇO");

		while (true) {
			Socket clientSocket = new Socket(server, 6789);
			BufferedReader inFromServer = new BufferedReader(
					new InputStreamReader(clientSocket.getInputStream()));

			teclado = new Teclado(sentence, clientSocket);
			teclado.run();

			modifiedSentence = inFromServer.readLine();

			System.out.println("FROM SERVER: " + modifiedSentence);

/*			sentence = teclado.sentence;
			clientSocket.close();
			if (sentence.endsWith("sair")) {
				System.out.println("FIM!");
				teclado.join();
				break;
			}*/
		}
	}
}

class Teclado extends Thread {
	String sentence;
	Socket clientSocket;
	BufferedReader inFromUser;
	DataOutputStream outToServer;

	public Teclado(String s, Socket c) throws IOException {
		sentence = s;
		clientSocket = c;
		inFromUser = new BufferedReader(new InputStreamReader(System.in));
		outToServer = new DataOutputStream(clientSocket.getOutputStream());
	}

	public void run() {
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