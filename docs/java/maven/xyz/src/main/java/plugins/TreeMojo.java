package plugins;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

@Mojo(name = "tree")
public class TreeMojo extends AbstractMojo {
    /**
     * Location of the file.
     */
    @Parameter(defaultValue = "${project.basedir}", property = "dir", required = false)
    private File basedir;

    void tree(StringBuilder stringBuilder,File file, int level){

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

    @Override
    public void execute() throws MojoExecutionException {

        StringBuilder builder = new StringBuilder();
        tree(builder,basedir,0);
        System.out.println(builder.toString());

    }
}
