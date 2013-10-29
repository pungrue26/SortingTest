import java.io.*;
import java.util.*;

public class SortingTest {
	static int [] tmp = null;
	
	static int numsize = -1;
	static int rminimum = -1;
	static int rmaximum = -1;
	
	public static void main(String args[]) throws IOException {
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		InputStream is = new FileInputStream("/Users/TonyWannaBe/Dropbox/workspace/SortingTest/bin/RegularInput.txt");
		InputStream is = new FileInputStream("/Users/TonyWannaBe/Dropbox/workspace/SortingTest/bin/RandomInput.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		
		try {
			boolean isRandom = false;
			int[] value;
			String nums = br.readLine();
			if (nums.charAt(0) == 'r') {
				isRandom = true;

				String[] nums_arg = nums.split(" ");

				numsize = Integer.parseInt(nums_arg[1]);
				rminimum = Integer.parseInt(nums_arg[2]);
				rmaximum = Integer.parseInt(nums_arg[3]);

				Random rand = new Random();

				value = new int[numsize];
				for (int i = 0; i < value.length; i++) {
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
				}
			}
			else {
				int numsize = Integer.parseInt(nums);
				value = new int[numsize];
				for (int i = 0; i < value.length; i++)
					value[i] = Integer.parseInt(br.readLine());
			}

			while (true) {
				int[] newvalue = (int[])value.clone();

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0)) {
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'S':	// Selection Sort
						newvalue = DoSelectionSort(newvalue);
						break;
					case 'X':
						return;
					default:
						throw new IOException("IOException occured.");
				}
				if (isRandom) {
					long elapsed = System.currentTimeMillis() - t;
					if(tmp == null) {
						tmp = newvalue;
					} else {
						if(!java.util.Arrays.equals(tmp, newvalue)) {
							System.out.println("Sorting Result is NOT the same.");
							return;
						}
						tmp = newvalue;
					}
					System.out.println(command.charAt(0) + ": " + elapsed + " ms");
				} else {
					for (int i = 0; i < newvalue.length; i++) {
						System.out.println(newvalue[i]);
					}
				}

			}
		} catch (IOException e) {
			System.out.println("IOException : " + e.toString());
		} finally {
			is.close();
		}
	}

	private static int[] DoBubbleSort(int[] value) {
		for(int i = value.length - 1; i >=1; i--) {
			boolean sorted = true;
			for(int j = 0; j < i; j++) {
				if(value[j] > value[j+1]) {
					int tmp = value[j+1];
					value[j+1] = value[j];
					value[j] = tmp;
					sorted = false;
				}
			}
			if(sorted) {
				return value;
			}
		}
		return value;
	}

	private static int[] DoInsertionSort(int[] value) {
		for(int toBeSorted = 1; toBeSorted < value.length; toBeSorted++) {
			int tmpIndex = toBeSorted;
			while(value[tmpIndex - 1] > value[tmpIndex]) {
				int tmpValue = value[tmpIndex];
				value[tmpIndex] = value[tmpIndex - 1];
				value[tmpIndex - 1] = tmpValue;
				
				tmpIndex -= tmpIndex;
				if(tmpIndex == 0)
					break;
			};
		}
		return value;
	}

	private static int[] DoHeapSort(int[] value) {
		// First, build heap.
		// "(value.length - 1) / 2" is the last node that has at least one child..
		for(int i = (value.length - 1) / 2; i >= 0; i--) {
			value = heapify(value, i, value.length - 1);
		}
		
		// Change the root node with the last, and heapify the whole tree again.
		for(int i = value.length - 1; i >= 1; i--) {
			int tmp = value[0];
			value[0] = value[i];
			value[i] = tmp;
			
			heapify(value, 0, i - 1);
		}
		
		// The result so far is reverse ordered array. Let's re-reverse it to ascending order.
		int [] ascendingOrder = new int [value.length];
		for(int i = 0; i < value.length; i++) {
			ascendingOrder[i] = value[value.length - 1 - i];
		}
		return ascendingOrder;
	}

	private static int [] heapify(int[] value, int targetIndex, int lastIndex) {
		// The input targetIndex's two children are roots of heaps(two children and their descendants constitute heaps).
		// In this situation, heapify method makes a combined heap whose root is targetIndex.
		int leftChildIndex = 2 * targetIndex;
		int rightChildIndex = 2 * targetIndex + 1;
		int smallerChildIndex = 0;
		
		if(rightChildIndex <= lastIndex) {
			// Both left and right child exist.
			if(value[leftChildIndex] < value[rightChildIndex]) {
				smallerChildIndex = leftChildIndex;
			} else {
				smallerChildIndex = rightChildIndex;
			}
		} else if(leftChildIndex <= lastIndex) {
			// Only left Child exists.
			smallerChildIndex = leftChildIndex;
		} else {
			// Leaf Node case.
			return value;
		}
		
		if(value[smallerChildIndex] < value[targetIndex]) {
			int tmp =  value[smallerChildIndex];
			value[smallerChildIndex] = value[targetIndex];
			value[targetIndex] = tmp;
			
			heapify(value, smallerChildIndex, lastIndex);
		}
		return value;

	}

	private static int[] DoMergeSort(int[] value) {
		if(value.length == 1) {
			return value;
		}
		
		int halfLenth = value.length / 2;
		int [] firstHalf = new int [halfLenth];
		for(int i = 0; i < halfLenth; i++) {
			firstHalf[i] = value[i];
		}
		int [] secondHalf = new int [value.length - halfLenth];
		for(int i = 0, j = halfLenth; j < value.length; i++, j++) {
			secondHalf[i] = value[j];
		}
		
		int [] sortedFirstHalf = DoMergeSort(firstHalf);
		int [] sortedSecondHalf = DoMergeSort(secondHalf);
		
		int sortedFirstHalfIndex = 0;
		int sortedSecondHalfIndex = 0;
		int returnArrayIndex = 0;
		int returnArray [] = new int [value.length];
		while(sortedFirstHalfIndex < sortedFirstHalf.length && sortedSecondHalfIndex < sortedSecondHalf.length) {
			if(sortedFirstHalf[sortedFirstHalfIndex] > sortedSecondHalf[sortedSecondHalfIndex]) {
				returnArray[returnArrayIndex++] = sortedSecondHalf[sortedSecondHalfIndex++];
			} else {
				returnArray[returnArrayIndex++] = sortedFirstHalf[sortedFirstHalfIndex++];
			}
		}
		while(sortedFirstHalfIndex < sortedFirstHalf.length) {
			returnArray[returnArrayIndex++] = sortedFirstHalf[sortedFirstHalfIndex++];
		}
		while(sortedSecondHalfIndex < sortedSecondHalf.length) {
			returnArray[returnArrayIndex++] = sortedSecondHalf[sortedSecondHalfIndex++];
		}
		
		return returnArray;
	}

	private static int[] DoQuickSort(int[] value) {
		if(value.length == 1)  {
			return value;
		}
		
		int lastIndex = value.length - 1;
		// Elements that placed left of "standardPos" are less than "value[lastIndex]" which is the standard.
		// And the standard element, which "value[lastIndex]" will be placed at standardPos index after reordering.
		int standardPos = 0;
		int unchecked = 0;
		while(unchecked < lastIndex) {
			if(value[unchecked] <= value[lastIndex]) {
				int tmp = value[unchecked];
				value[unchecked] = value[standardPos];
				value[standardPos] = tmp;
				standardPos++;
			} else {
				//do nothing.
			}
			unchecked++;
		}
		// Place the standard element at standardPos index, and right shift all elements that were there before.
		int tmp = value[lastIndex];
		for(int i = lastIndex; i > standardPos; i--) {
			value[i] = value[i-1];
		}
		value[standardPos] = tmp;
		// if value.length is 2, the whole array is sorted so return here.
		if(value.length == 2) {
			return value;
		}

		int [] leftArray = null;
		int [] rightArray = null;
		if(standardPos == 0) {
			rightArray = new int [value.length - 1];
			for(int i = 0, j = 1; j < value.length; i++, j++) {
				rightArray[i] = value[j];
			}
			DoQuickSort(rightArray);
		} else if(standardPos == lastIndex) {
			leftArray = new int [value.length - 1];
			for(int i = 0; i < value.length - 1; i++) {
				leftArray[i] = value[i];
			}
			DoQuickSort(leftArray);
		} else {
			leftArray = new int [standardPos];
			rightArray = new int [value.length - standardPos - 1];
			for(int i = 0; i < standardPos; i++) {
				leftArray[i] = value[i];
			}
			for(int i = 0, j = standardPos + 1; j < value.length; i++, j++) {
				rightArray[i] = value[j];
			}
			
			DoQuickSort(leftArray);
			DoQuickSort(rightArray);
		}
		
		int leftArrayLength = 0;
		if(leftArray != null) {
			leftArrayLength = leftArray.length;
			for(int i = 0; i < leftArray.length; i++) {
				value[i] = leftArray[i];
			}
		}
		if(rightArray != null) {
			for(int i = leftArrayLength + 1, j = 0; j < rightArray.length; i++, j++) {
				value[i] = rightArray[j];
			}	
		}
		
		return value;
	}

	private static int[] DoRadixSort(int[] value) {
		if(rminimum != -1 && rminimum < 0)
			throw new IllegalArgumentException("Can't perform Radix Sort");
		
		int [][] bucketsA = new int [10][value.length];
		int [][] bucketsB = new int [10][value.length];
		int [] indexesA = new int [10];
		int [] indexesB = new int [10];
		
		int maxDigits = 0;
		int tmpRMaximum = rmaximum;
		while(tmpRMaximum >= 10) {
			tmpRMaximum = tmpRMaximum / 10;
			maxDigits++;
		}
		
		for(int i = 0; i < value.length; i++) {
			int num = value[i] % 10;
			bucketsA[num][indexesA[num]] = value[i];
			indexesA[num]++;				
		}
		
		int exp = 10;
		boolean flag = true;
		for(int i = 0; i < maxDigits - 1; i++) {
			if(flag) {
				for(int j = 0; j < 10; j++) {
					for(int k = 0; k <indexesA[j]; k++) {
						int num = (bucketsA[j][k] / exp) % 10;
						bucketsB[num][indexesB[num]] = bucketsA[j][k];
						indexesB[num]++;	
					}
				}
//				printMultiDimenArray(bucketsB, indexesB);
				// initialize bucketsA, indexesA.
				bucketsA = new int [10][value.length];
				indexesA = new int [10];
				flag = false;
			} else {
				for(int j = 0; j < 10; j++) {
					for(int k = 0; k <indexesB[j]; k++) {
						int num = (bucketsB[j][k] / exp) % 10;
						bucketsA[num][indexesA[num]] = bucketsB[j][k];
						indexesA[num]++;	
					}
				}
//				printMultiDimenArray(bucketsA, indexesA);
				// initialize bucketsB, indexesB.
				bucketsB = new int [10][value.length];
				indexesB = new int [10];
				flag = true;
			}
			exp *= 10;
		}

		int accumulIndex = 0;
		if(flag) {
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j <indexesA[i]; j++) {
					value[accumulIndex++] = bucketsA[i][j];
				}
			}
		} else {
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j <indexesB[i]; j++) {
					value[accumulIndex++] = bucketsB[i][j];
				}
			}
		}
		
		return value;
	}

	private static void printMultiDimenArray(int[][] bucketsB, int[] indexes) {
		for(int i = 0; i < 10; i++) {
			System.out.print("queue[" + i + "] : ");
			for(int j = 0; j < indexes[i]; j++) {
				System.out.print(bucketsB[i][j] + ", ");
			}
			System.out.println("");
		}
		System.out.println("----------------------------");
	}

	private static int[] DoSelectionSort(int[] value) {
		for(int i = value.length - 1; i >= 1; i--) {
			// find the index of the biggest value in array from 0 to i 
			int biggestIndex = findBiggest(value, 0, i);
			
			int tmp = value[i];
			value[i] = value[biggestIndex];
			value[biggestIndex] = tmp;
		}
		return value;
	}

	private static int findBiggest(int[] value, int startIndex, int endIndex) {
		int biggestIndex = startIndex;
		for(int i = 1; i <= endIndex; i++) {
			if(value[i] > value[biggestIndex])
				biggestIndex = i;
		}
		return biggestIndex;
	}
}