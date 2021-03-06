package codility.examples;

public class QuitCoupleLetter {

	public void quitCouple(String s) {
		for (int i = 0; i < s.length(); i++) {
			char f = s.charAt(i);
			String r = s.substring(i + 1, s.length());
			if (r.indexOf(f) >= 0) {
				char[] charArray = r.toCharArray();
				charArray[r.indexOf(f)] = ' ';
				s = String.valueOf(charArray);
				quitCouple(s.trim());
				break;
			} else {
				System.err.print(String.valueOf(f));
			}
		}
	}

	public static void main(String[] args) {
		String s = "ACCAABBC";
//		String s = "AAADDDGGFFFTTBCCCCBACCAABBC";
//		String s = "CBACCAABBC";
//		String s = "ACCAABBC";
//	    String s = "ABBC";
		QuitCoupleLetter coupleLetter = new QuitCoupleLetter();
		coupleLetter.quitCouple(s);
	}
}
