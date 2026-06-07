# Expense Recording

Expense Recording is a simple Android application for logging money records for a selected date and viewing basic statistics for either the current week or the current month

The app lets the user:

- enter a day, month, and year
- save a monetary record for that date
- view the total, average, minimum, and maximum record values
- switch the statistics view between week and month

Records and date information are stored locally with SharedPreferences, so the app can restore the last saved data after reopening

## Project Details

- Android Studio: 2025.3.4.7-1
- Language: Java 11
- Minimum SDK: 33
- Target SDK: 36
- Build system: Gradle

## How It Works

1. Set the current date using the date fields
2. Enter a money value in the record field
3. Save the record
4. Review the calculated statistics for the selected period

The app validates the entered date before saving data. If no valid date is stored yet, the date label shows that no date is available

## Notes

- Weekly statistics are calculated from the current seven-day range used by the app logic
- Monthly statistics are calculated from the saved month and year
- The assignment brief is available in [doc/AndroidPLH2526_ergasia1.pdf](doc/AndroidPLH2526_ergasia1.pdf)

## Source Structure

- `app/src/main/java`: application code
- `app/src/main/res`: layouts, strings, and resources
- `doc`: assignment documentation and related files