/*
 * Decompiled with CFR 0.152.
 */
package com.android.multidex;

import com.android.multidex.ClassPathElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

class FolderPathElement
implements ClassPathElement {
    private final File baseFolder;

    public FolderPathElement(File baseFolder) {
        this.baseFolder = baseFolder;
    }

    @Override
    public InputStream open(String path) throws FileNotFoundException {
        return new FileInputStream(new File(this.baseFolder, path.replace('/', File.separatorChar)));
    }

    @Override
    public void close() {
    }

    @Override
    public Iterable<String> list() {
        ArrayList<String> result = new ArrayList<String>();
        this.collect(this.baseFolder, "", result);
        return result;
    }

    private void collect(File folder, String prefix, ArrayList<String> result) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                this.collect(file, prefix + '/' + file.getName(), result);
                continue;
            }
            result.add(prefix + '/' + file.getName());
        }
    }
}

