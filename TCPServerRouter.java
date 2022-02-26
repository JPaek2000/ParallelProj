	import java.net.*;
   import java.io.*;

    public class TCPServerRouter {
       public static void main(String[] args) throws IOException {
         Socket clientSocket = null; // socket for the thread
         Object [][] RoutingTable = new Object [10][2]; // routing table
			int SockNum = 22; // port number
			Boolean Running = true;
			int ind = 0; // indext in the routing table	

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
				SThread t = new SThread(RoutingTable, clientSocket, ind); // creates a thread with a random port
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

      public static void writeStatistics(int ind) throws IOException {
         File statsFile = new File("statistics_p1.txt");

         if (statsFile.createNewFile())         
            System.out.println("Server statisitcs written to " + statsFile.getPath());         
         else System.out.println(statsFile.getPath() + " already exists");

         FileWriter writer = new FileWriter(statsFile);
         writer.write("Size of routing table: " + ind);

         writer.close();
      }
   }