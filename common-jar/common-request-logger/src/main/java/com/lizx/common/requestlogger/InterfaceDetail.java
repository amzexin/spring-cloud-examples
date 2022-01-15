package com.lizx.common.requestlogger;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2021-05-17 14:35
 */
@Data
public class InterfaceDetail {
    // region http
    /**
     * 请求方式
     */
    @JSONField(ordinal = 1)
    private String httpRequestMethod;
    /**
     * 请求接口
     */
    @JSONField(ordinal = 2)
    private String httpRequestURI;
    // endregion

    // region thrift
    /**
     * 接口名称
     */
    @JSONField(ordinal = 1)
    private String thriftClassName;
    /**
     * 方法名称
     */
    @JSONField(ordinal = 2)
    private String thriftMethodName;
    // endregion

    // region mqtt
    /**
     * mqtt topic
     */
    @JSONField(ordinal = 1)
    private String mqttTopic;
    /**
     * 消息质量
     */
    @JSONField(ordinal = 2)
    private Integer mqttQos;
    // endregion
}
