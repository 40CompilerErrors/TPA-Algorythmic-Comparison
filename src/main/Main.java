package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import sun.security.util.Cache;

public class Main {
	
	static int busquedaSec(int[] a, int x) {
		boolean aux = true;
		int i = 0;
		while (aux == true && i < a.length) {
			if (a[i] == x) aux = false;
			else i++;
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
			return a.length;
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
		//Abrimos el fichero en el que escribiremos los datos
		fileWriter = new FileWriter("Eficiencia.csv");
		//Iniciamos la cabecera de esta iteracion con un determinado tamaño de array
		final String FILE_HEADER = "Quicksort Memoria,Quicksort nsec,Mergesort Memoria,Mergesort nsc,Sequential Search nsec,Binary Search nsec";
		

		/*
		 * Necesitamos comprobar los siguientes rangos:
		 * 5000, 7500, 10000, 15000
		 * Hemos añadido el rango de 12500 para poder tener una mayor facilidad de implimentación del bucle
		 */
		for (; arraySize <= 15000; arraySize += 2500) {
			
			//Definición de variables que usara todo el tamaño
			int iteracion = 100;
			double binTimeTotal = 0;
			double mergeTimeTotal = 0;
			double quickTimeTotal = 0;
			double secTimeTotal = 0;
			int[] quick;
			int[] mershe;
			long memoriaQuickTotal = 0;
			long memoriaMergeTotal = 0;
			//Creamos la caberecera del documento
			fileWriter.append("Tamaño Array," + arraySize);
			fileWriter.append('\n');
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append('\n');
			
			for (int i = 0; i < 5; i++)
				Runtime.getRuntime().gc();

			int ite = 0;
			for (; ite < iteracion; ite++) {
				
				//Lamamos al recolector de basura ahora para asegurarnos de que las medidas de memoria son correctas luego.
				for (int i = 0; i < 5; i++) Runtime.getRuntime().gc();

				quick = new int[arraySize];
				
				/*
				 * Nos aseguramos como mejor podemos que los numeros generados 
				 * son de distribucion lo mas aleatoria posible, tomando inspiracion de los algoritmos de
				 * hashing.
				 * 
				 */
				for (int i = 0; i < arraySize; ++i) {
					quick[i] = (int) Math.floor((Math.random() * (Integer.MAX_VALUE) % 1299827));
				}
				int aBuscar = quick[(int)Math.floor((Math.random() * arraySize))];
				
				//Clonamos el array, ya que la eficiencia sería poco fiable si ordenasen el mismo
				mershe = quick.clone();
				
				//Llamada al recolector de basura.
				for (int i = 0; i < 5; i++) Runtime.getRuntime().gc();
				
				//Quicksort
				//Inializamos valores locales
				double quickTime = 0 - System.nanoTime();
				long memoriaQuick=quicksort(quick, 0, quick.length - 1);
				quickTime += System.nanoTime();
				//Añadimos al total
				quickTimeTotal += quickTime;
				memoriaQuickTotal += memoriaQuick;
				//Añadimos al CSV
				fileWriter.append(Long.toString(memoriaQuick));
				fileWriter.append(',');
				fileWriter.append(Double.toString(quickTime));
				fileWriter.append(',');
				

				
				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();
			 
				//Mergesort
				//Inializamos valores locales
				double mergeTime = 0 -System.nanoTime();
				long mergeMemory = mergesort(mershe, 0, mershe.length - 1);
				mergeTime += System.nanoTime();
				//Añadimos al total
				mergeTimeTotal += mergeTime;
				memoriaMergeTotal += mergeMemory;
				//Añadimos al CSV
				fileWriter.append(Long.toString(mergeMemory));
				fileWriter.append(',');
				fileWriter.append(Double.toString(mergeTime));
				fileWriter.append(',');
						
				//Nos aseguramos que el recolector de basura no supone un problema 
				for (int i = 0; i < 5; i++) Runtime.getRuntime().gc();
				
				/*
				 * Durante testeo, nos dimos cuenta que el orden en el que llamabamos a las busquedas era de mucha
				 * importancia en lo que a su tiempo de ejecución se refiere. Nuestra conclusión es que esto se 
				 * debe a la caché, por lo que hemos decidido alternarlas, ejecutando una primero en las iteraciones
				 * pares, y la otra en las iteraciones impares.
				 * 
				 */
				double secTime = 0, binTime = 0;
				int lugarS = 0,lugarB = 0;
				if(ite % 2 == 0) {
					//SequentialSearch
					secTime -= System.nanoTime();
					lugarS = busquedaSec(quick,aBuscar);
					secTime += System.nanoTime();
					//Nos aseguramos que el recolector de basura no supone un problema 
					for (int i = 0; i < 5; i++) Runtime.getRuntime().gc();
					//Binary Search
					binTime -= System.nanoTime();
					lugarB = BusBin(mershe, aBuscar);
					binTime += System.nanoTime();
				}
				else {
					//Binary Search
					binTime -= System.nanoTime();
					lugarB = BusBin(mershe, aBuscar);
					binTime += System.nanoTime();
					//Nos aseguramos que el recolector de basura no supone un problema 
					for (int i = 0; i < 5; i++) Runtime.getRuntime().gc();
					//SequentialSearch
					secTime -= System.nanoTime();
					lugarS = busquedaSec(quick,aBuscar);
					secTime += System.nanoTime();		
				}
			
				//Comprobamos si el indice que devuelve es correcto, y si llevan al mismo valor
				//Cabe mencionar que si hay valores repetidos, el indice puede no ser el mismo pero el valor si
				if(aBuscar != quick[lugarS]) System.out.println(" lugarS NO DEVUELVE EL INDICE CORRECTO");
				if(aBuscar != quick[lugarB]) System.out.println(" lugarB NO DEVUELVE EL INDICE CORRECTO");
				if(quick[lugarS] != quick[lugarB]) System.out.println("ERROR! Busquedas no devuelven el mismo valor ");
				//Add to total
				secTimeTotal += secTime;
				binTimeTotal += binTime;
				//Append to CSV
				fileWriter.append(Double.toString(secTime));
				fileWriter.append(',');
				fileWriter.append(Double.toString(binTime));
				fileWriter.append(',');	
		
				//End the line for the CSV
				fileWriter.append('\n');
				
			}
			//Imprimimos las medias en CSV utilizando el sumatorio de Excel
			int itNum = (arraySize-5000)/2500;
			char[] columns = {'A','B','C','D','E','F'};
			fileWriter.append("Medias:\n");
			for(char c :columns) fileWriter.append("=SUMA("+ c + (3+ 5*itNum + iteracion*itNum) + 
												":" + c + (2+ 5*itNum + iteracion  +iteracion*itNum)+ ")/" + ite +",");	
			
			//Dejamos un espacio entre iteraciones
			fileWriter.append('\n');
			fileWriter.append('\n');

			//Preparamos las medias para imprimirlas por pantalla 
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
			//Una ultima llamada al recolector de basura.
			for (int i = 0; i < 5; i++) Runtime.getRuntime().gc();
		}
		//Cerramos el documento
        fileWriter.flush();
        fileWriter.close();

	}
}
