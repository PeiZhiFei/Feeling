package com.feifei.util;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class StringUtil {

	/**
	* @param inputString
	* @return
	* @notice 中文字符串转拼音，如"你好"："NIHAO"，其他字符不变
	*/
	public static String getPinYin(String chinese) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		char[] input = chinese.trim().toCharArray();
		String output = "";
		try {
			for (char element : input) {
				if (java.lang.Character.toString(element).matches(
						"[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(
							element, format);
					output += temp[0];
				} else {
					output += java.lang.Character.toString(element);
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return output;
	}

	/**
	* @param chines
	* @return
	* @notice 获取汉语拼音的首字母，如"你好"："NH"，其他字符不变
	*/
	public static String getFirstSpell(String chinese) {
		String pinyinName = "";
		char[] nameChar = chinese.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (char element : nameChar) {
			if (element > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							element, defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += element;
			}
		}
		return pinyinName;
	}

	/**
	* @param list
	* @return
	* @notice 将一个list<String>转换成拼音
	*/
	public static List<String> getPinyinList(List<String> list) {
		List<String> pinyinList = new ArrayList<String>();
		for (String str : list) {
			String pinyin = getPinYin(str);
			pinyinList.add(pinyin);
		}
		return pinyinList;
	}

	/**
	 * @param obj
	 * @return
	 * @notice 对象转为String
	 */
	public static String string(Object obj) {
		String str = "";
		if (obj != null) {
			str = String.valueOf(obj);
		}
		return str;
	}

	/**
	 * @param text
	 * @param defaultText
	 * @return
	 * @notice 获取字符串中需要的内容，此处为括号
	 */
	public static String getCountryCodeBracketsInfo(String text,
			String defaultText) {
		if (text.contains("(") && text.contains(")")) {
			int leftBrackets = text.indexOf("(");
			int rightBrackets = text.lastIndexOf(")");
			if (leftBrackets < rightBrackets) {
				return "+" + text.substring(leftBrackets + 1, rightBrackets);
			}
		}
		if (defaultText != null) {
			return defaultText;
		} else {
			return text;
		}
	}

}
