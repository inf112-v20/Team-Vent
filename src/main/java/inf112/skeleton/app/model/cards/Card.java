package inf112.skeleton.app.model.cards;

public enum Card {
    MOVE_ONE,
    MOVE_TWO,
    MOVE_THREE,
    BACK_UP,
    ROTATE_RIGHT,
    ROTATE_LEFT,
    U_TURN {
        @Override
        public String toString() {
            return "U-TURN";
        }
    },
    ;

    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }
}