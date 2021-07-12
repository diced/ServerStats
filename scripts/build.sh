# vars
version=$(cat gradle.properties | awk '/mod_version/ && sub(/^.{14}/,"",$0)')
projects=("bukkit" "bungee" "fabric" "velocity")
project_file_names=("ServerStats-Bukkit" "ServerStats-Bungee" "ServerStats-Fabric" "ServerStats-Velocity")

[ -d "./builds" ] && rm -rf builds

mkdir builds


for ((i = 0; i < ${#projects[@]}; ++i )); do
  project=${projects[i]}
  project_file_name="${project_file_names[i]}-$version.jar"

  echo "./$project/build/libs/$project_file_name"

  echo -e "\n\n-------- Building - $project --------\n\n"
  ./gradlew ":$project:build"

  cp "./$project/build/libs/$project_file_name" ./builds
done
