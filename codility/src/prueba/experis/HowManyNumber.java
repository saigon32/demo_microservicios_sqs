package prueba.experis;

public class HowManyNumber {

	public int findAndCountNumber(int[] intArray, int num) {
		int countNum = 0;
		for (int i = 0; i < intArray.length; i++) {
			if (intArray[i] == num) {
				countNum++;
			}
		}
		return countNum;
	}

	public static void main(String[] args) {
		int[] intArray = new int[] { 1, 2, 3, 3, 4, 5 };
		HowManyNumber howManyNumber = new HowManyNumber();
		System.err.println(howManyNumber.findAndCountNumber(intArray, 3));
	}

}
