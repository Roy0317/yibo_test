package com.yibo.yiboapp.views.accountmanager;

import android.content.Context;
import android.widget.LinearLayout;

public abstract class AccountEntryView extends LinearLayout {

    public static final int TYPE_HINT = -1;
    public static final int TYPE_INPUT = 0;
    public static final int TYPE_SELECTION = 1;
    public static final int TYPE_PASSWORD = 10;

    protected EntryBean entryBean;

    public AccountEntryView(Context context) {
        super(context);
    }

    /**
     * 依据entryBean生成实际的View
     */
    protected abstract void buildView(EntryBean entryBean);

    public EntryBean getOriginalEntryBean(){ return entryBean; }

    /**
     * 回传更新数据之後的entryBean
     */
    public abstract EntryBean getUpdatedEntryBean();



    public static class EntryBean{
        private String tag;
        private String title;
        private String hint;
        private String content;
        private int inputType;  // -1：备注文字。 0：一般的EditText 文字输入。 1：银行list。
        private boolean required;   //是否必填项目
        private boolean editable;   //是否可修改栏位

        public EntryBean(String tag, String title, String hint, String content) {
            this(tag, title, hint, content, TYPE_INPUT, true);
        }

        public EntryBean(String tag, String title, String hint, String content, int inputType) {
            this(tag, title, hint, content, inputType, true);
        }

        public EntryBean(String tag, String title, String hint, String content, int inputType, boolean required) {
            this(tag, title, hint, content, inputType, required, true);
        }

        public EntryBean(String tag, String title, String hint, String content, int inputType, boolean required, boolean editable) {
            this.tag = tag;
            this.title = title;
            this.hint = hint;
            this.content = content;
            this.inputType = inputType;
            this.required = required;
            this.editable = editable;
        }


        public String getTag() {
            return tag;
        }

        public String getTitle() {
            return title;
        }

        public String getHint() {
            return hint;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getInputType() {
            return inputType;
        }

        public boolean isRequired() {
            return required;
        }

        public boolean isEditable() {
            return editable;
        }

        @Override
        public String toString() {
            return "EntryBean{" +
                    "tag='" + tag + '\'' +
                    ", title='" + title + '\'' +
                    ", hint='" + hint + '\'' +
                    ", content='" + content + '\'' +
                    ", inputType=" + inputType +
                    ", required=" + required +
                    ", editable=" + editable +
                    '}';
        }
    }
}
