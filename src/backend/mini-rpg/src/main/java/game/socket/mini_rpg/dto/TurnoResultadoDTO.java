package game.socket.mini_rpg.dto;

import java.util.List;
import java.util.Map;
import game.socket.mini_rpg.model.Personagem;
import lombok.Builder;

@Builder
public record TurnoResultadoDTO (
    List<String> logs,
    Map<String, Personagem> estadoJogadores,
    String vencedor,
    String animacaoTipo
){}