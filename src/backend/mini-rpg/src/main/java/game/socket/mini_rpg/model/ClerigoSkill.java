package game.socket.mini_rpg.model;

public class ClerigoSkill implements SpecialSkill {

    @Override
    public String getNome() {
        return "Cura Sagrada";
    }

    @Override
    public double getMana() {
        return 18.0;
    }

    @Override
    public double getVigor() {
        return 4.0;
    }

    @Override
    public void usar(Personagem usuario, Personagem alvo) {
        double cura = 35.0;
        usuario.setHpAtual(Math.min(usuario.getHpMax(), usuario.getHpAtual() + cura));

        if (usuario.getStatusEfeito() == StatusEfeito.QUEIMANDO) {
            usuario.setStatusEfeito(StatusEfeito.NORMAL);
        }
    }
}