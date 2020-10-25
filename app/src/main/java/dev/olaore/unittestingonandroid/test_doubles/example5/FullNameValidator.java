package dev.olaore.unittestingonandroid.test_doubles.example5;

public class FullNameValidator {

    public static boolean isValidFullName(String fullName) {
        // trivially simple task
        return !fullName.isEmpty();
    }
}
