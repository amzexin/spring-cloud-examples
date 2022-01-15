package com.lizx.common.requestlogger.plugin;

/**
 * Description: DefaultSwitchPlugin
 *
 * @author Lizexin
 * @date 2021-12-14 19:01
 */
public class DefaultSwitchPlugin implements SwitchPlugin {

    @Override
    public boolean opened() {
        return true;
    }

}
