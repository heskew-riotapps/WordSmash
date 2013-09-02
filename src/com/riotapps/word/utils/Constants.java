package com.riotapps.word.utils;

public class Constants {

	/**============================================
	 * misc
	 *=============================================*/
	public static final int WORD_DATABASE_VERSION =  3; 
	public static final int DEFAULT_WORD_DATABASE_VERSION =  0; 
	
	public static final String DATABASE_PATH = "/databases/";
 	public static final String MAIN_FONT =  "fonts/mplus_2c_bold.ttf"; //"fonts/Capriola-Regular.ttf"; //"fonts/FullDeceSans1.0.ttf";
 	//public static final String GAME_BOARD_FONT = "fonts/Asap_Bold.ttf";//"fonts/banksia.ttf"; 
 	public static final String GAME_BOARD_FONT = "fonts/Asap_Bold.ttf";//"fonts/banksia.ttf"; //"fonts/Crushed.ttf";//"fonts/MILFCD_B.ttf";  //"fonts/Vegur_B_0.602.otf";
 	public static final String GAME_LETTER_FONT = "fonts/Asap_Bold.ttf"; //"fonts/Vegur_B_0.602.otf"; //Asap_Bold
 	public static final String GAME_LETTER_VALUE_FONT = "fonts/Asap_Regular.ttf"; //"fonts/Vegur_B_0.602.otf"; //Asap_Bold
 	public static final String SCOREBOARD_FONT = "fonts/Asap_Bold.ttf"; //"fonts/banksia.ttf"; "fonts/Crushed.ttf";//"fonts/Vegur_B_0.602.otf"; Asap_Bold
	public static final String SCOREBOARD_BUTTON_FONT = "fonts/Asap_Bold.ttf"; 
	public static final String EMPTY_JSON = "{}";
	public static final String EMPTY_JSON_ARRAY = "[]";
	public static final String DEFAULT_COMPLETED_GAMES_DATE = "6/10/2012";
	public static final String DEFAULT_LAST_ALERT_ACTIVATION_DATE = "6/10/2012";
 	public static final String WN_KEY = "48f31f368d20791114b01067e1d05b68ca177aabdbab4150b";
 	public static final String DRAWABLE_LOCATION = "com.riotapps.word:drawable/";
 	public static final String CONTENT_AREA_BACKGROUND_COLOR = "content_area_background_color";
 	public static final String CONTENT_AREA_BACKGROUND_SELECTED_COLOR = "content_area_background_selected_color";
 	
 	public static final String UNDERSCORE = "_";

 	public static final int SMASHER_BONUS_POINTS = 40;
 	public static final String LAYOUT_SCOPE_WORD = "W";
 	public static final String LAYOUT_SCOPE_LETTER = "L";
 	public static final String IMAGE_CACHE_DIR = "http";
 	public static final int DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS = 4000;
 	public static final long LOCAL_GAME_STORAGE_DURATION_IN_MILLISECONDS = 300000;//5 minutes in milliseconds
 	public static final long LOCAL_GAME_LIST_STORAGE_DURATION_IN_MILLISECONDS = 300000; //5 minutes in milliseconds
 	public static final long WORD_LOADER_SERVICE_SLEEP_BETWEEN_LETTERS = 250; //5 minutes in milliseconds
 	
 	public static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/%s?r=pg&s=75&d=mm"; 
 	public static final String FACEBOOK_IMAGE_URL = "http://graph.facebook.com/%s/picture?r=1&type=square";		
 	public static final int DEFAULT_CONNECTION_TIMEOUT = 12000;
	public static final int DEFAULT_SOCKET_CONNECTION_TIMEOUT = 20000;
	public static final int INITIAL_CONNECTIVITY_THREAD_SLEEP = 1000;
	public static final int MAIN_LANDING_THREAD_SLEEP = 500;
	public static final int REGISTERED_FB_FRIENDS_CACHE_DURATION = 604800000; //a week of milliseconds
	public static final int SPLASH_ACTIVITY_TIMEOUT = 500;
	public static final int SPLASH_DELAY_DURATION = 2000;
	public static final int NETWORK_CONNECTIVITY_CHECK_DURATION = 2000;
	public static final long GAME_LIST_CHECK_START_IN_MILLISECONDS = 300000;// 300000;
	public static final long GAME_LIST_CHECK_INTERVAL_IN_MILLISECONDS = 300000;// = 300000;
	public static final long GAME_SURFACE_CHECK_START_IN_MILLISECONDS = 30000;// 300000;
	public static final long GAME_SURFACE_CHECK_INTERVAL_IN_MILLISECONDS = 120000;// = 2 minutes
	public static final long GAME_SURFACE_CHECK_START_AFTER_RESTART_IN_MILLISECONDS = 18000;// 2 seconds;
	public static final long GAME_SURFACE_INTERSTITIAL_AD_CHECK_IN_MILLISECONDS = 7000;// = 7 seconds;
	public static final long BACKGROUND_GAME_LIST_DELAY_IN_MILLISECONDS = 2000;// = 300000;
	 
	public static final int NUM_LOCAL_COMPLETED_GAMES_TO_STORE = 10;
	 
	/**============================================
	 * tracker constants
	 *=============================================*/
	
	public static final String TRACKER_CATEGORY_STORE = "store";
	public static final String TRACKER_CATEGORY_HOPPER_PEEK = "hopper_peek";
	public static final String TRACKER_CATEGORY_WORD_HINTS = "word_hints";
	public static final String TRACKER_CATEGORY_GAME_LOOKUP = "game_lookup";
	public static final String TRACKER_CATEGORY_GAMEBOARD = "game_surface";
	public static final String TRACKER_CATEGORY_START_GAME = "start_game";
	public static final String TRACKER_CATEGORY_MAIN_LANDING = "main";
	public static final String TRACKER_CATEGORY_ADD_OPPONENTS = "add_opponents";
	public static final String TRACKER_LABEL_START_GAME = "start_game";
	public static final String TRACKER_LABEL_START_GAME_MAX_REACHED = "start_game_max_reached";
	public static final String TRACKER_LABEL_CANCEL_GAME = "cancel_game";
	public static final String TRACKER_LABEL_BADGES = "badges";
	public static final String TRACKER_LABEL_OPTIONS = "options";
	public static final String TRACKER_LABEL_FIND_BY_NICKNAME = "by_nickname";
	public static final String TRACKER_LABEL_FIND_BY_FACEBOOK = "by_facebook";
	public static final String TRACKER_LABEL_FIND_BY_OPPONENT = "by_opponent";
	public static final String TRACKER_ACTION_BUTTON_TAPPED = "button_click";
	public static final String TRACKER_ACTION_WORD_LOOKED_UP = "word_looked_up";
	public static final String TRACKER_ACTION_GAME_STARTED = "game_started";
	public static final String TRACKER_ACTION_GAME_COMPLETED = "game_completed";

	public static final String TRACKER_ACTION_GAME_AUTO_PLAY = "game_opponent_play";
	public static final String TRACKER_ACTION_GAME_AUTO_SKIP = "game_opponent_skip";
	public static final String TRACKER_ACTION_GAME_AUTO_SWAP = "game_opponent_swap";
	public static final String TRACKER_ACTION_GAME_AUTO_CONCEDE = "game_opponent_concede";
	public static final String TRACKER_ACTION_GAME_PLAYER_PLAY = "game_player_play";
	public static final String TRACKER_ACTION_GAME_PLAYER_SKIP = "game_player_skip";
	public static final String TRACKER_ACTION_GAME_PLAYER_SWAP = "game_player_swap";
	public static final String TRACKER_ACTION_GAME_PLAYER_CONCEDE = "game_player_concede";


	public static final String TRACKER_ACTION_GAME_WON = "game_won";
	public static final String TRACKER_ACTION_GAME_LOST = "game_lost";
	public static final String TRACKER_ACTION_GAME_DRAW = "game_draw";
	public static final String TRACKER_ACTION_PURCHASE = "purchase";
	public static final String TRACKER_ACTION_HOPPER_PEEK = "hopper_peek";
	public static final String TRACKER_ACTION_WORD_HINT = "word_hints";
	public static final String TRACKER_LABEL_OPPONENT = "opponent";

	public static final String TRACKER_LABEL_OPPONENT_WITH_ID = "opponent_%s";

	public static final String TRACKER_LABEL_OPPONENT_WITH_ID_LOSS = "opponent_%s_loss";
	public static final String TRACKER_LABEL_OPPONENT_WITH_ID_WON = "opponent_%s_won";
	public static final String TRACKER_LABEL_OPPONENT_WITH_ID_DRAW = "opponent_%s_draw";
	public static final String TRACKER_LABEL_RECALL = "recall_letters";
	public static final String TRACKER_LABEL_SHUFFLE = "shuffle";
	public static final String TRACKER_LABEL_WORD = "word";
	public static final String TRACKER_LABEL_HOPPER_PEEK_GO_TO_STORE = "hopper_peek_go_to_store";
	public static final String TRACKER_LABEL_HOPPER_PEEK_DECLINE_STORE = "hopper_peek_decline_store";
	public static final String TRACKER_LABEL_HOPPER_PEEK_CLOSE = "hopper_peek_close";

	public static final String TRACKER_LABEL_PLAY_INITIAL = "play_initial";
	public static final String TRACKER_LABEL_PLAY_CANCEL = "play_cancel";
	public static final String TRACKER_LABEL_PLAY_DISMISS = "play_dismiss";
	public static final String TRACKER_LABEL_PLAY_OK = "play_ok";
	public static final String TRACKER_LABEL_SKIP_INITIAL = "skip_initial";
	public static final String TRACKER_LABEL_SKIP_CANCEL = "skip_cancel";
	public static final String TRACKER_LABEL_SKIP_DISMISS = "skip_dismiss";
	public static final String TRACKER_LABEL_SKIP_OK = "skip_ok";
	public static final String TRACKER_LABEL_SWAP_INITIAL = "swap_initial";
	public static final String TRACKER_LABEL_SWAP_CANCEL = "swap_cancel";
	public static final String TRACKER_LABEL_SWAP_DISMISS = "swap_dismiss";
	public static final String TRACKER_LABEL_SWAP_DISMISS_OUTSIDE = "swap_dismiss_outside";
	public static final String TRACKER_LABEL_SWAP_NONE_CLICKED = "swap_none_clicked";
	public static final String TRACKER_LABEL_SWAP_OK = "swap_ok";
	public static final String TRACKER_LABEL_DECLINE_INITIAL = "decline_initial";
	public static final String TRACKER_LABEL_DECLINE_CANCEL = "decline_cancel";
	public static final String TRACKER_LABEL_DECLINE_DISMISS = "decline_dismiss";
	public static final String TRACKER_LABEL_DECLINE_OK = "decline_ok";
	public static final String TRACKER_LABEL_RESIGN_INITIAL = "resign_initial";
	public static final String TRACKER_LABEL_RESIGN_CANCEL = "resign_cancel";
	public static final String TRACKER_LABEL_RESIGN_DISMISS = "resign_dismiss";
	public static final String TRACKER_LABEL_RESIGN_OK = "resign_ok";
	public static final String TRACKER_LABEL_REMATCH_INITIAL = "rematch_initial";
	public static final String TRACKER_LABEL_REMATCH_CANCEL = "rematch_cancel";
	public static final String TRACKER_LABEL_REMATCH_DISMISS = "rematch_dismiss";
	public static final String TRACKER_LABEL_REMATCH_OK = "rematch_ok";
	public static final String TRACKER_LABEL_CANCEL_INITIAL = "cancel_initial";
	public static final String TRACKER_LABEL_CANCEL_CANCEL = "cancel_cancel";
	public static final String TRACKER_LABEL_CANCEL_DISMISS = "cancel_dismiss";
	public static final String TRACKER_LABEL_CANCEL_OK = "cancel_ok";
	public static final String TRACKER_LABEL_START_GAME_OK = "start_game_ok";
	public static final String TRACKER_LABEL_START_GAME_CANCEL = "start_game_cancel";
	public static final String TRACKER_LABEL_START_GAME_DISMISS = "start_game_dismiss";
	 public static final String TRACKER_LABEL_PLAY_WITH_ERRORS = "play_with_errors_%s";
	public static final long TRACKER_DEFAULT_OPTION_VALUE = 0;
	public static final long TRACKER_SINGLE_VALUE = 1;
	
	/**============================================
	 * error codes. can change to eum later
	 *=============================================*/
	public static final int ERROR_CODE_OVERLAY_PREVIOUS_LETTER = 1;
	public static final int ERROR_CODE_TOO_FEW_LETTERS = 2;
	public static final int ERROR_CODE_INVALID_START_POSITION = 3;
	public static final int ERROR_CODE_INVALID_AXIS = 4;
	public static final int ERROR_CODE_INVALID_GAPS = 5;
	public static final int ERROR_CODE_INVALID_PLACEMENT = 6;
	public static final int ERROR_CODE_INVALID_WORDS = 7;
 

	/**============================================
	 * intent extras
	 *=============================================*/
	public static final String EXTRA_GAME_LIST_PREFETCHED = "isGameListFetched";
 	public static final String EXTRA_GAME = "com.riotapps.word.hooks.Game";
	public static final String EXTRA_GAME_ID = "gameId";
	public static final String EXTRA_FROM_COMPLETED_GAME_LIST = "from_cg";
	public static final String EXTRA_PLAYER = "com.riotapps.word.hooks.Player";
	public static final String EXTRA_GCM_GAME_ID = "id";
	public static final String EXTRA_GCM_MESSAGE = "msg";
	public static final String NOTIFICATION_ID = "___com.riotapps.word";
	public static final String EXTRA_IS_GAME_UPDATED = "game_u";
	public static final String EXTRA_WORD_LOOKUP = "_wlu";
	public static final String INTENT_GCM_MESSAGE_RECEIVED = "GCM_mr";
	public static final String INTENT_GAME_LIST_REFRESHED = "GL_rf";
	public static final String INTENT_GAME_LIST_REFRESHED_TO_BRIDGE = "GL_rf_b";
	public static final String EXTRA_PLAYER_AUTH_RESULT = "ex_p_a_r";
	public static final String EXTRA_PLAYER_TOKEN = "ex_token";
	public static final String EXTRA_PLAYER_COMPLETED_DATE = "ex_c_d";
	public static final String EXTRA_PLAYER_LAST_ALERT_ACTIVATION_DATE = "ex_l_a_a_d";
	public static final String EXTRA_PLAYER_GCM_RID = "ex_rid";
	 	
 	/**============================================
	 * storage
	 *=============================================*/
 	public static final String SHARED_PREFERENCE_PREFIX = "com.riotapps.word.utils_";
 	public static final String USER_PREFS = "user_";
 	public static final String GAME_PREFS = "game_";
 	public static final String OPPONENT_PREFS = "opponent_";
 	public static final String PURCHASE_PREFS = "purchase_";
 	public static final String USER_PREFS_USER_ID = "user_uid";
 	public static final String USER_PREFS_AUTH_TOKEN = "user_at";
 	public static final String USER_PREFS_EMAIL = "user_em";
 	public static final String USER_PREFS_PWD = "user_pw";
 	public static final String USER_PREFS_ACTIVE_GAMES = "a_games";
 	public static final String USER_PREFS_OPPONENTS = "_opps";
 	public static final String USER_PREFS_OPPONENT_GROUPS = "_opp_groups";
	public static final String USER_PREFS_COMPLETED_GAMES = "c_games";
	public static final String USER_PREFS_PLAYER_JSON = "player_json";
	public static final String USER_PREFS_WORD_DATABASE_VERSION = "wdb_v";
	public static final String USER_PREFS_FREE_REMAINING_USES_HOPPER_PEEK = "fu_hp";
	public static final String USER_PREFS_FREE_REMAINING_USES_WORD_HINTS = "fu_wh";
	public static final String USER_PREFS_FREE_REMAINING_USES_WORD_DEFINITION = "fu_wd";
 	public static final String USER_PREFS_GAME_JSON = "game_json_%s";
 	public static final String USER_PREFS_GAME_LIST_JSON = "game_list_";
 	public static final String USER_PREFS_FRIENDS_JSON = "friends_json";
 	public static final String USER_PREFS_FRIENDS_LAST_REGISTERED_CHECK_TIME = "friends_reg_check";
 	public static final String USER_PREFS_PLAYER_CHECK_TIME = "player_check";
 	public static final String USER_PREFS_GAME_LIST_CHECK_TIME = "game_list_check";
 	public static final String USER_PREFS_GCM_REGISTRATION_ID = "gcm";
 	public static final String PURCHASE_PREFS_ITEM = "p_item_%s";
 	public static final String OPPONENT_PREFS_RECORD = "o_rec_%s";
 	public static final String OPPONENT_PREFS_GROUP_ACTIVATED = "o_rec_%s";
	public static final String USER_PREFS_GAME_ALERT_CHECK = "game_alert_check_%s";
	public static final String USER_PREFS_FIRST_TIME_GAME_SURFACE_ALERT_CHECK = "f_t_g_s";
	public static final String USER_PREFS_FIRST_TIME_MAIN_ALERT_CHECK = "f_t_M";
	public static final String USER_PREFS_ALERT_CHECK = "alert_check_";
	public static final String USER_PREFS_ALERT_CHECK_DATE = "alert_check_date";
	public static final String USER_PREFS_GAME_CHAT_CHECK = "game_chat_check_%s";
 	public static final String USER_PREFS_LATEST_COMPLETED_GAME_DATE = "cg_date";
 	public static final String GAME_STATE = "game_state";
 	public static final String FB_TOKEN = "fb_token";
	public static final String FB_TOKEN_EXPIRES = "fb_token_expires";
 	
	public static final int FREE_USES_WORD_HINTS = 4;
	public static final int FREE_USES_HOPPER_PEEK = 4;
	public static final int FREE_USES_WORD_DEFINITION = 2;
	
	public static final String IAB_1 = "MIIBIjANBgkqhkiG9";
	public static final String IAB_2 = "w0BAQEFAAOCAQ8AMIIBCgKCAQ";
	public static final String IAB_3 = "EAjsNPfIZBLfO68j+8rjkMwYi5694";
	public static final String IAB_4 = "YApXBOcPNfg28djJm07uAcTcqHs";
	public static final String IAB_5 = "TbuRL/nfcVe1ztMyBuzbuzHwZeHSbrV";
	public static final String IAB_6 = "u+f5UWCwMHNGi8lmyCDSnaw7yUepzl";
	public static final String IAB_7 = "E8ZcdEfs1ljznksOLgi1WjzuiiOtWSt";
	public static final String IAB_8 = "xgoT61MpV6JDBl1oS7neDRy9Tb7t5faLe/dDvlw3";
	public static final String IAB_9 = "vCeWZYL1qH7mzIaXZIyLGjNdpyv7yEC+";
	public static final String IAB_10 = "MdPTxn2CjWv9bkY09cFysUDFcFMPC/2J";
	public static final String IAB_11 = "rvn3fPeTVPpfobpzbHCEq/RCCtnxy";
	public static final String IAB_12 = "rfUxLNFtJ7TbjaqgIk13Ujl1dB8I50nwOMfR";
	public static final String IAB_13 = "jWPyYs9aA0e6Y50ozjlkKZqpVxQIDAQAB";
	
	

//	public static final String SKU_HOPPER_PEEK = "riotapps_wordsmash_hopper_peek.a";
	/**=============================================
	 * creatingXMLParser for class
	 *==============================================*/
	//public static final int XML_PARSER_FOR_USERS = 200;
	//public static final int XML_PARSER_FOR_ERRORS = 201;
 
	/**=============================================
	 * mode of page
	 *==============================================*/
	//public static final String MODE_OF_PAGE = "MODE_OF_PAGE";
	///public static final int PAGE_ADD = 300;
	//public static final int PAGE_EDIT = 301; 	   

	/**=============================================
	 * ad networks 
	 *==============================================*/  
	public static final boolean HIDE_ALL_ADS = false; 
	public static final boolean INTERSTITIAL_ADMOB = false; 
	public static final boolean INTERSTITIAL_CHARTBOOST = true;
	public static final boolean INTERSTITIAL_REVMOB = true;
	public static final boolean BANNERS_REVMOB = false;
	public static final boolean BANNERS_ADMOB = true;
	

	/**=============================================
	 * the web 
	 *==============================================*/  
	public static final String REST_URL_SITE = "http://smash.riotapps.com/en/";        
	//public static final String REST_URL_SITE = "http://10.0.2.2:3000/en/";      
	public static final String FACEBOOK_API_ID = "314938401925933"; 
	public static final String REST_CREATE_PLAYER_URL = REST_URL_SITE + "players.json";
	public static final String REST_GET_PLAYER_URL = REST_URL_SITE + "players/%s.json";
	public static final String REST_FIND_PLAYER_BY_NICKNAME = REST_URL_SITE + "players/find.json?n_n=%s";
	public static final String REST_AUTHENTICATE_PLAYER = REST_URL_SITE + "players/auth.json";
	public static final String REST_AUTHENTICATE_PLAYER_BY_TOKEN = REST_URL_SITE + "players/auth_with_payload.json";
	public static final String REST_AUTHENTICATE_PLAYER_BY_TOKEN_WITH_GAME = REST_URL_SITE + "players/auth_with_game.json";
	public static final String REST_GET_PLAYER_BY_TOKEN = REST_URL_SITE + "players/get_with_payload.json";
	public static final String REST_GAME_LIST_CHECK = REST_URL_SITE + "players/game_list_refresh.json";
	public static final String REST_PLAYER_LOGOUT = REST_URL_SITE + "players/log_out.json";
	public static final String REST_PLAYER_CHANGE_PASSWORD = REST_URL_SITE + "players/change_password.json";
	public static final String REST_PLAYER_UPDATE_ACCOUNT = REST_URL_SITE + "players/update_account.json";
	public static final String REST_PLAYER_UPDATE_FB_ACCOUNT = REST_URL_SITE + "players/update_fb_account.json";
	public static final String REST_PLAYER_GET_GAMES = REST_URL_SITE + "games/get_active_games.json";
	public static final String REST_PLAYER_GCM_REGISTER = REST_URL_SITE + "games/gcm_register.json";
	public static final String REST_CREATE_GAME_URL = REST_URL_SITE + "/games/create_.json";
	public static final String REST_GET_GAME_URL = REST_URL_SITE + "games/get_.json";
	public static final String REST_GAME_REFRESH_URL = REST_URL_SITE + "games/refresh_.json";
	public static final String REST_FIND_REGISTERED_FB_FRIENDS = REST_URL_SITE + "players/find_all_by_fb";
	public static final String REST_GAME_CANCEL = REST_URL_SITE + "games/cancel";
	public static final String REST_GAME_PLAY = REST_URL_SITE + "games/play_.json";
	public static final String REST_GAME_SKIP = REST_URL_SITE + "games/skip_.json";
	public static final String REST_GAME_SWAP = REST_URL_SITE + "games/swap_.json";
	public static final String REST_GAME_CHAT = REST_URL_SITE + "games/chat_.json";
	public static final String REST_GAME_RESIGN = REST_URL_SITE + "games/resign";
	public static final String REST_GAME_DECLINE = REST_URL_SITE + "games/decline";
	public static final String GRAVATAR_SITE_URL = "http://en.gravatar.com";
	public static final String SUPPORT_SITE_URL = "http://www.getsatisfaction.com/wordsmash";
	public static final String WORDNIK_WORD_URL = "http://www.wordnik.com/word/%s";
	public static final String CODICODE_URL = "http://www.codicode.com/";
	
	/**=============================================
	 * rails
	 *==============================================*/
	public static final String REST_METHOD = "_method";
	public static final String PUT_VERB = "PUT";
	public static final String DELETE_VERB = "DELETE";
	
	
	/**=============================================
	 * badge drawables
	 *==============================================*/
	public static final String BADGE_INVITED = "badge_invited";
	public static final String BADGE_0 = "badge_0";
	public static final String BADGE_1_4 = "badge_1_4";
	public static final String BADGE_5_9 = "badge_5_9";
	public static final String BADGE_10_14 = "badge_10_14";
	public static final String BADGE_15_19 = "badge_15_19";
	public static final String BADGE_20_24 = "badge_20_24";
	public static final String BADGE_25_29 = "badge_25_29";
	public static final String BADGE_30_39 = "badge_30_39";
	public static final String BADGE_40_49 = "badge_40_49";
	public static final String BADGE_50_59 = "badge_50_59";
	public static final String BADGE_60_69 = "badge_60_69";
	public static final String BADGE_70_79 = "badge_70_79";
	public static final String BADGE_80_89 = "badge_80_89";
    public static final String BADGE_90_99 = "badge_90_99";
	public static final String BADGE_100_124 = "badge_100_124";
	public static final String BADGE_125_149 = "badge_125_149";
	public static final String BADGE_150_174 = "badge_150_174";
	public static final String BADGE_175_199 = "badge_175_199";
	public static final String BADGE_200_224 = "badge_200_224";
	public static final String BADGE_225_249 = "badge_225_249";
	public static final String BADGE_250_274 = "badge_250_274";
	public static final String BADGE_275_299 = "badge_275_299";
	public static final String BADGE_300_349 = "badge_300_349";
	public static final String BADGE_350_399 = "badge_350_399";
	public static final String BADGE_400_449 = "badge_400_449";
	public static final String BADGE_450_499 = "badge_450_499";
	public static final String BADGE_500_599 = "badge_500_599";
	public static final String BADGE_600_699 = "badge_600_699";
	public static final String BADGE_700_799 = "badge_700_799";
	public static final String BADGE_800_899 = "badge_800_899";
	public static final String BADGE_900_999 = "badge_900_999";
	public static final String BADGE_1000_1249 = "badge_1000_1249";
	public static final String BADGE_1250_1499 = "badge_1250_1499";
	public static final String BADGE_1500_1749 = "badge_1500_1749";
	public static final String BADGE_1750_1999 = "badge_1750_1999";
	public static final String BADGE_2000_2499 = "badge_2000_2449";
	public static final String BADGE_2500_2999 = "badge_2500_2999";
	public static final String BADGE_3000_3999 = "badge_3000_3999";
	public static final String BADGE_4000_4999 = "badge_4000_4999";
	public static final String BADGE_5000 = "badge_5000";
	
	
	public static final String EMPTY_STRING = "";
	public static final String DEFAULT_PLAYER_ID = "111";
	
	/**=============================================
	 * opponent image modes 
	 *==============================================*/
	public static final String OPPONENT_IMAGE_MODE_MAIN_X_LARGE = "main_xl";
	public static final String OPPONENT_IMAGE_MODE_MAIN = "main";
	public static final String OPPONENT_IMAGE_MODE_SMALL = "small";

	
	/**=============================================
	 * opponent image modes 
	 *==============================================*/
    public static final int MENU_COMPLETED_GAMES = 1;
    public static final int MENU_ABOUT = 2;
    public static final int MENU_BADGES = 3;
    public static final int MENU_RULES = 4;
    public static final int MENU_STORE = 5;
    public static final int MENU_SHARE = 6;
    
    /**=============================================
	 * store skus 
	 *==============================================*/
	public static final String SKU_GOOGLE_PLAY_HOPPER_PEEK = "riotapps_wordsmash_hopper_peek.a";
	public static final String SKU_GOOGLE_PLAY_PREMIUM_UPGRADE = "riotapps_wordsmash_premium_upgrade.a";
	public static final String SKU_GOOGLE_PLAY_WORD_DEFINITIONS = "riotapps_wordsmash_word_definitions.a";
	public static final String SKU_GOOGLE_PLAY_HIDE_INTERSTITIAL = "riotapps_wordsmash_hide_interstitial.a";
	public static final String SKU_GOOGLE_PLAY_WORD_HINTS = "riotapps_wordsmash_word_hints.a";
	
	/**=============================================
	 * return code 
	 *==============================================*/
	public static final int RETURN_CODE_HOPPER_PEEK_CLOSE = 100;
	public static final int RETURN_CODE_CUSTOM_DIALOG_OK_CLICKED = 101;
	public static final int RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLICKED = 102;
	public static final int RETURN_CODE_CUSTOM_DIALOG_CLOSE_CLICKED = 103;
	public static final int RETURN_CODE_CUSTOM_DIALOG_SKIP_CLICKED = 104;
	public static final int RETURN_CODE_CUSTOM_DIALOG_SKIP_CANCEL_CLICKED = 105;
	public static final int RETURN_CODE_CUSTOM_DIALOG_PLAY_CLICKED = 106;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_PLAY_CANCEL_CLICKED = 107;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_SKIP_CLOSE_CLICKED = 108;
	public static final int RETURN_CODE_CUSTOM_DIALOG_PLAY_CLOSE_CLICKED = 109;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_CANCEL_OK_CLICKED = 110;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_CANCEL_CLOSE_CLICKED = 111;
	public static final int RETURN_CODE_CUSTOM_DIALOG_CANCEL_CANCEL_CLICKED = 112;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_RESIGN_OK_CLICKED = 113;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_RESIGN_CLOSE_CLICKED = 114;
	public static final int RETURN_CODE_CUSTOM_DIALOG_RESIGN_CANCEL_CLICKED = 115;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_OK_CLICKED = 116;	
	public static final int RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CLOSE_CLICKED = 117;
	public static final int RETURN_CODE_CUSTOM_DIALOG_GAME_CONFIRMATION_CANCEL_CLICKED = 118;	

	
	/**=============================================
	 * directions 
	 *==============================================*/
	public static final String DIRECTION_ABOVE = "above";
	public static final String DIRECTION_BELOW = "below";
	public static final String DIRECTION_LEFT = "left";
	public static final String DIRECTION_RIGHT = "right";
	public static final String AXIS_VERTICAL = "vertical";
	public static final String AXIS_HORIZONTAL = "horizontal";	
	
	/**=============================================
	 * coin flip 
	 *==============================================*/
	public static final int COIN_FLIP_HEADS = 0;
	public static final int COIN_FLIP_TAILS = 1;
	
	/**=============================================
	 * skill level 
	 *==============================================*/
	public static final int SKILL_LEVEL_NOVICE = 1;
	public static final int SKILL_LEVEL_AMATEUR = 2;
	public static final int SKILL_LEVEL_SEMI_PRO = 3;	
	public static final int SKILL_LEVEL_PROFESSIONAL = 4;
	public static final int SKILL_LEVEL_EXPERT = 5;
	public static final int SKILL_LEVEL_MASTER = 6;
	
	public static final int MAX_WORD_MATCHES_WORDS_TO_START_GAME = 20;
	public static final int MAX_WORD_MATCHES_ACROSS = 20;
	public static final int MAX_WORD_MATCHES_PERPENDICULAR = 20;
	public static final int MAX_WORD_MATCHES_EXTENSIONS = 15;
	public static final int MAX_WORD_MATCHES_OVERLAYS = 20;
	
	public static final boolean FIND_MATCHES_ACROSS = true;
	public static final boolean FIND_MATCHES_PERPENDICULAR = true;
	public static final boolean FIND_MATCHES_EXTENSIONS = true;
	public static final boolean FIND_MATCHES_OVERLAY = true;

	public static final int AUTOPLAY_MATCH_ACROSS = 20;
	public static final int AUTOPLAY_MATCH_PERPENDICULAR = 20; 
	public static final int AUTOPLAY_MATCH_EXTENSIONS = 20;
	public static final int AUTOPLAY_MATCH_OVERLAYS = 20;
	
	public static final String AUTOPLAY_DISCOVERY_ACROSS = "_ACROSS";
	public static final String AUTOPLAY_DISCOVERY_PERPENDICULAR = "_PERPENDICULAR"; 
	public static final String AUTOPLAY_DISCOVERY_EXTENSIONS = "_EXTENSIONS";

	
	public static final int TILE_POSITION_ON_BORDER = 255;
	
	public static final int SCORE_DIFFERENCE_TRIGGER_EXPERT = 80;
	public static final int SCORE_DIFFERENCE_TRIGGER_PROFESSIONAL = 100;
	public static final int SCORE_DIFFERENCE_TRIGGER_SEMI_PRO = 120;
	
	public static final int SCREEN_SIZE_LARGE = 3;	
	public static final int SCREEN_SIZE_XLARGE = 4;
	public static final int MAX_AUTOPLAY_MILLISECONDS = 8000;
	
}//end class Constants
 
