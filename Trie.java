package spell;


import java.util.Locale;
import java.util.stream.Stream;

public class Trie implements ITrie{
    public Node root = new Node();
    public int totalWordCount = 0;
    public StringBuilder printThis = new StringBuilder();

    @Override
    public void add(String word) {
        //System.out.print("\nAdding word: " + word + "\n");
        word = word.toLowerCase();
        Node currentNode = this.root;

        for(int i = 0; i < word.length(); ++i){
            char currentChar = word.charAt(i);
            int index = currentChar - 'a';
            //System.out.print("current char is: " + currentChar + " and the index is: " + index + "\n");

            if(currentNode.nodes[index] == null) {
                currentNode.nodes[index] = new Node();
                root.nodeCount ++;
            }

            currentNode = currentNode.nodes[index];
            if( i == word.length() - 1){
                if(currentNode.getValue() < 1 ) {
                    totalWordCount++;
                }
                //System.out.print("Total Word Count is: " + totalWordCount + "\n");
                currentNode.incrementValue();
                //System.out.print("Specific word count is: " + currentNode.getValue() + "\n\n");

            }
        }

        //System.out.print("node count is: " + root.nodeCount + "\n");
        //System.out.print("word count is: " + root.wordCount + "\n");
    }

    @Override
    public INode find(String word) {
        word = word.toLowerCase();
        Node currentNode = this.root;

        for(int i = 0; i < word.length(); ++i){
            char currentChar = word.charAt(i);
            int index = currentChar - 'a';

            if(currentNode.nodes[index] != null){
                //System.out.print("found character " + currentChar + "\n");
                currentNode = currentNode.nodes[index];
                if(i == (word.length() - 1) && (currentNode.getValue() > 0)){
                    //System.out.print("returning " + currentNode.nodes[index] + "\n");
                    return currentNode;
                }
            }
            else{
                //System.out.print("did not find character " + currentChar + "\n");
                return null;
            }
        }

        return null;
    }

    @Override
    public int getWordCount() {
        //System.out.print("word count is: " + totalWordCount + "\n");
        return totalWordCount;
    }

    @Override
    public int getNodeCount() {
        return root.nodeCount;
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     * MUST BE RECURSIVE.
     */
    @Override
    public String toString(){
        //System.out.print("\nIn toString method \n");
        StringBuilder printThis = new StringBuilder();
        StringBuilder singleWord = new StringBuilder();
        Node currentNode = this.root;

        String TheString;
        TheString = toStringHelper(currentNode, printThis, singleWord);
        //System.out.print(TheString + "\n\n");
        return TheString;
    }

    public String toStringHelper(Node currentNode, StringBuilder printThis, StringBuilder singleWord){
        for(int i = 0; i < 26; ++i){
            if(currentNode.nodes[i] != null){
                char characterToAdd = (char) (i + 'a');
                //System.out.print("adding " +characterToAdd+ " to the string\n");
                singleWord.append(characterToAdd);

                if(currentNode.nodes[i].getValue() > 0){
                    //System.out.print(characterToAdd + " has a value of: " + currentNode.getValue() +"\n");
                    printThis.append(singleWord + "\n");
                }

                toStringHelper(currentNode.nodes[i], printThis, singleWord);
                //System.out.print("deleting " + singleWord.charAt(singleWord.length() - 1) + "\n");
                singleWord.deleteCharAt(singleWord.length() - 1);
            }
        }
        String returnString = printThis.toString();
        return returnString;
    }


    /**
     * Returns the hashcode of this trie.
     * MUST be constant time.
     * @return a uniform, deterministic identifier for this trie.
     */
    @Override
    public int hashCode(){
        int firstNode = 26;
        int returnValue;
        for(int i = 0; i < 26; ++i){
            if(this.root.nodes[i] != null){
                firstNode = i;
                if(this.root.nodes[i].getValue() != 0){
                    firstNode *= 31;
                }
                break;
            }
        }
        returnValue = ((firstNode * 31) + (this.root.nodeCount * 31) + (this.totalWordCount * 31));
        //System.out.print("\nHash value is: " + returnValue + "\n");
        return returnValue;
    }

    /**
     * Checks if an object is equal to this trie.
     * MUST be recursive.
     * @return true if o is a Trie with same structure and node count for each node
     * 		   false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass())
            return false;
        if (o == this)
            return true;
        Trie other = (Trie) o;
        if (totalWordCount != other.totalWordCount || getNodeCount() != other.getNodeCount())
            return false;
        return root.equals(other.root);
    }

    public class Node implements INode{
        public Node[] nodes;
        private static final int ALPHABET_COUNT = 26;
        private int frequency;
        private int nodeCount;

        public Node(){
            frequency = 0;
            nodeCount = 1;
            nodes = new Node[ALPHABET_COUNT];
        }

        @Override
        public boolean equals(Object o) {
            if (o.getClass() != this.getClass())
                return false;
            if (o == this)
                return true;
            Node other = (Node) o;
            if (other.frequency != frequency)
                return false;
            for (int i = 0; i < nodes.length; i++) {
                if ((nodes[i] == null && other.nodes[i] != null) || (other.nodes[i] == null && nodes[i] != null))
                    return false;
                else if (nodes[i] == null)
                    ;
                else if (!nodes[i].equals(other.nodes[i]))
                    return false;
            }
            return true;
        }

        @Override
        public int getValue() {
            return frequency;
        }

        @Override
        public void incrementValue() {
            frequency ++;
        }

        @Override
        public INode[] getChildren() {
            return new INode[0];
        }
    }
}
