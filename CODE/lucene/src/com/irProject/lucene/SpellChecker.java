package com.irProject.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
  
public class SpellChecker {
  	 
	 BufferedReader br = null;
 
 public static void buildDictionary(String file)
 {
	 StringTokenizer st ;
	 try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String word;

			while ((word = br.readLine()) != null) {
				st = new StringTokenizer(word);
				while(st.hasMoreElements())
				{
					String tempword  = st.nextElement().toString();
					int weight = Integer.parseInt(st.nextElement().toString());
					dictionary.put(tempword,weight);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		 
 }
  
 public static Map<String, Integer> dictionary = new HashMap<String, Integer>();
 
 public static String correct(String word) {
   
  if(dictionary.containsKey(word))
   return word;
   
  List<String> edits = edits(word);
 
  Map<Integer, String> candidates = new HashMap<Integer, String>();
 
  for(String s : edits) {
   if(dictionary.containsKey(s)) {
    candidates.put(dictionary.get(s), s);
   }
  }
   
  if(candidates.size() > 0)
   return candidates.get(Collections.max(candidates.keySet()));
   
  for(String s : edits) {
    
   List<String> newEdits = edits(s);
   for(String ns : newEdits) {
    if(dictionary.containsKey(ns)) {
     candidates.put(dictionary.get(ns), ns);
    }
   }
  }
  if(candidates.size() > 0)
   return candidates.get(Collections.max(candidates.keySet()));
  else
   return word;
 }
  
 public static List<String> edits(String word) 
 {
   
  if(word == null || word.isEmpty())
   return null;
   
  List<String> list = new ArrayList<String>();
   
  String w = null;
   
  for (int i = 0; i < word.length(); i++)
  {
   w = word.substring(0, i) + word.substring(i + 1); 
   list.add(w);
  }

  for (int i = 0; i < word.length() - 1; i++) 
  {
   w = word.substring(0, i) + word.charAt(i + 1) + word.charAt(i) + word.substring(i + 2); 
   list.add(w);
  }
   
  for (int i = 0; i < word.length(); i++)
  {
   for (char c = 'a'; c <= 'z'; c++)
   {
    w = word.substring(0, i) + c + word.substring(i + 1); 
    list.add(w);
   }
  }
   
  for (int i = 0; i <= word.length(); i++) 
  {
   for (char c = 'a'; c <= 'z'; c++)
   {
    w = word.substring(0, i) + c + word.substring(i); 
    list.add(w);
   }
  }
   
  return list;
 }
  
}
