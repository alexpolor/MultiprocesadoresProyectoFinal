// =================================================================
// File: multMatrix.cu
// Author: David Alejandro Polo Rivero
// Description: This file implements matrix multiplication 
//                      of the same dimension with CUDA.
//				The time this implementation takes will be used as the basis
//				to calculate the improvement obtained with parallel technologies.
//
// Copyright (c) 2022 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//
// avg time = 0.003 ms
// 2:300*300 matrixes
// =================================================================

#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <cuda_runtime.h>
#include "utils.h"

#define RENS    300//00
#define COLS    300//00
#define THREADS 256
#define BLOCKS	MMIN(32, (((RENS * COLS) / THREADS) + 1))

__global__ void matrix_vector(int *matrix1, int *matrix2, int *result) {
	int tid = threadIdx.x + (blockIdx.x * blockDim.x);
  int j,k, acum = 0;

  while (tid < RENS){
		for (j = 0; j < COLS; j++) {
			acum=0;
			for (k=0;k<RENS;k++){
				acum+=matrix1[(tid*COLS)+k]*matrix2[(k*RENS) +j];
			}
	    result[(tid*COLS)+j]=acum;
		}
		tid += blockDim.x * gridDim.x;
  }


}

int main(int argc, char* argv[]) {
	int i, j, *matrix1, *matrix2, *result;
  int *d_m1, *d_m2, *d_r;
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

  cudaMalloc((void**)&d_m1, sizeof(int) * RENS* COLS);
  cudaMalloc((void**)&d_m2, sizeof(int) * RENS* COLS);
  cudaMalloc((void**)&d_r, sizeof(int) * RENS* COLS);

  cudaMemcpy(d_m1, matrix1, sizeof(int) * RENS* COLS, cudaMemcpyHostToDevice);
  cudaMemcpy(d_m2, matrix2, sizeof(int) * RENS* COLS, cudaMemcpyHostToDevice);

	printf("Starting...\n");
	ms = 0;
	for (i = 0; i < N; i++) {
		start_timer();

		matrix_vector<<<BLOCKS, THREADS>>>(d_m1, d_m2, d_r);

		ms += stop_timer();
	}

  cudaMemcpy(result, d_r, sizeof(int) * RENS*COLS, cudaMemcpyDeviceToHost);
/* //print the results matrix
	for (i = 0; i < RENS; i++) {
		for (j = 0; j < COLS; j++) {
			printf("%d, ", result[(i*COLS)+j]);
		}
		printf("\n" );
	}*/
	printf("avg time = %.5lf ms\n", (ms / N));

  cudaFree(d_m1); cudaFree(d_m2); cudaFree(d_r);
	free(matrix1); free(matrix2); free(result);
	return 0;
}
