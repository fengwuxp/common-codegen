package com.wuxp.codegen.core.event;

import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.util.concurrent.locks.LockSupport;

/**
 * publish codegen event if support Park {@link CodeGenPublisher#supportPark} {@link LockSupport#unpark(Thread)} {@link
 * CodeGenerator#generate()}
 *
 * @author wxup
 */
public interface CodeGenPublisher<T extends CommonCodeGenClassMeta> {

    /**
     * 默认的最大的part nanos
     */
    long DEFAULT_MAX_PARK_NANOS = 10L * 1000 * 1000 * 100;

    CodeGenPublisher NONE = new CodeGenPublisher() {
        @Override
        public void sendCodeGen(CommonCodeGenClassMeta data) {

        }

        @Override
        public void sendCodeGenError(Exception exception, CommonCodeGenClassMeta data) {

        }

        @Override
        public void sendCodeGenEnd() {

        }

        @Override
        public boolean supportPark() {
            return false;
        }
    };


    /**
     * 发送一次生成事件
     *
     * @param data
     */
    void sendCodeGen(T data);

    /**
     * 发送生成异常事件
     *
     * @param exception
     * @param data
     */
    void sendCodeGenError(Exception exception, T data);

    /**
     * 发送生成完成事件
     */
    void sendCodeGenEnd();

    /**
     * 是否是否支持 {@link LockSupport}
     *
     * @return
     */
    default boolean supportPark() {
        return true;
    }

    /**
     * 默认最多等待10秒
     *
     * @return 获取最大的等待纳秒数 if return code>-1</code> 则一直等待
     */
    default long getMaxParkNanos() {
        return DEFAULT_MAX_PARK_NANOS;
    }
}
