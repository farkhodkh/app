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

import android.text.TextUtils;

import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.HostsProvider;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
final class ApiClientWrapper extends DefaultApiClient {

    public final static String PRODUCTION_HOST = "https://money.yandex.ru";

    private final HostsProvider hostsProvider;
    private final boolean sandbox;

    ApiClientWrapper(final String clientId, final String url) {
        super(clientId, true);
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url is null or empty");
        }

        sandbox = !url.equals(PRODUCTION_HOST);
        hostsProvider = new HostsProvider(true) {
            @Override
            public String getMoney() {
                return url;
            }
        };
    }

    @Override
    public HostsProvider getHostsProvider() {
        return hostsProvider;
    }

    public boolean isSandbox() {
        return sandbox;
    }
}
