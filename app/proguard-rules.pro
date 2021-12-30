

-dontshrink
-keep public class * extends androidx.**.*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.** {*;}
-keep class androidx.** {*;}
-keep public class * extends android.view.View {
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    *;
}
-keep class **.R$* {
    *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
-keep class com.aoyuehan.permission.bean.PermissionBean {*;}
-keep class com.aoyuehan.permission.utils.PermissionDialogHelper {*;}
-keep interface com.aoyuehan.permission.callBack.CallBack {*;}
#Fastjson混淆配置
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

