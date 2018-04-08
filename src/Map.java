import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.function.BiConsumer;

/*
* Map manager
* for Retail manager
*/

public class Map {
    public static class pricemaster {
        public static double get(String barcode) {
            return main.pm.get(barcode);
        }

        public static void put(String barcode, Double price) {
            main.pm.put(barcode, price);
        }

        public static void remove(String barcode) {
            main.pm.remove(barcode);
        }
    }

    public static class inventorymaster {
        public static int get(String barcode) {
            return main.im.get(barcode);
        }

        public static void put(String barcode, Integer amount) {
            if (main.im.containsKey(barcode)) {
                int prechange = inventorymaster.get(barcode);
                amount = prechange + amount;
            } //If barcode already exists add to that inventory
            main.im.put(barcode, amount);
        }

        public static void remove(String barcode, Integer amount) {
            int prechange = inventorymaster.get(barcode);
            int after = prechange - amount;
            main.im.remove(barcode);
            if (after > 0) {
                inventorymaster.put(barcode, after); //Add back amount after change
            } else {
                inventorymaster.put(barcode, 0); //Add back amount after change
                System.out.println(namemaster.get(barcode) + "(" + barcode + ") is now out of stock"); //Out of stock message
            }
        }

        public static void removecomplelty(String barcode) {
            namemaster.remove(barcode);
            pricemaster.remove(barcode);
            main.im.remove(barcode);
        }

        public static boolean check(String barcode) {
            return main.im.containsKey(barcode);
        }
    }

    public static class adminpasswords {
        public static boolean get(String password) {
            if(main.ap.containsKey(password)) {
                if (main.ap.get(password)) {
                    return true;
                }
            }
            return false;
        }

        public static void put(String password) {
            main.ap.put(password, true);
        }

        public static void remove(String password) {
            main.ap.remove(password);
        }
    }

    public static class namemaster {
        public static String get(String barcode) {
            return main.nm.get(barcode);
        }

        public static void put(String barcode, String name) {
            main.nm.put(barcode, name);
        }

        public static void remove(String barcode) {
            main.nm.remove(barcode);
        }
    }

    public static class cart {
        public static void put(String barcode, Integer amount) {
            double price = pricemaster.get(barcode); //Get price of item
            main.total = main.total + price * amount; //Calculate new total price
            if (main.cart.containsKey(barcode)) {
                amount = main.cart.get(barcode) + amount;
            } //If item already in cart add the amounts
            main.cart.put(barcode, amount);
        }

        public static void remove(String barcode, Integer amount) {
            int prechange = main.cart.get(barcode); //Get amount in cart
            int after = prechange - amount; //Find amount in cart after change
            double price = pricemaster.get(barcode) * amount; //Find price change
            main.total = main.total - price; //Update price
            main.cart.remove(barcode); //Remove from cart
            if (after > 0) {
                main.cart.put(barcode, after); //If change does not remove from cart completely add back the remains
            }
        } //Usage: remove(<barcode to update>, <amount to remove>)

        public static void clear() {
            main.cart.clear();
        }

        public static String export() {
            StringBuilder export = new StringBuilder("Thank you for shopping with us!\n"); //Start export string
            int sizeofcart = main.cart.size();
            if (sizeofcart<1) {
                return "No items in cart!";
            }
            main.cart.forEach(new BiConsumer<String, Integer>() {
                @Override
                public void accept(String s, Integer integer) {
                    String barcode = s;
                    int amount = integer;
                    inventorymaster.remove(barcode, amount); //Remove from inventory
                    String name = namemaster.get(barcode); //Get name
                    double price = pricemaster.get(barcode); //Get price
                    export.append("\n" + name + " $"); //Make new line with product name
                    if (amount > 1) {
                        export.append(price + " x " + amount + "  $" + (amount * price)); //If more then 1 of item add <price> x <amount>  $<total price>
                    } else {
                        export.append(price); //If only 1 of item add price
                    }
                }
            });
            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.CEILING);
            double tax = main.tax * main.total; //Calculate tax
            double total = tax + main.total; //Add tax to subtotal
            export.append("\n       Subtotal $" + df.format(main.total) + "\n         Tax " + main.tax * 100 + "%" + " $" + df.format(tax) + "\n          Total $" + df.format(total));
            cart.clear();
            main.total = 0.00;
            return export.toString();
        }
    }
}