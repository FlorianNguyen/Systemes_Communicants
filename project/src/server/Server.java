package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Server extends Thread {

	private Socket connectionSocket;

	Server()
	{
	}

	public void run()
	{
		String clientSentence;
		String capitalizedSentence;	
		BufferedReader inFromClient;
		try {
			inFromClient = new	BufferedReader(new	InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream	outToClient	=new	DataOutputStream(	connectionSocket.getOutputStream());	//	flux sortant	
			clientSentence	=	inFromClient.readLine();	//	lecture	depuis	le	client	
			String delims = "[ ]+";
			String[] tokens = clientSentence.split(delims);
			//capitalizedSentence	=	clientSentence.toUpperCase()	+	'\n';
			Integer sum = Integer.parseInt(tokens[0])+Integer.parseInt(tokens[1]);
			capitalizedSentence = String.valueOf(sum) + '\n';
			outToClient.writeBytes(capitalizedSentence);
			//outToClient.writeBytes(capitalizedSentence);	//	écriture	vers	le	client	
			connectionSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	//	flux	entrant

	}
	
	public void setSocket(Socket s)
	{
		connectionSocket = s;
	}
}
