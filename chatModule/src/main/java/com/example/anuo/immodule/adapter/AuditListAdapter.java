package com.example.anuo.immodule.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anuo.immodule.R;
import com.example.anuo.immodule.bean.GetAuditListBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AuditListAdapter extends RecyclerView.Adapter<AuditListAdapter.AuditViewHolder> {

    public static final int AUDIT_ACTION_AGREE = 1;
    public static final int AUDIT_ACTION_REFUSE = 2;
    public static final int AUDIT_ACTION_DELETE = 3;

    private List<GetAuditListBean.UserAuditItem> auditItems;
    private AuditActionListener actionListener;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());



    public AuditListAdapter(List<GetAuditListBean.UserAuditItem> items){
        this.auditItems = items;
    }

    public void setActionListener(AuditActionListener l){
        this.actionListener = l;
    }

    @Override
    public int getItemCount() { return auditItems.size(); }


    @Override
    public AuditViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_audit_list, null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new AuditViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AuditViewHolder auditViewHolder, int position) {
        auditViewHolder.bindData(auditItems.get(position));
    }

    class AuditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textAccount;
        private TextView textStatus;
        private TextView textTime;
        private TextView textAgree;
        private TextView textRefuse;
        private TextView textDelete;

        private AuditViewHolder(View view){
            super(view);

            textAccount = view.findViewById(R.id.text_account);
            textStatus = view.findViewById(R.id.text_status);
            textTime = view.findViewById(R.id.text_time);
            textAgree = view.findViewById(R.id.text_agree);
            textRefuse = view.findViewById(R.id.text_refuse);
            textDelete = view.findViewById(R.id.text_delete);

            textAgree.setOnClickListener(this);
            textRefuse.setOnClickListener(this);
            textDelete.setOnClickListener(this);
        }

        public void bindData(GetAuditListBean.UserAuditItem item){
            textAgree.setTag(item.getUserId());
            textRefuse.setTag(item.getUserId());
            textDelete.setTag(item.getUserId());

            textAccount.setText(item.getAccount());
            switch (item.getAuditStatus()){
                case 1:
                    textStatus.setText("待审核");
//                    textAgree.setEnabled(true);
//                    textRefuse.setEnabled(true);
                    textAgree.setVisibility(View.VISIBLE);
                    textRefuse.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    textStatus.setText("已审核");
//                    textAgree.setEnabled(false);
//                    textRefuse.setEnabled(false);
                    textAgree.setVisibility(View.GONE);
                    textRefuse.setVisibility(View.GONE);
                    break;
                case 3:
                    textStatus.setText("已拒绝");
//                    textAgree.setEnabled(false);
//                    textRefuse.setEnabled(false);
                    textAgree.setVisibility(View.GONE);
                    textRefuse.setVisibility(View.GONE);
                    break;
            }

            textTime.setText(sdf.format(new Date(item.getCreateTime())));
        }

        @Override
        public void onClick(View v) {
            if(actionListener != null){
                int id = v.getId();
                String auditUserID = (String) v.getTag();
                Log.d("AuditListAdapter", "audituserID = " + auditUserID);

                if (id == R.id.text_agree) {
                    actionListener.onAuditAction(auditUserID, AUDIT_ACTION_AGREE);
                } else if (id == R.id.text_refuse) {
                    actionListener.onAuditAction(auditUserID, AUDIT_ACTION_REFUSE);
                }else if(id == R.id.text_delete){
                    actionListener.onAuditAction(auditUserID, AUDIT_ACTION_DELETE);
                }
            }
        }
    }

    public interface AuditActionListener{
        void onAuditAction(String auditUserID, int action);
    }
}
