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
    
    private List<Integer> kyBlcCnt;
    private List<List<Integer>> ordInd;
    
    private int binarySearch(List<List<Integer>> arr,int start, int end, int element, int target)
    {
        //element: 0=keys, 1=blocks
        
        if(end>=start)
        {
            int mid = start+(end-start)/2;
            if(arr.get(mid).get(element)>target)
            {
                if(mid-1<start)
                {
                    return mid*-1;
                }
                return binarySearch(arr, start, mid-1, element,target);
            }
            else if(arr.get(mid).get(0)<target)
            {
                if(mid+1>end)
                {
                    return (mid+1)*-1;
                }
                return binarySearch(arr, mid+1, end, element,target);
            }
            else
            {
                return mid;
            }
        }
        else
            return -13;
        
    }
    private ArrayList<Integer> rangeBinSearch(List<List<Integer>> arr,int start, int end, int element,int target)
    {
        int ind = binarySearch(arr, start, end, element,target);
        
        ArrayList<Integer> indList = new ArrayList<Integer>();
        boolean isPrev=true, isNext=true;
        int i=1;
        
        //If ind is negative or if its zero but does not
        //equal the target then the target does not exist in ordInd
        //so return then index where its supposed to go
        if(ind<0 || ind==0&&arr.get(ind).get(element)!=target)
        {
            indList.add(ind);
            return indList;
        }
        
        while(isPrev||isNext)
        {
            if(ind-i<0)
            {
                indList.add(ind);
                isPrev=false;
            }
            if(ind+i>=arr.size())
            {
                isNext=false;
            }
            if(ind-i>=0&&isPrev)
            {
                if(arr.get(ind-i).get(element)==target)
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
                if(arr.get(ind+i).get(element)==target)
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
        ordInd = new ArrayList<>();
    }
    
    @Override
    public List<Integer> lookup(int key)
    {
        List<Integer> indList = rangeBinSearch(ordInd, 0, ordInd.size()-1, 0,key);
        
        if(indList.size()==1)
        {
            if(indList.get(0)<0 || indList.get(0)==0&&ordInd.get(0).get(0)!=key) {
                indList.clear();
                return indList;
            }
        }
        
        List<Integer> blockList = new ArrayList<>();
        for (int i=0;i<indList.size();i++)
        {
            int currInd=indList.get(i);
            blockList.add(ordInd.get(currInd).get(1));
        }
        
        return blockList;
    }
    
    @Override
    public void insert(int key, int blockNum)
    {
        List<Integer> newInd = new ArrayList<>();
        newInd.add(key);
        newInd.add(blockNum);
        newInd.add(1);
        
        //If ordInd is empty simply insert
        if(ordInd.size()==0)
        {
            ordInd.add(newInd);
        }
        else
        {
            //Get a list of all the indices with the given key
            List<Integer> indList= rangeBinSearch(ordInd, 0, ordInd.size()-1, 0, key);
            
            boolean wasInserted=false;
            //If the size of indList is only 1 and that
            // value is <=0 the key does not exist in ordInd
            if(indList.size()==1)
            {
                
                if(indList.get(0)<=0)
                {
                    if(indList.get(0)==0 && ordInd.get(0).get(0)!=key)
                    {
                        ordInd.add(0, newInd);
                        wasInserted=true;
                    }
                    else if(indList.get(0)<0)
                    {
                        int tempInd = indList.get(0)*-1;
                        ordInd.add(tempInd, newInd);
                        wasInserted=true;
                    }
                }
            }
            
            if(!wasInserted)
            {
                int blckInd = binarySearch(ordInd, indList.get(0), indList.get(indList.size()-1), 1, blockNum);
                
                if(blckInd>0&&ordInd.get(blckInd).get(1)==blockNum || blckInd==0&&ordInd.get(blckInd).get(1)==blockNum)
                {
                    List<Integer> tempInd = ordInd.get(blckInd);
                    tempInd.set(2, ordInd.get(blckInd).get(2)+1);
                    ordInd.set(blckInd, tempInd);
                }
                else
                {
                    ordInd.add(indList.get(indList.size()-1)+1,newInd);
                }
            }
        }
    }
    
    @Override
    public void delete(int key, int blockNum)
    {
        List<Integer> indList = rangeBinSearch(ordInd, 0, ordInd.size()-1, 0, key);
        
        if(indList.size()==1)
        {
            if(indList.get(0)==0 && ordInd.get(0).get(0)==key && ordInd.get(0).get(1)== blockNum || indList.get(0)>0)
            {
                ordInd.remove(ordInd.get(indList.get(0)));
            }
        }
        else
        {
            int blckInd = binarySearch(ordInd, indList.get(0), indList.get(indList.size()-1), 1, blockNum);
            
            if(blckInd>=0&&ordInd.get(blckInd).get(1)==blockNum)
            {
                if(ordInd.get(blckInd).get(2)>1)
                {
                    List<Integer> newInd = ordInd.get(blckInd);
                    newInd.set(2, ordInd.get(blckInd).get(2)-1);
                    ordInd.set(blckInd, newInd);
                }
                else
                {
                    ordInd.remove(ordInd.get(blckInd));
                }
            }
        }
    }
    
    /**
     * Return the number of entries in the index
     * @return
     */
    public int size() {
        // you may find it useful to implement this
        return ordInd.size();
    }
    
    @Override
    public String toString() {
        String ordIndStr="[";
        for(int i=0;i<ordInd.size(); i++)
        {
            ordIndStr+="[";
            List<Integer> currInd = ordInd.get(i);
            for(int j=0; j<currInd.size();j++)
            {
                if(j+1==currInd.size())
                {
                    ordIndStr+=currInd.get(j);
                }
                else
                {
                    ordIndStr+=currInd.get(j)+", ";
                }
            }
            if(i+1==ordInd.size())
            {
                ordIndStr+="]";
            }
            else
            {
                ordIndStr+="], ";
            }
        }
        
        ordIndStr+="]";
        return ordIndStr;
    }
}
