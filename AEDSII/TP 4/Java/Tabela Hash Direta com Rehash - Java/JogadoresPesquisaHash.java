import java.io.*;
import java.util.Scanner;

public class JogadoresPesquisaHash {

    static void searchAndUpdateHashTable(String playerId, HashTable hashTable) {
        try {
            final String fileName = "/tmp/players.csv";
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            Player player = new Player();
            String currentLine;
            String[] splitted;

            while ((currentLine = bufferedReader.readLine()) != null) {
                splitted = currentLine.split(",", -1);
                if (playerId.equals(splitted[0])) {
                    player.setAll(splitted);
                    hashTable.insert(player.getName());
                }
            }

            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        final long startTime = System.currentTimeMillis();
        String playerId;
        HashTable hashTable = new HashTable();

        while (!(playerId = scanner.nextLine()).equals("FIM")) {
            searchAndUpdateHashTable(playerId, hashTable);
        }

        while (!(playerId = scanner.nextLine()).equals("FIM")) {
            searchAndPrintResult(playerId, hashTable.getTable());
        }

        final String outputFileName = "1461656_hashRehash.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            writer.write("1461656\t");
            writer.write("Time:" + (System.currentTimeMillis() - startTime) + "\t");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchAndPrintResult(String playerId, String[] table) {
        String result = "NAO";
        for (String element : table) {
            if (playerId.equals(element)) {
                result = "SIM";
                break;
            }
        }
        System.out.println(playerId + " " + result);
    }
}

class Player {
    private int id;
    private String name;
    private int height;
    private int weight;
    private String university;
    private int birthYear;
    private String birthCity;
    private String birthState;

    Player() {
    }

    public String getName() {
        return name;
    }

    void setAll(String[] array) {
        id = Integer.parseInt(array[0]);
        name = array[1].isEmpty() ? "not specified" : array[1];
        height = Integer.parseInt(array[2]);
        weight = Integer.parseInt(array[3]);
        university = array[4].isEmpty() ? "not specified" : array[4];
        birthYear = Integer.parseInt(array[5]);
        birthCity = array[6].isEmpty() ? "not specified" : array[6];
        birthState = array[7].isEmpty() ? "not specified" : array[7];
    }
}

class HashTable {
    String[] table;
    int size;
    final String NULL_VALUE = "-1";

    HashTable() {
        this(35);
    }

    HashTable(int size) {
        this.size = size;
        table = new String[size];
        for (int i = 0; i < size; i++) {
            table[i] = NULL_VALUE;
        }
    }

    public int hash(String element) {
        return Math.abs(element.hashCode()) % size;
    }

    public int rehash(String element) {
        return Math.abs(element.hashCode() + 1) % size;
    }

    public void insert(String player) {
        String element = player;
        if (!element.equals(NULL_VALUE)) {
            int position = hash(element);
            if (table[position] == NULL_VALUE) {
                table[position] = element;
            } else {
                position = rehash(element);
                if (table[position] == NULL_VALUE) {
                    table[position] = element;
                }
            }
        }
    }

    public String[] getTable() {
        return table;
    }
}
