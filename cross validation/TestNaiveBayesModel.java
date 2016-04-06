import java.util.*;
import java.util.regex.Pattern;
import java.io.*;

public class TestNaiveBayesModel implements Serializable
{
    private static LinkedList<String> reviewType= new LinkedList<String>();	 
	private static double countCorrectlyPredicted=0;
	private static int totalReview=0;
	
   public static NaiveBayesModel deserializeModel(String model_name){
   NaiveBayesModel model=null;
   try{
      FileInputStream fis=new FileInputStream(model_name);
	  ObjectInputStream ois=new ObjectInputStream(fis);
	  model=(NaiveBayesModel)ois.readObject();
	  ois.close();
	  fis.close();
   }catch(IOException e){
	   e.printStackTrace();
   }catch(ClassNotFoundException e){
	   e.printStackTrace();
   }
   return model;
  }
  public static void main(String[] args){
	  String model_name,test_Fname,review,classType;
	  LinkedList<double[]> probablityList=new LinkedList<double[]>();
	  double[] classProbablity;
	  int i=1;
	  Scanner keyboard = new Scanner(System.in);
	  System.out.print("please type the filename of NaiveBayesModel to test: ");
	  	model_name=keyboard.next();
		System.out.print("please type the test filename: ");
	  	test_Fname=keyboard.next();
	 NaiveBayesModel  model=deserializeModel(model_name);
	 try{
	 Scanner scanFile=new Scanner(new File(test_Fname));
	 scanFile.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]"));
	   
	   while(scanFile.hasNextLine()){
		   review=scanFile.nextLine();
		   if(review.isEmpty())
			   break;
		   totalReview++;
		   classProbablity=findReviewProbablity(model,review);
		   probablityList.add(classProbablity);
	   }
	   
	   scanFile.close();
	   
	 }catch(IOException e){
		 e.printStackTrace();
	 }
	  Iterator reviewClass=reviewType.listIterator();
	  Iterator list=probablityList.listIterator(); 
	  while(list.hasNext()){
		  classProbablity=(double[])list.next();
		   
		  classType=(String) reviewClass.next();
		  
		  if(Math.abs(classProbablity[0])<Math.abs(classProbablity[1]))
		  {
			  System.out.println("review "+Integer.toString(i)+" predicted type positive(+) "+"(Log positveprobablity: "+Double.toString(classProbablity[0])+","+" Log negativeprobablity: "+Double.toString(classProbablity[1])+")");
		        if(classType.compareToIgnoreCase("+")==0)
				 countCorrectlyPredicted++;					
		  }
		  else
		  {
			  System.out.println("review "+Integer.toString(i)+" predicted type negative(-) "+"(Log positveprobablity: "+Double.toString(classProbablity[0])+","+" Log negativeprobablity: "+Double.toString(classProbablity[1])+")");
		 
              if(classType.compareToIgnoreCase("-")==0)
			  countCorrectlyPredicted++;
		 }
		  i++;
		}
		
		System.out.println("Accuracy: "+((countCorrectlyPredicted/totalReview)*100));
	  
  }
  public static double[] findReviewProbablity(NaiveBayesModel model,String Fname){
	 double pstvClassProbablity=0,ngtvClassProbablity=0,x=0.5,classProbablity[];
	 String word;
     Boolean firstWord=true;
	Scanner   scanReview=new Scanner(Fname);
	
	scanReview.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
	
	      while(scanReview.hasNext()){
			      
				  
			       word=scanReview.next();
				  if(firstWord)
				    {reviewType.add(word);firstWord=false;}					  
				   
				   if(!word.matches("[^a-zA-z]")){
					   
					  pstvClassProbablity += Math.log(model.getPstvProbablity(word));  
					  ngtvClassProbablity += Math.log(model.getNgtvProbablity(word)); 
				   }
				  }
				  
				
				scanReview.close();
  	
  
  pstvClassProbablity += Math.log(x);
  ngtvClassProbablity += Math.log(x);
  classProbablity=new double[2];
  classProbablity[0]=pstvClassProbablity;
  classProbablity[1]=ngtvClassProbablity;
  return classProbablity;
  
  
  }

}