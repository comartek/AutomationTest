Meta:
Scenario: Verify login successfuly fỏ iPhone

!-- Then step 1

Given application "%{env('browser')}%" is opened
When wait for "2" seconds
Then page "LoginiPhonePage" is loaded
When mobile field is senkeys with value "0969223206" ADB
When wait for "2" seconds
When mobile tap coordinates "210" and "447"
When mobile field scroll up width OTP screen
When wait for "4" seconds
When mobile field "OTP_NotificationCell" value is saved OTP in variable "OTP_Notification_iPhone" for iPhone
When mobile field scroll down width Notification screen
When wait for "2" seconds
When mobile field is senkeys with value "%{OTP_Notification_iPhone}%" ADB
Then page "HomePageiPhone" is loaded
Then mobile field "Quan_Ly_Button" exists

