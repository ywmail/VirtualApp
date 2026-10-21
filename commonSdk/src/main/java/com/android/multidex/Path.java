/*
 * Decompiled with CFR 0.152.
 */
package com.android.multidex;

import com.android.dx.cf.direct.DirectClassFile;
import com.android.dx.cf.direct.StdAttributeFactory;
import com.android.multidex.ArchivePathElement;
import com.android.multidex.ClassPathElement;
import com.android.multidex.FolderPathElement;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

class Path {
    List<ClassPathElement> elements = new ArrayList<ClassPathElement>();
    private final String definition;
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream(40960);
    private final byte[] readBuffer = new byte[20480];

    static ClassPathElement getClassPathElement(File file) throws ZipException, IOException {
        if (file.isDirectory()) {
            return new FolderPathElement(file);
        }
        if (file.isFile()) {
            return new ArchivePathElement(new ZipFile(file));
        }
        if (file.exists()) {
            throw new IOException("\"" + file.getPath() + "\" is not a directory neither a zip file");
        }
        throw new FileNotFoundException("File \"" + file.getPath() + "\" not found");
    }

    Path(String definition) throws IOException {
        this.definition = definition;
        for (String filePath : definition.split(Pattern.quote(File.pathSeparator))) {
            try {
                this.addElement(Path.getClassPathElement(new File(filePath)));
            }
            catch (IOException e) {
                throw new IOException("Wrong classpath: " + e.getMessage(), e);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private static byte[] readStream(InputStream in, ByteArrayOutputStream baos, byte[] readBuffer) throws IOException {
        try {
            int amt;
            while ((amt = in.read(readBuffer)) >= 0) {
                baos.write(readBuffer, 0, amt);
            }
        }
        finally {
            in.close();
        }
        return baos.toByteArray();
    }

    public String toString() {
        return this.definition;
    }

    Iterable<ClassPathElement> getElements() {
        return this.elements;
    }

    private void addElement(ClassPathElement element) {
        assert (element != null);
        this.elements.add(element);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    synchronized DirectClassFile getClass(String path) throws FileNotFoundException {
        DirectClassFile classFile = null;
        for (ClassPathElement element : this.elements) {
            try (InputStream in = element.open(path);){
                byte[] bytes = Path.readStream(in, this.baos, this.readBuffer);
                this.baos.reset();
                classFile = new DirectClassFile(bytes, path, false);
                classFile.setAttributeFactory(StdAttributeFactory.THE_ONE);
                break;
            }
            catch (IOException iOException) {
            }
        }
        if (classFile == null) {
            throw new FileNotFoundException("File \"" + path + "\" not found");
        }
        return classFile;
    }
}

