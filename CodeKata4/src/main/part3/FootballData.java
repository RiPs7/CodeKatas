package main.part3;

public class FootballData {

    private static final ModelDataHelper modelDataHelper = new ModelDataHelper();

    public static void main (String[] args)  {
        modelDataHelper.solveProblem("res/football.dat", 1, 1, 6, 8);
    }

}
