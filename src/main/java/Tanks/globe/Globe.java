package Tanks.globe;

public interface Globe {

    enum GAME_TYPE{
        INSTANCE("instance"),
        TREE("T"),
        BACKGROUND("X"),
        PLAYER_A("A"),
        PLAYER_B("B"),
        PLAYER_C("C"),
        PLAYER_D("D");

        final String value;
        GAME_TYPE(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public char getChar(){
            return value.charAt(0);
        }

        public boolean isPlayer (char player) {
            return  player == PLAYER_A.getChar() || player == PLAYER_B.getChar() ||
                    player == PLAYER_C.getChar() || player == PLAYER_D.getChar();
        }

    }

}
