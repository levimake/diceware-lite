package com.levimake.dicewarelite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class credits extends AppCompatActivity {

    // Array of strings...
    private final String[] creditsArray = {
            "Arnold Reinhold originally created Diceware in 1995",
            "Catalan word lists provided by Marcel Hernandez.",
            "Danish word list provided by Povl Falk-Jensen.",
            "German word list provided by Benjamin Tenne.",
            "Esperanto translated by Makis Diras.",
            "Spanish translated by Manuel Palao.",
            "Finnish translated by Kai Puolamaki.",
            "French translated by Joachim Dubuquoy-Portois.",
            " Italian translated by Tarin Gamberini.",
            "Japanese translated by Hiroshi Yuki.",
            "Maori word list provided by Rangi Kemara",
            "Norwegian word list provided by Willy T. Koch",
            "Dutch word list provided by Bart Van den Eynde",
            "Polish translated by Piotr (DrFugazi) Tarnowski, Computer Science Techniques Centre, University of Silesia, Katowice, PL.",
            "Portuguese word list translated by Patxi Pierce.",
            "Swedish word list provided by Magnus Bodin.",
            "Turkish word list provided by Mert Dirik.",
            "The longer list in English is provided by Electronical Frontier Foundation (EFF)",
            "Diceware Lite is developed by LeviMake"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);


        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.credits, creditsArray);

        final ListView listView = (ListView) findViewById(R.id.credits_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                //TODO
            }
        });
    }


}
