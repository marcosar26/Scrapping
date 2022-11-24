package marcos;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.Objects;
import java.util.Scanner;

public class InputManager {


    public static int leerNumero(String message, int condicion) {
        Scanner sc = new Scanner(System.in);
        String str;
        int num = 0;
        boolean isValid = false;

        do {
            System.out.println(message);
            str = sc.next();

            try {
                num = Integer.parseInt(str);

                if (num >= condicion) {
                    isValid = true;
                } else {
                    System.out.println("El numero tiene que ser mayor o igual a " + condicion);
                }


            } catch (NumberFormatException ex) {
                System.out.println("No has introducido un numero entero. Intentalo de nuevo");
            }
        } while (!isValid);

        return num;
    }

    public static int leerNumeroEnRango(String message, int min, int max) {
        Scanner sc = new Scanner(System.in);
        String str;
        int num = 0;
        boolean isValid = false;

        do {
            System.out.print(message);
            str = sc.next();

            try {
                num = Integer.parseInt(str);

                if (num >= min && num <= max) {
                    isValid = true;
                } else {
                    System.out.println("El numero tiene que estar entre " + min + " y " + max);
                }


            } catch (NumberFormatException ex) {
                System.out.println("No has introducido un numero entero. Intentalo de nuevo");
            }
        } while (!isValid);

        return num;
    }


    public static double leerNumeroDecimal(String message) {
        Scanner sc = new Scanner(System.in);
        String str;
        double num = 0;
        boolean isValid = false;

        do {
            System.out.println(message);
            str = sc.next();

            try {
                num = Double.parseDouble(str);
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println("No has introducido un numero. Intentalo de nuevo");
            }
        } while (!isValid);

        return num;
    }

    public static String leerCadena(String message) {
        Scanner sc = new Scanner(System.in);
        String str = "";
        boolean isValid = false;

        do {
            System.out.print(message);
            str = sc.nextLine();

            if (!Objects.equals(str, "")) {
                isValid = true;
            } else {
                System.out.println("No has introducido ningun dato. Intentalo de nuevo");
            }
        } while (!isValid);

        return str;
    }

    public static String leerUrl(String message) {
        Scanner sc = new Scanner(System.in);
        String url = "";
        boolean isValid = false;

        UrlValidator myUrlValidator = new UrlValidator();

        do {
            System.out.print(message);
            url = sc.nextLine();

            if (!Objects.equals(url, "") && myUrlValidator.isValid(url)) {
                isValid = true;
            } else {
                System.out.println("No has introducido una URL válida. Intentalo de nuevo");
            }
        } while (!isValid);

        return url;
    }
}