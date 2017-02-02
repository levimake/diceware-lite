package com.levimake.dicewarelite;

/**
 * Created by ${USER} on ${DATE}.
 */


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Diceware {

    private static int DICE_PER_WORD = 5;

    private SparseArray<String> _wordMap;

    public Diceware(Context context, int REQUIRED_NUMBER_OF_ROLLS) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        InputStream inputStream;

        String filename = "diceware_wordlist_";

        if(sharedPrefs.getString("prefType","number").equals("passphrase")) {
            filename+=sharedPrefs.getString("prefLanguage", "english");
        }
        else {
            filename+=sharedPrefs.getString("prefType","passphrase");
        }

        DICE_PER_WORD=REQUIRED_NUMBER_OF_ROLLS;

        inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier(filename,
                        "raw", context.getPackageName()));

        List<String> diceLines = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            inputStream.reset();
            String line;
            while ((line = br.readLine()) != null) {
                diceLines.add(line);
            }
            br.close();
        } catch (IOException e) {
            // Problem reading the diceware wordlist.  Return.
            // Users of _wordMap will have to ensure that it is not null.
            return;
        }


        // Some diceware word lists are separated by tabs, some by single spaces.  Try both.
        String[] separators = new String[]{"\t", " "};
        String tokenSeparator = null;
        for (String separator : separators) {
            if (diceLines.get(0).contains(separator)) {
                tokenSeparator = separator;
                break;
            }
        }

        // Parse the file into our word map.
        _wordMap = new SparseArray<>();
        for (String diceLine : diceLines) {
            String numberString = diceLine.split(tokenSeparator)[0];
            String word = diceLine.split(tokenSeparator)[1];
            _wordMap.append(Integer.parseInt(numberString), word);
            Log.v("WORD",word);
        }

        Log.v("Adding","Completed");

        diceLines.clear();

    }

    public String generatePassword(Collection<Integer> randomNumbers, boolean withSpaces) throws PasswordGenerationException {

        StringBuilder passwordBuilder = new StringBuilder();
        StringBuilder numberBuilder = new StringBuilder();
        for (Integer randomNumber : randomNumbers) {
            numberBuilder.append(randomNumber);
            if (numberBuilder.length() == DICE_PER_WORD) {
                Log.v("NUMBER",randomNumber.toString());
                Log.v("DICE_PER_WORD",String.valueOf(DICE_PER_WORD));
                String word = _wordMap.get(Integer.parseInt(numberBuilder.toString()));
                Log.v("Word",word);
                if(word.equals("@RollAgain@")) {
                    return "rollagain@dicewarelite";
                }

                passwordBuilder.append(word);
                numberBuilder = new StringBuilder();

                if (withSpaces) {
                    passwordBuilder.append(" ");
                }

            }
        }
        return passwordBuilder.toString();

    }

    public void reset() {
        _wordMap.clear();
    }

    public class PasswordGenerationException extends Exception {
        public PasswordGenerationException(String message) {
            super(message);
        }
    }


}
