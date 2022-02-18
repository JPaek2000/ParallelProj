import java.io.IOException;
import java.util.Scanner;
import java.io.IOException;
public class Main {
    public static void main(String[] args) throws IOException {
        int choice = 0;
        do {
        System.out.println("Make a choice: ");
        System.out.println("1. Listen ");
        System.out.println("2. Send message");
        System.out.println("3. Start Server Router");
        System.out.println("4. Exit");

        Scanner reader = new Scanner(System.in);        
        choice = reader.nextInt();

        //SocketClient cli = new SocketClient();
        switch (choice) {
            case 1: //RunServer()
            case 2: //RunClient()
            case 3: //RunServerRouter()
            case 4: //exit
                break;
            default:
                System.out.println("Choose another number");

            }
        } while (choice > 4);
    }
}
