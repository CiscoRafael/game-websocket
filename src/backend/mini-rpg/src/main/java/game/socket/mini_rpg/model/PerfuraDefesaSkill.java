package game.socket.mini_rpg.model;

public class PerfuraDefesaSkill implements SpecialSkill {
    public String getNome() { return "Golpe Perfurante"; }
    public double getMana(){ return 20.00; }
    public double getVigor() {return 4.00; }
    
    @Override
    public void usar(Personagem usuario, Personagem alvo) {
        usuario.setStatusEfeito(StatusEfeito.DANO_PERFURANTE);
    }
}
