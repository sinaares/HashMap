import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

public class File {
    private String file;

    public File(String file){
        this.file = file;
    }


    public void read(MotherHashMap table){    //method for reading the file and put them in to the hash table
        String line = null;
        try {
            BufferedReader text = new BufferedReader(new FileReader(file));
            String[] list = new String[10];
            text.readLine();
            while ((line = text.readLine()) != null) {
                list = line.split(",");
                list[2] = list[2].replaceAll("-" , "/");

                Customer customer = (Customer) table.getValue(list[0]);
                if(customer == null){
                    customer = new Customer(list[0] , list[1]);
                    table.add(list[0] , customer);
                }

                customer.addSell(new Date(list[2]) , list[3]);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void search(MotherHashMap table){      // method looking for the keys in the file if they are exsits or not
        String line = null;
        int a = 0;
        try {
            BufferedReader text = new BufferedReader(new FileReader(file));
            String id = null;
            while ((line = text.readLine()) != null){
                id = line;
                System.out.println(table.getValue(id));
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void remove(MotherHashMap table){    // method for rempving the entry from the table by reading the file
        String line = null;
        try {
            BufferedReader text = new BufferedReader(new FileReader(file));
            String id = null;
            while ((line = text.readLine()) != null){
                id = line;
                System.out.println(table.remove(id));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
