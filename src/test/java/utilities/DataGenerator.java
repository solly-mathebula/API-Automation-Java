package utilities;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Random;

public class DataGenerator {

    private static final Faker faker = new Faker();
    private static final Faker saFaker = new Faker(new Locale("en-ZA"));
    private static final Random random = new Random();

    public static String getFirstName() {
        return faker.name().firstName();
    }

    public static String getLastName() {
        return faker.name().lastName();
    }

    public static String getFullName() {
        return faker.name().fullName();
    }

    public static String getEmail() {
        return faker.internet().emailAddress();
    }

    public static String getPhoneNumber() {
        return saFaker.phoneNumber().cellPhone(); // Local SA format
    }

    public static String getAddress() {
        return faker.address().fullAddress();
    }

    public static String getCity() {
        return faker.address().city();
    }

    public static String getCountry() {
        return faker.address().country();
    }

    public static String getZipCode() {
        return faker.address().zipCode();
    }

    public static String getCompany() {
        return faker.company().name();
    }

    public static String getJobTitle() {
        return faker.job().title();
    }

    public static String getUsername() {
        return faker.name().username();
    }

    public static String getPassword() {
        return faker.internet().password(8, 16, true, true);
    }

    public static String getRandomNumberString(int length) {
        return faker.number().digits(length);
    }

    public static String getRandomText() {
        return faker.lorem().sentence();
    }

    public static String getCreditCardNumber() {
        return faker.finance().creditCard();
    }

    public static String getBankAccountNumber() {
        return faker.finance().iban(); // or .bankAccountNumber()
    }

    public static String getSouthAfricanIDNumber() {
        // SA ID: YYMMDDSSSSCAZ
        String birthDate = faker.date().birthday().toInstant().toString().substring(2, 10).replaceAll("-", "");
        String sequence = String.format("%04d", random.nextInt(9999));
        String citizenship = "0"; // 0 = SA citizen, 1 = permanent resident
        String gender = "8"; // Random digit for gender
        String checkDigit = String.valueOf(random.nextInt(10)); // Not a real Luhn check
        return birthDate + sequence + citizenship + gender + checkDigit;
    }

    public static String getPassportNumber() {
        // Format: 2 letters + 7 digits
        String letters = faker.letterify("??").toUpperCase();
        String digits = faker.numerify("#######");
        return letters + digits;
    }

    public static String getMedicalAidNumber() {
        // Format: MA-XXXX-XXXX
        return "MA-" + getRandomNumberString(4) + "-" + getRandomNumberString(4);
    }

    // Country-specific ID (you can expand this with switch-case for more countries)
    public static String getNationalIDByCountry(String countryCode) {
        switch (countryCode.toUpperCase()) {
            case "ZA":
                return getSouthAfricanIDNumber();
            case "US":
                return faker.idNumber().ssnValid();
            case "IN":
                return "IN" + getRandomNumberString(12); // Aadhar-style mock
            default:
                return "ID-" + getRandomNumberString(10);
        }
    }
}
