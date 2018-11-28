package kivi.ugran.com.core;

public class Constants {
    public static final class RegistrationSettings {
        public static final String KEY_REGISTRATION_OTP = "registration_otp";
        public static final String KEY_REGISTRATION_COMPLETED = "registration_completed";
        public static final String KEY_INSTANCE_ID = "instance_id";
        public static final String KEY_CONFIGURATION_ID = "configuration_id";

        public static final String KEY_KIVI_DEVICE_TOKEN = "kivi_device_token";
        public static final String KEY_KIVI_NAME = "kivi_name";
        public static final String KEY_KIVI_EMAIL = "kivi_email";
        public static final String KEY_KIVI_DOB = "kivi_dob";
        public static final String KEY_KIVI_ACCEPTED_TERMS = "kivi_terms_accepted";
    }

    public static final class KiviPreferences {
        public static final String KEY_LOCATION_LAT = "last_location_lat";
        public static final String KEY_LOCATION_LNG = "last_location_lon";
        public static final String KEY_PROFILE_PICTURE = "profile_picture";
        public static final String KEY_KIVI_ONLINE = "kivi_online";

        public static final String KEY_USER_SEARCHED_LOCATION_LAT = "user_searched_location_lat";
        public static final String KEY_USER_SEARCHED_LOCATION_LNG = "user_searched_location_lon";

        public static final int SEARCH_RESULTS_DEFAULT_LIMIT = 30;
        public static final int SEARCH_DISTANCE_DEFAULT_LIMIT = 100;
    }

    public static final class KiviTutorial {
        public static final String KEY_LAUNCHED_BY_MENU = "launched_by_drawer_menu";
        public static int VIEW_PAGER_COUNT_TUTORIAL_AND_REGISTRATION_MODE = 4;
        public static int VIEW_PAGER_COUNT_TUTORIAL_MODE = 3;
    }

    public static final class ChildFragmentTags {
        public static final String HOME_ROOT_FRAGMENT_TAG = "home_root_fragment";
        public static final String REQUEST_ROOT_FRAGMENT_TAG = "request_root_fragment";
        public static final String GALLERY_ROOT_FRAGMENT_TAG = "gallery_root_fragment";
        /**
         *  All other fragment should be named like xxx_CHILD_FRAGMENT_TAG
         */
        public static final String TEST_CHILD_FRAGMENT_TAG = "test_fragment";
    }

    //public static final class ActivityCallBac

    public static String API_KIVI_ENDPOINT = "https://gokivi.com/v1/";
    public static final String API_KIVI_KEY = "8978c11a09ae7daae6a3d7771ee85f12";
    public static final int TIMEOUT_IN_SEC = 15;

    public static final String API_FIREBASE_ENDPOINT = "https://fcm.googleapis.com/";
    public static final String API_FIREBASE_KEY = "";
}
