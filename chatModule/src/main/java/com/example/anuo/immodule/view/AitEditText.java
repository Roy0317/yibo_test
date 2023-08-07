package com.example.anuo.immodule.view;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import com.example.anuo.immodule.R;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 * <p>
 * Author:anuo
 * Date  :19/06/2019
 * Desc  :com.example.anuo.immodule.view 带@操作的edittext
 */
public class AitEditText extends AppCompatEditText {
    public AitEditText(Context context) {
        super(context);
        init();
    }

    public AitEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AitEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        setEditableFactory(new NoCopySpanEditableFactory(new DirtySpanWatcher()));
    }

    /**
     * 添加 @内容
     *
     * @param text 包含 @ 符号的字符
     */
    public void addSpan(String text) {
        getText().insert(getSelectionEnd(), text);
        DataSpan myTextSpan = new DataSpan(R.color.plan_btn_end);
        getText().setSpan(myTextSpan, getSelectionEnd() - text.length(), getSelectionEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        setSelection(getText().length());
    }

    /**
     * 找到最后 Span 块
     *
     * @param text
     * @return
     */
    public static boolean KeyDownHelper(Editable text) {
        //找到光标开始结束坐标
        int selectionEnd = Selection.getSelectionEnd(text);
        int selectionStart = Selection.getSelectionStart(text);
        //获取 EditText 中所有的 Span 通过 DataSpan 绑定是的类型
        DataSpan[] spans = text.getSpans(selectionStart, selectionEnd, DataSpan.class);
        for (DataSpan span : spans) {
            if (span != null) {
                //找到第一个非空的 span 和该 span 对应 EditText 中的开始结束位置
                int spanStart = text.getSpanStart(span);
                int spanEnd = text.getSpanEnd(span);
                //假如光标的位置位于该 span 中的最后一位，即位于@ 字符串后面
                if (selectionEnd == spanEnd) {
                    //设置选中该 span 字符串
                    Selection.setSelection(text, spanStart, spanEnd);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 数据载体
     */
    class DataSpan extends ForegroundColorSpan{
        public DataSpan(int color) {
            super(color);
        }
    }


    class NoCopySpanEditableFactory extends Editable.Factory {

        private NoCopySpan spans;

        public NoCopySpanEditableFactory(NoCopySpan spans) {
            this.spans = spans;
        }

        @Override
        public Editable newEditable(CharSequence source) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(source);
            stringBuilder.setSpan(spans, 0, source.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return stringBuilder;
        }
    }

    class DirtySpanWatcher implements SpanWatcher {
//        private int selStart = 0;
//        private int selEnd = 0;

        @Override
        public void onSpanAdded(Spannable text, Object what, int start, int end) {

        }

        @Override
        public void onSpanRemoved(Spannable text, Object what, int start, int end) {

        }

        @Override
        public void onSpanChanged(Spannable text, Object what, int ostart, int oend, int nstart, int nend) {
            int spanEnd = text.getSpanEnd(what);
            int spanStart = text.getSpanStart(what);
            if (spanStart >= 0 && spanEnd >= 0 && what instanceof DataSpan) {
                CharSequence charSequence = text.subSequence(spanStart, spanEnd);
                if (!charSequence.toString().contains("@")) {
                    DataSpan[] spans = text.getSpans(spanStart, spanEnd, DataSpan.class);
                    for (DataSpan span : spans) {
                        if (span != null) {
                            text.removeSpan(span);
                            break;
                        }
                    }
                }
            }
//            if (what == Selection.SELECTION_END && selEnd != nstart) {
//                selEnd = nstart;
//                DataSpan[] spans = text.getSpans(nstart, nend, DataSpan.class);
//                for (DataSpan span : spans) {
//                    if (span != null) {
//                        int index;
//                        if (Math.abs(selEnd - spanEnd) > Math.abs(selEnd - spanStart)) {
//                            index = spanStart;
//                        } else {
//                            index = spanEnd;
//                        }
//                        Selection.setSelection(text, Selection.getSelectionStart(text), index);
//                    }
//                }
//            }
//
//            if (what == Selection.SELECTION_START && selStart != nstart) {
//                selStart = nstart;
//                DataSpan[] spans = text.getSpans(nstart, nend, DataSpan.class);
//                for (DataSpan span : spans) {
//                    if (span != null) {
//                        int index2;
//                        if (Math.abs(selStart - spanEnd) > Math.abs(selStart - spanStart)) {
//                            index2 = spanStart;
//                        } else {
//                            index2 = spanEnd;
//                        }
//                        Selection.setSelection(text, index2, Selection.getSelectionEnd(text));
//                    }
//                }
//            }
        }
    }
}
