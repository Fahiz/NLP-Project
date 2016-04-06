

import java.util.*;
import java.util.regex.Pattern;
import java.io.*;


public class StopWordRemover
{
    
    public static Boolean isStopWord(String word, String[] stopWords)
    {
	boolean found = false; 
    int i=0;

   for (i=0;i<stopWords.length;i++){
	   
	    if(compareWords(word,stopWords[i])==0)
	    { 
          found=true; 
          break;
        }
    }
	 return found;
    }


    public static int compareWords(String word1, String word2)
    {
	return word1.compareToIgnoreCase(word2);
    }

    public static void main(String[] arg)
    {
	Scanner keyboard = new Scanner(System.in);

	System.out.print("Please type the stop words file name: ");
	String[] stopWords = readStopWords(keyboard.next());

	
	System.out.print("Please type the text file name: ");
	removeStopWords(keyboard.next(), stopWords);

    }

    
    public static String[] readStopWords(String stopWordsFilename) 
    {
	String[] stopWords = null;

	try
	    {
		Scanner stopWordsFile = new Scanner(new File(stopWordsFilename));
		int numStopWords = stopWordsFile.nextInt();
		stopWords = new String[numStopWords];
		for (int i = 0; i < numStopWords; i++)
		    stopWords[i] = stopWordsFile.next();

		stopWordsFile.close();
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

	return stopWords;
    }

    public static void removeStopWords(String textFilename, String[] stopWords)
    {
	String word,Line,parsedWord;
	Boolean startOfLine=false;
	String[] words;
	try
	    {
		Scanner textFile = new Scanner(new File(textFilename));
		textFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));

		PrintWriter outFile = new PrintWriter(new File("result.txt"));
		
		while (textFile.hasNextLine())
		    {
			Line=textFile.nextLine();
			Scanner scanLine=new Scanner(Line);
			   startOfLine=true;
			   while(scanLine.hasNext()){
			        word = scanLine.next();
			      if(word.matches(".*\\.[t|T]he\\s*"))
					{
						System.out.println(word);
						words=word.split("\\.");
						word=words[0];						
				    }
					
				    if(!startOfLine)
					    parsedWord=word.replaceAll("[^a-zA-Z]","");
				    else
					    parsedWord=word;
				 
			         if (!isStopWord(parsedWord, stopWords))
					   outFile.print(parsedWord.replaceAll("[a-z]+n't\\s*","not").toLowerCase() + " ");     
				   startOfLine=false;
				}
				outFile.println();
				scanLine.close();
			}
		System.out.println("\nText File after deleting stop words is saved as result.txt ");
		outFile.println();

		textFile.close();
		outFile.close();
	    }
	catch (FileNotFoundException e)
	    {
		System.err.println(e.getMessage());
		System.exit(-1);
	    }

    }

}