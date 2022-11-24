package marcos;

public class Dominio {
    private final String nombre;
    private final String url;
    private Estado estado;

    public Dominio(String nombre, String url) {
        this.nombre = nombre;
        this.url = url;
        this.estado = Estado.SIN_ANALIZAR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dominio dominio = (Dominio) o;

        if (!nombre.equals(dominio.nombre)) return false;
        return url.equals(dominio.url);
    }

    @Override
    public int hashCode() {
        int result = nombre.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return this.nombre + ": " + this.url + " | Estado: " + this.estado;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrl() {
        return url;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
