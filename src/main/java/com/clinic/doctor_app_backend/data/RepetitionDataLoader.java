package com.clinic.doctor_app_backend.data;

import com.clinic.doctor_app_backend.model.Repetition;
import com.clinic.doctor_app_backend.repository.RepetitionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RepetitionDataLoader implements CommandLineRunner {

    private final RepetitionRepository repetitionRepository;

    public RepetitionDataLoader(RepetitionRepository repetitionRepository) {
        this.repetitionRepository = repetitionRepository;
    }

    @Override
    public void run(String... args) {

        saveIfNotExists("NONE", "NONE");
        saveIfNotExists("OD, Every Day", "OD");
        saveIfNotExists("BID, Twice Daily", "BID");
        saveIfNotExists("TID, Three times per day", "TID");
        saveIfNotExists("QID, Four times per day", "QID");
        saveIfNotExists("Q1 Hrs, Every Hour", "Q1H");
        saveIfNotExists("Q2 Hrs, Even Hours", "Q2HE");
        saveIfNotExists("Q3 Hrs, Start @ 03:00", "Q3H3");
        saveIfNotExists("Q4 Hrs, Start @ 01:00", "Q4H1");
        saveIfNotExists("Q6 Hrs, Start @ 02:00", "Q6H2");
        saveIfNotExists("Q8 Hrs, Start @ 01:00", "Q8H1");
        saveIfNotExists("Q12 Hrs, Nine o'clock", "Q12N");
        saveIfNotExists("Q18 Hrs, Start @ 08:00", "Q18H");
        saveIfNotExists("Q24 Hrs, Start @ 08:00", "Q24H");
        saveIfNotExists("Q48 Hrs, Start @ 09:00", "Q48H");
        saveIfNotExists("Q96 Hrs, Start @ 10:00", "Q96H");
        saveIfNotExists("STAT", "STAT");
        saveIfNotExists("PRN, OD", "PRNOD");
        saveIfNotExists("PRN, TID", "PRNTID");
        saveIfNotExists("PRN, QID", "PRNQID");
        saveIfNotExists("PRN, BID", "PRNBID");
        saveIfNotExists("SCH, SCHEMA DOSES", "SCH");
        saveIfNotExists("CST, CUSTOM DOSES", "CST");
        saveIfNotExists("OTH, OTHERS DOSES", "OTH");
        saveIfNotExists("TPR, TAPERING DOSES", "TPR");
        saveIfNotExists("Q2 Hrs, Odd Hours", "Q2HO");
        saveIfNotExists("Q4 Hrs, Start @ 02:00", "Q4H2");

        saveIfNotExists("Q4 Hrs, Start @ 03:00", "Q4H3");
        saveIfNotExists("Q4 Hrs, Start @ 04:00", "Q4H4");
        saveIfNotExists("Q6 Hrs, Start @ 03:00", "Q6H3");
        saveIfNotExists("Q6 Hrs, Start @ 04:00", "Q6H4");
        saveIfNotExists("Q6 Hrs, Start @ 06:00", "Q6H6");
        saveIfNotExists("Q8 Hrs, Start @ 02:00", "Q8H2");
        saveIfNotExists("Q8 Hrs, Start @ 04:00", "Q8H4");
        saveIfNotExists("Q8 Hrs, Start @ 06:00", "Q8H6");
        saveIfNotExists("Q8 Hrs, Start @ 08:00", "Q8H9");

        saveIfNotExists("Q12 Hrs, Six o'clock", "Q12P");
        saveIfNotExists("Q12 Hrs, Twelve o'clock", "Q12T");

        saveIfNotExists("TID, Three times per day, With Meals", "TIDM");
        saveIfNotExists("TID, Three times per day, Pediatrices", "TIDP");

        saveIfNotExists("QID, Four times per day, With Meals", "QIDM");
        saveIfNotExists("QID, Four times per day, Pediatrices", "QIDP");

        saveIfNotExists("QS, Every Shift Change", "QS");

        saveIfNotExists("QD15, TPN's Pediatrics, Start @ 15:00", "OD15");
        saveIfNotExists("QD17, Every Day @ 17:00", "OD17");
        saveIfNotExists("QD06, Every Day @ 06:00", "OD06");
        saveIfNotExists("QD07, Every Day @ 07:00", "OD07");
        saveIfNotExists("QD08, Every Day @ 08:00", "OD08");

        saveIfNotExists("QHSA,QHS, Every Day @ Adult Bedtime", "ODHSA");
        saveIfNotExists("QHSP,QHP, Every Day @ Pediatric Bedtime", "ODHSP");

        saveIfNotExists("Q5xD, 5 times per day, Every 3 Hours", "Q5xD3H");
        saveIfNotExists("Q5xD, 5 times per day, Every 4 Hours", "Q5xD4H");

        saveIfNotExists("AC, Before Meals", "AC");
        saveIfNotExists("ACHS, Before Meals & at bedtime", "ACHS");

        saveIfNotExists("BIDF, Twice Daily, Start @ 04:00", "BIDF");
        saveIfNotExists("BIDI, Twice Daily, For Insulin", "BIDI");
        saveIfNotExists("BIDM, Twice Daily, With Meals", "BIDM");
        saveIfNotExists("BIDM, Twice Daily, For Pediatrices", "BIDP");

        saveIfNotExists("PC, After Meals", "PC");
        saveIfNotExists("PCHS, After Meals & at bedtime", "PCHS");

        saveIfNotExists("NTP, Q6H, nitropaste, off at bedtime", "NTP");
        saveIfNotExists("OCOR, On Call to operating room", "OCOR");

        saveIfNotExists("HS, At bedtime", "HS");
        saveIfNotExists("CC, WM, With Meals", "CC");

        saveIfNotExists("QAM, Every Morning", "QAM");
        saveIfNotExists("QPM, Every Evening", "QPM");

        saveIfNotExists("BIDAC, Twice Daily Before Meals", "BIDAC");
        saveIfNotExists("BIDPC, Twice Daily After Meals", "BIDPC");
        saveIfNotExists("BIDWM, Twice Daily With Meals", "BIDWM");

        saveIfNotExists("QAMHS, Morning & Bedtime", "QAMHS");

        saveIfNotExists("Once Weekly", "WEEK");
        saveIfNotExists("Once Monthly", "MONTH");
        saveIfNotExists("Twice Weekly", "WEEK2");
        saveIfNotExists("O2D, Once Every Two Days", "O2D");
        saveIfNotExists("Every 10 Days", "E10D");
        saveIfNotExists("Every Other Weak", "EOW");
        saveIfNotExists("Three Times Every Week", "TIW");
        saveIfNotExists("Every Other Day For Week", "QODW");
        saveIfNotExists("Every Three Days", "Q3D");
        saveIfNotExists("TTT, Three Times", "TTT");

        saveIfNotExists("ON CALL", "ONCALL");
        saveIfNotExists("ASAP", "ASAP");

        saveIfNotExists("Every 6 Months", "O6M");
        saveIfNotExists("Every 1 Year", "O1Y");

        saveIfNotExists("6 Days/Week", "6D/W");
        saveIfNotExists("Ten Times", "TT");
        saveIfNotExists("MORNING, ONCE AT 10 AM", "MO");
        saveIfNotExists("EVENING, ONCE AT 10 PM", "EO");

        saveIfNotExists("Every Other Day", "EOD");
        saveIfNotExists("Sliding scale", "Sliding");
        saveIfNotExists("4h", "q4");
        saveIfNotExists("OD, Every Day", "ODE");


    }

    private void saveIfNotExists(String desc, String code) {

        if (!repetitionRepository.existsByRepetitionCode(code)) {

            repetitionRepository.save(
                    new Repetition(
                            null,
                            desc,
                            code
                    )
            );
        }
    }
}
