package main.part3;

public class WeatherData {

    private static final ModelDataHelper modelDataHelper = new ModelDataHelper();

    public static void main (String[] args) {
        modelDataHelper.solveProblem("res/weather.dat", 2, 0, 1, 2);
    }

}
