/*
 * Decompiled with CFR 0.152.
 */
package com.android.dx.rop.code;

import com.android.dx.rop.code.Exceptions;
import com.android.dx.rop.code.RegOps;
import com.android.dx.rop.code.Rop;
import com.android.dx.rop.cst.Constant;
import com.android.dx.rop.cst.CstCallSiteRef;
import com.android.dx.rop.cst.CstMethodRef;
import com.android.dx.rop.cst.CstType;
import com.android.dx.rop.type.Prototype;
import com.android.dx.rop.type.StdTypeList;
import com.android.dx.rop.type.Type;
import com.android.dx.rop.type.TypeBearer;
import com.android.dx.rop.type.TypeList;

public final class Rops {
    public static final Rop NOP = new Rop(1, Type.VOID, StdTypeList.EMPTY, "nop");
    public static final Rop MOVE_INT = new Rop(2, Type.INT, StdTypeList.INT, "move-int");
    public static final Rop MOVE_LONG = new Rop(2, Type.LONG, StdTypeList.LONG, "move-long");
    public static final Rop MOVE_FLOAT = new Rop(2, Type.FLOAT, StdTypeList.FLOAT, "move-float");
    public static final Rop MOVE_DOUBLE = new Rop(2, Type.DOUBLE, StdTypeList.DOUBLE, "move-double");
    public static final Rop MOVE_OBJECT = new Rop(2, Type.OBJECT, StdTypeList.OBJECT, "move-object");
    public static final Rop MOVE_RETURN_ADDRESS = new Rop(2, Type.RETURN_ADDRESS, StdTypeList.RETURN_ADDRESS, "move-return-address");
    public static final Rop MOVE_PARAM_INT = new Rop(3, Type.INT, StdTypeList.EMPTY, "move-param-int");
    public static final Rop MOVE_PARAM_LONG = new Rop(3, Type.LONG, StdTypeList.EMPTY, "move-param-long");
    public static final Rop MOVE_PARAM_FLOAT = new Rop(3, Type.FLOAT, StdTypeList.EMPTY, "move-param-float");
    public static final Rop MOVE_PARAM_DOUBLE = new Rop(3, Type.DOUBLE, StdTypeList.EMPTY, "move-param-double");
    public static final Rop MOVE_PARAM_OBJECT = new Rop(3, Type.OBJECT, StdTypeList.EMPTY, "move-param-object");
    public static final Rop CONST_INT = new Rop(5, Type.INT, StdTypeList.EMPTY, "const-int");
    public static final Rop CONST_LONG = new Rop(5, Type.LONG, StdTypeList.EMPTY, "const-long");
    public static final Rop CONST_FLOAT = new Rop(5, Type.FLOAT, StdTypeList.EMPTY, "const-float");
    public static final Rop CONST_DOUBLE = new Rop(5, Type.DOUBLE, StdTypeList.EMPTY, "const-double");
    public static final Rop CONST_OBJECT = new Rop(5, Type.OBJECT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "const-object");
    public static final Rop CONST_OBJECT_NOTHROW = new Rop(5, Type.OBJECT, StdTypeList.EMPTY, "const-object-nothrow");
    public static final Rop GOTO = new Rop(6, Type.VOID, (TypeList)StdTypeList.EMPTY, 3, "goto");
    public static final Rop IF_EQZ_INT = new Rop(7, Type.VOID, (TypeList)StdTypeList.INT, 4, "if-eqz-int");
    public static final Rop IF_NEZ_INT = new Rop(8, Type.VOID, (TypeList)StdTypeList.INT, 4, "if-nez-int");
    public static final Rop IF_LTZ_INT = new Rop(9, Type.VOID, (TypeList)StdTypeList.INT, 4, "if-ltz-int");
    public static final Rop IF_GEZ_INT = new Rop(10, Type.VOID, (TypeList)StdTypeList.INT, 4, "if-gez-int");
    public static final Rop IF_LEZ_INT = new Rop(11, Type.VOID, (TypeList)StdTypeList.INT, 4, "if-lez-int");
    public static final Rop IF_GTZ_INT = new Rop(12, Type.VOID, (TypeList)StdTypeList.INT, 4, "if-gtz-int");
    public static final Rop IF_EQZ_OBJECT = new Rop(7, Type.VOID, (TypeList)StdTypeList.OBJECT, 4, "if-eqz-object");
    public static final Rop IF_NEZ_OBJECT = new Rop(8, Type.VOID, (TypeList)StdTypeList.OBJECT, 4, "if-nez-object");
    public static final Rop IF_EQ_INT = new Rop(7, Type.VOID, (TypeList)StdTypeList.INT_INT, 4, "if-eq-int");
    public static final Rop IF_NE_INT = new Rop(8, Type.VOID, (TypeList)StdTypeList.INT_INT, 4, "if-ne-int");
    public static final Rop IF_LT_INT = new Rop(9, Type.VOID, (TypeList)StdTypeList.INT_INT, 4, "if-lt-int");
    public static final Rop IF_GE_INT = new Rop(10, Type.VOID, (TypeList)StdTypeList.INT_INT, 4, "if-ge-int");
    public static final Rop IF_LE_INT = new Rop(11, Type.VOID, (TypeList)StdTypeList.INT_INT, 4, "if-le-int");
    public static final Rop IF_GT_INT = new Rop(12, Type.VOID, (TypeList)StdTypeList.INT_INT, 4, "if-gt-int");
    public static final Rop IF_EQ_OBJECT = new Rop(7, Type.VOID, (TypeList)StdTypeList.OBJECT_OBJECT, 4, "if-eq-object");
    public static final Rop IF_NE_OBJECT = new Rop(8, Type.VOID, (TypeList)StdTypeList.OBJECT_OBJECT, 4, "if-ne-object");
    public static final Rop SWITCH = new Rop(13, Type.VOID, (TypeList)StdTypeList.INT, 5, "switch");
    public static final Rop ADD_INT = new Rop(14, Type.INT, StdTypeList.INT_INT, "add-int");
    public static final Rop ADD_LONG = new Rop(14, Type.LONG, StdTypeList.LONG_LONG, "add-long");
    public static final Rop ADD_FLOAT = new Rop(14, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "add-float");
    public static final Rop ADD_DOUBLE = new Rop(14, Type.DOUBLE, (TypeList)StdTypeList.DOUBLE_DOUBLE, 1, "add-double");
    public static final Rop SUB_INT = new Rop(15, Type.INT, StdTypeList.INT_INT, "sub-int");
    public static final Rop SUB_LONG = new Rop(15, Type.LONG, StdTypeList.LONG_LONG, "sub-long");
    public static final Rop SUB_FLOAT = new Rop(15, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "sub-float");
    public static final Rop SUB_DOUBLE = new Rop(15, Type.DOUBLE, (TypeList)StdTypeList.DOUBLE_DOUBLE, 1, "sub-double");
    public static final Rop MUL_INT = new Rop(16, Type.INT, StdTypeList.INT_INT, "mul-int");
    public static final Rop MUL_LONG = new Rop(16, Type.LONG, StdTypeList.LONG_LONG, "mul-long");
    public static final Rop MUL_FLOAT = new Rop(16, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "mul-float");
    public static final Rop MUL_DOUBLE = new Rop(16, Type.DOUBLE, (TypeList)StdTypeList.DOUBLE_DOUBLE, 1, "mul-double");
    public static final Rop DIV_INT = new Rop(17, Type.INT, (TypeList)StdTypeList.INT_INT, Exceptions.LIST_Error_ArithmeticException, "div-int");
    public static final Rop DIV_LONG = new Rop(17, Type.LONG, (TypeList)StdTypeList.LONG_LONG, Exceptions.LIST_Error_ArithmeticException, "div-long");
    public static final Rop DIV_FLOAT = new Rop(17, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "div-float");
    public static final Rop DIV_DOUBLE = new Rop(17, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, "div-double");
    public static final Rop REM_INT = new Rop(18, Type.INT, (TypeList)StdTypeList.INT_INT, Exceptions.LIST_Error_ArithmeticException, "rem-int");
    public static final Rop REM_LONG = new Rop(18, Type.LONG, (TypeList)StdTypeList.LONG_LONG, Exceptions.LIST_Error_ArithmeticException, "rem-long");
    public static final Rop REM_FLOAT = new Rop(18, Type.FLOAT, StdTypeList.FLOAT_FLOAT, "rem-float");
    public static final Rop REM_DOUBLE = new Rop(18, Type.DOUBLE, StdTypeList.DOUBLE_DOUBLE, "rem-double");
    public static final Rop NEG_INT = new Rop(19, Type.INT, StdTypeList.INT, "neg-int");
    public static final Rop NEG_LONG = new Rop(19, Type.LONG, StdTypeList.LONG, "neg-long");
    public static final Rop NEG_FLOAT = new Rop(19, Type.FLOAT, StdTypeList.FLOAT, "neg-float");
    public static final Rop NEG_DOUBLE = new Rop(19, Type.DOUBLE, StdTypeList.DOUBLE, "neg-double");
    public static final Rop AND_INT = new Rop(20, Type.INT, StdTypeList.INT_INT, "and-int");
    public static final Rop AND_LONG = new Rop(20, Type.LONG, StdTypeList.LONG_LONG, "and-long");
    public static final Rop OR_INT = new Rop(21, Type.INT, StdTypeList.INT_INT, "or-int");
    public static final Rop OR_LONG = new Rop(21, Type.LONG, StdTypeList.LONG_LONG, "or-long");
    public static final Rop XOR_INT = new Rop(22, Type.INT, StdTypeList.INT_INT, "xor-int");
    public static final Rop XOR_LONG = new Rop(22, Type.LONG, StdTypeList.LONG_LONG, "xor-long");
    public static final Rop SHL_INT = new Rop(23, Type.INT, StdTypeList.INT_INT, "shl-int");
    public static final Rop SHL_LONG = new Rop(23, Type.LONG, StdTypeList.LONG_INT, "shl-long");
    public static final Rop SHR_INT = new Rop(24, Type.INT, StdTypeList.INT_INT, "shr-int");
    public static final Rop SHR_LONG = new Rop(24, Type.LONG, StdTypeList.LONG_INT, "shr-long");
    public static final Rop USHR_INT = new Rop(25, Type.INT, StdTypeList.INT_INT, "ushr-int");
    public static final Rop USHR_LONG = new Rop(25, Type.LONG, StdTypeList.LONG_INT, "ushr-long");
    public static final Rop NOT_INT = new Rop(26, Type.INT, StdTypeList.INT, "not-int");
    public static final Rop NOT_LONG = new Rop(26, Type.LONG, StdTypeList.LONG, "not-long");
    public static final Rop ADD_CONST_INT = new Rop(14, Type.INT, StdTypeList.INT, "add-const-int");
    public static final Rop ADD_CONST_LONG = new Rop(14, Type.LONG, StdTypeList.LONG, "add-const-long");
    public static final Rop ADD_CONST_FLOAT = new Rop(14, Type.FLOAT, StdTypeList.FLOAT, "add-const-float");
    public static final Rop ADD_CONST_DOUBLE = new Rop(14, Type.DOUBLE, StdTypeList.DOUBLE, "add-const-double");
    public static final Rop SUB_CONST_INT = new Rop(15, Type.INT, StdTypeList.INT, "sub-const-int");
    public static final Rop SUB_CONST_LONG = new Rop(15, Type.LONG, StdTypeList.LONG, "sub-const-long");
    public static final Rop SUB_CONST_FLOAT = new Rop(15, Type.FLOAT, StdTypeList.FLOAT, "sub-const-float");
    public static final Rop SUB_CONST_DOUBLE = new Rop(15, Type.DOUBLE, StdTypeList.DOUBLE, "sub-const-double");
    public static final Rop MUL_CONST_INT = new Rop(16, Type.INT, StdTypeList.INT, "mul-const-int");
    public static final Rop MUL_CONST_LONG = new Rop(16, Type.LONG, StdTypeList.LONG, "mul-const-long");
    public static final Rop MUL_CONST_FLOAT = new Rop(16, Type.FLOAT, StdTypeList.FLOAT, "mul-const-float");
    public static final Rop MUL_CONST_DOUBLE = new Rop(16, Type.DOUBLE, StdTypeList.DOUBLE, "mul-const-double");
    public static final Rop DIV_CONST_INT = new Rop(17, Type.INT, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_ArithmeticException, "div-const-int");
    public static final Rop DIV_CONST_LONG = new Rop(17, Type.LONG, (TypeList)StdTypeList.LONG, Exceptions.LIST_Error_ArithmeticException, "div-const-long");
    public static final Rop DIV_CONST_FLOAT = new Rop(17, Type.FLOAT, StdTypeList.FLOAT, "div-const-float");
    public static final Rop DIV_CONST_DOUBLE = new Rop(17, Type.DOUBLE, StdTypeList.DOUBLE, "div-const-double");
    public static final Rop REM_CONST_INT = new Rop(18, Type.INT, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_ArithmeticException, "rem-const-int");
    public static final Rop REM_CONST_LONG = new Rop(18, Type.LONG, (TypeList)StdTypeList.LONG, Exceptions.LIST_Error_ArithmeticException, "rem-const-long");
    public static final Rop REM_CONST_FLOAT = new Rop(18, Type.FLOAT, StdTypeList.FLOAT, "rem-const-float");
    public static final Rop REM_CONST_DOUBLE = new Rop(18, Type.DOUBLE, StdTypeList.DOUBLE, "rem-const-double");
    public static final Rop AND_CONST_INT = new Rop(20, Type.INT, StdTypeList.INT, "and-const-int");
    public static final Rop AND_CONST_LONG = new Rop(20, Type.LONG, StdTypeList.LONG, "and-const-long");
    public static final Rop OR_CONST_INT = new Rop(21, Type.INT, StdTypeList.INT, "or-const-int");
    public static final Rop OR_CONST_LONG = new Rop(21, Type.LONG, StdTypeList.LONG, "or-const-long");
    public static final Rop XOR_CONST_INT = new Rop(22, Type.INT, StdTypeList.INT, "xor-const-int");
    public static final Rop XOR_CONST_LONG = new Rop(22, Type.LONG, StdTypeList.LONG, "xor-const-long");
    public static final Rop SHL_CONST_INT = new Rop(23, Type.INT, StdTypeList.INT, "shl-const-int");
    public static final Rop SHL_CONST_LONG = new Rop(23, Type.LONG, StdTypeList.INT, "shl-const-long");
    public static final Rop SHR_CONST_INT = new Rop(24, Type.INT, StdTypeList.INT, "shr-const-int");
    public static final Rop SHR_CONST_LONG = new Rop(24, Type.LONG, StdTypeList.INT, "shr-const-long");
    public static final Rop USHR_CONST_INT = new Rop(25, Type.INT, StdTypeList.INT, "ushr-const-int");
    public static final Rop USHR_CONST_LONG = new Rop(25, Type.LONG, StdTypeList.INT, "ushr-const-long");
    public static final Rop CMPL_LONG = new Rop(27, Type.INT, StdTypeList.LONG_LONG, "cmpl-long");
    public static final Rop CMPL_FLOAT = new Rop(27, Type.INT, StdTypeList.FLOAT_FLOAT, "cmpl-float");
    public static final Rop CMPL_DOUBLE = new Rop(27, Type.INT, StdTypeList.DOUBLE_DOUBLE, "cmpl-double");
    public static final Rop CMPG_FLOAT = new Rop(28, Type.INT, StdTypeList.FLOAT_FLOAT, "cmpg-float");
    public static final Rop CMPG_DOUBLE = new Rop(28, Type.INT, StdTypeList.DOUBLE_DOUBLE, "cmpg-double");
    public static final Rop CONV_L2I = new Rop(29, Type.INT, StdTypeList.LONG, "conv-l2i");
    public static final Rop CONV_F2I = new Rop(29, Type.INT, StdTypeList.FLOAT, "conv-f2i");
    public static final Rop CONV_D2I = new Rop(29, Type.INT, StdTypeList.DOUBLE, "conv-d2i");
    public static final Rop CONV_I2L = new Rop(29, Type.LONG, StdTypeList.INT, "conv-i2l");
    public static final Rop CONV_F2L = new Rop(29, Type.LONG, StdTypeList.FLOAT, "conv-f2l");
    public static final Rop CONV_D2L = new Rop(29, Type.LONG, StdTypeList.DOUBLE, "conv-d2l");
    public static final Rop CONV_I2F = new Rop(29, Type.FLOAT, StdTypeList.INT, "conv-i2f");
    public static final Rop CONV_L2F = new Rop(29, Type.FLOAT, StdTypeList.LONG, "conv-l2f");
    public static final Rop CONV_D2F = new Rop(29, Type.FLOAT, StdTypeList.DOUBLE, "conv-d2f");
    public static final Rop CONV_I2D = new Rop(29, Type.DOUBLE, StdTypeList.INT, "conv-i2d");
    public static final Rop CONV_L2D = new Rop(29, Type.DOUBLE, StdTypeList.LONG, "conv-l2d");
    public static final Rop CONV_F2D = new Rop(29, Type.DOUBLE, StdTypeList.FLOAT, "conv-f2d");
    public static final Rop TO_BYTE = new Rop(30, Type.INT, StdTypeList.INT, "to-byte");
    public static final Rop TO_CHAR = new Rop(31, Type.INT, StdTypeList.INT, "to-char");
    public static final Rop TO_SHORT = new Rop(32, Type.INT, StdTypeList.INT, "to-short");
    public static final Rop RETURN_VOID = new Rop(33, Type.VOID, (TypeList)StdTypeList.EMPTY, 2, "return-void");
    public static final Rop RETURN_INT = new Rop(33, Type.VOID, (TypeList)StdTypeList.INT, 2, "return-int");
    public static final Rop RETURN_LONG = new Rop(33, Type.VOID, (TypeList)StdTypeList.LONG, 2, "return-long");
    public static final Rop RETURN_FLOAT = new Rop(33, Type.VOID, (TypeList)StdTypeList.FLOAT, 2, "return-float");
    public static final Rop RETURN_DOUBLE = new Rop(33, Type.VOID, (TypeList)StdTypeList.DOUBLE, 2, "return-double");
    public static final Rop RETURN_OBJECT = new Rop(33, Type.VOID, (TypeList)StdTypeList.OBJECT, 2, "return-object");
    public static final Rop ARRAY_LENGTH = new Rop(34, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "array-length");
    public static final Rop THROW = new Rop(35, Type.VOID, (TypeList)StdTypeList.THROWABLE, StdTypeList.THROWABLE, "throw");
    public static final Rop MONITOR_ENTER = new Rop(36, Type.VOID, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "monitor-enter");
    public static final Rop MONITOR_EXIT = new Rop(37, Type.VOID, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_Null_IllegalMonitorStateException, "monitor-exit");
    public static final Rop AGET_INT = new Rop(38, Type.INT, (TypeList)StdTypeList.INTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-int");
    public static final Rop AGET_LONG = new Rop(38, Type.LONG, (TypeList)StdTypeList.LONGARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-long");
    public static final Rop AGET_FLOAT = new Rop(38, Type.FLOAT, (TypeList)StdTypeList.FLOATARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-float");
    public static final Rop AGET_DOUBLE = new Rop(38, Type.DOUBLE, (TypeList)StdTypeList.DOUBLEARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-double");
    public static final Rop AGET_OBJECT = new Rop(38, Type.OBJECT, (TypeList)StdTypeList.OBJECTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-object");
    public static final Rop AGET_BOOLEAN = new Rop(38, Type.INT, (TypeList)StdTypeList.BOOLEANARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-boolean");
    public static final Rop AGET_BYTE = new Rop(38, Type.INT, (TypeList)StdTypeList.BYTEARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-byte");
    public static final Rop AGET_CHAR = new Rop(38, Type.INT, (TypeList)StdTypeList.CHARARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-char");
    public static final Rop AGET_SHORT = new Rop(38, Type.INT, (TypeList)StdTypeList.SHORTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aget-short");
    public static final Rop APUT_INT = new Rop(39, Type.VOID, (TypeList)StdTypeList.INT_INTARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-int");
    public static final Rop APUT_LONG = new Rop(39, Type.VOID, (TypeList)StdTypeList.LONG_LONGARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-long");
    public static final Rop APUT_FLOAT = new Rop(39, Type.VOID, (TypeList)StdTypeList.FLOAT_FLOATARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-float");
    public static final Rop APUT_DOUBLE = new Rop(39, Type.VOID, (TypeList)StdTypeList.DOUBLE_DOUBLEARR_INT, Exceptions.LIST_Error_Null_ArrayIndexOutOfBounds, "aput-double");
    public static final Rop APUT_OBJECT = new Rop(39, Type.VOID, (TypeList)StdTypeList.OBJECT_OBJECTARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-object");
    public static final Rop APUT_BOOLEAN = new Rop(39, Type.VOID, (TypeList)StdTypeList.INT_BOOLEANARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-boolean");
    public static final Rop APUT_BYTE = new Rop(39, Type.VOID, (TypeList)StdTypeList.INT_BYTEARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-byte");
    public static final Rop APUT_CHAR = new Rop(39, Type.VOID, (TypeList)StdTypeList.INT_CHARARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-char");
    public static final Rop APUT_SHORT = new Rop(39, Type.VOID, (TypeList)StdTypeList.INT_SHORTARR_INT, Exceptions.LIST_Error_Null_ArrayIndex_ArrayStore, "aput-short");
    public static final Rop NEW_INSTANCE = new Rop(40, Type.OBJECT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "new-instance");
    public static final Rop NEW_ARRAY_INT = new Rop(41, Type.INT_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-int");
    public static final Rop NEW_ARRAY_LONG = new Rop(41, Type.LONG_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-long");
    public static final Rop NEW_ARRAY_FLOAT = new Rop(41, Type.FLOAT_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-float");
    public static final Rop NEW_ARRAY_DOUBLE = new Rop(41, Type.DOUBLE_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-double");
    public static final Rop NEW_ARRAY_BOOLEAN = new Rop(41, Type.BOOLEAN_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-boolean");
    public static final Rop NEW_ARRAY_BYTE = new Rop(41, Type.BYTE_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-byte");
    public static final Rop NEW_ARRAY_CHAR = new Rop(41, Type.CHAR_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-char");
    public static final Rop NEW_ARRAY_SHORT = new Rop(41, Type.SHORT_ARRAY, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-short");
    public static final Rop CHECK_CAST = new Rop(43, Type.VOID, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_ClassCastException, "check-cast");
    public static final Rop INSTANCE_OF = new Rop(44, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error, "instance-of");
    public static final Rop GET_FIELD_INT = new Rop(45, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-int");
    public static final Rop GET_FIELD_LONG = new Rop(45, Type.LONG, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-long");
    public static final Rop GET_FIELD_FLOAT = new Rop(45, Type.FLOAT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-float");
    public static final Rop GET_FIELD_DOUBLE = new Rop(45, Type.DOUBLE, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-double");
    public static final Rop GET_FIELD_OBJECT = new Rop(45, Type.OBJECT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-object");
    public static final Rop GET_FIELD_BOOLEAN = new Rop(45, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-boolean");
    public static final Rop GET_FIELD_BYTE = new Rop(45, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-byte");
    public static final Rop GET_FIELD_CHAR = new Rop(45, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-char");
    public static final Rop GET_FIELD_SHORT = new Rop(45, Type.INT, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error_NullPointerException, "get-field-short");
    public static final Rop GET_STATIC_INT = new Rop(46, Type.INT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-int");
    public static final Rop GET_STATIC_LONG = new Rop(46, Type.LONG, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-long");
    public static final Rop GET_STATIC_FLOAT = new Rop(46, Type.FLOAT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-float");
    public static final Rop GET_STATIC_DOUBLE = new Rop(46, Type.DOUBLE, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-double");
    public static final Rop GET_STATIC_OBJECT = new Rop(46, Type.OBJECT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-static-object");
    public static final Rop GET_STATIC_BOOLEAN = new Rop(46, Type.INT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-boolean");
    public static final Rop GET_STATIC_BYTE = new Rop(46, Type.INT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-byte");
    public static final Rop GET_STATIC_CHAR = new Rop(46, Type.INT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-char");
    public static final Rop GET_STATIC_SHORT = new Rop(46, Type.INT, (TypeList)StdTypeList.EMPTY, Exceptions.LIST_Error, "get-field-short");
    public static final Rop PUT_FIELD_INT = new Rop(47, Type.VOID, (TypeList)StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-int");
    public static final Rop PUT_FIELD_LONG = new Rop(47, Type.VOID, (TypeList)StdTypeList.LONG_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-long");
    public static final Rop PUT_FIELD_FLOAT = new Rop(47, Type.VOID, (TypeList)StdTypeList.FLOAT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-float");
    public static final Rop PUT_FIELD_DOUBLE = new Rop(47, Type.VOID, (TypeList)StdTypeList.DOUBLE_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-double");
    public static final Rop PUT_FIELD_OBJECT = new Rop(47, Type.VOID, (TypeList)StdTypeList.OBJECT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-object");
    public static final Rop PUT_FIELD_BOOLEAN = new Rop(47, Type.VOID, (TypeList)StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-boolean");
    public static final Rop PUT_FIELD_BYTE = new Rop(47, Type.VOID, (TypeList)StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-byte");
    public static final Rop PUT_FIELD_CHAR = new Rop(47, Type.VOID, (TypeList)StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-char");
    public static final Rop PUT_FIELD_SHORT = new Rop(47, Type.VOID, (TypeList)StdTypeList.INT_OBJECT, Exceptions.LIST_Error_NullPointerException, "put-field-short");
    public static final Rop PUT_STATIC_INT = new Rop(48, Type.VOID, (TypeList)StdTypeList.INT, Exceptions.LIST_Error, "put-static-int");
    public static final Rop PUT_STATIC_LONG = new Rop(48, Type.VOID, (TypeList)StdTypeList.LONG, Exceptions.LIST_Error, "put-static-long");
    public static final Rop PUT_STATIC_FLOAT = new Rop(48, Type.VOID, (TypeList)StdTypeList.FLOAT, Exceptions.LIST_Error, "put-static-float");
    public static final Rop PUT_STATIC_DOUBLE = new Rop(48, Type.VOID, (TypeList)StdTypeList.DOUBLE, Exceptions.LIST_Error, "put-static-double");
    public static final Rop PUT_STATIC_OBJECT = new Rop(48, Type.VOID, (TypeList)StdTypeList.OBJECT, Exceptions.LIST_Error, "put-static-object");
    public static final Rop PUT_STATIC_BOOLEAN = new Rop(48, Type.VOID, (TypeList)StdTypeList.INT, Exceptions.LIST_Error, "put-static-boolean");
    public static final Rop PUT_STATIC_BYTE = new Rop(48, Type.VOID, (TypeList)StdTypeList.INT, Exceptions.LIST_Error, "put-static-byte");
    public static final Rop PUT_STATIC_CHAR = new Rop(48, Type.VOID, (TypeList)StdTypeList.INT, Exceptions.LIST_Error, "put-static-char");
    public static final Rop PUT_STATIC_SHORT = new Rop(48, Type.VOID, (TypeList)StdTypeList.INT, Exceptions.LIST_Error, "put-static-short");
    public static final Rop MARK_LOCAL_INT = new Rop(54, Type.VOID, StdTypeList.INT, "mark-local-int");
    public static final Rop MARK_LOCAL_LONG = new Rop(54, Type.VOID, StdTypeList.LONG, "mark-local-long");
    public static final Rop MARK_LOCAL_FLOAT = new Rop(54, Type.VOID, StdTypeList.FLOAT, "mark-local-float");
    public static final Rop MARK_LOCAL_DOUBLE = new Rop(54, Type.VOID, StdTypeList.DOUBLE, "mark-local-double");
    public static final Rop MARK_LOCAL_OBJECT = new Rop(54, Type.VOID, StdTypeList.OBJECT, "mark-local-object");
    public static final Rop FILL_ARRAY_DATA = new Rop(57, Type.VOID, StdTypeList.EMPTY, "fill-array-data");

    public static Rop ropFor(int opcode, TypeBearer dest, TypeList sources, Constant cst) {
        switch (opcode) {
            case 1: {
                return NOP;
            }
            case 2: {
                return Rops.opMove(dest);
            }
            case 3: {
                return Rops.opMoveParam(dest);
            }
            case 4: {
                return Rops.opMoveException(dest);
            }
            case 5: {
                return Rops.opConst(dest);
            }
            case 6: {
                return GOTO;
            }
            case 7: {
                return Rops.opIfEq(sources);
            }
            case 8: {
                return Rops.opIfNe(sources);
            }
            case 9: {
                return Rops.opIfLt(sources);
            }
            case 10: {
                return Rops.opIfGe(sources);
            }
            case 11: {
                return Rops.opIfLe(sources);
            }
            case 12: {
                return Rops.opIfGt(sources);
            }
            case 13: {
                return SWITCH;
            }
            case 14: {
                return Rops.opAdd(sources);
            }
            case 15: {
                return Rops.opSub(sources);
            }
            case 16: {
                return Rops.opMul(sources);
            }
            case 17: {
                return Rops.opDiv(sources);
            }
            case 18: {
                return Rops.opRem(sources);
            }
            case 19: {
                return Rops.opNeg(dest);
            }
            case 20: {
                return Rops.opAnd(sources);
            }
            case 21: {
                return Rops.opOr(sources);
            }
            case 22: {
                return Rops.opXor(sources);
            }
            case 23: {
                return Rops.opShl(sources);
            }
            case 24: {
                return Rops.opShr(sources);
            }
            case 25: {
                return Rops.opUshr(sources);
            }
            case 26: {
                return Rops.opNot(dest);
            }
            case 27: {
                return Rops.opCmpl(sources.getType(0));
            }
            case 28: {
                return Rops.opCmpg(sources.getType(0));
            }
            case 29: {
                return Rops.opConv(dest, sources.getType(0));
            }
            case 30: {
                return TO_BYTE;
            }
            case 31: {
                return TO_CHAR;
            }
            case 32: {
                return TO_SHORT;
            }
            case 33: {
                if (sources.size() == 0) {
                    return RETURN_VOID;
                }
                return Rops.opReturn(sources.getType(0));
            }
            case 34: {
                return ARRAY_LENGTH;
            }
            case 35: {
                return THROW;
            }
            case 36: {
                return MONITOR_ENTER;
            }
            case 37: {
                return MONITOR_EXIT;
            }
            case 38: {
                Type source = sources.getType(0);
                Type componentType = source == Type.KNOWN_NULL ? dest.getType() : source.getComponentType();
                return Rops.opAget(componentType);
            }
            case 39: {
                Type source = sources.getType(1);
                Type componentType = source == Type.KNOWN_NULL ? sources.getType(0) : source.getComponentType();
                return Rops.opAput(componentType);
            }
            case 40: {
                return NEW_INSTANCE;
            }
            case 41: {
                return Rops.opNewArray(dest.getType());
            }
            case 43: {
                return CHECK_CAST;
            }
            case 44: {
                return INSTANCE_OF;
            }
            case 45: {
                return Rops.opGetField(dest);
            }
            case 46: {
                return Rops.opGetStatic(dest);
            }
            case 47: {
                return Rops.opPutField(sources.getType(0));
            }
            case 48: {
                return Rops.opPutStatic(sources.getType(0));
            }
            case 49: {
                return Rops.opInvokeStatic(((CstMethodRef)cst).getPrototype());
            }
            case 50: {
                CstMethodRef cstMeth = (CstMethodRef)cst;
                Prototype meth = cstMeth.getPrototype();
                CstType definer = cstMeth.getDefiningClass();
                meth = meth.withFirstParameter(definer.getClassType());
                return Rops.opInvokeVirtual(meth);
            }
            case 51: {
                CstMethodRef cstMeth = (CstMethodRef)cst;
                Prototype meth = cstMeth.getPrototype();
                CstType definer = cstMeth.getDefiningClass();
                meth = meth.withFirstParameter(definer.getClassType());
                return Rops.opInvokeSuper(meth);
            }
            case 52: {
                CstMethodRef cstMeth = (CstMethodRef)cst;
                Prototype meth = cstMeth.getPrototype();
                CstType definer = cstMeth.getDefiningClass();
                meth = meth.withFirstParameter(definer.getClassType());
                return Rops.opInvokeDirect(meth);
            }
            case 53: {
                CstMethodRef cstMeth = (CstMethodRef)cst;
                Prototype meth = cstMeth.getPrototype();
                CstType definer = cstMeth.getDefiningClass();
                meth = meth.withFirstParameter(definer.getClassType());
                return Rops.opInvokeInterface(meth);
            }
            case 58: {
                CstMethodRef cstMeth = (CstMethodRef)cst;
                Prototype proto = cstMeth.getPrototype();
                CstType definer = cstMeth.getDefiningClass();
                Prototype meth = proto.withFirstParameter(definer.getClassType());
                return Rops.opInvokePolymorphic(meth);
            }
            case 59: {
                CstCallSiteRef cstInvokeDynamicRef = (CstCallSiteRef)cst;
                Prototype proto = cstInvokeDynamicRef.getPrototype();
                return Rops.opInvokeCustom(proto);
            }
        }
        throw new RuntimeException("unknown opcode " + RegOps.opName(opcode));
    }

    public static Rop opMove(TypeBearer type) {
        switch (type.getBasicFrameType()) {
            case 6: {
                return MOVE_INT;
            }
            case 7: {
                return MOVE_LONG;
            }
            case 5: {
                return MOVE_FLOAT;
            }
            case 4: {
                return MOVE_DOUBLE;
            }
            case 9: {
                return MOVE_OBJECT;
            }
            case 10: {
                return MOVE_RETURN_ADDRESS;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opMoveParam(TypeBearer type) {
        switch (type.getBasicFrameType()) {
            case 6: {
                return MOVE_PARAM_INT;
            }
            case 7: {
                return MOVE_PARAM_LONG;
            }
            case 5: {
                return MOVE_PARAM_FLOAT;
            }
            case 4: {
                return MOVE_PARAM_DOUBLE;
            }
            case 9: {
                return MOVE_PARAM_OBJECT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opMoveException(TypeBearer type) {
        return new Rop(4, type.getType(), StdTypeList.EMPTY, null);
    }

    public static Rop opMoveResult(TypeBearer type) {
        return new Rop(55, type.getType(), StdTypeList.EMPTY, null);
    }

    public static Rop opMoveResultPseudo(TypeBearer type) {
        return new Rop(56, type.getType(), StdTypeList.EMPTY, null);
    }

    public static Rop opConst(TypeBearer type) {
        if (type.getType() == Type.KNOWN_NULL) {
            return CONST_OBJECT_NOTHROW;
        }
        switch (type.getBasicFrameType()) {
            case 6: {
                return CONST_INT;
            }
            case 7: {
                return CONST_LONG;
            }
            case 5: {
                return CONST_FLOAT;
            }
            case 4: {
                return CONST_DOUBLE;
            }
            case 9: {
                return CONST_OBJECT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opIfEq(TypeList types) {
        return Rops.pickIf(types, IF_EQZ_INT, IF_EQZ_OBJECT, IF_EQ_INT, IF_EQ_OBJECT);
    }

    public static Rop opIfNe(TypeList types) {
        return Rops.pickIf(types, IF_NEZ_INT, IF_NEZ_OBJECT, IF_NE_INT, IF_NE_OBJECT);
    }

    public static Rop opIfLt(TypeList types) {
        return Rops.pickIf(types, IF_LTZ_INT, null, IF_LT_INT, null);
    }

    public static Rop opIfGe(TypeList types) {
        return Rops.pickIf(types, IF_GEZ_INT, null, IF_GE_INT, null);
    }

    public static Rop opIfGt(TypeList types) {
        return Rops.pickIf(types, IF_GTZ_INT, null, IF_GT_INT, null);
    }

    public static Rop opIfLe(TypeList types) {
        return Rops.pickIf(types, IF_LEZ_INT, null, IF_LE_INT, null);
    }

    private static Rop pickIf(TypeList types, Rop intZ, Rop objZ, Rop intInt, Rop objObj) {
        switch (types.size()) {
            case 1: {
                switch (types.getType(0).getBasicFrameType()) {
                    case 6: {
                        return intZ;
                    }
                    case 9: {
                        if (objZ == null) break;
                        return objZ;
                    }
                }
                break;
            }
            case 2: {
                int bt = types.getType(0).getBasicFrameType();
                if (bt != types.getType(1).getBasicFrameType()) break;
                switch (bt) {
                    case 6: {
                        return intInt;
                    }
                    case 9: {
                        if (objObj == null) break;
                        return objObj;
                    }
                }
                break;
            }
        }
        return Rops.throwBadTypes(types);
    }

    public static Rop opAdd(TypeList types) {
        return Rops.pickBinaryOp(types, ADD_CONST_INT, ADD_CONST_LONG, ADD_CONST_FLOAT, ADD_CONST_DOUBLE, ADD_INT, ADD_LONG, ADD_FLOAT, ADD_DOUBLE);
    }

    public static Rop opSub(TypeList types) {
        return Rops.pickBinaryOp(types, SUB_CONST_INT, SUB_CONST_LONG, SUB_CONST_FLOAT, SUB_CONST_DOUBLE, SUB_INT, SUB_LONG, SUB_FLOAT, SUB_DOUBLE);
    }

    public static Rop opMul(TypeList types) {
        return Rops.pickBinaryOp(types, MUL_CONST_INT, MUL_CONST_LONG, MUL_CONST_FLOAT, MUL_CONST_DOUBLE, MUL_INT, MUL_LONG, MUL_FLOAT, MUL_DOUBLE);
    }

    public static Rop opDiv(TypeList types) {
        return Rops.pickBinaryOp(types, DIV_CONST_INT, DIV_CONST_LONG, DIV_CONST_FLOAT, DIV_CONST_DOUBLE, DIV_INT, DIV_LONG, DIV_FLOAT, DIV_DOUBLE);
    }

    public static Rop opRem(TypeList types) {
        return Rops.pickBinaryOp(types, REM_CONST_INT, REM_CONST_LONG, REM_CONST_FLOAT, REM_CONST_DOUBLE, REM_INT, REM_LONG, REM_FLOAT, REM_DOUBLE);
    }

    public static Rop opAnd(TypeList types) {
        return Rops.pickBinaryOp(types, AND_CONST_INT, AND_CONST_LONG, null, null, AND_INT, AND_LONG, null, null);
    }

    public static Rop opOr(TypeList types) {
        return Rops.pickBinaryOp(types, OR_CONST_INT, OR_CONST_LONG, null, null, OR_INT, OR_LONG, null, null);
    }

    public static Rop opXor(TypeList types) {
        return Rops.pickBinaryOp(types, XOR_CONST_INT, XOR_CONST_LONG, null, null, XOR_INT, XOR_LONG, null, null);
    }

    public static Rop opShl(TypeList types) {
        return Rops.pickBinaryOp(types, SHL_CONST_INT, SHL_CONST_LONG, null, null, SHL_INT, SHL_LONG, null, null);
    }

    public static Rop opShr(TypeList types) {
        return Rops.pickBinaryOp(types, SHR_CONST_INT, SHR_CONST_LONG, null, null, SHR_INT, SHR_LONG, null, null);
    }

    public static Rop opUshr(TypeList types) {
        return Rops.pickBinaryOp(types, USHR_CONST_INT, USHR_CONST_LONG, null, null, USHR_INT, USHR_LONG, null, null);
    }

    private static Rop pickBinaryOp(TypeList types, Rop int1, Rop long1, Rop float1, Rop double1, Rop int2, Rop long2, Rop float2, Rop double2) {
        int bt1 = types.getType(0).getBasicFrameType();
        Rop result = null;
        block0 : switch (types.size()) {
            case 1: {
                switch (bt1) {
                    case 6: {
                        return int1;
                    }
                    case 7: {
                        return long1;
                    }
                    case 5: {
                        result = float1;
                        break;
                    }
                    case 4: {
                        result = double1;
                    }
                }
                break;
            }
            case 2: {
                switch (bt1) {
                    case 6: {
                        return int2;
                    }
                    case 7: {
                        return long2;
                    }
                    case 5: {
                        result = float2;
                        break block0;
                    }
                    case 4: {
                        result = double2;
                    }
                }
            }
        }
        if (result == null) {
            return Rops.throwBadTypes(types);
        }
        return result;
    }

    public static Rop opNeg(TypeBearer type) {
        switch (type.getBasicFrameType()) {
            case 6: {
                return NEG_INT;
            }
            case 7: {
                return NEG_LONG;
            }
            case 5: {
                return NEG_FLOAT;
            }
            case 4: {
                return NEG_DOUBLE;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opNot(TypeBearer type) {
        switch (type.getBasicFrameType()) {
            case 6: {
                return NOT_INT;
            }
            case 7: {
                return NOT_LONG;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opCmpl(TypeBearer type) {
        switch (type.getBasicType()) {
            case 7: {
                return CMPL_LONG;
            }
            case 5: {
                return CMPL_FLOAT;
            }
            case 4: {
                return CMPL_DOUBLE;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opCmpg(TypeBearer type) {
        switch (type.getBasicType()) {
            case 5: {
                return CMPG_FLOAT;
            }
            case 4: {
                return CMPG_DOUBLE;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opConv(TypeBearer dest, TypeBearer source) {
        int dbt = dest.getBasicFrameType();
        switch (source.getBasicFrameType()) {
            case 6: {
                switch (dbt) {
                    case 7: {
                        return CONV_I2L;
                    }
                    case 5: {
                        return CONV_I2F;
                    }
                    case 4: {
                        return CONV_I2D;
                    }
                }
            }
            case 7: {
                switch (dbt) {
                    case 6: {
                        return CONV_L2I;
                    }
                    case 5: {
                        return CONV_L2F;
                    }
                    case 4: {
                        return CONV_L2D;
                    }
                }
            }
            case 5: {
                switch (dbt) {
                    case 6: {
                        return CONV_F2I;
                    }
                    case 7: {
                        return CONV_F2L;
                    }
                    case 4: {
                        return CONV_F2D;
                    }
                }
            }
            case 4: {
                switch (dbt) {
                    case 6: {
                        return CONV_D2I;
                    }
                    case 7: {
                        return CONV_D2L;
                    }
                    case 5: {
                        return CONV_D2F;
                    }
                }
            }
        }
        return Rops.throwBadTypes(StdTypeList.make(dest.getType(), source.getType()));
    }

    public static Rop opReturn(TypeBearer type) {
        switch (type.getBasicFrameType()) {
            case 6: {
                return RETURN_INT;
            }
            case 7: {
                return RETURN_LONG;
            }
            case 5: {
                return RETURN_FLOAT;
            }
            case 4: {
                return RETURN_DOUBLE;
            }
            case 9: {
                return RETURN_OBJECT;
            }
            case 0: {
                return RETURN_VOID;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opAget(TypeBearer type) {
        switch (type.getBasicType()) {
            case 6: {
                return AGET_INT;
            }
            case 7: {
                return AGET_LONG;
            }
            case 5: {
                return AGET_FLOAT;
            }
            case 4: {
                return AGET_DOUBLE;
            }
            case 9: {
                return AGET_OBJECT;
            }
            case 1: {
                return AGET_BOOLEAN;
            }
            case 2: {
                return AGET_BYTE;
            }
            case 3: {
                return AGET_CHAR;
            }
            case 8: {
                return AGET_SHORT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opAput(TypeBearer type) {
        switch (type.getBasicType()) {
            case 6: {
                return APUT_INT;
            }
            case 7: {
                return APUT_LONG;
            }
            case 5: {
                return APUT_FLOAT;
            }
            case 4: {
                return APUT_DOUBLE;
            }
            case 9: {
                return APUT_OBJECT;
            }
            case 1: {
                return APUT_BOOLEAN;
            }
            case 2: {
                return APUT_BYTE;
            }
            case 3: {
                return APUT_CHAR;
            }
            case 8: {
                return APUT_SHORT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opNewArray(TypeBearer arrayType) {
        Type type = arrayType.getType();
        Type elementType = type.getComponentType();
        switch (elementType.getBasicType()) {
            case 6: {
                return NEW_ARRAY_INT;
            }
            case 7: {
                return NEW_ARRAY_LONG;
            }
            case 5: {
                return NEW_ARRAY_FLOAT;
            }
            case 4: {
                return NEW_ARRAY_DOUBLE;
            }
            case 1: {
                return NEW_ARRAY_BOOLEAN;
            }
            case 2: {
                return NEW_ARRAY_BYTE;
            }
            case 3: {
                return NEW_ARRAY_CHAR;
            }
            case 8: {
                return NEW_ARRAY_SHORT;
            }
            case 9: {
                return new Rop(41, type, (TypeList)StdTypeList.INT, Exceptions.LIST_Error_NegativeArraySizeException, "new-array-object");
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opFilledNewArray(TypeBearer arrayType, int count) {
        Type type = arrayType.getType();
        Type elementType = type.getComponentType();
        if (elementType.isCategory2()) {
            return Rops.throwBadType(arrayType);
        }
        if (count < 0) {
            throw new IllegalArgumentException("count < 0");
        }
        StdTypeList sourceTypes = new StdTypeList(count);
        for (int i = 0; i < count; ++i) {
            sourceTypes.set(i, elementType);
        }
        return new Rop(42, sourceTypes, Exceptions.LIST_Error);
    }

    public static Rop opGetField(TypeBearer type) {
        switch (type.getBasicType()) {
            case 6: {
                return GET_FIELD_INT;
            }
            case 7: {
                return GET_FIELD_LONG;
            }
            case 5: {
                return GET_FIELD_FLOAT;
            }
            case 4: {
                return GET_FIELD_DOUBLE;
            }
            case 9: {
                return GET_FIELD_OBJECT;
            }
            case 1: {
                return GET_FIELD_BOOLEAN;
            }
            case 2: {
                return GET_FIELD_BYTE;
            }
            case 3: {
                return GET_FIELD_CHAR;
            }
            case 8: {
                return GET_FIELD_SHORT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opPutField(TypeBearer type) {
        switch (type.getBasicType()) {
            case 6: {
                return PUT_FIELD_INT;
            }
            case 7: {
                return PUT_FIELD_LONG;
            }
            case 5: {
                return PUT_FIELD_FLOAT;
            }
            case 4: {
                return PUT_FIELD_DOUBLE;
            }
            case 9: {
                return PUT_FIELD_OBJECT;
            }
            case 1: {
                return PUT_FIELD_BOOLEAN;
            }
            case 2: {
                return PUT_FIELD_BYTE;
            }
            case 3: {
                return PUT_FIELD_CHAR;
            }
            case 8: {
                return PUT_FIELD_SHORT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opGetStatic(TypeBearer type) {
        switch (type.getBasicType()) {
            case 6: {
                return GET_STATIC_INT;
            }
            case 7: {
                return GET_STATIC_LONG;
            }
            case 5: {
                return GET_STATIC_FLOAT;
            }
            case 4: {
                return GET_STATIC_DOUBLE;
            }
            case 9: {
                return GET_STATIC_OBJECT;
            }
            case 1: {
                return GET_STATIC_BOOLEAN;
            }
            case 2: {
                return GET_STATIC_BYTE;
            }
            case 3: {
                return GET_STATIC_CHAR;
            }
            case 8: {
                return GET_STATIC_SHORT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opPutStatic(TypeBearer type) {
        switch (type.getBasicType()) {
            case 6: {
                return PUT_STATIC_INT;
            }
            case 7: {
                return PUT_STATIC_LONG;
            }
            case 5: {
                return PUT_STATIC_FLOAT;
            }
            case 4: {
                return PUT_STATIC_DOUBLE;
            }
            case 9: {
                return PUT_STATIC_OBJECT;
            }
            case 1: {
                return PUT_STATIC_BOOLEAN;
            }
            case 2: {
                return PUT_STATIC_BYTE;
            }
            case 3: {
                return PUT_STATIC_CHAR;
            }
            case 8: {
                return PUT_STATIC_SHORT;
            }
        }
        return Rops.throwBadType(type);
    }

    public static Rop opInvokeStatic(Prototype meth) {
        return new Rop(49, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    public static Rop opInvokeVirtual(Prototype meth) {
        return new Rop(50, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    public static Rop opInvokeSuper(Prototype meth) {
        return new Rop(51, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    public static Rop opInvokeDirect(Prototype meth) {
        return new Rop(52, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    public static Rop opInvokeInterface(Prototype meth) {
        return new Rop(53, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    public static Rop opInvokePolymorphic(Prototype meth) {
        return new Rop(58, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    private static Rop opInvokeCustom(Prototype meth) {
        return new Rop(59, meth.getParameterFrameTypes(), StdTypeList.THROWABLE);
    }

    public static Rop opMarkLocal(TypeBearer type) {
        switch (type.getBasicFrameType()) {
            case 6: {
                return MARK_LOCAL_INT;
            }
            case 7: {
                return MARK_LOCAL_LONG;
            }
            case 5: {
                return MARK_LOCAL_FLOAT;
            }
            case 4: {
                return MARK_LOCAL_DOUBLE;
            }
            case 9: {
                return MARK_LOCAL_OBJECT;
            }
        }
        return Rops.throwBadType(type);
    }

    private Rops() {
    }

    private static Rop throwBadType(TypeBearer type) {
        throw new IllegalArgumentException("bad type: " + type);
    }

    private static Rop throwBadTypes(TypeList types) {
        throw new IllegalArgumentException("bad types: " + types);
    }
}

