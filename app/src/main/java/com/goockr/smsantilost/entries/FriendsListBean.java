package com.goockr.smsantilost.entries;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * Created by yuyouming on 2017/11/27.
 */

public class FriendsListBean {
    private String icon;
    private String name;
    private String num;
    private String firstChar;

    public FriendsListBean(String icon, String name, String num) {
        this.icon = icon;
        this.name = name;
        this.num = num;
        this.firstChar = getFirstChar(name);
    }

    public String getFirstChar() {
        return firstChar;
    }

    public void setFirstChar(String firstChar) {
        this.firstChar = firstChar;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getNameFirstChar() {
        return getFirstChar(name);
    }

    // 获得字符串的首字母 首字符 转汉语拼音
    public String getFirstChar(String value) {
        // 首字符
        char firstChar = value.charAt(0);
        // 首字母分类
        String first = null;
        // 是否是非汉字
        String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

        if (print == null) {

            // 将小写字母改成大写
            if ((firstChar >= 97 && firstChar <= 122)) {
                firstChar -= 32;
            }
            if (firstChar >= 65 && firstChar <= 90) {
                first = String.valueOf((char) firstChar);
            } else {
                // 认为首字符为数字或者特殊字符
                first = "#";
            }
        } else {
            // 如果是中文 分类大写字母
            // 这里对多音字“长”做一些处理
            if (("" + firstChar).equals("长")) {
                first = String.valueOf((char) (print[1].charAt(0) - 32));
            }else {
                first = String.valueOf((char) (print[0].charAt(0) - 32));
            }

        }
        if (first == null) {
            first = "?";
        }
        return first;
    }
}
