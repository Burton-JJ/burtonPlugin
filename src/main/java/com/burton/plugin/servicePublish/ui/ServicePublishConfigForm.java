package com.burton.plugin.servicePublish.ui;


import com.burton.plugin.servicePublish.config.ServicePublishConfig;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/*********************************
 * <p> 文件名称: ServicePublishConfigForm
 * <p> 模块名称：com.burton.plugin.servicepublish.settings
 * <p> 功能说明: 
 * <p> 开发人员：burton
 * <p> 开发时间：2020/8/26
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class ServicePublishConfigForm {
    /**
     * 主面板
     */
    private JPanel mainPane;
    private JTextField serviceXmlPath;
    private JTextField serviceWrapperXmlPath;
    private JTextField commonJarPath;
    private JButton serviceXmlBrowser;
    private JButton serviceWrapperXmlBrowser;
    private JButton commonJarBrowser;

    /**
     * 构造方法
     */
    public ServicePublishConfigForm() {
        ServicePublishConfig settings = ServicePublishConfig.getInstance();
        Map<String, String> configResult = settings.getConfigResult();
        initInput(configResult);
        //region 添加serviceXmlBrowser按钮事件
        serviceXmlBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileOrPkgSelect(JFileChooser.DIRECTORIES_ONLY, serviceXmlPath);
            }
        });
        //endregion
        //region 添加serviceWrapperXmlBrowser按钮事件
        serviceWrapperXmlBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileOrPkgSelect(JFileChooser.DIRECTORIES_ONLY, serviceWrapperXmlPath);
            }
        });
        //endregion
        //region 添加commonJarBrowser按钮事件
        commonJarBrowser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fileOrPkgSelect(JFileChooser.FILES_ONLY, commonJarPath);
            }
        });
        //endregion
    }

    /**
     * 初始化填写框
     *
     * @param configResult
     */
    private void initInput(Map<String, String> configResult) {
        serviceXmlPath.setText(configResult.get("serviceXmlPath"));
        serviceWrapperXmlPath.setText(configResult.get("serviceWrapperXmlPath"));
        commonJarPath.setText(configResult.get("commonJarPath"));
    }

    /**
     * 选择文件或者文件夹按钮事件
     *
     * @param fileSelectionMode
     * @param textField
     */
    private void fileOrPkgSelect(int fileSelectionMode, JTextField textField) {
        String userDir = System.getProperty("user.home");
        JFileChooser fileChooser = new JFileChooser(userDir + "/Desktop");
        if (JFileChooser.FILES_ONLY == fileSelectionMode) {
            //只能选文件
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        } else if (JFileChooser.DIRECTORIES_ONLY == fileSelectionMode) {
            //只能选文件夹
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else if (JFileChooser.FILES_AND_DIRECTORIES == fileSelectionMode) {
            //文件和文件夹都可以选
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        } else {
            fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        int flag = fileChooser.showOpenDialog(null);
        //若获取成功,则拿到所选文件/文件夹的绝对路径
        if (flag == JFileChooser.APPROVE_OPTION) {
            textField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public JPanel getMainPane() {
        return mainPane;
    }

    public void setMainPane(JPanel mainPane) {
        this.mainPane = mainPane;
    }

    public JTextField getServiceXmlPath() {
        return serviceXmlPath;
    }

    public void setServiceXmlPath(JTextField serviceXmlPath) {
        this.serviceXmlPath = serviceXmlPath;
    }

    public JTextField getServiceWrapperXmlPath() {
        return serviceWrapperXmlPath;
    }

    public void setServiceWrapperXmlPath(JTextField serviceWrapperXmlPath) {
        this.serviceWrapperXmlPath = serviceWrapperXmlPath;
    }

    public JTextField getCommonJarPath() {
        return commonJarPath;
    }

    public void setCommonJarPath(JTextField commonJarPath) {
        this.commonJarPath = commonJarPath;
    }
}
