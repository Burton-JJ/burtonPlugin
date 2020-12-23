package com.burton.plugin.servicePublish.config;

/*********************************
 * <p> 文件名称: ConfigListenersRegistry
 * <p> 模块名称：com.burton.plugin.servicepublish.settings
 * <p> 功能说明: 监听配置变化的回调接口
 * <p> 开发人员：burton
 * <p> 开发时间：2020/8/26
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public interface ConfigListenersRegistry<T> {
    /**
     * 根据配置刷新资源
     *
     * @param settings
     * @return
     */
    String refresh(T settings);
}
