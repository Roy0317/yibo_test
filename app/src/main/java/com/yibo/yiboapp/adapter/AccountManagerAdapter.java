package com.yibo.yiboapp.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yibo.yiboapp.R;
import com.yibo.yiboapp.entify.PickAccountMultiResponse;
import com.yibo.yiboapp.manager.BankingManager;

public class AccountManagerAdapter extends BaseRecyclerAdapter<PickAccountMultiResponse.AccountBean>
                                        implements View.OnClickListener {

    private PickAccountListener accountListener;

    public AccountManagerAdapter(Context context){
        super(context);
    }

    public void setAccountListener(PickAccountListener l){ this.accountListener = l; }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_account_manager, parent, false);
        view.setOnClickListener(this);
        return new AccountHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((AccountHolder) holder).bindData(position, mList.get(position));
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if(accountListener != null){
            accountListener.onPickAccount(mList.get(position));
        }
    }

    private static class AccountHolder extends RecyclerView.ViewHolder{

        private ImageView imageType;
        private TextView textType;
        private TextView textAccount;
        private TextView textRemark;

        private AccountHolder(View view){
            super(view);

            imageType = view.findViewById(R.id.imageType);
            textType = view.findViewById(R.id.textType);
            textAccount = view.findViewById(R.id.textAccount);
            textRemark = view.findViewById(R.id.textRemark);
        }

        public void bindData(int position, PickAccountMultiResponse.AccountBean account){
            itemView.setTag(position);
            switch (account.getType()){
                case BankingManager.LOCAL_ACCOUNT_ALIPAY:
                    imageType.setImageResource(R.drawable.icon_pay_alipay);
                    textType.setText("支付宝");
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.VISIBLE);
                    textRemark.setText(TextUtils.isEmpty(account.getRealName()) ? account.getUserName() : account.getRealName());
                    break;
                case BankingManager.LOCAL_ACCOUNT_USDT:
                    imageType.setImageResource(R.drawable.icon_pay_usdt);
                    textType.setText("USDT");
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.GONE);
                    break;
                case BankingManager.LOCAL_ACCOUNT_GOPAY:
                    imageType.setImageResource(R.drawable.icon_pay_gopay);
                    textType.setText("GoPay");
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.VISIBLE);
                    textRemark.setText(TextUtils.isEmpty(account.getRealName()) ? account.getUserName() : account.getRealName());
                    break;
                case BankingManager.LOCAL_ACCOUNT_OKPAY:
                    imageType.setImageResource(R.drawable.icon_pay_okpay);
                    textType.setText("OkPay");
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.VISIBLE);
                    textRemark.setText(TextUtils.isEmpty(account.getRealName()) ? account.getUserName() : account.getRealName());
                    break;
                case BankingManager.LOCAL_ACCOUNT_TOPAY:
                    imageType.setImageResource(R.drawable.icon_pay_topay);
                    textType.setText("ToPay");
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.VISIBLE);
                    textRemark.setText(TextUtils.isEmpty(account.getRealName()) ? account.getUserName() : account.getRealName());
                    break;
                case BankingManager.LOCAL_ACCOUNT_VPAY:
                    imageType.setImageResource(R.drawable.icon_pay_vpay);
                    textType.setText("VPay");
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.VISIBLE);
                    textRemark.setText(TextUtils.isEmpty(account.getRealName()) ? account.getUserName() : account.getRealName());
                    break;
                case BankingManager.LOCAL_ACCOUNT_BANK:
                default:
                    imageType.setImageResource(R.drawable.icon_pay_bank);
                    textType.setText(account.getBankName());
                    textAccount.setText(account.getCardNo());
                    textRemark.setVisibility(View.VISIBLE);
                    textRemark.setText(TextUtils.isEmpty(account.getRealName()) ? account.getUserName() : account.getRealName());
                    break;
            }
        }
    }

    public interface PickAccountListener{
        void onPickAccount(PickAccountMultiResponse.AccountBean account);
    }
}
