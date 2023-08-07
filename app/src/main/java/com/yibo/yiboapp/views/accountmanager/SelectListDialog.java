package com.yibo.yiboapp.views.accountmanager;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yibo.yiboapp.R;

import java.util.List;

public class SelectListDialog extends Dialog {

    private final SelectListListener listListener;

    public SelectListDialog(Context context, String title, List<String> list, SelectListListener l){
        super(context);
        this.listListener = l;

        View view = View.inflate(context, R.layout.dialog_simple_select, null);
        setContentView(view);
        TextView textTitle = view.findViewById(R.id.text_title);
        textTitle.setText(title);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SelectionAdapter adapter = new SelectionAdapter(list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));

        setCancelable(true);
        setCanceledOnTouchOutside(true);//点击外部Dialog消失
    }

    private class SelectionAdapter extends RecyclerView.Adapter<SelectionHolder> implements View.OnClickListener {

        private final List<String> selections;

        private SelectionAdapter(List<String> selections){
            this.selections = selections;
        }

        @Override
        public int getItemCount() {
            return selections.size();
        }

        @NonNull
        @Override
        public SelectionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_type, parent, false);
            view.setOnClickListener(this);
            return new SelectionHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SelectionHolder holder, int position) {
            holder.bindData(selections.get(position));
            holder.itemView.setTag(position);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if(listListener != null){
                listListener.onItemSelected(position);
                dismiss();
            }
        }
    }

    private static class SelectionHolder extends RecyclerView.ViewHolder{

        private final TextView textItem;

        private SelectionHolder(View view){
            super(view);
            textItem = view.findViewById(R.id.text_type);
        }

        public void bindData(String item){
            textItem.setText(item);
        }
    }

    public interface SelectListListener { void onItemSelected(int position);}
}
