import java.util.*;
import java.io.*;

public class Hangman
{
    private int badGuesses, totalGuesses, randomInt;
    private char guess;
    private List<String> wordList = new ArrayList<String>();
    private String gameWord, wordStatus = "", incorrectGuesses = "", guesses = "";
    private char[] correctGuesses, wordStat;
    private final String[] stateArr = new String[7];
    Random randomGenerator = new Random();

    public Hangman()
    {
        try
        {
            this.setUpGame();
        }
        catch (Exception ex)
        {
            System.err.println("word list not found. make you you are in correct directory"
                    + " and/or a file called word.list exists in the current directory");
            ex.printStackTrace();
        }
    }

    public void setUpGame() throws FileNotFoundException
    {
    /* populate array of states with hangman pictures */
        stateArr[0] = " +----+\n |    |\n      |\n      |\n      |\n      |\n========\n";
        stateArr[1] = " +----+\n |    |\n O    |\n      |\n      |\n      |\n========\n";
        stateArr[2] = " +----+\n |    |\n O    |\n |    |\n      |\n      |\n========\n";
        stateArr[3] = " +----+\n |    |\n O    |\n/|    |\n      |\n      |\n========\n";
        stateArr[4] = " +----+\n |    |\n O    |\n/|\\   |\n      |\n      |\n========\n";
        stateArr[5] = " +----+\n |    |\n O    |\n/|\\   |\n/     |\n      |\n========\n";
        stateArr[6]  = " +----+\n |    |\n O    |\n/|\\   |\n/ \\   |\n      |\n========\n";
    /* populate wordlist with words from textfile in same folder */
        Scanner in = new Scanner(new File("./word.list"));
        while (in.hasNext())
        {
            wordList.add(in.next());
        }
        in.close();
    /* get a random number and use it to select a random word */
        randomInt = randomGenerator.nextInt(wordList.size());
        gameWord = wordList.get(randomInt);
        /** @param wordStat an array which holds the letters so far guessed and _ for unknown ones
         *  used to create wordStatus string which is re-displayed each guess */
        wordStat = gameWord.toCharArray();
        for (int i = 0; i < gameWord.length(); i++)
        {
            wordStat[i] = '_';
            wordStatus += (wordStat[i] + " ");
        }
    /* make array of correct letters, for comparison against */
        correctGuesses = gameWord.toCharArray();
    }

    public void play()
    {
        while (badGuesses < 6 && !complete())
        {
            System.out.println(stateArr[badGuesses]);
            System.out.println("\nWord so far:\t" + wordStatus);
            System.out.println("Incorrect Guesses:   " + incorrectGuesses);

            getGuess();

            if (isWrong(guess))
            {
                addIncorrectGuess(guess);
                badGuesses++;
            }
            else
            {
                updateWordStatus(guess);
            }
        }

        if (complete())
        {
            endgame("Yes - word is:\t" + wordStatus + "\nYou Win! Congratulations!");
        }
        else
        {
            endgame(stateArr[6] + "You Lose! What a Shame!\nWord was:\t" + doubleSpaced(gameWord));
        }
    }

    public char getGuess()
    {
        while (true)
        {
            String input = System.console().readLine();
            guess = input.charAt(0);

            if (!Character.isLetter(guess))
            {
                System.out.println("Please enter a letter!");
            }
            else if (guesses.contains(Character.toString(guess)))
            {
                System.out.println("You've already guessed that letter. Please guess again.");
            }
            else if (input.length() > 1)
            {
                System.out.println("Please enter one letter at a time!");
            }
            else
            {
                totalGuesses++;
                guesses += guess;
                return guess;
            }
        }
    }

    public String addIncorrectGuess(char letter)
    {
        if (incorrectGuesses.equals(""))
        {
        /* special case to make sure commas are inserted correctly */
            incorrectGuesses += letter;
        }
        else
        {
            incorrectGuesses += (" ," + letter);
        }
        return incorrectGuesses;
    }

    public boolean isWrong(char guess)
    {
        boolean wrong = true;
        for (int i = 0; i < correctGuesses.length; i++)
        {
            if (correctGuesses[i] == guess)
            {
                wrong = false;
            }
        }
        return wrong;
    }

    public boolean complete()
    {
        boolean complete = true;
        for(int i = 0; i < wordStat.length; i++)
        {
            if('_' == wordStat[i])
            {
                complete = false;
            }
        }
        return complete;
    }

    public void updateWordStatus(char guess)
    {
        for (int i = 0; i < gameWord.length(); i++)
        {
            if (gameWord.charAt(i) == guess)
            {
                wordStat[i] = guess;	// replace a '_' with the char guess
            }
        }
        String tmp = new String(wordStat);		// make a string from the char array
        wordStatus = doubleSpaced(tmp);		// update wordStatus by double-spacing the string
    }

    public static String doubleSpaced(String str)
    {
        String dbl_spaced_str = "";
        while (str.length() > 0)
        {
            dbl_spaced_str += (str.substring(0, 1) + " ");
            str = str.substring(1);
        }
        return dbl_spaced_str;
    }

    public void endgame(String message)
    {
        System.out.println(message);
        System.out.println("You made " + totalGuesses + " guesses , with " + badGuesses + " incorrect guesses!");
        Scanner in = new Scanner(System.in);
        System.out.print("Would you like to play again? [y/n]");
        char answer = in.next().charAt(0);
        if (answer == 'y' || answer == 'Y')
        {
            Hangman game = new Hangman();
            game.play();
        }
    }

    public static void main(String[] arguments)
    {
        Hangman game = new Hangman();
        game.play();
    }
}
