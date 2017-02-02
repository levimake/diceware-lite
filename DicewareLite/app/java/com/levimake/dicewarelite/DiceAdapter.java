package com.levimake.dicewarelite;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ${USER} on ${DATE}.
 */


public class DiceAdapter extends RecyclerView.Adapter<DiceAdapter.MyViewHolder> {

    private final List<Dice> diceList;
    private final TextView mTextView;
    private String temp = "";
    ImageView reloadButton,tickButton;
    Diceware diceware;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView dicePhrase;
        public final TextView diceSequence;
        public final TextView counter;
        public final ImageView imageRemoveIcon;

        public MyViewHolder(View view) {
            super(view);
            counter = (TextView) view.findViewById(R.id.counter_textview);
            dicePhrase = (TextView) view.findViewById(R.id.dicephrase_textview);
            diceSequence = (TextView) view.findViewById(R.id.diceseq_textview);
            imageRemoveIcon = (ImageView) view.findViewById(R.id.close_button);
        }

    }


    public DiceAdapter(List<Dice> diceList, TextView tv, ImageView rButton, ImageView tButton) {
        this.diceList = diceList;
        mTextView = tv;
        reloadButton=rButton;
        tickButton=tButton;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dicephrase_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Dice dice = diceList.get(position);

        setCounter();
        setPass();

        holder.counter.setText("" + String.valueOf(dice.getCounter()) + ". ");
        holder.dicePhrase.setText(dice.getDicePhrase());
        holder.diceSequence.setText(dice.getDiceSequence());
        holder.imageRemoveIcon.setImageResource(dice.getImageId());

        holder.imageRemoveIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newPosition = holder.getAdapterPosition();

                if (diceList.get(newPosition).getImageId() == R.drawable.close_button_green) {

                    diceList.remove(newPosition);
                    notifyItemRemoved(newPosition);
                    notifyItemRangeChanged(newPosition, diceList.size());

                    tickButton.setVisibility(View.GONE);

                    if(diceList.size()==0) {
                        reloadButton.setVisibility(View.GONE);
                    }

                }

                setPass();
                setCounter();
                holder.counter.setText("" + String.valueOf(dice.getCounter()));


            }
        });


    }

    @Override
    public int getItemCount() {
        return diceList.size();
    }

    public List<Dice> getList() {
        return this.diceList;
    }

    public void setPass() {
        temp = "";
        for (int i = 0; i < diceList.size(); i++) {
            temp += diceList.get(i).getDicePhrase();
            //temp += " ";
        }
        mTextView.setText("" + temp);
    }

    private void setCounter() {
        for (int i = 0; i < diceList.size(); i++) {
            diceList.get(i).setCounter(i + 1);
        }
    }

}
