package base;

public enum UserRole {
    ROLE_ADMIN(0),
    ROLE_METHODIST(1),
    ROLE_STUDENT(2);

    private int value;
    public int getValue(){return value;}

    UserRole(int value){this.value = value;}
}
