package main.java.com.biblioteca.repositorio;

import java.util.Objects;

class ParUsuarioLivro {
    private final int codigoUsuario;
    private final int codigoLivro;

    public ParUsuarioLivro(int codigoUsuario, int codigoLivro) {
        this.codigoUsuario = codigoUsuario;
        this.codigoLivro = codigoLivro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParUsuarioLivro that = (ParUsuarioLivro) o;
        return codigoUsuario == that.codigoUsuario && 
               codigoLivro == that.codigoLivro;
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigoUsuario, codigoLivro);
    }
}