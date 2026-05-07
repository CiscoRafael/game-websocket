package game.socket.mini_rpg.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import game.socket.mini_rpg.dto.TurnoResultadoDTO;
import game.socket.mini_rpg.model.Intencao;
import game.socket.mini_rpg.model.Personagem;
import game.socket.mini_rpg.model.Classe;
import game.socket.mini_rpg.model.StatusEfeito;
import game.socket.mini_rpg.model.TipoAcao;
import game.socket.mini_rpg.model.SpecialSkill;
import java.util.Optional;

@Service
public class BattleService {
    private final Map<String, Personagem> jogadoresAtivos = new ConcurrentHashMap<>();
    private final Map<String, Intencao> acoesPendentes = new ConcurrentHashMap<>();
    private boolean alternarTurnoPrioridade = true; 

    public synchronized Optional<TurnoResultadoDTO> receberAcao(String jogador, TipoAcao acao) {
        if (!jogadoresAtivos.containsKey(jogador)) {
            return Optional.empty(); // ou erro
        }

        acoesPendentes.put(jogador, new Intencao(jogador, acao));

        if (acoesPendentes.size() == 2) {
            return Optional.of(resolverConflito());
        }

        return Optional.empty();
    }

    public String entrarNaArena(String nome, Classe classe) {
        if (jogadoresAtivos.size() >= 2) {
            return "Arena cheia!";
        }

        Personagem p = new Personagem(nome, classe);

        jogadoresAtivos.put(nome, p);

        // segundo jogador entrou
        if (jogadoresAtivos.size() == 2) {

            return "START_GAME:🔥 Batalha iniciada entre "
                    + jogadoresAtivos.keySet();
        }

        // primeiro jogador esperando
        return "WAITING:" + nome + " entrou na arena. Aguardando oponente...";
    }

    private TurnoResultadoDTO resolverConflito() {
        // Inicializa a lista de logs que estava faltando
        List<String> logsTurno = new ArrayList<>();
            
        Object[] nomes = acoesPendentes.keySet().toArray();
        Personagem p1 = jogadoresAtivos.get(nomes[0].toString());
        Personagem p2 = jogadoresAtivos.get(nomes[1].toString());

        // Determinar prioridade
        Personagem primeiro, segundo;
        double chance = Math.random();
        Personagem maisRapido = (p1.getVelocidadeMovimento() > p2.getVelocidadeMovimento()) ? p1 : p2;
        Personagem maisLento = (maisRapido == p1) ? p2 : p1;

        if (chance < 0.20 && p1.getVelocidadeMovimento() != p2.getVelocidadeMovimento()) {
            primeiro = maisRapido;
            segundo = maisLento;
            logsTurno.add("⚡ " + primeiro.getNome() + " foi mais rápido e tomou a iniciativa!");
        } else {
            if (alternarTurnoPrioridade) { primeiro = p1; segundo = p2; }
            else { primeiro = p2; segundo = p1; }
            alternarTurnoPrioridade = !alternarTurnoPrioridade;
        }

        // Execução das ações
        logsTurno.add(executarAcao(primeiro, segundo, acoesPendentes.get(primeiro.getNome()).getAcao()));

        if (segundo.getHpAtual() > 0) {
            logsTurno.add(executarAcao(segundo, primeiro, acoesPendentes.get(segundo.getNome()).getAcao()));
        } else {
            logsTurno.add("💀 " + segundo.getNome() + " caiu antes de conseguir agir!");
        }

        //  APLICAÇÃO DE STATUS (FOGO) 
        String logFogo1 = aplicarDanoContinuo(p1);
        if (logFogo1 != null) logsTurno.add(logFogo1);
        
        String logFogo2 = aplicarDanoContinuo(p2);
        if (logFogo2 != null) logsTurno.add(logFogo2);

        // Pós-turno
        recuperarVigor(p1);
        recuperarVigor(p2);
        String vencedor = checarVencedor(p1, p2);

        // Cria o objeto de retorno
        TurnoResultadoDTO resultado = TurnoResultadoDTO.builder()
            .logs(logsTurno)
            .estadoJogadores(Map.of(p1.getNome(), p1, p2.getNome(), p2))
            .vencedor(vencedor)
            .animacaoTipo("TURNO_FINALIZADO")
            .build();

        // LIMPANDO OS DADOS SE A LUTA ACABOU
        acoesPendentes.clear();
        
        if (vencedor != null) {
            // Remove os jogadores do mapa de ativos para liberar memória
            jogadoresAtivos.remove(p1.getNome());
            jogadoresAtivos.remove(p2.getNome());
            
            // Opcional: resetar a alternância de turno para a próxima partida
            alternarTurnoPrioridade = true; 
        }

        return resultado;
    }

    private String realizarAtaque(Personagem atacante, Personagem defensor) {
        if (atacante.getVigorAtual() < atacante.getVigorPorAtaque()) {
            return "Cansado demais para atacar";
        }

        // Lógica de Desvio Passivo (Velocidade de Movimento vs Ataque)
        if (defensor.getVelocidadeMovimento() > atacante.getVelocidadeAtaque()) {
            if (Math.random() < 0.10) {
                atacante.setVigorAtual(atacante.getVigorAtual() - atacante.getVigorPorAtaque());
                return defensor.getNome() + " deu um passinho pro lado e desviou!";
            }
        }

        // Tratamento de Status do Defensor (Reativo)
        if (defensor.getStatusEfeito() == StatusEfeito.PARRY) {
            consumirVigorEAtaque(atacante);
            defensor.setStatusEfeito(StatusEfeito.NORMAL);
            return defensor.getNome() + " usou PARRY! O golpe de " + atacante.getNome() + "foi totalmente bloqueado";
        }
        // Tratamento de Status do Defensor (Esquiva)
        if (defensor.getStatusEfeito() == StatusEfeito.ESQUIVA) {
            double contraAtaque = defensor.getDano() * 1.5;
            atacante.setHpAtual(atacante.getHpAtual() - contraAtaque);
            consumirVigorEAtaque(atacante);
            defensor.setStatusEfeito(StatusEfeito.NORMAL);
            return defensor.getNome() + " desviou e contratacou";
        }

        // Cálculo de Dano (Verificando se o atacante está com DANO_PERFURANTE)
        double danoCalculado;
        if (atacante.getStatusEfeito() == StatusEfeito.DANO_PERFURANTE) {
            danoCalculado = atacante.getDano(); // Ignora defesa
            atacante.setStatusEfeito(StatusEfeito.NORMAL); // Consome o efeito
            defensor.setHpAtual(defensor.getHpAtual() - danoCalculado);
            consumirVigorEAtaque(atacante);
            return atacante.getNome() + " perfurar a defesa do oponente causando " + danoCalculado + " de dano";
        } else if (defensor.getStatusEfeito() == StatusEfeito.EM_DEFESA){
            danoCalculado = Math.max(0, atacante.getDano() - (defensor.getDefense() + defensor.getDefense() * Math.random()));
            defensor.setStatusEfeito(StatusEfeito.NORMAL); // Consome o efeito
            consumirVigorEAtaque(atacante);
            return defensor.getNome() + " estava em modo de defesa tomando só " + danoCalculado + " de dano";
        }else{
            danoCalculado = Math.max(0, atacante.getDano() - defensor.getDefense());
            defensor.setHpAtual(defensor.getHpAtual() - danoCalculado);
            consumirVigorEAtaque(atacante);
            return atacante.getNome() + " atacou e causou " + danoCalculado + " de dano";
        }
    }

    private void procesarSkillEspecial(Personagem atacante, Personagem defensor) {
        SpecialSkill skill = atacante.getSpecialSkill();
        
        if (atacante.getManaAtual() >= skill.getMana() && atacante.getVigorAtual() >= skill.getVigor()) {
            
            atacante.setManaAtual(atacante.getManaAtual() - skill.getMana());
            atacante.setVigorAtual(atacante.getVigorAtual() - skill.getVigor());
            
            skill.usar(atacante, defensor);
        }
    }

    private void consumirVigorEAtaque(Personagem p) {
        p.setVigorAtual(Math.max(0, p.getVigorAtual() - p.getVigorPorAtaque()));
    }

    private void defender(Personagem p){
        p.setStatusEfeito(StatusEfeito.EM_DEFESA);
        p.setVigorAtual(Math.max(0, p.getVigorAtual() - p.getVigorPorDefesa()));
    }

    private String usarPotion(Personagem p) {
        if (p.getPotions() <= 0) return p.getNome() + " não tem mais poções!";
        
        double cura = 50;
        p.setHpAtual(Math.min(p.getHpMax(), p.getHpAtual() + cura));
        p.setPotions(p.getPotions() - 1);
        
        // Limpa o fogo ao usar poção!
        if (p.getStatusEfeito() == StatusEfeito.QUEIMANDO) {
            p.setStatusEfeito(StatusEfeito.NORMAL);
            return p.getNome() + " usou uma poção, recuperou vida e apagou as chamas!";
        }
        
        return p.getNome() + " recuperou vida! HP atual: " + p.getHpAtual();
    }

    private String aplicarDanoContinuo(Personagem p) {
        if (p.getStatusEfeito() == StatusEfeito.QUEIMANDO) {
            double danoFogo = 5.0; // Valor fixo
            p.setHpAtual(Math.max(0, p.getHpAtual() - danoFogo));
            return "🔥 " + p.getNome() + " sofreu " + danoFogo + " de dano por queimadura!";
        }
        return null;
    }

    private String executarAcao(Personagem executor, Personagem alvo, TipoAcao tipo) {
        return switch (tipo) {
            case ATACAR -> realizarAtaque(executor, alvo);
            case DEFENDER -> { 
                defender(executor); 
                yield executor.getNome() + " levantou o escudo e está em modo de defesa!"; 
            }
            case ESPECIAL -> { 
                String nomeSkill = executor.getSpecialSkill().getNome();
                procesarSkillEspecial(executor, alvo); 
                yield executor.getNome() + " usou a habilidade especial: " + nomeSkill + "!"; 
            }
            case POTION -> usarPotion(executor);
        };
    }

    private void recuperarVigor(Personagem p) {
        p.setVigorAtual(Math.min(p.getVigorTotal(), p.getVigorAtual() + 1));
    }

    private String checarVencedor(Personagem p1, Personagem p2) {
        if (p1.getHpAtual() <= 0) return p2.getNome() + "VENCEU";
        if (p2.getHpAtual() <= 0) return p1.getNome() + "VENCEU";
        return null;
    }

    public String abandonarPartida(String nomeJogador) {
        // Se o jogador sair, o oponente vence por W.O. ou simplesmente limpamos a arena
        jogadoresAtivos.remove(nomeJogador);
        acoesPendentes.remove(nomeJogador);
        return "Jogador "+ nomeJogador + "fugiu da luta!";
    }
}