package com.company;

import java.util.*;
import java.util.concurrent.SynchronousQueue;

public class Main {

    public static void main(String[] args) {


        final int winningScore = 100;
        int score = 0;
        //creates arraylists for players and decks
        ArrayList stockDeck = new ArrayList();
        ArrayList discardDeck = new ArrayList();

        String card = "";
        Scanner sc = new Scanner(System.in);

        boolean gameOver = false;
        boolean firstTime = true;

        //figures out if you want to start the game (just 2 player for now)
        System.out.println("Would you like to begin?");
        String yesNo = sc.nextLine();
        yesNo.toLowerCase();
        Players computer = new Players("Bob");
        Players player = new Players("Me");

        //as long as no isn't registered it will draw you a random card and display it.
        try {
            if (yesNo != "n" && yesNo != "no") {
                for (int x = 0; x < 13; x++) {
                    for (int y = 0; y < 4; y++) {
                        card = fillDeck(x, y);
                        stockDeck.add(card);
                    }
                }

                // shuffle the deck before we begin.
                Collections.shuffle(stockDeck);

                //draws the initial 7 cards into the each hand
                for (int x = 0; x < 7; x++) {
                    Random rand = new Random();
                    //picks random number of the cards that are left in the "stock" pile.
                    int random = rand.nextInt(stockDeck.size());
                    //saves it temporarily
                    String temp = String.valueOf(stockDeck.get(random));
                    //adds the object to the players hand arraylist
                    player.addCard(temp);
                    //deletes the object from the "stock" pile of cards so it can't be drawn again.
                    stockDeck.remove(random);
                    //does the same for the computer
                    random = rand.nextInt(stockDeck.size());
                    temp = String.valueOf(stockDeck.get(random));
                    computer.addCard(temp);
                    stockDeck.remove(random);
                }
            } else {
                System.exit(0);
            }
            computer.displayHand(computer);
            player.displayHand(player);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }


        //game begins.
        while (gameOver == false) {
            //first draw is different than the rest so this plays first
            while (firstTime == true) {
                //switches firstTime boolean
                firstTime = false;
                System.out.println("There are no cards in the discard pile. \n "
                        + "Hit 1 to draw a stock card!");
                int temp = sc.nextInt();
                if (temp == 1) {
                    drawFromStock(stockDeck, player);
                    discardCard(discardDeck, player);
                    computerTurn(computer, stockDeck, discardDeck);
                } else {
                    System.out.println("You can only draw from the stock deck on your first turn!");
                }
            }
            System.out.println("Pick a number:\n" + "1. Draw card from stock pile.\n" + "2. Draw card from discard pile.\n"
                    + "3. Display table/hands.\n" + "4. Lay down some cards.\n" + "5. Meld cards.\n");
            int temp = 0;
                temp = sc.nextInt();
            switch (temp) {
                case 1:
                    drawFromStock(stockDeck, player);
                    discardCard(discardDeck, player);
                    computerTurn(computer, stockDeck, discardDeck);
                    break;
                case 2:
                    drawFromDiscard(discardDeck, player);
                    discardCard(discardDeck, player);
                    computerTurn(computer, stockDeck, discardDeck);
                    break;
                case 3:
                    displayTable(stockDeck, discardDeck, player, computer);
                    break;

            }
        }
        sc.close();
    }

    private static void displayTable(ArrayList stockDeck, ArrayList discardDeck, Players player, Players computer) {
        computer.displayHand(computer);
        //TODO layCards method for meld
        System.out.println("\n\n");
        System.out.println("Stock Deck:");
        System.out.println(stockDeck.size() + " cards\n\n");
        System.out.println("Discard Deck:");
        System.out.println(discardDeck.size() + " cards");
        System.out.println(discardDeck.lastIndexOf(0));
        //TODO more laycards method stuff
        player.displayHand(player);
    }

    private static void discardCard(ArrayList discardDeck, Players player) {
        if (player.getName() != "Bob") {
            Scanner discardScanner = new Scanner(System.in);
            System.out.println("Enter the number that is to the left of the card you would like to discard.");
            int cardNum = (discardScanner.nextInt() - 1);
            String card = String.valueOf(player.getHand().get(cardNum));
            player.removeCard(card);
            discardDeck.add(card);
            System.out.println(player.getName() + " discarded " + card);
            discardScanner.close();
            //computer ai is random so this plays for the computer to discard a card.
        } else {
            Random rand = new Random();
            int cardNum = (rand.nextInt(player.getHand().size()) - 1);
            String card = String.valueOf(player.getHand().indexOf(cardNum));
            player.removeCard(card);
            discardDeck.add(card);
        }
    }

    private static void drawFromStock(ArrayList stockDeck, Players player) {
        //saves it temporarily
        String temp = String.valueOf(stockDeck.get(stockDeck.size() - 1));
        //adds the object to the players hand arraylist
        player.addCard(temp);
        player.displayHand(player);
        stockDeck.remove(stockDeck.size() - 1);
    }

    private static void drawFromDiscard(ArrayList discardDeck, Players player) {
        //saves it temporarily
        String temp = String.valueOf(discardDeck.get(discardDeck.size() - 1));
        //adds the object to the players hand arraylist
        player.addCard(temp);
        player.displayHand(player);
        discardDeck.remove(discardDeck.size());
    }

    //TODO add computer ai rather than just random
    private static void computerTurn(Players computer, ArrayList stockDeck, ArrayList discardDeck) {
        Random rand = new Random();
        int choice = rand.nextInt(2);
        if (discardDeck.size() == 0){
            System.out.println(computer.getName() + " draws from the stock pile.");
            drawFromStock(stockDeck, computer);
        } else if (stockDeck.size() == 0){
            System.out.println(computer.getName() + " draws from the discard pile.");
            drawFromDiscard(discardDeck, computer);
        } else {
            if (choice == 1) {
                System.out.println(computer.getName() + " draws from the stock pile.");
                drawFromStock(stockDeck, computer);
            } else {
                System.out.println(computer.getName() + " draws from the discard pile.");
                drawFromDiscard(discardDeck, computer);
            }
        }
    }

    public static String fillDeck(int num, int suit) {
        String face = "";
        if (num == 0) {
            face = "Ace";
        } else if (num == 10) {
            face = "Jack";
        } else if (num == 11) {
            face = "Queen";
        } else if (num == 12) {
            face = "King";
        } else {
            face = Integer.toString(num);
        }

        String temp = "";
        if (suit == 0) {
            temp = "Hearts";
        } else if (suit == 1) {
            temp = "Clubs";
        } else if (suit == 2) {
            temp = "Diamonds";
        } else if (suit == 3) {
            temp = "Spades";
        }

        String card = face + " " + temp;
        return card;
    }
}