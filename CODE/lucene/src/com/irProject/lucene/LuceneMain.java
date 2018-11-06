package com.irProject.lucene;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class LuceneMain {
	
   static String indexDir = "C:\\Users\\COM\\Documents\\IRProject\\Index";
   static ArrayList<String> topSearchResults = new ArrayList<>();
   String dataDir = "C:\\Users\\COM\\Documents\\IRProject\\Data";
   Indexer indexer;
   Searcher searcher;
   static HashMap<String,String> dictionary = new HashMap<>();
   static ArrayList<String> words = new ArrayList<>();
   
   public static void main(String[] args) throws CorruptIndexException, IOException {
	 
	  
	   Scanner sc = new Scanner(System.in);
	   System.out.println("Please Enter the query to search : ");
	   String query = sc.nextLine();
	   String filePath = "C:\\Users\\COM\\Documents\\IRProject\\topWords.txt";
	   SpellChecker.buildDictionary(filePath); 
	   
	   StringTokenizer st = new StringTokenizer(query);
	   System.out.println("Did you mean...");
	   while(st.hasMoreElements())
	   {
	   String suggestion = SpellChecker.correct(st.nextElement().toString());
	   System.out.print(suggestion+" ");
	   }
	   System.out.println("");
	   
	 //  buildDictionary(filePath);
	 
	/*   IndexReader indexReader = IndexReader.open(NIOFSDirectory.open(new File(indexDir)));
	   //IndexReader indexReader = IndexReader.open(indexDir); 
	   TermEnum termEnum = indexReader.terms(); 
	   while (termEnum.next()) { 
	       Term term = termEnum.term(); 
	       String temp = term.text();
	       if(dictionary.containsKey(temp.toUpperCase()))
	       {
	    	   System.out.println("Added"+temp);
	    	   words.add(temp);
	       }
	      // System.out.println(temp);
	       count++;
	   }
	   termEnum.close(); 
	   indexReader.close();
	   writeToDictionary(words);
	  */ 
	      sc.close();
      LuceneMain tester;
      try {
         tester = new LuceneMain();
      //   tester.createIndex();
         tester.search(query);
         
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParseException e) {
         e.printStackTrace();
      }  
   }
   
   private void createIndex() throws IOException {
      indexer = new Indexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.close();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }
   
   private void search(String searchQuery) throws IOException, ParseException {
	 
      searcher = new Searcher(indexDir);
      long startTime = System.currentTimeMillis();
      TopDocs hits = searcher.search(searchQuery);
      long endTime = System.currentTimeMillis();
   
      System.out.println(hits.totalHits +
         " documents found. Time :" + (endTime - startTime));
      for(ScoreDoc scoreDoc : hits.scoreDocs) {
         Document doc = searcher.getDocument(scoreDoc);
            System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
         String snippet =  getSnippet((getFileData(doc.get(LuceneConstants.FILE_PATH))).toString().toUpperCase(), searchQuery) ;
         System.out.println(snippet);
      }
      searcher.close();
   }
   
   
   public StringBuilder getFileData(String filePath)
	{
		StringBuilder sb = new StringBuilder();
		
		 try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			 	String word ;
				while ((word = br.readLine()) != null) 
				{
						sb.append(word);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		return sb;
	}
	
	public String getSnippet(String sb, String searchQuery)
	{
		StringTokenizer st = new StringTokenizer(searchQuery);
	
		String searchWord =st.nextElement().toString().toUpperCase();	
		int pos = sb.indexOf(searchWord);
		String snippet = sb.substring(pos,pos+200)+"......";
		return snippet;
	}
	
   
   
}
