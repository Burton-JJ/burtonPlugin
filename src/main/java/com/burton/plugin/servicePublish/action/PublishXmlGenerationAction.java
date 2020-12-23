package com.burton.plugin.servicePublish.action;

import com.burton.plugin.servicePublish.config.ServicePublishConfig;
import com.burton.plugin.servicePublish.exception.BurtonPluginExecoption;
import com.burton.plugin.servicePublish.util.PsiUtil;
import com.burton.plugin.servicePublish.util.ServicePublishUtil;
import com.burton.plugin.servicePublish.util.TbspInterfaceMethodInfo;
import com.intellij.lang.Language;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.ui.MessageType;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.impl.file.PsiDirectoryFactory;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.PsiShortNamesCache;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.TypeConversionUtil;
import com.intellij.psi.xml.XmlFile;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*********************************
 * <p> 文件名称: PublishXmlGenerationAction
 * <p> 模块名称：com.burton.plugin.servicePublish.action
 * <p> 功能说明: 服务发布action
 * <p> 开发人员：burton
 * <p> 开发时间：2020/12/16
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class PublishXmlGenerationAction extends AnAction {

    private NotificationGroup notificationGroup = new NotificationGroup("servicePublish_id", NotificationDisplayType.BALLOON, true);

    @Override
    public void actionPerformed(final AnActionEvent e) {
        //获取配置
        ServicePublishConfig servicePublishConfig = ServiceManager.getService(ServicePublishConfig.class);
        final Map<String, String> configMap = servicePublishConfig.getConfigResult();

        //动态加载common jar包
        try {
            ServicePublishUtil.addUrl(new File(configMap.get("commonJarPath")));
        } catch (Exception ex) {
            //提示加载jar包出错
            //NotificationGroup notificationGroup = new NotificationGroup("servicePublish_id", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification("加载commonjar包失败", MessageType.ERROR);
            Notifications.Bus.notify(notification);
        }

        //获取选中的文件
        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        //转化为class文件
        PsiClass c = PsiUtil.getPsiClass(psiFile);
        //获取接口所在报名 com.burton.plugin
        PsiJavaFile psiJavaFile = (PsiJavaFile) psiFile;
        String servicePackageName = psiJavaFile.getPackageName();
        //获取接口名
        String serviceName = c.getName();
        String completeServiceName = servicePackageName + "." + serviceName;
        //获取接口的方法列表
        PsiMethod[] methods = c.getMethods();
        final List<TbspInterfaceMethodInfo> interfaceMethodInfoList = new ArrayList<>();
        for (PsiMethod method : methods) {
            TbspInterfaceMethodInfo methodInfo = new TbspInterfaceMethodInfo();
            //方法名
            String methodName = method.getName();
            //获取入参
            PsiParameter[] extParams = method.getParameterList().getParameters();
            PsiParameter extParam = extParams[0];
            //获取入参的Type 其实就是入参的类名
            PsiType reqType = TypeConversionUtil.erasure(extParam.getType());
            //入参类名
            String reqClassName = reqType.getPresentableText();
            //根据入参的类名转为class
            PsiClass reqClass = PsiUtil.getPsiClassByName(reqClassName, e);
            //将入参class转为javaFile 为了获取如在所在包的包名
            PsiJavaFile reqJavaFile = (PsiJavaFile) reqClass.getContainingFile();
            //获取入参所在包包名
//            PsiPackage reqpkg = JavaPsiFacade.getInstance(e.getProject()).findPackage(reqJavaFile.getPackageName());
//            String reqPkgName = reqpkg.getQualifiedName();
            String reqPkgName = reqJavaFile.getPackageName();
            String reqPkgAndName = reqPkgName + "." + reqClassName;

//            PsiTypeParameter[] typeParameters = method.getTypeParameters();
//            PsiTypeParameter reqType = typeParameters[0];
            //出参Type
            PsiType respType = method.getReturnType();
            //出参类名
            String respClassName = respType.getPresentableText();
            //根据出参的类名转为class
            PsiClass respClass = PsiUtil.getPsiClassByName(respClassName, e);
            //将入参class转为javaFile 为了获取如在所在包的包名
            PsiJavaFile respJavaFile = (PsiJavaFile) respClass.getContainingFile();
            //获取入参所在包包名
//            PsiPackage resppkg = JavaPsiFacade.getInstance(e.getProject()).findPackage(respJavaFile.getPackageName());
//            String respPkgName = resppkg.getQualifiedName();
            String respPkgName = respJavaFile.getPackageName();
            String respPkgAndName = respPkgName + "." + respClassName;

            methodInfo.setServiceName(completeServiceName);
            methodInfo.setServicePackageName(servicePackageName);
            methodInfo.setMethodName(methodName);
            methodInfo.setParamName(reqPkgAndName);
            methodInfo.setReturnName(respPkgAndName);
            interfaceMethodInfoList.add(methodInfo);

//            JvmParameter[] parameters = method.getParameters();
//            JvmParameter req = parameters[0];
        }
        if (!interfaceMethodInfoList.isEmpty()) {
            ApplicationManager.getApplication().runWriteAction(
                    new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String serviceWrapperXmlPath = configMap.get("serviceWrapperXmlPath");
                                String serviceXmlPath = configMap.get("serviceWrapperXmlPath");
                                if ("待填写".equals(serviceWrapperXmlPath) || "".equals(serviceWrapperXmlPath) || "待填写".equals(serviceXmlPath) || "".equals(serviceXmlPath)) {
                                    throw new BurtonPluginExecoption("服务发布配置未填写");
                                }
                                ServicePublishUtil.generateServiceWrapper(interfaceMethodInfoList, serviceWrapperXmlPath, e);
                                Notification notification = notificationGroup.createNotification("生成ServiceWrapperXml文件成功", MessageType.INFO);
                                Notifications.Bus.notify(notification);
                                ServicePublishUtil.generateService(interfaceMethodInfoList, serviceXmlPath, e);
                                Notification notification2 = notificationGroup.createNotification("生成ServiceXml文件成功", MessageType.INFO);
                                Notifications.Bus.notify(notification2);
                            } catch (BurtonPluginExecoption ex) {
                                //提示服务发布出错
                                Notification notification = notificationGroup.createNotification(ex.getErrorMsg(), MessageType.ERROR);
                                Notifications.Bus.notify(notification);
                                ex.printStackTrace();
                            } catch (Exception ex) {
                                //提示服务发布出错
                                Notification notification = notificationGroup.createNotification("生成服务发布文件失败", MessageType.ERROR);
                                Notifications.Bus.notify(notification);
                                ex.printStackTrace();
                            }
                        }
                    }
            );

        }

    }

    /**
     * 按钮什么情况下能显示 什么情况下不能显示
     *
     * @param e
     */
    @Override
    public void update(@NotNull AnActionEvent e) {
        //只有接口文件可以点击按钮
        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        Language language = psiFile.getLanguage();
        String languageType = language.getID().toLowerCase();
        if ("java".equals(languageType) && PsiUtil.getPsiClass(psiFile).isInterface()) {
            e.getPresentation().setEnabled(true);
        } else {
            e.getPresentation().setEnabled(false);
        }

//        if ("java".equals(getFileExtension(e.getDataContext()))) {
//            e.getPresentation().setEnabled(true);
//        } else {
//            e.getPresentation().setEnabled(false);
//        }
    }


}
