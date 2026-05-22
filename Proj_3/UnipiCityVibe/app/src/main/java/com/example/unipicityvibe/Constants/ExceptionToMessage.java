package com.example.unipicityvibe.Constants;

import com.example.unipicityvibe.Constants.Exception.AuthControlException;
import com.example.unipicityvibe.Constants.Exception.UserAuthException;
import com.example.unipicityvibe.Constants.Exception.UserDBException;
import com.example.unipicityvibe.R;

public final class ExceptionToMessage {

    public static int AuthExceptionToTextId(String errorText){
        int textResId;
        switch (errorText){
            // AuthControl exceptions
            case AuthControlException.USER_LOGGED_IN:
                textResId = R.string.error_user_already_logged_in;
                break;
            case AuthControlException.USER_NOT_LOGGED_IN:
                textResId = R.string.error_user_not_logged_in;
                break;

            // UserAuth & UserDB Validation
            case UserAuthException.EMPTY_EMAIL: // UserDBException.EMPTY_EMAIL
                textResId = R.string.error_email_empty;
                break;
            case UserAuthException.EMPTY_PASSWORD:
                textResId = R.string.error_password_empty;
                break;
            case UserAuthException.EMAIL_VALIDATION_ERROR: // UserDBException.EMAIL_VALIDATION_ERROR
                textResId = R.string.error_email_invalid;
                break;
            case UserDBException.EMPTY_UID:
                textResId = R.string.error_uid_empty;
                break;
            // UserAuth & UserDB Authentication
            case UserAuthException.ERROR_USER_CREATE: // UserDBException.ERROR_USER_CREATE
                textResId = R.string.error_user_create_failed;
                break;
            case UserAuthException.ERROR_USER_DELETE: // UserDBException.ERROR_USER_DELETE
                textResId = R.string.error_user_delete_failed;
                break;
            case UserAuthException.USER_NOT_EXIST: // UserDBException.USER_NOT_EXIST
                textResId = R.string.error_user_not_exist;
                break;

            // UserAuth exceptions
            case UserAuthException.ERROR_USER_SIGNIN:
                textResId = R.string.error_signin_failed;
                break;

            case UserAuthException.ERROR_USER_AUTHENTICATION:
                textResId = R.string.error_auth_failed;
                break;
            case UserAuthException.SIGNOUT_FAIL:
                textResId = R.string.error_signout_failed;
                break;


            // UserDB exceptions
            case UserDBException.NAME_EMPTY:
                textResId = R.string.error_name_empty;
                break;
            case UserDBException.LASTNAME_EMPTY:
                textResId = R.string.error_lastname_empty;
                break;
            case UserDBException.EMPTY_USER:
                textResId = R.string.error_user_data_not_found;
                break;
            case UserDBException.ERROR_GET_USER:
                textResId = R.string.error_get_user_failed;
                break;
            case UserDBException.TICKET_NOT_ADDED:
                textResId = R.string.error_ticket_add_failed;
                break;
            case UserDBException.EMPTY_TICKET:
                textResId = R.string.error_ticket_not_found;
                break;
            case UserDBException.ERROR_GET_TICKET:
                textResId = R.string.error_get_ticket_failed;
                break;
            case UserDBException.NO_TICKETS_FOUND:
                textResId = R.string.error_no_tickets;
                break;
            case UserDBException.ERROR_GET_TICKETS:
                textResId = R.string.error_get_tickets_failed;
                break;

            default:
                textResId = R.string.error_unknown;
                break;
        }

        return textResId;
    }
}
