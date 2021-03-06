package prueba.experis;

/**
 * Se realiza validacion de cuantas veces puede subir la escalera, teniendo como
 * premisa que como minimo debe dar 1 paso y como maximo 2, teniendo tan solo 10
 * escalones, por lo tanto el resultado para subir los 10 escalones, existirian
 * 89 formas posibles de subir por la escalera
 * 
 * @author andres.jurado
 *
 */
public class ClimbingStair {


	static int fib(int n) {
		if (n <= 1)
			return n;
		
		return fib(n - 1) + fib(n - 2);
	}


	static int countWays(int s) {
		return fib(s + 1);
	}


	public static void main(String args[]) {
		int s = 10;
		System.out.println("Number of ways = " + countWays(s));
	}
}
