	import java.net.*;
   import java.io.*;

    public class TCPServerRouter {
       public static void main(String[] args) throws IOException {
         Socket clientSocket = null; // socket for the thread
         Object [][] RoutingTable = new Object [10][2]; // routing table
			int SockNum = 22; // port number
			Boolean Running = true;
			int ind = 0; // indext in the routing table	
                  
         int bytesRead;
         File file = new File("../assets/file.txt");
			//Accepting connections
         ServerSocket serverSocket = null; // server socket for accepting connections
         try {
            serverSocket = new ServerSocket(22);
            System.out.println("ServerRouter is Listening on port: 22.");
         }
             catch (IOException e) {
               System.err.println("Could not listen on port: 22.");
               System.exit(1);
            }
			
			// Creating threads with accepted connections
			while (Running == true)
			{
			try {
				clientSocket = serverSocket.accept();
            System.out.println("New client connected");
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            System.out.println("Assigning new thread");
/*             InputStream in = clientSocket.getInputStream();
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            while ((bytesRead = in.read(buffer)) != -1) {
               output.write(buffer,0,bytesRead);
            } */            
				SThread t = new SThread(RoutingTable, clientSocket, ind,dis,dos); // creates a thread with a random port
				t.start(); // starts the thread
				ind++; // increments the index
            System.out.println("ServerRouter connected with Client/Server: " + clientSocket.getInetAddress().getHostAddress());
         }
             catch (IOException e) {
               System.err.println("Client/Server failed to connect.");
               System.exit(1);
            }
			}//end while			
			//closing connections
		   clientSocket.close();
         serverSocket.close();
         

      }
   }