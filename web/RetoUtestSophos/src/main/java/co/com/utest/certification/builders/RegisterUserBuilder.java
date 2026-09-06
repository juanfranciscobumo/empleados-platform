package co.com.utest.certification.builders;

import co.com.utest.certification.interfaces.IRegisterUser;
import co.com.utest.certification.models.RegisterUserModel;

public class RegisterUserBuilder implements IRegisterUser {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String month;
    private String year;
    private String day;
    private String languages;
    private String device;
    private String model;
    private String operatingSystem;
    private String password;

    public RegisterUserBuilder(String firstName) {
        this.firstName = firstName;
    }

    public static RegisterUserBuilder firstName(String firstName) {
        return new RegisterUserBuilder(firstName);
    }

    public RegisterUserBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public RegisterUserBuilder emailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public RegisterUserBuilder month(String month) {
        this.month = month;
        return this;
    }

    public RegisterUserBuilder year(String year) {
        this.year = year;
        return this;
    }

    public RegisterUserBuilder day(String day) {
        this.day = day;
        return this;
    }

    public RegisterUserBuilder languages(String languages) {
        this.languages = languages;
        return this;
    }

    public RegisterUserBuilder device(String device) {
        this.device = device;
        return this;
    }

    public RegisterUserBuilder model(String model) {
        this.model = model;
        return this;
    }

    public RegisterUserBuilder operatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
        return this;
    }

    public RegisterUserModel password(String password) {
        this.password = password;
        return this.build();
    }

    @Override
    public RegisterUserModel build() {
        RegisterUserModel register = new RegisterUserModel();
        register.setFirstName(this.firstName);
        register.setLastName(this.lastName);
        register.setEmailAddress(this.emailAddress);
        register.setMonth(this.month);
        register.setYear(this.year);
        register.setDay(this.day);
        register.setLanguages(this.languages);
        register.setDevice(this.device);
        register.setModel(this.model);
        register.setOperatingSystem(this.operatingSystem);
        register.setPassword(this.password);
        return register;
    }
}
