/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.epic.dlb.util.common;

import com.epic.dlb.model.DlbStatus;

/**
 *
 * @author Charan
 */
public class SystemVarList {

    public static final int ACTIVE = 1;
    public static final int INACTIVE = 2;
    public static final int SUBMITED = 31;
    public static final int APPROVED = 27;
    public static final int FAILED = 47;
    public static final String WORKFLOW_USR_CAT = "WEB";

    public static final String WINING_FILE_APPROVAL = "APP";
    public static final String COMPLATEDTE_STAGE = "CMPL";

    public static int SESSION_ERROR_CODE = 600;
    public static int ACCESS_ERROR_CODE = 601;

    public static int PAGE_FILE_COUNT = 12;

    public static String USER = "user";
    public static String USER_PRIVILEGES = "user_privileges";
    public static String USER_PRIVILEGED_SECTIONS = "user_privileged_sections";
    public static String EMPLOYEE = "employee";
    public static String IP = "ip";

    //locaked record
    public static String LOCKED = "LCKD";
    public static String OPENED = "OPD";

    //yes or No
    public static String YES = "32";
    public static String NO = "29";

    //System CRUD status
    public static String ERROR = "error";
    public static String SUCCESS = "success";
    public static String WARNING = "warning";

    //where operator
    public static String LIKE = "like";
    public static String EQUAL = "=";
    public static String NOT_EQUAL = "<>";
    public static String IS_NULL = "IS NULL";
    public static String IS_NOT_NULL = "IS NOT NULL";
    public static String ALL = "all";
    public static String LESS_THAN = "less_than";
    public static String LESS_THAN_OR_EQUAL = "less_than_or_equal";
    public static String GREATER_THAN = "greater_than";
    public static String GREATER_THAN_OR_EQUAL  = "greater_than_or_equal";

    //document action
    public static String DOCUMENT_VIEW_ONLY = "VO";
    public static String DOCUMENT_SAVE_ONLY = "SV";
    public static String DOCUMENT_VIEW_AND_SAVE = "VNS";

    public static String MESSAGE = "msg";
    public static String STATUS = "status";
    public static String RECORD = "record";
    public static String LOGIN_PAGE = "common/login";
    public static String HOME_PAGE = "common/home";

    public static String ASC = "ASC";
    public static String DESC = "DESC";

    //hibernate exception
    public static String ConstraintViolationException = "Info:   org.hibernate.exception.ConstraintViolationException: could not execute statement";

    //task
    public static String TASK_INFO = "task_info";
    public static String TASK_PROGRESS = "tp";
    public static String TASK_STATUS = "tst";
    public static String COMPLTED = "CML";
    public static String INIT = "INT";
    public static String PENDING = "PND";
    public static String FAIL = "FL";

    //workflow
    public static String APPROVAL_PENDING = "APP";
    public static int PROCESSING = 30;
    public static String READY_TO_CHECKOUT = "17";
    public static String TEMP_TO_CHECKOUT = "18";
    public static String CHECKOUT = "19";
    public static String WINNING_EXPIRED = "28";
    public static int RETURNED = 36;
    public static int SALES_FILE_GENERATED = 37;
    public static int REDEEM = 18;
    public static int WINNER_AMT_CLAIMED = 46;
    public static int DLB_CLAIMED = 44;
    public static int DLB_CLAIMED_1 = 24;

    //history types
    public static String ACTIVITY_HISTORY = "activity";
    public static String PURCHASE_HISTORY = "purchase";
    public static String TRANSACTION_HISTORY = "transaction_history";
    public static String RECEIVED_TICKET_COUNT = "Reserved Ticket Count";

    //Operator
    public static String AND = " and ";
    public static String OR = " or ";

    //other bank transaction
    public static int OTHER_BNK_TXT_PENDING = 40;
    public static int OTHER_BNK_TXT_FILE_GENERATED = 45;
    public static int OTHER_BNK_TXT_SUSCESS = 41;
    public static int OTHER_BNK_TXT_FAIL = 42;
    public static int PRIZE_PAY_FILE_GENERATED = 60;
    public static int CLAIMED_DONE = 61;
    public static int USER_CLAIMED = 25;
    public static int USER_CLAIME_PENDING = 26;

    public static int HNB_BANK_CODE = 7083;

    public static int HND_TXT_PENDING = 38;
    public static int HND_TXT_SUCCESS = 39;

    public static Double EPIC_MAX_CLAIMED_AMOUNT = 100000.00;

    public static int PRIZE_LESS_THAN_100000 = 20;
    public static int PRIZE_LARGE_THAN_OR_EQ_100000 = 21;

    public static int OTP_SUCESS = 4;
    public static int OTP_FAIL = 5;

    public static int TICKERT_PURCHASE = 15;
    public static Double TRANSACTION_COMMESION = 0.1875;

    public static String DISTRIBUTER_CODE = "300001";

    public static Double CARD_COMMISION = 0.0325;
    public static Double CASA_COMMISION = 0.015;

    public static int PAY_METHOD_CREDIT_CARD = 1;
    public static int PAY_METHOD_DEBIT_CARD = 2;
    public static int PAY_METHOD_CASA = 3;
    public static String JACKPOT_PRIZE_CODE="1";
    
    //operation request types
    public static String MANUL_REDEEM_REQUEST="MANUL-REDEEM-REQUEST";

}
