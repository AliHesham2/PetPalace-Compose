# كيف يعمل موديول الـ Splash Screen من الداخل (`:core-splashscreen`)

> هذا الملف يشرح طريقة العمل الداخلية وأسباب القرارات. لطريقة الاستخدام والتعديل راجع [README.md](./README.md).

## فهرس

1. [المشكلة التي يحلها الموديول](#1-المشكلة-التي-يحلها-الموديول)
2. [الحل: الاستلام من النظام](#2-الحل-الاستلام-من-النظام)
3. [التسلسل الزمني](#3-التسلسل-الزمني)
4. [أين تعمل كل حركة ولماذا](#4-أين-تعمل-كل-حركة-ولماذا)
5. [ملفات الكود](#5-ملفات-الكود)
6. [ملفات الموارد](#6-ملفات-الموارد)
7. [الثوابت](#7-الثوابت)
8. [حالات خاصة](#8-حالات-خاصة)
9. [الفرق عن المكتبة الأصلية](#9-الفرق-عن-المكتبة-الأصلية)

---

## 1. المشكلة التي يحلها الموديول

عند الضغط على أيقونة التطبيق، النظام يعرض شاشة بداية **قبل** أن يعمل أي كود من التطبيق:

- **قبل API 31:** نافذة مؤقتة ترسم `android:windowBackground` الخاص بالـ theme. صورة ثابتة فقط.
- **API 31 فأعلى:** `SplashScreenView` يرسمه النظام. يدعم animated vector للأيقونة وصورة branding، لا أكثر.

في الحالتين لا يمكن تشغيل أنيميشن مخصصة أو عرض نصوص. والمكتبة الأصلية توقف رسم التطبيق طوال فترة الانتظار (`OnPreDrawListener` يرجع `false`)، فأي أنيميشن تُشغَّل على views التطبيق في هذه الفترة لا تُرسم أصلاً.

---

## 2. الحل: الاستلام من النظام

الموديول يضيف view خاصاً به فوق محتوى الـ Activity، مطابقاً لما يرسمه النظام (نفس الخلفية، ونفس الأيقونة في نفس المكان). بعدها:

1. التطبيق يرسم أول frame دون أي تأخير، والـ view الخاص بالموديول يغطي المحتوى.
2. شاشة النظام تختفي، فيظهر تحتها view مطابق لها. المستخدم لا يلاحظ الانتقال.
3. الـ view أصبح ملكاً للتطبيق، فتُشغَّل عليه الأنيميشن.

في الإعداد الحالي الأيقونة التي يرسمها النظام شفافة، فالمطابقة تكون على لون الخلفية فقط، والشعار يدخل بـ entry animation.

---

## 3. التسلسل الزمني

```
ضغط على أيقونة التطبيق
│
├─ النظام يعرض الخلفية (النافذة المؤقتة أو SplashScreenView)
│
├─ Activity.onCreate
│   ├─ installSplashScreen()
│   │    ├─ قراءة الـ attributes من الـ theme
│   │    └─ setTheme(postSplashScreenTheme)
│   └─ setKeepOnScreenCondition { ... }
│        └─ setupSplashScreenView(): إضافة الـ view للـ decor
│
├─ أول frame من التطبيق
│   ├─ قبل API 31: أول preDraw يبدأ الأنيميشن مباشرة
│   └─ API 31+:    النظام يستدعي exit listener، فتُزال SplashScreenView
│                  دون أنيميشن وتبدأ أنيميشن الموديول
│
├─ startIntroAnimation()
│   ├─ showBranding()        نافذة الـ badge بحركة الدخول
│   └─ animatedIcon.start()  الـ animated vector
│
├─ نهاية حركة الأيقونة
│   └─ dispatchOnExitAnimationWhenReady(): فحص مع كل frame حتى
│        ينتهي الشرط ويمر الحد الأدنى للمدة
│
└─ الخروج
    ├─ listener مخصص، أو
    └─ defaultSplashExitAnimation(): fade في 220ms ثم remove()
```

---

## 4. أين تعمل كل حركة ولماذا

أثناء الـ splash يكون الـ main thread مشغولاً ببناء أول شاشة (Hilt و Compose و navigation). أي حركة تعتمد عليه تتجمّد. لذلك كل حركة تعمل في مكان مختلف:

| الحركة | التقنية | من ينفّذها |
|---|---|---|
| دخول الأيقونة | `AnimatedVectorDrawable` | الـ render thread |
| دخول وخروج الـ badge | window animation على `PopupWindow` | النظام (system_server) |
| الخروج (fade) | `View.animate()` | الـ main thread |

الخروج يعمل على الـ main thread لأن التطبيق يكون قد انتهى من التحميل في تلك اللحظة.

**لماذا الـ badge في نافذة مستقلة؟** لا توجد طريقة عامة لتحريك view عادي بعيداً عن الـ main thread. أما حركات دخول وخروج النوافذ فينفّذها النظام على سطح النافذة نفسه، فلا تتأثر بانشغال التطبيق. الثمن الوحيد: أول رسمة للنافذة تحتاج الـ main thread، فقد يتأخر ظهور الـ badge قليلاً، لكن حركته لا تتقطع.

**المسار الاحتياطي:** لو لم يُحدَّد `splashScreenIntroAnimatedIcon` أو لم يكن animated vector، تُحرَّك الأيقونة الثابتة بـ `ObjectAnimator` (لفة كاملة مع تكبير ثم رجوع). هذا المسار على الـ main thread، فينتظر الموديول قبل تشغيله حتى ينتهي الشرط وتمر 5 frames متتالية في وقتها (`startIntroAnimationWhenSettled`)، بحد أقصى 1500ms.

---

## 5. ملفات الكود

### 5.1 `SplashScreen.kt`

الكلاس الوحيد الذي يتعامل معه التطبيق. تنفيذ واحد لكل الإصدارات.

| الدالة | الوظيفة |
|---|---|
| `Activity.installSplashScreen()` | تنشئ الكائن وتستدعي `install()`. |
| `install()` | تقرأ الخلفية والأيقونتين وسطري الـ branding من الـ theme، ثم تطبّق `postSplashScreenTheme`. |
| `setKeepOnScreenCondition()` | تخزّن الشرط والحد الأدنى للمدة، وتستدعي `setupSplashScreenView()`. |
| `setOnExitAnimationListener()` | تخزّن الـ listener، وتستدعي `setupSplashScreenView()`. |
| `setupSplashScreenView()` | تضيف الـ view للـ decor وتجهّزه وتسجّل محفّزات بدء الأنيميشن. تعمل مرة واحدة، ولا تعمل عند `recreate`. |
| `displaySplashScreenIcon()` | تضع الـ animated vector إن وُجد، وإلا الأيقونة الثابتة داخل `MaskedDrawable`. |
| `displayBranding()` | تملأ السطرين وتخفي الفارغ منهما. |
| `startIntroAnimationWhenSettled()` | تبدأ فوراً مع animated vector، أو تنتظر هدوء الـ main thread في المسار الاحتياطي. |
| `startIntroAnimation()` | تُظهر الـ badge وتبدأ حركة الأيقونة. محمية من الاستدعاء المتكرر. |
| `dispatchOnExitAnimationWhenReady()` | تنتظر الشرط والمدة ثم تبدأ الخروج. |

**`PlatformHandoff31`** (API 31+ فقط): يسجّل `OnExitAnimationListener` عند النظام. عند استدعائه يزيل `SplashScreenView` فوراً ويبدأ أنيميشن الموديول. يحتوي على معالجتين منقولتين من المكتبة الأصلية لـ API 31 و 32:

- `hierarchyListener` و `computeDecorFitsWindow`: تسجيل قيمة `decorFitsSystemWindows` قبل أن يغيّرها النظام، ثم إعادتها.
- `applyAppSystemUiTheme`: تطبيق ألوان شريط الحالة والتنقل من الـ theme مبكراً لتجنب الوميض.

### 5.2 `SplashScreenViewProvider.kt`

يملك الـ view والنافذة:

- `view`: الـ `FrameLayout` الذي يغطي التطبيق (من `splash_screen_view.xml`).
- `iconView`: الـ `ImageView` الخاص بالأيقونة.
- `brandingView`: الـ badge (من `splash_screen_branding.xml`).
- `brandingWindow`: `PopupWindow` يحمل الـ badge، غير قابل للمس، وحركاته من `Animation.SplashScreen.Branding`.
- `showBranding()` و `hideBranding()`: داخليتان، للإظهار والإخفاء بالحركة.
- `remove()`: تخفي الـ badge وتزيل الـ view. إلزامية مع أي listener مخصص.

النافذة تُغلق تلقائياً لو فُصل الـ view عن النافذة الأم (مثلاً عند إغلاق الـ Activity أثناء الـ splash) لتجنب تسريب النافذة.

### 5.3 `MaskedDrawable.kt`

يقص الأيقونة الثابتة داخل دائرة قطرها ثلثا حجم الأيقونة، مثل قص النظام على Android 12. يُستخدم فقط عندما لا يوجد animated vector. الـ animated vector **لا يُقص**، ولذلك يجب أن يبقى رسمه داخل حدوده.

### 5.4 `ThemeUtils.kt`

دالة واحدة تضبط لون أيقونات شريط الحالة والتنقل (فاتحة أو داكنة) من الـ theme. تُستخدم على API 31 و 32 فقط.

---

## 6. ملفات الموارد

### 6.1 `values/attrs.xml`

يعرّف الـ attributes المذكورة في README، ومعها `splashScreenIconSize` (داخلي، يحدده الـ theme الأب) و `isLightTheme`.

### 6.2 `values/styles.xml`

- `Theme.SplashScreen`: الـ theme الأب. يضبط `android:windowBackground` على `compat_splash_screen_no_icon_background`، وحجم الأيقونة 288dp.
- `Theme.SplashScreen.IconBackground`: نسخة بدائرة خلف الأيقونة، وحجم 240dp.
- `Animation.SplashScreen.Branding`: حركتا دخول وخروج نافذة الـ badge.

### 6.3 `values-v31/styles.xml`

يعيد تعريف `Theme.SplashScreen` ليحوّل attributes الموديول إلى نظيرتها في النظام:

```xml
<item name="android:windowSplashScreenAnimatedIcon">?windowSplashScreenAnimatedIcon</item>
<item name="android:windowSplashScreenBackground">?windowSplashScreenBackground</item>
```

بفضل هذا الملف لا يحتاج التطبيق إلى كتابة أي attribute بالبادئة `android:` ولا إلى `values-v31` خاص به.

### 6.4 `drawable/`

- `compat_splash_screen_no_icon_background.xml` و `compat_splash_screen.xml`: الـ `layer-list` الذي يرسمه النظام كخلفية نافذة قبل API 31 (الخلفية ثم الأيقونة ثم حلقة بلون الخلفية تقص أطراف الأيقونة).
- `icon_background.xml`: دائرة خلفية الأيقونة.
- `bg_splash_branding_badge.xml`: خلفية الـ badge، ولها نسخة في `drawable-night`.

### 6.5 `layout/`

- `splash_screen_view.xml`: `FrameLayout` بحجم الشاشة فيه `ImageView` الأيقونة في المنتصف.
- `splash_screen_branding.xml`: `LinearLayout` رأسي فيه سطران نصيان.

### 6.6 `anim/` و `interpolator/`

- `splash_branding_enter.xml`: scale من 0.3 إلى 1 في 700ms بـ overshoot، مع alpha من 0 إلى 1 في 200ms.
- `splash_branding_exit.xml`: alpha من 1 إلى 0 في 220ms.
- `splash_branding_overshoot.xml`: `overshootInterpolator` بـ tension يساوي 2.5.

---

## 7. الثوابت

كلها في `SplashScreen.kt`:

| الثابت | القيمة | المعنى |
|---|---|---|
| `DEFAULT_MIN_DURATION_MS` | 1500 | الحد الأدنى الافتراضي لمدة العرض، من بدء الأنيميشن. |
| `PLATFORM_HANDOFF_TIMEOUT_MS` | 500 | على API 31+: مهلة انتظار تسليم النظام قبل البدء على أي حال. |
| `INTRO_SETTLE_FRAMES` | 5 | المسار الاحتياطي: عدد الـ frames المنتظمة المطلوبة قبل البدء. |
| `INTRO_SETTLE_TIMEOUT_MS` | 1500 | المسار الاحتياطي: أقصى انتظار لهدوء الـ main thread. |
| `MASK_FACTOR` | 2/3 | نسبة قطر دائرة القص إلى حجم الأيقونة. |
| `ICON_INTRO_DURATION_MS` | 700 | المسار الاحتياطي: مدة حركة الأيقونة. |
| `ICON_INTRO_PEAK_SCALE` | 1.25 | المسار الاحتياطي: أقصى تكبير. |
| `ICON_INTRO_ROTATION_DEGREES` | 360 | المسار الاحتياطي: زاوية اللفة. |

---

## 8. حالات خاصة

- **إعادة إنشاء الـ Activity** (دوران، تغيير لغة أو theme): `isRelaunch` يكون `true` لأن `lastNonConfigurationInstance` ليس `null`، فلا يُضاف الـ view ولا تُعرض الـ splash.
- **تشغيل بدون شاشة نظام على API 31+** (مثلاً من إشعار في بعض الحالات): الـ exit listener لا يُستدعى، فتبدأ الأنيميشن بعد `PLATFORM_HANDOFF_TIMEOUT_MS` من أول frame.
- **اللمس أثناء الـ splash:** الـ view قابل للنقر (`isClickable = true`) فيبتلع اللمسات ولا تصل للمحتوى تحته.
- **الأنيميشن معطّلة في إعدادات الجهاز:** الحركات تنتهي فوراً عند قيمها النهائية، والحد الأدنى للمدة يبقى سارياً.
- **الوضع الداكن:** الـ theme الخاص بالـ splash يتبع وضع النظام (`values-night`)، لا إعداد الـ theme داخل التطبيق، لأن النظام يرسمه قبل أن يقرأ التطبيق إعداداته.

---

## 9. الفرق عن المكتبة الأصلية

| | المكتبة الأصلية | هذا الموديول |
|---|---|---|
| التنفيذ | `Impl` و `Impl31` منفصلان | تنفيذ واحد، مع `PlatformHandoff31` للاستلام |
| فترة الانتظار | إيقاف رسم التطبيق | التطبيق يرسم، والـ view يغطيه |
| أنيميشن الأيقونة | من النظام على API 31+ فقط | animated vector من الموديول على كل الإصدارات |
| الـ branding | صورة، على API 31+ فقط | سطران نصيان في نافذة مستقلة |
| الحد الأدنى للمدة | غير موجود | 1500ms افتراضياً |
| الخروج الافتراضي | إزالة فورية | fade في 220ms |
| عند `recreate` | تُعاد الـ splash قبل API 31 | لا تُعاد |
| `SplashScreenViewProvider.view` | `SplashScreenView` الخاص بالنظام على API 31+ | view الموديول دائماً |

`iconAnimationStartMillis` و `iconAnimationDurationMillis` حُذفتا من `SplashScreenViewProvider` لأنهما كانتا تخصّان أنيميشن النظام.

**لم يُختبر على جهاز:** مسار API 31+ (`PlatformHandoff31`) تم بناؤه دون أخطاء، لكن التجربة الفعلية كانت على جهاز API 29 فقط.
