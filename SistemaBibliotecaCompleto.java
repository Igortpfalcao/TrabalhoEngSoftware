import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SistemaBibliotecaCompleto {
    public static void main(String[] args) {
        Map<Integer, Set<Integer>> reservas = new HashMap<>(); // Usuário -> Livros reservados
        Map<Integer, Integer> emprestimos = new HashMap<>();  // Livro -> Usuário que pegou emprestado
        Set<Integer> livrosCadastrados = new HashSet<>();
        Map<Integer, String> titulosLivros = new HashMap<>(); // Código -> Título
        Map<Integer, List<Emprestimo>> historicoEmprestimos = new HashMap<>(); // Usuário -> Lista de empréstimos
        Map<Integer, List<Reserva>> historicoReservas = new HashMap<>(); // Usuário -> Lista de reservas
        Map<Integer, Set<Integer>> observadoresLivros = new HashMap<>(); // Livro -> Observadores
        Map<Integer, Integer> contadorNotificacoes = new HashMap<>(); // Observador -> Contagem
        
        // Inicialização com alguns dados de exemplo
        livrosCadastrados.addAll(Arrays.asList(100, 200, 300, 400, 500));
        titulosLivros.put(100, "Dom Casmurro");
        titulosLivros.put(200, "O Senhor dos Anéis");
        titulosLivros.put(300, "1984");
        titulosLivros.put(400, "A Revolução dos Bichos");
        titulosLivros.put(500, "Clean Code");
        
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        System.out.println("Sistema de Biblioteca - Comandos:");
        System.out.println("  emp [usuário] [livro] - Empréstimo");
        System.out.println("  dev [usuário] [livro] - Devolução");
        System.out.println("  res [usuário] [livro] - Reserva");
        System.out.println("  obs [usuário] [livro] - Registrar observador");
        System.out.println("  liv [livro] - Consultar livro");
        System.out.println("  usu [usuário] - Histórico do usuário");
        System.out.println("  ntf [usuário] - Notificações recebidas");
        System.out.println("Digite 'sai' para encerrar");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("sai")) {
                break;
            }
            
            try {
                String[] partes = input.split(" ");
                String comando = partes[0].toLowerCase();
                
                switch (comando) {
                    case "emp": // Empréstimo
                        if (partes.length != 3) throw new IllegalArgumentException();
                        int usuarioEmp = Integer.parseInt(partes[1]);
                        int livroEmp = Integer.parseInt(partes[2]);
                        
                        if (!livrosCadastrados.contains(livroEmp)) {
                            System.out.println("Erro: Livro não cadastrado");
                        } else if (emprestimos.containsKey(livroEmp)) {
                            System.out.println("Erro: Livro já emprestado para usuário " + emprestimos.get(livroEmp));
                        } else {
                            // Cancela reserva se existir
                            if (reservas.containsKey(usuarioEmp) && reservas.get(usuarioEmp).contains(livroEmp)) {
                                reservas.get(usuarioEmp).remove(livroEmp);
                                if (reservas.get(usuarioEmp).isEmpty()) {
                                    reservas.remove(usuarioEmp);
                                }
                                
                                // Atualiza status da reserva no histórico
                                if (historicoReservas.containsKey(usuarioEmp)) {
                                    for (Reserva r : historicoReservas.get(usuarioEmp)) {
                                        if (r.codigoLivro == livroEmp && r.dataFim == null) {
                                            r.dataFim = LocalDate.now();
                                            break;
                                        }
                                    }
                                }
                            }
                            
                            emprestimos.put(livroEmp, usuarioEmp);
                            Emprestimo novoEmp = new Emprestimo(livroEmp, LocalDate.now(), null);
                            historicoEmprestimos.computeIfAbsent(usuarioEmp, k -> new ArrayList<>()).add(novoEmp);
                            System.out.println("Sucesso: Livro " + livroEmp + " emprestado para usuário " + usuarioEmp);
                        }
                        break;
                        
                    case "dev": // Devolução
                        if (partes.length != 3) throw new IllegalArgumentException();
                        int usuarioDev = Integer.parseInt(partes[1]);
                        int livroDev = Integer.parseInt(partes[2]);
                        
                        if (!emprestimos.containsKey(livroDev)) {
                            System.out.println("Erro: Livro não está emprestado");
                        } else if (emprestimos.get(livroDev) != usuarioDev) {
                            System.out.println("Erro: Este livro foi emprestado por outro usuário");
                        } else {
                            emprestimos.remove(livroDev);
                            
                            // Atualiza histórico
                            if (historicoEmprestimos.containsKey(usuarioDev)) {
                                for (Emprestimo emp : historicoEmprestimos.get(usuarioDev)) {
                                    if (emp.codigoLivro == livroDev && emp.dataDevolucao == null) {
                                        emp.dataDevolucao = LocalDate.now();
                                        break;
                                    }
                                }
                            }
                            
                            System.out.println("Sucesso: Livro " + livroDev + " devolvido pelo usuário " + usuarioDev);
                        }
                        break;
                        
                    case "res": // Reserva
                        if (partes.length != 3) throw new IllegalArgumentException();
                        int usuarioRes = Integer.parseInt(partes[1]);
                        int livroRes = Integer.parseInt(partes[2]);
                        
                        if (!livrosCadastrados.contains(livroRes)) {
                            System.out.println("Erro: Livro não cadastrado");
                        } else if (emprestimos.containsKey(livroRes)) {
                            System.out.println("Erro: Livro já emprestado para usuário " + emprestimos.get(livroRes));
                        } else {
                            reservas.computeIfAbsent(usuarioRes, k -> new HashSet<>()).add(livroRes);
                            Reserva novaRes = new Reserva(livroRes, LocalDate.now());
                            historicoReservas.computeIfAbsent(usuarioRes, k -> new ArrayList<>()).add(novaRes);
                            System.out.println("Sucesso: Livro " + livroRes + " reservado para usuário " + usuarioRes);
                            
                            // Verifica se precisa notificar observadores
                            int totalReservas = 0;
                            for (Set<Integer> livrosUsuario : reservas.values()) {
                                if (livrosUsuario.contains(livroRes)) {
                                    totalReservas++;
                                }
                            }
                            
                            if (totalReservas > 2 && observadoresLivros.containsKey(livroRes)) {
                                for (Integer observador : observadoresLivros.get(livroRes)) {
                                    contadorNotificacoes.put(observador, contadorNotificacoes.getOrDefault(observador, 0) + 1);
                                    System.out.println("Notificação: Livro " + livroRes + " tem " + totalReservas + 
                                                     " reservas (enviada para usuário " + observador + ")");
                                }
                            }
                        }
                        break;
                        
                    case "obs": // Registrar observador
                        if (partes.length != 3) throw new IllegalArgumentException();
                        int usuarioObs = Integer.parseInt(partes[1]);
                        int livroObs = Integer.parseInt(partes[2]);
                        
                        if (!livrosCadastrados.contains(livroObs)) {
                            System.out.println("Erro: Livro não cadastrado");
                        } else {
                            observadoresLivros.computeIfAbsent(livroObs, k -> new HashSet<>()).add(usuarioObs);
                            System.out.println("Sucesso: Usuário " + usuarioObs + " registrado como observador do livro " + livroObs);
                        }
                        break;
                        
                    case "liv": // Consultar livro
                        if (partes.length != 2) throw new IllegalArgumentException();
                        int livro = Integer.parseInt(partes[1]);
                        
                        if (!livrosCadastrados.contains(livro)) {
                            System.out.println("Erro: Livro não cadastrado");
                        } else {
                            System.out.println("Livro: " + titulosLivros.getOrDefault(livro, "Desconhecido"));
                            System.out.println(emprestimos.containsKey(livro)
                                ? "Status: Emprestado para usuário " + emprestimos.get(livro)
                                : "Status: Disponível para empréstimo");
                            
                            // Mostra quantidade de reservas
                            int totalReservas = 0;
                            for (Set<Integer> livrosUsuario : reservas.values()) {
                                if (livrosUsuario.contains(livro)) {
                                    totalReservas++;
                                }
                            }
                            System.out.println("Reservas ativas: " + totalReservas);
                        }
                        break;
                        
                    case "usu": // Histórico do usuário
                        if (partes.length != 2) throw new IllegalArgumentException();
                        int usuario = Integer.parseInt(partes[1]);
                        
                        System.out.println("\n=== Histórico do usuário " + usuario + " ===");
                        
                        // Empréstimos
                        System.out.println("\nEMPRÉSTIMOS:");
                        if (historicoEmprestimos.containsKey(usuario)) {
                            for (Emprestimo emp : historicoEmprestimos.get(usuario)) {
                                System.out.println("- " + titulosLivros.getOrDefault(emp.codigoLivro, "Desconhecido") + 
                                               " | Empréstimo: " + emp.dataEmprestimo.format(dateFormatter) +
                                               " | Status: " + (emp.dataDevolucao == null ? "EM CURSO" : 
                                               "FINALIZADO em " + emp.dataDevolucao.format(dateFormatter)));
                            }
                        } else {
                            System.out.println("Nenhum empréstimo registrado");
                        }
                        
                        // Reservas
                        System.out.println("\nRESERVAS:");
                        if (historicoReservas.containsKey(usuario)) {
                            for (Reserva res : historicoReservas.get(usuario)) {
                                System.out.println("- " + titulosLivros.getOrDefault(res.codigoLivro, "Desconhecido") + 
                                               " | Reserva: " + res.dataInicio.format(dateFormatter) +
                                               (res.dataFim == null ? "" : " | Cancelada: " + res.dataFim.format(dateFormatter)));
                            }
                        } else {
                            System.out.println("Nenhuma reserva registrada");
                        }
                        break;
                        
                    case "ntf": // Notificações recebidas
                        if (partes.length != 2) throw new IllegalArgumentException();
                        int usuarioNtf = Integer.parseInt(partes[1]);
                        
                        System.out.println("Usuário " + usuarioNtf + " recebeu " + 
                                         contadorNotificacoes.getOrDefault(usuarioNtf, 0) + " notificações");
                        break;
                        
                    default:
                        System.out.println("Comando inválido. Use: emp, dev, res, obs, liv, usu, ntf ou sai");
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Códigos devem ser números inteiros");
            } catch (IllegalArgumentException e) {
                System.out.println("Formato inválido. Use:");
                System.out.println("  emp [usuário] [livro] - Empréstimo");
                System.out.println("  dev [usuário] [livro] - Devolução");
                System.out.println("  res [usuário] [livro] - Reserva");
                System.out.println("  obs [usuário] [livro] - Registrar observador");
                System.out.println("  liv [livro] - Consultar livro");
                System.out.println("  usu [usuário] - Histórico do usuário");
                System.out.println("  ntf [usuário] - Notificações recebidas");
            }
        }
        
        scanner.close();
        System.out.println("Sistema encerrado.");
    }
    
    // Classes auxiliares para armazenar histórico
    static class Emprestimo {
        int codigoLivro;
        LocalDate dataEmprestimo;
        LocalDate dataDevolucao;
        
        Emprestimo(int codigoLivro, LocalDate dataEmprestimo, LocalDate dataDevolucao) {
            this.codigoLivro = codigoLivro;
            this.dataEmprestimo = dataEmprestimo;
            this.dataDevolucao = dataDevolucao;
        }
    }
    
    static class Reserva {
        int codigoLivro;
        LocalDate dataInicio;
        LocalDate dataFim;
        
        Reserva(int codigoLivro, LocalDate dataInicio) {
            this.codigoLivro = codigoLivro;
            this.dataInicio = dataInicio;
            this.dataFim = null;
        }
    }
}