package game.socket.mini_rpg.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import game.socket.mini_rpg.dto.AcaoRequestDTO;
import game.socket.mini_rpg.dto.JogadorRequestDTO;
import game.socket.mini_rpg.service.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class BatalhaHandler extends TextWebSocketHandler {

    @Autowired
    private BattleService battleService;

    private final ObjectMapper mapper = new ObjectMapper();
    // Lista de sessões aberta para o resto do sistema
    public static final CopyOnWriteArrayList<WebSocketSession> sessoes = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessoes.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessoes.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        
        try {
            if (payload.contains("\"acao\"")) {
                // 1. Converte para o DTO de ação
                AcaoRequestDTO acaoReq = mapper.readValue(payload, AcaoRequestDTO.class);
                
                // 2. Chama o método correto: receberAcao (que retorna Optional)
                var resultadoOpt = battleService.receberAcao(acaoReq.jogador(), acaoReq.acao());
                
                // 3. Se o Optional estiver presente, significa que os dois jogaram e o turno acabou
                if (resultadoOpt.isPresent()) {
                    var resultadoDTO = resultadoOpt.get();
                    // Envia os logs linha por linha para todos
                    for (String log : resultadoDTO.logs()) {
                        broadcast(log);
                    }
                    if (resultadoDTO.vencedor() != null) {
                        broadcast("🏆 FIM DE JOGO! " + resultadoDTO.vencedor());
                    }
                } else {
                    // Se o turno não acabou, apenas confirma que recebeu a ação
                    session.sendMessage(new TextMessage("Ação de " + acaoReq.jogador() + " registrada. Aguardando oponente..."));
                }

            } else {
                // Lógica de entrada de jogador (continua igual, mas chamando entrarNaArena)
                JogadorRequestDTO entrada = mapper.readValue(payload, JogadorRequestDTO.class);
                String resultado = battleService.entrarNaArena(entrada.nome(), entrada.tipo());
                broadcast(resultado);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Ajuda a ver o erro real no console do VS Code
            session.sendMessage(new TextMessage("Erro no processamento: " + e.getMessage()));
        }
    }

    public void broadcast(String mensagem) throws IOException {
        for (WebSocketSession s : sessoes) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(mensagem));
            }
        }
    }
}