
package ru.laundromat.washer.bluetoothchat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.laundromat.washer.common.logger.Log;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment2 extends Fragment {
    private static final String TAG = "BluetoothChatFragment2";
/*
// ТАЙМЕР --------------
int count= 10;

    TextView texto_count;
    Timer t;
    View v;

    TimerTask timer= new TimerTask(){
        @Override
        public void run() {
            FragmentActivity activity = getActivity();
            activity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    count--;
                    if(count >= 0) {
                        texto_count.setText(Integer.toString(count));
                    }
                }
            });

            if (count <= 0) {
               // t.cancel();
                count = 10;
            }
        }

    };

// ТАЙМЕР -------------- дальше в onCreateView ---------------------
*/


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mSendButton;

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

    private LinearLayout machine1, machine2, machine3, machine4, machine5, machine6,
            machine7, machine8, machine9, machine10, machine11, machine12,
            machine1_settings, machine2_settings, machine3_settings, machine4_settings, machine5_settings,
            machine6_settings, machine7_settings, machine8_settings, machine9_settings, machine10_settings,
            machine11_settings, machine12_settings;

    private ListView StatisticWash1, StatisticWash2, StatisticWash3,
            StatisticWash4, StatisticWash5, StatisticWash6,
            StatisticWash7, StatisticWash8, StatisticWash9,
            StatisticWash10, StatisticWash11, StatisticWash12;

    private ToggleButton btn_settings_bluetooth_adapter;
    private HorizontalScrollView horizontalScrollView, horizontalScrollView2;

    private ImageButton img_slide_back, img_slide,
            mButton, // button Ok
    // smart keyboard
    btn_cancel,
            btn_numbers_select,
            btn_keyboard_choose_prog, btn_keyboard_sms,
            btn_keyboard_temp, btn_keyboard_pay, btn_keyboard_creditcard;

    private Button
            start, // button Smart keyboard
    // numbers selected
    wash1_go, wash2_go, wash3_go, wash4_go, wash5_go, wash6_go,
            wash7_go, wash8_go, wash9_go, wash10_go, wash11_go, wash12_go,
    // choose mode
    choose1mode, choose2mode, choose3mode,
            choose4mode, choose5mode, choose6mode,
    // меню выбора подменю
    btn1_settings_choose, btn2_settings_choose, btn3_settings_choose,
            btn4_settings_choose, btn5_settings_choose, btn6_settings_choose,
    // WASHER SETTINGS
    num1_settings, num2_settings, num3_settings, num4_settings, num5_settings,
            num6_settings, num7_settings, num8_settings, num9_settings, num10_settings,
            num11_settings, num12_settings,
    // menu stat
    btn_settings_stat_inkass,
    // меню тарифы
    btn_settings_tarif_base, btn_settings_tarif_yandex,
            btn_settings_tarif_valuta, btn_settings_tarif_wash,
            btn_settings_tarif_mode, btn_settings_tarif_iron,
            btn_settings_tarif_sber, btn_settings_tarif_qiwi,
            btn_settings_mode_name,
    //меню bluetooth
    btn_settings_bluetooth_base, btn_settings_bluetooth_search,
            btn_settings_bluetooth_list,
    // меню утюги
    btn_settings_iron_base,
    // меню смс
    btn_settings_sms_base, btn_settings_sms_director,
            btn_settings_sms_manager, btn_settings_sms_otchet_admin,
            btn_settings_sms_otchet_start_washer, btn_settings_sms_otchet_client,
            btn_settings_sms_read_command, btn_settings_sms_otchet_power,
    // меню стиральные машины
    btn_settings_washer_base,
    // menu system
    btn_settings_system_base, btn_settings_system_log_view,
            btn_settings_system_com, btn_settings_system_destroy_upgrade,
            btn_settings_system_licence, btn_settings_system_blockcalls,
            btn_settings_system_optionsplus, btn_settings_system_fullscreen,
            btn_settings_system_autostart, btn_settings_system_keyboard,
            btn_settings_system_display, btn_settings_system_autoreboot,
            btn_settings_system_camera, btn_settings_system_battary,
    // кнопка закрывает системный лог не удалять
    btn_close_layout_system_log,
            btn_close_layout_com,
    // left side menu
    // меню только кнопки навигации
    btn_settings_exit, btn_settings_washer, btn_settings_iron,
            btn_settings_sms, btn_settings_tarif, btn_settings_stat,
            btn_settings_system, btn_settings_save, btn_settings_bluetooth;


    private TextView wash_number, wash_mode, money_tarif, money_kup,
    //  bluetooth_status,
    statistics_txt, valuta_statistics_txt,
            textMsg1,  // смс
    // change title of numbers selected
    txt_number_selected_title,
    // chande title name of mode in prepair to start screen
    txt_layout_prepair_wash1,
    // chande title name of valuta in prepair to start screen
    money_tarif_valuta, txt_footer_prepair,
    // change visibility of left side menu
    txt_opacity_menu1, txt_opacity_menu2, txt_opacity_menu3,
            txt_opacity_menu4, txt_opacity_menu5, txt_opacity_menu6,
            txt_opacity_menu7, txt_opacity_menu8;


    private RelativeLayout  layout_bt_not,
    // change visibility for disconnect or search
    layout_bt_search,
    // change visibility or background for home screen
    layout_home,
    // chenge visibility or background for numbers selected screen
    layout_number_selected, layout_washer,
    // change visibility of how much of machines!!!
    layout_wash1, layout_wash2, layout_wash3, layout_wash4,
            layout_wash5, layout_wash6, layout_wash7, layout_wash8,
            layout_wash9, layout_wash10, layout_wash11, layout_wash12,
    // change visibility of choose menu
    layout_choose_mode,
    // change visibility or background of prepair to start
    layout_prepair_start, prepair_background_dry1, prepair_background_remont1,
            prepair_background1, prepair_background2, prepair_background3,
            prepair_background_mode1, prepair_background_mode2,
            prepair_background_mode3, prepair_background_mode4,
            prepair_background_mode5, prepair_background_mode6,
            layout1, // fields allvays views
            layout_smart_keyboard, // keyboard for prepair to start Allways view
    // change visibility or background for anim start screen *& input image adapter for local animation!!!
    layout_start_anim,
    // ACTION BAR
    layout_action_bar,
    // SETTINGS MODE -----------------------------------------------
    // change visibility or background of main menu
    layout_settings,
            layout_settings_washer,     // change visibility submenu
            layout_settings_iron,       // change visibility submenu
            layout_settings_tarif,      // change visibility submenu
            layout_settings_bluetooth,  // change visibility submenu
            layout_settings_sms,        // change visibility submenu
            layout_settings_system,     // change visibility submenu
    // sub sub menu
    layout_log,        // my log;
            layout_system_log, // blutooth chat log
            layout_settings_stat,      // change visibility submenu statistics
    // wash number for statistics & washer
    layout_settings_wash_number,
    // change visibility of how much of machines!!!!
    layout_wash1_settings, layout_wash2_settings,
            layout_wash3_settings, layout_wash4_settings,
            layout_wash5_settings, layout_wash6_settings,
            layout_wash7_settings, layout_wash8_settings,
            layout_wash9_settings, layout_wash10_settings,
            layout_wash11_settings, layout_wash12_settings,
    // actions bar left side
    layout_settings_action_bar_leftmenu, layout_settings_buttons,
    // actions bar for all settings
    layout_settings_action_bar,

    // disabled
    layout_settings_preview, relativeLayout;


    private ImageView img_fullscreen_search_bluetooth;   // search bluetooth connect
    private ImageView iron_view1;
    private ImageView iron_view2;
    private ImageView iron_view3;       // iron image in settings
    private ImageView wash1;
    private ImageView wash2;
    private ImageView wash3;
    private ImageView wash4;
    private ImageView wash5;
    private ImageView wash6;
    private ImageView wash7;
    private ImageView wash8;
    private ImageView wash9;
    private ImageView wash10;
    private ImageView wash11;
    private ImageView wash12;
    private ImageView dry1;
    private ImageView dry2;
    private ImageView dry3;
    private ImageView dry4;
    private ImageView dry5;
    private ImageView dry6;
    private ImageView dry7;
    private ImageView dry8;
    private ImageView dry9;
    private ImageView dry10;
    private ImageView dry11;
    private ImageView dry12;
    private ImageView connecting_animation;
    private ImageView status1;
    private ImageView status2;
    private ImageView status3;
    private ImageView ok1;
    private ImageView ok2;
    private ImageView ok3;
    private ImageView start_anim_washer;
    private ImageView programs_start_clother;
    private ImageView imageDoor1;
    private ImageView imageDoor2;
    private ImageView imageDoor3;
    private ImageView imageDoor4;
    private ImageView imageDoor5;
    private ImageView imageDoor6;
    private ImageView imageDoor7;
    private ImageView imageDoor8;
    private ImageView imageDoor9;
    private ImageView imageDoor10;
    private ImageView imageDoor11;
    private ImageView imageDoor12;

    private ImageView// change status bluetooth icon in actions bar
            img_action_bar_icon_laundry_connected;
    private ImageView img_action_bar_icon_lost_connected;
    private ImageView img_action_bar_icon_not_connected;
    private ImageView// change to invisible for one & two screen
            img_action_bar_icon_wash;
    private ImageView img_action_bar_icon_vneseno;
    private ImageView img_action_bar_icon_electro;
    private ImageView img_action_bar_icon_battary;


    private ImageView// относится к action bar и left menu
            img_settings_action_bar_icon_view_leftmenu;
    private ImageView img_settings_action_bar_icon_smsstatus;
    private ImageView img_settings_action_bar_icon_valuta;
    private ImageView img_settings_action_bar_icon_allcashsum;
    private ImageView img_settings_action_bar_icon_electro;
    private ImageView img_settings_action_bar_icon_battary;
    private ImageView img_settings_show_leftmenu;

    private ImageView wash1_settings;
    private ImageView wash2_settings;
    private ImageView wash3_settings;
    private ImageView wash4_settings;
    private ImageView wash5_settings;
    private ImageView wash6_settings;
    private ImageView wash7_settings;
    private ImageView wash8_settings;
    private ImageView wash9_settings;
    private ImageView wash10_settings;
    private ImageView wash11_settings;
    private ImageView wash12_settings;

    private ImageView dry1_settings;
    private ImageView dry2_settings;
    private ImageView dry3_settings;
    private ImageView dry4_settings;
    private ImageView dry5_settings;
    private ImageView dry6_settings;
    private ImageView dry7_settings;
    private ImageView dry8_settings;
    private ImageView dry9_settings;
    private ImageView dry10_settings;
    private ImageView dry11_settings;
    private ImageView dry12_settings;


    /**
     * Array adapter for the conversation thread
     */

    private ArrayAdapter<String> statisticWash1ArrayAdapter;
    private ArrayAdapter<String> statisticWash2ArrayAdapter;
    private ArrayAdapter<String> statisticWash3ArrayAdapter;
    private ArrayAdapter<String> statisticWash4ArrayAdapter;
    private ArrayAdapter<String> statisticWash5ArrayAdapter;
    private ArrayAdapter<String> statisticWash6ArrayAdapter;
    private ArrayAdapter<String> statisticWash7ArrayAdapter;
    private ArrayAdapter<String> statisticWash8ArrayAdapter;
    private ArrayAdapter<String> statisticWash9ArrayAdapter;
    private ArrayAdapter<String> statisticWash10ArrayAdapter;
    private ArrayAdapter<String> statisticWash11ArrayAdapter;
    private ArrayAdapter<String> statisticWash12ArrayAdapter;


    private int washertimer = 0; // записываем время выбранной машины
    private int washer1timer = 0;
    private int washer2timer = 0;
    private int washer3timer = 0;
    private int washer4timer = 0;
    private int washer5timer = 0;
    private int washer6timer = 0;
    private int washer7timer = 0;
    private int washer8timer = 0;
    private int washer9timer = 0;
    private int washer10timer = 0;
    private int washer11timer = 0;
    private int washer12timer = 0;

    private int counter_bluetooth_state = 0; // если нет соединения открыть окно поиска блютус
    private int mButtonCounter = 0; // если не приходит ответ от ардуино, но соединение есть

    private int
            wash1statcounter_mode1, wash2statcounter_mode1, wash3statcounter_mode1, wash4statcounter_mode1, wash5statcounter_mode1, wash6statcounter_mode1,
            wash1statcounter_mode2, wash2statcounter_mode2, wash3statcounter_mode2, wash4statcounter_mode2, wash5statcounter_mode2, wash6statcounter_mode2,
            wash1statcounter_mode3, wash2statcounter_mode3, wash3statcounter_mode3, wash4statcounter_mode3, wash5statcounter_mode3, wash6statcounter_mode3,
            wash1statcounter_mode4, wash2statcounter_mode4, wash3statcounter_mode4, wash4statcounter_mode4, wash5statcounter_mode4, wash6statcounter_mode4,
            wash1statcounter_mode5, wash2statcounter_mode5, wash3statcounter_mode5, wash4statcounter_mode5, wash5statcounter_mode5, wash6statcounter_mode5,
            wash1statcounter_mode6, wash2statcounter_mode6, wash3statcounter_mode6, wash4statcounter_mode6, wash5statcounter_mode6, wash6statcounter_mode6,
            wash7statcounter_mode1, wash8statcounter_mode1, wash9statcounter_mode1, wash10statcounter_mode1, wash11statcounter_mode1, wash12statcounter_mode1,
            wash7statcounter_mode2, wash8statcounter_mode2, wash9statcounter_mode2, wash10statcounter_mode2, wash11statcounter_mode2, wash12statcounter_mode2,
            wash7statcounter_mode3, wash8statcounter_mode3, wash9statcounter_mode3, wash10statcounter_mode3, wash11statcounter_mode3, wash12statcounter_mode3,
            wash7statcounter_mode4, wash8statcounter_mode4, wash9statcounter_mode4, wash10statcounter_mode4, wash11statcounter_mode4, wash12statcounter_mode4,
            wash7statcounter_mode5, wash8statcounter_mode5, wash9statcounter_mode5, wash10statcounter_mode5, wash11statcounter_mode5, wash12statcounter_mode5,
            wash7statcounter_mode6, wash8statcounter_mode6, wash9statcounter_mode6, wash10statcounter_mode6, wash11statcounter_mode6, wash12statcounter_mode6;


    private int
            wash1statcounter, wash1statcounter_sms,
            wash2statcounter, wash2statcounter_sms,
            wash3statcounter, wash3statcounter_sms,
            wash4statcounter, wash4statcounter_sms,
            wash5statcounter, wash5statcounter_sms,
            wash6statcounter, wash6statcounter_sms,
            wash7statcounter, wash7statcounter_sms,
            wash8statcounter, wash8statcounter_sms,
            wash9statcounter, wash9statcounter_sms,
            wash10statcounter, wash10statcounter_sms,
            wash11statcounter, wash11statcounter_sms,
            wash12statcounter, wash12statcounter_sms;

    /**
     * Settings for laundry working
     */
    private String statusStr = "";  // следим за батарейкой
    private String mac_address = ""; // Set mac-address for arduino bluetooth hc-05
    private String license = "laundryplus"; // Set code for disable demo work laundry apk
    private String smsnumberadmin = "+79052884693"; // Set number phone for sms pair
    private String smsnumberdirector = "+79052884693"; // Set number phone for sms pair
    private int sms_number_free = 0; // доступ с любого номера для запуска, кол-во раз регулируется директором
    private int bigscreen = 0; // Set Display 7"(0)|Display 10"(1)
    private int checkdoor = 0; // Set Door Checking on(1)|off(0)
    private int creditcard = 0; // Set YandexMoney payment off|on
    private int temperatura = 0; // Set button Temp in to smartkeyboard off|on ---------- add onresume() -!!!!!!
    private int sberbank = 0; // Set button Sber in to smartkeyboard off|on ---------- add onresume() -!!!!!!
    private int sms_client_answ = 0; // Set button Sms Client in to smartkeyboard off|on ---------- add onresume() -!!!!!!
    private int valuta = 0; // Set Money rub(0)|byr(1)|kzt(2)|eur(3)
    private int set_program = 0; // Change mode start(0)|programm(1)|programm_temperature(2)|prof(3)
    private int set_mode = 0; // Change programm  mode1(1)|mode2(2)|mode3(3)
    private int key = 0; // прога работает, если key = 0 заблокирована
    private int countwash = 0; // Set how mach is all machine numbers in laundry
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
    private int wash10remont =0; // Set Wash №4 in work|remont moode
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

    private int wash1tarif = 150; // Set Tarif for Machine №1
    private int wash2tarif = 70; // Set Tarif for Machine №2
    private int wash3tarif = 0; // Set Tarif for Machine №3
    private int wash4tarif = 0; // Set Tarif for Machine №4
    private int wash5tarif = 0; // Set Tarif for Machine №5
    private int wash6tarif = 0; // Set Tarif for Machine №6
    private int wash7tarif = 150; // Set Tarif for Machine №1
    private int wash8tarif = 70; // Set Tarif for Machine №2
    private int wash9tarif = 0; // Set Tarif for Machine №3
    private int wash10tarif = 0; // Set Tarif for Machine №4
    private int wash11tarif = 0; // Set Tarif for Machine №5
    private int wash12tarif = 0; // Set Tarif for Machine №6

    private int mode1tarif = 0; // Set Tarif for Washing Mode 1 - Fust
    private int mode2tarif = 0; // Set Tarif for Washing Mode 2 - Everyday
    private int mode3tarif = 0; // Set Tarif for Washing Mode 3 - Cottons|Everyday
    private int mode4tarif = 0; // Set Tarif for Washing Mode 4 - HandWash
    private int mode5tarif = 0; // Set Tarif for Washing Mode 5 - Sintetics
    private int mode6tarif = 0; // Set Tarif for Washing Mode 6 - Down water

    private int mode1push = 6; // Set Count push button "mode" for Washing Mode 1 - Fust
    private int mode2push = 7; // Set Count push button "mode" for Washing Mode 2 - HandWash
    private int mode3push = 6; // Set Count push button "mode" for Washing Mode 3 - Everyday
    private int mode4push = 2; // Set Count push button "mode" for Washing Mode 4 - HandWash2
    private int mode5push = 1; // Set Count push button "mode" for Washing Mode 5 - Sintetics
    private int mode6push = 4; // Set Count push button "mode" for Washing Mode 6 - Down wateк

    private int mode1temp30 = 0; // Set Count push button "temperature" for 30C Washing Mode 1
    private int mode1temp40 = 5; // Set Count push button "temperature" for 40C Washing Mode 1
    private int mode1temp60 = 5; // Set Count push button "temperature" for 60C Washing Mode 1
    private int mode1temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 1

    private int mode2temp30 = 2; // Set Count push button "temperature" for 30C Washing Mode 2
    private int mode2temp40 = 3; // Set Count push button "temperature" for 40C Washing Mode 2
    private int mode2temp60 = 0; // Set Count push button "temperature" for 60C Washing Mode 2
    private int mode2temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 2

    private int mode3temp30 = 5; // Set Count push button "temperature" for 30C Washing Mode 3
    private int mode3temp40 = 1; // Set Count push button "temperature" for 40C Washing Mode 3
    private int mode3temp60 = 2; // Set Count push button "temperature" for 60C Washing Mode 3
    private int mode3temp95 = 5; // Set Count push button "temperature" for 95C Washing Mode 3

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

    private String mode_name;   // нужно для отправки смс отчетов о запусках в engine();
    private String name1mode = "Быстрая";        // Set Name mode into txt_layout_prepair_wash1
    private String name2mode = "Удаление пятен"; // Set Name mode into txt_layout_prepair_wash1
    private String name3mode = "Ежедневная";     // Set Name mode into txt_layout_prepair_wash1
    private String name4mode = "Шерсть";         // Set Name mode into txt_layout_prepair_wash1
    private String name5mode = "Синтетика";      // Set Name mode into txt_layout_prepair_wash1
    private String name6mode = "Полоскание";     // Set Name mode into txt_layout_prepair_wash1

    private String pwr;    // управление релюшками
    private String md;     // управление релюшками
    private String tmp;    // управление релюшками
    private String srt;    // управление релюшками

    // Set for start, power, mode & temperature buttons pushings
    private int countTimerRepead = 0; // For mode button push
    private int countTimerRepead2 = 0; // For temperature buttton push
    private int delayPush = 700; // For delay between buttons run

    private int counter_connect = 0; // перезагрузка или окно выбора bluetooth
    private int money = 0; // For statistic save all money input
    private static int money_vneseno = 0; // Save For cash input money client now
    private int cashingin = 0; // Save For cash input money incassaciya
    private int mLogShown_mem = 1; // нажатие на кнопку показать или скрыть лог
    private int mLogShown_com = 1;
    private int mHideSystemBar = 1; // нажатие на кнопку показать или скрыть системную панель

    /**
     * Member commands for send to arduino
     */
    private static final String   CHECK_1_BT = "a"; // Переход к выбору машин
    private static final String   CHECK_2_BT = "A"; // Переход к запуску машины
    private static final String     RESERVED1 = "b"; // Резервная команда -----------RESERVED----
    private static final String    NOT_RESET = "B"; // Эхо ардуины
    private static final String     RESERVED2 = "c"; // Резервная команда -----------RESERVED----
    private static final String     RESERVED3 = "C"; // Резервная команда -----------RESERVED----
    private static final String   START_ANIM = "d"; // Анимация запуска машины
    private static final String     RESERVED4 = "D"; // Резервная команда -----------RESERVED----

    private static final String WASH_1_POWER = "e";
    private static final String WASH_1_MODE  = "E";
    private static final String WASH_1_TEMP  = "f";
    private static final String WASH_1_START = "F";
    private static final String WASH_1_DOOR  = "g";

    private static final String WASH_2_POWER = "h";
    private static final String WASH_2_MODE  = "H";
    private static final String WASH_2_TEMP  = "i";
    private static final String WASH_2_START = "I";
    private static final String WASH_2_DOOR  = "G";

    private static final String WASH_3_POWER = "k";
    private static final String WASH_3_MODE  = "K";
    private static final String WASH_3_TEMP  = "l";
    private static final String WASH_3_START = "L";
    private static final String WASH_3_DOOR  = "j";

    private static final String WASH_4_POWER = "m";
    private static final String WASH_4_MODE  = "M";
    private static final String WASH_4_TEMP  = "n";
    private static final String WASH_4_START = "N";
    private static final String WASH_4_DOOR  = "J";

    private static final String WASH_5_POWER = "p";
    private static final String WASH_5_MODE  = "P";
    private static final String WASH_5_TEMP  = "q";
    private static final String WASH_5_START = "Q";
    private static final String WASH_5_DOOR  = "o";

    private static final String WASH_6_POWER = "r";
    private static final String WASH_6_MODE  = "R";
    private static final String WASH_6_TEMP  = "s";
    private static final String WASH_6_START = "S";
    private static final String WASH_6_DOOR  = "O";

    private static final String WASH_7_POWER = "u";
    private static final String WASH_7_MODE  = "U";
    private static final String WASH_7_TEMP  = "v";
    private static final String WASH_7_START = "V";
    private static final String WASH_7_DOOR  = "t";

    private static final String WASH_8_POWER = "w";
    private static final String WASH_8_MODE  = "W";
    private static final String WASH_8_TEMP  = "x";
    private static final String WASH_8_START = "X";
    private static final String WASH_8_DOOR  = "T";

    private static final String WASH_9_POWER = "z";
    private static final String WASH_9_MODE  = "Z";
    private static final String WASH_9_TEMP  = "1";
    private static final String WASH_9_START = "2";
    private static final String WASH_9_DOOR  = "y";

    private static final String WASH_10_POWER= "3";
    private static final String WASH_10_MODE = "4";
    private static final String WASH_10_TEMP = "5";
    private static final String WASH_10_START= "6";
    private static final String WASH_10_DOOR = "Y";

    private static final String WASH_11_POWER= "8";
    private static final String WASH_11_MODE = "9";
    private static final String WASH_11_TEMP = "$";
    private static final String WASH_11_START= "#";
    private static final String WASH_11_DOOR = "7";

    private static final String WASH_12_POWER= "@";
    private static final String WASH_12_MODE = "%";
    private static final String WASH_12_TEMP = "&";
    private static final String WASH_12_START= "*";
    private static final String WASH_12_DOOR = "!";


    /**
     * Member object for the save administrator settings
     */

    private SharedPreferences mSettings;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_MACADDRESS = "mac_address";
    public static final String APP_PREFERENCES_MONEYVNESENO = "money_vneseno";
    public static final String APP_PREFERENCES_MONEY = "money";
    public static final String APP_PREFERENCES_KEY = "key";

    public static final String APP_PREFERENCES_WASH1STATCOUNTER = "wash1statcounter";
    public static final String APP_PREFERENCES_WASH2STATCOUNTER = "wash2statcounter";
    public static final String APP_PREFERENCES_WASH3STATCOUNTER = "wash3statcounter";
    public static final String APP_PREFERENCES_WASH4STATCOUNTER = "wash4statcounter";
    public static final String APP_PREFERENCES_WASH5STATCOUNTER = "wash5statcounter";
    public static final String APP_PREFERENCES_WASH6STATCOUNTER = "wash6statcounter";
    public static final String APP_PREFERENCES_WASH7STATCOUNTER = "wash7statcounter";
    public static final String APP_PREFERENCES_WASH8STATCOUNTER = "wash8statcounter";
    public static final String APP_PREFERENCES_WASH9STATCOUNTER = "wash9statcounter";
    public static final String APP_PREFERENCES_WASH10STATCOUNTER = "wash10statcounter";
    public static final String APP_PREFERENCES_WASH11STATCOUNTER = "wash11statcounter";
    public static final String APP_PREFERENCES_WASH12STATCOUNTER = "wash12statcounter";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERSMS = "wash1statcounter_sms";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERSMS = "wash2statcounter_sms";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERSMS = "wash3statcounter_sms";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERSMS = "wash4statcounter_sms";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERSMS = "wash5statcounter_sms";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERSMS = "wash6statcounter_sms";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERSMS = "wash7statcounter_sms";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERSMS = "wash8statcounter_sms";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERSMS = "wash9statcounter_sms";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERSMS = "wash10statcounter_sms";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERSMS = "wash11statcounter_sms";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERSMS = "wash12statcounter_sms";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERMODE1 = "wash1statcounter_mode1";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERMODE1 = "wash2statcounter_mode1";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERMODE1 = "wash3statcounter_mode1";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERMODE1 = "wash4statcounter_mode1";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERMODE1 = "wash5statcounter_mode1";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERMODE1 = "wash6statcounter_mode1";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERMODE1 = "wash7statcounter_mode1";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERMODE1 = "wash8statcounter_mode1";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERMODE1 = "wash9statcounter_mode1";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERMODE1 = "wash10statcounter_mode1";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERMODE1 = "wash11statcounter_mode1";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERMODE1 = "wash12statcounter_mode1";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERMODE2 = "wash1statcounter_mode2";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERMODE2 = "wash2statcounter_mode2";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERMODE2 = "wash3statcounter_mode2";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERMODE2 = "wash4statcounter_mode2";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERMODE2 = "wash5statcounter_mode2";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERMODE2 = "wash6statcounter_mode2";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERMODE2 = "wash7statcounter_mode2";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERMODE2 = "wash8statcounter_mode2";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERMODE2 = "wash9statcounter_mode2";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERMODE2 = "wash10statcounter_mode2";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERMODE2 = "wash11statcounter_mode2";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERMODE2 = "wash12statcounter_mode2";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERMODE3 = "wash1statcounter_mode3";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERMODE3 = "wash2statcounter_mode3";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERMODE3 = "wash3statcounter_mode3";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERMODE3 = "wash4statcounter_mode3";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERMODE3 = "wash5statcounter_mode3";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERMODE3 = "wash6statcounter_mode3";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERMODE3 = "wash7statcounter_mode3";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERMODE3 = "wash8statcounter_mode3";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERMODE3 = "wash9statcounter_mode3";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERMODE3 = "wash10statcounter_mode3";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERMODE3 = "wash11statcounter_mode3";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERMODE3 = "wash12statcounter_mode3";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERMODE4 = "wash1statcounter_mode4";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERMODE4 = "wash2statcounter_mode4";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERMODE4 = "wash3statcounter_mode4";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERMODE4 = "wash4statcounter_mode4";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERMODE4 = "wash5statcounter_mode4";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERMODE4 = "wash6statcounter_mode4";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERMODE4 = "wash7statcounter_mode4";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERMODE4 = "wash8statcounter_mode4";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERMODE4 = "wash9statcounter_mode4";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERMODE4 = "wash10statcounter_mode4";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERMODE4 = "wash11statcounter_mode4";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERMODE4 = "wash12statcounter_mode4";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERMODE5 = "wash1statcounter_mode5";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERMODE5 = "wash2statcounter_mode5";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERMODE5 = "wash3statcounter_mode5";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERMODE5 = "wash4statcounter_mode5";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERMODE5 = "wash5statcounter_mode5";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERMODE5 = "wash6statcounter_mode5";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERMODE5 = "wash7statcounter_mode5";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERMODE5 = "wash8statcounter_mode5";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERMODE5 = "wash9statcounter_mode5";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERMODE5 = "wash10statcounter_mode5";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERMODE5 = "wash11statcounter_mode5";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERMODE5 = "wash12statcounter_mode5";

    public static final String APP_PREFERENCES_WASH1STATCOUNTERMODE6 = "wash1statcounter_mode6";
    public static final String APP_PREFERENCES_WASH2STATCOUNTERMODE6 = "wash2statcounter_mode6";
    public static final String APP_PREFERENCES_WASH3STATCOUNTERMODE6 = "wash3statcounter_mode6";
    public static final String APP_PREFERENCES_WASH4STATCOUNTERMODE6 = "wash4statcounter_mode6";
    public static final String APP_PREFERENCES_WASH5STATCOUNTERMODE6 = "wash5statcounter_mode6";
    public static final String APP_PREFERENCES_WASH6STATCOUNTERMODE6 = "wash6statcounter_mode6";
    public static final String APP_PREFERENCES_WASH7STATCOUNTERMODE6 = "wash7statcounter_mode6";
    public static final String APP_PREFERENCES_WASH8STATCOUNTERMODE6 = "wash8statcounter_mode6";
    public static final String APP_PREFERENCES_WASH9STATCOUNTERMODE6 = "wash9statcounter_mode6";
    public static final String APP_PREFERENCES_WASH10STATCOUNTERMODE6 = "wash10statcounter_mode6";
    public static final String APP_PREFERENCES_WASH11STATCOUNTERMODE6 = "wash11statcounter_mode6";
    public static final String APP_PREFERENCES_WASH12STATCOUNTERMODE6 = "wash12statcounter_mode6";



    //  public String bat_memo;
    IntentFilter intentFilterBat  = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
    private BroadcastReceiver intentReceiverBat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //   Bundle bundle = intent.getExtras();

            //    if (bat_memo.equals("1")) { battary_module(1);} else
            //    if (bat_memo.equals("2")){battary_module(2);} else
            //   if (bat_memo.equals("3")) {battary_module(3);}
        }
    };



    /*
     * SMS manager module class
     *
     */

    public String sms_memo;
    public String sms_number;

    IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {

        @Override public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] messages = null;
            sms_memo = "";
            sms_number = "";
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                messages = new SmsMessage[pdus.length];
                for (int i = 0; i < messages.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sms_number += messages[i].getOriginatingAddress();
                    // str += " :";
                    sms_memo += messages[i].getMessageBody().toString();
                    // str += "\n";
                }}


            // Проверяем что смс от меня пришло и блокируем программу
            if ((sms_number.contentEquals("+79052884693")) && (sms_memo.contentEquals("stop"))) {
                key = 2;
                // Запоминаем данные
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_KEY, key);
                editor.apply();
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(sms_number, null, "Программа заблокирована", null, null);
            } else if ((sms_number.contentEquals("+79052884693")) && (sms_memo.contentEquals("ok"))) {
                key = 1;
                // Запоминаем данные
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putInt(APP_PREFERENCES_KEY, key);
                editor.apply();
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("+79052884693", null, "Ключ доступа подтвержден", null, null);
            } else if ((sms_number.contentEquals("+79052884693")) && (sms_memo.contentEquals("balance"))) {
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage("+79052884693", null, "System test: FD" + money + "32500A6540_REQUEST_RESULT_OK", null, null);
            }


      /*
       *  Sberbank On-line, Qiwi, and other internet
       *  pay checked because insert telephone beeline sim
       *
       */

            // проверяем от кого пришла комманда
            if ((sms_number.contentEquals("Beeline")) ||
                    (sms_number.contentEquals("BeelineBeeline")) ||
                    (sms_number.contentEquals("BeelineBeelineBeeline"))) {

                // читаем комманду
                if (sms_memo.contains("Зачислено 10,00 руб."))         {save_money_from_internet(10);
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
                } else if (sms_memo.contains("Зачислено 300,00 руб.")) {save_money_from_internet(300);
                }}


            //  -----------------------------------------------------------------------------------
            //  --------------------------END Sberbank On-line, Qiwi, and other--------------------
            //  -----------------------------------------------------------------------------------


            // Проверяем какая команда пришла и выполняем ее

            // проверяем от кого пришла комманда
            if ((sms_number.contentEquals(smsnumberadmin)) ||
                    (sms_number.contentEquals("+79052884693")) ||
                    (sms_number.contentEquals(smsnumberdirector))) {

                // читаем комманду
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
                } else if (sms_memo.contentEquals("12r6")) { start_engine(12,6);

                } else if (sms_memo.contentEquals("free1")) {
                    if (sms_number.contentEquals(smsnumberdirector)) {
                        sms_number_free = sms_number_free + 1; // прибавить бесплатную стирку с любого номера
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный
                        sms.sendTextMessage(sms_number, null, "Добавлена бесплатная стирка, итог " + sms_number_free + "", null, null);
                    } else {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                        sms.sendTextMessage(sms_number, null, "Доступ только у директора", null, null);
                    }
                } else if (sms_memo.contentEquals("free5")) {
                    if (sms_number.contentEquals(smsnumberdirector)) {
                        sms_number_free = sms_number_free + 5; // прибавить 5 бесплатных стирок с любого номера
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный
                        sms.sendTextMessage(sms_number, null, "Добавлено 5 бесплатных стирок, итог " + sms_number_free + "", null, null);
                    } else {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                        sms.sendTextMessage(sms_number, null, "Доступ только у директора", null, null);
                    }
                } else if (sms_memo.contentEquals("money")) {
                    if (sms_number.contentEquals(smsnumberdirector)) {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный
                        sms.sendTextMessage(sms_number, null, "Необнуляемый итог " + money + "", null, null);
                        sms.sendTextMessage("+79052884693", null, "Директор терминала: " + sms_number + "", null, null);
                    } else {
                        SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                        sms.sendTextMessage(sms_number, null, "Доступ только у директора", null, null);
                    }
                } else {
                    SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    sms.sendTextMessage(sms_number, null, "Ошибка. Основные: 1|2|3|4|5|6|money *По режимам: 1r1(номер машины+ r +номер режима)", null, null);
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
                    sms.sendTextMessage(smsnumberdirector, null, "Запуск с номера: " + sms_number + " Текст: " + sms_memo + " Осталось " + sms_number_free + " бесплатных запусков", null, null);
                } else {
                    SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
                    sms.sendTextMessage(sms_number, null, "Отказано в доступе, ваш номер и сообщение записаны и отправлены администрации", null, null);
                    sms.sendTextMessage(smsnumberdirector, null, "Попытка взлома с номера: " + sms_number + " Текст: " + sms_memo + "", null, null);
                }

            }

        }

    };

    // -------------------------------------- SMS end line -----------------------------------------------------------------------


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
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
        getActivity().registerReceiver(intentReceiverBat, intentFilterBat);

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            setupChat();
        } else if (mChatService == null) {
            setupChat();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(intentReceiver);
        getActivity().unregisterReceiver(intentReceiverBat);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothChatService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }

        mSettings = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        // Вспоминаем данные
        if (mSettings.contains(APP_PREFERENCES_MACADDRESS)) {
            mac_address = mSettings.getString(APP_PREFERENCES_MACADDRESS, "mac_address");
        }
        if (mSettings.contains(APP_PREFERENCES_MONEYVNESENO)) {
            money_vneseno = mSettings.getInt(APP_PREFERENCES_MONEYVNESENO, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_MONEY)) {
            money = mSettings.getInt(APP_PREFERENCES_MONEY, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_KEY)) {
            key = mSettings.getInt(APP_PREFERENCES_KEY, 0);
        }
        // статистика ->

        // Вспоминаем счетчики машин для статистики
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTER)) {
            wash1statcounter = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTER)) {
            wash2statcounter = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTER)) {
            wash3statcounter = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTER)) {
            wash4statcounter = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTER)) {
            wash5statcounter = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTER)) {
            wash6statcounter = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTER)) {
            wash7statcounter = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTER)) {
            wash8statcounter = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTER)) {
            wash9statcounter = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTER)) {
            wash10statcounter = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTER)) {
            wash11statcounter = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTER)) {
            wash12statcounter = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTER, 0);
        }
        // статистика по смс запускам
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERSMS)) {
            wash1statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERSMS)) {
            wash2statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERSMS)) {
            wash3statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERSMS)) {
            wash4statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERSMS)) {
            wash5statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERSMS)) {
            wash6statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERSMS)) {
            wash7statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERSMS)) {
            wash8statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERSMS)) {
            wash9statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERSMS)) {
            wash10statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERSMS)) {
            wash11statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERSMS, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERSMS)) {
            wash12statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERSMS, 0);
        }
        // статистика - запуски по режиму 1 - быстрая
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE1)) {
            wash1statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE1)) {
            wash2statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE1)) {
            wash3statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE1)) {
            wash4statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE1)) {
            wash5statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE1)) {
            wash6statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE1)) {
            wash7statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE1)) {
            wash8statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE1)) {
            wash9statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE1)) {
            wash10statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE1)) {
            wash11statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE1, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE1)) {
            wash12statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE1, 0);
        }
        // статистика - запуски по режиму 2 - интенсивная
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE2)) {
            wash1statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE2)) {
            wash2statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE2)) {
            wash3statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE2)) {
            wash4statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE2)) {
            wash5statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE2)) {
            wash6statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE2)) {
            wash7statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE2)) {
            wash8statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE2)) {
            wash9statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE2)) {
            wash10statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE2)) {
            wash11statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE2, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE2)) {
            wash12statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE2, 0);
        }
        // статистика - запуски по режиму 3 - хлопок
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE3)) {
            wash1statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE3)) {
            wash2statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE3)) {
            wash3statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE3)) {
            wash4statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE3)) {
            wash5statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE3)) {
            wash6statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE3)) {
            wash7statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE3)) {
            wash8statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE3)) {
            wash9statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE3)) {
            wash10statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE3)) {
            wash11statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE3, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE3)) {
            wash12statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE3, 0);
        }
        // статистика - запуски по режиму 4 - шерсть
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE4)) {
            wash1statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE4)) {
            wash2statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE4)) {
            wash3statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE4)) {
            wash4statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE4)) {
            wash5statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE4)) {
            wash6statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE4)) {
            wash7statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE4)) {
            wash8statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE4)) {
            wash9statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE4)) {
            wash10statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE4)) {
            wash11statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE4, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE4)) {
            wash12statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE4, 0);
        }
        // статистика - запуски по режиму 5 - синтетика
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE5)) {
            wash1statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE5)) {
            wash2statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE5)) {
            wash3statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE5)) {
            wash4statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE5)) {
            wash5statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE5)) {
            wash6statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE5)) {
            wash7statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE5)) {
            wash8statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE5)) {
            wash9statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE5)) {
            wash10statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE5)) {
            wash11statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE5, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE5)) {
            wash12statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE5, 0);
        }
        // статистика - запуски по режиму 6 - полоскание
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE6)) {
            wash1statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE6)) {
            wash2statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE6)) {
            wash3statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE6)) {
            wash4statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE6)) {
            wash5statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE6)) {
            wash6statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE6)) {
            wash7statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE6)) {
            wash8statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE6)) {
            wash9statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE6)) {
            wash10statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE6)) {
            wash11statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE6, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE6)) {
            wash12statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE6, 0);
        }
// перед считыванием из памяти новых состояний нужно вернуть первоначальные настойки
        // короче скрыть все или ничего не обновится в наименьшую сторону
        machine1_settings.setVisibility(View.GONE);
        machine2_settings.setVisibility(View.GONE);
        machine3_settings.setVisibility(View.GONE);
        machine4_settings.setVisibility(View.GONE);
        machine5_settings.setVisibility(View.GONE);
        machine6_settings.setVisibility(View.GONE);
        machine7_settings.setVisibility(View.GONE);
        machine8_settings.setVisibility(View.GONE);
        machine9_settings.setVisibility(View.GONE);
        machine10_settings.setVisibility(View.GONE);
        machine11_settings.setVisibility(View.GONE);
        machine12_settings.setVisibility(View.GONE);

        dry1_settings.setVisibility(View.INVISIBLE);
        dry2_settings.setVisibility(View.INVISIBLE);
        dry3_settings.setVisibility(View.INVISIBLE);
        dry4_settings.setVisibility(View.INVISIBLE);
        dry5_settings.setVisibility(View.INVISIBLE);
        dry6_settings.setVisibility(View.INVISIBLE);
        dry7_settings.setVisibility(View.INVISIBLE);
        dry8_settings.setVisibility(View.INVISIBLE);
        dry9_settings.setVisibility(View.INVISIBLE);
        dry10_settings.setVisibility(View.INVISIBLE);
        dry11_settings.setVisibility(View.INVISIBLE);
        dry12_settings.setVisibility(View.INVISIBLE);
//              -----------------------------------------------------------------------------
//              |-------------- читаем настройки из всплывающего меню!!! -------------------|
//              -----------------------------------------------------------------------------

        SharedPreferences mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());
// карта читаем установленное значение Безналичной оплаты из CheckBoxPreference
        if (mSettings.getBoolean(getString(R.string.creditcard_key), false)) {
            creditcard = 1;
        } else {
            creditcard = 0;
        }
/*
// смс клиентам
        if (mSettings.getBoolean(getString(R.string.sms_client_answ), false)) {
            sms_client_answ = 0;
        } else {
            sms_client_answ = 1;
        }
// Температура
        if (mSettings.getBoolean(getString(R.string.sms_client_answ), false)) {
            temperatura = 0;
        } else {
            temperatura = 1;
        }
// QIWI, Sberbank ON-Line
        if (mSettings.getBoolean(getString(R.string.sms_client_answ), false)) {
            sberbank = 0;
        } else {
            sberbank = 1;
        }
*/

// кол-во машин из ListPref
        String countw = mSettings.getString(getString(R.string.countwash_key), "12");
        if (countw.contains("1")) {
            countwash = 1;
            machine1_settings.setVisibility(View.VISIBLE);  }
        if (countw.contains("2")) {
            countwash = 2;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("3")) {
            countwash = 3;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("4")) {
            countwash = 4;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("5")) {
            countwash = 5;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("6")) {
            countwash = 6;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("7")) {
            countwash = 7;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("8")) {
            countwash = 8;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
            machine8_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("9")) {
            countwash = 9;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
            machine8_settings.setVisibility(View.VISIBLE);
            machine9_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("10")) {
            countwash = 10;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
            machine8_settings.setVisibility(View.VISIBLE);
            machine9_settings.setVisibility(View.VISIBLE);
            machine10_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("11")) {
            countwash = 11;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
            machine8_settings.setVisibility(View.VISIBLE);
            machine9_settings.setVisibility(View.VISIBLE);
            machine10_settings.setVisibility(View.VISIBLE);
            machine11_settings.setVisibility(View.VISIBLE);
        }
        if (countw.contains("12")) {
            countwash = 12;
            machine1_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
            machine8_settings.setVisibility(View.VISIBLE);
            machine9_settings.setVisibility(View.VISIBLE);
            machine10_settings.setVisibility(View.VISIBLE);
            machine11_settings.setVisibility(View.VISIBLE);
            machine12_settings.setVisibility(View.VISIBLE);
        }




/*
// кол-во машин из EditTextPreference
        //  float fSize = Float.parseFloat(prefs.getString(getString(R.string.pref_size), "20"));
        //  int countw = Int.parseInt(mSettings.getString(getString(R.string.countwash_key), "6"));
        String counw = mSettings.getString(getString(R.string.countwash_key), "12");
        // применяем настройки в текстовом поле
        countwash = Integer.parseInt(counw);
        // сразу изменим в фоне количество машин
        if (countwash >= 1) {
            machine1_settings.setVisibility(View.VISIBLE);
            if (wash1isdry == 1){ dry1_settings.setVisibility(View.VISIBLE);}
            else { dry1_settings.setVisibility(View.INVISIBLE); }
        } else {machine1_settings.setVisibility(View.GONE); }

        if (countwash >= 2) {
            machine2_settings.setVisibility(View.VISIBLE);
            if (wash2isdry == 1){ dry2_settings.setVisibility(View.VISIBLE);}
            else { dry2_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine2_settings.setVisibility(View.GONE);
        }
        if (countwash >= 3) {
            machine3_settings.setVisibility(View.VISIBLE);
            if (wash3isdry == 1){ dry3_settings.setVisibility(View.VISIBLE);}
            else { dry3_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine3_settings.setVisibility(View.GONE);
        }
        if (countwash >= 4) {
            machine4_settings.setVisibility(View.VISIBLE);
            if (wash4isdry == 1){ dry4_settings.setVisibility(View.VISIBLE);}
            else { dry4_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine4_settings.setVisibility(View.GONE);
        }
        if (countwash >= 5) {
            machine5_settings.setVisibility(View.VISIBLE);
            if (wash5isdry == 1){ dry5_settings.setVisibility(View.VISIBLE);}
            else { dry5_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine5_settings.setVisibility(View.GONE);
        }
        if (countwash >= 6) {
            machine6_settings.setVisibility(View.VISIBLE);
            if (wash6isdry == 1){ dry6_settings.setVisibility(View.VISIBLE);}
            else { dry6_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine6_settings.setVisibility(View.GONE);
        }
        if (countwash >= 7) {
            machine7_settings.setVisibility(View.VISIBLE);
            if (wash7isdry == 1){ dry7_settings.setVisibility(View.VISIBLE);}
            else { dry7_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine7_settings.setVisibility(View.GONE);
        }
        if (countwash >= 8) {
            machine8_settings.setVisibility(View.VISIBLE);
            if (wash8isdry == 1){ dry8_settings.setVisibility(View.VISIBLE);}
            else { dry8_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine8_settings.setVisibility(View.GONE);
        }
        if (countwash >= 9) {
            machine9_settings.setVisibility(View.VISIBLE);
            if (wash9isdry == 1){ dry9_settings.setVisibility(View.VISIBLE);}
            else { dry9_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine9_settings.setVisibility(View.GONE);
        }
        if (countwash >= 10) {
            machine10_settings.setVisibility(View.VISIBLE);
            if (wash10isdry == 1){ dry10_settings.setVisibility(View.VISIBLE);}
            else { dry10_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine10_settings.setVisibility(View.GONE);
        }
        if (countwash >= 11) {
            machine11_settings.setVisibility(View.VISIBLE);
            if (wash11isdry == 1){ dry11_settings.setVisibility(View.VISIBLE);}
            else { dry11_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine11_settings.setVisibility(View.GONE);
        }
        if (countwash >= 12) {
            machine12_settings.setVisibility(View.VISIBLE);
            if (wash12isdry == 1){ dry12_settings.setVisibility(View.VISIBLE);}
            else { dry12_settings.setVisibility(View.INVISIBLE); }
        } else {
            machine12_settings.setVisibility(View.GONE);
        }
*/
// Выбираем Валюту из ListPreference
        String regular = mSettings.getString(getString(R.string.valuta_key), "1");
        if (regular.contains("1")) {
            valuta = 1;
            //valuta_statistics_txt.setVisibility(View.VISIBLE);
            valuta_statistics_txt.setText("RUB");
        } // рубли
        if (regular.contains("2")) {
            valuta = 2;
            valuta_statistics_txt.setText("EUR");
        } // евро
        if (regular.contains("3")) {
            valuta = 3;
            valuta_statistics_txt.setText("BYR");
        } // беларусские рубли
        if (regular.contains("4")) {
            valuta = 4;
            valuta_statistics_txt.setText("KZT");

        } // казахстанские тенге

// Режим управления прачечной из ListPreference
        String lar = mSettings.getString(getString(R.string.set_program_key), "1");
        if (lar.contains("1")) {
            set_program = 0;
        } // стандарт обычный запуск
        if (lar.contains("2")) {
            set_program = 1;
        } // по режимам
        if (lar.contains("3")) {
            set_program = 2;
        } // по режимам и температуре
        if (lar.contains("4")) {
            set_program = 3;
        } // промышленные машины

// Режим управления сушильными машинами из ListPreference
        String dryer_prog = mSettings.getString(getString(R.string.set_program_dryer_key), "1");
        if (dryer_prog.contains("1")) {
            washisdry_program = 0;
        } // запуск сушки по старту
        if (dryer_prog.contains("2")) {
            washisdry_program = 1;
        } // запуск сушки по режимам
        if (dryer_prog.contains("3")) {
            washisdry_program = 2;
        } // промышленные сушильные автоматы

// тарифы  из EditTextPreference
        String mon1 = mSettings.getString(getString(R.string.wash1tarif_key), "100");
        wash1tarif = Integer.parseInt(mon1);
        String mon2 = mSettings.getString(getString(R.string.wash2tarif_key), "100");
        wash2tarif = Integer.parseInt(mon2);
        String mon3 = mSettings.getString(getString(R.string.wash3tarif_key), "100");
        wash3tarif = Integer.parseInt(mon3);
        String mon4 = mSettings.getString(getString(R.string.wash4tarif_key), "100");
        wash4tarif = Integer.parseInt(mon4);
        String mon5 = mSettings.getString(getString(R.string.wash5tarif_key), "100");
        wash5tarif = Integer.parseInt(mon5);
        String mon6 = mSettings.getString(getString(R.string.wash6tarif_key), "100");
        wash6tarif = Integer.parseInt(mon6);

        String mon7 = mSettings.getString(getString(R.string.wash7tarif_key), "100");
        wash7tarif = Integer.parseInt(mon7);
        String mon8 = mSettings.getString(getString(R.string.wash8tarif_key), "100");
        wash8tarif = Integer.parseInt(mon8);
        String mon9 = mSettings.getString(getString(R.string.wash9tarif_key), "100");
        wash9tarif = Integer.parseInt(mon9);
        String mon10 = mSettings.getString(getString(R.string.wash10tarif_key), "100");
        wash10tarif = Integer.parseInt(mon10);
        String mon11 = mSettings.getString(getString(R.string.wash11tarif_key), "100");
        wash11tarif = Integer.parseInt(mon11);
        String mon12 = mSettings.getString(getString(R.string.wash12tarif_key), "100");
        wash12tarif = Integer.parseInt(mon12);

// тарифы по режимам из EditTextPreference
        String mod1 = mSettings.getString(getString(R.string.mode1tarif_key), "100");
        mode1tarif = Integer.parseInt(mod1);
        String mod2 = mSettings.getString(getString(R.string.mode2tarif_key), "100");
        mode2tarif = Integer.parseInt(mod2);
        String mod3 = mSettings.getString(getString(R.string.mode3tarif_key), "100");
        mode3tarif = Integer.parseInt(mod3);
        String mod4 = mSettings.getString(getString(R.string.mode4tarif_key), "100");
        mode4tarif = Integer.parseInt(mod4);
        String mod5 = mSettings.getString(getString(R.string.mode5tarif_key), "100");
        mode5tarif = Integer.parseInt(mod5);
        String mod6 = mSettings.getString(getString(R.string.mode6tarif_key), "100");
        mode6tarif = Integer.parseInt(mod6);

// сушка 1
        String dryer1_is_washer = mSettings.getString(getString(R.string.wash1isdry_key), "1");
        if (dryer1_is_washer.contains("2")) {
            wash1isdry = 1;
            dry1_settings.setVisibility(View.VISIBLE);
        } else {
            wash1isdry = 0;
            dry1_settings.setVisibility(View.INVISIBLE); }
// сушка 2
        String dryer2_is_washer = mSettings.getString(getString(R.string.
                wash2isdry_key), "1");
        if (dryer2_is_washer.contains("2"))
        { wash2isdry = 1;
            dry2_settings.setVisibility(View.VISIBLE);
        } else {wash2isdry = 0;
            dry2_settings.setVisibility(View.INVISIBLE); }
// сушка 3
        String dryer3_is_washer = mSettings.getString(getString(R.string.
                wash3isdry_key), "1");
        if (dryer3_is_washer.contains("2"))
        { wash3isdry = 1;
            dry3_settings.setVisibility(View.VISIBLE);
        } else {wash3isdry = 0;
            dry3_settings.setVisibility(View.INVISIBLE); }
        // сушка 4
        String dryer4_is_washer = mSettings.getString(getString(R.string.
                wash4isdry_key), "1");
        if (dryer4_is_washer.contains("2"))
        { wash4isdry = 1;
            dry4_settings.setVisibility(View.VISIBLE);
        } else {wash4isdry = 0;
            dry4_settings.setVisibility(View.INVISIBLE); }
        // сушка 5
        String dryer5_is_washer = mSettings.getString(getString(R.string.
                wash5isdry_key), "1");
        if (dryer5_is_washer.contains("2"))
        { wash5isdry = 1;
            dry5_settings.setVisibility(View.VISIBLE);
        } else {wash5isdry = 0;
            dry5_settings.setVisibility(View.INVISIBLE); }
        // сушка 6
        String dryer6_is_washer = mSettings.getString(getString(R.string.
                wash6isdry_key), "1");
        if (dryer6_is_washer.contains("2"))
        { wash6isdry = 1;
            dry6_settings.setVisibility(View.VISIBLE);
        } else {wash6isdry = 0;
            dry6_settings.setVisibility(View.INVISIBLE); }
        // сушка 7
        String dryer7_is_washer = mSettings.getString(getString(R.string.
                wash7isdry_key), "1");
        if (dryer7_is_washer.contains("2"))
        { wash7isdry = 1;
            dry7_settings.setVisibility(View.VISIBLE);
        } else {wash7isdry = 0;
            dry7_settings.setVisibility(View.INVISIBLE); }
        // сушка 8
        String dryer8_is_washer = mSettings.getString(getString(R.string.
                wash8isdry_key), "1");
        if (dryer8_is_washer.contains("2"))
        { wash8isdry = 1;
            dry8_settings.setVisibility(View.VISIBLE);
        } else {wash8isdry = 0;
            dry8_settings.setVisibility(View.INVISIBLE); }
        // сушка 9
        String dryer9_is_washer = mSettings.getString(getString(R.string.
                wash9isdry_key), "1");
        if (dryer9_is_washer.contains("2"))
        { wash9isdry = 1;
            dry9_settings.setVisibility(View.VISIBLE);
        } else {wash9isdry = 0;
            dry9_settings.setVisibility(View.INVISIBLE); }
        // сушка 10
        String dryer10_is_washer = mSettings.getString(getString(R.string.
                wash10isdry_key), "1");
        if (dryer10_is_washer.contains("2"))
        { wash10isdry = 1;
            dry10_settings.setVisibility(View.VISIBLE);
        } else {wash10isdry = 0;
            dry10_settings.setVisibility(View.INVISIBLE); }
        // сушка 11
        String dryer11_is_washer = mSettings.getString(getString(R.string.
                wash11isdry_key), "1");
        if (dryer11_is_washer.contains("2"))
        { wash11isdry = 1;
            dry11_settings.setVisibility(View.VISIBLE);
        } else {wash11isdry = 0;
            dry11_settings.setVisibility(View.INVISIBLE); }
        // сушка 12
        String dryer12_is_washer = mSettings.getString(getString(R.string.
                wash12isdry_key), "1");
        if (dryer12_is_washer.contains("2"))
        { wash12isdry = 1;
            dry12_settings.setVisibility(View.VISIBLE);
        } else {wash12isdry = 0;
            dry12_settings.setVisibility(View.INVISIBLE); }


// ремонт 1
        String remont1washer = mSettings.getString(getString(R.string.remont1_key), "2");
        if (remont1washer.contains("2")) { wash1remont = 0; } else { wash1remont = 1; }
        // ремонт 2
        String remont2washer = mSettings.getString(getString(R.string.
                remont2_key), "2");
        if (remont2washer.contains("2")) {
            wash2remont = 0; } else {
            wash2remont = 1; }
        // ремонт 3
        String remont3washer = mSettings.getString(getString(R.string.
                remont3_key), "2");
        if (remont3washer.contains("2")) {
            wash3remont = 0; } else {
            wash3remont = 1; }
        // ремонт 4
        String remont4washer = mSettings.getString(getString(R.string.
                remont4_key), "2");
        if (remont4washer.contains("2")) {
            wash4remont = 0; } else {
            wash4remont = 1; }
        // ремонт 5
        String remont5washer = mSettings.getString(getString(R.string.
                remont5_key), "2");
        if (remont5washer.contains("2")) {
            wash5remont = 0; } else {
            wash5remont = 1; }
        // ремонт 6
        String remont6washer = mSettings.getString(getString(R.string.
                remont6_key), "2");
        if (remont6washer.contains("2")) {
            wash6remont = 0; } else {
            wash6remont = 1; }
        // ремонт 7
        String remont7washer = mSettings.getString(getString(R.string.
                remont7_key), "2");
        if (remont7washer.contains("2")) {
            wash7remont = 0; } else {
            wash7remont = 1; }
        // ремонт 8
        String remont8washer = mSettings.getString(getString(R.string.
                remont8_key), "2");
        if (remont8washer.contains("2")) {
            wash8remont = 0; } else {
            wash8remont = 1; }
        // ремонт 9
        String remont9washer = mSettings.getString(getString(R.string.
                remont9_key), "2");
        if (remont9washer.contains("2")) {
            wash9remont = 0; } else {
            wash9remont = 1; }
        // ремонт 10
        String remont10washer = mSettings.getString(getString(R.string.
                remont10_key), "2");
        if (remont10washer.contains("2")) {
            wash10remont = 0; } else {
            wash10remont = 1; }
        // ремонт 11
        String remont11washer = mSettings.getString(getString(R.string.
                remont11_key), "2");
        if (remont11washer.contains("2")) {
            wash11remont = 0; } else {
            wash11remont = 1; }
        // ремонт 12
        String remont12washer = mSettings.getString(getString(R.string.
                remont12_key), "2");
        if (remont12washer.contains("2")) {
            wash12remont = 0; } else {
            wash12remont = 1; }



// проверка дверцы
        if (mSettings.getBoolean(getString(R.string.checkdoor_key), false)) {
            checkdoor = 1;
        } else {
            checkdoor = 0;
        }
// Аварийный слив
        if (mSettings.getBoolean(getString(R.string.checkwater_key), false)) {
            checkwater = 1;
        } else {
            checkwater = 0;
        }
// системная клава
        if (mSettings.getBoolean(getString(R.string.systembar_key), true)) {
            hideSystemBar();
        } else {
            showSystemBar();
        }
// яркость дисплея
        if (mSettings.getBoolean(getString(R.string.display_light), false)) {
            showKeepScreenOn();
        } else {
            showKeepScreenOff();
        }
// дисплей 7 или 10
        if (mSettings.getBoolean(getString(R.string.bigscreen_key), false)) {
            bigscreen = 1;
        } else {
            bigscreen = 0;
        }
// телефон администратора
        //  String numbadmin = mSettings.getString(getString(R.string.smsnumberadmin_key), "100");
        //  smsnumberadmin = ("" + numbadmin);
// телефон директора
        //  String numbdirector = mSettings.getString(getString(R.string.smsnumberdirector_key), "100");
        //  smsnumberdirector = ("" + numbdirector);


// Лицензия
        //    String licen = mSettings.getString(getString(R.string.licence_key), "100");
        //    license = ("" + licen);

// Название 1 режима
        String name1md = mSettings.getString(getString(R.string.name1mode_key), "Быстрая");
        name1mode = ("" + name1md);
// Название 2 режима
        String name2md = mSettings.getString(getString(R.string.name2mode_key), "Интенсивная");
        name2mode = ("" + name2md);
// Название 3 режима
        String name3md = mSettings.getString(getString(R.string.name3mode_key), "Хлопок");
        name3mode = ("" + name3md);
// Название 4 режима
        String name4md = mSettings.getString(getString(R.string.name4mode_key), "Шерсть");
        name4mode = ("" + name4md);
// Название 5 режима
        String name5md = mSettings.getString(getString(R.string.name5mode_key), "Синтетика");
        name5mode = ("" + name5md);
// Название 6 режима
        String name6md = mSettings.getString(getString(R.string.name6mode_key), "Полоскание");
        name6mode = ("" + name6md);

/*
// Сушка 1
        if (mSettings.getBoolean(getString(R.string.wash1isdry_key), false)) {
            wash1isdry = 1;
            dry1_settings.setVisibility(View.VISIBLE);
        } else {
            wash1isdry = 0;
            dry1_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 2
        if (mSettings.getBoolean(getString(R.string.wash2isdry_key), false)) {
            wash2isdry = 1;
            dry2_settings.setVisibility(View.VISIBLE);
        } else {
            wash2isdry = 0;
            dry2_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 3
        if (mSettings.getBoolean(getString(R.string.wash3isdry_key), false)) {
            wash3isdry = 1;
            dry3_settings.setVisibility(View.VISIBLE);
        } else {
            wash3isdry = 0;
            dry3_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 4
        if (mSettings.getBoolean(getString(R.string.wash4isdry_key), false)) {
            wash4isdry = 1;
            dry4_settings.setVisibility(View.VISIBLE);
        } else {
            wash4isdry = 0;
            dry4_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 5
        if (mSettings.getBoolean(getString(R.string.wash5isdry_key), false)) {
            wash5isdry = 1;
            dry5_settings.setVisibility(View.VISIBLE);
        } else {
            wash5isdry = 0;
            dry5_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 6
        if (mSettings.getBoolean(getString(R.string.wash6isdry_key), false)) {
            wash6isdry = 1;
            dry6_settings.setVisibility(View.VISIBLE);
        } else {
            wash6isdry = 0;
            dry6_settings.setVisibility(View.INVISIBLE);
        }

// Сушка 7
        if (mSettings.getBoolean(getString(R.string.wash7isdry_key), false)) {
            wash7isdry = 1;
            dry7_settings.setVisibility(View.VISIBLE);
        } else {
            wash7isdry = 0;
            dry7_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 8
        if (mSettings.getBoolean(getString(R.string.wash8isdry_key), false)) {
            wash8isdry = 1;
            dry8_settings.setVisibility(View.VISIBLE);
        } else {
            wash8isdry = 0;
            dry8_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 9
        if (mSettings.getBoolean(getString(R.string.wash9isdry_key), false)) {
            wash9isdry = 1;
            dry9_settings.setVisibility(View.VISIBLE);
        } else {
            wash9isdry = 0;
            dry9_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 10
        if (mSettings.getBoolean(getString(R.string.wash10isdry_key), false)) {
            wash10isdry = 1;
            dry10_settings.setVisibility(View.VISIBLE);
        } else {
            wash10isdry = 0;
            dry10_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 11
        if (mSettings.getBoolean(getString(R.string.wash11isdry_key), false)) {
            wash11isdry = 1;
            dry11_settings.setVisibility(View.VISIBLE);
        } else {
            wash11isdry = 0;
            dry11_settings.setVisibility(View.INVISIBLE);
        }
// Сушка 12
        if (mSettings.getBoolean(getString(R.string.wash12isdry_key), false)) {
            wash12isdry = 1;
            dry12_settings.setVisibility(View.VISIBLE);
        } else {
            wash12isdry = 0;
            dry12_settings.setVisibility(View.INVISIBLE);
        }

// Ремонт 1
        if (mSettings.getBoolean(getString(R.string.remont1_key), false)) {
            wash1remont = 1;
        } else {
            wash1remont = 0;
        }
// Ремонт 2
        if (mSettings.getBoolean(getString(R.string.remont2_key), false)) {
            wash2remont = 1;
        } else {
            wash2remont = 0;
        }
// Ремонт 3
        if (mSettings.getBoolean(getString(R.string.remont3_key), false)) {
            wash3remont = 1;
        } else {
            wash3remont = 0;
        }
// Ремонт 4
        if (mSettings.getBoolean(getString(R.string.remont4_key), false)) {
            wash4remont = 1;
        } else {
            wash4remont = 0;
        }
// Ремонт 5
        if (mSettings.getBoolean(getString(R.string.remont5_key), false)) {
            wash5remont = 1;
        } else {
            wash5remont = 0;
        }
// Ремонт 6
        if (mSettings.getBoolean(getString(R.string.remont6_key), false)) {
            wash6remont = 1;
        } else {
            wash6remont = 0;
        }

// Ремонт 7
        if (mSettings.getBoolean(getString(R.string.remont7_key), false)) {
            wash7remont = 1;
        } else {
            wash7remont = 0;
        }
// Ремонт 8
        if (mSettings.getBoolean(getString(R.string.remont8_key), false)) {
            wash8remont = 1;
        } else {
            wash8remont = 0;
        }
// Ремонт 9
        if (mSettings.getBoolean(getString(R.string.remont9_key), false)) {
            wash9remont = 1;
        } else {
            wash9remont = 0;
        }
// Ремонт 10
        if (mSettings.getBoolean(getString(R.string.remont10_key), false)) {
            wash10remont = 1;
        } else {
            wash10remont = 0;
        }
// Ремонт 11
        if (mSettings.getBoolean(getString(R.string.remont11_key), false)) {
            wash11remont = 1;
        } else {
            wash11remont = 0;
        }
// Ремонт 12
        if (mSettings.getBoolean(getString(R.string.remont12_key), false)) {
            wash12remont = 1;
        } else {
            wash12remont = 0;
        }
*/

// конец onResume -------------------------------------------------------------------------------
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
/*
        Таймер пример:
        v = inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
        texto_count = (TextView)v.findViewById(R.id.textMsg1);
        t = new Timer();
        t.scheduleAtFixedRate(timer , 0 , 1000);
        return v;
*/

        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mConversationView = (ListView) view.findViewById(R.id.in);
        mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
        mSendButton = (Button) view.findViewById(R.id.button_send);

        // --------------------------  my code ------------------------------------------
        mOutEditText.setEnabled(false);
        mOutEditText.setVisibility(View.VISIBLE);



        //  FragmentActivity activity = getActivity();
        //    mShowPanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_show);
        //    mShowPanelAnimation.setAnimationListener((Animation.AnimationListener) getActivity());
        //    mHidePanelAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_hide);
        //    mHidePanelAnimation.setAnimationListener((Animation.AnimationListener) getActivity());

        //  Registration Layout Views
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

        img_fullscreen_search_bluetooth = (ImageView) view.findViewById(R.id.img_fullscreen_search_bluetooth);

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
        img_action_bar_icon_laundry_connected = (ImageView) view.findViewById(R.id.img_action_bar_icon_laundry_connected);
        img_action_bar_icon_lost_connected = (ImageView) view.findViewById(R.id.img_action_bar_icon_lost_connected);
        img_action_bar_icon_not_connected = (ImageView) view.findViewById(R.id.img_action_bar_icon_not_connected);
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

        //  money_tarif_valuta.setText("руб"); if (valuta==1)
        //  txt_layout_prepair_wash1.setText("Тариф"); name_mode
        //  txt_footer_prepair.setText("Перед оплатой включить машину"); или время стирки


        layout_smart_keyboard = (RelativeLayout) view.findViewById(R.id.layout_smart_keyboard);

        //  start.setEnabled(true);
        //  btn_keyboard_choose_prog.setEnabled(true);
        //  btn_keyboard_sms.setEnabled(true);
        //  btn_keyboard_temp.setEnabled(true);
        //  btn_keyboard_pay.setEnabled(true);
        //  btn_keyboard_creditcard.setEnabled(true);


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


        layout_settings_action_bar = (RelativeLayout) view.findViewById(R.id.layout_settings_action_bar);
        layout_settings_action_bar_leftmenu = (RelativeLayout) view.findViewById(R.id.layout_settings_action_bar_leftmenu);
        layout_settings_buttons = (RelativeLayout) view.findViewById(R.id.layout_settings_buttons);


        layout_bt_not = (RelativeLayout) view.findViewById(R.id.layout_bt_not);
        layout_bt_search = (RelativeLayout) view.findViewById(R.id.layout_bt_search);


        /**
         *   Set disable & invisible all Layout Views
         *
         */

        // закрываем предыдущий экран? и что со статусами bluetooth?
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_choose_mode.setVisibility(View.INVISIBLE);
        layout_start_anim.setVisibility(View.INVISIBLE);
        layout_settings.setVisibility(View.INVISIBLE);
        layout_bt_not.setVisibility(View.INVISIBLE);
        layout_bt_search.setVisibility(View.INVISIBLE);
        // VISIBLE LAYOUTS SCREEN
        layout_home.setVisibility(View.VISIBLE);
        // вложения
        mButton.setEnabled(true);
        layout_action_bar.setVisibility(View.VISIBLE);
        // убрать из бара номер машинки, еще не выбрана
        layout_action_bar.setVisibility(View.VISIBLE);
        img_action_bar_icon_wash.setVisibility(View.INVISIBLE);
        wash_number.setVisibility(View.INVISIBLE);
        // скрываем иконки bluetooth для самоопределения
        img_action_bar_icon_laundry_connected.setVisibility(View.INVISIBLE);
        img_action_bar_icon_lost_connected.setVisibility(View.INVISIBLE);
        img_action_bar_icon_not_connected.setVisibility(View.INVISIBLE);
        // показать внесено
        img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
        money_kup.setVisibility(View.VISIBLE);
        img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
        img_action_bar_icon_battary.setVisibility(View.INVISIBLE);

    }


    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

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


        //  -----------------------------  my code -----------------------------------------


        // Initialize the array adapter for the conversation thread
        statisticWash1ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message1);
        StatisticWash1.setAdapter(statisticWash1ArrayAdapter);
        statisticWash2ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message2);
        StatisticWash2.setAdapter(statisticWash2ArrayAdapter);
        statisticWash3ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message3);
        StatisticWash3.setAdapter(statisticWash3ArrayAdapter);
        statisticWash4ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message4);
        StatisticWash4.setAdapter(statisticWash4ArrayAdapter);
        statisticWash5ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message5);
        StatisticWash5.setAdapter(statisticWash5ArrayAdapter);
        statisticWash6ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message6);
        StatisticWash6.setAdapter(statisticWash6ArrayAdapter);
        statisticWash7ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message7);
        StatisticWash7.setAdapter(statisticWash7ArrayAdapter);
        statisticWash8ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message8);
        StatisticWash8.setAdapter(statisticWash8ArrayAdapter);
        statisticWash9ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message9);
        StatisticWash9.setAdapter(statisticWash9ArrayAdapter);
        statisticWash10ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message10);
        StatisticWash10.setAdapter(statisticWash10ArrayAdapter);
        statisticWash11ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message11);
        StatisticWash11.setAdapter(statisticWash11ArrayAdapter);
        statisticWash12ArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message12);
        StatisticWash12.setAdapter(statisticWash12ArrayAdapter);



        // Initialize the compose field with a listener for the return key
        //   mOutEditText.setOnEditorActionListener(mWriteListener);
        mOutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("settings")) {
                    display_settings_load();
                }

            }
        });


        // нажать на любое место экрана для перезапуска соединения
        img_fullscreen_search_bluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //    img_fullscreen_search_bluetooth.setEnabled(false); // защита от двойного нажатия
                //    img_fullscreen_search_bluetooth.setVisibility(View.GONE);
                //    counter_bluetooth_state++;
                if (counter_bluetooth_state >= 6) {
                    counter_bluetooth_state = 0;
                    // Launch the DeviceListActivity to see devices and do scan
                    Intent serverIntent = new Intent(getActivity(), ru.laundromat.washer.bluetoothchat.DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                } else {
                    reconnect_Device();
                }
            }
        });

        // ok button with a listener that for click events
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //  mButton.setEnabled(false); // защита от двойного нажатия
                if (mButtonCounter >= 5) {
                    dialog_error_button();
                    mButtonCounter = 0;
                } else {
                    mButtonCounter++;
                    sendMessage(CHECK_1_BT); // если нет соединения вызовет сообщение об ошибке

                }
            }
        });


        // кнопки prepair_to_start, третьего экрана

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                go_home();
            }
        });

        btn_numbers_select.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                numbers_selected(countwash);
            }
        });

        img_slide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_slide.setVisibility(View.VISIBLE);
                img_slide_back.setVisibility(View.VISIBLE);
                horizontalScrollView.scrollBy(0, +20);
                horizontalScrollView.fullScroll(View.FOCUS_RIGHT);


            }
        });

        img_slide_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                img_slide.setVisibility(View.VISIBLE);
                img_slide_back.setVisibility(View.VISIBLE);
                horizontalScrollView.setSmoothScrollingEnabled(true);
                horizontalScrollView.smoothScrollTo(-500, 0);
                //   horizontalScrollView.fullScroll(View.FOCUS_LEFT);
            }
        });

        btn_keyboard_choose_prog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                choose_mode();
            }
        });

        btn_keyboard_sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_out_of_service();
            }
        });

        btn_keyboard_temp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_out_of_service();
            }
        });

        btn_keyboard_pay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_out_of_service();
            }
        });

        btn_keyboard_creditcard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_out_of_service();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                start.setEnabled(false); // защита от двойного нажатия
                int num1st = Integer.parseInt(money_kup.getText().toString());
                int num2st = Integer.parseInt(money_tarif.getText().toString());
                if (num2st < 0) {
                    dialog_check_money_tarif_error();
                } else {
                    int resultst = (num1st - num2st);
                    if (resultst >= 0) {
                        sendMessage(CHECK_2_BT); // если нет соединения вызовет ошибку
                    } else {
                        start_error();
                    }
                }
            }
        });


        wash1_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash1_go.setEnabled(false);
                wash_number.setText("1");
                wash_number_int = 1;
                if (wash1crash == 1) {  // если машина сейчас стирает
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer1timer;
                        dialog_crash_downwatermode(); // слить воду?
                    }
                } else {
                    prepair_to_start(wash1remont, wash1isdry, set_program, set_mode);
                }
            }
        });

        wash2_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash2_go.setEnabled(false);
                wash_number.setText("2");
                wash_number_int = 2;
                if (wash2crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer2timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash2remont, wash2isdry, set_program, set_mode);
                }
            }
        });

        wash3_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash3_go.setEnabled(false);
                wash_number.setText("3");
                wash_number_int = 3;
                if (wash3crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer3timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash3remont, wash3isdry, set_program, set_mode);
                }
            }
        });

        wash4_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash4_go.setEnabled(false);
                wash_number.setText("4");
                wash_number_int = 4;
                if (wash4crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer4timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash4remont, wash4isdry, set_program, set_mode);
                }
            }
        });

        wash5_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash5_go.setEnabled(false);
                wash_number.setText("5");
                wash_number_int = 5;
                if (wash5crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer5timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash5remont, wash5isdry, set_program, set_mode);
                }
            }
        });

        wash6_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash6_go.setEnabled(false);
                wash_number.setText("6");
                wash_number_int = 6;
                if (wash6crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer6timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash6remont, wash6isdry, set_program, set_mode);
                }
            }
        });
        wash7_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash7_go.setEnabled(false);
                wash_number.setText("7");
                wash_number_int = 7;
                if (wash7crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer7timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash7remont, wash7isdry, set_program, set_mode);
                }
            }
        });
        wash8_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash8_go.setEnabled(false);
                wash_number.setText("8");
                wash_number_int = 8;
                if (wash8crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer8timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash8remont, wash8isdry, set_program, set_mode);
                }
            }
        });
        wash9_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash9_go.setEnabled(false);
                wash_number.setText("9");
                wash_number_int = 9;
                if (wash9crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer9timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash9remont, wash9isdry, set_program, set_mode);
                }
            }
        });
        wash10_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash10_go.setEnabled(false);
                wash_number.setText("10");
                wash_number_int = 10;
                if (wash10crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer10timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash10remont, wash10isdry, set_program, set_mode);
                }
            }
        });
        wash11_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы если запуск по режимам открыть окно выбора режимов
                wash11_go.setEnabled(false);
                wash_number.setText("11");
                wash_number_int = 11;
                if (wash11crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer11timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash11remont, wash11isdry, set_program, set_mode);
                }
            }
        });
        wash12_go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 0; // чтобы, если запуск по режимам, открыть окно выбора режимов
                wash12_go.setEnabled(false);
                wash_number.setText("12");
                wash_number_int = 12;
                if (wash12crash == 1) {
                    if ((set_program == 1)||(set_program == 2)) {
                        washertimer = washer12timer;
                        dialog_crash_downwatermode();
                    }
                } else {
                    prepair_to_start(wash12remont, wash12isdry, set_program, set_mode);
                }
            }
        });


        // кнопки второго экрана, когда выбор программы ---------------------------------------
        choose1mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 1;
                auto_prepair_to_start();
            }
        });

        choose2mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 2;
                auto_prepair_to_start();
            }
        });

        choose3mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 3;
                auto_prepair_to_start();
            }
        });

        choose4mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 4;
                auto_prepair_to_start();
            }
        });

        choose5mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 5;
                auto_prepair_to_start();
            }
        });

        choose6mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                set_mode = 6;
                auto_prepair_to_start();
            }
        });

// кнопки второго экрана конец, когда выбор программы ---------------------------------------


// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
// ----------------------------------- кнопки меню начало ------------------------------------------
// -------------------------------------------------------------------------------------------------
// -------------------------------------------------------------------------------------------------
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
                            if (set_mode >= 1) {
                                if (set_mode >= 6) {
                                    set_mode = 1;
                                    auto_prepair_to_start();
                                } else {
                                    set_mode++;
                                    auto_prepair_to_start();
                                }
                            }
                        } else {

                        }
                    }
                }
                return true;
            }
        });

        // ********************************************************************************>
        // PARAMETERS LAYOUT ******************** ШТОРКА **********************************>
        // ********************************************************************************>

        layout_settings.setOnTouchListener(new OnTouchListener() {
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
                        if (endX < mStartX) {
                            FragmentActivity activity = getActivity();
                            final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_hide);
                            if (layout_settings_action_bar_leftmenu.getVisibility() == View.VISIBLE) {
                                layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
                                layout_settings_action_bar_leftmenu.setVisibility(View.GONE);
                            }
                            final Animation animationRotateCenterPopapButton = AnimationUtils.loadAnimation(
                                    activity, R.anim.popup_btn);
                            img_settings_action_bar_icon_view_leftmenu.setVisibility(View.VISIBLE);
                            img_settings_action_bar_icon_view_leftmenu.startAnimation(animationRotateCenterPopapButton);
                        } else {
                            FragmentActivity activity = getActivity();
                            final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_show);
                            if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                                layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // ОТОБРАЖАЕМ
                                layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                return true;
            }
        });

        // если нажали на horisontScroll машинки, то прячем шторку
        layout_settings_wash_number.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // прячем  шторку если мы в настройках и она видна
                FragmentActivity activity = getActivity();
                final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_hide);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.VISIBLE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
                    layout_settings_action_bar_leftmenu.setVisibility(View.GONE);
                }
            }
        });

        // если нажали на фон статистики, то показываем шторку
        layout_settings_stat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // отображаем  шторку если мы в настройках и она скрыта
                FragmentActivity activity = getActivity();
                final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_show);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // ОТОБРАЖАЕМ
                    layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
                }
            }
        });
        // если нажали на фон раздела машины, то показываем шторку
        layout_settings_washer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // отображаем  шторку если мы в настройках и она скрыта
                FragmentActivity activity = getActivity();
                final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_show);
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // ОТОБРАЖАЕМ
                    layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
                }
            }
        });

        // теперь эта кнопка сворачивает left menu (ШТОРКУ)
        btn_settings_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentActivity activity = getActivity();
                final Animation mShowPanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_show);
                final Animation mHidePanelAnimation = AnimationUtils.loadAnimation(activity, R.anim.popup_hide);
                // шторка
                if (layout_settings_action_bar_leftmenu.getVisibility() == View.GONE) {
                    layout_settings_action_bar_leftmenu.startAnimation(mShowPanelAnimation); // показываем
                    layout_settings_action_bar_leftmenu.setVisibility(View.VISIBLE);
                    //  layout_settings.setBackgroundResource(R.drawable.settings_menu_background);
                } else {
                    layout_settings_action_bar_leftmenu.startAnimation(mHidePanelAnimation); // прячем
                    layout_settings_action_bar_leftmenu.setVisibility(View.GONE);
                }
            }
        });


        // ********************************************************************************
        // SETTINGS BUTTON ******************** ШТОРКА ************************************
        // ********************************************************************************

        // (1) --- НАЖИМАЕМ КНОПКУ  "МАШИНЫ"  НА ШТОРКЕ ------------------------------- (1)
        btn_settings_washer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(1, 0); // очищаем шторку после предыдущего раздела
                layout_settings_wash_number.setVisibility(View.VISIBLE);  }});

        // (2) --- НАЖИМАЕМ КНОПКУ  "УТЮГИ"  НА ШТОРКЕ ------------------------------- (2)
        btn_settings_iron.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(2, 1); // очищаем шторку после предыдущего раздела
                // утюг 2 вздрогнет, так динамичнее
                FragmentActivity activity = getActivity();
                final Animation mIronLoadAnimation = AnimationUtils.loadAnimation(
                        activity, R.anim.iron_load);
                iron_view2.setVisibility(View.VISIBLE);
                iron_view2.startAnimation(mIronLoadAnimation);
            }});

        // (3) --- НАЖИМАЕМ КНОПКУ  "СМС"  НА ШТОРКЕ -------------------------------- (3)
        btn_settings_sms.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(3, 1);  }});

        // (4) --- НАЖИМАЕМ КНОПКУ  "BLUETOOTH"  НА ШТОРКЕ ------------------------- (4)
        btn_settings_bluetooth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(4, 1);   }});

        // (5) --- НАЖИМАЕМ КНОПКУ  "ТАРИФЫ"  НА ШТОРКЕ --------------------------- (5)
        btn_settings_tarif.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(5, 1);   }});

        // (6) --- НАЖИМАЕМ КНОПКУ  "СИСТЕМА"  НА ШТОРКЕ ------------------------- (6)
        btn_settings_system.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(6, 0);      }
        });

        // (7) --- НАЖИМАЕМ КНОПКУ  "СТАТИСТИКА"  НА ШТОРКЕ ---------------------- (7)
        btn_settings_stat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(7, 0);   }});

        // (8) --- НАЖИМАЕМ КНОПКУ  "ВЫХОД"  НА ШТОРКЕ --------------------------- (8)
        btn_settings_exit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // step_leftmenu(8, 0);
                dialog_exit_settings();
            }
        });

// ********************************************************************************
// SETTINGS BUTTONS ******************** ШТОРКА КОНЕЦ *****************************
// ********************************************************************************


//                        **********************************
//                        **    КНОПКИ ТИТУЛЬНОГО МЕНЮ    **
//                        **********************************


        // (1) --- НАЖИМАЕМ КНОПКУ  "СТАТИСТИКА"  НА ТИТУЛЬНОМ ЭКРАНЕ МЕНЮ --- (1)
        btn1_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(7, 0); // очищаем шторку после предыдущего раздела
            }
        });
        // (2) --- НАЖИМАЕМ КНОПКУ    "ТАРИФЫ"    НА ТИТУЛЬНОМ ЭКРАНЕ МЕНЮ --- (2)
        btn2_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(5, 1); // очищаем шторку после предыдущего раздела
            }
        });
        // (3) --- НАЖИМАЕМ КНОПКУ   "МАШИНКИ"    НА ТИТУЛЬНОМ ЭКРАНЕ МЕНЮ --- (3)
        btn3_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(1, 0); // очищаем шторку после предыдущего раздела
            }
        });
        // (4) --- НАЖИМАЕМ КНОПКУ     "СМС"      НА ТИТУЛЬНОМ ЭКРАНЕ МЕНЮ --- (4)
        btn4_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(3, 1); // очищаем шторку после предыдущего раздела
            }
        });
        // (5) --- НАЖИМАЕМ КНОПКУ   "СИСТЕМА"    НА ТИТУЛЬНОМ ЭКРАНЕ МЕНЮ --- (5)
        btn5_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(6, 0); // убираем шторку после предыдущего раздела
            } });
        // (6) --- НАЖИМАЕМ КНОПКУ    "УТЮГИ"     НА ТИТУЛЬНОМ ЭКРАНЕ МЕНЮ --- (6)
        btn6_settings_choose.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                step_leftmenu(2, 1); // очищаем шторку после предыдущего раздела
                // утюг 2 вздрогнет, так динамичнее
                FragmentActivity activity = getActivity();
                final Animation mIronLoadAnimation = AnimationUtils.loadAnimation(
                        activity, R.anim.iron_load);
                iron_view2.setVisibility(View.VISIBLE);
                iron_view2.startAnimation(mIronLoadAnimation);
            }
        });

//                            ****************************************
//                            **    КНОПКИ ТИТУЛЬНОГО МЕНЮ КОНЕЦ    **
//                            ****************************************



//               |----------------------------------------------------------------|
//               |----------------------*********************---------------------|
//               |----------------------*    КНОПКИ МЕНЮ    *---------------------|
//               |----------------------*********************---------------------|
//               |----------------------------------------------------------------|


        //-------------------------------------------------------------------------------- \\
        // ---------------------------> РАЗДЕЛ МАШИНЫ <----------------------------------- \\
        //-------------------------------------------------------------------------------- \\
        // (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ" НА ЭКРАНЕ МАШИНОК ----------------(1)-OK!
        btn_settings_washer_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer.class);
                startActivity(intent);
                //     startActivityForResult(i, REQUEST_BTT);
            }
        });
        // (1.1) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 1" НА ЭКРАНЕ МАШИНОК ----------------(1)-OK!
        num1_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer1.class);
                startActivity(intent);
            }
        });
        // (1.2) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 2" НА ЭКРАНЕ МАШИНОК ----------------(2)-OK!
        num2_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer2.class);
                startActivity(intent);
            }
        });
        // (1.3) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 3" НА ЭКРАНЕ МАШИНОК ----------------(3)-OK!
        num3_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer3.class);
                startActivity(intent);
            }
        });
        // (1.4) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 4" НА ЭКРАНЕ МАШИНОК ----------------(4)-OK!
        num4_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer4.class);
                startActivity(intent);
            }
        });
        // (1.5) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 5" НА ЭКРАНЕ МАШИНОК ----------------(5)-OK!
        num5_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer5.class);
                startActivity(intent);
            }
        });
        // (1.6) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 6" НА ЭКРАНЕ МАШИНОК ----------------(6)-OK!
        num6_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer6.class);
                startActivity(intent);
            }
        });
        // (1.7) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 7" НА ЭКРАНЕ МАШИНОК ----------------(7)-OK!
        num7_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer7.class);
                startActivity(intent);
            }
        });
        // (1.8) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 8" НА ЭКРАНЕ МАШИНОК ----------------(8)-OK!
        num8_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer8.class);
                startActivity(intent);
            }
        });
        // (1.9) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 9" НА ЭКРАНЕ МАШИНОК ----------------(9)-OK!
        num9_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer9.class);
                startActivity(intent);
            }
        });
        // (1.10) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 10" НА ЭКРАНЕ МАШИНОК ----------------(10)-OK!
        num10_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer10.class);
                startActivity(intent);
            }
        });
        // (1.11) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 11" НА ЭКРАНЕ МАШИНОК ----------------(11)-OK!
        num11_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer11.class);
                startActivity(intent);
            }
        });
        // (1.12) НАЖИМАЕМ КНОПКУ "СТИРАЛЬНАЯ МАШИНА 12" НА ЭКРАНЕ МАШИНОК ----------------(12)-OK!
        num12_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_washer12.class);
                startActivity(intent);
            }
        });

        // ------------------------------------------------------------------------------- \\
        // -----------------------------> СМС РАЗДЕЛ <------------------------------------ \\
        // ------------------------------------------------------------------------------- \\
        // (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ" НА ЭКРАНЕ СМС НАСТРОЕК -----------(1)-OK!
        btn_settings_sms_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_sms.class);
                startActivity(intent);
            }
        });
        // (2) НАЖИМАЕМ КНОПКУ    "ТЕЛ. ДИРЕКТОРА" НА  ЭКРАНЕ СМС НАСТРОЕК ----------(2)-OK!
        btn_settings_sms_director.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_sms_director.class);
                startActivity(intent);
            }
        });
        // (3) НАЖИМАЕМ КНОПКУ    "ТЕЛ. МЕНЕДЖЕРА" НА  ЭКРАНЕ СМС НАСТРОЕК ----------(3)-OK!
        btn_settings_sms_manager.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_sms_client.class);
                startActivity(intent);
            }
        });
        // (4) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ ЭЛ-ВО" НА ЭКРАНЕ СМС НАСТРОЕК -------------(4)-OK!
        btn_settings_sms_otchet_power.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_sms_error_volts.class);
                startActivity(intent);
            }
        });
        // (5) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ О ПОЛОМКЕ" НА  ЭКРАНЕ СМС НАСТРОЕК --------(5)-OK!
        btn_settings_sms_otchet_admin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_sms_error_door.class);
                startActivity(intent);
            }
        });
        // (5) НАЖИМАЕМ КНОПКУ "СМС ОТЧЕТ О ПОЛОМКЕ" НА  ЭКРАНЕ СМС НАСТРОЕК --------(5)-OK!
        btn_settings_sms_otchet_client.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_sms_client.class);
                startActivity(intent);
            }
        });

        // ------------------------------------------------------------------------------ \\
        // ---------------------------> РАЗДЕЛ BLUETOOTH <------------------------------- \\
        // ------------------------------------------------------------------------------ \\
        // (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ"  НА ЭКРАНЕ BLUETOOTH НАСТРОЕК ---(1)-OK!
        btn_settings_bluetooth_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // dialog_out_of_service();

            }
        });
        // (2) НАЖИМАЕМ КНОПКУ "BLUETOOTH ADAPTER" ----------------------------------(2)-OK!
        btn_settings_bluetooth_adapter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_bluetooth_adapter();
                if (!mBluetoothAdapter.isEnabled()) {
                    btn_settings_bluetooth_adapter.setChecked(false);
                } else { btn_settings_bluetooth_adapter.setChecked(true); }
            }
        });
        // (3) НАЖИМАЕМ КНОПКУ "BLUETOOTH SEARCH LIST" ------------------------------(3)-OK!
        btn_settings_bluetooth_list.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
            }
        });
        // (4) НАЖИМАЕМ КНОПКУ "BLUETOOTH RECONNECT" --------------------------------(4)-OK!
        btn_settings_bluetooth_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reconnect_Device();
            }
        });

        // ------------------------------------------------------------------------------ \\
        // ---------------------------- РАЗДЕЛ ТАРИФЫ ----------------------------------- \\
        // ------------------------------------------------------------------------------ \\
        // (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ"  НА ЭКРАНЕ ТАРИФОВ --------------(1)-OK!
        btn_settings_tarif_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_tarif.class);
                startActivity(intent);
            }
        });
        // (2) НАЖИМАЕМ КНОПКУ "TARIF VALUTA"  --------------------------------------(2)-OK!
        btn_settings_tarif_valuta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //    Intent intent = new Intent(getActivity(), PrefActivity_sms_valuta.class);
                //    startActivity(intent);
            }
        });
        // (3) НАЖИМАЕМ КНОПКУ "TARIF SBER" -----------------------------------------(3)-OK!
        btn_settings_tarif_sber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // dialog_out_of_service();
                Intent intent = new Intent(getActivity(), PrefActivity_tarif_sber.class);
                startActivity(intent);
            }
        });
        // (4) НАЖИМАЕМ КНОПКУ "TARIF QIWI" -----------------------------------------(4)-OK!
        btn_settings_tarif_qiwi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity_tarif_qiwi.class);
                startActivity(intent);
            }
        });
        // (5) НАЖИМАЕМ КНОПКУ "TARIF NAME" -----------------------------------------(5)-OK!
        btn_settings_mode_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_tarif_name.class);
                startActivity(intent);
            }
        });
        // (6) НАЖИМАЕМ КНОПКУ "TARIF IRON" -----------------------------------------(6)-OK!
        btn_settings_tarif_iron.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_out_of_service();
            }
        });
        // (7) НАЖИМАЕМ КНОПКУ "TARIF MODE" -----------------------------------------(7)-OK!
        btn_settings_tarif_mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_tarif_mode.class);
                startActivity(intent);
            }
        });
        // (8) НАЖИМАЕМ КНОПКУ "TARIF WASH" -----------------------------------------(8)-OK!
        btn_settings_tarif_wash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_tarif_machine.class);
                startActivity(intent);
            }
        });
        // (9) НАЖИМАЕМ КНОПКУ "TARIF YANDEX" ---------------------------------------(9)-OK!
        btn_settings_tarif_yandex.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        PrefActivity_tarif_yandex.class);
                startActivity(intent);
            }
        });

        // ----------------------------------------------------------------------------- \\
        // ---------------------------- РАЗДЕЛ СИСТЕМА --------------------------------- \\
        // ----------------------------------------------------------------------------- \\
        // (1) НАЖИМАЕМ КНОПКУ "ОСНОВНЫЕ НАСТРОЙКИ"  НА ЭКРАНЕ СИСТЕМНЫХ НАСТРОЕК --(1)-OK!
        btn_settings_system_base.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PrefActivity.class);
                startActivity(intent);
            }
        });
        // (2) НАЖИМАЕМ КНОПКУ "Programm.COM"  НА ЭКРАНЕ СИСТЕМНЫХ НАСТРОЕК --------(2)-OK!
        btn_settings_system_com.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout_log.setVisibility(View.VISIBLE);
                btn_close_layout_com.setVisibility(View.VISIBLE);
                dialog_program_com();
            }
        });
        // (2.1) НАЖИМАЕМ КНОПКУ "ЗАКРЫТЬ Programm.COM"  В СИСТЕМНЫХ НАСТРОЙКАХ ---(2.1)OK!
        btn_close_layout_com.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layout_log.setVisibility(View.INVISIBLE);
                layout_settings_system.setVisibility(View.VISIBLE);
            }
        });
        // (3) НАЖИМАЕМ КНОПКУ "SYSTEM LOG.e"  НА ЭКРАНЕ СИСТЕМНЫХ НАСТРОЕК --------(3)-OK!
        btn_settings_system_log_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // видим прозрачный экран, значит видим фрагмент лога
                layout_settings_system.setVisibility(View.INVISIBLE);
                //   layout_settings.setBackgroundResource(R.drawable.wash_ready_background);
                layout_settings_action_bar_leftmenu.setVisibility(View.GONE);
                layout_log.setVisibility(View.INVISIBLE);
                layout_system_log.setVisibility(View.VISIBLE);
                // параметр

                // основное меню
                layout_settings_action_bar.setVisibility(View.VISIBLE);
                displayInfo();
                if (statusStr.contentEquals("1")){
                    img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
                    img_settings_action_bar_icon_battary.setVisibility(View.GONE);
                } else {
                    img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
                    img_settings_action_bar_icon_electro.setVisibility(View.GONE);
                }

            }
        });

        // (4) НАЖИМАЕМ КНОПКУ "FULLSCREEN"  НА ЭКРАНЕ СИСТЕМНЫХ НАСТРОЕК --------(4)-OK!
        btn_settings_system_fullscreen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mHideSystemBar == 0) {
                    mHideSystemBar = 1;
                    hideSystemBar();
                } else {
                    mHideSystemBar = 0;
                    showSystemBar();
                    dialog_fullscreen_open();
                }
            }
        });



        // ----------------------------------------------------------------------------- \\
        // --------------------------- РАЗДЕЛ СТАТИСТИКА ------------------------------- \\
        // ----------------------------------------------------------------------------- \\
        // (1) НАЖИМАЕМ КНОПКУ "инкассация" НА ЭКРАНЕ НАСТРОЕК СТАТИСТИКА  ---------(1)-OK!
        btn_settings_stat_inkass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog_out_of_service();
            }
        });


//               |-----------------------------------------------------------------|
//               |-----------------------*********************---------------------|
//               |-----------------------* КНОПКИ МЕНЮ КОНЕЦ *---------------------|
//               |-----------------------*********************---------------------|
//               |-----------------------------------------------------------------|





        // Подключаем файл сохраненных настроек
        FragmentActivity activity = getActivity();
        mSettings = activity.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        // Вспоминаем данные мак-адреса
        if (mSettings.contains(APP_PREFERENCES_MACADDRESS)) {
            mac_address = mSettings.getString(APP_PREFERENCES_MACADDRESS, "mac_address");
            // macaddress_txt.setText(mSettings.getString(APP_PREFERENCES_MACADDRESS, "mac_address"));
        }
        // Вспоминаем данные внесенных денег клиента
        if (mSettings.contains(APP_PREFERENCES_MONEYVNESENO)) {
            money_vneseno = mSettings.getInt(APP_PREFERENCES_MONEYVNESENO, 0);
        }
        // Вспоминаем данные внесенных денег статистика
        if (mSettings.contains(APP_PREFERENCES_MONEY)) {
            money = mSettings.getInt(APP_PREFERENCES_MONEY, 0);
        }

        // Вспоминаем счетчики машин для статистики
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTER)) {
            wash1statcounter = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTER)) {
            wash2statcounter = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTER)) {
            wash3statcounter = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTER)) {
            wash4statcounter = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTER)) {
            wash5statcounter = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTER)) {
            wash6statcounter = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTER, 0);
        }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTER)) {
            wash7statcounter = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTER, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTER)) {
            wash8statcounter = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTER, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTER)) {
            wash9statcounter = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTER, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTER)) {
            wash10statcounter = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTER, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTER)) {
            wash11statcounter = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTER, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTER)) {
            wash12statcounter = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTER, 0); }

        // статистика по смс запускам
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERSMS)) {
            wash1statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERSMS)) {
            wash2statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERSMS)) {
            wash3statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERSMS)) {
            wash4statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERSMS)) {
            wash5statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERSMS)) {
            wash6statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERSMS)) {
            wash7statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERSMS)) {
            wash8statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERSMS)) {
            wash9statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERSMS)) {
            wash10statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERSMS)) {
            wash11statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERSMS, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERSMS)) {
            wash12statcounter_sms = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERSMS, 0); }

        // статистика - запуски по режиму 1 - быстрая
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE1)) {
            wash1statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE1)) {
            wash2statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE1)) {
            wash3statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE1)) {
            wash4statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE1)) {
            wash5statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE1)) {
            wash6statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE1)) {
            wash7statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE1)) {
            wash8statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE1)) {
            wash9statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE1)) {
            wash10statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE1)) {
            wash11statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE1, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE1)) {
            wash12statcounter_mode1 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE1, 0); }

        // статистика - запуски по режиму 2 - интенсивная
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE2)) {
            wash1statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE2)) {
            wash2statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE2)) {
            wash3statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE2)) {
            wash4statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE2)) {
            wash5statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE2)) {
            wash6statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE2)) {
            wash7statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE2)) {
            wash8statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE2)) {
            wash9statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE2)) {
            wash10statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE2)) {
            wash11statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE2, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE2)) {
            wash12statcounter_mode2 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE2, 0); }

        // статистика - запуски по режиму 3 - хлопок
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE3)) {
            wash1statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE3)) {
            wash2statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE3)) {
            wash3statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE3)) {
            wash4statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE3)) {
            wash5statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE3)) {
            wash6statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE3)) {
            wash7statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE3)) {
            wash8statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE3)) {
            wash9statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE3)) {
            wash10statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE3)) {
            wash11statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE3, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE3)) {
            wash12statcounter_mode3 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE3, 0); }

        // статистика - запуски по режиму 4 - шерсть
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE4)) {
            wash1statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE4)) {
            wash2statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE4)) {
            wash3statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE4)) {
            wash4statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE4)) {
            wash5statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE4)) {
            wash6statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE4)) {
            wash7statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE4)) {
            wash8statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE4)) {
            wash9statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE4)) {
            wash10statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE4)) {
            wash11statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE4, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE4)) {
            wash12statcounter_mode4 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE4, 0); }

        // статистика - запуски по режиму 5 - синтетика
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE5)) {
            wash1statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE5)) {
            wash2statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE5)) {
            wash3statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE5)) {
            wash4statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE5)) {
            wash5statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE5)) {
            wash6statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE5)) {
            wash7statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE5)) {
            wash8statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE5)) {
            wash9statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE5)) {
            wash10statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE5)) {
            wash11statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE5, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE5)) {
            wash12statcounter_mode5 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE5, 0); }

        // статистика - запуски по режиму 6 - полоскание
        if (mSettings.contains(APP_PREFERENCES_WASH1STATCOUNTERMODE6)) {
            wash1statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH1STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH2STATCOUNTERMODE6)) {
            wash2statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH2STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH3STATCOUNTERMODE6)) {
            wash3statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH3STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH4STATCOUNTERMODE6)) {
            wash4statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH4STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH5STATCOUNTERMODE6)) {
            wash5statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH5STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH6STATCOUNTERMODE6)) {
            wash6statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH6STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH7STATCOUNTERMODE6)) {
            wash7statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH7STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH8STATCOUNTERMODE6)) {
            wash8statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH8STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH9STATCOUNTERMODE6)) {
            wash9statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH9STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH10STATCOUNTERMODE6)) {
            wash10statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH10STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH11STATCOUNTERMODE6)) {
            wash11statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH11STATCOUNTERMODE6, 0); }
        if (mSettings.contains(APP_PREFERENCES_WASH12STATCOUNTERMODE6)) {
            wash12statcounter_mode6 = mSettings.getInt(APP_PREFERENCES_WASH12STATCOUNTERMODE6, 0); }

        // выводим данные о деньгах на экран
        money_kup.setText("" + money_vneseno);

        boolean hasVisited = mSettings.getBoolean("hasVisited", false);
        if (!hasVisited) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean("hasVisited", true);
            editor.commit();
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        } else {
            reconnect_Device();
        }






// end line Runnuble UI -----------------------------SetupChat-----------------------------------
    }



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
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            mButton.setEnabled(true);
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
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
                String message = view.getText().toString();
                sendMessage(message);
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


    public void battary_module(int param) {
        if (param==1){
            //     displayInfo();
            //     if (statusStr.contentEquals("1")){
            img_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_action_bar_icon_battary.setVisibility(View.INVISIBLE);
            img_settings_action_bar_icon_battary.setVisibility(View.INVISIBLE);
            img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
            //      } else {
            //          img_action_bar_icon_battary.setVisibility(View.VISIBLE);
            //          img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
            //      }
            //   img_action_bar_icon_electro.setVisibility(View.VISIBLE);
            //   img_action_bar_icon_battary.setVisibility(View.INVISIBLE);

            Toast.makeText(getActivity(), "Устройство заряжается", Toast.LENGTH_LONG).show();
        }
        if (param==2){
            SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
            sms.sendTextMessage("", null, "Терминал: Уровень батареи 20%", null, null);}
        if (param==3){
            if (statusStr.contentEquals("1")){
                img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                img_action_bar_icon_battary.setVisibility(View.INVISIBLE);
            } else {
                img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
                img_settings_action_bar_icon_electro.setVisibility(View.INVISIBLE);
                img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
            }
            //    img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
            //    img_action_bar_icon_battary.setVisibility(View.VISIBLE);
            //    img_settings_action_bar_icon_electro.setVisibility(View.INVISIBLE);
            //    img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Устройство не заряжается", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Закрываем все и обратно на первый экран
     * Set disable & invisible all Layout Views
     * кроме статусов Bluetooth и кнопки OK первого экрана
     */
    private void go_home() {

        // VISIBLE LAYOUTS SCREEN
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_choose_mode.setVisibility(View.INVISIBLE);
        layout_start_anim.setVisibility(View.INVISIBLE);
        layout_bt_search.setVisibility(View.INVISIBLE);
        layout_settings.setVisibility(View.INVISIBLE);
        layout_bt_not.setVisibility(View.INVISIBLE);

        // определяем фон главной страницы для возврата
        layout_home.setVisibility(View.VISIBLE);
        // параметры активируем кнопку "Ok"
        mButton.setVisibility(View.VISIBLE);
        mButton.setEnabled(true);

        // запускаем бар
        layout_action_bar.setVisibility(View.VISIBLE);
        // параметры бара
        img_action_bar_icon_wash.setVisibility(View.INVISIBLE);
        wash_number.setVisibility(View.INVISIBLE);
        img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
        money_kup.setVisibility(View.VISIBLE);
        // ставим иконку Connected Bluetooth, так как сюда по другому не попасть
        img_action_bar_icon_not_connected.setVisibility(View.INVISIBLE);
        img_action_bar_icon_lost_connected.setVisibility(View.INVISIBLE);
        img_action_bar_icon_laundry_connected.setVisibility(View.VISIBLE);
        // проверяем батареечку
        displayInfo();
        if (statusStr.contentEquals("1")){
            img_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_action_bar_icon_battary.setVisibility(View.INVISIBLE);
        } else {
            img_action_bar_icon_battary.setVisibility(View.VISIBLE);
            img_action_bar_icon_electro.setVisibility(View.INVISIBLE);
        }

    }



    private void prepare_flipper_goto_numbers(){
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
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
     * @param remont            (0-off, 1-on)
     * @param washisdry(0-wash,1-dry)
     * @param program(0-simple start,1-program, 2-programm_temperatura)
     * @param washmode          (0-open choose_mode screen, 1-mode1, 2-mode2, ..)
     */
    private void prepair_to_start(int remont, int washisdry, int program, int washmode) {

        // VISIBLE LAYOUTS SCREEN
        // определяем фон главной страницы
        layout_prepair_start.setVisibility(View.VISIBLE);
        // вложенные фреймы
        layout1.setVisibility(View.VISIBLE);
        // вложенные объекты
        wash_mode.setVisibility(View.VISIBLE);
        money_tarif.setVisibility(View.VISIBLE);
        money_tarif_valuta.setVisibility(View.VISIBLE);
        // parameters
        if (valuta==1){ money_tarif_valuta.setText("руб"); }
        if (valuta==2){ money_tarif_valuta.setText("euro"); }
        if (valuta==3){ money_tarif_valuta.setText("руб"); }
        if (valuta==4){ money_tarif_valuta.setText("тенге"); }
        // Название режима по умолчанию
        txt_layout_prepair_wash1.setVisibility(View.VISIBLE);
        txt_layout_prepair_wash1.setText("Тариф"); // name_mode


        // фон по умолчанию для сушки, все остальные скрыты
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


        // первоначальная настройка клавиатуры
        // видны все кнопки, но активны только старт, отмена и сменить машину!

        btn_keyboard_choose_prog.setVisibility(View.VISIBLE);
        btn_keyboard_sms.setVisibility(View.VISIBLE);
        btn_keyboard_temp.setVisibility(View.VISIBLE);
        btn_keyboard_pay.setVisibility(View.VISIBLE);
        btn_keyboard_creditcard.setVisibility(View.VISIBLE);
        start.setVisibility(View.GONE);
        start.setEnabled(false);
        btn_keyboard_choose_prog.setEnabled(false);
        // состояние кнопок берем из наличия подключенных опций
        if(sms_client_answ==1){ btn_keyboard_sms.setEnabled(true);}
        else{btn_keyboard_sms.setEnabled(false);}
        if(program == 2){ btn_keyboard_temp.setEnabled(true);}
        else{btn_keyboard_temp.setEnabled(false);}
        if(sberbank==1){ btn_keyboard_pay.setEnabled(true);}
        else{btn_keyboard_pay.setEnabled(false);}
        if(creditcard==1){btn_keyboard_creditcard.setEnabled(true);}
        else{btn_keyboard_creditcard.setEnabled(false);}

        // VISIBLE LAYOUTS SCREEN
        layout_number_selected.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        layout_bt_search.setVisibility(View.GONE);
        layout_settings.setVisibility(View.GONE);
        layout_bt_not.setVisibility(View.GONE);
        layout_home.setVisibility(View.GONE);
        // запускаем бар
        layout_action_bar.setVisibility(View.VISIBLE);
        // параметры бара ----------------------
        img_action_bar_icon_wash.setVisibility(View.VISIBLE);
        wash_number.setVisibility(View.VISIBLE);
        img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
        money_kup.setVisibility(View.VISIBLE);
        img_action_bar_icon_not_connected.setVisibility(View.GONE);
        img_action_bar_icon_lost_connected.setVisibility(View.GONE);
        img_action_bar_icon_laundry_connected.setVisibility(View.VISIBLE);
        img_action_bar_icon_electro.setVisibility(View.GONE);
        img_action_bar_icon_battary.setVisibility(View.GONE);
        // параметры бара конец ----------------
        // открываем клавиатуру
        final Animation smart_buttons_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_buttons);
        layout_smart_keyboard.startAnimation(smart_buttons_Animation);
        //  layout_smart_keyboard.setVisibility(View.VISIBLE);
        // Подпись внизу по умолчанию
        // txt_footer_prepair.setVisibility(View.VISIBLE);
        txt_footer_prepair.setText("Внимание! Перед оплатой включить машину."); //или время стирки
        final Animation txt_Animation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
        txt_footer_prepair.startAnimation(txt_Animation);





        if (remont == 1) { // исключительный вариант, убираем все
            // определяем фон
            prepair_background_remont1.setVisibility(View.VISIBLE);
            // устанавливаем индивидуальную клавиатуру
            start.setVisibility(View.INVISIBLE);
            start.setEnabled(false);
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
            // закрываем все остальное
            layout1.setVisibility(View.INVISIBLE);

        } else { // if remont off

            if (program == 0) { // if simple start washing

                if (wash_number_int == 1) {
                    money_tarif.setText("" + wash1tarif);
                    prepair_background1.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 2) {
                    money_tarif.setText("" + wash2tarif);
                    prepair_background2.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 3) {
                    money_tarif.setText("" + wash3tarif);
                    prepair_background3.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 4) {
                    money_tarif.setText("" + wash4tarif);
                    prepair_background1.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 5) {
                    money_tarif.setText("" + wash5tarif);
                    prepair_background2.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 6) {
                    money_tarif.setText("" + wash6tarif);
                    prepair_background3.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 7) {
                    money_tarif.setText("" + wash7tarif);
                    prepair_background1.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 8) {
                    money_tarif.setText("" + wash8tarif);
                    prepair_background2.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 9) {
                    money_tarif.setText("" + wash9tarif);
                    prepair_background3.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 10) {
                    money_tarif.setText("" + wash10tarif);
                    prepair_background1.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 11) {
                    money_tarif.setText("" + wash11tarif);
                    prepair_background2.setVisibility(View.VISIBLE);
                } else if (wash_number_int == 12) {
                    money_tarif.setText("" + wash12tarif);
                    prepair_background3.setVisibility(View.VISIBLE);
                }
                start.setEnabled(true);
                start.setVisibility(View.VISIBLE);
                if (washisdry == 1) {
                    // Название режима по умолчанию
                    txt_layout_prepair_wash1.setText("Сушка"); // name_mode
                    prepair_background_dry1.setVisibility(View.VISIBLE); }


            } else { // if program power, start, mode, temp

                // Подпись внизу скрываем
                txt_footer_prepair.setVisibility(View.INVISIBLE);
                // клавиатура изменяется
                btn_keyboard_choose_prog.setEnabled(true);
                btn_keyboard_temp.setEnabled(false);

                if (washmode == 0) { // open choose_mode screen

                    if ((washisdry == 1) && (washisdry_program == 0)) {
                        // Название режима по умолчанию
                        txt_layout_prepair_wash1.setText("Сушка"); // name_mode
                        // Подпись внизу скрываем
                        txt_footer_prepair.setVisibility(View.VISIBLE);
                        // клавиатура изменяется
                        btn_keyboard_choose_prog.setEnabled(false);
                        btn_keyboard_temp.setEnabled(false);
                        // Фон сушки
                        prepair_background_dry1.setVisibility(View.VISIBLE);
                        // теперь фон и тарифы
                        if (wash_number_int == 1) { money_tarif.setText("" + wash1tarif); }
                        else if (wash_number_int == 2) { money_tarif.setText("" + wash2tarif); }
                        else if (wash_number_int == 3) { money_tarif.setText("" + wash3tarif); }
                        else if (wash_number_int == 4) { money_tarif.setText("" + wash4tarif); }
                        else if (wash_number_int == 5) { money_tarif.setText("" + wash5tarif); }
                        else if (wash_number_int == 6) { money_tarif.setText("" + wash6tarif); }
                        else if (wash_number_int == 7) { money_tarif.setText("" + wash7tarif); }
                        else if (wash_number_int == 8) { money_tarif.setText("" + wash8tarif); }
                        else if (wash_number_int == 9) { money_tarif.setText("" + wash9tarif); }
                        else if (wash_number_int == 10) { money_tarif.setText("" + wash10tarif); }
                        else if (wash_number_int == 11) { money_tarif.setText("" + wash11tarif); }
                        else if (wash_number_int == 12) { money_tarif.setText("" + wash12tarif); }
                        start.setEnabled(true);
                        start.setVisibility(View.VISIBLE);
                    } else {  choose_mode(); }

                } else if (washmode == 1) { // if wash mode 1
                    if (washisdry != 1){ prepair_background_mode1.setVisibility(View.VISIBLE);}
                    // Название режима из настроек
                    txt_layout_prepair_wash1.setText(name1mode);
                    int summ = 0;
                    if (wash_number_int == 1){summ = wash1tarif + mode1tarif; }
                    else if (wash_number_int == 2){summ = wash2tarif + mode1tarif; }
                    else if (wash_number_int == 3){summ = wash3tarif + mode1tarif; }
                    else if (wash_number_int == 4){summ = wash4tarif + mode1tarif; }
                    else if (wash_number_int == 5){summ = wash5tarif + mode1tarif; }
                    else if (wash_number_int == 6){summ = wash6tarif + mode1tarif; }
                    else if (wash_number_int == 7){summ = wash7tarif + mode1tarif; }
                    else if (wash_number_int == 8){summ = wash8tarif + mode1tarif; }
                    else if (wash_number_int == 9){summ = wash9tarif + mode1tarif; }
                    else if (wash_number_int == 10){summ = wash10tarif + mode1tarif; }
                    else if (wash_number_int == 11){summ = wash11tarif + mode1tarif; }
                    else if (wash_number_int == 12){summ = wash12tarif + mode1tarif; }
                    money_tarif.setText("" + summ);
                    start.setEnabled(true);
                    start.setVisibility(View.VISIBLE);

                } else if (washmode == 2) { // if wash mode 2
                    if (washisdry != 1){ prepair_background_mode2.setVisibility(View.VISIBLE);}
                    // Название режима из настроек
                    txt_layout_prepair_wash1.setText(name2mode);
                    int summ = 0;
                    if (wash_number_int == 1){summ = wash1tarif + mode2tarif; }
                    else if (wash_number_int == 2){summ = wash2tarif + mode2tarif; }
                    else if (wash_number_int == 3){summ = wash3tarif + mode2tarif; }
                    else if (wash_number_int == 4){summ = wash4tarif + mode2tarif; }
                    else if (wash_number_int == 5){summ = wash5tarif + mode2tarif; }
                    else if (wash_number_int == 6){summ = wash6tarif + mode2tarif; }
                    else if (wash_number_int == 7){summ = wash7tarif + mode2tarif; }
                    else if (wash_number_int == 8){summ = wash8tarif + mode2tarif; }
                    else if (wash_number_int == 9){summ = wash9tarif + mode2tarif; }
                    else if (wash_number_int == 10){summ = wash10tarif + mode2tarif; }
                    else if (wash_number_int == 11){summ = wash11tarif + mode2tarif; }
                    else if (wash_number_int == 12){summ = wash12tarif + mode2tarif; }
                    money_tarif.setText("" + summ);
                    start.setEnabled(true);
                    start.setVisibility(View.VISIBLE);

                } else if (washmode == 3) { // if wash mode 3
                    if (washisdry != 1){ prepair_background_mode3.setVisibility(View.VISIBLE);}
                    // Название режима из настроек
                    txt_layout_prepair_wash1.setText(name3mode);
                    int summ = 0;
                    if (wash_number_int == 1){summ = wash1tarif + mode3tarif; }
                    else if (wash_number_int == 2){summ = wash2tarif + mode3tarif; }
                    else if (wash_number_int == 3){summ = wash3tarif + mode3tarif; }
                    else if (wash_number_int == 4){summ = wash4tarif + mode3tarif; }
                    else if (wash_number_int == 5){summ = wash5tarif + mode3tarif; }
                    else if (wash_number_int == 6){summ = wash6tarif + mode3tarif; }
                    else if (wash_number_int == 7){summ = wash7tarif + mode3tarif; }
                    else if (wash_number_int == 8){summ = wash8tarif + mode3tarif; }
                    else if (wash_number_int == 9){summ = wash9tarif + mode3tarif; }
                    else if (wash_number_int == 10){summ = wash10tarif + mode3tarif; }
                    else if (wash_number_int == 11){summ = wash11tarif + mode3tarif; }
                    else if (wash_number_int == 12){summ = wash12tarif + mode3tarif; }
                    money_tarif.setText("" + summ);
                    start.setEnabled(true);
                    start.setVisibility(View.VISIBLE);

                } else if (washmode == 4) { // if wash mode 4
                    if (washisdry != 1){ prepair_background_mode4.setVisibility(View.VISIBLE);}
                    // Название режима из настроек
                    txt_layout_prepair_wash1.setText(name4mode);
                    int summ = 0;
                    if (wash_number_int == 1){summ = wash1tarif + mode4tarif; }
                    else if (wash_number_int == 2){summ = wash2tarif + mode4tarif; }
                    else if (wash_number_int == 3){summ = wash3tarif + mode4tarif; }
                    else if (wash_number_int == 4){summ = wash4tarif + mode4tarif; }
                    else if (wash_number_int == 5){summ = wash5tarif + mode4tarif; }
                    else if (wash_number_int == 6){summ = wash6tarif + mode4tarif; }
                    else if (wash_number_int == 7){summ = wash7tarif + mode4tarif; }
                    else if (wash_number_int == 8){summ = wash8tarif + mode4tarif; }
                    else if (wash_number_int == 9){summ = wash9tarif + mode4tarif; }
                    else if (wash_number_int == 10){summ = wash10tarif + mode4tarif; }
                    else if (wash_number_int == 11){summ = wash11tarif + mode4tarif; }
                    else if (wash_number_int == 12){summ = wash12tarif + mode4tarif; }
                    money_tarif.setText("" + summ);
                    start.setEnabled(true);
                    start.setVisibility(View.VISIBLE);

                } else if (washmode == 5) { // if wash mode 5
                    if (washisdry != 1){ prepair_background_mode5.setVisibility(View.VISIBLE);}
                    // Название режима из настроек
                    txt_layout_prepair_wash1.setText(name5mode);
                    int summ = 0;
                    if (wash_number_int == 1){summ = wash1tarif + mode5tarif; }
                    else if (wash_number_int == 2){summ = wash2tarif + mode5tarif; }
                    else if (wash_number_int == 3){summ = wash3tarif + mode5tarif; }
                    else if (wash_number_int == 4){summ = wash4tarif + mode5tarif; }
                    else if (wash_number_int == 5){summ = wash5tarif + mode5tarif; }
                    else if (wash_number_int == 6){summ = wash6tarif + mode5tarif; }
                    else if (wash_number_int == 7){summ = wash7tarif + mode5tarif; }
                    else if (wash_number_int == 8){summ = wash8tarif + mode5tarif; }
                    else if (wash_number_int == 9){summ = wash9tarif + mode5tarif; }
                    else if (wash_number_int == 10){summ = wash10tarif + mode5tarif; }
                    else if (wash_number_int == 11){summ = wash11tarif + mode5tarif; }
                    else if (wash_number_int == 12){summ = wash12tarif + mode5tarif; }
                    money_tarif.setText("" + summ);
                    start.setEnabled(true);
                    start.setVisibility(View.VISIBLE);

                } else if (washmode == 6) { // if wash mode 6
                    if (washisdry != 1){ prepair_background_mode6.setVisibility(View.VISIBLE);}
                    // Название режима из настроек
                    txt_layout_prepair_wash1.setText(name6mode);
                    int summ = 0;
                    if (wash_number_int == 1){summ = wash1tarif + mode6tarif; }
                    else if (wash_number_int == 2){summ = wash2tarif + mode6tarif; }
                    else if (wash_number_int == 3){summ = wash3tarif + mode6tarif; }
                    else if (wash_number_int == 4){summ = wash4tarif + mode6tarif; }
                    else if (wash_number_int == 5){summ = wash5tarif + mode6tarif; }
                    else if (wash_number_int == 6){summ = wash6tarif + mode6tarif; }
                    else if (wash_number_int == 7){summ = wash7tarif + mode6tarif; }
                    else if (wash_number_int == 8){summ = wash8tarif + mode6tarif; }
                    else if (wash_number_int == 9){summ = wash9tarif + mode6tarif; }
                    else if (wash_number_int == 10){summ = wash10tarif + mode6tarif; }
                    else if (wash_number_int == 11){summ = wash11tarif + mode6tarif; }
                    else if (wash_number_int == 12){summ = wash12tarif + mode6tarif; }
                    money_tarif.setText("" + summ);
                    start.setEnabled(true);
                    start.setVisibility(View.VISIBLE);
                }
            }
        }
    }



    /**
     * Проверка на связь с ардуино пройдена
     * Проверяем условия запуска
     * Отправляем на ардуино нужные команды
     * Создаем форму анимации запуска
     * Перенаправляемся в окно показа/выбора машин
     * чтобы увидеть как сработала обратка
     *
     * @param sms_wash (0-off, 1-wash, .., 12wash)
     * @param sms_mode (0-start, 1-mode, .., 12-mode)
     *
     */
    private void start_engine(int sms_wash, int sms_mode) {

        // по нулям входим через прогу, а с другими параметрами - если смс
        if ((sms_wash == 0)&&(sms_mode == 0)) {
            // убедимся, что второй раз мы сюда не войдем, блокируем кнопку
            start.setEnabled(false);
            start.setVisibility(View.INVISIBLE);
            // запускаем анимацию
            sendMessage(START_ANIM);
            // animation_start();


// записываем счетчики запусков -------------------------- начало ------------------
//                             ------------ MODE 0 -------------
            if ((wash_number_int == 1)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTER,  wash1statcounter); }
            if ((wash_number_int == 2)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTER,  wash2statcounter); }
            if ((wash_number_int == 3)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTER,  wash3statcounter); }
            if ((wash_number_int == 4)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTER,  wash4statcounter); }
            if ((wash_number_int == 5)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTER,  wash5statcounter); }
            if ((wash_number_int == 6)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTER,  wash6statcounter); }
            if ((wash_number_int == 7)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTER,  wash7statcounter); }
            if ((wash_number_int == 8)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTER,  wash8statcounter); }
            if ((wash_number_int == 9)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTER,  wash9statcounter); }
            if ((wash_number_int == 10)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTER, wash10statcounter); }
            if ((wash_number_int == 11)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTER, wash11statcounter); }
            if ((wash_number_int == 12)&&(set_mode == 0)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTER, wash12statcounter); }
//                             ------------ MODE 1 -------------
            if ((wash_number_int == 1)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERMODE1, wash1statcounter_mode1); }
            if ((wash_number_int == 2)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERMODE1, wash2statcounter_mode1); }
            if ((wash_number_int == 3)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERMODE1, wash3statcounter_mode1); }
            if ((wash_number_int == 4)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERMODE1, wash4statcounter_mode1); }
            if ((wash_number_int == 5)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERMODE1, wash5statcounter_mode1); }
            if ((wash_number_int == 6)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERMODE1, wash6statcounter_mode1); }
            if ((wash_number_int == 7)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERMODE1, wash7statcounter_mode1); }
            if ((wash_number_int == 8)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERMODE1, wash8statcounter_mode1); }
            if ((wash_number_int == 9)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERMODE1, wash9statcounter_mode1); }
            if ((wash_number_int == 10)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERMODE1, wash10statcounter_mode1); }
            if ((wash_number_int == 11)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERMODE1, wash11statcounter_mode1); }
            if ((wash_number_int == 12)&&(set_mode == 1)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERMODE1, wash12statcounter_mode1); }
//                             ------------ MODE 2 -------------
            if ((wash_number_int == 1)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERMODE2, wash1statcounter_mode2); }
            if ((wash_number_int == 2)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERMODE2, wash2statcounter_mode2); }
            if ((wash_number_int == 3)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERMODE2, wash3statcounter_mode2); }
            if ((wash_number_int == 4)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERMODE2, wash4statcounter_mode2); }
            if ((wash_number_int == 5)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERMODE2, wash5statcounter_mode2); }
            if ((wash_number_int == 6)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERMODE2, wash6statcounter_mode2); }
            if ((wash_number_int == 7)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERMODE2, wash7statcounter_mode2); }
            if ((wash_number_int == 8)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERMODE2, wash8statcounter_mode2); }
            if ((wash_number_int == 9)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERMODE2, wash9statcounter_mode2); }
            if ((wash_number_int == 10)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERMODE2, wash10statcounter_mode2); }
            if ((wash_number_int == 11)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERMODE2, wash11statcounter_mode2); }
            if ((wash_number_int == 12)&&(set_mode == 2)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERMODE2, wash12statcounter_mode2); }
//                             ------------ MODE 3 -------------
            if ((wash_number_int == 1)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERMODE3, wash1statcounter_mode3); }
            if ((wash_number_int == 2)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERMODE3, wash2statcounter_mode3); }
            if ((wash_number_int == 3)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERMODE3, wash3statcounter_mode3); }
            if ((wash_number_int == 4)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERMODE3, wash4statcounter_mode3); }
            if ((wash_number_int == 5)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERMODE3, wash5statcounter_mode3); }
            if ((wash_number_int == 6)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERMODE3, wash6statcounter_mode3); }
            if ((wash_number_int == 7)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERMODE3, wash7statcounter_mode3); }
            if ((wash_number_int == 8)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERMODE3, wash8statcounter_mode3); }
            if ((wash_number_int == 9)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERMODE3, wash9statcounter_mode3); }
            if ((wash_number_int == 10)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERMODE3, wash10statcounter_mode3); }
            if ((wash_number_int == 11)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERMODE3, wash11statcounter_mode3); }
            if ((wash_number_int == 12)&&(set_mode == 3)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERMODE3, wash12statcounter_mode3); }
//                             ------------ MODE 4 -------------
            if ((wash_number_int == 1)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERMODE4, wash1statcounter_mode4); }
            if ((wash_number_int == 2)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERMODE4, wash2statcounter_mode4); }
            if ((wash_number_int == 3)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERMODE4, wash3statcounter_mode4); }
            if ((wash_number_int == 4)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERMODE4, wash4statcounter_mode4); }
            if ((wash_number_int == 5)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERMODE4, wash5statcounter_mode4); }
            if ((wash_number_int == 6)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERMODE4, wash6statcounter_mode4); }
            if ((wash_number_int == 7)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERMODE4, wash7statcounter_mode4); }
            if ((wash_number_int == 8)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERMODE4, wash8statcounter_mode4); }
            if ((wash_number_int == 9)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERMODE4, wash9statcounter_mode4); }
            if ((wash_number_int == 10)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERMODE4, wash10statcounter_mode4); }
            if ((wash_number_int == 11)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERMODE4, wash11statcounter_mode4); }
            if ((wash_number_int == 12)&&(set_mode == 4)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERMODE4, wash12statcounter_mode4); }
//                             ------------ MODE 5 -------------
            if ((wash_number_int == 1)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERMODE5, wash1statcounter_mode5); }
            if ((wash_number_int == 2)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERMODE5, wash2statcounter_mode5); }
            if ((wash_number_int == 3)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERMODE5, wash3statcounter_mode5); }
            if ((wash_number_int == 4)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERMODE5, wash4statcounter_mode5); }
            if ((wash_number_int == 5)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERMODE5, wash5statcounter_mode5); }
            if ((wash_number_int == 6)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERMODE5, wash6statcounter_mode5); }
            if ((wash_number_int == 7)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERMODE5, wash7statcounter_mode5); }
            if ((wash_number_int == 8)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERMODE5, wash8statcounter_mode5); }
            if ((wash_number_int == 9)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERMODE5, wash9statcounter_mode5); }
            if ((wash_number_int == 10)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERMODE5, wash10statcounter_mode5); }
            if ((wash_number_int == 11)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERMODE5, wash11statcounter_mode5); }
            if ((wash_number_int == 12)&&(set_mode == 5)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERMODE5, wash12statcounter_mode5); }
//                             ------------ MODE 6 -------------
            if ((wash_number_int == 1)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERMODE6, wash1statcounter_mode6); }
            if ((wash_number_int == 2)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERMODE6, wash2statcounter_mode6); }
            if ((wash_number_int == 3)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERMODE6, wash3statcounter_mode6); }
            if ((wash_number_int == 4)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERMODE6, wash4statcounter_mode6); }
            if ((wash_number_int == 5)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERMODE6, wash5statcounter_mode6); }
            if ((wash_number_int == 6)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERMODE6, wash6statcounter_mode6); }
            if ((wash_number_int == 7)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERMODE6, wash7statcounter_mode6); }
            if ((wash_number_int == 8)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERMODE6, wash8statcounter_mode6); }
            if ((wash_number_int == 9)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERMODE6, wash9statcounter_mode6); }
            if ((wash_number_int == 10)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERMODE6, wash10statcounter_mode6); }
            if ((wash_number_int == 11)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERMODE6, wash11statcounter_mode6); }
            if ((wash_number_int == 12)&&(set_mode == 6)){ save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERMODE6, wash12statcounter_mode6); }
            // записываем счетчики запусков -------------------------- конец ------------------


            // запускаем нужные релюшки
            relay_manager(wash_number_int, set_mode);
            // теперь спишем деньги
            spisanie_deneg();

            //      ---------------------------------------------------
            //      ------------ СМС УПРАВЛЕНИЕ МАШИНАМИ --------------
            //      ---------------------------------------------------
            // когда поступает команда с телефона на экран ничего не выводится
        } else { // пришла смс, то
            // записать счетчик смс запуска
            if (sms_wash == 1)  {  save_counter_starting(APP_PREFERENCES_WASH1STATCOUNTERSMS,  wash1statcounter_sms); }
            if (sms_wash == 2)  {  save_counter_starting(APP_PREFERENCES_WASH2STATCOUNTERSMS,  wash2statcounter_sms); }
            if (sms_wash == 3)  {  save_counter_starting(APP_PREFERENCES_WASH3STATCOUNTERSMS,  wash3statcounter_sms); }
            if (sms_wash == 4)  {  save_counter_starting(APP_PREFERENCES_WASH4STATCOUNTERSMS,  wash4statcounter_sms); }
            if (sms_wash == 5)  {  save_counter_starting(APP_PREFERENCES_WASH5STATCOUNTERSMS,  wash5statcounter_sms); }
            if (sms_wash == 6)  {  save_counter_starting(APP_PREFERENCES_WASH6STATCOUNTERSMS,  wash6statcounter_sms); }
            if (sms_wash == 7)  {  save_counter_starting(APP_PREFERENCES_WASH7STATCOUNTERSMS,  wash7statcounter_sms); }
            if (sms_wash == 8)  {  save_counter_starting(APP_PREFERENCES_WASH8STATCOUNTERSMS,  wash8statcounter_sms); }
            if (sms_wash == 9)  {  save_counter_starting(APP_PREFERENCES_WASH9STATCOUNTERSMS,  wash9statcounter_sms); }
            if (sms_wash == 10) {  save_counter_starting(APP_PREFERENCES_WASH10STATCOUNTERSMS, wash10statcounter_sms); }
            if (sms_wash == 11) {  save_counter_starting(APP_PREFERENCES_WASH11STATCOUNTERSMS, wash11statcounter_sms); }
            if (sms_wash == 12) {  save_counter_starting(APP_PREFERENCES_WASH12STATCOUNTERSMS, wash12statcounter_sms); }
            // запускаем нужные релюшки с параметрами из смс
            relay_manager(sms_wash, sms_mode);

            // отвечаем смс-кой о выполнении комманды

            if (sms_mode == 0){ mode_name = ""; }
            if (sms_mode == 1){ mode_name = "на " + name1mode; }
            if (sms_mode == 2){ mode_name = "на " + name2mode; }
            if (sms_mode == 3){ mode_name = "на " + name3mode; }
            if (sms_mode == 4){ mode_name = "на " + name4mode; }
            if (sms_mode == 5){ mode_name = "на " + name5mode; }
            if (sms_mode == 6){ mode_name = "на " + name6mode; }
            SmsManager sms = SmsManager.getDefault(); // задаём стандартный  мэнеджер
            sms.sendTextMessage(sms_number, null, "Машинка " + sms_wash + " запущена " + mode_name + "", null, null);

        }


    }





    /**
     * Записать в память данные для статистики
     * @param  app_pref     имя ячейки памяти
     * @param  washstatcounter_mode     переменная для записи с номером машины и режима
     *
     */
    private void save_counter_starting(String app_pref, int washstatcounter_mode) {
        washstatcounter_mode++;
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(app_pref, washstatcounter_mode);
        editor.apply();
    }


    /**
     * Настройка включения режимов управление релюшками
     * @param  wash_num     номер машины
     * @param  washer_modes     номер режима
     *
     */
    private void relay_manager(int wash_num, int washer_modes) {

        // -------------- ATTANTION! -------- 'WASH CORRECTOR' -------------------------------------


        if (wash_num == 1) {
            pwr = "" + WASH_1_POWER;
            md  = "" + WASH_1_MODE;
            tmp = "" + WASH_1_TEMP;
            srt = "" + WASH_1_START;  }
        if (wash_num == 2) {
            pwr = "" + WASH_2_POWER;
            md  = "" + WASH_2_MODE;
            tmp = "" + WASH_2_TEMP;
            srt = "" + WASH_2_START;  }
        if (wash_num == 3) {
            pwr = "" + WASH_3_POWER;
            md  = "" + WASH_3_MODE;
            tmp = "" + WASH_3_TEMP;
            srt = "" + WASH_3_START;  }
        if (wash_num == 4) {
            pwr = "" + WASH_4_POWER;
            md  = "" + WASH_4_MODE;
            tmp = "" + WASH_4_TEMP;
            srt = "" + WASH_4_START;  }
        if (wash_num == 5) {
            pwr = "" + WASH_5_POWER;
            md  = "" + WASH_5_MODE;
            tmp = "" + WASH_5_TEMP;
            srt = "" + WASH_5_START;  }
        if (wash_num == 6) {
            pwr = "" + WASH_6_POWER;
            md  = "" + WASH_6_MODE;
            tmp = "" + WASH_6_TEMP;
            srt = "" + WASH_6_START;  }
        if (wash_num == 7) {
            pwr = "" + WASH_7_POWER;
            md  = "" + WASH_7_MODE;
            tmp = "" + WASH_7_TEMP;
            srt = "" + WASH_7_START;  }
        if (wash_num == 8) {
            pwr = "" + WASH_8_POWER;
            md  = "" + WASH_8_MODE;
            tmp = "" + WASH_8_TEMP;
            srt = "" + WASH_8_START;  }
        if (wash_num == 9) {
            pwr = "" + WASH_9_POWER;
            md  = "" + WASH_9_MODE;
            tmp = "" + WASH_9_TEMP;
            srt = "" + WASH_9_START;  }
        if (wash_num == 10) {
            pwr = "" + WASH_10_POWER;
            md  = "" + WASH_10_MODE;
            tmp = "" + WASH_10_TEMP;
            srt = "" + WASH_10_START;  }
        if (wash_num == 11) {
            pwr = "" + WASH_11_POWER;
            md  = "" + WASH_11_MODE;
            tmp = "" + WASH_11_TEMP;
            srt = "" + WASH_11_START;  }
        if (wash_num == 12) {
            pwr = "" + WASH_12_POWER;
            md  = "" + WASH_12_MODE;
            tmp = "" + WASH_12_TEMP;
            srt = "" + WASH_12_START;  }

        // mode0 *** СТАРТ ***
        if (washer_modes == 0) {
            sendMessage(srt);
        }
        // mode1 *** БЫСТРАЯ ***
        if (washer_modes == 1) {
            sendMessage(pwr);
            // берем из памяти количество щелчков
            if (mode1push == 0){
            } else if (mode1push == 1){
                sendMessage(md);
            } else if (mode1push == 2){
                sendMessage(md);
                sendMessage(md);
            } else if (mode1push == 3){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode1push == 4){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode1push == 5){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode1push == 6){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode1push == 7){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);      }
            // и не забываем нажать старт
            sendMessage(srt); }
        // mode2 *** ИНТЕНСИВНАЯ ***
        if (washer_modes == 2) {
            sendMessage(pwr);
            // берем из памяти количество щелчков
            if (mode2push == 0){
            } else if (mode2push == 1){
                sendMessage(md);
            } else if (mode2push == 2){
                sendMessage(md);
                sendMessage(md);
            } else if (mode2push == 3){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode2push == 4){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode2push == 5){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode2push == 6){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode2push == 7){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);      }
            // и не забываем нажать старт
            sendMessage(srt); }
        // mode3 *** ХЛОПОК ***
        if (washer_modes == 3) {
            sendMessage(pwr);
            // берем из памяти количество щелчков
            if (mode3push == 0){
            } else if (mode3push == 1){
                sendMessage(md);
            } else if (mode3push == 2){
                sendMessage(md);
                sendMessage(md);
            } else if (mode3push == 3){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode3push == 4){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode3push == 5){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode3push == 6){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode3push == 7){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);      }
            // и не забываем нажать старт
            sendMessage(srt); }
        // mode4 *** ШЕРСТЬ ***
        if (washer_modes == 4) {
            sendMessage(pwr);
            // берем из памяти количество щелчков
            if (mode4push == 0){
            } else if (mode4push == 1){
                sendMessage(md);
            } else if (mode4push == 2){
                sendMessage(md);
                sendMessage(md);
            } else if (mode4push == 3){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode4push == 4){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode4push == 5){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode4push == 6){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode4push == 7){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);      }
            // и не забываем нажать старт
            sendMessage(srt); }
        // mode5 *** СИНТЕТИКА ***
        if (washer_modes == 5) {
            sendMessage(pwr);
            // берем из памяти количество щелчков
            if (mode5push == 0){
            } else if (mode5push == 1){
                sendMessage(md);
            } else if (mode5push == 2){
                sendMessage(md);
                sendMessage(md);
            } else if (mode5push == 3){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode5push == 4){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode5push == 5){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode5push == 6){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode5push == 7){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);      }
            // и не забываем нажать старт
            sendMessage(srt); }
        // mode6 *** ПОЛОСКАНИЕ ***
        if (washer_modes == 6) {
            sendMessage(pwr);
            // берем из памяти количество щелчков
            if (mode6push == 0){
            } else if (mode6push == 1){
                sendMessage(md);
            } else if (mode6push == 2){
                sendMessage(md);
                sendMessage(md);
            } else if (mode6push == 3){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode6push == 4){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode6push == 5){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode6push == 6){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
            } else if (mode6push == 7){
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);
                sendMessage(md);      }
            // и не забываем нажать старт
            sendMessage(srt); }

    }






    /**
     * Списываем деньги
     * берем текущий тариф не из переменной потому что
     * не надо проверять к какой машине он относится
     */
    private void spisanie_deneg() {

        int kup1 = Integer.parseInt(money_kup.getText().toString());
        int tar1 = Integer.parseInt(money_tarif.getText().toString());
        int kuptar = kup1 - tar1;
        if (kuptar >= 0) {
            money_vneseno = kuptar;
            money_kup.setText("" + kuptar);
            // счетчики запусков и тарифов и времени
            // save  money vneseno
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
            editor.apply();
        } else {
            Log.i(TAG, "Error Kuptar! Backup by memory.");
            kuptar = 0;
            money_vneseno = kuptar;
            money_kup.setText("" + kuptar);
            // счетчики запусков и тарифов и времени
            // save  money vneseno
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
            editor.apply();
        }

    }




    /**
     * Закрываем анимацию барабанов
     */
    public void close_animation() {
        imageDoor1.clearAnimation();
        imageDoor2.clearAnimation();
        imageDoor3.clearAnimation();
        imageDoor4.clearAnimation();
        imageDoor5.clearAnimation();
        imageDoor6.clearAnimation();
        imageDoor7.clearAnimation();
        imageDoor8.clearAnimation();
        imageDoor9.clearAnimation();
        imageDoor10.clearAnimation();
        imageDoor11.clearAnimation();
        imageDoor12.clearAnimation();
        imageDoor1.setVisibility(View.INVISIBLE);
        imageDoor2.setVisibility(View.INVISIBLE);
        imageDoor3.setVisibility(View.INVISIBLE);
        imageDoor4.setVisibility(View.INVISIBLE);
        imageDoor5.setVisibility(View.INVISIBLE);
        imageDoor6.setVisibility(View.INVISIBLE);
        imageDoor7.setVisibility(View.INVISIBLE);
        imageDoor8.setVisibility(View.INVISIBLE);
        imageDoor9.setVisibility(View.INVISIBLE);
        imageDoor10.setVisibility(View.INVISIBLE);
        imageDoor11.setVisibility(View.INVISIBLE);
        imageDoor12.setVisibility(View.INVISIBLE);
    }


    /**
     * Анимация запуска
     *
     */
    private void animation_start() {
        // INVISIBLE LAYOUTS SCREEN
        layout_home.setVisibility(View.INVISIBLE);
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_choose_mode.setVisibility(View.INVISIBLE);
        layout_bt_search.setVisibility(View.INVISIBLE);
        layout_settings.setVisibility(View.INVISIBLE);
        layout_action_bar.setVisibility(View.VISIBLE);
        layout_bt_not.setVisibility(View.INVISIBLE);
        // открываем фрейм анимации, там все уже настроено
        layout_start_anim.setVisibility(View.VISIBLE);
        ok1.setVisibility(View.VISIBLE);
        status1.setVisibility(View.INVISIBLE);
        ok2.setVisibility(View.INVISIBLE);
        status2.setVisibility(View.INVISIBLE);
        ok3.setVisibility(View.INVISIBLE);
        status3.setVisibility(View.INVISIBLE);
        programs_start_clother.setVisibility(View.VISIBLE);
        programs_start_clother.clearAnimation();
    }

    public void ok1() {
        ok1.setVisibility(View.VISIBLE);
    }

    public void status1() {
        ok1.setVisibility(View.INVISIBLE);
        status1.setVisibility(View.VISIBLE);
    }

    public void ok2() {
        status1.setVisibility(View.INVISIBLE);
        ok2.setVisibility(View.VISIBLE);
    }

    public void status2() {
        ok2.setVisibility(View.INVISIBLE);
        status2.setVisibility(View.VISIBLE);
    }

    public void ok3() {
        status2.setVisibility(View.INVISIBLE);
        ok3.setVisibility(View.VISIBLE);
    }

    public void status3() {
        ok3.setVisibility(View.INVISIBLE);
        status3.setVisibility(View.VISIBLE);
    }

    public void animation_start_clother() {
        FragmentActivity activity = getActivity();
        final Animation animationRotateCenter = AnimationUtils.loadAnimation(activity, R.anim.rotate);
        programs_start_clother.setVisibility(View.VISIBLE);
        programs_start_clother.startAnimation(animationRotateCenter);
    }

    /*
    *   новая функция анимации ПРОВЕРИТЬ
    */
    private void startFrameAnimation() {
        BitmapDrawable ok1 = (BitmapDrawable) getResources().getDrawable(R.drawable.num1);
        BitmapDrawable status1 = (BitmapDrawable) getResources().getDrawable(R.drawable.num2);
        BitmapDrawable ok2 = (BitmapDrawable) getResources().getDrawable(R.drawable.num3);
        BitmapDrawable status2 = (BitmapDrawable) getResources().getDrawable(R.drawable.num4);
        BitmapDrawable ok3 = (BitmapDrawable) getResources().getDrawable(R.drawable.num5);
        BitmapDrawable status3 = (BitmapDrawable) getResources().getDrawable(R.drawable.num6);

        mAnimationDrawable = new AnimationDrawable();

        mAnimationDrawable.setOneShot(false);
        mAnimationDrawable.addFrame(ok1, DURATION);
        mAnimationDrawable.addFrame(status1, DURATION);
        mAnimationDrawable.addFrame(ok2, DURATION);
        mAnimationDrawable.addFrame(status2, DURATION);
        mAnimationDrawable.addFrame(ok3, DURATION);
        mAnimationDrawable.addFrame(status3, DURATION);

        img_fullscreen_search_bluetooth.setBackground(mAnimationDrawable);
    }


    /*
    *   Подготовка ШТОРКИ к переходу в раздел (очистка)
    */
    private void step_leftmenu(int menu, int leftmenut) {
        layout_settings_preview.setVisibility(View.INVISIBLE); // закрыть титульный лист
        if (leftmenut==1){show_leftmenu();}else{hide_leftmenu();}

        if (menu==1){
            txt_opacity_menu1.setVisibility(View.VISIBLE);
            layout_settings_washer.setVisibility(View.VISIBLE); // раздел машинки
            layout_settings_wash_number.setVisibility(View.VISIBLE);
            final Animation washnumberAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_wash_number);
            layout_settings_wash_number.startAnimation(washnumberAnimation);

            // ПАРАМЕТР
            StatisticWash1.setVisibility(View.INVISIBLE); // СТАТИСТИКА 1
            StatisticWash2.setVisibility(View.INVISIBLE); // СТАТИСТИКА 2
            StatisticWash3.setVisibility(View.INVISIBLE); // СТАТИСТИКА 3
            StatisticWash4.setVisibility(View.INVISIBLE); // СТАТИСТИКА 4
            StatisticWash5.setVisibility(View.INVISIBLE); // СТАТИСТИКА 5
            StatisticWash6.setVisibility(View.INVISIBLE); // СТАТИСТИКА 6
            StatisticWash7.setVisibility(View.INVISIBLE); // СТАТИСТИКА 7
            StatisticWash8.setVisibility(View.INVISIBLE); // СТАТИСТИКА 8
            StatisticWash9.setVisibility(View.INVISIBLE); // СТАТИСТИКА 9
            StatisticWash10.setVisibility(View.INVISIBLE);// СТАТИСТИКА 10
            StatisticWash11.setVisibility(View.INVISIBLE);// СТАТИСТИКА 11
            StatisticWash12.setVisibility(View.INVISIBLE);// СТАТИСТИКА 12
        }
        else {
            txt_opacity_menu1.setVisibility(View.INVISIBLE);
            layout_settings_washer.setVisibility(View.INVISIBLE); // раздел машинки
            //   layout_settings_wash_number.setVisibility(View.INVISIBLE);
        }
        if (menu==2){
            txt_opacity_menu2.setVisibility(View.VISIBLE);
            layout_settings_iron.setVisibility(View.VISIBLE); // раздел утюги

        }
        else{
            txt_opacity_menu2.setVisibility(View.INVISIBLE);
            layout_settings_iron.setVisibility(View.INVISIBLE); // раздел утюги
        }
        if (menu==3){
            txt_opacity_menu3.setVisibility(View.VISIBLE);
            layout_settings_sms.setVisibility(View.VISIBLE);  // раздел смс
        }
        else{
            txt_opacity_menu3.setVisibility(View.INVISIBLE);
            layout_settings_sms.setVisibility(View.INVISIBLE);  // раздел смс
        }
        if (menu==4){
            txt_opacity_menu4.setVisibility(View.VISIBLE);
            layout_settings_bluetooth.setVisibility(View.VISIBLE); // раздел bluetooth
        }
        else{
            txt_opacity_menu4.setVisibility(View.INVISIBLE);
            layout_settings_bluetooth.setVisibility(View.INVISIBLE); // раздел bluetooth
        }
        if (menu==5){
            txt_opacity_menu5.setVisibility(View.VISIBLE);
            layout_settings_tarif.setVisibility(View.VISIBLE); // раздел тарифы
        }
        else {
            txt_opacity_menu5.setVisibility(View.INVISIBLE);
            layout_settings_tarif.setVisibility(View.INVISIBLE); // раздел тарифы
        }
        if (menu==6){
            txt_opacity_menu6.setVisibility(View.VISIBLE);
            layout_settings_system.setVisibility(View.VISIBLE); // раздел система
            layout_log.setVisibility(View.INVISIBLE); // подразделы системы
        }
        else{
            txt_opacity_menu6.setVisibility(View.INVISIBLE);
            layout_settings_system.setVisibility(View.INVISIBLE); // раздел система
            layout_log.setVisibility(View.INVISIBLE); // подразделы системы
        }
        if (menu==7){
            txt_opacity_menu7.setVisibility(View.VISIBLE);
            layout_settings_stat.setVisibility(View.VISIBLE); // раздел статистика
            layout_settings_wash_number.setVisibility(View.VISIBLE);
            // ПАРАМЕТР
            StatisticWash1.setVisibility(View.VISIBLE); // СТАТИСТИКА 1
            StatisticWash2.setVisibility(View.VISIBLE); // СТАТИСТИКА 2
            StatisticWash3.setVisibility(View.VISIBLE); // СТАТИСТИКА 3
            StatisticWash4.setVisibility(View.VISIBLE); // СТАТИСТИКА 4
            StatisticWash5.setVisibility(View.VISIBLE); // СТАТИСТИКА 5
            StatisticWash6.setVisibility(View.VISIBLE); // СТАТИСТИКА 6
            StatisticWash7.setVisibility(View.VISIBLE); // СТАТИСТИКА 7
            StatisticWash8.setVisibility(View.VISIBLE); // СТАТИСТИКА 8
            StatisticWash9.setVisibility(View.VISIBLE); // СТАТИСТИКА 9
            StatisticWash10.setVisibility(View.VISIBLE);// СТАТИСТИКА 10
            StatisticWash11.setVisibility(View.VISIBLE);// СТАТИСТИКА 11
            StatisticWash12.setVisibility(View.VISIBLE);// СТАТИСТИКА 12
        }
        else{
            txt_opacity_menu7.setVisibility(View.INVISIBLE);
            layout_settings_stat.setVisibility(View.INVISIBLE); // раздел статистика
            //  layout_settings_wash_number.setVisibility(View.INVISIBLE);
        }
        if (menu==8){txt_opacity_menu8.setVisibility(View.VISIBLE);}
        else{txt_opacity_menu8.setVisibility(View.INVISIBLE);}

        // фрейм для отображения стиралок, использ.в машинах и статистике
        // не забывать скрывать его при переходе в другие разделы!
        // layout_settings_wash_number.setVisibility(View.INVISIBLE);
        if((menu!=1)&&(menu!=7)){
            layout_settings_wash_number.setVisibility(View.INVISIBLE);
        }
        // проверяем батареку
        displayInfo();
        if (statusStr.contentEquals("1")) {
            img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_battary.setVisibility(View.GONE);
        } else {
            img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_electro.setVisibility(View.GONE);
        }


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
     */
    private void dialog_check_money_tarif_error() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Машинка №" + wash_number.getText().toString());
        alertDialog.setMessage("ERROR25-74 Запуск не возможен, тариф не может быть меньше нуля.");
        alertDialog.setPositiveButton("Автозамена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                money_tarif_correct();

            }});
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(true);
        final AlertDialog dlg = alertDialog.create();
        dlg.show();
    }

    private void money_tarif_correct() {
        money_tarif.setText("0");
    };

    /**
     * Alert dialog in numbers wash screen client
     * go to crash down woter screen mode
     */
    private void dialog_crash_downwatermode() {
        if (checkwater == 1) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Машинка №" + wash_number.getText().toString() + "Стирка " + washertimer + " мин");
            alertDialog.setMessage("Если машина выключена и вам не открыть дверцу, слейте воду.");
            alertDialog.setPositiveButton("Аварийный слив воды", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    if ((wash_number_int==1)&&(wash1remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==2)&&(wash2remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==3)&&(wash3remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==4)&&(wash4remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==5)&&(wash5remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==6)&&(wash6remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==7)&&(wash7remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==8)&&(wash8remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==9)&&(wash9remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==10)&&(wash10remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==11)&&(wash11remont==0)) { prepair_to_start(0, 0, 1, 6);
                    } else if ((wash_number_int==12)&&(wash12remont==0)) { prepair_to_start(0, 0, 1, 6); }
                }
            });
            alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.setCancelable(true);
            final AlertDialog dlg = alertDialog.create();
            dlg.show();
        }
    }

    /**
     * Alert dialog in settings screen administrator
     * go to home screen client
     */
    private void dialog_exit_settings() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Выход из настроек");
        alertDialog.setMessage("Вы уверены, что хотите выйти?");
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                go_home();
            }
        });
        alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(true);
        final AlertDialog dlg = alertDialog.create();
        dlg.show();
    }


    /**
     * Alert dialog in settings screen administrator
     * go to home screen client
     */
    private void dialog_bluetooth_adapter() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Системная настройка");
        alertDialog.setMessage("ВНИМАНИЕ! Использовать только в присутствии " +
                "специалиста 'Лондри Плюс', данная операция может привести к неправильной" +
                " работе приложения.  Вы уверены, что хотите продолжить?");
        alertDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                    setupChat(); }
                if (!mBluetoothAdapter.isEnabled()) {
                    btn_settings_bluetooth_adapter.setChecked(false);
                } else {
                    btn_settings_bluetooth_adapter.setChecked(true);
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
        dlg.show();
    }


    /**
     * Alert dialog multiple button Ok press alert
     * go to home screen client
     */
    private void dialog_error_button() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("ERROR-245");
        alertDialog.setMessage("Нет ответа от платы терминала, обновите ПО.");
        alertDialog.setPositiveButton("Обновить", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Сервис временно не доступен.", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        final AlertDialog dlg = alertDialog.create();
        dlg.show();

    }

    /**
     * Alert dialog check press button Start alert
     * enough money to star
     */
    private void start_error() {
        int kup1dial = Integer.parseInt(money_kup.getText().toString());
        int tar1dial = Integer.parseInt(money_tarif.getText().toString());
        int kuptardial = (kup1dial - tar1dial)*-1;


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        // alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("Для запуска нужно внести еще: " + kuptardial +" "+ valuta_statistics_txt.getText().toString() + "");
        alertDialog.setMessage("          Хотите воспользоваться банковской картой?");
        alertDialog.setIcon(R.drawable.icon_wash_tarif);
        alertDialog.setPositiveButton("Оплатить картой", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Сервис временно не доступен.", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.setCancelable(false);
        final AlertDialog dlg = alertDialog.create();
        dlg.show();

        start.setEnabled(true);
        start.setVisibility(View.VISIBLE);
    }

    /**
     * Alert dialog out of servise
     * to All functions & buttons
     */
    private void dialog_out_of_service(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Сервис временно не доступен.").
                setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }


    /**
     * Alert dialog Programm.COM
     * to exit from this
     */
    private void dialog_program_com() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Чтобы закрыть окно COM, нажмите кнопку 'CLOSE' в правом нижнем углу.").
                setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Alert dialog Fullscreen
     * to exit from this
     */
    private void dialog_fullscreen_open() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Системная панель загружается..").
                setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Alert dialog Settings VS AnimationStart
     * ignor
     */
    private void dialog_settings_vs_animstart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Вы не можете сейчас перейти в настройки, повторите позже.").
                setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }



    /**
     * Choose wash mode screen client
     *
     */
    private void choose_mode() {
        // VISIBLE LAYOUTS SCREEN
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_start_anim.setVisibility(View.INVISIBLE);
        layout_bt_search.setVisibility(View.INVISIBLE);
        layout_settings.setVisibility(View.INVISIBLE);
        layout_bt_not.setVisibility(View.INVISIBLE);
        layout_home.setVisibility(View.INVISIBLE);
        // VISIBLE LAYOUTS SCREEN
        // определяем фон главной страницы
        layout_choose_mode.setVisibility(View.VISIBLE);
        // параметры
        choose1mode.setVisibility(View.VISIBLE);
        choose2mode.setVisibility(View.VISIBLE);
        choose3mode.setVisibility(View.VISIBLE);
        choose4mode.setVisibility(View.VISIBLE);
        choose5mode.setVisibility(View.VISIBLE);
        choose6mode.setVisibility(View.VISIBLE);
        // запускаем бар
        layout_action_bar.setVisibility(View.VISIBLE);
        // параметры бара
        img_action_bar_icon_wash.setVisibility(View.VISIBLE);
        wash_number.setVisibility(View.VISIBLE);
        img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
        money_kup.setVisibility(View.VISIBLE);
        img_action_bar_icon_not_connected.setVisibility(View.GONE);
        img_action_bar_icon_lost_connected.setVisibility(View.GONE);
        img_action_bar_icon_laundry_connected.setVisibility(View.VISIBLE);
        img_action_bar_icon_electro.setVisibility(View.GONE);
        img_action_bar_icon_battary.setVisibility(View.GONE);
    }



    /**
     * Автоподстановка данных по номеру текущей машинки
     */
    private void auto_prepair_to_start() {
        if (wash_number_int==1){prepair_to_start(wash1remont, wash1isdry, set_program, set_mode);}
        else if (wash_number_int==2){prepair_to_start(wash2remont, wash2isdry, set_program, set_mode);}
        else if (wash_number_int==3){prepair_to_start(wash3remont, wash3isdry, set_program, set_mode);}
        else if (wash_number_int==4){prepair_to_start(wash4remont, wash4isdry, set_program, set_mode);}
        else if (wash_number_int==5){prepair_to_start(wash5remont, wash5isdry, set_program, set_mode);}
        else if (wash_number_int==6){prepair_to_start(wash6remont, wash6isdry, set_program, set_mode);}
        else if (wash_number_int==7){prepair_to_start(wash7remont, wash7isdry, set_program, set_mode);}
        else if (wash_number_int==8){prepair_to_start(wash8remont, wash8isdry, set_program, set_mode);}
        else if (wash_number_int==9){prepair_to_start(wash9remont, wash9isdry, set_program, set_mode);}
        else if (wash_number_int==10){prepair_to_start(wash10remont, wash10isdry, set_program, set_mode);}
        else if (wash_number_int==11){prepair_to_start(wash11remont, wash11isdry, set_program, set_mode);}
        else if (wash_number_int==12){prepair_to_start(wash12remont, wash12isdry, set_program, set_mode);}
    }


    /**
     * Choose wash number screen client
     *
     * @param countwash int (how much your washes in laundry)
     */
    private void numbers_selected(int countwash) {
        FragmentActivity activity = getActivity();
        final Animation animationRotateCenter = AnimationUtils.loadAnimation(activity, R.anim.rotate);
        // VISIBLE LAYOUTS SCREEN
        // определяем фон главной страницы
        mButtonCounter = 0;
        layout_number_selected.setVisibility(View.VISIBLE);
        // параметры ---------------------------------
        txt_number_selected_title.setVisibility(View.VISIBLE);
        txt_number_selected_title.setText("Выберите № машины");
        // задать видимость кнопок скроллинга
        if (countwash>6){
            img_slide_back.setVisibility(View.VISIBLE);

            img_slide.setVisibility(View.VISIBLE);
        } else {
            img_slide_back.setVisibility(View.GONE);
            img_slide.setVisibility(View.GONE);
        }




        // показать машинки
        layout_washer.setVisibility(View.VISIBLE);
        final Animation washerAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.popup_smart_txt);
        layout_washer.startAnimation(washerAnimation);
        // параметры ---------------------
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
        // параметры layout_washer конец ---
        // параметры layout_number_selected конец ---------------

        // VISIBLE LAYOUTS SCREEN
        layout_prepair_start.setVisibility(View.GONE);
        layout_choose_mode.setVisibility(View.GONE);
        layout_start_anim.setVisibility(View.GONE);
        layout_bt_search.setVisibility(View.GONE);
        layout_settings.setVisibility(View.GONE);
        layout_bt_not.setVisibility(View.GONE);
        layout_home.setVisibility(View.GONE);
        // запускаем бар
        layout_action_bar.setVisibility(View.VISIBLE);
        // параметры бара ----------------------
        img_action_bar_icon_wash.setVisibility(View.GONE);
        wash_number.setVisibility(View.GONE);
        img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
        money_kup.setVisibility(View.VISIBLE);
        img_action_bar_icon_not_connected.setVisibility(View.GONE);
        img_action_bar_icon_lost_connected.setVisibility(View.GONE);
        img_action_bar_icon_laundry_connected.setVisibility(View.VISIBLE);
        // параметры бара конец ----------------
        displayInfo();
        if (statusStr.contentEquals("1")){
            img_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_action_bar_icon_battary.setVisibility(View.GONE);
        } else {
            img_action_bar_icon_battary.setVisibility(View.VISIBLE);
            img_action_bar_icon_electro.setVisibility(View.GONE);
        }

        // параметры по умолчанию ---------------------  начало ------------------------
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

        // параметры по умолчанию ---------------------  конец ------------------------



        // начало функции, до этого было вступление *******************************
        if (countwash == 0) {
            countwash = 12;
        }
        if (countwash >= 1) {
            //  layout_wash1.setVisibility(View.VISIBLE);
            machine1.setVisibility(View.VISIBLE);
            wash1_go.setVisibility(View.VISIBLE);
            wash1_go.setEnabled(true);
            wash1.setVisibility(View.VISIBLE);
            if (wash1isdry == 1){ dry1.setVisibility(View.VISIBLE);}
            else { dry1.setVisibility(View.INVISIBLE); }
            if (wash1crash ==1) {
                imageDoor1.setVisibility(View.VISIBLE);
                imageDoor1.startAnimation(animationRotateCenter);
            } else {
                imageDoor1.setVisibility(View.INVISIBLE);
                imageDoor1.clearAnimation(); }
            // отправляем только на первую машину, а дальше смотрим ответ ардуины
            sendMessage(WASH_1_DOOR);
        }
        if (countwash >= 2) {
            //   layout_wash2.setVisibility(View.VISIBLE);
            machine2.setVisibility(View.VISIBLE);
            wash2_go.setVisibility(View.VISIBLE);
            wash2_go.setEnabled(true);
            wash2.setVisibility(View.VISIBLE);
            if (wash2isdry == 1){ dry2.setVisibility(View.VISIBLE);
            } else { dry2.setVisibility(View.INVISIBLE); }
            if (wash2crash ==1) {
                imageDoor2.setVisibility(View.VISIBLE);
                imageDoor2.startAnimation(animationRotateCenter);
            } else {
                imageDoor2.setVisibility(View.INVISIBLE);
                imageDoor2.clearAnimation();
            }}
        if (countwash >= 3) {
            //   layout_wash3.setVisibility(View.VISIBLE);
            machine3.setVisibility(View.VISIBLE);
            wash3.setVisibility(View.VISIBLE);
            if (wash3isdry == 1){ dry3.setVisibility(View.VISIBLE);
            } else { dry3.setVisibility(View.INVISIBLE);  }
            wash3_go.setVisibility(View.VISIBLE);
            wash3_go.setEnabled(true);
            if (wash3crash ==1) {
                imageDoor3.setVisibility(View.VISIBLE);
                imageDoor3.startAnimation(animationRotateCenter);
            } else {
                imageDoor3.setVisibility(View.INVISIBLE);
                imageDoor3.clearAnimation();
            }}
        if (countwash >= 4) {
            //   layout_wash4.setVisibility(View.VISIBLE);
            machine4.setVisibility(View.VISIBLE);
            wash4.setVisibility(View.VISIBLE);
            if (wash4isdry == 1){ dry4.setVisibility(View.VISIBLE);
            } else { dry4.setVisibility(View.INVISIBLE); }
            wash4_go.setVisibility(View.VISIBLE);
            wash4_go.setEnabled(true);
            if (wash4crash ==1) {
                imageDoor4.setVisibility(View.VISIBLE);
                imageDoor4.startAnimation(animationRotateCenter);
            } else {
                imageDoor4.setVisibility(View.INVISIBLE);
                imageDoor4.clearAnimation();
            }}
        if (countwash >= 5) {
            // layout_wash5.setVisibility(View.VISIBLE);
            machine5.setVisibility(View.VISIBLE);
            wash5.setVisibility(View.VISIBLE);
            if (wash5isdry == 1){ dry5.setVisibility(View.VISIBLE);
            } else { dry5.setVisibility(View.INVISIBLE); }
            wash5_go.setVisibility(View.VISIBLE);
            wash5_go.setEnabled(true);
            if (wash5crash ==1) {
                imageDoor5.setVisibility(View.VISIBLE);
                imageDoor5.startAnimation(animationRotateCenter);
            } else {
                imageDoor5.setVisibility(View.INVISIBLE);
                imageDoor5.clearAnimation();
            }}
        if (countwash >= 6) {
            // layout_wash6.setVisibility(View.VISIBLE);
            machine6.setVisibility(View.VISIBLE);
            wash6.setVisibility(View.VISIBLE);
            if (wash6isdry == 1){  dry6.setVisibility(View.VISIBLE);
            } else { dry6.setVisibility(View.INVISIBLE); }
            wash6_go.setVisibility(View.VISIBLE);
            wash6_go.setEnabled(true);
            if (wash6crash ==1) {
                imageDoor6.setVisibility(View.VISIBLE);
                imageDoor6.startAnimation(animationRotateCenter);
            } else {
                imageDoor6.setVisibility(View.INVISIBLE);
                imageDoor6.clearAnimation();
            }}
        if (countwash >= 7) {
            //   layout_wash7.setVisibility(View.VISIBLE);
            machine7.setVisibility(View.VISIBLE);
            wash7.setVisibility(View.VISIBLE);
            if (wash7isdry == 1){ dry7.setVisibility(View.VISIBLE);
            } else { dry7.setVisibility(View.INVISIBLE); }
            wash7_go.setVisibility(View.VISIBLE);
            wash7_go.setEnabled(true);
            if (wash7crash ==1) {
                imageDoor7.setVisibility(View.VISIBLE);
                imageDoor7.startAnimation(animationRotateCenter);
            } else {
                imageDoor7.setVisibility(View.INVISIBLE);
                imageDoor7.clearAnimation();
            }}
        if (countwash >= 8) {
            //  layout_wash8.setVisibility(View.VISIBLE);
            machine8.setVisibility(View.VISIBLE);
            wash8.setVisibility(View.VISIBLE);
            if (wash8isdry == 1){ dry8.setVisibility(View.VISIBLE);
            } else { dry8.setVisibility(View.INVISIBLE); }
            wash8_go.setVisibility(View.VISIBLE);
            wash8_go.setEnabled(true);
            if (wash8crash ==1) {
                imageDoor8.setVisibility(View.VISIBLE);
                imageDoor8.startAnimation(animationRotateCenter);
            } else {
                imageDoor8.setVisibility(View.INVISIBLE);
                imageDoor8.clearAnimation();
            }}
        if (countwash >= 9) {
            // layout_wash9.setVisibility(View.VISIBLE);
            machine9.setVisibility(View.VISIBLE);
            wash9.setVisibility(View.VISIBLE);
            if (wash9isdry == 1){ dry9.setVisibility(View.VISIBLE);
            } else { dry9.setVisibility(View.INVISIBLE); }
            wash9_go.setVisibility(View.VISIBLE);
            wash9_go.setEnabled(true);
            if (wash9crash ==1) {
                imageDoor9.setVisibility(View.VISIBLE);
                imageDoor9.startAnimation(animationRotateCenter);
            } else {
                imageDoor9.setVisibility(View.INVISIBLE);
                imageDoor9.clearAnimation();
            }}
        if (countwash >= 10) {
            //  layout_wash10.setVisibility(View.VISIBLE);
            machine10.setVisibility(View.VISIBLE);
            wash10.setVisibility(View.VISIBLE);
            if (wash10isdry == 1){ dry10.setVisibility(View.VISIBLE);
            } else { dry10.setVisibility(View.INVISIBLE); }
            wash10_go.setVisibility(View.VISIBLE);
            wash10_go.setEnabled(true);
            if (wash10crash ==1) {
                imageDoor10.setVisibility(View.VISIBLE);
                imageDoor10.startAnimation(animationRotateCenter);
            } else {
                imageDoor10.setVisibility(View.INVISIBLE);
                imageDoor10.clearAnimation();
            }}
        if (countwash >= 11) {
            // layout_wash11.setVisibility(View.VISIBLE);
            machine11.setVisibility(View.VISIBLE);
            wash11.setVisibility(View.VISIBLE);
            if (wash11isdry == 1){ dry11.setVisibility(View.VISIBLE);
            } else { dry11.setVisibility(View.INVISIBLE); }
            wash11_go.setVisibility(View.VISIBLE);
            wash11_go.setEnabled(true);
            if (wash11crash ==1) {
                imageDoor11.setVisibility(View.VISIBLE);
                imageDoor11.startAnimation(animationRotateCenter);
            } else {
                imageDoor11.setVisibility(View.INVISIBLE);
                imageDoor11.clearAnimation();
            }}
        if (countwash >= 12) {
            //  layout_wash12.setVisibility(View.VISIBLE);
            machine12.setVisibility(View.VISIBLE);
            wash12.setVisibility(View.VISIBLE);
            if (wash12isdry == 1){ dry12.setVisibility(View.VISIBLE);
            } else { dry12.setVisibility(View.INVISIBLE); }
            wash12_go.setVisibility(View.VISIBLE);
            wash12_go.setEnabled(true);
            if (wash12crash ==1) {
                imageDoor12.setVisibility(View.VISIBLE);
                imageDoor12.startAnimation(animationRotateCenter);
            } else {
                imageDoor12.setVisibility(View.INVISIBLE);
                imageDoor12.clearAnimation();
            }}


        // end functions numbers selected
    }






    private void arduinoLife() {
        sendMessage(NOT_RESET); }

    private void door2_view() {
        sendMessage(WASH_2_DOOR); }
    private void door3_view() {
        sendMessage(WASH_3_DOOR); }
    private void door4_view() {
        sendMessage(WASH_4_DOOR); }
    private void door5_view() {
        sendMessage(WASH_5_DOOR); }
    private void door6_view() {
        sendMessage(WASH_6_DOOR); }
    private void door7_view() {
        sendMessage(WASH_7_DOOR); }
    private void door8_view() {
        sendMessage(WASH_8_DOOR); }
    private void door9_view() {
        sendMessage(WASH_9_DOOR); }
    private void door10_view() {
        sendMessage(WASH_10_DOOR); }
    private void door11_view() {
        sendMessage(WASH_11_DOOR); }
    private void door12_view() {
        sendMessage(WASH_12_DOOR); }



    /*
    *  All My System settings
    */

    private void display_settings_load() {
        if (layout_start_anim.getVisibility() == View.VISIBLE) {
            dialog_settings_vs_animstart();
        } else {
            // открываем настройки
            layout_settings.setVisibility(View.VISIBLE);
            layout_settings_preview.setVisibility(View.VISIBLE);
            display_settings_choose();
        }
    }

    /*
     *  All My System settings
     */
    private void display_settings_choose() {
        // onResume();
        // закрываем предыдущий экран?
        layout_home.setVisibility(View.INVISIBLE);
        layout_number_selected.setVisibility(View.INVISIBLE);
        layout_prepair_start.setVisibility(View.INVISIBLE);
        layout_choose_mode.setVisibility(View.INVISIBLE);
        layout_start_anim.setVisibility(View.INVISIBLE);
        layout_bt_search.setVisibility(View.INVISIBLE);
        layout_bt_not.setVisibility(View.INVISIBLE);
        layout_action_bar.setVisibility(View.INVISIBLE);

        // открываем настройки
        layout_settings.setVisibility(View.VISIBLE);

        // layout_settings.setBackgroundResource(R.drawable.settings_menu_background);
        // вложенные
        // основное меню
        layout_settings_action_bar.setVisibility(View.VISIBLE);
        // запуск с параметрами кнопки открывающей левое меню и необнуляемым итогом
        img_settings_action_bar_icon_view_leftmenu.setVisibility(View.VISIBLE);
        btn_settings_save.setEnabled(true);
        // подкрутим кнопочку немного
        FragmentActivity activity = getActivity();
        final Animation animationRotateCenterPopapButton = AnimationUtils.loadAnimation(
                activity, R.anim.popup_btn);
        img_settings_action_bar_icon_view_leftmenu.startAnimation(animationRotateCenterPopapButton);
        img_settings_action_bar_icon_smsstatus.setVisibility(View.INVISIBLE);
        img_settings_action_bar_icon_valuta.setVisibility(View.INVISIBLE);
        img_settings_action_bar_icon_allcashsum.setVisibility(View.VISIBLE);
        // глянем батареечку
        displayInfo();
        if (statusStr.contentEquals("1")){
            img_settings_action_bar_icon_electro.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_battary.setVisibility(View.GONE);
        } else {
            img_settings_action_bar_icon_battary.setVisibility(View.VISIBLE);
            img_settings_action_bar_icon_electro.setVisibility(View.GONE);
        }
        // покажем общую сумму
        statistics_txt.setVisibility(View.VISIBLE);
        // покажем валюту
        valuta_statistics_txt.setVisibility(View.VISIBLE);
        // parameters
        if (valuta==1){ valuta_statistics_txt.setText("RUB"); }
        if (valuta==2){ valuta_statistics_txt.setText("EUR"); }
        if (valuta==3){ valuta_statistics_txt.setText("BYR"); }
        if (valuta==4){ valuta_statistics_txt.setText("KZT"); }
        statistics_txt.setText("" + money + " ");


        //                    ------------------------------------------------
        //                    ---------------- ШТОРКА РАЗДЕЛОВ ---------------
        //                    ------------------------------------------------
        //                    ----------- Первоначальные настройки -----------
        //                    ------------------------------------------------
        layout_settings_action_bar_leftmenu.setVisibility(View.INVISIBLE);
        // параметры
        layout_settings_buttons.setVisibility(View.VISIBLE);
        // кнопки left menu
        btn_settings_washer.setEnabled(true);
        txt_opacity_menu1.setVisibility(View.INVISIBLE);
        btn_settings_iron.setEnabled(true);
        txt_opacity_menu2.setVisibility(View.INVISIBLE);
        btn_settings_sms.setEnabled(true);
        txt_opacity_menu3.setVisibility(View.INVISIBLE);
        btn_settings_bluetooth.setEnabled(true);
        txt_opacity_menu4.setVisibility(View.INVISIBLE);
        btn_settings_tarif.setEnabled(true);
        txt_opacity_menu5.setVisibility(View.INVISIBLE);
        btn_settings_system.setEnabled(true);
        txt_opacity_menu6.setVisibility(View.INVISIBLE);
        btn_settings_stat.setEnabled(true);
        txt_opacity_menu7.setVisibility(View.INVISIBLE);
        btn_settings_exit.setEnabled(true);
        txt_opacity_menu8.setVisibility(View.INVISIBLE);


        //                    ------------------------------------------------
        //                    ---------------- РАЗДЕЛЫ МЕНЮ ------------------
        //                    ------------------------------------------------
        //                    ----------- Первоначальные настройки -----------
        //                    ------------------------------------------------

        // соответственно отображаем меню тарифы, а другие скрываем
        // раздел машинки, включать вместе с машинами layout_settings_wash_number
        layout_settings_washer.setVisibility(View.INVISIBLE);
        // кнопки машинки
        btn_settings_washer_base.setEnabled(true);

        // раздел утюги
        layout_settings_iron.setVisibility(View.INVISIBLE);
        // кнопки утюгов
        btn_settings_iron_base.setEnabled(true);

        // раздел смс
        layout_settings_sms.setVisibility(View.INVISIBLE);
        // кнопки смс
        btn_settings_sms_base.setEnabled(true);
        btn_settings_sms_director.setEnabled(true);
        btn_settings_sms_manager.setEnabled(true);
        btn_settings_sms_otchet_admin.setEnabled(true);
        btn_settings_sms_otchet_start_washer.setEnabled(true);
        btn_settings_sms_otchet_client.setEnabled(true);
        btn_settings_sms_otchet_power.setEnabled(true);
        btn_settings_sms_read_command.setEnabled(true);

        // раздел bluetooth
        layout_settings_bluetooth.setVisibility(View.INVISIBLE);
        // кнопки bluetooth
        btn_settings_bluetooth_base.setEnabled(true);
        if (!mBluetoothAdapter.isEnabled()) {
            btn_settings_bluetooth_adapter.setChecked(false);
        } else {
            btn_settings_bluetooth_adapter.setChecked(true);
        }
        btn_settings_bluetooth_list.setEnabled(true);
        btn_settings_bluetooth_search.setEnabled(true);
        // раздел тарифы
        layout_settings_tarif.setVisibility(View.INVISIBLE);
        // кнопки тарифов
        btn_settings_tarif_base.setEnabled(true);
        btn_settings_tarif_valuta.setEnabled(true);
        btn_settings_tarif_wash.setEnabled(true);
        btn_settings_tarif_yandex.setEnabled(true);
        btn_settings_tarif_mode.setEnabled(true);
        btn_settings_tarif_iron.setEnabled(true);
        btn_settings_mode_name.setEnabled(true);
        btn_settings_tarif_sber.setEnabled(true);
        btn_settings_tarif_qiwi.setEnabled(true);
        // раздел система
        layout_settings_system.setVisibility(View.INVISIBLE);
        // кнопки
        btn_settings_system_base.setEnabled(true);
        btn_settings_system_log_view.setEnabled(true);
        btn_settings_system_com.setEnabled(true);
        btn_settings_system_destroy_upgrade.setEnabled(true);
        btn_settings_system_licence.setEnabled(true);
        btn_settings_system_blockcalls.setEnabled(true);
        btn_settings_system_optionsplus.setEnabled(true);
        btn_settings_system_fullscreen.setEnabled(true);
        btn_settings_system_autostart.setEnabled(true);
        btn_settings_system_keyboard.setEnabled(true);
        btn_settings_system_display.setEnabled(true);
        btn_settings_system_autoreboot.setEnabled(true);
        btn_settings_system_camera.setEnabled(true);
        btn_settings_system_battary.setEnabled(true);
        // подразделы системы
        layout_system_log.setVisibility(View.INVISIBLE);
        // кнопка возврата назад в меню ВАЖНО!
        //  btn_close_layout_system_log.setEnabled(true);
        layout_log.setVisibility(View.INVISIBLE);
        btn_close_layout_com.setEnabled(true);
        btn_close_layout_com.setVisibility(View.VISIBLE);
        // раздел статистика, включать вместе с машинами layout_settings_wash_number
        layout_settings_stat.setVisibility(View.INVISIBLE);
        // инкассация
        btn_settings_stat_inkass.setEnabled(true);
        // загрузка статистики
        // очистка старых данных перед записью новых

        statisticWash1ArrayAdapter.clear();
        statisticWash2ArrayAdapter.clear();
        statisticWash3ArrayAdapter.clear();
        statisticWash4ArrayAdapter.clear();
        statisticWash5ArrayAdapter.clear();
        statisticWash6ArrayAdapter.clear();
        statisticWash7ArrayAdapter.clear();
        statisticWash8ArrayAdapter.clear();
        statisticWash9ArrayAdapter.clear();
        statisticWash10ArrayAdapter.clear();
        statisticWash11ArrayAdapter.clear();
        statisticWash12ArrayAdapter.clear();

        int wash1countall = wash1statcounter + wash1statcounter_mode1 +
                wash1statcounter_mode2 + wash1statcounter_mode3 + wash1statcounter_mode4 +
                wash1statcounter_mode5 + wash1statcounter_mode6;
        int wash2countall = wash2statcounter + wash2statcounter_mode1 +
                wash2statcounter_mode2 + wash2statcounter_mode3 + wash2statcounter_mode4 +
                wash2statcounter_mode5 + wash2statcounter_mode6;
        int wash3countall = wash3statcounter + wash3statcounter_mode1 +
                wash3statcounter_mode2 + wash3statcounter_mode3 + wash3statcounter_mode4 +
                wash3statcounter_mode5 + wash3statcounter_mode6;
        int wash4countall = wash4statcounter + wash4statcounter_mode1 +
                wash4statcounter_mode2 + wash4statcounter_mode3 + wash4statcounter_mode4 +
                wash4statcounter_mode5 + wash4statcounter_mode6;
        int wash5countall = wash5statcounter + wash5statcounter_mode1 +
                wash5statcounter_mode2 + wash5statcounter_mode3 + wash5statcounter_mode4 +
                wash5statcounter_mode5 + wash5statcounter_mode6;
        int wash6countall = wash6statcounter + wash6statcounter_mode1 +
                wash6statcounter_mode2 + wash6statcounter_mode3 + wash6statcounter_mode4 +
                wash6statcounter_mode5 + wash6statcounter_mode6;
        int wash7countall = wash7statcounter + wash7statcounter_mode1 +
                wash7statcounter_mode2 + wash7statcounter_mode3 + wash7statcounter_mode4 +
                wash7statcounter_mode5 + wash7statcounter_mode6;
        int wash8countall = wash8statcounter + wash8statcounter_mode1 +
                wash8statcounter_mode2 + wash8statcounter_mode3 + wash8statcounter_mode4 +
                wash8statcounter_mode5 + wash8statcounter_mode6;
        int wash9countall = wash9statcounter + wash9statcounter_mode1 +
                wash9statcounter_mode2 + wash9statcounter_mode3 + wash9statcounter_mode4 +
                wash9statcounter_mode5 + wash9statcounter_mode6;
        int wash10countall = wash10statcounter + wash10statcounter_mode1 +
                wash10statcounter_mode2 + wash10statcounter_mode3 + wash10statcounter_mode4 +
                wash10statcounter_mode5 + wash10statcounter_mode6;
        int wash11countall = wash11statcounter + wash11statcounter_mode1 +
                wash11statcounter_mode2 + wash11statcounter_mode3 + wash11statcounter_mode4 +
                wash11statcounter_mode5 + wash11statcounter_mode6;
        int wash12countall = wash12statcounter +
                wash12statcounter_mode1 +
                wash12statcounter_mode2 +
                wash12statcounter_mode3 +
                wash12statcounter_mode4 +
                wash12statcounter_mode5 +
                wash12statcounter_mode6;

        statisticWash1ArrayAdapter.add("Всего стирок: " + wash1countall);
        statisticWash1ArrayAdapter.add("Запусков по старту: " + wash1statcounter);
        statisticWash1ArrayAdapter.add("Запусков по режимам: ");
        statisticWash1ArrayAdapter.add("Быстрая: "     + wash1statcounter_mode1);
        statisticWash1ArrayAdapter.add("Интенсивная: " + wash1statcounter_mode2);
        statisticWash1ArrayAdapter.add("Хлопок: "      + wash1statcounter_mode3);
        statisticWash1ArrayAdapter.add("Шерсть: "      + wash1statcounter_mode4);
        statisticWash1ArrayAdapter.add("Синтетика: "   + wash1statcounter_mode5);
        statisticWash1ArrayAdapter.add("Полоскание: " + wash1statcounter_mode6);
        statisticWash1ArrayAdapter.add("Запусков по СМС: ");
        statisticWash1ArrayAdapter.add("Общее кол:"  + wash1statcounter_sms);
        statisticWash1ArrayAdapter.add("СМС Директора: откл");
        statisticWash1ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash1ArrayAdapter.add("Показатели машины:");
        statisticWash1ArrayAdapter.add("Ремонтов: откл");
        statisticWash1ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash1ArrayAdapter.add("Заработано денег: откл");


        statisticWash2ArrayAdapter.add("Всего стирок: " + wash2countall);
        statisticWash2ArrayAdapter.add("Запусков по старту: " + wash2statcounter);
        statisticWash2ArrayAdapter.add("Запусков по режимам: ");
        statisticWash2ArrayAdapter.add("Быстрая: "     + wash2statcounter_mode1);
        statisticWash2ArrayAdapter.add("Интенсивная: " + wash2statcounter_mode2);
        statisticWash2ArrayAdapter.add("Хлопок: "      + wash2statcounter_mode3);
        statisticWash2ArrayAdapter.add("Шерсть: "      + wash2statcounter_mode4);
        statisticWash2ArrayAdapter.add("Синтетика: "   + wash2statcounter_mode5);
        statisticWash2ArrayAdapter.add("Полоскание: "  + wash2statcounter_mode6);
        statisticWash2ArrayAdapter.add("Запусков по СМС: ");
        statisticWash2ArrayAdapter.add("Общее кол:"  + wash2statcounter_sms);
        statisticWash2ArrayAdapter.add("СМС Директора: откл");
        statisticWash2ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash2ArrayAdapter.add("Показатели машины:");
        statisticWash2ArrayAdapter.add("Ремонтов: откл");
        statisticWash2ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash2ArrayAdapter.add("Заработано денег: откл");

        statisticWash3ArrayAdapter.add("Всего стирок: " + wash3countall);
        statisticWash3ArrayAdapter.add("Запусков по старту: " + wash3statcounter);
        statisticWash3ArrayAdapter.add("Запусков по режимам: ");
        statisticWash3ArrayAdapter.add("Быстрая: "     + wash3statcounter_mode1);
        statisticWash3ArrayAdapter.add("Интенсивная: " + wash3statcounter_mode2);
        statisticWash3ArrayAdapter.add("Хлопок: "      + wash3statcounter_mode3);
        statisticWash3ArrayAdapter.add("Шерсть: "      + wash3statcounter_mode4);
        statisticWash3ArrayAdapter.add("Синтетика: "   + wash3statcounter_mode5);
        statisticWash3ArrayAdapter.add("Полоскание: "  + wash3statcounter_mode6);
        statisticWash3ArrayAdapter.add("Запусков по СМС: ");
        statisticWash3ArrayAdapter.add("Общее кол:"  + wash3statcounter_sms);
        statisticWash3ArrayAdapter.add("СМС Директора: откл");
        statisticWash3ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash3ArrayAdapter.add("Показатели машины:");
        statisticWash3ArrayAdapter.add("Ремонтов: откл");
        statisticWash3ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash3ArrayAdapter.add("Заработано денег: откл");

        statisticWash4ArrayAdapter.add("Всего стирок: " + wash4countall);
        statisticWash4ArrayAdapter.add("Запусков по старту: " + wash4statcounter);
        statisticWash4ArrayAdapter.add("Запусков по режимам: ");
        statisticWash4ArrayAdapter.add("Быстрая: "     + wash4statcounter_mode1);
        statisticWash4ArrayAdapter.add("Интенсивная: " + wash4statcounter_mode2);
        statisticWash4ArrayAdapter.add("Хлопок: "      + wash4statcounter_mode3);
        statisticWash4ArrayAdapter.add("Шерсть: "      + wash4statcounter_mode4);
        statisticWash4ArrayAdapter.add("Синтетика: "   + wash4statcounter_mode5);
        statisticWash4ArrayAdapter.add("Полоскание: " + wash4statcounter_mode6);
        statisticWash4ArrayAdapter.add("Запусков по СМС: ");
        statisticWash4ArrayAdapter.add("Общее кол:"  + wash4statcounter_sms);
        statisticWash4ArrayAdapter.add("СМС Директора: откл");
        statisticWash4ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash4ArrayAdapter.add("Показатели машины:");
        statisticWash4ArrayAdapter.add("Ремонтов: откл");
        statisticWash4ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash4ArrayAdapter.add("Заработано денег: откл");

        statisticWash5ArrayAdapter.add("Всего стирок: " + wash5countall);
        statisticWash5ArrayAdapter.add("Запусков по старту: " + wash5statcounter);
        statisticWash5ArrayAdapter.add("Запусков по режимам: ");
        statisticWash5ArrayAdapter.add("Быстрая: "     + wash5statcounter_mode1);
        statisticWash5ArrayAdapter.add("Интенсивная: " + wash5statcounter_mode2);
        statisticWash5ArrayAdapter.add("Хлопок: "      + wash5statcounter_mode3);
        statisticWash5ArrayAdapter.add("Шерсть: "      + wash5statcounter_mode4);
        statisticWash5ArrayAdapter.add("Синтетика: "   + wash5statcounter_mode5);
        statisticWash5ArrayAdapter.add("Полоскание: "  + wash5statcounter_mode6);
        statisticWash5ArrayAdapter.add("Запусков по СМС: ");
        statisticWash5ArrayAdapter.add("Общее кол:"  + wash5statcounter_sms);
        statisticWash5ArrayAdapter.add("СМС Директора: откл");
        statisticWash5ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash5ArrayAdapter.add("Показатели машины:");
        statisticWash5ArrayAdapter.add("Ремонтов: откл");
        statisticWash5ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash5ArrayAdapter.add("Заработано денег: откл");

        statisticWash6ArrayAdapter.add("Всего стирок: " + wash6countall);
        statisticWash6ArrayAdapter.add("Запусков по старту: " + wash6statcounter);
        statisticWash6ArrayAdapter.add("Запусков по режимам: ");
        statisticWash6ArrayAdapter.add("Быстрая: "     + wash6statcounter_mode1);
        statisticWash6ArrayAdapter.add("Интенсивная: " + wash6statcounter_mode2);
        statisticWash6ArrayAdapter.add("Хлопок: " + wash6statcounter_mode3);
        statisticWash6ArrayAdapter.add("Шерсть: " + wash6statcounter_mode4);
        statisticWash6ArrayAdapter.add("Синтетика: " + wash6statcounter_mode5);
        statisticWash6ArrayAdapter.add("Полоскание: " + wash6statcounter_mode6);
        statisticWash6ArrayAdapter.add("Запусков по СМС: ");
        statisticWash6ArrayAdapter.add("Общее кол:"  + wash6statcounter_sms);
        statisticWash6ArrayAdapter.add("СМС Директора: откл");
        statisticWash6ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash6ArrayAdapter.add("Показатели машины:");
        statisticWash6ArrayAdapter.add("Ремонтов: откл");
        statisticWash6ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash6ArrayAdapter.add("Заработано денег: откл");

        statisticWash7ArrayAdapter.add("Всего стирок: " + wash7countall);
        statisticWash7ArrayAdapter.add("Запусков по старту: " + wash7statcounter);
        statisticWash7ArrayAdapter.add("Запусков по режимам: ");
        statisticWash7ArrayAdapter.add("Быстрая: "     + wash7statcounter_mode1);
        statisticWash7ArrayAdapter.add("Интенсивная: " + wash7statcounter_mode2);
        statisticWash7ArrayAdapter.add("Хлопок: "      + wash7statcounter_mode3);
        statisticWash7ArrayAdapter.add("Шерсть: "      + wash7statcounter_mode4);
        statisticWash7ArrayAdapter.add("Синтетика: "   + wash7statcounter_mode5);
        statisticWash7ArrayAdapter.add("Полоскание: " + wash7statcounter_mode6);
        statisticWash7ArrayAdapter.add("Запусков по СМС: ");
        statisticWash7ArrayAdapter.add("Общее кол:"  + wash7statcounter_sms);
        statisticWash7ArrayAdapter.add("СМС Директора: откл");
        statisticWash7ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash7ArrayAdapter.add("Показатели машины:");
        statisticWash7ArrayAdapter.add("Ремонтов: откл");
        statisticWash7ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash7ArrayAdapter.add("Заработано денег: откл");

        statisticWash8ArrayAdapter.add("Всего стирок: " + wash8countall);
        statisticWash8ArrayAdapter.add("Запусков по старту: " + wash8statcounter);
        statisticWash8ArrayAdapter.add("Запусков по режимам: ");
        statisticWash8ArrayAdapter.add("Быстрая: "     + wash8statcounter_mode1);
        statisticWash8ArrayAdapter.add("Интенсивная: " + wash8statcounter_mode2);
        statisticWash8ArrayAdapter.add("Хлопок: "      + wash8statcounter_mode3);
        statisticWash8ArrayAdapter.add("Шерсть: "      + wash8statcounter_mode4);
        statisticWash8ArrayAdapter.add("Синтетика: "   + wash8statcounter_mode5);
        statisticWash8ArrayAdapter.add("Полоскание: "  + wash8statcounter_mode6);
        statisticWash8ArrayAdapter.add("Запусков по СМС: ");
        statisticWash8ArrayAdapter.add("Общее кол:"  + wash8statcounter_sms);
        statisticWash8ArrayAdapter.add("СМС Директора: откл");
        statisticWash8ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash8ArrayAdapter.add("Показатели машины:");
        statisticWash8ArrayAdapter.add("Ремонтов: откл");
        statisticWash8ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash8ArrayAdapter.add("Заработано денег: откл");

        statisticWash9ArrayAdapter.add("Всего стирок: " + wash9countall);
        statisticWash9ArrayAdapter.add("Запусков по старту: " + wash9statcounter);
        statisticWash9ArrayAdapter.add("Запусков по режимам: ");
        statisticWash9ArrayAdapter.add("Быстрая: "     + wash9statcounter_mode1);
        statisticWash9ArrayAdapter.add("Интенсивная: " + wash9statcounter_mode2);
        statisticWash9ArrayAdapter.add("Хлопок: "      + wash9statcounter_mode3);
        statisticWash9ArrayAdapter.add("Шерсть: "      + wash9statcounter_mode4);
        statisticWash9ArrayAdapter.add("Синтетика: "   + wash9statcounter_mode5);
        statisticWash9ArrayAdapter.add("Полоскание: "  + wash9statcounter_mode6);
        statisticWash9ArrayAdapter.add("Запусков по СМС: ");
        statisticWash9ArrayAdapter.add("Общее кол:"  + wash9statcounter_sms);
        statisticWash9ArrayAdapter.add("СМС Директора: откл");
        statisticWash9ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash9ArrayAdapter.add("Показатели машины:");
        statisticWash9ArrayAdapter.add("Ремонтов: откл");
        statisticWash9ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash9ArrayAdapter.add("Заработано денег: откл");

        statisticWash10ArrayAdapter.add("Всего стирок: " + wash10countall);
        statisticWash10ArrayAdapter.add("Запусков по старту: " + wash10statcounter);
        statisticWash10ArrayAdapter.add("Запусков по режимам: ");
        statisticWash10ArrayAdapter.add("Быстрая: "     + wash10statcounter_mode1);
        statisticWash10ArrayAdapter.add("Интенсивная: " + wash10statcounter_mode2);
        statisticWash10ArrayAdapter.add("Хлопок: "      + wash10statcounter_mode3);
        statisticWash10ArrayAdapter.add("Шерсть: "      + wash10statcounter_mode4);
        statisticWash10ArrayAdapter.add("Синтетика: "   + wash10statcounter_mode5);
        statisticWash10ArrayAdapter.add("Полоскание: " + wash10statcounter_mode6);
        statisticWash10ArrayAdapter.add("Запусков по СМС: ");
        statisticWash10ArrayAdapter.add("Общее кол:"  + wash10statcounter_sms);
        statisticWash10ArrayAdapter.add("СМС Директора: откл");
        statisticWash10ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash10ArrayAdapter.add("Показатели машины:");
        statisticWash10ArrayAdapter.add("Ремонтов: откл");
        statisticWash10ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash10ArrayAdapter.add("Заработано денег: откл");

        statisticWash11ArrayAdapter.add("Всего стирок: " + wash11countall);
        statisticWash11ArrayAdapter.add("Запусков по старту: " + wash11statcounter);
        statisticWash11ArrayAdapter.add("Запусков по режимам: ");
        statisticWash11ArrayAdapter.add("Быстрая: "     + wash11statcounter_mode1);
        statisticWash11ArrayAdapter.add("Интенсивная: " + wash11statcounter_mode2);
        statisticWash11ArrayAdapter.add("Хлопок: "      + wash11statcounter_mode3);
        statisticWash11ArrayAdapter.add("Шерсть: "      + wash11statcounter_mode4);
        statisticWash11ArrayAdapter.add("Синтетика: "   + wash11statcounter_mode5);
        statisticWash11ArrayAdapter.add("Полоскание: "  + wash11statcounter_mode6);
        statisticWash11ArrayAdapter.add("Запусков по СМС: ");
        statisticWash11ArrayAdapter.add("Общее кол:"  + wash11statcounter_sms);
        statisticWash11ArrayAdapter.add("СМС Директора: откл");
        statisticWash11ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash11ArrayAdapter.add("Показатели машины:");
        statisticWash11ArrayAdapter.add("Ремонтов: откл");
        statisticWash11ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash11ArrayAdapter.add("Заработано денег: откл");

        statisticWash12ArrayAdapter.add("Всего стирок: " + wash12countall);
        statisticWash12ArrayAdapter.add("Запусков по старту: " + wash12statcounter);
        statisticWash12ArrayAdapter.add("Запусков по режимам: ");
        statisticWash12ArrayAdapter.add("Быстрая = "     + wash12statcounter_mode1);
        statisticWash12ArrayAdapter.add("Интенсивная = " + wash12statcounter_mode2);
        statisticWash12ArrayAdapter.add("Хлопок = " + wash12statcounter_mode3);
        statisticWash12ArrayAdapter.add("Шерсть = " + wash12statcounter_mode4);
        statisticWash12ArrayAdapter.add("Синтетика = " + wash12statcounter_mode5);
        statisticWash12ArrayAdapter.add("Полоскание = " + wash12statcounter_mode6);
        statisticWash12ArrayAdapter.add("Запусков по СМС: ");
        statisticWash12ArrayAdapter.add("Общее кол:"  + wash12statcounter_sms);
        statisticWash12ArrayAdapter.add("СМС Директора: откл");
        statisticWash12ArrayAdapter.add("СМС Менеджера: откл");
        statisticWash12ArrayAdapter.add("Показатели машины:");
        statisticWash12ArrayAdapter.add("Ремонтов: откл");
        statisticWash12ArrayAdapter.add("Ошибки датчиков: откл");
        statisticWash12ArrayAdapter.add("Заработано денег: откл");



        // фрейм для отображения стиралок, использ.в машинах и статистике
        layout_settings_wash_number.setVisibility(View.INVISIBLE);
        // вложенные машинки

        // параметры по умолчанию ---------------------  начало ------------------------
        machine1_settings.setVisibility(View.GONE);
        machine2_settings.setVisibility(View.GONE);
        machine3_settings.setVisibility(View.GONE);
        machine4_settings.setVisibility(View.GONE);
        machine5_settings.setVisibility(View.GONE);
        machine6_settings.setVisibility(View.GONE);
        machine7_settings.setVisibility(View.GONE);
        machine8_settings.setVisibility(View.GONE);
        machine9_settings.setVisibility(View.GONE);
        machine10_settings.setVisibility(View.GONE);
        machine11_settings.setVisibility(View.GONE);
        machine12_settings.setVisibility(View.GONE);

        layout_wash1_settings.setVisibility(View.VISIBLE);
        layout_wash2_settings.setVisibility(View.VISIBLE);
        layout_wash3_settings.setVisibility(View.VISIBLE);
        layout_wash4_settings.setVisibility(View.VISIBLE);
        layout_wash5_settings.setVisibility(View.VISIBLE);
        layout_wash6_settings.setVisibility(View.VISIBLE);
        layout_wash7_settings.setVisibility(View.VISIBLE);
        layout_wash8_settings.setVisibility(View.VISIBLE);
        layout_wash9_settings.setVisibility(View.VISIBLE);
        layout_wash10_settings.setVisibility(View.VISIBLE);
        layout_wash11_settings.setVisibility(View.VISIBLE);
        layout_wash12_settings.setVisibility(View.VISIBLE);

        num1_settings.setVisibility(View.VISIBLE);
        num1_settings.setEnabled(true);
        num2_settings.setVisibility(View.VISIBLE);
        num2_settings.setEnabled(true);
        num3_settings.setVisibility(View.VISIBLE);
        num3_settings.setEnabled(true);
        num4_settings.setVisibility(View.VISIBLE);
        num4_settings.setEnabled(true);
        num5_settings.setVisibility(View.VISIBLE);
        num5_settings.setEnabled(true);
        num6_settings.setVisibility(View.VISIBLE);
        num6_settings.setEnabled(true);
        num7_settings.setVisibility(View.VISIBLE);
        num7_settings.setEnabled(true);
        num8_settings.setVisibility(View.VISIBLE);
        num8_settings.setEnabled(true);
        num9_settings.setVisibility(View.VISIBLE);
        num9_settings.setEnabled(true);
        num10_settings.setVisibility(View.VISIBLE);
        num10_settings.setEnabled(true);
        num11_settings.setVisibility(View.VISIBLE);
        num11_settings.setEnabled(true);
        num12_settings.setVisibility(View.VISIBLE);
        num12_settings.setEnabled(true);

        wash1_settings.setVisibility(View.VISIBLE);
        wash2_settings.setVisibility(View.VISIBLE);
        wash3_settings.setVisibility(View.VISIBLE);
        wash4_settings.setVisibility(View.VISIBLE);
        wash5_settings.setVisibility(View.VISIBLE);
        wash6_settings.setVisibility(View.VISIBLE);
        wash7_settings.setVisibility(View.VISIBLE);
        wash8_settings.setVisibility(View.VISIBLE);
        wash9_settings.setVisibility(View.VISIBLE);
        wash10_settings.setVisibility(View.VISIBLE);
        wash11_settings.setVisibility(View.VISIBLE);
        wash12_settings.setVisibility(View.VISIBLE);

        // параметры по умолчанию -----------------------------  конец --------------------------


        // все настроено под вывод в раздел машинки
        // для отображения в статистике добавить видимость "ArrayAdapter"-ов
        if (countwash == 0) { countwash = 12; }
        if (countwash >= 1) {
            //layout_wash1_settings.setVisibility(View.VISIBLE);
            machine1_settings.setVisibility(View.VISIBLE);
            // num1_settings.setVisibility(View.VISIBLE);
            // num1_settings.setEnabled(false);
            // wash1_settings.setVisibility(View.VISIBLE);
            if (wash1isdry == 1){ dry1_settings.setVisibility(View.VISIBLE);}
            else { dry1_settings.setVisibility(View.INVISIBLE); }
            if (wash1remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 2) {
            //layout_wash2_settings.setVisibility(View.VISIBLE);
            machine2_settings.setVisibility(View.VISIBLE);
            // num2_settings.setVisibility(View.VISIBLE);
            // num2_settings.setEnabled(false);
            // wash2_settings.setVisibility(View.VISIBLE);
            if (wash2isdry == 1){ dry2_settings.setVisibility(View.VISIBLE);}
            else { dry2_settings.setVisibility(View.INVISIBLE); }
            if (wash2remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 3) {
            // layout_wash3_settings.setVisibility(View.VISIBLE);
            machine3_settings.setVisibility(View.VISIBLE);
            // num3_settings.setVisibility(View.VISIBLE);
            // num3_settings.setEnabled(false);
            // wash3_settings.setVisibility(View.VISIBLE);
            if (wash3isdry == 1){ dry3_settings.setVisibility(View.VISIBLE);}
            else { dry3_settings.setVisibility(View.INVISIBLE); }
            if (wash3remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 4) {
            //  layout_wash4_settings.setVisibility(View.VISIBLE);
            machine4_settings.setVisibility(View.VISIBLE);
            //  num4_settings.setVisibility(View.VISIBLE);
            //  num4_settings.setEnabled(false);
            //  wash4_settings.setVisibility(View.VISIBLE);
            if (wash4isdry == 1){ dry4_settings.setVisibility(View.VISIBLE);}
            else { dry4_settings.setVisibility(View.INVISIBLE); }
            if (wash4remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 5) {
            //  layout_wash5_settings.setVisibility(View.VISIBLE);
            machine5_settings.setVisibility(View.VISIBLE);
            //  num5_settings.setVisibility(View.VISIBLE);
            //  num5_settings.setEnabled(false);
            //  wash5_settings.setVisibility(View.VISIBLE);
            if (wash5isdry == 1){ dry5_settings.setVisibility(View.VISIBLE);}
            else { dry5_settings.setVisibility(View.INVISIBLE); }
            if (wash5remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 6) {
            //  layout_wash6_settings.setVisibility(View.VISIBLE);
            machine6_settings.setVisibility(View.VISIBLE);
            //  num6_settings.setVisibility(View.VISIBLE);
            //  num6_settings.setEnabled(false);
            // wash6_settings.setVisibility(View.VISIBLE);
            if (wash6isdry == 1){ dry6_settings.setVisibility(View.VISIBLE);}
            else { dry6_settings.setVisibility(View.INVISIBLE); }
            if (wash6remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 7) {
            //  layout_wash7_settings.setVisibility(View.VISIBLE);
            machine7_settings.setVisibility(View.VISIBLE);
            //  num7_settings.setVisibility(View.VISIBLE);
            //  num7_settings.setEnabled(false);
            //  wash7_settings.setVisibility(View.VISIBLE);
            if (wash7isdry == 1){ dry7_settings.setVisibility(View.VISIBLE);}
            else { dry7_settings.setVisibility(View.INVISIBLE); }
            if (wash7remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 8) {
            //  layout_wash8_settings.setVisibility(View.VISIBLE);
            machine8_settings.setVisibility(View.VISIBLE);
            //  num8_settings.setVisibility(View.VISIBLE);
            //  num8_settings.setEnabled(false);
            //  wash8_settings.setVisibility(View.VISIBLE);
            if (wash8isdry == 1){ dry8_settings.setVisibility(View.VISIBLE);}
            else { dry8_settings.setVisibility(View.INVISIBLE); }
            if (wash8remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 9) {
            //  layout_wash9_settings.setVisibility(View.VISIBLE);
            machine9_settings.setVisibility(View.VISIBLE);
            //  num9_settings.setVisibility(View.VISIBLE);
            //  num9_settings.setEnabled(false);
            //  wash9_settings.setVisibility(View.VISIBLE);
            if (wash9isdry == 1){ dry9_settings.setVisibility(View.VISIBLE);}
            else { dry9_settings.setVisibility(View.INVISIBLE); }
            if (wash9remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 10) {
            //  layout_wash10_settings.setVisibility(View.VISIBLE);
            machine10_settings.setVisibility(View.VISIBLE);
            //  num10_settings.setVisibility(View.VISIBLE);
            //  num10_settings.setEnabled(false);
            //  wash10_settings.setVisibility(View.VISIBLE);
            if (wash10isdry == 1){ dry10_settings.setVisibility(View.VISIBLE);}
            else { dry10_settings.setVisibility(View.INVISIBLE); }
            if (wash10remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 11) {
            //   layout_wash11_settings.setVisibility(View.VISIBLE);
            machine11_settings.setVisibility(View.VISIBLE);
            //   num11_settings.setVisibility(View.VISIBLE);
            //   num11_settings.setEnabled(false);
            //  wash11_settings.setVisibility(View.VISIBLE);
            if (wash11isdry == 1){ dry11_settings.setVisibility(View.VISIBLE);}
            else { dry11_settings.setVisibility(View.INVISIBLE); }
            if (wash11remont ==1) {} } // потом можно подвесить иконку ремонта
        if (countwash >= 12) {
            //   layout_wash12_settings.setVisibility(View.VISIBLE);
            machine12_settings.setVisibility(View.VISIBLE);
            //   num12_settings.setVisibility(View.VISIBLE);
            //   num12_settings.setEnabled(false);
            //   wash12_settings.setVisibility(View.VISIBLE);
            if (wash12isdry == 1){ dry12_settings.setVisibility(View.VISIBLE);}
            else { dry12_settings.setVisibility(View.INVISIBLE); }
            if (wash12remont ==1) {} } // потом можно подвесить иконку ремонта



        // Готовый блок Титульного меню с выбором категорий

        btn1_settings_choose.setVisibility(View.VISIBLE);
        btn1_settings_choose.setEnabled(true);
        btn2_settings_choose.setVisibility(View.VISIBLE);
        btn2_settings_choose.setEnabled(true);
        btn3_settings_choose.setVisibility(View.VISIBLE);
        btn3_settings_choose.setEnabled(true);
        btn4_settings_choose.setVisibility(View.VISIBLE);
        btn4_settings_choose.setEnabled(true);
        btn5_settings_choose.setVisibility(View.VISIBLE);
        btn5_settings_choose.setEnabled(true);
        btn6_settings_choose.setVisibility(View.VISIBLE);
        btn6_settings_choose.setEnabled(true);
        // ------------------  НЕ УДАЛЯТЬ!  ---------------



        // ****************************************** PS: **************************
        // ПЕРЕШЛИ В МЕНЮ В КАТЕГОРИЮ - "ТАРИФЫ"
        // ВКЛЮЧИЛИ ШТОРКУ
        // АКТИВИРОВАЛИ ВСЕ КНОПКИ, НО СКРЫЛИ ДРУГИЕ КАТЕГОРИИ (РАЗДЕЛЫ)
        // ПРОПИСАТЬ НАЖАТИЕ КНОПОК НАВИГАЦИИ С ПРОСТЫМ VISIBILITY ФРЕЙМОВ
        // ВНУТРИ САМИХ ФРЕЙМОВ УЖЕ ВСЕ НАСТРОЕНО
        // УПРАВЛЕНИЕ ШТОРКОЙ НЕ ЗАБЫТЬ
    }



    private void displayInfo() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = getActivity().registerReceiver(null, ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        // String statusStr;
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                //statusStr = "BATTERY_STATUS_CHARGING";
                //statusStr = "Идет зарядка батареи";
                statusStr = "1";
                break;
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                // statusStr = "BATTERY_STATUS_DISCHARGING";
                break;
            case BatteryManager.BATTERY_STATUS_FULL:
                // statusStr = "BATTERY_STATUS_FULL";
                // statusStr = "Питание от электросети";
                statusStr = "1";
                break;
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                // statusStr = "BATTERY_STATUS_NOT_CHARGING";
                // statusStr = "Питание от батареи";
                statusStr = "2";
                break;
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                // statusStr = "BATTERY_STATUS_UNKNOWN";
                break;
            default:
                // statusStr = "UNDEFINED";
                statusStr = "2";
        }
    }



    /*
     *  SAVE MONEY FROM INTERNET PAY (SMS CHECK, SIM BEELINE)
     */
    private void save_money_from_internet(int cash) {
        int vnes = money_vneseno + cash;
        money_kup.setText("" + vnes);
        money_vneseno = vnes;
        money = money + cash;
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
        editor.putInt(APP_PREFERENCES_MONEY, money);
        editor.apply();
        mConversationArrayAdapter.add("INTERNET_PAY:  +"+cash+" RUB");
        Toast.makeText(getActivity(), "Внесение денег: " + cash + " руб ", Toast.LENGTH_LONG).show();
    }




    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            if (null != activity) {
                                final Animation animationRotateCenter = AnimationUtils.loadAnimation(
                                        activity, R.anim.rotate);
                                connecting_animation.clearAnimation();
                            }
                            counter_bluetooth_state = 0; // заново отсчитаем 6 нажатий при потере соединения и выведем список спаренных
                            arduinoLife(); // запускает жизненный цикл ардуино
                            go_home();
                            break;
                        case BluetoothChatService.STATE_CONNECTING:

                            setStatus(R.string.title_connecting);
                            if (null != activity) {
                                final Animation animationRotateCenter = AnimationUtils.loadAnimation(
                                        activity, R.anim.rotate);
                                connecting_animation.startAnimation(animationRotateCenter);
                            }
                            // VISIBLE LAYOUTS SCREEN
                            // закрываем прозрачную кнопку во весь экран для поиска bluetooth
                            layout_bt_search.setVisibility(View.VISIBLE);
                            layout_bt_not.setVisibility(View.GONE);
                            // параметры фрейма
                            img_fullscreen_search_bluetooth.setVisibility(View.VISIBLE);
                            img_fullscreen_search_bluetooth.setEnabled(true);
                            // VISIBLE LAYOUTS SCREEN
                            layout_number_selected.setVisibility(View.GONE);
                            layout_prepair_start.setVisibility(View.GONE);
                            layout_choose_mode.setVisibility(View.GONE);
                            layout_start_anim.setVisibility(View.GONE);
                            layout_settings.setVisibility(View.GONE);
                            layout_home.setVisibility(View.GONE);

                            // запускаем бар
                            layout_action_bar.setVisibility(View.VISIBLE);
                            // параметры бара
                            img_action_bar_icon_wash.setVisibility(View.GONE);
                            wash_number.setVisibility(View.GONE);
                            img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
                            money_kup.setVisibility(View.VISIBLE);
                            img_action_bar_icon_not_connected.setVisibility(View.GONE);
                            img_action_bar_icon_lost_connected.setVisibility(View.VISIBLE);
                            img_action_bar_icon_laundry_connected.setVisibility(View.GONE);
                            displayInfo();
                            if (statusStr.contentEquals("1")){
                                img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                                img_action_bar_icon_battary.setVisibility(View.GONE);
                            } else {
                                img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                                img_action_bar_icon_electro.setVisibility(View.GONE);
                            }

                            break;
                       // case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            setStatus(R.string.title_not_connected);

                            // VISIBLE LAYOUTS SCREEN
                            // выводим прозрачную кнопку во весь экран для поиска bluetooth
                            layout_bt_not.setVisibility(View.VISIBLE);
                            // параметры фрейма
                            img_fullscreen_search_bluetooth.setVisibility(View.VISIBLE);
                            img_fullscreen_search_bluetooth.setEnabled(true);
                            // VISIBLE LAYOUTS SCREEN
                            layout_number_selected.setVisibility(View.GONE);
                            layout_prepair_start.setVisibility(View.GONE);
                            layout_choose_mode.setVisibility(View.GONE);
                            layout_start_anim.setVisibility(View.GONE);
                            layout_bt_search.setVisibility(View.GONE);
                            layout_settings.setVisibility(View.GONE);
                            layout_home.setVisibility(View.GONE);

                            // запускаем бар
                            layout_action_bar.setVisibility(View.VISIBLE);
                            // параметры бара
                            img_action_bar_icon_wash.setVisibility(View.GONE);
                            wash_number.setVisibility(View.GONE);
                            img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
                            money_kup.setVisibility(View.VISIBLE);
                            img_action_bar_icon_not_connected.setVisibility(View.VISIBLE);
                            img_action_bar_icon_lost_connected.setVisibility(View.GONE);
                            img_action_bar_icon_laundry_connected.setVisibility(View.GONE);
                            displayInfo();
                            if (statusStr.contentEquals("1")){
                                img_action_bar_icon_electro.setVisibility(View.VISIBLE);
                                img_action_bar_icon_battary.setVisibility(View.GONE);
                            } else {
                                img_action_bar_icon_battary.setVisibility(View.VISIBLE);
                                img_action_bar_icon_electro.setVisibility(View.GONE);
                            }

                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    mConversationArrayAdapter.add("Send to laundry: ["+timeformat.format(new Date())+"] " + writeMessage);//}
                    break;
                case Constants.MESSAGE_READ:
                    // byte[] readBuf = (byte[]) msg.obj;
                    // String readMessage = new String(msg.obj, 0, msg.arg1);
                    final String readMessage = (String) msg.obj;
                    if (readMessage != null) {
                        mConversationArrayAdapter.add(mConnectedDeviceName+" ["+timeformat.format(new Date())+"] "+readMessage);

/*
                    if (readMessage != null) {
                    StringBuilder msg1 = new StringBuilder();
                    msg1.append(mConnectedDeviceName);
                    msg1.append("[").append(timeformat.format(new Date())).append("]");
                    msg1.append(" - ");
                    msg1.append(readMessage.toString());
                    mConversationArrayAdapter.add(msg1 + "\n");
                  //    mConversationArrayAdapter.add(readMessage.toString());
*/
                        final Animation animationRotateCenter = AnimationUtils.loadAnimation(
                                activity, R.anim.rotate);

                        if (readMessage.contains("B")) { // don't reset arduino
                            arduinoLife();
                        }


                        if (readMessage.contains("c")) {
                            if (valuta == 1) {  // РУБЛИ
                                int vnes = money_vneseno + 10;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 10;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +10 RUB");
                            } else if (valuta == 2) { // ЕВРО
                                int vnes = money_vneseno + 5;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 5;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +5 EUR");
                            } else if (valuta == 3) { // БЕЛОРУСС.РУБЛИ
                                int vnes = money_vneseno + 1000;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 1000;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +1000 BYR");
                            } else if (valuta == 4) { // КАЗАХСКИЕ ТЕНГЕ
                                int vnes = money_vneseno + 100;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 100;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +1000 BYR");
                            }
                        }


                        if (readMessage.contains("d")) {
                            if (valuta == 1) {
                                int vnes = money_vneseno + 10;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 10;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +10 RUB");
                            } else if (valuta == 2) {      // ЕВРО
                                int vnes = money_vneseno + 1;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 1;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +1 EUR");
                            } else if (valuta == 3) {
                                int vnes = money_vneseno + 1000;
                                money_kup.setText("" + vnes);
                                money_vneseno = vnes;
                                money = money + 1000;
                                // Запоминаем данные
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putInt(APP_PREFERENCES_MONEYVNESENO, money_vneseno);
                                editor.putInt(APP_PREFERENCES_MONEY, money);
                                editor.apply();
                                mConversationArrayAdapter.add("Done:  +1000 BYR");
                            }
                        }



                        if (readMessage.contains("b")) {
                            display_settings_load();
                            mConversationArrayAdapter.add("Done:  adminButton_OK");
                        }

                        if (readMessage.contains("a")) {
                            flipper_goto_numbers();


                            mConversationArrayAdapter.add("Done:  laundryView_OK");
                        }

                        if (readMessage.contains("A")) {
                            start_engine(0,0);
                            mConversationArrayAdapter.add("Done:  washerStart_OK");
                        }

                        // первая машина занята
                        if (readMessage.contains("g")) {
                            if (countwash >= 2) { door2_view(); }
                            if (checkdoor == 1) {
                                wash1crash = 1;
                                if (wash1.getVisibility() == View.VISIBLE) {
                                    imageDoor1.setVisibility(View.VISIBLE);
                                    imageDoor1.startAnimation(animationRotateCenter);
                                }}
                            mConversationArrayAdapter.add("Done: washer1busy_OK");
                        }

                        if (readMessage.contains("F")) {
                            if (countwash >= 2) { door2_view(); }
                            washer1timer = 0;
                            wash1crash = 0;
                            imageDoor1.setVisibility(View.INVISIBLE);
                            imageDoor1.clearAnimation();
                            wash1_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer1free_OK");
                        }

                        // вторая машина - занята
                        if (readMessage.contains("G")) {
                            mConversationArrayAdapter.add("Done:  washer2busy_OK");
                            if (countwash >= 3) { door3_view(); }
                            if (checkdoor == 1) {
                                wash2crash = 1;
                                if (wash2.getVisibility() == View.VISIBLE) {
                                    imageDoor2.setVisibility(View.VISIBLE);
                                    imageDoor2.startAnimation(animationRotateCenter);
                                }}}
                        // вторая машина - свободна
                        if (readMessage.contains("I")) {
                            if (countwash >= 3) { door3_view(); }
                            washer2timer = 0;
                            wash2crash = 0;
                            imageDoor2.setVisibility(View.INVISIBLE);
                            imageDoor2.clearAnimation();
                            wash2_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer2free_OK");
                        }


                        // 3 машина - занята
                        if (readMessage.contains("j")) {
                            mConversationArrayAdapter.add("Done:  washer3busy_OK");
                            if (countwash >= 4) { door4_view(); }
                            if (checkdoor == 1) {
                                wash3crash = 1;
                                if (wash3.getVisibility() == View.VISIBLE) {
                                    imageDoor3.setVisibility(View.VISIBLE);
                                    imageDoor3.startAnimation(animationRotateCenter);
                                }}}
                        // 3 машина - свободна
                        if (readMessage.contains("L")) {
                            if (countwash >= 4) { door4_view(); }
                            washer3timer = 0;
                            wash3crash = 0;
                            imageDoor3.setVisibility(View.INVISIBLE);
                            imageDoor3.clearAnimation();
                            wash3_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer3free_OK");
                        }
                        // 4 машина - занята
                        if (readMessage.contains("J")) {
                            mConversationArrayAdapter.add("Done:  washer4busy_OK");
                            if (countwash >= 5) { door5_view(); }
                            if (checkdoor == 1) {
                                wash4crash = 1;
                                if (wash4.getVisibility() == View.VISIBLE) {
                                    imageDoor4.setVisibility(View.VISIBLE);
                                    imageDoor4.startAnimation(animationRotateCenter);
                                }}}
                        // 4 машина - свободна
                        if (readMessage.contains("N")) {
                            if (countwash >= 5) { door5_view(); }
                            washer4timer = 0;
                            wash4crash = 0;
                            imageDoor4.setVisibility(View.INVISIBLE);
                            imageDoor4.clearAnimation();
                            wash4_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer4free_OK");
                        }
                        // 5 машина - занята
                        if (readMessage.contains("o")) {
                            mConversationArrayAdapter.add("Done:  washer5busy_OK");
                            if (countwash >= 6) { door6_view(); }
                            if (checkdoor == 1) {
                                wash5crash = 1;
                                if (wash5.getVisibility() == View.VISIBLE) {
                                    imageDoor5.setVisibility(View.VISIBLE);
                                    imageDoor5.startAnimation(animationRotateCenter);
                                }}}
                        // 5 машина - свободна
                        if (readMessage.contains("Q")) {
                            if (countwash >= 6) { door6_view(); }
                            washer5timer = 0;
                            wash5crash = 0;
                            imageDoor5.setVisibility(View.INVISIBLE);
                            imageDoor5.clearAnimation();
                            wash5_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer5free_OK");
                        }
                        // 6 машина - занята
                        if (readMessage.contains("O")) {
                            mConversationArrayAdapter.add("Done:  washer6busy_OK");
                            if (countwash >= 7) { door7_view(); }
                            if (checkdoor == 1) {
                                wash6crash = 1;
                                if (wash6.getVisibility() == View.VISIBLE) {
                                    imageDoor6.setVisibility(View.VISIBLE);
                                    imageDoor6.startAnimation(animationRotateCenter);
                                }}}
                        // 6 машина - свободна
                        if (readMessage.contains("S")) {
                            if (countwash >= 7) { door7_view(); }
                            washer6timer = 0;
                            wash6crash = 0;
                            imageDoor6.setVisibility(View.INVISIBLE);
                            imageDoor6.clearAnimation();
                            wash6_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer6free_OK");
                        }
                        // 7 машина - занята
                        if (readMessage.contains("t")) {
                            mConversationArrayAdapter.add("Done:  washer7busy_OK");
                            if (countwash >= 8) { door8_view(); }
                            if (checkdoor == 1) {
                                wash7crash = 1;
                                if (wash7.getVisibility() == View.VISIBLE) {
                                    imageDoor7.setVisibility(View.VISIBLE);
                                    imageDoor7.startAnimation(animationRotateCenter);
                                }}}
                        // 7 машина - свободна
                        if (readMessage.contains("V")) {
                            if (countwash >= 8) { door8_view(); }
                            washer7timer = 0;
                            wash7crash = 0;
                            imageDoor7.setVisibility(View.INVISIBLE);
                            imageDoor7.clearAnimation();
                            wash7_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer7free_OK");
                        }
                        // 8 машина - занята
                        if (readMessage.contains("T")) {
                            mConversationArrayAdapter.add("Done:  washer8busy_OK");
                            if (countwash >= 9) { door9_view(); }
                            if (checkdoor == 1) {
                                wash8crash = 1;
                                if (wash8.getVisibility() == View.VISIBLE) {
                                    imageDoor8.setVisibility(View.VISIBLE);
                                    imageDoor8.startAnimation(animationRotateCenter);
                                }}}
                        // 8 машина - свободна
                        if (readMessage.contains("X")) {
                            if (countwash >= 9) { door9_view(); }
                            washer8timer = 0;
                            wash8crash = 0;
                            imageDoor8.setVisibility(View.INVISIBLE);
                            imageDoor8.clearAnimation();
                            wash8_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer8free_OK");
                        }
                        // 9 машина - занята
                        if (readMessage.contains("y")) {
                            mConversationArrayAdapter.add("Done:  washer9busy_OK");
                            if (countwash >= 10) { door10_view(); }
                            if (checkdoor == 1) {
                                wash9crash = 1;
                                if (wash9.getVisibility() == View.VISIBLE) {
                                    imageDoor9.setVisibility(View.VISIBLE);
                                    imageDoor9.startAnimation(animationRotateCenter);
                                }}}
                        // 9 машина - свободна
                        if (readMessage.contains("2")) {
                            if (countwash >= 10) { door10_view(); }
                            washer9timer = 0;
                            wash9crash = 0;
                            imageDoor9.setVisibility(View.INVISIBLE);
                            imageDoor9.clearAnimation();
                            wash9_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer9free_OK");
                        }
                        // 10 машина - занята
                        if (readMessage.contains("Y")) {
                            mConversationArrayAdapter.add("Done:  washer10busy_OK");
                            if (countwash >= 11) { door11_view(); }
                            if (checkdoor == 1) {
                                wash10crash = 1;
                                if (wash10.getVisibility() == View.VISIBLE) {
                                    imageDoor10.setVisibility(View.VISIBLE);
                                    imageDoor10.startAnimation(animationRotateCenter);
                                }}}
                        // 10 машина - свободна
                        if (readMessage.contains("6")) {
                            if (countwash >= 11) { door11_view(); }
                            washer10timer = 0;
                            wash10crash = 0;
                            imageDoor10.setVisibility(View.INVISIBLE);
                            imageDoor10.clearAnimation();
                            wash10_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer10free_OK");
                        }
                        // 11 машина - занята
                        if (readMessage.contains("7")) {
                            mConversationArrayAdapter.add("Done:  washer11busy_OK");
                            if (countwash >= 12) { door12_view(); }
                            if (checkdoor == 1) {
                                wash11crash = 1;
                                if (wash11.getVisibility() == View.VISIBLE) {
                                    imageDoor11.setVisibility(View.VISIBLE);
                                    imageDoor11.startAnimation(animationRotateCenter);
                                }}}
                        // 11 машина - свободна
                        if (readMessage.contains("#")) {
                            if (countwash >= 12) { door12_view(); }
                            washer11timer = 0;
                            wash11crash = 0;
                            imageDoor11.setVisibility(View.INVISIBLE);
                            imageDoor11.clearAnimation();
                            wash11_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer11free_OK");
                        }
                        // 12 машина - занята
                        if (readMessage.contains("!")) {
                            mConversationArrayAdapter.add("Done:  washer12busy_OK");
                            if (checkdoor == 1) {
                                wash12crash = 1;
                                if (wash12.getVisibility() == View.VISIBLE) {
                                    imageDoor12.setVisibility(View.VISIBLE);
                                    imageDoor12.startAnimation(animationRotateCenter);
                                }}}
                        // 12 машина - свободна
                        if (readMessage.contains("*")) {
                            washer12timer = 0;
                            wash12crash = 0;
                            imageDoor12.setVisibility(View.INVISIBLE);
                            imageDoor12.clearAnimation();
                            wash12_go.setEnabled(true);
                            mConversationArrayAdapter.add("Done:  washer12free_OK");
                        }


                        // время с начала стирки, обновляется каждую мин
                        // -----------------  машина 1 ------------------
                        if (readMessage.contains("f")) {
                            mConversationArrayAdapter.add("Done:  washer1timer_OK");
                            washer1timer++;
                            if (checkdoor == 1) {
                                wash1crash = 1;
                                if (wash1.getVisibility() == View.VISIBLE) {
                                    imageDoor1.setVisibility(View.VISIBLE);
                                    imageDoor1.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 2 ------------------
                        if (readMessage.contains("i")) {
                            mConversationArrayAdapter.add("Done:  washer2timer_OK");
                            washer2timer++;
                            if (checkdoor == 1) {
                                wash2crash = 1;
                                if (wash2.getVisibility() == View.VISIBLE) {
                                    imageDoor2.setVisibility(View.VISIBLE);
                                    imageDoor2.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 3 ------------------
                        if (readMessage.contains("l")) {
                            mConversationArrayAdapter.add("Done:  washer3timer_OK");
                            washer3timer++;
                            if (checkdoor == 1) {
                                wash3crash = 1;
                                if (wash3.getVisibility() == View.VISIBLE) {
                                    imageDoor3.setVisibility(View.VISIBLE);
                                    imageDoor3.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 4 ------------------
                        if (readMessage.contains("n")) {
                            mConversationArrayAdapter.add("Done:  washer4timer_OK");
                            washer4timer++;
                            if (checkdoor == 1) {
                                wash4crash = 1;
                                if (wash4.getVisibility() == View.VISIBLE) {
                                    imageDoor4.setVisibility(View.VISIBLE);
                                    imageDoor4.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 5 ------------------
                        if (readMessage.contains("q")) {
                            mConversationArrayAdapter.add("Done:  washer5timer_OK");
                            washer5timer++;
                            if (checkdoor == 1) {
                                wash5crash = 1;
                                if (wash5.getVisibility() == View.VISIBLE) {
                                    imageDoor5.setVisibility(View.VISIBLE);
                                    imageDoor5.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 6 ------------------
                        if (readMessage.contains("s")) {
                            mConversationArrayAdapter.add("Done: washer6timer_OK");
                            washer6timer++;
                            if (checkdoor == 1) {
                                wash6crash = 1;
                                if (wash6.getVisibility() == View.VISIBLE) {
                                    imageDoor6.setVisibility(View.VISIBLE);
                                    imageDoor6.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 7 ------------------
                        if (readMessage.contains("v")) {
                            mConversationArrayAdapter.add("Done: washer7timer_OK");
                            washer7timer++;
                            if (checkdoor == 1) {
                                wash7crash = 1;
                                if (wash7.getVisibility() == View.VISIBLE) {
                                    imageDoor7.setVisibility(View.VISIBLE);
                                    imageDoor7.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 8 ------------------
                        if (readMessage.contains("x")) {
                            mConversationArrayAdapter.add("Done: washer8timer_OK");
                            washer8timer++;
                            if (checkdoor == 1) {
                                wash8crash = 1;
                                if (wash8.getVisibility() == View.VISIBLE) {
                                    imageDoor8.setVisibility(View.VISIBLE);
                                    imageDoor8.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 9 ------------------
                        if (readMessage.contains("1")) {
                            mConversationArrayAdapter.add("Done: washer9timer_OK");
                            washer9timer++;
                            if (checkdoor == 1) {
                                wash9crash = 1;
                                if (wash9.getVisibility() == View.VISIBLE) {
                                    imageDoor9.setVisibility(View.VISIBLE);
                                    imageDoor9.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 10 ------------------
                        if (readMessage.contains("5")) {
                            mConversationArrayAdapter.add("Done: washer10timer_OK");
                            washer10timer++;
                            if (checkdoor == 1) {
                                wash10crash = 1;
                                if (wash10.getVisibility() == View.VISIBLE) {
                                    imageDoor10.setVisibility(View.VISIBLE);
                                    imageDoor10.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 11 ------------------
                        if (readMessage.contains("$")) {
                            mConversationArrayAdapter.add("Done: washer11timer_OK");
                            washer11timer++;
                            if (checkdoor == 1) {
                                wash11crash = 1;
                                if (wash11.getVisibility() == View.VISIBLE) {
                                    imageDoor11.setVisibility(View.VISIBLE);
                                    imageDoor11.startAnimation(animationRotateCenter);
                                }}}
                        // -----------------  машина 12 ------------------
                        if (readMessage.contains("&")) {
                            mConversationArrayAdapter.add("Done: washer12timer_OK");
                            washer12timer++;
                            if (checkdoor == 1) {
                                wash12crash = 1;
                                if (wash12.getVisibility() == View.VISIBLE) {
                                    imageDoor12.setVisibility(View.VISIBLE);
                                    imageDoor12.startAnimation(animationRotateCenter);
                                }}}




                        // Получили ответ разрешающий старт анимации
                        // -----------------  шаг 1 ------------------
                        if (readMessage.contains("e")) {
                            animation_start();
                            mConversationArrayAdapter.add("Status: CHECK_WASHER_CONNECT_OK");
                        }
                        // -----------------  шаг 2 ------------------
                        if (readMessage.contains("E")) {
                            animation_start();
                            ok1();
                            mConversationArrayAdapter.add("Status: CHECK_BUTTONS_RELAY");
                        }
                        // -----------------  шаг 3 ------------------
                        if (readMessage.contains("h")) {
                            status1();
                            mConversationArrayAdapter.add("Status: BUTTONS_OK");
                        }
                        // -----------------  шаг 4 ------------------
                        if (readMessage.contains("H")) {
                            mConversationArrayAdapter.add("Status: CHECK_CLOSE_DOOR");
                            ok2();
                        }
                        // -----------------  шаг 5 ------------------
                        if (readMessage.contains("k")) {
                            mConversationArrayAdapter.add("Status: CLOSE_DOOR_OK");
                            status2();
                        }
                        // -----------------  шаг 6 ------------------
                        if (readMessage.contains("K")) {
                            mConversationArrayAdapter.add("Status: CHECK_WASHER_WATER_OK");
                            ok3();
                        }
                        // -----------------  шаг 7 ------------------
                        if (readMessage.contains("m")) {
                            mConversationArrayAdapter.add("Status: CHECK_WASHER_START_OK");
                            status3();
                            animation_start_clother();
                        }
                        // -----------------  шаг 8 ------------------
                        if (readMessage.contains("M")) {
                            mConversationArrayAdapter.add("Status: CHECK_ENGINE");
                            animation_start_clother();
                        }
                        // -----------------  шаг 9 ------------------
                        if (readMessage.contains("p")) {
                            mConversationArrayAdapter.add("Status: ENGINE_OK");
                            go_home();
                        }


                    } // конец входящих сообщений -----------------------------

                    break;
                case ru.laundromat.washer.bluetoothchat.Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    //   if (null != activity) {
                    //        Toast.makeText(activity, "Connected to "
                    //                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    //    }
                    break;
                case ru.laundromat.washer.bluetoothchat.Constants.MESSAGE_TOAST:

                    if (null != activity) {
                        if (msg.getData().getString(ru.laundromat.washer.bluetoothchat.Constants.TOAST) == "Device connection was lost") {
                            //   Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                            //           Toast.LENGTH_SHORT).show();

                            // запускаем бар
                            layout_action_bar.setVisibility(View.VISIBLE);
                            // параметры бара
                            img_action_bar_icon_wash.setVisibility(View.GONE);
                            wash_number.setVisibility(View.GONE);
                            img_action_bar_icon_vneseno.setVisibility(View.VISIBLE);
                            money_kup.setVisibility(View.VISIBLE);
                            img_action_bar_icon_not_connected.setVisibility(View.GONE);
                            img_action_bar_icon_lost_connected.setVisibility(View.VISIBLE);
                            img_action_bar_icon_laundry_connected.setVisibility(View.GONE);

                            reconnect_Device();
                        } else {

                            //   Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                            //           Toast.LENGTH_SHORT).show();
                        }
                    }

                    break;
            }
        }
    };
    // end of Handler --------------------------------------------------------------------------



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    //    Toast.makeText(getActivity(), R.string.bt_not_enabled_leaving,
                    //            Toast.LENGTH_SHORT).show();
                    //    getActivity().finish();

                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                        setupChat();}
                }
        }
    }





    /**
     * Establish reconnection with save divice
     *
     */

    private void reconnect_Device() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            setupChat();} else {
            // Get the device MAC address
            if (mac_address.length() > 0) {
                // нужно для вывода списка спаренных после 6 попыток
                counter_bluetooth_state++;
                // Get the BluetoothDevice object
                BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(mac_address);
                // Attempt to connect to the device
                mChatService.connect(device, true);
            } else {
                Toast.makeText(getActivity(), "Мак-адрес задан некорректно!",
                        Toast.LENGTH_SHORT).show();
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
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

        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // получаем в нашу переменную мак адрес bluetooth
        mac_address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        //  сохраняем данные мак адрреса
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_MACADDRESS, mac_address);
        editor.apply();
        if (mac_address.length() > 0) {
            // Get the BluetoothDevice object
            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
            // Attempt to connect to the device
            mChatService.connect(device, secure);
        } else {
            Toast.makeText(getActivity(), "Мак-адрес задан некорректно!",
                    Toast.LENGTH_SHORT).show();
            // Launch the DeviceListActivity to see devices and do scan
            Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }


    /*
    *    Скрыть мою клавиатуру
     */
    private void hideKeyboardOff() {
//        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(setMacAddress.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showKeepScreenOn() {
        // держать экран включенным ----------------------------------------
        //    getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
    private void showKeepScreenOff() {
        //   onResume();
    }

/*
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
            Command command = new Command(0, commandStr);
            RootTools.getShell(true).add(command);

        } catch (Exception e) {
            // something went wrong, deal with it here
        }
    }
*/

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
