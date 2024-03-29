// automatically generated by the FlatBuffers compiler, do not modify

package io.lindb.client.flat.metrics.v1;

import com.google.flatbuffers.BaseVector;
import com.google.flatbuffers.BooleanVector;
import com.google.flatbuffers.ByteVector;
import com.google.flatbuffers.Constants;
import com.google.flatbuffers.DoubleVector;
import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FloatVector;
import com.google.flatbuffers.IntVector;
import com.google.flatbuffers.LongVector;
import com.google.flatbuffers.ShortVector;
import com.google.flatbuffers.StringVector;
import com.google.flatbuffers.Struct;
import com.google.flatbuffers.Table;
import com.google.flatbuffers.UnionVector;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@javax.annotation.Generated(value="flatc")
@SuppressWarnings("unused")
public final class CompoundField extends Table {
  public static void ValidateVersion() { Constants.FLATBUFFERS_23_3_3(); }
  public static CompoundField getRootAsCompoundField(ByteBuffer _bb) { return getRootAsCompoundField(_bb, new CompoundField()); }
  public static CompoundField getRootAsCompoundField(ByteBuffer _bb, CompoundField obj) { _bb.order(ByteOrder.LITTLE_ENDIAN); return (obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb)); }
  public void __init(int _i, ByteBuffer _bb) { __reset(_i, _bb); }
  public CompoundField __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public io.lindb.client.flat.metrics.v1.Exemplar exemplars(int j) { return exemplars(new io.lindb.client.flat.metrics.v1.Exemplar(), j); }
  public io.lindb.client.flat.metrics.v1.Exemplar exemplars(io.lindb.client.flat.metrics.v1.Exemplar obj, int j) { int o = __offset(4); return o != 0 ? obj.__assign(__indirect(__vector(o) + j * 4), bb) : null; }
  public int exemplarsLength() { int o = __offset(4); return o != 0 ? __vector_len(o) : 0; }
  public io.lindb.client.flat.metrics.v1.Exemplar.Vector exemplarsVector() { return exemplarsVector(new io.lindb.client.flat.metrics.v1.Exemplar.Vector()); }
  public io.lindb.client.flat.metrics.v1.Exemplar.Vector exemplarsVector(io.lindb.client.flat.metrics.v1.Exemplar.Vector obj) { int o = __offset(4); return o != 0 ? obj.__assign(__vector(o), 4, bb) : null; }
  public double min() { int o = __offset(6); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double max() { int o = __offset(8); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double sum() { int o = __offset(10); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double count() { int o = __offset(12); return o != 0 ? bb.getDouble(o + bb_pos) : 0.0; }
  public double explicitBounds(int j) { int o = __offset(14); return o != 0 ? bb.getDouble(__vector(o) + j * 8) : 0; }
  public int explicitBoundsLength() { int o = __offset(14); return o != 0 ? __vector_len(o) : 0; }
  public DoubleVector explicitBoundsVector() { return explicitBoundsVector(new DoubleVector()); }
  public DoubleVector explicitBoundsVector(DoubleVector obj) { int o = __offset(14); return o != 0 ? obj.__assign(__vector(o), bb) : null; }
  public ByteBuffer explicitBoundsAsByteBuffer() { return __vector_as_bytebuffer(14, 8); }
  public ByteBuffer explicitBoundsInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 14, 8); }
  public double values(int j) { int o = __offset(16); return o != 0 ? bb.getDouble(__vector(o) + j * 8) : 0; }
  public int valuesLength() { int o = __offset(16); return o != 0 ? __vector_len(o) : 0; }
  public DoubleVector valuesVector() { return valuesVector(new DoubleVector()); }
  public DoubleVector valuesVector(DoubleVector obj) { int o = __offset(16); return o != 0 ? obj.__assign(__vector(o), bb) : null; }
  public ByteBuffer valuesAsByteBuffer() { return __vector_as_bytebuffer(16, 8); }
  public ByteBuffer valuesInByteBuffer(ByteBuffer _bb) { return __vector_in_bytebuffer(_bb, 16, 8); }

  public static int createCompoundField(FlatBufferBuilder builder,
      int exemplarsOffset,
      double min,
      double max,
      double sum,
      double count,
      int explicitBoundsOffset,
      int valuesOffset) {
    builder.startTable(7);
    CompoundField.addCount(builder, count);
    CompoundField.addSum(builder, sum);
    CompoundField.addMax(builder, max);
    CompoundField.addMin(builder, min);
    CompoundField.addValues(builder, valuesOffset);
    CompoundField.addExplicitBounds(builder, explicitBoundsOffset);
    CompoundField.addExemplars(builder, exemplarsOffset);
    return CompoundField.endCompoundField(builder);
  }

  public static void startCompoundField(FlatBufferBuilder builder) { builder.startTable(7); }
  public static void addExemplars(FlatBufferBuilder builder, int exemplarsOffset) { builder.addOffset(0, exemplarsOffset, 0); }
  public static int createExemplarsVector(FlatBufferBuilder builder, int[] data) { builder.startVector(4, data.length, 4); for (int i = data.length - 1; i >= 0; i--) builder.addOffset(data[i]); return builder.endVector(); }
  public static void startExemplarsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(4, numElems, 4); }
  public static void addMin(FlatBufferBuilder builder, double min) { builder.addDouble(1, min, 0.0); }
  public static void addMax(FlatBufferBuilder builder, double max) { builder.addDouble(2, max, 0.0); }
  public static void addSum(FlatBufferBuilder builder, double sum) { builder.addDouble(3, sum, 0.0); }
  public static void addCount(FlatBufferBuilder builder, double count) { builder.addDouble(4, count, 0.0); }
  public static void addExplicitBounds(FlatBufferBuilder builder, int explicitBoundsOffset) { builder.addOffset(5, explicitBoundsOffset, 0); }
  public static int createExplicitBoundsVector(FlatBufferBuilder builder, double[] data) { builder.startVector(8, data.length, 8); for (int i = data.length - 1; i >= 0; i--) builder.addDouble(data[i]); return builder.endVector(); }
  public static void startExplicitBoundsVector(FlatBufferBuilder builder, int numElems) { builder.startVector(8, numElems, 8); }
  public static void addValues(FlatBufferBuilder builder, int valuesOffset) { builder.addOffset(6, valuesOffset, 0); }
  public static int createValuesVector(FlatBufferBuilder builder, double[] data) { builder.startVector(8, data.length, 8); for (int i = data.length - 1; i >= 0; i--) builder.addDouble(data[i]); return builder.endVector(); }
  public static void startValuesVector(FlatBufferBuilder builder, int numElems) { builder.startVector(8, numElems, 8); }
  public static int endCompoundField(FlatBufferBuilder builder) {
    int o = builder.endTable();
    return o;
  }

  public static final class Vector extends BaseVector {
    public Vector __assign(int _vector, int _element_size, ByteBuffer _bb) { __reset(_vector, _element_size, _bb); return this; }

    public CompoundField get(int j) { return get(new CompoundField(), j); }
    public CompoundField get(CompoundField obj, int j) {  return obj.__assign(__indirect(__element(j), bb), bb); }
  }
}

