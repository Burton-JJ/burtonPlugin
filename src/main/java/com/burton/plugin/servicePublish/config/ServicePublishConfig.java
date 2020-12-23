package com.burton.plugin.servicePublish.config;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/*********************************
 * <p> 文件名称: ServicePublishConfig
 * <p> 模块名称：com.burton.plugin.servicepublish.settings
 * <p> 功能说明: 保存设置中的配置
 * <p> 开发人员：burton
 * <p> 开发时间：2020/8/26
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
@State(name = "myServicePublishConfig", storages = @Storage("$APP_CONFIG$/servicePublish-settings.xml"))
public class ServicePublishConfig implements PersistentStateComponent<ServicePublishConfig> {
    private Map<String, String> configResult;
//    @Transient
//    private List<ConfigListenersRegistry> registries = new ArrayList<>();

    /**
     * 构造方法 --选择框默认显示文字设置
     */
    public ServicePublishConfig() {
        loadDefaultSettings();
    }


    public static ServicePublishConfig getInstance() {
        return ServiceManager.getService(ServicePublishConfig.class);
    }


    /**
     * 组件加载时读入数据
     * 返回当前组件的state
     * <p>
     * 修改配置项后会调用这个方法 this便会是新的配置
     *
     * @return
     */
    @Nullable
    @Override
    public ServicePublishConfig getState() {
        return this;
    }

    /**
     * 组件关闭时存入数据
     * 新的组件状态被加载时，调用该方法，如果IDE运行期间，保存数据的文件被从外部修改，则该方法会被再次调用
     * <p>
     * <p>
     * 配置修改后重新读取配置
     *
     * @param servicePublishConfig
     */
    @Override
    public void loadState(@NotNull ServicePublishConfig servicePublishConfig) {
        XmlSerializerUtil.copyBean(servicePublishConfig, this);
        //下面这句功能相同
        //setConfigResult(servicePublishConfig.getConfigResult());
    }

    /**
     * 默认文字设置
     */
    private void loadDefaultSettings() {
        this.configResult = new HashMap<>();
        configResult.put("serviceXmlPath", "待填写");
        configResult.put("serviceWrapperXmlPath", "待填写");
        configResult.put("commonJarPath", "待填写");
    }

    public Map<String, String> getConfigResult() {
        return configResult;
    }

    public void setConfigResult(Map<String, String> configResult) {
        this.configResult = configResult;
//        return notifyRegistries();
    }


//    public void registries(List<ConfigListenersRegistry> registry) {
//        registries.addAll(registry);
//    }
//
//    private String notifyRegistries() {
//        for (ConfigListenersRegistry registry : registries) {
//            String res= registry.refresh(this);
//            if (StringUtils.isNotEmpty(res)){
//                return res;
//            }
//        }
//        return StringUtils.EMPTY;
//    }
}
