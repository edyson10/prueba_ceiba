package co.com.ceiba.mobile.pruebadeingreso.Database.DTO;

public class Post {

    private String titulo;
    private String body;

    public Post() { }

    public Post(String titulo, String body) {
        this.titulo = titulo;
        this.body = body;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
