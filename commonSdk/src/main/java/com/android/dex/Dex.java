/*
 * Decompiled with CFR 0.152.
 */
package com.android.dex;

import com.android.dex.Annotation;
import com.android.dex.CallSiteId;
import com.android.dex.ClassData;
import com.android.dex.ClassDef;
import com.android.dex.Code;
import com.android.dex.DexException;
import com.android.dex.EncodedValue;
import com.android.dex.EncodedValueReader;
import com.android.dex.FieldId;
import com.android.dex.Leb128;
import com.android.dex.MethodHandle;
import com.android.dex.MethodId;
import com.android.dex.Mutf8;
import com.android.dex.ProtoId;
import com.android.dex.TableOfContents;
import com.android.dex.TypeList;
import com.android.dex.util.ByteInput;
import com.android.dex.util.ByteOutput;
import com.android.dex.util.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UTFDataFormatException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.zip.Adler32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Dex {
    private static final int CHECKSUM_OFFSET = 8;
    private static final int CHECKSUM_SIZE = 4;
    private static final int SIGNATURE_OFFSET = 12;
    private static final int SIGNATURE_SIZE = 20;
    static final short[] EMPTY_SHORT_ARRAY = new short[0];
    private ByteBuffer data;
    private final TableOfContents tableOfContents = new TableOfContents();
    private int nextSectionStart = 0;
    private final StringTable strings = new StringTable();
    private final TypeIndexToDescriptorIndexTable typeIds = new TypeIndexToDescriptorIndexTable();
    private final TypeIndexToDescriptorTable typeNames = new TypeIndexToDescriptorTable();
    private final ProtoIdTable protoIds = new ProtoIdTable();
    private final FieldIdTable fieldIds = new FieldIdTable();
    private final MethodIdTable methodIds = new MethodIdTable();

    public Dex(byte[] data) throws IOException {
        this(ByteBuffer.wrap(data));
    }

    private Dex(ByteBuffer data) throws IOException {
        this.data = data;
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }

    public Dex(int byteCount) throws IOException {
        this.data = ByteBuffer.wrap(new byte[byteCount]);
        this.data.order(ByteOrder.LITTLE_ENDIAN);
    }

    public Dex(InputStream in) throws IOException {
        try {
            this.loadFrom(in);
        }
        finally {
            in.close();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public Dex(File file) throws IOException {
        if (FileUtils.hasArchiveSuffix(file.getName())) {
            ZipFile zipFile = new ZipFile(file);
            ZipEntry entry = zipFile.getEntry("classes.dex");
            if (entry == null) throw new DexException("Expected classes.dex in " + file);
            try (InputStream inputStream = zipFile.getInputStream(entry);){
                this.loadFrom(inputStream);
            }
            zipFile.close();
            return;
        }
        if (!file.getName().endsWith(".dex")) throw new DexException("unknown output extension: " + file);
        try (FileInputStream inputStream = new FileInputStream(file);){
            this.loadFrom(inputStream);
            return;
        }
    }

    private void loadFrom(InputStream in) throws IOException {
        int count;
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        while ((count = in.read(buffer)) != -1) {
            bytesOut.write(buffer, 0, count);
        }
        this.data = ByteBuffer.wrap(bytesOut.toByteArray());
        this.data.order(ByteOrder.LITTLE_ENDIAN);
        this.tableOfContents.readFrom(this);
    }

    private static void checkBounds(int index, int length) {
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index:" + index + ", length=" + length);
        }
    }

    public void writeTo(OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        ByteBuffer data = this.data.duplicate();
        data.clear();
        while (data.hasRemaining()) {
            int count = Math.min(buffer.length, data.remaining());
            data.get(buffer, 0, count);
            out.write(buffer, 0, count);
        }
    }

    public void writeTo(File dexOut) throws IOException {
        try (FileOutputStream out = new FileOutputStream(dexOut);){
            this.writeTo(out);
        }
    }

    public TableOfContents getTableOfContents() {
        return this.tableOfContents;
    }

    public Section open(int position) {
        if (position < 0 || position >= this.data.capacity()) {
            throw new IllegalArgumentException("position=" + position + " length=" + this.data.capacity());
        }
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(position);
        sectionData.limit(this.data.capacity());
        return new Section("section", sectionData);
    }

    public Section appendSection(int maxByteCount, String name) {
        if ((maxByteCount & 3) != 0) {
            throw new IllegalStateException("Not four byte aligned!");
        }
        int limit = this.nextSectionStart + maxByteCount;
        ByteBuffer sectionData = this.data.duplicate();
        sectionData.order(ByteOrder.LITTLE_ENDIAN);
        sectionData.position(this.nextSectionStart);
        sectionData.limit(limit);
        Section result = new Section(name, sectionData);
        this.nextSectionStart = limit;
        return result;
    }

    public int getLength() {
        return this.data.capacity();
    }

    public int getNextSectionStart() {
        return this.nextSectionStart;
    }

    public byte[] getBytes() {
        ByteBuffer data = this.data.duplicate();
        byte[] result = new byte[data.capacity()];
        data.position(0);
        data.get(result);
        return result;
    }

    public List<String> strings() {
        return this.strings;
    }

    public List<Integer> typeIds() {
        return this.typeIds;
    }

    public List<String> typeNames() {
        return this.typeNames;
    }

    public List<ProtoId> protoIds() {
        return this.protoIds;
    }

    public List<FieldId> fieldIds() {
        return this.fieldIds;
    }

    public List<MethodId> methodIds() {
        return this.methodIds;
    }

    public Iterable<ClassDef> classDefs() {
        return new ClassDefIterable();
    }

    public TypeList readTypeList(int offset) {
        if (offset == 0) {
            return TypeList.EMPTY;
        }
        return this.open(offset).readTypeList();
    }

    public ClassData readClassData(ClassDef classDef) {
        int offset = classDef.getClassDataOffset();
        if (offset == 0) {
            throw new IllegalArgumentException("offset == 0");
        }
        return this.open(offset).readClassData();
    }

    public Code readCode(ClassData.Method method) {
        int offset = method.getCodeOffset();
        if (offset == 0) {
            throw new IllegalArgumentException("offset == 0");
        }
        return this.open(offset).readCode();
    }

    public byte[] computeSignature() throws IOException {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException e) {
            throw new AssertionError();
        }
        byte[] buffer = new byte[8192];
        ByteBuffer data = this.data.duplicate();
        data.limit(data.capacity());
        data.position(32);
        while (data.hasRemaining()) {
            int count = Math.min(buffer.length, data.remaining());
            data.get(buffer, 0, count);
            digest.update(buffer, 0, count);
        }
        return digest.digest();
    }

    public int computeChecksum() throws IOException {
        Adler32 adler32 = new Adler32();
        byte[] buffer = new byte[8192];
        ByteBuffer data = this.data.duplicate();
        data.limit(data.capacity());
        data.position(12);
        while (data.hasRemaining()) {
            int count = Math.min(buffer.length, data.remaining());
            data.get(buffer, 0, count);
            adler32.update(buffer, 0, count);
        }
        return (int)adler32.getValue();
    }

    public void writeHashes() throws IOException {
        this.open(12).write(this.computeSignature());
        this.open(8).writeInt(this.computeChecksum());
    }

    public int descriptorIndexFromTypeIndex(int typeIndex) {
        Dex.checkBounds(typeIndex, this.tableOfContents.typeIds.size);
        int position = this.tableOfContents.typeIds.off + 4 * typeIndex;
        return this.data.getInt(position);
    }

    private final class ClassDefIterable implements Iterable<ClassDef> {
        private ClassDefIterable() {
        }

        @Override
        public Iterator<ClassDef> iterator() {
            return !Dex.this.tableOfContents.classDefs.exists()
                    ? Collections.emptyIterator()
                    : new ClassDefIterator();
        }
    }

    private final class ClassDefIterator
    implements Iterator<ClassDef> {
        private final Section in;
        private int count;

        private ClassDefIterator() {
            this.in = Dex.this.open(((Dex)Dex.this).tableOfContents.classDefs.off);
            this.count = 0;
        }

        @Override
        public boolean hasNext() {
            return this.count < ((Dex)Dex.this).tableOfContents.classDefs.size;
        }

        @Override
        public ClassDef next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            ++this.count;
            return this.in.readClassDef();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private final class MethodIdTable
    extends AbstractList<MethodId>
    implements RandomAccess {
        private MethodIdTable() {
        }

        @Override
        public MethodId get(int index) {
            Dex.checkBounds(index, ((Dex)Dex.this).tableOfContents.methodIds.size);
            return Dex.this.open(((Dex)Dex.this).tableOfContents.methodIds.off + 8 * index).readMethodId();
        }

        @Override
        public int size() {
            return ((Dex)Dex.this).tableOfContents.methodIds.size;
        }
    }

    private final class FieldIdTable
    extends AbstractList<FieldId>
    implements RandomAccess {
        private FieldIdTable() {
        }

        @Override
        public FieldId get(int index) {
            Dex.checkBounds(index, ((Dex)Dex.this).tableOfContents.fieldIds.size);
            return Dex.this.open(((Dex)Dex.this).tableOfContents.fieldIds.off + 8 * index).readFieldId();
        }

        @Override
        public int size() {
            return ((Dex)Dex.this).tableOfContents.fieldIds.size;
        }
    }

    private final class ProtoIdTable
    extends AbstractList<ProtoId>
    implements RandomAccess {
        private ProtoIdTable() {
        }

        @Override
        public ProtoId get(int index) {
            Dex.checkBounds(index, ((Dex)Dex.this).tableOfContents.protoIds.size);
            return Dex.this.open(((Dex)Dex.this).tableOfContents.protoIds.off + 12 * index).readProtoId();
        }

        @Override
        public int size() {
            return ((Dex)Dex.this).tableOfContents.protoIds.size;
        }
    }

    private final class TypeIndexToDescriptorTable
    extends AbstractList<String>
    implements RandomAccess {
        private TypeIndexToDescriptorTable() {
        }

        @Override
        public String get(int index) {
            return Dex.this.strings.get(Dex.this.descriptorIndexFromTypeIndex(index));
        }

        @Override
        public int size() {
            return ((Dex)Dex.this).tableOfContents.typeIds.size;
        }
    }

    private final class TypeIndexToDescriptorIndexTable
    extends AbstractList<Integer>
    implements RandomAccess {
        private TypeIndexToDescriptorIndexTable() {
        }

        @Override
        public Integer get(int index) {
            return Dex.this.descriptorIndexFromTypeIndex(index);
        }

        @Override
        public int size() {
            return ((Dex)Dex.this).tableOfContents.typeIds.size;
        }
    }

    private final class StringTable
    extends AbstractList<String>
    implements RandomAccess {
        private StringTable() {
        }

        @Override
        public String get(int index) {
            Dex.checkBounds(index, ((Dex)Dex.this).tableOfContents.stringIds.size);
            return Dex.this.open(((Dex)Dex.this).tableOfContents.stringIds.off + index * 4).readString();
        }

        @Override
        public int size() {
            return ((Dex)Dex.this).tableOfContents.stringIds.size;
        }
    }

    public final class Section
    implements ByteInput,
    ByteOutput {
        private final String name;
        private final ByteBuffer data;
        private final int initialPosition;

        private Section(String name, ByteBuffer data) {
            this.name = name;
            this.data = data;
            this.initialPosition = data.position();
        }

        public int getPosition() {
            return this.data.position();
        }

        public int readInt() {
            return this.data.getInt();
        }

        public short readShort() {
            return this.data.getShort();
        }

        public int readUnsignedShort() {
            return this.readShort() & 0xFFFF;
        }

        @Override
        public byte readByte() {
            return this.data.get();
        }

        public byte[] readByteArray(int length) {
            byte[] result = new byte[length];
            this.data.get(result);
            return result;
        }

        public short[] readShortArray(int length) {
            if (length == 0) {
                return EMPTY_SHORT_ARRAY;
            }
            short[] result = new short[length];
            for (int i = 0; i < length; ++i) {
                result[i] = this.readShort();
            }
            return result;
        }

        public int readUleb128() {
            return Leb128.readUnsignedLeb128(this);
        }

        public int readUleb128p1() {
            return Leb128.readUnsignedLeb128(this) - 1;
        }

        public int readSleb128() {
            return Leb128.readSignedLeb128(this);
        }

        public void writeUleb128p1(int i) {
            this.writeUleb128(i + 1);
        }

        public TypeList readTypeList() {
            int size = this.readInt();
            short[] types = this.readShortArray(size);
            this.alignToFourBytes();
            return new TypeList(Dex.this, types);
        }

        public String readString() {
            int offset = this.readInt();
            int savedPosition = this.data.position();
            int savedLimit = this.data.limit();
            this.data.position(offset);
            this.data.limit(this.data.capacity());
            try {
                int expectedLength = this.readUleb128();
                String result = Mutf8.decode(this, new char[expectedLength]);
                if (result.length() != expectedLength) {
                    throw new DexException("Declared length " + expectedLength + " doesn't match decoded length of " + result.length());
                }
                String string = result;
                return string;
            }
            catch (UTFDataFormatException e) {
                throw new DexException(e);
            }
            finally {
                this.data.position(savedPosition);
                this.data.limit(savedLimit);
            }
        }

        public FieldId readFieldId() {
            int declaringClassIndex = this.readUnsignedShort();
            int typeIndex = this.readUnsignedShort();
            int nameIndex = this.readInt();
            return new FieldId(Dex.this, declaringClassIndex, typeIndex, nameIndex);
        }

        public MethodId readMethodId() {
            int declaringClassIndex = this.readUnsignedShort();
            int protoIndex = this.readUnsignedShort();
            int nameIndex = this.readInt();
            return new MethodId(Dex.this, declaringClassIndex, protoIndex, nameIndex);
        }

        public ProtoId readProtoId() {
            int shortyIndex = this.readInt();
            int returnTypeIndex = this.readInt();
            int parametersOffset = this.readInt();
            return new ProtoId(Dex.this, shortyIndex, returnTypeIndex, parametersOffset);
        }

        public CallSiteId readCallSiteId() {
            int offset = this.readInt();
            return new CallSiteId(Dex.this, offset);
        }

        public MethodHandle readMethodHandle() {
            MethodHandle.MethodHandleType methodHandleType = MethodHandle.MethodHandleType.fromValue(this.readUnsignedShort());
            int unused1 = this.readUnsignedShort();
            int fieldOrMethodId = this.readUnsignedShort();
            int unused2 = this.readUnsignedShort();
            return new MethodHandle(Dex.this, methodHandleType, unused1, fieldOrMethodId, unused2);
        }

        public ClassDef readClassDef() {
            int offset = this.getPosition();
            int type = this.readInt();
            int accessFlags = this.readInt();
            int supertype = this.readInt();
            int interfacesOffset = this.readInt();
            int sourceFileIndex = this.readInt();
            int annotationsOffset = this.readInt();
            int classDataOffset = this.readInt();
            int staticValuesOffset = this.readInt();
            return new ClassDef(Dex.this, offset, type, accessFlags, supertype, interfacesOffset, sourceFileIndex, annotationsOffset, classDataOffset, staticValuesOffset);
        }

        private Code readCode() {
            Code.Try[] tries;
            Code.CatchHandler[] catchHandlers;
            int registersSize = this.readUnsignedShort();
            int insSize = this.readUnsignedShort();
            int outsSize = this.readUnsignedShort();
            int triesSize = this.readUnsignedShort();
            int debugInfoOffset = this.readInt();
            int instructionsSize = this.readInt();
            short[] instructions = this.readShortArray(instructionsSize);
            if (triesSize > 0) {
                if (instructions.length % 2 == 1) {
                    this.readShort();
                }
                Section triesSection = Dex.this.open(this.data.position());
                this.skip(triesSize * 8);
                catchHandlers = this.readCatchHandlers();
                tries = triesSection.readTries(triesSize, catchHandlers);
            } else {
                tries = new Code.Try[]{};
                catchHandlers = new Code.CatchHandler[]{};
            }
            return new Code(registersSize, insSize, outsSize, debugInfoOffset, instructions, tries, catchHandlers);
        }

        private Code.CatchHandler[] readCatchHandlers() {
            int baseOffset = this.data.position();
            int catchHandlersSize = this.readUleb128();
            Code.CatchHandler[] result = new Code.CatchHandler[catchHandlersSize];
            for (int i = 0; i < catchHandlersSize; ++i) {
                int offset = this.data.position() - baseOffset;
                result[i] = this.readCatchHandler(offset);
            }
            return result;
        }

        private Code.Try[] readTries(int triesSize, Code.CatchHandler[] catchHandlers) {
            Code.Try[] result = new Code.Try[triesSize];
            for (int i = 0; i < triesSize; ++i) {
                int startAddress = this.readInt();
                int instructionCount = this.readUnsignedShort();
                int handlerOffset = this.readUnsignedShort();
                int catchHandlerIndex = this.findCatchHandlerIndex(catchHandlers, handlerOffset);
                result[i] = new Code.Try(startAddress, instructionCount, catchHandlerIndex);
            }
            return result;
        }

        private int findCatchHandlerIndex(Code.CatchHandler[] catchHandlers, int offset) {
            for (int i = 0; i < catchHandlers.length; ++i) {
                Code.CatchHandler catchHandler = catchHandlers[i];
                if (catchHandler.getOffset() != offset) continue;
                return i;
            }
            throw new IllegalArgumentException();
        }

        private Code.CatchHandler readCatchHandler(int offset) {
            int size = this.readSleb128();
            int handlersCount = Math.abs(size);
            int[] typeIndexes = new int[handlersCount];
            int[] addresses = new int[handlersCount];
            for (int i = 0; i < handlersCount; ++i) {
                typeIndexes[i] = this.readUleb128();
                addresses[i] = this.readUleb128();
            }
            int catchAllAddress = size <= 0 ? this.readUleb128() : -1;
            return new Code.CatchHandler(typeIndexes, addresses, catchAllAddress, offset);
        }

        private ClassData readClassData() {
            int staticFieldsSize = this.readUleb128();
            int instanceFieldsSize = this.readUleb128();
            int directMethodsSize = this.readUleb128();
            int virtualMethodsSize = this.readUleb128();
            ClassData.Field[] staticFields = this.readFields(staticFieldsSize);
            ClassData.Field[] instanceFields = this.readFields(instanceFieldsSize);
            ClassData.Method[] directMethods = this.readMethods(directMethodsSize);
            ClassData.Method[] virtualMethods = this.readMethods(virtualMethodsSize);
            return new ClassData(staticFields, instanceFields, directMethods, virtualMethods);
        }

        private ClassData.Field[] readFields(int count) {
            ClassData.Field[] result = new ClassData.Field[count];
            int fieldIndex = 0;
            for (int i = 0; i < count; ++i) {
                int accessFlags = this.readUleb128();
                result[i] = new ClassData.Field(fieldIndex += this.readUleb128(), accessFlags);
            }
            return result;
        }

        private ClassData.Method[] readMethods(int count) {
            ClassData.Method[] result = new ClassData.Method[count];
            int methodIndex = 0;
            for (int i = 0; i < count; ++i) {
                int accessFlags = this.readUleb128();
                int codeOff = this.readUleb128();
                result[i] = new ClassData.Method(methodIndex += this.readUleb128(), accessFlags, codeOff);
            }
            return result;
        }

        private byte[] getBytesFrom(int start) {
            int end = this.data.position();
            byte[] result = new byte[end - start];
            this.data.position(start);
            this.data.get(result);
            return result;
        }

        public Annotation readAnnotation() {
            byte visibility = this.readByte();
            int start = this.data.position();
            new EncodedValueReader(this, 29).skipValue();
            return new Annotation(Dex.this, visibility, new EncodedValue(this.getBytesFrom(start)));
        }

        public EncodedValue readEncodedArray() {
            int start = this.data.position();
            new EncodedValueReader(this, 28).skipValue();
            return new EncodedValue(this.getBytesFrom(start));
        }

        public void skip(int count) {
            if (count < 0) {
                throw new IllegalArgumentException();
            }
            this.data.position(this.data.position() + count);
        }

        public void alignToFourBytes() {
            this.data.position(this.data.position() + 3 & 0xFFFFFFFC);
        }

        public void alignToFourBytesWithZeroFill() {
            while ((this.data.position() & 3) != 0) {
                this.data.put((byte)0);
            }
        }

        public void assertFourByteAligned() {
            if ((this.data.position() & 3) != 0) {
                throw new IllegalStateException("Not four byte aligned!");
            }
        }

        public void write(byte[] bytes) {
            this.data.put(bytes);
        }

        @Override
        public void writeByte(int b) {
            this.data.put((byte)b);
        }

        public void writeShort(short i) {
            this.data.putShort(i);
        }

        public void writeUnsignedShort(int i) {
            short s = (short)i;
            if (i != (s & 0xFFFF)) {
                throw new IllegalArgumentException("Expected an unsigned short: " + i);
            }
            this.writeShort(s);
        }

        public void write(short[] shorts) {
            for (short s : shorts) {
                this.writeShort(s);
            }
        }

        public void writeInt(int i) {
            this.data.putInt(i);
        }

        public void writeUleb128(int i) {
            try {
                Leb128.writeUnsignedLeb128(this, i);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                throw new DexException("Section limit " + this.data.limit() + " exceeded by " + this.name);
            }
        }

        public void writeSleb128(int i) {
            try {
                Leb128.writeSignedLeb128(this, i);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                throw new DexException("Section limit " + this.data.limit() + " exceeded by " + this.name);
            }
        }

        public void writeStringData(String value) {
            try {
                int length = value.length();
                this.writeUleb128(length);
                this.write(Mutf8.encode(value));
                this.writeByte(0);
            }
            catch (UTFDataFormatException e) {
                throw new AssertionError();
            }
        }

        public void writeTypeList(TypeList typeList) {
            short[] types = typeList.getTypes();
            this.writeInt(types.length);
            for (short type : types) {
                this.writeShort(type);
            }
            this.alignToFourBytesWithZeroFill();
        }

        public int used() {
            return this.data.position() - this.initialPosition;
        }
    }
}

