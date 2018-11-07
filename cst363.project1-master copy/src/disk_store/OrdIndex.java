package disk_store;

import java.util.ArrayList;
import java.util.List;

/**
 * An ordered index.  Duplicate search key values are allowed,
 * but not duplicate index table entries.  In DB terminology, a
 * search key is not a superkey.
 * 
 * A limitation of this class is that only single integer search
 * keys are supported.
 *
 */

public class OrdIndex implements DBIndex {

	public static void main(String[] args)
	{
		ArrayList<Integer> kyBlcCnt = new ArrayList<>();
		ArrayList <ArrayList<Integer>> OrdInd =  new ArrayList <ArrayList<Integer>>();

		ArrayList<Integer> arr1 = new ArrayList<>();
		arr1.add(1);
		arr1.add(0);
		arr1.add(1);
		OrdInd.add(arr1);
		ArrayList<Integer> arr2 = new ArrayList<>();
		arr2.add(2);
		arr2.add(0);
		arr2.add(1);
		OrdInd.add(arr2);
		OrdInd.add(arr2);
		OrdInd.add(arr2);
		ArrayList<Integer> arr3 = new ArrayList<>();
		arr3.add(3);
		arr3.add(0);
		arr3.add(1);
		OrdInd.add(arr3);
		ArrayList<Integer> arr4 = new ArrayList<>();
		arr4.add(4);
		arr4.add(0);
		arr4.add(1);
		OrdInd.add(arr4);
		OrdInd.add(arr4);
		OrdInd.add(arr4);
		ArrayList<Integer> arr5 = new ArrayList<>();
		arr5.add(5);
		arr5.add(0);
		arr5.add(1);
		OrdInd.add(arr5);
		OrdInd.add(arr5);
		OrdInd.add(arr5);

		System.out.println(OrdInd);

		int size = OrdInd.size();

		ArrayList<Integer> temp = rangeBinSearch(OrdInd, 0, size-1, 4);

		System.out.println(temp);
	}

	private List<Integer> kyBlcCnt;
	private List<List<Integer>> OrdInd;

	private static int binarySearch(ArrayList<ArrayList<Integer>> arr,int start, int end, int target)
	{
		if(end>=start)
		{
			int mid = start+(end-start)/2;
			if(arr.get(mid).get(0)>target)
			{
				return binarySearch(arr, start, mid-1, target);
			}
			else if(arr.get(mid).get(0)<target)
			{
				return binarySearch(arr, mid+1, end, target);
			}
			else
			{
				return mid;
			}
		}
		else
			return -1;

	}
	private static ArrayList<Integer> rangeBinSearch(ArrayList<ArrayList<Integer>> arr,int start, int end, int target)
	{
		int ind = binarySearch(arr, start, end, target);

		ArrayList<Integer> indList = new ArrayList<Integer>();
		boolean isPrev=true, isNext=true;
		int i=1;

		if(ind==-1)
		{
			return indList;
		}

		while(isPrev||isNext)
		{
			if(ind-i<0)
			{
				indList.add(ind);
				isPrev=false;
			}
			else if(ind+i>=arr.size())
			{
				isNext=false;
			}
			if(ind-i>=0&&isPrev)
			{
				if(arr.get(ind-i).get(0)==target)
				{
					indList.add(ind-i);
					i++;
				}
				else
				{
					indList.add(ind);
					isPrev=false;
					i=1;
				}
			}
			else if(ind+i<arr.size()&&isNext)
			{
				if(arr.get(ind+i).get(0)==target)
				{
					indList.add(ind+i);
					i++;
				}
				else
				{
					isNext = false;
				}
			}
		}

		return indList;
	}

	/**
	 * Create an new ordered index.
	 */
	public OrdIndex()
	{
		kyBlcCnt = new ArrayList<>();
		OrdInd = new ArrayList<>();
	}
	
	@Override
	public List<Integer> lookup(int key)
	{
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void insert(int key, int blockNum)
	{

	}

	@Override
	public void delete(int key, int blockNum) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Return the number of entries in the index
	 * @return
	 */
	public int size() {
		// you may find it useful to implement this
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		throw new UnsupportedOperationException();
	}
}
