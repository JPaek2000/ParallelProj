import java.net.SocketException;
import java.net.UnknownHostException;

public class Server extends Thread{
    private int socketNumber;
    private String destIP;
    private String router;

    public String msg;

    
    public Server(String router, String destIP, int socketNumber) throws SocketException {
        this.router = router;
        this.destIP = destIP;
        this.socketNumber = socketNumber;
    }

    public void run() {
        try {
        Socket socket = new Socket(router, socketNumber);
        }
        catch (UnknownHostException e) {
            System.err.println("Could not find router: " + router);
        }
    

    }

}
