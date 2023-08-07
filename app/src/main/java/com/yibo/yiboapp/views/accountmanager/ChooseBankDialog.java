package com.yibo.yiboapp.views.accountmanager;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;

import java.util.List;

public class ChooseBankDialog extends Dialog {

    private TextView textTitle;
    private BankAdapter adapter;
    private ChooseBankListener chooseBankListener;
    private int chosenPosition = -1;


    public ChooseBankDialog(Context context, List<String> banks){
        super(context);

        View view = View.inflate(context, R.layout.dialog_choose_bank, null);
        setContentView(view);
        textTitle = view.findViewById(R.id.text_title);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BankAdapter(banks);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        Display d = window.getWindowManager().getDefaultDisplay();
        layoutParams.width = d.getWidth() * 5/6;
        layoutParams.height = d.getHeight() * 3/4;
        window.setAttributes(layoutParams);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void setChooseBankListener(ChooseBankListener chooseBankListener) {
        this.chooseBankListener = chooseBankListener;
    }

    public void setChosenPosition(int position){
        chosenPosition = position;
        adapter.notifyDataSetChanged();
    }

    private class BankAdapter extends RecyclerView.Adapter<BankHolder> implements View.OnClickListener {

        private List<String> banks;

        private BankAdapter(List<String> banks){
            this.banks = banks;
        }

        @Override
        public int getItemCount() { return banks.size(); }

        @NonNull
        @Override
        public BankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(parent.getContext(), R.layout.item_choose_bank, null);
            view.setOnClickListener(this);
            return new BankHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BankHolder holder, int position) {
            holder.bindData(position, position == chosenPosition, banks.get(position));
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if(chooseBankListener != null){
                chooseBankListener.onBankChosen(position, banks.get(position));
            }
        }
    }

    private static class BankHolder extends RecyclerView.ViewHolder{

        private ImageView imageChoose;
        private TextView textName;

        private BankHolder(View view){
            super(view);

            imageChoose = view.findViewById(R.id.image_choose);
            textName = view.findViewById(R.id.text_name);
        }

        private void bindData(int position, boolean isChosen, String bankName){
            itemView.setTag(position);
            imageChoose.setImageResource(isChosen ? R.drawable.icon_circle_light : R.drawable.icon_circle);
            textName.setText(bankName);
        }
    }

    public interface ChooseBankListener{
        void onBankChosen(int position, String bankName);
    }
}
