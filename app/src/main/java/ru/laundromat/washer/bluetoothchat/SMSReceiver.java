package ru.laundromat.washer.bluetoothchat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] messages = null;
        String str = "";
        String nbr = "";
        if (bundle != null) {

            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                nbr += messages[i].getOriginatingAddress();
                // str += " :";
                str += messages[i].getMessageBody().toString();
                // str += "\n";
            }

      //      Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

            if (str.contentEquals("1")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RECEIVED");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            }
/*
            else if (str.contentEquals("2")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("3")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("4")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("5")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("6")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("stop")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("ok")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("balance")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("money")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            }
*/
/*
            }else if (str.contains("Зачислено 10,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            }else if (str.contains("Зачислено 20,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 30,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 40,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 50,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 60,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 70,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 80,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 90,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 100,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 110,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 120,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 130,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 140,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Зачислено 150,00 руб.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);


            } else if (str.contentEquals("2")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("3")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("4")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("5")) { // запустить все машинки
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("6")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("7")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("8")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("9")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("10")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("11")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contentEquals("12")) { // перезагрузка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("money")) { // запрос статистики
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smsmoney", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("smsadminon")) { // вкл
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smsadminonoff", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("smsadminoff")) { // вкл
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smsadminonoff", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("exit")) { // запрос статистики
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smsexit", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("settings")) { // запрос статистики
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smssettings", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("10")) { // внести 10 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms10", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("50")) { // внести 50 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms50", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("100")) { // внести 100 в блок
                // (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("sms100", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont1on")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont1off")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont2on")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont2off")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont3on")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont3off")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont4on")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont4off")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont5on")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont5off")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont6on")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("remont6off")) { //
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswashremont", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("01")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("11")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("21")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("31")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("41")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("51")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("61")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("71")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("81")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("91")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("101")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("111")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("121")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("131")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("141")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("151")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif1", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("02")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("12")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("22")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("32")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("42")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("52")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("62")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("72")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("82")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("92")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("102")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("112")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("122")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("132")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("142")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("152")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif2", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("01")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("13")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("23")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("33")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("43")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("53")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("63")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("73")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("83")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("93")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("103")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("113")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("123")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("133")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("143")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("153")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif3", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("04")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("14")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("24")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("34")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("44")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("54")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("64")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("74")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("84")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("94")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("104")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("114")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("124")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("134")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("144")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("154")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif4", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("05")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("15")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("25")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("35")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("45")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("55")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("65")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("75")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("85")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("95")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("105")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("115")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("125")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("135")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("145")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("155")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif5", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("06")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("16")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("26")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("36")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("46")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("56")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("66")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("76")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("86")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("96")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("106")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("116")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("126")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("136")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("146")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("156")) { // тариф 100 в блок (возврат)
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smstarif6", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("wash2")) { // подключено 2 машины
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smscountwash", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("wash3")) { // подключено 3 машины
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smscountwash", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("wash4")) { // подключено 4 машины
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smscountwash", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("wash5")) { // подключено 5 машин
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smscountwash", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("wash6")) { // подключено 6 машин
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smscountwash", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);


            } else if (str.contains("отправьте код")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash1isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);
            } else if (str.contains("Имя присвоено.")) {
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash1isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);


            } else if (str.contentEquals("dry1on")) { // Машинка 1 в режиме
                // сушка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash1isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry1off")) { // Машинка 1 в режиме
                // стирка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash1isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry2on")) { // Машинка 1 в режиме
                // сушка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash2isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry2off")) { // Машинка 1 в режиме
                // стирка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash2isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry3on")) { // Машинка 1 в режиме
                // сушка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash3isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry3off")) { // Машинка 1 в режиме
                // стирка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash3isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry4on")) { // Машинка 4 в режиме
                // сушка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash4isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry4off")) { // Машинка 4 в режиме
                // стирка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash4isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry5on")) { // Машинка 5 в режиме
                // сушка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash5isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry5off")) { // Машинка 5 в режиме
                // стирка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash5isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry6on")) { // Машинка 6 в режиме
                // сушка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash6isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("dry6off")) { // Машинка 6 в режиме
                // стирка
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smswash6isdry", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            } else if (str.contentEquals("cashingin")) { // Инкассация
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("SMS_RESEIVED_ACTION");
                broadcastIntent.putExtra("smscashingin", str);
                broadcastIntent.putExtra("address", nbr);
                context.sendBroadcast(broadcastIntent);

            }
*/
        }
    }
   }
