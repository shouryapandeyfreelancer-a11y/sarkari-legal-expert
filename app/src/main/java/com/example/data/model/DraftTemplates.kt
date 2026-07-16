package com.example.data.model

data class DraftField(
    val key: String,
    val label: String,
    val placeholder: String,
    val defaultValue: String = ""
)

data class DraftTemplate(
    val id: String,
    val title: String,
    val category: String,
    val description: String,
    val fields: List<DraftField>,
    val promptBuilder: (Map<String, String>) -> String
)

object DraftTemplates {
    val templates = listOf(
        DraftTemplate(
            id = "rti_app",
            title = "RTI Application",
            category = "Transparency",
            description = "Draft a formal application to seek public information under the Right to Information Act.",
            fields = listOf(
                DraftField("authority", "Public Authority Department", "e.g., Municipal Corporation / Department of Education"),
                DraftField("info", "Information Required", "e.g., Details of funds allocated and spent on road repairs in Area X during 2024-25"),
                DraftField("applicant", "Applicant Name", "e.g., John Doe"),
                DraftField("address", "Applicant Address", "e.g., 123, Blue Street, New Delhi, 110001")
            ),
            promptBuilder = { fields ->
                """
                Create a formal Right to Information (RTI) Application under the Right to Information Act, 2005 based on the following details:
                - Public Authority Department: ${fields["authority"]}
                - Information Required: ${fields["info"]}
                - Applicant Name: ${fields["applicant"]}
                - Applicant Address: ${fields["address"]}
                
                Please ensure the document contains the correct legal formatting, references to Section 6(1) of the RTI Act, 2005, a declaration of citizenship, a request for information in print/soft-copy format, and clear placeholders for application fees and signatures. Provide only the clean, beautifully formatted text document without markdown notes or explanations.
                """.trimIndent()
            }
        ),
        DraftTemplate(
            id = "rental_agreement",
            title = "Lease / Rental Agreement",
            category = "Property & Civil",
            description = "Draft a comprehensive residential lease agreement between a landlord and tenant.",
            fields = listOf(
                DraftField("landlord", "Landlord Name", "e.g., Jane Smith"),
                DraftField("tenant", "Tenant Name", "e.g., David Miller"),
                DraftField("address", "Property Address", "e.g., Flat 402, Green Meadows Apartment, Sector 15"),
                DraftField("rent", "Monthly Rent Amount", "e.g., $1,200"),
                DraftField("deposit", "Security Deposit Amount", "e.g., $2,400"),
                DraftField("duration", "Lease Duration (Months)", "e.g., 11 months")
            ),
            promptBuilder = { fields ->
                """
                Draft a formal and professional residential rental/lease agreement using the following details:
                - Landlord (Lessor): ${fields["landlord"]}
                - Tenant (Lessee): ${fields["tenant"]}
                - Property Address: ${fields["address"]}
                - Monthly Rent: ${fields["rent"]}
                - Security Deposit: ${fields["deposit"]}
                - Duration: ${fields["duration"]}
                
                The document should include standard legal clauses for tenancy: rent payment date, termination notice period (e.g., 30 days), maintenance responsibilities, subletting restrictions, and utilities responsibility. Present it in a clear, formatted style with numbered articles and signature blocks. Do not add general notes or explanations.
                """.trimIndent()
            }
        ),
        DraftTemplate(
            id = "affidavit",
            title = "General Affidavit",
            category = "Civil & Court",
            description = "Draft a sworn statement of facts to be presented as legal proof for official purposes.",
            fields = listOf(
                DraftField("deponent", "Deponent (Your Name)", "e.g., Robert Harrison"),
                DraftField("parent", "Father's / Spouse's Name", "e.g., Thomas Harrison"),
                DraftField("address", "Residential Address", "e.g., 45, Oakwood Lane, CA"),
                DraftField("statement", "Statement of Facts", "e.g., State that I lost my original High School graduation certificate (ID: 98765) and have not misused it anywhere.")
            ),
            promptBuilder = { fields ->
                """
                Generate a formal General Affidavit based on the following details:
                - Deponent: ${fields["deponent"]}
                - Father's/Spouse's Name: ${fields["parent"]}
                - Resident of: ${fields["address"]}
                - Solemnly Affirmed Facts: ${fields["statement"]}
                
                Please use the standard legal structure for an affidavit, beginning with 'I, [Name], solemnly affirm and state as under:'. It should list the statements clearly in numbered paragraphs, include a formal Verification clause at the end where the deponent verifies the contents are true to their knowledge, and provide clearly marked signature blocks. Do not write notes or explanations outside the legal document.
                """.trimIndent()
            }
        ),
        DraftTemplate(
            id = "consumer_complaint",
            title = "Consumer Complaint Notice",
            category = "Consumer Protection",
            description = "Draft a formal legal notice to a company or vendor demanding redressal for a defective product or service.",
            fields = listOf(
                DraftField("complainant", "Complainant Name", "e.g., Patricia Evans"),
                DraftField("company", "Company / Vendor Name", "e.g., ElectroMax Retailers Ltd."),
                DraftField("product", "Product / Service Name", "e.g., ElectroMax Pro Washing Machine"),
                DraftField("date", "Date of Purchase", "e.g., May 10, 2026"),
                DraftField("issue", "Issue Description", "e.g., The washing machine leaked on first use. Technicians came twice but couldn't fix it. The vendor is refusing a refund or replacement.")
            ),
            promptBuilder = { fields ->
                """
                Generate a formal Legal Notice for a Consumer Complaint with the following details:
                - Complainant (Consumer): ${fields["complainant"]}
                - Opposite Party (Company/Vendor): ${fields["company"]}
                - Product/Service: ${fields["product"]}
                - Date of Purchase: ${fields["date"]}
                - Issue details: ${fields["issue"]}
                
                Draft this as a legal demand notice under the Consumer Protection Act, 2019 (or relevant consumer law). It should demand rectification, replacement, or refund of the purchase price along with reasonable compensation for mental agony within 15 days of receiving the notice. Include a section outlining the details of the purchase, the specific grievance, and a warning that legal proceedings will be initiated in the Consumer Forum if compliance is not met. Clean text only, formatted.
                """.trimIndent()
            }
        )
    )
}
