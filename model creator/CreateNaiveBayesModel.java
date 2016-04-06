import java.util.*;
import java.util.regex.Pattern;
import java.io.*;


public class CreateNaiveBayesModel implements Serializable{
	
public static void main(String[] args){
	String model_name;
	     Scanner keyboard = new Scanner(System.in);
	System.out.print("Please type a name to save Model(! No need of filetype): ");
	model_name=keyboard.next();
	NaiveBayesModel model=new NaiveBayesModel();
	Serializer   ser=new Serializer();
	model_name += ".ser";
	 ser.serializeModel(model,model_name);
}
	
}
class Serializer{
	
	public static void serializeModel(NaiveBayesModel model,String model_name){
	try{
	FileOutputStream fos=new FileOutputStream(model_name);
	ObjectOutputStream oos=new ObjectOutputStream(fos);
	oos.writeObject(model);
	oos.close();
	fos.close();
	System.out.println("Model saved successfully as "+model_name+" !!!");
	}catch(IOException e){
		e.printStackTrace();
	}
}
}






class NaiveBayesModel implements Serializable{
 private  Dictionary<String,ProbablityPair> table=new Hashtable<String,ProbablityPair>();
 private  int vocabularySize=0;
 
 public  double getPstvProbablity(String word){
	   ProbablityPair pair=table.get(word);
	   if(pair==null)
		   return 1;
	   else
		   return pair.getPstv();
 }
 
 public  double getNgtvProbablity(String word){
	   ProbablityPair pair=table.get(word);
	   if(pair==null)
		   return 1;
	   else
		   return pair.getNgtv();
 }
 public int getSize(){
	 return table.size();
 }

public NaiveBayesModel(){
	String trainingSetFile,word,vocabulary_Fname;
     Scanner keyboard = new Scanner(System.in);
	Boolean startOfFile=true;
	System.out.print("Please type training set file name: ");
	trainingSetFile=keyboard.next();
	System.out.print("Please type vocabulary file name: ");
	vocabulary_Fname=keyboard.next();
	
	String[] review=divideCorpusOnClass(trainingSetFile);
	System.out.println("processing.....");
	
  try{	
	Scanner   scanVocabulary=new Scanner(new File(vocabulary_Fname));
	scanVocabulary.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
	
	      while(scanVocabulary.hasNext()){
			      if(startOfFile)
					  vocabularySize=scanVocabulary.nextInt();
				  else{
			       word=scanVocabulary.next();
				   
				   if(!word.matches("[^a-zA-z]")){
					   
					   ProbablityPair pair=findProbablityPair(word,review);
					 
					   table.put(word,pair);
					   
				   }
				  }
				  startOfFile=false;
				}
				scanVocabulary.close();
  }catch(IOException e){
	  e.printStackTrace();
  }
		  
		  	
	
	
		
}

public  ProbablityPair findProbablityPair(String word,String[] review){
	int totalWordsPstv=0,totalWordsNgtv=0,countPstv=0,countNgtv=0;
	double probablityPstv,probablityNgtv;
	
	 String reviewWord;
	Scanner scanPostiveReview=new Scanner(review[0]);
	Scanner scanNegativeReview=new Scanner(review[1]);
	while(scanPostiveReview.hasNext()){
		reviewWord=scanPostiveReview.next();
		if(!reviewWord.matches("[^a-zA-z]"))
		{totalWordsPstv++;
	      if(reviewWord.compareToIgnoreCase(word)==0)
			  countPstv++;
		}
	}
	while(scanNegativeReview.hasNext()){
		reviewWord=scanNegativeReview.next();
		if(!reviewWord.matches("[^a-zA-z]"))
		{totalWordsNgtv++;
	      if(reviewWord.compareToIgnoreCase(word)==0)
			  countNgtv++;
		}
	}
	
	scanPostiveReview.close();
	scanNegativeReview.close();
	
	probablityPstv=findProbablity(totalWordsPstv,countPstv);

	probablityNgtv=findProbablity(totalWordsNgtv,countNgtv);
	
     ProbablityPair pair=new ProbablityPair(probablityPstv,probablityNgtv);
     return pair;	 
}
public  double findProbablity(int totalWords,int wordFrequency){
	double probablity;
	double wordfrequency=(double)wordFrequency;
	probablity=(wordfrequency+1)/(totalWords+vocabularySize);
	return probablity;
}


public  String[] divideCorpusOnClass(String filename){
	String[] review =new String[2];
	String postiveReview="",negativeReview="",line,firstWord="";

	try
	    {
		Scanner textFile = new Scanner(new File(filename));
		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
		
		while(textFile.hasNextLine()){
			line=textFile.nextLine();
			Scanner scanLine=new Scanner(line);
			if(scanLine.hasNext())
			firstWord=scanLine.next();
			scanLine.close();
			if(firstWord.matches("\\+"))
			postiveReview += line + "\n";
			else if(firstWord.matches("-"))
			negativeReview += line +"\n";
		}
		
		review[0]=postiveReview;
		review[1]=negativeReview;
		textFile.close();
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

	return review;
}

}
class ProbablityPair implements Serializable{
	
	private double pstvProbablity;
	private double ngtvProbablity;
	
	public ProbablityPair(double l,double r){
		this.pstvProbablity=l;
		this.ngtvProbablity=r;
	}
	public double getPstv(){
		return this.pstvProbablity;
	}
	public double getNgtv(){
		return this.ngtvProbablity;
	}
	public void setPstv(double prob){
		this.pstvProbablity=prob;
	}
	public void setNgtv(double prob){
		this.ngtvProbablity=prob;
	}
}