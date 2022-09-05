


import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;



public class ConsoleApplication {
    private TextStorage textCollection;

    public ConsoleApplication() {
        textCollection = new TextStorage();
    }

    public void run() throws IOException {
        if (!textCollection.isCollectionExists()) {
            System.out.println("Cоздайте новую коллекцию текстовых файлов,задав ей корневую папку.");
            String rootPath = inputRootDirName();
            textCollection.createCollection(rootPath);
        }

        textCollection.openCollection();

        Menu menu = createMenuApplication();
        menu.run();
    }

    public String inputRootDirName() throws IOException {
        System.out.println("Введите путь к коллекции текстовых файлов:");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String string = scanner.nextLine();
            File dir = new File(string);
            if (!dir.isDirectory() || !dir.exists()) {
                System.out.println("Такого каталога нет - повторите ввод:");
            } else {
                return string;
            }
        }
    }

    public Menu createMenuApplication() {
        Menu menu = new Menu("Текстовая коллекция version 0.9" +
                "\n================================", false);
        menu.add("Добавить текстовый документ", this::addDocument);
        menu.add("Открыть текстовый документ", this::openDocument);
        menu.add("Задать корневую папку коллекции", this::changeRootDir);
        menu.add("Показать коллекцию", this::showCollection);
        menu.add("Показать свойства файла", this::showFileAtributes);
        menu.add("Удалить файл", this::deleteFile);
        menu.add("Сортировка коллекции", this::sortTextCollection);
        menu.add("Выход", () -> {
            menu.setExit(true);
        });
        return menu;
    }

    public void addDocument() {
        TextDoc textDoc = new TextDoc();

        System.out.println("Введите автора:");
        textDoc.setAuthor(Console.inputString());

        System.out.println("Введите текст:");
        try {
            textDoc.setText(Console.inputText());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Введите название файла  :");
        textDoc.setName(Console.inputString() + ".tdoc");

        textCollection.saveFile(textDoc);
    }

    private void openDocument() {
        ArrayList<TextDoc> fileList = textCollection.createFileList();
        Menu menu1 = new Menu("Какой файл коллекции открыть :", false);
        for (TextDoc textDoc : fileList) {
            menu1.add(textDoc.getName(), () -> {
                Console console = new Console(12, 100);
                console.outputPageText(console.create(textDoc.getText()),true);
            });
        }
        menu1.add("Выход", () -> menu1.setExit(true));
        menu1.run();
    }

    private void changeRootDir(){
        try {
            textCollection.createCollection(inputRootDirName());
            textCollection.openCollection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private Console getConsole() {
        return new Console(12, 100);
    }

    private void showCollection(Sort sort) {
        for (TextDoc textDoc : textCollection.getSortedCollection(sort)) {
            System.out.print(textDoc.getName() + "\n");
        }
    }

    private void showCollection() {
        showCollection(Sort.NAME);
    }

    public void showFileAtributes() {
        ArrayList<TextDoc> fileList = textCollection.createFileList();
        Menu menu3 = new Menu("Атрибуты какого файла показать  :", false);
        for (TextDoc x : fileList) {
            menu3.add(x.getName(), () -> {
                System.out.println("Название "+ x.getName() + ": " + x.showAttributes());
            });
        }
        menu3.add("Выход", () -> menu3.setExit(true));
        menu3.run();
    }

    private void deleteFile() {
        ArrayList<TextDoc> fileList = textCollection.createFileList();
        Menu menu = new Menu("Какой файл коллекции удалить :", false);
        for (TextDoc x : fileList) {
            menu.add(x.getName(), () -> {
                if (textCollection.deleteFile(x)){System.out.println("Файл " + x.getName() + "уcпешно удален");}
                menu.setExit(true);
            });
        }
        menu.add("Выход", () -> menu.setExit(true));
        menu.run();
    }

    public void sortTextCollection (){
        Menu menu4 = new Menu("Сортировка коллекции",false);
        menu4.add("Сортировка по имени",()->showCollection(Sort.NAME));
        menu4.add("Сортировка по длине текста",()->showCollection(Sort.LENGTH));
        menu4.add("Сортировка по дате создания",()->showCollection(Sort.DATE));
        menu4.add("Сортировка по автору",()->showCollection(Sort.AUTHOR));
        menu4.add("Выход",()->menu4.setExit(true));
        menu4.run();
    }
}