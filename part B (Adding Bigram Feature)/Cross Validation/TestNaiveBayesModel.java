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
	  System.out.print("please type the filename of NaiveBayesModel to test with: ");
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
	 double pstvClassProbablity=0,ngtvClassProbablity=0,classProbablity[];
	 String bigram="",word,firstWord;
	 String[] words;
    boolean startOfLine=true;
	 double pstvProbablity,ngtvProbablity;
	Scanner   scanReview=new Scanner(Fname);
	
	scanReview.useDelimiter(Pattern.compile("[ \n\r\t,.;:?!'\"]+"));
	  word=scanReview.next();
	  if(scanReview.hasNext()&&(word.matches("\\+")||word.matches("-"))){
		  reviewType.add(word);
	           while(scanReview.hasNext()){
			      
				  if(!(word.matches("\\+")||word.matches("-")))
					{ bigram += word + " ";
				      word=scanReview.next();
					  bigram +=word;
				    }else{
					 word=scanReview.next();
					}
				 			 
				   
				   if(!startOfLine){
					      		 words=bigram.split(" ");  
					    pstvProbablity=model.getPstvProbablity(bigram,true);
						
						if(pstvProbablity==0)
                           pstvProbablity=model.getPstvProbablity(words[0],false);
					   
					   ngtvProbablity=model.getNgtvProbablity(bigram,true);
						if(ngtvProbablity==0)
                           ngtvProbablity=model.getNgtvProbablity(words[0],false);
					   
					   pstvClassProbablity += Math.log(pstvProbablity);  
					  ngtvClassProbablity += Math.log(ngtvProbablity); 
				   }
				   bigram="";
				   startOfLine=false;
				}
				 pstvProbablity=model.getPstvProbablity(word,false);
				  ngtvProbablity=model.getNgtvProbablity(word,false);
				     pstvClassProbablity += Math.log(pstvProbablity);  
					  ngtvClassProbablity += Math.log(ngtvProbablity); 
		}
				  
		
				
				scanReview.close();
 
  
  pstvClassProbablity += model.getPstvClassProbablity();
  ngtvClassProbablity += model.getNgtvClassProbablity();
  classProbablity=new double[2];
  classProbablity[0]=pstvClassProbablity;
  classProbablity[1]=ngtvClassProbablity;
  return classProbablity;
  
  
  }

}