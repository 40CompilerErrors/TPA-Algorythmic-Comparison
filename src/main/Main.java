package main;

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
				L[cont] = Liz[cont];
				i++;
			} else {
				L[cont] = Lde[cont];
				j++;
			}
		}
		return start - Runtime.getRuntime().freeMemory();
	}

	public static void main(String[] args) {
		int arraySize = 5000;

		for (; arraySize <= 15000; arraySize += 2500) {
			int iteracion = 100;
			double Bintime = 0;
			double mershtime = 0;
			double quicktime = 0;
			double Sectime = 0;
			int[] quick;
			int[] mershe;
			long memoriaQuick = 0;
			long memoriaMersh = 0;

			int ite = 0;
			for (; ite < iteracion; ite++) {

				quick = new int[arraySize];

				for (int i = 0; i < arraySize; ++i) {
					quick[i] = (int) (Math.random() * (arraySize));
				}
				if (ite == 0) {
					// System.out.print(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
				}
				mershe = quick.clone();

				memoriaQuick += (Runtime.getRuntime().freeMemory());
				quicktime -= System.nanoTime();
				quicksort(quick, 0, quick.length - 1);
				quicktime += System.nanoTime();
				memoriaQuick -= (Runtime.getRuntime().freeMemory());

				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();

				mershtime = -System.nanoTime();
				memoriaMersh += mergesort(mershe, 0, mershe.length - 1);
				mershtime += System.nanoTime();

				for (int i = 0; i < 5; i++)
					Runtime.getRuntime().gc();

				int aBuscar = (int) (Math.random() * (arraySize));

				Bintime -= System.nanoTime();
				BusBin(quick, aBuscar);
				Bintime += System.nanoTime();

				Sectime -= System.nanoTime();
				boolean aux = true;
				int i = 0;
				while (aux == true && i < quick.length) {
					if (quick[i] == aBuscar) {
						aux = false;
					}
					i++;
				}
				Sectime += System.nanoTime();

				for (int j = 0; j < 5; j++)
					Runtime.getRuntime().gc();
			}

			quicktime /= ite;
			mershtime /= ite;
			Bintime /= ite;
			Sectime /= ite;
			memoriaQuick /= ite;
			memoriaMersh /= ite;

			System.out.println("Para tamaño " + arraySize + ":");
			System.out.println("\tQUICK Memoria " + memoriaQuick);
			System.out.println("\tMERSH Memoria " + memoriaMersh);
			System.out.println("\tQUICK " + quicktime + " nsec");
			System.out.println("\tMERSH " + mershtime + " nsec");
			System.out.println("\tBINARY " + Bintime + " nsec");
			System.out.println("\tSecuencial " + Sectime + " nsec");
			for (int i = 0; i < 5; i++)
				Runtime.getRuntime().gc();
		}

	}
}
