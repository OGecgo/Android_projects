package com.example.unipicityvibe.UI.Exception;

import com.example.unipicityvibe.Service.Exception.AuthServiceException;
import com.example.unipicityvibe.Data.Exception.TicketDBException;
import com.example.unipicityvibe.Data.Exception.UserAuthException;
import com.example.unipicityvibe.Data.Exception.UserDBException;
import com.example.unipicityvibe.R;

public final class ExceptionToMessage {

    public static int AuthExceptionToTextId(String errorText){
        int textResId;
        switch (errorText){
            // AuthService exceptions
            case AuthServiceException.USER_LOGGED_IN:
                textResId = R.string.error_user_already_logged_in;
                break;
            case AuthServiceException.USER_NOT_LOGGED_IN:
                textResId = R.string.error_user_not_logged_in;
                break;

            // UserAuth & UserDB & TicketDB Validation
            case UserAuthException.EMPTY_EMAIL: // UserDBException & TicketDBException.
                textResId = R.string.error_email_empty;
                break;
            case UserAuthException.EMPTY_PASSWORD: // UserDBException & TicketDBException
                textResId = R.string.error_password_empty;
                break;
            case UserAuthException.EMAIL_VALIDATION_ERROR: // UserDBException & TicketDBException
                textResId = R.string.error_email_invalid;
                break;
            case UserDBException.EMPTY_UID:
                textResId = R.string.error_uid_empty;
                break;
            // UserAuth & UserDB Authentication
            case UserAuthException.ERROR_USER_CREATE: // UserDBException
                textResId = R.string.error_user_create_failed;
                break;
            case UserAuthException.ERROR_USER_DELETE: // UserDBException
                textResId = R.string.error_user_delete_failed;
                break;
            case UserAuthException.USER_NOT_EXIST: // UserDBException
                textResId = R.string.error_user_not_exist;
                break;
            case UserAuthException.ERROR_USER_AUTHENTICATION: // UserDBException
                textResId = R.string.error_auth_failed;
                break;

            // UserAuth exceptions
            case UserAuthException.ERROR_USER_SIGNIN:
                textResId = R.string.error_signin_failed;
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

            // TicketDB
            case TicketDBException.TICKET_NOT_ADDED:
                textResId = R.string.error_ticket_add_failed;
                break;
            case TicketDBException.EMPTY_TICKET:
                textResId = R.string.error_ticket_not_found;
                break;
            case TicketDBException.ERROR_GET_TICKET:
                textResId = R.string.error_get_ticket_failed;
                break;
            case TicketDBException.NO_TICKETS_FOUND:
                textResId = R.string.error_no_tickets;
                break;
            case TicketDBException.ERROR_GET_TICKETS:
                textResId = R.string.error_get_tickets_failed;
                break;

            // Event DB

            default:
                textResId = R.string.error_unknown;
                break;
        }

        return textResId;
    }
}
