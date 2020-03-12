import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

class Solver {
    Matrix mat;
    Errors error = null;
    List<Integer> colSwaps;

    Solver(Matrix mat) {
        this.mat = mat;
        colSwaps = new LinkedList<>();
    }

    ComplexNumber[] execute() {
        ComplexNumber[] result = new ComplexNumber[mat.getLength() - 1];
        System.out.println("Start solving the equation.");
        System.out.println("Row manipulation:");
        gaussianElimination();
        error = checkSignEqus();
        if (error != null)
            return null;
        gaussJordanElimination();
        //saving result
        for (int i = 0; i < mat.getLength() - 1; i++) {
            result[i] = mat.getNum(i, mat.getLength() - 1);
        }
        //columns reverse
        Collections.reverse(colSwaps);
        for (int i = 0; i < colSwaps.size(); i += 2) {
            ComplexNumber temp = result[colSwaps.get(i)];
            result[colSwaps.get(i)] = result[colSwaps.get(i + 1)];
            result[colSwaps.get(i + 1)] = temp;
        }
        return result;
    }

    private Errors checkSignEqus() {
        int count = 0;
        for (int i = 0; i < mat.getSize(); i++) {
            for (int j = 0; j < mat.getLength(); j++) {
                if (!mat.getNum(i, j).compareToSimpleNumber(0) && j != mat.getLength() - 1) {
                    count++;
                    break;
                } else if (!mat.getNum(i, j).compareToSimpleNumber(0) && j == mat.getLength() - 1) {
                    return Errors.NO_SOLUTIONS;
                }
            }
        }
        if (count > mat.getLength() - 1) {
            return Errors.NO_SOLUTIONS;
        } else if (count < mat.getLength() - 1) {
            return Errors.TOO_MANY_SOLUTIONS;
        }
        return null;
    }

    private void gaussJordanElimination() {
        for (int i = mat.getSize() - 1; i > 0; i--) {
            for (int j = 0; j < mat.getLength() - 1; j++) {
                if (mat.getNum(i, j).compareToSimpleNumber(1)) {
                    columnClear(i, j, false);
                    break;
                }
            }
        }
    }

    private void gaussianElimination() {
        for (int i = 0; i < mat.getLength() - 1 && i < mat.getSize(); i++) {
            int j = i;
            ComplexNumber x = mat.getNum(i, j);
            //work with null element
            if (x.compareToSimpleNumber(0)) {
                int[] coordinates = findNextNonZero(i, j);
                if (coordinates[0] > i)
                    rowSwap(i, coordinates[0]);
                if (coordinates[1] > j) {
                    colSwap(j, coordinates[1]);
                    colSwaps.add(j);
                    colSwaps.add(coordinates[1]);
                }
                if (i == coordinates[0] && j == coordinates[1])
                    return;
                x = mat.getNum(i, j);
            }

            //work with non-null element
            if (!x.compareToSimpleNumber(1))
                rowMultiplication(ComplexNumber.division(new ComplexNumber(1, 0), x), i, i);
            if (i != mat.getLength() - 2)
                columnClear(i, j, true);
        }
    }

    private int[] findNextNonZero(int startRow, int startCol) {
        int resRow = startRow;
        int resCol = startCol;
        boolean finished = false;
        while (!finished) {
            for (int i = startRow; i < mat.getSize() && !finished; i++) {
                if (!mat.getNum(i, startCol).compareToSimpleNumber(0)) {
                    finished = true;
                    resRow = i;
                    resCol = startCol;
                }
            }
            for (int i = startCol; i < mat.getLength() - 1 && !finished; i++) {
                if (!mat.getNum(startRow, i).compareToSimpleNumber(0)) {
                    finished = true;
                    resRow = startRow;
                    resCol = i;
                }
            }
            if (startRow != mat.getSize() - 1)
                startRow++;
            if (startCol != mat.getLength() - 2)
                startCol++;
            if (startRow == mat.getSize() - 1 && startCol == mat.getLength() - 2)
                finished = true;
        }
        return (new int[] {resRow, resCol});
    }

    private void rowSwap(int sourceRow, int destRow) {
        for (int i = 0; i < mat.getLength(); i++) {
            ComplexNumber temp = mat.getNum(destRow, i);
            mat.setNum(destRow, i, mat.getNum(sourceRow, i));
            mat.setNum(sourceRow, i, temp);
        }
        System.out.println("R" + (sourceRow + 1) + " <-> R" + (destRow + 1));
    }

    private void colSwap(int sourceCol, int destCol) {
        for (int i = 0; i < mat.getSize(); i++) {
            ComplexNumber temp = mat.getNum(i, destCol);
            mat.setNum(i, destCol, mat.getNum(i, sourceCol));
            mat.setNum(i, sourceCol, temp);
        }
        System.out.println("R" + (sourceCol + 1) + " <-> R" + (destCol + 1));
    }

    private void rowMultiplication(ComplexNumber coef, int sourceRow, int destRow) {
        int len = mat.getLength();
        for (int i = 0; i < len; i++) {
            ComplexNumber x = mat.getNum(sourceRow, i);
            x = ComplexNumber.multiplication(x, coef);
            if (sourceRow == destRow) {
                mat.setNum(sourceRow, i, x);
            }
            else {
                ComplexNumber y = mat.getNum(destRow, i);
                x = ComplexNumber.add(x, y);
                mat.setNum(destRow, i, x);
            }
        }
        if (Math.abs(coef.getImage() - 0) < coef.PRECISION)
            System.out.printf(Locale.ENGLISH, "%.1f * R%d", coef.getReal(), sourceRow + 1);
        else
            System.out.printf(Locale.ENGLISH, "%.1f%+.1fi * R%d", coef.getReal(),
                    coef.getImage(), sourceRow + 1);
        if (sourceRow != destRow)
            System.out.print(" + R" + (destRow + 1));
        System.out.println(" -> R" + (destRow + 1));
    }

    private void columnClear(int startRow, int column, boolean down) {
        ComplexNumber start = mat.getNum(startRow, column);
        if (down) {
            for (int i = startRow + 1; i < mat.getSize(); i++) {
                ComplexNumber x = mat.getNum(i, column);
                if (x.compareToSimpleNumber(0))
                    continue;
                ComplexNumber coef = ComplexNumber.multiplication(-1, ComplexNumber.division(x, start));
                rowMultiplication(coef, startRow, i);
            }
        } else {
            for (int i = startRow - 1; i >= 0; i--) {
                ComplexNumber x = mat.getNum(i, column);
                if (x.compareToSimpleNumber(0))
                    continue;
                ComplexNumber coef = ComplexNumber.multiplication(-1, ComplexNumber.division(x, start));
                rowMultiplication(coef, startRow, i);
            }
        }
    }
}
