package stellarburgers.generators;

import com.github.javafaker.Faker;
import models.User;

import java.util.Locale;

public class UserGenerator {
    Faker fakerEn;
    Faker fakerRu;
    public static User getRandom(){
        Faker fakerRu = new Faker(Locale.forLanguageTag("ru"));
        Faker fakerEn = new Faker();
        return new User(fakerRu.name().firstName(), fakerEn.internet().emailAddress(), fakerRu.internet().password());
    }

    public static User getWithoutField(String fieldName){
        Faker fakerRu = new Faker(Locale.forLanguageTag("ru"));
        Faker fakerEn = new Faker();

        if(fieldName.equals("name")){
            return new User(null, fakerEn.internet().emailAddress(), fakerRu.internet().password());
        } else if (fieldName.equals("email")) {
            return new User(fakerRu.name().firstName(), null, fakerRu.internet().password());
        } else if (fieldName.equals("password")) {
            return new User(fakerRu.name().firstName(), fakerEn.internet().emailAddress(), null);
        }
        return null;
    }
}
