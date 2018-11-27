package ru.laundromat.washer.bluetoothchat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.HapticFeedbackConstants;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.stericson.RootTools.RootTools;
import com.stericson.RootTools.execution.CommandCapture;
import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PaymentParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ru.laundromat.washer.common.logger.Log;
import ru.yandex.money.android.PaymentActivity;


/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    //Текущее время
    Calendar now = Calendar.getInstance( TimeZone.getDefault() );
    int hour = now.get(Calendar.HOUR_OF_DAY);
    int hour1= now.get(Calendar.HOUR_OF_DAY);
    int hour2= now.get(Calendar.HOUR_OF_DAY);

    private DevicePolicyManager dpm;
    private ComponentName deviceAdmin;
    private static float  result_credit;
    private static final String CLIENT_ID = "4599FFD7F23224426BAF02131DB39EEC04A7AFD7A6E87C39056FFF617926F690";
    private static final String HOST = "https://money.yandex.ru";
    private static final int REQUEST_CODE = 4;

    private static final String TAG = "BluetoothChatFragment";
    //  public MediaPlayer mMediaPlayer;
    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;
    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;
    private int faaaa = 0;
    private int kluchik=0;
    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private ru.laundromat.washer.bluetoothchat.BluetoothChatService mChatService = null;

    // ----------------------------  my code ------------------------------------

    private static final SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss.SSS");
    // All Object Views
    private AnimationDrawable mAnimationDrawable = null;
    private final static int DURATION = 1000;
    private float mStartY, mStartX;
    private LinearLayout
            machine1, machine2, machine3, machine4, machine5, machine6, machine7, machine8, machine9, machine10, machine11, machine12,
            machine1_settings, machine2_settings, machine3_settings, machine4_settings, machine5_settings, machine6_settings,
            machine7_settings, machine8_settings, machine9_settings, machine10_settings, machine11_settings, machine12_settings;
    private ListView
            StatisticWash1, StatisticWash2, StatisticWash3, StatisticWash4, StatisticWash5, StatisticWash6,
            StatisticWash7, StatisticWash8, StatisticWash9, StatisticWash10, StatisticWash11, StatisticWash12;
    private ToggleButton btn_settings_bluetooth_adapter;
    private HorizontalScrollView horizontalScrollView, horizontalScrollView2;
    private ImageButton
            img_slide_back, img_slide, btn_cancel, btn_numbers_select, btn_keyboard_choose_prog,mButton,
            btn_keyboard_sms, btn_keyboard_temp, btn_keyboard_pay, btn_keyboard_creditcard;
    private Button
            start, // button Smart keyboard
            wash1_go, wash2_go, wash3_go, wash4_go, wash5_go, wash6_go, wash7_go, wash8_go, wash9_go, wash10_go, wash11_go, wash12_go,
            choose1mode, choose2mode, choose3mode, choose4mode, choose5mode, choose6mode,
            btn1_settings_choose, btn2_settings_choose, btn3_settings_choose,
            btn4_settings_choose, btn5_settings_choose, btn6_settings_choose,
    // WASHER SETTINGS
    num1_settings, num2_settings, num3_settings, num4_settings, num5_settings, num6_settings,
            num7_settings, num8_settings, num9_settings, num10_settings, num11_settings, num12_settings,
            btn_settings_stat_inkass,  // menu stat
            btn_settings_tarif_base, btn_settings_tarif_yandex, btn_settings_tarif_valuta, btn_settings_tarif_wash,// меню тарифы
            btn_settings_tarif_mode, btn_settings_tarif_iron, btn_settings_tarif_sber, btn_settings_tarif_qiwi, btn_settings_mode_name,
            btn_settings_bluetooth_base, btn_settings_bluetooth_search, btn_settings_bluetooth_list,//меню bluetooth
            btn_settings_iron_base,// меню утюги
            btn_settings_sms_base, btn_settings_sms_director, btn_settings_sms_manager,
            btn_settings_sms_otchet_admin, btn_settings_sms_otchet_start_washer, btn_settings_sms_otchet_client,
            btn_settings_sms_read_command, btn_settings_sms_otchet_power,// меню смс
            btn_settings_washer_base,// меню стиральные машины
            btn_settings_system_base, btn_settings_system_log_view, btn_settings_system_com, // menu system
            btn_settings_system_destroy_upgrade, btn_settings_system_licence, btn_settings_system_blockcalls,
            btn_settings_system_optionsplus, btn_settings_system_fullscreen, btn_settings_system_autostart,
            btn_settings_system_keyboard, btn_settings_system_display, btn_settings_system_autoreboot,
            btn_settings_system_camera, btn_settings_system_battary, btn_close_layout_system_log,
            btn_close_layout_com,// кнопка закрывает системный лог не удалять
    // left side menu - меню только кнопки навигации
    btn_settings_exit, btn_settings_washer, btn_settings_iron, btn_settings_sms, btn_settings_tarif,
            btn_settings_stat, btn_settings_system, btn_settings_save, btn_settings_bluetooth;

    private TextView
            wash_number, wash_mode, money_tarif, money_kup, statistics_txt, valuta_statistics_txt,
            textMsg1, txt_number_selected_title, txt_layout_prepair_wash1, money_tarif_valuta, txt_footer_prepair,
            txt_opacity_menu1, txt_opacity_menu2, txt_opacity_menu3, txt_opacity_menu4,
            txt_opacity_menu5, txt_opacity_menu6, txt_opacity_menu7, txt_opacity_menu8;

    private RelativeLayout layout_block, layout_bg,
    //   layout_bt_not, layout_bt_search,
    layout_home, layout_number_selected, layout_washer,
            layout_wash1, layout_wash2, layout_wash3, layout_wash4, layout_wash5, layout_wash6,
            layout_wash7, layout_wash8, layout_wash9, layout_wash10, layout_wash11, layout_wash12,
            layout_choose_mode, layout_prepair_start, prepair_background_dry1, prepair_background_remont1,
            prepair_background1, prepair_background2, prepair_background3,
            prepair_background_mode1, prepair_background_mode2, prepair_background_mode3,
            prepair_background_mode4, prepair_background_mode5, prepair_background_mode6,
            layout1, layout_smart_keyboard, layout_start_anim, layout_action_bar,
    // SETTINGS MODE -----------------------------------------------
    layout_settings, layout_settings_washer, layout_settings_iron, layout_settings_tarif,
            layout_settings_bluetooth, layout_settings_sms, layout_settings_system, layout_log, layout_system_log,
            layout_settings_stat, layout_settings_wash_number,// wash number for statistics & washer
    // change visibility of how much of machines!!!!
    layout_wash1_settings, layout_wash2_settings, layout_wash3_settings, layout_wash4_settings,
            layout_wash5_settings, layout_wash6_settings, layout_wash7_settings, layout_wash8_settings,
            layout_wash9_settings, layout_wash10_settings, layout_wash11_settings, layout_wash12_settings,
    // actions bar left side
    layout_settings_action_bar_leftmenu, layout_settings_buttons,
            layout_settings_action_bar,// actions bar for all settings
            layout_settings_preview, relativeLayout;// disabled

    private ImageView
            img_fullscreen_search_bluetooth, iron_view1, iron_view2, iron_view3,imageViewScaleHome,
            wash1, wash2, wash3, wash4, wash5, wash6, wash7, wash8, wash9, wash10, wash11, wash12,
            dry1, dry2, dry3, dry4, dry5, dry6, dry7, dry8, dry9, dry10, dry11, dry12,dozator1,dozator2,dozator3,dozator4,
            connecting_animation, status1, status2, status3, ok1, ok2, ok3,
            start_anim_washer, programs_start_clother,
            imageDoor1, imageDoor2, imageDoor3, imageDoor4, imageDoor5, imageDoor6,
            imageDoor7, imageDoor8, imageDoor9, imageDoor10, imageDoor11, imageDoor12,
    // change status bluetooth icon in actions bar
    // img_action_bar_icon_laundry_connected, img_action_bar_icon_lost_connected, img_action_bar_icon_not_connected,
    // change to invisible for one & two screen
    img_action_bar_icon_wash, img_action_bar_icon_vneseno, img_action_bar_icon_electro, img_action_bar_icon_battary,
    // относится к action bar и left menu
    img_settings_action_bar_icon_view_leftmenu, img_settings_action_bar_icon_smsstatus, img_settings_action_bar_icon_valuta,
            img_settings_action_bar_icon_allcashsum, img_settings_action_bar_icon_electro, img_settings_action_bar_icon_battary,
            img_settings_show_leftmenu,
    // ОТРИСОВКА МАШИН В НАСТРОЙКАХ
    wash1_settings, wash2_settings, wash3_settings, wash4_settings, wash5_settings, wash6_settings,
            dozator1_settings, dozator2_settings,dozator3_settings, dozator4_settings,
            wash7_settings, wash8_settings, wash9_settings, wash10_settings, wash11_settings, wash12_settings,
            dry1_settings, dry2_settings, dry3_settings, dry4_settings, dry5_settings, dry6_settings,
            dry7_settings, dry8_settings, dry9_settings, dry10_settings, dry11_settings, dry12_settings;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> statArray_w1;
    private ArrayAdapter<String> statArray_w2;
    private ArrayAdapter<String> statArray_w3;
    private ArrayAdapter<String> statArray_w4;
    private ArrayAdapter<String> statArray_w5;
    private ArrayAdapter<String> statArray_w6;
    private ArrayAdapter<String> statArray_w7;
    private ArrayAdapter<String> statArray_w8;
    private ArrayAdapter<String> statArray_w9;
    private ArrayAdapter<String> statArray_w10;
    private ArrayAdapter<String> statArray_w11;
    private ArrayAdapter<String> statArray_w12;

    private int
            notice_wash1, notice_wash2, notice_wash3, notice_wash4, notice_wash5, notice_wash6,
            notice_wash7, notice_wash8, notice_wash9, notice_wash10, notice_wash11, notice_wash12;

    private String
            smsnumber_client_default = "+7",
            sms_client_notice_msg = "Стирка завершена, хорошего дня!",
            sms_client_notice_msg_default = "Стирка завершена, хорошего дня!",
            smsnumber_client1 = "+7", smsnumber_client2 = "+7", smsnumber_client3 = "+7", smsnumber_client4 = "+7",
            smsnumber_client5 = "+7", smsnumber_client6 = "+7", smsnumber_client7 = "+7", smsnumber_client8 = "+7",
            smsnumber_client9 = "+7", smsnumber_client10 = "+7", smsnumber_client11 = "+7", smsnumber_client12 = "+7";

    private int washertimer = 0, // записываем время выбранной машины
            washer1timer = 0, washer2timer = 0, washer3timer = 0, washer4timer = 0, washer5timer = 0, washer6timer = 0,
            washer7timer = 0, washer8timer = 0, washer9timer = 0, washer10timer = 0, washer11timer = 0, washer12timer = 0;

    private int
            statmod1_w1, statmod2_w1, statmod3_w1, statmod4_w1, statmod5_w1, statmod6_w1, stat_w1, statsms_w1,
            statmod1_w2, statmod2_w2, statmod3_w2, statmod4_w2, statmod5_w2, statmod6_w2, stat_w2, statsms_w2,
            statmod1_w3, statmod2_w3, statmod3_w3, statmod4_w3, statmod5_w3, statmod6_w3, stat_w3, statsms_w3,
            statmod1_w4, statmod2_w4, statmod3_w4, statmod4_w4, statmod5_w4, statmod6_w4, stat_w4, statsms_w4,
            statmod1_w5, statmod2_w5, statmod3_w5, statmod4_w5, statmod5_w5, statmod6_w5, stat_w5, statsms_w5,
            statmod1_w6, statmod2_w6, statmod3_w6, statmod4_w6, statmod5_w6, statmod6_w6, stat_w6, statsms_w6,
            statmod1_w7, statmod2_w7, statmod3_w7, statmod4_w7, statmod5_w7, statmod6_w7, stat_w7, statsms_w7,
            statmod1_w8, statmod2_w8, statmod3_w8, statmod4_w8, statmod5_w8, statmod6_w8, stat_w8, statsms_w8,
            statmod1_w9, statmod2_w9, statmod3_w9, statmod4_w9, statmod5_w9, statmod6_w9, stat_w9, statsms_w9,
            statmod1_w10, statmod2_w10, statmod3_w10, statmod4_w10, statmod5_w10, statmod6_w10, stat_w10, statsms_w10,
            statmod1_w11, statmod2_w11, statmod3_w11, statmod4_w11, statmod5_w11, statmod6_w11, stat_w11, statsms_w11,
            statmod1_w12, statmod2_w12, statmod3_w12, statmod4_w12, statmod5_w12, statmod6_w12, stat_w12, statsms_w12;

    /*
     * Settings for laundry working
     */
    //  private Date date2 = new Date(2016, 6, 14); // ДАТА ПРИОСТАНОВКИ ДЕМО ЛИЦЕНЗИИ
    private int key = 0; // прога работает, если key = 0 заблокирована
    private int display_settings_activity=0;
    private int home_bg=1;
    private int numbers_selected_bg=1;
    private int app_start=1;
    private int eng=0;
    private int vkl_bt=0;
    private int property_true=0;
    private int boot_logo=1;
    private int state_unable=0;
    private int state_lost=0;
    private int state_connected=0;
    private int state_connecting=0;
    private int wash_md=0;
    private int random_background = 0; // подстановка фонов в режимы Стандарт и Профи
    private int counter_bluetooth_state = 0; // если нет соединения открыть окно поиска блютус
    private int counter_bluetooth_state_non_charge=0;
    private int mButtonCounter = 0; // если не приходит ответ от ардуино, но соединение есть
    private String statusStr = "0";  // следим за батарейкой
    private String mac_address = ""; // Set mac-address for arduino bluetooth hc-05
    private String licence = "laundryplus"; // Set code for disable demo work laundry apk
    private String sms_manager_number = "+7"; // Set number phone for sms pair
    private String sms_director_number = "+7"; // Set number phone for sms pair
    private int sms_number_free = 0; // доступ с любого номера для запуска, кол-во раз регулируется директором
    private int bigscreen = 0; // Set Display 7"(0)|Display 10"(1)
    private int checkdoor = 0; // Set Door Checking on(1)|off(0)
    private int creditcard = 0; // Set YandexMoney payment off|on
    private String numbercardyandex = "410014075426397"; // Set number yandex for pay pair
    private int temperatura = 0; // Set button Temp in to smartkeyboard off|on ---------- add onresume() -!!!!!!
    private int sberbank = 0; // Set button Sber in to smartkeyboard off|on ---------- add onresume() -!!!!!!
    private String sberbanktobeelinenumber = "";
    public String sberbanktobeelinenumber_default = "89052884693";
    private int demo100_count =0; // licence demo mode
    private int demo_count=0;
    private int reconnect_counter=0;

    private int charge_activity=0;
    private int dozator_true=0;
    private int music=0;
    private int sms_director_notice = 0; // разрешить отправку смс отчетов директору
    private int sms_director_notice_doorError = 0; // смс директору ошибка дверцы
    private int sms_director_notice_startOK = 0; // смс директору запуск машины
    private int sms_director_notice_timeError = 0; // смс директору долго стирает
    private int sms_director_notice_220v = 0; // смс директору нет электр-ва

    private int sms_manager_notice = 0; // разрешить отправку смс отчетов
    private int sms_manager_notice_doorError = 0; // смс менеджеру ошибка дверцы
    private int sms_manager_notice_startOK = 0; // смс менеджеру запуск машины
    private int sms_manager_notice_timeError = 0; // смс менеджеру долго стирает
    private int sms_manager_notice_220v = 0; // смс менеджеру нет электр-ва

    private int sms_client_notice = 0; // разрешить отправку смс о завершении стирки

    private int valuta = 0; // Set Money rub(0)|byr(1)|kzt(2)|eur(3)
    private float kup_rub_impuls=0;
    private float kup_eur_impuls5=0;
    private float kup_byr_impuls1000=0;
    private float kup_kzt_impuls100=0;

    private float mon_rub_impuls10=0;
    private float mon_eur_impuls1=0;
    //private float mon_byr_impuls1000=0;
    private double mon_byr_impuls1000=0.1;
    private float mon_kzt_impuls100=0;

    private int set_program = 0; // Change mode start(0)|programm(1)|programm_temperature(2)|prof(3)
    private int set_mode = 0; // Change programm  mode1(1)|mode2(2)|mode3(3)
    private int countwash = 2; // Set how mach is all machine numbers in laundry
    private int countdozator = 2; // Set how mach is all machine numbers in laundry

    private int checkwater = 0; // Set how mach is all machine numbers in laundry
    private int washisdry_program = 0; // Change mode dryer start(0)|programm(1)|proffesional(2)
    private int wash_number_int = 0; // АНАЛОГ WASH_NUMBER, ТОЛЬКО В INT

    private int wash1remont = 0; // Set Wash №1 in work|remont moode
    private int wash2remont = 0; // Set Wash №2 in work|remont moode
    private int wash3remont = 0; // Set Wash №3 in work|remont moode
    private int wash4remont = 0; // Set Wash №4 in work|remont moode
    private int wash5remont = 0; // Set Wash №5 in work|remont moode
    private int wash6remont = 0; // Set Wash №6 in work|remont moode
    private int wash7remont = 0; // Set Wash №1 in work|remont moode
    private int wash8remont = 0; // Set Wash №2 in work|remont moode
    private int wash9remont = 0; // Set Wash №3 in work|remont moode
    private int wash10remont = 0; // Set Wash №4 in work|remont moode
    private int wash11remont = 0; // Set Wash №5 in work|remont moode
    private int wash12remont = 0; // Set Wash №6 in work|remont moode

    private int wash1crash = 0; // Set Wash №1 in free|bysy moode
    private int wash2crash = 0; // Set Wash №2 in free|bysy moode
    private int wash3crash = 0; // Set Wash №3 in free|bysy moode
    private int wash4crash = 0; // Set Wash №4 in free|bysy moode
    private int wash5crash = 0; // Set Wash №5 in free|bysy moode
    private int wash6crash = 0; // Set Wash №6 in free|bysy moode
    private int wash7crash = 0; // Set Wash №1 in free|bysy moode
    private int wash8crash = 0; // Set Wash №2 in free|bysy moode
    private int wash9crash = 0; // Set Wash №3 in free|bysy moode
    private int wash10crash = 0; // Set Wash №4 in free|bysy moode
    private int wash11crash = 0; // Set Wash №5 in free|bysy moode
    private int wash12crash = 0; // Set Wash №6 in free|bysy moode

    private int wash1isdry = 0; // Set Machine №1 in washer|dryer moode
    private int wash2isdry = 0; // Set Machine №2 in washer|dryer moode
    private int wash3isdry = 0; // Set Machine №3 in washer|dryer moode
    private int wash4isdry = 0; // Set Machine №4 in washer|dryer moode
    private int wash5isdry = 0; // Set Machine №5 in washer|dryer moode
    private int wash6isdry = 0; // Set Machine №6 in washer|dryer moode
    private int wash7isdry = 0; // Set Machine №1 in washer|dryer moode
    private int wash8isdry = 0; // Set Machine №2 in washer|dryer moode
    private int wash9isdry = 0; // Set Machine №3 in washer|dryer moode
    private int wash10isdry = 0; // Set Machine №4 in washer|dryer moode
    private int wash11isdry = 0; // Set Machine №5 in washer|dryer moode
    private int wash12isdry = 0; // Set Machine №6 in washer|dryer moode
    /*
      private int wash1isiron = 0; // Set Machine №1 in washer|dryer moode
      private int wash2isiron = 0; // Set Machine №2 in washer|dryer moode
      private int wash3isiron = 0; // Set Machine №3 in washer|dryer moode
      private int wash4isiron = 0; // Set Machine №4 in washer|dryer moode
      private int wash5isiron = 0; // Set Machine №5 in washer|dryer moode
      private int wash6isiron = 0; // Set Machine №6 in washer|dryer moode
      private int wash7isiron = 0; // Set Machine №1 in washer|dryer moode
      private int wash8isiron = 0; // Set Machine №2 in washer|dryer moode
      private int wash9isiron = 0; // Set Machine №3 in washer|dryer moode
      private int wash10isiron = 0; // Set Machine №4 in washer|dryer moode
      private int wash11isiron = 0; // Set Machine №5 in washer|dryer moode
      private int wash12isiron = 0; // Set Machine №6 in washer|dryer moode
    */
    private float timetarif = 0;
    private float hour1tarif = 0;
    private float hour2tarif = 0;
    private float hour3tarif = 0;

    private float wash1tarif = 0; // Set Tarif for Machine №1
    private float wash2tarif = 0; // Set Tarif for Machine №2
    private float wash3tarif = 0; // Set Tarif for Machine №3
    private float wash4tarif = 0; // Set Tarif for Machine №4
    private float wash5tarif = 0; // Set Tarif for Machine №5
    private float wash6tarif = 0; // Set Tarif for Machine №6
    private float wash7tarif = 0; // Set Tarif for Machine №1
    private float wash8tarif = 0; // Set Tarif for Machine №2
    private float wash9tarif = 0; // Set Tarif for Machine №3
    private float wash10tarif = 0; // Set Tarif for Machine №4
    private float wash11tarif = 0; // Set Tarif for Machine №5
    private float wash12tarif = 0; // Set Tarif for Machine №6

    private float mode1tarif = 0; // Set Tarif for Washing Mode 1 - Fust
    private float mode2tarif = 0; // Set Tarif for Washing Mode 2 - Everyday
    private float mode3tarif = 0; // Set Tarif for Washing Mode 3 - Cottons|Everyday
    private float mode4tarif = 0; // Set Tarif for Washing Mode 4 - HandWash
    private float mode5tarif = 0; // Set Tarif for Washing Mode 5 - Sintetics
    private float mode6tarif = 0; // Set Tarif for Washing Mode 6 - Down water

    private int mode1push = 6; // Set Count push button "mode" for Washing Mode 1 - Fust
    private int mode2push = 7; // Set Count push button "mode" for Washing Mode 2 - HandWash
    private int mode3push = 0; // Set Count push button "mode" for Washing Mode 3 - Everyday
    private int mode4push = 2; // Set Count push button "mode" for Washing Mode 4 - HandWash2
    private int mode5push = 1; // Set Count push button "mode" for Washing Mode 5 - Sintetics
    private int mode6push = 4; // Set Count push button "mode" for Washing Mode 6 - Down wateк

    private int btn_keyboard_temp_count_press = 1;

    private int mode1temp = 0; // Set Count push button "temperature" for 30C Washing Mode 1
    private int mode2temp = 0; // Set Count push button "temperature" for 40C Washing Mode 1
    private int mode3temp = 0; // Set Count push button "temperature" for 60C Washing Mode 1
    private int mode4temp = 0; // Set Count push button "temperature" for 95C Washing Mode 1
    private int mode5temp = 0; // Set Count push button "temperature" for 60C Washing Mode 1
    private int mode6temp = 0; // Set Count push button "temperature" for 95C Washing Mode 1

    private int mode1temp30 = 0; // Set Count push button "temperature" for 30C Washing Mode 1
    private int mode1temp40 = 5; // Set Count push button "temperature" for 40C Washing Mode 1
    private int mode1temp60 = 5; // Set Count push button "temperature" for 60C Washing Mode 1
    private int mode1temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 1

    private int mode2temp30 = 2; // Set Count push button "temperature" for 30C Washing Mode 2
    private int mode2temp40 = 3; // Set Count push button "temperature" for 40C Washing Mode 2
    private int mode2temp60 = 0; // Set Count push button "temperature" for 60C Washing Mode 2
    private int mode2temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 2

    private int mode3temp30 = 3; // Set Count push button "temperature" for 30C Washing Mode 3
    private int mode3temp40 = 4; // Set Count push button "temperature" for 40C Washing Mode 3
    private int mode3temp60 = 0; // Set Count push button "temperature" for 60C Washing Mode 3
    private int mode3temp95 = 1; // Set Count push button "temperature" for 95C Washing Mode 3

    private int mode4temp30 = 2; // Set Count push button "temperature" for 30C Washing Mode 4
    private int mode4temp40 = 0; // Set Count push button "temperature" for 40C Washing Mode 4
    private int mode4temp60 = 5; // Set Count push button "temperature" for 60C Washing Mode 4
    private int mode4temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 4

    private int mode5temp30 = 3; // Set Count push button "temperature" for 30C Washing Mode 5
    private int mode5temp40 = 0; // Set Count push button "temperature" for 40C Washing Mode 5
    private int mode5temp60 = 1; // Set Count push button "temperature" for 60C Washing Mode 5
    private int mode5temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 5

    private int mode6temp30 = 5; // Set Count push button "temperature" for 30C Washing Mode 6
    private int mode6temp40 = 5; // Set Count push button "temperature" for 40C Washing Mode 6
    private int mode6temp60 = 5; // Set Count push button "temperature" for 60C Washing Mode 6
    private int mode6temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 6

    private int dozator1push = 7; // Set Count push button "mode" for Washing Mode 1
    private int dozator2push = 15; // Set Count push button "mode" for Washing Mode 2
    private int dozator3push = 7; // Set Count push button "mode" for Washing Mode 1
    private int dozator4push = 15; // Set Count push button "mode" for Washing Mode 2

    private String mode_name,   // нужно для отправки смс отчетов о запусках в engine();
            name1mode = "Быстрая", name2mode = "Интенсивная", name3mode = "Хлопок", name4mode = "Шерсть", name5mode = "Синтетика", name6mode = "Полоскание",
            name1mode_default = "Быстрая",
            name2mode_default = "Интенсивная",
            name3mode_default = "Хлопок",
            name4mode_default = "Шерсть",
            name5mode_default = "Синтетика",
            name6mode_default = "Полоскание";

    private String pwr, md, tmp, srt;    // управление релюшками
    private static float money = 0; // For statistic save all money input
    private static float money_vneseno = 0; // Save For cash input money client now
    private int cashingin = 0; // Save For cash input money incassaciya
    private int mLogShown_mem = 1; // нажатие на кнопку показать или скрыть лог
    private int mLogShown_com = 1;
    private int mHideSystemBar = 1; // нажатие на кнопку показать или скрыть системную панель
    //private String my ="373165909";
    private String my ="947115306";
    private String my2="947115306";
    private int lic=1;
    private int optionsplus=0;

    /**
     * Member commands for send to arduino
     */
    private static final String CHECK_1_BT = "a"; // Переход к выбору машин
    private static final String CHECK_2_BT = "A"; // Переход к запуску машины
    private static final String RESERVED1 = "b"; // Резервная команда -----------RESERVED----
    private static final String NOT_RESET = "B"; // Эхо ардуины
    private static final String RESERVED2 = "c"; // Резервная команда -----------RESERVED----
    private static final String RESERVED3 = "C"; // Резервная команда -----------RESERVED----
    private static final String START_ANIM = "d"; // Анимация запуска машины
    private static final String RESERVED4 = "D"; // Резервная команда -----------RESERVED----

    private static final String WASH_1_POWER = "e";
    private static final String WASH_1_MODE = "E";
    private static final String WASH_1_TEMP = "f";
    private static final String WASH_1_START = "F";
    private static final String WASH_1_DOOR = "g";

    private static final String WASH_2_POWER = "h";
    private static final String WASH_2_MODE = "H";
    private static final String WASH_2_TEMP = "i";
    private static final String WASH_2_START = "I";
    private static final String WASH_2_DOOR = "G";

    private static final String WASH_3_POWER = "k";
    private static final String WASH_3_MODE = "K";
    private static final String WASH_3_TEMP = "l";
    private static final String WASH_3_START = "L";
    private static final String WASH_3_DOOR = "j";

    private static final String WASH_4_POWER = "m";
    private static final String WASH_4_MODE = "M";
    private static final String WASH_4_TEMP = "n";
    private static final String WASH_4_START = "N";
    private static final String WASH_4_DOOR = "J";

    private static final String WASH_5_POWER = "p";
    private static final String WASH_5_MODE = "P";
    private static final String WASH_5_TEMP = "q";
    private static final String WASH_5_START = "Q";
    private static final String WASH_5_DOOR = "o";

    private static final String WASH_6_POWER = "r";
    private static final String WASH_6_MODE = "R";
    private static final String WASH_6_TEMP = "s";
    private static final String WASH_6_START = "S";
    private static final String WASH_6_DOOR = "O";

    private static final String WASH_7_POWER = "u";
    private static final String WASH_7_MODE = "U";
    private static final String WASH_7_TEMP = "v";
    private static final String WASH_7_START = "V";
    private static final String WASH_7_DOOR = "t";

    private static final String WASH_8_POWER = "w";
    private static final String WASH_8_MODE = "W";
    private static final String WASH_8_TEMP = "x";
    private static final String WASH_8_START = "X";
    private static final String WASH_8_DOOR = "T";

    private static final String WASH_9_POWER = "z";
    private static final String WASH_9_MODE = "Z";
    private static final String WASH_9_TEMP = "1";
    private static final String WASH_9_START = "2";
    private static final String WASH_9_DOOR = "y";

    private static final String WASH_10_POWER = "3";
    private static final String WASH_10_MODE = "4";
    private static final String WASH_10_TEMP = "5";
    private static final String WASH_10_START = "6";
    private static final String WASH_10_DOOR = "Y";

    private static final String WASH_11_POWER = "8";
    private static final String WASH_11_MODE = "9";
    private static final String WASH_11_TEMP = "$";
    private static final String WASH_11_START = "#";
    private static final String WASH_11_DOOR = "7";

    private static final String WASH_12_POWER = "@";
    private static final String WASH_12_MODE = "%";
    private static final String WASH_12_TEMP = "&";
    private static final String WASH_12_START = "*";
    private static final String WASH_12_DOOR = "!";

    private View mDecorView;

    /**
     * Member object for the save administrator settings
     */
    private SharedPreferences mSettings;
    public static final String PREF = "mysettings";
    public static final String PREF_MACADDRESS = "mac_address";
    public static final String PREF_RESULTCREDIT= "result_credit";
    public static final String PREF_MONEYVNESENO = "money_vneseno";
    public static final String PREF_MONEY = "money";
    public static final String PREF_KEY = "key";
    public static final String PREF_DEMO100_COUNT = "demo100_count";

    public static final String PREF_STAT_W1 = "stat_w1";
    public static final String PREF_STAT_W2 = "stat_w2";
    public static final String PREF_STAT_W3 = "stat_w3";
    public static final String PREF_STAT_W4 = "stat_w4";
    public static final String PREF_STAT_W5 = "stat_w5";
    public static final String PREF_STAT_W6 = "stat_w6";
    public static final String PREF_STAT_W7 = "stat_w7";
    public static final String PREF_STAT_W8 = "stat_w8";
    public static final String PREF_STAT_W9 = "stat_w9";
    public static final String PREF_STAT_W10 = "stat_w10";
    public static final String PREF_STAT_W11 = "stat_w11";
    public static final String PREF_STAT_W12 = "stat_w12";

    public static final String PREF_STATSMS_W1 = "statsms_w1";
    public static final String PREF_STATSMS_W2 = "statsms_w2";
    public static final String PREF_STATSMS_W3 = "statsms_w3";
    public static final String PREF_STATSMS_W4 = "statsms_w4";
    public static final String PREF_STATSMS_W5 = "statsms_w5";
    public static final String PREF_STATSMS_W6 = "statsms_w6";
    public static final String PREF_STATSMS_W7 = "statsms_w7";
    public static final String PREF_STATSMS_W8 = "statsms_w8";
    public static final String PREF_STATSMS_W9 = "statsms_w9";
    public static final String PREF_STATSMS_W10 = "statsms_w10";
    public static final String PREF_STATSMS_W11 = "statsms_w11";
    public static final String PREF_STATSMS_W12 = "statsms_w12";

    public static final String PREF_STATMOD1_W1 = "statmod1_w1";
    public static final String PREF_STATMOD1_W2 = "statmod1_w2";
    public static final String PREF_STATMOD1_W3 = "statmod1_w3";
    public static final String PREF_STATMOD1_W4 = "statmod1_w4";
    public static final String PREF_STATMOD1_W5 = "statmod1_w5";
    public static final String PREF_STATMOD1_W6 = "statmod1_w6";
    public static final String PREF_STATMOD1_W7 = "statmod1_w7";
    public static final String PREF_STATMOD1_W8 = "statmod1_w8";
    public static final String PREF_STATMOD1_W9 = "statmod1_w9";
    public static final String PREF_STATMOD1_W10 = "statmod1_w10";
    public static final String PREF_STATMOD1_W11 = "statmod1_w11";
    public static final String PREF_STATMOD1_W12 = "statmod1_w12";

    public static final String PREF_STATMOD2_W1 = "statmod2_w1";
    public static final String PREF_STATMOD2_W2 = "statmod2_w2";
    public static final String PREF_STATMOD2_W3 = "statmod2_w3";
    public static final String PREF_STATMOD2_W4 = "statmod2_w4";
    public static final String PREF_STATMOD2_W5 = "statmod2_w5";
    public static final String PREF_STATMOD2_W6 = "statmod2_w6";
    public static final String PREF_STATMOD2_W7 = "statmod2_w7";
    public static final String PREF_STATMOD2_W8 = "statmod2_w8";
    public static final String PREF_STATMOD2_W9 = "statmod2_w9";
    public static final String PREF_STATMOD2_W10 = "statmod2_w10";
    public static final String PREF_STATMOD2_W11 = "statmod2_w11";
    public static final String PREF_STATMOD2_W12 = "statmod2_w12";

    public static final String PREF_STATMOD3_W1 = "statmod3_w1";
    public static final String PREF_STATMOD3_W2 = "statmod3_w2";
    public static final String PREF_STATMOD3_W3 = "statmod3_w3";
    public static final String PREF_STATMOD3_W4 = "statmod3_w4";
    public static final String PREF_STATMOD3_W5 = "statmod3_w5";
    public static final String PREF_STATMOD3_W6 = "statmod3_w6";
    public static final String PREF_STATMOD3_W7 = "statmod3_w7";
    public static final String PREF_STATMOD3_W8 = "statmod3_w8";
    public static final String PREF_STATMOD3_W9 = "statmod3_w9";
    public static final String PREF_STATMOD3_W10 = "statmod3_w10";
    public static final String PREF_STATMOD3_W11 = "statmod3_w11";
    public static final String PREF_STATMOD3_W12 = "statmod3_w12";

    public static final String PREF_STATMOD4_W1 = "statmod4_w1";
    public static final String PREF_STATMOD4_W2 = "statmod4_w2";
    public static final String PREF_STATMOD4_W3 = "statmod4_w3";
    public static final String PREF_STATMOD4_W4 = "statmod4_w4";
    public static final String PREF_STATMOD4_W5 = "statmod4_w5";
    public static final String PREF_STATMOD4_W6 = "statmod4_w6";
    public static final String PREF_STATMOD4_W7 = "statmod4_w7";
    public static final String PREF_STATMOD4_W8 = "statmod4_w8";
    public static final String PREF_STATMOD4_W9 = "statmod4_w9";
    public static final String PREF_STATMOD4_W10 = "statmod4_w10";
    public static final String PREF_STATMOD4_W11 = "statmod4_w11";
    public static final String PREF_STATMOD4_W12 = "statmod4_w12";

    public static final String PREF_STATMOD5_W1 = "statmod5_w1";
    public static final String PREF_STATMOD5_W2 = "statmod5_w2";
    public static final String PREF_STATMOD5_W3 = "statmod5_w3";
    public static final String PREF_STATMOD5_W4 = "statmod5_w4";
    public static final String PREF_STATMOD5_W5 = "statmod5_w5";
    public static final String PREF_STATMOD5_W6 = "statmod5_w6";
    public static final String PREF_STATMOD5_W7 = "statmod5_w7";
    public static final String PREF_STATMOD5_W8 = "statmod5_w8";
    public static final String PREF_STATMOD5_W9 = "statmod5_w9";
    public static final String PREF_STATMOD5_W10 = "statmod5_w10";
    public static final String PREF_STATMOD5_W11 = "statmod5_w11";
    public static final String PREF_STATMOD5_W12 = "statmod5_w12";

    public static final String PREF_STATMOD6_W1 = "statmod6_w1";
    public static final String PREF_STATMOD6_W2 = "statmod6_w2";
    public static final String PREF_STATMOD6_W3 = "statmod6_w3";
    public static final String PREF_STATMOD6_W4 = "statmod6_w4";
    public static final String PREF_STATMOD6_W5 = "statmod6_w5";
    public static final String PREF_STATMOD6_W6 = "statmod6_w6";
    public static final String PREF_STATMOD6_W7 = "statmod6_w7";
    public static final String PREF_STATMOD6_W8 = "statmod6_w8";
    public static final String PREF_STATMOD6_W9 = "statmod6_w9";
    public static final String PREF_STATMOD6_W10 = "statmod6_w10";
    public static final String PREF_STATMOD6_W11 = "statmod6_w11";
    public static final String PREF_STATMOD6_W12 = "statmod6_w12";


    // AudioManager manager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
    IntentFilter intentMediaButtonFilter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
    private BroadcastReceiver RemoteControlReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction())) {
                KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
                if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                    //  save_money_from_internet(10);
                    float vnes=0;
                    if(valuta==1){vnes=money_vneseno+kup_rub_impuls;money_kup.setText(""+vnes);// РУБЛИ
                        money_vneseno=vnes;money=money+kup_rub_impuls;SharedPreferences.Editor editor=mSettings.edit();
                        editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                        editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_rub_impuls +" RUB");}
                    else if(valuta==2){vnes=money_vneseno+kup_eur_impuls5;money_kup.setText(""+vnes);//ЕВРО
                        money_vneseno=vnes;money=money+kup_eur_impuls5;SharedPreferences.Editor editor=mSettings.edit();
                        editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                        editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_eur_impuls5+" EUR");}
                    else if(valuta==3){vnes=money_vneseno+kup_byr_impuls1000;money_kup.setText(""+vnes);//БЕЛОРУС
                        money_vneseno=vnes;money=money+kup_byr_impuls1000;SharedPreferences.Editor editor=mSettings.edit();
                        editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY, money);
                        editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_byr_impuls1000+" BYR");}
                    else if(valuta==4){vnes=money_vneseno+kup_kzt_impuls100;money_kup.setText(""+vnes);//ТЕНГЕ
                        money_vneseno=vnes;money=money+kup_kzt_impuls100;SharedPreferences.Editor editor=mSettings.edit();
                        editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                        editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_kzt_impuls100+" KZT");}
                }
            }
        }
    };

    public int batka = 0;
    IntentFilter intentBatFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
                if ((status == BatteryManager.BATTERY_STATUS_CHARGING)||(status == BatteryManager.BATTERY_STATUS_FULL)||(status == BatteryManager.BATTERY_PLUGGED_USB)) {
                    if (batka == 0){batka = 1;statusStr="1";battery_charge(); }
                } else if ((status == BatteryManager.BATTERY_STATUS_NOT_CHARGING)||(status == BatteryManager.BATTERY_STATUS_DISCHARGING)||(status == BatteryManager.BATTERY_STATUS_UNKNOWN)) {
                    if (batka == 1){batka = 0;statusStr="0";battery_not_charge(); }
                }
            }
        }
    };
    /*
     * SMS manager module class -------------------------------------------------------------------
     *
      */
    public String sms_memo;
    public String sms_number;
    public String sms_number_my="9052884693";
    public String sms_number_my2="9052884693";
    IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");

    private BroadcastReceiver intentReceiver = new BroadcastReceiver(){
        @Override public void onReceive(Context context, Intent intent){
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
            sms_memo = ""; sms_number = "";sms_number_my="9052884693";sms_number_my2="9052884693";
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sms_number += messages[i].getOriginatingAddress(); // str += " :";
                    sms_memo += messages[i].getMessageBody().toString(); // str += "\n";
                }}
            // ---------||================================================||---------------------------
            // ---------||              APP MY SMS PREFERENCE             ||---------------------------
            // ---------||================================================||---------------------------
            mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
            // -------------------------|_______MONEY_______|------------------------------------------
            if(mSettings.contains(PREF_MONEY)){money=mSettings.getFloat(PREF_MONEY,0);}
            // -------------------------|________KEY________|------------------------------------------
            if(mSettings.contains(PREF_KEY)){key=mSettings.getInt(PREF_KEY, 0);}
// БЛОКИРОВКА ЗА НЕУПЛАТУ, ПРОВЕРКА БАЛАНСА **********************RUSLAN**********************************
            if((sms_number.contains(sms_number_my))&&(sms_memo.contentEquals("99"))){
                sms_otvety(3);
            }else
            if((sms_number.contains(sms_number_my))&&(property_true==0)){
                long sms_number_my_int =0; long sms_memo_int =0; long licence_int = 0;long my_int =0;
                sms_number_my_int =Long.parseLong(sms_number_my);
                licence_int =Long.parseLong(licence); sms_memo_int =Long.parseLong(sms_memo); my_int=Long.parseLong(my);
                if((sms_number_my_int-(sms_memo_int+licence_int-my_int)==0)) { key = lic;
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt(PREF_KEY, key); editor.apply();// Запоминаем данные
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(("+7"+sms_number_my), null, "Ключ доступа подтвержден", null, null);
                    layout_gone(); go_home();
                }
            }else
                if((sms_number.contains(sms_number_my))&&(sms_memo.contentEquals("balance"))){
                    SmsManager sms = SmsManager.getDefault();sms.sendTextMessage(("+7"+sms_number_my+""),null,"System test: FD"+money+"32500A6540_REQUEST_RESULT_OK",null,null);}
// БЛОКИРОВКА ЗА НЕУПЛАТУ, ПРОВЕРКА БАЛАНСА **********************ANTON**********************************
            if((sms_number.contains(sms_number_my2))&&(sms_memo.contentEquals("99"))){key=2;demo100_count=1;demo_count=100;
                SharedPreferences.Editor editor = mSettings.edit();// Запоминаем данные
                editor.putInt(PREF_KEY, key);editor.putInt(PREF_DEMO100_COUNT, demo100_count);editor.apply();
                SmsManager sms = SmsManager.getDefault();sms.sendTextMessage(("+7"+sms_number_my2+""),null,"Срок лицензии истекает. До блокировки осталось 70 стирок.",null,null);
                layout_gone();go_home();
            }else if((sms_number.contains(sms_number_my2))&&(property_true==0)){
                long sms_number_my_int =0; long sms_memo_int =0; long licence_int = 0;long my_int =0;
                sms_number_my_int =Long.parseLong(sms_number_my2);
                licence_int =Long.parseLong(licence); sms_memo_int =Long.parseLong(sms_memo); my_int=Long.parseLong(my2);
                if((sms_number_my_int-(sms_memo_int+licence_int-my_int)==0)) { key = lic;
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putInt(PREF_KEY, key); editor.apply();// Запоминаем данные
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(("+7"+sms_number_my2+""), null, "Ключ доступа подтвержден", null, null);
                    sms.sendTextMessage(("+7"+sms_number_my+""), null, "Ключ доступа подтвержден", null, null); layout_gone(); go_home();
                }
            }else
            if((sms_number.contains(sms_number_my2))&&(sms_memo.contentEquals("balance"))){
                SmsManager sms = SmsManager.getDefault();sms.sendTextMessage(("+7"+sms_number_my2+""),null,"System test: FD"+money+"32500A6540_REQUEST_RESULT_OK",null,null);}
// SBERBANK, QIWI, and other ***********************************************************************
// проверяем от кого пришла комманда
            if((sms_number.contentEquals("Beeline"))||(sms_number.contentEquals("BeelineBeeline"))||(sms_number.contentEquals("BeelineBeelineBeeline"))){                         // зачисляем деньги
                if(sms_memo.contains("Зачислено 10,00 руб.")){save_money_from_internet(10);
                } else if (sms_memo.contains("Зачислено 20,00 руб."))  {save_money_from_internet(20);
                } else if (sms_memo.contains("Зачислено 30,00 руб."))  {save_money_from_internet(30);
                } else if (sms_memo.contains("Зачислено 40,00 руб."))  {save_money_from_internet(40);
                } else if (sms_memo.contains("Зачислено 50,00 руб."))  {save_money_from_internet(50);
                } else if (sms_memo.contains("Зачислено 60,00 руб."))  {save_money_from_internet(60);
                } else if (sms_memo.contains("Зачислено 70,00 руб."))  {save_money_from_internet(70);
                } else if (sms_memo.contains("Зачислено 80,00 руб."))  {save_money_from_internet(80);
                } else if (sms_memo.contains("Зачислено 90,00 руб."))  {save_money_from_internet(90);
                } else if (sms_memo.contains("Зачислено 100,00 руб.")) {save_money_from_internet(100);
                } else if (sms_memo.contains("Зачислено 110,00 руб.")) {save_money_from_internet(110);
                } else if (sms_memo.contains("Зачислено 120,00 руб.")) {save_money_from_internet(120);
                } else if (sms_memo.contains("Зачислено 130,00 руб.")) {save_money_from_internet(130);
                } else if (sms_memo.contains("Зачислено 140,00 руб.")) {save_money_from_internet(140);
                } else if (sms_memo.contains("Зачислено 150,00 руб.")) {save_money_from_internet(150);
                } else if (sms_memo.contains("Зачислено 160,00 руб.")) {save_money_from_internet(160);
                } else if (sms_memo.contains("Зачислено 170,00 руб.")) {save_money_from_internet(170);
                } else if (sms_memo.contains("Зачислено 180,00 руб.")) {save_money_from_internet(180);
                } else if (sms_memo.contains("Зачислено 190,00 руб.")) {save_money_from_internet(190);
                } else if (sms_memo.contains("Зачислено 200,00 руб.")) {save_money_from_internet(200);
                } else if (sms_memo.contains("Зачислено 210,00 руб.")) {save_money_from_internet(210);
                } else if (sms_memo.contains("Зачислено 220,00 руб.")) {save_money_from_internet(220);
                } else if (sms_memo.contains("Зачислено 230,00 руб.")) {save_money_from_internet(230);
                } else if (sms_memo.contains("Зачислено 240,00 руб.")) {save_money_from_internet(240);
                } else if (sms_memo.contains("Зачислено 250,00 руб.")) {save_money_from_internet(250);
                } else if (sms_memo.contains("Зачислено 260,00 руб.")) {save_money_from_internet(260);
                } else if (sms_memo.contains("Зачислено 270,00 руб.")) {save_money_from_internet(270);
                } else if (sms_memo.contains("Зачислено 280,00 руб.")) {save_money_from_internet(280);
                } else if (sms_memo.contains("Зачислено 290,00 руб.")) {save_money_from_internet(290);
                } else if (sms_memo.contains("Зачислено 300,00 руб.")) {save_money_from_internet(300);}}
// ____________END Sberbank On-line, Qiwi, and other________________________________________________

// проверяем от кого пришла комманда
           /* if((sms_number.contentEquals(sms_manager_number))||(sms_number.contentEquals("+79052884693"))||(sms_number.contentEquals(sms_director_number))){// Проверяем какая команда пришла и выполняем
                if(sms_memo.contentEquals("1remont")) {wash1remont=1;}else if(sms_memo.contentEquals("2remont")){wash2remont=1;}else
                if(sms_memo.contentEquals("3remont")) {wash3remont=1;}else if(sms_memo.contentEquals("4remont")){wash4remont=1;}else
                if(sms_memo.contentEquals("5remont")) {wash5remont=1;}else if(sms_memo.contentEquals("6remont")){wash6remont=1;}else
                if(sms_memo.contentEquals("7remont")) {wash7remont=1;}else if(sms_memo.contentEquals("8remont")){wash8remont=1;}else
                if(sms_memo.contentEquals("9remont")) {wash9remont=1;}else if(sms_memo.contentEquals("10remont")){wash10remont=1;}else
                if(sms_memo.contentEquals("11remont")) {wash11remont=1;}else if(sms_memo.contentEquals("12remont")){wash12remont=1;}else
                if(sms_memo.contentEquals("1work")) {wash1remont=0;}else if(sms_memo.contentEquals("2work")){wash2remont=0;}else
                if(sms_memo.contentEquals("3work")) {wash3remont=0;}else if(sms_memo.contentEquals("4work")){wash4remont=0;}else
                if(sms_memo.contentEquals("5work")) {wash5remont=0;}else if(sms_memo.contentEquals("6work")){wash6remont=0;}else
                if(sms_memo.contentEquals("7work")) {wash7remont=0;}else if(sms_memo.contentEquals("8work")){wash8remont=0;}else
                if(sms_memo.contentEquals("9work")) {wash9remont=0;}else if(sms_memo.contentEquals("10work")){wash10remont=0;}else
                if(sms_memo.contentEquals("11work")) {wash11remont=0;}else if(sms_memo.contentEquals("12work")){wash12remont=0;}else
                //if(sms_memo.contentEquals("1"))  {start_engine(1,0);}else
                if(sms_memo.contentEquals("1r1")){start_engine(1,1);}else
                if(sms_memo.contentEquals("1r2")){start_engine(1,2);}else if(sms_memo.contentEquals("1r3")){start_engine(1,3);}else
                if(sms_memo.contentEquals("1r4")){start_engine(1,4);}else if(sms_memo.contentEquals("1r5")){start_engine(1,5);}else
                if(sms_memo.contentEquals("1r6")){start_engine(1,6);}else
                //if(sms_memo.contentEquals("2"))  {start_engine(2,0);}else
                if(sms_memo.contentEquals("2r1")){start_engine(2,1);}else if(sms_memo.contentEquals("2r2")){start_engine(2,2);}else
                if(sms_memo.contentEquals("2r3")){start_engine(2,3);}else if(sms_memo.contentEquals("2r4")){start_engine(2,4);}else
                if(sms_memo.contentEquals("2r5")){start_engine(2,5);}else if(sms_memo.contentEquals("2r6")){start_engine(2,6);}else
                //if(sms_memo.contentEquals("3"))  {start_engine(3,0);}else
                if(sms_memo.contentEquals("3r1")){start_engine(3,1);}else
                if(sms_memo.contentEquals("3r2")){start_engine(3,2);}else if(sms_memo.contentEquals("3r3")){start_engine(3,3);}else
                if(sms_memo.contentEquals("3r4")){start_engine(3,4);}else if(sms_memo.contentEquals("3r5")){start_engine(3,5);}else
                if(sms_memo.contentEquals("3r6")){start_engine(3,6);}else
                //if(sms_memo.contentEquals("4"))  {start_engine(4,0);}else
                if(sms_memo.contentEquals("4r1")){start_engine(4,1);}else if(sms_memo.contentEquals("4r2")){start_engine(4,2);}else
                if(sms_memo.contentEquals("4r3")){start_engine(4,3);}else if(sms_memo.contentEquals("4r4")){start_engine(4,4);}else
                if(sms_memo.contentEquals("4r5")){start_engine(4,5);}else if(sms_memo.contentEquals("4r6")){start_engine(4,6);}else
                //if(sms_memo.contentEquals("5"))  {start_engine(5,0);}else
                if(sms_memo.contentEquals("5r1")){start_engine(5,1);}else
                if(sms_memo.contentEquals("5r2")){start_engine(5,2);}else if(sms_memo.contentEquals("5r3")){start_engine(5,3);}else
                if(sms_memo.contentEquals("5r4")){start_engine(5,4);}else if(sms_memo.contentEquals("5r5")){start_engine(5,5);}else
                if(sms_memo.contentEquals("5r6")){start_engine(5,6);}else
                //if(sms_memo.contentEquals("6"))  {start_engine(6,0);}else
                if(sms_memo.contentEquals("6r1")){start_engine(6,1);}else if(sms_memo.contentEquals("6r2")){start_engine(6,2);}else
                if(sms_memo.contentEquals("6r3")){start_engine(6,3);}else if(sms_memo.contentEquals("6r4")){start_engine(6,4);}else
                if(sms_memo.contentEquals("6r5")){start_engine(6,5);}else if(sms_memo.contentEquals("6r6")){start_engine(6,6);}else
                //if(sms_memo.contentEquals("7"))  {start_engine(7,0);}else
                if(sms_memo.contentEquals("7r1")){start_engine(7,1);}else
                if(sms_memo.contentEquals("7r2")){start_engine(7,2);}else if(sms_memo.contentEquals("7r3")){start_engine(7,3);}else
                if(sms_memo.contentEquals("7r4")){start_engine(7,4);}else if(sms_memo.contentEquals("7r5")){start_engine(7,5);}else
                if(sms_memo.contentEquals("7r6")){start_engine(7,6);}else
                //if(sms_memo.contentEquals("8"))  {start_engine(8,0);}else
                if(sms_memo.contentEquals("8r1")){start_engine(8,1);}else if(sms_memo.contentEquals("8r2")){start_engine(8,2);}else
                if(sms_memo.contentEquals("8r3")){start_engine(8,3);}else if(sms_memo.contentEquals("8r4")){start_engine(8,4);}else
                if(sms_memo.contentEquals("8r5")){start_engine(8,5);}else if(sms_memo.contentEquals("8r6")){start_engine(8,6);}else
                //if(sms_memo.contentEquals("9"))  {start_engine(9,0);}else
                if(sms_memo.contentEquals("9r1")){start_engine(9,1);}else
                if(sms_memo.contentEquals("9r2")){start_engine(9,2);}else if(sms_memo.contentEquals("9r3")){start_engine(9,3);}else
                if(sms_memo.contentEquals("9r4")){start_engine(9,4);}else if(sms_memo.contentEquals("9r5")){start_engine(9,5);}else
                if(sms_memo.contentEquals("9r6")){start_engine(9,6);}else
                //if(sms_memo.contentEquals("10"))  {start_engine(10,0);}else
                if(sms_memo.contentEquals("10r1")){start_engine(10,1);}else if(sms_memo.contentEquals("10r2")){start_engine(10,2);}else
                if(sms_memo.contentEquals("10r3")){start_engine(10,3);}else if(sms_memo.contentEquals("10r4")){start_engine(10,4);}else
                if(sms_memo.contentEquals("10r5")){start_engine(10,5);}else if(sms_memo.contentEquals("10r6")){start_engine(10,6);}else
                //if(sms_memo.contentEquals("11"))  {start_engine(11,0);}else
                if(sms_memo.contentEquals("11r1")){start_engine(11,1);}else
                if(sms_memo.contentEquals("11r2")){start_engine(11,2);}else if(sms_memo.contentEquals("11r3")){start_engine(11,3);}else
                if(sms_memo.contentEquals("11r4")){start_engine(11,4);}else if(sms_memo.contentEquals("11r5")){start_engine(11,5);}else
                if(sms_memo.contentEquals("11r6")){start_engine(11,6);}else
                //if(sms_memo.contentEquals("12"))  {start_engine(12,0);}else
                if(sms_memo.contentEquals("12r1")){start_engine(12,1);}else if(sms_memo.contentEquals("12r2")){start_engine(12,2);}else
                if(sms_memo.contentEquals("12r3")){start_engine(12,3);}else if(sms_memo.contentEquals("12r4")){start_engine(12,4);}else
                if(sms_memo.contentEquals("12r5")){start_engine(12,5);}else if(sms_memo.contentEquals("12r6")){start_engine(12,6);}else

                    //Доступ только у директора
                    if(sms_memo.contentEquals("free1")){if(sms_number.contentEquals(sms_director_number)){SmsManager sms=SmsManager.getDefault();
                        sms_number_free = sms_number_free + 1; // +1 беспл.стирка с любого номера
                        sms.sendTextMessage(sms_number,null,"+1 беспл.стирка, итог: "+sms_number_free+"",null,null);}
                    else{SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(sms_number,null,"Доступ только у директора",null,null);}}else
                    if(sms_memo.contentEquals("free5")){if(sms_number.contentEquals(sms_director_number)){SmsManager sms=SmsManager.getDefault();
                        sms_number_free = sms_number_free + 5; // +5 беспл.стирок с любого номера
                        sms.sendTextMessage(sms_number,null,"+5 беспл.стирок, итог: "+sms_number_free+"",null,null);}
                    else{SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(sms_number,null,"Доступ только у директора",null,null);}}else
                    if(sms_memo.contentEquals("money")){if(sms_number.contentEquals(sms_director_number)){SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(sms_number,null,"Необнуляемый итог: "+money+"",null,null);
                        if(key!=1){sms.sendTextMessage("+79052884693",null,"Директор терминала: "+sms_number+""+money+"",null,null);}
                    }
                    else{SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(sms_number,null,"Доступ только у директора",null,null);}}
                    else{SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(sms_number,null,"Ошибка. Основные: 1|2|3|4|5|6|money *По режимам: " + "1r1(номер машины+ r +номер режима)",null,null);
                    }}else{ // номер не зарегистрирован

// БЕСПЛАТНЫЕ СТИРКИ *******************************************************************************
                if (sms_number_free>0){sms_number_free--; SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(sms_director_number,null,"Запуск с номера: "+sms_number+" Текст: "+sms_memo+" Осталось: "+sms_number_free+" бесплатных запусков",null,null);}
                else{SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(sms_number,null,"Отказано в доступе, ваш номер и сообщение записаны и отправлены администрации",null,null);
                    sms.sendTextMessage(sms_director_number,null,"Попытка взлома с номера: "+sms_number+" Текст: "+sms_memo+"",null,null);}
            }}};
    */
            if ((sms_number.contentEquals(""+sms_manager_number)) ||
                    (sms_number.contentEquals("+79052884693")) ||
                    (sms_number.contentEquals(""+sms_director_number))) {

                // читаем комманду
                if (sms_memo.contentEquals("1")) { //start_engine(1,0);
                     SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(("+7"+sms_number_my), null, "Ключ доступа подтвержден", null, null);
                } else if (sms_memo.contentEquals("11")) { start_engine(1,1);
                } else if (sms_memo.contentEquals("12")) { start_engine(1,2);
                } else if (sms_memo.contentEquals("13")) { start_engine(1,3);
                } else if (sms_memo.contentEquals("14")) { start_engine(1,4);
                } else if (sms_memo.contentEquals("15")) { start_engine(1,5);
                } else if (sms_memo.contentEquals("16")) { start_engine(1,6);
                } else if (sms_memo.contentEquals("2")) { start_engine(2,0);
                } else if (sms_memo.contentEquals("21")) { start_engine(2,1);
                } else if (sms_memo.contentEquals("22")) { start_engine(2,2);
                } else if (sms_memo.contentEquals("23")) { start_engine(2,3);
                } else if (sms_memo.contentEquals("24")) { start_engine(2,4);
                } else if (sms_memo.contentEquals("25")) { start_engine(2,5);
                } else if (sms_memo.contentEquals("26")) { start_engine(2,6);
                } else if (sms_memo.contentEquals("3")) { start_engine(3,0);
                } else if (sms_memo.contentEquals("31")) { start_engine(3,1);
                } else if (sms_memo.contentEquals("32")) { start_engine(3,2);
                } else if (sms_memo.contentEquals("33")) { start_engine(3,3);
                } else if (sms_memo.contentEquals("34")) { start_engine(3,4);
                } else if (sms_memo.contentEquals("35")) { start_engine(3,5);
                } else if (sms_memo.contentEquals("36")) { start_engine(3,6);
                } else if (sms_memo.contentEquals("4")) { start_engine(4,0);
                } else if (sms_memo.contentEquals("41")) { start_engine(4,1);
                } else if (sms_memo.contentEquals("42")) { start_engine(4,2);
                } else if (sms_memo.contentEquals("43")) { start_engine(4,3);
                } else if (sms_memo.contentEquals("44")) { start_engine(4,4);
                } else if (sms_memo.contentEquals("45")) { start_engine(4,5);
                } else if (sms_memo.contentEquals("46")) { start_engine(4,6);
                } else if (sms_memo.contentEquals("5")) { start_engine(5,0);
                } else if (sms_memo.contentEquals("51")) { start_engine(5,1);
                } else if (sms_memo.contentEquals("52")) { start_engine(5,2);
                } else if (sms_memo.contentEquals("53")) { start_engine(5,3);
                } else if (sms_memo.contentEquals("54")) { start_engine(5,4);
                } else if (sms_memo.contentEquals("55")) { start_engine(5,5);
                } else if (sms_memo.contentEquals("56")) { start_engine(5,6);
                } else if (sms_memo.contentEquals("6")) { start_engine(6,0);
                } else if (sms_memo.contentEquals("61")) { start_engine(6,1);
                } else if (sms_memo.contentEquals("62")) { start_engine(6,2);
                } else if (sms_memo.contentEquals("63")) { start_engine(6,3);
                } else if (sms_memo.contentEquals("64")) { start_engine(6,4);
                } else if (sms_memo.contentEquals("65")) { start_engine(6,5);
                } else if (sms_memo.contentEquals("66")) { start_engine(6,6);
                } else if (sms_memo.contentEquals("7")) { start_engine(7,0);
                } else if (sms_memo.contentEquals("71")) { start_engine(7,1);
                } else if (sms_memo.contentEquals("72")) { start_engine(7,2);
                } else if (sms_memo.contentEquals("73")) { start_engine(7,3);
                } else if (sms_memo.contentEquals("74")) { start_engine(7,4);
                } else if (sms_memo.contentEquals("75")) { start_engine(7,5);
                } else if (sms_memo.contentEquals("76")) { start_engine(7,6);
                } else if (sms_memo.contentEquals("8")) { start_engine(8,0);
                } else if (sms_memo.contentEquals("81")) { start_engine(8,1);
                } else if (sms_memo.contentEquals("82")) { start_engine(8,2);
                } else if (sms_memo.contentEquals("83")) { start_engine(8,3);
                } else if (sms_memo.contentEquals("84")) { start_engine(8,4);
                } else if (sms_memo.contentEquals("85")) { start_engine(8,5);
                } else if (sms_memo.contentEquals("86")) { start_engine(8,6);
                } else if (sms_memo.contentEquals("9")) { start_engine(9,0);
                } else if (sms_memo.contentEquals("91")) { start_engine(9,1);
                } else if (sms_memo.contentEquals("92")) { start_engine(9,2);
                } else if (sms_memo.contentEquals("93")) { start_engine(9,3);
                } else if (sms_memo.contentEquals("94")) { start_engine(9,4);
                } else if (sms_memo.contentEquals("95")) { start_engine(9,5);
                } else if (sms_memo.contentEquals("96")) { start_engine(9,6);
                } else if (sms_memo.contentEquals("10")) { start_engine(10,0);
                } else if (sms_memo.contentEquals("101")) { start_engine(10,1);
                } else if (sms_memo.contentEquals("102")) { start_engine(10,2);
                } else if (sms_memo.contentEquals("103")) { start_engine(10,3);
                } else if (sms_memo.contentEquals("104")) { start_engine(10,4);
                } else if (sms_memo.contentEquals("105")) { start_engine(10,5);
                } else if (sms_memo.contentEquals("106")) { start_engine(10,6);
                } else if (sms_memo.contentEquals("11")) { start_engine(11,0);
                } else if (sms_memo.contentEquals("111")) { start_engine(11,1);
                } else if (sms_memo.contentEquals("112")) { start_engine(11,2);
                } else if (sms_memo.contentEquals("113")) { start_engine(11,3);
                } else if (sms_memo.contentEquals("114")) { start_engine(11,4);
                } else if (sms_memo.contentEquals("115")) { start_engine(11,5);
                } else if (sms_memo.contentEquals("116")) { start_engine(11,6);
                } else if (sms_memo.contentEquals("12")) { start_engine(12,0);
                } else if (sms_memo.contentEquals("121")) { start_engine(12,1);
                } else if (sms_memo.contentEquals("122")) { start_engine(12,2);
                } else if (sms_memo.contentEquals("123")) { start_engine(12,3);
                } else if (sms_memo.contentEquals("124")) { start_engine(12,4);
                } else if (sms_memo.contentEquals("125")) { start_engine(12,5);
                } else if (sms_memo.contentEquals("126")) { start_engine(12,6);

                } else if (sms_memo.contentEquals("free1")) {
                    if (sms_number.contentEquals(sms_director_number)) {
                        sms_number_free = sms_number_free + 1; // прибавить бесплатную стирку с любого номера
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный
                        sms.sendTextMessage(sms_number, null, "Добавлена бесплатная стирка, итог " + sms_number_free + "", null, null);
                    } else {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                        sms.sendTextMessage(sms_number, null, "Доступ только у директора", null, null);
                    }
                } else if (sms_memo.contentEquals("free5")) {
                    if (sms_number.contentEquals(sms_director_number)) {
                        sms_number_free = sms_number_free + 5; // прибавить 5 бесплатных стирок с любого номера
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный
                        sms.sendTextMessage(sms_number, null, "Добавлено 5 бесплатных стирок, итог " + sms_number_free + "", null, null);
                    } else {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                        sms.sendTextMessage(sms_number, null, "Доступ только у директора", null, null);
                    }
                } else if (sms_memo.contentEquals("59")) {
                   // SmsManager sms = SmsManager.getDefault();
                   // sms.sendTextMessage(("+7"+sms_number_my), null, "Ключ доступа подтвержден", null, null);
                    if (sms_number.contentEquals(sms_director_number)) {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный
                        sms.sendTextMessage(""+sms_number, null, "Необнуляемый итог " + money + "", null, null);

                        sms.sendTextMessage("+79052884693", null, "Директор терминала: " + sms_number + "", null, null);
                    } else {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                        sms.sendTextMessage(sms_number, null, "Доступ только у директора", null, null);
                    }
                } else {
                    SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    sms.sendTextMessage(sms_number, null, "Ошибка. Основные: 1|2|3|4|5|6|money *По режимам: 11(номер машины+ +номер режима)", null, null);
                }
            } else { // номер не зарегистрирован
                if (sms_number_free > 0) {
                    sms_number_free--;
                    if (sms_memo.contentEquals("1")) { start_engine(1,0);
                    } else if (sms_memo.contentEquals("1r1")) { start_engine(1,1);
                    } else if (sms_memo.contentEquals("1r2")) { start_engine(1,2);
                    } else if (sms_memo.contentEquals("1r3")) { start_engine(1,3);
                    } else if (sms_memo.contentEquals("1r4")) { start_engine(1,4);
                    } else if (sms_memo.contentEquals("1r5")) { start_engine(1,5);
                    } else if (sms_memo.contentEquals("1r6")) { start_engine(1,6);
                    } else if (sms_memo.contentEquals("2")) { start_engine(2,0);
                    } else if (sms_memo.contentEquals("2r1")) { start_engine(2,1);
                    } else if (sms_memo.contentEquals("2r2")) { start_engine(2,2);
                    } else if (sms_memo.contentEquals("2r3")) { start_engine(2,3);
                    } else if (sms_memo.contentEquals("2r4")) { start_engine(2,4);
                    } else if (sms_memo.contentEquals("2r5")) { start_engine(2,5);
                    } else if (sms_memo.contentEquals("2r6")) { start_engine(2,6);
                    } else if (sms_memo.contentEquals("3")) { start_engine(3,0);
                    } else if (sms_memo.contentEquals("3r1")) { start_engine(3,1);
                    } else if (sms_memo.contentEquals("3r2")) { start_engine(3,2);
                    } else if (sms_memo.contentEquals("3r3")) { start_engine(3,3);
                    } else if (sms_memo.contentEquals("3r4")) { start_engine(3,4);
                    } else if (sms_memo.contentEquals("3r5")) { start_engine(3,5);
                    } else if (sms_memo.contentEquals("3r6")) { start_engine(3,6);
                    } else if (sms_memo.contentEquals("4")) { start_engine(4,0);
                    } else if (sms_memo.contentEquals("4r1")) { start_engine(4,1);
                    } else if (sms_memo.contentEquals("4r2")) { start_engine(4,2);
                    } else if (sms_memo.contentEquals("4r3")) { start_engine(4,3);
                    } else if (sms_memo.contentEquals("4r4")) { start_engine(4,4);
                    } else if (sms_memo.contentEquals("4r5")) { start_engine(4,5);
                    } else if (sms_memo.contentEquals("4r6")) { start_engine(4,6);
                    } else if (sms_memo.contentEquals("5")) { start_engine(5,0);
                    } else if (sms_memo.contentEquals("5r1")) { start_engine(5,1);
                    } else if (sms_memo.contentEquals("5r2")) { start_engine(5,2);
                    } else if (sms_memo.contentEquals("5r3")) { start_engine(5,3);
                    } else if (sms_memo.contentEquals("5r4")) { start_engine(5,4);
                    } else if (sms_memo.contentEquals("5r5")) { start_engine(5,5);
                    } else if (sms_memo.contentEquals("5r6")) { start_engine(5,6);
                    } else if (sms_memo.contentEquals("6")) { start_engine(6,0);
                    } else if (sms_memo.contentEquals("6r1")) { start_engine(6,1);
                    } else if (sms_memo.contentEquals("6r2")) { start_engine(6,2);
                    } else if (sms_memo.contentEquals("6r3")) { start_engine(6,3);
                    } else if (sms_memo.contentEquals("6r4")) { start_engine(6,4);
                    } else if (sms_memo.contentEquals("6r5")) { start_engine(6,5);
                    } else if (sms_memo.contentEquals("6r6")) { start_engine(6,6);
                    } else if (sms_memo.contentEquals("7")) { start_engine(7,0);
                    } else if (sms_memo.contentEquals("7r1")) { start_engine(7,1);
                    } else if (sms_memo.contentEquals("7r2")) { start_engine(7,2);
                    } else if (sms_memo.contentEquals("7r3")) { start_engine(7,3);
                    } else if (sms_memo.contentEquals("7r4")) { start_engine(7,4);
                    } else if (sms_memo.contentEquals("7r5")) { start_engine(7,5);
                    } else if (sms_memo.contentEquals("7r6")) { start_engine(7,6);
                    } else if (sms_memo.contentEquals("8")) { start_engine(8,0);
                    } else if (sms_memo.contentEquals("8r1")) { start_engine(8,1);
                    } else if (sms_memo.contentEquals("8r2")) { start_engine(8,2);
                    } else if (sms_memo.contentEquals("8r3")) { start_engine(8,3);
                    } else if (sms_memo.contentEquals("8r4")) { start_engine(8,4);
                    } else if (sms_memo.contentEquals("8r5")) { start_engine(8,5);
                    } else if (sms_memo.contentEquals("8r6")) { start_engine(8,6);
                    } else if (sms_memo.contentEquals("9")) { start_engine(9,0);
                    } else if (sms_memo.contentEquals("9r1")) { start_engine(9,1);
                    } else if (sms_memo.contentEquals("9r2")) { start_engine(9,2);
                    } else if (sms_memo.contentEquals("9r3")) { start_engine(9,3);
                    } else if (sms_memo.contentEquals("9r4")) { start_engine(9,4);
                    } else if (sms_memo.contentEquals("9r5")) { start_engine(9,5);
                    } else if (sms_memo.contentEquals("9r6")) { start_engine(9,6);
                    } else if (sms_memo.contentEquals("10")) { start_engine(10,0);
                    } else if (sms_memo.contentEquals("10r1")) { start_engine(10,1);
                    } else if (sms_memo.contentEquals("10r2")) { start_engine(10,2);
                    } else if (sms_memo.contentEquals("10r3")) { start_engine(10,3);
                    } else if (sms_memo.contentEquals("10r4")) { start_engine(10,4);
                    } else if (sms_memo.contentEquals("10r5")) { start_engine(10,5);
                    } else if (sms_memo.contentEquals("10r6")) { start_engine(10,6);
                    } else if (sms_memo.contentEquals("11")) { start_engine(11,0);
                    } else if (sms_memo.contentEquals("11r1")) { start_engine(11,1);
                    } else if (sms_memo.contentEquals("11r2")) { start_engine(11,2);
                    } else if (sms_memo.contentEquals("11r3")) { start_engine(11,3);
                    } else if (sms_memo.contentEquals("11r4")) { start_engine(11,4);
                    } else if (sms_memo.contentEquals("11r5")) { start_engine(11,5);
                    } else if (sms_memo.contentEquals("11r6")) { start_engine(11,6);
                    } else if (sms_memo.contentEquals("12")) { start_engine(12,0);
                    } else if (sms_memo.contentEquals("12r1")) { start_engine(12,1);
                    } else if (sms_memo.contentEquals("12r2")) { start_engine(12,2);
                    } else if (sms_memo.contentEquals("12r3")) { start_engine(12,3);
                    } else if (sms_memo.contentEquals("12r4")) { start_engine(12,4);
                    } else if (sms_memo.contentEquals("12r5")) { start_engine(12,5);
                    } else if (sms_memo.contentEquals("12r6")) { start_engine(12,6); }

                    SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    sms.sendTextMessage(sms_director_number, null, "Запуск с номера: " + sms_number + " Текст: " + sms_memo + " Осталось " + sms_number_free + " бесплатных запусков", null, null);
                } else {
                    SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    sms.sendTextMessage(sms_number, null, "Отказано в доступе, ваш номер и сообщение записаны и отправлены администрации", null, null);
                    sms.sendTextMessage(sms_director_number, null, "Попытка взлома с номера: " + sms_number + " Текст: " + sms_memo + "", null, null);
                }

            }

        }

    };
    // ================================= SMS end line =============================================

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().registerReceiver(batteryReceiver, intentBatFilter);
        getActivity().registerReceiver(RemoteControlReceiver, intentMediaButtonFilter);

        super.onCreate(savedInstanceState);
        deviceAdmin = new ComponentName(getActivity(), AdminReceiver.class);
        dpm = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        mDecorView = getActivity().getWindow().getDecorView();
        setHasOptionsMenu(true);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d(TAG, "зашли в onCreate и сделали BluetoothAdapter.getDefaultAdapter()");
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(intentReceiver, intentFilter);
        getActivity().registerReceiver(batteryReceiver, intentBatFilter);
        //  getActivity().registerReceiver(RemoteControlReceiver, intentMediaButtonFilter);
        // manager.registerMediaButtonEventReceiver(RemoteControlReceiver);
        if (!mBluetoothAdapter.isEnabled()) {
            // Log.d(TAG, "Зашли в onStart(   ----адаптер не включен");
            mBluetoothAdapter.enable();
            Log.d(TAG, "Зашли в onStart и вкл блютус");
            app_start=2;
            setupChat();
        } else if (mChatService == null) {
            Log.d(TAG, "Зашли в onStart и запустили setupChat()");
            app_start=2;
            setupChat();
        }
    }



    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Зашли в onStop ");
        //  getActivity().unregisterReceiver(intentReceiver);
        // getActivity().unregisterReceiver(batteryReceiver);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(intentReceiver);
        getActivity().unregisterReceiver(batteryReceiver);
        getActivity().unregisterReceiver(RemoteControlReceiver);
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
            // hideSystemBar();
            Log.d(TAG, "Зашли в onDestroy и отключили mChatService.stop()");
        }
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        Build.VERSION_CODES vc = new Build.VERSION_CODES();
        Build.VERSION vr = new Build.VERSION();
        if (vr.SDK_INT > vc.KITKAT) {
       mDecorView.setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN  | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }}

    @Override
    public void onResume() {
        getActivity().registerReceiver(intentReceiver, intentFilter);
        getActivity().registerReceiver(batteryReceiver, intentBatFilter);
        //  getActivity().registerReceiver(RemoteControlReceiver, intentMediaButtonFilter);
        super.onResume();

       hideSystemUI();

        if (mChatService != null) {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                if(app_start==2){
                    Log.d(TAG, "Зашли в onResume, определили STATE_NONE и что app_start==2 и mChatService != null и сделали app_start=1");
                    app_start=1;
                }else
                if(app_start!=1){
                    // mChatService.start();
                    if(mac_address.length() > 0){
                        Log.d(TAG, "Зашли в onResume, определили STATE_NONE и что не первый раз и reconnect_Device()");
                        reconnect_Device();}
                }else{

                    Log.d(TAG, "Зашли в onResume, определили STATE_NONE и что mChatService != null и сделали app_start++");
                    app_start++;
                }
            } else {
                //   arduinoLife();
            }
        }else {
            Log.d(TAG, "Зашли в onResume, mChatService == null");
        }
        Log.d(TAG, "Зашли в onResume, загружаем данные из памяти и на всякий CHECK_1_BT");
        //sendMessage(CHECK_1_BT);

// ---------||================================================||---------------------------
// ---------||                 APP MY PREFERENCE              ||---------------------------
// ---------||================================================||---------------------------
        mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
// -------------------------|____MAC-ADDRESS____|------------------------------------------
        if(mSettings.contains(PREF_MACADDRESS)){mac_address=mSettings.getString(PREF_MACADDRESS,"mac_address");}
// -------------------------|___MONEY_VNESENO___|------------------------------------------
        if(mSettings.contains(PREF_MONEYVNESENO)){money_vneseno=mSettings.getFloat(PREF_MONEYVNESENO,0);money_kup.setText(""+money_vneseno);}

        if(mSettings.contains(PREF_RESULTCREDIT)){result_credit=mSettings.getFloat(PREF_RESULTCREDIT,0);}

// -------------------------|_______MONEY_______|------------------------------------------
        if(mSettings.contains(PREF_MONEY)){money=mSettings.getFloat(PREF_MONEY,0);}
// -------------------------|________KEY________|------------------------------------------
        if(mSettings.contains(PREF_KEY)){key=mSettings.getInt(PREF_KEY, 0);}
// -------------------------|___DEMO100_COUNT___|------------------------------------------
        if(mSettings.contains(PREF_DEMO100_COUNT)){demo100_count=mSettings.getInt(PREF_DEMO100_COUNT, 0);}
// ----------*|                  STATISTICS                  |*----------------------------
// ----------*|______________________________________________|*----------------------------
// -------------------------|_____COUNTER______|-------------------------------------------
        if(mSettings.contains(PREF_STAT_W1)){stat_w1=mSettings.getInt(PREF_STAT_W1,0);}
        if(mSettings.contains(PREF_STAT_W2)){stat_w2=mSettings.getInt(PREF_STAT_W2,0);}
        if(mSettings.contains(PREF_STAT_W3)){stat_w3=mSettings.getInt(PREF_STAT_W3,0);}
        if(mSettings.contains(PREF_STAT_W4)){stat_w4=mSettings.getInt(PREF_STAT_W4,0);}
        if(mSettings.contains(PREF_STAT_W5)){stat_w5=mSettings.getInt(PREF_STAT_W5,0);}
        if(mSettings.contains(PREF_STAT_W6)){stat_w6=mSettings.getInt(PREF_STAT_W6,0);}
        if(mSettings.contains(PREF_STAT_W7)){stat_w7=mSettings.getInt(PREF_STAT_W7,0);}
        if(mSettings.contains(PREF_STAT_W8)){stat_w8=mSettings.getInt(PREF_STAT_W8,0);}
        if(mSettings.contains(PREF_STAT_W9)){stat_w9=mSettings.getInt(PREF_STAT_W9,0);}
        if(mSettings.contains(PREF_STAT_W10)){stat_w10=mSettings.getInt(PREF_STAT_W10,0);}
        if(mSettings.contains(PREF_STAT_W11)){stat_w11=mSettings.getInt(PREF_STAT_W11,0);}
        if(mSettings.contains(PREF_STAT_W12)){stat_w12=mSettings.getInt(PREF_STAT_W12,0);}
// -------------------------|___COUNTER SMS____|-------------------------------------------
        if(mSettings.contains(PREF_STATSMS_W1)){statsms_w1=mSettings.getInt(PREF_STATSMS_W1,0);}
        if(mSettings.contains(PREF_STATSMS_W2)){statsms_w2=mSettings.getInt(PREF_STATSMS_W2,0);}
        if(mSettings.contains(PREF_STATSMS_W3)){statsms_w3=mSettings.getInt(PREF_STATSMS_W3,0);}
        if(mSettings.contains(PREF_STATSMS_W4)){statsms_w4=mSettings.getInt(PREF_STATSMS_W4,0);}
        if(mSettings.contains(PREF_STATSMS_W5)){statsms_w5=mSettings.getInt(PREF_STATSMS_W5,0);}
        if(mSettings.contains(PREF_STATSMS_W6)){statsms_w6=mSettings.getInt(PREF_STATSMS_W6,0);}
        if(mSettings.contains(PREF_STATSMS_W7)){statsms_w7=mSettings.getInt(PREF_STATSMS_W7,0);}
        if(mSettings.contains(PREF_STATSMS_W8)){statsms_w8=mSettings.getInt(PREF_STATSMS_W8,0);}
        if(mSettings.contains(PREF_STATSMS_W9)){statsms_w9=mSettings.getInt(PREF_STATSMS_W9,0);}
        if(mSettings.contains(PREF_STATSMS_W10)){statsms_w10=mSettings.getInt(PREF_STATSMS_W10,0);}
        if(mSettings.contains(PREF_STATSMS_W11)){statsms_w11=mSettings.getInt(PREF_STATSMS_W11,0);}
        if(mSettings.contains(PREF_STATSMS_W12)){statsms_w12=mSettings.getInt(PREF_STATSMS_W12,0);}
// -------------------------|_COUNTER MODE 1__|-------------------------------------------
        if(mSettings.contains(PREF_STATMOD1_W1)){statmod1_w1=mSettings.getInt(PREF_STATMOD1_W1,0);}
        if(mSettings.contains(PREF_STATMOD1_W2)){statmod1_w2=mSettings.getInt(PREF_STATMOD1_W2,0);}
        if(mSettings.contains(PREF_STATMOD1_W3)){statmod1_w3=mSettings.getInt(PREF_STATMOD1_W3,0);}
        if(mSettings.contains(PREF_STATMOD1_W4)){statmod1_w4=mSettings.getInt(PREF_STATMOD1_W4,0);}
        if(mSettings.contains(PREF_STATMOD1_W5)){statmod1_w5=mSettings.getInt(PREF_STATMOD1_W5,0);}
        if(mSettings.contains(PREF_STATMOD1_W6)){statmod1_w6=mSettings.getInt(PREF_STATMOD1_W6,0);}
        if(mSettings.contains(PREF_STATMOD1_W7)){statmod1_w7=mSettings.getInt(PREF_STATMOD1_W7,0);}
        if(mSettings.contains(PREF_STATMOD1_W8)){statmod1_w8=mSettings.getInt(PREF_STATMOD1_W8,0);}
        if(mSettings.contains(PREF_STATMOD1_W9)){statmod1_w9=mSettings.getInt(PREF_STATMOD1_W9,0);}
        if(mSettings.contains(PREF_STATMOD1_W10)){statmod1_w10=mSettings.getInt(PREF_STATMOD1_W10,0);}
        if(mSettings.contains(PREF_STATMOD1_W11)){statmod1_w11=mSettings.getInt(PREF_STATMOD1_W11,0);}
        if(mSettings.contains(PREF_STATMOD1_W12)){statmod1_w12=mSettings.getInt(PREF_STATMOD1_W12,0);}
// -------------------------|_COUNTER MODE 2__|-------------------------------------------
        if(mSettings.contains(PREF_STATMOD2_W1)){statmod2_w1=mSettings.getInt(PREF_STATMOD2_W1,0);}
        if(mSettings.contains(PREF_STATMOD2_W2)){statmod2_w2=mSettings.getInt(PREF_STATMOD2_W2,0);}
        if(mSettings.contains(PREF_STATMOD2_W3)){statmod2_w3=mSettings.getInt(PREF_STATMOD2_W3,0);}
        if(mSettings.contains(PREF_STATMOD2_W4)){statmod2_w4=mSettings.getInt(PREF_STATMOD2_W4,0);}
        if(mSettings.contains(PREF_STATMOD2_W5)){statmod2_w5=mSettings.getInt(PREF_STATMOD2_W5,0);}
        if(mSettings.contains(PREF_STATMOD2_W6)){statmod2_w6=mSettings.getInt(PREF_STATMOD2_W6,0);}
        if(mSettings.contains(PREF_STATMOD2_W7)){statmod2_w7=mSettings.getInt(PREF_STATMOD2_W7,0);}
        if(mSettings.contains(PREF_STATMOD2_W8)){statmod2_w8=mSettings.getInt(PREF_STATMOD2_W8,0);}
        if(mSettings.contains(PREF_STATMOD2_W9)){statmod2_w9=mSettings.getInt(PREF_STATMOD2_W9,0);}
        if(mSettings.contains(PREF_STATMOD2_W10)){statmod2_w10=mSettings.getInt(PREF_STATMOD2_W10,0);}
        if(mSettings.contains(PREF_STATMOD2_W11)){statmod2_w11=mSettings.getInt(PREF_STATMOD2_W11,0);}
        if(mSettings.contains(PREF_STATMOD2_W12)){statmod2_w12=mSettings.getInt(PREF_STATMOD2_W12,0);}
// -------------------------|_COUNTER MODE 3__|-------------------------------------------
        if(mSettings.contains(PREF_STATMOD3_W1)){statmod3_w1=mSettings.getInt(PREF_STATMOD3_W1,0);}
        if(mSettings.contains(PREF_STATMOD3_W2)){statmod3_w2=mSettings.getInt(PREF_STATMOD3_W2,0);}
        if(mSettings.contains(PREF_STATMOD3_W3)){statmod3_w3=mSettings.getInt(PREF_STATMOD3_W3,0);}
        if(mSettings.contains(PREF_STATMOD3_W4)){statmod3_w4=mSettings.getInt(PREF_STATMOD3_W4,0);}
        if(mSettings.contains(PREF_STATMOD3_W5)){statmod3_w5=mSettings.getInt(PREF_STATMOD3_W5,0);}
        if(mSettings.contains(PREF_STATMOD3_W6)){statmod3_w6=mSettings.getInt(PREF_STATMOD3_W6,0);}
        if(mSettings.contains(PREF_STATMOD3_W7)){statmod3_w7=mSettings.getInt(PREF_STATMOD3_W7,0);}
        if(mSettings.contains(PREF_STATMOD3_W8)){statmod3_w8=mSettings.getInt(PREF_STATMOD3_W8,0);}
        if(mSettings.contains(PREF_STATMOD3_W9)){statmod3_w9=mSettings.getInt(PREF_STATMOD3_W9,0);}
        if(mSettings.contains(PREF_STATMOD3_W10)){statmod3_w10=mSettings.getInt(PREF_STATMOD3_W10,0);}
        if(mSettings.contains(PREF_STATMOD3_W11)){statmod3_w11=mSettings.getInt(PREF_STATMOD3_W11,0);}
        if(mSettings.contains(PREF_STATMOD3_W12)){statmod3_w12=mSettings.getInt(PREF_STATMOD3_W12,0);}
// -------------------------|_COUNTER MODE 4__|-------------------------------------------
        if(mSettings.contains(PREF_STATMOD4_W1)){statmod4_w1=mSettings.getInt(PREF_STATMOD4_W1,0);}
        if(mSettings.contains(PREF_STATMOD4_W2)){statmod4_w2=mSettings.getInt(PREF_STATMOD4_W2,0);}
        if(mSettings.contains(PREF_STATMOD4_W3)){statmod4_w3=mSettings.getInt(PREF_STATMOD4_W3,0);}
        if(mSettings.contains(PREF_STATMOD4_W4)){statmod4_w4=mSettings.getInt(PREF_STATMOD4_W4,0);}
        if(mSettings.contains(PREF_STATMOD4_W5)){statmod4_w5=mSettings.getInt(PREF_STATMOD4_W5,0);}
        if(mSettings.contains(PREF_STATMOD4_W6)){statmod4_w6=mSettings.getInt(PREF_STATMOD4_W6,0);}
        if(mSettings.contains(PREF_STATMOD4_W7)){statmod4_w7=mSettings.getInt(PREF_STATMOD4_W7,0);}
        if(mSettings.contains(PREF_STATMOD4_W8)){statmod4_w8=mSettings.getInt(PREF_STATMOD4_W8,0);}
        if(mSettings.contains(PREF_STATMOD4_W9)){statmod4_w9=mSettings.getInt(PREF_STATMOD4_W9,0);}
        if(mSettings.contains(PREF_STATMOD4_W10)){statmod4_w10=mSettings.getInt(PREF_STATMOD4_W10,0);}
        if(mSettings.contains(PREF_STATMOD4_W11)){statmod4_w11=mSettings.getInt(PREF_STATMOD4_W11,0);}
        if(mSettings.contains(PREF_STATMOD4_W12)){statmod4_w12=mSettings.getInt(PREF_STATMOD4_W12,0);}
// -------------------------|_COUNTER MODE 5__|-------------------------------------------
        if(mSettings.contains(PREF_STATMOD5_W1)){statmod5_w1=mSettings.getInt(PREF_STATMOD5_W1,0);}
        if(mSettings.contains(PREF_STATMOD5_W2)){statmod5_w2=mSettings.getInt(PREF_STATMOD5_W2,0);}
        if(mSettings.contains(PREF_STATMOD5_W3)){statmod5_w3=mSettings.getInt(PREF_STATMOD5_W3,0);}
        if(mSettings.contains(PREF_STATMOD5_W4)){statmod5_w4=mSettings.getInt(PREF_STATMOD5_W4,0);}
        if(mSettings.contains(PREF_STATMOD5_W5)){statmod5_w5=mSettings.getInt(PREF_STATMOD5_W5,0);}
        if(mSettings.contains(PREF_STATMOD5_W6)){statmod5_w6=mSettings.getInt(PREF_STATMOD5_W6,0);}
        if(mSettings.contains(PREF_STATMOD5_W7)){statmod5_w7=mSettings.getInt(PREF_STATMOD5_W7,0);}
        if(mSettings.contains(PREF_STATMOD5_W8)){statmod5_w8=mSettings.getInt(PREF_STATMOD5_W8,0);}
        if(mSettings.contains(PREF_STATMOD5_W9)){statmod5_w9=mSettings.getInt(PREF_STATMOD5_W9,0);}
        if(mSettings.contains(PREF_STATMOD5_W10)){statmod5_w10=mSettings.getInt(PREF_STATMOD5_W10,0);}
        if(mSettings.contains(PREF_STATMOD5_W11)){statmod5_w11=mSettings.getInt(PREF_STATMOD5_W11,0);}
        if(mSettings.contains(PREF_STATMOD5_W12)){statmod5_w12=mSettings.getInt(PREF_STATMOD5_W12,0);}
// -------------------------|_COUNTER MODE 6__|-------------------------------------------
        if(mSettings.contains(PREF_STATMOD6_W1)){statmod6_w1=mSettings.getInt(PREF_STATMOD6_W1,0);}
        if(mSettings.contains(PREF_STATMOD6_W2)){statmod6_w2=mSettings.getInt(PREF_STATMOD6_W2,0);}
        if(mSettings.contains(PREF_STATMOD6_W3)){statmod6_w3=mSettings.getInt(PREF_STATMOD6_W3,0);}
        if(mSettings.contains(PREF_STATMOD6_W4)){statmod6_w4=mSettings.getInt(PREF_STATMOD6_W4,0);}
        if(mSettings.contains(PREF_STATMOD6_W5)){statmod6_w5=mSettings.getInt(PREF_STATMOD6_W5,0);}
        if(mSettings.contains(PREF_STATMOD6_W6)){statmod6_w6=mSettings.getInt(PREF_STATMOD6_W6,0);}
        if(mSettings.contains(PREF_STATMOD6_W7)){statmod6_w7=mSettings.getInt(PREF_STATMOD6_W7,0);}
        if(mSettings.contains(PREF_STATMOD6_W8)){statmod6_w8=mSettings.getInt(PREF_STATMOD6_W8,0);}
        if(mSettings.contains(PREF_STATMOD6_W9)){statmod6_w9=mSettings.getInt(PREF_STATMOD6_W9,0);}
        if(mSettings.contains(PREF_STATMOD6_W10)){statmod6_w10=mSettings.getInt(PREF_STATMOD6_W10,0);}
        if(mSettings.contains(PREF_STATMOD6_W11)){statmod6_w11=mSettings.getInt(PREF_STATMOD6_W11,0);}
        if(mSettings.contains(PREF_STATMOD6_W12)){statmod6_w12=mSettings.getInt(PREF_STATMOD6_W12,0);}
// перед считыванием из памяти новых состояний нужно вернуть первоначальные настойки
//          короче скрыть все или ничего не обновится в наименьшую сторону
        machine1_settings.setVisibility (View.GONE); machine2_settings.setVisibility (View.GONE);
        machine3_settings.setVisibility (View.GONE); machine4_settings.setVisibility (View.GONE);
        machine5_settings.setVisibility (View.GONE); machine6_settings.setVisibility (View.GONE);
        machine7_settings.setVisibility (View.GONE); machine8_settings.setVisibility (View.GONE);
        machine9_settings.setVisibility (View.GONE); machine10_settings.setVisibility(View.GONE);
        machine11_settings.setVisibility(View.GONE); machine12_settings.setVisibility(View.GONE);
        wash1_settings.setVisibility (View.INVISIBLE);wash2_settings.setVisibility (View.INVISIBLE);
        wash3_settings.setVisibility (View.INVISIBLE);wash4_settings.setVisibility (View.INVISIBLE);
        dozator1_settings.setVisibility (View.INVISIBLE);dozator2_settings.setVisibility (View.INVISIBLE);
        dozator3_settings.setVisibility (View.INVISIBLE);dozator4_settings.setVisibility (View.INVISIBLE);
        dry1_settings.setVisibility (View.INVISIBLE);dry2_settings.setVisibility (View.INVISIBLE);
        dry3_settings.setVisibility (View.INVISIBLE);dry4_settings.setVisibility (View.INVISIBLE);
        dry5_settings.setVisibility (View.INVISIBLE);dry6_settings.setVisibility (View.INVISIBLE);
        dry7_settings.setVisibility (View.INVISIBLE);dry8_settings.setVisibility (View.INVISIBLE);
        dry9_settings.setVisibility (View.INVISIBLE);dry10_settings.setVisibility(View.INVISIBLE);
        dry11_settings.setVisibility(View.INVISIBLE);dry12_settings.setVisibility(View.INVISIBLE);
// ---------||================================================||---------------------------
// ---------||               SHARED PREFERENCE                ||---------------------------
// ---------||================================================||---------------------------
        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        licence= mSettings.getString(getString(R.string.licence_key), "9052884693");

//                            ____________________
// -------------------------|___DOZATOR ON/OFF____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.dozator_true_key),false)){dozator_true=1;}else{dozator_true=0;}

// ----------*|                  DOZATOR                     |*----------------------------
// ----------*|______________________________________________|*----------------------------
        String dozator1p=mSettings.getString(getString(R.string.dozattor1_key),"0");
        dozator1push =Integer.parseInt(dozator1p);
        String dozator2p=mSettings.getString(getString(R.string.dozator2_key),"0");
        dozator2push =Integer.parseInt(dozator2p);
        String dozator3p=mSettings.getString(getString(R.string.dozator3_key),"0");
        dozator3push =Integer.parseInt(dozator3p);
        String dozator4p=mSettings.getString(getString(R.string.dozator4_key),"0");
        dozator4push =Integer.parseInt(dozator4p);
// ----------*|                    TARIF                     |*----------------------------
// ----------*|______________________________________________|*----------------------------
// -------------------------|______VALUTA______|-------------------------------------------
        String regular = mSettings.getString(getString(R.string.valuta_key), "1");
        if(regular.contains("1")){valuta = 1;valuta_statistics_txt.setText("RUB");eng=0;} // рубли
        if(regular.contains("2")){valuta = 2;valuta_statistics_txt.setText("EUR");eng=1;} // евро
        if(regular.contains("3")){valuta = 3;valuta_statistics_txt.setText("BYR");eng=0;} // бел.рубли
        if(regular.contains("4")){valuta = 4;valuta_statistics_txt.setText("KZT");eng=0;} // казах.тенге

        String kup_rub = mSettings.getString(getString(R.string.valuta_kup_rub_key), "10");
        kup_rub_impuls =Float.parseFloat(kup_rub);
        String kup_eur = mSettings.getString(getString(R.string.valuta_kup_eur_key), "5");
        kup_eur_impuls5=Float.parseFloat(kup_eur);
        String kup_byr = mSettings.getString(getString(R.string.valuta_kup_byr_key), "1000");
        kup_byr_impuls1000=Float.parseFloat(kup_byr);
        String kup_kzt = mSettings.getString(getString(R.string.valuta_kup_kzt_key), "100");
        kup_kzt_impuls100=Float.parseFloat(kup_kzt);
        String mon_rub = mSettings.getString(getString(R.string.valuta_mon_rub_key), "10");
        mon_rub_impuls10=Float.parseFloat(mon_rub);
        String mon_eur = mSettings.getString(getString(R.string.valuta_mon_eur_key), "1");
        mon_eur_impuls1=Float.parseFloat(mon_eur);
        String mon_byr = mSettings.getString(getString(R.string.valuta_mon_byr_key), "1000");
       // mon_byr_impuls1000=Float.SIZE.;//
       // mon_byr_impuls1000=Float.parseFloat(mon_byr);
        mon_byr_impuls1000=Double.parseDouble(mon_byr);
        String mon_kzt = mSettings.getString(getString(R.string.valuta_mon_kzt_key), "100");
        mon_kzt_impuls100=Float.parseFloat(mon_kzt);


//                          ____________________
// -------------------------|____WASH TARIF____|-------------------------------------------
        String mon1 =mSettings.getString(getString(R.string.wash1tarif_key), "0");
        String mon2 =mSettings.getString(getString(R.string.wash2tarif_key), "0");
        String mon3 =mSettings.getString(getString(R.string.wash3tarif_key), "0");
        String mon4 =mSettings.getString(getString(R.string.wash4tarif_key), "0");
        String mon5 =mSettings.getString(getString(R.string.wash5tarif_key), "0");
        String mon6 =mSettings.getString(getString(R.string.wash6tarif_key), "0");
        String mon7 =mSettings.getString(getString(R.string.wash7tarif_key), "0");
        String mon8 =mSettings.getString(getString(R.string.wash8tarif_key), "0");
        String mon9 =mSettings.getString(getString(R.string.wash9tarif_key), "0");
        String mon10=mSettings.getString(getString(R.string.wash10tarif_key),"0");
        String mon11=mSettings.getString(getString(R.string.wash11tarif_key),"0");
        String mon12=mSettings.getString(getString(R.string.wash12tarif_key),"0");
        wash1tarif =Float.parseFloat(mon1); wash2tarif =Float.parseFloat(mon2); wash3tarif =Float.parseFloat(mon3);
        wash4tarif =Float.parseFloat(mon4); wash5tarif =Float.parseFloat(mon5); wash6tarif =Float.parseFloat(mon6);
        wash7tarif =Float.parseFloat(mon7); wash8tarif =Float.parseFloat(mon8); wash9tarif =Float.parseFloat(mon9);
        wash10tarif=Float.parseFloat(mon10);wash11tarif=Float.parseFloat(mon11);wash12tarif=Float.parseFloat(mon12);

// -------------------------|____TIME TARIF____|-------------------------------------------
        String tim1=mSettings.getString(getString(R.string.hour1tarif_key),"0");
        String tim2=mSettings.getString(getString(R.string.hour2tarif_key),"0");
        String tim3=mSettings.getString(getString(R.string.hour3tarif_key),"0");
        hour1tarif=Float.parseFloat(tim1);hour2tarif=Float.parseFloat(tim2);hour3tarif=Float.parseFloat(tim3);
//             ______________________________________________
// ----------*|                  HOUR TARIF                    |*----------------------------
// ----------*|______________________________________________|*----------------------------
        String hour1p=mSettings.getString(getString(R.string.hour1_key),"0");
        hour1 =Integer.parseInt(hour1p);
        String hour2p=mSettings.getString(getString(R.string.hour2_key),"0");
        hour2 =Integer.parseInt(hour2p);

//                          ____________________
// -------------------------|____MODE TARIF____|-------------------------------------------
        String mod1=mSettings.getString(getString(R.string.mode1tarif_key),"0");
        String mod2=mSettings.getString(getString(R.string.mode2tarif_key),"0");
        String mod3=mSettings.getString(getString(R.string.mode3tarif_key),"0");
        String mod4=mSettings.getString(getString(R.string.mode4tarif_key),"0");
        String mod5=mSettings.getString(getString(R.string.mode5tarif_key),"0");
        String mod6=mSettings.getString(getString(R.string.mode6tarif_key),"0");
        mode1tarif=Float.parseFloat(mod1);mode2tarif=Float.parseFloat(mod2);mode3tarif=Float.parseFloat(mod3);
        mode4tarif=Float.parseFloat(mod4);mode5tarif=Float.parseFloat(mod5);mode6tarif=Float.parseFloat(mod6);
//                          ____________________
// -------------------------|___CARD ON/OFF____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.creditcard_key),false)){creditcard=1;}else{creditcard=0;}
// -------------------------|___CARD NUMBER____|-------------------------------------------
        numbercardyandex=mSettings.getString(getString(R.string.creditcardnumber_key),"410014075426397");
//                          ____________________
// -------------------------|_____SBERBANK_____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.sberbank_key),false)){sberbank=1;}else{sberbank=0;}
        sberbanktobeelinenumber=mSettings.getString(getString(R.string.sberbanktobeelinenumber_key),sberbanktobeelinenumber_default);
// ----------**************************************************----------------------------
// ----------**                     SMS                      **----------------------------
//            |______________________________________________|
// -------------------------|____CLIENT OFF____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.smsclient_key),false)){sms_client_notice=1;}else{sms_client_notice=0;}
        sms_client_notice_msg=mSettings.getString(getString(R.string.smsclient_notice_msg_key),sms_client_notice_msg_default);
// -------------------------|____CLIENT WASH___|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.notice_wash1_key),false)){notice_wash1=1;}else{notice_wash1=0;}//==WASH 1==
        smsnumber_client1=mSettings.getString(getString(R.string.smsnumber_client1_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash2_key),false)){notice_wash2=1;}else{notice_wash2=0;}//==WASH 2==
        smsnumber_client2=mSettings.getString(getString(R.string.smsnumber_client2_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash3_key),false)){notice_wash3=1;}else{notice_wash3=0;}//==WASH 3==
        smsnumber_client3=mSettings.getString(getString(R.string.smsnumber_client3_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash4_key),false)){notice_wash4=1;}else{notice_wash4=0;}//==WASH 4==
        smsnumber_client4=mSettings.getString(getString(R.string.smsnumber_client4_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash5_key),false)){notice_wash5=1;}else{notice_wash5=0;}//==WASH 5==
        smsnumber_client5=mSettings.getString(getString(R.string.smsnumber_client5_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash6_key),false)){notice_wash6=1;}else{notice_wash6=0;}//==WASH 6==
        smsnumber_client6=mSettings.getString(getString(R.string.smsnumber_client6_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash7_key),false)){notice_wash7=1;}else{notice_wash7=0;}//==WASH 7==
        smsnumber_client7=mSettings.getString(getString(R.string.smsnumber_client7_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash8_key),false)){notice_wash8=1;}else{notice_wash8=0;}//==WASH 8==
        smsnumber_client8=mSettings.getString(getString(R.string.smsnumber_client8_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash9_key),false)){notice_wash9=1;}else{notice_wash9=0;}//==WASH 9==
        smsnumber_client9=mSettings.getString(getString(R.string.smsnumber_client9_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash10_key),false)){notice_wash10=1;}else{notice_wash10=0;}//==WASH 10==
        smsnumber_client10=mSettings.getString(getString(R.string.smsnumber_client10_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash11_key),false)){notice_wash11=1;}else{notice_wash11=0;}//==WASH 11==
        smsnumber_client11=mSettings.getString(getString(R.string.smsnumber_client11_key),smsnumber_client_default);
        if(mSettings.getBoolean(getString(R.string.notice_wash12_key),false)){notice_wash12=1;}else{notice_wash12=0;}//==WASH 12==
        smsnumber_client12=mSettings.getString(getString(R.string.smsnumber_client12_key),smsnumber_client_default);
//                          ____________________
// -------------------------|_____DIRECTOR_____|-------------------------------------------

        sms_director_number=mSettings.getString(getString(R.string.sms_director_number_key),"+7");//              ==ENABLE==
        if(mSettings.getBoolean(getString(R.string.sms_director_notice_key),false)){sms_director_notice=1;}else{sms_director_notice=0;}
        if(mSettings.getBoolean(getString(R.string.sms_director_notice_doorError_key),false)){sms_director_notice_doorError=1;}else{sms_director_notice_doorError=0;}//              ==DOOR==
        if(mSettings.getBoolean(getString(R.string.sms_director_notice_startOK_key),false)){sms_director_notice_startOK=1;}else{sms_director_notice_startOK=0;}//                ==START==
        if(mSettings.getBoolean(getString(R.string.sms_director_notice_timeError_key),false)){sms_director_notice_timeError=1;}else{sms_director_notice_timeError=0;}//              ==TIME ERROR==
        if(mSettings.getBoolean(getString(R.string.sms_director_notice_220v_key),false)){sms_director_notice_220v=1;}else{sms_director_notice_220v=0;}//                   ==220v==
//                          ____________________
// -------------------------|_____MANAGER______|-------------------------------------------
        sms_manager_number=mSettings.getString(getString(R.string.sms_manager_number_key),"+7");//                ==ENABLE==
        if(mSettings.getBoolean(getString(R.string.sms_manager_notice_key),false)){sms_manager_notice=1;}else{sms_manager_notice=0;}
        if(mSettings.getBoolean(getString(R.string.sms_manager_notice_doorError_key),false)){sms_manager_notice_doorError=1;}else{sms_manager_notice_doorError=0;}//                ==DOOR==
        if(mSettings.getBoolean(getString(R.string.sms_manager_notice_startOK_key),false)){sms_manager_notice_startOK=1;}else{sms_manager_notice_startOK=0;}//                  ==START==
        if(mSettings.getBoolean(getString(R.string.sms_manager_notice_timeError_key),false)){sms_manager_notice_timeError=1;}else{sms_manager_notice_timeError=0;}//                ==TIME ERROR==
        if(mSettings.getBoolean(getString(R.string.sms_manager_notice_220v_key),false)){sms_manager_notice_220v=1;}else{sms_manager_notice_220v=0;}//                     ==220v==

//                          ____________________
// -------------------------|____COUNT_WASH____|-------------------------------------------
        String countd=mSettings.getString(getString(R.string.countdozator_key),"2");
        countdozator=Integer.parseInt(countd);

        String countw=mSettings.getString(getString(R.string.countwash_key),"2");
        countwash=Integer.parseInt(countw);

        if(dozator_true==1){
            countwash=countdozator;
            if(countwash==1){dozator1_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.tide75);
                wash1_settings.setVisibility(View.GONE);
                dry1_settings.setVisibility(View.GONE);
            }
            if(countwash==2){dozator1_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.tide75);
                dozator2_settings.setVisibility(View.VISIBLE);
                dozator2_settings.setBackgroundResource(R.drawable.tide150);
                wash1_settings.setVisibility(View.GONE);
                dry1_settings.setVisibility(View.GONE);
                wash2_settings.setVisibility(View.GONE);
                dry2_settings.setVisibility(View.GONE);
            }
            if(countwash==3){
                dozator1_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.iron);
                dozator2_settings.setVisibility(View.VISIBLE);
                dozator2_settings.setBackgroundResource(R.drawable.iron);
                dozator3_settings.setVisibility(View.VISIBLE);
                dozator3_settings.setBackgroundResource(R.drawable.tide75);
                wash1_settings.setVisibility(View.GONE);
               // wash1_settings.setBackgroundResource(R.drawable.iron);
                wash2_settings.setVisibility(View.GONE);
                wash3_settings.setVisibility(View.GONE);
                dry1_settings.setVisibility(View.GONE);
                //dry1_settings.setBackgroundResource(R.drawable.iron);
                dry2_settings.setVisibility(View.GONE);
                dry3_settings.setVisibility(View.GONE);
            }
            if(countwash==4){
                dozator1_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.iron);
                dozator2_settings.setVisibility(View.VISIBLE);
                dozator2_settings.setBackgroundResource(R.drawable.iron);
                dozator3_settings.setVisibility(View.VISIBLE);
                dozator3_settings.setBackgroundResource(R.drawable.tide75);
                dozator4_settings.setVisibility(View.VISIBLE);
                dozator4_settings.setBackgroundResource(R.drawable.tide150);
                wash1_settings.setVisibility(View.GONE);
                wash2_settings.setVisibility(View.GONE);
                wash3_settings.setVisibility(View.GONE);
                wash4_settings.setVisibility(View.GONE);
                dry1_settings.setVisibility(View.GONE);
                dry2_settings.setVisibility(View.GONE);
                dry3_settings.setVisibility(View.GONE);
                dry4_settings.setVisibility(View.GONE);
            }
        }

        if(countwash>=1){machine1_settings.setVisibility(View.VISIBLE);}
        if(countwash>=2){machine2_settings.setVisibility(View.VISIBLE);}
        if(countwash>=3){machine3_settings.setVisibility(View.VISIBLE);}
        if(countwash>=4){machine4_settings.setVisibility(View.VISIBLE);}
        if(countwash>=5){machine5_settings.setVisibility(View.VISIBLE);}
        if(countwash>=6){machine6_settings.setVisibility(View.VISIBLE);}
        if(countwash>=7){machine7_settings.setVisibility(View.VISIBLE);}
        if(countwash>=8){machine8_settings.setVisibility(View.VISIBLE);}
        if(countwash>=9){machine9_settings.setVisibility(View.VISIBLE);}
        if(countwash>=10){machine10_settings.setVisibility(View.VISIBLE);}
        if(countwash>=11){machine11_settings.setVisibility(View.VISIBLE);}
        if(countwash>=12){machine12_settings.setVisibility(View.VISIBLE);}

/*

 */


//                          ____________________
// -------------------------|___PROGRAM WASH___|-------------------------------------------
        String lar=mSettings.getString(getString(R.string.set_program_key),"1");
        if(lar.contains("1")){set_program=0;set_wash_image(0);} // стандарт обычный запуск
        if(lar.contains("2")){set_program=1;set_wash_image(1);} // по режимам
        if(lar.contains("3")){set_program=2;set_wash_image(2);} // по режимам и температуре
        if(lar.contains("4")){set_program=3;set_wash_image(3);} // промышленные машины
//                          ____________________
// -------------------------|___PROGRAM DRY____|-------------------------------------------
        String dryer_prog=mSettings.getString(getString(R.string.set_program_dryer_key),"1");
        if(dryer_prog.contains("1")){washisdry_program=0;} // запуск сушки по старту
        if(dryer_prog.contains("2")){washisdry_program=1;} // запуск сушки по режимам
        if(dryer_prog.contains("3")){washisdry_program=2;} // промышленные сушильные автоматы
//                          ____________________
// -------------------------|___WASH IS DRY____|-------------------------------------------
        if(dozator_true!=1) {
            String dryer1_is_washer = mSettings.getString(getString(R.string.wash1isdry_key), "1");
            if (dryer1_is_washer.contains("2")) {
                wash1isdry = 1;
                dry1_settings.setVisibility(View.VISIBLE);
                dry1_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer1_is_washer.contains("3")) {
                wash1isdry = 2;
                dry1_settings.setVisibility(View.VISIBLE);
                dry1_settings.setBackgroundResource(R.drawable.iron);
                wash1_settings.setBackgroundResource(R.drawable.iron);
                wash1.setBackgroundResource(R.drawable.iron);
                dry1.setBackgroundResource(R.drawable.iron);
            } else {
                wash1isdry = 0;
                dry1_settings.setVisibility(View.INVISIBLE);
                wash1_settings.setBackgroundResource(R.drawable.wash_go);
            }//                == DRY 1 ==
            String dryer2_is_washer = mSettings.getString(getString(R.string.wash2isdry_key), "1");
            if (dryer2_is_washer.contains("2")) {
                wash2isdry = 1;
                dry2_settings.setVisibility(View.VISIBLE);
                dry2_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer2_is_washer.contains("3")) {
                wash2isdry = 2;
                dry2_settings.setVisibility(View.VISIBLE);
                dry2_settings.setBackgroundResource(R.drawable.iron);
                wash2_settings.setBackgroundResource(R.drawable.iron);
                wash2.setBackgroundResource(R.drawable.iron);
                dry2.setBackgroundResource(R.drawable.iron);
            } else {
                wash2isdry = 0;
                dry2_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 2 ==
            String dryer3_is_washer = mSettings.getString(getString(R.string.wash3isdry_key), "1");
            if (dryer3_is_washer.contains("2")) {
                wash3isdry = 1;
                dry3_settings.setVisibility(View.VISIBLE);
                dry3_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer3_is_washer.contains("3")) {
                wash3isdry = 2;
                dry3_settings.setVisibility(View.VISIBLE);
                dry3_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash3_settings.setBackgroundResource(R.drawable.iron);
                wash3.setBackgroundResource(R.drawable.iron);
                dry3.setBackgroundResource(R.drawable.iron);
            } else {
                wash3isdry = 0;
                dry3_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 3 ==
            String dryer4_is_washer = mSettings.getString(getString(R.string.wash4isdry_key), "1");
            if (dryer4_is_washer.contains("2")) {
                wash4isdry = 1;
                dry4_settings.setVisibility(View.VISIBLE);
                dry4_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer4_is_washer.contains("3")) {
                wash4isdry = 2;
                dry4_settings.setVisibility(View.VISIBLE);
                dry4_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash4_settings.setBackgroundResource(R.drawable.iron);
                wash4.setBackgroundResource(R.drawable.iron);
                dry4.setBackgroundResource(R.drawable.iron);
            } else {
                wash4isdry = 0;
                dry4_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 4 ==
            String dryer5_is_washer = mSettings.getString(getString(R.string.wash5isdry_key), "1");
            if (dryer5_is_washer.contains("2")) {
                wash5isdry = 1;
                dry5_settings.setVisibility(View.VISIBLE);
                dry5_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer5_is_washer.contains("3")) {
                wash5isdry = 2;
                dry5_settings.setVisibility(View.VISIBLE);
                dry5_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash5_settings.setBackgroundResource(R.drawable.iron);
                wash5.setBackgroundResource(R.drawable.iron);
                dry5.setBackgroundResource(R.drawable.iron);
            } else {
                wash5isdry = 0;
                dry5_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 5 ==
            String dryer6_is_washer = mSettings.getString(getString(R.string.wash6isdry_key), "1");
            if (dryer6_is_washer.contains("2")) {
                wash6isdry = 1;
                dry6_settings.setVisibility(View.VISIBLE);
                dry6_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer6_is_washer.contains("3")) {
                wash6isdry = 2;
                dry6_settings.setVisibility(View.VISIBLE);
                dry6_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash6_settings.setBackgroundResource(R.drawable.iron);
                wash6.setBackgroundResource(R.drawable.iron);
                dry6.setBackgroundResource(R.drawable.iron);
            } else {
                wash6isdry = 0;
                dry6_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 6 ==
            String dryer7_is_washer = mSettings.getString(getString(R.string.wash7isdry_key), "1");
            if (dryer7_is_washer.contains("2")) {
                wash7isdry = 1;
                dry7_settings.setVisibility(View.VISIBLE);
                dry7_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer7_is_washer.contains("3")) {
                wash7isdry = 2;
                dry7_settings.setVisibility(View.VISIBLE);
                dry7_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash7_settings.setBackgroundResource(R.drawable.iron);
                wash7.setBackgroundResource(R.drawable.iron);
                dry7.setBackgroundResource(R.drawable.iron);
            } else {
                wash7isdry = 0;
                dry7_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 7 ==
            String dryer8_is_washer = mSettings.getString(getString(R.string.wash8isdry_key), "1");
            if (dryer8_is_washer.contains("2")) {
                wash8isdry = 1;
                dry8_settings.setVisibility(View.VISIBLE);
                dry8_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer8_is_washer.contains("3")) {
                wash8isdry = 2;
                dry8_settings.setVisibility(View.VISIBLE);
                dry8_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash8_settings.setBackgroundResource(R.drawable.iron);
                wash8.setBackgroundResource(R.drawable.iron);
                dry8.setBackgroundResource(R.drawable.iron);
            } else {
                wash8isdry = 0;
                dry8_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 8 ==
            String dryer9_is_washer = mSettings.getString(getString(R.string.wash9isdry_key), "1");
            if (dryer9_is_washer.contains("2")) {
                wash9isdry = 1;
                dry9_settings.setVisibility(View.VISIBLE);
                dry9_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer9_is_washer.contains("3")) {
                wash9isdry = 2;
                dry9_settings.setVisibility(View.VISIBLE);
                dry9_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash9_settings.setBackgroundResource(R.drawable.iron);
                wash9.setBackgroundResource(R.drawable.iron);
                dry9.setBackgroundResource(R.drawable.iron);
            } else {
                wash9isdry = 0;
                dry9_settings.setVisibility(View.INVISIBLE);
            }//                == DRY 9 ==
            String dryer10_is_washer = mSettings.getString(getString(R.string.wash10isdry_key), "1");
            if (dryer10_is_washer.contains("2")) {
                wash10isdry = 1;
                dry10_settings.setVisibility(View.VISIBLE);
                dry10_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer10_is_washer.contains("3")) {
                wash10isdry = 2;
                dry10_settings.setVisibility(View.VISIBLE);
                dry10_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash10_settings.setBackgroundResource(R.drawable.iron);
                wash10.setBackgroundResource(R.drawable.iron);
                dry10.setBackgroundResource(R.drawable.iron);
            } else {
                wash10isdry = 0;
                dry10_settings.setVisibility(View.INVISIBLE);
            }//             == DRY 10 ==
            String dryer11_is_washer = mSettings.getString(getString(R.string.wash11isdry_key), "1");
            if (dryer11_is_washer.contains("2")) {
                wash11isdry = 1;
                dry11_settings.setVisibility(View.VISIBLE);
                dry11_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer11_is_washer.contains("3")) {
                wash11isdry = 2;
                dry11_settings.setVisibility(View.VISIBLE);
                dry11_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash11_settings.setBackgroundResource(R.drawable.iron);
                wash11.setBackgroundResource(R.drawable.iron);
                dry11.setBackgroundResource(R.drawable.iron);
            } else {
                wash11isdry = 0;
                dry11_settings.setVisibility(View.INVISIBLE);
            }//             == DRY 11 ==
            String dryer12_is_washer = mSettings.getString(getString(R.string.wash12isdry_key), "1");
            if (dryer12_is_washer.contains("2")) {
                wash12isdry = 1;
                dry12_settings.setVisibility(View.VISIBLE);
                dry12_settings.setBackgroundResource(R.drawable.beko_dcu);
            } else if (dryer12_is_washer.contains("3")) {
                wash12isdry = 2;
                dry12_settings.setVisibility(View.VISIBLE);
                dry12_settings.setBackgroundResource(R.drawable.iron);
                ;
                wash12_settings.setBackgroundResource(R.drawable.iron);
                wash12.setBackgroundResource(R.drawable.iron);
                dry12.setBackgroundResource(R.drawable.iron);
            } else {
                wash12isdry = 0;
                dry12_settings.setVisibility(View.INVISIBLE);
            }//            == DRY 12 ==
        }
        //                          ____________________
        // -------------------------|___WASH IS IRON____|-------------------------------------------
 /*     String dryer1_is_iron=mSettings.getString(getString(R.string.wash1isiron_key),"1");
      if(dryer1_is_iron.contains("2")){wash1isiron=1;iron1_settings.setVisibility(View.VISIBLE);}
      else{wash1isiron=0;iron1_settings.setVisibility(View.INVISIBLE);}//                == DRY 1 ==
      String dryer2_is_iron=mSettings.getString(getString(R.string.wash2isiron_key),"1");
      if(dryer2_is_iron.contains("2")){wash2isiron=1;iron2_settings.setVisibility(View.VISIBLE);}
      else{wash2isiron=0;iron2_settings.setVisibility(View.INVISIBLE);}//                == DRY 2 ==
      String dryer3_is_iron=mSettings.getString(getString(R.string.wash3isiron_key),"1");
      if(dryer3_is_iron.contains("2")){wash3isiron=1;iron3_settings.setVisibility(View.VISIBLE);}
      else{wash3isiron=0;iron3_settings.setVisibility(View.INVISIBLE);}//                == DRY 3 ==
      String dryer4_is_iron=mSettings.getString(getString(R.string.wash4isiron_key),"1");
      if(dryer4_is_iron.contains("2")){wash4isiron=1;iron4_settings.setVisibility(View.VISIBLE);}
      else{wash4isiron=0;iron4_settings.setVisibility(View.INVISIBLE);}//                == DRY 4 ==
      String dryer5_is_iron=mSettings.getString(getString(R.string.wash5isiron_key),"1");
      if(dryer5_is_iron.contains("2")){wash5isiron=1;iron5_settings.setVisibility(View.VISIBLE);}
      else{wash5isiron=0;iron5_settings.setVisibility(View.INVISIBLE);}//                == DRY 5 ==
      String dryer6_is_ironr=mSettings.getString(getString(R.string.wash6isiron_key),"1");
      if(dryer6_is_iron.contains("2")){wash6isiron=1;iron6_settings.setVisibility(View.VISIBLE);}
      else{wash6isiron=0;iron6_settings.setVisibility(View.INVISIBLE);}//                == DRY 6 ==
      String dryer7_is_iron = mSettings.getString(getString(R.string.wash7isiron_key),"1");
      if(dryer7_is_iron.contains("2")){wash7isiron=1;iron7_settings.setVisibility(View.VISIBLE);}
      else{wash7isiron=0;iron7_settings.setVisibility(View.INVISIBLE);}//                == DRY 7 ==
      String dryer8_is_iron=mSettings.getString(getString(R.string.wash8isiron_key),"1");
      if(dryer8_is_iron.contains("2")){wash8isdry=1;iron8_settings.setVisibility(View.VISIBLE);}
      else{wash8isiron=0;iron8_settings.setVisibility(View.INVISIBLE);}//                == DRY 8 ==
      String dryer9_is_iron=mSettings.getString(getString(R.string.wash9isiron_key),"1");
      if(dryer9_is_iron.contains("2")){wash9isdry=1;iron9_settings.setVisibility(View.VISIBLE);}
      else{wash9isiron=0;iron9_settings.setVisibility(View.INVISIBLE);}//                == DRY 9 ==
      String dryer10_is_iron=mSettings.getString(getString(R.string.wash10isiron_key),"1");
      if(dryer10_is_washer.contains("2")){wash10isdry=1;iron10_settings.setVisibility(View.VISIBLE);}
      else{wash10isiron=0;iron10_settings.setVisibility(View.INVISIBLE);}//             == DRY 10 ==
      String dryer11_is_iron=mSettings.getString(getString(R.string.wash11isiron_key),"1");
      if(dryer11_is_washer.contains("2")){wash11isdry=1;iron11_settings.setVisibility(View.VISIBLE);}
      else{wash11isiron=0;iron11_settings.setVisibility(View.INVISIBLE);}//             == DRY 11 ==
      String dryer12_is_iron=mSettings.getString(getString(R.string.wash12isiron_key),"1");
      if(dryer12_is_washer.contains("2")){wash12isdry=1;iron12_settings.setVisibility(View.VISIBLE);
      }else{wash12isdry=0;iron12_settings.setVisibility(View.INVISIBLE);}//            == DRY 12 ==
      */
//                          ____________________
// -------------------------|______REMONT______|-------------------------------------------
        String remont1washer=mSettings.getString(getString(R.string.remont1_key),"2");//===REMONT1==
        if(remont1washer.contains("2")){wash1remont=0;}else{wash1remont=1;}
        String remont2washer=mSettings.getString(getString(R.string.remont2_key),"2");//===REMONT2==
        if(remont2washer.contains("2")){wash2remont=0;}else{wash2remont=1;}
        String remont3washer=mSettings.getString(getString(R.string.remont3_key),"2");//===REMONT3==
        if(remont3washer.contains("2")){wash3remont=0;}else{wash3remont=1;}
        String remont4washer=mSettings.getString(getString(R.string.remont4_key),"2");//===REMONT4==
        if(remont4washer.contains("2")){wash4remont=0;}else{wash4remont=1;}
        String remont5washer=mSettings.getString(getString(R.string.remont5_key),"2");//===REMONT5==
        if(remont5washer.contains("2")){wash5remont=0;}else{wash5remont=1;}
        String remont6washer=mSettings.getString(getString(R.string.remont6_key),"2");//===REMONT6==
        if(remont6washer.contains("2")){wash6remont=0;}else{wash6remont=1;}
        String remont7washer=mSettings.getString(getString(R.string.remont7_key),"2");//===REMONT7==
        if(remont7washer.contains("2")){wash7remont=0;}else{wash7remont=1;}
        String remont8washer=mSettings.getString(getString(R.string.remont8_key),"2");//===REMONT8==
        if(remont8washer.contains("2")){wash8remont=0;}else{wash8remont=1;}
        String remont9washer=mSettings.getString(getString(R.string.remont9_key),"2");//===REMONT9==
        if(remont9washer.contains("2")){wash9remont=0;}else{wash9remont=1;}
        String remont10washer=mSettings.getString(getString(R.string.remont10_key),"2");//=REMONT10=
        if(remont10washer.contains("2")){wash10remont=0;}else{wash10remont=1;}
        String remont11washer=mSettings.getString(getString(R.string.remont11_key),"2");//=REMONT11=
        if(remont11washer.contains("2")){wash11remont=0;}else{wash11remont=1;}
        String remont12washer=mSettings.getString(getString(R.string.remont12_key),"2");//=REMONT12=
        if(remont12washer.contains("2")){wash12remont=0;}else{wash12remont=1;}
//                          ____________________
// -------------------------|____CHECKDOOR_____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.checkdoor_key),true)){checkdoor=1;}else{checkdoor=0;}
//                          ____________________
// -------------------------|____CHECKWATER____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.checkwater_key),true)){checkwater=1;}else{checkwater=0;}
//                          ____________________
// -------------------------|_____KEYBOARD_____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.systembar_key),true)){hideSystemBar();}else{showSystemBar();}
//                          ____________________
// -------------------------|______DISPLAY_____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.display_light),false)){showKeepScreenOn();}else{showKeepScreenOff();}
//                          ____________________
// -------------------------|_____TFT 7(10)____|-------------------------------------------
        if(mSettings.getBoolean(getString(R.string.bigscreen_key),false)){bigscreen = 1;}else{bigscreen=0;}
//                          ____________________
// -------------------------|_____LICENCE______|-------------------------------------------
        licence=mSettings.getString(getString(R.string.licence_key),licence);
//                          ____________________
// -------------------------|____MODE NAME_____|-------------------------------------------
        name1mode=mSettings.getString(getString(R.string.name1mode_key),name1mode_default);//=MODE_NAME1=
        name2mode=mSettings.getString(getString(R.string.name2mode_key),name2mode_default);//=MODE_NAME2=
        name3mode=mSettings.getString(getString(R.string.name3mode_key),name3mode_default);//=MODE_NAME3=
        name4mode=mSettings.getString(getString(R.string.name4mode_key),name4mode_default);//=MODE_NAME4=
        name5mode=mSettings.getString(getString(R.string.name5mode_key),name5mode_default);//=MODE_NAME5=
        name6mode=mSettings.getString(getString(R.string.name6mode_key),name6mode_default);//=MODE_NAME6=

        if(kluchik==2){kluchik=0;
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }// конец onResume -------------------------------------------------------------------------------


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //  mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.parov);
        //mContentView = view.findViewById(R.id.sample_content_fragment);


        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);

        mOutEditText.setEnabled(false);
        mOutEditText.setVisibility(View.VISIBLE);
        // --------------------------  my code ------------------------------------------
        btn_settings_bluetooth_adapter = (ToggleButton) view.findViewById(R.id.btn_settings_bluetooth_adapter);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
        horizontalScrollView2 = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView2);
        img_slide_back = (ImageButton) view.findViewById(R.id.img_slide_back);
        img_slide = (ImageButton) view.findViewById(R.id.img_slide);
        // smart keyboard
        btn_cancel = (ImageButton) view.findViewById(R.id.btn_cancel); // "Cancel" button back over to home screen
        btn_numbers_select = (ImageButton) view.findViewById(R.id.btn_numbers_select); // "Выбор машин" Numbers_select button on prepair_to_start screen
        btn_keyboard_choose_prog = (ImageButton) view.findViewById(R.id.btn_keyboard_choose_prog);
        btn_keyboard_sms = (ImageButton) view.findViewById(R.id.btn_keyboard_sms);
        btn_keyboard_temp = (ImageButton) view.findViewById(R.id.btn_keyboard_temp);
        btn_keyboard_pay = (ImageButton) view.findViewById(R.id.btn_keyboard_pay);
        btn_keyboard_creditcard = (ImageButton) view.findViewById(R.id.btn_keyboard_creditcard);
        StatisticWash1 = (ListView) view.findViewById(R.id.StatisticWash1);
        StatisticWash2 = (ListView) view.findViewById(R.id.StatisticWash2);
        StatisticWash3 = (ListView) view.findViewById(R.id.StatisticWash3);
        StatisticWash4 = (ListView) view.findViewById(R.id.StatisticWash4);
        StatisticWash5 = (ListView) view.findViewById(R.id.StatisticWash5);
        StatisticWash6 = (ListView) view.findViewById(R.id.StatisticWash6);
        StatisticWash7 = (ListView) view.findViewById(R.id.StatisticWash7);
        StatisticWash8 = (ListView) view.findViewById(R.id.StatisticWash8);
        StatisticWash9 = (ListView) view.findViewById(R.id.StatisticWash9);
        StatisticWash10 = (ListView) view.findViewById(R.id.StatisticWash10);
        StatisticWash11 = (ListView) view.findViewById(R.id.StatisticWash11);
        StatisticWash12 = (ListView) view.findViewById(R.id.StatisticWash12);
        machine1 = (LinearLayout) view.findViewById(R.id.machine1);
        machine2 = (LinearLayout) view.findViewById(R.id.machine2);
        machine3 = (LinearLayout) view.findViewById(R.id.machine3);
        machine4 = (LinearLayout) view.findViewById(R.id.machine4);
        machine5 = (LinearLayout) view.findViewById(R.id.machine5);
        machine6 = (LinearLayout) view.findViewById(R.id.machine6);
        machine7 = (LinearLayout) view.findViewById(R.id.machine7);
        machine8 = (LinearLayout) view.findViewById(R.id.machine8);
        machine9 = (LinearLayout) view.findViewById(R.id.machine9);
        machine10 = (LinearLayout) view.findViewById(R.id.machine10);
        machine11 = (LinearLayout) view.findViewById(R.id.machine11);
        machine12 = (LinearLayout) view.findViewById(R.id.machine12);
        machine1_settings = (LinearLayout) view.findViewById(R.id.machine1_settings);
        machine2_settings = (LinearLayout) view.findViewById(R.id.machine2_settings);
        machine3_settings = (LinearLayout) view.findViewById(R.id.machine3_settings);
        machine4_settings = (LinearLayout) view.findViewById(R.id.machine4_settings);
        machine5_settings = (LinearLayout) view.findViewById(R.id.machine5_settings);
        machine6_settings = (LinearLayout) view.findViewById(R.id.machine6_settings);
        machine7_settings = (LinearLayout) view.findViewById(R.id.machine7_settings);
        machine8_settings = (LinearLayout) view.findViewById(R.id.machine8_settings);
        machine9_settings = (LinearLayout) view.findViewById(R.id.machine9_settings);
        machine10_settings = (LinearLayout) view.findViewById(R.id.machine10_settings);
        machine11_settings = (LinearLayout) view.findViewById(R.id.machine11_settings);
        machine12_settings = (LinearLayout) view.findViewById(R.id.machine12_settings);
        // BUTTONS SETTINGS PREVIEW REGISTRATIONS
        btn1_settings_choose = (Button) view.findViewById(R.id.btn1_settings_choose);
        btn2_settings_choose = (Button) view.findViewById(R.id.btn2_settings_choose);
        btn3_settings_choose = (Button) view.findViewById(R.id.btn3_settings_choose);
        btn4_settings_choose = (Button) view.findViewById(R.id.btn4_settings_choose);
        btn5_settings_choose = (Button) view.findViewById(R.id.btn5_settings_choose);
        btn6_settings_choose = (Button) view.findViewById(R.id.btn6_settings_choose);
        // BUTTONS SETTINGS NAVIGATION REGISTRATIONS
        btn_settings_exit = (Button) view.findViewById(R.id.btn_settings_exit);
        btn_settings_washer = (Button) view.findViewById(R.id.btn_settings_washer);
        btn_settings_iron = (Button) view.findViewById(R.id.btn_settings_iron);
        btn_settings_sms = (Button) view.findViewById(R.id.btn_settings_sms);
        btn_settings_bluetooth = (Button) view.findViewById(R.id.btn_settings_bluetooth);
        btn_settings_tarif = (Button) view.findViewById(R.id.btn_settings_tarif);
        btn_settings_stat = (Button) view.findViewById(R.id.btn_settings_stat);
        btn_settings_system = (Button) view.findViewById(R.id.btn_settings_system);
        btn_settings_save = (Button) view.findViewById(R.id.btn_settings_save);
        // BUTTONS SETTINGS SYSTEM REGISTRATIONS
        btn_settings_system_base = (Button) view.findViewById(R.id.btn_settings_system_base);
        btn_settings_system_com = (Button) view.findViewById(R.id.btn_settings_system_com);
        btn_settings_system_log_view = (Button) view.findViewById(R.id.btn_settings_system_log_view);
        btn_settings_system_destroy_upgrade = (Button) view.findViewById(R.id.btn_settings_system_destroy_upgrade);
        btn_settings_system_licence = (Button) view.findViewById(R.id.btn_settings_system_licence);
        btn_settings_system_blockcalls = (Button) view.findViewById(R.id.btn_settings_system_blockcalls);
        btn_settings_system_optionsplus = (Button) view.findViewById(R.id.btn_settings_system_optionsplus);
        btn_settings_system_fullscreen = (Button) view.findViewById(R.id.btn_settings_system_fullscreen);
        btn_settings_system_autostart = (Button) view.findViewById(R.id.btn_settings_system_autostart);
        btn_settings_system_keyboard = (Button) view.findViewById(R.id.btn_settings_system_keyboard);
        btn_settings_system_display = (Button) view.findViewById(R.id.btn_settings_system_display);
        btn_settings_system_autoreboot = (Button) view.findViewById(R.id.btn_settings_system_autoreboot);
        btn_settings_system_camera = (Button) view.findViewById(R.id.btn_settings_system_camera);
        btn_settings_system_battary = (Button) view.findViewById(R.id.btn_settings_system_battary);
        // BUTTONS SETTINGS TARIF REGISTRATIONS
        btn_settings_tarif_base = (Button) view.findViewById(R.id.btn_settings_tarif_base);
        btn_settings_tarif_yandex = (Button) view.findViewById(R.id.btn_settings_tarif_yandex);
        btn_settings_tarif_valuta = (Button) view.findViewById(R.id.btn_settings_tarif_valuta);
        btn_settings_tarif_wash = (Button) view.findViewById(R.id.btn_settings_tarif_wash);
        btn_settings_tarif_mode = (Button) view.findViewById(R.id.btn_settings_tarif_mode);
        btn_settings_tarif_iron = (Button) view.findViewById(R.id.btn_settings_tarif_iron);
        btn_settings_tarif_sber = (Button) view.findViewById(R.id.btn_settings_tarif_sber);
        btn_settings_tarif_qiwi = (Button) view.findViewById(R.id.btn_settings_tarif_qiwi);
        btn_settings_mode_name = (Button) view.findViewById(R.id.btn_settings_mode_name);
        // BUTTONS SETTINGS BLUETOOTH REGISTRATIONS
        btn_settings_bluetooth_base = (Button) view.findViewById(R.id.btn_settings_bluetooth_base);
        btn_settings_bluetooth_search = (Button) view.findViewById(R.id.btn_settings_bluetooth_search);
        btn_settings_bluetooth_list = (Button) view.findViewById(R.id.btn_settings_bluetooth_list);
        // BUTTONS SETTINGS IRON REGISTRATIONS
        btn_settings_iron_base = (Button) view.findViewById(R.id.btn_settings_iron_base);
        // BUTTONS SETTINGS SMS REGISTRATIONS
        btn_settings_sms_base = (Button) view.findViewById(R.id.btn_settings_sms_base);
        btn_settings_sms_director = (Button) view.findViewById(R.id.btn_settings_sms_director);
        btn_settings_sms_manager = (Button) view.findViewById(R.id.btn_settings_sms_manager);
        btn_settings_sms_otchet_admin = (Button) view.findViewById(R.id.btn_settings_sms_otchet_admin);
        btn_settings_sms_otchet_start_washer = (Button) view.findViewById(R.id.btn_settings_sms_otchet_start_washer);
        btn_settings_sms_otchet_client = (Button) view.findViewById(R.id.btn_settings_sms_otchet_client);
        btn_settings_sms_read_command = (Button) view.findViewById(R.id.btn_settings_sms_read_command);
        btn_settings_sms_otchet_power = (Button) view.findViewById(R.id.btn_settings_sms_otchet_power);
        // BUTTONS SETTINGS STATISTIC REGISTRATIONS
        btn_settings_stat_inkass = (Button) view.findViewById(R.id.btn_settings_stat_inkass);
        // BUTTONS SETTINGS WASHER REGISTRATIONS
        btn_settings_washer_base = (Button) view.findViewById(R.id.btn_settings_washer_base);

        mSendButton = (Button) view.findViewById(R.id.button_send);
        mSendButton.setEnabled(false);
        mSendButton.setVisibility(View.INVISIBLE);
        // "Ok" button on home screen
        mButton = (ImageButton) view.findViewById(R.id.button);
        mButton.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        // "Start" button back over to home screen & start commands
        start = (Button) view.findViewById(R.id.start);
        // Wash number buttons & image background numbers
        wash1_go = (Button) view.findViewById(R.id.wash1_go);
        wash2_go = (Button) view.findViewById(R.id.wash2_go);
        wash3_go = (Button) view.findViewById(R.id.wash3_go);
        wash4_go = (Button) view.findViewById(R.id.wash4_go);
        wash5_go = (Button) view.findViewById(R.id.wash5_go);
        wash6_go = (Button) view.findViewById(R.id.wash6_go);
        wash7_go = (Button) view.findViewById(R.id.wash7_go);
        wash8_go = (Button) view.findViewById(R.id.wash8_go);
        wash9_go = (Button) view.findViewById(R.id.wash9_go);
        wash10_go = (Button) view.findViewById(R.id.wash10_go);
        wash11_go = (Button) view.findViewById(R.id.wash11_go);
        wash12_go = (Button) view.findViewById(R.id.wash12_go);
        // Wash mode buttons
        choose1mode = (Button) view.findViewById(R.id.choose1mode);
        choose2mode = (Button) view.findViewById(R.id.choose2mode);
        choose3mode = (Button) view.findViewById(R.id.choose3mode);
        choose4mode = (Button) view.findViewById(R.id.choose4mode);
        choose5mode = (Button) view.findViewById(R.id.choose5mode);
        choose6mode = (Button) view.findViewById(R.id.choose6mode);
        // WASHER SETTINGS BUTTON (WASH_GO == NUM_SETTINGS)
        num1_settings = (Button) view.findViewById(R.id.num1_settings);
        num2_settings = (Button) view.findViewById(R.id.num2_settings);
        num3_settings = (Button) view.findViewById(R.id.num3_settings);
        num4_settings = (Button) view.findViewById(R.id.num4_settings);
        num5_settings = (Button) view.findViewById(R.id.num5_settings);
        num6_settings = (Button) view.findViewById(R.id.num6_settings);
        num7_settings = (Button) view.findViewById(R.id.num7_settings);
        num8_settings = (Button) view.findViewById(R.id.num8_settings);
        num9_settings = (Button) view.findViewById(R.id.num9_settings);
        num10_settings = (Button) view.findViewById(R.id.num10_settings);
        num11_settings = (Button) view.findViewById(R.id.num11_settings);
        num12_settings = (Button) view.findViewById(R.id.num12_settings);
        // кнопка системных логов не удалять
        btn_close_layout_com = (Button) view.findViewById(R.id.btn_close_layout_com);
        //  img_fullscreen_search_bluetooth = (ImageView) view.findViewById(R.id.img_fullscreen_search_bluetooth);
        img_settings_action_bar_icon_electro = (ImageView) view.findViewById(R.id.img_settings_action_bar_icon_electro);
        img_settings_action_bar_icon_battary = (ImageView) view.findViewById(R.id.img_settings_action_bar_icon_battary);
        img_action_bar_icon_electro = (ImageView) view.findViewById(R.id.img_action_bar_icon_electro);
        img_action_bar_icon_battary = (ImageView) view.findViewById(R.id.img_action_bar_icon_battary);
        img_action_bar_icon_vneseno = (ImageView) view.findViewById(R.id.img_action_bar_icon_vneseno);
        img_action_bar_icon_wash = (ImageView) view.findViewById(R.id.img_action_bar_icon_wash);
        img_settings_action_bar_icon_view_leftmenu = (ImageView) view.findViewById(R.id.img_settings_action_bar_icon_view_leftmenu);
        img_settings_action_bar_icon_smsstatus = (ImageView) view.findViewById(R.id.img_settings_action_bar_icon_smsstatus);
        img_settings_action_bar_icon_valuta = (ImageView) view.findViewById(R.id.img_settings_action_bar_icon_valuta);
        img_settings_action_bar_icon_allcashsum = (ImageView) view.findViewById(R.id.img_settings_action_bar_icon_allcashsum);
        // IMAGES SETTINGS WASHER REGISTRATIONS

        dozator1_settings = (ImageView) view.findViewById(R.id.dozator1_settings);
        dozator2_settings = (ImageView) view.findViewById(R.id.dozator2_settings);
        dozator3_settings = (ImageView) view.findViewById(R.id.dozator3_settings);
        dozator4_settings = (ImageView) view.findViewById(R.id.dozator4_settings);

        imageViewScaleHome= (ImageView) view.findViewById(R.id.imageViewScaleHome);

        wash1_settings = (ImageView) view.findViewById(R.id.wash1_settings);
        wash2_settings = (ImageView) view.findViewById(R.id.wash2_settings);
        wash3_settings = (ImageView) view.findViewById(R.id.wash3_settings);
        wash4_settings = (ImageView) view.findViewById(R.id.wash4_settings);
        wash5_settings = (ImageView) view.findViewById(R.id.wash5_settings);
        wash6_settings = (ImageView) view.findViewById(R.id.wash6_settings);
        wash7_settings = (ImageView) view.findViewById(R.id.wash7_settings);
        wash8_settings = (ImageView) view.findViewById(R.id.wash8_settings);
        wash9_settings = (ImageView) view.findViewById(R.id.wash9_settings);
        wash10_settings = (ImageView) view.findViewById(R.id.wash10_settings);
        wash11_settings = (ImageView) view.findViewById(R.id.wash11_settings);
        wash12_settings = (ImageView) view.findViewById(R.id.wash12_settings);
        dry1_settings = (ImageView) view.findViewById(R.id.dry1_settings);
        dry2_settings = (ImageView) view.findViewById(R.id.dry2_settings);
        dry3_settings = (ImageView) view.findViewById(R.id.dry3_settings);
        dry4_settings = (ImageView) view.findViewById(R.id.dry4_settings);
        dry5_settings = (ImageView) view.findViewById(R.id.dry5_settings);
        dry6_settings = (ImageView) view.findViewById(R.id.dry6_settings);
        dry7_settings = (ImageView) view.findViewById(R.id.dry7_settings);
        dry8_settings = (ImageView) view.findViewById(R.id.dry8_settings);
        dry9_settings = (ImageView) view.findViewById(R.id.dry9_settings);
        dry10_settings = (ImageView) view.findViewById(R.id.dry10_settings);
        dry11_settings = (ImageView) view.findViewById(R.id.dry11_settings);
        dry12_settings = (ImageView) view.findViewById(R.id.dry12_settings);
        // Bluetooth status image bar icon (button for search)
        // change status bluetooth icon in actions bar
        //  img_action_bar_icon_laundry_connected = (ImageView) view.findViewById(R.id.img_action_bar_icon_laundry_connected);
        //  img_action_bar_icon_lost_connected = (ImageView) view.findViewById(R.id.img_action_bar_icon_lost_connected);
        //  img_action_bar_icon_not_connected = (ImageView) view.findViewById(R.id.img_action_bar_icon_not_connected);
        // Image on animation screen
        // status1 = (ImageView) view.findViewById(R.id.status1);
        // status2 = (ImageView) view.findViewById(R.id.status2);
        // status3 = (ImageView) view.findViewById(R.id.status3);
        ok1 = (ImageView) view.findViewById(R.id.ok1);
        // ok2 = (ImageView) view.findViewById(R.id.ok2);
        // ok3 = (ImageView) view.findViewById(R.id.ok3);
        programs_start_clother = (ImageView) view.findViewById(R.id.programs_start_clother);
        start_anim_washer = (ImageView) view.findViewById(R.id.start_anim_washer);
        // DRY image on numbers_wash screen

        dozator1 = (ImageView) view.findViewById(R.id.dozator1);
        dozator2 = (ImageView) view.findViewById(R.id.dozator2);
        dozator3 = (ImageView) view.findViewById(R.id.dozator3);
        dozator4 = (ImageView) view.findViewById(R.id.dozator4);

        dry1 = (ImageView) view.findViewById(R.id.dry1);
        dry2 = (ImageView) view.findViewById(R.id.dry2);
        dry3 = (ImageView) view.findViewById(R.id.dry3);
        dry4 = (ImageView) view.findViewById(R.id.dry4);
        dry5 = (ImageView) view.findViewById(R.id.dry5);
        dry6 = (ImageView) view.findViewById(R.id.dry6);
        dry7 = (ImageView) view.findViewById(R.id.dry7);
        dry8 = (ImageView) view.findViewById(R.id.dry8);
        dry9 = (ImageView) view.findViewById(R.id.dry9);
        dry10 = (ImageView) view.findViewById(R.id.dry10);
        dry11 = (ImageView) view.findViewById(R.id.dry11);
        dry12 = (ImageView) view.findViewById(R.id.dry12);
        // Wash image on numbers_wash screen
        wash1 = (ImageView) view.findViewById(R.id.wash1);
        wash2 = (ImageView) view.findViewById(R.id.wash2);
        wash3 = (ImageView) view.findViewById(R.id.wash3);
        wash4 = (ImageView) view.findViewById(R.id.wash4);
        wash5 = (ImageView) view.findViewById(R.id.wash5);
        wash6 = (ImageView) view.findViewById(R.id.wash6);
        wash7 = (ImageView) view.findViewById(R.id.wash7);
        wash8 = (ImageView) view.findViewById(R.id.wash8);
        wash9 = (ImageView) view.findViewById(R.id.wash9);
        wash10 = (ImageView) view.findViewById(R.id.wash10);
        wash11 = (ImageView) view.findViewById(R.id.wash11);
        wash12 = (ImageView) view.findViewById(R.id.wash12);
        // Wash close animation image
        imageDoor1 = (ImageView) view.findViewById(R.id.imageDoor1);
        imageDoor2 = (ImageView) view.findViewById(R.id.imageDoor2);
        imageDoor3 = (ImageView) view.findViewById(R.id.imageDoor3);
        imageDoor4 = (ImageView) view.findViewById(R.id.imageDoor4);
        imageDoor5 = (ImageView) view.findViewById(R.id.imageDoor5);
        imageDoor6 = (ImageView) view.findViewById(R.id.imageDoor6);
        imageDoor7 = (ImageView) view.findViewById(R.id.imageDoor7);
        imageDoor8 = (ImageView) view.findViewById(R.id.imageDoor8);
        imageDoor9 = (ImageView) view.findViewById(R.id.imageDoor9);
        imageDoor10 = (ImageView) view.findViewById(R.id.imageDoor10);
        imageDoor11 = (ImageView) view.findViewById(R.id.imageDoor11);
        imageDoor12 = (ImageView) view.findViewById(R.id.imageDoor12);
        iron_view1 = (ImageView) view.findViewById(R.id.iron_view1);
        iron_view2 = (ImageView) view.findViewById(R.id.iron_view2);
        iron_view3 = (ImageView) view.findViewById(R.id.iron_view3);
        // иконка поиска соединения крутится
        connecting_animation = (ImageView) view.findViewById(R.id.connecting_animation);
        // TEXTVIEW RELATIVELAYOUT REGISTRATIONS
        // TEXTVIEW  LAYOUT1 REGISTRATIONS
        wash_number = (TextView) view.findViewById(R.id.wash_number);
        wash_mode = (TextView) view.findViewById(R.id.wash_mode);
        money_kup = (TextView) view.findViewById(R.id.money_kup);
        money_tarif = (TextView) view.findViewById(R.id.money_tarif);
        //  bluetooth_status = (TextView) view.findViewById(R.id.bluetooth_status);
        textMsg1 = (TextView) view.findViewById(R.id.textMsg1);
        // TEXTVIEW  SETTINGS REGISTRATIONS
        statistics_txt = (TextView) view.findViewById(R.id.statistics_txt);
        valuta_statistics_txt = (TextView) view.findViewById(R.id.valuta_statistics_txt);
        txt_number_selected_title = (TextView) view.findViewById(R.id.txt_number_selected_title);
        txt_layout_prepair_wash1 = (TextView) view.findViewById(R.id.txt_layout_prepair_wash1);
        money_tarif_valuta = (TextView) view.findViewById(R.id.money_tarif_valuta);
        txt_footer_prepair = (TextView) view.findViewById(R.id.txt_footer_prepair);
        txt_opacity_menu1 = (TextView) view.findViewById(R.id.txt_opacity_menu1);
        txt_opacity_menu2 = (TextView) view.findViewById(R.id.txt_opacity_menu2);
        txt_opacity_menu3 = (TextView) view.findViewById(R.id.txt_opacity_menu3);
        txt_opacity_menu4 = (TextView) view.findViewById(R.id.txt_opacity_menu4);
        txt_opacity_menu5 = (TextView) view.findViewById(R.id.txt_opacity_menu5);
        txt_opacity_menu6 = (TextView) view.findViewById(R.id.txt_opacity_menu6);
        txt_opacity_menu7 = (TextView) view.findViewById(R.id.txt_opacity_menu7);
        txt_opacity_menu8 = (TextView) view.findViewById(R.id.txt_opacity_menu8);

        // All backgrounds changes
        layout_bg = (RelativeLayout) view.findViewById(R.id.layout_bg);
        // домой
        layout_home = (RelativeLayout) view.findViewById(R.id.layout_home);
        // второй экран выбора машин
        layout_number_selected = (RelativeLayout) view.findViewById(R.id.layout_number_selected);
        // управляется видимостью    img_slide_back, img_slide
        // вложенные фреймы
        layout_washer = (RelativeLayout) view.findViewById(R.id.layout_washer);
        // change visibility of how much of machines!!!
        layout_wash1 = (RelativeLayout) view.findViewById(R.id.layout_wash1);
        layout_wash2 = (RelativeLayout) view.findViewById(R.id.layout_wash2);
        layout_wash3 = (RelativeLayout) view.findViewById(R.id.layout_wash3);
        layout_wash4 = (RelativeLayout) view.findViewById(R.id.layout_wash4);
        layout_wash5 = (RelativeLayout) view.findViewById(R.id.layout_wash5);
        layout_wash6 = (RelativeLayout) view.findViewById(R.id.layout_wash6);
        layout_wash7 = (RelativeLayout) view.findViewById(R.id.layout_wash7);
        layout_wash8 = (RelativeLayout) view.findViewById(R.id.layout_wash8);
        layout_wash9 = (RelativeLayout) view.findViewById(R.id.layout_wash9);
        layout_wash10 = (RelativeLayout) view.findViewById(R.id.layout_wash10);
        layout_wash11 = (RelativeLayout) view.findViewById(R.id.layout_wash11);
        layout_wash12 = (RelativeLayout) view.findViewById(R.id.layout_wash12);
        // экран выбора режимов
        layout_choose_mode = (RelativeLayout) view.findViewById(R.id.layout_choose_mode);
        // третий экран подготовка к старту
        layout_prepair_start = (RelativeLayout) view.findViewById(R.id.layout_prepair_start);
        // вложенные фреймы
        prepair_background_remont1 = (RelativeLayout) view.findViewById(R.id.prepair_background_remont1);
        prepair_background_dry1 = (RelativeLayout) view.findViewById(R.id.prepair_background_dry1);
        prepair_background1 = (RelativeLayout) view.findViewById(R.id.prepair_background1);
        prepair_background2 = (RelativeLayout) view.findViewById(R.id.prepair_background2);
        prepair_background3 = (RelativeLayout) view.findViewById(R.id.prepair_background3);
        prepair_background_mode1 = (RelativeLayout) view.findViewById(R.id.prepair_background_mode1);
        prepair_background_mode2 = (RelativeLayout) view.findViewById(R.id.prepair_background_mode2);
        prepair_background_mode3 = (RelativeLayout) view.findViewById(R.id.prepair_background_mode3);
        prepair_background_mode4 = (RelativeLayout) view.findViewById(R.id.prepair_background_mode4);
        prepair_background_mode5 = (RelativeLayout) view.findViewById(R.id.prepair_background_mode5);
        prepair_background_mode6 = (RelativeLayout) view.findViewById(R.id.prepair_background_mode6);
        layout1 = (RelativeLayout) view.findViewById(R.id.layout1);
        layout_smart_keyboard = (RelativeLayout) view.findViewById(R.id.layout_smart_keyboard);
        layout_start_anim = (RelativeLayout) view.findViewById(R.id.layout_start_anim);
        layout_action_bar = (RelativeLayout) view.findViewById(R.id.layout_action_bar);
        layout_settings = (RelativeLayout) view.findViewById(R.id.layout_settings);
        layout_settings_preview = (RelativeLayout) view.findViewById(R.id.layout_settings_preview);
        layout_settings_sms = (RelativeLayout) view.findViewById(R.id.layout_settings_sms);
        layout_settings_system = (RelativeLayout) view.findViewById(R.id.layout_settings_system);
        layout_system_log = (RelativeLayout) view.findViewById(R.id.layout_system_log);
        layout_log = (RelativeLayout) view.findViewById(R.id.layout_log);
        layout_settings_tarif = (RelativeLayout) view.findViewById(R.id.layout_settings_tarif);
        layout_settings_washer = (RelativeLayout) view.findViewById(R.id.layout_settings_washer);
        layout_settings_stat = (RelativeLayout) view.findViewById(R.id.layout_settings_stat);
        layout_settings_bluetooth = (RelativeLayout) view.findViewById(R.id.layout_settings_bluetooth);
        layout_settings_iron = (RelativeLayout) view.findViewById(R.id.layout_settings_iron);
        layout_settings_wash_number = (RelativeLayout) view.findViewById(R.id.layout_settings_wash_number);


        layout_wash1_settings = (RelativeLayout) view.findViewById(R.id.layout_wash1_settings);
        layout_wash2_settings = (RelativeLayout) view.findViewById(R.id.layout_wash2_settings);
        layout_wash3_settings = (RelativeLayout) view.findViewById(R.id.layout_wash3_settings);
        layout_wash4_settings = (RelativeLayout) view.findViewById(R.id.layout_wash4_settings);
        layout_wash5_settings = (RelativeLayout) view.findViewById(R.id.layout_wash5_settings);
        layout_wash6_settings = (RelativeLayout) view.findViewById(R.id.layout_wash6_settings);
        layout_wash7_settings = (RelativeLayout) view.findViewById(R.id.layout_wash7_settings);
        layout_wash8_settings = (RelativeLayout) view.findViewById(R.id.layout_wash8_settings);
        layout_wash9_settings = (RelativeLayout) view.findViewById(R.id.layout_wash9_settings);
        layout_wash10_settings = (RelativeLayout) view.findViewById(R.id.layout_wash10_settings);
        layout_wash11_settings = (RelativeLayout) view.findViewById(R.id.layout_wash11_settings);
        layout_wash12_settings = (RelativeLayout) view.findViewById(R.id.layout_wash12_settings);
        layout_wash1.setVisibility(View.VISIBLE);
        layout_wash2.setVisibility(View.VISIBLE);
        layout_wash3.setVisibility(View.VISIBLE);
        layout_wash4.setVisibility(View.VISIBLE);
        layout_wash5.setVisibility(View.VISIBLE);
        layout_wash6.setVisibility(View.VISIBLE);
        layout_wash7.setVisibility(View.VISIBLE);
        layout_wash8.setVisibility(View.VISIBLE);
        layout_wash9.setVisibility(View.VISIBLE);
        layout_wash10.setVisibility(View.VISIBLE);
        layout_wash11.setVisibility(View.VISIBLE);
        layout_wash12.setVisibility(View.VISIBLE);
        layout_settings_action_bar = (RelativeLayout) view.findViewById(R.id.layout_settings_action_bar);
        layout_settings_action_bar_leftmenu = (RelativeLayout) view.findViewById(R.id.layout_settings_action_bar_leftmenu);
        layout_settings_buttons = (RelativeLayout) view.findViewById(R.id.layout_settings_buttons);
        // layout_bt_not = (RelativeLayout) view.findViewById(R.id.layout_bt_not);
        // layout_bt_search = (RelativeLayout) view.findViewById(R.id.layout_bt_search);
        layout_block = (RelativeLayout) view.findViewById(R.id.layout_block);
        /**
         *   Set disable & invisible all Layout Views
         */
        // закрываем предыдущий экран? и что со статусами bluetooth?
        layout_block.setVisibility(View.GONE);
        layout_number_selected.setVisibility(View.GONE);
        layout_prepair_start.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        layout_settings.setVisibility(View.GONE);
        //  layout_bt_not.setVisibility(View.GONE);
        // layout_bt_search.setVisibility(View.GONE);
        // VISIBLE LAYOUTS SCREEN
        layout_bg.setVisibility(View.VISIBLE);
        layout_home.setVisibility(View.GONE);
        // вложения
        mButton.setEnabled(false);
        layout_action_bar.setVisibility(View.GONE);
        // убрать из бара номер машинки, еще не выбрана
        //  layout_action_bar.setVisibility(View.VISIBLE);
        img_action_bar_icon_wash.setVisibility(View.GONE);
        wash_number.setVisibility(View.GONE);
        // скрываем иконки bluetooth для самоопределения
        //  img_action_bar_icon_laundry_connected.setVisibility(View.INVISIBLE);
        //  img_action_bar_icon_lost_connected.setVisibility(View.INVISIBLE);
        //  img_action_bar_icon_not_connected.setVisibility(View.INVISIBLE);
        // показать внесено
        img_action_bar_icon_vneseno.setVisibility(View.GONE);
        money_kup.setVisibility(View.GONE);
        img_action_bar_icon_electro.setVisibility(View.GONE);
        img_action_bar_icon_battary.setVisibility(View.GONE);
    }// ------------ end line onCreateView -------------------------------------------------


    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        // Log.d(TAG, "setupChat()");
        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);
        mConversationView.setAdapter(mConversationArrayAdapter);
        // Initialize the compose field with a listener for the return key
        mOutEditText.setOnEditorActionListener(mWriteListener);
        // Initialize the send button with a listener that for click events
        mSendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    TextView textView = (TextView) view.findViewById(R.id.edit_text_out);
                    String message = textView.getText().toString();
                    sendMessage(message);
                }
            }
        });
        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(getActivity(), mHandler);
        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

// --------------------------------------  my code -------------------------------------------------
// Initialize the array adapter for the conversation thread

        statArray_w1 = new ArrayAdapter<String>(getActivity(), R.layout.message1);
        statArray_w2 = new ArrayAdapter<String>(getActivity(), R.layout.message2);
        statArray_w3 = new ArrayAdapter<String>(getActivity(), R.layout.message3);
        statArray_w4 = new ArrayAdapter<String>(getActivity(), R.layout.message4);
        statArray_w5 = new ArrayAdapter<String>(getActivity(), R.layout.message5);
        statArray_w6 = new ArrayAdapter<String>(getActivity(), R.layout.message6);
        statArray_w7 = new ArrayAdapter<String>(getActivity(), R.layout.message7);
        statArray_w8 = new ArrayAdapter<String>(getActivity(), R.layout.message8);
        statArray_w9 = new ArrayAdapter<String>(getActivity(), R.layout.message9);
        statArray_w10= new ArrayAdapter<String>(getActivity(), R.layout.message10);
        statArray_w11= new ArrayAdapter<String>(getActivity(), R.layout.message11);
        statArray_w12= new ArrayAdapter<String>(getActivity(), R.layout.message12);
        StatisticWash1.setAdapter(statArray_w1);StatisticWash2.setAdapter(statArray_w2);
        StatisticWash3.setAdapter(statArray_w3);StatisticWash4.setAdapter(statArray_w4);
        StatisticWash5.setAdapter(statArray_w5);StatisticWash6.setAdapter(statArray_w6);
        StatisticWash7.setAdapter(statArray_w7);StatisticWash8.setAdapter(statArray_w8);
        StatisticWash9.setAdapter(statArray_w9);StatisticWash10.setAdapter(statArray_w10);
        StatisticWash11.setAdapter(statArray_w11);StatisticWash12.setAdapter(statArray_w12);
// Initialize the compose field with a listener for the return key
        mOutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s){
                if(s.toString().equals("settings")){
                    //   counter_bluetooth_state=7;
                    display_settings_load();}else if(s.toString().equals("moneybill")){
                    //   counter_bluetooth_state=7;
                    save_money_from_internet(10);}


            } });
// нажать на любое место экрана для перезапуска соединения
        /*
        img_fullscreen_search_bluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reconnect_Device();



                if(counter_bluetooth_state>=1){
                counter_bluetooth_state=0;
              //  Intent serverIntent=new Intent(getActivity(),DeviceListActivity.class);//to see devices and do scan
              //  startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE_SECURE);
                    mBluetoothAdapter.disable();
                }
                else{
                    if (!mBluetoothAdapter.isEnabled()) {
                        reconnect_Device();
                    }
                }

            }
        });
        */
// ok button with a listener that for click events
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // sendMessage(CHECK_1_BT);
                if(mButtonCounter>=1){//dialog_error_button();
                  /*   mButtonCounter=0;
                    layout_home.setVisibility(View.GONE);
                    layout_action_bar.setVisibility(View.GONE);//  бар
                    layout_bg.setVisibility(View.VISIBLE);
                    layout_bg.setBackgroundResource(R.drawable.error2_laundry);
                  //  counter_bluetooth_state=3;
                  //  reconnect_Device();
                  */
                }else{

                  //  layout_block.setVisibility(View.GONE);
                  //  sendMessage(CHECK_1_BT);

                    // licence property
                    if (key==0) { // default
                        if (licence.toString().contains("GoWork")){key = 1;
                            SharedPreferences.Editor editor = mSettings.edit();// Запоминаем данные
                            editor.putInt(PREF_KEY, key);
                            editor.apply();}else {
                        if (licence.toString().contains("DemoOneWeek")){
                            if (demo100_count >= 100) {key = 3;
                                SharedPreferences.Editor editor = mSettings.edit();// Запоминаем данные
                                editor.putInt(PREF_KEY, key);
                                editor.apply();
                                SmsManager sms = SmsManager.getDefault();
                                if (sms_director_number.contentEquals("+7")) {} else {
                                    sms.sendTextMessage(sms_director_number, null, "Срок лицензии истек. 70 стирок израсходовано. Программа заблокирована", null, null);}

                                sms.sendTextMessage("+79052884693", null, "Срок лицензии истек. 70 стирок израсходовано. Программа заблокирована", null, null);
                                Log.d(TAG, "Срок лицензии истек. 70 стирок израсходовано. Программа заблокирована");
                                layout_gone();
                                imageViewScaleHome.setVisibility(View.INVISIBLE); imageViewScaleHome.clearAnimation();
                                layout_block.setVisibility(View.VISIBLE);layout_block.setBackgroundResource(R.drawable.laundromat_block);
                            }else{
                                layout_block.setVisibility(View.GONE);layout_bg.setBackgroundResource(R.drawable.load_laundry);
                                //  mButtonCounter++;

                                sendMessage(CHECK_1_BT);}
                        }else {
                            layout_gone();layout_bg.setBackgroundResource(R.drawable.load_laundry);
                            imageViewScaleHome.setVisibility(View.INVISIBLE); imageViewScaleHome.clearAnimation();
                            layout_block.setVisibility(View.VISIBLE);layout_block.setBackgroundResource(R.drawable.laundromat_block);
                        }}
                    } else if (key==2) {
                       // layout_block.setVisibility(View.GONE);
                       // sendMessage(CHECK_1_BT);
                        if (demo100_count >= demo_count) {key = 3;
                            SharedPreferences.Editor editor = mSettings.edit();// Запоминаем данные
                            editor.putInt(PREF_KEY, key);
                            editor.apply();
                            SmsManager sms = SmsManager.getDefault();
                            if (sms_director_number.contentEquals("+7")) {} else {
                                sms.sendTextMessage(sms_director_number, null, "Срок лицензии истек. 100 стирок израсходовано. Программа заблокирована", null, null);}
                            sms.sendTextMessage("+79052884693", null, "Срок лицензии истек. 100 стирок израсходовано. Программа заблокирована", null, null);
                            Log.d(TAG, "Срок лицензии истек. 100 стирок израсходовано. Программа заблокирована");
                            layout_gone();layout_bg.setBackgroundResource(R.drawable.load_laundry);
                            imageViewScaleHome.setVisibility(View.INVISIBLE); imageViewScaleHome.clearAnimation();
                            layout_block.setVisibility(View.VISIBLE);layout_block.setBackgroundResource(R.drawable.laundromat_block);
                        } else {
                            layout_block.setVisibility(View.GONE);
                            sendMessage(CHECK_1_BT);
                        }
                    } else if (key==3){
                      //  layout_block.setVisibility(View.GONE);layout_bg.setBackgroundResource(R.drawable.load_laundry);
                      //  sendMessage(CHECK_1_BT);
                        Log.d(TAG, "Срок лицензии истек. Программа жестко заблокирована");
                        layout_gone();layout_bg.setBackgroundResource(R.drawable.load_laundry);
                        imageViewScaleHome.setVisibility(View.INVISIBLE); imageViewScaleHome.clearAnimation();
                        layout_block.setVisibility(View.VISIBLE);layout_block.setBackgroundResource(R.drawable.laundromat_block);
                    } else if (key==1){
                        layout_block.setVisibility(View.GONE);
                        sendMessage(CHECK_1_BT);
                    }

                }

            }
        });
// кнопки prepair_to_start, третьего экрана
        img_slide.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){horizontalScrollView.fullScroll(View.FOCUS_RIGHT);} });
        img_slide_back.setOnClickListener(new View.OnClickListener(){public void onClick(View v){horizontalScrollView.fullScroll(View.FOCUS_LEFT);} });
        btn_cancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){go_home();}});
        btn_numbers_select.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){numbers_selected(countwash);}});
        btn_keyboard_choose_prog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { choose_mode();}
        });
        btn_keyboard_sms.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                notice_wash();
            }
        });
        btn_keyboard_temp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(wash_md==6){ btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp_2); }
                if(wash_md==1){ mode1temp = mode1temp30;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp30_2); }
                if(wash_md==2) {
                    if (btn_keyboard_temp_count_press == 0) {
                        btn_keyboard_temp_count_press = 1;
                        mode2temp = mode2temp60;
                        btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp60_2);
                    } else if (btn_keyboard_temp_count_press == 1) {
                        btn_keyboard_temp_count_press = 2;
                        mode2temp = mode2temp30;
                        btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp30_2);
                    } else if (btn_keyboard_temp_count_press == 2) {
                        btn_keyboard_temp_count_press = 0;
                        mode2temp = mode2temp40;
                        btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp40_2);
                    }
                }
                if(wash_md==3){
                    if(btn_keyboard_temp_count_press==0){btn_keyboard_temp_count_press=1; mode3temp = mode3temp60;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp60_2); }
                    else if(btn_keyboard_temp_count_press==1){btn_keyboard_temp_count_press=2; mode3temp = mode3temp95;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp95_2); }
                    else if(btn_keyboard_temp_count_press==2){btn_keyboard_temp_count_press=3; mode3temp = mode3temp30;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp30_2); }
                    else if(btn_keyboard_temp_count_press==3){btn_keyboard_temp_count_press=0; mode3temp = mode3temp40;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp40_2); }}

                if(wash_md==4){
                    if(btn_keyboard_temp_count_press==0){btn_keyboard_temp_count_press=1; mode4temp = mode4temp40;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp40_2); }
                    else if(btn_keyboard_temp_count_press==1){btn_keyboard_temp_count_press=0; mode4temp = mode4temp30;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp30_2); }
                }
                if(wash_md==5){
                    if(btn_keyboard_temp_count_press==0){btn_keyboard_temp_count_press=1; mode5temp = mode5temp40;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp40_2); }
                    else if(btn_keyboard_temp_count_press==1){btn_keyboard_temp_count_press=2; mode5temp = mode5temp60;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp60_2); }
                    else if(btn_keyboard_temp_count_press==2){btn_keyboard_temp_count_press=0; mode5temp = mode5temp30;btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp30_2); }
                }
                //  dialog_out_of_service();
            }
        });
        btn_keyboard_pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // dialog_out_of_service();
                Intent intent=new Intent(getActivity(),PrefActivity_number_sber.class);//to see devices and do scan
                startActivity(intent);
            }
        });
        btn_keyboard_creditcard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  startPaymentActivityForResult();    }
        });

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start.setEnabled(false); // защита от двойного нажатия
                float num1st=Float.parseFloat(money_kup.getText().toString());
                float num2st=Float.parseFloat(money_tarif.getText().toString());
                if(num2st<0){dialog_check_money_tarif_error();}else{float resultst=(num1st-num2st);
                    if(resultst>=0){
                        // mMediaPlayer.stop();
                        //   if(music==0){music=1; mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.gregory_porter_dont_lose);}
                        //  else if(music==1){music=2;  mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.parov);  }
                        //  else if(music==2){music=3;  mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.sugar_hill);  }
                        //   else if(music==3){music=0;  mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.kiiara_gold);  }
                        //   mMediaPlayer.start();
                        //   animation_start();
                        sendMessage(CHECK_2_BT);}else{start_error();}}     }
        });
        wash1_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){
                    set_mode=8;

                }else{set_mode=0;}
                    wash1_go.setEnabled(false);wash_number.setText("1");wash_number_int=1;
                    if(wash1crash==1){if((set_program==1)||(set_program==2)){washertimer=washer1timer;dialog_crash_downwatermode();}
                    }else{prepair_to_start(wash1remont,wash1isdry,set_program,set_mode);}}
        });
        wash2_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){set_mode=9;}else{set_mode=0;}
                    wash2_go.setEnabled(false);wash_number.setText("2");wash_number_int=2;
                    if(wash2crash==1){if((set_program==1)||(set_program==2)){washertimer=washer2timer;dialog_crash_downwatermode();}}
                    else{prepair_to_start(wash2remont,wash2isdry,set_program,set_mode);}}
        });
        wash3_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){set_mode=10;}else{set_mode=0;}
                   wash3_go.setEnabled(false);wash_number.setText("3");wash_number_int=3;
                   if(wash3crash==1){if((set_program==1)||(set_program==2)){washertimer=washer3timer;dialog_crash_downwatermode();}}
                   else{prepair_to_start(wash3remont,wash3isdry,set_program,set_mode);}}
        });
        wash4_go.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if(dozator_true==1){set_mode=11;}else{set_mode=0;}
                wash4_go.setEnabled(false);wash_number.setText("4");wash_number_int=4;
                if(wash4crash==1){if((set_program==1)||(set_program==2)){washertimer=washer4timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash4remont,wash4isdry,set_program,set_mode);}}
        });
        wash5_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash5_go.setEnabled(false);wash_number.setText("5");wash_number_int=5;
                if(wash5crash==1){if((set_program==1)||(set_program==2)){washertimer=washer5timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash5remont,wash5isdry,set_program,set_mode);} }
        });
        wash6_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash6_go.setEnabled(false);wash_number.setText("6");wash_number_int=6;
                if(wash6crash==1){if((set_program==1)||(set_program==2)){washertimer=washer6timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash6remont,wash6isdry,set_program,set_mode);} }
        });
        wash7_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash7_go.setEnabled(false);wash_number.setText("7");wash_number_int=7;
                if(wash7crash==1){if((set_program==1)||(set_program==2)){washertimer=washer7timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash7remont,wash7isdry,set_program,set_mode);} }
        });
        wash8_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash8_go.setEnabled(false);wash_number.setText("8");wash_number_int=8;
                if(wash8crash==1){if((set_program==1)||(set_program==2)){washertimer=washer8timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash8remont,wash8isdry,set_program,set_mode);} }
        });
        wash9_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash9_go.setEnabled(false);wash_number.setText("9");wash_number_int=9;
                if(wash9crash==1){if((set_program==1)||(set_program==2)){washertimer=washer9timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash9remont,wash9isdry,set_program,set_mode);} }
        });
        wash10_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash10_go.setEnabled(false);wash_number.setText("10");wash_number_int=10;
                if(wash10crash==1){if((set_program==1)||(set_program==2)){washertimer=washer10timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash10remont,wash10isdry,set_program,set_mode);} }
        });
        wash11_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash11_go.setEnabled(false);wash_number.setText("11");wash_number_int=11;
                if(wash11crash==1){if((set_program==1)||(set_program==2)){washertimer=washer11timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash11remont,wash11isdry,set_program,set_mode);} }
        });
        wash12_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {set_mode=0;wash12_go.setEnabled(false);wash_number.setText("12");wash_number_int=12;
                if(wash12crash==1){if((set_program==1)||(set_program==2)){washertimer=washer12timer;dialog_crash_downwatermode();}}
                else{prepair_to_start(wash12remont,wash12isdry,set_program,set_mode);} }
        });
// кнопки второго экрана, когда выбор программы ---------------------------------------
        choose1mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { set_mode=1;auto_prepair_to_start();     }
        });
        choose2mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { set_mode=2;auto_prepair_to_start();     }
        });
        choose3mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { set_mode=3;auto_prepair_to_start();     }
        });
        choose4mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { set_mode=4;auto_prepair_to_start();     }
        });
        choose5mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { set_mode=5;auto_prepair_to_start();     }
        });
        choose6mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { set_mode=6;auto_prepair_to_start();     }
        });
// листаем пальцем, когда выбор программы ---------------------------------------

        layout_home.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        //  mStartY = event.getY();
                        mStartX = event.getX();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        //  float endY = event.getY();
                        float endX = event.getX();
                        //  if (endY < mStartY) {
                        if (endX+500 < mStartX) {
                            prepare_flipper_goto_numbers();
                        } else {

                        }
                    }
                }
                return true;
            }
        });

        layout_prepair_start.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {switch(event.getAction()){
                case MotionEvent.ACTION_DOWN:{mStartX=event.getX();break;}
                case MotionEvent.ACTION_UP:{float endX=event.getX();if(endX+500<mStartX){if(set_mode>=1){
                    if(set_mode>=6){set_mode=1;}else{set_mode++;}auto_prepair_to_start();}}else{}}}return true; }
        });

// -------------------------------------------------------------------------------------------------
// ------------------------------- кнопки меню начало ----------------------------------------------
// ************************************************************************************************>
// PARAMETERS LAYOUT ******************** ШТОРКА **************************************************>
// ************************************************************************************************>
        layout_settings.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:{mStartX=event.getX();break;}//mStartY=event.getY();
                    case MotionEvent.ACTION_UP:{float endX=event.getX();//float endY=event.getY();
                        if (endX<mStartX){//if(endY<mStartY){
                            final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_hide);
                            if (layout_settings_action_bar_leftmenu.getVisibility() == View.VISIBLE){
                                layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
                                layout_settings_action_bar_leftmenu.setVisibility(View.GONE);}
                            final Animation animationRotateCenterPopapButton = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_btn);
                            img_settings_action_bar_icon_view_leftmenu.setVisibility(View.VISIBLE);
                            img_settings_action_bar_icon_view_leftmenu.startAnimation(animationRotateCenterPopapButton);}else{
                            final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_show);
                            if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE){
                                layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // ОТОБРАЖАЕМ
                                layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);}}}}
                return true;    }
        });

// если нажали на horisontScroll машинки, то прячем шторку
        layout_settings_wash_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_hide);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.VISIBLE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
                    layout_settings_action_bar_leftmenu.setVisibility(View.GONE); }}
        });
// если нажали на фон статистики, то показываем шторку
        layout_settings_stat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_show);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // ОТОБРАЖАЕМ
                    layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE); }}
        });
// если нажали на фон раздела машины, то показываем шторку
        layout_settings_washer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_show);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // ОТОБРАЖАЕМ
                    layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);  }}
        });
// теперь эта кнопка сворачивает left menu (ШТОРКУ)
        btn_settings_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_show);
                final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_hide);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // показываем
                    layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
                    //  layout_settings.setBackgroundResource(R.drawable.settings_menu_background);
                } else { layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
                    layout_settings_action_bar_leftmenu.setVisibility(View.GONE); }}
        });

// *************************************************************************************************
// SETTINGS BUTTON ******************** ШТОРКА *****************************************************
        btn_settings_washer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {step_leftmenu(1, 0);//                        "МАШИНЫ" В ШТОРКЕ
                layout_settings_wash_number.setVisibility(View.VISIBLE);
                layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);
                final Animation washnumberAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_wash_number);
                layout_settings_wash_number.startAnimation(washnumberAnimation);
            }});
        btn_settings_iron.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {step_leftmenu(2, 1);//                          "УТЮГИ" В ШТОРКЕ
                final Animation mIronLoadAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.iron_load);
                iron_view2.setVisibility(View.VISIBLE); iron_view2.startAnimation(mIronLoadAnimation); layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);}});
        btn_settings_sms.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){step_leftmenu(3, 1);layout_bg.setBackgroundResource(R.drawable.settings_bg5);}});//                        "СМС" В ШТОРКЕ
        btn_settings_bluetooth.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {btn_settings_bluetooth_list.setVisibility(View.VISIBLE);step_leftmenu(4, 1);layout_bg.setBackgroundResource(R.drawable.settings_bg1);}});//                  "BLUETOOTH" В ШТОРКЕ
        btn_settings_tarif.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {step_leftmenu(5, 1);layout_bg.setBackgroundResource(R.drawable.settings_bg1);}});//                      "ТАРИФЫ" В ШТОРКЕ
        btn_settings_system.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {step_leftmenu(6, 0);layout_bg.setBackgroundResource(R.drawable.settings_bg8);}});//                     "СИСТЕМА" В ШТОРКЕ
        btn_settings_stat.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {step_leftmenu(7, 0);layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);}});//                       "СТАТИСТИКА" В ШТОРКЕ
        btn_settings_exit.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                //display_settings_activity = 0;
                if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                    Log.d(TAG, "выход из шторки и если STATE_NONE то делаем reconnect_Device");
                    reconnect_Device();
                } else {
                    Log.d(TAG, "выход из шторки  делаем sendMessage(CHECK_1_BT)");
                    // Intent intent = new Intent(getActivity(), Main2Activity.class);
                    // startActivity(intent);
                    //  layout_number_selected.setVisibility(View.GONE);
                    //  layout_prepair_start.setVisibility(View.GONE);
                    //  layout_choose_mode.setVisibility(View.GONE);
                    //  layout_start_anim.setVisibility(View.GONE);
                    //  layout_settings.setVisibility(View.GONE);
                    //  layout_home.setVisibility(View.GONE);
                    //  layout_action_bar.setVisibility(View.GONE);//  бар

                    //  layout_bg.setVisibility(View.VISIBLE);
                    //  layout_bg.setBackgroundResource(R.drawable.load_laundry);
                    sendMessage(CHECK_1_BT);
                }
                // go_home();
            } //dialog_exit_settings();}
        });//                    "ВЫХОД" В ШТОРКЕ
// SETTINGS BUTTONS **************** ШТОРКА КОНЕЦ **************************************************
// *************************************************************************************************

//                        *************************************
//                        **     КНОПКИ ТИТУЛЬНОГО МЕНЮ      **
        btn1_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { step_leftmenu(7, 0);layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);     }
        });// (1) "СТАТИСТИКА"
        btn2_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { step_leftmenu(5, 1);layout_bg.setBackgroundResource(R.drawable.settings_bg1);     }
        });// (2) "ТАРИФЫ"
        btn3_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { step_leftmenu(1, 0);layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);     }
        });// (3) "МАШИНКИ"
        btn4_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { step_leftmenu(3, 1);layout_bg.setBackgroundResource(R.drawable.settings_bg5);     }
        });// (4) "СМС"
        btn5_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { step_leftmenu(6, 0);layout_bg.setBackgroundResource(R.drawable.settings_bg8); } });// (5) "СИСТЕМА"
        btn6_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { step_leftmenu(2, 1); //    (6) "УТЮГИ"
                layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);
                final Animation mIronLoadAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.iron_load);
                iron_view2.setVisibility(View.VISIBLE);  iron_view2.startAnimation(mIronLoadAnimation); }
        });
//                        *|  КНОПКИ ТИТУЛЬНОГО МЕНЮ КОНЕЦ    |*
//                        **************************************
//                            ________________________
//                            |    РАЗДЕЛ МАШИНЫ     |
// (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ" НА ЭКРАНЕ МАШИНОК -------------------(1)-OK!
        btn_settings_washer_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){Intent intent = new Intent(getActivity(), PrefActivity_dozator.class);startActivity(intent);}else{
                    Intent intent = new Intent(getActivity(), PrefActivity_washer.class);startActivity(intent);  }  }
        });
// (1.1) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 1" НА ЭКРАНЕ МАШИНОК ----------------(1)-OK!
        num1_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){Intent intent = new Intent(getActivity(), PrefActivity_dozator1.class);startActivity(intent);}else{
                    Intent intent = new Intent(getActivity(), PrefActivity_washer1.class);startActivity(intent);}    }
        });
// (1.2) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 2" НА ЭКРАНЕ МАШИНОК ----------------(2)-OK!
        num2_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){Intent intent = new Intent(getActivity(), PrefActivity_dozator2.class);startActivity(intent);}else{
                    Intent intent = new Intent(getActivity(), PrefActivity_washer2.class);startActivity(intent); }   }
        });
// (1.3) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 3" НА ЭКРАНЕ МАШИНОК ----------------(3)-OK!
        num3_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){Intent intent = new Intent(getActivity(), PrefActivity_dozator3.class);startActivity(intent);}else{
                Intent intent = new Intent(getActivity(), PrefActivity_washer3.class);startActivity(intent);   } }
        });
// (1.4) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 4" НА ЭКРАНЕ МАШИНОК ----------------(4)-OK!
        num4_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(dozator_true==1){Intent intent = new Intent(getActivity(), PrefActivity_dozator4.class);startActivity(intent);}else{
                Intent intent = new Intent(getActivity(), PrefActivity_washer4.class);startActivity(intent);  }  }
        });
// (1.5) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 5" НА ЭКРАНЕ МАШИНОК ----------------(5)-OK!
        num5_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(), PrefActivity_washer5.class);startActivity(intent);    }
        });
// (1.6) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 6" НА ЭКРАНЕ МАШИНОК ----------------(6)-OK!
        num6_settings.setOnClickListener(new View.OnClickListener(){public void onClick(View v){Intent intent = new Intent(getActivity(), PrefActivity_washer6.class);startActivity(intent);}});
// (1.7) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 7" НА ЭКРАНЕ МАШИНОК ----------------(7)-OK!
        num7_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(), PrefActivity_washer7.class);startActivity(intent);}
        });
// (1.8) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 8" НА ЭКРАНЕ МАШИНОК ----------------(8)-OK!
        num8_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(), PrefActivity_washer8.class);startActivity(intent);}
        });
        num9_settings.setOnClickListener(new View.OnClickListener(){public void onClick(View v){Intent intent=new Intent(getActivity(), PrefActivity_washer9.class);startActivity(intent);}});
// (1.10) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 10" НА ЭКРАНЕ МАШИНОК ----------------(10)-OK!
        num10_settings.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {Intent intent = new Intent(getActivity(), PrefActivity_washer10.class);startActivity(intent);}});
// (1.11) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 11" НА ЭКРАНЕ МАШИНОК ----------------(11)-OK!
        num11_settings.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {Intent intent = new Intent(getActivity(), PrefActivity_washer11.class);startActivity(intent);}});
// (1.12) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 12" НА ЭКРАНЕ МАШИНОК ----------------(12)-OK!
        num12_settings.setOnClickListener(new View.OnClickListener() {public void onClick(View v) {Intent intent = new Intent(getActivity(),PrefActivity_washer12.class);startActivity(intent);}});
//                            _____________________
//                            |    РАЗДЕЛ СМС     |
// (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ" НА ЭКРАНЕ СМС НАСТРОЕК -----------(1)-OK!
        btn_settings_sms_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(),PrefActivity_sms.class);startActivity(intent);}});
// (2) НАЖИМАЕМ КНОПКУ    "ТЕЛ. ДИРЕКТОРА" НА  ЭКРАНЕ СМС НАСТРОЕК ----------(2)-OK!
        btn_settings_sms_director.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(),PrefActivity_sms_director.class);startActivity(intent);}
        });
// (3) НАЖИМАЕМ КНОПКУ    "ТЕЛ. МЕНЕДЖЕРА" НА  ЭКРАНЕ СМС НАСТРОЕК ----------(3)-OK!
        btn_settings_sms_manager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(),PrefActivity_sms_manager.class);startActivity(intent);}
        });
// (4) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ ЭЛ-ВО" НА ЭКРАНЕ СМС НАСТРОЕК -------------(4)-OK!
        btn_settings_sms_otchet_power.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(),PrefActivity_sms_error_volts.class);startActivity(intent);}
        });
// (5) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ О ПОЛОМКЕ" НА  ЭКРАНЕ СМС НАСТРОЕК --------(5)-OK!
        btn_settings_sms_otchet_admin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent = new Intent(getActivity(),PrefActivity_sms_error_door.class);startActivity(intent);}
        });
// (7) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ О ЗАВЕРШЕНИИ ДЛЯ КЛИЕНТОВ" НА  ЭКРАНЕ СМС НАСТРОЕК -(7)-OK!
        btn_settings_sms_otchet_start_washer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_sms_start_OK.class);startActivity(intent);} });
// (6) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ О ЗАПУСКАХ" НА  ЭКРАНЕ СМС НАСТРОЕК -(6)-OK!
        btn_settings_sms_otchet_client.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_sms_client.class);startActivity(intent);} });
// (7) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ О ЗАВЕРШЕНИИ ДЛЯ КЛИЕНТОВ" НА  ЭКРАНЕ СМС НАСТРОЕК -(7)-OK!
        btn_settings_sms_otchet_client.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_sms_client.class);startActivity(intent);}
        });
//                            _____________________
//                            | РАЗДЕЛ BLUETOOTH  |
// (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ"  НА ЭКРАНЕ BLUETOOTH НАСТРОЕК ---(1)-OK!
        btn_settings_bluetooth_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // dialog_out_of_service();

            }
        });
// (2) НАЖИМАЕМ КНОПКУ "BLUETOOTH ADAPTER" ----------------------------------(2)-OK!
        btn_settings_bluetooth_adapter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_bluetooth_adapter();
                if(!mBluetoothAdapter.isEnabled()){
                    btn_settings_bluetooth_adapter.setChecked(false);
                }else{btn_settings_bluetooth_adapter.setChecked(true);}}
        });
// (3) НАЖИМАЕМ КНОПКУ "BLUETOOTH SEARCH LIST" ------------------------------(3)-OK!
        btn_settings_bluetooth_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {//display_settings_activity = 1;
                btn_settings_bluetooth_list.setVisibility(View.GONE);
                if(!mBluetoothAdapter.isEnabled()){ mBluetoothAdapter.enable(); }
                Intent serIntent=new Intent(getActivity(),DeviceListActivity.class);
                startActivityForResult(serIntent,REQUEST_CONNECT_DEVICE_SECURE);  }
        });
// (4) НАЖИМАЕМ КНОПКУ "BLUETOOTH RECONNECT" --------------------------------(4)-OK!
        btn_settings_bluetooth_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // reconnect_Device();
              //  Intent serIntent=new Intent(getActivity(),PrefActivity_bluetooth_base.class);
              //  startActivity(serIntent);
                startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));

            }
        });
//                            _____________________
//                            |   РАЗДЕЛ ТАРИФЫ   |
// (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ"  НА ЭКРАНЕ ТАРИФОВ --------------(1)-OK!
        btn_settings_tarif_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif.class);startActivity(intent);}
        });
// (2) НАЖИМАЕМ КНОПКУ "TARIF VALUTA"  --------------------------------------(2)-OK!
        btn_settings_tarif_valuta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif_valuta.class);startActivity(intent);} });
// (3) НАЖИМАЕМ КНОПКУ "TARIF SBER" -----------------------------------------(3)-OK!
        btn_settings_tarif_sber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif_sber.class);startActivity(intent);}
        });
// (4) НАЖИМАЕМ КНОПКУ "TARIF QIWI" -----------------------------------------(4)-OK!
        btn_settings_tarif_qiwi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent=new Intent(getActivity(),PrefActivity_tarif_qiwi.class);startActivity(intent);
                Intent intent=new Intent(getActivity(),PrefActivity_tarif_time.class);startActivity(intent);
            }
        });
// (5) НАЖИМАЕМ КНОПКУ "TARIF NAME" -----------------------------------------(5)-OK!
        btn_settings_mode_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif_name.class);startActivity(intent);}
        });
// (6) НАЖИМАЕМ КНОПКУ "TARIF IRON" -----------------------------------------(6)-OK!
        btn_settings_tarif_iron.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {dialog_out_of_service();
            }
        });
// (7) НАЖИМАЕМ КНОПКУ "TARIF MODE" -----------------------------------------(7)-OK!
        btn_settings_tarif_mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif_mode.class);startActivity(intent);}
        });
// (8) НАЖИМАЕМ КНОПКУ "TARIF WASH" -----------------------------------------(8)-OK!
        btn_settings_tarif_wash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif_machine.class);startActivity(intent); }
        });
// (9) НАЖИМАЕМ КНОПКУ "TARIF YANDEX" ---------------------------------------(9)-OK!
        btn_settings_tarif_yandex.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {Intent intent=new Intent(getActivity(),PrefActivity_tarif_yandex.class);startActivity(intent);}
        });

//                           ________________________
//                           |    РАЗДЕЛ СИСТЕМА    |
        btn_settings_system_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {// ---------------------------------------------"ОСНОВНЫЕ НАСТРОЙКИ"
                Intent intent = new Intent(getActivity(), PrefActivity.class); startActivity(intent);}
        });
        btn_settings_system_display.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {// ---------------------------------------------"ОСНОВНЫЕ НАСТРОЙКИ"
                startActivity(new Intent(Settings.ACTION_DISPLAY_SETTINGS));

            }
        });
        btn_settings_system_keyboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {// ---------------------------------------------"ОСНОВНЫЕ НАСТРОЙКИ"
                startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS));
            }
        });
        btn_settings_system_com.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // ---------------------------------------------"Programm.COM"
                layout_log.setVisibility(View.VISIBLE);btn_close_layout_com.setVisibility(View.VISIBLE); dialog_program_com();}
        });
        btn_settings_system_autoreboot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // ---------------------------------------------"Programm.COM"
                startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));}
        });
        btn_close_layout_com.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {  // -----------------------------------------------"ЗАКРЫТЬ Programm.COM"
                layout_log.setVisibility(View.INVISIBLE);layout_settings_system.setVisibility(View.VISIBLE); }
        });
        btn_settings_system_optionsplus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // --------------------------------------
              Intent intent=new Intent(getActivity(),PrefActivity_system_dozator.class);startActivity(intent);kluchik=2; } });
        btn_settings_system_log_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { //-----------------------------------------"SYSTEM LOG.e"
                layout_settings_system.setVisibility(View.INVISIBLE);
                layout_settings.setBackgroundResource(R.drawable.wash_ready_background);
                layout_settings_action_bar_leftmenu.setVisibility(View.GONE);
                layout_log.setVisibility(View.INVISIBLE);
                layout_system_log.setVisibility(View.VISIBLE);
                layout_settings_action_bar.setVisibility(View.VISIBLE); displayInfo();
                if(statusStr.contentEquals("1")){img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
                    img_settings_action_bar_icon_battary.setVisibility(View.GONE);}
                else{img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
                    img_settings_action_bar_icon_electro.setVisibility(View.GONE); } }
        });
        btn_settings_system_fullscreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){ // --------------------------------------"FULLSCREEN"
                Build.VERSION_CODES vc = new Build.VERSION_CODES();
                Build.VERSION vr = new Build.VERSION();
                DevicePolicyManager dpm;
                ComponentName deviceAdmin;
                if (vr.SDK_INT <= vc.KITKAT) {
                      if(mHideSystemBar==0){mHideSystemBar=1;hideSystemBar();}
                      else{mHideSystemBar=0;showSystemBar(); dialog_fullscreen_open();}
                } else {
                    if (mHideSystemBar == 0) {  mHideSystemBar = 1;
                        deviceAdmin = new ComponentName(getActivity(), AdminReceiver.class);
                        dpm = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
                        if (dpm.isDeviceOwnerApp(getActivity().getPackageName())) {
                            dpm.setLockTaskPackages(deviceAdmin, new String[] { getActivity().getPackageName() });
                        }
                        getActivity().startLockTask();
                        Common.becomeHomeActivity(getActivity());
                       // Intent intent = new Intent(getActivity(), KioskModeDemo.class);
                        //startActivity(intent);
                    } else {  mHideSystemBar = 0; getActivity().stopLockTask();  }
                }

            } });
        btn_settings_system_battary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { // -----------------------------------------"BATTARY"
                  Intent intent = new Intent(getActivity(), BattaryStatus.class);
                //  Intent intent = new Intent(getActivity(), KioskModeDemo2.class);
                // Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
                  startActivity(intent);
               // getActivity().stopLockTask();
            }  });
        btn_settings_system_licence.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_licence.class);
                // Intent intent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
                startActivity(intent);

            }
        }); // ------------------"FULLSCREEN"
//                         _______________________
//                         |  РАЗДЕЛ СТАТИСТИКА  |
        btn_settings_stat_inkass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(),"laundry_stat.xls - saved",Toast.LENGTH_LONG).show();
                FileOutputStream fos;
                try {
                    File myFile = new File("/sdcard/laundry_stat.csv");
                    myFile.createNewFile();
                    FileOutputStream fOut = new FileOutputStream(myFile);
                    OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
                    int all_w1=0;all_w1=stat_w1 +statmod1_w1 +statmod2_w1 +statmod3_w1 +statmod4_w1 +statmod5_w1 +statmod6_w1;
                    int all_w2=0;all_w2=stat_w2 +statmod1_w2 +statmod2_w2 +statmod3_w2 +statmod4_w2 +statmod5_w2 +statmod6_w2;
                    int all_w3=0;all_w3=stat_w3 +statmod1_w3 +statmod2_w3 +statmod3_w3 +statmod4_w3 +statmod5_w3 +statmod6_w3;
                    int all_w4=0;all_w4=stat_w4 +statmod1_w4 +statmod2_w4 +statmod3_w4 +statmod4_w4 +statmod5_w4 +statmod6_w4;
                    int all_w5=0;all_w5=stat_w5 +statmod1_w5 +statmod2_w5 +statmod3_w5 +statmod4_w5 +statmod5_w5 +statmod6_w5;
                    int all_w6=0;all_w6=stat_w6 +statmod1_w6 +statmod2_w6 +statmod3_w6 +statmod4_w6 +statmod5_w6 +statmod6_w6;
                    int all_w7=0;all_w7=stat_w7 +statmod1_w7 +statmod2_w7 +statmod3_w7 +statmod4_w7 +statmod5_w7 +statmod6_w7;
                    int all_w8=0;all_w8=stat_w8 +statmod1_w8 +statmod2_w8 +statmod3_w8 +statmod4_w8 +statmod5_w8 +statmod6_w8;
                    int all_w9=0;all_w9=stat_w9 +statmod1_w9 +statmod2_w9 +statmod3_w9 +statmod4_w9 +statmod5_w9 +statmod6_w9;
                    int all_w10=0;all_w10=stat_w10 +statmod1_w10 +statmod2_w10 +statmod3_w10 +statmod4_w10 +statmod5_w10 +statmod6_w10;
                    int all_w11=0;all_w11=stat_w11 +statmod1_w11 +statmod2_w11 +statmod3_w11 +statmod4_w11 +statmod5_w11 +statmod6_w11;
                    int all_w12=0;all_w12=stat_w12 +statmod1_w12 +statmod2_w12 +statmod3_w12 +statmod4_w12 +statmod5_w12 +statmod6_w12;
                    myOutWriter.append(timeformat.format(new Date())+"  "    +";"+"Wash1"+";"+"Wash2"+";"+"Wash3"+";"+"Wash4"+";"+"Wash5"+";"+"Wash6"+";"+"Wash7"+";"+"Wash8"+";"+"Wash9"+";"+"Wash10"+";"+"Wash11"+";"+"Wash12"+"\n");
                    myOutWriter.append("All Washing: "    +";"+all_w1+";"+all_w2+";"+all_w3+";"+all_w4+";"+all_w5+";"+all_w6+";"+all_w7+";"+all_w8+";"+all_w9+";"+all_w10+";"+all_w11+";"+all_w12+"\n");
                    myOutWriter.append("Simple Start: "   +";"+stat_w1+";"+stat_w2+";"+stat_w3+";"+stat_w4+";"+stat_w5+";"+stat_w6+";"+stat_w7+";"+stat_w8+";"+stat_w9+";"+stat_w10+";"+stat_w11+";"+stat_w12    +"\n");
                    myOutWriter.append("Fast wash: "      +";"+statmod1_w1+";"+statmod1_w2+";"+statmod1_w3+";"+statmod1_w4+";"+statmod1_w5+";"+statmod1_w6+";"+statmod1_w7+";"+statmod1_w8+";"+statmod1_w9+";"+statmod1_w10+";"+statmod1_w11+";"+statmod1_w12+"\n");
                    myOutWriter.append("Intensive: "      +";"+statmod2_w1+";"+statmod2_w2+";"+statmod2_w3+";"+statmod2_w4+";"+statmod2_w5+";"+statmod2_w6+";"+statmod2_w7+";"+statmod2_w8+";"+statmod2_w9+";"+statmod2_w10+";"+statmod2_w11+";"+statmod2_w12+"\n");
                    myOutWriter.append("Cotton: "         +";"+statmod3_w1+";"+statmod3_w2+";"+statmod3_w3+";"+statmod3_w4+";"+statmod3_w5+";"+statmod3_w6+";"+statmod3_w7+";"+statmod3_w8+";"+statmod3_w9+";"+statmod3_w10+";"+statmod3_w11+";"+statmod3_w12+"\n");
                    myOutWriter.append("Hand wash: "      +";"+statmod4_w1+";"+statmod4_w2+";"+statmod4_w3+";"+statmod4_w4+";"+statmod4_w5+";"+statmod4_w6+";"+statmod4_w7+";"+statmod4_w8+";"+statmod4_w9+";"+statmod4_w10+";"+statmod4_w11+";"+statmod4_w12+"\n");
                    myOutWriter.append("Sintetics: "      +";"+statmod5_w1+";"+statmod5_w2+";"+statmod5_w3+";"+statmod5_w4+";"+statmod5_w5+";"+statmod5_w6+";"+statmod5_w7+";"+statmod5_w8+";"+statmod5_w9+";"+statmod5_w10+";"+statmod5_w11+";"+statmod5_w12+"\n");
                    myOutWriter.append("Rinse: "          +";"+statmod6_w1+";"+statmod6_w2+";"+statmod6_w3+";"+statmod6_w4+";"+statmod6_w5+";"+statmod6_w6+";"+statmod6_w7+";"+statmod6_w8+";"+statmod6_w9+";"+statmod6_w10+";"+statmod6_w11+";"+statmod6_w12+"\n");
                    myOutWriter.append("Admin SMS Start: "+";"+statsms_w1+ ";"+statsms_w2 +";"+statsms_w3 +";"+statsms_w4 +";"+statsms_w5 +";"+statsms_w6 +";"+statsms_w7 +";"+statsms_w8 +";"+statsms_w9 +";"+statsms_w10 +";"+statsms_w11 +";"+statsms_w12 +"\n");

                    myOutWriter.close();
                    fOut.close();




                } catch (FileNotFoundException e) {e.printStackTrace();}
                catch (IOException e) {e.printStackTrace();}
                dialog_out_of_service(); }
        });//  ----------------"ИНКАССАЦИЯ"

        mSettings = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);

        // money_kup.setText("" + money_vneseno);// выводим данные о деньгах на экран
        //  boolean hasVisited = mSettings.getBoolean("hasVisited", false);
        //  if (hasVisited) {
        //      Log.d(TAG, "SetupChat первый запуск открываем лист девайс" );
        //      SharedPreferences.Editor editor = mSettings.edit();editor.putBoolean("hasVisited", true);editor.commit();
        // display_settings_activity = 1;
        //     Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);// Launch the DeviceListActivity to see devices and do scan
        //     startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        // } else {
        // app_start=1;
        Log.d(TAG, "зашли в SetupChat и сделали app_start++" );
        app_start++;

        // reconnect_Device();
        //   }

    }// end line Runnuble UI -----------------------------SetupChat-------------------------------------

    /**
     * Makes this device discoverable.
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            //  Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            //  mButton.setEnabled(true);
            // counter_bluetooth_state=-1;
            //  reconnect_Device();
            Log.d(TAG, "sendMessage STATE_NONE, поэтому return " );
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            Log.d(TAG, "sendMessage STATE_CONNECTED отправляем "+send+"");
            mChatService.write(send);
            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();sendMessage(message);
            }
            return true;
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }


    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

// --------------------------  my code  -----------------------------------------------------

    private void set_wash_image(int parame){
        if ((parame==0)||(parame==1)||(parame==2)) {
            wash1.setVisibility(View.VISIBLE);wash1_settings.setVisibility(View.VISIBLE);wash1.setBackgroundResource(R.drawable.wash_go);wash1_settings.setBackgroundResource(R.drawable.wash_go);if((dozator_true==1)&&(countwash>2)){wash1_settings.setBackgroundResource(R.drawable.iron);}else if((dozator_true==1)&&(countwash<3)){wash1_settings.setBackgroundResource(R.drawable.tide75);}
            wash2.setVisibility(View.VISIBLE);wash2_settings.setVisibility(View.VISIBLE);wash2.setBackgroundResource(R.drawable.wash_go);wash2_settings.setBackgroundResource(R.drawable.wash_go);if((dozator_true==1)&&(countwash>2)){wash2_settings.setBackgroundResource(R.drawable.iron);}else if((dozator_true==1)&&(countwash<3)){wash2_settings.setBackgroundResource(R.drawable.tide150);}
            wash3.setVisibility(View.VISIBLE);wash3_settings.setVisibility(View.VISIBLE);wash3.setBackgroundResource(R.drawable.wash_go);wash3_settings.setBackgroundResource(R.drawable.wash_go);if((dozator_true==1)&&(countwash>=3)){wash3_settings.setBackgroundResource(R.drawable.tide75);}
            wash4.setVisibility(View.VISIBLE);wash4_settings.setVisibility(View.VISIBLE);wash4.setBackgroundResource(R.drawable.wash_go);wash4_settings.setBackgroundResource(R.drawable.wash_go);if((dozator_true==1)&&(countwash>=4)){wash4_settings.setBackgroundResource(R.drawable.tide150);}
            wash5.setVisibility(View.VISIBLE);wash5_settings.setVisibility(View.VISIBLE);wash5.setBackgroundResource(R.drawable.wash_go);wash5_settings.setBackgroundResource(R.drawable.wash_go);
            wash6.setVisibility(View.VISIBLE);wash6_settings.setVisibility(View.VISIBLE);wash6.setBackgroundResource(R.drawable.wash_go);wash6_settings.setBackgroundResource(R.drawable.wash_go);
            wash7.setVisibility(View.VISIBLE);wash7_settings.setVisibility(View.VISIBLE);wash7.setBackgroundResource(R.drawable.wash_go);wash7_settings.setBackgroundResource(R.drawable.wash_go);
            wash8.setVisibility(View.VISIBLE);wash8_settings.setVisibility(View.VISIBLE);wash8.setBackgroundResource(R.drawable.wash_go);wash8_settings.setBackgroundResource(R.drawable.wash_go);
            wash9.setVisibility(View.VISIBLE);wash9_settings.setVisibility(View.VISIBLE);wash9.setBackgroundResource(R.drawable.wash_go);wash9_settings.setBackgroundResource(R.drawable.wash_go);
            wash10.setVisibility(View.VISIBLE);wash10_settings.setVisibility(View.VISIBLE);wash10.setBackgroundResource(R.drawable.wash_go);wash10_settings.setBackgroundResource(R.drawable.wash_go);
            wash11.setVisibility(View.VISIBLE);wash11_settings.setVisibility(View.VISIBLE);wash11.setBackgroundResource(R.drawable.wash_go);wash11_settings.setBackgroundResource(R.drawable.wash_go);
            wash12.setVisibility(View.VISIBLE);wash12_settings.setVisibility(View.VISIBLE);wash12.setBackgroundResource(R.drawable.wash_go);wash12_settings.setBackgroundResource(R.drawable.wash_go);
        } else {
            wash1.setVisibility(View.VISIBLE);wash1_settings.setVisibility(View.VISIBLE);wash1.setBackgroundResource(R.drawable.maytag_go);wash1_settings.setBackgroundResource(R.drawable.maytag_go);
            wash2.setVisibility(View.VISIBLE);wash2_settings.setVisibility(View.VISIBLE);wash2.setBackgroundResource(R.drawable.maytag_go);wash2_settings.setBackgroundResource(R.drawable.maytag_go);
            wash3.setVisibility(View.VISIBLE);wash3_settings.setVisibility(View.VISIBLE);wash3.setBackgroundResource(R.drawable.maytag_go);wash3_settings.setBackgroundResource(R.drawable.maytag_go);
            wash4.setVisibility(View.VISIBLE);wash4_settings.setVisibility(View.VISIBLE);wash4.setBackgroundResource(R.drawable.maytag_go);wash4_settings.setBackgroundResource(R.drawable.maytag_go);
            wash5.setVisibility(View.VISIBLE);wash5_settings.setVisibility(View.VISIBLE);wash5.setBackgroundResource(R.drawable.maytag_go);wash5_settings.setBackgroundResource(R.drawable.maytag_go);
            wash6.setVisibility(View.VISIBLE);wash6_settings.setVisibility(View.VISIBLE);wash6.setBackgroundResource(R.drawable.maytag_go);wash6_settings.setBackgroundResource(R.drawable.maytag_go);
            wash7.setVisibility(View.VISIBLE);wash7_settings.setVisibility(View.VISIBLE);wash7.setBackgroundResource(R.drawable.maytag_go);wash7_settings.setBackgroundResource(R.drawable.maytag_go);
            wash8.setVisibility(View.VISIBLE);wash8_settings.setVisibility(View.VISIBLE);wash8.setBackgroundResource(R.drawable.maytag_go);wash8_settings.setBackgroundResource(R.drawable.maytag_go);
            wash9.setVisibility(View.VISIBLE);wash9_settings.setVisibility(View.VISIBLE);wash9.setBackgroundResource(R.drawable.maytag_go);wash9_settings.setBackgroundResource(R.drawable.maytag_go);
            wash10.setVisibility(View.VISIBLE);wash10_settings.setVisibility(View.VISIBLE);wash10.setBackgroundResource(R.drawable.maytag_go);wash10_settings.setBackgroundResource(R.drawable.maytag_go);
            wash11.setVisibility(View.VISIBLE);wash11_settings.setVisibility(View.VISIBLE);wash11.setBackgroundResource(R.drawable.maytag_go);wash11_settings.setBackgroundResource(R.drawable.maytag_go);
            wash12.setVisibility(View.VISIBLE);wash12_settings.setVisibility(View.VISIBLE);wash12.setBackgroundResource(R.drawable.maytag_go);wash12_settings.setBackgroundResource(R.drawable.maytag_go);
        }
    }

    /**
     * Закрываем все и обратно на первый экран
     * Set disable & invisible all Layout Views
     * кроме статусов Bluetooth и кнопки OK первого экрана
     */
    private void go_home() { // VISIBLE LAYOUTS SCREEN
        //  mMediaPlayer.stop();
        display_settings_activity=0;
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_choose_mode.setVisibility(View.INVISIBLE);
        layout_start_anim.setVisibility(View.INVISIBLE);
        // layout_bt_search.setVisibility(View.INVISIBLE);
        layout_settings.setVisibility(View.INVISIBLE);
        //  layout_bt_not.setVisibility(View.INVISIBLE);
        layout_bg.setVisibility(View.VISIBLE);
        layout_action_bar.setVisibility(View.INVISIBLE);// запускаем бар
        if(dozator_true==1) {
            layout_action_bar.setVisibility(View.GONE);
            //final Animation homeScaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
            //layout_bg.setVisibility(View.VISIBLE);  layout_bg.startAnimation(homeScaleAnimation);

            layout_bg.setBackgroundResource(R.drawable.dozator_home);
            //layout_bg.setBackgroundResource(R.drawable.dozator_home_seavilliya);


        }else {
            if (eng==1){
                //final Animation homeScaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                //layout_bg.setVisibility(View.VISIBLE);  layout_bg.startAnimation(homeScaleAnimation);
                layout_bg.setBackgroundResource(R.drawable.step_home2_hd_eng);}else{
                if(home_bg==1){home_bg=2;
                    //  final Animation homeScaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                    layout_bg.setVisibility(View.VISIBLE);
                    layout_bg.setBackgroundResource(R.drawable.main_logo1);
                    //layout_bg.setBackgroundResource(R.drawable.main_logo1seavilliya);

                    // layout_bg.startAnimation(homeScaleAnimation);
                    imageViewScaleHome.setVisibility(View.VISIBLE);
                    imageViewScaleHome.setBackgroundResource(R.drawable.main_logo1);
                   // imageViewScaleHome.setBackgroundResource(R.drawable.main_logo1seavilliya);
                    //   imageViewScaleHome.startAnimation(homeScaleAnimation);
                    //  layout_bg.setBackgroundResource(R.drawable.wash_ready_background);
                }
                else if(home_bg==2){home_bg=1;
                    //  final Animation homeScaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                    layout_bg.setVisibility(View.VISIBLE);
                    layout_bg.setBackgroundResource(R.drawable.main_logo2);
                    //layout_bg.setBackgroundResource(R.drawable.main_logo1seavilliya);
                    // layout_bg.startAnimation(homeScaleAnimation);
                    imageViewScaleHome.setVisibility(View.VISIBLE);
                    imageViewScaleHome.setBackgroundResource(R.drawable.main_logo2);
                   // imageViewScaleHome.setBackgroundResource(R.drawable.main_logo1seavilliya);
                    //  imageViewScaleHome.startAnimation(homeScaleAnimation);
                    //  layout_bg.setBackgroundResource(R.drawable.wash_ready_background);
                }
       /* else if(home_bg==3){home_bg=1;
            final Animation homeScaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
            layout_bg.setVisibility(View.VISIBLE); // layout_bg.startAnimation(homeScaleAnimation);
            imageViewScaleHome.setVisibility(View.VISIBLE); imageViewScaleHome.setBackgroundResource(R.drawable.step_one_tree_hd);
          //  imageViewScaleHome.setBackgroundResource(R.drawable.main_logo3);
            imageViewScaleHome.startAnimation(homeScaleAnimation);
            layout_bg.setBackgroundResource(R.drawable.wash_ready_background);}*/}}


        // layout_bg.setBackgroundResource(R.drawable.home5);
        layout_home.setVisibility(View.VISIBLE);// определяем фон главной страницы для возврата
        mButton.setVisibility(View.VISIBLE);
        mButton.setEnabled(true);// параметры активируем кнопку "Ok"

        img_action_bar_icon_wash.setVisibility(View.INVISIBLE);
        wash_number.setVisibility(View.INVISIBLE);


        if (eng == 1) {
            img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno_eng);
        } else {
            img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno);
        }


        money_kup.setVisibility(View.VISIBLE);
        // ставим иконку Connected Bluetooth, так как сюда по другому не попасть
        //  img_action_bar_icon_not_connected.setVisibility(View.INVISIBLE);
        //  img_action_bar_icon_lost_connected.setVisibility(View.INVISIBLE);
        //  img_action_bar_icon_laundry_connected.setVisibility(View.INVISIBLE);
        displayInfo();// проверяем батареечку
        if (statusStr.contentEquals("1")) {
            if(eng==1){img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                img_action_bar_icon_electro.setImageResource(R.drawable.action_bar_icon_electro_eng);
                img_action_bar_icon_battary.setVisibility(View.INVISIBLE);
            }else{
                img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                img_action_bar_icon_electro.setImageResource(R.drawable.action_bar_icon_electro);
                img_action_bar_icon_battary.setVisibility(View.INVISIBLE);}
        } else {
            if(eng==1){img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                img_action_bar_icon_battary.setImageResource(R.drawable.action_bar_icon_battary_eng);
                img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
            }else{
                img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                img_action_bar_icon_battary.setImageResource(R.drawable.action_bar_icon_battary);
                img_action_bar_icon_electro.setVisibility(View.INVISIBLE);}
        }

    }


    private void prepare_flipper_goto_numbers(){
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            //  Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            flipper_goto_numbers();}
    }

    private void flipper_goto_numbers() {

        //   final ViewFlipper viewFlipper = (ViewFlipper) getActivity().findViewById(R.id.viewflipper);
        //   Animation animationFlipIn = AnimationUtils.loadAnimation(getActivity(), R.anim.flipin);
        //   Animation animationFlipOut = AnimationUtils.loadAnimation(getActivity(), R.anim.flipout);
        //   viewFlipper.setInAnimation(animationFlipIn);
        //   viewFlipper.setOutAnimation(animationFlipOut);
        //   viewFlipper.stopFlipping();
        //  viewFlipper.setFlipInterval(20000);
        //   viewFlipper.showNext();
        numbers_selected(countwash);
    }

    /**
     * Prepair to start screen client
     *
     * @param remont                  (0-off, 1-on)
     * @param washisdry(0-wash,1-dry)
     * @param program(0-simple start,1-program,2-programm_temperatura)
     * @param washmode                (0-open choose_mode screen, 1-mode1, 2-mode2, ..)
     */
    private void prepair_to_start(int remont, int washisdry, int program, int washmode) {
//VISIBLE LAYOUTS SCREEN
        btn_keyboard_temp_count_press=1;
        layout_number_selected.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        // layout_bt_search.setVisibility(View.GONE);
        layout_settings.setVisibility(View.GONE);
        //  layout_bt_not.setVisibility(View.GONE);
        layout_home.setVisibility(View.GONE);
        layout_bg.setVisibility(View.VISIBLE);
        imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
        layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);
        layout_prepair_start.setVisibility(View.VISIBLE);// фон главной страницы
        layout1.setVisibility(View.VISIBLE);
        wash_mode.setVisibility(View.VISIBLE);
//определяем видимость денег и валюты
        money_tarif.setVisibility(View.VISIBLE);
        money_tarif_valuta.setVisibility(View.VISIBLE);
        if (valuta == 1) {money_tarif_valuta.setText("руб");}
        if (valuta == 2) {money_tarif_valuta.setText("euro");}
        if (valuta == 3) {money_tarif_valuta.setText("руб");}
        if (valuta == 4) {money_tarif_valuta.setText("тенге");}
        txt_layout_prepair_wash1.setVisibility(View.VISIBLE);
        if(eng==1){txt_layout_prepair_wash1.setText("PRICE"); }else {
            txt_layout_prepair_wash1.setText("ТАРИФ"); }
        txt_footer_prepair.setVisibility(View.VISIBLE);
        if ((program == 0) || (program == 3)) {
            if(eng==1){txt_footer_prepair.setText("Attention! Before you start the machine pay."); }else {
                txt_footer_prepair.setText("Внимание! Перед оплатой включить машину."); }
            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
            txt_footer_prepair.startAnimation(txt_Animation);
        }
//фон по умолчанию для сушки, все остальные скрыты
        prepair_background_dry1.setVisibility(View.INVISIBLE);
        prepair_background_remont1.setVisibility(View.INVISIBLE);
        prepair_background1.setVisibility(View.INVISIBLE);
        prepair_background2.setVisibility(View.INVISIBLE);
        prepair_background3.setVisibility(View.INVISIBLE);
        prepair_background_mode1.setVisibility(View.INVISIBLE);
        prepair_background_mode2.setVisibility(View.INVISIBLE);
        prepair_background_mode3.setVisibility(View.INVISIBLE);
        prepair_background_mode4.setVisibility(View.INVISIBLE);
        prepair_background_mode5.setVisibility(View.INVISIBLE);
        prepair_background_mode6.setVisibility(View.INVISIBLE);
//открываем клавиатуру
        layout_smart_keyboard.setVisibility(View.VISIBLE);
        final Animation smart_buttons_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_buttons);
        layout_smart_keyboard.startAnimation(smart_buttons_Animation);
        btn_numbers_select.setImageResource(R.drawable.ic_local_laundry_service_white_48dp);
        if (eng==1){
            btn_keyboard_choose_prog.setVisibility(View.GONE);
            btn_keyboard_choose_prog.setEnabled(false);
            btn_keyboard_sms.setVisibility(View.GONE);
            btn_keyboard_temp.setVisibility(View.GONE);
            btn_keyboard_pay.setVisibility(View.GONE);
            btn_keyboard_creditcard.setVisibility(View.GONE);
        }else{
            btn_keyboard_choose_prog.setVisibility(View.VISIBLE);
            btn_keyboard_choose_prog.setEnabled(false);
            btn_keyboard_sms.setVisibility(View.VISIBLE);
            btn_keyboard_temp.setVisibility(View.VISIBLE);
            btn_keyboard_pay.setVisibility(View.VISIBLE);
            btn_keyboard_creditcard.setVisibility(View.VISIBLE);}
        start.setVisibility(View.GONE);
        start.setEnabled(false);
//состояние кнопок берем из наличия подключенных опций
        if (sms_client_notice == 1) {
            // btn_keyboard_sms.setImageResource(R.drawable.smart_keyboard_sms);
            btn_keyboard_sms.setEnabled(true);
            btn_keyboard_sms.setImageResource(R.drawable.ic_notifications_none_white_48dp);
        } else {
            // btn_keyboard_sms.setImageResource(R.drawable.smart_keyboard_sms_off);
            btn_keyboard_sms.setEnabled(false);

            btn_keyboard_sms.setImageResource(R.drawable.wash_ready_background);
            // btn_keyboard_sms.setVisibility(View.GONE);
        }
        if (program == 1) {
            //  btn_keyboard_choose_prog.setImageResource(R.drawable.smart_keyboard_mode);
            btn_keyboard_choose_prog.setEnabled(true);
            btn_keyboard_choose_prog.setImageResource(R.drawable.ic_collections_white_48dp);
            btn_keyboard_temp.setEnabled(false);
            btn_keyboard_temp.setImageResource(R.drawable.wash_ready_background);
        } else {
            //  btn_keyboard_choose_prog.setImageResource(R.drawable.smart_keyboard_mode_off);
            btn_keyboard_choose_prog.setEnabled(false);
            btn_keyboard_choose_prog.setImageResource(R.drawable.wash_ready_background);
            //btn_keyboard_choose_prog.setVisibility(View.GONE);
        }
        if (program == 2) {
            // btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp);
            btn_keyboard_temp.setEnabled(true);
            btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp_2);

            btn_keyboard_choose_prog.setEnabled(true);
            btn_keyboard_choose_prog.setImageResource(R.drawable.ic_collections_white_48dp);
        } else {
            //  btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp_off);
            btn_keyboard_temp.setEnabled(false);
            btn_keyboard_temp.setImageResource(R.drawable.wash_ready_background);
            //   btn_keyboard_choose_prog.setEnabled(false);
            //   btn_keyboard_choose_prog.setImageResource(R.drawable.wash_ready_background);
            // btn_keyboard_temp.setVisibility(View.GONE);

        }
        if (dozator_true == 1) {
            //  btn_keyboard_choose_prog.setImageResource(R.drawable.smart_keyboard_mode);
            btn_keyboard_choose_prog.setEnabled(false);
            btn_keyboard_temp.setEnabled(false);
        }
        if (sberbank == 1) {
            // btn_keyboard_pay.setImageResource(R.drawable.smart_keyboard_sber);
            btn_keyboard_pay.setEnabled(true);
            btn_keyboard_pay.setImageResource(R.drawable.smart_keyboard_sber_2);
        } else {
            //  btn_keyboard_pay.setImageResource(R.drawable.smart_keyboard_sber_off);
            btn_keyboard_pay.setEnabled(false);
            btn_keyboard_pay.setImageResource(R.drawable.wash_ready_background);
            // btn_keyboard_pay.setVisibility(View.GONE);
        }
        if (creditcard == 1) {
            //  btn_keyboard_creditcard.setImageResource(R.drawable.smart_keyboard_creditcard);
            btn_keyboard_creditcard.setEnabled(true);
            btn_keyboard_creditcard.setImageResource(R.drawable.ic_credit_card_white_48dp);
        } else {
            //  btn_keyboard_creditcard.setImageResource(R.drawable.smart_keyboard_creditcard_off);
            btn_keyboard_creditcard.setEnabled(false);
            btn_keyboard_creditcard.setImageResource(R.drawable.wash_ready_background);
            // btn_keyboard_creditcard.setVisibility(View.GONE);
        }
//запускаем бар
        layout_action_bar.setVisibility(View.VISIBLE);
        //  img_action_bar_icon_laundry_connected.setVisibility(View.INVISIBLE);
        if(eng==1){img_action_bar_icon_wash.setVisibility(View.VISIBLE);
            img_action_bar_icon_wash.setImageResource(R.drawable.action_bar_icon_wash_eng);
        }else{
            if(dozator_true==1){wash_number.setVisibility(View.INVISIBLE);}else{
                img_action_bar_icon_wash.setVisibility(View.VISIBLE);
                img_action_bar_icon_wash.setImageResource(R.drawable.action_bar_icon_wash);}}
        //img_action_bar_icon_wash.setVisibility(View.VISIBLE);
        if(dozator_true==1){wash_number.setVisibility(View.INVISIBLE);}else{
            wash_number.setVisibility(View.VISIBLE);}
        if(eng==1){img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno_eng);
        }else{img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno);}
        money_kup.setVisibility(View.VISIBLE);
        if(valuta==3){        money_kup.setText(""+money_vneseno+"0");}else{money_kup.setText(""+money_vneseno);}
        //  img_action_bar_icon_not_connected.setVisibility(View.GONE);
        //  img_action_bar_icon_lost_connected.setVisibility(View.GONE);
        img_action_bar_icon_electro.setVisibility(View.GONE);
        img_action_bar_icon_battary.setVisibility(View.GONE);

//начало самой функции
        if (remont == 1) {// исключительный вариант, убираем все
            prepair_background_remont1.setVisibility(View.VISIBLE);// определяем фон
            layout_bg.setBackgroundResource(R.drawable.wash_remont);
            start.setVisibility(View.INVISIBLE);
            start.setEnabled(false);// устанавливаем индивидуальную клавиатуру
            btn_keyboard_choose_prog.setVisibility(View.INVISIBLE);
            btn_keyboard_choose_prog.setEnabled(false);
            btn_keyboard_sms.setVisibility(View.INVISIBLE);
            btn_keyboard_sms.setEnabled(false);
            btn_keyboard_temp.setVisibility(View.INVISIBLE);
            btn_keyboard_temp.setEnabled(false);
            btn_keyboard_pay.setVisibility(View.INVISIBLE);
            btn_keyboard_pay.setEnabled(false);
            btn_keyboard_creditcard.setVisibility(View.INVISIBLE);
            btn_keyboard_creditcard.setEnabled(false);
            layout1.setVisibility(View.INVISIBLE);
        } else if (remont == 0) {
            float mdtrf = 0;
            if (washisdry == 2) {
                txt_footer_prepair.setVisibility(View.VISIBLE);
                txt_footer_prepair.setText("Включите утюг на 5 минут");
                layout_bg.setBackgroundResource(R.drawable.iron_background);
                btn_keyboard_choose_prog.setVisibility(View.GONE);
                btn_keyboard_choose_prog.setEnabled(false);
                btn_keyboard_sms.setVisibility(View.GONE);
                btn_keyboard_temp.setVisibility(View.GONE);
                btn_keyboard_pay.setVisibility(View.GONE);
                btn_keyboard_creditcard.setVisibility(View.GONE);
            }else {

                if ((program == 0) || (program == 3)) {
                    if (washisdry != 1) {
                        if (dozator_true == 1) {
                            if((countwash>=3)&&(washmode<=9)){layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);}
                            else if((countwash<=2)&&(washmode<=9)){layout_bg.setBackgroundResource(R.drawable.dozator_prepair);}
                            else {  layout_bg.setBackgroundResource(R.drawable.dozator_prepair);}
                            btn_keyboard_choose_prog.setVisibility(View.GONE);
                            btn_keyboard_choose_prog.setEnabled(false);
                            btn_keyboard_sms.setVisibility(View.GONE);
                            btn_keyboard_temp.setVisibility(View.GONE);
                            btn_keyboard_pay.setVisibility(View.GONE);
                            btn_keyboard_creditcard.setVisibility(View.GONE);

                            txt_footer_prepair.setText(" ");
                            btn_numbers_select.setImageResource(R.drawable.ic_insert_chart_white_48dp);
                        } else {
                            if (random_background == 0) {
                                random_background = 1;
                                prepair_background1.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.best_washing_machines);
                            } else if (random_background == 1) {
                                random_background = 2;
                                prepair_background2.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);
                            } else if (random_background == 2) {
                                random_background = 3;
                                prepair_background3.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.prepair_wash1);
                            } else if (random_background == 3) {
                                random_background = 4;
                                prepair_background2.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.samsung19);
                            } else if (random_background == 4) {
                                random_background = 5;
                                prepair_background3.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.bg_watermelow);
                            } else if (random_background == 5) {
                                random_background = 6;
                                prepair_background2.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.bg_water);
                            } else if (random_background == 6) {
                                random_background = 0;
                                prepair_background3.setVisibility(View.VISIBLE);
                                layout_bg.setBackgroundResource(R.drawable.samsung17);
                            }

                        }
                    }
                } else if ((program == 1) || (program == 2)) {
                    if (dozator_true == 1) {
                        if((countwash>=3)&&(washmode<=9)){layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);}
                        else if((countwash<=2)&&(washmode<=9)){layout_bg.setBackgroundResource(R.drawable.dozator_prepair);}
                        else {  layout_bg.setBackgroundResource(R.drawable.dozator_prepair);}
                        btn_keyboard_choose_prog.setVisibility(View.GONE);
                        btn_keyboard_choose_prog.setEnabled(false);
                        btn_keyboard_sms.setVisibility(View.GONE);
                        btn_keyboard_temp.setVisibility(View.GONE);
                        btn_keyboard_pay.setVisibility(View.GONE);
                        btn_keyboard_creditcard.setVisibility(View.GONE);

                        txt_footer_prepair.setText(" ");
                        btn_numbers_select.setImageResource(R.drawable.ic_insert_chart_white_48dp);
                    }else {

                        txt_footer_prepair.setVisibility(View.INVISIBLE);// Подпись внизу скрываем
                        btn_keyboard_choose_prog.setEnabled(true);       // клавиатура изменяется
                        if (washmode == 0) {
                            wash_md = 0;
                            if ((washisdry == 1) && (washisdry_program == 0)) {

                            } else {
                                choose_mode();
                            }
                        } else if (washmode == 1) {
                            wash_md = 1;
                            txt_footer_prepair.setVisibility(View.VISIBLE);
                            txt_footer_prepair.setText("30мин");
                            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
                            txt_footer_prepair.startAnimation(txt_Animation);
                            if (washisdry != 1) {
                                prepair_background_mode1.setVisibility(View.VISIBLE);
                                if (program == 2) {
                                    mode1temp = mode1temp30;
                                    btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp30_2);
                                }
                                if (random_background == 0) {
                                    random_background = 1;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);
                                } else if (random_background == 1) {
                                    random_background = 2;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);
                                    //layout_bg.setBackgroundResource(R.drawable.bg_lemon);
                                } else if (random_background == 2) {
                                    random_background = 3;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);
                                    //layout_bg.setBackgroundResource(R.drawable.landscapedesktop);
                                } else if (random_background == 3) {
                                    random_background = 0;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_everyday);
                                    //layout_bg.setBackgroundResource(R.drawable.samsung5);
                                }

                            }
                            txt_layout_prepair_wash1.setText(name1mode);
                            mdtrf = mode1tarif;
                        } else if (washmode == 2) {
                            wash_md = 2;
                            txt_footer_prepair.setVisibility(View.VISIBLE);
                            txt_footer_prepair.setText("120мин");
                            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
                            txt_footer_prepair.startAnimation(txt_Animation);
                            if (washisdry != 1) {
                                prepair_background_mode2.setVisibility(View.VISIBLE);
                                if (program == 2) {
                                    mode2temp = mode2temp60;
                                    btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp60_2);
                                }
                                if (random_background == 0) {
                                    random_background = 1;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_wash1);
                                } else if (random_background == 1) {
                                    random_background = 2;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_wash1);
                             //       layout_bg.setBackgroundResource(R.drawable.settings_menu_background2);
                                } else if (random_background == 2) {
                                    random_background = 3;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_wash1);
                             //       layout_bg.setBackgroundResource(R.drawable.water_blue);
                                } else if (random_background == 3) {
                                    random_background = 0;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_wash1);
                             //       layout_bg.setBackgroundResource(R.drawable.prepair_background_girlpersil);
                                }
                            }
                            txt_layout_prepair_wash1.setText(name2mode);
                            mdtrf = mode2tarif;
                        } else if (washmode == 3) {
                            wash_md = 3;
                            txt_footer_prepair.setVisibility(View.VISIBLE);
                            txt_footer_prepair.setText("180мин");
                            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
                            txt_footer_prepair.startAnimation(txt_Animation);
                            if (washisdry != 1) {
                                prepair_background_mode3.setVisibility(View.VISIBLE);
                                if (program == 2) {
                                    mode3temp = mode3temp60;
                                    btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp60_2);
                                }
                                if (random_background == 0) {
                                    random_background = 1;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_cotton);
                                } else if (random_background == 1) {
                                    random_background = 2;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_cotton);
                                   // layout_bg.setBackgroundResource(R.drawable.samsung17);
                                } else if (random_background == 2) {
                                    random_background = 3;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_cotton);
                                    //layout_bg.setBackgroundResource(R.drawable.settings_bg5);
                                } else if (random_background == 3) {
                                    random_background = 0;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_cotton);
                                   // layout_bg.setBackgroundResource(R.drawable.background_green_forest);
                                }
                            }
                            txt_layout_prepair_wash1.setText(name3mode);
                            mdtrf = mode3tarif;
                        } else if (washmode == 4) {
                            wash_md = 4;
                            txt_footer_prepair.setVisibility(View.VISIBLE);
                            txt_footer_prepair.setText("90мин");
                            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
                            txt_footer_prepair.startAnimation(txt_Animation);
                            if (washisdry != 1) {
                                prepair_background_mode4.setVisibility(View.VISIBLE);
                                if (program == 2) {
                                    mode4temp = mode4temp40;
                                    btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp40_2);
                                }
                                if (random_background == 0) {
                                    random_background = 1;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_hand);
                                } else if (random_background == 1) {
                                    random_background = 2;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_hand);
                                    //layout_bg.setBackgroundResource(R.drawable.samsung11);
                                } else if (random_background == 2) {
                                    random_background = 3;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_hand);
                                    //layout_bg.setBackgroundResource(R.drawable.bg_water);
                                } else if (random_background == 3) {
                                    random_background = 0;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_hand);
                                    //layout_bg.setBackgroundResource(R.drawable.background_blue);
                                }
                            }
                            txt_layout_prepair_wash1.setText(name4mode);
                            mdtrf = mode4tarif;
                        } else if (washmode == 5) {
                            wash_md = 5;
                            txt_footer_prepair.setVisibility(View.VISIBLE);
                            txt_footer_prepair.setText("120мин");
                            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
                            txt_footer_prepair.startAnimation(txt_Animation);
                            if (washisdry != 1) {
                                prepair_background_mode5.setVisibility(View.VISIBLE);
                                if (program == 2) {
                                    mode5temp = mode5temp40;
                                    btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp40_2);
                                }
                                if (random_background == 0) {
                                    random_background = 1;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_sintetics);
                                } else if (random_background == 1) {
                                    random_background = 2;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_sintetics);
                                    //layout_bg.setBackgroundResource(R.drawable.samsung11);
                                } else if (random_background == 2) {
                                    random_background = 3;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_sintetics);
                                    //layout_bg.setBackgroundResource(R.drawable.bg_ferrary);
                                } else if (random_background == 3) {
                                    random_background = 0;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_sintetics);
                                    //layout_bg.setBackgroundResource(R.drawable.background_blue);
                                }
                            }
                            txt_layout_prepair_wash1.setText(name5mode);
                            mdtrf = mode5tarif;
                        } else if (washmode == 6) {
                            wash_md = 6;
                            txt_footer_prepair.setVisibility(View.VISIBLE);
                            txt_footer_prepair.setText("17мин");
                            final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
                            txt_footer_prepair.startAnimation(txt_Animation);

                            if (washisdry != 1) {
                                prepair_background_mode6.setVisibility(View.VISIBLE);
                                if (program == 2) {
                                    mode1temp = 0;
                                    btn_keyboard_temp.setImageResource(R.drawable.smart_keyboard_temp_2);
                                }
                                if (random_background == 0) {
                                    random_background = 1;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_baraban);
                                } else if (random_background == 1) {
                                    random_background = 2;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_baraban);
                                    //layout_bg.setBackgroundResource(R.drawable.samsung15);
                                } else if (random_background == 2) {
                                    random_background = 3;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_baraban);
                                    //layout_bg.setBackgroundResource(R.drawable.samsung13);
                                } else if (random_background == 3) {
                                    random_background = 0;
                                    layout_bg.setBackgroundResource(R.drawable.prepair_background_baraban);
                                    //layout_bg.setBackgroundResource(R.drawable.samsung12);
                                }
                            }
                            txt_layout_prepair_wash1.setText(name6mode);
                            mdtrf = mode6tarif;
                        }
                    }
                }//end program
            }
            float summ = 0;
            //   5  >=  0(0:00)   5  <   9(9:00)
            if((hour<hour1)){timetarif=hour1tarif;}
            //   14 >=  9(9:00)  14  <  17(17:00)
            if((hour>=hour1)&&(hour<hour2)){timetarif=hour2tarif;}
            //   23 >= 17(17:00) 23  <  24 (23:59)
            if((hour>=hour2)){timetarif=hour3tarif;}

            if (wash_number_int == 1) {summ = wash1tarif + mdtrf + timetarif;
            } else if (wash_number_int == 2) {summ = wash2tarif + mdtrf + timetarif;
            } else if (wash_number_int == 3) {summ = wash3tarif + mdtrf + timetarif;
            } else if (wash_number_int == 4) {summ = wash4tarif + mdtrf+timetarif;
            } else if (wash_number_int == 5) {summ = wash5tarif + mdtrf+timetarif;
            } else if (wash_number_int == 6) {summ = wash6tarif + mdtrf+timetarif;
            } else if (wash_number_int == 7) {summ = wash7tarif + mdtrf+timetarif;
            } else if (wash_number_int == 8) {summ = wash8tarif + mdtrf+timetarif;
            } else if (wash_number_int == 9) {summ = wash9tarif + mdtrf+timetarif;
            } else if (wash_number_int == 10) {summ = wash10tarif + mdtrf+timetarif;
            } else if (wash_number_int == 11) {summ = wash11tarif + mdtrf+timetarif;
            } else if (wash_number_int == 12) {summ = wash12tarif + mdtrf+timetarif;
            }
            if(valuta==3){
            money_tarif.setText("" + summ+"0");}else{money_tarif.setText("" + summ);}
            start.setEnabled(true);
            start.setVisibility(View.VISIBLE);
            if (washisdry == 1) {
                prepair_background_dry1.setVisibility(View.VISIBLE);
                layout_bg.setBackgroundResource(R.drawable.prepair_background_dry2);
                txt_layout_prepair_wash1.setText("Сушка");
                if (washisdry_program == 0) {
                    btn_keyboard_choose_prog.setEnabled(false);// клавиатура изменяется
                    btn_keyboard_temp.setEnabled(false);
                    txt_footer_prepair.setVisibility(View.VISIBLE);
                }
            }
        }
    }


    /**
     * == START ENGINE ==
     *
     * @param sms_wash (0-off, 1-wash, .., 12wash)
     * @param sms_mode (0-start, 1-mode, .., 12-mode)
     */

    private void start_engine(int sms_wash, int sms_mode) {
        int sms_director_answer = 0;
        int sms_manager_answer = 0;
        if ((sms_wash == 0) && (sms_mode == 0)) {// по нулям входим через прогу, а с другими параметрами - если смс
            sms_director_answer = 1;
            sms_manager_answer = 1;
            start.setEnabled(false);
            start.setVisibility(View.INVISIBLE);// убедимся, что второй раз мы сюда не войдем, блокируем кнопку

// записываем счетчики запусков -------------------------- начало ----------------------------------
//                             ------------ MODE 0 -------------
            if ((wash_number_int == 1) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W1, stat_w1++);}
            if ((wash_number_int == 2) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W2, stat_w2++);}
            if ((wash_number_int == 3) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W3, stat_w3++);}
            if ((wash_number_int == 4) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W4, stat_w4++);}
            if ((wash_number_int == 5) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W5, stat_w5++);}
            if ((wash_number_int == 6) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W6, stat_w6++);}
            if ((wash_number_int == 7) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W7, stat_w7++);}
            if ((wash_number_int == 8) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W8, stat_w8++);}
            if ((wash_number_int == 9) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W9, stat_w9++);}
            if ((wash_number_int == 10) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W10, stat_w10++);}
            if ((wash_number_int == 11) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W11, stat_w11++);}
            if ((wash_number_int == 12) && (set_mode == 0)) {save_counter_starting(PREF_STAT_W12, stat_w12++);}
//                             ------------ MODE 1 -------------
            if ((wash_number_int == 1) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W1, statmod1_w1++);}
            if ((wash_number_int == 2) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W2, statmod1_w2++);}
            if ((wash_number_int == 3) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W3, statmod1_w3++);}
            if ((wash_number_int == 4) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W4, statmod1_w4++);}
            if ((wash_number_int == 5) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W5, statmod1_w5++);}
            if ((wash_number_int == 6) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W6, statmod1_w6++);}
            if ((wash_number_int == 7) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W7, statmod1_w7++);}
            if ((wash_number_int == 8) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W8, statmod1_w8++);}
            if ((wash_number_int == 9) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W9, statmod1_w9++);}
            if ((wash_number_int == 10) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W10, statmod1_w10++);}
            if ((wash_number_int == 11) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W11, statmod1_w11++);}
            if ((wash_number_int == 12) && (set_mode == 1)) {save_counter_starting(PREF_STATMOD1_W12, statmod1_w12++);}
//                             ------------ MODE 2 -------------
            if ((wash_number_int == 1) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W1, statmod2_w1++);}
            if ((wash_number_int == 2) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W2, statmod2_w2++);}
            if ((wash_number_int == 3) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W3, statmod2_w3++);}
            if ((wash_number_int == 4) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W4, statmod2_w4++);}
            if ((wash_number_int == 5) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W5, statmod2_w5++);}
            if ((wash_number_int == 6) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W6, statmod2_w6++);}
            if ((wash_number_int == 7) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W7, statmod2_w7++);}
            if ((wash_number_int == 8) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W8, statmod2_w8++);}
            if ((wash_number_int == 9) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W9, statmod2_w9++);}
            if ((wash_number_int == 10) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W10, statmod2_w10++);}
            if ((wash_number_int == 11) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W11, statmod2_w11++);}
            if ((wash_number_int == 12) && (set_mode == 2)) {save_counter_starting(PREF_STATMOD2_W12, statmod2_w12++);}
//                             ------------ MODE 3 -------------
            if ((wash_number_int == 1) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W1, statmod3_w1++);}
            if ((wash_number_int == 2) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W2, statmod3_w2++);}
            if ((wash_number_int == 3) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W3, statmod3_w3++);}
            if ((wash_number_int == 4) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W4, statmod3_w4++);}
            if ((wash_number_int == 5) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W5, statmod3_w5++);}
            if ((wash_number_int == 6) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W6, statmod3_w6++);}
            if ((wash_number_int == 7) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W7, statmod3_w7++);}
            if ((wash_number_int == 8) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W8, statmod3_w8++);}
            if ((wash_number_int == 9) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W9, statmod3_w9++);}
            if ((wash_number_int == 10) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W10, statmod3_w10++);}
            if ((wash_number_int == 11) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W11, statmod3_w11++);}
            if ((wash_number_int == 12) && (set_mode == 3)) {save_counter_starting(PREF_STATMOD3_W12, statmod3_w12++);}
//                             ------------ MODE 4 -------------
            if ((wash_number_int == 1) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W1, statmod4_w1++);}
            if ((wash_number_int == 2) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W2, statmod4_w2++);}
            if ((wash_number_int == 3) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W3, statmod4_w3++);}
            if ((wash_number_int == 4) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W4, statmod4_w4++);}
            if ((wash_number_int == 5) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W5, statmod4_w5++);}
            if ((wash_number_int == 6) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W6, statmod4_w6++);}
            if ((wash_number_int == 7) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W7, statmod4_w7++);}
            if ((wash_number_int == 8) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W8, statmod4_w8++);}
            if ((wash_number_int == 9) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W9, statmod4_w9++);}
            if ((wash_number_int == 10) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W10, statmod4_w10++);}
            if ((wash_number_int == 11) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W11, statmod4_w11++);}
            if ((wash_number_int == 12) && (set_mode == 4)) {save_counter_starting(PREF_STATMOD4_W12, statmod4_w12++);}
//                             ------------ MODE 5 -------------
            if ((wash_number_int == 1) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W1, statmod5_w1++);}
            if ((wash_number_int == 2) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W2, statmod5_w2++);}
            if ((wash_number_int == 3) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W3, statmod5_w3++);}
            if ((wash_number_int == 4) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W4, statmod5_w4++);}
            if ((wash_number_int == 5) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W5, statmod5_w5++);}
            if ((wash_number_int == 6) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W6, statmod5_w6++);}
            if ((wash_number_int == 7) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W7, statmod5_w7++);}
            if ((wash_number_int == 8) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W8, statmod5_w8++);}
            if ((wash_number_int == 9) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W9, statmod5_w9++);}
            if ((wash_number_int == 10) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W10, statmod5_w10++);}
            if ((wash_number_int == 11) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W11, statmod5_w11++);}
            if ((wash_number_int == 12) && (set_mode == 5)) {save_counter_starting(PREF_STATMOD5_W12, statmod5_w12++);}
//                             ------------ MODE 6 -------------
            if ((wash_number_int == 1) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W1, statmod6_w1++);}
            if ((wash_number_int == 2) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W2, statmod6_w2++);}
            if ((wash_number_int == 3) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W3, statmod6_w3++);}
            if ((wash_number_int == 4) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W4, statmod6_w4++);}
            if ((wash_number_int == 5) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W5, statmod6_w5++);}
            if ((wash_number_int == 6) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W6, statmod6_w6++);}
            if ((wash_number_int == 7) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W7, statmod6_w7++);}
            if ((wash_number_int == 8) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W8, statmod6_w8++);}
            if ((wash_number_int == 9) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W9, statmod6_w9++);}
            if ((wash_number_int == 10) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W10, statmod6_w10++);}
            if ((wash_number_int == 11) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W11, statmod6_w11++);}
            if ((wash_number_int == 12) && (set_mode == 6)) {save_counter_starting(PREF_STATMOD6_W12, statmod6_w12++);}

            if ((wash_number_int == 1) && (set_mode == 8)) {save_counter_starting(PREF_STAT_W1, stat_w1++);}
            if ((wash_number_int == 2) && (set_mode == 9)) {save_counter_starting(PREF_STAT_W2, stat_w2++);}
            if ((wash_number_int == 3) && (set_mode == 10)) {save_counter_starting(PREF_STAT_W3, stat_w3++);}
            if ((wash_number_int == 4) && (set_mode == 11)) {save_counter_starting(PREF_STAT_W4, stat_w4++);}
// записываем счетчики запусков -------------------------- конец -----------------------------------
            if(dozator_true==1){layout_action_bar.setVisibility(View.GONE);layout_smart_keyboard.setVisibility(View.GONE);layout_prepair_start.setVisibility(View.INVISIBLE);
                      if(((countwash>=3)&&(set_mode==8)) ||((countwash>=3)&&(set_mode==9))) {layout_bg.setBackgroundResource(R.drawable.programs_start_iron2);
                }else if(((countwash<=2)&&(set_mode==8)) ||((countwash<=2)&&(set_mode==9))) {layout_bg.setBackgroundResource(R.drawable.programs_start_dozator);
                }else if(((countwash>=3)&&(set_mode==10))||((countwash>=3)&&(set_mode==11))){layout_bg.setBackgroundResource(R.drawable.programs_start_dozator);}
            }else{
                //layout_bg.setBackgroundResource(R.drawable.programs_start_ok1);
                //layout_action_bar.setVisibility(View.GONE);
                layout_smart_keyboard.setVisibility(View.GONE);}
           // ok1.setVisibility(View.VISIBLE);
            animation_start();
            relay_manager(wash_number_int, set_mode);// запускаем нужные релюшки
            sendMessage(START_ANIM); // запускаем анимацию
            //  startFrameAnimation();

            spisanie_deneg();// теперь спишем деньги
// -------------------------------------------------------------------------------------------------
// ------------------------------- СМС УПРАВЛЕНИЕ МАШИНАМИ -----------------------------------------
// когда поступает команда с телефона на экран ничего не выводится
        } else { // пришла смс, то записать счетчик смс запуска
            if (sms_wash == 1) {save_counter_starting(PREF_STATSMS_W1, statsms_w1++);}
            if (sms_wash == 2) {save_counter_starting(PREF_STATSMS_W2, statsms_w2++);}
            if (sms_wash == 3) {save_counter_starting(PREF_STATSMS_W3, statsms_w3++);}
            if (sms_wash == 4) {save_counter_starting(PREF_STATSMS_W4, statsms_w4++);}
            if (sms_wash == 5) {save_counter_starting(PREF_STATSMS_W5, statsms_w5++);}
            if (sms_wash == 6) {save_counter_starting(PREF_STATSMS_W6, statsms_w6++);}
            if (sms_wash == 7) {save_counter_starting(PREF_STATSMS_W7, statsms_w7++);}
            if (sms_wash == 8) {save_counter_starting(PREF_STATSMS_W8, statsms_w8++);}
            if (sms_wash == 9) {save_counter_starting(PREF_STATSMS_W9, statsms_w9++);}
            if (sms_wash == 10) {save_counter_starting(PREF_STATSMS_W10, statsms_w10++);}
            if (sms_wash == 11) {save_counter_starting(PREF_STATSMS_W11, statsms_w11++);}
            if (sms_wash == 12) {save_counter_starting(PREF_STATSMS_W12, statsms_w12++);}
// запускаем нужные релюшки с параметрами из смс
            relay_manager(sms_wash, sms_mode);
// отвечаем смс-кой о выполнении комманды
            if (sms_mode == 0) {mode_name = "";}else
            if (sms_mode == 1) {mode_name = "Режим: " + name1mode;}else
            if (sms_mode == 2) {mode_name = "Режим: " + name2mode;}else
            if (sms_mode == 3) {mode_name = "Режим: " + name3mode;}else
            if (sms_mode == 4) {mode_name = "Режим: " + name4mode;}else
            if (sms_mode == 5) {mode_name = "Режим: " + name5mode;}else
            if (sms_mode == 6) {mode_name = "Режим: " + name6mode;}
            SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
            sms.sendTextMessage(sms_number, null, "Машинка " + sms_wash + " запущена " + mode_name + "", null, null);
        }

// отчет СМС при запуске машины
        if (sms_mode == 0) {mode_name = "";}else
        if (sms_mode == 1) {mode_name = "Режим: " + name1mode;}else
        if (sms_mode == 2) {mode_name = "Режим: " + name2mode;}else
        if (sms_mode == 3) {mode_name = "Режим: " + name3mode;}else
        if (sms_mode == 4) {mode_name = "Режим: " + name4mode;}else
        if (sms_mode == 5) {mode_name = "Режим: " + name5mode;}else
        if (sms_mode == 6) {mode_name = "Режим: " + name6mode;}

        if ((sms_director_notice == 1) && (sms_director_notice_startOK == 1) && (sms_director_answer == 1)) {
            if (sms_director_number.contentEquals("+7")) {} else {SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                //   Toast.makeText(getActivity(), "Send SMS director: Washer " + wash_number_int + " start", Toast.LENGTH_SHORT).show();
                sms.sendTextMessage(sms_director_number, null, "Машинка " + wash_number_int + " запущена " + mode_name + "", null, null);}}
        if ((sms_manager_notice == 1) && (sms_manager_notice_startOK == 1) && (sms_manager_answer == 1)) {
            if (sms_manager_number.contentEquals("+7")) {} else {SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                //  Toast.makeText(getActivity(), "Send SMS manager: Washer " + wash_number_int + " start", Toast.LENGTH_SHORT).show();
                sms.sendTextMessage(sms_manager_number, null, "Машинка " + wash_number_int + " запущена " + mode_name + "", null, null);}}
    }


//-------------  end of  start_engine --------------------------------------------------------------
//**************************************************************************************************

    /**
     * Записать в память данные для статистики
     *
     * @param APP_PREF             имя ячейки памяти
     * @param washstatcounter_mode переменная для записи с номером машины и режима
     */
    private void save_counter_starting(String APP_PREF, int washstatcounter_mode) {
         /* washstatcounter_mode++; */
        mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREF, washstatcounter_mode); editor.apply(); }

    /**
     * Настройка включения режимов управление релюшками
     *
     * @param wash_num     номер машины
     * @param washer_modes номер режима
     */
    private void relay_manager(int wash_num, int washer_modes) {

        demo100_count++; // лицензия демо режим
        Log.d(TAG, "Счетчик до блокировки " + demo100_count);
        if(wash_num==1){pwr=""+WASH_1_POWER;md=""+WASH_1_MODE;tmp=""+WASH_1_TEMP;srt=""+WASH_1_START;}
        if(wash_num==2){pwr=""+WASH_2_POWER;md=""+WASH_2_MODE;tmp=""+WASH_2_TEMP;srt=""+WASH_2_START;}
        if(wash_num==3){pwr=""+WASH_3_POWER;md=""+WASH_3_MODE;tmp=""+WASH_3_TEMP;srt=""+WASH_3_START;}
        if(wash_num==4){pwr=""+WASH_4_POWER;md=""+WASH_4_MODE;tmp=""+WASH_4_TEMP;srt=""+WASH_4_START;}
        if(wash_num==5){pwr=""+WASH_5_POWER;md=""+WASH_5_MODE;tmp=""+WASH_5_TEMP;srt=""+WASH_5_START;}
        if(wash_num==6){pwr=""+WASH_6_POWER;md=""+WASH_6_MODE;tmp=""+WASH_6_TEMP;srt=""+WASH_6_START;}
        if(wash_num==7){pwr=""+WASH_7_POWER;md=""+WASH_7_MODE;tmp=""+WASH_7_TEMP;srt=""+WASH_7_START;}
        if(wash_num==8){pwr=""+WASH_8_POWER;md=""+WASH_8_MODE;tmp=""+WASH_8_TEMP;srt=""+WASH_8_START;}
        if(wash_num==9){pwr=""+WASH_9_POWER;md=""+WASH_9_MODE;tmp=""+WASH_9_TEMP;srt=""+WASH_9_START;}
      if(wash_num==10){pwr=""+WASH_10_POWER;md=""+WASH_10_MODE;tmp=""+WASH_10_TEMP;srt=""+WASH_10_START;}
      if(wash_num==11){pwr=""+WASH_11_POWER;md=""+WASH_11_MODE;tmp=""+WASH_11_TEMP;srt=""+WASH_11_START;}
      if(wash_num==12){pwr=""+WASH_12_POWER;md=""+WASH_12_MODE;tmp=""+WASH_12_TEMP;srt=""+WASH_12_START;}

        if(dozator_true==1){
            if((washer_modes==8)&&(countwash<=2)){
                sendMessage(WASH_3_POWER);for(int i=1;i<=dozator1push;i++){sendMessage(WASH_3_MODE);}
                sendMessage(WASH_3_START);}
            if((washer_modes==8)&&(countwash>=3)){sendMessage(pwr);for(int i=1;i<=dozator1push;i++){sendMessage(md);}sendMessage(srt);}

            if((washer_modes==9)&&(countwash<=2)){
                sendMessage(WASH_3_POWER);for(int i=1;i<=dozator2push;i++){sendMessage(WASH_3_MODE);}
                sendMessage(WASH_3_START);}
            if((washer_modes==9)&&(countwash>=3)){sendMessage(pwr);for(int i=1;i<=dozator2push;i++){sendMessage(md);}sendMessage(srt);}

            if(washer_modes==10){sendMessage(WASH_3_POWER);for(int i=1;i<=dozator3push;i++){sendMessage(WASH_3_MODE);}sendMessage(WASH_3_START);}

            if(washer_modes==11){sendMessage(WASH_3_POWER);for(int i=1;i<=dozator4push;i++){sendMessage(WASH_3_MODE);}sendMessage(WASH_3_START);}

        }else{
            if (washer_modes==0){sendMessage(srt);}
            if (washer_modes==1){sendMessage(pwr);for (int i = 1; i <= mode1push; i++) {sendMessage(md);}for(int t=1;t<=mode1temp;t++){sendMessage(tmp);}sendMessage(srt);}
            if (washer_modes==2){sendMessage(pwr);for (int i = 1; i <= mode2push; i++) {sendMessage(md);}for(int t=1;t<=mode2temp;t++){sendMessage(tmp);Log.d(TAG, "температура");}sendMessage(srt);}
            if (washer_modes==3){sendMessage(pwr);for (int i = 1; i <= mode3push; i++) {sendMessage(md);}for(int t=1;t<=mode3temp;t++){sendMessage(tmp);}sendMessage(srt);}
            if (washer_modes==4){sendMessage(pwr);for (int i = 1; i <= mode4push; i++) {sendMessage(md);}for(int t=1;t<=mode4temp;t++){sendMessage(tmp);}sendMessage(srt);}
            if (washer_modes==5){sendMessage(pwr);for (int i = 1; i <= mode5push; i++) {sendMessage(md);}for(int t=1;t<=mode5temp;t++){sendMessage(tmp);}sendMessage(srt);}
            if (washer_modes==6){sendMessage(pwr);for (int i = 1; i <= mode6push; i++) {sendMessage(md);}for(int t=1;t<=mode6temp;t++){sendMessage(tmp);}sendMessage(srt);}
        }}


    /**
     * Списываем деньги
     * берем текущий тариф не из переменной потому что
     * не надо проверять к какой машине он относится
     */
    private void spisanie_deneg() {
        mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
        float kup1 = Float.parseFloat(money_kup.getText().toString());
        float tar1 = Float.parseFloat(money_tarif.getText().toString());
        float kuptar = 0;
        kuptar = kup1-tar1;
        float prov = 0;
        if (kuptar >= prov) {
            if(valuta==3) {

                double kup11 = Double.parseDouble(money_kup.getText().toString());
                double tar11 = Double.parseDouble(money_tarif.getText().toString());
                String bla = "" + money_vneseno;
                double blabla = Double.parseDouble(bla);
                blabla -= tar11;
               String vnesee = "" + blabla + "0";
             //   String vnesee = "0";
                money_vneseno = Float.parseFloat(vnesee);
            }else{

           money_vneseno = kuptar;}
            if(valuta==3){
               // money_tarif.setText("" + summ+"0");
                money_kup.setText("" + money_vneseno+"0");

            }else{
            money_kup.setText("" + kuptar);}
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putFloat(PREF_MONEYVNESENO, money_vneseno);
            editor.apply();
            if(valuta==3){
                // money_tarif.setText("" + summ+"0");
                money_kup.setText("" + money_vneseno+"0");
            }
        } else {
            Log.i(TAG, "Error Kuptar! Backup by memory.");
            kuptar = 0;
            money_vneseno = kuptar;
            if(valuta==3){
                // money_tarif.setText("" + summ+"0");
                money_kup.setText("" + kuptar+"0");
            }else{
                money_kup.setText("" + kuptar);}
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putFloat(PREF_MONEYVNESENO, money_vneseno);
            editor.apply();
        }
    }

    /**
     * Открытие окна ввода номера для клиента, получить отчет о запуске
     */
    public void notice_wash() {
        if (wash_number_int == 1) {
            FragmentActivity activity = getActivity();
            if(null!=activity){Intent intent = new Intent(activity, PrefActivity_sms_notice1.class);
                startActivity(intent);}
        } else if (wash_number_int == 2) {
            FragmentActivity activity = getActivity();
            if(null!=activity){Intent intent = new Intent(activity, PrefActivity_sms_notice2.class);
                startActivity(intent);}
        } else if (wash_number_int == 3) {
            FragmentActivity activity = getActivity();
            if(null!=activity){Intent intent = new Intent(activity, PrefActivity_sms_notice3.class);
                startActivity(intent);}
        } else if (wash_number_int == 4) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice4.class);
            startActivity(intent);
        } else if (wash_number_int == 5) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice5.class);
            startActivity(intent);
        } else if (wash_number_int == 6) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice6.class);
            startActivity(intent);
        } else if (wash_number_int == 7) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice7.class);
            startActivity(intent);
        } else if (wash_number_int == 8) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice8.class);
            startActivity(intent);
        } else if (wash_number_int == 9) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice9.class);
            startActivity(intent);
        } else if (wash_number_int == 10) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice10.class);
            startActivity(intent);
        } else if (wash_number_int == 11) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice11.class);
            startActivity(intent);
        } else if (wash_number_int == 12) {
            Intent intent = new Intent(getActivity(), PrefActivity_sms_notice12.class);
            startActivity(intent);
        }
    }

    /**
     * Анимация запуска
     */
    private void animation_start() {
        // INVISIBLE LAYOUTS SCREEN
        layout_home.setVisibility(View.INVISIBLE);
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_choose_mode.setVisibility(View.INVISIBLE);
        // layout_bt_search.setVisibility(View.INVISIBLE);
        layout_settings.setVisibility(View.INVISIBLE);

        //  layout_bt_not.setVisibility(View.INVISIBLE);
        // открываем фрейм анимации, там все уже настроено
        layout_start_anim.setVisibility(View.VISIBLE);


        if(dozator_true==1){
            layout_action_bar.setVisibility(View.GONE);
            start_anim_washer.setVisibility(View.GONE);
            programs_start_clother.setVisibility(View.GONE);
            programs_start_clother.clearAnimation();
                 if(((countwash>2)&&(set_mode==8))||((countwash>2)&&(set_mode==9))){
                     layout_bg.setBackgroundResource(R.drawable.programs_start_iron2);
           }else if(((countwash>2)&&(set_mode==10))||((countwash>2)&&(set_mode==11))){
                     layout_bg.setBackgroundResource(R.drawable.programs_start_dozator);
           }else if(((countwash<3)&&(set_mode==8))||((countwash<3)&&(set_mode==9))){
                     layout_bg.setBackgroundResource(R.drawable.programs_start_dozator);}

        }else{
            programs_start_clother.setVisibility(View.VISIBLE);
            programs_start_clother.setImageResource(R.drawable.programs_start_washer_closer);
            start_anim_washer.setVisibility(View.VISIBLE);
            start_anim_washer.setImageResource(R.drawable.programs_start_washer);
            programs_start_clother.clearAnimation();
            layout_bg.setBackgroundResource(R.drawable.programs_start_background);}
        ok1.setVisibility(View.VISIBLE);
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_ok1_eng);}
        else{ if(dozator_true==1){
            if(((countwash>2)&&(set_mode==8))||((countwash>2)&&(set_mode==9))){
                ok1.setBackgroundResource(R.drawable.programs_start_iron2);
            }else if(((countwash>2)&&(set_mode==10))||((countwash>2)&&(set_mode==11))){
                ok1.setBackgroundResource(R.drawable.programs_start_dozator);
            }else if(((countwash<3)&&(set_mode==8))||((countwash<3)&&(set_mode==9))){
                ok1.setBackgroundResource(R.drawable.programs_start_dozator);}


        }else{ ok1.setBackgroundResource(R.drawable.programs_start_ok1);}}
       // status1.setVisibility(View.INVISIBLE);
       // ok2.setVisibility(View.INVISIBLE);
       // status2.setVisibility(View.INVISIBLE);
       // ok3.setVisibility(View.INVISIBLE);
       // status3.setVisibility(View.INVISIBLE);

    }

    /*
    *   Анимация
    */
    public void ok1() {
        ok1.setVisibility(View.VISIBLE);
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_ok1_eng);}else{
            if(dozator_true==1){
                start_anim_washer.setVisibility(View.GONE);
                programs_start_clother.setVisibility(View.GONE);
                ok1.setBackgroundResource(R.drawable.programs_start_step1);}else{

                start_anim_washer.setVisibility(View.VISIBLE);
                programs_start_clother.setVisibility(View.VISIBLE);
                ok1.setBackgroundResource(R.drawable.programs_start_ok1);}}
    }

    public void status1() {ok1.setVisibility(View.VISIBLE);
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_status1_eng);}else{
            if(dozator_true==1){
                ok1.setBackgroundResource(R.drawable.programs_start_step2);
            }else{
                ok1.setBackgroundResource(R.drawable.programs_start_status1);}}}

    public void ok2() {
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_ok2_eng);}else{
            if(dozator_true==1){
                ok1.setBackgroundResource(R.drawable.programs_start_step3);
            }else{
                ok1.setBackgroundResource(R.drawable.programs_start_ok2);}}}

    public void status2() {
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_status2_eng);}else{
            if(dozator_true==1){
                ok1.setBackgroundResource(R.drawable.programs_start_step4);
            }else{
                ok1.setBackgroundResource(R.drawable.programs_start_status2);}}}

    public void ok3() {
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_ok3_eng);}else{
            if(dozator_true==1){
                ok1.setBackgroundResource(R.drawable.programs_start_step5);
            }else{
                ok1.setBackgroundResource(R.drawable.programs_start_ok3);}}}

    public void status3() {
        if(eng==1){ok1.setBackgroundResource(R.drawable.programs_start_status3_eng);}else{
            if(dozator_true==1){
                ok1.setBackgroundResource(R.drawable.programs_start_step6);
            }else{
                ok1.setBackgroundResource(R.drawable.programs_start_status3);}}}

    public void animation_start_clother() {
        if(dozator_true==1){}else{
            FragmentActivity activity = getActivity();
            final Animation animationRotateCenter = AnimationUtils.loadAnimation(activity, R.anim.rotate);
            programs_start_clother.setVisibility(View.VISIBLE);programs_start_clother.startAnimation(animationRotateCenter);}}

    /*
    *   новая функция анимации ПРОВЕРИТЬ

    private void startFrameAnimation() {
        BitmapDrawable ok11 = (BitmapDrawable) getResources().getDrawable(R.drawable.programs_start_ok1);
        BitmapDrawable status11 = (BitmapDrawable) getResources().getDrawable(R.drawable.programs_start_status1);
        BitmapDrawable ok22 = (BitmapDrawable) getResources().getDrawable(R.drawable.programs_start_ok2);
        BitmapDrawable status22 = (BitmapDrawable) getResources().getDrawable(R.drawable.programs_start_status2);
        BitmapDrawable ok33 = (BitmapDrawable) getResources().getDrawable(R.drawable.programs_start_ok3);
        BitmapDrawable status33 = (BitmapDrawable) getResources().getDrawable(R.drawable.programs_start_status3);
        mAnimationDrawable = new AnimationDrawable(); mAnimationDrawable.setOneShot(true);
        mAnimationDrawable.addFrame(ok11, DURATION); mAnimationDrawable.addFrame(status11, DURATION);
        mAnimationDrawable.addFrame(ok22, DURATION); mAnimationDrawable.addFrame(status22, DURATION);
        mAnimationDrawable.addFrame(ok33, DURATION); mAnimationDrawable.addFrame(status33, DURATION);
      //  reconnect_Device();
        ok1.setVisibility(View.VISIBLE);
        ok1.setBackground(mAnimationDrawable);

    }
*/
    /*
    *   Подготовка ШТОРКИ к переходу в раздел (очистка)
    */
    private void step_leftmenu(int menu, int leftmenut) {
        layout_settings_preview.setVisibility(View.INVISIBLE); // закрыть титульный лист
        if (leftmenut == 1){show_leftmenu();} else {hide_leftmenu();}
        if (menu == 1) {txt_opacity_menu1.setVisibility(View.VISIBLE);
            layout_settings_washer.setVisibility(View.VISIBLE); // раздел машинки
            layout_settings_wash_number.setVisibility(View.VISIBLE);
            StatisticWash1.setVisibility(View.INVISIBLE);
            StatisticWash2.setVisibility(View.INVISIBLE);
            StatisticWash3.setVisibility(View.INVISIBLE);
            StatisticWash4.setVisibility(View.INVISIBLE);
            StatisticWash5.setVisibility(View.INVISIBLE);
            StatisticWash6.setVisibility(View.INVISIBLE);
            StatisticWash7.setVisibility(View.INVISIBLE);
            StatisticWash8.setVisibility(View.INVISIBLE);
            StatisticWash9.setVisibility(View.INVISIBLE);
            StatisticWash10.setVisibility(View.INVISIBLE);
            StatisticWash11.setVisibility(View.INVISIBLE);
            StatisticWash12.setVisibility(View.INVISIBLE);
        } else {txt_opacity_menu1.setVisibility(View.INVISIBLE); // раздел машинки
            layout_settings_washer.setVisibility(View.INVISIBLE);}
        if (menu == 2){txt_opacity_menu2.setVisibility(View.VISIBLE);// раздел утюги
            layout_settings_iron.setVisibility(View.VISIBLE);
        } else {txt_opacity_menu2.setVisibility(View.INVISIBLE); // раздел утюги
            layout_settings_iron.setVisibility(View.INVISIBLE);}
        if (menu == 3) {txt_opacity_menu3.setVisibility(View.VISIBLE);// раздел смс
            layout_settings_sms.setVisibility(View.VISIBLE);} else {
            txt_opacity_menu3.setVisibility(View.INVISIBLE); // раздел смс
            layout_settings_sms.setVisibility(View.INVISIBLE);}
        if (menu == 4) {txt_opacity_menu4.setVisibility(View.VISIBLE);// раздел bluetooth
            layout_settings_bluetooth.setVisibility(View.VISIBLE);} else {
            txt_opacity_menu4.setVisibility(View.INVISIBLE); // раздел bluetooth
            layout_settings_bluetooth.setVisibility(View.INVISIBLE);}
        if (menu == 5) {txt_opacity_menu5.setVisibility(View.VISIBLE);// раздел тарифы
            layout_settings_tarif.setVisibility(View.VISIBLE);} else {
            txt_opacity_menu5.setVisibility(View.INVISIBLE);// раздел тарифы
            layout_settings_tarif.setVisibility(View.INVISIBLE);}
        if (menu == 6) {txt_opacity_menu6.setVisibility(View.VISIBLE);
            layout_settings_system.setVisibility(View.VISIBLE); // раздел система
            layout_log.setVisibility(View.INVISIBLE); // подразделы системы
        } else {txt_opacity_menu6.setVisibility(View.INVISIBLE);
            layout_settings_system.setVisibility(View.INVISIBLE); // раздел система
            layout_log.setVisibility(View.INVISIBLE);}
        if (menu == 7) {txt_opacity_menu7.setVisibility(View.VISIBLE);
            layout_settings_stat.setVisibility(View.VISIBLE); // раздел статистика
            layout_settings_wash_number.setVisibility(View.VISIBLE);
            StatisticWash1.setVisibility(View.VISIBLE);
            StatisticWash2.setVisibility(View.VISIBLE);
            StatisticWash3.setVisibility(View.VISIBLE);
            StatisticWash4.setVisibility(View.VISIBLE);
            StatisticWash5.setVisibility(View.VISIBLE);
            StatisticWash6.setVisibility(View.VISIBLE);
            StatisticWash7.setVisibility(View.VISIBLE);
            StatisticWash8.setVisibility(View.VISIBLE);
            StatisticWash9.setVisibility(View.VISIBLE);
            StatisticWash10.setVisibility(View.VISIBLE);
            StatisticWash11.setVisibility(View.VISIBLE);
            StatisticWash12.setVisibility(View.VISIBLE);} else {
            txt_opacity_menu7.setVisibility(View.INVISIBLE);// раздел статистика
            layout_settings_stat.setVisibility(View.INVISIBLE);}
        if (menu == 8) {txt_opacity_menu8.setVisibility(View.VISIBLE);} else {txt_opacity_menu8.setVisibility(View.INVISIBLE);}
        if ((menu != 1) && (menu != 7)) {layout_settings_wash_number.setVisibility(View.INVISIBLE);}
        //displayInfo();// проверяем батареку
        if (statusStr.contentEquals("1")) {img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_battary.setVisibility(View.GONE);
        } else {img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_electro.setVisibility(View.GONE);}
        // возвращаем фон настроек, если логами был изменен на прозрачный
        // layout_settings.setBackgroundResource(R.drawable.settings_menu_background);
    }

    /*
     *   Спрятать ШТОРКУ
     */
    private void hide_leftmenu() {
        // прячем шторку чтобы не мешалась
        FragmentActivity activity = getActivity();
        final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_hide);
        if (layout_settings_action_bar_leftmenu.getVisibility() == View.VISIBLE) {
            layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
            layout_settings_action_bar_leftmenu.setVisibility(View.GONE);
            final Animation animationRotateCenterPopapButton = AnimationUtils.loadAnimation(
                    activity, R.anim.popup_btn);
            img_settings_action_bar_icon_view_leftmenu.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_view_leftmenu.startAnimation(animationRotateCenterPopapButton);
        }
    }

    /*
       *   Показать ШТОРКУ
       */
    private void show_leftmenu() {
        FragmentActivity activity = getActivity();
        final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_show);
        if (layout_settings_action_bar_leftmenu.getVisibility() != View.VISIBLE) {
            layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // показываем
            layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
        }
    }

    private void battery_charge(){
        imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
        if (display_settings_activity == 1){  // режим админ.настроек
            Log.d(TAG, "зашли в battery_charge и определили режим администратора, иконку зарядки");
            img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_battary.setVisibility(View.GONE);
        } else {
            if(eng==1){layout_bg.setBackgroundResource(R.drawable.load_laundry_eng);}else{
                layout_bg.setBackgroundResource(R.drawable.load_laundry);}
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                if(app_start==1) {
                    Log.d(TAG, "зашли в battery_charge и определили app_start==1 и STATE_NONE и заставка non_laundry");
                    layout_gone();
                    layout_bg.setBackgroundResource(R.drawable.non_laundry);
                    //  Toast.makeText(getActivity(), "зарядка, STATE_NONE, делать реконнект?", Toast.LENGTH_SHORT).show();
                } else if(app_start!=1) {
                    layout_gone();
                    if(eng==1){layout_bg.setBackgroundResource(R.drawable.load_laundry_eng);}else{
                        layout_bg.setBackgroundResource(R.drawable.load_laundry);}
                    Log.d(TAG, "зашли в battery_charge и определили app_start!=1 и STATE_NONE, поэтому reconnect_Device()");
                    reconnect_Device();}
            } else if (mChatService.getState() == BluetoothChatService.STATE_CONNECTING) {
                layout_gone();
                if(eng==1){layout_bg.setBackgroundResource(R.drawable.load_laundry_eng);}else{
                    layout_bg.setBackgroundResource(R.drawable.load_laundry);}
                reconnect_Device();
                sendMessage(CHECK_1_BT);
                Log.d(TAG, "зашли в battery_charge и определили STATE_CONNECTING, поэтому sendMessage(CHECK_1_BT)");

                //  Toast.makeText(getActivity(), "зарядка, STATE_CONNECTED, ждем lost или ok!", Toast.LENGTH_SHORT).show();
            }
            else {Log.d(TAG, "зашли в battery_charge и определили STATE_CONNECTED, поэтому sendMessage(CHECK_1_BT)");
                charge_activity=1;
                sendMessage(CHECK_1_BT);
                img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
                img_settings_action_bar_icon_battary.setVisibility(View.GONE);
                //  Toast.makeText(getActivity(), "зарядка, STATE_CONNECTING..., не может быть!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        sms_otvety(1); // СМС отчет администратору о включении питания
    }

    private void battery_not_charge(){
        imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
        if (display_settings_activity == 1){  // режим админ.настроек
            Log.d(TAG, "зашли в battery_not_charge и определили режим администратора, иконку батарейки");
            img_settings_action_bar_icon_electro.setVisibility(View.GONE);
            img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
        } else {
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Log.d(TAG, "battery_not_charge  STATE_NONE  ");
                Log.d(TAG, "зашли в battery_not_charge и определили STATE_NONE, заставку подключите питание");
                layout_gone();
                if(eng==1){layout_bg.setBackgroundResource(R.drawable.error_laundry_eng);}else{
                    layout_bg.setBackgroundResource(R.drawable.error_laundry);}
                //  Toast.makeText(getActivity(), "батарея, STATE_NONE.", Toast.LENGTH_SHORT).show();
            } else if (mChatService.getState() == BluetoothChatService.STATE_CONNECTING) {
                Log.d(TAG, "зашли в battery_not_charge и определили STATE_CONNECTING, заставку подключите питание");
                layout_gone();
                reconnect_Device();
                if(eng==1){layout_bg.setBackgroundResource(R.drawable.error_laundry_eng);}else{
                    layout_bg.setBackgroundResource(R.drawable.error_laundry);}
                //  Toast.makeText(getActivity(), "батарея, STATE_CONNECTED.", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "зашли в battery_not_charge и просто заставку подключите питание");
                layout_gone();
                if(eng==1){layout_bg.setBackgroundResource(R.drawable.error_laundry_eng);}else{
                    layout_bg.setBackgroundResource(R.drawable.error_laundry);}
                //  Toast.makeText(getActivity(), "батарея, STATE_CONNECTING..., не может быть!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        sms_otvety(2); // СМС отчет администратору об отключении питания
    }


    private void sms_otvety(int val){
        switch(val){
            case 1:
                if ((sms_director_notice == 1) && (sms_director_notice_220v == 1)) {SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    if(sms_director_number.contentEquals("+7")){}else{ //  Toast.makeText(getActivity(), "Send SMS director: Is electric now!", Toast.LENGTH_SHORT).show();
                        sms.sendTextMessage(sms_director_number, null, "Терминал включен в сеть: ["+timeformat.format(new Date())+"] ", null, null);
                        Log.d(TAG, "зашли в sms_otvety и отправляем смс директору: "+sms_director_number+" !Терминал включен в сеть: [\"+timeformat.format(new Date())+\"] ");
                    }}
                if ((sms_manager_notice == 1) && (sms_director_notice_220v == 1)) {SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    if(sms_manager_number.contentEquals("+7")){}else{ // Toast.makeText(getActivity(), "Send SMS manager: Is electric now!", Toast.LENGTH_SHORT).show();
                        sms.sendTextMessage(sms_manager_number, null, "Терминал включен в сеть: ["+timeformat.format(new Date())+"] ", null, null);
                        Log.d(TAG, "зашли в sms_otvety и отправляем смс менеджеру: "+sms_manager_number+" !Терминал включен в сеть: [\"+timeformat.format(new Date())+\"] ");
                    }}
                break;
            case 2:
                if ((sms_director_notice == 1) && (sms_director_notice_220v == 1)) {SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    if(sms_director_number.contentEquals("+7")){}else{ //Toast.makeText(getActivity(), "Send SMS director: Not electric now!", Toast.LENGTH_SHORT).show();
                        sms.sendTextMessage(sms_director_number, null, "Терминал перешел в спящий режим, питание от батареи: ["+timeformat.format(new Date())+"] ", null, null);
                        Log.d(TAG, "зашли в sms_otvety и отправляем смс директору: "+sms_director_number+" !Терминал перешел в спящий режим, питание от батареи: [\"+timeformat.format(new Date())+\"] ");
                    }}
                if ((sms_manager_notice == 1) && (sms_director_notice_220v == 1)) {SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    if(sms_manager_number.contentEquals("+7")){}else{ // Toast.makeText(getActivity(), "Send SMS manager: Not electric now!", Toast.LENGTH_SHORT).show();
                        sms.sendTextMessage(sms_manager_number, null, "Терминал перешел в спящий режим, питание от батареи: ["+timeformat.format(new Date())+"] ", null, null);
                        Log.d(TAG, "зашли в sms_otvety и отправляем смс менеджеру: "+sms_manager_number+" !Терминал перешел в спящий режим, питание от батареи: [\"+timeformat.format(new Date())+\"] ");
                    }}
                break;
            case 3:
                key=2;
                demo100_count=1;demo_count=100;
                SharedPreferences.Editor editor = mSettings.edit();// Запоминаем данные
                editor.putInt(PREF_KEY, key);
                editor.putInt(PREF_DEMO100_COUNT, demo100_count);editor.apply();
                SmsManager sms = SmsManager.getDefault();sms.sendTextMessage("+79052884693",null,"Срок лицензии истекает. До блокировки осталось 20 стирок.",null,null);
                Log.d(TAG, "Лицензия продлена еще на 100 стирок, сейчас счетчик равен " + demo100_count);
                layout_gone();go_home();
                break;
        }
    }

    // ПРИМЕР ДИАЛОГА
  /*
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("упс").setCancelable(true);
    AlertDialog alert = builder.create();
    alert.show();
  */

    /**
     * Alert dialog in numbers wash screen client
     * go to crash down woter screen mode
     */  //      ______________________________
    private void dialog_check_money_tarif_error() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());hideSystemUI();
        alertDialog.setTitle("Машинка №" + wash_number.getText().toString());
        alertDialog.setMessage("ERROR25-74 Запуск не возможен, тариф не может быть меньше нуля.");
        alertDialog.setPositiveButton("Автозамена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                money_tarif_correct();hideSystemUI();
            }
        });
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();hideSystemUI();
            }
        });
        alertDialog.setCancelable(true);
        final AlertDialog dlg = alertDialog.create();
        dlg.show();hideSystemUI();
    }

    private void money_tarif_correct() {
        money_tarif.setText("0");
    }

    /**
     * Alert dialog in numbers wash screen client
     * go to crash down woter screen mode
     */  //      __________________________

    private void dialog_crash_downwatermode() {
        if (checkwater == 1) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());hideSystemUI();
            alertDialog.setTitle("Машинка №" + wash_number.getText().toString() + "Стирка " + washertimer + " мин");
            alertDialog.setMessage("Если машина выключена и вам не открыть дверцу, слейте воду.");
            alertDialog.setPositiveButton("Аварийный слив воды", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                           if ((wash_number_int == 1) && (wash1remont == 0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 2) && (wash2remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 3) && (wash3remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 4) && (wash4remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 5) && (wash5remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 6) && (wash6remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 7) && (wash7remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 8) && (wash8remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 9) && (wash9remont == 0)) {  prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 10) && (wash10remont == 0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 11) && (wash11remont == 0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int == 12) && (wash12remont == 0)) { prepair_to_start(0, 0, 1, 6);
                    }hideSystemUI();
                }
            });
            alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();hideSystemUI();
                }
            });
            alertDialog.setCancelable(true);
            final AlertDialog dlg = alertDialog.create();
            dlg.show();hideSystemUI();
        }
    }

    /**
     * Alert dialog in settings screen administrator
     * go to home screen client
     */ //       ________________________
    private void dialog_bluetooth_adapter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Системная настройка");
        alertDialog.setMessage("ВНИМАНИЕ! Использовать только в присутствии специалиста 'Лондри Плюс', данная операция может привести к неправильной работе приложения.");
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //  if (mBluetoothAdapter.isEnabled()) {
                //      mBluetoothAdapter.cancelDiscovery();
                //      mBluetoothAdapter.disable();
                // setupChat();
                //  }
                if (!mBluetoothAdapter.isEnabled()) {
                    btn_settings_bluetooth_adapter.setChecked(true);
                    mBluetoothAdapter.enable();
                    Log.d(TAG, "вкл блютус");
                } else {
                    btn_settings_bluetooth_adapter.setChecked(false);
                    mBluetoothAdapter.cancelDiscovery();
                    mBluetoothAdapter.disable();
                    Log.d(TAG, "отключили блютус");
                }
            }
        });
        alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(true);
        final AlertDialog dlg = alertDialog.create();
        dlg.show();hideSystemUI();
    }

    /**
     * Alert dialog check press button Start alert
     * enough money to star
     */    //    ___________
    private void start_error() {
        float kup1dial = Float.parseFloat(money_kup.getText().toString());
        float tar1dial = Float.parseFloat(money_tarif.getText().toString());
        float kuptardial = (kup1dial - tar1dial) * -1;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());hideSystemUI();
        if(eng==1){
            alertDialog.setTitle("Not enough money");
            alertDialog.setMessage("To start you need to make more:" + kuptardial + " " + valuta_statistics_txt.getText().toString() + "");
            // alertDialog.setIcon(R.drawable.icon_wash_tarif);
            alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // Toast.makeText(getActivity(), "Service is temporarily unavailable.", Toast.LENGTH_SHORT).show();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();hideSystemUI();
                }
            });
            alertDialog.setCancelable(false);
        }else {hideSystemUI();
            alertDialog.setTitle("Для запуска нужно внести еще: " + kuptardial + " " + valuta_statistics_txt.getText().toString() + "");
            alertDialog.setMessage("          Хотите воспользоваться банковской картой?");
            alertDialog.setIcon(R.drawable.icon_wash_tarif);
            alertDialog.setPositiveButton("Оплатить картой", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity(), "Сервис временно не доступен.", Toast.LENGTH_SHORT).show();hideSystemUI();
                }
            });
            alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel(); hideSystemUI();
                }
            });
            alertDialog.setCancelable(false);}
        final AlertDialog dlg = alertDialog.create();
        dlg.show();
        start.setEnabled(true);
        start.setVisibility(View.VISIBLE);hideSystemUI();
    }

    /**
     * Alert dialog out of servise
     * to All functions & buttons
     */       //     _____________________
    private void dialog_out_of_service() {
        //  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //  builder.setMessage("Сервис временно не доступен. И таймер теперь РАБОТАЕТ!!!").setCancelable(true);
        //  builder.setView(R.layout.sber_instruct_form_light);
        //  AlertDialog alert = builder.create();
        //  alert.show();
        new AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("Service")
                .setMessage("Подключите терминал к компьютеру и скопируйте отчет в корневой папке.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();hideSystemUI();
    }

    private void dialog_program_com() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Чтобы закрыть окно COM, нажмите кнопку 'CLOSE' в правом нижнем углу.").setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();hideSystemUI();
    }

    private void dialog_fullscreen_open() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Системная панель загружается..").setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();hideSystemUI();
    }

    private void dialog_settings_vs_animstart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы не можете сейчас перейти в настройки, повторите позже.").setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();hideSystemUI();
    }

    /**
     * Choose wash mode screen client
     */ //        ___________
    private void choose_mode() {  layout_gone();
        layout_bg.setBackgroundResource(R.drawable.choose_wash_prog);
        layout_choose_mode.setVisibility(View.VISIBLE);
        choose1mode.setVisibility(View.VISIBLE);
        choose2mode.setVisibility(View.VISIBLE);
        choose3mode.setVisibility(View.VISIBLE);
        choose4mode.setVisibility(View.VISIBLE);
        choose5mode.setVisibility(View.VISIBLE);
        choose6mode.setVisibility(View.VISIBLE);
        layout_action_bar.setVisibility(View.INVISIBLE);
        if(eng==1){img_action_bar_icon_wash.setVisibility(View.VISIBLE);
            img_action_bar_icon_wash.setImageResource(R.drawable.action_bar_icon_wash_eng);
        }else{img_action_bar_icon_wash.setVisibility(View.VISIBLE);
            img_action_bar_icon_wash.setImageResource(R.drawable.action_bar_icon_wash);}
        // img_action_bar_icon_wash.setVisibility(View.VISIBLE);
        wash_number.setVisibility(View.VISIBLE);
        if(eng==1){img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno_eng);
        }else{img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno);}
        money_kup.setVisibility(View.VISIBLE);
        img_action_bar_icon_electro.setVisibility(View.GONE);
        img_action_bar_icon_battary.setVisibility(View.GONE);
    }

    /**
     * Автоподстановка данных по номеру текущей машинки
     */
    private void auto_prepair_to_start() {
               if (wash_number_int == 1) {prepair_to_start(wash1remont, wash1isdry, set_program, set_mode);
        } else if (wash_number_int == 2) {prepair_to_start(wash2remont, wash2isdry, set_program, set_mode);
        } else if (wash_number_int == 3) {prepair_to_start(wash3remont, wash3isdry, set_program, set_mode);
        } else if (wash_number_int == 4) {prepair_to_start(wash4remont, wash4isdry, set_program, set_mode);
        } else if (wash_number_int == 5) {prepair_to_start(wash5remont, wash5isdry, set_program, set_mode);
        } else if (wash_number_int == 6) {prepair_to_start(wash6remont, wash6isdry, set_program, set_mode);
        } else if (wash_number_int == 7) {prepair_to_start(wash7remont, wash7isdry, set_program, set_mode);
        } else if (wash_number_int == 8) {prepair_to_start(wash8remont, wash8isdry, set_program, set_mode);
        } else if (wash_number_int == 9) {prepair_to_start(wash9remont, wash9isdry, set_program, set_mode);
        } else if (wash_number_int == 10) {prepair_to_start(wash10remont, wash10isdry, set_program, set_mode);
        } else if (wash_number_int == 11) {prepair_to_start(wash11remont, wash11isdry, set_program, set_mode);
        } else if (wash_number_int == 12) {prepair_to_start(wash12remont, wash12isdry, set_program, set_mode);
        }
    }

    /**
     * Choose wash number screen client
     *
     * @param countwash int (how much your washes in laundry)
     */ //       ________________
    private void numbers_selected(int countwash) {
        final Animation animationRotateCenter = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
// VISIBLE LAYOUTS SCREEN
        mButtonCounter = 0;
        imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
        layout_number_selected.setVisibility(View.VISIBLE);
        if(dozator_true==1){
            layout_bg.setBackgroundResource(R.drawable.numbers_selected_background);
        }else{
            if(numbers_selected_bg==1){numbers_selected_bg=2;
                layout_bg.setBackgroundResource(R.drawable.bg_wood);}
            else if(numbers_selected_bg==2){numbers_selected_bg=3;
                layout_bg.setBackgroundResource(R.drawable.numbers_selected_background);}
            else if(numbers_selected_bg==3){numbers_selected_bg=4;
                layout_bg.setBackgroundResource(R.drawable.bg_orange);}
            else if(numbers_selected_bg==4){numbers_selected_bg=5;
                layout_bg.setBackgroundResource(R.drawable.numbers_selected_background);}
            else if(numbers_selected_bg==5){numbers_selected_bg=6;
                layout_bg.setBackgroundResource(R.drawable.bg_orange);
                //layout_bg.setBackgroundResource(R.drawable.bg_kirpich);
            }
            else if(numbers_selected_bg==6){numbers_selected_bg=7;
                layout_bg.setBackgroundResource(R.drawable.bg_european);}
            else if(numbers_selected_bg==7){numbers_selected_bg=8;
                layout_bg.setBackgroundResource(R.drawable.numbers_selected_background);
               // layout_bg.setBackgroundResource(R.drawable.bg_laundry3);
            }
            else if(numbers_selected_bg==8){numbers_selected_bg=1;
                layout_bg.setBackgroundResource(R.drawable.bg_orange);
                //layout_bg.setBackgroundResource(R.drawable.background_sunflower);
            }}
        txt_number_selected_title.setVisibility(View.VISIBLE);
        if(eng==1){txt_number_selected_title.setText("Select № of the machine"); }else {
            if(dozator_true==1){ txt_number_selected_title.setText("Выберите услугу");}else{
                txt_number_selected_title.setText("Выберите № машины");}}
        if (countwash > 6) {
            img_slide_back.setVisibility(View.VISIBLE);
            img_slide.setVisibility(View.VISIBLE);
        } else {
            img_slide_back.setVisibility(View.GONE);
            img_slide.setVisibility(View.GONE);
        }
// показать машинки
        layout_washer.setVisibility(View.VISIBLE);// параметры ---------------------------------------------
        // final Animation washerAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
        // layout_washer.startAnimation(washerAnimation);

// параметры layout_number_selected конец ----------------------------------------------------------
// VISIBLE LAYOUTS SCREEN
        layout_prepair_start.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        // layout_bt_search.setVisibility(View.GONE);
        layout_settings.setVisibility(View.GONE);
        //  layout_bt_not.setVisibility(View.GONE);
        layout_home.setVisibility(View.GONE);
// запускаем бар
        layout_action_bar.setVisibility(View.INVISIBLE);
        img_action_bar_icon_wash.setVisibility(View.GONE);
        wash_number.setVisibility(View.GONE);
        if(eng==1){img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno_eng);
        }else{img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
            img_action_bar_icon_vneseno.setImageResource(R.drawable.action_bar_icon_vneseno);}
        money_kup.setVisibility(View.VISIBLE);

        displayInfo();
        if (statusStr.contentEquals("1")) {
            if(eng==1){img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                img_action_bar_icon_electro.setImageResource(R.drawable.action_bar_icon_electro_eng);
                img_action_bar_icon_battary.setVisibility(View.INVISIBLE);
            }else{
                img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                img_action_bar_icon_electro.setImageResource(R.drawable.action_bar_icon_electro);
                img_action_bar_icon_battary.setVisibility(View.INVISIBLE);}
        } else {
            if(eng==1){img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                img_action_bar_icon_battary.setImageResource(R.drawable.action_bar_icon_battary_eng);
                img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
            }else{
                img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                img_action_bar_icon_battary.setImageResource(R.drawable.action_bar_icon_battary);
                img_action_bar_icon_electro.setVisibility(View.INVISIBLE);}
        }
// параметры по умолчанию ---------------------  начало --------------------------------------------
        machine1.setVisibility(View.GONE);
        machine2.setVisibility(View.GONE);
        machine3.setVisibility(View.GONE);
        machine4.setVisibility(View.GONE);
        machine5.setVisibility(View.GONE);
        machine6.setVisibility(View.GONE);
        machine7.setVisibility(View.GONE);
        machine8.setVisibility(View.GONE);
        machine9.setVisibility(View.GONE);
        machine10.setVisibility(View.GONE);
        machine11.setVisibility(View.GONE);
        machine12.setVisibility(View.GONE);
      /*  layout_wash1.setVisibility(View.VISIBLE);
        layout_wash2.setVisibility(View.VISIBLE);
        layout_wash3.setVisibility(View.VISIBLE);
        layout_wash4.setVisibility(View.VISIBLE);
        layout_wash5.setVisibility(View.VISIBLE);
        layout_wash6.setVisibility(View.VISIBLE);
        layout_wash7.setVisibility(View.VISIBLE);
        layout_wash8.setVisibility(View.VISIBLE);
        layout_wash9.setVisibility(View.VISIBLE);
        layout_wash10.setVisibility(View.VISIBLE);
        layout_wash11.setVisibility(View.VISIBLE);
        layout_wash12.setVisibility(View.VISIBLE);*/
        if(set_program==0){set_wash_image(0);} // стандарт обычный запуск
        if(set_program==1){set_wash_image(1);} // по режимам
        if(set_program==2){set_wash_image(2);} // по режимам и температуре
        if(set_program==3){set_wash_image(3);} // промышленные машины
// параметры по умолчанию ---------------------  конец ---------------------------------------------
// начало функции, до этого было вступление ********************************************************
/*
        switch(countwash) {
            case 0:

                break;
            case 1:
                layout_wash1.setVisibility(View.VISIBLE);
                machine1.setVisibility(View.VISIBLE);
                wash1_go.setVisibility(View.VISIBLE);
                wash1_go.setEnabled(false);
                if (wash1isdry == 1) { dry1.setVisibility(View.VISIBLE);
                } else { dry1.setVisibility(View.INVISIBLE); }
                if (wash1crash == 1) { imageDoor1.setVisibility(View.VISIBLE); imageDoor1.startAnimation(animationRotateCenter);
                } else { imageDoor1.setVisibility(View.INVISIBLE); imageDoor1.clearAnimation(); }
                sendMessage(WASH_1_DOOR);
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
        }
        */
        if (countwash == 0) {
            countwash = 2;
        }
        if (countwash >= 1) {
            machine1.setVisibility(View.VISIBLE);
            //  wash1.setVisibility(View.VISIBLE);
            if (dozator_true==1) {
                countwash=countdozator;
                if (countwash > 6) {
                    img_slide_back.setVisibility(View.VISIBLE);
                    img_slide.setVisibility(View.VISIBLE);
                } else {
                    img_slide_back.setVisibility(View.GONE);
                    img_slide.setVisibility(View.GONE);
                }
                dozator1.setVisibility(View.VISIBLE);
                dozator1.setBackgroundResource(R.drawable.tide75);
                wash1.setVisibility(View.GONE);
                wash1_go.setVisibility(View.VISIBLE);
                wash1_go.setEnabled(true);
            } else {
                wash1_go.setVisibility(View.VISIBLE);
                wash1_go.setEnabled(false);
                //  wash1.setVisibility(View.INVISIBLE);
                //  maytag1.setVisibility(View.VISIBLE);
                dozator1.setVisibility(View.GONE);

                if (wash1isdry == 1) {
                    dry1.setVisibility(View.VISIBLE); dry1.setBackgroundResource(R.drawable.beko_dcu);}
                else if (wash1isdry == 2) {
                    dry1.setVisibility(View.VISIBLE);wash1.setBackgroundResource(R.drawable.iron);dry1.setBackgroundResource(R.drawable.iron);
                } else {
                    dry1.setVisibility(View.INVISIBLE);
                }
                if (wash1crash == 1) {
                    imageDoor1.setVisibility(View.VISIBLE);
                    imageDoor1.startAnimation(animationRotateCenter);
                } else {
                    imageDoor1.setVisibility(View.INVISIBLE);
                    imageDoor1.clearAnimation();
                }
                sendMessage(WASH_1_DOOR);}
        }//отправляем только на первую машину, а дальше смотрим ответ ардуины
        if (countwash >= 2) {
            machine2.setVisibility(View.VISIBLE);
            dozator1.setBackgroundResource(R.drawable.tide75);
            dozator2.setBackgroundResource(R.drawable.tide150);
            //   wash2.setVisibility(View.VISIBLE);
            if (dozator_true==1) {
                dozator2.setVisibility(View.VISIBLE);
                wash2.setVisibility(View.GONE);
                //  maytag1.setVisibility(View.GONE);
                wash2_go.setVisibility(View.VISIBLE);
                wash2_go.setEnabled(true);
            } else {
                wash2_go.setVisibility(View.VISIBLE);
                wash2_go.setEnabled(false);
                // wash2.setVisibility(View.INVISIBLE);
                dozator2.setVisibility(View.GONE);
                //   maytag2.setVisibility(View.VISIBLE);
            }
            if (wash2isdry == 1) {
                dry2.setVisibility(View.VISIBLE); dry2.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash2isdry == 2) {
                dry2.setVisibility(View.VISIBLE);wash2.setBackgroundResource(R.drawable.iron);dry2.setBackgroundResource(R.drawable.iron);
            } else {
                dry2.setVisibility(View.INVISIBLE);
            }
            if (wash2crash == 1) {
                imageDoor2.setVisibility(View.VISIBLE);
                imageDoor2.startAnimation(animationRotateCenter);
            } else {
                imageDoor2.setVisibility(View.INVISIBLE);
                imageDoor2.clearAnimation();
            }
        }
        if (countwash >= 3) {
            machine3.setVisibility(View.VISIBLE);
            //    wash3.setVisibility(View.VISIBLE);
            if (dozator_true==1) {
                dozator3.setVisibility(View.VISIBLE);
                dozator1.setBackgroundResource(R.drawable.iron);
                dozator2.setBackgroundResource(R.drawable.iron);
                wash3.setVisibility(View.GONE);
                //  maytag1.setVisibility(View.GONE);
                wash3_go.setVisibility(View.VISIBLE);
                wash3_go.setEnabled(true);
            } else {
                wash3_go.setVisibility(View.VISIBLE);
                wash3_go.setEnabled(false);
                // wash2.setVisibility(View.INVISIBLE);
                dozator3.setVisibility(View.GONE);
                //   maytag2.setVisibility(View.VISIBLE);
            }
            if (wash3isdry == 1) {
                dry3.setVisibility(View.VISIBLE); dry3.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash3isdry == 2) {
                dry3.setVisibility(View.VISIBLE);wash3.setBackgroundResource(R.drawable.iron);dry3.setBackgroundResource(R.drawable.iron);
            } else {
                dry3.setVisibility(View.INVISIBLE);
            }
           // wash3_go.setVisibility(View.VISIBLE);
           // wash3_go.setEnabled(false);
            if (wash3crash == 1) {
                imageDoor3.setVisibility(View.VISIBLE);
                imageDoor3.startAnimation(animationRotateCenter);
            } else {
                imageDoor3.setVisibility(View.INVISIBLE);
                imageDoor3.clearAnimation();
            }
        }
        if (countwash >= 4) {
            machine4.setVisibility(View.VISIBLE);
            //    wash4.setVisibility(View.VISIBLE);
            if (dozator_true==1) {
                dozator4.setVisibility(View.VISIBLE);
                dozator1.setBackgroundResource(R.drawable.iron);
                dozator2.setBackgroundResource(R.drawable.iron);
                wash4.setVisibility(View.GONE);
                //  maytag1.setVisibility(View.GONE);
                wash4_go.setVisibility(View.VISIBLE);
                wash4_go.setEnabled(true);
            } else {
                wash4_go.setVisibility(View.VISIBLE);
                wash4_go.setEnabled(false);
                // wash2.setVisibility(View.INVISIBLE);
                dozator4.setVisibility(View.GONE);
                //   maytag2.setVisibility(View.VISIBLE);
            }
            if (wash4isdry == 1) {
                dry4.setVisibility(View.VISIBLE); dry4.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash4isdry == 2) {
                dry4.setVisibility(View.VISIBLE);wash4.setBackgroundResource(R.drawable.iron);dry4.setBackgroundResource(R.drawable.iron);
            } else {
                dry4.setVisibility(View.INVISIBLE);
            }
           // wash4_go.setVisibility(View.VISIBLE);
           // wash4_go.setEnabled(false);
            if (wash4crash == 1) {
                imageDoor4.setVisibility(View.VISIBLE);
                imageDoor4.startAnimation(animationRotateCenter);
            } else {
                imageDoor4.setVisibility(View.INVISIBLE);
                imageDoor4.clearAnimation();
            }
        }
        if (countwash >= 5) {
            machine5.setVisibility(View.VISIBLE);
            //   wash5.setVisibility(View.VISIBLE);
            if (wash5isdry == 1) {
                dry5.setVisibility(View.VISIBLE); dry5.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash5isdry == 2) {
                dry5.setVisibility(View.VISIBLE);wash5.setBackgroundResource(R.drawable.iron);dry5.setBackgroundResource(R.drawable.iron);
            } else {
                dry5.setVisibility(View.INVISIBLE);
            }
            wash5_go.setVisibility(View.VISIBLE);
            wash5_go.setEnabled(false);
            if (wash5crash == 1) {
                imageDoor5.setVisibility(View.VISIBLE);
                imageDoor5.startAnimation(animationRotateCenter);
            } else {
                imageDoor5.setVisibility(View.INVISIBLE);
                imageDoor5.clearAnimation();
            }
        }
        if (countwash >= 6) {
            machine6.setVisibility(View.VISIBLE);
            //   wash6.setVisibility(View.VISIBLE);
            if (wash6isdry == 1) {
                dry6.setVisibility(View.VISIBLE); dry6.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash6isdry == 2) {
                dry6.setVisibility(View.VISIBLE);wash6.setBackgroundResource(R.drawable.iron);dry6.setBackgroundResource(R.drawable.iron);
            } else {
                dry6.setVisibility(View.INVISIBLE);
            }
            wash6_go.setVisibility(View.VISIBLE);
            wash6_go.setEnabled(false);
            if (wash6crash == 1) {
                imageDoor6.setVisibility(View.VISIBLE);
                imageDoor6.startAnimation(animationRotateCenter);
            } else {
                imageDoor6.setVisibility(View.INVISIBLE);
                imageDoor6.clearAnimation();
            }
        }
        if (countwash >= 7) {
            machine7.setVisibility(View.VISIBLE);
            //   wash7.setVisibility(View.VISIBLE);
            if (wash7isdry == 1) {
                dry7.setVisibility(View.VISIBLE); dry7.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash7isdry == 2) {
                dry7.setVisibility(View.VISIBLE);wash7.setBackgroundResource(R.drawable.iron);dry7.setBackgroundResource(R.drawable.iron);
            } else {
                dry7.setVisibility(View.INVISIBLE);
            }
            wash7_go.setVisibility(View.VISIBLE);
            wash7_go.setEnabled(true);
            if (wash7crash == 1) {imageDoor7.setVisibility(View.VISIBLE);
                imageDoor7.startAnimation(animationRotateCenter);
            } else {imageDoor7.setVisibility(View.INVISIBLE);
                imageDoor7.clearAnimation();
            }
        }
        if (countwash >= 8) {
            machine8.setVisibility(View.VISIBLE);
            //    wash8.setVisibility(View.VISIBLE);
            if (wash8isdry == 1) {
                dry8.setVisibility(View.VISIBLE); dry8.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash8isdry == 2) {
                dry8.setVisibility(View.VISIBLE);wash8.setBackgroundResource(R.drawable.iron);dry8.setBackgroundResource(R.drawable.iron);
            } else {
                dry8.setVisibility(View.INVISIBLE);
            }
            wash8_go.setVisibility(View.VISIBLE);
            wash8_go.setEnabled(true);
            if (wash8crash == 1) {
                imageDoor8.setVisibility(View.VISIBLE);
                imageDoor8.startAnimation(animationRotateCenter);
            } else {
                imageDoor8.setVisibility(View.INVISIBLE);
                imageDoor8.clearAnimation();
            }
        }
        if (countwash >= 9) {
            machine9.setVisibility(View.VISIBLE);
            //  wash9.setVisibility(View.VISIBLE);
            if (wash9isdry == 1) {
                dry9.setVisibility(View.VISIBLE); dry9.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash9isdry == 2) {
                dry9.setVisibility(View.VISIBLE);wash9.setBackgroundResource(R.drawable.iron);dry9.setBackgroundResource(R.drawable.iron);
            } else {
                dry9.setVisibility(View.INVISIBLE);
            }
            wash9_go.setVisibility(View.VISIBLE);
            wash9_go.setEnabled(true);
            if (wash9crash == 1) {
                imageDoor9.setVisibility(View.VISIBLE);
                imageDoor9.startAnimation(animationRotateCenter);
            } else {
                imageDoor9.setVisibility(View.INVISIBLE);
                imageDoor9.clearAnimation();
            }
        }
        if (countwash >= 10) {
            machine10.setVisibility(View.VISIBLE);
            //  wash10.setVisibility(View.VISIBLE);
            if (wash10isdry == 1) {
                dry10.setVisibility(View.VISIBLE); dry10.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash10isdry == 2) {
                dry10.setVisibility(View.VISIBLE);wash10.setBackgroundResource(R.drawable.iron);dry10.setBackgroundResource(R.drawable.iron);
            } else {
                dry10.setVisibility(View.INVISIBLE);
            }
            wash10_go.setVisibility(View.VISIBLE);
            wash10_go.setEnabled(true);
            if (wash10isdry == 1) {
                dry10.setVisibility(View.VISIBLE); dry10.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash10isdry == 2) {
                imageDoor10.setVisibility(View.VISIBLE);
                imageDoor10.startAnimation(animationRotateCenter);
            } else {
                imageDoor10.setVisibility(View.INVISIBLE);
                imageDoor10.clearAnimation();
            }
        }
        if (countwash >= 11) {
            machine11.setVisibility(View.VISIBLE);
            // wash11.setVisibility(View.VISIBLE);
            if (wash11isdry == 1) {
                dry11.setVisibility(View.VISIBLE); dry11.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash11isdry == 2) {
                dry11.setVisibility(View.VISIBLE);wash11.setBackgroundResource(R.drawable.iron);dry11.setBackgroundResource(R.drawable.iron);
            } else {
                dry11.setVisibility(View.INVISIBLE);
            }
            wash11_go.setVisibility(View.VISIBLE);
            wash11_go.setEnabled(true);
            if (wash11crash == 1) {
                imageDoor11.setVisibility(View.VISIBLE);
                imageDoor11.startAnimation(animationRotateCenter);
            } else {
                imageDoor11.setVisibility(View.INVISIBLE);
                imageDoor11.clearAnimation();
            }
        }
        if (countwash >= 12) {
            machine12.setVisibility(View.VISIBLE);
            // wash12.setVisibility(View.VISIBLE);
            if (wash12isdry == 1) {
                dry12.setVisibility(View.VISIBLE); dry12.setBackgroundResource(R.drawable.beko_dcu);}
            else if (wash12isdry == 2) {
                dry12.setVisibility(View.VISIBLE);wash12.setBackgroundResource(R.drawable.iron);dry12.setBackgroundResource(R.drawable.iron);
            } else {
                dry12.setVisibility(View.INVISIBLE);
            }
            wash12_go.setVisibility(View.VISIBLE);
            wash12_go.setEnabled(true);
            if (wash12crash == 1) {
                imageDoor12.setVisibility(View.VISIBLE);
                imageDoor12.startAnimation(animationRotateCenter);
            } else {
                imageDoor12.setVisibility(View.INVISIBLE);
                imageDoor12.clearAnimation();
            }
        }
    }// END FUNCTIONS NUMBERS SELECTED -----------------------**************----------------------------


    private void arduinoLife() {
        if(dozator_true==1){sendMessage(NOT_RESET);}else{sendMessage(RESERVED4);}
                                           //B                         //D
    }
    private void checkBT1() {
        Log.d(TAG, "определили статус STATE_NONE и отправляем букву "+CHECK_1_BT+" запрос на отображение машин");
        sendMessage(CHECK_1_BT);
    }
    private void door2_view() {
        Log.d(TAG, "отправляем букву "+WASH_2_DOOR+" запрос на датчик двери машины 2");
        sendMessage(WASH_2_DOOR);
    }
    private void door3_view() {
        Log.d(TAG, "отправляем букву "+WASH_3_DOOR+" запрос на датчик двери машины 3");
        sendMessage(WASH_3_DOOR);
    }
    private void door4_view() {
        sendMessage(WASH_4_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_4_DOOR+" запрос на датчик двери машины 4");
    }
    private void door5_view() {
        sendMessage(WASH_5_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_5_DOOR+" запрос на датчик двери машины 5");
    }
    private void door6_view() {
        sendMessage(WASH_6_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_6_DOOR+" запрос на датчик двери машины 6");
    }
    private void door7_view() {
        sendMessage(WASH_7_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_7_DOOR+" запрос на датчик двери машины 7");
    }
    private void door8_view() {
        sendMessage(WASH_8_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_8_DOOR+" запрос на датчик двери машины 8");
    }
    private void door9_view() {
        sendMessage(WASH_9_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_9_DOOR+" запрос на датчик двери машины 9");
    }
    private void door10_view() {
        sendMessage(WASH_10_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_10_DOOR+" запрос на датчик двери машины 10");
    }
    private void door11_view() {
        sendMessage(WASH_11_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_11_DOOR+" запрос на датчик двери машины 11");
    }
    private void door12_view() {
        sendMessage(WASH_12_DOOR);
        Log.d(TAG, "отправляем букву "+WASH_12_DOOR+" запрос на датчик двери машины 12");
    }
    private void change_dozator_off() {
        sendMessage(WASH_12_START);
        Log.d(TAG, "отправляем букву "+WASH_12_START+"  ");
    }
    private void change_dozator_on() {
        sendMessage(WASH_12_POWER);
        Log.d(TAG, "отправляем букву "+WASH_12_POWER+"  ");
    }
    /**
     *  Prepair to My System settings ------------------------------------------------------------------
     */
    private void display_settings_load() { if(layout_start_anim.getVisibility()==View.VISIBLE){dialog_settings_vs_animstart();}
    else{layout_bg.setBackgroundResource(R.drawable.settings_choose);//layout_gone();
        imageViewScaleHome.setVisibility(View.INVISIBLE);imageViewScaleHome.clearAnimation();
        layout_number_selected.setVisibility(View.GONE);
        layout_prepair_start.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        layout_home.setVisibility(View.GONE);
        layout_action_bar.setVisibility(View.GONE);//  бар
        layout_block.setVisibility(View.GONE);
        layout_settings.setVisibility(View.VISIBLE);
        layout_settings_preview.setVisibility(View.VISIBLE);
        layout_settings_preview.setBackgroundResource(R.drawable.settings_choose);
        display_settings_choose();}}

    /**
     *  All My System settings  ========================================================================
     */
    private void display_settings_choose() {
        display_settings_activity = 1;

// основное меню -----------------------------------------------------------------------------------
        layout_settings_action_bar.setVisibility(View.VISIBLE);// запуск с параметрами кнопки открывающей левое меню и необнуляемым итогом
        img_settings_action_bar_icon_view_leftmenu.setVisibility(View.VISIBLE);btn_settings_save.setEnabled(true);
        final Animation animationRotateCenterPopapButton = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_btn);// подкрутим кнопочку немного
        img_settings_action_bar_icon_view_leftmenu.startAnimation(animationRotateCenterPopapButton);img_settings_action_bar_icon_smsstatus.setVisibility(View.INVISIBLE);
        img_settings_action_bar_icon_valuta.setVisibility(View.INVISIBLE);img_settings_action_bar_icon_allcashsum.setVisibility(View.VISIBLE);
        displayInfo();// глянем батареечку
        if (statusStr.contentEquals("1")){
            img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_battary.setVisibility(View.GONE);
        }else{
            img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_electro.setVisibility(View.GONE);
        }
        statistics_txt.setVisibility(View.VISIBLE);// покажем общую сумму
        valuta_statistics_txt.setVisibility(View.VISIBLE);// покажем валюту
        if (valuta==1){ statistics_txt.setText("" + money + " "); valuta_statistics_txt.setText("RUB");}
        if (valuta==2){ statistics_txt.setText("" + money + " "); valuta_statistics_txt.setText("EUR"); }
        if (valuta==3){ statistics_txt.setText("" + money + "0 "); valuta_statistics_txt.setText("BYR");}
        if (valuta==4){ statistics_txt.setText("" + money + " "); valuta_statistics_txt.setText("KZT"); }



// -------------------------------------------------------------------------------------------------
// ====================---------------- ШТОРКА РАЗДЕЛОВ ---------------=============================
        layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
        layout_settings_buttons.setVisibility(View.VISIBLE);// параметры // кнопки left menu
        btn_settings_washer.setEnabled(true);txt_opacity_menu1.setVisibility(View.INVISIBLE);
        btn_settings_iron.setEnabled(true);txt_opacity_menu2.setVisibility(View.INVISIBLE);
        btn_settings_sms.setEnabled(true);txt_opacity_menu3.setVisibility(View.INVISIBLE);
        btn_settings_bluetooth.setEnabled(true);txt_opacity_menu4.setVisibility(View.INVISIBLE);
        btn_settings_tarif.setEnabled(true);txt_opacity_menu5.setVisibility(View.INVISIBLE);
        btn_settings_system.setEnabled(true);txt_opacity_menu6.setVisibility(View.INVISIBLE);
        btn_settings_stat.setEnabled(true);txt_opacity_menu7.setVisibility(View.INVISIBLE);
        btn_settings_exit.setEnabled(true);txt_opacity_menu8.setVisibility(View.INVISIBLE);
// -------------------------------------------------------------------------------------------------
// -------------------------------------- РАЗДЕЛЫ МЕНЮ ---------------------------------------------
// соответственно отображаем меню тарифы, а другие скрываем,, раздел машинки, включать вместе с машинами layout_settings_wash_number
        layout_settings_washer.setVisibility(View.INVISIBLE);
        if(dozator_true==1){btn_settings_washer_base.setEnabled(true);}else{
            btn_settings_washer_base.setEnabled(true);}// кнопки машинки
// раздел утюги
        layout_settings_iron.setVisibility(View.INVISIBLE);   btn_settings_iron_base.setEnabled(true);// кнопки утюгов
// раздел смс
        layout_settings_sms.setVisibility(View.INVISIBLE);     // кнопки смс
        btn_settings_sms_director.setEnabled(true);     btn_settings_sms_manager.setEnabled(true);
        btn_settings_sms_otchet_admin.setEnabled(true); btn_settings_sms_otchet_start_washer.setEnabled(true);
        btn_settings_sms_otchet_client.setEnabled(true);btn_settings_sms_otchet_power.setEnabled(true);
        btn_settings_sms_read_command.setEnabled(true); btn_settings_sms_base.setEnabled(false);
// раздел bluetooth
        layout_settings_bluetooth.setVisibility(View.INVISIBLE);
        btn_settings_bluetooth_base.setEnabled(false); // кнопки bluetooth
        if(!mBluetoothAdapter.isEnabled()){btn_settings_bluetooth_adapter.setChecked(false);
        }else{btn_settings_bluetooth_adapter.setChecked(true);}
        btn_settings_bluetooth_list.setEnabled(true);btn_settings_bluetooth_search.setEnabled(true);
// раздел тарифы
        layout_settings_tarif.setVisibility(View.INVISIBLE); // кнопки тарифов
        btn_settings_tarif_base.setEnabled(false);btn_settings_tarif_valuta.setEnabled(true);
        if(key!=1){
            btn_settings_tarif_wash.setEnabled(true);btn_settings_tarif_yandex.setEnabled(false);
            btn_settings_tarif_mode.setEnabled(true);btn_settings_tarif_iron.setEnabled(false);
            btn_settings_tarif_sber.setEnabled(false);
        }else{
            btn_settings_tarif_wash.setEnabled(true);btn_settings_tarif_yandex.setEnabled(true);
            btn_settings_tarif_mode.setEnabled(true);btn_settings_tarif_iron.setEnabled(true);
            btn_settings_tarif_sber.setEnabled(true);}
        btn_settings_mode_name.setEnabled(true);
        btn_settings_tarif_qiwi.setEnabled(true);
// раздел система
        layout_settings_system.setVisibility(View.INVISIBLE);   // кнопки
        btn_settings_system_base.setEnabled(false);btn_settings_system_log_view.setEnabled(true);
        btn_settings_system_com.setEnabled(true);btn_settings_system_destroy_upgrade.setEnabled(true);
        btn_settings_system_licence.setEnabled(true);btn_settings_system_blockcalls.setEnabled(true);
        btn_settings_system_optionsplus.setEnabled(true);btn_settings_system_fullscreen.setEnabled(true);
        btn_settings_system_autostart.setEnabled(true);btn_settings_system_keyboard.setEnabled(true);
        btn_settings_system_display.setEnabled(true);btn_settings_system_autoreboot.setEnabled(true);
        btn_settings_system_camera.setEnabled(true);btn_settings_system_battary.setEnabled(true);
        // подразделы системы
        layout_system_log.setVisibility(View.GONE);layout_log.setVisibility(View.INVISIBLE);
        btn_close_layout_com.setEnabled(true); btn_close_layout_com.setVisibility(View.VISIBLE);
// раздел статистика, включать вместе с машинами layout_settings_wash_number
        layout_settings_stat.setVisibility(View.VISIBLE); btn_settings_stat_inkass.setEnabled(true);// инкассация

        if(dozator_true==1){countwash=countdozator;
            if(countwash>=1){statArray_w1.clear();
                int all_w1=0;
                all_w1=stat_w1;
                statArray_w1.add("Всего стирок: "       +all_w1);
                statArray_w1.add("По старту: "+stat_w1);
                statArray_w1.add("Запусков по СМС: "+statsms_w1);}
            if(countwash>=2){statArray_w2.clear();int all_w2=0;
                all_w2=stat_w2;
                statArray_w2.add("Всего стирок: "       +all_w2);
                statArray_w2.add("По старту: "+stat_w2);
                statArray_w2.add("Запусков по СМС: "+statsms_w2);}
            if(countwash>=3){statArray_w3.clear();int all_w3=0;
                all_w3=stat_w3;
                statArray_w3.add("Всего стирок: "       +all_w3);
                statArray_w3.add("По старту: "+stat_w3);
                statArray_w3.add("Запусков по СМС: "+statsms_w3);}
            if(countwash>=4){statArray_w4.clear();int all_w4=0;
                all_w4=stat_w4;
                statArray_w4.add("Всего стирок: "       +all_w4);
                statArray_w4.add("По старту: "+stat_w4);
                statArray_w4.add("Запусков по СМС: "+statsms_w4);}
        }else{

// загрузка статистики, очистка старых данных перед записью новых
            if(countwash>=1){statArray_w1.clear();
                int all_w1=0;
                all_w1=stat_w1 +statmod1_w1 +statmod2_w1 +statmod3_w1 +statmod4_w1 +statmod5_w1 +statmod6_w1;
                statArray_w1.add("Всего стирок: "       +all_w1);
                statArray_w1.add("По старту: "+stat_w1);
                // statArray_w1.add("Запусков по режимам: ");
                statArray_w1.add("Быстрая: "       +statmod1_w1);
                statArray_w1.add("Интенсивная: "   +statmod2_w1);
                statArray_w1.add("Хлопок: "        +statmod3_w1);
                statArray_w1.add("Шерсть: "        +statmod4_w1);
                statArray_w1.add("Синтетика: "     +statmod5_w1);
                statArray_w1.add("Полоскание: "    +statmod6_w1);
                statArray_w1.add("Запусков по СМС: "+statsms_w1);}
            // statArray_w1.add("Общее кол:"       +statsms_w1);
            // statArray_w1.add("СМС Директора: откл");
            // statArray_w1.add("СМС Менеджера: откл");
            // statArray_w1.add("Показатели машины:");
            // statArray_w1.add("Ремонтов: откл");
            // statArray_w1.add("Ошибки датчиков: откл");
            // statArray_w1.add("Заработано денег: откл");}
            if(countwash>=2){statArray_w2.clear();int all_w2=0;
                all_w2=stat_w2
                        +statmod1_w2
                        +statmod2_w2
                        +statmod3_w2
                        +statmod4_w2
                        +statmod5_w2
                        +statmod6_w2;
                statArray_w2.add("Всего стирок: "       +all_w2);
                statArray_w2.add("По старту: "+stat_w2);
                statArray_w2.add("Быстрая: "       +statmod1_w2);
                statArray_w2.add("Интенсивная: "   +statmod2_w2);
                statArray_w2.add("Хлопок: "        +statmod3_w2);
                statArray_w2.add("Шерсть: "        +statmod4_w2);
                statArray_w2.add("Синтетика: "     +statmod5_w2);
                statArray_w2.add("Полоскание: "    +statmod6_w2);
                statArray_w2.add("Запусков по СМС: "+statsms_w2);}

            if(countwash>=3){statArray_w3.clear();
                int all_w3=0;
                all_w3 =stat_w3
                        +statmod1_w3
                        +statmod2_w3
                        +statmod3_w3
                        +statmod4_w3
                        +statmod5_w3
                        +statmod6_w3;
                statArray_w3.add("Всего стирок: "       +all_w3);
                statArray_w3.add("Запусков по старту: "+stat_w3);
                statArray_w3.add("Быстрая: "       +statmod1_w3);
                statArray_w3.add("Интенсивная: "   +statmod2_w3);
                statArray_w3.add("Хлопок: "        +statmod3_w3);
                statArray_w3.add("Шерсть: "        +statmod4_w3);
                statArray_w3.add("Синтетика: "     +statmod5_w3);
                statArray_w3.add("Полоскание: "    +statmod6_w3);
                statArray_w3.add("Запусков по СМС: "+statsms_w3);}
            if(countwash>=4){statArray_w4.clear();
                int all_w4=0;
                all_w4 =stat_w4
                        +statmod1_w4
                        +statmod2_w4
                        +statmod3_w4
                        +statmod4_w4
                        +statmod5_w4
                        +statmod6_w4;
                statArray_w4.add("Всего стирок: "    +all_w4);statArray_w4.add("Запусков по старту: "+stat_w4);
                statArray_w4.add("Быстрая: "       +statmod1_w4);
                statArray_w4.add("Интенсивная: "+statmod2_w4);statArray_w4.add("Хлопок: "        +statmod3_w4);
                statArray_w4.add("Шерсть: "     +statmod4_w4);statArray_w4.add("Синтетика: "     +statmod5_w4);
                statArray_w4.add("Полоскание: " +statmod6_w4);statArray_w4.add("Запусков по СМС: "+statsms_w4);}
            if(countwash>=5){statArray_w5.clear();int all_w5=0;
                all_w5 =stat_w5 +statmod1_w5 +statmod2_w5 +statmod3_w5 +statmod4_w5 +statmod5_w5 +statmod6_w5;
                statArray_w5.add("Всего стирок: "    +all_w5);statArray_w5.add("Запусков по старту: "+stat_w5);
                statArray_w5.add("Быстрая: "       +statmod1_w5);
                statArray_w5.add("Интенсивная: "+statmod2_w5);statArray_w5.add("Хлопок: "        +statmod3_w5);
                statArray_w5.add("Шерсть: "     +statmod4_w5);statArray_w5.add("Синтетика: "     +statmod5_w5);
                statArray_w5.add("Полоскание: " +statmod6_w5);statArray_w5.add("Запусков по СМС: "+statsms_w5);}
            if(countwash>=6){statArray_w6.clear();int all_w6=0;
                all_w6 =stat_w6 +statmod1_w6 +statmod2_w6 +statmod3_w6 +statmod4_w6 +statmod5_w6 +statmod6_w6;
                statArray_w6.add("Всего стирок: "    +all_w6);statArray_w6.add("Запусков по старту: "+stat_w6);
                statArray_w6.add("Быстрая: "       +statmod1_w6);
                statArray_w6.add("Интенсивная: "+statmod2_w6);statArray_w6.add("Хлопок: "        +statmod3_w6);
                statArray_w6.add("Шерсть: "     +statmod4_w6);statArray_w6.add("Синтетика: "     +statmod5_w6);
                statArray_w6.add("Полоскание: " +statmod6_w6);statArray_w6.add("Запусков по СМС: "+statsms_w6);}
            if(countwash>=7){statArray_w7.clear();int all_w7=0;
                all_w7 =stat_w7 +statmod1_w7 +statmod2_w7 +statmod3_w7 +statmod4_w7 +statmod5_w7 +statmod6_w7;
                statArray_w7.add("Всего стирок: "    +all_w7);statArray_w7.add("Запусков по старту: "+stat_w7);
                statArray_w7.add("Быстрая: "       +statmod1_w7);
                statArray_w7.add("Интенсивная: "+statmod2_w7);statArray_w7.add("Хлопок: "        +statmod3_w7);
                statArray_w7.add("Шерсть: "     +statmod4_w7);statArray_w7.add("Синтетика: "     +statmod5_w7);
                statArray_w7.add("Полоскание: " +statmod6_w7);statArray_w7.add("Запусков по СМС: "+statsms_w7);}
            if(countwash>=8){statArray_w8.clear();int all_w8=0;
                all_w8 =stat_w8 +statmod1_w8 +statmod2_w8 +statmod3_w8 +statmod4_w8 +statmod5_w8 +statmod6_w8;
                statArray_w8.add("Всего стирок: "    +all_w8);statArray_w8.add("Запусков по старту: "+stat_w8);
                statArray_w8.add("Быстрая: "       +statmod1_w8);
                statArray_w8.add("Интенсивная: "+statmod2_w8);statArray_w8.add("Хлопок: "        +statmod3_w8);
                statArray_w8.add("Шерсть: "     +statmod4_w8);statArray_w8.add("Синтетика: "     +statmod5_w8);
                statArray_w8.add("Полоскание: " +statmod6_w8);statArray_w8.add("Запусков по СМС: "+statsms_w8);}
            if(countwash>=9){statArray_w9.clear();int all_w9=0;
                all_w9 =stat_w9 +statmod1_w9 +statmod2_w9 +statmod3_w9 +statmod4_w9 +statmod5_w9 +statmod6_w9;
                statArray_w9.add("Всего стирок: "    +all_w9);statArray_w9.add("Запусков по старту: "+stat_w9);
                statArray_w9.add("Быстрая: "       +statmod1_w9);
                statArray_w9.add("Интенсивная: "+statmod2_w9);statArray_w9.add("Хлопок: "        +statmod3_w9);
                statArray_w9.add("Шерсть: "     +statmod4_w9);statArray_w9.add("Синтетика: "     +statmod5_w9);
                statArray_w9.add("Полоскание: " +statmod6_w9);statArray_w9.add("Запусков по СМС: "+statsms_w9);}
            if(countwash>=10){statArray_w10.clear();int all_w10=0;
                all_w10=stat_w10+statmod1_w10+statmod2_w10+statmod3_w10+statmod4_w10+statmod5_w10+statmod6_w10;
                statArray_w10.add("Всего стирок: "    +all_w10);statArray_w10.add("Запусков по старту: "+stat_w10);
                statArray_w10.add("Быстрая: "       +statmod1_w10);
                statArray_w10.add("Интенсивная: "+statmod2_w10);statArray_w10.add("Хлопок: "        +statmod3_w10);
                statArray_w10.add("Шерсть: "     +statmod4_w10);statArray_w10.add("Синтетика: "     +statmod5_w10);
                statArray_w10.add("Полоскание: " +statmod6_w10);statArray_w10.add("Запусков по СМС: "+statsms_w10);}
            if(countwash>=11){statArray_w11.clear();int all_w11=0;
                all_w11=stat_w11+statmod1_w11+statmod2_w11+statmod3_w11+statmod4_w11+statmod5_w11+statmod6_w11;
                statArray_w11.add("Всего стирок: "    +all_w11);statArray_w11.add("Запусков по старту: "+stat_w11);
                statArray_w11.add("Быстрая: "       +statmod1_w11);
                statArray_w11.add("Интенсивная: "+statmod2_w11);statArray_w11.add("Хлопок: "        +statmod3_w11);
                statArray_w11.add("Шерсть: "     +statmod4_w11);statArray_w11.add("Синтетика: "     +statmod5_w11);
                statArray_w11.add("Полоскание: " +statmod6_w11);statArray_w11.add("Запусков по СМС: "+statsms_w11);}
            if(countwash>=12){statArray_w12.clear();int all_w12=0;
                all_w12=stat_w12+statmod1_w12+statmod2_w12+statmod3_w12+statmod4_w12+statmod5_w12+statmod6_w12;
                statArray_w12.add("Всего стирок: "    +all_w12);statArray_w12.add("Запусков по старту: "+stat_w12);
                statArray_w12.add("Быстрая: "       +statmod1_w12);
                statArray_w12.add("Интенсивная: "+statmod2_w12);statArray_w12.add("Хлопок: "        +statmod3_w12);
                statArray_w12.add("Шерсть: "     +statmod4_w12);statArray_w12.add("Синтетика: "     +statmod5_w12);
                statArray_w12.add("Полоскание: " +statmod6_w12);statArray_w12.add("Запусков по СМС: "+statsms_w12);}}

// фрейм для отображения стиралок, использ.в машинах и статистике
        layout_settings_wash_number.setVisibility(View.INVISIBLE);
// вложенные машинки, параметры по умолчанию ---------------------  начало ------------------------
        machine1_settings.setVisibility(View.GONE); machine2_settings.setVisibility(View.GONE);machine3_settings.setVisibility(View.GONE); machine4_settings.setVisibility(View.GONE);
        machine5_settings.setVisibility(View.GONE); machine6_settings.setVisibility(View.GONE);machine7_settings.setVisibility(View.GONE); machine8_settings.setVisibility(View.GONE);
        machine9_settings.setVisibility(View.GONE); machine10_settings.setVisibility(View.GONE);machine11_settings.setVisibility(View.GONE); machine12_settings.setVisibility(View.GONE);
        layout_wash1_settings.setVisibility(View.VISIBLE); layout_wash2_settings.setVisibility(View.VISIBLE);layout_wash3_settings.setVisibility(View.VISIBLE); layout_wash4_settings.setVisibility(View.VISIBLE);
        layout_wash5_settings.setVisibility(View.VISIBLE); layout_wash6_settings.setVisibility(View.VISIBLE);layout_wash7_settings.setVisibility(View.VISIBLE); layout_wash8_settings.setVisibility(View.VISIBLE);
        layout_wash9_settings.setVisibility(View.VISIBLE); layout_wash10_settings.setVisibility(View.VISIBLE);layout_wash11_settings.setVisibility(View.VISIBLE); layout_wash12_settings.setVisibility(View.VISIBLE);
        num1_settings.setVisibility(View.VISIBLE); num1_settings.setEnabled(true);num2_settings.setVisibility(View.VISIBLE); num2_settings.setEnabled(true);
        num3_settings.setVisibility(View.VISIBLE); num3_settings.setEnabled(true);num4_settings.setVisibility(View.VISIBLE); num4_settings.setEnabled(true);
        num5_settings.setVisibility(View.VISIBLE); num5_settings.setEnabled(true);num6_settings.setVisibility(View.VISIBLE); num6_settings.setEnabled(true);
        num7_settings.setVisibility(View.VISIBLE); num7_settings.setEnabled(true);num8_settings.setVisibility(View.VISIBLE); num8_settings.setEnabled(true);
        num9_settings.setVisibility(View.VISIBLE); num9_settings.setEnabled(true);num10_settings.setVisibility(View.VISIBLE); num10_settings.setEnabled(true);
        num11_settings.setVisibility(View.VISIBLE);num11_settings.setEnabled(true);num12_settings.setVisibility(View.VISIBLE); num12_settings.setEnabled(true);
        // wash1_settings.setVisibility(View.VISIBLE);wash2_settings.setVisibility(View.VISIBLE);wash3_settings.setVisibility(View.VISIBLE); wash4_settings.setVisibility(View.VISIBLE);
        //  wash5_settings.setVisibility(View.VISIBLE);wash6_settings.setVisibility(View.VISIBLE);wash7_settings.setVisibility(View.VISIBLE); wash8_settings.setVisibility(View.VISIBLE);
        //  wash9_settings.setVisibility(View.VISIBLE);wash10_settings.setVisibility(View.VISIBLE);wash11_settings.setVisibility(View.VISIBLE); wash12_settings.setVisibility(View.VISIBLE);
// все настроено под вывод в раздел машинки, для отображения в статистике добавить видимость "ArrayAdapter"-ов
        if(countwash==0){countwash=2;}
        if(countwash>=1){
            machine1_settings.setVisibility(View.VISIBLE);
            //machine2_settings.setVisibility(View.GONE);
            //machine3_settings.setVisibility(View.GONE);
            //machine4_settings.setVisibility(View.GONE);
            if(wash1remont==1){}
            /*if(dozator_true==1){
                wash1_settings.setVisibility(View.GONE);
                dry1_settings.setVisibility(View.GONE);
                dozator1_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.tide75);
                dozator2_settings.setVisibility(View.GONE);
                dozator3_settings.setVisibility(View.GONE);
                dozator4_settings.setVisibility(View.GONE);
            }else{
                if(wash1isdry>=1){
                    dry1_settings.setVisibility(View.VISIBLE);
                    wash1_settings.setBackgroundResource(R.drawable.iron);
                    wash1_settings.setVisibility(View.VISIBLE);
                    dozator1_settings.setVisibility(View.GONE);
                    dozator2_settings.setVisibility(View.GONE);
                    dozator3_settings.setVisibility(View.GONE);
                    dozator4_settings.setVisibility(View.GONE);
                }else{
                    dry1_settings.setVisibility(View.GONE);
                    wash1_settings.setVisibility(View.VISIBLE);
                    dozator1_settings.setVisibility(View.GONE);
                    dozator2_settings.setVisibility(View.GONE);
                    dozator3_settings.setVisibility(View.GONE);
                    dozator4_settings.setVisibility(View.GONE);
                }

            }*/
        }//потом можно подвесить иконку ремонта
        if(countwash>=2){machine2_settings.setVisibility(View.VISIBLE);
           /* if(wash2isdry>=1){
                dry2_settings.setVisibility(View.VISIBLE);
                wash2_settings.setBackgroundResource(R.drawable.iron);
            }else{
                dry2_settings.setVisibility(View.GONE);
            }
            if(wash2remont==1){}
            if(dozator_true==1){
                wash2_settings.setVisibility(View.GONE);
                dry2_settings.setVisibility(View.GONE);
                dozator2_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.tide75);
                dozator2_settings.setBackgroundResource(R.drawable.tide150);
                dozator3_settings.setVisibility(View.GONE);
                dozator4_settings.setVisibility(View.GONE);
            }else{
                wash2_settings.setVisibility(View.VISIBLE);
                dozator2_settings.setVisibility(View.GONE);
                dozator3_settings.setVisibility(View.GONE);
                dozator4_settings.setVisibility(View.GONE);
            }*/
        }//потом можно подвесить иконку ремонта
        if(countwash>=3){machine3_settings.setVisibility(View.VISIBLE);if(wash3isdry>=1){dry3_settings.setVisibility(View.VISIBLE);wash3_settings.setBackgroundResource(R.drawable.iron);}
        else{dry3_settings.setVisibility(View.GONE);}if(wash3remont==1){}
            if(dozator_true==1){
                wash3_settings.setVisibility(View.GONE);
                dry3_settings.setVisibility(View.GONE);
                dozator3_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.iron);
                dozator2_settings.setBackgroundResource(R.drawable.iron);
                dozator4_settings.setVisibility(View.GONE);
            }else{
                wash3_settings.setVisibility(View.VISIBLE);
                dozator3_settings.setVisibility(View.GONE);
                dozator4_settings.setVisibility(View.GONE);
            }
        }//потом можно подвесить иконку ремонта
        if(countwash>=4){machine4_settings.setVisibility(View.VISIBLE);if(wash4isdry>=1){dry4_settings.setVisibility(View.VISIBLE);wash4_settings.setBackgroundResource(R.drawable.iron);}
        else{dry4_settings.setVisibility(View.GONE);}if(wash4remont==1){}
            if(dozator_true==1){
                wash4_settings.setVisibility(View.GONE);
                dry4_settings.setVisibility(View.GONE);
                dozator4_settings.setVisibility(View.VISIBLE);
                dozator1_settings.setBackgroundResource(R.drawable.iron);
                dozator2_settings.setBackgroundResource(R.drawable.iron);
            }else{
                wash4_settings.setVisibility(View.VISIBLE);
                dozator4_settings.setVisibility(View.GONE);
            }
        }//потом можно подвесить иконку ремонта
        if(countwash>=5){machine5_settings.setVisibility(View.VISIBLE);if(wash5isdry>=1){dry5_settings.setVisibility(View.VISIBLE);wash5_settings.setBackgroundResource(R.drawable.iron);}
        else{dry5_settings.setVisibility(View.GONE);}if(wash5remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=6){machine6_settings.setVisibility(View.VISIBLE);if(wash6isdry>=1){dry6_settings.setVisibility(View.VISIBLE);wash6_settings.setBackgroundResource(R.drawable.iron);}
        else{dry6_settings.setVisibility(View.GONE);}if(wash6remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=7){machine7_settings.setVisibility(View.VISIBLE);if(wash7isdry>=1){dry7_settings.setVisibility(View.VISIBLE);wash7_settings.setBackgroundResource(R.drawable.iron);}
        else{dry7_settings.setVisibility(View.GONE);}if(wash7remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=8){machine8_settings.setVisibility(View.VISIBLE);if(wash8isdry>=1){dry8_settings.setVisibility(View.VISIBLE);wash8_settings.setBackgroundResource(R.drawable.iron);}
        else{dry8_settings.setVisibility(View.GONE);}if(wash8remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=9){machine9_settings.setVisibility(View.VISIBLE);if(wash9isdry>=1){dry9_settings.setVisibility(View.VISIBLE);wash9_settings.setBackgroundResource(R.drawable.iron);}
        else{dry9_settings.setVisibility(View.GONE);}if(wash9remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=10){machine10_settings.setVisibility(View.VISIBLE);if(wash10isdry>=1){dry10_settings.setVisibility(View.VISIBLE);wash10_settings.setBackgroundResource(R.drawable.iron);}
        else{dry10_settings.setVisibility(View.GONE);}if(wash10remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=11){machine11_settings.setVisibility(View.VISIBLE);if(wash11isdry>=1){dry11_settings.setVisibility(View.VISIBLE);wash11_settings.setBackgroundResource(R.drawable.iron);}
        else{dry11_settings.setVisibility(View.GONE);}if(wash11remont==1){}}//потом можно подвесить иконку ремонта
        if(countwash>=12){machine12_settings.setVisibility(View.VISIBLE);if(wash12isdry>=1){dry12_settings.setVisibility(View.VISIBLE);wash12_settings.setBackgroundResource(R.drawable.iron);}
        else{dry12_settings.setVisibility(View.GONE);}if(wash12remont==1){}}//потом можно подвесить иконку ремонта

        layout_settings_preview.setVisibility(View.VISIBLE);
// Готовый блок Титульного меню с выбором категорий
        btn1_settings_choose.setVisibility(View.VISIBLE); btn1_settings_choose.setEnabled(true);btn2_settings_choose.setVisibility(View.VISIBLE);
        btn2_settings_choose.setEnabled(true);btn3_settings_choose.setVisibility(View.VISIBLE); btn3_settings_choose.setEnabled(true);
        btn4_settings_choose.setVisibility(View.VISIBLE); btn4_settings_choose.setEnabled(true);btn5_settings_choose.setVisibility(View.VISIBLE);
        btn5_settings_choose.setEnabled(true);btn6_settings_choose.setVisibility(View.VISIBLE); btn6_settings_choose.setEnabled(true);
    }

    private void displayInfo() {
        /*
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getActivity().registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        switch(status){case BatteryManager.BATTERY_STATUS_CHARGING:statusStr="1";break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:break;
            case BatteryManager.BATTERY_STATUS_FULL:statusStr="1";break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:statusStr="2";break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:break;
            default:statusStr="2";}}
*/
    }

    /**
     * SAVE MONEY FROM INTERNET PAY (SMS CHECK, SIM BEELINE)
     */
    private void save_money_from_internet(float cash) {
        float vnes = money_vneseno + cash;
        money_kup.setText("" + vnes);
        money_vneseno = vnes;
        money = money + cash;
        mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putFloat(PREF_MONEYVNESENO, money_vneseno);
        editor.putFloat(PREF_MONEY, money);
        editor.apply();
        mConversationArrayAdapter.add("INTERNET_PAY:  +" + cash + " RUB");
        // Toast.makeText(getActivity(), "Внесение денег: " + cash + " руб ", Toast.LENGTH_LONG).show();
    }

    /**
     *  SEND SMS CHECK DOOR, CLIENTS
     */
    private void mSMSmanager(int notice, String number, int wash, String PREF) {
        if((notice==1)&&(number.length()>7)){
            if(number.contentEquals("+7")){

            }else{
                SmsManager sms=SmsManager.getDefault();
                sms.sendTextMessage(number,null,"Машина "+wash+": "+sms_client_notice_msg,null,null);
                // Toast.makeText(getActivity(), "Send SMS: Washer "+wash+" is free", Toast.LENGTH_SHORT).show();
                SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor=mSettings.edit();Boolean faf = false;
                editor.putString("smsnumber_client"+wash+"_key", "+7");
                editor.putBoolean("notice_wash"+wash+"_key", faf);editor.apply();
            }}
        mConversationArrayAdapter.add("Done: washer"+wash+"free_OK");
        if(wash==1){if(countwash>=2){door2_view();}notice_wash1=0;washer1timer=0;wash1crash=0;imageDoor1.setVisibility(View.INVISIBLE);imageDoor1.clearAnimation();wash1_go.setEnabled(true);}
        if(wash==2){if(countwash>=3){door3_view();}notice_wash2=0;washer2timer=0;wash2crash=0;imageDoor2.setVisibility(View.INVISIBLE);imageDoor2.clearAnimation();wash2_go.setEnabled(true);}
        if(wash==3){if(countwash>=4){door4_view();}notice_wash3=0;washer3timer=0;wash3crash=0;imageDoor3.setVisibility(View.INVISIBLE);imageDoor3.clearAnimation();wash3_go.setEnabled(true);}
        if(wash==4){if(countwash>=5){door5_view();}notice_wash4=0;washer4timer=0;wash4crash=0;imageDoor4.setVisibility(View.INVISIBLE);imageDoor4.clearAnimation();wash4_go.setEnabled(true);}
        if(wash==5){if(countwash>=6){door6_view();}notice_wash5=0;washer5timer=0;wash5crash=0;imageDoor5.setVisibility(View.INVISIBLE);imageDoor5.clearAnimation();wash5_go.setEnabled(true);}
        if(wash==6){if(countwash>=7){door7_view();}notice_wash6=0;washer6timer=0;wash6crash=0;imageDoor6.setVisibility(View.INVISIBLE);imageDoor6.clearAnimation();wash6_go.setEnabled(true);}
        if(wash==7){if(countwash>=8){door8_view();}notice_wash7=0;washer7timer=0;wash7crash=0;imageDoor7.setVisibility(View.INVISIBLE);imageDoor7.clearAnimation();wash7_go.setEnabled(true);}
        if(wash==8){if(countwash>=9){door9_view();}notice_wash8=0;washer8timer=0;wash8crash=0;imageDoor8.setVisibility(View.INVISIBLE);imageDoor8.clearAnimation();wash8_go.setEnabled(true);}
        if(wash==9){if(countwash>=10){door10_view();}notice_wash9=0;washer9timer=0;wash9crash=0;imageDoor9.setVisibility(View.INVISIBLE);imageDoor9.clearAnimation();wash9_go.setEnabled(true);}
        if(wash==10){if(countwash>=11){door11_view();}notice_wash10=0;washer10timer=0;wash10crash=0;imageDoor10.setVisibility(View.INVISIBLE);imageDoor10.clearAnimation();wash10_go.setEnabled(true);}
        if(wash==11){if(countwash>=12){door12_view();}notice_wash11=0;washer11timer=0;wash11crash=0;imageDoor11.setVisibility(View.INVISIBLE);imageDoor11.clearAnimation();wash11_go.setEnabled(true);}
        if(wash==12){notice_wash12=0;washer12timer=0;wash12crash=0;imageDoor12.setVisibility(View.INVISIBLE);imageDoor12.clearAnimation();wash12_go.setEnabled(true);}
    }

    /**
     * SEND SMS CHECK DOOR, CLIENTS
     */
    private void notice_timeError(int wash, int time) {
        mConversationArrayAdapter.add("Done: washer" + wash + "timer_" + time + "_OK");
        if ((sms_director_notice == 1) && (sms_director_notice_timeError == 1)) {if((time>=300)&&(time<=303)){SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(sms_director_number, null, "Внимание! Машина " + wash + "работает >" + time + "мин. Стирка не завершена корректно.", null, null);
        }}
        if ((sms_manager_notice == 1) && (sms_manager_notice_timeError == 1)) {if((time>=300)&&(time<=303)){SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(sms_manager_number, null, "Внимание! Машина " + wash + " работает >" + time + " мин. Стирка не завершена корректно.", null, null);
        }}
        if (checkdoor == 1) {final Animation animationRotateCenter = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
            if (wash == 1) {wash1crash = 1;if (wash1.getVisibility() == View.VISIBLE) {imageDoor1.setVisibility(View.VISIBLE);imageDoor1.startAnimation(animationRotateCenter);}}
            if (wash == 2) {wash2crash = 1;if (wash2.getVisibility() == View.VISIBLE) {imageDoor2.setVisibility(View.VISIBLE);imageDoor2.startAnimation(animationRotateCenter);}}
            if (wash == 3) {wash3crash = 1;if (wash3.getVisibility() == View.VISIBLE) {imageDoor3.setVisibility(View.VISIBLE);imageDoor3.startAnimation(animationRotateCenter);}}
            if (wash == 4) {wash4crash = 1;if (wash4.getVisibility() == View.VISIBLE) {imageDoor4.setVisibility(View.VISIBLE);imageDoor4.startAnimation(animationRotateCenter);}}
            if (wash == 5) {wash5crash = 1;if (wash5.getVisibility() == View.VISIBLE) {imageDoor5.setVisibility(View.VISIBLE);imageDoor5.startAnimation(animationRotateCenter);}}
            if (wash == 6) {wash6crash = 1;if (wash6.getVisibility() == View.VISIBLE) {imageDoor6.setVisibility(View.VISIBLE);imageDoor6.startAnimation(animationRotateCenter);}}
            if (wash == 7) {wash7crash = 1;if (wash7.getVisibility() == View.VISIBLE) {imageDoor7.setVisibility(View.VISIBLE);imageDoor7.startAnimation(animationRotateCenter);}}
            if (wash == 8) {wash8crash = 1;if (wash8.getVisibility() == View.VISIBLE) {imageDoor8.setVisibility(View.VISIBLE);imageDoor8.startAnimation(animationRotateCenter);}}
            if (wash == 9) {wash9crash = 1;if (wash9.getVisibility() == View.VISIBLE) {imageDoor9.setVisibility(View.VISIBLE);imageDoor9.startAnimation(animationRotateCenter);}}
            if (wash ==10) {wash10crash =1;if (wash10.getVisibility()== View.VISIBLE) {imageDoor10.setVisibility(View.VISIBLE);imageDoor10.startAnimation(animationRotateCenter);}}
            if (wash ==11) {wash11crash =1;if (wash11.getVisibility()== View.VISIBLE) {imageDoor11.setVisibility(View.VISIBLE);imageDoor11.startAnimation(animationRotateCenter);}}
            if (wash ==12) {wash12crash =1;if (wash12.getVisibility()== View.VISIBLE) {imageDoor12.setVisibility(View.VISIBLE);imageDoor12.startAnimation(animationRotateCenter);}}}}

    private void layout_gone() {
        layout_number_selected.setVisibility(View.GONE);
        layout_prepair_start.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        layout_settings.setVisibility(View.GONE);
        layout_home.setVisibility(View.GONE);
        layout_action_bar.setVisibility(View.GONE);//  бар
        layout_block.setVisibility(View.GONE);
    }
    /**
     * The Handler that gets information back from the BluetoothChatService////////////////////////////
     */
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            Log.d(TAG, "определили STATE_CONNECTED и запускаем arduinoLife и go_home");
                            state_connected=1;
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            arduinoLife();
                            if(kluchik==1){kluchik=0;
                                mHideSystemBar = 1;
                                Build.VERSION_CODES vc = new Build.VERSION_CODES();
                                Build.VERSION vr = new Build.VERSION();
                                if (vr.SDK_INT > vc.KITKAT) {
                                    deviceAdmin = new ComponentName(getActivity(), AdminReceiver.class);
                                    dpm = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
                                    if (dpm.isDeviceOwnerApp(getActivity().getPackageName())) {
                                        dpm.setLockTaskPackages(deviceAdmin, new String[]{getActivity().getPackageName()});
                                    }
                                    getActivity().startLockTask();
                                    Common.becomeHomeActivity(getActivity());
                                }
                                go_home();
                            }else{
                            go_home();}
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
                            display_settings_activity=0;
                            state_connecting=1;
                            layout_gone();
                            if(statusStr.contentEquals("1")){ // питание от сети
                                Log.d(TAG, "определили STATE_CONNECTING и ставим заставку load_laundry");
                                if(eng==1){layout_bg.setBackgroundResource(R.drawable.load_laundry_eng);}else{
                                    layout_bg.setBackgroundResource(R.drawable.load_laundry);}
                                // Toast.makeText(getActivity(), "ПОЖАЛУЙСТА, ПОДОЖДИТЕ.", Toast.LENGTH_SHORT).show();
                            }
                            else { // питание от батареи
                                Log.d(TAG, "определили STATE_CONNECTING и ставим заставку error_laundry");
                                if(eng==1){layout_bg.setBackgroundResource(R.drawable.error_laundry_eng);}else{
                                    layout_bg.setBackgroundResource(R.drawable.error_laundry); }}
                            break;
                        //case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
                            Log.d(TAG, "определили STATE_NONE");
                            if(state_connected==1){state_connected=0;
                               // arduinoLife();
                                reconnect_Device();
                                checkBT1();
                                Log.d(TAG, "определили STATE_NONE и до этого был STATE_CONNECTED и запускаем arduinoLife и go_home");
                                //     go_home();
                            }else{
                                if (state_connecting==1){state_connecting=0;
                                    // Toast.makeText(getActivity(), "STATE_NONE", Toast.LENGTH_SHORT).show();
                                    layout_gone();
                                    if(statusStr.contentEquals("1")){ // питание от сети
                                        Log.d(TAG, "определили STATE_NONE и до этого был STATE_CONNECTING и запускаем ЗАСТАВКУ load_laundry");
                                        if (boot_logo==0){boot_logo=1;
                                            layout_bg.setBackgroundResource(R.drawable.hello_laundry);}else
                                        if (boot_logo==1){boot_logo=0;
                                            if(eng==1){layout_bg.setBackgroundResource(R.drawable.load_laundry_eng);}else{
                                                layout_bg.setBackgroundResource(R.drawable.load_laundry);}}
                                        if (display_settings_activity != 1){
                                            Log.d(TAG, "находимся в STATE_NONE и не в режиме администратора ");
                                            if((state_unable==1)||(state_lost==1)){state_unable=0;state_lost=0;
                                                //   Toast.makeText(getActivity(), "до этого был unable или lost, поэтому надо попробовать reconnect...", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "находимся в STATE_NONE и до этого был unable или lost, поэтому reconnect_Device");
                                                reconnect_Device();
                                            }}
                                    }
                                    else { // питание от батареи
                                        Log.d(TAG, "определили STATE_NONE и до этого был STATE_CONNECTING, но питание от батареи и запускаем ЗАСТАВКУ error_laundry");
                                        if(eng==1){layout_bg.setBackgroundResource(R.drawable.error_laundry_eng);}else{
                                            layout_bg.setBackgroundResource(R.drawable.error_laundry); }
                                    }}else{
                                    Log.d(TAG, "определили STATE_NONE и до этого был ХРЕН_ЗНАЕТ, поэтому checkBT1");
                                    checkBT1();
                                }}
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf=(byte[])msg.obj;String writeMessage = new String(writeBuf);
                    mConversationArrayAdapter.add("Send to laundry: ["+timeformat.format(new Date())+"] "+writeMessage);
                    break;
                case Constants.MESSAGE_READ:
                    final Animation animationRotateCenter=AnimationUtils.loadAnimation(activity,R.anim.rotate);
                    final String readMessage=(String)msg.obj;if(readMessage!=null){
                    mConversationArrayAdapter.add(mConnectedDeviceName+" ["+timeformat.format(new Date())+"] "+readMessage);
                    mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
                    float vnes=0;
                    if(readMessage.contains("c")){ // ****************** КУПЮРНИК ************************
                        if(valuta==1){vnes=money_vneseno+kup_rub_impuls;money_kup.setText(""+vnes);// РУБЛИ
                            money_vneseno=vnes;money=money+kup_rub_impuls;SharedPreferences.Editor editor=mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_rub_impuls +" RUB");}
                        else if(valuta==2){vnes=money_vneseno+kup_eur_impuls5;money_kup.setText(""+vnes);//ЕВРО
                            money_vneseno=vnes;money=money+kup_eur_impuls5;SharedPreferences.Editor editor=mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_eur_impuls5+" EUR");}
                        else if(valuta==3){vnes=money_vneseno+kup_byr_impuls1000;money_kup.setText(""+vnes);//БЕЛОРУС
                            money_vneseno=vnes;money=money+kup_byr_impuls1000;SharedPreferences.Editor editor=mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY, money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_byr_impuls1000+" BYR");}
                        else if(valuta==4){vnes=money_vneseno+kup_kzt_impuls100;money_kup.setText(""+vnes);//ТЕНГЕ
                            money_vneseno=vnes;money=money+kup_kzt_impuls100;SharedPreferences.Editor editor=mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+kup_kzt_impuls100+" KZT");}}
                    if(readMessage.contains("d")){ // ****************** МОНЕТНИК ************************
                        if(valuta==1){vnes=money_vneseno+mon_rub_impuls10;money_kup.setText(""+vnes);
                            money_vneseno=vnes;money=money+mon_rub_impuls10;SharedPreferences.Editor editor=mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+mon_rub_impuls10+" RUB");}
                        else if(valuta==2){vnes=money_vneseno+mon_eur_impuls1;money_kup.setText(""+vnes);
                            money_vneseno=vnes;money=money+mon_eur_impuls1;SharedPreferences.Editor editor=mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+mon_eur_impuls1+" EUR");}
                        else if(valuta==3){

                           // vnes=money_vneseno+(mon_byr_impuls1000/pur);money_kup.setText(""+vnes);
                            String bla = ""+money_vneseno; double blabla = Double.parseDouble(bla);

                            blabla+= mon_byr_impuls1000;
                           // money_kup.setText(""+blabla);
                           // money_kup.setVisibility(View.GONE);money_kup.setVisibility(View.VISIBLE);
                           // money_kup.setText("");

                            String vnesee = ""+blabla+"0";
                           // String mon_byr = ""+mon_byr_impuls1000;
                            //String.format(vnesee, "000.0");

                            money_vneseno=Float.parseFloat(vnesee);
                            String mmoonn = ""+money+"0";
                           // float vneseee = Float.parseFloat(mon_byr);
                            double vneseee = Double.parseDouble(mmoonn);
                            vneseee+=mon_byr_impuls1000;mmoonn = ""+vneseee+"0";
                            money=Float.parseFloat(mmoonn);

                            SharedPreferences.Editor editor = mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+mon_byr_impuls1000+" BYR");
                            money_kup.setText(""+money_vneseno+"0");}
                        else if(valuta==4){vnes=money_vneseno+mon_kzt_impuls100;money_kup.setText(""+vnes);
                            money_vneseno=vnes;money=money+mon_kzt_impuls100;SharedPreferences.Editor editor = mSettings.edit();
                            editor.putFloat(PREF_MONEYVNESENO,money_vneseno);editor.putFloat(PREF_MONEY,money);
                            editor.apply();mConversationArrayAdapter.add("Done:  +"+mon_kzt_impuls100+" KZT");}}

                    if(readMessage.contains("a")){
                        if((display_settings_activity == 1)||(charge_activity == 1)){charge_activity =0;layout_gone();go_home();}else{
                            flipper_goto_numbers();mConversationArrayAdapter.add("Done: laundryView_OK");}}//}

                    if(readMessage.contains("A")){start_engine(0,0);mConversationArrayAdapter.add("Done: washerStart_OK");}
                    if(readMessage.contains("b")){display_settings_load();mConversationArrayAdapter.add("Done: adminButton_OK");}
                   // if(readMessage.contains("B")){
                       // dozator_true=0;
                       // go_home();
                        // arduinoLife();

                       // Log.d(TAG, "получили от АРДУИНО букву В и запускаем arduinoLife");
                   // }  // don't reset arduino
                   // if(readMessage.contains("D")){
                     //   dozator_true=1;
                     //   go_home();
                        // arduinoLife();
                     //   Log.d(TAG, "получили от АРДУИНО букву D и запускаем режим дозатора");
                   // }// don't reset arduino
                  //  if(readMessage.contains("0")){ property_true=0; go_home();}

                    //1-занята и т.д.
                    if(readMessage.contains("g")){mConversationArrayAdapter.add("Done: washer1busy_OK");
                        if(countwash>=2){door2_view();}if(checkdoor==1){wash1crash=1;if(wash1.getVisibility()==View.VISIBLE){
                            imageDoor1.setVisibility(View.VISIBLE);imageDoor1.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("G")){mConversationArrayAdapter.add("Done: washer2busy_OK");
                        if(countwash>=3){door3_view();}if(checkdoor==1){wash2crash=1;if(wash2.getVisibility()==View.VISIBLE){
                            imageDoor2.setVisibility(View.VISIBLE);imageDoor2.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("j")){mConversationArrayAdapter.add("Done: washer3busy_OK");
                        if(countwash>=4){door4_view();}if(checkdoor==1){wash3crash=1;if(wash3.getVisibility()==View.VISIBLE){
                            imageDoor3.setVisibility(View.VISIBLE);imageDoor3.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("J")){mConversationArrayAdapter.add("Done: washer4busy_OK");
                        if(countwash>=5){door5_view();}if(checkdoor==1){wash4crash=1;if(wash4.getVisibility()==View.VISIBLE){
                            imageDoor4.setVisibility(View.VISIBLE);imageDoor4.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("o")){mConversationArrayAdapter.add("Done: washer5busy_OK");
                        if(countwash>=6){door6_view();}if(checkdoor==1){wash5crash=1;if(wash5.getVisibility()==View.VISIBLE){
                            imageDoor5.setVisibility(View.VISIBLE);imageDoor5.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("O")){mConversationArrayAdapter.add("Done: washer6busy_OK");
                        if(countwash>=7){door7_view();}if(checkdoor==1){wash6crash=1;if(wash6.getVisibility()==View.VISIBLE){
                            imageDoor6.setVisibility(View.VISIBLE);imageDoor6.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("t")){mConversationArrayAdapter.add("Done: washer7busy_OK");
                        if(countwash>=8){door8_view();}if(checkdoor==1){wash7crash=1;if(wash7.getVisibility()==View.VISIBLE){
                            imageDoor7.setVisibility(View.VISIBLE);imageDoor7.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("T")){mConversationArrayAdapter.add("Done: washer8busy_OK");
                        if(countwash>=9){door9_view();}if(checkdoor==1){wash8crash=1;if(wash8.getVisibility()==View.VISIBLE){
                            imageDoor8.setVisibility(View.VISIBLE);imageDoor8.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("y")){mConversationArrayAdapter.add("Done: washer9busy_OK");
                        if(countwash>=10){door10_view();}if(checkdoor==1){wash9crash=1;if(wash9.getVisibility()==View.VISIBLE){
                            imageDoor9.setVisibility(View.VISIBLE);imageDoor9.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("Y")){mConversationArrayAdapter.add("Done: washer10busy_OK");
                        if(countwash>=11){door11_view();}if(checkdoor==1){wash10crash=1;if(wash10.getVisibility()==View.VISIBLE){
                            imageDoor10.setVisibility(View.VISIBLE);imageDoor10.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("7")){mConversationArrayAdapter.add("Done: washer11busy_OK");//==11 BUSY==
                        if(countwash>=12){door12_view();}if(checkdoor==1){wash11crash=1;if(wash11.getVisibility()==View.VISIBLE){
                            imageDoor11.setVisibility(View.VISIBLE);imageDoor11.startAnimation(animationRotateCenter);}}}
                    if(readMessage.contains("!")){mConversationArrayAdapter.add("Done: washer12busy_OK");//==12 BUSY==
                        if(checkdoor==1){wash12crash=1;if(wash12.getVisibility()==View.VISIBLE){
                            imageDoor12.setVisibility(View.VISIBLE);imageDoor12.startAnimation(animationRotateCenter);}}}
                    // машинка 1 свободна и т.д.
                    if(readMessage.contains("F")){mSMSmanager(notice_wash1,smsnumber_client1,1,"notice_wash1_key");}
                    if(readMessage.contains("I")){mSMSmanager(notice_wash2,smsnumber_client2,2,"notice_wash2_key");}
                    if(readMessage.contains("L")){mSMSmanager(notice_wash3,smsnumber_client3,3,"notice_wash3_key");}
                    if(readMessage.contains("N")){mSMSmanager(notice_wash4,smsnumber_client4,4,"notice_wash4_key");}
                    if(readMessage.contains("Q")){mSMSmanager(notice_wash5,smsnumber_client5,5,"notice_wash5_key");}
                    if(readMessage.contains("S")){mSMSmanager(notice_wash6,smsnumber_client6,6,"notice_wash6_key");}
                    if(readMessage.contains("V")){mSMSmanager(notice_wash7,smsnumber_client7,7,"notice_wash7_key");}
                    if(readMessage.contains("X")){mSMSmanager(notice_wash8,smsnumber_client8,8,"notice_wash8_key");}
                    if(readMessage.contains("2")){mSMSmanager(notice_wash9,smsnumber_client9,9,"notice_wash9_key");}
                    if(readMessage.contains("6")){mSMSmanager(notice_wash10,smsnumber_client10,10,"notice_wash10_key");}
                    if(readMessage.contains("#")){mSMSmanager(notice_wash11,smsnumber_client11,11,"notice_wash11_key");}
                    if(readMessage.contains("*")){mSMSmanager(notice_wash12,smsnumber_client12,12,"notice_wash12_key");}
                    //время с начала стирки, обновляется каждую мин
                    if(readMessage.contains("f")){notice_timeError(1,washer1timer++);}
                    if(readMessage.contains("i")){notice_timeError(2,washer2timer++);}
                    if(readMessage.contains("l")){notice_timeError(3,washer3timer++);}
                    if(readMessage.contains("n")){notice_timeError(4,washer4timer++);}
                    if(readMessage.contains("q")){notice_timeError(5,washer5timer++);}
                    if(readMessage.contains("s")){notice_timeError(6,washer6timer++);}
                    if(readMessage.contains("v")){notice_timeError(7,washer7timer++);}
                    if(readMessage.contains("x")){notice_timeError(8,washer8timer++);}
                    if(readMessage.contains("1")){notice_timeError(9,washer9timer++);}
                    if(readMessage.contains("5")){notice_timeError(10,washer10timer++);}
                    if(readMessage.contains("$")){notice_timeError(11,washer11timer++);}
                    if(readMessage.contains("&")){notice_timeError(12,washer12timer++);}
                    //Получили ответ разрешающий старт анимации
                    if(readMessage.contains("e")){mConversationArrayAdapter.add("Status: CHECK_WASHER_CONNECT_OK");animation_start();}
                    if(readMessage.contains("E")){mConversationArrayAdapter.add("Status: CHECK_BUTTONS_RELAY");animation_start();ok1();}
                    if(readMessage.contains("h")){mConversationArrayAdapter.add("Status: BUTTONS_OK");status1();}
                    if(readMessage.contains("H")){mConversationArrayAdapter.add("Status: CHECK_CLOSE_DOOR");ok2();}
                    if(readMessage.contains("k")){mConversationArrayAdapter.add("Status: CLOSE_DOOR_OK");status2();}
                    if(readMessage.contains("K")){mConversationArrayAdapter.add("Status: CHECK_WASHER_WATER_OK");ok3();}
                    if(readMessage.contains("m")){mConversationArrayAdapter.add("Status: CHECK_WASHER_START_OK");status3();animation_start_clother();}
                    if(readMessage.contains("M")){mConversationArrayAdapter.add("Status: CHECK_ENGINE");animation_start_clother();}
                    if(readMessage.contains("p")){mConversationArrayAdapter.add("Status: ENGINE_OK");go_home();}
                }

                    break;//конец входящих сообщений ------------------------------------------------------
                case Constants.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName=msg.getData().getString(Constants.DEVICE_NAME);
                    break;
                case Constants.MESSAGE_TOAST:
                    if(null!=activity){
                        if(msg.getData().getString(Constants.TOAST)=="Unable to connect laundry"){
                            state_unable=1; imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
                            Log.d(TAG, "получили тост Unable");
                            mChatService.stop();
                        }
                        if(msg.getData().getString(Constants.TOAST)=="Laundry connection was lost"){
                            state_lost=1; imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
                            Log.d(TAG, "получили тост lost");
                            if (display_settings_activity == 1){}else{reconnect_counter=0;
                                // Toast.makeText(getActivity(), "lost!", Toast.LENGTH_SHORT).show();
                                reconnect_Device();
                            }
                        }}
                    break;

            }
        }
    }; // end of Handler -----------------------------------------------------------------------------


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "получили onActivityResult from yandex");
                    float num1 = Float.parseFloat(money_kup.getText().toString());
                    float num2 = result_credit;
                    float result = num1 + num2;
                    Toast.makeText(getActivity(), "ОПЛАТА " + result_credit + " ПОЛУЧЕНА С КАРТЫ", Toast.LENGTH_LONG).show();
                    money_kup.setText("" + result);

                    //  int num3 = Integer.parseInt(moneypole.getText().toString());

                    float num3 =Float.parseFloat(money_kup.getText().toString());
                    money = num3 + num2;
                    money_kup.setText("" + money);
                    money_vneseno = result;
                    // ?????????? ??????
                    SharedPreferences.Editor editor = mSettings.edit();
                    editor.putFloat(PREF_MONEY, money);
                    editor.putFloat(PREF_MONEYVNESENO, money_vneseno);
                    editor.apply();
                    result_credit = 0;
                } else { result_credit = 0;
                }
                break;
            case REQUEST_CONNECT_DEVICE_SECURE:
                if (resultCode == Activity.RESULT_OK) {
                   // Log.d(TAG, "получили onActivityResult и запускаем connectDevice(data, true)");

                    Build.VERSION_CODES vc = new Build.VERSION_CODES();
                    Build.VERSION vr = new Build.VERSION();
                    if (vr.SDK_INT <= vc.KITKAT) {
                       // mHideSystemBar=0;showSystemBar();
                    } else {
                       mHideSystemBar = 0; getActivity().stopLockTask();
                    }
                    kluchik=1;
                    connectDevice(data, true);
                  // BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac_address);
                  // mChatService.connect(device, true);
                }break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "получили onActivityResult и запускаем connectDevice(data, true)");
                    //connectDevice(data, false);
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac_address);
                    mChatService.connect(device, false);
                }break;
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Log.d(TAG, "получили onActivityResult-REQUEST_ENABLE_BT и запускаем setupChat()");
                    setupChat();
                } else {Log.d(TAG, "BT not enabled");
                    if (!mBluetoothAdapter.isEnabled()) {mBluetoothAdapter.enable();setupChat();}
                }
        }
    }

    /**
     * Establish reconnection with save divice
     */
    private void reconnect_Device() {
        imageViewScaleHome.setVisibility(View.INVISIBLE);  imageViewScaleHome.clearAnimation();
        //  mMediaPlayer.stop();
        if(reconnect_counter>=5){
            reconnect_counter=0;
            //mBluetoothAdapter.enable();
            Log.d(TAG, "зашли в reconnect_Device() и отдохнем");
            if (mBluetoothAdapter.isEnabled()) {mBluetoothAdapter.disable();}
            layout_gone();layout_bg.setVisibility(View.VISIBLE);
            layout_bg.setBackgroundResource(R.drawable.error_power2);
        } else {
            //   mBluetoothAdapter.enable();
            // Log.d(TAG, "зашли в reconnect_Device()");
            if (!mBluetoothAdapter.isEnabled()) {

                mBluetoothAdapter.enable();
                if(vkl_bt!=1){ Log.d(TAG, "зашли в reconnect_Device() и вкл блютус и еще раз запустим reconnect_Device()");
                }else{
                    Log.d(TAG, "ждем пока вкл блютус...");}
                vkl_bt=1;
                reconnect_Device();
                //     reconnect_Device();}}
                // setupChat();
            } else {
                vkl_bt=0;
                if (mac_address.length() > 0) {counter_bluetooth_state++;//display_settings_activity = 0;
                    reconnect_counter++;
                    Log.d(TAG, "зашли в reconnect_Device() и вкл mChatService.start() и запустим mChatService.connect(device, true)"+reconnect_counter);
                    // mChatService.start();
                    app_start++;
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac_address);
                    mChatService.connect(device, true);
                } else {
                    Log.d(TAG, "зашли в reconnect_Device() и запустим DeviceListActivity.class и app_start=1");
                    app_start=1;
                    reconnect_counter++;
                    // display_settings_activity = 1;
                    //Toast.makeText(getActivity(), "Мак-адрес задан некорректно!", Toast.LENGTH_SHORT).show();
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                }
            }
        }
    }

    /**
     * Establish connection with other divice
     *
     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        app_start=3;
        //  Intent intent = new Intent(getActivity(),Main2Activity.class);startActivity(intent);
        //  String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        mac_address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        mSettings=getActivity().getSharedPreferences(PREF,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(PREF_MACADDRESS, mac_address);
        editor.apply();
        Log.d(TAG, "зашли в connectDevice и занесли мак в память и отключили блютус"+mac_address+"app_start++"+app_start);
        // if (display_settings_activity == 1){
        reconnect_counter=0;
        // mChatService.start();
        mBluetoothAdapter.disable();
        /// Log.d(TAG, "отключили блютус");
        reconnect_Device();

       /* if (mac_address.length() > 0) {
            Log.d(TAG, "if (address.length() > 0");
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac_address);
            mChatService.connect(device, secure);
        }*/
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {// Launch to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {// Launch to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {// Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }



    private void startPaymentActivityForResult() {
        float num1 = Float.parseFloat(money_kup.getText().toString());
        float num2 = Float.parseFloat(money_tarif.getText().toString());
        float result = num2 - num1;
        Toast.makeText(getActivity(), "Создается транзакция на сумму: " + result + "руб.", Toast.LENGTH_LONG).show();

        result_credit = result;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putFloat(PREF_RESULTCREDIT, result_credit);
        editor.apply();
        PaymentParams P2pTransferParams = new P2pTransferParams.Builder(numbercardyandex).setAmount(new BigDecimal(result_credit)).create();

        //PaymentParams phoneParams = PhoneParams.newInstance("79012345678", new BigDecimal(100.0))
        Intent intent = PaymentActivity.getBuilder(getActivity())
                .setPaymentParams(P2pTransferParams)
                .setClientId(CLIENT_ID)
                .setHost(HOST)
                .build();
        startActivityForResult(intent, REQUEST_CODE);
    }




    private void hideKeyboardOff() { //Скрыть мою клавиатуру
        //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.hideSoftInputFromWindow(setMacAddress.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void showKeepScreenOn() {
    } //getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    private void showKeepScreenOff() {
    } //onResume();//getWindow().addFlags((int) WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_OFF);

    private void showSystemBar() {
        String commandStr = "am startservice -n com.android.systemui/.SystemUIService";
        runAsRoot(commandStr);
    }
    private void hideSystemBar() {
        try {
            //REQUIRES ROOT
            Build.VERSION_CODES vc = new Build.VERSION_CODES();
            Build.VERSION vr = new Build.VERSION();
            String ProcID = "79"; //HONEYCOMB AND OLDER

            //v.RELEASE  //4.0.3
            if (vr.SDK_INT >= vc.ICE_CREAM_SANDWICH) {
                ProcID = "42"; //ICS AND NEWER
            }

            String commandStr = "service call activity " +
                    ProcID + " s16 com.android.systemui";
            runAsRoot(commandStr);
        } catch (Exception e) {
            // something went wrong, deal with it here
        }
    }
    private void runAsRoot(String commandStr) {
        try {
            CommandCapture command = new CommandCapture(0, commandStr);
            RootTools.getShell(true).add(command).waitForFinish();
        } catch (Exception e) {
            // something went wrong, deal with it here
        }
    }



}