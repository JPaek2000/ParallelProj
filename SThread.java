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
	long t0, t;

	// Constructor
	SThread(Object [][] Table, Socket toClient, int index) throws IOException
	{
			out = new PrintWriter(toClient.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(toClient.getInputStream()));
			RTable = Table;
			addr = toClient.getInetAddress().getHostAddress();
			RTable[index][0] = addr; // IP addresses 
			RTable[index][1] = toClient; // sockets for communication
			ind = index;
	}
	
	// Run method (will run for each machine that connects to the ServerRouter)
	public void run()
	{
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
		t0 = System.nanoTime();
		for ( int i=0; i<10; i++) 
				{
					if (destination.equals((String) RTable[i][0])){
						outSocket = (Socket) RTable[i][1]; // gets the socket for communication from the table
						System.out.println("Found destination: " + destination);
						outTo = new PrintWriter(outSocket.getOutputStream(), true); // assigns a writer
				}}
		//t1 = System.currentTimeMillis();
		t = System.nanoTime() - t0;
		// Communication loop	
		
		while ((inputLine = in.readLine()) != null) {   
            System.out.println("Client/Server said: " + inputLine);
            if (inputLine.equals("Bye.")) {// exit statement 
				//System.out.println("Client ID: " + ind);	//each new client prints their ID {Bryan}
				
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

		try {
			writeStatistics(ind,t,inputLine);
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}

	public static void writeStatistics(int ind,long t, String input) throws IOException {
		File statsFile = new File("statistics_p1.txt");
		final byte[] fileSize =  input.getBytes("UTF-8");

		if (statsFile.createNewFile())         
		   System.out.println("Server statistics written to " + statsFile.getPath());         
		else System.out.println(statsFile.getPath() + " already exists and will be appended to");

		FileWriter writer = new FileWriter(statsFile,true);
		writer.write("Client ID: " + ind + "\n");
		writer.write("Routing Table lookup time: " + t + "ns\n"); //prints lookup time in nanoseconds
		writer.write("File size: " + fileSize.length + " bytes\n");
		
		//writer.write("\n");
		writer.close();
	 }



	

}