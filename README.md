# Linear-Equations-Solver
JetBrains Academy's project

Linear Equations Solver is a program that solves linear equations.

## Usage
Compilation:
```
javac -d out srcs/*.java  
```
Launch:
```
java -cp ./out Main -in [name of file with equations] -out [name of file where answer will be written] 
```
Test files are present in examples folder:  
```
java -cp ./out Main -in examples/in.txt -out out.txt
```

Format of input data:  
N (number of variables) M (number of equations)  
x (coefficient of the first variable) ... z (coefficient of the last variable) A (result of equation)  
x (coefficient of the first variable) ... z (coefficient of the last variable) A (result of equation)  
...  
x (coefficient of the first variable) ... z (coefficient of the last variable) A (result of equation)  

Example:
```
3 3
2 3 5 10
3 7 4 3
1 2 2 3

equal for

3 variables and 3 equations
2x + 3y + 5z = 10;
3x + 7y + 4z = 3;
1x + 2y + 2z = 3;
```
### Complex numbers
Solver also can read and solve equations with complex numbers.
Complex numbers format:
```
3 3
 1+2i  -1.5-1.1i  2.12   91+5i
-1+3i   1.2+3.5i -3.3    1+15i
 12.31  1.3-5i    12.3i -78.3i

```

## Used technologies
- Gauss-Jordan elimination
- Matrixes
- Design patterns: Singleton
