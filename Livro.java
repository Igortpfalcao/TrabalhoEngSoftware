import java.util.List;

public class Livro {
	private int codigo;
	private String titulo;
    private String editora;
    private String autores;
    private int edicao;
    private int ano;
	private List<Exemplar> exemplares;
    private List<Reserva> reservas;

	
	public Livro(int codigo, String titulo, String editora, String autores, int edicao, int ano) {
		this.codigo = codigo;
		this.titulo = titulo;
        this.editora = editora;
		this.autores = autores;
        this.edicao = edicao;
		this.ano = ano;
	}
	
	public int getCodigo() {
		return codigo;
	}
	
	public String getTitulo() {
		return titulo;
	}

    public String getEditora() {
		return editora;
	}
	
	public String getAutores() {
		return autores;
	}

	public int getEdicao() {
		return edicao;
	}
	
	public int getAno() {
		return ano;
	}

    public void adicionarExemplar(Exemplar exemplar) {
        exemplares.add(exemplar);
    }

    public void adicionarReserva(Reserva reserva) {
        reservas.add(reserva);
    }

    public List<Exemplar> getExemplares() {
        return exemplares;
    }

    public List<Reserva> getReservas() {
        return reservas;
    }
}