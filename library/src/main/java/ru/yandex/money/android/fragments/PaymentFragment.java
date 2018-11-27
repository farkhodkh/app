/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ru.yandex.money.android.fragments;

import android.app.Fragment;

import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.ExternalCard;

import ru.yandex.money.android.PaymentActivity;

/**
 * @author vyasevich
 */
public abstract class PaymentFragment extends Fragment {

    protected static final String KEY_MONEY_SOURCE = "moneySource";

    protected PaymentActivity getPaymentActivity() {
        return (PaymentActivity) getActivity();
    }

    protected void proceed() {
        startActionSafely(new Action() {
            @Override
            public void start(PaymentActivity activity) {
                activity.proceed();
            }
        });
    }

    protected void repeat() {
        startActionSafely(new Action() {
            @Override
            public void start(PaymentActivity activity) {
                activity.repeat();
            }
        });
    }

    protected void showError(final Error error, final String status) {
        startActionSafely(new Action() {
            @Override
            public void start(PaymentActivity activity) {
                activity.showError(error, status);
            }
        });
    }

    protected void showCsc(final ExternalCard moneySource) {
        startActionSafely(new Action() {
            @Override
            public void start(PaymentActivity activity) {
                activity.showCsc(moneySource);
            }
        });
    }

    protected void showProgressBar() {
        startActionSafely(new Action() {
            @Override
            public void start(PaymentActivity activity) {
                activity.showProgressBar();
            }
        });
    }

    protected void hideProgressBar() {
        startActionSafely(new Action() {
            @Override
            public void start(PaymentActivity activity) {
                activity.hideProgressBar();
            }
        });
    }

    protected void startActionSafely(Action action) {
        PaymentActivity activity = getPaymentActivity();
        if (activity != null) {
            action.start(activity);
        }
    }

    public interface Action {
        void start(PaymentActivity activity);
    }
}