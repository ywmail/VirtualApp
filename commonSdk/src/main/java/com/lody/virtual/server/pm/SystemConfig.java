//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.lody.virtual.server.pm;

import android.util.Xml;
import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.FileUtils;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.helper.utils.XmlUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SystemConfig {
    private static final String TAG = "SystemConfig";
    private final Map<String, SharedLibraryEntry> mSharedLibraries = new HashMap();

    public SystemConfig() {
    }

    public void load() {
        long beforeTime = System.currentTimeMillis();
        File permissionsDir = new File("/etc/permissions/");
        this.readSharedLibraries(permissionsDir);
        long costTime = System.currentTimeMillis() - beforeTime;
        VLog.e(TAG, "load cost time: " + costTime + "ms.");
    }

    public SharedLibraryEntry getSharedLibrary(String name) {
        return (SharedLibraryEntry)this.mSharedLibraries.get(name);
    }

    private void readPermissionsFromXml(File permFile) {
        FileReader permReader;
        try {
            permReader = new FileReader(permFile);
        } catch (FileNotFoundException var16) {
            VLog.w(TAG, "Couldn't find or open permissions file " + permFile, new Object[0]);
            return;
        }

        VLog.i(TAG, "Reading permissions from " + permFile, new Object[0]);
        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(permReader);

            int type;
            while((type = parser.next()) != 2 && type != 1) {
            }

            if (type != 2) {
                throw new XmlPullParserException("No start tag found");
            }

            if (!parser.getName().equals("permissions") && !parser.getName().equals("config")) {
                throw new XmlPullParserException("Unexpected start tag in " + permFile + ": found " + parser.getName() + ", expected 'permissions' or 'config'");
            }

            while(true) {
                XmlUtils.nextElement(parser);
                if (parser.getEventType() == 1) {
                    break;
                }

                String name = parser.getName();
                if (name == null) {
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    byte var7 = -1;
                    switch (name.hashCode()) {
                        case 166208699:
                            if (name.equals("library")) {
                                var7 = 0;
                            }
                        default:
                            switch (var7) {
                                case 0:
                                    String lname = parser.getAttributeValue((String)null, "name");
                                    String lfile = parser.getAttributeValue((String)null, "file");
                                    String ldependency = parser.getAttributeValue((String)null, "dependency");
                                    if (lname == null) {
                                        VLog.w(TAG, "<" + name + "> without name in " + permFile + " at " + parser.getPositionDescription(), new Object[0]);
                                    } else if (lfile == null) {
                                        VLog.w(TAG, "<" + name + "> without file in " + permFile + " at " + parser.getPositionDescription(), new Object[0]);
                                    } else {
                                        SharedLibraryEntry entry = new SharedLibraryEntry(lname, lfile, ldependency == null ? new String[0] : ldependency.split(":"));
                                        this.mSharedLibraries.put(lname, entry);
                                    }

                                    XmlUtils.skipCurrentTag(parser);
                                    break;
                                default:
                                    XmlUtils.skipCurrentTag(parser);
                            }
                    }
                }
            }
        } catch (IOException | XmlPullParserException var17) {
        } finally {
            FileUtils.closeQuietly(permReader);
        }

    }

    public void readSharedLibraries(File permissionsDir) {
        File[] permissionFiles = permissionsDir.listFiles();
        if (permissionFiles != null) {
            File[] var3 = permissionFiles;
            int var4 = permissionFiles.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File permissionFile = var3[var5];
                if (permissionFile.isFile() && permissionFile.getName().endsWith(".xml")) {
                    this.readPermissionsFromXml(permissionFile);
                }
            }

        }
    }

    public static class SharedLibraryEntry {
        public String name;
        public String path;
        public String[] dependencies;

        public SharedLibraryEntry(String name, String path, String[] dependencies) {
            this.name = name;
            this.path = path;
            this.dependencies = dependencies;
        }
    }
}
