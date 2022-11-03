package ru.netology;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        File file = new File("basket.txt");
        File data = new File("data.bin");
        File csv = new File("log.csv");
        File json = new File("basket.json");
        File json1 = new File("basket.json");
        ClientLog addingLog = new ClientLog();

        Basket basket = new Basket(
                Arrays.asList("Хлеб", "Яблоки", "Молоко", "Чай"),
                Arrays.asList(100, 200, 300, 400));

//        basket.rebuildBasketFromFile(file);
        basket.basketFromJson(json);

        System.out.println("Список возможных продуктов для покупки:");
        for (int i = 0; i < basket.getProducts().size(); i++) {
            System.out.println((i + 1) + ". " + basket.getProducts().get(i) +
                    " " + basket.getPrices().get(i) + " руб/шт");
        }

        int productNum = 0;
        int amount = 0;

        while (true) {
            System.out.println("Выберите пункт меню или ведите 'end':");
            System.out.println("1 Добавить товары в корзину");
            System.out.println("2 Вывести корзину на экран");
            System.out.println("3 Сохранить корзину в текстовый файл");
            System.out.println("4 Получить объект корзины из текстового файла");
            System.out.println("5 Сохранить файл в бинарном формате");
            System.out.println("6 Загрузить корзину из бинарного файла");

            String input = scanner.nextLine();
            if (input.equals("end")) {
                addingLog.exportAsCSV(csv);
                basket.basketInJson();
                break;
            }

            if (input.equals("1")) {
                System.out.println("Введите пункт товара, через пробел " +
                        "его количество. Чтобы выйти - 'end'. " +
                        "Вернуться в меню - 'Menu'");

                String input1 = scanner.nextLine();
                if (input1.equalsIgnoreCase("end")) {
                    break;
                } else if (input1.equalsIgnoreCase("Menu")) {
                    continue;
                }
                String[] parts = input1.split(" ");
                productNum = Integer.parseInt(parts[0]) - 1;
                amount = Integer.parseInt(parts[1]);
                basket.addTo(productNum, amount);
                addingLog.log(productNum, amount);
                basket.saveTxt(file);
            }

            if (input.equals("2")) {
                basket.printCart().forEach(System.out::println);
            }

            if (input.equals("3")) {
                basket.saveTxt(file);
            }

            if (input.equals("4")) {
                Basket basket1 = Basket.loadFromTxtFile(file);
                System.out.println(basket1);
            }

            if (input.equals("5")) {
                basket.saveBin(data);
            }

            if (input.equals("6")) {
                Basket.loadFromBinFile(data);
            }
        }
    }
}