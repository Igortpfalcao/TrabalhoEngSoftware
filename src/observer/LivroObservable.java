package observer;

import java.util.ArrayList;
import java.util.List;

import modelos.Livro;

public class LivroObservable {
    private Livro livro;
    private List<Observer> observers = new ArrayList<>();
    private static final int LIMITE_RESERVAS_NOTIFICACAO = 2;

    public LivroObservable(Livro livro) {
        this.livro = livro;
    }

    public void registrarObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removerObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notificarObservers() {
        if (deveNotificar()) {
            for (Observer observer : observers) {
                observer.atualizar(livro);
            }
        }
    }

    private boolean deveNotificar() {
        // Implementação fictícia - você precisará injetar o repositório
        int totalReservas = 0; // repositorio.contarReservasAtivas(livro.getCodigo());
        return totalReservas > LIMITE_RESERVAS_NOTIFICACAO;
    }
}
