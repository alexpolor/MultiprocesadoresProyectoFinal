// =================================================================
// File: multMatrix_forkjoin.java
// Author: David Alejandro Polo Rivero
// Description: This file implements matrix multiplication 
//                      of the same dimension.
//				The time this implementation takes will be used as the basis
//				to calculate the improvement obtained with parallel technologies.
//
// Copyright (c) 2022 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
//avg time = 18.5 ms
// 2 300*300 matrixes
// =================================================================
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;

public class multMatrix_forkjoin extends RecursiveAction {
	private static final int RENS = 300;
	private static final int COLS = 300;
	private static final int MIN = 100;
	private int matrix1[], matrix2[], result[], start, end;

		public multMatrix_forkjoin(int matrix1[], int matrix2[], int result[], int start, int end) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.result = result;
		this.start = start;
		this.end = end;
	}

	public void computeDirectly() {
		int acum;

		for (int i = start; i < end; i++) {
			for (int j = 0; j < COLS; j++) {
				acum = 0;
				for (int k=0;k<RENS;k++){
					acum += matrix1[(i*COLS)+k]*matrix2[(k*RENS) +j];
				}
				result[(i*COLS)+j]=acum;
			}
		}
	}

	@Override
	protected void compute() {
		if ( (end - start) <= MIN ) {
			computeDirectly();
		} else {
			int mid = start + ((end - start) / 2);
			invokeAll(new multMatrix_forkjoin(matrix1, matrix2, result, start, mid),
					  new multMatrix_forkjoin(matrix1, matrix2, result, mid, end));
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		double ms;
		ForkJoinPool pool;
		int i,j;

		int m1[] = new int[RENS * COLS];
		int m2[] = new int[RENS * COLS];
		int r[]  = new int[RENS * COLS];

		for ( i = 0; i < RENS; i++) {
			for ( j = 0; j < COLS; j++) {
				m1[(i * COLS) + j] = (j + 1);
				m2[(i * COLS) + j] = (j + 2);
			}
		}/*
		System.out.printf("\nMatrix1\n");
		for (i = 0; i < RENS; i++) {
				for (j = 0; j < COLS; j++) {
					System.out.printf("%d, ", m1[(i*COLS)+j]);
				}
				System.out.printf("\n" );
			}

			System.out.printf("\nMatrix2\n");
			for (i = 0; i < RENS; i++) {
					for (j = 0; j < COLS; j++) {
						System.out.printf("%d, ", m2[(i*COLS)+j]);
					}
					System.out.printf("\n");
				}
*/
		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for ( i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			pool = new ForkJoinPool(Utils.MAXTHREADS);
			pool.invoke(new multMatrix_forkjoin(m1, m2, r, 0, RENS));

			stopTime = System.currentTimeMillis();
			ms += (stopTime - startTime);
		}
		//Utils.displayArray("c", c);
	/*	for (i = 0; i < RENS; i++) {
			for (j = 0; j < COLS; j++) {
				System.out.printf("%d, ", r[(i*COLS)+j]);
			}
			System.out.printf("\n" );
		}
		*/System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}
