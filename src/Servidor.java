import java.io.*;
import java.net.*;
import java.util.ArrayList;

class Servidor {

	public static String host;
	private static ServerSocket welcomeSocket;
	private static ArrayList<DataOutputStream> outToClients;

	public static void main(String argv[]) throws Exception {
		Thread[] comunicacoes = new Thread[10];
		Integer contador = 0;

		host = InetAddress.getLocalHost().getHostName();

		welcomeSocket = new ServerSocket(6789);
		outToClients = new ArrayList<DataOutputStream>();
		
		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			outToClients.add(outToClient);
			comunicacoes[contador] = new Comunicacao(connectionSocket, contador, outToClients);
			comunicacoes[contador++].start();
		}
	}
}

class Comunicacao extends Thread {
	Integer id;
	Boolean ativo=false;
	Socket connectionSocket;
	String clientSentence;
	String capitalizedSentence;
	BufferedReader inFromClient;
	DataOutputStream outToClient;
	ArrayList<DataOutputStream> outToClients;

	public Comunicacao(Socket c, Integer i, ArrayList<DataOutputStream> otc) {
		connectionSocket = c;
		id = i;
		outToClients = otc;
	}

	public void run() {
		System.out.println("Conexão feita com:"+id);
		ativo=true;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			//outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			
			while (true) {
				clientSentence = inFromClient.readLine();
				System.out.println("Recebeu mensagem: \""+clientSentence+"\"- de:"+id);

				capitalizedSentence = clientSentence.toUpperCase() + '\n';
				for(int i=0; i<outToClients.size();i++){
					outToClients.get(i).writeBytes(capitalizedSentence);
				}
				
				if (clientSentence.equals("sair")) {
					System.out.println("ClientDisconect");
					break;
				}
			}
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}