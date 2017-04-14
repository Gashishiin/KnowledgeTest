package base;

public enum QuestionType {
    SINGLE_CHOICE(0),
    MULTI_CHOICE(1),
    FREE(2);

    public int getValue() {
        return value;
    }

    private int value;


    QuestionType(int value) {
        this.value = value;
    }


}
