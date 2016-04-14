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
 private Hashtable<String,Integer> postiveWordsTable=new Hashtable<String,Integer>();
 private Hashtable<String,Integer> negativeWordsTable=new Hashtable<String,Integer>();
 private  int vocabularySize=0;
 private int totalWordsPstv=0,totalWordsNgtv=0;
 
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
	
	setWordCount(trainingSetFile);
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
					   
					   ProbablityPair pair=findProbablityPair(word);
					 
					   table.put(word,pair);
					   
				   }
				  }
				  startOfFile=false;
				}
				scanVocabulary.close();
  }catch(IOException e){
	  e.printStackTrace();
  }
 
	
	postiveWordsTable=null;
    negativeWordsTable=null;	
		  	
}

public  ProbablityPair findProbablityPair(String word){
	int countPstv,countNgtv;
	double probablityPstv,probablityNgtv;
	
	if(postiveWordsTable.containsKey(word))
	countPstv=postiveWordsTable.get(word);
    else
		countPstv=0;
	if(negativeWordsTable.containsKey(word))
	countNgtv=negativeWordsTable.get(word);
     else
		 countNgtv=0;
	
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


public  void setWordCount(String filename){
      String word,line;
	  int count;
	try
	    {
		Scanner textFile = new Scanner(new File(filename));
		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
		
		while(textFile.hasNextLine()){
			line=textFile.nextLine();
			
			if(line.isEmpty())
				 continue;
			Scanner scanLine=new Scanner(line);
			 
			word=scanLine.next();
			if((scanLine.hasNext())&&(word.matches("\\+")))
			{  while(scanLine.hasNext()){
					word=scanLine.next();
					  totalWordsPstv++;
					if(postiveWordsTable.containsKey(word)){
						count=(int)postiveWordsTable.get(word);
						 count++;
					     postiveWordsTable.put(word,new Integer(count));
					}else{
						postiveWordsTable.put(word,new Integer(1));
					}
					
				}
			}else if((scanLine.hasNext())&&(word.matches("-"))){
				while(scanLine.hasNext()){
					word=scanLine.next();
					totalWordsNgtv++;
					if(negativeWordsTable.containsKey(word)){
						count=(int)negativeWordsTable.get(word);
						 count++;
					     negativeWordsTable.put(word,count);
					}else{
						negativeWordsTable.put(word,1);
					}
					
				}
			}
			scanLine.close();
		}
		}catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

	return;
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