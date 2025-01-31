public class Main {
    public static void main(String[] args) {

        Long starttime = System.nanoTime();

        MotherHashMap table = new SSFDH();
        File text = new File("supermarket_dataset_50k.csv");

        text.read(table);

        File search = new File("customer_1K.txt");

        search.search(table);
        search.remove(table);

        Long endtime = System.nanoTime();
        double avragetime = (endtime - starttime) / 1000000000;

        System.out.println(avragetime);
    }
}