import java.util.HashMap;
import java.util.Scanner;


public class main {
    //Variables
    public static void print(Object text) {
        System.out.println(text);
    }
    public static String version = "v1.5.0";
    public static HashMap<String, Double> pm = new HashMap<>(); //price master
    public static HashMap<String, Integer> im = new HashMap<>(); //inventory master
    public static HashMap<String, String> nm = new HashMap<>(); //name master
    public static HashMap<String, Boolean> ap = new HashMap<>(); //admin passwords
    public static HashMap<String, Integer> cart = new HashMap<>(); //list of items in checkout
    public static Double total = 0.00;
    public static Double tax = FileSystem.tax.load();
    public static boolean admin = true;

    public static void main(String args[]) {
        //Exit on command
        boolean exit = false;
        //Define help command
        String help = Color.text.BLUE + "---==Help==---\n" + Color.text.CYAN +
                "<barcode> [amount] >> Add item to checkout\n" +
                "checkout >> End session and return total\n" +
                "clear >> Clear cart\n" +
                "remove <barcode> [amount] >> Remove item from cart\n" +
                "price <barcode> [amount] >> Get price of item\n" +
                "stock <barcode> >> Get amount of item in stock\n\n" +
                Color.text.RED + "---==Admin access required==---\n" +
                Color.text.GREEN + "admin <add/remove> >> Add/Remove admin password\n" +
                Color.text.PURPLE + "addstock <barcode> <amount added> >> Add items from inventory\n" +
                "removestock <barcode> <amount removed> >> Remove items from inventory\n" +
                Color.text.CYAN + "sysremove <barcode> >> Remove item from system\n" +
                "sysadd <barcode> <amount> <price> >> Add item to system\n" +
                Color.text.GREEN + "setname <barcode> <name> >> Set name of an item\n" +
                "setprice <barcode> <price> >> Set price of an item\n" +
                "settax <percent> >> Set sales tax rate\n" +
                "save <passwords/inventory/names/prices/all> >> Save files\n" +
                Color.text.BLUE + "load <passwords/inventory/names/prices/all> >> (Re)load files\n" +
                "clear <passwords/inventory/names/prices/all> >> Clear internal files (does not delete saved files unless you exit or save over them)\n" +
                Color.text.RESET + "logout >> Log out of admin account\n" +
                "exit >> Close program safely";

        //Load up script
        print("Starting " + version + "\n\nLoading Files");
        print("\nCreating files if they don't exist");
        FileSystem.system.load();
        print("\nLoading price master");
        FileSystem.pricemaster.load();
        print(pm.values());
        print("Loading inventory master");
        FileSystem.inventorymaster.load();
        print(im.values());
        print("Loading admin passwords");
        FileSystem.adminpasswords.load();
        print(ap.values());
        print("Loading name master");
        FileSystem.namemaster.load();
        print(nm.values());
        print("\nType 'help' for commands");

        //Setup scanner
        Scanner scObject = new Scanner(System.in);

        //Command loop
        while (!exit) {
            String command = scObject.nextLine();
            String[] parts = command.split(" ");
            if (command.toLowerCase().contains("help")) { //Help
                print(help);
            } else if (command.toLowerCase().contains("checkout")) { //Checkout
                print(Map.cart.export());
            } else if (command.toLowerCase().contains("clear")) { //Clear cart
                print(Color.text.GREEN + "Cart cleared");
                Map.cart.clear();
            } else if (command.toLowerCase().contains("remove")) { //Remove from cart
                if(nm.containsKey(parts[1])){
                    String name = Map.namemaster.get(parts[1]);
                    if(parts.length == 3){
                        int amount = Integer.parseInt(parts[3]);
                        Map.cart.remove(parts[1], amount);
                        print(Color.text.GREEN + "Removed " + amount + " of " + name + " from cart");
                    } else {
                        Map.cart.remove(parts[1], cart.get(parts[1]));
                        print(Color.text.GREEN + "Removed " + name + " from cart");
                    }
                }
            } else if (command.toLowerCase().contains("price")) { //Price check
                if(pm.containsKey(parts[1])) {
                    print(Color.text.GREEN + Map.namemaster.get(parts[1]) + " $" + Map.pricemaster.get(parts[1]));
                } else {
                    print(Color.text.RED + "Item does not exist!");
                }
            } else if (command.toLowerCase().contains("addstock")) { //Add items to inventory
                if(admin()) {
                    if (pm.containsKey(parts[1])) {
                        Map.inventorymaster.put(parts[1], Integer.parseInt(parts[2]));
                        print(Color.text.GREEN + Map.namemaster.get(parts[1]) + " " + Map.inventorymaster.get(parts[1]) + " in stock");
                    } else {
                        print(Color.text.RED + "Item does not exist!");
                    }
                }
            } else if (command.toLowerCase().contains("removestock")) { //Remove items from inventory
                if(admin()) {
                    if (pm.containsKey(parts[1])) {
                        Map.inventorymaster.remove(parts[1], Integer.parseInt(parts[2]));
                        print(Color.text.GREEN + Map.namemaster.get(parts[1]) + " " + Map.inventorymaster.get(parts[1]) + " in stock");
                    } else {
                        print(Color.text.RED + "Item does not exist!");
                    }
                }
            } else if (command.toLowerCase().contains("stock")) { //Inventory check
                if(pm.containsKey(parts[1])) {
                    print(Color.text.GREEN + Map.namemaster.get(parts[1]) + " " + Map.inventorymaster.get(parts[1]) + " in stock");
                } else {
                    print(Color.text.RED + "Item does not exist!");
                }
            } else if (command.toLowerCase().contains("admin")) { //Add/Remove admin password
                if(parts.length == 2) {
                    if (admin()) {
                        print("Enter admin password you want to add/remove");
                        Scanner scObjectTwo = new Scanner(System.in);
                        String password = scObjectTwo.nextLine();
                        String change = parts[1];
                        if (change.toLowerCase() == "add") {
                            Map.adminpasswords.put(password);
                        } else {
                            Map.adminpasswords.remove(password);
                        }
                        print(Color.text.GREEN + "Password added!");
                    }
                } else {
                    print("Missing add/remove arugment");
                }
            } else if (command.toLowerCase().contains("sysremove")) { //Remove item from system
                if(admin()) {
                    if (pm.containsKey(parts[1])) {
                        Map.inventorymaster.removecomplelty(parts[1]);
                        print(Color.text.GREEN + "Item removed!");
                    } else {
                        print(Color.text.RED + "Item does not exist!");
                    }
                }
            } else if (command.toLowerCase().contains("sysadd")) { //Add to system
                if(admin()) {
                    String barcode = parts[1];
                    int amount = Integer.parseInt(parts[2]);
                    double price = Double.parseDouble(parts[3]);
                    print("Added: " + barcode + " " + amount + "qty $" + price);
                    Map.pricemaster.put(barcode, price);
                    Map.inventorymaster.put(barcode, amount);
                }
            } else if (command.toLowerCase().contains("setname")) { //Setname of item
                if(admin()) {
                    if (pm.containsKey(parts[1])) {
                        Map.namemaster.put(parts[1], parts[2]);
                        print(Color.text.GREEN + Map.namemaster.get(parts[1]) + " renamed");
                    } else {
                        print(Color.text.RED + "Item does not exist!");
                    }
                }
            } else if (command.toLowerCase().contains("setprice")) { //Setprice of item
                if(admin()) {
                    if (pm.containsKey(parts[1])) {
                        Map.pricemaster.put(parts[1], Double.parseDouble(parts[2]));
                        print(Color.text.GREEN + Map.namemaster.get(parts[1]) + " now costs $" + Map.pricemaster.get(parts[1]));
                    } else {
                        print(Color.text.RED + "Item does not exist!");
                    }
                }
            } else if (command.toLowerCase().contains("settax")) { //Set tax for store
                if(admin()) {
                    tax = Double.parseDouble(parts[1]);
                }
            } else if (command.toLowerCase().contains("save")) { //Save stuff
                if(admin()) {
                    print("Saving inventory master");
                    FileSystem.inventorymaster.save();
                    print("\nSaving price master");
                    FileSystem.pricemaster.save();
                    print("\nSaving name master");
                    FileSystem.namemaster.save();
                    print("\nSaving admin passwords");
                    FileSystem.adminpasswords.save();
                    FileSystem.tax.save(tax);
                }
            } else if (command.toLowerCase().contains("load")) { //Load stuff
                if(admin()) {
                    print("Saving inventory master");
                    FileSystem.inventorymaster.load();
                    print("\nSaving price master");
                    FileSystem.pricemaster.load();
                    print("\nSaving name master");
                    FileSystem.namemaster.load();
                    print("\nSaving admin passwords");
                    FileSystem.adminpasswords.load();
                    FileSystem.tax.load();
                }
            } else if (command.toLowerCase().contains("clear")) { //Clear hashmaps

            } else if (command.toLowerCase().contains("logout")) { //Logout
                print(Color.text.CYAN + "Logged out successfully");
            } else if (command.toLowerCase().contains("exit")) {
                print("Saving inventory master");
                FileSystem.inventorymaster.save();
                print("\nSaving price master");
                FileSystem.pricemaster.save();
                print("\nSaving name master");
                FileSystem.namemaster.save();
                print("\nSaving admin passwords");
                FileSystem.adminpasswords.save();
                FileSystem.tax.save(tax);
                exit = true;
            } else {
                if (Map.inventorymaster.check(command)) { //Check if barcode actually exists
                    Map.cart.put(command, 1);
                    print(Color.text.GREEN + Map.namemaster.get(command) + " $" + Map.pricemaster.get(command));
                } else {
                    print(Color.background.RED + "Item not found!");
                }
            }
        }
    }
    public static boolean admin(){
        if(!admin) {
            print("Enter admin password to perform this function");
            Scanner scObjectTwo = new Scanner(System.in);
            String password = scObjectTwo.nextLine();
            if (Map.adminpasswords.get(password)) {
                admin = true;
                scObjectTwo.close();
                print(Color.text.GREEN + "Password correct! Performing action, remember to logout!");
                return true;
            } else {
                print(Color.background.RED + "Password incorrect!");
                scObjectTwo.close();
                return false;
            }
        } else {
            return true;
        }
    }
}
