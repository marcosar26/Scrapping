package marcos;

import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class Scrapear {
    private final Dominio dominio;

    public Scrapear(Dominio dominio) {
        this.dominio = dominio;
    }

    private void descargarCodigo() {
        String codigo = getHTML();
        URL url;
        try {
            url = new URL(dominio.getUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        File carpeta = new File("." + "/" + url.getHost());
        if (!carpeta.exists()) carpeta.mkdir();

        File file = new File(carpeta + "/codigo.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            FileUtils.writeStringToFile(file, codigo, StandardCharsets.UTF_8, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHTML() {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(this.dominio.getUrl());
            URLConnection urlConn = url.openConnection();
            if (urlConn != null) urlConn.setReadTimeout(60 * 1000);
            if (urlConn != null && urlConn.getInputStream() != null) {
                InputStreamReader in = new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8);
                BufferedReader bufferedReader = new BufferedReader(in);
                int cp;
                while ((cp = bufferedReader.read()) != -1) {
                    result.append((char) cp);
                }
                in.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
