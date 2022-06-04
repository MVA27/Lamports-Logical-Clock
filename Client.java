import java.net.*;
import java.io.*;

class ClientClock extends Thread
{
	static int time = 0;
	static int step = 8;
	
	public void run(){
		for(;;){
			time = time + step;
			try{
				Thread.sleep(2000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}

class Client{

	static String ip = null;
	static int port = 0;	
	
	static{
		ip = System.console().readLine("Enter IP Address of the Server : ");
		port = Integer.parseInt(System.console().readLine("Enter Port Number of the Server : "));
	}

	public static void main(String args[]) throws Exception{
	
		new ClientClock().start();
	
		Socket socket = new Socket(ip,port);
		System.out.println("Connection Established...");
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		System.out.println("\n======== START CHAT ========");
		String message = "", reply="";
		while(!message.contains("bye")){
		
			message = System.console().readLine("Client : ");
			
			message = ClientClock.time + "!" + message;
			
			out.writeUTF(message);
			out.flush();
			
			reply = in.readUTF();
			System.out.println("Server : "+splitTimeMessage(reply));
		}

		System.out.println("====== CHAT TERMINATED ======");
		out.close();
		socket.close();
	}
	
	public static String splitTimeMessage(String message){
		String[] arr = message.split("!",2);
		
		int receivedTime = Integer.parseInt(arr[0]);
		message = arr[1];
		
		return message+" [sent to Client at "+ClientClock.time+" from Server at "+receivedTime+" ]"+synchronizeTime(receivedTime);
	}
	
	public static String synchronizeTime(int receivedTime){
		if(ClientClock.time < receivedTime){
			String response = "\ncurrent time "+ClientClock.time+" set to "+(receivedTime + 1);
			ClientClock.time = receivedTime + 1;
			return response;
		}
		return "";
	}
	
}

