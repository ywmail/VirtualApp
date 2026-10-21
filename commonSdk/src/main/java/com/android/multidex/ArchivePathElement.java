/*
 * Decompiled with CFR 0.152.
 */
package com.android.multidex;

import com.android.multidex.ClassPathElement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ArchivePathElement
implements ClassPathElement {
    private final ZipFile archive;

    public ArchivePathElement(ZipFile archive) {
        this.archive = archive;
    }

    @Override
    public InputStream open(String path) throws IOException {
        ZipEntry entry = this.archive.getEntry(path);
        if (entry == null) {
            throw new FileNotFoundException("File \"" + path + "\" not found");
        }
        if (entry.isDirectory()) {
            throw new DirectoryEntryException();
        }
        return this.archive.getInputStream(entry);
    }

    @Override
    public void close() throws IOException {
        this.archive.close();
    }

    @Override
    public Iterable<String> list() {
        return new Iterable<String>(){

            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>(){
                    Enumeration<? extends ZipEntry> delegate;
                    ZipEntry next;
                    {
                        this.delegate = ArchivePathElement.this.archive.entries();
                        this.next = null;
                    }

                    @Override
                    public boolean hasNext() {
                        while (this.next == null && this.delegate.hasMoreElements()) {
                            this.next = this.delegate.nextElement();
                            if (!this.next.isDirectory()) continue;
                            this.next = null;
                        }
                        return this.next != null;
                    }

                    @Override
                    public String next() {
                        if (this.hasNext()) {
                            String name = this.next.getName();
                            this.next = null;
                            return name;
                        }
                        throw new NoSuchElementException();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    static class DirectoryEntryException
    extends IOException {
        DirectoryEntryException() {
        }
    }
}

