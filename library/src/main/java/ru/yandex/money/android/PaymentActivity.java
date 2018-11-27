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

package ru.yandex.money.android;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.Window;

import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.params.PaymentParams;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.processes.ExternalPaymentProcess;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import ru.yandex.money.android.database.DatabaseStorage;
import ru.yandex.money.android.fragments.CardsFragment;
import ru.yandex.money.android.fragments.CscFragment;
import ru.yandex.money.android.fragments.ErrorFragment;
import ru.yandex.money.android.fragments.SuccessFragment;
import ru.yandex.money.android.fragments.WebFragment;
import ru.yandex.money.android.parcelables.ExternalCardParcelable;
import ru.yandex.money.android.parcelables.ExternalPaymentProcessSavedStateParcelable;
import ru.yandex.money.android.utils.Keyboards;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author vyasevich
 */
public final class PaymentActivity extends Activity {

    public static final String EXTRA_INVOICE_ID = "ru.yandex.money.android.extra.INVOICE_ID";

    private static final String EXTRA_ARGUMENTS = "ru.yandex.money.android.extra.ARGUMENTS";
    private static final String EXTRA_HOST = "ru.yandex.money.android.extra.HOST";
    private static final String EXTRA_CLIENT_ID = "ru.yandex.money.android.extra.CLIENT_ID";

    private static final String KEY_PROCESS_SAVED_STATE = "processSavedState";
    private static final String KEY_SELECTED_CARD = "selectedCard";

    private ExternalPaymentProcess process;
    private ExternalPaymentProcess.ParameterProvider parameterProvider;
    private PaymentArguments arguments;
    private List<ExternalCard> cards;
    private ExternalCard selectedCard;
    private boolean immediateProceed = true;
    private Subscription subscription;

    /**
     * Returns intent builder used for launch this activity
     *
     * @param context application context or {@code null}
     */
    public static PaymentParamsBuilder getBuilder(Context context) {
        return new IntentBuilder(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.ym_payment_activity);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // we hide progress bar because on some devices we have it shown right from the start
        hideProgressBar();

        arguments = new PaymentArguments(getIntent().getBundleExtra(EXTRA_ARGUMENTS));
        cards = new DatabaseStorage(this).selectMoneySources();

        boolean ready = initPaymentProcess();
        if (!ready) {
            return;
        }

        if (savedInstanceState == null) {
            proceed();
        } else {
            ExternalPaymentProcessSavedStateParcelable savedStateParcelable =
                    savedInstanceState.getParcelable(KEY_PROCESS_SAVED_STATE);
            if (savedStateParcelable != null) {
                process.restoreSavedState(savedStateParcelable.value);
            }

            ExternalCardParcelable externalCardParcelable =
                    savedInstanceState.getParcelable(KEY_SELECTED_CARD);
            if (externalCardParcelable != null) {
                selectedCard = (ExternalCard) externalCardParcelable.value;
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_PROCESS_SAVED_STATE,
                new ExternalPaymentProcessSavedStateParcelable(process.getSavedState()));
        if (selectedCard != null) {
            outState.putParcelable(KEY_SELECTED_CARD, new ExternalCardParcelable(selectedCard));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                hideKeyboard();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
        applyResult();

        Fragment fragment = getCurrentFragment();
        super.onBackPressed();

        Fragment currentFragment = getCurrentFragment();
        if (currentFragment instanceof CscFragment) {
            super.onBackPressed();
            currentFragment = getCurrentFragment();
        }
        if (fragment instanceof WebFragment && currentFragment instanceof CardsFragment) {
            if (cards.size() == 0) {
                immediateProceed = false;
            }
            getFragmentManager()
                    .beginTransaction()
                    .remove(currentFragment)
                    .commit();
            reset();
        }
    }

    public List<ExternalCard> getCards() {
        return cards;
    }

    public void showWeb(String url, Map<String, String> postData) {
        Fragment fragment = getCurrentFragment();
        boolean clearBackStack = !(fragment instanceof CardsFragment ||
                fragment instanceof CscFragment);
        replaceFragment(WebFragment.newInstance(url, postData), clearBackStack);
    }

    public void showCards() {
        RequestExternalPayment rep = (RequestExternalPayment) process.getRequestPayment();
        replaceFragment(CardsFragment.newInstance(rep.title, rep.contractAmount), true);
    }

    public void showError(Error error, String status) {
        replaceFragment(ErrorFragment.newInstance(error, status), true);
    }

    public void showUnknownError() {
        replaceFragment(ErrorFragment.newInstance(), true);
    }

    public void showSuccess(ExternalCard moneySource) {
        replaceFragment(SuccessFragment.newInstance(process.getRequestPayment().contractAmount,
                moneySource), true);
    }

    public void showCsc(ExternalCard externalCard) {
        selectedCard = externalCard;
        replaceFragment(CscFragment.newInstance(externalCard), false);
    }

    public void showProgressBar() {
        setProgressBarIndeterminateVisibility(true);
    }

    public void hideProgressBar() {
        setProgressBarIndeterminateVisibility(false);
    }

    public void proceed() {
        subscription = performPaymentOperation(process::proceed);
    }

    public void repeat() {
        subscription = performPaymentOperation(process::repeat);
    }

    public void reset() {
        selectedCard = null;
        process.reset();
        proceed();
    }

    public void cancel() {
        selectedCard = null;
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private Subscription performPaymentOperation(@NonNull Callable<Boolean> operation) {
        return performOperation(operation, aBoolean -> handleProcess());
    }

    private <T> Subscription performOperation(@NonNull final Callable<T> operation,
                                      @NonNull final Action1<T> onResponse) {

        showProgressBar();
        return Observable.fromCallable(operation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    onResponse.call(o);
                    hideProgressBar();
                }, throwable -> {
                    onOperationFailed();
                    hideProgressBar();
                });
    }

    private void handleProcess() {
        BaseProcessPayment processPayment = process.getProcessPayment();
        if (processPayment != null) {
            onExternalPaymentProcessed((ProcessExternalPayment) processPayment);
            return;
        }

        BaseRequestPayment requestPayment = process.getRequestPayment();
        if (requestPayment != null) {
            onExternalPaymentReceived((RequestExternalPayment) requestPayment);
        }
    }

    private boolean initPaymentProcess() {
        final Intent intent = getIntent();
        final String clientId = intent.getStringExtra(EXTRA_CLIENT_ID);
        ApiClientWrapper apiClient = new ApiClientWrapper(clientId,
                intent.getStringExtra(EXTRA_HOST));
        final OAuth2Session session = new OAuth2Session(apiClient);
        session.setDebugLogging(apiClient.isSandbox());

        parameterProvider = new ExternalPaymentProcess.ParameterProvider() {
            @Override
            public String getPatternId() {
                return arguments.getPatternId();
            }

            @Override
            public Map<String, String> getPaymentParameters() {
                return arguments.getParams();
            }

            @Override
            public MoneySource getMoneySource() {
                return selectedCard;
            }

            @Override
            public String getCsc() {
                Fragment fragment = getCurrentFragment();
                return fragment instanceof CscFragment ?
                        ((CscFragment) fragment).getCsc() : null;
            }

            @Override
            public String getExtAuthSuccessUri() {
                return PaymentArguments.EXT_AUTH_SUCCESS_URI;
            }

            @Override
            public String getExtAuthFailUri() {
                return PaymentArguments.EXT_AUTH_FAIL_URI;
            }

            @Override
            public boolean isRequestToken() {
                Fragment fragment = getCurrentFragment();
                return fragment instanceof SuccessFragment;
            }
        };

        process = new ExternalPaymentProcess(session, parameterProvider);

        final Prefs prefs = new Prefs(this);
        String instanceId = prefs.restoreInstanceId();
        if (TextUtils.isEmpty(instanceId)) {
            performOperation(() -> session.execute(new InstanceId.Request(clientId)), response -> {
                if (response.isSuccess()) {
                    prefs.storeInstanceId(response.instanceId);
                    process.setInstanceId(response.instanceId);
                    proceed();
                } else {
                    showError(response.error, response.status.code);
                }
            });
            return false;
        }

        process.setInstanceId(instanceId);
        return true;
    }

    private void onExternalPaymentReceived(RequestExternalPayment rep) {
        if (rep.status == BaseRequestPayment.Status.SUCCESS) {
            if (immediateProceed && cards.size() == 0) {
                proceed();
            } else {
                showCards();
            }
        } else {
            showError(rep.error, rep.status.code);
        }
    }

    private void onExternalPaymentProcessed(ProcessExternalPayment pep) {
        switch (pep.status) {
            case SUCCESS:
                Fragment fragment = getCurrentFragment();
                if (!(fragment instanceof SuccessFragment)) {
                    showSuccess((ExternalCard) parameterProvider.getMoneySource());
                } else if (pep.externalCard != null) {
                    ((SuccessFragment) fragment).saveCard(pep.externalCard);
                }
                break;
            case EXT_AUTH_REQUIRED:
                showWeb(pep.acsUri, pep.acsParams);
                break;
            default:
                showError(pep.error, pep.status.code);
        }
    }

    private void onOperationFailed() {
        showUnknownError();
        hideProgressBar();
    }

    private void replaceFragment(Fragment fragment, boolean clearBackStack) {
        if (fragment == null) {
            return;
        }

        Fragment currentFragment = getCurrentFragment();
        FragmentManager manager = getFragmentManager();
        if (clearBackStack) {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        @SuppressLint("CommitTransaction")
        FragmentTransaction transaction = manager.beginTransaction()
                .replace(R.id.ym_container, fragment);
        if (!clearBackStack && currentFragment != null) {
            transaction.addToBackStack(fragment.getTag());
        }
        transaction.commit();
        hideKeyboard();
    }

    private Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.ym_container);
    }

    private void hideKeyboard() {
        Keyboards.hideKeyboard(this);
    }

    private void applyResult() {
        BaseProcessPayment pp = process.getProcessPayment();
        if (pp != null && pp.status == BaseProcessPayment.Status.SUCCESS) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_INVOICE_ID, pp.invoiceId);
            setResult(RESULT_OK, intent);
        } else {
            setResult(RESULT_CANCELED);
        }
    }

    public interface PaymentParamsBuilder {
        AppClientIdBuilder setPaymentParams(String patternId, Map<String, String> paymentParams);
        AppClientIdBuilder setPaymentParams(PaymentParams paymentParams);
    }

    public interface AppClientIdBuilder {
        Builder setClientId(String clientId);
    }

    public interface Builder {
        Builder setHost(String host);
        Intent build();
    }

    private final static class IntentBuilder
            implements PaymentParamsBuilder, AppClientIdBuilder, Builder {

        @NonNull
        private final Context context;

        private String patternId;
        private Map<String, String> paymentParams;

        private String host;
        private String clientId;

        public IntentBuilder(@NonNull Context context) {
            this.context = context;
            this.host = ApiClientWrapper.PRODUCTION_HOST;
        }

        public AppClientIdBuilder setPaymentParams(String patternId,
                                                   Map<String, String> paymentParams) {
            this.patternId = patternId;
            this.paymentParams = paymentParams;
            return this;
        }

        public AppClientIdBuilder setPaymentParams(PaymentParams paymentParams) {
            this.patternId = paymentParams.getPatternId();
            this.paymentParams = paymentParams.makeParams();
            return this;
        }

        public IntentBuilder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Intent build() {
            return createIntent()
                    .putExtra(EXTRA_ARGUMENTS, new PaymentArguments(patternId, paymentParams).
                            toBundle())
                    .putExtra(EXTRA_HOST, host)
                    .putExtra(EXTRA_CLIENT_ID, clientId);
        }

        private Intent createIntent() {
            return new Intent(context, PaymentActivity.class);
        }
    }
}
