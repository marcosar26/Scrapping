package marcos;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public final class Scrapear extends Thread {
    private final File dataDir = new File(".", "data");
    private final Dominio dominio;
    private final File dominioDir;
    private String html;
    private final Set<String> palabras = new HashSet<>();
    private final Set<String> enlaces = new HashSet<>();
    private final Map<String, Integer> palabrasOcurrencias = new HashMap<>();

    public Scrapear(Dominio dominio, Collection<String> palabras) {
        this.dominio = dominio;
        this.dominioDir = new File(dataDir, dominio.getNombre());
        this.palabras.addAll(palabras);
        this.html = null;

        System.out.println(List.of(palabras).size());
    }

    @Override
    public void run() {
        descargarCodigo();
        consultarOcurrencia();
        consultarEnlaces();
        generarResumen();
        this.dominio.setEstado(Estado.ANALIZADO);
    }

    private void descargarCodigo() {
        if (!dataDir.exists()) {
            dataDir.mkdir();
        }

        if (!dominioDir.exists()) {
            dominioDir.mkdir();
        }

        try {
            html = Jsoup.connect(dominio.getUrl()).get().html();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void consultarOcurrencia() {
        for (String palabra : palabras) {
            if (palabra.isEmpty() || palabra.isBlank()) continue;

            final List<String> cmd = List.of("java -jar ContarPalabras.jar", html, palabra);

            ProcessBuilder pb = new ProcessBuilder();
            pb.directory(new File("."));
            pb.command(cmd);

            try {
                Process p = pb.start();

                int ocurrencias = 0;

                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        ocurrencias = Integer.parseInt(str);
                    }
                }

                if (palabrasOcurrencias.containsKey(palabra)) {
                    palabrasOcurrencias.put(palabra, ocurrencias);
                } else {
                    palabrasOcurrencias.putIfAbsent(palabra, ocurrencias);
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void consultarEnlaces() {
        Elements elements = Jsoup.parse(html).select("a[href]");
        elements.forEach(element -> enlaces.add(element.attr("abs:href")));
    }

    private void generarResumen() {
        final File resumen = new File(dominioDir, "resumen.txt");
        final StringBuilder sb = new StringBuilder();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String fecha = dateTimeFormatter.format(LocalDateTime.now());

        sb.append("Fecha: ").append(fecha);
        sb.append(System.lineSeparator());
        sb.append("Nombre: ").append(dominio.getNombre()).append(" | URL: ").append(dominio.getUrl());
        sb.append(System.lineSeparator());
        sb.append("Palabras buscadas: ").append(palabras);
        sb.append(System.lineSeparator());
        for (String palabra : palabrasOcurrencias.keySet()) {
            sb.append("'").append(palabra).append("'").append(": ").append(palabrasOcurrencias.get(palabra).intValue());
            sb.append(System.lineSeparator());
        }
        sb.append("Enlaces: ");
        sb.append(System.lineSeparator());
        int contador = 1;
        for (String enlace : enlaces) {
            if (enlace.isBlank() || enlace.isEmpty()) continue;
            sb.append(contador).append(". ").append(enlace);
            sb.append(System.lineSeparator());
            contador++;
        }

        try {
            FileUtils.writeStringToFile(resumen, sb.toString(), StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}