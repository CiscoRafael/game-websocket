package game.socket.mini_rpg.model;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class Intencao {
    private String nomeJogador;
    private TipoAcao acao;
}