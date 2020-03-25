package com.crcc.commonlib.utils;

import android.text.InputFilter;
import android.text.Spanned;

import com.crcc.commonlib.CommonApplication;


public class EmojiFilter extends InputFilter.LengthFilter implements InputFilter {

    int mMax;

    public EmojiFilter() {
        super(50);
        mMax = 50;
    }

    public EmojiFilter(int max) {
        super(max);
        mMax = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                               int dend) {
        if (containsEmoji(source.toString())) {
            UsefulToast.showToast(CommonApplication.getApplication(), "", "不支持输入表情符号", 0);
            return "";
        }
        int keep = mMax - (dest.length() - (dend - dstart));
        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return source; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, keep);
        }
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

}