   import java.io.*;
   import java.net.*;
   import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
   

    public class TCPClient {
       public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
         //files for transferring
         String audio_file = "sample-audio.wav";
         String video_file = "sample-video.mp4";
         String text_file = "file.txt";
         Scanner scanner = new Scanner(System.in);
      	
			// Variables for setting up connection and communication
         Socket Socket = null; // socket to connect with ServerRouter
         PrintWriter out = null; // for writing to ServerRouter
         BufferedReader in = null; // for reading form ServerRouter
         //DataInputStream in = null;
			InetAddress addr = InetAddress.getLocalHost();
			String host = addr.getHostAddress(); // Client machine's IP
      	String routerName ="127.0.0.1"; // ServerRouter host name
			int SockNum = 22; // port number

         System.out.println("Which sample file would you like to transfer?:");
         System.out.println("1. Text");
         System.out.println("2. Audio");
         System.out.println("3. Video");
         int choice = scanner.nextInt();
         String filePath = null;
         switch (choice) {
            case 1: filePath = text_file; readTextFile(filePath); break;
            case 2: filePath = audio_file; readAudioFile(filePath); break;
            case 3: filePath = video_file; readVideoFile(filePath); break;
            default: break;
         }               
			scanner.close(); 

			// Tries to connect to the ServerRouter
         try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
            //in = new DataInputStream(new BufferedInputStream(Socket.getInputStream()));
         } 
             catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            } 
             catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }
				
      	// Variables for message passing                
         Reader reader = new FileReader(filePath);    
         File file = new File(filePath);
         long fileSize = file.length();     
			BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file
         String fromServer; // messages received from ServerRouter
         String fromUser; // messages sent to ServerRouter
			String address ="127.0.0.1"; // destination IP (Server)
			long t0, t1, t;
			
			// Communication process (initial sends/receives
			out.println(address);// initial send (IP of the destination Server)
			fromServer = in.readLine();//initial receive from router (verification of connection)
			System.out.println("ServerRouter: " + fromServer);
			out.println(host); // Client sends the IP of its machine as initial send
			t0 = System.currentTimeMillis();
      	
			// Communication while loop
         while ((fromServer = in.readLine()) != null) {
            System.out.println("\nServer: " + fromServer);
				t1 = System.currentTimeMillis();
            if (fromServer.equals("Bye.")) // exit statement
               break;
				t = t1 - t0;
				System.out.println("Cycle time: " + t);
            
          
            fromUser = fromFile.readLine(); // reading strings from a file
            if (fromUser != null) {
               System.out.println("Client: " + fromUser);
               out.println(fromUser); // sending the strings to the Server via ServerRouter
					t0 = System.currentTimeMillis();
            }
            System.out.format("\nFile size: %d bytes\n", fileSize);
         }
         fromFile.close();      	
			// closing connections
         out.close();
         in.close();
         Socket.close();
      }

      public static void readTextFile(String filePath) throws IOException {
         Reader reader = new FileReader(filePath);         
			BufferedReader fromFile =  new BufferedReader(reader); // reader for the string file
      }

      public static void readAudioFile(String filePath) throws UnsupportedAudioFileException, IOException
      {
         try {
         File file = new File(filePath);
         AudioInputStream input = AudioSystem.getAudioInputStream(file);  
         } catch (Exception e) {
            System.out.println(e);
         }

      }


      public static void readVideoFile(String filePath) {


      }


   }

       