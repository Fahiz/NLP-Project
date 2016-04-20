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
  
     private ArrayList<String> unigramList= new ArrayList<String>();
	  private ArrayList<String>	bigramList= new ArrayList<String>();
 private Hashtable<String,Double> postiveProbablityTable=new Hashtable<String,Double>();
 private Hashtable<String,Double> negativeProbablityTable=new Hashtable<String,Double>();
 private  int totalPstv,totalNgtv,totalReviewsNgtv=0,totalReviewsPstv=0;
 private double classPstvProbablity,classNgtvProbablity;
 
 public double getPstvClassProbablity(){
	 return classPstvProbablity;
 }
 public double getNgtvClassProbablity(){
	 return classNgtvProbablity;
 }
 public  double getPstvProbablity(String word,boolean bigram){
	   Double probablity=postiveProbablityTable.get(word);
	   if(probablity==null&&bigram)
		   return 0;
	   else if(probablity==null)
		   return 1;
	   else
		   return probablity.doubleValue();
 }
 
 public  double getNgtvProbablity(String word,boolean bigram){
	   Double probablity=negativeProbablityTable.get(word);
	   if(probablity==null&&bigram)
		   return 0;
	   else if(probablity==null)
		   return 1;
	   else
		   return probablity.doubleValue();
 }

public NaiveBayesModel(){
	String trainingSetFile,vocabulary_Fname,word;
     Scanner keyboard = new Scanner(System.in);
	System.out.print("Please type training set file name: ");
	trainingSetFile=keyboard.next();
	System.out.print("Please type vocabulary file name: ");
	vocabulary_Fname=keyboard.next();
	
	System.out.println("processing.....");
	
  try{	
	Scanner   scanVocabulary=new Scanner(new File(vocabulary_Fname));
	scanVocabulary.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
	
	      while(scanVocabulary.hasNextLine()){
			  
			      word=scanVocabulary.nextLine();
				  if(word.isEmpty())
					  continue;
                   postiveProbablityTable.put(word,1.0);
                   negativeProbablityTable.put(word,1.0);
				   if(word.contains(" "))
				     bigramList.add(word);
			       else
				   unigramList.add(word);
				  
			       
				}
				scanVocabulary.close();
  }catch(IOException e){
	  e.printStackTrace();
  }
  totalPstv=unigramList.size()+bigramList.size();
   totalNgtv=unigramList.size()+bigramList.size();
   setProbablityTables(trainingSetFile);
		
		  	
}


public  void setProbablityTables(String filename){
      String word,line,bigram="";
	  String[] words;
	  int count;
	  boolean firstOfLine=true;
	try
	    {
		Scanner textFile = new Scanner(new File(filename));
		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
		
		while(textFile.hasNextLine()){
			line=textFile.nextLine();
			
			if(line.isEmpty())
				 continue;
			Scanner scanLine=new Scanner(line);
			 firstOfLine=true;
			word=scanLine.next();
			if(word.matches("\\+"))
				totalReviewsPstv++;
			else
				totalReviewsNgtv++;
			
			if((scanLine.hasNext())&&(word.matches("\\+")))
			{  while(scanLine.hasNext()){
					if(!word.matches("\\+"))
					{ bigram += word + " ";
				      word=scanLine.next();
					  bigram +=word;
				    }else{
					   word=scanLine.next();
					 }
					 
					
                     if(!firstOfLine){
						 words=bigram.split(" ");
						 if(bigramList.contains(bigram))
						 {postiveProbablityTable.put(bigram,postiveProbablityTable.get(bigram)+1);totalPstv++;}
						 else if(unigramList.contains(words[0]))
						 {postiveProbablityTable.put(words[0],postiveProbablityTable.get(words[0])+1);totalPstv++;}
							 
					 }
					 

					firstOfLine=false;
					bigram="";
				}
				 if(unigramList.contains(word))
						 {postiveProbablityTable.put(word,postiveProbablityTable.get(word)+1);totalPstv++;}
				
			}else if((scanLine.hasNext())&&(word.matches("-"))){
				while(scanLine.hasNext()){
					if(!word.matches("-"))
					{ bigram += word + " ";
				      word=scanLine.next();
					  bigram +=word;
					}else{
						word=scanLine.next();
					}
				
							
                     if(!firstOfLine){
						 words=bigram.split(" ");
						 if(bigramList.contains(bigram))
						 {negativeProbablityTable.put(bigram,negativeProbablityTable.get(bigram)+1);totalNgtv++;}
						 else if(unigramList.contains(words[0]))
						 {negativeProbablityTable.put(words[0],negativeProbablityTable.get(words[0])+1);totalNgtv++;}
							 
					 }
					 		
					firstOfLine=false;
					bigram="";
			    }
				if(unigramList.contains(word))
						 {negativeProbablityTable.put(word,negativeProbablityTable.get(word)+1);totalNgtv++;}
			
		  }
		  scanLine.close();
		}
		}catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }
		Enumeration<String> pstvKeys=postiveProbablityTable.keys();
		String key;
		double value;
		while(pstvKeys.hasMoreElements()){
			  key=pstvKeys.nextElement();
			  value=(double)postiveProbablityTable.get(key);
			   postiveProbablityTable.put(key,value/totalPstv);
		}
		Enumeration<String> ngtvKeys=negativeProbablityTable.keys();
       while(ngtvKeys.hasMoreElements()){
			  key=ngtvKeys.nextElement();
			  value=(double)negativeProbablityTable.get(key);
			   negativeProbablityTable.put(key,value/totalNgtv);
		  }
		  classPstvProbablity = Math.log(1.0*totalReviewsPstv/(totalReviewsPstv+totalReviewsNgtv));
		classNgtvProbablity = Math.log(1.0*totalReviewsNgtv/(totalReviewsPstv+totalReviewsNgtv));

		
	return;
}
}


