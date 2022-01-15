package com.lizx.common.requestlogger.plugin;

/**
 * 开关插件
 *
 * @author lizexin
 * @date 2021-12-14 19:02
 */
public interface SwitchPlugin {

    /**
     * 是否打开？
     * true：表示需要打印log
     * false：表示不需要打印log
     *
     * @return
     */
    boolean opened();

}
