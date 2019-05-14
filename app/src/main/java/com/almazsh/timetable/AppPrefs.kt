package com.almazsh.timetable

import com.chibatching.kotpref.KotprefModel

object AppPrefs : KotprefModel() {
    // TODO prefs file name
//    override val kotprefName: String = "com.almazsh.timetable_preferences"
//    override val kotprefName: String = "main_prefs_1"
    override val kotprefName: String = BuildConfig.APPLICATION_ID + "_preferences"

    var groupName by nullableStringPref()

//    var savedResponse: SavedResponse? by gsonNullablePref()

    var filterModeAnswered by booleanPref(false)

    var filterElectiveCourses by booleanPref(key = R.string.key_filter_elective_courses)


    var hideDayStart by booleanPref(key = R.string.key_hide_day_start)

    var hideDayEnd by booleanPref(key = R.string.key_hide_day_end)

    var hideAllEmptyLessons by booleanPref(key = R.string.key_hide_all_empty_lessons)


    var serverBaseUrl by stringPref(key = R.string.key_server_base_url)


//    var serializedSelectedCourses by nullableStringPref()
//
//    var cachedSelectedCourses: List<Pair<String, Boolean>>? = null
//
//    var selectedCourses: List<Pair<String, Boolean>>
//        get() {
//            if (cachedSelectedCourses == null) {
//                cachedSelectedCourses =
//                    Gson().fromJson(serializedSelectedCourses, List<Pair<String, Boolean>>::class)
//            }
//        }
//        set(value) {
//
//        }

}
