public class Professor implements IUsuario {
    private double codigoUsuario;
    private String nome;

    public Professor(double codigoUsuario, String nome) {
        this.codigoUsuario = codigoUsuario;
        this.nome = nome;
    }

    public double getCodigoUsuario() {
        return codigoUsuario;
    }

    public String getNome() {
        return nome;
    }
}
