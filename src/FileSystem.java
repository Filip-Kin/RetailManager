import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class FileSystem {
    public static class system {
        public static void load() {
            system.writeFile("price.ser", "data");
            system.writeFile("name.ser", "data");
            system.writeFile("inventory.ser", "data");
            system.writeFile("adminpasswords.ser", "data");
            system.writeFile("tax.ser", "data");
            FileSystem.inventorymaster.save();
            FileSystem.pricemaster.save();
            FileSystem.namemaster.save();
            FileSystem.adminpasswords.save();
            FileSystem.tax.save(7.0);
        }
        public static void writeFile(String fileName, String path){

            File directory = new File(path);
            if (! directory.exists()){
                directory.mkdirs();
            }

            File file = new File(path + "/" + fileName);
            try{
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("");
                bw.close();
            }
            catch (IOException e){
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
    public static class pricemaster {
        public static void load() {
            try {
                FileInputStream fis = new FileInputStream("data/price.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                main.pm = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException ioe) {
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                return;
            }
            System.out.println("Deserialized HashMap..");
            // Display content using Iterator
            Set set = main.pm.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry mentry = (HashMap.Entry) iterator.next();
                System.out.print("key: " + mentry.getKey() + " & Value: ");
                System.out.println(mentry.getValue());
            }
        }

        public static void reload() {
            main.pm.clear();
            FileSystem.pricemaster.load();
        }

        public static void unload() {
            main.pm.clear();
        }

        public static void save() {
            try {
                FileOutputStream fos = new FileOutputStream("data/price.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(main.pm);
                oos.close();
                fos.close();
                System.out.printf("Serialized HashMap data is saved in price.ser");
            } catch (IOException ioe) {
                
            }
        }
    }
    
    public static class namemaster {
        public static void load() {
            try {
                FileInputStream fis = new FileInputStream("data/name.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                main.nm = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException ioe) {
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                
                return;
            }
            System.out.println("Deserialized HashMap..");
            // Display content using Iterator
            Set set = main.nm.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry mentry = (HashMap.Entry) iterator.next();
                System.out.print("key: " + mentry.getKey() + " & Value: ");
                System.out.println(mentry.getValue());
            }
        }

        public static void reload() {
            main.nm.clear();
            FileSystem.namemaster.load();
        }

        public static void unload() {
            main.nm.clear();
        }

        public static void save() {
            try {
                FileOutputStream fos = new FileOutputStream("data/name.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(main.nm);
                oos.close();
                fos.close();
                System.out.printf("Serialized HashMap data is saved in name.ser");
            } catch (IOException ioe) {
                
            }
        }
    }

    public static class inventorymaster {
        public static void load() {
            try {
                FileInputStream fis = new FileInputStream("data/inventory.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                main.im = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException ioe) {
                
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                
                return;
            }
            System.out.println("Deserialized HashMap..");
            // Display content using Iterator
            Set set = main.im.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry mentry = (HashMap.Entry) iterator.next();
                System.out.print("key: " + mentry.getKey() + " & Value: ");
                System.out.println(mentry.getValue());
            }
        }

        public static void reload() {
            main.im.clear();
            FileSystem.inventorymaster.load();
        }

        public static void unload() {
            main.im.clear();
        }

        public static void save() {
            try {
                FileOutputStream fos = new FileOutputStream("data/inventory.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(main.im);
                oos.close();
                fos.close();
                System.out.printf("Serialized HashMap data is saved in inventory.ser");
            } catch (IOException ioe) {
                
            }
        }
    }

    public static class adminpasswords {
        public static void load() {
            try {
                FileInputStream fis = new FileInputStream("data/adminpasswords.ser");
                ObjectInputStream ois = new ObjectInputStream(fis);
                main.ap = (HashMap) ois.readObject();
                ois.close();
                fis.close();
            } catch (IOException ioe) {
                
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                
                return;
            }
            System.out.println("Deserialized HashMap..");
            // Display content using Iterator
            Set set = main.ap.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                HashMap.Entry mentry = (HashMap.Entry) iterator.next();
                System.out.print("key: " + mentry.getKey() + " & Value: ");
                System.out.println(mentry.getValue());
            }
        }

        public static void reload() {
            main.ap.clear();
            FileSystem.adminpasswords.load();
        }

        public static void unload() {
            main.ap.clear();
        }

        public static void save() {
            try {
                FileOutputStream fos = new FileOutputStream("data/adminpasswords.ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(main.ap);
                oos.close();
                fos.close();
                System.out.printf("Serialized HashMap data is saved in adminpasswords.ser");
            } catch (IOException ioe) {
                
            }
        }
    }

    public static class tax {
        public static double load() {
            try {
                FileReader taxfile = new FileReader("data/tax.ser");
                BufferedReader reader = new BufferedReader(taxfile);
                return Double.parseDouble(reader.readLine());
            } catch(Exception e) {
                main.print(e);
                return 0.065;
            }
        }

        public static void save(double tax) {
            try {
                FileWriter taxfile = new FileWriter("data/tax.ser");
                BufferedWriter writer = new BufferedWriter(taxfile);
                writer.write(String.valueOf(tax));
            } catch(Exception e) {
                main.print(e);
            }
        }
    }
}
