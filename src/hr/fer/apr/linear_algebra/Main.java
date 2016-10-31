package hr.fer.apr.linear_algebra;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Glavna klasa koja sluzi za pokretanje prve laboratorijske vjezbe
 */

public class Main {

	/**
	 * Glavna metoda pokrece zadatak, vrti beskonacnu while petlju dok korisnik ne upise exit
	 * while petlja simulira konzolu u kojoj korisnik upisuje naredbe koje zeli
	 * @param args
	 * @throws FileNotFoundException
	 */

	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		ConsoleParser console = new ConsoleParser();

		System.out.println("Welcome to My_APR_Lab");

		while (true) {

			console.parser(sc.nextLine());

		}
		
	}
	
}
