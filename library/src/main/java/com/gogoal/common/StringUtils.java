package com.gogoal.common;

import android.text.Html;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    // 判断一个字符是否是中文
    private static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean isChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (isChinese(c)) {
                return true;// 有一个中文字符就返回}
            }
        }
        return false;
    }

    /**
     * 字符串去掉空格回车换行
     */
    private static final Pattern SPACE_PATTERN = Pattern.compile("\\s*|\t|\r|\n");

    public static String replaceBlank(String str) {
        String dest = "";
        if (!TextUtils.isEmpty(str)) {
            Matcher m = SPACE_PATTERN.matcher(str);
            dest = m.replaceAll("").trim();
        }
        return dest;
    }

    /**
     * 字符串全角化
     */
    public static String toDBC(String input) {
        if (input != null && input.length() > 0) {
            char[] c = input.toCharArray();
            for (int i = 0; i < c.length; i++) {
                if (c[i] == 12288) {
                    c[i] = (char) 32;
                    continue;
                }
                if (c[i] > 65280 && c[i] < 65375) {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            return new String(c);
        }
        return "";
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(String s) {
        if (TextUtils.isEmpty(s)) {
            return s;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 简单的异或加密
     */
    public static String encryption(String str) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) ^ 'W');
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    /**
     * 对上面加密的解密
     */
    public static String decode(String str) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = (char) (str.charAt(i) ^ 'W');
            stringBuffer.append(c);
        }
        return stringBuffer.toString();
    }

    /**
     * URL编码
     */
    public static String decodeUrl(String urlString) {
        try {
            return URLDecoder.decode(urlString, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return urlString;
        }
    }

    /**
     * 百分比处理
     */
    public static String percent(double value) {
        return percent(value, 2);
    }

    /**
     * 百分比处理
     */
    public static String percent(String valueString) {
        return percent(valueString, 2);
    }

    /**
     * 百分比处理
     *
     * @param value 处理的值
     * @param unit  保留的小数位
     */
    public static String percent(double value, int unit) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(unit);
        return nf.format(value);
    }

    /**
     * 百分比处理
     *
     * @param value 处理的值
     * @param unit  保留的小数位
     */
    public static String percent(String value, int unit) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(unit);
        return nf.format(StringUtils.parseStringDouble(value));
    }

    //字符串反转
    public static String reverseString(String orderStr) {
        return new StringBuilder(orderStr).reverse().toString();
    }

    //绝对空
    public static boolean isActuallyEmpty(CharSequence words) {
        return TextUtils.isEmpty(words) || "null".equals(words) ||
                TextUtils.isEmpty(words.toString().replace(" ", ""));
    }

    /**
     * 输入的手机号是否合法
     */
    public static boolean checkPhoneString(CharSequence phoneNumber) {
        return !isActuallyEmpty(phoneNumber) && phoneNumber.toString().matches("^1[3-57-9]\\d{9}$");
    }

    /**
     * 输入的邮箱格式是否合法
     */
    public static boolean checkEmail(String email) {
        if (!email.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
            return false;
        }
        return true;
    }
//==================================================================================================

    /**
     * 输入的身份证号码是否合法
     */
    public static boolean checkIdentity(String identity) {
        /*
         * 1.基本格式
		 * 2.六位数年月日有效，有效月份，日期
		 * 3.校验位
		 * 4.地区码
		 * */
        return checkIdFormat(identity) && checkIdValidityCode(identity);
    }

    /**
     * 用正则匹配身份证基本格式以及判断年月
     */
    private static boolean checkIdFormat(String idNum) {
        if (!idNum.matches("[1-9][0-9]{16}[0-9,x,X]")) {
            return false;
        }
        //小技巧，判断年月合法最好的办法应该就是这个了，把写的日期转成时间毫秒数，
        //再把这个时间毫秒数转成标准的时间，如果这两个时间的字符串相同，这个时间肯定是合法的
        String year = idNum.substring(6, 10);
        String month = idNum.substring(10, 12);
        String day = idNum.substring(12, 14);
        long time;
        try {
            time = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).parse(year + month + day).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        String newDate = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(time);
        return newDate.equals(year + month + day);
    }

    /**
     * 用代码计算身份证校验位和最后一位是否相等
     */
    private static boolean checkIdValidityCode(String idNum) {

        // Wi系数列表
        final int[] ratioArr = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

        // 校验码列表
        final char[] checkCodeList = {'1', '0', 'X', '9', '8', '7', '6', '5', '4',
                '3', '2'};

        // 获取身份证号字符数组
        char[] idArrs = idNum.toCharArray();

        // 获取最后一位（身份证校验码）
        char lastCode = idArrs[idArrs.length - 1];
        int[] intIds = new int[idArrs.length - 1];
        int idSum = 0;// 身份证号第1-17位与系数之积的和

        for (int i = 0; i < idArrs.length - 1; i++) {
            intIds[i] = idArrs[i] - '0';
            idSum += intIds[i] * ratioArr[i];
        }

        return Character.toUpperCase(lastCode) == checkCodeList[idSum % 11];
    }

    public static CharSequence getNotNullString(String orderStr) {
        return isActuallyEmpty(orderStr) ? "" : Html.fromHtml(orderStr);
    }

    //自定义占位符
    public static CharSequence getNotNullString(String orderStr, String placeholderChar) {
        if (TextUtils.isEmpty(placeholderChar)) {
            return getNotNullString(orderStr);
        }
        return isActuallyEmpty(orderStr) ? placeholderChar : Html.fromHtml(orderStr);
    }

    /**
     * 取整
     */
    public static String getIntegerData(String floatData) {
        return String.valueOf(parseStringDouble(floatData).intValue());
    }

//==================================================================================================

    /**
     * 字符串转double
     *
     * @param stringValue 转换的double字符串
     */
    public static Double parseStringDouble(String stringValue) {
        if (StringUtils.isActuallyEmpty(stringValue)) {
            return 0.0d;
        }
        try {
            return Double.parseDouble(stringValue);
        } catch (Exception e) {
            return 0.0d;
        }
    }

    /**
     * 字符串转double
     *
     * @param value 转换的double字符串
     * @param unit  保存小数点后多少位
     */
    public static String parseStringDouble(String value, int unit) {
        return saveSignificand(parseStringDouble(value), unit);
    }

    /**
     * 保留有效数字
     *
     * @param doubleData  保留的doubel值
     * @param significand 保留的位数
     */
    public static String saveSignificand(double doubleData, int significand) {
        return String.format("%." + significand + "f", doubleData);
    }

    public static String saveSignificand(String strDoubleData, int significand) {
        return parseStringDouble(strDoubleData, significand);
    }

    public static String save2Significand(double value) {
        return saveSignificand(value, 2);
    }

    public static String save2Significand(String doubleData) {
        return saveSignificand(doubleData, 2);
    }

    //格式化1-9这几个数字的为01,02等格式
    public static String formatInt(int i) {
        return i > 9 ? (i + "") : ("0" + i);
    }

    /**
     * 除掉HTML里面所有标签
     */
    public static String removeTag(String htmlStr) {

        if (htmlStr == null) {
            return "--";
        }

        String regexScript = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // script
        String regexStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // style
        String regexHtml = "<[^>]+>"; // HTML tag
        String regexSpace = "\\s+|\t|\r|\n|&nbsp;|&rdquo;|&ldquo;";// other characters

        Pattern pScript = Pattern.compile(regexScript,
                Pattern.CASE_INSENSITIVE);
        Matcher mScript = pScript.matcher(htmlStr);
        htmlStr = mScript.replaceAll("");
        Pattern pStyle = Pattern
                .compile(regexStyle, Pattern.CASE_INSENSITIVE);
        Matcher mStyle = pStyle.matcher(htmlStr);
        htmlStr = mStyle.replaceAll("");
        Pattern pHtml = Pattern.compile(regexHtml, Pattern.CASE_INSENSITIVE);
        Matcher mHtml = pHtml.matcher(htmlStr);
        htmlStr = mHtml.replaceAll("");
        Pattern pSpace = Pattern
                .compile(regexSpace, Pattern.CASE_INSENSITIVE);
        Matcher mSpace = pSpace.matcher(htmlStr);
        htmlStr = mSpace.replaceAll("");

        return htmlStr;
    }

    public static String map2Parameter(Map<String, String> map) {
        if (map==null){
            return "";
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String,String> entry:map.entrySet()){
            params.append("&")
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }
        return params.toString();
    }

    public static String formatPhoneNum(String num) {
        if (isActuallyEmpty(num)) {
            return "";
        } else if (num.length() <= 7) {
            return num.substring(0, 3) + "-";
        } else {
            return num.substring(0, 3) + "-" + num.substring(3, 7) + "-" + num.substring(7);
        }
    }

    /**
     * 校验验证码
     */
    public static boolean checkVerificationCode(String verCode) {
        return (!TextUtils.isEmpty(verCode)) && verCode.length() >= 6;
    }

    /**
     * 生产随机数验证码：有良知的程序员去掉验证码中的0,o,i,I,1,l,L
     */
    public static String getRandomString(int length) {
        String str = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNOPQRSTUVWXYZ123456789";
        return RandomStringUtils.randomFromChars(length, str.toCharArray());
    }

    /**
     * 是否是url
     */
    private static final Pattern URL_PATTERN = Pattern.compile(
            "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");

    public static boolean isUrl(String url) {
        return !TextUtils.isEmpty(url) && URL_PATTERN.matcher(url).matches();
    }
}
