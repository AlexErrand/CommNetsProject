# English to Morse Converter

The English to Morse Converter is a versatile application designed to facilitate seamless communication across different languages of dots and dashes. This sophisticated tool empowers users to translate English text into Morse Code and vice versa. By leveraging the robust `CharacterConversion` class alongside regular expressions (regex), the application ensures accurate and efficient conversion processes.

The client-server architecture allows for real-time conversion; users can input text on the client side, and the server promptly processes and returns the translated version.

Key features:
- Bi-directional translation between English and Morse Code.
- Real-time processing and conversion through a client-server model.
- Use of regex for pattern recognition, ensuring conversion accuracy.
- A user-friendly interface that simplifies the conversion experience.

## Installation

Go to the following github page and pull the project: https://github.com/AlexErrand/CommNetsProject.

Once pulled, you can use localhost (127.0.0.1) or use a Local Network IP Address to have multiple users. You can find your Local Network IP
Address by going to your command prompt and typing in "ipconfig". By default, the application is set to localhost. To start the client and server, 
you must first start the server by running the ServerTest.java file, otherwise the client will return an error and close immediately due to no server being present. 
If the ServerTest.java was run first and then ClientTest.java was ran after, an application layer handshake will occur (as shown below in Example Usage), and then you can send either English or Morse code and get them translated into Morse code or English respectively.

## Example Usage
![Here is an Example of our CLient and Server Communicating, and also shows the initial application layer handshaking process](MorseCode/src/ExampleUsage.png)

## License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).



