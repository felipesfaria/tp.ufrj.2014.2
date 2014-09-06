import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;

class Servidor {

	public static String host;
	private static ServerSocket welcomeSocket;
	private static ArrayList<DataOutputStream> outToClients;
	private static ArrayList<Comunicacao> comunicacoes;

	public static void main(String argv[]) throws Exception {
		comunicacoes = new ArrayList<Comunicacao>();
		Integer contador = 0;

		host = InetAddress.getLocalHost().getHostName();

		welcomeSocket = new ServerSocket(6789);
		outToClients = new ArrayList<DataOutputStream>();

		while (true) {

			Socket connectionSocket = welcomeSocket.accept();
			DataOutputStream outToClient = new DataOutputStream(
					connectionSocket.getOutputStream());
			outToClients.add(outToClient);
			comunicacoes.add(new Comunicacao(connectionSocket, contador++,
					outToClients, comunicacoes));
			comunicacoes.get(comunicacoes.size() - 1).start();
		}
	}
}

class Comunicacao extends Thread {
	Integer id;
	Boolean ativo = false;
	Socket connectionSocket;
	String inMessage;
	String outMessage;
	BufferedReader inFromClient;
	DataOutputStream outToClient;
	ArrayList<DataOutputStream> outToClients;
	ArrayList<Comunicacao> comunicacoes;

	public Comunicacao(Socket c, Integer i, ArrayList<DataOutputStream> otc,
			ArrayList<Comunicacao> coms) {
		connectionSocket = c;
		id = i;
		outToClients = otc;
		comunicacoes = coms;
	}

	public void run() {
		System.out.println("Conexão feita com:" + id);
		ativo = true;
		try {
			inFromClient = new BufferedReader(new InputStreamReader(
					connectionSocket.getInputStream()));
			// outToClient = new
			// DataOutputStream(connectionSocket.getOutputStream());

			while (true) {
				inMessage = inFromClient.readLine();
				System.out.println("Recebeu mensagem: \"" + inMessage
						+ "\"- de:" + id);
				
				if (inMessage.equals("getUsers")) {
					outMessage = "";
					for (int i = 0; i < comunicacoes.size(); i++) {
						outMessage += "Usuario:" + i + "esta :";
						if (comunicacoes.get(i).ativo) {
							outMessage += "ativo\n";
						} else {
							outMessage += "inativo\n";
						}
					}
					
				} else if (inMessage.equals("sair")) {
					outMessage = "SAIR";
					outToClients.get(id).writeBytes(outMessage);
					System.out.println("ClientDisconect");
					break;
					
				} else {
					outMessage = inMessage.toUpperCase() + '\n';
				}

				for (int i = 0; i < outToClients.size(); i++) {
					if (comunicacoes.get(i).ativo)
						outToClients.get(i).writeBytes(outMessage);
				}

			}
			ativo = false;
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}