#!/usr/bin/env bash
# 1 - schoolID, 2 - mainColor, 3 - secondaryColor
# 4 - name, 5 - language, 6-icon
files_upload_dir="/home/applications"
flavors_dir="../Lingvomake/app/src/main/res/values/"
android_dir="../Lingvomake"
back_dir="../TPBack"
apk_dir="../Lingvomake/app/build/outputs/apk/debug"
scripts_dir="src/main/resources/scripts"
icon_dir="../Lingvomake/app/src/main/res/mipmap-xxxhdpi/ic_launcher.jpg"
image_dir="uploads/school"
set_tag_value="$scripts_dir/set_xml_tag_value.sh"
app_suffix=whitelabel$1dc
echo "$4"

chmod +x "$set_tag_value"

source "$set_tag_value" "$flavors_dir"strings.xml string app_name "$4"
source "$set_tag_value" "$flavors_dir"colors.xml color colorPrimary "$2"
source "$set_tag_value" "$flavors_dir"colors.xml color colorPrimaryDark "$3"
source "$set_tag_value" "$flavors_dir"strings.xml string logo "$4"
ls
cp -f "$image_dir/$6" "$icon_dir"
## Build
cd $android_dir
./gradlew assembleDebug -q
cd $back_dir

mv $apk_dir/app-debug.apk $files_upload_dir/$1.apk

#
## Clean and copy APK
#mkdir $files_upload_dir
#mv ./app/build/outputs/apk/defaultFlavor/debug/app-defaultFlavor-debug.apk $files_upload_dir/whitelabel.apk
#rm -rf ./app/whitelabel/defaultFlavor
