

import java.io.IOException;
import java.util.ArrayList;


public interface Storage {

    void openCollection() throws IOException;

    boolean deleteFile(TextDoc textDoc);


    ArrayList<TextDoc> getSortedCollection(Sort sort);

    void createCollection(String rootPath);

    ArrayList<TextDoc> createFileList();
}