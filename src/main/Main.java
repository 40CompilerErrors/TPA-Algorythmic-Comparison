package main;

import java.io.FileWriter;
import java.io.IOException;

import sun.security.util.Cache;

public class Main {
	
	static int busquedaSec(int[] a, int x) {
		boolean aux = true;
		int i = 0;
		while (aux == true && i < a.length) {
			if (a[i] == x) {
				aux = false;
			}
			i++;
		}
		return i;
	}
	
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

	static long quicksort(int[] L,int ini, int fin){
		if(ini<fin){
			long x[]=pivotear(L,ini,fin);
			return Math.abs(x[1])+quicksort(L,ini,(int)x[0]-1)+
			quicksort(L,(int)x[0]+1,fin);
		} else {
			return 0;
		}
	}
	
	static long[] pivotear(int[] L,int ini, int fin){
		long start=(Runtime.getRuntime().freeMemory());
		int i=ini;
		int p=L[ini];
		for(int j=ini+1;j<=fin;++j){
			if(L[j]<=p){
				i++;
				if(i!=j){
					int aux=L[i];
					L[i]=L[j];
					L[j]=aux;
				}
			} 
		} 
		int aux=L[ini];
		L[ini]=L[i];
		L[i]=aux;
		start-=(Runtime.getRuntime().freeMemory());
		
		return new long[]{i,start};
	}
/*
 * Tanto en el mergesort como en el merge se tenia problemas con el paso del recolector de basura
 * para disminuir este error hemos hecho la medicion desde dentro del metodo y que sume para posteriormente
 * devolverla, al realizar la resta podian seguir saliendo valores negativos por lo tanto decidimos sacar valores
 * absolutos para ver la diferencia ya que un 0 o un numero negativo podrian atrofiar mucho mas la media,
 * al ser comparado con el quicksort decidimos realizar lo mismo dentro de su metodo para que tengan las mismas operaciones
 * adicionales
 */
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
		for (int cont = ini; cont <= fin; cont++) {
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
			
			fileWriter.append("Tama�o Array," + arraySize);
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
					quick[i] = (int) (Math.random() * (10000000));
				}
				int aBuscar = (int) (Math.random() * (10000000));
				
				
				mershe = quick.clone();
				
				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();
				
				//Quicksort
				//Initialize locals
				
				double quickTime = 0 - System.nanoTime();
				long memoriaQuick=quicksort(quick, 0, quick.length - 1);
				quickTime += System.nanoTime();
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

				
				
				//Sequential Search
				double secTime = 0 - System.nanoTime();
				int lugar=busquedaSec(quick,aBuscar);
				secTime += System.nanoTime();
				//System.out.println(aBuscar+" "+lugar+" "+secTime);
				//Add to total
				secTimeTotal += secTime;
				
				//Append to CSV
				fileWriter.append(Double.toString(secTime));
				fileWriter.append(',');
				
				//Binary Search
				
				double binTime = 0 - System.nanoTime();
				BusBin(mershe, aBuscar);
				binTime += System.nanoTime();
				//Add to total
				binTimeTotal += binTime;
				//Append to CSV
				fileWriter.append(Double.toString(binTime));
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

			System.out.println("Para tamaño " + arraySize + ":");
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
