# دليل استخدام موديول الـ Splash Screen (`:core-splashscreen`)

> هذا الملف هو دليل الاستخدام والتعديل: كيف تربط الموديول بالتطبيق، وأين تغيّر كل شيء.
> لشرح طريقة العمل من الداخل راجع [Details.md](./Details.md).

الموديول نسخة معدّلة (fork) من مكتبة `androidx.core:core-splashscreen`، وتضيف عليها:

- **Entry animation للأيقونة** (scale + rotate) تعمل على الـ render thread فلا تتقطع أثناء إقلاع التطبيق.
- **Branding badge** من سطرين نصيين أسفل الشاشة، يدخل بحركة overshoot.
- **حد أدنى لمدة العرض** وخروج بـ fade.
- نفس السلوك على كل الإصدارات (minSdk 28)، بدون `values-v31` في التطبيق.

---

## 1. الفكرة في سطرين

النظام يرسم شاشة البداية قبل أن يعمل أي كود من التطبيق، ولا يستطيع تشغيل أنيميشن مخصصة. لذلك النظام يعرض **الخلفية فقط**، وعند أول frame يرسمه التطبيق يستلم الموديول الشاشة بـ view مطابق ويشغّل عليه الأنيميشن.

---

## 2. الربط بالتطبيق

### 2.1 الـ Gradle

```kotlin
// settings.gradle.kts
include(":core-splashscreen")

// app/build.gradle.kts
implementation(project(":core-splashscreen"))
```

### 2.2 الـ Theme

في `res/values/themes.xml` (ونسخة مطابقة بلون خلفية مختلف في `res/values-night/themes.xml`):

```xml
<style name="Theme.PetCompose.Splash" parent="Theme.SplashScreen">
    <item name="windowSplashScreenBackground">#FBFBFE</item>
    <!-- النظام يعرض الخلفية فقط، والشعار يدخل بالـ entry animation -->
    <item name="windowSplashScreenAnimatedIcon">@android:color/transparent</item>
    <item name="splashScreenIntroAnimatedIcon">@drawable/ic_splash_animated</item>
    <item name="splashScreenBrandingLabel">POWERED BY</item>
    <item name="splashScreenBrandingName">Ali Hesham</item>
    <item name="postSplashScreenTheme">@style/Theme.PetCompose</item>
</style>
```

استخدم الـ attributes بدون البادئة `android:`. الموديول يحوّلها لنظيرتها في النظام على Android 12+، ولو كتبتها بـ `android:` سيظهر خطأ lint يطلب `values-v31`.

### 2.3 الـ Manifest

```xml
<activity
    android:name=".MainActivity"
    android:theme="@style/Theme.PetCompose.Splash" />
```

### 2.4 الـ Activity

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    val splashScreen = installSplashScreen()   // قبل super.onCreate
    super.onCreate(savedInstanceState)

    splashScreen.setKeepOnScreenCondition {
        mainViewModel.uiState.value is MainActivityUiState.Loading
    }

    setContent { /* ... */ }
}
```

يجب استدعاء `setKeepOnScreenCondition` أو `setOnExitAnimationListener` مرة واحدة على الأقل. بدون أي منهما لا يُضاف الـ view الخاص بالموديول، ولا تعمل الأنيميشن.

---

## 3. الـ Theme attributes

| الـ Attribute | الوظيفة |
|---|---|
| `windowSplashScreenBackground` | لون خلفية الشاشة. |
| `windowSplashScreenAnimatedIcon` | الأيقونة التي يرسمها النظام قبل إقلاع التطبيق (ثابتة دائماً). اجعلها شفافة مع الـ entry animation. |
| `splashScreenIntroAnimatedIcon` | الـ animated vector الذي يشتغل بعد استلام الموديول للشاشة. |
| `splashScreenBrandingLabel` | السطر الأول (الصغير) في الـ badge. |
| `splashScreenBrandingName` | السطر الثاني (العريض) في الـ badge. |
| `postSplashScreenTheme` | الـ theme الذي يُطبَّق على الـ Activity بعد `installSplashScreen()`. |
| `windowSplashScreenIconBackgroundColor` | لون دائرة خلف الأيقونة. يعمل فقط مع الأب `Theme.SplashScreen.IconBackground`. |

لو تُرك أحد سطري الـ branding فارغاً يختفي ذلك السطر، ولو تُرك الاثنان يختفي الـ badge كله.

---

## 4. أين أعدّل؟

### 4.1 حركة الأيقونة

الملفات في التطبيق، لا في الموديول:

| الملف | المحتوى |
|---|---|
| `app/src/main/res/drawable/ic_splash_icon.xml` | رسم الشعار داخل group اسمه `logo_group`. |
| `app/src/main/res/animator/splash_icon_animator.xml` | الحركة: rotation من ‎-180 إلى 0، و scale من 0 إلى 1 مع overshoot، في 700ms. |
| `app/src/main/res/drawable/ic_splash_animated.xml` | يربط الاثنين معاً. |

قاعدتان:

1. **قيم البداية يجب أن تتطابق.** قيم `valueFrom` في الـ animator يجب أن تساوي قيم `rotation` و `scaleX` و `scaleY` المكتوبة على `logo_group`. الـ vector يُعرض بقيمه الأصلية إلى أن تبدأ الحركة، وأي اختلاف يظهر كقفزة.
2. **أول frame يجب أن يطابق ما يرسمه النظام.** مع entry animation الاثنان غير مرئيين (أيقونة شفافة و scale يساوي 0).

الشعار مرسوم كدائرة نصف قطرها 36 من viewport مقاسه 108، أي ثلثي المساحة. هذا يترك مساحة للـ overshoot دون أن يُقص الرسم.

### 4.2 حركة الـ branding

| ماذا | أين |
|---|---|
| المدة وحجم البداية (30%) | `res/anim/splash_branding_enter.xml` |
| قوة الارتداد (`tension`) | `res/interpolator/splash_branding_overshoot.xml` |
| حركة الخروج | `res/anim/splash_branding_exit.xml` |
| الخطوط والألوان والـ padding | `res/layout/splash_screen_branding.xml` |
| خلفية الـ badge | `res/drawable/bg_splash_branding_badge.xml` (ونسخة `drawable-night`) |
| المسافة من أسفل الشاشة | `splashscreen_branding_margin_bottom` في `res/values/dimens.xml` |

اجعل مدة دخول الـ badge مساوية لمدة حركة الأيقونة ليستقرا معاً.

### 4.3 المدة والخروج

| ماذا | أين |
|---|---|
| الحد الأدنى لمدة العرض (1500ms) | `setKeepOnScreenCondition(minDurationMs = ...)` عند الاستدعاء |
| الخروج الافتراضي (fade في 220ms) | `defaultSplashExitAnimation` في `SplashScreen.kt` |
| خروج مخصص | `setOnExitAnimationListener` |

مدة العرض تُحسب من لحظة بدء الأنيميشن، لا من لحظة الضغط على أيقونة التطبيق.

مثال لخروج مخصص:

```kotlin
splashScreen.setOnExitAnimationListener { provider ->
    provider.iconView.animate()
        .scaleX(0f).scaleY(0f)
        .setDuration(250)
        .withEndAction { provider.remove() }   // إلزامي
        .start()
}
```

`provider.remove()` إلزامي مع أي listener مخصص، وإلا تبقى الشاشة فوق التطبيق.

---

## 5. قواعد يجب الالتزام بها

- **لا تحرّك views على الـ main thread أثناء الـ splash.** التطبيق يبني أول شاشة في هذا الوقت، وأي `ObjectAnimator` أو `View.animate()` سيتجمّد. استخدم animated vector (render thread) أو window animation (النظام).
- **الشرط في `KeepOnScreenCondition` يجب أن يكون خفيفاً.** يُستدعى مع كل frame على الـ main thread، فاقرأ منه قيمة جاهزة فقط.
- **لا تعدّل `PlatformHandoff31` دون سبب.** فيه معالجات لمشاكل الـ insets وشريط الحالة على Android 12 و 12L.
- **الأنيميشن لا تُعاد عند `recreate`.** الدوران وتغيير اللغة أو الـ theme لا يعرض الـ splash مرة أخرى، وهذا مقصود.

---

## 6. مشاكل شائعة

| المشكلة | السبب والحل |
|---|---|
| خلفية فارغة لفترة قبل دخول الأيقونة | هذا زمن إقلاع التطبيق نفسه (حوالي 1.6 ثانية في قياس واحد على debug build). جرّب release، أو قلّل العمل في `Application.onCreate` و `Activity.onCreate`. |
| الأيقونة تظهر فجأة بحجمها ثم تبدأ الحركة | قيم `logo_group` لا تطابق `valueFrom` في الـ animator. |
| خطأ lint يطلب `values-v31` | attribute مكتوب بالبادئة `android:` في theme التطبيق. احذف البادئة. |
| الـ badge لا يظهر | السطران فارغان، أو لم يُستدعَ `setKeepOnScreenCondition`. |
| الـ badge يتأخر قليلاً عن الأيقونة | الـ main thread كان مشغولاً لحظة إظهاره. حركته نفسها لا تتأثر. |
| الشاشة لا تختفي | listener مخصص لم يستدعِ `provider.remove()`، أو الشرط يرجع `true` دائماً. |

---

## 7. خريطة الملفات

```
core-splashscreen/src/main/
├── java/androidx/core/splashscreen/
│   ├── SplashScreen.kt              المنطق كله: التثبيت، الاستلام من النظام، الأنيميشن، الخروج
│   ├── SplashScreenViewProvider.kt  الـ view الخاص بالموديول ونافذة الـ branding
│   ├── MaskedDrawable.kt            قص الأيقونة الثابتة على شكل دائرة
│   └── ThemeUtils.kt                ضبط ألوان أيقونات شريط الحالة (API 31-32)
└── res/
    ├── anim/                        دخول وخروج الـ branding
    ├── interpolator/                الـ overshoot الخاص بالـ branding
    ├── layout/
    │   ├── splash_screen_view.xml       الأيقونة
    │   └── splash_screen_branding.xml   الـ badge
    ├── drawable/                    خلفية النافذة قبل API 31، وخلفية الـ badge
    ├── values/                      attrs و styles و dimens
    └── values-v31/styles.xml        تحويل الـ attributes لنظيرتها في النظام
```
