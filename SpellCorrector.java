package spell;

import java.io.IOException;
import java.util.*;
import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

public class SpellCorrector implements ISpellCorrector{
    private Trie MyTrie = new Trie();

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        Scanner scanner = new Scanner(new File (dictionaryFileName));
        String wordToAdd;

        while(scanner.hasNext()){
            wordToAdd = scanner.next();
            MyTrie.add(wordToAdd);
        }
        scanner.close();
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        ArrayList<String> candidates1 = new ArrayList<String>();
        String returnWord = inputWord.toLowerCase();

        //System.out.print("\testing word: " + returnWord + "\n");

        if(MyTrie.find(returnWord) != null){
            return returnWord;
        }
        else{
            alteration(candidates1,returnWord);
            transposition(candidates1,returnWord);
            deletion(candidates1,returnWord);
            insertion(candidates1, returnWord);
            if(suggestedWord(candidates1) != null){
                return suggestedWord(candidates1);
            }
            else{
                ArrayList<String> candidates2 = round2(candidates1);
                return suggestedWord(candidates2);
            }
        }
    }

    public ArrayList<String> round2(ArrayList<String> candidates){
        ArrayList<String> candidates2 = new ArrayList<String>();
        for (String word:candidates) {
            alteration(candidates2,word);
            transposition(candidates2,word);
            insertion(candidates2, word);
            deletion(candidates2,word);
        }
        return candidates2;
    }

    public String suggestedWord(ArrayList<String> candidates){
        ArrayList<String> foundWords = new ArrayList<String>();
        SortedSet<String> refinedWords = new TreeSet<>();
        int maxFrequency = 0;
        int frequency = 0;

        for (String word: candidates) {
            if((MyTrie.find(word) != null)) {
                foundWords.add(word);
            }
        }

        if(foundWords != null){
            for(String word: foundWords){
                frequency = MyTrie.find(word).getValue();
                //System.out.print("word: " + word + "\tfrequency: " + frequency + "\n");
                if(frequency > maxFrequency){
                    maxFrequency = frequency;
                }
            }
        }

        for(String word: foundWords){
            if(MyTrie.find(word).getValue() == maxFrequency){
                refinedWords.add(word);
            }
        }

        for(String word: refinedWords){
            return word;
        }

        return null;
    }

    public void deletion(ArrayList<String> candidates, String inputWord){
        for(int i = 0; i < inputWord.length(); ++i){
            StringBuilder editedWord = new StringBuilder(inputWord);
            editedWord = editedWord.deleteCharAt(i);
            String editedWordString = editedWord.toString();
            candidates.add(editedWordString);
        }
    }

    public void transposition(ArrayList<String> candidates, String inputWord){
        for(int i = 0; i < inputWord.length() - 1; ++i){
            StringBuilder editedWord = new StringBuilder(inputWord);
            editedWord = editedWord.deleteCharAt(i);
            editedWord = editedWord.insert(i+1, inputWord.charAt(i));
            String editedWordString = editedWord.toString();
            candidates.add(editedWordString);
        }
    }

    public void alteration(ArrayList<String> candidates, String inputWord){
        for(int i = 0; i < inputWord.length(); ++i){
            for(char j = 'a'; j <= 'z'; ++j){
                StringBuilder editedWord = new StringBuilder(inputWord);
                editedWord = editedWord.replace(i,i+1, String.valueOf(j));
                String editedWordString = editedWord.toString();
                candidates.add(editedWordString);
            }
        }
    }

    public void insertion(ArrayList<String> candidates, String inputWord){
        for(int i = 0; i <= inputWord.length(); ++i){
            for(char j = 'a'; j <= 'z'; ++j){
                StringBuilder editedWord = new StringBuilder(inputWord);
                editedWord = editedWord.insert(i,j);
                String editedWordString = editedWord.toString();
                //System.out.print("edited word = " + editedWordString + "\n");
                candidates.add(editedWordString);
            }
        }
    }



}