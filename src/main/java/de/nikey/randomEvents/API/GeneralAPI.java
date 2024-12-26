package de.nikey.randomEvents.API;

public class GeneralAPI {
    public static int randomAmount(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }
}
