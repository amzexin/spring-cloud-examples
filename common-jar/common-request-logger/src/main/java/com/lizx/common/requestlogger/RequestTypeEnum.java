package com.lizx.common.requestlogger;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-01-20 11:02
 */
@Getter
@AllArgsConstructor
public enum RequestTypeEnum {
    /**
     * http类型接口
     */
    HTTP("http"),
    /**
     * thrift类型接口
     */
    THRIFT("thrift"),
    /**
     * mqtt类型接口
     */
    MQTT("mqtt"),
    ;

    private String key;

}
