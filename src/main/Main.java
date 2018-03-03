package main;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
	
	static int BusBin(int[] a, int x) {
		if (a.length == 0 || a[0] > x || a[a.length - 1] < x) {
			return 0;
		} else {
			return BusBinDV(a, 0, a.length - 1, x);
		}
	}

	static int BusBinDV(int[] a, int ini, int fin, int x) {
		if (ini > fin) {
			return 0;
		} else {
			int k = (ini + fin) / 2;
			if (x == a[k]) {
				return k;
			} else {
				if (x < a[k]) {
					return BusBinDV(a, ini, k - 1, x);
				} else {
					return BusBinDV(a, k + 1, fin, x);
				}
			}
		}
	}

	static void quicksort(int[] L, int ini, int fin) {
		if (ini < fin) {
			int x = pivotear(L, ini, fin);
			quicksort(L, ini, x - 1);
			quicksort(L, x + 1, fin);
		}
	}

	static int pivotear(int[] L, int ini, int fin) {
		int i = ini;
		int p = L[ini];
		for (int j = ini + 1; j <= fin; ++j) {
			if (L[j] <= p) {
				i++;
				if (i != j) {
					int aux = L[i];
					L[i] = L[j];
					L[j] = aux;
				}
			}
		}
		int aux = L[ini];
		L[ini] = L[i];
		L[i] = aux;

		return i;
	}

	static long mergesort(int[] L, int ini, int fin) {

		if (ini < fin) {
			int med = (ini + fin) / 2;
			long start = Runtime.getRuntime().freeMemory();
			int[] Liz = new int[L.length + 1];
			int[] Lde = new int[L.length + 1];
			for (int i = 0; i <= med; i++) {
				Liz[i] = L[i];
			}
			for (int i = med + 1; i <= fin; i++) {
				Lde[i] = L[i];
			}
			start -= Runtime.getRuntime().freeMemory();
			start = Math.abs(start);

			return start + mergesort(Liz, ini, med) + mergesort(Lde, med + 1, fin) + merge(Liz, Lde, L, ini, fin, med);

		}
		return 0;
	}

	static public long merge(int[] Liz, int[] Lde, int[] L, int ini, int fin, int med) {
		long start = Runtime.getRuntime().freeMemory();
		Liz[med + 1] = Integer.MAX_VALUE;
		Lde[fin + 1] = Integer.MAX_VALUE;
		int i = ini;
		int j = med + 1;
		for (int cont = ini; i <= fin; i++) {
			if (Liz[i] < Lde[j]) {
				L[cont] = Liz[i];
				i++;
			} else {
				L[cont] = Lde[j];
				j++;
			}
		}
		return start - Runtime.getRuntime().freeMemory();
	}

	public static void main(String[] args) throws IOException {
		int arraySize = 5000;
		
		FileWriter fileWriter = null;
		fileWriter = new FileWriter("Eficiencia.csv");
		
		final String FILE_HEADER = "Quicksort Memoria,Quicksort nsec,Mergesort Memoria,Mergesort nsc,Binary Search nsec,Sequential Search nsec";
		

		for (; arraySize <= 15000; arraySize += 2500) {
			
			int iteracion = 100;
			double binTimeTotal = 0;
			double mergeTimeTotal = 0;
			double quickTimeTotal = 0;
			double secTimeTotal = 0;
			int[] quick;
			int[] mershe;
			long memoriaQuickTotal = 0;
			long memoriaMergeTotal = 0;
			
			fileWriter.append("Tamaño Array," + arraySize);
			fileWriter.append('\n');
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append('\n');
			
			for (int i = 0; i < 5; i++)
				Runtime.getRuntime().gc();

			int ite = 0;
			for (; ite < iteracion; ite++) {
				
				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();

				quick = new int[arraySize];
				
				for (int i = 0; i < arraySize; ++i) {
					quick[i] = (int) (Math.random() * (arraySize));
				}
				if (ite == 0) {
					// System.out.print(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
				}
				mershe = quick.clone();
				
				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();
				
				//Quicksort
				//Initialize locals
				long memoriaQuick = (Runtime.getRuntime().freeMemory());
				double quickTime = 0 - System.nanoTime();
				quicksort(quick, 0, quick.length - 1);
				quickTime += System.nanoTime();
				memoriaQuick -= (Runtime.getRuntime().freeMemory());
				//Add to totals
				quickTimeTotal += quickTime;
				memoriaQuickTotal += memoriaQuick;
				//Add to CSV
				fileWriter.append(Long.toString(memoriaQuick));
				fileWriter.append(',');
				fileWriter.append(Double.toString(quickTime));
				fileWriter.append(',');

				
				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();
			 
				//Mergesort
				//Initialize locals
				double mergeTime = 0 -System.nanoTime();
				long mergeMemory = mergesort(mershe, 0, mershe.length - 1);
				mergeTime += System.nanoTime();
				//Add to totals
				mergeTimeTotal += mergeTime;
				memoriaMergeTotal += mergeMemory;
				//Add to CSV
				fileWriter.append(Long.toString(mergeMemory));
				fileWriter.append(',');
				fileWriter.append(Double.toString(mergeTime));
				fileWriter.append(',');
						
				
				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();

				//Binary Search
				int aBuscar = (int) (Math.random() * (arraySize));
				double binTime = 0 - System.nanoTime();
				BusBin(quick, aBuscar);
				binTime += System.nanoTime();
				//Add to total
				binTimeTotal += binTime;
				//Append to CSV
				fileWriter.append(Double.toString(binTime));
				fileWriter.append(',');

				//Sequential Search
				double secTime = 0 - System.nanoTime();
				boolean aux = true;
				int i = 0;
				while (aux == true && i < quick.length) {
					if (quick[i] == aBuscar) {
						aux = false;
					}
					i++;
				}
				secTime += System.nanoTime();
				//Add to total
				secTimeTotal += secTime;
				//Append to CSV
				fileWriter.append(Double.toString(secTime));
				fileWriter.append(',');
				
				for (int j = 0; j < 5; j++)
					Runtime.getRuntime().gc();
				
				//End the line for the CSV
				fileWriter.append('\n');
			}
			//Print means to CSV
			int itNum = (arraySize-5000)/2500;
			char[] columns = {'A','B','C','D','E','F'};
			fileWriter.append("Totales:\n");
			for(char c :columns) fileWriter.append("=SUM("+ c + (3+ 5*itNum + iteracion*itNum) + 
												":" + c + (2+ 5*itNum + iteracion  +iteracion*itNum)+ ")/" + ite +",");	
			fileWriter.append('\n');
			fileWriter.append('\n');

			quickTimeTotal /= ite;
			mergeTimeTotal /= ite;
			binTimeTotal /= ite;
			secTimeTotal /= ite;
			memoriaQuickTotal /= ite;
			memoriaMergeTotal /= ite;

			System.out.println("Para tamaÃ±o " + arraySize + ":");
			System.out.println("\tQUICK Memoria " + memoriaQuickTotal);
			System.out.println("\tMERSH Memoria " + memoriaMergeTotal);
			System.out.println("\tQUICK " + quickTimeTotal + " nsec");
			System.out.println("\tMERSH " + mergeTimeTotal + " nsec");
			System.out.println("\tBINARY " + binTimeTotal + " nsec");
			System.out.println("\tSecuencial " + secTimeTotal + " nsec");
			for (int i = 0; i < 5; i++)
				Runtime.getRuntime().gc();
		}
		
        fileWriter.flush();
        fileWriter.close();


	}
}
