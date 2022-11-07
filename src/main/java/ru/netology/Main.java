package ru.netology;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        File data = new File("data.bin");
        File xml = new File("shop.xml");
        ClientLog addingLog = new ClientLog();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xml);

        List<Map<String, String>> load = workingMode(doc, "load");
        List<Map<String, String>> save = workingMode(doc, "save");
        List<Map<String, String>> log = workingMode(doc, "log");

        Basket basket = new Basket(
                Arrays.asList("Хлеб", "Яблоки", "Молоко", "Чай"),
                Arrays.asList(100, 200, 300, 400));

        // Блок: load
        File json = new File((load.get(0).get("fileName")));
        File file = new File((load.get(0).get("fileName")));
        if (load.get(0).get("enabled").equals("true")) {
            if (load.get(0).get("format").equals("json")) {
                basket.basketFromJson(json);
            } else if (load.get(0).get("format").equals("text")) {
                basket.rebuildBasketFromFile(file);
            }
        }

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

            File csv = new File(log.get(0).get("fileName"));
            String input = scanner.nextLine();
            if (input.equals("end")) {
                // Блок: log
                if (log.get(0).get("enabled").equals("true")) {
                    addingLog.exportAsCSV(csv);
                }
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
                // Блок: save
                if (save.get(0).get("enabled").equals("true")) {
                    if (save.get(0).get("format").equals("json")) {
                        basket.basketInJson(json);
                    } else if (save.get(0).get("format").equals("text")) {
                        basket.saveTxt(file);
                    }
                }

                if (log.get(0).get("enabled").equals("true")) {
                    addingLog.log(productNum, amount);
                }
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

    public static List<Map<String, String>> workingMode(Document doc, String tag) {
        List<Map<String, String>> listSettings = new ArrayList<>();
        Map<String, String> map = new TreeMap<>();
        NodeList nodeList = doc.getElementsByTagName(tag);
        Node currentNode = nodeList.item(0);
        Element element1 = (Element) currentNode;
        for (int i = 0; i < element1.getChildNodes().getLength(); i++) {
            if (element1.getChildNodes().item(i).getChildNodes().item(0) != null) {
                String nodeName = element1.getChildNodes().item(i).getNodeName();
                String textContent = element1.getChildNodes().item(i).getTextContent();
                map.put(nodeName, textContent);
            }
        }
        listSettings.add(map);
        return listSettings;
    }
}