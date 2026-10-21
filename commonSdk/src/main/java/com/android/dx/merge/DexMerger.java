//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.android.dx.merge;

import com.android.dex.Annotation;
import com.android.dex.CallSiteId;
import com.android.dex.ClassData;
import com.android.dex.ClassDef;
import com.android.dex.Code;
import com.android.dex.Dex;
import com.android.dex.DexException;
import com.android.dex.DexIndexOverflowException;
import com.android.dex.FieldId;
import com.android.dex.MethodHandle;
import com.android.dex.MethodId;
import com.android.dex.ProtoId;
import com.android.dex.TableOfContents;
import com.android.dex.TypeList;
import com.android.dx.command.dexer.DxContext;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class DexMerger {
    private final Dex[] dexes;
    private final IndexMap[] indexMaps;
    private final CollisionPolicy collisionPolicy;
    private final DxContext context;
    private final WriterSizes writerSizes;
    private final Dex dexOut;
    private final Dex.Section headerOut;
    private final Dex.Section idsDefsOut;
    private final Dex.Section mapListOut;
    private final Dex.Section typeListOut;
    private final Dex.Section classDataOut;
    private final Dex.Section codeOut;
    private final Dex.Section stringDataOut;
    private final Dex.Section debugInfoOut;
    private final Dex.Section encodedArrayOut;
    private final Dex.Section annotationsDirectoryOut;
    private final Dex.Section annotationSetOut;
    private final Dex.Section annotationSetRefListOut;
    private final Dex.Section annotationOut;
    private final TableOfContents contentsOut;
    private final InstructionTransformer instructionTransformer;
    private int compactWasteThreshold;
    private static final byte DBG_END_SEQUENCE = 0;
    private static final byte DBG_ADVANCE_PC = 1;
    private static final byte DBG_ADVANCE_LINE = 2;
    private static final byte DBG_START_LOCAL = 3;
    private static final byte DBG_START_LOCAL_EXTENDED = 4;
    private static final byte DBG_END_LOCAL = 5;
    private static final byte DBG_RESTART_LOCAL = 6;
    private static final byte DBG_SET_PROLOGUE_END = 7;
    private static final byte DBG_SET_EPILOGUE_BEGIN = 8;
    private static final byte DBG_SET_FILE = 9;

    public DexMerger(Dex[] dexes, CollisionPolicy collisionPolicy, DxContext context) throws IOException {
        this(dexes, collisionPolicy, context, new WriterSizes(dexes));
    }

    private DexMerger(Dex[] dexes, CollisionPolicy collisionPolicy, DxContext context, WriterSizes writerSizes) throws IOException {
        this.compactWasteThreshold = 1048576;
        this.dexes = dexes;
        this.collisionPolicy = collisionPolicy;
        this.context = context;
        this.writerSizes = writerSizes;
        this.dexOut = new Dex(writerSizes.size());
        this.indexMaps = new IndexMap[dexes.length];

        for(int i = 0; i < dexes.length; ++i) {
            this.indexMaps[i] = new IndexMap(this.dexOut, dexes[i].getTableOfContents());
        }

        this.instructionTransformer = new InstructionTransformer();
        this.headerOut = this.dexOut.appendSection(writerSizes.header, "header");
        this.idsDefsOut = this.dexOut.appendSection(writerSizes.idsDefs, "ids defs");
        this.contentsOut = this.dexOut.getTableOfContents();
        this.contentsOut.dataOff = this.dexOut.getNextSectionStart();
        this.contentsOut.mapList.off = this.dexOut.getNextSectionStart();
        this.contentsOut.mapList.size = 1;
        this.mapListOut = this.dexOut.appendSection(writerSizes.mapList, "map list");
        this.contentsOut.typeLists.off = this.dexOut.getNextSectionStart();
        this.typeListOut = this.dexOut.appendSection(writerSizes.typeList, "type list");
        this.contentsOut.annotationSetRefLists.off = this.dexOut.getNextSectionStart();
        this.annotationSetRefListOut = this.dexOut.appendSection(writerSizes.annotationsSetRefList, "annotation set ref list");
        this.contentsOut.annotationSets.off = this.dexOut.getNextSectionStart();
        this.annotationSetOut = this.dexOut.appendSection(writerSizes.annotationsSet, "annotation sets");
        this.contentsOut.classDatas.off = this.dexOut.getNextSectionStart();
        this.classDataOut = this.dexOut.appendSection(writerSizes.classData, "class data");
        this.contentsOut.codes.off = this.dexOut.getNextSectionStart();
        this.codeOut = this.dexOut.appendSection(writerSizes.code, "code");
        this.contentsOut.stringDatas.off = this.dexOut.getNextSectionStart();
        this.stringDataOut = this.dexOut.appendSection(writerSizes.stringData, "string data");
        this.contentsOut.debugInfos.off = this.dexOut.getNextSectionStart();
        this.debugInfoOut = this.dexOut.appendSection(writerSizes.debugInfo, "debug info");
        this.contentsOut.annotations.off = this.dexOut.getNextSectionStart();
        this.annotationOut = this.dexOut.appendSection(writerSizes.annotation, "annotation");
        this.contentsOut.encodedArrays.off = this.dexOut.getNextSectionStart();
        this.encodedArrayOut = this.dexOut.appendSection(writerSizes.encodedArray, "encoded array");
        this.contentsOut.annotationsDirectories.off = this.dexOut.getNextSectionStart();
        this.annotationsDirectoryOut = this.dexOut.appendSection(writerSizes.annotationsDirectory, "annotations directory");
        this.contentsOut.dataSize = this.dexOut.getNextSectionStart() - this.contentsOut.dataOff;
    }

    public void setCompactWasteThreshold(int compactWasteThreshold) {
        this.compactWasteThreshold = compactWasteThreshold;
    }

    private Dex mergeDexes() throws IOException {
        this.mergeStringIds();
        this.mergeTypeIds();
        this.mergeTypeLists();
        this.mergeProtoIds();
        this.mergeFieldIds();
        this.mergeMethodIds();
        this.mergeMethodHandles();
        this.mergeAnnotations();
        this.unionAnnotationSetsAndDirectories();
        this.mergeCallSiteIds();
        this.mergeClassDefs();
        Arrays.sort(this.contentsOut.sections);
        this.contentsOut.header.off = 0;
        this.contentsOut.header.size = 1;
        this.contentsOut.fileSize = this.dexOut.getLength();
        this.contentsOut.computeSizesFromOffsets();
        this.contentsOut.writeHeader(this.headerOut, this.mergeApiLevels());
        this.contentsOut.writeMap(this.mapListOut);
        this.dexOut.writeHashes();
        return this.dexOut;
    }

    public Dex merge() throws IOException {
        if (this.dexes.length == 1) {
            return this.dexes[0];
        } else if (this.dexes.length == 0) {
            return null;
        } else {
            long start = System.nanoTime();
            Dex result = this.mergeDexes();
            WriterSizes compactedSizes = new WriterSizes(this);
            int wastedByteCount = this.writerSizes.size() - compactedSizes.size();
            if (wastedByteCount > this.compactWasteThreshold) {
                DexMerger compacter = new DexMerger(new Dex[]{this.dexOut, new Dex(0)}, CollisionPolicy.FAIL, this.context, compactedSizes);
                result = compacter.mergeDexes();
                this.context.out.printf("Result compacted from %.1fKiB to %.1fKiB to save %.1fKiB%n", (float)this.dexOut.getLength() / 1024.0F, (float)result.getLength() / 1024.0F, (float)wastedByteCount / 1024.0F);
            }

            long elapsed = System.nanoTime() - start;

            for(int i = 0; i < this.dexes.length; ++i) {
                this.context.out.printf("Merged dex #%d (%d defs/%.1fKiB)%n", i + 1, this.dexes[i].getTableOfContents().classDefs.size, (float)this.dexes[i].getLength() / 1024.0F);
            }

            this.context.out.printf("Result is %d defs/%.1fKiB. Took %.1fs%n", result.getTableOfContents().classDefs.size, (float)result.getLength() / 1024.0F, (float)elapsed / 1.0E9F);
            return result;
        }
    }

    private int mergeApiLevels() {
        int maxApi = -1;

        for(int i = 0; i < this.dexes.length; ++i) {
            int dexMinApi = this.dexes[i].getTableOfContents().apiLevel;
            if (maxApi < dexMinApi) {
                maxApi = dexMinApi;
            }
        }

        return maxApi;
    }

    private void mergeStringIds() {
        (new IdMerger<String>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.stringIds;
            }

            String read(Dex.Section in, IndexMap indexMap, int index) {
                return in.readString();
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.stringIds[oldIndex] = newIndex;
            }

            void write(String value) {
                ++DexMerger.this.contentsOut.stringDatas.size;
                DexMerger.this.idsDefsOut.writeInt(DexMerger.this.stringDataOut.getPosition());
                DexMerger.this.stringDataOut.writeStringData(value);
            }
        }).mergeSorted();
    }

    private void mergeTypeIds() {
        (new IdMerger<Integer>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.typeIds;
            }

            Integer read(Dex.Section in, IndexMap indexMap, int index) {
                int stringIndex = in.readInt();
                return indexMap.adjustString(stringIndex);
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex >= 0 && newIndex <= 65535) {
                    indexMap.typeIds[oldIndex] = (short)newIndex;
                } else {
                    throw new DexIndexOverflowException("type ID not in [0, 0xffff]: " + newIndex);
                }
            }

            void write(Integer value) {
                DexMerger.this.idsDefsOut.writeInt(value);
            }
        }).mergeSorted();
    }

    private void mergeTypeLists() {
        (new IdMerger<TypeList>(this.typeListOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.typeLists;
            }

            TypeList read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjustTypeList(in.readTypeList());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.putTypeListOffset(offset, DexMerger.this.typeListOut.getPosition());
            }

            void write(TypeList value) {
                DexMerger.this.typeListOut.writeTypeList(value);
            }
        }).mergeUnsorted();
    }

    private void mergeProtoIds() {
        (new IdMerger<ProtoId>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.protoIds;
            }

            ProtoId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readProtoId());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex >= 0 && newIndex <= 65535) {
                    indexMap.protoIds[oldIndex] = (short)newIndex;
                } else {
                    throw new DexIndexOverflowException("proto ID not in [0, 0xffff]: " + newIndex);
                }
            }

            void write(ProtoId value) {
                value.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }

    private void mergeCallSiteIds() {
        (new IdMerger<CallSiteId>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.callSiteIds;
            }

            CallSiteId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readCallSiteId());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.callSiteIds[oldIndex] = newIndex;
            }

            void write(CallSiteId value) {
                value.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }

    private void mergeMethodHandles() {
        (new IdMerger<MethodHandle>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.methodHandles;
            }

            MethodHandle read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readMethodHandle());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.methodHandleIds.put(oldIndex, indexMap.methodHandleIds.size());
            }

            void write(MethodHandle value) {
                value.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeUnsorted();
    }

    private void mergeFieldIds() {
        (new IdMerger<FieldId>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.fieldIds;
            }

            FieldId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readFieldId());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex >= 0 && newIndex <= 65535) {
                    indexMap.fieldIds[oldIndex] = (short)newIndex;
                } else {
                    throw new DexIndexOverflowException("field ID not in [0, 0xffff]: " + newIndex);
                }
            }

            void write(FieldId value) {
                value.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }

    private void mergeMethodIds() {
        (new IdMerger<MethodId>(this.idsDefsOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.methodIds;
            }

            MethodId read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readMethodId());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                if (newIndex >= 0 && newIndex <= 65535) {
                    indexMap.methodIds[oldIndex] = (short)newIndex;
                } else {
                    throw new DexIndexOverflowException("method ID not in [0, 0xffff]: " + newIndex);
                }
            }

            void write(MethodId methodId) {
                methodId.writeTo(DexMerger.this.idsDefsOut);
            }
        }).mergeSorted();
    }

    private void mergeAnnotations() {
        (new IdMerger<Annotation>(this.annotationOut) {
            TableOfContents.Section getSection(TableOfContents tableOfContents) {
                return tableOfContents.annotations;
            }

            Annotation read(Dex.Section in, IndexMap indexMap, int index) {
                return indexMap.adjust(in.readAnnotation());
            }

            void updateIndex(int offset, IndexMap indexMap, int oldIndex, int newIndex) {
                indexMap.putAnnotationOffset(offset, DexMerger.this.annotationOut.getPosition());
            }

            void write(Annotation value) {
                value.writeTo(DexMerger.this.annotationOut);
            }
        }).mergeUnsorted();
    }

    private void mergeClassDefs() {
        SortableType[] types = this.getSortedTypes();
        this.contentsOut.classDefs.off = this.idsDefsOut.getPosition();
        this.contentsOut.classDefs.size = types.length;
        SortableType[] var2 = types;
        int var3 = types.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            SortableType type = var2[var4];
            Dex in = type.getDex();
            this.transformClassDef(in, type.getClassDef(), type.getIndexMap());
        }

    }

    private SortableType[] getSortedTypes() {
        SortableType[] sortableTypes = new SortableType[this.contentsOut.typeIds.size];

        int firstNull;
        for(firstNull = 0; firstNull < this.dexes.length; ++firstNull) {
            this.readSortableTypes(sortableTypes, this.dexes[firstNull], this.indexMaps[firstNull]);
        }

        boolean allDone;
        do {
            allDone = true;
            SortableType[] var3 = sortableTypes;
            int var4 = sortableTypes.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                SortableType sortableType = var3[var5];
                if (sortableType != null && !sortableType.isDepthAssigned()) {
                    allDone &= sortableType.tryAssignDepth(sortableTypes);
                }
            }
        } while(!allDone);

        Arrays.sort(sortableTypes, SortableType.NULLS_LAST_ORDER);
        firstNull = Arrays.asList(sortableTypes).indexOf((Object)null);
        return firstNull != -1 ? (SortableType[])Arrays.copyOfRange(sortableTypes, 0, firstNull) : sortableTypes;
    }

    private void readSortableTypes(SortableType[] sortableTypes, Dex buffer, IndexMap indexMap) {
        Iterator var4 = buffer.classDefs().iterator();

        while(var4.hasNext()) {
            ClassDef classDef = (ClassDef)var4.next();
            SortableType sortableType = indexMap.adjust(new SortableType(buffer, indexMap, classDef));
            int t = sortableType.getTypeIndex();
            if (sortableTypes[t] == null) {
                sortableTypes[t] = sortableType;
            } else if (this.collisionPolicy != CollisionPolicy.KEEP_FIRST) {
                throw new DexException("Multiple dex files define " + (String)buffer.typeNames().get(classDef.getTypeIndex()));
            }
        }

    }

    private void unionAnnotationSetsAndDirectories() {
        int i;
        for(i = 0; i < this.dexes.length; ++i) {
            this.transformAnnotationSets(this.dexes[i], this.indexMaps[i]);
        }

        for(i = 0; i < this.dexes.length; ++i) {
            this.transformAnnotationSetRefLists(this.dexes[i], this.indexMaps[i]);
        }

        for(i = 0; i < this.dexes.length; ++i) {
            this.transformAnnotationDirectories(this.dexes[i], this.indexMaps[i]);
        }

        for(i = 0; i < this.dexes.length; ++i) {
            this.transformStaticValues(this.dexes[i], this.indexMaps[i]);
        }

    }

    private void transformAnnotationSets(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationSets;
        if (section.exists()) {
            Dex.Section setIn = in.open(section.off);

            for(int i = 0; i < section.size; ++i) {
                this.transformAnnotationSet(indexMap, setIn);
            }
        }

    }

    private void transformAnnotationSetRefLists(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationSetRefLists;
        if (section.exists()) {
            Dex.Section setIn = in.open(section.off);

            for(int i = 0; i < section.size; ++i) {
                this.transformAnnotationSetRefList(indexMap, setIn);
            }
        }

    }

    private void transformAnnotationDirectories(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().annotationsDirectories;
        if (section.exists()) {
            Dex.Section directoryIn = in.open(section.off);

            for(int i = 0; i < section.size; ++i) {
                this.transformAnnotationDirectory(directoryIn, indexMap);
            }
        }

    }

    private void transformStaticValues(Dex in, IndexMap indexMap) {
        TableOfContents.Section section = in.getTableOfContents().encodedArrays;
        if (section.exists()) {
            Dex.Section staticValuesIn = in.open(section.off);

            for(int i = 0; i < section.size; ++i) {
                this.transformStaticValues(staticValuesIn, indexMap);
            }
        }

    }

    private void transformClassDef(Dex in, ClassDef classDef, IndexMap indexMap) {
        this.idsDefsOut.assertFourByteAligned();
        this.idsDefsOut.writeInt(classDef.getTypeIndex());
        this.idsDefsOut.writeInt(classDef.getAccessFlags());
        this.idsDefsOut.writeInt(classDef.getSupertypeIndex());
        this.idsDefsOut.writeInt(classDef.getInterfacesOffset());
        int sourceFileIndex = indexMap.adjustString(classDef.getSourceFileIndex());
        this.idsDefsOut.writeInt(sourceFileIndex);
        int annotationsOff = classDef.getAnnotationsOffset();
        this.idsDefsOut.writeInt(indexMap.adjustAnnotationDirectory(annotationsOff));
        int classDataOff = classDef.getClassDataOffset();
        if (classDataOff == 0) {
            this.idsDefsOut.writeInt(0);
        } else {
            this.idsDefsOut.writeInt(this.classDataOut.getPosition());
            ClassData classData = in.readClassData(classDef);
            this.transformClassData(in, classData, indexMap);
        }

        int staticValuesOff = classDef.getStaticValuesOffset();
        this.idsDefsOut.writeInt(indexMap.adjustEncodedArray(staticValuesOff));
    }

    private void transformAnnotationDirectory(Dex.Section directoryIn, IndexMap indexMap) {
        ++this.contentsOut.annotationsDirectories.size;
        this.annotationsDirectoryOut.assertFourByteAligned();
        indexMap.putAnnotationDirectoryOffset(directoryIn.getPosition(), this.annotationsDirectoryOut.getPosition());
        int classAnnotationsOffset = indexMap.adjustAnnotationSet(directoryIn.readInt());
        this.annotationsDirectoryOut.writeInt(classAnnotationsOffset);
        int fieldsSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(fieldsSize);
        int methodsSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(methodsSize);
        int parameterListSize = directoryIn.readInt();
        this.annotationsDirectoryOut.writeInt(parameterListSize);

        int i;
        for(i = 0; i < fieldsSize; ++i) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustField(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(directoryIn.readInt()));
        }

        for(i = 0; i < methodsSize; ++i) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustMethod(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSet(directoryIn.readInt()));
        }

        for(i = 0; i < parameterListSize; ++i) {
            this.annotationsDirectoryOut.writeInt(indexMap.adjustMethod(directoryIn.readInt()));
            this.annotationsDirectoryOut.writeInt(indexMap.adjustAnnotationSetRefList(directoryIn.readInt()));
        }

    }

    private void transformAnnotationSet(IndexMap indexMap, Dex.Section setIn) {
        ++this.contentsOut.annotationSets.size;
        this.annotationSetOut.assertFourByteAligned();
        indexMap.putAnnotationSetOffset(setIn.getPosition(), this.annotationSetOut.getPosition());
        int size = setIn.readInt();
        this.annotationSetOut.writeInt(size);

        for(int j = 0; j < size; ++j) {
            this.annotationSetOut.writeInt(indexMap.adjustAnnotation(setIn.readInt()));
        }

    }

    private void transformAnnotationSetRefList(IndexMap indexMap, Dex.Section refListIn) {
        ++this.contentsOut.annotationSetRefLists.size;
        this.annotationSetRefListOut.assertFourByteAligned();
        indexMap.putAnnotationSetRefListOffset(refListIn.getPosition(), this.annotationSetRefListOut.getPosition());
        int parameterCount = refListIn.readInt();
        this.annotationSetRefListOut.writeInt(parameterCount);

        for(int p = 0; p < parameterCount; ++p) {
            this.annotationSetRefListOut.writeInt(indexMap.adjustAnnotationSet(refListIn.readInt()));
        }

    }

    private void transformClassData(Dex in, ClassData classData, IndexMap indexMap) {
        ++this.contentsOut.classDatas.size;
        ClassData.Field[] staticFields = classData.getStaticFields();
        ClassData.Field[] instanceFields = classData.getInstanceFields();
        ClassData.Method[] directMethods = classData.getDirectMethods();
        ClassData.Method[] virtualMethods = classData.getVirtualMethods();
        this.classDataOut.writeUleb128(staticFields.length);
        this.classDataOut.writeUleb128(instanceFields.length);
        this.classDataOut.writeUleb128(directMethods.length);
        this.classDataOut.writeUleb128(virtualMethods.length);
        this.transformFields(indexMap, staticFields);
        this.transformFields(indexMap, instanceFields);
        this.transformMethods(in, indexMap, directMethods);
        this.transformMethods(in, indexMap, virtualMethods);
    }

    private void transformFields(IndexMap indexMap, ClassData.Field[] fields) {
        int lastOutFieldIndex = 0;
        ClassData.Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            ClassData.Field field = var4[var6];
            int outFieldIndex = indexMap.adjustField(field.getFieldIndex());
            this.classDataOut.writeUleb128(outFieldIndex - lastOutFieldIndex);
            lastOutFieldIndex = outFieldIndex;
            this.classDataOut.writeUleb128(field.getAccessFlags());
        }

    }

    private void transformMethods(Dex in, IndexMap indexMap, ClassData.Method[] methods) {
        int lastOutMethodIndex = 0;
        ClassData.Method[] var5 = methods;
        int var6 = methods.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            ClassData.Method method = var5[var7];
            int outMethodIndex = indexMap.adjustMethod(method.getMethodIndex());
            this.classDataOut.writeUleb128(outMethodIndex - lastOutMethodIndex);
            lastOutMethodIndex = outMethodIndex;
            this.classDataOut.writeUleb128(method.getAccessFlags());
            if (method.getCodeOffset() == 0) {
                this.classDataOut.writeUleb128(0);
            } else {
                this.codeOut.alignToFourBytesWithZeroFill();
                this.classDataOut.writeUleb128(this.codeOut.getPosition());
                this.transformCode(in, in.readCode(method), indexMap);
            }
        }

    }

    private void transformCode(Dex in, Code code, IndexMap indexMap) {
        ++this.contentsOut.codes.size;
        this.codeOut.assertFourByteAligned();
        this.codeOut.writeUnsignedShort(code.getRegistersSize());
        this.codeOut.writeUnsignedShort(code.getInsSize());
        this.codeOut.writeUnsignedShort(code.getOutsSize());
        Code.Try[] tries = code.getTries();
        Code.CatchHandler[] catchHandlers = code.getCatchHandlers();
        this.codeOut.writeUnsignedShort(tries.length);
        int debugInfoOffset = code.getDebugInfoOffset();
        if (debugInfoOffset != 0) {
            this.codeOut.writeInt(this.debugInfoOut.getPosition());
            this.transformDebugInfoItem(in.open(debugInfoOffset), indexMap);
        } else {
            this.codeOut.writeInt(0);
        }

        short[] instructions = code.getInstructions();
        short[] newInstructions = this.instructionTransformer.transform(indexMap, instructions);
        this.codeOut.writeInt(newInstructions.length);
        this.codeOut.write(newInstructions);
        if (tries.length > 0) {
            if (newInstructions.length % 2 == 1) {
                this.codeOut.writeShort((short)0);
            }

            Dex.Section triesSection = this.dexOut.open(this.codeOut.getPosition());
            this.codeOut.skip(tries.length * 8);
            int[] offsets = this.transformCatchHandlers(indexMap, catchHandlers);
            this.transformTries(triesSection, tries, offsets);
        }

    }

    private int[] transformCatchHandlers(IndexMap indexMap, Code.CatchHandler[] catchHandlers) {
        int baseOffset = this.codeOut.getPosition();
        this.codeOut.writeUleb128(catchHandlers.length);
        int[] offsets = new int[catchHandlers.length];

        for(int i = 0; i < catchHandlers.length; ++i) {
            offsets[i] = this.codeOut.getPosition() - baseOffset;
            this.transformEncodedCatchHandler(catchHandlers[i], indexMap);
        }

        return offsets;
    }

    private void transformTries(Dex.Section out, Code.Try[] tries, int[] catchHandlerOffsets) {
        Code.Try[] var4 = tries;
        int var5 = tries.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Code.Try tryItem = var4[var6];
            out.writeInt(tryItem.getStartAddress());
            out.writeUnsignedShort(tryItem.getInstructionCount());
            out.writeUnsignedShort(catchHandlerOffsets[tryItem.getCatchHandlerIndex()]);
        }

    }

    private void transformDebugInfoItem(Dex.Section in, IndexMap indexMap) {
        ++this.contentsOut.debugInfos.size;
        int lineStart = in.readUleb128();
        this.debugInfoOut.writeUleb128(lineStart);
        int parametersSize = in.readUleb128();
        this.debugInfoOut.writeUleb128(parametersSize);

        int addrDiff;
        int lineDiff;
        for(addrDiff = 0; addrDiff < parametersSize; ++addrDiff) {
            lineDiff = in.readUleb128p1();
            this.debugInfoOut.writeUleb128p1(indexMap.adjustString(lineDiff));
        }

        while(true) {
            int opcode = in.readByte();
            this.debugInfoOut.writeByte(opcode);
            int registerNum;
            int nameIndex;
            switch (opcode) {
                case 0:
                    return;
                case 1:
                    addrDiff = in.readUleb128();
                    this.debugInfoOut.writeUleb128(addrDiff);
                    break;
                case 2:
                    lineDiff = in.readSleb128();
                    this.debugInfoOut.writeSleb128(lineDiff);
                    break;
                case 3:
                case 4:
                    registerNum = in.readUleb128();
                    this.debugInfoOut.writeUleb128(registerNum);
                    nameIndex = in.readUleb128p1();
                    this.debugInfoOut.writeUleb128p1(indexMap.adjustString(nameIndex));
                    int typeIndex = in.readUleb128p1();
                    this.debugInfoOut.writeUleb128p1(indexMap.adjustType(typeIndex));
                    if (opcode == 4) {
                        int sigIndex = in.readUleb128p1();
                        this.debugInfoOut.writeUleb128p1(indexMap.adjustString(sigIndex));
                    }
                    break;
                case 5:
                case 6:
                    registerNum = in.readUleb128();
                    this.debugInfoOut.writeUleb128(registerNum);
                case 7:
                case 8:
                default:
                    break;
                case 9:
                    nameIndex = in.readUleb128p1();
                    this.debugInfoOut.writeUleb128p1(indexMap.adjustString(nameIndex));
            }
        }
    }

    private void transformEncodedCatchHandler(Code.CatchHandler catchHandler, IndexMap indexMap) {
        int catchAllAddress = catchHandler.getCatchAllAddress();
        int[] typeIndexes = catchHandler.getTypeIndexes();
        int[] addresses = catchHandler.getAddresses();
        if (catchAllAddress != -1) {
            this.codeOut.writeSleb128(-typeIndexes.length);
        } else {
            this.codeOut.writeSleb128(typeIndexes.length);
        }

        for(int i = 0; i < typeIndexes.length; ++i) {
            this.codeOut.writeUleb128(indexMap.adjustType(typeIndexes[i]));
            this.codeOut.writeUleb128(addresses[i]);
        }

        if (catchAllAddress != -1) {
            this.codeOut.writeUleb128(catchAllAddress);
        }

    }

    private void transformStaticValues(Dex.Section in, IndexMap indexMap) {
        ++this.contentsOut.encodedArrays.size;
        indexMap.putEncodedArrayValueOffset(in.getPosition(), this.encodedArrayOut.getPosition());
        indexMap.adjustEncodedArray(in.readEncodedArray()).writeTo(this.encodedArrayOut);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            printUsage();
        } else {
            Dex[] dexes = new Dex[args.length - 1];

            for(int i = 1; i < args.length; ++i) {
                dexes[i - 1] = new Dex(new File(args[i]));
            }

            Dex merged = (new DexMerger(dexes, CollisionPolicy.KEEP_FIRST, new DxContext())).merge();
            merged.writeTo(new File(args[0]));
        }
    }

    private static void printUsage() {
        System.out.println("Usage: DexMerger <out.dex> <a.dex> <b.dex> ...");
        System.out.println();
        System.out.println("If a class is defined in several dex, the class found in the first dex will be used.");
    }

    private static class WriterSizes {
        private int header = 112;
        private int idsDefs;
        private int mapList;
        private int typeList;
        private int classData;
        private int code;
        private int stringData;
        private int debugInfo;
        private int encodedArray;
        private int annotationsDirectory;
        private int annotationsSet;
        private int annotationsSetRefList;
        private int annotation;

        public WriterSizes(Dex[] dexes) {
            for(int i = 0; i < dexes.length; ++i) {
                this.plus(dexes[i].getTableOfContents(), false);
            }

            this.fourByteAlign();
        }

        public WriterSizes(DexMerger dexMerger) {
            this.header = dexMerger.headerOut.used();
            this.idsDefs = dexMerger.idsDefsOut.used();
            this.mapList = dexMerger.mapListOut.used();
            this.typeList = dexMerger.typeListOut.used();
            this.classData = dexMerger.classDataOut.used();
            this.code = dexMerger.codeOut.used();
            this.stringData = dexMerger.stringDataOut.used();
            this.debugInfo = dexMerger.debugInfoOut.used();
            this.encodedArray = dexMerger.encodedArrayOut.used();
            this.annotationsDirectory = dexMerger.annotationsDirectoryOut.used();
            this.annotationsSet = dexMerger.annotationSetOut.used();
            this.annotationsSetRefList = dexMerger.annotationSetRefListOut.used();
            this.annotation = dexMerger.annotationOut.used();
            this.fourByteAlign();
        }

        private void plus(TableOfContents contents, boolean exact) {
            this.idsDefs += contents.stringIds.size * 4 + contents.typeIds.size * 4 + contents.protoIds.size * 12 + contents.fieldIds.size * 8 + contents.methodIds.size * 8 + contents.classDefs.size * 32;
            this.mapList = 4 + contents.sections.length * 12;
            this.typeList += fourByteAlign(contents.typeLists.byteCount);
            this.stringData += contents.stringDatas.byteCount;
            this.annotationsDirectory += contents.annotationsDirectories.byteCount;
            this.annotationsSet += contents.annotationSets.byteCount;
            this.annotationsSetRefList += contents.annotationSetRefLists.byteCount;
            if (exact) {
                this.code += contents.codes.byteCount;
                this.classData += contents.classDatas.byteCount;
                this.encodedArray += contents.encodedArrays.byteCount;
                this.annotation += contents.annotations.byteCount;
                this.debugInfo += contents.debugInfos.byteCount;
            } else {
                this.code += (int)Math.ceil((double)contents.codes.byteCount * 1.25);
                this.classData += (int)Math.ceil((double)contents.classDatas.byteCount * 1.67);
                this.encodedArray += contents.encodedArrays.byteCount * 2;
                this.annotation += (int)Math.ceil((double)(contents.annotations.byteCount * 2));
                this.debugInfo += contents.debugInfos.byteCount * 2 + 8;
            }

        }

        private void fourByteAlign() {
            this.header = fourByteAlign(this.header);
            this.idsDefs = fourByteAlign(this.idsDefs);
            this.mapList = fourByteAlign(this.mapList);
            this.typeList = fourByteAlign(this.typeList);
            this.classData = fourByteAlign(this.classData);
            this.code = fourByteAlign(this.code);
            this.stringData = fourByteAlign(this.stringData);
            this.debugInfo = fourByteAlign(this.debugInfo);
            this.encodedArray = fourByteAlign(this.encodedArray);
            this.annotationsDirectory = fourByteAlign(this.annotationsDirectory);
            this.annotationsSet = fourByteAlign(this.annotationsSet);
            this.annotationsSetRefList = fourByteAlign(this.annotationsSetRefList);
            this.annotation = fourByteAlign(this.annotation);
        }

        private static int fourByteAlign(int position) {
            return position + 3 & -4;
        }

        public int size() {
            return this.header + this.idsDefs + this.mapList + this.typeList + this.classData + this.code + this.stringData + this.debugInfo + this.encodedArray + this.annotationsDirectory + this.annotationsSet + this.annotationsSetRefList + this.annotation;
        }
    }

    abstract class IdMerger<T extends Comparable<T>> {
        private final Dex.Section out;

        protected IdMerger(Dex.Section out) {
            this.out = out;
        }

        public final void mergeSorted() {
            TableOfContents.Section[] sections = new TableOfContents.Section[DexMerger.this.dexes.length];
            Dex.Section[] dexSections = new Dex.Section[DexMerger.this.dexes.length];
            int[] offsets = new int[DexMerger.this.dexes.length];
            int[] indexes = new int[DexMerger.this.dexes.length];
            TreeMap<T, List<Integer>> values = new TreeMap();

            int outCount;
            for(outCount = 0; outCount < DexMerger.this.dexes.length; ++outCount) {
                sections[outCount] = this.getSection(DexMerger.this.dexes[outCount].getTableOfContents());
                dexSections[outCount] = sections[outCount].exists() ? DexMerger.this.dexes[outCount].open(sections[outCount].off) : null;
                offsets[outCount] = this.readIntoMap(dexSections[outCount], sections[outCount], DexMerger.this.indexMaps[outCount], indexes[outCount], values, outCount);
            }

            if (values.isEmpty()) {
                this.getSection(DexMerger.this.contentsOut).off = 0;
                this.getSection(DexMerger.this.contentsOut).size = 0;
            } else {
                this.getSection(DexMerger.this.contentsOut).off = this.out.getPosition();

                for(outCount = 0; !values.isEmpty(); ++outCount) {
                    Map.Entry<T, List<Integer>> first = values.pollFirstEntry();

                    Integer dex;
                    for(Iterator var8 = ((List)first.getValue()).iterator(); var8.hasNext(); offsets[dex] = this.readIntoMap(dexSections[dex], sections[dex], DexMerger.this.indexMaps[dex], indexes[dex], values, dex)) {
                        dex = (Integer)var8.next();
                        this.updateIndex(offsets[dex], DexMerger.this.indexMaps[dex], indexes[dex]++, outCount);
                    }

                    this.write((T) first.getKey());
                }

                this.getSection(DexMerger.this.contentsOut).size = outCount;
            }
        }

        private int readIntoMap(Dex.Section in, TableOfContents.Section section, IndexMap indexMap, int index, TreeMap<T, List<Integer>> values, int dex) {
            int offset = in != null ? in.getPosition() : -1;
            if (index < section.size) {
                T v = this.read(in, indexMap, index);
                List<Integer> l = (List)values.get(v);
                if (l == null) {
                    l = new ArrayList();
                    values.put(v, l);
                }

                ((List)l).add(dex);
            }

            return offset;
        }

        public final void mergeUnsorted() {
            this.getSection(DexMerger.this.contentsOut).off = this.out.getPosition();
            List<IdMerger<T>.UnsortedValue> all = new ArrayList();

            int outCount;
            for(outCount = 0; outCount < DexMerger.this.dexes.length; ++outCount) {
                all.addAll(this.readUnsortedValues(DexMerger.this.dexes[outCount], DexMerger.this.indexMaps[outCount]));
            }

            if (all.isEmpty()) {
                this.getSection(DexMerger.this.contentsOut).off = 0;
                this.getSection(DexMerger.this.contentsOut).size = 0;
            } else {
                Collections.sort(all);
                outCount = 0;

                for(int i = 0; i < all.size(); ++outCount) {
                    IdMerger<T>.UnsortedValue e1 = (UnsortedValue)all.get(i++);
                    this.updateIndex(e1.offset, e1.indexMap, e1.index, outCount - 1);

                    while(i < all.size() && e1.compareTo((UnsortedValue)all.get(i)) == 0) {
                        IdMerger<T>.UnsortedValue e2 = (UnsortedValue)all.get(i++);
                        this.updateIndex(e2.offset, e2.indexMap, e2.index, outCount - 1);
                    }

                    this.write(e1.value);
                }

                this.getSection(DexMerger.this.contentsOut).size = outCount;
            }
        }

        private List<IdMerger<T>.UnsortedValue> readUnsortedValues(Dex source, IndexMap indexMap) {
            TableOfContents.Section section = this.getSection(source.getTableOfContents());
            if (!section.exists()) {
                return Collections.emptyList();
            } else {
                List<IdMerger<T>.UnsortedValue> result = new ArrayList();
                Dex.Section in = source.open(section.off);

                for(int i = 0; i < section.size; ++i) {
                    int offset = in.getPosition();
                    T value = this.read(in, indexMap, 0);
                    result.add(new UnsortedValue(source, indexMap, value, i, offset));
                }

                return result;
            }
        }

        abstract TableOfContents.Section getSection(TableOfContents var1);

        abstract T read(Dex.Section var1, IndexMap var2, int var3);

        abstract void updateIndex(int var1, IndexMap var2, int var3, int var4);

        abstract void write(T var1);

        class UnsortedValue implements Comparable<IdMerger<T>.UnsortedValue> {
            final Dex source;
            final IndexMap indexMap;
            final T value;
            final int index;
            final int offset;

            UnsortedValue(Dex source, IndexMap indexMap, T value, int index, int offset) {
                this.source = source;
                this.indexMap = indexMap;
                this.value = value;
                this.index = index;
                this.offset = offset;
            }

            public int compareTo(IdMerger<T>.UnsortedValue unsortedValue) {
                return this.value.compareTo(unsortedValue.value);
            }
        }
    }
}
