package utils;

public class RegexString {

	public static final String Regex_theme = "#(\\w|[\\x{4e00}-\\x{9fa5}]|[-]|[_])*#";
	public static final String Regex_at = "@(\\w|[\\x{4e00}-\\x{9fa5}]|[-]|[_])*(\\s|[:]|$)";
	public static final String Regex_email = "\\w+\\.*\\w+@\\w+\\.\\w+";
	public static final String Regex_url = "(http|https):/+[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
	public static final String Regex_punt = "[[[\\p{P}+]|[$\\^=<>~+`～｀＄＾ˇ˙＋＝｜＜＞￥×٩●۶๑ ڡ ★☆❤❥✰♥❃❂❁❀✿☀☁☂☃♂♀]]&&[^@\\[\\]]]";  
	public static final String Regex_char = "[\\w|[\\x{4e00}-\\x{9fa5}]]";//匹配英文字母，大小写，_ 和数字、汉字
	public static final String Regex_word = "[[a-zA-Z]|[\\x{4e00}-\\x{9fa5}]]";//匹配英文字母，汉字
	public static final String Regex_chinese = "[\\x{4e00}-\\x{9fa5}]";//匹配汉字
	public static final String Regex_english = "[a-zA-Z]+";//匹配英文
	public static final String Regex_number = "[0-9]+";//匹配数字
	public static final String Regex_number_punt = "[0-9]+|\\p{P}+";//匹配数字
	
}
