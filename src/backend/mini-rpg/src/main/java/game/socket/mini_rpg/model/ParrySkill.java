package game.socket.mini_rpg.model;

public class ParrySkill implements SpecialSkill{
    public String getNome() { return "Parry otario"; }
    public double getMana() { return 10.00; }
    public double getVigor() {return 5.00; }

    @Override
    public void usar(Personagem usuario, Personagem alvo) {
        usuario.setStatusEfeito(StatusEfeito.PARRY);
    }  
}
