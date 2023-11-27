import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * This class operates as the client that will be used to communicate english
 * and more phrases to the server for translation
 * */
public class Client extends JFrame
{
    //JFrame objects
    private JTextField enterField;
    private JTextArea displayArea;
    //Strings
    private String message;
    private String chatServer;
    private Socket client;
    //input and output
    private ObjectOutputStream output;
    private ObjectInputStream input;



    /**
     * Client constructor used to set up JFrame elements
     * @param host designates host of the chatServer
     * */
    public Client(String host)
    {
        super("Client - Character Converter - Morse and English");

        chatServer = host;

        enterField = new JTextField();
        enterField.setEditable(false);
        enterField.addActionListener(
                new ActionListener() //anonymous inner class
                {
                    public void actionPerformed(ActionEvent event)
                    {
                        sendData(event.getActionCommand());
                        enterField.setText(""); //sets initial text to ""
                    }
                }
        );

        add(enterField, BorderLayout.NORTH);
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);
        setSize(300, 300);
        setVisible(true);
    }

    /**
     * setTextFieldEditable uses a boolean to determine if the textfield is editable by the client
     * @param editBool
     * */
    private void setTextFieldEditable(final boolean editBool)
    {
        SwingUtilities.invokeLater(
                new Runnable() //anonymous inner class for while the client is active
                {
                    public void run() // sets enterField's editability
                    {
                        enterField.setEditable(editBool);
                    }
                }
        );
    }

    /**
     * displayMessage() uses a final String to determine what message to display
     * @param messageToDisplay determines message to be displayed
     * */
    private void displayMessage(final String messageToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable()
                {
                    public void run()
                    {
                        displayArea.append(messageToDisplay);
                    }
                }
        );
    }


    /**
     * runClient attemps to run connectToServer(), getStreams() and processConnection(), and will throw different errors
     * for different circumstances
     * @throws EOFException if the client connection terminates
     * @throws IOException if an error occured with the user input or returning output
     * */
    public void runClient()
    {
        try
        {
            connectToServer();
            getStreams();
            processConnection(); //process what the server is trying to send
        }
        catch (EOFException eofException)
        {
            displayMessage("\nError Code 101 - Client terminated connection");
            eofException.printStackTrace();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            closeConnection();
        }
    }

    /**
     * connectToServer() establishes a socket to be used to connect to the server with the same port
     * @throws IOException if an error occurs with the IO operations
     * */
    private void connectToServer() throws IOException
    {
        client = new Socket(InetAddress.getByName(chatServer), 12345);
    }

    /**
     * getStreams is used to manage the streams of text from the client and server, sending/receiving to the socket
     * based on what information is being sent/received.
     * @throws IOException if an error occurs with the IO operations
     * */
    private void getStreams() throws IOException
    {
        output = new ObjectOutputStream(client.getOutputStream());
        output.flush();
        input = new ObjectInputStream(client.getInputStream());
    }

    /**
     * processConnection() attemps to process a specific message being input to the client
     * @throws IOException if an error occurs with the IO operations
     * */
    private void processConnection() throws IOException
    {
        setTextFieldEditable(true);
        do
        {
            try
            {
                message = (String) input.readObject();
                displayMessage("\n" + message);
            }
            catch (ClassNotFoundException classNotFoundException)
            {
                displayMessage("\nError code 102 - UNKNOWN OBJECT - ERROR");
            }

        } while (!message.equals("SERVER TERMINATED"));
    }

    /**
     * sendData() writes to the output and sends the data to the server
     * @param message to be sent
     * @throws IOException if an error occurs with the IO operations
     * */
    private void sendData(String message)
    {
        try
        {
            output.writeObject(message);
            output.flush(); //flushes from stream, prints in server
            displayMessage("\nCLIENT>>> Sending to Address:" + InetAddress.getByName(chatServer) + ", Port: " + 12345 + "," + message);
        }
        catch (IOException ioException)
        {
            displayArea.append("\nERROR CODE: 255 - Error writing object");
        }
    }

    /**
     * closeConnection() ends the connection between the server and client
     * @throws IOException if an error occurs with the IO operations
     * */
    private void closeConnection()
    {
        displayMessage("\nCLOSING CONNECTION...");
        setTextFieldEditable(false);
        try
        {
            output.close();//must close operations
            input.close();
            client.close();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
}
