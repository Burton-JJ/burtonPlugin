package com.burton.plugin.servicePublish.config;

import com.burton.plugin.servicePublish.ui.ServicePublishConfigForm;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/*********************************
 * <p> 文件名称: ServicePublishConfigurable
 * <p> 模块名称：com.burton.plugin.servicepublish.settings
 * <p> 功能说明: 继承自SearchableConfigurable就能显示在setting
 * <p> 开发人员：burton
 * <p> 开发时间：2020/8/26
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class ServicePublishConfigurable implements SearchableConfigurable {
    /**
     * 保存应用配置
     */
    private ServicePublishConfig servicePublishConfig;
    /**
     * 面板
     */
    private ServicePublishConfigForm servicePublishConfigForm;

    public ServicePublishConfigurable() {
        servicePublishConfig = ServicePublishConfig.getInstance();
    }

    @NotNull
    @Override
    public String getId() {
        return "servicePublish.config";
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "1.0ServicePublish";
    }

    /**
     * 点击左侧菜单栏 弹出的面板
     *
     * @return
     */
    @Nullable
    @Override
    public JComponent createComponent() {
        if (servicePublishConfigForm == null) {
            servicePublishConfigForm = new ServicePublishConfigForm();
        }
        return servicePublishConfigForm.getMainPane();
    }

    /**
     * 判断设置中内容是否被改变
     *
     * @return
     */
    @Override
    public boolean isModified() {
        //老的配置信息
        Map<String, String> oldConfig = servicePublishConfig.getConfigResult();
        //新录入的信息
        String serviceXmlPath = servicePublishConfigForm.getServiceXmlPath().getText();
        String serviceWrapperXmlPath = servicePublishConfigForm.getServiceWrapperXmlPath().getText();
        String commonJarPath = servicePublishConfigForm.getCommonJarPath().getText();
        if (!serviceXmlPath.equals(oldConfig.get("serviceXmlPath")) || !serviceWrapperXmlPath.equals(oldConfig.get("serviceWrapperXmlPath"))
                || !commonJarPath.equals(oldConfig.get("commonJarPath"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 点击左侧菜单栏 弹出的面板 修改设置后 点确定调用 修改了设置后会调这个方法
     *
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException {
        //新录入的信息
        String serviceXmlPath = servicePublishConfigForm.getServiceXmlPath().getText();
        String serviceWrapperXmlPath = servicePublishConfigForm.getServiceWrapperXmlPath().getText();
        String commonJarPath = servicePublishConfigForm.getCommonJarPath().getText();
        Map<String, String> newConfig = new HashMap<>();
        newConfig.put("serviceXmlPath", serviceXmlPath);
        newConfig.put("serviceWrapperXmlPath", serviceWrapperXmlPath);
        newConfig.put("commonJarPath", commonJarPath);
        //设置新的配置信息
        servicePublishConfig.setConfigResult(newConfig);

    }

    @Override
    public void reset() {
        this.servicePublishConfigForm.getServiceXmlPath().setText(this.servicePublishConfig.getConfigResult().get("serviceXmlPath"));
        this.servicePublishConfigForm.getServiceWrapperXmlPath().setText(this.servicePublishConfig.getConfigResult().get("serviceWrapperXmlPath"));
        this.servicePublishConfigForm.getCommonJarPath().setText(this.servicePublishConfig.getConfigResult().get("commonJarPath"));
    }
}
