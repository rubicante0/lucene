package lt.mxs.lucene;

public class Person {
    private final long id;
    private final String name;
    private final int age;
    private final GenderType gender;

    public Person(long id, String name, int age, GenderType gender) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public GenderType getGender() {
        return gender;
    }

    public enum GenderType {
        MALE,
        FEMALE,
        UNKNOWN
    }
}
