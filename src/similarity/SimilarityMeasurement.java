package similarity;

import java.io.IOException;
import java.util.Map;

public class SimilarityMeasurement {
	
	ExpandSeed es = new ExpandSeed();
	public boolean equalsString(String line ,String s) {
		String[] strs=line.split("\\s{1,}");
		//List<String> tempList = Arrays.asList(stringArray);
		for(int i=0;i<strs.length;i++) {
			if(s.equalsIgnoreCase(strs[i])) {		
				return true;
			} 
		}
		return false;
	}
	public double calVecCos(float[] vec1,float[] vec2) {
		double vec1Modulo=0.00;
		double vec2Modulo=0.00;
		double vecProduct=0.00;
		
		for(int i=0;i<vec1.length;i++) {
			vecProduct+=vec1[i]*vec2[i];
			vec1Modulo+=vec1[i]*vec1[i];
			vec2Modulo+=vec2[i]*vec2[i];
		}
		vec1Modulo=Math.sqrt(vec1Modulo);
		vec2Modulo=Math.sqrt(vec2Modulo);
		return vecProduct/(vec1Modulo*vec2Modulo);
	}
	
	public double calEuDistance(float[] vec1,float[] vec2) {
		double sum=0.00;
		for(int i=0;i<vec1.length;i++) {
			sum+=(vec1[i]-vec2[i])*(vec1[i]-vec2[i]);
		}
		
		return Math.sqrt(sum);
		
	}
	
	public double maxCos (float[] wordVec,String seedLine, Map<String, float[]> seedVec) throws IOException {
		
		String[] ss = null;
		ss = seedLine.split("\\s{1,}");
		double cos = 0.0;

		for (int i = 0; i < ss.length; i++) {
			if (seedVec.containsKey(ss[i]) && calVecCos(wordVec, seedVec.get(ss[i])) > cos) {
				cos = calVecCos(wordVec, seedVec.get(ss[i]));
			} else {

			}

		}

		return cos;

	}

}
