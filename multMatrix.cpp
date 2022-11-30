// =================================================================
// File: multMatrix.cpp
// Author: David Alejandro Polo Rivero
// Description: This file implements matrix multiplication 
//                      of the same dimension.
//				The time this implementation takes will be used as the basis
//				to calculate the improvement obtained with parallel technologies.
//
// Copyright (c) 2022 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// =================================================================

#include <iostream>
#include <iomanip>
#include <climits>
#include "utils.h"

const int RENS = 300;//00; //1e5
const int COLS = 300;//00;

using namespace std;

class multMatrix {
private:
	int *M1, *M2, *R;

public:
	multMatrix(int *matrix1, int *matrix2, int *result) : M1(matrix1), M2(matrix2), R(result) {}

	void calculate() {
		int i, j,k, acum;

		for (i = 0; i < RENS; i++) {
			for (j = 0; j < COLS; j++) {
				acum = 0;
				for (k = 0; k < RENS; k++) {
					acum += (M1[(i * COLS) + k] * M2[(k*RENS) +j]);
				}
				R[(i*COLS)+j]+= acum;
			}
		}
	}
};

int main(int argc, char* argv[]) {
	int i, j, *matrix1, *matrix2, *result;
	double ms;

	matrix1 = new int [RENS * COLS];
	matrix2 = new int [RENS * COLS];
	result = new int [RENS * COLS];

	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			matrix1[(i * COLS) + j] = (j + 1);
			matrix2[(i * COLS) + j] = (j + 2);
		}
	}

	cout << "Starting..." << endl;
	ms = 0;
	multMatrix obj(matrix1, matrix2, result);
	for (i = 0; i < N; i++) {
		start_timer();

		obj.calculate();

		ms += stop_timer();
	}
	//display_array("c:", c);
	cout << "avg time = " << setprecision(15) << (ms / N) << " ms" << endl;

	delete [] matrix1;
	delete [] matrix2;
	delete [] result;
	return 0;
}
