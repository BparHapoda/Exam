

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public class TextCollection implements Storage {
    private String rootPath;
    private final String configFileName = "textcollection.tcol";

    public void saveFile(TextDoc textDoc) {
        try (FileOutputStream fos = new FileOutputStream(rootPath  + textDoc.getName());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(textDoc);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<TextDoc> getSortedCollection(){
        return getSortedCollection(sort.NAME);
    }

    public ArrayList<TextDoc> getSortedCollection(sort sort){
        ArrayList<TextDoc> textDocs = createFileList();

        ArrayList<TextDoc> sortedDocs = new ArrayList<>();

       switch (sort) {
          case LENGTH:
                sortedDocs = (ArrayList<TextDoc>) textDocs.stream().sorted(new Comparator<TextDoc>() {
                   @Override
                   public int compare(TextDoc o1, TextDoc o2) {
                       if (o1.getText().length()<o2.getText().length()){return 1;}
                        if (o1.getText().length()>o2.getText().length()){return -1;}
                        return 0;
                    }
             }).collect(Collectors.toList());
               break;
            case AUTHOR:
               sortedDocs = (ArrayList<TextDoc>) textDocs.stream().
                       sorted(Comparator.comparing(TextDoc::getName)).collect(Collectors.toList());
                break;
            case DATE:
               sortedDocs = (ArrayList<TextDoc>) textDocs.stream().sorted(new Comparator<TextDoc>() {
                   @Override
                    public int compare(TextDoc o1, TextDoc o2) {
                       if (o2.getDate().isAfter(o1.getDate())){return 1;}
                       if (o2.getDate().isBefore(o1.getDate())){return -1;}
                       return 0;
                 }
             }).collect(Collectors.toList());
             break;
         case NAME:
             sortedDocs=textDocs;

        }

        return sortedDocs;
    }


    public ArrayList<TextDoc> createFileList() {
        ArrayList<TextDoc> collection = new ArrayList<>();
        String regex = ".+tdoc$";
        for (File fileItem : (new File(rootPath)).listFiles()) {
            if (fileItem.toString().matches(regex))
                collection.add(openFile(fileItem));
        }
        return collection;
    }

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
    public boolean isCollectionExists() {
        File file = new File(configFileName);
        return file.exists();
    }

    @Override
    public void createCollection(String rootPath) {
        try (FileWriter fileWriter = new FileWriter("textcollection.tcol");
        ) {
            fileWriter.write(rootPath);
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    @Override
    public void openCollection() throws IOException {
        rootPath = Files.readString(Paths.get(configFileName));
    }

    @Override
    public boolean deleteFile(TextDoc doc) {
        return getFile(doc).delete();
    }

    private File getFile(TextDoc textDoc) {
        return new File(rootPath + textDoc.getName());
    }
}

enum sort {
    DATE,
    AUTHOR,
    LENGTH,
    NAME,
}