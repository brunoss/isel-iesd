package isos.tutorial.isyiesd.cesvector.servector;

import javax.jws.WebService;
import java.util.Arrays;
import java.util.List;

@WebService(endpointInterface = "isos.tutorial.isyiesd.cesvector.servector.IVector")
public class Vector implements IVector {

    private static List<Integer> vector = Arrays.asList(300, 234, 56, 789);
    private static int sum = sum();
    
    private static int sum() {
    	int sum = 0;
    	for(int i = 0; i < vector.size(); ++i) {
    		sum += vector.get(i);
    	}
    	return sum;
    }
    
    @Override
    public int read(int pos) 
    {
        return vector.get(pos);
    }

    @Override
    public void write(int pos, int n) 
    {
        vector.set(pos, n);
    }
}
