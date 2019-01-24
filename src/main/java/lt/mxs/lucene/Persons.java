package lt.mxs.lucene;

import java.util.Arrays;
import java.util.List;

public class Persons {

    public static List<Person> buildPersons() {
        return Arrays.asList(
                new Person(0, "Alpha and Omega", Integer.MAX_VALUE, Person.GenderType.UNKNOWN),
                new Person(1, "Marius", 40, Person.GenderType.MALE),
                new Person(2, "Petras Kurmelis", 30, Person.GenderType.MALE),
                new Person(3, "Stephen Morse", 19, Person.GenderType.UNKNOWN),
                new Person(4, "Caroline", 8, Person.GenderType.FEMALE));
    }

    private Persons() {
    }
}
