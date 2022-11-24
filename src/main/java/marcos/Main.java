package marcos;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Main {
    static Set<Dominio> dominios = new HashSet<>();

    public static void main(String[] args) {
        int respuesta;

        do {
            System.out.println("1. Añadir dominio.");
            System.out.println("2. Listar dominios.");
            System.out.println("3. Eliminar dominio.");
            System.out.println("4. Analizar dominio.");
            System.out.println("5. Analizar todos.");
            System.out.println("6. Salir.");

            respuesta = InputManager.leerNumeroEnRango("Selecciona opción: ", 1, 6);

            switch (respuesta) {
                default -> System.out.println("Opción inválida.");
                case 1 -> addDominio();
                case 2 -> listarDominios();
                case 3 -> eliminarDominio();
                case 4 -> analizarDominio(null);
                case 5 -> analizarDominios();
                case 6 -> System.out.println("Saliendo...");
            }

        } while (respuesta != 6);
    }

    private static void addDominio() {
        String nombre, url;

        do {
            nombre = InputManager.leerCadena("Introduce el nombre del dominio: ");

        } while (nombre.isEmpty() || nombre.isBlank());

        do {
            url = InputManager.leerUrl("Introduce la URL del dominio: ");

            try {
                if (!UtilsManager.doesURLExist(new URL(url))) {
                    System.out.println("La URL no es válida, vuelve a intentarlo.");
                    url = "";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } while (url.isBlank() || url.isEmpty());

        Dominio dominio = new Dominio(nombre, url);
        dominios.add(dominio);
    }

    private static void listarDominios() {
        System.out.println("DOMINIOS:");
        for (Dominio dominio : dominios) {
            System.out.println(dominio);
        }
    }

    private static void eliminarDominio() {
        String nombre;
        Dominio dominio = null;
        do {
            nombre = InputManager.leerCadena("Introduce el nombre del dominio: ");

        } while (nombre.isEmpty() || nombre.isBlank());

        for (Dominio d : dominios) {
            if (Objects.equals(nombre, d.getNombre())) {
                dominio = d;
                break;
            }
        }

        if (dominio != null) {
            dominios.remove(dominio);
            System.out.println("Se ha eliminado el dominio solicitado.");
        } else {
            System.out.println("No se ha encontrado ningún dominio con ese nombre.");
        }
    }

    private static void analizarDominio(Dominio dominio) {
        if (dominio == null) {
            String nombre;
            do {
                nombre = InputManager.leerCadena("Introduce el nombre del dominio: ");

            } while (nombre.isEmpty() || nombre.isBlank());

            for (Dominio d : dominios) {
                if (Objects.equals(nombre, d.getNombre())) {
                    dominio = d;
                    break;
                }
            }

            if (dominio == null) System.out.println("No se ha encontrado el dominio.");
            return;
        }

        int opcion;
        do {
            System.out.println("1. Abrir en navegador.");
            System.out.println("2. Ver estado actual.");
            System.out.println("3. Configurar palabras a buscar.");
            System.out.println("4. Scrapear web.");
            System.out.println("5. Volver al menú principal.");

            opcion = InputManager.leerNumeroEnRango("Selecciona opción: ", 1, 5);

            switch (opcion) {
                default -> System.out.println("Opción inválida.");
                case 1 -> {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        try {
                            Desktop.getDesktop().browse(new URI(dominio.getUrl()));
                        } catch (IOException | URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                case 2 -> System.out.println("El estado del dominio es: " + dominio.getEstado());
                case 3 -> {
                }
                case 4 -> {
                    dominio.setEstado(Estado.ANALIZANDO);
                    final Dominio finalDominio = dominio;
                    Thread thread = new Thread(() -> new Scrapear(finalDominio));
                    thread.start();
                }
                case 5 -> System.out.println("Volviendo al menú...");
            }

        } while (opcion != 5);
    }

    private static void analizarDominios() {
        for (Dominio dominio : dominios) {
            if (!Objects.equals(Estado.SIN_ANALIZAR, dominio.getEstado())) {
                analizarDominio(dominio);
            }
        }
    }
}