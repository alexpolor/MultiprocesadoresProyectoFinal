// =================================================================
// File: multMatrix.c
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
//avg time = 124.795 ms
// 300*300 matrixes
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include "utils.h"

#define RENS 300//00
#define COLS 300//00

void multMatrix(int *matrix1, int *matrix2, int *result) {
	int i, j, k, acum;

	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			acum=0;
			for (k=0;k<RENS;k++){
				acum+=matrix1[(i*COLS)+k]*matrix2[(k*RENS) +j];
			}
		//	printf("acum en %d, %d = %d\n",i,j,acum );
		result[(i*COLS)+j]+=acum;
		}
	}
}

int main(int argc, char* argv[]) {
	int i, j, *matrix1, *matrix2, *result;
	double ms;

	matrix1 = (int*) malloc(sizeof(int) * RENS * COLS);
	matrix2 = (int*) malloc(sizeof(int) * RENS * COLS);
	result = (int*) malloc(sizeof(int) * RENS * COLS);

	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			matrix1[(i * COLS) + j] = (j + 1);
			matrix2[(i * COLS) + j] = (j + 2);
		}
	}
/* printf("\nMatrix1\n");	
	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			printf("%d, ", matrix1[(i*COLS)+j]);
		}
		printf("\n" );
	}
	printf("\nMatrix2\n");
	for (i = 0; i < RENS; i++) {
			for (j = 0; j < COLS; j++) {
				printf("%d, ", matrix2[(i*COLS)+j]);
			}
			printf("\n");
		}
*/
	printf("Starting...\n");
	ms = 0;
	for (i = 0; i < N; i++) {
		start_timer();

		multMatrix(matrix1, matrix2, result);

		ms += stop_timer();
	}
	/* //Uncomment this block to test functionality

	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			printf("%d, ", result[(i*COLS)+j]/N);
		}
		printf("\n" );
	}
	*/
	printf("avg time = %.5lf ms\n", (ms / N));

	free(matrix1); free(matrix2); free(result);
	return 0;
}
