package cn.bugstack.guide.idea.plugin.infrastructure.utils;

import com.intellij.DynamicBundle;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import java.util.Arrays;
import java.util.function.Supplier;

/**
 * @Author: wenguoxing
 * @Date: 2022/9/7 17:31
 * @Version 1.0
 */
public class MyBundle extends DynamicBundle {
    @NotNull
    public static MyBundle INSTANCE = null;

    public static MyBundle getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new MyBundle();
        }

        return INSTANCE;
    }

    @JvmStatic
    @NotNull
    public String message(@PropertyKey(resourceBundle = "messages.MyBundle") @NotNull String key, @NotNull Object... params) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(params, "params");
        String var10000 = INSTANCE.getMessage(key, Arrays.copyOf(params, params.length));
        Intrinsics.checkNotNullExpressionValue(var10000, "getMessage(key, *params)");
        return var10000;
    }

    @JvmStatic
    @NotNull
    public Supplier messagePointer(@PropertyKey(resourceBundle = "messages.MyBundle") @NotNull String key, @NotNull Object... params) {
        Intrinsics.checkNotNullParameter(key, "key");
        Intrinsics.checkNotNullParameter(params, "params");
        Supplier var10000 = INSTANCE.getLazyMessage(key, Arrays.copyOf(params, params.length));
        Intrinsics.checkNotNullExpressionValue(var10000, "getLazyMessage(key, *params)");
        return var10000;
    }

    /**
     * getValue
     * @param key
     * @return
     */
    public String getValue(String key) {
        String strRet = "";
        strRet = INSTANCE.getResourceBundle().getString(key);
        return strRet;
    }

    private MyBundle() {
        super("messages.MyBundle");
    }


    public MyBundle(@NotNull String pathToBundle) {
        super(pathToBundle);
    }
}