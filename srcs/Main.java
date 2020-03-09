package solver;

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
        Solver solver = new Solver(mat);
        ComplexNumber[] result = solver.execute();
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
            List<ComplexNumber> nums = new ArrayList<>(vars * vars + vars);
            for (int i = 1; i < list.size(); i++) {
                String[] numbers = list.get(i).split(" ");
                for (String s : numbers) {
                    nums.add(parseComplexNumber(s));
                }
            }
            res.fillMatrix(nums);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

    private static ComplexNumber parseComplexNumber(String s) {
        String[] nums;
        int minus = 0;
        double real = 0;
        double image = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '-')
                minus++;
        }
        if (s.contains("+"))
            nums = s.split("\\+");
        else if (minus == 2 || (minus == 1 && s.charAt(0) != '-')) {
            nums = new String[2];
            nums[0] = s.substring(0, s.lastIndexOf('-'));
            nums[1] = s.substring(s.lastIndexOf('-'));
        } else
            nums = new String[]{s};
        for (String str : nums) {
            if (str.contains("i")) {
                str = str.substring(0, str.indexOf("i"));
                if (str.length() == 0)
                    str = "1";
                else if (str.length() == 1 && str.contains("-"))
                    str = "-1";
                image = Double.parseDouble(str);
            } else {
                real = Double.parseDouble(str);
            }
        }
        return new ComplexNumber(real, image);
    }

    private static void saveToFile(String out, ComplexNumber[] result) {
        try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(Paths.get(out),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            //printing solution
            System.out.print("The solution is: (");
            for (ComplexNumber d : result) {
                String s = "";
                //if real part exists write it
                if (Math.abs(d.getReal() - 0) > d.PRECISION)
                    s += String.format(Locale.ENGLISH,"%.4f", d.getReal());
                //if both parts exist and image not one write standard format
                if (Math.abs(d.getImage() - 0) > d.PRECISION && Math.abs(d.getReal() - 0) > d.PRECISION &&
                    Math.abs(d.getImage() - 1) > d.PRECISION)
                    s += String.format(Locale.ENGLISH,"%+.4fi", d.getImage());
                //if real part doesn't exist and image not one write image without plus sign
                else if ((Math.abs(d.getImage() - 0) > d.PRECISION && Math.abs(d.getReal() - 0) < d.PRECISION) &&
                        Math.abs(d.getImage() - 1) > d.PRECISION)
                    s += String.format(Locale.ENGLISH,"%.4fi", d.getImage());
                //if real exists and image is plus one - just i
                else if (Math.abs(d.getReal() - 0) > d.PRECISION &&Math.abs(d.getImage() - 1) < d.PRECISION
                        && d.getImage() > 0)
                    s += "+i";
                //if real doesn't exist and image is plus one - just i
                else if (Math.abs(d.getReal() - 0) < d.PRECISION &&Math.abs(d.getImage() - 1) < d.PRECISION
                        && d.getImage() > 0)
                    s += "i";
                //if image is minus one - i with sign
                else if (Math.abs(d.getImage() - 1) < d.PRECISION && d.getImage() < 0)
                    s += "-i";
                //else number is zero
                else
                    s += "0";
                System.out.print(s);
                if (d != result[result.length - 1])
                    System.out.print(", ");
                s += "\n";
                writer.append(s);
            }
            System.out.println(")");
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
