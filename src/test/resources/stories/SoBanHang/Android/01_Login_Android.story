Meta:
Scenario: Verify open app

!-- Then step 1

Given application "%{env('browser')}%" is opened
When wait for "1" seconds
Then page "LoginHomePage" is loaded
When mobile field "Input_Phone" is filled with value "0969223206"
When mobile field "Continue_Button" is clicked
When wait for "2" seconds
When mobile field scroll up width OTP screen
!-- When mobile field "OTP_SoBanHang" is clicked
Then mobile field "OTP_SoBanHang" exists
When mobile field "OTP_SoBanHang" value is saved OTP in variable "OTP_code"
When mobile field "Dismiss_OTP_Notifications" is clicked
When wait for "2" seconds
When mobile field is filled with value "%{OTP_code}%" ADB
When mobile field "Continue_Button" is clicked