package com.gogoal.common;

import java.util.Random;

/**
 * @author wangjd on 2017/12/4 0004.
 * @description :随机字符串
 */
public class RandomStringUtils {

    private static final Random RANDOM = new Random();


    /**
     * @param count   随机数长度 ;
     * @param start   开始 ;
     * @param end     结束值 ;
     * @param letters 是否包含字母 ;
     * @param numbers 是否包含数字;
     * @param chars   指定的char[]对象中生成
     * @param random  Random
     */
    private static String random(int count, int start, int end, final boolean letters, final boolean numbers,
                                 final char[] chars, final Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The chars array must not be empty");
        }

        if (start == 0 && end == 0) {
            if (chars != null) {
                end = chars.length;
            } else {
                if (!letters && !numbers) {
                    end = Character.MAX_CODE_POINT;
                } else {
                    end = 'z' + 1;
                    start = ' ';
                }
            }
        } else {
            if (end <= start) {
                throw new IllegalArgumentException("Parameter end (" + end + ") must be greater than start (" + start + ")");
            }
        }

        final int zeroDigitAscii = 48;
        final int firstLetterAscii = 65;

        if (chars == null && (numbers && end <= zeroDigitAscii
                || letters && end <= firstLetterAscii)) {
            throw new IllegalArgumentException("Parameter end (" + end + ") must be greater then (" + zeroDigitAscii + ") for generating digits " +
                    "or greater then (" + firstLetterAscii + ") for generating letters.");
        }

        final StringBuilder builder = new StringBuilder(count);
        final int gap = end - start;

        while (count-- != 0) {
            int codePoint;
            if (chars == null) {
                codePoint = random.nextInt(gap) + start;

                switch (Character.getType(codePoint)) {
                    case Character.UNASSIGNED:
                    case Character.PRIVATE_USE:
                    case Character.SURROGATE:
                        count++;
                        continue;
                    default:
                        break;
                }

            } else {
                codePoint = chars[random.nextInt(gap) + start];
            }

            final int numberOfChars = Character.charCount(codePoint);
            if (count == 0 && numberOfChars > 1) {
                count++;
                continue;
            }

            if (letters && Character.isLetter(codePoint)
                    || numbers && Character.isDigit(codePoint)
                    || !letters && !numbers) {
                builder.appendCodePoint(codePoint);

                if (numberOfChars == 2) {
                    count--;
                }

            } else {
                count++;
            }
        }
        return builder.toString();
    }

    private static String random(final int count, final int start, final int end, final boolean letters, final boolean numbers) {
        return random(count, start, end, letters, numbers, null, RANDOM);
    }

    //
    private static String random(final int count, final boolean letters, final boolean numbers) {
        return random(count, 0, 0, letters, numbers);
    }

    /**
     * 随机数串
     */
    public static String randomNum(final int count) {
        return random(count, false, true);
    }

    /**
     * 随机字符串
     */
    public static String randomString(final int count) {
        return random(count, true, false);
    }

    /**
     * 随机数串
     *
     * @param count 长度
     * @param chars 指定的目标串
     */
    public static String randomFromChars(final int count, char[] chars) {
        if (chars == null) {
            return random(count, 0, 0, false, false, null, RANDOM);
        }
        return random(count, 0, chars.length, false, false, chars, RANDOM);
    }

}
