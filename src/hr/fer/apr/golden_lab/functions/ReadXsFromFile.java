package hr.fer.apr.golden_lab.functions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Igor Farszky on 31.10.2016..
 */
public abstract class ReadXsFromFile {

    public static double[] readOneSpot(String path) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        try {

            String line = br.readLine();

            int elCount = line.split(" ").length;
            double[] elements = new double[elCount];

            int i = 0;
            for (String el : line.split(" ")) {
                elements[i] = Double.parseDouble(el);
                i++;
            }

        return elements;
    } catch(
    IOException e)

    {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

        return null;
}

    public static double[][] readSpots(String path) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(path));

        String line = "";
        try {
            List<String> rows = new ArrayList<>();

            while ((line = br.readLine()) != null) {
                rows.add(line);
            }

            int elCount = rows.get(0).split(" ").length;
            double[][] elements = new double[rows.size()][elCount];

            int i = 0;
            int j = 0;
            for (String s : rows) {
                for (String el : s.split(" ")) {
                    elements[i][j] = Double.parseDouble(el);
                    j++;
                }
                i++;
                j = 0;
            }


            return elements;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
