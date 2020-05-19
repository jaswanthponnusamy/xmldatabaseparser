import java.io.File;

public class GetPath {

    public static String path="";

    public static String getPath() {

        File f = new File("");
        path=f.getAbsolutePath();

        return path;
    }

}