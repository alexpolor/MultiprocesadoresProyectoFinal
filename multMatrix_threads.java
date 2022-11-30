// =================================================================
// File: multMatrix_threads.java
// Author: David Alejandro Polo Rivero
// Description: This file implements matrix multiplication 
//                      of the same dimension.
//				The time this implementation takes will be used as the basis
//				to calculate the improvement obtained with parallel technologies.
//
// Copyright (c) 2022 by Tecnologico de Monterrey.
// All Rights Reserved. May be reproduced for any non-commercial
// purpose.
// avg time = 22.9 ms
// 2 300*300 matrixes
// =================================================================

public class multMatrix_threads extends Thread {
	private static final int RENS = 300;
	private static final int COLS = 300;
	private int matrix1[], matrix2[], result[], start, end;

	public multMatrix_threads(int matrix1[], int matrix2[], int result[], int start, int end) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.result = result;
		this.start = start;
		this.end = end;
	}

	public void run() {
		int acum;

		for (int i = start; i < end; i++) {
			for (int j = 0; j < COLS; j++) {
				acum = 0;
				for (int k=0;k<RENS;k++){
					acum += matrix1[(i*COLS)+k]*matrix2[(k*RENS) +j];
				}
					//System.out.printf("%d\n",acum );
				result[(i*COLS)+j]=acum;
			}
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		double ms;
		int block;
		int i, j;
		multMatrix_threads threads[];

		int m1[] = new int[RENS * COLS];
		int m2[] = new int[RENS * COLS];
		int r[] = new int[RENS * COLS];

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
		block = RENS / Utils.MAXTHREADS;
		threads = new multMatrix_threads[Utils.MAXTHREADS];

		System.out.printf("Starting with %d threads...\n", Utils.MAXTHREADS);
		ms = 0;
		for ( j = 1; j <= Utils.N; j++) {
			for ( i = 0; i < threads.length; i++) {
				if (i != threads.length - 1) {
					threads[i] = new multMatrix_threads(m1, m2, r, (i * block), ((i + 1) * block));
				} else {
					threads[i] = new multMatrix_threads(m1, m2, r, (i * block), RENS);
				}
			}

			startTime = System.currentTimeMillis();
			for ( i = 0; i < threads.length; i++) {
				threads[i].start();
			}
			for ( i = 0; i < threads.length; i++) {
				try {
					threads[i].join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			stopTime = System.currentTimeMillis();
			ms +=  (stopTime - startTime);
		}/*
		for (i = 0; i < RENS; i++) {
			for (j = 0; j < COLS; j++) {
				System.out.printf("%d, ", r[(i*COLS)+j]);
			}
			System.out.printf("\n" );
		}*/
		System.out.printf("avg time = %.5f\n", (ms / Utils.N));
	}
}
