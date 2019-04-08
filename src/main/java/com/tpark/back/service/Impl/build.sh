#!/usr/bin/env bash
# 1 - schoolID, 2 - service_id, 3 - colorPrimary
# 4 - about_us, 5 - vk, 6 - instagram
# 7 - facebook, 8 - site, 9 - application_name
files_upload_dir="../../resourses/static/$1"
flavors_dir="../white-label/white-label/app/whitelabel/"
scripts_dir="../../../../../../../whitelabelbuilder"
set_tag_value="$scripts_dir/set_xml_tag_value.sh"
app_suffix=whitelabel$1dc

# Init flavor
cd $flavors_dir
cp -a ../../../defaultFlavor/. ./defaultFlavor
echo $app_suffix | tr -d '-' > ./defaultFlavor/applicationIdSuffix
cat ./defaultFlavor/applicationIdSuffix

# Customize flavor
# ~/FinalFight/TPBack/src/main/java/com/tpark/back/service/Impl
cd defaultFlavor/res/values
chmod +x $set_tag_value
$set_tag_value colors.xml color "$2" " name=\"colorPrimary\""
$set_tag_value colors.xml color "$3" " name=\"colorPrimaryDark\""
$set_tag_value strings.xml string "$4" " name=\"app_name\""
$set_tag_value customization.xml string "$8" " name=\"site\""
$set_tag_value customization.xml string "$9" " name=\"application_name\""

# Build
cd ../../../../../
./gradlew assembleDebug -q

# Clean and copy APK
mkdir $files_upload_dir
mv ./app/build/outputs/apk/defaultFlavor/debug/app-defaultFlavor-debug.apk $files_upload_dir/whitelabel.apk
rm -rf ./app/whitelabel/defaultFlavor