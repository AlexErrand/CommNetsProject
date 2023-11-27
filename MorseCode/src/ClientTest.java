import javax.swing.JFrame;

public class ClientTest
{
    public static void main(String[] args)
    {
        Client application;
        if (args.length == 0)
            application = new Client("192.168.56.1"); //setting local ip address
        else
            application = new Client(args[0]);

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.runClient();
    }
}

