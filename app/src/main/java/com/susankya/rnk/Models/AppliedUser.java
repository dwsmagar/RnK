package com.susankya.rnk.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppliedUser {
//    public AppliedUser(String lastName, String middleName, String firstName) {
//        this.lastName = lastName;
//        this.middleName = middleName;
//        this.firstName = firstName;
//    }

    @SerializedName("year")
    @Expose
    public String year;
    @SerializedName("intake")
    @Expose
    public String intake;
    @SerializedName("course")
    @Expose
    public String course;
    @SerializedName("permanent_country")
    @Expose
    public String permanentCountry;
    @SerializedName("title")
    @Expose
    public String title;
    @SerializedName("last_name")
    @Expose
    public String lastName;
    @SerializedName("middle_name")
    @Expose
    public String middleName;
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("date_of_birth")
    @Expose
    public String dateOfBirth;
    @SerializedName("place_of_birth")
    @Expose
    public String placeOfBirth;
    @SerializedName("sex")
    @Expose
    public String sex;
    @SerializedName("nationality")
    @Expose
    public String nationality;
    @SerializedName("marital_status")
    @Expose
    public String maritalStatus;
    @SerializedName("citizenship_number")
    @Expose
    public String citizenshipNumber;
    @SerializedName("passport_number")
    @Expose
    public String passportNumber;
    @SerializedName("date_of_issue")
    @Expose
    public String dateOfIssue;
    @SerializedName("date_of_expiry")
    @Expose
    public String dateOfExpiry;
    @SerializedName("place_of_issue")
    @Expose
    public String placeOfIssue;
    @SerializedName("street_name_number")
    @Expose
    public String streetNameNumber;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("phone")
    @Expose
    public String phone;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("is_permanent")
    @Expose
    public Boolean isPermanent;
    @SerializedName("school_name")
    @Expose
    public String schoolName;
    @SerializedName("school_city")
    @Expose
    public String schoolCity;
    @SerializedName("school_state")
    @Expose
    public String schoolState;
    @SerializedName("school_country")
    @Expose
    public String schoolCountry;
    @SerializedName("school_qualification_obtained")
    @Expose
    public String schoolQualificationObtained;
    @SerializedName("school_marks_obtained")
    @Expose
    public String schoolMarksObtained;
    @SerializedName("school_date_of_completion")
    @Expose
    public String schoolDateOfCompletion;
    @SerializedName("high_school_name")
    @Expose
    public String highSchoolName;
    @SerializedName("high_school_city")
    @Expose
    public String highSchoolCity;
    @SerializedName("high_school_state")
    @Expose
    public String highSchoolState;
    @SerializedName("high_school_country")
    @Expose
    public String highSchoolCountry;
    @SerializedName("high_school_qualification_obtained")
    @Expose
    public String highSchoolQualificationObtained;
    @SerializedName("high_school_marks_obtained")
    @Expose
    public String highSchoolMarksObtained;
    @SerializedName("high_school_date_of_completion")
    @Expose
    public String highSchoolDateOfCompletion;
    @SerializedName("undergrad_university")
    @Expose
    public String undergradUniversity;
    @SerializedName("undergrad_city")
    @Expose
    public String undergradCity;
    @SerializedName("undergrad_state")
    @Expose
    public String undergradState;
    @SerializedName("undergrad_country")
    @Expose
    public String undergradCountry;
    @SerializedName("undergrad_degree_obtained")
    @Expose
    public String undergradDegreeObtained;
    @SerializedName("undergrad_major_subject")
    @Expose
    public String undergradMajorSubject;
    @SerializedName("undergrad_marks_obtained")
    @Expose
    public String undergradMarksObtained;
    @SerializedName("undergrad_date_of_completion")
    @Expose
    public String undergradDateOfCompletion;
    @SerializedName("graduate_university")
    @Expose
    public String graduateUniversity;
    @SerializedName("graduate_city")
    @Expose
    public String graduateCity;
    @SerializedName("graduate_state")
    @Expose
    public String graduateState;
    @SerializedName("graduate_country")
    @Expose
    public String graduateCountry;
    @SerializedName("graduate_degree_obtained")
    @Expose
    public String graduateDegreeObtained;
    @SerializedName("graduate_major_subject")
    @Expose
    public String graduateMajorSubject;
    @SerializedName("graduate_marks_obtained")
    @Expose
    public String graduateMarksObtained;
    @SerializedName("graduate_date_of_completion")
    @Expose
    public String graduateDateOfCompletion;
    @SerializedName("ielts")
    @Expose
    public String ielts;
    @SerializedName("toefl")
    @Expose
    public String toefl;
    @SerializedName("sat")
    @Expose
    public String sat;
    @SerializedName("gre")
    @Expose
    public String gre;
    @SerializedName("gmat")
    @Expose
    public String gmat;
    @SerializedName("pte")
    @Expose
    public String pte;
    @SerializedName("employment_current_employer")
    @Expose
    public String employmentCurrentEmployer;
    @SerializedName("field_of_activity")
    @Expose
    public String fieldOfActivity;
    @SerializedName("position")
    @Expose
    public String position;
    @SerializedName("start_date")
    @Expose
    public String startDate;
    @SerializedName("department")
    @Expose
    public String department;
    @SerializedName("employment_type")
    @Expose
    public String employmentType;
    @SerializedName("responsibilites")
    @Expose
    public String responsibilites;
    @SerializedName("previous_employer")
    @Expose
    public String previousEmployer;
    @SerializedName("previous_location")
    @Expose
    public String previousLocation;
    @SerializedName("previous_job_title")
    @Expose
    public String previousJobTitle;
    @SerializedName("previous_start_date")
    @Expose
    public String previousStartDate;
    @SerializedName("previous_end_date")
    @Expose
    public String previousEndDate;
    @SerializedName("previous_employment_type")
    @Expose
    public String previousEmploymentType;
    @SerializedName("question1")
    @Expose
    public String question1;
    @SerializedName("question2")
    @Expose
    public String question2;
    @SerializedName("question3")
    @Expose
    public String question3;
    @SerializedName("question4")
    @Expose
    public String question4;
    @SerializedName("question5")
    @Expose
    public String question5;
    @SerializedName("question6")
    @Expose
    public String question6;
    @SerializedName("question7")
    @Expose
    public String question7;
    @SerializedName("question8")
    @Expose
    public String question8;
    @SerializedName("statement_of_purpose")
    @Expose
    public String statementOfPurpose;
    @SerializedName("photograph")
    @Expose
    public String photograph;
    @SerializedName("resume")
    @Expose
    public String resume;
    @SerializedName("passport_copy")
    @Expose
    public String passportCopy;
    @SerializedName("citizenship_copy")
    @Expose
    public String citizenshipCopy;
    @SerializedName("school_education_certificate")
    @Expose
    public String schoolEducationCertificate;
    @SerializedName("high_school_certificate")
    @Expose
    public String highSchoolCertificate;
    @SerializedName("undergrad_certificate")
    @Expose
    public String undergradCertificate;
    @SerializedName("graduate_certificate")
    @Expose
    public String graduateCertificate;
    @SerializedName("test")
    @Expose
    public String test;
    @SerializedName("certification")
    @Expose
    public Boolean certification;
    @SerializedName("signature_name")
    @Expose
    public String signatureName;
    @SerializedName("date_of_signature")
    @Expose
    public String dateOfSignature;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIntake() {
        return intake;
    }

    public void setIntake(String intake) {
        this.intake = intake;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getPermanentCountry() {
        return permanentCountry;
    }

    public void setPermanentCountry(String permanentCountry) {
        this.permanentCountry = permanentCountry;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(String dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getPlaceOfIssue() {
        return placeOfIssue;
    }

    public void setPlaceOfIssue(String placeOfIssue) {
        this.placeOfIssue = placeOfIssue;
    }

    public String getStreetNameNumber() {
        return streetNameNumber;
    }

    public void setStreetNameNumber(String streetNameNumber) {
        this.streetNameNumber = streetNameNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getIsPermanent() {
        return isPermanent;
    }

    public void setIsPermanent(Boolean isPermanent) {
        this.isPermanent = isPermanent;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolCity() {
        return schoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        this.schoolCity = schoolCity;
    }

    public String getSchoolState() {
        return schoolState;
    }

    public void setSchoolState(String schoolState) {
        this.schoolState = schoolState;
    }

    public String getSchoolCountry() {
        return schoolCountry;
    }

    public void setSchoolCountry(String schoolCountry) {
        this.schoolCountry = schoolCountry;
    }

    public String getSchoolQualificationObtained() {
        return schoolQualificationObtained;
    }

    public void setSchoolQualificationObtained(String schoolQualificationObtained) {
        this.schoolQualificationObtained = schoolQualificationObtained;
    }

    public String getSchoolMarksObtained() {
        return schoolMarksObtained;
    }

    public void setSchoolMarksObtained(String schoolMarksObtained) {
        this.schoolMarksObtained = schoolMarksObtained;
    }

    public String getSchoolDateOfCompletion() {
        return schoolDateOfCompletion;
    }

    public void setSchoolDateOfCompletion(String schoolDateOfCompletion) {
        this.schoolDateOfCompletion = schoolDateOfCompletion;
    }

    public String getHighSchoolName() {
        return highSchoolName;
    }

    public void setHighSchoolName(String highSchoolName) {
        this.highSchoolName = highSchoolName;
    }

    public String getHighSchoolCity() {
        return highSchoolCity;
    }

    public void setHighSchoolCity(String highSchoolCity) {
        this.highSchoolCity = highSchoolCity;
    }

    public String getHighSchoolState() {
        return highSchoolState;
    }

    public void setHighSchoolState(String highSchoolState) {
        this.highSchoolState = highSchoolState;
    }

    public String getHighSchoolCountry() {
        return highSchoolCountry;
    }

    public void setHighSchoolCountry(String highSchoolCountry) {
        this.highSchoolCountry = highSchoolCountry;
    }

    public String getHighSchoolQualificationObtained() {
        return highSchoolQualificationObtained;
    }

    public void setHighSchoolQualificationObtained(String highSchoolQualificationObtained) {
        this.highSchoolQualificationObtained = highSchoolQualificationObtained;
    }

    public String getHighSchoolMarksObtained() {
        return highSchoolMarksObtained;
    }

    public void setHighSchoolMarksObtained(String highSchoolMarksObtained) {
        this.highSchoolMarksObtained = highSchoolMarksObtained;
    }

    public String getHighSchoolDateOfCompletion() {
        return highSchoolDateOfCompletion;
    }

    public void setHighSchoolDateOfCompletion(String highSchoolDateOfCompletion) {
        this.highSchoolDateOfCompletion = highSchoolDateOfCompletion;
    }

    public String getUndergradUniversity() {
        return undergradUniversity;
    }

    public void setUndergradUniversity(String undergradUniversity) {
        this.undergradUniversity = undergradUniversity;
    }

    public String getUndergradCity() {
        return undergradCity;
    }

    public void setUndergradCity(String undergradCity) {
        this.undergradCity = undergradCity;
    }

    public String getUndergradState() {
        return undergradState;
    }

    public void setUndergradState(String undergradState) {
        this.undergradState = undergradState;
    }

    public String getUndergradCountry() {
        return undergradCountry;
    }

    public void setUndergradCountry(String undergradCountry) {
        this.undergradCountry = undergradCountry;
    }

    public String getUndergradDegreeObtained() {
        return undergradDegreeObtained;
    }

    public void setUndergradDegreeObtained(String undergradDegreeObtained) {
        this.undergradDegreeObtained = undergradDegreeObtained;
    }

    public String getUndergradMajorSubject() {
        return undergradMajorSubject;
    }

    public void setUndergradMajorSubject(String undergradMajorSubject) {
        this.undergradMajorSubject = undergradMajorSubject;
    }

    public String getUndergradMarksObtained() {
        return undergradMarksObtained;
    }

    public void setUndergradMarksObtained(String undergradMarksObtained) {
        this.undergradMarksObtained = undergradMarksObtained;
    }

    public String getUndergradDateOfCompletion() {
        return undergradDateOfCompletion;
    }

    public void setUndergradDateOfCompletion(String undergradDateOfCompletion) {
        this.undergradDateOfCompletion = undergradDateOfCompletion;
    }

    public String getGraduateUniversity() {
        return graduateUniversity;
    }

    public void setGraduateUniversity(String graduateUniversity) {
        this.graduateUniversity = graduateUniversity;
    }

    public String getGraduateCity() {
        return graduateCity;
    }

    public void setGraduateCity(String graduateCity) {
        this.graduateCity = graduateCity;
    }

    public String getGraduateState() {
        return graduateState;
    }

    public void setGraduateState(String graduateState) {
        this.graduateState = graduateState;
    }

    public String getGraduateCountry() {
        return graduateCountry;
    }

    public void setGraduateCountry(String graduateCountry) {
        this.graduateCountry = graduateCountry;
    }

    public String getGraduateDegreeObtained() {
        return graduateDegreeObtained;
    }

    public void setGraduateDegreeObtained(String graduateDegreeObtained) {
        this.graduateDegreeObtained = graduateDegreeObtained;
    }

    public String getGraduateMajorSubject() {
        return graduateMajorSubject;
    }

    public void setGraduateMajorSubject(String graduateMajorSubject) {
        this.graduateMajorSubject = graduateMajorSubject;
    }

    public String getGraduateMarksObtained() {
        return graduateMarksObtained;
    }

    public void setGraduateMarksObtained(String graduateMarksObtained) {
        this.graduateMarksObtained = graduateMarksObtained;
    }

    public String getGraduateDateOfCompletion() {
        return graduateDateOfCompletion;
    }

    public void setGraduateDateOfCompletion(String graduateDateOfCompletion) {
        this.graduateDateOfCompletion = graduateDateOfCompletion;
    }

    public String getIelts() {
        return ielts;
    }

    public void setIelts(String ielts) {
        this.ielts = ielts;
    }

    public String getToefl() {
        return toefl;
    }

    public void setToefl(String toefl) {
        this.toefl = toefl;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getGre() {
        return gre;
    }

    public void setGre(String gre) {
        this.gre = gre;
    }

    public String getGmat() {
        return gmat;
    }

    public void setGmat(String gmat) {
        this.gmat = gmat;
    }

    public String getPte() {
        return pte;
    }

    public void setPte(String pte) {
        this.pte = pte;
    }

    public String getEmploymentCurrentEmployer() {
        return employmentCurrentEmployer;
    }

    public void setEmploymentCurrentEmployer(String employmentCurrentEmployer) {
        this.employmentCurrentEmployer = employmentCurrentEmployer;
    }

    public String getFieldOfActivity() {
        return fieldOfActivity;
    }

    public void setFieldOfActivity(String fieldOfActivity) {
        this.fieldOfActivity = fieldOfActivity;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getResponsibilites() {
        return responsibilites;
    }

    public void setResponsibilites(String responsibilites) {
        this.responsibilites = responsibilites;
    }

    public String getPreviousEmployer() {
        return previousEmployer;
    }

    public void setPreviousEmployer(String previousEmployer) {
        this.previousEmployer = previousEmployer;
    }

    public String getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(String previousLocation) {
        this.previousLocation = previousLocation;
    }

    public String getPreviousJobTitle() {
        return previousJobTitle;
    }

    public void setPreviousJobTitle(String previousJobTitle) {
        this.previousJobTitle = previousJobTitle;
    }

    public String getPreviousStartDate() {
        return previousStartDate;
    }

    public void setPreviousStartDate(String previousStartDate) {
        this.previousStartDate = previousStartDate;
    }

    public String getPreviousEndDate() {
        return previousEndDate;
    }

    public void setPreviousEndDate(String previousEndDate) {
        this.previousEndDate = previousEndDate;
    }

    public String getPreviousEmploymentType() {
        return previousEmploymentType;
    }

    public void setPreviousEmploymentType(String previousEmploymentType) {
        this.previousEmploymentType = previousEmploymentType;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getQuestion4() {
        return question4;
    }

    public void setQuestion4(String question4) {
        this.question4 = question4;
    }

    public String getQuestion5() {
        return question5;
    }

    public void setQuestion5(String question5) {
        this.question5 = question5;
    }

    public String getQuestion6() {
        return question6;
    }

    public void setQuestion6(String question6) {
        this.question6 = question6;
    }

    public String getQuestion7() {
        return question7;
    }

    public void setQuestion7(String question7) {
        this.question7 = question7;
    }

    public String getQuestion8() {
        return question8;
    }

    public void setQuestion8(String question8) {
        this.question8 = question8;
    }

    public String getStatementOfPurpose() {
        return statementOfPurpose;
    }

    public void setStatementOfPurpose(String statementOfPurpose) {
        this.statementOfPurpose = statementOfPurpose;
    }

    public Boolean getPermanent() {
        return isPermanent;
    }

    public void setPermanent(Boolean permanent) {
        isPermanent = permanent;
    }

    public String getPhotograph() {
        return photograph;
    }

    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getPassportCopy() {
        return passportCopy;
    }

    public void setPassportCopy(String passportCopy) {
        this.passportCopy = passportCopy;
    }

    public String getCitizenshipCopy() {
        return citizenshipCopy;
    }

    public void setCitizenshipCopy(String citizenshipCopy) {
        this.citizenshipCopy = citizenshipCopy;
    }

    public String getSchoolEducationCertificate() {
        return schoolEducationCertificate;
    }

    public void setSchoolEducationCertificate(String schoolEducationCertificate) {
        this.schoolEducationCertificate = schoolEducationCertificate;
    }

    public String getHighSchoolCertificate() {
        return highSchoolCertificate;
    }

    public void setHighSchoolCertificate(String highSchoolCertificate) {
        this.highSchoolCertificate = highSchoolCertificate;
    }

    public String getUndergradCertificate() {
        return undergradCertificate;
    }

    public void setUndergradCertificate(String undergradCertificate) {
        this.undergradCertificate = undergradCertificate;
    }

    public String getGraduateCertificate() {
        return graduateCertificate;
    }

    public void setGraduateCertificate(String graduateCertificate) {
        this.graduateCertificate = graduateCertificate;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Boolean getCertification() {
        return certification;
    }

    public void setCertification(Boolean certification) {
        this.certification = certification;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getDateOfSignature() {
        return dateOfSignature;
    }

    public void setDateOfSignature(String dateOfSignature) {
        this.dateOfSignature = dateOfSignature;
    }

}
