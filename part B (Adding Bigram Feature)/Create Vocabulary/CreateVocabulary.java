
import java.util.*;
import java.util.regex.Pattern;
import java.io.*;


public class CreateVocabulary
{
    
   private static Hashtable<String,Integer> unigramsTable=new Hashtable<String,Integer>();
   private static Hashtable<String,Integer> bigramsTable=new Hashtable<String,Integer>();
   private static String vocabularyName;


    public static void main(String[] arg)
    {
	Scanner keyboard = new Scanner(System.in);
	
   System.out.print("Please type a filename to save vocabulary: ");
	vocabularyName=keyboard.next();

	
	System.out.print("Please type the text file name: ");
	fillBigramsTable(keyboard.next());
	writeToFile();
         System.out.print("vocabulary saved as "+vocabularyName);
    }
public static void writeToFile(){
	
	Enumeration<String> bigrams=bigramsTable.keys();
	Enumeration<String> unigrams=unigramsTable.keys();
	
try{	
 	PrintWriter outFile = new PrintWriter(new File(vocabularyName));
	String key;
	
	while(unigrams.hasMoreElements()){
		key=unigrams.nextElement();
		if((unigramsTable.get(key))>2)
			outFile.println(key); 
	}
	while(bigrams.hasMoreElements()){
		key=bigrams.nextElement();
		if((bigramsTable.get(key))>3)
			outFile.println(key); 
	}
	
   outFile.close();
  }catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }
}
 
    
  
 
 
   
   
   
   public static void fillBigramsTable(String textFilename)
    {
	String word,Line,parsedWord,bigram="";
	Boolean startOfLine=false;
	int count;
	try
	    {
		Scanner textFile = new Scanner(new File(textFilename));
		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));

	
		
		while (textFile.hasNextLine())
		    {
			Line=textFile.nextLine();
				if(Line.isEmpty())
				 continue;
			Scanner scanLine=new Scanner(Line);
			   
			   word=scanLine.next();
			   if(scanLine.hasNext()&&(word.matches("\\+")||word.matches("-"))){
			   while(scanLine.hasNext()){
			      if(!(word.matches("\\+")||word.matches("-")))
					{ bigram += word + " ";
				      word=scanLine.next();
					  bigram +=word;
				    }else{
					 
					 word=scanLine.next();
					 
					 
					}
					
					if(!startOfLine){
					if(bigramsTable.containsKey(bigram)){
						count=(int)bigramsTable.get(bigram);
						 count++;
					     bigramsTable.put(bigram,new Integer(count));
					    }else{
						bigramsTable.put(bigram,new Integer(1));
					    }
					}
		                 bigram="";
				    
					      if(unigramsTable.containsKey(word)){
						count=(int)unigramsTable.get(word);
						 count++;
					     unigramsTable.put(word,new Integer(count));
					    }else{
						unigramsTable.put(word,new Integer(1));
					    }
				   startOfLine=false;
				}
				
			   scanLine.close();
			   }
			}
		textFile.close();
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

    }

}