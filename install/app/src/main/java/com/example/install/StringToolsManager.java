package com.example.install;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;

/**
 * Created by Administrator on 2016/6/12.
 */
public class StringToolsManager {

//    testTest.setMovementMethod(LinkMovementMethod.getInstance());不添加这一句，拨号，http，发短信的超链接不能执行.

    public static StringToolsManager m_STM = null;
    private static SpannableString ss;

    public static StringToolsManager GetInstance(String string){
        if (m_STM == null){
            m_STM = new StringToolsManager();
        }
        ss = new SpannableString(string);
        return m_STM;
    }

    /**
     * 设置begin 到 end 字符的颜色
     * @param begin 需要变色的起始位置
     * @param end 需要变色的终了位置
     * @param color 需要变色的字符颜色
     * @return
     */
    public SpannableString setFontColor(int begin,int end,int color){
        ss.setSpan(new ForegroundColorSpan(color),begin,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置begin 到 end 的字符点击触发拨号
     * @param begin
     * @param end
     * @param tel  电话号码 （字符型）
     * @return
     */
    public SpannableString setFontCallTel(int begin,int end,String tel){
        ss.setSpan(new URLSpan("tel:"+tel), begin, end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置begin 到 end 的字符点击触发打开网页
     * @param begin
     * @param end
     * @param url 打开网址
     * @return
     */
    public SpannableString setFontCallUrl(int begin,int end,String url){
        ss.setSpan(new URLSpan(url),begin,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置begin 到 end 的字符点击触发打开短信界面
     * @param begin
     * @param end
     * @param phone 发送短信号码
     * @return
     */
    public SpannableString setFontCallSms(int begin,int end,String phone){
        ss.setSpan(new URLSpan("sms:"+phone),begin,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置begin 到 end 的字符的字体
     * @param begin
     * @param end
     * @param typeface 字体样式
     *                 Typeface.BOLD_ITALIC 粗体
     *                 android.graphics.Typeface.ITALIC 斜体
     * @return
     */
    public SpannableString setFontType(int begin,int end,int typeface){
        ss.setSpan(new StyleSpan(typeface),begin,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置begin 到 end 的字符的下划线
     * @param begin
     * @param end
     * @return
     */
    public SpannableString setFontUnderline(int begin,int end){
        ss.setSpan(new UnderlineSpan(),begin,end,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    /**
     * 设置begin 到 end 的字符之间插入图片
     * @param begin
     * @param end
     * @param d
     * @return
     */
    public SpannableString setFontAddImage(int begin,int end,Drawable d){
        ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), begin, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return ss;
    }
}
