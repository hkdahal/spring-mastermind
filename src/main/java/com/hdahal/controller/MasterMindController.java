package com.hdahal.controller;

import com.hdahal.model.MastermindModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observable;

/**
 * Author: Hari Dahal
 * Package: com.hdahal.controller
 * Project: mastermind
 * Created on: Feb 26, 2017
 */

@Controller
public class MasterMindController {

    /**
     * The underlying model.
     */
    private MastermindModel model;

    @RequestMapping("/")
    String info(Model model){
        this.model = new MastermindModel();
        // this.model.addObserver(this);
        model.addAttribute("info", displayGame());
        return "index";
    }

    // this.model.addObserver(this);

    private ArrayList<String> displayHelp() {

        ArrayList<String> results = new ArrayList<>();

        results.add("");
        results.add("\nChoose numbers from 1-" +
                MastermindModel.UNIQUE_SYMBOLS + " for each guess component.");
        results.add("Each 'B' in your clue indicates a match.");
        results.add("Each 'W' in your clue indicates a correct symbol");
        results.add(" but incorrect location.\n");
        results.add("Available commands:");
        results.add("guess a b c d ... -- make next guess");
        results.add("quit              -- quit the game");
        results.add("reset             -- start a new game");
        results.add("peek              -- toggle solution visibility");

        // displayBoard()

        return results;
    }

    /**
     * Display the solution row, the guess rows, and the clues.
     * Clues are output as a sequence of characters to the left of the guess:
     * each B indicates a correct symbol and location,
     * each W indicates a correct symbol but incorrect location.
     * Each guess appears as the numerical symbol values chosen.
     * Guesses not yet made are represented with "X" characters.
     * The solution is represented by "X" characters, unless it
     * is visible, in which case it is the numerical symbol values.
     *
     * This display is flexible for up to 9 different symbols.
     */
    private ArrayList<String> displayBoard() {
        ArrayList<String> results = new ArrayList<>();

        results.add("\n");

        ArrayList<Integer> sol = this.model.getSolution();
        ArrayList<Character> clues = this.model.getClueData();
        ArrayList<Integer> guesses = this.model.getGuessData();

        String result = "";
        for(int i = 0; i < MastermindModel.CODE_LENGTH + 1; i++) {
            result += " ";
        }
        results.add(result);
        result = "";
        for(int i = 0; i < MastermindModel.CODE_LENGTH; i++) {
            if (sol.get(i) == 0) {
                result += "X ";
            } else {
                result += sol.get(i) + " ";
            }
        }
        results.add(result);
        result = "";
        results.add("\n");

        for(int i = 0; i < MastermindModel.CODE_LENGTH + 1; i++) {
            result += " ";
        }
        results.add(result);
        result = "";


        for(int i = 0; i < 2*MastermindModel.CODE_LENGTH - 1; i++) {
            result += "-";
        }
        results.add(result);
        result = "";

        results.add("\n");

        for(int i = 0; i < MastermindModel.MAX_GUESSES; i++) {
            // need to reverse top-to-bottom order of clues/guesses
            int loc = MastermindModel.CODE_LENGTH *
                    (MastermindModel.MAX_GUESSES - i - 1);
            result = "";
            for (int j = 0; j < MastermindModel.CODE_LENGTH; j++) {
                result += clues.get(loc + j);
            }
            results.add(result);
            result = " ";

            for (int j = 0; j < MastermindModel.CODE_LENGTH; j++) {
                if (guesses.get(loc + j) == 0) {
                    result += "X ";
                } else {
                    result += guesses.get(loc + j) + " ";
                }
            }
            result += ("  Guess: " + (MastermindModel.MAX_GUESSES - i));
            results.add(result);
        }

        return results;
    }

    /** Displays one of three messages: winning message, losing message,
     * or number of guessing remaining message.
     */
    private ArrayList<String> displayMessage() {
        ArrayList<String> results = new ArrayList<>();

        results.add("\n");
        if (this.model.getVictoryStatus()) {
            results.add("You won the game!!");
        } else if (this.model.getRemainingGuesses() == 0) {
            results.add("You lost the game!!");
        } else {
            results.add("You have " + this.model.getRemainingGuesses());
            results.add(" guesses remaining.");
        }

        return results;
    }

    /**
     * Textual display of Mastermind game state.
     */
    private ArrayList<ArrayList<String>> displayGame() {
        return new ArrayList<>(Arrays.asList(displayHelp(), displayBoard(), displayMessage()));
    }

    /**
     * Update method causes the text user interface
     * to be re-displayed.  The help information
     * is not included in the display.
     *
     * @param o the observable object (not used)
     * @param arg an argument passed to the notifyObservers method (not used)
     */
    public void update(Observable o, Object arg) {
        displayGame();
    }
}

