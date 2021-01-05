package cn.vove7.antiscreenshot

import android.app.AndroidAppHelper
import android.graphics.Bitmap
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.nio.ByteBuffer

/**
 * # HookEntry
 *
 * @author Vove
 * 2021/1/5
 */
class HookEntry : IXposedHookLoadPackage {


    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {

        if (lpparam?.packageName == "cn.vove7.antiscreenshot") {
            XposedBridge.hookAllMethods(
                lpparam.classLoader.loadClass("cn.vove7.antiscreenshot.MainActivity"),
                "enabled",
                object : XC_MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam?) {
                        XposedBridge.log("antiscreenshot 模块生效")
                        param?.result = true
                    }
                })
            return
        }

        val TARGET_PKG = XSharedPreferences("cn.vove7.antiscreenshot", "cn.vove7.antiscreenshot")
            .getString("target", "com.chaoxing.mobile")!!

        XposedBridge.log("target: $TARGET_PKG")

        if (lpparam?.packageName != TARGET_PKG) {
            return
        }



        XposedBridge.hookAllMethods(Bitmap::class.java, "copyPixelsFromBuffer",
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    param.args[0] = ByteBuffer.allocate((param.thisObject as Bitmap).byteCount)
                    XposedBridge.log("截屏hook ${AndroidAppHelper.currentPackageName()}")
                }
            })


    }

}