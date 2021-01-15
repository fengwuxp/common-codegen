package com.wuxp.codegen.core.event;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Data;

import java.util.concurrent.Executors;

/**
 * 基于 disruptor 队列的的事件发送者
 *
 * @author wxup
 */
public class DisruptorCodeGenPublisher<T extends CommonCodeGenClassMeta> implements CodeGenPublisher<T> {


  private Disruptor<CodegenEvent<T>> disruptor;
  private RingBuffer<CodegenEvent<T>> ringBuffer;

  public DisruptorCodeGenPublisher() {
    this(null);
  }

  public DisruptorCodeGenPublisher(EventHandler<CodegenEvent<T>> defaultHandler) {
    this(new Disruptor<CodegenEvent<T>>(new CodegenEventFactory<T>(), 1024, Executors.defaultThreadFactory()), defaultHandler);
  }


  public DisruptorCodeGenPublisher(Disruptor<CodegenEvent<T>> disruptor, EventHandler<CodegenEvent<T>> defaultHandler) {
    this.disruptor = disruptor;
    if (defaultHandler != null) {
      disruptor.handleEventsWith(defaultHandler);
    }
    // Start the Disruptor, starts all threads running
    disruptor.start();
    // Get the ring buffer from the Disruptor to be used for publishing.
    this.ringBuffer = disruptor.getRingBuffer();
  }


  @Override
  public void sendCodeGen(T data) {
    this.sendCodeGenError(null, data);
  }

  @Override
  public void sendCodeGenError(Exception exception, T data) {
    // Grab the next sequence
    long sequence = ringBuffer.next();
    try {
      // Get the entry in the Disruptor
      CodegenEvent<T> codegenEvent = ringBuffer.get(sequence);
      // for the sequence
      // Fill with data
      codegenEvent.setException(exception);
      codegenEvent.setGenData(data);
    } finally {
      ringBuffer.publish(sequence);
    }
  }

  @Override
  public void sendCodeGenEnd() {
    this.sendCodeGenError(null, null);
  }

//    public final EventHandlerGroup<CodegenEvent<T>> handleEventsWith(final EventHandler<? super CodegenEvent<T>>... handlers) {
//        return disruptor.handleEventsWith(handlers);
//    }

  /**
   * code gen event load
   */
  @Data
  public static class CodegenEvent<T extends CommonCodeGenClassMeta> {

    /**
     * 异常
     */
    private Exception exception;

    /**
     * 用于生成代码的数据
     */
    private T genData;

    /**
     * 生成完成
     *
     * @return
     */
    public boolean isEndEvent() {
      return this.exception == null && genData == null;
    }

    /**
     * 本次是否生成生成
     *
     * @return
     */
    public boolean isCodegenEvent() {
      return this.exception == null;
    }

  }


  public static class CodegenEventFactory<T extends CommonCodeGenClassMeta> implements EventFactory<CodegenEvent<T>> {

    @Override
    public CodegenEvent<T> newInstance() {
      return new CodegenEvent<T>();
    }
  }

}
