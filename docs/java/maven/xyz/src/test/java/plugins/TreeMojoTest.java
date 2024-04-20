package plugins;

import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class TreeMojoTest {


    void tree(StringBuilder stringBuilder,File file, int level){

        // ├─ 文件夹
        // └─ 结尾
        String flag = "";
        if(level > 1){
            flag = new String(new char[level - 1]).replace("\0", "  ");
        }

        if(file.isDirectory()){
            if(level == 0){
                stringBuilder.append(file.getAbsolutePath()).append("\n");
            }else{
                stringBuilder.append(flag + "├─"+file.getName()).append("\n");
            }

            File[] files = file.listFiles();

            Arrays.sort(Objects.requireNonNull(files), (f1, f2) -> {
                if (f1.isDirectory() && !f2.isDirectory()) {
                    return -1;
                } else if (!f1.isDirectory() && f2.isDirectory()) {
                    return 1;
                } else {
                    return f1.compareTo(f2);
                }
            });

            for(File temp:files){
                tree(stringBuilder,temp,level+1);
            }

        }else {
            stringBuilder.append(flag + "│ "+file.getName()).append("\n");
        }
    }

    @Test
    public void treee1() {
        StringBuilder stringBuilder = new StringBuilder();
        tree(stringBuilder,new File("F:\\study\\2000-code\\java\\java-code\\01-examples\\xyz"),0);
        System.out.printf(stringBuilder.toString());
    }
}