/*
 * Decompiled with CFR 0.152.
 */
package com.lody.virtual.server.memory;

import com.lody.virtual.StringFog;
import com.lody.virtual.helper.utils.VLog;
import com.lody.virtual.server.memory.MappedMemoryRegion;
import com.lody.virtual.server.memory.MemoryRegionParser;
import com.lody.virtual.server.memory.MemoryValue;
import com.lody.virtual.server.memory.ProcessMemory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MemoryScanEngine {
    private List<MappedMemoryRegion> regions;
    private int pid;
    private ProcessMemory memory;
    private static final int PAGE = 4096;
    private List<Match> matches;

    public MemoryScanEngine(int pid) throws IOException {
        this.pid = pid;
        this.memory = new ProcessMemory(pid);
        this.updateMemoryLayout();
    }

    public void updateMemoryLayout() {
        try {
            this.regions = MemoryRegionParser.getMemoryRegions(this.pid);
        }
        catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public List<Match> getMatches() {
        return this.matches;
    }

    public void search(MemoryValue value) throws IOException {
        this.matches = new LinkedList<Match>();
        byte[] bytes = new byte[4096];
        byte[] valueBytes = value.toBytes();
        for (MappedMemoryRegion region : this.regions) {
            long end = region.endAddress;
            try {
                for (long start = region.startAddress; start < end; start += 4096L) {
                    int read = Math.min(bytes.length, (int)(end - start));
                    read = this.memory.read(start, bytes, read);
                    this.matches.addAll(this.matchBytes(region, start, bytes, read, valueBytes));
                }
            }
            catch (IOException e) {
                VLog.e(this.getClass().getSimpleName(), "Unable to read region : " + region.description);
            }
        }
    }

    public void modify(Match match, MemoryValue value) throws IOException {
        this.memory.write(match.address, value.toBytes());
    }

    public void modifyAll(MemoryValue value) throws IOException {
        for (Match match : this.matches) {
            this.modify(match, value);
        }
    }

    private List<Match> matchBytes(MappedMemoryRegion region, long startAddress, byte[] page, int read, byte[] value) {
        LinkedList<Match> matches = new LinkedList<Match>();
        int len = value.length;
        int step = 2;
        for (int start = 0; start < read; start += step) {
            boolean match = true;
            for (int i = 0; i < len && i + start < read; ++i) {
                if (page[start + i] == value[i]) continue;
                match = false;
                break;
            }
            if (!match) continue;
            matches.add(new Match(region, startAddress + (long)start, len));
        }
        return matches;
    }

    public void close() {
        try {
            this.memory.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class Match {
        MappedMemoryRegion region;
        long address;
        int len;

        public Match(MappedMemoryRegion region, long address, int len) {
            this.region = region;
            this.address = address;
            this.len = len;
        }
    }
}

