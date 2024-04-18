package com.ai.boy.programming.util;

/**
 * @author zhaoning
 * @date 2024/04/06
 * @desc
 */
public class StringUtils {

    /**
     * 删除Markdown代码块标记
     *
     * @param text
     * @return
     */
    public static String removeCodeBlockMarker(String text) {
        if (text == null) {
            return null;
        }
        return text.replace("```java", "")
                .replace("```", "");
    }

    /**
     * 删除文本的开头换行符、结尾换行符
     *
     * @param text 需要处理的文本
     * @return 处理后的文本
     */
    public static String removeLeadingAndTrailingLineBreaks(String text) {
        if (text == null) {
            return null;
        }

        int beginIndex = 0;
        int endIndex = text.length() - 1;

        while (beginIndex <= endIndex && text.charAt(beginIndex) == '\n') {
            beginIndex++;
        }

        while (beginIndex <= endIndex && text.charAt(endIndex) == '\n') {
            endIndex--;
        }

        return text.substring(beginIndex, endIndex + 1);
    }
}
