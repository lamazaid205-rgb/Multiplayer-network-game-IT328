package phase1;

import java.util.*;

public class GameManager {

    // ==============================
    // 1. Board Letters
    // ==============================
    private static final String[] LETTERS = {
            "ن", "م", "ك", "ي", "ت",
            "ط", "ز", "ق", "ث", "ض",
            "خ", "م", "ج", "ر", "أ",
            "ح", "س", "غ", "ذ", "ه",
            "ف", "ل", "ب", "ش", "ع"
    };

    private static final int SIZE = 5;

    // ==============================
    // 2. Game Data
    // ==============================
    private static String[] cellOwners = new String[25];

    private static Map<String, Integer> scores = new HashMap<>();

    private static Map<String, List<Question>> questions = new HashMap<>();

    private static Map<Integer, Question> activeQuestions = new HashMap<>();

    private static Set<Integer> answeredQuestions = new HashSet<>();

    private static Random random = new Random();

    static {
        loadQuestions();
    }

    // ==============================
    // 3. Load Questions
    // ==============================
    private static void loadQuestions() {

        // ===== حرف أ =====
        addQuestion("أ", new Question(
                "من هو العالم اليوناني القديم الذي وضع قانون الطفو؟",
                new String[]{"أرسطو", "أرخميدس", "أفلاطون", "أبقراط"},
                "أرخميدس"
        ));

        addQuestion("أ", new Question(
                "اسم علم مؤنث معناه فوحان العطر الطيب أو رائحة الأزهار؟",
                new String[]{"أريج", "أزهار", "أماني", "أفنان"},
                "أريج"
        ));

        addQuestion("أ", new Question(
                "ماذا يطلق على الكذب البيّن والافتراء والقلب الشديد للحقائق؟",
                new String[]{"إفك", "إثم", "إجحاف", "إهمال"},
                "إفك"
        ));

        addQuestion("أ", new Question(
                "من هي الصحابية الملقبة بذات النطاقين؟",
                new String[]{"أسماء بنت أبي بكر", "أم سلمة", "أروى بنت عبدالمطلب", "أميمة بنت الحارث"},
                "أسماء بنت أبي بكر"
        ));

        addQuestion("أ", new Question(
                "ماذا يسمى مَن لا يُبصر ليلاً ويُبصر نهاراً؟",
                new String[]{"أعشى", "أطرش", "أعرج", "أبكم"},
                "أعشى"
        ));

        addQuestion("أ", new Question(
                "ما اسم أول سورة في القرآن الكريم؟",
                new String[]{"أحزاب", "آل عمران", "الفاتحة", "الأعلى"},
                "الفاتحة"
        ));

        addQuestion("أ", new Question(
                "ما اسم الجهاز الذي يقيس شدة الزلازل؟",
                new String[]{"أمبيرمتر", "أوميتر", "أكسجين", "إكتاف"},
                "أمبيرمتر"
        ));

        // ===== حرف ب =====
        addQuestion("ب", new Question(
                "ما الاسم المعروف للطائر مالك الحزين، وهو طائر ذو أرجل طويلة؟",
                new String[]{"بلشون", "ببغاء", "باز", "بطريق"},
                "بلشون"
        ));

        addQuestion("ب", new Question(
                "كلمة تطلق على الجهاز الذي يستخدم لقياس الضغط الجوي؟",
                new String[]{"بارومتر", "بوصلة", "بندول", "بلف"},
                "بارومتر"
        ));

        addQuestion("ب", new Question(
                "ماذا تسمى الجسيمات ذات الشحنة الموجبة في الذرة؟",
                new String[]{"بروتونات", "بكتيريا", "بلازما", "بلورات"},
                "بروتونات"
        ));

        addQuestion("ب", new Question(
                "ما اسم الغدة التي تقع خلف المعدة وتفرز الأنسولين؟",
                new String[]{"بنكرياس", "بلعوم", "بروستاتا", "بطين"},
                "بنكرياس"
        ));

        addQuestion("ب", new Question(
                "وسيلة نقل بحرية كبيرة تُستخدم لنقل الركاب أو البضائع؟",
                new String[]{"باخرة", "بلم", "بارجة", "باص"},
                "باخرة"
        ));

        addQuestion("ب", new Question(
                "ما العلم الذي يدرس الكائنات الحية وتركيبها ووظائفها؟",
                new String[]{"بلاغة", "بيولوجيا", "بيئة", "بيانات"},
                "بيولوجيا"
        ));

        addQuestion("ب", new Question(
                "ما الطاقة المخزونة في الجسم الساكن بسبب موضعه؟",
                new String[]{"بثور", "بروق", "بيزونية", "بوتنشيال"},
                "بوتنشيال"
        ));

        // ===== حرف ت =====
        addQuestion("ت", new Question(
                "ما المصطلح الاقتصادي الذي يدل على ارتفاع مستمر في الأسعار وانخفاض قيمة العملة؟",
                new String[]{"تضخم", "تباطؤ", "تراكم", "تقشف"},
                "تضخم"
        ));

        addQuestion("ت", new Question(
                "ماذا يسمى صوت البلبل؟",
                new String[]{"تغريد", "تأوه", "ترجيع", "تكسير"},
                "تغريد"
        ));

        addQuestion("ت", new Question(
                "ما هي آخر غزوات النبي صلى الله عليه وسلم؟",
                new String[]{"تبوك", "تيماء", "تستر", "تبالة"},
                "تبوك"
        ));

        addQuestion("ت", new Question(
                "ما الحيوان الذي لا يستطيع إخراج لسانه من فمه؟",
                new String[]{"تمساح", "تنين", "تيس", "تابير"},
                "تمساح"
        ));

        addQuestion("ت", new Question(
                "ما العملية التي تُستخدم لتحويل المعلومات إلى صيغة سرية؟",
                new String[]{"تشفير", "تجميع", "ترميز", "تخزين"},
                "تشفير"
        ));

        addQuestion("ت", new Question(
                "ما اسم العملية التي تحول الضوء إلى طاقة كيميائية في النباتات؟",
                new String[]{"تخمير", "تبخر", "تمثيل ضوئي", "تحلل"},
                "تمثيل ضوئي"
        ));

        addQuestion("ت", new Question(
                "ما الكلمة التي تعني الاعتراف بالذنب والتوجه إلى الله؟",
                new String[]{"تكبر", "تهاون", "توبة", "تعالٍ"},
                "توبة"
        ));

        // ===== حرف ث =====
        addQuestion("ث", new Question(
                "ما الاسم الذي يُطلق على الانتقام ردًا على اعتداء سابق؟",
                new String[]{"ثواب", "ثأر", "ثناء", "ثقل"},
                "ثأر"
        ));

        addQuestion("ث", new Question(
                "ما اسم الكوكبة الشهيرة التي تُعرف بمجموعة نجوم متقاربة في السماء؟",
                new String[]{"ثريا", "ثمار", "ثبات", "ثقب"},
                "ثريا"
        ));

        addQuestion("ث", new Question(
                "ما الكلمة التي تعني المدح الجميل وإظهار المحاسن؟",
                new String[]{"ثراء", "ثناء", "ثقل", "ثورة"},
                "ثناء"
        ));

        addQuestion("ث", new Question(
                "ما الكلمة التي تعني الغنى والكثرة في المال؟",
                new String[]{"ثقل", "ثراء", "ثبات", "ثأر"},
                "ثراء"
        ));

        addQuestion("ث", new Question(
                "ما الكلمة التي تعني الحبوط والسكون وعدم الحركة؟",
                new String[]{"ثناء", "ثريا", "ثبات", "ثقب"},
                "ثبات"
        ));

        // ===== حرف ج =====
        addQuestion("ج", new Question(
                "ما المصطلح الذي يُطلق على المادة الوراثية المسؤولة عن تحديد الصفات؟",
                new String[]{"جينات", "جداول", "جلطات", "جزيئات"},
                "جينات"
        ));

        addQuestion("ج", new Question(
                "ما العلم الذي يختص بدراسة تكوين الأرض وطبقاتها؟",
                new String[]{"جراحة", "جغرافيا", "جيولوجيا", "جبر"},
                "جيولوجيا"
        ));

        addQuestion("ج", new Question(
                "ما الاسم الذي يُطلق على البئر العميق وقد ورد في سورة يوسف؟",
                new String[]{"جب", "جرف", "جدار", "جوف"},
                "جب"
        ));

        addQuestion("ج", new Question(
                "ما الاسم الذي يُطلق على النقاش السلبي بين طرفين يختلفان في الرأي؟",
                new String[]{"جموح", "جدال", "جفاف", "جحود"},
                "جدال"
        ));

        addQuestion("ج", new Question(
                "ما الكلمة التي تعني الشجاعة والإقدام وعدم الخوف؟",
                new String[]{"جشع", "جدل", "جرأة", "جحود"},
                "جرأة"
        ));

        addQuestion("ج", new Question(
                "ما اسم المضيق المائي الفاصل بين المغرب وإسبانيا؟",
                new String[]{"جاوا", "جبل طارق", "جيبوتي", "جيلان"},
                "جبل طارق"
        ));

        // ===== حرف ح =====
        addQuestion("ح", new Question(
                "ما اسم المادة الوراثية التي تحمل الشيفرة الجينية للكائنات الحية؟",
                new String[]{"الحويصلات", "الحجاب الحاجز", "الحمض النووي", "الحبل الشوكي"},
                "الحمض النووي"
        ));

        addQuestion("ح", new Question(
                "ماذا تسمى النباتات التي تنمو تلقائيًا في غير موضعها المرغوب؟",
                new String[]{"حراشف", "حواجز", "حشائش", "حبوب"},
                "حشائش"
        ));

        addQuestion("ح", new Question(
                "اسم يطلق على قطعة من الأرض تُستخدم للزراعة أو الرعي؟",
                new String[]{"حقل", "حوض", "حزام", "حصن"},
                "حقل"
        ));

        addQuestion("ح", new Question(
                "ما يطلق على الشوق العاطفي إلى ماضٍ جميل أو مكان محبوب؟",
                new String[]{"حنين", "حسد", "حزم", "حذر"},
                "حنين"
        ));

        

        addQuestion("ح", new Question(
                "ما العلم الذي يدرس حركة الأجرام السماوية ومواقعها؟",
                new String[]{"حساب", "حفريات", "حيوانات", "حركيات فلكية"},
                "حركيات فلكية"
        ));

        addQuestion("ح", new Question(
                "ما الكلمة التي تعني الشعور بالفرح والسرور الشديد؟",
                new String[]{"حزن", "حقد", "حبور", "حسد"},
                "حبور"
        ));

        // ===== حرف خ =====
        addQuestion("خ", new Question(
                "ما الطعام الذي يشار إليه بغذاء الغني والفقير؟",
                new String[]{"الخبز", "الخشاف", "الخروب", "الخس"},
                "الخبز"
        ));

        addQuestion("خ", new Question(
                "ما المصطلح الذي يُطلق في علوم الحاسوب على مجموعة خطوات لحل مشكلة؟",
                new String[]{"ختم", "خلايا", "خريطة", "خوارزمية"},
                "خوارزمية"
        ));

        addQuestion("خ", new Question(
                "ما الأداة التي تُستخدم لوضع علامة رسمية على المستندات؟",
                new String[]{"خيط", "ختم", "خزان", "خوذة"},
                "ختم"
        ));

        addQuestion("خ", new Question(
                "ما الكلمة التي تصف الشخص الكريم الذي يعطي بسخاء؟",
                new String[]{"خبيث", "خسيس", "خجول", "خيّر"},
                "خيّر"
        ));

        addQuestion("خ", new Question(
                "ما الخلية التي تنتج الأجسام المضادة في الجهاز المناعي؟",
                new String[]{"خلايا حمراء", "خلايا عصبية", "خلايا بائية", "خلايا جذعية"},
                "خلايا بائية"
        ));

        // ===== حرف ذ =====
        addQuestion("ذ", new Question(
                "ما الكلمة التي تعني حفظ الشيء وصيانته من الضياع؟",
                new String[]{"ذوبان", "ذخيرة", "ذخر", "ذعر"},
                "ذخر"
        ));

        addQuestion("ذ", new Question(
                "شخصية ذكرت في القرآن الكريم ضمن الأنبياء والصابرين؟",
                new String[]{"ذو النورين", "ذو الكفل", "ذو القرنين", "ذو الفقار"},
                "ذو الكفل"
        ));

        addQuestion("ذ", new Question(
                "ما الكلمة التي تصف سيلان دموع العين؟",
                new String[]{"ذبول", "ذرف", "ذكر", "ذعر"},
                "ذرف"
        ));

        addQuestion("ذ", new Question(
                "ما الكلمة التي تعني الاهتزاز والخوف الشديد؟",
                new String[]{"ذرف", "ذبول", "ذعر", "ذكاء"},
                "ذعر"
        ));

        addQuestion("ذ", new Question(
                "ما الكلمة التي تعني سرعة الفهم وحدة الذهن؟",
                new String[]{"ذل", "ذكاء", "ذيل", "ذوق"},
                "ذكاء"
        ));

        // ===== حرف ز =====
        addQuestion("ز", new Question(
                "كلمة تعني المنع أو النهي بشدة؟",
                new String[]{"زوال", "زجر", "زحف", "زينة"},
                "زجر"
        ));

        addQuestion("ز", new Question(
                "ما المادة الشفافة التي تُصنع عادة من الرمل؟",
                new String[]{"زجاج", "زنك", "زرنيخ", "زئبق"},
                "زجاج"
        ));

        addQuestion("ز", new Question(
                "ما العنصر الكيميائي المستخدم في البطاريات وطلاء المعادن؟",
                new String[]{"زجاج", "زنك", "زمرد", "زئبق"},
                "زنك"
        ));

        addQuestion("ز", new Question(
                "ما الكلمة التي تعني التطور والارتقاء من حال إلى حال أفضل؟",
                new String[]{"زلزال", "زهد", "زيادة", "زهو"},
                "زيادة"
        ));

        addQuestion("ز", new Question(
                "ما اسم الحجر الكريم الأخضر اللون؟",
                new String[]{"زبرجد", "زنجبيل", "زعفران", "زمرد"},
                "زمرد"
        ));

        // ===== حرف س =====
        addQuestion("س", new Question(
                "ما المنصب الدبلوماسي الأعلى الذي يمثل دولته؟",
                new String[]{"سائق", "سفير", "سمسار", "سكرتير"},
                "سفير"
        ));

        addQuestion("س", new Question(
                "ما اسم البيئة الطبيعية المنتشرة في أفريقيا؟",
                new String[]{"سهوب", "سهول", "سواحل", "سافانا"},
                "سافانا"
        ));

        addQuestion("س", new Question(
                "ما نوع التضاريس المنبسطة منخفضة الارتفاع؟",
                new String[]{"سراديب", "سدود", "سلاسل", "سهول"},
                "سهول"
        ));

        addQuestion("س", new Question(
                "ما الشيء الذي نكتب به على الورق؟",
                new String[]{"سير القلم", "سبأ", "سيالة", "سن"},
                "سيالة"
        ));

        addQuestion("س", new Question(
                "ماهو الجهاز الذي يستعمل لسماع دقات القلب؟",
                new String[]{"سكالبل", "سيروم", "سماعة", "سونار"},
                "سماعة"
        ));

        // ===== حرف ش =====
        addQuestion("ش", new Question(
                "ما اسم الغلاف الواقي الذي تنسجه بعض اليرقات؟",
                new String[]{"شرنقة", "شراع", "شظية", "شبكة"},
                "شرنقة"
        ));

        addQuestion("ش", new Question(
                "ما الاسم الأصلي لعبدالمطلب؟",
                new String[]{"شيبة", "شريف", "شداد", "شهاب"},
                "شيبة"
        ));

        addQuestion("ش", new Question(
                "ماذا يسمى التقلص اللا إرادي والمفاجئ في العضلة؟",
                new String[]{"شلل", "شحوب", "شد عضلي", "شخير"},
                "شد عضلي"
        ));

        addQuestion("ش", new Question(
                "التوسط للغير لجلب منفعة أو دفع مضرة يسمى؟",
                new String[]{"الشعيرة", "الشهامة", "الشجاعة", "الشفاعة"},
                "الشفاعة"
        ));

        addQuestion("ش", new Question(
                "ما الكلمة التي تعني الإحساس بالغيرة والأنفة على المحارم؟",
                new String[]{"شقاء", "شهوة", "شيمة", "شهامة"},
                "شهامة"
        ));

        addQuestion("ش", new Question(
                "ما اسم الشجرة التي ذكرها الله في القرآن وتخرج من طور سيناء؟",
                new String[]{"شوك", "شجرة الزيتون", "شجرة الموز", "شجرة النخيل"},
                "شجرة الزيتون"
        ));

        // ===== حرف ض =====
        addQuestion("ض", new Question(
                "ما المصطلح الذي يشير إلى عدم الوضوح والغموض؟",
                new String[]{"ضبابية", "ضجيج", "ضمان", "ضخامة"},
                "ضبابية"
        ));

        addQuestion("ض", new Question(
                "ما الوصف الذي يطلق على الشيء القليل جدًا أو الصغير الحجم؟",
                new String[]{"ضخم", "ضئيل", "ضبابي", "ضاحل"},
                "ضئيل"
        ));

        addQuestion("ض", new Question(
                "عظم منحنٍ يشكل جزءًا من القفص الصدري؟",
                new String[]{"ضرس", "ضفيرة", "ضريح", "ضلع"},
                "ضلع"
        ));

        addQuestion("ض", new Question(
                "ما الكلمة التي تعني الحرص الشديد وعدم الإسراف؟",
                new String[]{"ضعف", "ضبط", "ضنك", "ضجر"},
                "ضبط"
        ));

        addQuestion("ض", new Question(
                "ما الكلمة التي تعني المقابر والأضرحة الخاصة بالكبار؟",
                new String[]{"ضلع", "ضئيل", "ضريح", "ضجيج"},
                "ضريح"
        ));

        // ===== حرف ي =====
        addQuestion("ي", new Question(
                "ماذا تسمى الحالة التي تعني الانتباه والوعي الكامل؟",
                new String[]{"يسر", "يباس", "يقظة", "يقين"},
                "يقظة"
        ));

        addQuestion("ي", new Question(
                "ما العنصر المشع الذي يستخدم كوقود في المفاعلات النووية؟",
                new String[]{"يود", "يشب", "يورانيوم", "ياقوت"},
                "يورانيوم"
        ));

        addQuestion("ي", new Question(
                "قوم فاسدون حاصرهم ذو القرنين خلف سد منيع؟",
                new String[]{"يأجوج ومأجوج", "يزيد ويعرب", "يونس ويوشع", "يعقوب ويوسف"},
                "يأجوج ومأجوج"
        ));

        addQuestion("ي", new Question(
                "ما الكلمة التي تعني السهولة والانبساط وعدم المشقة؟",
                new String[]{"يقظة", "يباس", "يقين", "يسر"},
                "يسر"
        ));

        addQuestion("ي", new Question(
                "ما اسم الحجر الكريم الأحمر الشفاف الثمين؟",
                new String[]{"يشب", "يورانيوم", "ياقوت", "يود"},
                "ياقوت"
        ));

        // ===== حرف ك =====
        addQuestion("ك", new Question(
                "ما التركيب الخلوي الذي يحمل المعلومات الوراثية؟",
                new String[]{"كبريت", "كالسيوم", "كوكب", "كروموسوم"},
                "كروموسوم"
        ));

        addQuestion("ك", new Question(
                "ما المصطلح الذي يعني تحقيق الاستفادة من الموارد بأقل هدر؟",
                new String[]{"كرم", "كمال", "كثافة", "كفاءة"},
                "كفاءة"
        ));

        addQuestion("ك", new Question(
                "ما الظواهر غير المعتادة التي تظهر على يد الصالحين؟",
                new String[]{"كهوف", "كرامات", "كنوز", "كوارث"},
                "كرامات"
        ));

        addQuestion("ك", new Question(
                "ما العنصر المعدني الأساسي لبناء العظام والأسنان؟",
                new String[]{"كربون", "كلور", "كالسيوم", "كادميوم"},
                "كالسيوم"
        ));

        addQuestion("ك", new Question(
                "ما الكلمة التي تعني الاكتمال والتمام وبلوغ الغاية؟",
                new String[]{"كثافة", "كفاءة", "كمال", "كبرياء"},
                "كمال"
        ));

        // ===== حرف م =====
        addQuestion("م", new Question(
                "ما وصف الحديث النبوي الذي نقله عدد كبير من الرواة؟",
                new String[]{"مرسل", "معلق", "مقطوع", "متواتر"},
                "متواتر"
        ));

        addQuestion("م", new Question(
                "ما النظرية الفيزيائية التي تفسر الخصائص على مستوى الذرات؟",
                new String[]{"موجات", "ميكانيكا الكم", "مغناطيسية", "ميكانيكا نيوتن"},
                "ميكانيكا الكم"
        ));

        addQuestion("م", new Question(
                "ما الجزء من الدماغ المسؤول عن التوازن؟",
                new String[]{"المخ", "المخيخ", "المريء", "المعدة"},
                "المخيخ"
        ));

        addQuestion("م", new Question(
                "من هو القائد العثماني الذي فتح القسطنطينية؟",
                new String[]{"محمد الفاتح", "مروان", "مصعب بن عمير", "معاوية"},
                "محمد الفاتح"
        ));

        addQuestion("م", new Question(
                "ما الهرمون المسؤول عن تنظيم النوم؟",
                new String[]{"الماغنسيوم", "الميثان", "المورفين", "الميلاتونين"},
                "الميلاتونين"
        ));

        addQuestion("م", new Question(
                "ما الجزء من النهر الذي يلتقي فيه الماء العذب بالماء المالح؟",
                new String[]{"المحيط", "المجرى", "المصب", "المرفأ"},
                "المصب"
        ));

        addQuestion("م", new Question(
                "ماهو الاسم الذي يطلق على المضيق المائيالذي يربط بين البحر الاحمر و خليج عدن؟",
                new String[]{"مضيق ملقا", "مضيق جبل طارق", "مضيق هرمز", "مضيق باب المندب"},
                "مضيق باب المندب"
        ));

        addQuestion("م", new Question(
                "ما الكلمة التي تعني الرحمة الشديدة وعطف الوالدين؟",
                new String[]{"مكر", "مجد", "محبة", "مودة"},
                "مودة"
        ));

        // ===== حرف ن =====
        addQuestion("ن", new Question(
                "ماذا يسمى صوت الغراب؟",
                new String[]{"نباح", "نهيق", "نقيق", "نعيق"},
                "نعيق"
        ));

        addQuestion("ن", new Question(
                "حرفة يدوية تعتمد على تشكيل الخشب؟",
                new String[]{"نقش", "نسيج", "نحت", "نجارة"},
                "نجارة"
        ));

        addQuestion("ن", new Question(
                "ماذا يسمى الجسيم الذي لا شحنة له في نواة الذرة؟",
                new String[]{"النحاس", "النيتروجين", "النيوترون", "النواة"},
                "النيوترون"
        ));

        addQuestion("ن", new Question(
                "ما الغاز الأكثر وفرة في الغلاف الجوي للأرض؟",
                new String[]{"نيون", "نيتروجين", "نيكل", "نبتون"},
                "نيتروجين"
        ));

        addQuestion("ن", new Question(
                "ما نوع الطاقة المستخرجة من انشطار نواة الذرة؟",
                new String[]{"نفط", "نيل", "نووية", "ندى"},
                "نووية"
        ));

        // ===== حرف ق =====
        addQuestion("ق", new Question(
                "طريقة للمفاضلة والاختيار بين خيارات متساوية؟",
                new String[]{"القرعة", "القافلة", "القسمة", "القائمة"},
                "القرعة"
        ));

        addQuestion("ق", new Question(
                "ما المصدر الرابع من مصادر التشريع الإسلامي؟",
                new String[]{"قياس", "قضاء", "قرار", "قانون"},
                "قياس"
        ));

        addQuestion("ق", new Question(
                "ما الاسم القديم لمدينة إسطنبول؟",
                new String[]{"القسطنطينية", "القوقاز", "القاهرة", "القيروان"},
                "القسطنطينية"
        ));

        addQuestion("ق", new Question(
                "ما أطول نهر في العالم؟",
                new String[]{"قرطبة", "قناة السويس", "النيل", "قريش"},
                "النيل"
        ));

        addQuestion("ق", new Question(
                "ما الكلمة التي تعني المقياس الذي يُحكم به بين الناس؟",
                new String[]{"قصص", "قياس", "قضاء", "قيمة"},
                "قضاء"
        ));

        // ===== حرف ط =====
        addQuestion("ط", new Question(
                "من هو الصحابي الجليل أحد العشرة المبشرين بالجنة؟",
                new String[]{"طه حسين", "طارق بن زياد", "طلحة بن عبيد الله", "طلحة بن خويلد"},
                "طلحة بن عبيد الله"
        ));

        addQuestion("ط", new Question(
                "ماذا يسمى الماء الباقي على خلقته؟",
                new String[]{"طهور", "طرب", "طعام", "طين"},
                "طهور"
        ));

        

        addQuestion("ط", new Question(
                "ما الكلمة التي تعني الفرح والنشوة الشديدة؟",
                new String[]{"طيش", "طمع", "طرب", "طغيان"},
                "طرب"
        ));

        addQuestion("ط", new Question(
                "ما الشكل الهندسي المستطيل ذو الأضلاع المتوازية؟",
                new String[]{"طابق", "طيف", "طرفة", "طائرة"},
                "طائرة"
        ));

        // ===== حرف ر =====
        addQuestion("ر", new Question(
                "ماذا يسمى قطيع الخيول؟",
                new String[]{"رعيل", "رواق", "رفيق", "ركب"},
                "رعيل"
        ));

        addQuestion("ر", new Question(
                "ما أكبر صحراء رملية متصلة في العالم؟",
                new String[]{"الربع الخالي", "الرمال البيضاء", "الرستن", "الروكي"},
                "الربع الخالي"
        ));

        addQuestion("ر", new Question(
                "ما اسم الشخصية الأسطورية الإنجليزية التي سرقت الأغنياء لإعانة الفقراء؟",
                new String[]{"رستم", "رامبو", "روميو", "روبن هود"},
                "روبن هود"
        ));

        addQuestion("ر", new Question(
                "ما الكلمة التي تعني العودة إلى الله والتوقف عن الذنب؟",
                new String[]{"رغبة", "ركود", "رشوة", "رجوع"},
                "رجوع"
        ));

        addQuestion("ر", new Question(
                "ما الكلمة التي تعني سرعة الاستجابة وخفة الحركة؟",
                new String[]{"رشاقة", "ركود", "رتابة", "رهبة"},
                "رشاقة"
        ));

        // ===== حرف هـ =====
        addQuestion("ه", new Question(
                "كلمة تعني العزم والإرادة القوية لتحقيق هدف ما؟",
                new String[]{"هزيمة", "هواية", "هجرة", "همة"},
                "همة"
        ));

        addQuestion("ه", new Question(
                "ما المصطلح السياسي الذي يعني وقفًا مؤقتًا للعمليات العسكرية؟",
                new String[]{"هيمنة", "هجرة", "هروب", "هدنة"},
                "هدنة"
        ));

        addQuestion("ه", new Question(
                "ما المنطق الفارغ الذي لا معنى له؟",
                new String[]{"هراء", "هتاف", "هوس", "هشاشة"},
                "هراء"
        ));

        addQuestion("ه", new Question(
                "ما الكلمة التي تعني السكينة والطمأنينة في القلب؟",
                new String[]{"هلاك", "هجر", "هدوء", "هجوم"},
                "هدوء"
        ));

        addQuestion("ه", new Question(
                "ما الهرمون الذي يُفرز عند الإحساس بالسعادة في الجسم؟",
                new String[]{"هيموجلوبين", "هستامين", "هرمون النمو", "هرمون السعادة"},
                "هرمون السعادة"
        ));

        // ===== حرف غ =====
        addQuestion("غ", new Question(
                "الوصف الذي يطلق على أقصى درجات السواد؟",
                new String[]{"غربيب", "غائم", "غسق", "غامق"},
                "غربيب"
        ));

        addQuestion("غ", new Question(
                "ما الدولة الأفريقية التي عاصمتها أكرا؟",
                new String[]{"غامبيا", "غانا", "غرينلاند", "غينيا"},
                "غانا"
        ));

        addQuestion("غ", new Question(
                "ما أحد أسماء يوم القيامة الواردة في القرآن الكريم؟",
                new String[]{"الغيمة", "الغفلة", "الغروب", "الغاشية"},
                "الغاشية"
        ));

        addQuestion("غ", new Question(
                "ما الكلمة التي تعني الزهد في الدنيا والاكتفاء بالقليل؟",
                new String[]{"غضب", "غرور", "غنى", "غنية"},
                "غنى"
        ));

        addQuestion("غ", new Question(
                "ما الكلمة التي تعني الإعجاب بالنفس والتعالي على الآخرين؟",
                new String[]{"غفلة", "غياب", "غرور", "غموض"},
                "غرور"
        ));

        // ===== حرف ع =====
        addQuestion("ع", new Question(
                "من هو الصحابي الذي أعطى مفاتيح الكعبة للرسول ﷺ؟",
                new String[]{"عثمان بن طلحة", "عمر بن الخطاب", "علي بن أبي طالب", "عثمان بن عفان"},
                "عثمان بن طلحة"
        ));

        addQuestion("ع", new Question(
                "وظيفة إدارية وفن اجتماعي هدفها إقامة علاقات التفاهم؟",
                new String[]{"العلاقات العامة", "العناية الطبية", "العقود التجارية", "العمارة"},
                "العلاقات العامة"
        ));

        addQuestion("ع", new Question(
                "ما اسم الرقصة التقليدية التي تُؤدى في بعض دول الخليج؟",
                new String[]{"العزف", "العكاظ", "العرضة", "العمارية"},
                "العرضة"
        ));

        addQuestion("ع", new Question(
                "ماهي المدينة التي تعد اقدم مدينة ماهولة في التاريخ؟",
                new String[]{"عرعر", "عدن", "عمان", "عكا"},
                "عمان"
        ));

        addQuestion("ع", new Question(
                "ما الكلمة التي تعني الإنصاف والمساواة في الحكم؟",
                new String[]{"عقاب", "عجب", "عدل", "عتاب"},
                "عدل"
        ));

        // ===== حرف ل =====
        addQuestion("ل", new Question(
                "نظام من الرموز والأصوات يستخدمه البشر للتواصل؟",
                new String[]{"لهجة", "لوحة", "لحن", "لغة"},
                "لغة"
        ));

        addQuestion("ل", new Question(
                "من هو الحكيم الذي سميت باسمه سورة في القرآن الكريم؟",
                new String[]{"لبيب", "لوط", "ليمان", "لقمان"},
                "لقمان"
        ));

        addQuestion("ل", new Question(
                "ما اسم المستحضر الطبي الذي يحفز جهاز المناعة؟",
                new String[]{"لصقة", "لسعة", "لبوس", "لقاح"},
                "لقاح"
        ));

        addQuestion("ل", new Question(
                "ما الكلمة التي تعني الحنجرة والصوت الجميل في الغناء؟",
                new String[]{"لغط", "لدونة", "لحن", "لهجة"},
                "لحن"
        ));

        addQuestion("ل", new Question(
                "ما الكلمة التي تعني المرونة وسهولة التشكيل؟",
                new String[]{"لجاج", "لغط", "لدونة", "لمسة"},
                "لدونة"
        ));

        // ===== حرف ف =====
        addQuestion("ف", new Question(
                "مقياس قديم للمسافات يطلق على الثلاثة أميال؟",
                new String[]{"فدان", "فرسخ", "فلك", "فاصل"},
                "فرسخ"
        ));

        addQuestion("ف", new Question(
                "من هو العالم اللغوي الذي وضع أصول علم العروض؟",
                new String[]{"الفزاري", "الفارابي", "الفراهيدي", "الفيروزآبادي"},
                "الفراهيدي"
        ));

        addQuestion("ف", new Question(
                "ماذا يطلق على الذكاء وسرعة الفهم؟",
                new String[]{"فخامة", "فطنة", "فوضى", "فراسة"},
                "فطنة"
        ));

        addQuestion("ف", new Question(
                "ما العلم الذي يدرس الطبيعة والمادة والطاقة وقوانينهما؟",
                new String[]{"فلسفة", "فيزياء", "فلك", "فقه"},
                "فيزياء"
        ));

        addQuestion("ف", new Question(
                "ما الكلمة التي تعني الانتصار وتحقيق الهدف بعد صراع؟",
                new String[]{"فراق", "فضول", "فوز", "فتور"},
                "فوز"
        ));
    }

    // ==============================
    // 4. Add Question Helper
    // ==============================
    private static void addQuestion(String letter, Question q) {
        questions.computeIfAbsent(letter, k -> new ArrayList<>()).add(q);
    }

    // ==============================
    // 5. Player Setup
    // ==============================
    public static synchronized void initPlayer(String username) {
        scores.putIfAbsent(username, 0);
    }

    // ==============================
    // 6. Start Question
    // ==============================
    public static synchronized Question startQuestionForCell(int cellIndex) {
        if (cellIndex < 0 || cellIndex >= LETTERS.length) return null;

        if (!isCellAvailable(cellIndex)) return null;

        String letter = LETTERS[cellIndex];

        List<Question> list = questions.get(letter);

        if (list == null || list.isEmpty()) return null;

        Question q = list.get(random.nextInt(list.size()));

        activeQuestions.put(cellIndex, q);
        answeredQuestions.remove(cellIndex);

        return q;
    }

    // ==============================
    // 7. Submit First Answer
    // ==============================
    public static synchronized AnswerResult submitFirstAnswer(
            String username,
            int cellIndex,
            String answer
    ) {

        if (cellIndex < 0 || cellIndex >= cellOwners.length) {
            return new AnswerResult(false, false, "Invalid cell.");
        }

        if (!isCellAvailable(cellIndex)) {
            return new AnswerResult(false, false, "This cell is already owned.");
        }

        if (answeredQuestions.contains(cellIndex)) {
            return new AnswerResult(false, false, "This question was already answered.");
        }

        Question q = activeQuestions.get(cellIndex);

        if (q == null) {
            return new AnswerResult(false, false, "No active question for this cell.");
        }

        answeredQuestions.add(cellIndex);

        boolean correct = q.isCorrect(answer);

        if (correct) {
            cellOwners[cellIndex] = username;
            scores.put(username, scores.getOrDefault(username, 0) + 1);
        }

        activeQuestions.remove(cellIndex);

        return new AnswerResult(true, correct, correct ? "Correct answer." : "Wrong answer.");
    }

    // ==============================
    // 8. Score Message
    // ==============================
    public static synchronized String getScoreMessage() {
        StringBuilder sb = new StringBuilder("SCORE:");

        for (String player : MainServer.gamePlayers) {
            sb.append(player)
              .append("=")
              .append(scores.getOrDefault(player, 0))
              .append(",");
        }

        return sb.toString();
    }

    // ==============================
    // 9. Letter Helper
    // ==============================
    public static synchronized String getLetter(int cellIndex) {
        if (cellIndex < 0 || cellIndex >= LETTERS.length) return "";
        return LETTERS[cellIndex];
    }

    // ==============================
    // 10. Cell Availability
    // ==============================
    public static synchronized boolean isCellAvailable(int cellIndex) {
        return cellIndex >= 0 &&
               cellIndex < cellOwners.length &&
               cellOwners[cellIndex] == null;
    }

    // ==============================
    // 11. Winner Check
    // ==============================
    public static synchronized boolean hasWinner(String username) {

        boolean[] visited = new boolean[cellOwners.length];

        for (int i = 0; i < cellOwners.length; i++) {

            if (username.equals(cellOwners[i]) && !visited[i]) {

                int connectedCount = countConnectedCells(username, i, visited);

                if (connectedCount >= 3) {
                    return true;
                }
            }
        }

        return false;
    }

    private static int countConnectedCells(
            String username,
            int index,
            boolean[] visited
    ) {
        if (index < 0 || index >= cellOwners.length) {
            return 0;
        }

        if (visited[index]) {
            return 0;
        }

        if (!username.equals(cellOwners[index])) {
            return 0;
        }

        visited[index] = true;

        int count = 1;

        for (int neighbor : getNeighbors(index)) {
            count += countConnectedCells(username, neighbor, visited);
        }

        return count;
    }

    private static List<Integer> getNeighbors(int index) {

        List<Integer> neighbors = new ArrayList<>();

        int row = index / SIZE;
        int col = index % SIZE;

        int[][] directionsEven = {
                {0, -1}, {0, 1},
                {-1, -1}, {-1, 0},
                {1, -1}, {1, 0}
        };

        int[][] directionsOdd = {
                {0, -1}, {0, 1},
                {-1, 0}, {-1, 1},
                {1, 0}, {1, 1}
        };

        int[][] directions =
                (row % 2 == 0)
                        ? directionsEven
                        : directionsOdd;

        for (int[] d : directions) {

            int nr = row + d[0];
            int nc = col + d[1];

            if (nr >= 0 && nr < SIZE && nc >= 0 && nc < SIZE) {
                neighbors.add(nr * SIZE + nc);
            }
        }

        return neighbors;
    }

    // ==============================
    // 12. Board Full
    // ==============================
    public static synchronized boolean isBoardFull() {
        for (String owner : cellOwners) {
            if (owner == null) return false;
        }

        return true;
    }

    // ==============================
    // 13. Player Color Index
    // ==============================
    public static synchronized int getPlayerColorIndex(String username) {
        return MainServer.gamePlayers.indexOf(username);
    }

    // ==============================
    // 14. Reset Game
    // ==============================
    public static synchronized void resetGame() {
        Arrays.fill(cellOwners, null);
        scores.clear();
        activeQuestions.clear();
        answeredQuestions.clear();

        for (String player : MainServer.gamePlayers) {
            scores.put(player, 0);
        }
    }

    // ==============================
    // 15. Answer Result Class
    // ==============================
    public static class AnswerResult {
        public boolean accepted;
        public boolean correct;
        public String message;

        public AnswerResult(boolean accepted, boolean correct, String message) {
            this.accepted = accepted;
            this.correct = correct;
            this.message = message;
        }
    }
}