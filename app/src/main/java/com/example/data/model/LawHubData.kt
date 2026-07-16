package com.example.data.model

data class LawItem(
    val title: String,
    val titleHi: String,
    val description: String,
    val descriptionHi: String,
    val category: String,
    val categoryHi: String,
    val actYear: String,
    val details: String,
    val detailsHi: String
)

object LawHubData {
    val items = listOf(
        LawItem(
            title = "Right to Information (RTI) Act",
            titleHi = "सूचना का अधिकार (RTI) अधिनियम",
            description = "Empowers citizens to request information from public authorities, securing transparency and accountability.",
            descriptionHi = "नागरिकों को सार्वजनिक अधिकारियों से जानकारी मांगने का अधिकार देता है, जिससे सरकारी कामकाज में पारदर्शिता और जवाबदेही सुनिश्चित होती है।",
            category = "Transparency",
            categoryHi = "पारदर्शिता",
            actYear = "2005",
            details = "The RTI Act mandates timely response to citizen requests for government information. It applies to all public authorities and aims to curb corruption by promoting openness in government operations.",
            detailsHi = "RTI अधिनियम सार्वजनिक प्राधिकरणों से सरकारी जानकारी के लिए नागरिक अनुरोधों पर समय पर प्रतिक्रिया देना अनिवार्य बनाता है। यह सभी सार्वजनिक अधिकारियों पर लागू होता है और इसका उद्देश्य सरकारी कामकाज में खुलेपन को बढ़ावा देकर भ्रष्टाचार को रोकना है।"
        ),
        LawItem(
            title = "Consumer Protection Act",
            titleHi = "उपभोक्ता संरक्षण अधिनियम",
            description = "Protects consumers from unfair trade practices, defective goods, and negligent services.",
            descriptionHi = "उपभोक्ताओं को अनुचित व्यापार प्रथाओं, दोषपूर्ण वस्तुओं और सेवा में कमी या लापरवाही से कानूनी सुरक्षा प्रदान करता है।",
            category = "Consumer Rights",
            categoryHi = "उपभोक्ता अधिकार",
            actYear = "2019",
            details = "This act establishes Consumer Dispute Redressal Commissions at district, state, and national levels. It includes provisions for product liability, actions against misleading advertisements, and regulations for e-commerce.",
            detailsHi = "यह अधिनियम जिला, राज्य और राष्ट्रीय स्तर पर उपभोक्ता विवाद निवारण आयोगों की स्थापना करता है। इसमें उत्पाद दायित्व, भ्रामक विज्ञापनों के खिलाफ कार्रवाई और ई-कॉमर्स के नियम शामिल हैं।"
        ),
        LawItem(
            title = "Fundamental Rights (Constitution)",
            titleHi = "मौलिक अधिकार (संविधान)",
            description = "Core rights guaranteed to all citizens, including equality, freedom of speech, and right to life.",
            descriptionHi = "सभी नागरिकों को गारंटीकृत बुनियादी अधिकार, जिसमें समानता, अभिव्यक्ति की स्वतंत्रता और सम्मान के साथ जीवन जीने का अधिकार शामिल है।",
            category = "Constitutional Law",
            categoryHi = "संवैधानिक कानून",
            actYear = "1950",
            details = "These rights are enshrined in Part III of the Constitution. They include Right to Equality (Art. 14-18), Right to Freedom (Art. 19-22), Right against Exploitation (Art. 23-24), Freedom of Religion, and Right to Constitutional Remedies.",
            detailsHi = "ये अधिकार संविधान के भाग III में निहित हैं। इनमें समानता का अधिकार (अनुच्छेद 14-18), स्वतंत्रता का अधिकार (अनुच्छेद 19-22), शोषण के खिलाफ अधिकार (अनुच्छेद 23-24), धार्मिक स्वतंत्रता और संवैधानिक उपचारों का अधिकार शामिल हैं।"
        ),
        LawItem(
            title = "Code of Civil Procedure (CPC)",
            titleHi = "नागरिक प्रक्रिया संहिता (CPC)",
            description = "The administration of civil proceedings, governing civil lawsuits, court procedures, and enforcement.",
            descriptionHi = "दीवानी मुकदमों, अदालती प्रक्रियाओं और अदालती फैसलों के प्रवर्तन को नियंत्रित करने वाली सिविल कार्यवाहियों का प्रशासन।",
            category = "Civil Law",
            categoryHi = "दीवानी कानून",
            actYear = "1908",
            details = "The CPC is the body of laws that govern the procedure of civil courts. It provides the framework for filing suits, summons, hearings, judgments, appeals, execution of decrees, and interim orders.",
            detailsHi = "CPC उन कानूनों का समूह है जो दीवानी अदालतों की प्रक्रिया को नियंत्रित करते हैं। यह मुकदमा दायर करने, समन, सुनवाई, निर्णय, अपील, डिक्री के निष्पादन और अंतरिम आदेशों का ढांचा प्रदान करता है।"
        ),
        LawItem(
            title = "Code of Criminal Procedure (CrPC)",
            titleHi = "दंड प्रक्रिया संहिता (CrPC)",
            description = "The procedural law for administering criminal justice, covering investigation, arrest, and trial process.",
            descriptionHi = "आपराधिक न्याय के प्रशासन के लिए प्रक्रियात्मक कानून, जिसमें जांच, गिरफ्तारी, जमानत और अदालती परीक्षण प्रक्रिया शामिल है।",
            category = "Criminal Law",
            categoryHi = "आपराधिक कानून",
            actYear = "1973",
            details = "The CrPC details the entire mechanism for investigation of crime, apprehension of suspected criminals, collection of evidence, determination of guilt or innocence, and punishment of the guilty.",
            detailsHi = "CrPC अपराध की जांच, संदिग्ध अपराधियों को पकड़ने, सबूत इकट्ठा करने, अपराध या बेगुनाही के निर्धारण और दोषी को सजा देने की पूरी प्रक्रिया का विवरण देता है।"
        ),
        LawItem(
            title = "Maternity Benefit Act",
            titleHi = "मातृत्व लाभ अधिनियम",
            description = "Regulates the employment of women in certain establishments for certain periods before and after child-birth.",
            descriptionHi = "महिला कर्मचारियों के लिए बच्चे के जन्म से पहले और बाद में एक निश्चित अवधि के लिए रोजगार और मातृत्व अवकाश लाभों को नियंत्रित करता है।",
            category = "Labor Law",
            categoryHi = "श्रम कानून",
            actYear = "1961",
            details = "This act ensures paid maternity leave (fully paid salary) for 26 weeks for female employees, crèche facilities for establishments with 50+ employees, and protection against discrimination during pregnancy.",
            detailsHi = "यह अधिनियम महिला कर्मचारियों के लिए 26 सप्ताह के सवेतन मातृत्व अवकाश (पूरी तरह से भुगतान किया गया वेतन), 50+ कर्मचारियों वाले प्रतिष्ठानों के लिए शिशुगृह (क्रेश) सुविधाएं और गर्भावस्था के दौरान भेदभाव के खिलाफ सुरक्षा सुनिश्चित करता है।"
        ),
        LawItem(
            title = "Environmental Protection Act",
            titleHi = "पर्यावरण संरक्षण अधिनियम",
            description = "Empowers the government to protect and improve environmental quality, control and reduce pollution.",
            descriptionHi = "सरकार को पर्यावरण की गुणवत्ता की रक्षा करने और उसमें सुधार करने, तथा प्रदूषण को नियंत्रित और कम करने का अधिकार देता है।",
            category = "Environmental Law",
            categoryHi = "पर्यावरण कानून",
            actYear = "1986",
            details = "This act establishes the framework for coordinating various environmental protection activities and sets standards for environmental quality, discharge of industrial pollutants, and hazardous waste handling.",
            detailsHi = "यह अधिनियम विभिन्न पर्यावरण संरक्षण गतिविधियों के समन्वय के लिए रूपरेखा स्थापित करता है और पर्यावरणीय गुणवत्ता, औद्योगिक प्रदूषकों के निर्वहन और खतरनाक कचरे के प्रबंधन के मानक तय करता है।"
        )
    )
}
