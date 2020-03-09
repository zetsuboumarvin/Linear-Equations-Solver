package solver;

import java.util.stream.Collectors;

public class ComplexNumber {
    private double real;
    private double image;
    //precision for double comparison
    public final double PRECISION = 0.0001;

    public ComplexNumber(double real, double image) {
        this.real = real;
        this.image = image;
    }

    public double getImage() {
        return image;
    }
    
    public double getReal() {
        return real;
    }
    
    public void setReal(double real) {
        this.real = real;
    }
    
    public void setImage(double image) {
        this.image = image;
    }

    public static ComplexNumber add(ComplexNumber first, ComplexNumber second) {
        double r = first.real + second.real;
        double i = first.image + second.image;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber subtract(ComplexNumber first, ComplexNumber second) {
        double r = first.real - second.real;
        double i = first.image - second.image;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber multiplication(ComplexNumber first, ComplexNumber second) {
        double r = first.real * second.real - first.image * second.image;
        double i = first.real * second.image + first.image * second.real;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber multiplication(double first, ComplexNumber second) {
        double r = first * second.real;
        double i = first * second.image;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber division(ComplexNumber first, ComplexNumber second) {
        ComplexNumber conjugate = new ComplexNumber(second.real, second.image * -1.0);
        double denominator = second.real * second.real + second.image * second.image;
        ComplexNumber numerator = ComplexNumber.multiplication(first, conjugate);
        numerator.setReal(numerator.getReal() / denominator);
        numerator.setImage(numerator.getImage() / denominator);
        return numerator;
    }

    public boolean compareToSimpleNumber(double num) {
        if (Math.abs(this.real - num) < PRECISION && Math.abs(this.image - 0) < PRECISION)
            return true;
        else
            return false;
    }

    public boolean compareToComplexNumber(ComplexNumber num) {
        if (Math.abs(this.real - num.real) < PRECISION &&
                Math.abs(this.image - num.image) < PRECISION)
            return true;
        else
            return false;
    }
}
