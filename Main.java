// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Scanner;


public class Main {
    final int maxDice = 5;
    final int maxRolls = 3;
    ArrayList<Integer> playerSetAsideDice = new ArrayList<Integer>();
    ArrayList<Integer> aiSetAsideDice = new ArrayList<Integer>();
    ArrayList<Integer> playerDice = new ArrayList<Integer>();
    ArrayList<Integer> aiDice = new ArrayList<Integer>();
    Scanner playerResponse = new Scanner(System.in);
    boolean playGame;
    int playerScore;
    int aiScore;
    int playerWins;
    int aiWins;
    int currentRoll = 0;

    public void initialize() {
        currentRoll = 0;
        playerScore = 0;
        aiScore = 0;
        this.playerSetAsideDice = new ArrayList<Integer>();
        this.aiSetAsideDice = new ArrayList<Integer>();

    }
    public ArrayList<Integer> rollDice(int numOfDice) {
        ArrayList<Integer> diceRoll = new ArrayList<Integer>();
        for (int i = 0; i < numOfDice; i++) {
            diceRoll.add((int) (Math.random() * 6) + 1);
        }
        return diceRoll;
    }

    public int calculateScore(ArrayList<Integer> dice) {
        int score = 0;
        for(Integer i : dice) {
            score += i;
        }
        return score;
    }

    public ArrayList<Integer> combineArrayLists(ArrayList<Integer> array1, ArrayList<Integer> array2) {
        ArrayList<Integer> combinedList = new ArrayList<Integer>(array1);
        combinedList.addAll(array2);
        return combinedList;
    }

    public ArrayList<Integer> parsePlayerResponse(String response) {
        //turn response string into number array
        ArrayList<Integer> parsedResponse = new ArrayList<Integer>();
        String[] responseArray = response.split(" ");
        for(String i : responseArray) {
            Integer temp;
            try {
                //sub 1 to convert from human to computer indexing
                temp = Integer.parseInt(i);
                parsedResponse.add(temp - 1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid text input. Please enter a valid set of dice to keep. (e.g. 1 2 3) (or 0 to continue)");
                return parsePlayerResponse(playerResponse.nextLine());
            }
            if (temp > 6 || temp < 0) {
                System.out.println("Invalid number input. Please enter a valid set of dice to keep. (e.g. 1 2 3) (or 0 to continue)");
                return parsePlayerResponse(playerResponse.nextLine());
            }
        }
        return parsedResponse;
    }

    public void turn() {
        currentRoll++;
        //roll dice
        ArrayList<Integer> playerRolledDice = rollDice(maxDice - playerSetAsideDice.size());
        ArrayList<Integer> aiRolledDice = rollDice(maxDice - aiSetAsideDice.size());

        //combine rolled dice and set aside dice
        playerDice = combineArrayLists(playerSetAsideDice, playerRolledDice);
        aiDice = combineArrayLists(aiSetAsideDice, aiRolledDice);

        //calculate score
        playerScore = calculateScore(playerDice);
        aiScore = calculateScore(aiDice);

        //print dice rolls and score
        System.out.println("PLAYER \t COMPUTER\n");
        for(int i = 0; i < playerDice.size(); i++) {
            System.out.println((i + 1) + ". " + playerDice.get(i) + "\t" + (i + 1) + ". " + aiDice.get(i));
        }
        System.out.println("PLAYER SCORE: " + playerScore + "\tCOMPUTER SCORE: " + aiScore);

        //get response from player
        //only if not last turn
        if (currentRoll == maxRolls) return;
        System.out.println("Enter the numbers of each dice you want to set aside (separated by spaces), or 0 to start the next turn: ");
        StringBuilder setAsideDiceString = new StringBuilder();
        for (Integer Dice : playerSetAsideDice) {
            setAsideDiceString.append(Dice).append(" ");
        }
        System.out.println("Current set aside dice: " + setAsideDiceString);

        //$$$$$$$$$$$$$
        String playerResponseString = playerResponse.nextLine();
        //parse, test, and convert response to computer indexing
        ArrayList<Integer> indexedResponse = parsePlayerResponse(playerResponseString);
        for(Integer i : indexedResponse) {
            //make sure player does duplicate saved die
            if (i < playerSetAsideDice.size()) continue;
            else {
                playerSetAsideDice.add(playerDice.get(i));
            }
        }
        //set aside dice for ai
        for(int i = 0; i < aiDice.size(); i++) {
            if (i < aiSetAsideDice.size()) continue;
            else if (aiDice.get(i) > 3 ) {
                aiSetAsideDice.add(aiDice.get(i));
            } else continue;
        }
    }

    public void decideWinner(){
        if (playerScore > aiScore) {
            System.out.println("Player wins!");
            playerWins++;
        } else if (playerScore < aiScore) {
            System.out.println("Computer wins!");
            aiWins++;
        } else {
            System.out.println("Tie!");
        }
    }




    public static void main(String[] args) {
        Main game = new Main();
        game.playGame = true;
        game.playerWins = 0;
        game.aiWins = 0;

        while (game.playGame) {
            game.initialize();
            while (game.currentRoll < game.maxRolls) {
                game.turn();
            }
            game.decideWinner();
            System.out.println("Player wins: " + game.playerWins + "\t Computer wins: " + game.aiWins);
            System.out.println("Play again? (y/n)");
            String playAgain = game.playerResponse.nextLine();
            if (playAgain.equals("n")) {
                game.playGame = false;
            }
        }
    }

    // @Test
    // void addition() {
    //     assertEquals(2, 1 + 1);
    // }
}
