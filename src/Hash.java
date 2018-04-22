import java.io.*;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;


class LinkedListNode {
    String key;
    String data;

    public LinkedListNode(String key, String data) {
        this.key = key;
        this.data = data;
    }

    @Override
    public String toString()
    {
        return "(K:" + key + ",V:" + data + ")";
    }

}

class HashTable {
    private LinkedList<LinkedListNode>[ ] table;
    private int total_keys=0;

    /* Constructor */
    public HashTable(int tableSize) {
        this.table = new LinkedList[tableSize];
    }

    /* Function to insert an element */
    public void insert(String key, String data) {
        int pos = hash(key);
        // Make sure the list is initialized before adding to it
        if (table[pos] == null) {
            table[pos] = new LinkedList<LinkedListNode>();
        }

        LinkedList<LinkedListNode> list = table[pos];
        LinkedListNode node = new LinkedListNode(key, data);
        if(search(key)!=null)
        {
            int index = indexOfKeyInLinkedList(list,key);
            list.set(index, node);
        }
        else
        {
            total_keys++;
            list.add(node);
        }
    }

    /* Function hash */
    private int hash(String key) {
        int val = key.hashCode();

        Random rand = new Random();
        int t_size = (int) Hash.tableSize;
        int min=t_size;
        int rand_val=rand.nextInt((2*min) - min + 1) + min;

        if(!isPrime(rand_val))
        {
            rand_val = nextPrime(rand_val);
        }

        min=1;
        int a =rand.nextInt(rand_val - min + 1) + min;
        int b = rand.nextInt(rand_val - min + 1) + min;

        int hashVal = ((a* val) + b % rand_val)% t_size;

        if (hashVal < 0) {
            hashVal += t_size;
        }
        return hashVal;
    }


    private static int nextPrime(int n)
    {
        if (n % 2 == 0)
            n++;
        for ( ; !isPrime(n); n += 2);
        return n;
    }

    private static boolean isPrime(int n)
    {
        if (n == 2 || n == 3)
            return true;
        if (n == 1 || n % 2 == 0)
            return false;
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;
        return true;
    }


    public String search(String key)
    {
        int pos = hash(key);
        if (table[pos] == null) {
            return null;
        }
        LinkedList<LinkedListNode> list = table[pos];
        String result_data = null;
        for(LinkedListNode n : list)
        {
            if(n.key.equals(key))
            {
                result_data = n.data;
            }
        }
        return result_data;
    }


    public void printHashTable () {
        System.out.println();
        for (int i = 0; i < table.length; i++) {
            System.out.print ("Bucket " + i + ":  ");
            LinkedList list = table[i];
            if(list != null) {
                for(Object o : list){
                    System.out.print(o + "->");
                }
                System.out.print("null");
            }
            System.out.println();
        }
    }


    public Double average_length() {
        int num =getSize();
        int deno = table.length;
        return (double) num/ (double) deno;
    }


    public Double average_collision() {
        int sum=0;
        int count=0;
        for(int i=0;i<table.length;i++)
        {
            if(table[i]!= null && table[i].size()>1)
            {
                sum += table[i].size() -1;
                count ++;
            }
        }
        return (double) sum / (double) count;
    }

    private int indexOfKeyInLinkedList(LinkedList<LinkedListNode> list, String key)
    {
        for(int i=0;i< list.size();i++)
        {
            if(list.get(i).key.equals(key))
            {
                return i;
            }
        }
        return -1;
    }

    /* Function to get size */
    public int getSize() {
        return total_keys;
    }
}


public class Hash
{
    static double tableSize;
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("****     Hash Table Implementation     ****\n");
        System.out.println("Enter path of csv file: ");
        String path = scan.next();
        System.out.println("Enter load factor: ");
        double loadFactor=scan.nextDouble();
        int lines_in_csv = returnLineCountForFile(path);

        // duplicates in file
        int duplicates =103;

        tableSize = (double) (lines_in_csv - duplicates) / loadFactor;
        HashTable ht = new HashTable((int) tableSize);

        String line;
        Scanner scanner;
        int index = 0;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            scanner = new Scanner(line);
            scanner.useDelimiter(",");
            String zipcode=null;
            String population=null;
            while (scanner.hasNext()) {
                String data = scanner.next();
                if (index == 0)
                    zipcode = data;
                else if (index == 1)
                    population = data;
                else
                    System.out.println("ERROR: Invalid csv" + data);
                index++;
            }
            index = 0;
            ht.insert(zipcode,population);
        }
        //ht.printHashTable();
        //System.out.println("Total lines in csv: " + lines_in_csv);
        //System.out.println("Total keys in csv: " + ht.getSize());
        System.out.println("Average Length of Linked List: " + ht.average_length());
        System.out.println("Average collision: " + ht.average_collision());
    }

    private static int returnLineCountForFile(String path) throws IOException {
        int lineCount=0;
        BufferedReader reader = new BufferedReader(new FileReader(path));
        reader.readLine();
        while (reader.readLine() != null) {
            lineCount++;
        }
        return lineCount;
    }

}