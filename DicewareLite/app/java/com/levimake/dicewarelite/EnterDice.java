package com.levimake.dicewarelite;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;


public class EnterDice extends AppCompatActivity {


    static {
        PRNGFixes.apply();
    }

    private int numberOfWords = 0;
    private int i = 0;
    private int flag = 0;
    private List<Integer> _dieValues;
    private String source;

    private final List<Dice> diceList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DiceAdapter mAdapter;

    private String temp = "";

    private int REQUIRED_NUMBER_OF_ROLLS;

    private TextView textView;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout, linearLayout1;

    private ImageView tickImageView, reloadImageView;

    private Diceware diceware;

    private boolean withSpaces = true;
    private String language;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_enter_dice);

        if (_dieValues == null) {
            _dieValues = new ArrayList<>();
        }

        //getting ready

        relativeLayout = (RelativeLayout) findViewById(R.id.dice_display);
        linearLayout = (LinearLayout) findViewById(R.id.non_dice_layout);
        linearLayout1 = (LinearLayout) findViewById(R.id.linear_layout_list);

        textView = (TextView) findViewById(R.id.password_display_text_view);
        reloadImageView = (ImageView) findViewById(R.id.reload_button);
        tickImageView = (ImageView) findViewById(R.id.tick_button);

        reloadImageView.setVisibility(View.GONE);
        tickImageView.setVisibility(View.GONE);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new DiceAdapter(diceList, textView, reloadImageView, tickImageView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }

    public void diceClick(View view) throws Diceware.PasswordGenerationException {

        final ImageView imageView = (ImageView) findViewById(view.getId());

        if (diceList.size() >= numberOfWords) {
            Toast.makeText(this, "Rolls are completed.", Toast.LENGTH_SHORT).show();
            return;
        }

        i++;

        ValueAnimator animator = ValueAnimator.ofInt(0, 255);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    imageView.setImageAlpha((Integer) valueAnimator.getAnimatedValue());
                }
            }
        });
        animator.start();


        switch (view.getId()) {
            case R.id.dice_1:
                _dieValues.add(1);
                break;
            case R.id.dice_2:
                _dieValues.add(2);
                break;
            case R.id.dice_3:
                _dieValues.add(3);
                break;
            case R.id.dice_4:
                _dieValues.add(4);
                break;
            case R.id.dice_5:
                _dieValues.add(5);
                break;
            case R.id.dice_6:
                _dieValues.add(6);
                break;
        }

        if (i == REQUIRED_NUMBER_OF_ROLLS) {
            addValue();
        }

        update();

    }

    private void addValue() throws Diceware.PasswordGenerationException {

        flag++;

        update();

        i = 0;

        String result = diceware.generatePassword(_dieValues, withSpaces);

        String diceSeq = _dieValues.toString();
        _dieValues.clear();

        if (result.equals("rollagain@dicewarelite")) {
            Toast.makeText(this, "Please Roll Again", Toast.LENGTH_SHORT).show();
        } else {
            recyclerView.setAdapter(mAdapter);
            Dice dice = new Dice(result, diceSeq, R.drawable.close_button_green, flag);
            diceList.add(dice);
            mAdapter.notifyItemInserted(diceList.size() - 1);
            recyclerView.scrollToPosition(diceList.size() - 1);
        }

        if (diceList.size() == 1) {
            reloadImageView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                reloadImageView.setImageAlpha(0);
            }
            ValueAnimator animator = ValueAnimator.ofInt(0, 255);
            animator.setDuration(400);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        reloadImageView.setImageAlpha((Integer) valueAnimator.getAnimatedValue());
                    }
                }
            });
            animator.start();
        }

        update();

        if (diceList.size() == numberOfWords) {
            tickImageView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                tickImageView.setImageAlpha(0);
            }
            ValueAnimator animator = ValueAnimator.ofInt(0, 255);
            animator.setDuration(400);
            animator.setInterpolator(new DecelerateInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        tickImageView.setImageAlpha((Integer) valueAnimator.getAnimatedValue());
                    }
                }
            });
            animator.start();
        }


    }

    private void update() {

        if (flag == 1) {
            flag = 0;
        }

        temp = "";
        for (int i = 0; i < diceList.size(); i++) {
            temp += diceList.get(i).getDicePhrase();
        }

    }


    private void randomOffline() {

        int count = 0;
        while (count < numberOfWords) {
            SecureRandom secureRandom = new SecureRandom();

            for (int i = 0; i < REQUIRED_NUMBER_OF_ROLLS; i++) {
                _dieValues.add(secureRandom.nextInt(6) + 1);
            }

            String result = null;
            try {
                result = diceware.generatePassword(_dieValues, withSpaces);
            } catch (Diceware.PasswordGenerationException e) {
                e.printStackTrace();
            }


            String diceSeq = _dieValues.toString();
            _dieValues.clear();

            if (result.equals("rollagain@dicewarelite")) {
                //do nothing
            } else {
                recyclerView.setAdapter(mAdapter);
                Dice dice = new Dice(result, diceSeq, R.drawable.dice_1, flag);
                diceList.add(dice);
                mAdapter.notifyItemInserted(diceList.size() - 1);
                recyclerView.scrollToPosition(diceList.size() - 1);
                count++;
            }


        }

    }


    private void randomOnline() {

        Button button = (Button) findViewById(R.id.nextButton);
        button.setEnabled(true);
        RequestQueue queue = Volley.newRequestQueue(EnterDice.this);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://www.random.org/integers/?num=" + REQUIRED_NUMBER_OF_ROLLS * numberOfWords + "&min=1&max=6&col=1&base=10&format=plain&rnd=new",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        String[] tokens = response.split("\n");
                        int i = 0;

                        while (i < numberOfWords) {
                            _dieValues.clear();

                            for (int j = 0; j < REQUIRED_NUMBER_OF_ROLLS; j++) {
                                Integer integer = Integer.parseInt(tokens[((i + 1) * (j + 1)) - 1]);
                                _dieValues.add(integer);
                            }

                            String result = null;
                            try {
                                result = diceware.generatePassword(_dieValues, withSpaces);
                            } catch (Diceware.PasswordGenerationException e) {
                                e.printStackTrace();
                            }

                            String diceSeq = _dieValues.toString();
                            recyclerView.setAdapter(mAdapter);

                            if (mAdapter.getItemCount() < numberOfWords) {

                                Dice dice = new Dice(result, diceSeq, R.drawable.dice_1, flag);
                                diceList.add(dice);
                                mAdapter.notifyItemInserted(diceList.size() - 1);

                            }

                            i++;
                            progressDialog.dismiss();


                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Check your internet connection or Refresh", Toast.LENGTH_SHORT).show();
                Button button = (Button) findViewById(R.id.nextButton);
                button.setEnabled(false);
                progressDialog.dismiss();
            }


        });
        queue.add(stringRequest);
    }

    public void nextButtonClick(View view) {
        update();
        startActivity(new Intent(this, ViewPassword.class).putExtra(Intent.EXTRA_TEXT, temp));
    }

    public void refreshButtonClick(View view) {
        finish();
        startActivity(new Intent(getIntent()));
    }

    public void reloadButtonClick(View view) {
        _dieValues.clear();
        i = 0;
        getPrefs();
        new LoadingTask().execute();
        ValueAnimator animator = ValueAnimator.ofInt(0, 255);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    reloadImageView.setImageAlpha((Integer) valueAnimator.getAnimatedValue());
                }
            }
        });
        animator.start();
        reloadImageView.setVisibility(View.GONE);
        tickImageView.setVisibility(View.GONE);
    }

    public void tickButtonClick(View view) {
        diceList.clear();
        mAdapter.notifyDataSetChanged();
        mAdapter.setPass();

        Intent intent = new Intent(EnterDice.this, ViewPassword.class)
                .putExtra(Intent.EXTRA_TEXT, temp);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_pass_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i = new Intent(EnterDice.this, SettingsActivity.class);
                startActivity(i);
                return true;

            case R.id.action_help:
                startActivity(new Intent(EnterDice.this, HelpActivity.class));
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPrefs();
        new LoadingTask().execute();
    }

    @Override
    public void onPause() {
        super.onPause();
        diceware.reset();
    }

    public void getPrefs() {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linearLayout1.getLayoutParams();
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        numberOfWords = Integer.parseInt(sharedPrefs.getString("prefNumberOfWords", "6"));
        source = sharedPrefs.getString("prefSource", "dice");

        linearLayout.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);

        params.weight = 0.5f;
        params1.weight = 0.5f;

        String spaces = String.valueOf(sharedPrefs.getBoolean("prefSpaces", true));
        if (spaces.equals("true")) {
            withSpaces = true;
        } else {
            withSpaces = false;
        }
        language = sharedPrefs.getString("prefLanguage", "english");
        type = sharedPrefs.getString("prefType", "passphrase");

        if (type.equals("number")|| type.equals("alphanumeric")) {
            REQUIRED_NUMBER_OF_ROLLS = 2;
        } else if (type.equals("ascii")) {
            REQUIRED_NUMBER_OF_ROLLS = 3;
        } else {
            if ((language.equals("eff_short_1")) || (language.equals("eff_short_2"))) {
                REQUIRED_NUMBER_OF_ROLLS = 4;
            } else {
                REQUIRED_NUMBER_OF_ROLLS = 5;
            }
        }

        TextView textView = (TextView) findViewById(R.id.password_display_text_view);
        textView.setText("" + numberOfWords * REQUIRED_NUMBER_OF_ROLLS + " dice rolls required");

        diceList.clear();
        mAdapter = new DiceAdapter(diceList, textView, reloadImageView, tickImageView);
        recyclerView.setAdapter(mAdapter);

        if (source.equals("random_offline") || source.equals("random.org")) {

            relativeLayout.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);


            int orientation = this.getResources().getConfiguration().orientation;

            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                params.weight = 0.7f;
                params1.weight = 0.3f;
            }

            linearLayout1.setLayoutParams(params);
            linearLayout1.setLayoutParams(params);

        }
    }

    private class LoadingTask extends AsyncTask<Void, Void, Integer> {

        ProgressDialog progressDialog;

        @Override
        protected Integer doInBackground(Void... voids) {

            Log.v("NAME","diceware_wordlist"+type);

            diceware = null;
            diceware = new Diceware(EnterDice.this, REQUIRED_NUMBER_OF_ROLLS);
            return 1;

        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                Log.v("ADDED", "DONE");
                progressDialog.dismiss();

                if (source.equals("random_offline")) {
                    randomOffline();
                }

                if (source.equals("random.org")) {
                    if (type.equals("passphrase")||type.equals("alphanumeric")) {
                        randomOnline();
                    } else {
                        Toast.makeText(getApplicationContext(), "Random.org is currently available only for Passphrases and Alpha-Numeric passwords.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(EnterDice.this, "Loading Wordlist", "Please Wait...");
        }

    }


}
