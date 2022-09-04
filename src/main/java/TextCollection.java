

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Stream;

public class TextCollection implements Storage, Serializable {
    @Serial
    private static final long serialVersionUID = 5154197581460058884L;
    File file;

    @Override
    public void add() {
        TextDoc textDoc = new TextDoc();
        System.out.println("Введите автора :");
        textDoc.setAuthor(Console.inputString());
        System.out.println("Введите текст  :");


        try {
            String text = Console.inputText();
            textDoc.setText(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Введите название файла  :");
        StringBuilder fileName = new StringBuilder();
        fileName.append(file);
        String x = Console.inputString();
        Console.returnInputResult(x);
        fileName.append(Console.inputString());
        fileName.append(".tdoc");
        System.out.println(fileName);
        textDoc.setName(fileName.toString());
        saveFile(fileName.toString(), textDoc);


    }

    public void saveFile(String fileName, TextDoc textDoc) {
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(textDoc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void view() {
        ArrayList<File> fileList = createFileList();
        for (File fileItem : fileList) {
            Character[] array = new Character[fileItem.toString().length()];
            for (int i = 0; i < fileItem.toString().length(); i++) {
                array[i] = fileItem.toString().charAt(i);
            }
            Stream.of(array).skip(2).forEach(System.out::print);
            System.out.print("\n");
        }
    }
    public void sortTextCollection (Storage storage){
        ArrayList<File> fileList = createFileList();
        ArrayList<TextDoc> textDocs = new ArrayList<>();
        for (File x : fileList) {
            textDocs.add(storage.openFile(x));
        }
        Menu menu4 = new Menu("Сортировка коллекции",false);
        menu4.add("Сортировка по длине текста",()->textDocs.stream().sorted(new Comparator<TextDoc>() {
            @Override
            public int compare(TextDoc o1, TextDoc o2) {
                if (o1.getText().length()<o2.getText().length()){return 1;}
                if (o1.getText().length()>o2.getText().length()){return -1;}
                return 0;
            }
        }).forEach((x)->System.out.println(x.getName())));

        menu4.add("Сортировка по дате создания",()->textDocs.stream().sorted(new Comparator<TextDoc>() {
            @Override
            public int compare(TextDoc o1, TextDoc o2) {
                if (o2.getDate().isAfter(o1.getDate())){return 1;}
                if (o2.getDate().isBefore(o1.getDate())){return -1;}
                return 0;
            }
        }).forEach((x)->System.out.println(x.getName())));

        menu4.add("Сортировка по автору",()->textDocs.stream().sorted(Comparator.comparing(TextDoc::getName)).
                forEach((x)->System.out.println(x.getName()+" "+x.getAuthor())));


        menu4.add("Выход",()->menu4.setExit(true));
        menu4.run();
    }


    public ArrayList<File> createFileList() {
        ArrayList<File> collection = new ArrayList<>();
        String regex = ".+tdoc$";
        for (File fileItem : file.listFiles()) {
            if (fileItem.toString().matches(regex))
                collection.add(fileItem);
        }
        return collection;

    }


    public void setFile(File file) {

        this.file = file;
    }

    public void setRoot() {
        System.out.println("Введите путь к коллекции текстовых файлов:");
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        File dir;
        do {
            dir = new File(string);
        }
        while (!dir.isDirectory() && !dir.exists());
        file = dir;
        save();

    }

    @Override
    public void save() {

        try (FileOutputStream fileOutputStream = new FileOutputStream("textcollection.tcol");
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        ) {
            objectOutputStream.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    public void showFileAtributes(Storage storage) {
        ArrayList<File> fileList = createFileList();
        Menu menu3 = new Menu("Какой файл коллекции открыть :", false);
        for (File x : fileList) {
            menu3.add(x.toString(), () -> {
                TextDoc textDoc = storage.openFile(x);
                System.out.println("Название "+x+textDoc.showAttributes());
            });
        }
        menu3.add("Выход", () -> menu3.setExit(true));
        menu3.run();
    }

    @Override
    public TextCollection open() {
        TextCollection textCollection;
        try (FileInputStream fileInputStream = new FileInputStream(new File("textcollection.tcol"));
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
            textCollection = (TextCollection) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException();
        }
        return textCollection;


    }

    @Override
    public void openFileFromCollection(Storage storage) {
        ArrayList<File> fileList = createFileList();
        Menu menu1 = new Menu("Какой файл коллекции открыть :", false);
        for (File x : fileList) {
            menu1.add(x.toString(), () -> {
                TextDoc textDoc = storage.openFile(x);

                textDoc.print();
            });
        }
        menu1.add("Выход", () -> menu1.setExit(true));
        menu1.run();

    }

    @Override
    public void deleteFile(Storage storage) {
        ArrayList<File> fileList = createFileList();
        Menu menu = new Menu("Какой файл коллекции удалить :", false);
        for (File x : fileList) {
            menu.add(x.toString(), () -> {
                if (x.delete()){System.out.println("Файл "+x+ "уcпешно удален");}
                menu.setExit(true);
            });
        }
        menu.add("Выход", () -> menu.setExit(true));
        menu.run();

    }


    @Override
    public TextDoc openFile(File file) {
        try (
                FileInputStream fileInputStream = new FileInputStream(file);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
            TextDoc textDoc = (TextDoc) objectInputStream.readObject();
            return textDoc;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException();
        }

    }



}