import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.awt.BorderLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class Server extends JFrame
{
    private JTextArea displayArea;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;



    // set up GUI
    /**
     * Server constructor used to set up JFrame elements
     * */
    public Server()
    {
        super("Server - Character Converter - Morse and English");
        //jframe operations
        displayArea = new JTextArea();
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        setSize(300, 150); // set size of window
        setVisible(true); // show window

    }

    /**
     *  Starts the server and creates a ServerSocket to connect to the client
     *  Also runs waitForConnection(), getStreams() and processConnection() while waiting for a message from the Client
     *
     */
    public void runServer()
    {
        try // set up server to receive and process connections
        {
            server = new ServerSocket(12345, 100); // create ServerSocket to connect with client

            while (true)
            {
                try
                {
                    waitForConnection(); // wait for a connection
                    getStreams(); // get input & output streams
                    processConnection(); // process connection
                }
                catch (EOFException eofException)
                {
                    displayMessage("\nServer terminated connection");
                }
                finally
                {
                    closeConnection(); //  close the  connection

                }
            }
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace(); //prints the IOException if one occurs
        }
    }

    /**
     * waitForConnection Waits for a new connection from a soon-to-be made client
     * */
    private void waitForConnection() throws IOException
    {
        displayMessage("Waiting for connection\n");
        connection = server.accept();
        displayMessage("Connection received from: " +
                connection.getInetAddress().getHostName());
    }

    /**
     * getStreams is used to manage the streams of text from the client and server, sending/receiving to the socket
     * based on what information is being sent/received.
     * @throws IOException if an error occurs with the IO operations
     * */
    private void getStreams() throws IOException
    {

        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();


        input = new ObjectInputStream(connection.getInputStream());

        displayMessage("\nGot I/O streams\n");
    }

    /**
     * processConnection() attemps to process a specific message being sent from the client and tries to apply
     * the conversion logic I have implemented below. I use a pattern to help determine what kind of conversion
     * will be necessary, and then send the data accordingly.
     * @throws IOException if an error occurs with the IO operations
     * */

    private void processConnection() throws IOException {
        String message = "Connection successful";
        sendData(message);
        CharacterConversion morse = new CharacterConversion("");

        do {
            try {
                String morseMessage = (String) input.readObject();
                displayMessage("\nCONVERTING FROM CLIENT>>> " + morseMessage);
                morse.setConvert(morseMessage);

                // Regex to match valid English and numeral characters
                Pattern validEnglishPattern = Pattern.compile("[a-zA-Z0-9 ]+");
                // Regex to match valid Morse Code characters (dots, dashes, and spaces)
                Pattern validMorsePattern = Pattern.compile("[\\.\\- ]+");

                Matcher englishMatcher = validEnglishPattern.matcher(morse.getConvert());
                Matcher morseMatcher = validMorsePattern.matcher(morse.getConvert());

                if (englishMatcher.matches()) {
                    // Input contains only valid English characters and numbers
                    String output = CharacterConversion.convertE2M(morse.getConvert());
                    sendData(output);
                } else if (morseMatcher.matches()) {
                    // Input contains only valid Morse Code characters
                    String output = CharacterConversion.convertM2E(morse.getConvert());
                    sendData(output);
                } else {
                    // Input contains invalid characters
                    sendData("Error Code 256: Invalid character(s) used.");
                }

                displayMessage("\nSENDING BACK TO CLIENT...");
            } catch (ClassNotFoundException classNotFoundException) {
                displayMessage("\nError code 102 - UNKNOWN OBJECT - ERROR");
            }
        } while (true);
    }

    /**
     * closeConnection() ends the connection between the server and client
     * @throws IOException if an error occurs with the IO operations
     * */
    private void closeConnection()
    {
        displayMessage("\nTerminating connection\n");

        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch (IOException ioException)
        {
            ioException.printStackTrace();
        }
    }

    /**
     * sendData() writes to the output and sends the data to the client
     * @param message to be sent
     * @throws IOException if an error occurs with the IO operations
     * */
    private void sendData(String message)
    {
        try
        {
            output.writeObject("SERVER>>> " + message);
            output.flush();
            displayMessage("\nSERVER>>> " + message);
        }
        catch (IOException ioException)
        {
            displayArea.append("\nERROR CODE: 255 - Error writing object");
        }
    }
    /**
     * isplayMessage uses a final String to determine what message to display
     * @param messageToDisplay determines message to be displayed
     * */
    private void displayMessage(final String messageToDisplay)
    {
        SwingUtilities.invokeLater(
                new Runnable() //anonymous inner class, Runnable means instances are intended to be executed by a thread
                {
                    public void run()
                    {
                        displayArea.append(messageToDisplay);
                    }
                }
        );
    }


}