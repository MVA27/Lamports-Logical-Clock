import java.net.*;
import java.io.*;
 
class ServerClock extends Thread
{
	static int time = 0;
	static int step = 6;
	
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
 
class Server{
	static int port = 0;
	
	static{
		port = Integer.parseInt(System.console().readLine("Enter Port Number for the Server : "));
	}

	public static void main(String args[]) throws Exception{
		
		new ServerClock().start();
	
		ServerSocket server = new ServerSocket(port);
		System.out.println("Server Started on port "+port+" and waiting for connection...");
		
		Socket socket = server.accept();
		System.out.println("Client request accepted...");
		
		DataInputStream in = new DataInputStream(socket.getInputStream());
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		
		System.out.println("\n======== START CHAT ========");
		String message = "", reply = "";
		while(!message.contains("bye")){		
			message = in.readUTF();
			System.out.println("Client : "+splitTimeMessage(message));
			
			reply = System.console().readLine("Server : ");
			
			reply = ServerClock.time + "!" + reply;
			
			out.writeUTF(reply);
			out.flush();
		}
	
		System.out.println("====== CHAT TERMINATED ======");
		in.close();
		socket.close();
		server.close();
	}
	
	public static String splitTimeMessage(String message){
		String[] arr = message.split("!",2);
		
		int receivedTime = Integer.parseInt(arr[0]);
		message = arr[1];
		
		return message+" [sent to Server at "+ServerClock.time+" from Client at "+receivedTime+"]"+synchronizeTime(receivedTime);
	}
	
	public static String synchronizeTime(int receivedTime){
		if(ServerClock.time < receivedTime){
			String response = "\ncurrent time "+ServerClock.time+" set to "+(receivedTime+1);
			ServerClock.time = receivedTime + 1;
			return response;
		}
		return "";
	}	
	
}