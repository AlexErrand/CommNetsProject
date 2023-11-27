import java.util.Arrays;
/**
 * This class operates as the converter of characters and Strings for the Server to be
 * returned to the client
 * */
public class CharacterConversion {

    private String convert;

    /**
     * @param convert is the string to be converted
     */
    public CharacterConversion(String convert) {
        this.convert = convert;
    }

    //getters and setters for String convert
    public String getConvert() {
        return convert;
    }

    public void setConvert(String convert) {
        this.convert = convert;
    }

    //alphabet to be used for conversion
    public static String[] engAlphabet = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"
    };

    //alphabet but is converted to morse code
    public static String[] morseCode = {".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---", "-.-",
            ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-", "..-", "...-", ".--", "-..-", "-.--",
            "--..", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----.", "-----"
    };


    /**
     * convertE2M is a conversion method to convert a phrase from English to morse code
     *
     * @param input to be converted to morse code from english
     * @return a String by using a StringBuilder and .toString()
     */
    public static String convertE2M(String input) {
        String[] userInput = input.split("");//separate each char
        StringBuilder output = new StringBuilder();
        for (int x = 0; x < userInput.length; x++) {
            if (userInput[x].equals(" ")) {//append spaces as necessary
                output.append("   ");
            }
            for (int i = 0; i < engAlphabet.length; i++) {
                if (userInput[x].equalsIgnoreCase(engAlphabet[i])) { //traverse the user input to see if any characters match and use the same index to output morse code
                    output.append(morseCode[i]);

                    if (x != userInput.length - 1 && !userInput[x + 1].equals(" "))
                        output.append(" ");
                }
            }

        }
        return output.toString();
    }

    /**
     * convertM2E is a conversion method to convert a phrase from English to morse code
     *
     * @param input to be converted to morse code from english
     * @return a String by using a StringBuilder and .toString()
     */
    public static String convertM2E(String input) {
        StringBuilder output = new StringBuilder();
        String[] morseInput = input.split(" {1}"); //convert morse to array of strings for simplified conversion using regex
        System.out.println(Arrays.toString(morseInput));
        for (int s = 0; s < morseInput.length; s++) {
            if (s != morseInput.length - 1 && morseInput[s].equals("") && morseInput[s + 1].equals("")) { //checks for "   ". since 3rd space would not apply
                output.append(" ");
            }

            for (int j = 0; j < morseCode.length; j++) {
                if (morseInput[s].equals(morseCode[j])) { //regular character conversion
                    output.append(engAlphabet[j]);
                }
            }
        }
        return output.toString();
    }

}


