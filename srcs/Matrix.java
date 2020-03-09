package solver;

import java.util.List;
import java.util.Locale;

//Singleton pattern
class Matrix {
    private static Matrix mat = null;
    private ComplexNumber[][] array;
    private int size;
    private int length;

    private Matrix(int equ, int vars) {
        array = new ComplexNumber[equ][vars];
    }

    static Matrix createMatrix(int equ, int vars) {
        if (mat == null) {
            mat = new Matrix(equ, vars + 1);
            mat.size = equ;
            mat.length = vars + 1;
        }
        return mat;
    }

    void setNum(int row, int col, ComplexNumber num) {
        array[row][col] = num;
    }

    ComplexNumber getNum(int row, int col) {
        return array[row][col];
    }

    void fillMatrix(List<ComplexNumber> arr) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                setNum(i, j, arr.get(i * array[0].length + j));
            }
        }
    }

    //for tests
    public void printMatrix() {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                if (Math.abs(array[i][j].getImage() - 0) < array[i][j].PRECISION)
                    System.out.printf(Locale.ENGLISH, "%.1f   ", array[i][j].getReal());
                else
                    System.out.printf(Locale.ENGLISH, "%.1f%+.1fi   ", array[i][j].getReal(),
                        array[i][j].getImage());
            }
            System.out.println();
        }
    }

    int getSize() {
        return this.size;
    }

    int getLength() {
        return this.length;
    }
}
