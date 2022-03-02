//Names: Jason Paek, Bryan Nix, Mohammad Umar, Braxton Meyer
import java.io.*;
import java.net.*;
	
public class SThread extends Thread 
{
	private Object [][] RTable; // routing table
	private PrintWriter out, outTo; // writers (for writing back to the machine and to destination)
    private BufferedReader in; // reader (for reading from the machine connected to)   
	private String inputLine; // communication strings
	private String outputLine;
	private String destination;
	private String addr;
	private Socket outSocket; // socket for communicating with a destination
	private int ind; // indext in the routing table


	private long routing_t0, routing_time,thread_t0, thread_time;
	private byte[] file;
	DataInputStream dis; DataOutputStream dos;

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index, DataInputStream dis, DataOutputStream dos) throws IOException
	{
			out = new PrintWriter(toClient.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));			
			RTable = Table;
			addr = toClient.getInetAddress().getHostAddress();
			RTable[index][0] = addr; // IP addresses 
			RTable[index][1] = toClient; // sockets for communication
			ind = index;
			this.dis = dis;
			this.dos = dos;
	}

	// Run method (will run for each machine that connects to the ServerRouter)
	public void run()
	{
		String received;
		String toreturn;
		while (true) 
		{
			try {
				dos.writeUTF("Do you want (1) text, (2) audio, or (3) video? 4 for Exit");
				received = dis.readUTF();

				if (received.equals("4"))
				{
					this.outSocket.close();
					break;
				}

				switch (received) {
					case "1":
						toreturn = "../assets/file.txt";
						dos.writeUTF(toreturn);
						break;
					case "2":
						toreturn = "../assets/sample-audio.wav";
						dos.writeUTF(toreturn);
						break;
					case "3":
						toreturn = "../assets/sample-video.mp4";
						dos.writeUTF(toreturn);
						break;
					default:
						dos.writeUTF("Invalid input");
						break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}




		thread_t0 = System.currentTimeMillis();
		try
		{
		// Initial sends/receives
		destination = in.readLine(); // initial read (the destination for writing)
		System.out.println("Forwarding to " + destination);
		out.println("Connected to the router."); // confirmation of connection
		
		// waits 10 seconds to let the routing table fill with all machines' information
		try{
    		Thread.currentThread().sleep(10000); 
	   }
		catch(InterruptedException ie){
		System.out.println("Thread interrupted");
		}
		
		// loops through the routing table to find the destination
		routing_t0 = System.nanoTime();
		for ( int i=0; i<10; i++) 
				{
					if (destination.equals((String) RTable[i][0])){
						outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
						System.out.println("Found destination: " + destination);
						outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
				}}
		//t1 = System.currentTimeMillis();
		routing_time = System.nanoTime() - routing_t0;
		
		// Communication loop			
		while ((inputLine = in.readLine()) != null) {   
            System.out.println("Client/Server said: " + inputLine);
            if (inputLine.equals("Bye.")) {// exit statement 				
				break;
			}
            outputLine = inputLine; // passes the input from the machine to the output string for the destination
				
				if ( outSocket != null){				
				outTo.println(outputLine); // writes to the destination
				}			
       }// end while		   	 
		 }// end try
			catch (IOException e) {
               System.err.println("Could not listen to socket.");
               System.exit(1);
         }
		thread_time = System.currentTimeMillis() - thread_t0;

		try {
			writeStatistics(ind,thread_time,routing_time,inputLine);
		} catch (IOException e) {			
			e.printStackTrace();
		}

		
	}

	public static void writeStatistics(int ind,long thread_time,long routing_time, String input) throws IOException {
		//File statsFile = new File("statistics_p1.txt");
		final byte[] fileSize =  input.getBytes("UTF-8");
		PrintWriter pw = new PrintWriter(new FileOutputStream(new File("../printouts/raw_stats_p1.csv"),true));

		/* if (statsFile.createNewFile())         
		   System.out.println("Server statistics written to " + statsFile.getPath());         
		else System.out.println(statsFile.getPath() + " already exists and will be appended to"); */

		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(ind);
		sb.append(",");
		sb.append(thread_time);
		sb.append(",");
		sb.append(routing_time);
		sb.append(",");		
		sb.append(fileSize.length);		
		pw.write(sb.toString());	
		pw.close();
		System.out.println("Transmission stats recorded");		
		
		/* FileWriter writer = new FileWriter(statsFile,true);
		writer.write("Client ID: " + ind + "\n");
		writer.write("Routing Table lookup time: " + t + "ns\n"); //prints lookup time in nanoseconds
		writer.write("File size: " + fileSize.length + " bytes\n");
		
		//writer.write("\n");
		writer.close(); */
	 }



	

}