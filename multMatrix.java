// =================================================================
// File: multMatrix.java
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
// avg time = 33.6 ms
// 2:300*300 matrixes
// =================================================================

public class multMatrix {
	//Use RENS, COLS=3 to test functionality
	private static final int RENS = 300;
	private static final int COLS = 300;
	private int matrix1[], matrix2[], result[];

	public multMatrix(int matrix1[], int matrix2[], int result[]) {
		this.matrix1 = matrix1;
		this.matrix2 = matrix2;
		this.result = result;
	}

	public void calculate() {
		int acum;

		for (int i = 0; i < RENS; i++) {
			for (int j = 0; j < COLS; j++) {
				acum = 0;
				for (int k=0;k<RENS;k++){
					acum += matrix1[(i*COLS)+k]*matrix2[(k*RENS) +j];
				}
				result[(i*COLS)+j]+=acum;
			}
		}
	}

	public static void main(String args[]) {
		long startTime, stopTime;
		int i, j;
		double ms;

		int m1[] = new int[RENS * COLS];
		int m2[] = new int[RENS * COLS];
		int r[] = new int[RENS * COLS];

		for ( i = 0; i < RENS; i++) {
			for ( j = 0; j < COLS; j++) {
				m1[(i * COLS) + j] = (j + 1);
				m2[(i * COLS) + j] = (j + 2);
			}
		}

		/* Uncomment this block to check functionality

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
		System.out.printf("Starting...\n");
		ms = 0;
		multMatrix e = new multMatrix(m1, m2, r);
		for ( i = 0; i < Utils.N; i++) {
			startTime = System.currentTimeMillis();

			e.calculate();

			stopTime = System.currentTimeMillis();

			ms += (stopTime - startTime);
		}

		/* //Uncomment this block to test functionality

		for (i = 0; i < RENS; i++) {
			for (j = 0; j < COLS; j++) {
				System.out.printf("%d, ", r[(i*COLS)+j]/Utils.N);
			}
			System.out.printf("\n" );
		}
		*/
		System.out.printf("avg time = %.5f ms\n", (ms / Utils.N));
	}
}
