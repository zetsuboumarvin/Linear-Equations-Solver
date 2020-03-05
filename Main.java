import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

enum Errors {
    NO_SOLUTIONS, TOO_MANY_SOLUTIONS
}

public class Main {
    public static void main(String[] args) {
        String in = "";
        String out = "";
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-in":
                    in = args[i + 1];
                    break;
                case "-out":
                    out = args[i + 1];
                    break;
            }
        }
        Matrix mat = parseFile(in);
        if (mat == null)
            return;

        System.out.println();
        mat.printMatrix();
        System.out.println();

        Solver solver = new Solver(mat);
        double[] result = solver.execute();
        if (result == null) {
            saveToFile(out, solver.error);
        } else {
            saveToFile(out, result);
        }
    }

    // Reads the file content and writes it into Matrix
    private static Matrix parseFile(String in) {
        Matrix res;
        try {
            List<String> list = Files.readAllLines(Paths.get(in));
            String[] arr = list.get(0).split(" ");
            int vars = Integer.parseInt(arr[0]);
            int equ = Integer.parseInt(arr[1]);
            res = Matrix.createMatrix(equ, vars);
            List<Double> nums = new ArrayList<>(vars * vars + vars);
            for (int i = 1; i < list.size(); i++) {
                String[] numbers = list.get(i).split(" ");
                for (String s : numbers) {
                    nums.add(Double.parseDouble(s));
                }
            }
            res.fillMatrix(nums);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    private static void saveToFile(String out, double[] result) {
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(out),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            for (Double d : result) {
                String s = String.format(Locale.ENGLISH,"%.4f\n", d);
                writer.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Save to file " + out);
    }

    private static void saveToFile(String out, Errors error) {
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(out),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            if (error == Errors.NO_SOLUTIONS) {
                writer.append("No solutions\n");
                System.out.println("No solutions");
            } else if (error == Errors.TOO_MANY_SOLUTIONS) {
                writer.append("Infinitely many solutions\n");
                System.out.println("Infinitely many solutions");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("Save to file " + out);
    }
}
