rootProject.name = "hashutil"

include("generator")
include("api")
pluginManagement {
    includeBuild("api-generator")
}
