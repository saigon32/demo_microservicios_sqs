package codility.examples;

public class SplitString {

	public static void main(String[] args) {
		String s = "aaabbbcccdddeeeff";
		for (int i = 0; i < s.length(); i++) {
			if (i < 15) {
				String result = s.substring(i, i + 3);
				System.err.println(result);
				i = i + result.length() - 1;
			} else {
				String result = s.substring(i, i + 2);
				System.err.println(result);
				break;
			}
		}
	}

}
