# vars
version=$(cat gradle.properties | awk '/mod_version/ && sub(/^.{12}/,"",$0)')
projects=("bukkit" "bungee" "fabric")
project_file_names=("ServerStats-Bukkit" "ServerStats-Bungee" "ServerStats-Fabric")

[ -d "./builds" ] && rm -rf builds

mkdir builds


for ((i = 0; i < ${#projects[@]}; ++i )); do
  export project=${projects[i]}
  export project_file_name="${project_file_names[i]}-$version.jar"

  echo "Building - $project"
  ./gradlew ":$project:build"

  cp "./$project/build/libs/$project_file_name" ./builds
done
