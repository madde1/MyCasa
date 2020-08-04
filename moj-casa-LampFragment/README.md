# Build:

## Signing:
- Key Alias:
jonasvo
- Key Password:
MOJPasswordWithUpperCaseH
- Key Store File (example, may differ/needs to be generated):
C:/_Jonas/Android/keystores/android.jks
- Key Store Password:
MOJPasswordWithLowerCaseH
- First and Last Name:
Jonas Olsson
- Organizational Unit:
Mobile Development
- Organization
MOJ


## ADB commands
- To list devices:
adb devices
- Build .apk and install to selected device:
adb -s <device> install <app_name.apk>

--------------------------------------------------