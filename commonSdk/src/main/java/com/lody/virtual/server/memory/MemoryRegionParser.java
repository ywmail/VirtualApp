/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.memory;

import com.lody.virtual.StringFog;
import com.lody.virtual.server.memory.MappedMemoryRegion;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemoryRegionParser {
    public static final String PATTERN = "([0-9a-f]+)-([0-9a-f]+)\\s([r-])([w-])([x-])([sp])\\s([0-9a-f]+)\\s([0-9a-f]+):([0-9a-f]+)\\s(\\d+)\\s?(.*)";
    public static final Pattern MAPS_LINE_PATTERN = Pattern.compile("([0-9a-f]+)-([0-9a-f]+)\\s([r-])([w-])([x-])([sp])\\s([0-9a-f]+)\\s([0-9a-f]+):([0-9a-f]+)\\s(\\d+)\\s?(.*)", 2);

    private static long parseHex(String s) {
        return Long.parseLong(s, 16);
    }

    private static MappedMemoryRegion parseMapLine(String line) {
        Matcher m = MAPS_LINE_PATTERN.matcher(line = line.trim());
        if (!m.matches()) {
            throw new IllegalArgumentException(String.format("The provided line does not match the pattern for /proc/$pid/maps lines. Given: %s", line));
        }
        if (m.groupCount() != 11) {
            throw new InternalError(String.format(Locale.ENGLISH, "Invalid group count: Found %d, but expected %d", m.groupCount(), 12));
        }
        long start = MemoryRegionParser.parseHex(m.group(1));
        long end = MemoryRegionParser.parseHex(m.group(2));
        boolean read = m.group(3).equals("r");
        boolean write = m.group(4).equals("w");
        boolean exec = m.group(5).equals("x");
        boolean shared = m.group(6).equals("s");
        long fileOffset = MemoryRegionParser.parseHex(m.group(7));
        long majorDevNum = MemoryRegionParser.parseHex(m.group(8));
        long minorDevNum = MemoryRegionParser.parseHex(m.group(9));
        long inode = MemoryRegionParser.parseHex(m.group(10));
        String desc = m.group(11);
        return new MappedMemoryRegion(start, end, read, write, exec, shared, fileOffset, majorDevNum, minorDevNum, inode, desc);
    }

    public static List<MappedMemoryRegion> getMemoryRegions(int pid) throws IOException {
        String line;
        LinkedList<MappedMemoryRegion> list = new LinkedList<MappedMemoryRegion>();
        BufferedReader reader = new BufferedReader(new FileReader(String.format(Locale.ENGLISH, "/proc/%d/maps", pid)));
        while ((line = reader.readLine()) != null) {
            MappedMemoryRegion region = MemoryRegionParser.parseMapLine(line);
            if (!region.isReadable || !region.isWritable || region.description.endsWith("(deleted)")) continue;
            list.add(region);
        }
        return list;
    }
}

