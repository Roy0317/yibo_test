package crazy_wrapper.Crazy.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * 自定义列表框工厂类
 *
 * @author yingzhang
 */
public class CustomListDialog implements CustomDialogManager {

    Context context;
    LIST_DIALOG_CATEGORY dialogCategory;
    NormalListDialog dialog;
    String[] arr;
    boolean stratAnimation;
    CustomListDialogAdapter adapter;
    ArrayList<DialogMenuItem> datasources;
    OnOperItemClickL onOperItemClick;
    String title;


    public enum LIST_DIALOG_CATEGORY {
        WITH_ADAPTER, JUST_STRING_ARRAY
    }

    public CustomListDialog(Context ctx) {
        this.context = ctx;
    }

    //设置创建的列表框的样式
    public void setStyle(LIST_DIALOG_CATEGORY style) {
        dialogCategory = style;
    }

    //在style为WITH_ADAPTER情况下必须设置
    public void setStringArray(String[] arr) {
        this.arr = arr;
    }

    public void openAnimation(boolean open) {
        this.stratAnimation = open;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //在style为WITH_ADAPTER情况下必须设置
    public void setListContent(ArrayList<DialogMenuItem> datasources) {
        this.datasources = datasources;
        this.adapter = new CustomListDialogAdapter(context, datasources);
    }

    //列表项点击监听器，在style为WITH_ADAPTER情况下设置
    public void setOnListItemClick(OnOperItemClickL onOperItemClick) {
        this.onOperItemClick = onOperItemClick;
    }

    public void dismiss() {
        if (dialog == null) {
            return;
        }
        dialog.dismiss();
    }

    @Override public Dialog createDialog() {

        if (dialogCategory == LIST_DIALOG_CATEGORY.WITH_ADAPTER) {
            dialog = new NormalListDialog(context, adapter);
        } else if (dialogCategory == LIST_DIALOG_CATEGORY.JUST_STRING_ARRAY) {
            dialog = new NormalListDialog(context, arr);
        }
        dialog.title(this.title).titleTextSize_SP(18).titleBgColor(Color.parseColor("#409ED7")).itemPressColor(Color.parseColor("#85D3EF"))
            .itemTextColor(Color.parseColor("#2C2C2C")).itemTextSize(14).cornerRadius(5).widthScale(0.8f);
        //.show();
        dialog.show();
        dialog.setOnOperItemClickL(onOperItemClick);
        return dialog;
    }

    @Override
    public Dialog createDialog(boolean isShow) {
        return null;
    }


}
