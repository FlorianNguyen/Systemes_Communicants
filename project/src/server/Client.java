package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
	
	String sentence;
	String modifiedSentence;
	BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
	Socket clientSocket;
	DataOutputStream outToServer;
	BufferedReader inFromServer;
	
	public Client()
	{
		try {
			clientSocket = new Socket("picard.isae.fr",6789);
			outToServer = new DataOutputStream(clientSocket.getOutputStream());
			inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			sentence = inFromUser.readLine();
			outToServer.writeBytes(sentence + '\n');
			modifiedSentence = inFromServer.readLine();
			System.out.println("Reçu du serveur : " + modifiedSentence);
			clientSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run()
	{
		
	}
	
	
	public void sendData(int a, int b) throws Exception
	{
		String sentence;
		String modifiedSentence;
		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("picard.isae.fr",6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		sentence = inFromUser.readLine();
		outToServer.writeBytes(sentence + '\n');
		modifiedSentence = inFromServer.readLine();
		System.out.println("Reçu : " + modifiedSentence);
		clientSocket.close();
	}
}
