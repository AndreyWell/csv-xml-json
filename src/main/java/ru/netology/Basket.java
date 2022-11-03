package ru.netology;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Basket implements Serializable {
    private List<String> products;
    private List<Integer> prices;
    private int[] basketSum;

    public Basket(List<String> products, List<Integer> prices) {
        this.products = products;
        this.prices = prices;
        this.basketSum = new int[products.size()];
    }

    public Basket() {
    }

    public List<String> getProducts() {
        return products;
    }

    public void setProducts(List<String> products) {
        this.products = products;
    }

    public List<Integer> getPrices() {
        return prices;
    }

    public void setPrices(List<Integer> prices) {
        this.prices = prices;
    }

    public int[] getBasketSum() {
        return basketSum;
    }

    // Добавление в корзину amount штук продукта номер productNum
    public void addTo(int productNum, int amount) {
        basketSum[productNum] += amount;
    }

    // Вывод на экран покупательской корзины
    public List<String> printCart() {
        int count = 0;
        List<String> printCart = new ArrayList<>();
        int sumProducts = 0;
        for (int i = 0; i < basketSum.length; i++) {
            if (basketSum[i] == 0) {
                continue;
            }
            count++;
            printCart.add(count + ". " + getProducts().get(i) + " по цене: " + getPrices().get(i)
                    + " руб/шт, количество: " + basketSum[i] + " - сумма: "
                    + (getPrices().get(i) * basketSum[i]) + " руб");
            sumProducts += getPrices().get(i) * basketSum[i];
        }
        printCart.add(0, "Ваша корзина:");
        printCart.add("Итого - " + sumProducts + " руб");

        if (printCart.size() == 2) {
            printCart.clear();
            printCart.add("Корзина пустая");
        }
        return printCart;
    }

    // Сохранение корзины в текстовый файл
    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile)) {
            if (printCart().size() == 1) {
                System.out.println("Корзина пустая");
            }

            for (String b : printCart()) {
                out.println(b);
            }
        }
    }

    // Восстановление объекта корзины
    public static Basket loadFromTxtFile(File textFile) throws Exception {
        Basket rebuild = new Basket();
        if (textFile.canRead()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {
                List<String> rebuildProducts = new ArrayList<>();
                List<Integer> rebuildPrices = new ArrayList<>();
                List<String> readBasket = new ArrayList<>();
                String s;
                while ((s = reader.readLine()) != null) {
                    readBasket.add(s);
                }
                if (!readBasket.isEmpty()) {
                    int count = 0;
                    for (String s1 : readBasket) {
                        count++;
                        if (count == 1 || count == readBasket.size()) {
                            continue;
                        }

                        String[] s2 = s1.trim().split(" ");
                        String products = s2[1];
                        int prices = Integer.parseInt(s2[4]);
                        rebuildProducts.add(products);
                        rebuildPrices.add(prices);
                    }
                }
                rebuild.setProducts(rebuildProducts);
                rebuild.setPrices(rebuildPrices);
            }
        }
        return rebuild;
    }

    // Восстановление ранее сформированной корзины из текущего файла
    public void rebuildBasketFromFile(File basket) throws Exception {
        if (basket.canRead()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(basket))) {
                List<String> readBasket = new ArrayList<>();
                String s;
                while ((s = reader.readLine()) != null) {
                    readBasket.add(s);
                }
                if (!readBasket.isEmpty()) {
                    int count = 0;
                    for (String s1 : readBasket) {
                        count++;
                        if (count == 1 || count == readBasket.size()) {
                            continue;
                        }

                        String[] s2 = s1.trim().split(" ");
                        for (int i = 0; i < getProducts().size(); i++) {
                            if (getProducts().get(i).equals(s2[1])) {
                                int amount = Integer.parseInt(s2[7]);
                                addTo(i, amount);
                            }
                        }
                    }
                }
            }
        }
    }

    // Запись в бинарном формате через сериализацию
    public void saveBin(File file) throws Exception {
        File txtBasket = new File("basket.txt");
        if (txtBasket.canRead()) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(loadFromTxtFile(txtBasket));
            }
        } else {
            System.out.println("Корзина пустая");
        }
    }

    // Чтение корзины из бинарного файла
    public static void loadFromBinFile(File file) throws Exception {
        Basket basket1;
        if (file.canRead()) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                basket1 = (Basket) in.readObject();
                System.out.println(basket1);
            }
        } else {
            System.out.println("Корзина пустая");
        }
    }

    @Override
    public String toString() {
        return "Basket{" +
                "products=" + products +
                ", prices=" + prices +
                '}';
    }
}
