package com.burton.plugin.servicePublish.util;

/*********************************
 * <p> 文件名称: TbspInterfaceMethodInfo
 * <p> 模块名称：com.burton.plugin.servicepublish.utils
 * <p> 功能说明: 
 * <p> 开发人员：burton
 * <p> 开发时间：2020/8/28
 * <p> 修改记录：程序版本   修改日期    修改人员   修改单号   修改说明
 **********************************/
public class TbspInterfaceMethodInfo {
    String serviceName;
    String servicePackageName;
    String methodName;
    String paramName;
    String returnName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getReturnName() {
        return returnName;
    }

    public void setReturnName(String returnName) {
        this.returnName = returnName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePackageName() {
        return servicePackageName;
    }

    public void setServicePackageName(String servicePackageName) {
        this.servicePackageName = servicePackageName;
    }
}
